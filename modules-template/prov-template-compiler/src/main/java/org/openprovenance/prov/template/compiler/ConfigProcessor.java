package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.expansion.CompilerExpansionBuilder;
import org.openprovenance.prov.template.compiler.expansion.CompilerTypeManagement;
import org.openprovenance.prov.template.compiler.expansion.CompilerTypedRecord;
import org.openprovenance.prov.template.descriptors.DescriptorUtils;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.expansion.StatementTypeAction.gensym;


public class ConfigProcessor {
    public static final String PREFIX_LOG_VAR = "___";
    public static final String GET_NODES_METHOD = "getNodes";
    public static final String BUILDER_INTERFACE = "Builder";
    public static final String INIT = "Init";
    public static final String BUILDERS = "builders";
    public static final String TYPEMANAGERS = "typeManagers";
    public static final String PF = "pf";
    public static final String GET_SUCCESSOR_METHOD = "getSuccessors";
    public static final String GET_TYPED_SUCCESSOR_METHOD = "getTypedSuccessors";
    public static final String GET_NAME = "getName";
    public static final String LOGGER_INTERFACE = "LoggerInterface";
    public static final String PROCESSOR_ARGS_INTERFACE = "ProcessorArgsInterface";
    public static final String TESTER_FILE = "ExampleTest";
    public static final String GET_BUILDERS_METHOD = "getBuilders";
    public static final String CLIENT_PACKAGE = "org.openprovenance.prov.client";

    public static final String PROPERTY_ORDER = "propertyOrder";
    public static final String OUTPUTS = "outputs";
    public static final String COMPULSORY_INPUTS = "compulsoryInputs";
    public static final String INPUTS = "inputs";
    public static final String PROPERTY_ORDER_METHOD  = "getPropertyOrder";
    public static final String OUTPUTS_METHOD  = "getOutputs";
    public static final String COMPULSORY_INPUTS_METHOD = "getCompulsoryInputs";
    public static final String INPUTS_METHOD  = "getInputs";

    public static final String RECORD_CSV_PROCESSOR_METHOD  = "record2csv";
    public static final String ARGS_CSV_CONVERSION_METHOD   = "args2csv";
    public static final String BEAN_SQL_CONVERSION_METHOD   = "bean2sql";
    public static final String PROCESSOR_CONVERTER          = "processorConverter";
    public static final String ARGS2RECORD_CONVERTER        = "aArgs2RecordConverter";
    public static final String A_BEAN_SQL_CONVERTER         = "aBean2SqlConverter";
    public static final String A_ARGS_BEAN_CONVERTER        = "aArgs2BeanConverter";
    public static final String A_ARGS_CSV_CONVERTER         = "aArgs2CsVConverter";
    public static final String A_RECORD_BEAN_CONVERTER      = "aRecord2BeanConverter";
    public static final String A_RECORD_CSV_CONVERTER       = "aRecord2CsvConverter";
    public static final String A_RECORD_SQL_CONVERTER       = "aRecord2SqlConverter";
    public static final String BUILDER = "Builder";
    public static final String SQL_CONFIGURATOR = "SqlConfigurator";
    public static final String SQL_INSERT_CONFIGURATOR = "SqlInsertConfigurator";
    public static final String CSV_CONFIGURATOR = "CsvConfigurator";
    public static final String CONVERTER_CONFIGURATOR = "ConverterConfigurator";
    public static final String ENACTOR_CONFIGURATOR = "EnactorConfigurator";
    public static final String BEAN_COMPLETER = "BeanCompleter";
    public static final String BEAN_ENACTOR = "BeanEnactor";
    public static final String QUERY_INVOKER = "QueryInvoker";
    public static final String PROPERTY_ORDER_CONFIGURATOR = "PropertyOrderConfigurator";
    public static final String PROCESS_METHOD_NAME = "process";
    public static final String GETTER = "Getter";
    public static final String ENACTOR_IMPLEMENTATION = "EnactorImplementation";
    public static final String INSERT_PREFIX = "insert_";
    private final ProvFactory pFactory;
    private final CompilerSQL compilerSQL;
    private final boolean debugComment;
    public static final DescriptorUtils descriptorUtils;

    boolean withMain=true; // TODO need to be updatable via command line

    public static final ObjectMapper objectMapper = new ObjectMapper();

    private final CompilerUtil compilerUtil     = new CompilerUtil();
    private final CompilerLogger compilerLogger = new CompilerLogger();
    private final CompilerTemplateBuilders compilerTemplateBuilders = new CompilerTemplateBuilders();
    private final CompilerTableConfigurator compilerTableConfigurator = new CompilerTableConfigurator();
    private final CompilerBeanProcessor compilerBeanProcessor = new CompilerBeanProcessor();
    private final CompilerBeanCompleter compilerBeanCompleter = new CompilerBeanCompleter();
    private final CompilerBeanEnactor compilerBeanEnactor = new CompilerBeanEnactor();
    private final CompilerQueryInvoker compilerQueryInvoker = new CompilerQueryInvoker();
    private final CompilerConfigurations compilerConfigurations = new CompilerConfigurations();
    private final CompilerMaven compilerMaven   = new CompilerMaven(this);
    private final CompilerScript compilerScript   = new CompilerScript(this);
    private final CompilerDocumentation compilerDocumentation = new CompilerDocumentation();
    private final CompilerClient compilerClient;
    private final CompilerExpansionBuilder compilerExpansionBuilder;
    private final CompilerBuilderInit compilerBuilderInit;
    private final CompilerTypeManagement compilerTypeManagement;
    private final CompilerTypedRecord compilerTypedRecord;
   // private final CompilerTypePropagate compilerTypePropagate;

    private final CompilerSimpleBean compilerSimpleBean;
    private final CompilerProcessor compilerProcessor;
    private final CompilerJsonSchema compilerJsonSchema;
    private final CompilerClientTest compilerClientTest;

    static {
        descriptorUtils = new DescriptorUtils();
        descriptorUtils.setupDeserializer(objectMapper);
    }

    public ConfigProcessor(ProvFactory pFactory) {
        this.debugComment=true;
        this.pFactory=pFactory;
        this.compilerSQL=new CompilerSQL(true, "ID");
        this.compilerClient= new CompilerClient(pFactory,compilerSQL);
        this.compilerTypeManagement= new CompilerTypeManagement(withMain,compilerClient,pFactory,debugComment);

        this.compilerExpansionBuilder= new CompilerExpansionBuilder(withMain,compilerClient,pFactory,debugComment,compilerTypeManagement);
        //this.compilerTypePropagate= new CompilerTypePropagate(withMain,compilerClient,pFactory,debugComment);
        this.compilerTypedRecord = new CompilerTypedRecord(withMain,compilerClient,pFactory,debugComment);
        this.compilerBuilderInit= new CompilerBuilderInit(pFactory);
        this.compilerSimpleBean =new CompilerSimpleBean(pFactory);
        this.compilerProcessor =new CompilerProcessor(pFactory);
        this.compilerJsonSchema=new CompilerJsonSchema();
        this.compilerClientTest =new CompilerClientTest();


    }

    public String readCompilerVersion() {
        return Configuration.getPropertiesFromClasspath(getClass(),"compiler.properties").getProperty("compiler.version");
    }

    final String compilerVersion=readCompilerVersion();

    public int processTemplateGenerationConfig(String template_builder, ProvFactory pFactory) {
        TemplatesCompilerConfig configs;


        try {
            configs = objectMapper.readValue(new File(template_builder), TemplatesCompilerConfig.class);
            final String root_dir = configs.destination + "/" + configs.name;
            new File(root_dir).mkdirs(); 
            final String cli_lib = configs.name + "_cli";
            final String cli_dir = root_dir + "/" + cli_lib;
            new File(cli_dir).mkdirs(); 
            final String l2p_lib = configs.name + "_l2p";
            final String l2p_dir = root_dir + "/" + l2p_lib;
            new File(l2p_dir).mkdirs();

            final String l2p_src_dir=l2p_dir+"/src/main/java";
            final String l2p_test_src_dir=l2p_dir+"/src/test/java";
            final String cli_src_dir=cli_dir+"/src/main/java";
            final String cli_test_src_dir=cli_dir+"/src/test/java";
            final String cli_webjar_dir=cli_dir+"/src/main/resources/META-INF/resources/webjars/" + configs.name + "_cli/" + configs.version;
            final String cli_webjar_bindings_dir=cli_webjar_dir + "/bindings";
            final String cli_webjar_templates_dir=cli_webjar_dir + "/templates";
            new File(l2p_src_dir).mkdirs();
            new File(cli_src_dir).mkdirs();
            new File(l2p_test_src_dir).mkdirs();
            new File(cli_test_src_dir).mkdirs();
            new File(cli_webjar_dir).mkdirs();
            new File(cli_webjar_bindings_dir).mkdirs();
            new File(cli_webjar_templates_dir).mkdirs();


            for (TemplateCompilerConfig config: configs.templates) {
                System.out.println(config.toString());
                doGenerateServerForEntry(config,configs,cli_src_dir,l2p_src_dir,pFactory, cli_webjar_dir);
                FileUtils.copyFileToDirectory(new File(config.template), new File(cli_webjar_templates_dir));
                FileUtils.copyFileToDirectory(new File(config.bindings), new File(cli_webjar_bindings_dir));
            }

            generateJSonSchemaEnd(configs, cli_src_dir);

            generateSQLEnd(configs, cli_src_dir);
            generateDocumentationEnd(configs, cli_src_dir);

            doGenerateProject(configs, root_dir, cli_lib, l2p_lib, l2p_dir, l2p_src_dir, l2p_test_src_dir, cli_test_src_dir, cli_webjar_dir);

            doGenerateClientAndProject(configs, cli_lib, cli_dir, cli_src_dir);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void generateJSonSchemaEnd(TemplatesCompilerConfig configs, String cli_src_dir) {
        if (configs.jsonschema!=null) compilerJsonSchema.generateJSonSchemaEnd(configs.jsonschema, cli_src_dir +"/../resources");
    }

    public void generateSQLEnd(TemplatesCompilerConfig configs, String cli_src_dir) {
        if (configs.sqlFile!=null) compilerSQL.generateSQLEnd(configs.sqlFile, cli_src_dir +"/../resources");
    }


    public void generateDocumentationEnd(TemplatesCompilerConfig configs, String cli_webjar_dir) {
        if (configs.documentation!=null) compilerDocumentation.generateDocumentationEnd(configs,cli_webjar_dir);


    }

    public void doGenerateProject(TemplatesCompilerConfig configs, String root_dir, String cli_lib, String l2p_lib, String l2p_dir, String l2p_src_dir, String l2p_test_src_dir, String cli_test_src_dir, String cli_webjar_dir) {
        final String init_dir= l2p_src_dir + "/" + configs.init_package.replace('.', '/') + "/";
        JavaFile init=compilerBuilderInit.generateInitializer(configs);
        compilerUtil.saveToFile(init_dir, init_dir +INIT + ".java", init);

        final String l2p_test_dir= l2p_test_src_dir + "/" + configs.init_package.replace('.', '/') + "/";
        JavaFile testfile=compilerMaven.generateTestFile_l2p(configs);
        compilerUtil.saveToFile(l2p_test_dir, l2p_test_dir +TESTER_FILE+ ".java", testfile);



        compilerMaven.makeRootPom(configs, root_dir, cli_lib, l2p_lib);
        compilerMaven.makeSubPom(configs, l2p_dir, l2p_lib, true, false, false, false);

        final String cli_test_dir= cli_test_src_dir + "/" + configs.init_package.replace('.', '/') + "/";
        JavaFile testfile2= compilerClientTest.generateTestFile_cli(configs);
        compilerUtil.saveToFile(cli_test_dir, cli_test_dir +TESTER_FILE+ ".java", testfile2);

    }

    public void doGenerateClientAndProject(TemplatesCompilerConfig configs, String cli_lib, String cli_dir, String cli_src_dir) {
        final String configurator_dir= cli_src_dir + "/" + configs.configurator_package.replace('.', '/') + "/";
        final String logger_dir= cli_src_dir + "/" + configs.logger_package.replace('.', '/') + "/";
        final String openprovenance_dir= cli_src_dir + "/" + CLIENT_PACKAGE.replace('.', '/') + "/";

        compilerMaven.makeSubPom(configs, cli_dir, cli_lib, false, configs.jsweet, true, compilerClient.getFoundEscape());


        JavaFile logger=compilerLogger.generateLogger(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + configs.logger+ ".java", logger);

        JavaFile intface=compilerLogger.generateBuilderInterface(configs);
        compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir +BUILDER_INTERFACE+ ".java", intface);

        JavaFile intface2=compilerLogger.generateLoggerInterface(configs);
        compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir +LOGGER_INTERFACE+ ".java", intface2);

        JavaFile intface3=compilerLogger.generateProcessorArgsInterface(configs);
        compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir + PROCESSOR_ARGS_INTERFACE+ ".java", intface3);

        exportMiscFiles(configs, cli_dir, cli_lib);

        compilerScript.generateScript(configs);

        JavaFile templateBuilders=compilerTemplateBuilders.generateTemplateBuilders(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + configs.templateBuilders+ ".java", templateBuilders);

        JavaFile tableConfigurator=compilerTableConfigurator.generateTableConfigurator(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + configs.tableConfigurator+ ".java", tableConfigurator);

        JavaFile beanProcessor=compilerBeanProcessor.generateBeanProcessor(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + configs.beanProcessor+ ".java", beanProcessor);

        JavaFile beanCompleter=compilerBeanCompleter.generateBeanCompleter(configs);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + BEAN_COMPLETER + ".java", beanCompleter);

        JavaFile beanEnactor=compilerBeanEnactor.generateBeanEnactor(configs);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + BEAN_ENACTOR + ".java", beanEnactor);


        JavaFile queryComposer= compilerQueryInvoker.generateQueryInvoker(configs);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + QUERY_INVOKER + ".java", queryComposer);


        new File(configurator_dir).mkdirs();
        JavaFile configurationSql= compilerConfigurations.generateSqlConfigurator(configs,SQL_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + SQL_CONFIGURATOR + ".java", configurationSql);

        JavaFile configurationPropertyOrder= compilerConfigurations.generatePropertyOrderConfigurator(configs,PROPERTY_ORDER_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + PROPERTY_ORDER_CONFIGURATOR + ".java", configurationPropertyOrder);

        JavaFile configurationCsv= compilerConfigurations.generateCsvConfigurator(configs,CSV_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + CSV_CONFIGURATOR + ".java", configurationCsv);

        JavaFile configurationSqlInsert= compilerConfigurations.generateSqlInsertConfigurator(configs,SQL_INSERT_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + SQL_INSERT_CONFIGURATOR + ".java", configurationSqlInsert);

        JavaFile configurationConverter= compilerConfigurations.generateConverterConfigurator(configs,CONVERTER_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + CONVERTER_CONFIGURATOR + ".java", configurationConverter);

        JavaFile configurationEnactor= compilerConfigurations.generateEnactorConfigurator(configs,ENACTOR_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + ENACTOR_CONFIGURATOR + ".java", configurationEnactor);
    }

    private void exportMiscFiles(TemplatesCompilerConfig configs, String cli_dir, String cli_lib) {
        if (configs.jsweet) {
            InputStream is=getClass().getResourceAsStream("/js/TemplateManager.js");
            String targetLocation= cli_dir+"/src/main/js";
            try {
                FileUtils.copyToFile(is, new File (targetLocation+"/TemplateManager.js"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream is=getClass().getResourceAsStream("/css/provtemplate.css");
        String targetLocation= cli_dir+"/src/main/css";
        try {
            FileUtils.copyToFile(is, new File (targetLocation+"/provtemplate.css"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonNode readTree(File file) throws IOException {
        return objectMapper.readTree(file);
    }

    public TemplateBindingsSchema getBindingsSchema(String location) {
        return compilerUtil.getBindingsSchema(location);
    }

    public Document readDocumentFromFile(TemplateCompilerConfig config) throws ClassNotFoundException,
            NoSuchMethodException,
            SecurityException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        return compilerUtil.readDocumentFromFile(config.template);
    }



    public void doGenerateServerForEntry(TemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

        Document doc;
        try {
            doc = readDocumentFromFile(config);
            generate(doc, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.sbean, configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, cli_webjar_dir);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            System.out.println("could not find Interop Framework");
            e.printStackTrace();

            System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema));
        }
    }

    public void doGenerateServerForEntry1(Document doc, TemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        try {
            generate(doc, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.sbean,configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, cli_webjar_dir);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean generate(Document doc, String templateName, String packge, String cli_src_dir, String l2p_src_dir, String resource, boolean sbean, String jsonschema, String documentation, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, String cli_webjar_dir) {
        try {
            String bn= compilerUtil.templateNameClass(templateName);
            String bnI= compilerUtil.templateNameClass(templateName)+"Interface";
            String bnTM= compilerUtil.templateNameClass(templateName)+"TypeManagement";
            String bnTP= compilerUtil.templateNameClass(templateName)+"TypePropagate";
            String bnTR= compilerUtil.templateNameClass(templateName)+"TypedRecord";
            String bean=compilerUtil.beanNameClass(templateName);
            String processor=compilerUtil.processorNameClass(templateName);

            String destinationDir=l2p_src_dir + "/" + packge.replace('.', '/') + "/";
            String destinationDir2=cli_src_dir + "/" + packge.replace('.', '/') + "/" + "client" + "/";

            String destination=destinationDir + bn + ".java";
            String destinationI=destinationDir + bnI + ".java";
            String destinationTypeManagement=destinationDir + bnTM + ".java";
            String destinationTypePropagate=destinationDir + bnTP + ".java";
            String destinationTypedRecord=destinationDir + bnTR + ".java";
            String destination2=destinationDir2 + bn + ".java";
            String destinationSQL=destinationDir2 + "SQL" + ".java";
            String destination3=destinationDir2 + bean + ".java";
            String destination4=destinationDir2 + processor + ".java";




            IndexedDocument indexed = makeIndexedDodcument(doc);
            u.getBundle(doc).get(0).getStatement().clear();
            u.getBundle(doc).get(0).getStatement().addAll(u.getStatement(indexed.toDocument()));

            // generating client first to ensure successor is calculated
            Pair<JavaFile, Map<Integer, List<Integer>>> tmp=compilerClient.generateClientLib(doc,bn,templateName,packge+ ".client", resource, bindings_schema, bindingsSchema, indexed);
            JavaFile spec2=tmp.getLeft();
            Map<Integer, List<Integer>> successorTable=tmp.getRight();
            boolean val2=compilerUtil.saveToFile(destinationDir2, destination2, spec2);

            //ensure type declaration code is executed
            JavaFile spec5= compilerTypeManagement.generateTypeDeclaration(doc, bn, templateName, packge, resource, bindings_schema);
            // before propagation generation
            JavaFile spec0= compilerExpansionBuilder.generateBuilderSpecification(doc, bn, templateName, packge, resource, bindings_schema,successorTable);
            boolean val0=compilerUtil.saveToFile(destinationDir, destination, spec0);


            JavaFile spec1= compilerExpansionBuilder.generateBuilderInterfaceSpecification(doc, bn, templateName, packge, resource, bindings_schema);
            boolean val1=compilerUtil.saveToFile(destinationDir, destinationI, spec1);

            JavaFile spec2b=compilerClient.generateSQLInterface(packge+ ".client");
            boolean val2b=compilerUtil.saveToFile(destinationDir2, destinationSQL, spec2b);
            val2=val2&val2b;


            JavaFile spec6= compilerTypedRecord.generatedTypedRecordConstructor(doc, bn, templateName, packge, resource, bindings_schema);
          //  JavaFile spec7= compilerTypePropagate.generateTypeDeclaration(doc, bn, templateName, packge, resource, bindings_schema);

            boolean val5=compilerUtil.saveToFile(destinationDir, destinationTypeManagement, spec5);
            boolean val6=compilerUtil.saveToFile(destinationDir, destinationTypedRecord, spec6);
          //  boolean val7=compilerUtil.saveToFile(destinationDir, destinationTypePropagate, spec7);


            boolean val3=true;
            boolean val4=true;

            if (sbean) {
                JavaFile spec3 = compilerSimpleBean.generateSimpleBean(doc, bean, templateName, packge + ".client", resource, bindings_schema);
                val3 = compilerUtil.saveToFile(destinationDir2, destination3, spec3);

                JavaFile spec4 = compilerProcessor.generateProcessor(doc, processor, templateName, packge + ".client", resource, bindings_schema);
                val4 = compilerUtil.saveToFile(destinationDir2, destination4, spec4);



                compilerJsonSchema.generateJSonSchema(jsonschema,templateName,cli_src_dir + "/../resources", bindings_schema);
                compilerSQL.generateSQL(jsonschema+"SQL", templateName, cli_src_dir + "/../sql", bindingsSchema);

                final String cli_webjar_html_dir = cli_webjar_dir + "/html";
                new File(cli_webjar_html_dir).mkdirs();

                compilerDocumentation.generateDocumentation(documentation,templateName,cli_webjar_html_dir, bindings_schema);


            }




            return val1 & val2 & val3 & val4;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    private IndexedDocument makeIndexedDodcument(Document doc) {
        IndexedDocument indexed=new IndexedDocument(pFactory, pFactory.newDocument(),true);
        Bundle bun=u.getBundle(doc).get(0);
        u.forAllStatement(bun.getStatement(), indexed);

        indexed.getWasDerivedFrom().forEach(wdf -> {
            if (wdf.getId() == null) {
                wdf.setId(gensym());
            }
        });
        return indexed;
    }


    public CompilerJsonSchema getCompilerJsonSchema() {
        return compilerJsonSchema;
    }


}
