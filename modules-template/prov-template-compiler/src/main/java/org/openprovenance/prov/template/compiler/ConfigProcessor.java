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
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.expansion.CompilerExpansionBuilder;
import org.openprovenance.prov.template.compiler.expansion.CompilerTypeManagement;
import org.openprovenance.prov.template.compiler.expansion.CompilerTypedRecord;
import org.openprovenance.prov.template.compiler.integration.CompilerIntegrator;
import org.openprovenance.prov.template.descriptors.DescriptorUtils;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.expansion.StatementTypeAction.gensym;


public class ConfigProcessor implements Constants {
    static final TypeVariableName typeResult = TypeVariableName.get("RESULT");
    public static final TypeVariableName typeOutput = TypeVariableName.get("OUTPUT");
    static final TypeVariableName typeT = TypeVariableName.get("T");
    static final TypeName biconsumerType2=ParameterizedTypeName.get(ClassName.get(BiConsumer.class), typeResult, typeT);
    static final TypeName consumerT=ParameterizedTypeName.get(ClassName.get(Consumer.class), typeT);
    static final TypeName biconsumerType=ParameterizedTypeName.get(ClassName.get(BiConsumer.class),ClassName.get(StringBuilder.class), typeT);
    static final TypeName listTypeT=ParameterizedTypeName.get(ClassName.get(List.class), typeT);
    private final ProvFactory pFactory;
    private final CompilerSQL compilerSQL;
    private final boolean debugComment;
    public static final DescriptorUtils descriptorUtils;
    private final CompilerIntegrator compilerIntegrator;
    boolean withMain=true; // TODO need to be updatable via command line
    public static final ObjectMapper objectMapper = new ObjectMapper();
    private final CompilerUtil compilerUtil     = new CompilerUtil();
    private final CompilerLogger compilerLogger = new CompilerLogger();
    private final CompilerTemplateBuilders compilerTemplateBuilders = new CompilerTemplateBuilders();
    private final CompilerTableConfigurator compilerTableConfigurator = new CompilerTableConfigurator();
    private final CompilerBeanProcessor compilerBeanProcessor = new CompilerBeanProcessor();
    private final CompilerBeanCompleter compilerBeanCompleter = new CompilerBeanCompleter();
    private final CompilerBeanCompleter2 compilerBeanCompleter2 = new CompilerBeanCompleter2();
    private final CompilerTypeConverter compilerTypeConverter = new CompilerTypeConverter();
    private final CompilerBeanEnactor compilerBeanEnactor = new CompilerBeanEnactor();
    private final CompilerBeanEnactor2 compilerBeanEnactor2 = new CompilerBeanEnactor2();
    private final CompilerQueryInvoker compilerQueryInvoker = new CompilerQueryInvoker();
    private final CompilerBeanChecker compilerBeanChecker = new CompilerBeanChecker();
    private final CompilerConfigurations compilerConfigurations = new CompilerConfigurations();
    private final CompilerMaven compilerMaven   = new CompilerMaven(this);
    private final CompilerScript compilerScript   = new CompilerScript(this);
    private final CompilerDocumentation compilerDocumentation = new CompilerDocumentation();
    private final CompilerCommon compilerCommon;
    private final CompilerExpansionBuilder compilerExpansionBuilder;
    private final CompilerBuilderInit compilerBuilderInit;
    private final CompilerTypeManagement compilerTypeManagement;
    private final CompilerTypedRecord compilerTypedRecord;
    private final CompilerBeanGenerator compilerBeanGenerator;
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
        this.compilerCommon = new CompilerCommon(pFactory,compilerSQL);
        this.compilerIntegrator=new CompilerIntegrator(compilerCommon);
        this.compilerTypeManagement= new CompilerTypeManagement(withMain, compilerCommon,pFactory,debugComment);
        this.compilerExpansionBuilder= new CompilerExpansionBuilder(withMain, compilerCommon,pFactory,debugComment,compilerTypeManagement);
        this.compilerTypedRecord = new CompilerTypedRecord(withMain, compilerCommon,pFactory,debugComment);
        this.compilerBuilderInit= new CompilerBuilderInit(pFactory);
        this.compilerBeanGenerator =new CompilerBeanGenerator();
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
            //System.out.println(configs);
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


            for (TemplateCompilerConfig aconfig: configs.templates) {
                if (TemplateConfigurationEnum.isSimple(aconfig)) {
                    SimpleTemplateCompilerConfig config=(SimpleTemplateCompilerConfig) aconfig;
                    doGenerateServerForEntry(config, configs, cli_src_dir, l2p_src_dir, pFactory, cli_webjar_dir);
                    FileUtils.copyFileToDirectory(new File(config.template), new File(cli_webjar_templates_dir));
                    FileUtils.copyFileToDirectory(new File(config.bindings), new File(cli_webjar_bindings_dir));
                } else {
                    CompositeTemplateCompilerConfig config=(CompositeTemplateCompilerConfig) aconfig;


                    String simple=config.consistsOf;
                    boolean found=false;
                    //System.out.println("==> Found " + config);
                    for (TemplateCompilerConfig aconfig2: configs.templates) {
                        if (Objects.equals(aconfig2.name, simple)) {
                            found=true;

                            //System.out.println("==> Found " + aconfig2);
                            SimpleTemplateCompilerConfig sc=(SimpleTemplateCompilerConfig) aconfig2;
                            SimpleTemplateCompilerConfig sc2=sc.cloneAsInstanceInComposition(config.name);
                            doGenerateServerForEntry(config, sc2, configs, cli_src_dir, l2p_src_dir, pFactory, cli_webjar_dir);
                        }
                    }
                    if (!found) throw new UnsupportedOperationException("Composite template configuration referencing unknown template " + simple);
                }
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
        final String configurator_dir2= cli_src_dir + "/" + configs.configurator_package.replace('.', '/') + "2/";
        final String logger_dir= cli_src_dir + "/" + configs.logger_package.replace('.', '/') + "/";
        final String openprovenance_dir= cli_src_dir + "/" + CLIENT_PACKAGE.replace('.', '/') + "/";

        compilerMaven.makeSubPom(configs, cli_dir, cli_lib, false, configs.jsweet, true, compilerCommon.getFoundEscape());


        JavaFile logger=compilerLogger.generateLogger(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + configs.logger+ ".java", logger);

        JavaFile intface=compilerLogger.generateBuilderInterface(configs);
        compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir + BUILDER_INTERFACE + ".java", intface);

        JavaFile intface2=compilerLogger.generateLoggerInterface(configs);
        compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir + LOGGER_INTERFACE + ".java", intface2);

        JavaFile intface3=compilerLogger.generateProcessorArgsInterface(configs);
        compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir + PROCESSOR_ARGS_INTERFACE + ".java", intface3);

        JavaFile intface3b=compilerLogger.generateRecordsProcessorInterface(configs);
        compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir + RECORDS_PROCESSOR_INTERFACE + ".java", intface3b);


        exportMiscFiles(configs, cli_dir, cli_lib);

        compilerScript.generateScript(configs);

        JavaFile templateBuilders=compilerTemplateBuilders.generateTemplateBuilders(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + configs.templateBuilders+ ".java", templateBuilders);


        Pair<String, JavaFile> nameAndTableConfigurator=compilerTableConfigurator.generateTableConfigurator(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + nameAndTableConfigurator.getLeft()+ ".java", nameAndTableConfigurator.getRight());

        Pair<String, JavaFile> nameAndTableConfigurator2=compilerTableConfigurator.generateCompositeTableConfigurator(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + nameAndTableConfigurator2.getLeft()+ ".java", nameAndTableConfigurator2.getRight());

        JavaFile beanProcessor=compilerBeanProcessor.generateBeanProcessor(configs);
        compilerUtil.saveToFile(logger_dir, logger_dir + configs.beanProcessor+ ".java", beanProcessor);

        new File(configurator_dir).mkdirs();

        new File(configurator_dir2).mkdirs();


        JavaFile beanCompleter=compilerBeanCompleter.generateBeanCompleter(configs);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + BEAN_COMPLETER + ".java", beanCompleter);

        JavaFile beanCompleter2=compilerBeanCompleter2.generateBeanCompleter2(configs);
        compilerUtil.saveToFile(configurator_dir2, configurator_dir2 + BEAN_COMPLETER2 + ".java", beanCompleter2);

        JavaFile typeConverter=compilerTypeConverter.generateTypeConverter(configs);
        compilerUtil.saveToFile(configurator_dir2, configurator_dir2 + TYPE_CONVERTER + ".java", typeConverter);


        JavaFile beanEnactor=compilerBeanEnactor.generateBeanEnactor(configs);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + BEAN_ENACTOR + ".java", beanEnactor);

        JavaFile beanEnactor2=compilerBeanEnactor2.generateBeanEnactor2(configs);
        compilerUtil.saveToFile(configurator_dir2, configurator_dir2 + BEAN_ENACTOR2 + ".java", beanEnactor2);


        JavaFile queryComposer= compilerQueryInvoker.generateQueryInvoker(configs);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + QUERY_INVOKER + ".java", queryComposer);

        JavaFile beanChecker= compilerBeanChecker.generateBeanChecker(configs);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + BEAN_CHECKER + ".java", beanChecker);

        JavaFile configurationSql= compilerConfigurations.generateSqlConfigurator(configs,SQL_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + SQL_CONFIGURATOR + ".java", configurationSql);

        JavaFile configurationPropertyOrder= compilerConfigurations.generatePropertyOrderConfigurator(configs,PROPERTY_ORDER_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + PROPERTY_ORDER_CONFIGURATOR + ".java", configurationPropertyOrder);

        JavaFile configurationCsv= compilerConfigurations.generateCsvConfigurator(configs,CSV_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + CSV_CONFIGURATOR + ".java", configurationCsv);

        JavaFile configurationBuilder= compilerConfigurations.generateBuilderConfigurator(configs,BUILDER_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + BUILDER_CONFIGURATOR + ".java", configurationBuilder);

        JavaFile configurationSqlInsert= compilerConfigurations.generateSqlInsertConfigurator(configs,SQL_INSERT_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + SQL_INSERT_CONFIGURATOR + ".java", configurationSqlInsert);

        JavaFile configurationConverter= compilerConfigurations.generateConverterConfigurator(configs,CONVERTER_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + CONVERTER_CONFIGURATOR + ".java", configurationConverter);

        JavaFile configurationEnactor= compilerConfigurations.generateEnactorConfigurator(configs,ENACTOR_CONFIGURATOR);
        compilerUtil.saveToFile(configurator_dir, configurator_dir + ENACTOR_CONFIGURATOR + ".java", configurationEnactor);

        JavaFile configurationEnactor2= compilerConfigurations.generateEnactorConfigurator2(configs,ENACTOR_CONFIGURATOR2);
        compilerUtil.saveToFile(configurator_dir2, configurator_dir2 + ENACTOR_CONFIGURATOR2 + ".java", configurationEnactor2);


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

    public Document readDocumentFromFile(SimpleTemplateCompilerConfig config) throws ClassNotFoundException,
            NoSuchMethodException,
            SecurityException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        return compilerUtil.readDocumentFromFile(config.template);
    }

    public void doGenerateServerForEntry(SimpleTemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

        Document doc;
        try {
            doc = readDocumentFromFile(config);
            generate(doc, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.sbean, configs.logger, configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, new LinkedList<>(), null);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            System.out.println("could not find Interop Framework");
            e.printStackTrace();

            System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema));
        }
    }

    public void doGenerateServerForEntry(CompositeTemplateCompilerConfig compositeTemplateCompilerConfig,SimpleTemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

        Document doc;
        try {
            doc = readDocumentFromFile(config);
            generate(doc, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.sbean, configs.logger, configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, compositeTemplateCompilerConfig.sharing, compositeTemplateCompilerConfig.consistsOf);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            System.out.println("could not find Interop Framework");
            e.printStackTrace();

            System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema));
        }
    }

    public void doGenerateServerForEntry1(Document doc, SimpleTemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        try {
            generate(doc, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.sbean, configs.logger, configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, new LinkedList<>(), null);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean generate(Document doc, String templateName, String packge, String cli_src_dir, String l2p_src_dir, String resource, boolean sbean, String logger, String jsonschema, String documentation, JsonNode bindings_schema,
                            TemplateBindingsSchema bindingsSchema, Map<String, Map<String, String>> sqlTables, String cli_webjar_dir, boolean inComposition, List<String> sharing, String consistsOf) {
        try {
            String bn= compilerUtil.templateNameClass(templateName);
            String bnI= compilerUtil.templateNameClass(templateName)+"Interface";
            String bnTM= compilerUtil.templateNameClass(templateName)+"TypeManagement";
            String bnTP= compilerUtil.templateNameClass(templateName)+"TypePropagate";
            String bnTR= compilerUtil.templateNameClass(templateName)+"TypedRecord";
            String bean=compilerUtil.commonNameClass(templateName);
            String outputs=compilerUtil.outputsNameClass(templateName);
            String inputs=compilerUtil.inputsNameClass(templateName);
            String processor=compilerUtil.processorNameClass(templateName);
            String integratorBuilder=compilerUtil.integratorBuilderNameClass(templateName);
            String compositeBeanNameClass=compilerUtil.commonNameClass(templateName);
            String integrator=compilerUtil.integratorNameClass(templateName);

            String destinationDir=l2p_src_dir + "/" + packge.replace('.', '/') + "/";
            String destinationDir2=cli_src_dir + "/" + packge.replace('.', '/') + "/" + "client" + "/";
            String destinationDir2b=cli_src_dir + "/" + packge.replace('.', '/') + "/" + "client" + "/" + "integrator" + "/";

            String destination=destinationDir + bn + ".java";
            String destinationI=destinationDir + bnI + ".java";
            String destinationTypeManagement=destinationDir + bnTM + ".java";
            String destinationTypePropagate=destinationDir + bnTP + ".java";
            String destinationTypedRecord=destinationDir + bnTR + ".java";
            String destination2=destinationDir2 + bn + ".java";
            String destinationSQL=destinationDir2 + "SQL" + ".java";
            String destination3=destinationDir2 + bean + ".java";
            String destination3b=destinationDir2b + outputs + ".java";
            String destination3c=destinationDir2b + inputs + ".java";
            String destination4=destinationDir2 + processor + ".java";

            String destination7=destinationDir2b + integratorBuilder + ".java";
            String destination7b=destinationDir2 + compositeBeanNameClass + ".java";  // in same folder as simple beans
            String destination7c=destinationDir2 + bn + ".java";  // in same folder as simple beans
            String destination4b=destinationDir2b + integrator + ".java";

            String destination7g=destinationDir2b + outputs + ".java";
            String destination7h=destinationDir2b + inputs + ".java";



            IndexedDocument indexed = makeIndexedDocument(doc);
            u.getBundle(doc).get(0).getStatement().clear();
            u.getBundle(doc).get(0).getStatement().addAll(u.getStatement(indexed.toDocument()));


            boolean val0=true;
            boolean val1=true;
            boolean val2=true;
            boolean val2b=true;
            boolean val5=true;
            boolean val6=true;
            boolean val3=true;
            boolean val4=true;

            boolean val7=true;

            if (!inComposition) {
                // generating client first to ensure successor is calculated
                Pair<JavaFile, Map<Integer, List<Integer>>> tmp = compilerCommon.generateCommonLib(doc, bn, templateName, packge + ".client", bindings_schema, bindingsSchema, indexed, logger, BeanKind.SIMPLE);
                JavaFile spec2 = tmp.getLeft();
                Map<Integer, List<Integer>> successorTable = tmp.getRight();
                val2 = compilerUtil.saveToFile(destinationDir2, destination2, spec2);

                //ensure type declaration code is executed
                JavaFile spec5 = compilerTypeManagement.generateTypeDeclaration(doc, bn, templateName, packge, bindings_schema, bindingsSchema);
                // before propagation generation
                JavaFile spec0 = compilerExpansionBuilder.generateBuilderSpecification(doc, bn, templateName, packge, bindings_schema, bindingsSchema, successorTable);
                val0 = compilerUtil.saveToFile(destinationDir, destination, spec0);


                JavaFile spec1 = compilerExpansionBuilder.generateBuilderInterfaceSpecification(doc, bn, templateName, packge, bindingsSchema);
                val1 = compilerUtil.saveToFile(destinationDir, destinationI, spec1);

                JavaFile spec2b = compilerCommon.generateSQLInterface(packge + ".client");
                val2b = compilerUtil.saveToFile(destinationDir2, destinationSQL, spec2b);
                val2 = val2 & val2b;


                JavaFile spec6 = compilerTypedRecord.generatedTypedRecordConstructor(doc, bn, templateName, packge, resource, bindings_schema, bindingsSchema);
                //  JavaFile spec7= compilerTypePropagate.generateTypeDeclaration(doc, bn, templateName, packge, res"ource, bindings_schema);

                val5 = compilerUtil.saveToFile(destinationDir, destinationTypeManagement, spec5);
                val6 = compilerUtil.saveToFile(destinationDir, destinationTypedRecord, spec6);
                //  boolean val7=compilerUtil.saveToFile(destinationDir, destinationTypePropagate, spec7);


            }


            if (sbean) {
                if (!inComposition) {
                    JavaFile spec3 = compilerBeanGenerator.generateBean(templateName, packge + ".client", bindingsSchema, BeanKind.SIMPLE, BeanDirection.COMMON, null);
                    val3 = compilerUtil.saveToFile(destinationDir2, destination3, spec3);
                    JavaFile spec3b = compilerBeanGenerator.generateBean(templateName, packge + ".client.integrator", bindingsSchema, BeanKind.SIMPLE, BeanDirection.OUTPUTS, null);
                    val3 = compilerUtil.saveToFile(destinationDir2b, destination3b, spec3b);
                    JavaFile spec3c = compilerBeanGenerator.generateBean(templateName, packge + ".client.integrator", bindingsSchema, BeanKind.SIMPLE, BeanDirection.INPUTS, null);
                    val3 = compilerUtil.saveToFile(destinationDir2b, destination3c, spec3c);

                    JavaFile spec7 = compilerIntegrator.generateIntegrator(templateName, packge + ".client.integrator", bindingsSchema);
                    val3 = compilerUtil.saveToFile(destinationDir2b, destination7, spec7);


                    JavaFile spec4 = compilerProcessor.generateProcessor(templateName, packge + ".client", bindingsSchema, !IN_INTEGRATOR);
                    val4 = compilerUtil.saveToFile(destinationDir2, destination4, spec4);

                    JavaFile spec4b = compilerProcessor.generateProcessor(templateName, packge + ".client.integrator", bindingsSchema, IN_INTEGRATOR);
                    val4 = val4 & compilerUtil.saveToFile(destinationDir2b, destination4b, spec4b);
                }

                if (!inComposition) {

                    compilerJsonSchema.generateJSonSchema(templateName, bindingsSchema);
                    compilerSQL.generateSQL(jsonschema + "SQL", templateName, cli_src_dir + "/../sql", bindingsSchema);
                    compilerSQL.generateSQLInsertFunction(jsonschema + "SQL", templateName, null, cli_src_dir + "/../sql", bindingsSchema, Arrays.asList());

                    compilerSQL.generateSQLPrimitiveTables(sqlTables);
                }

                if (inComposition) {



                    if (consistsOf==null) {
                        throw new NullPointerException("No composed class has been specified for composite " + templateName);
                    }

                    compilerSQL.generateSQLInsertFunction(jsonschema + "SQL", templateName, consistsOf, cli_src_dir + "/../sql", bindingsSchema, sharing);

                    SimpleTemplateCompilerConfig config = new SimpleTemplateCompilerConfig();
                    config.name = compositeBeanNameClass;
                    config.package_ = packge;
                    config.bindings = "openprovenance:composite-bean.json";
                    config.template = "openprovenance:composite-bean.provn";
                    TemplateBindingsSchema bindingsSchema2 = compilerUtil.getBindingsSchema(config);

                    JavaFile spec7b = compilerBeanGenerator.generateBean(templateName, packge+ ".client", bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.COMMON, consistsOf);
                    if (spec7b!=null) {
                        val3 = val3 & compilerUtil.saveToFile(destinationDir2b, destination7b, spec7b);
                    }
                    JavaFile spec7c = compilerBeanGenerator.generateBean(templateName, packge+ ".client.integrator", bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.OUTPUTS, consistsOf);
                    if (spec7c!=null) {
                        val3 = val3 & compilerUtil.saveToFile(destinationDir2b, destination7g, spec7c);
                    }
                    JavaFile spec7d = compilerBeanGenerator.generateBean(templateName, packge+ ".client.integrator", bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.INPUTS, consistsOf);
                    if (spec7d!=null) {
                        val3 = val3 & compilerUtil.saveToFile(destinationDir2b, destination7h, spec7d);
                    }

                    Pair<JavaFile, Map<Integer, List<Integer>>> tmp = compilerCommon.generateCommonLib(doc, bn, templateName, packge + ".client", null, bindingsSchema2, indexed, logger, BeanKind.COMPOSITE);
                    if (tmp.getLeft()!=null) {
                        val3 = val3 & compilerUtil.saveToFile(destinationDir2b, destination7c, tmp.getLeft());
                    }


                }

                if (!inComposition) {
                    final String cli_webjar_html_dir = cli_webjar_dir + "/html";
                    new File(cli_webjar_html_dir).mkdirs();
                    compilerDocumentation.generateDocumentation(documentation, templateName, cli_webjar_html_dir, bindings_schema);
                }
            }




            return val1 & val2 & val3 & val4;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    private IndexedDocument makeIndexedDocument(Document doc) {
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
