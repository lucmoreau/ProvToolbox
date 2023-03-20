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
    public static final TypeVariableName typeOut = TypeVariableName.get("OUT");
    public static final TypeVariableName typeIn = TypeVariableName.get("IN");

    static final TypeVariableName typeT = TypeVariableName.get("T");
    static final TypeName biconsumerType2=ParameterizedTypeName.get(ClassName.get(BiConsumer.class), typeResult, typeT);
    static final TypeName biconsumerTypeOut=ParameterizedTypeName.get(ClassName.get(BiConsumer.class), typeResult, typeOut);
    static final TypeName consumerT=ParameterizedTypeName.get(ClassName.get(Consumer.class), typeT);
    static final TypeName consumerIn=ParameterizedTypeName.get(ClassName.get(Consumer.class), typeIn);
    static final TypeName biconsumerType=ParameterizedTypeName.get(ClassName.get(BiConsumer.class),ClassName.get(StringBuilder.class), typeT);
    static final TypeName biconsumerTypeIn=ParameterizedTypeName.get(ClassName.get(BiConsumer.class),ClassName.get(StringBuilder.class), typeIn);
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
    private final CompilerInputOutputProcessor compilerInputOutputProcessor = new CompilerInputOutputProcessor();
    private final CompilerTemplateInvoker compilerTemplateInvoker = new CompilerTemplateInvoker();
    private final CompilerBeanCompleter compilerBeanCompleter = new CompilerBeanCompleter();
    private final CompilerBeanCompleter2 compilerBeanCompleter2 = new CompilerBeanCompleter2();
    private final CompilerBeanCompleter2Composite compilerBeanCompleter2Composite = new CompilerBeanCompleter2Composite();
    private final CompilerTypeConverter compilerTypeConverter = new CompilerTypeConverter();
    private final CompilerBeanEnactor compilerBeanEnactor = new CompilerBeanEnactor();
    private final CompilerBeanEnactor2 compilerBeanEnactor2 = new CompilerBeanEnactor2();
    private final CompilerQueryInvoker compilerQueryInvoker = new CompilerQueryInvoker();
    private final CompilerBeanChecker compilerBeanChecker;
    private final CompilerDelegator compilerDelegator = new CompilerDelegator();
    private final CompilerConfigurations compilerConfigurations = new CompilerConfigurations();
    private final CompilerCompositeConfigurations compilerCompositeConfigurations = new CompilerCompositeConfigurations();
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
        this.compilerBeanGenerator =new CompilerBeanGenerator();
        this.compilerIntegrator=new CompilerIntegrator(compilerCommon,compilerBeanGenerator);
        this.compilerTypeManagement= new CompilerTypeManagement(withMain, compilerCommon,pFactory,debugComment);
        this.compilerExpansionBuilder= new CompilerExpansionBuilder(withMain, compilerCommon,pFactory,debugComment,compilerTypeManagement);
        this.compilerTypedRecord = new CompilerTypedRecord(withMain, compilerCommon,pFactory,debugComment);
        this.compilerBuilderInit= new CompilerBuilderInit(pFactory);
        this.compilerBeanChecker= new CompilerBeanChecker();
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


            Locations locations=new Locations(configs,cli_src_dir);

            for (TemplateCompilerConfig aconfig: configs.templates) {
                if (TemplateConfigurationEnum.isSimple(aconfig)) {
                    SimpleTemplateCompilerConfig config=(SimpleTemplateCompilerConfig) aconfig;
                    locations.updateWithConfig(config);
                    doGenerateServerForEntry(config, configs, locations, cli_src_dir, l2p_src_dir, pFactory, cli_webjar_dir);
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
                            doGenerateServerForEntry(config, sc2, configs, locations, cli_src_dir, l2p_src_dir, cli_webjar_dir);
                        }
                    }
                    if (!found) throw new UnsupportedOperationException("Composite template configuration referencing unknown template " + simple);
                }
            }

            compilerBeanGenerator.generateSimpleConfigsWithVariants(locations, configs);

            SpecificationFile beanChecker3= compilerBeanChecker.generateBeanChecker(configs, locations, BeanDirection.INPUTS, compilerBeanGenerator.variantTable, BEAN_CHECKER3);
            beanChecker3.save();


            generateJSonSchemaEnd(configs, cli_src_dir);

            generateSQLEnd(configs, cli_src_dir);
            generateDocumentationEnd(configs, cli_src_dir);



            doGenerateProject(configs, locations, root_dir, cli_lib, l2p_lib, l2p_dir, l2p_src_dir, l2p_test_src_dir, cli_test_src_dir, cli_webjar_dir);

            doGenerateClientAndProject(configs, locations, cli_lib, cli_dir, cli_src_dir);


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

    public void doGenerateProject(TemplatesCompilerConfig configs, Locations locations, String root_dir, String cli_lib, String l2p_lib, String l2p_dir, String l2p_src_dir, String l2p_test_src_dir, String cli_test_src_dir, String cli_webjar_dir) {
        final String init_dir= l2p_src_dir + "/" + configs.init_package.replace('.', '/') + "/";
        final String l2p_test_dir= l2p_test_src_dir + "/" + configs.init_package.replace('.', '/') + "/";
        final String cli_test_dir= cli_test_src_dir + "/" + configs.init_package.replace('.', '/') + "/";


        SpecificationFile init=compilerBuilderInit.generateInitializer(configs, locations, init_dir, INIT + DOT_JAVA_EXTENSION);
        init.save();

        SpecificationFile testfile=compilerMaven.generateTestFile_l2p(configs, l2p_test_dir, TESTER_FILE+ DOT_JAVA_EXTENSION);
        testfile.save();

        compilerMaven.makeRootPom(configs, root_dir, cli_lib, l2p_lib);
        compilerMaven.makeSubPom(configs, l2p_dir, l2p_lib, true, true, false, false);

        SpecificationFile testfile2= compilerClientTest.generateTestFile_cli(configs, locations, cli_test_dir, TESTER_FILE+ DOT_JAVA_EXTENSION);
        testfile2.save();

    }

    public void doGenerateClientAndProject(TemplatesCompilerConfig configs, Locations locations, String cli_lib, String cli_dir, String cli_src_dir) {


        final String openprovenance_dir= cli_src_dir + "/" + CLIENT_PACKAGE.replace('.', '/') + "/";




        compilerMaven.makeSubPom(configs, cli_dir, cli_lib, false, configs.jsweet, true, compilerCommon.getFoundEscape());


        SpecificationFile logger=compilerLogger.generateLogger(configs, locations, configs.logger);
        logger.save();

        SpecificationFile intface=compilerLogger.generateBuilderInterface(configs, openprovenance_dir, BUILDER_INTERFACE + DOT_JAVA_EXTENSION);
        intface.save();

        SpecificationFile intface2=compilerLogger.generateLoggerInterface(configs, openprovenance_dir, LOGGER_INTERFACE + DOT_JAVA_EXTENSION);
        intface2.save();

        SpecificationFile intface3=compilerLogger.generateProcessorArgsInterface(configs, openprovenance_dir, PROCESSOR_ARGS_INTERFACE + DOT_JAVA_EXTENSION);
        intface3.save();

        SpecificationFile intface3b=compilerLogger.generateRecordsProcessorInterface(configs, openprovenance_dir, RECORDS_PROCESSOR_INTERFACE + DOT_JAVA_EXTENSION);
        intface3b.save();


        exportMiscFiles(configs, cli_dir, cli_lib);

        compilerScript.generateScript(configs);

        SpecificationFile templateBuilders=compilerTemplateBuilders.generateTemplateBuilders(configs, locations, configs.templateBuilders);
        templateBuilders.save();


        SpecificationFile tableConfigurator=compilerTableConfigurator.generateTableConfigurator(configs, locations);
        tableConfigurator.save();

        SpecificationFile tableConfigurator2=compilerTableConfigurator.generateCompositeTableConfigurator(configs, locations);
        tableConfigurator2.save();

        SpecificationFile beanProcessor=compilerBeanProcessor.generateBeanProcessor(configs, locations, configs.beanProcessor);
        beanProcessor.save();

        String integrator_package = locations.getFilePackage(BeanDirection.OUTPUTS);
        String integrator_dir=locations.convertToDirectory(integrator_package);
        SpecificationFile inputOutputProcessor=compilerInputOutputProcessor.generateInputOutputProcessor(configs, locations, integrator_package, true,integrator_dir, INPUT_OUTPUT_PROCESSOR + DOT_JAVA_EXTENSION);
        inputOutputProcessor.save();

        SpecificationFile inputProcessor=compilerInputOutputProcessor.generateInputOutputProcessor(configs, locations, integrator_package, false, integrator_dir, INPUT_PROCESSOR + DOT_JAVA_EXTENSION);
        inputProcessor.save();

        SpecificationFile templateInvoker=compilerTemplateInvoker.generateTemplateInvoker(configs, locations, TEMPLATE_INVOKER);
        templateInvoker.save();



        SpecificationFile beanCompleter=compilerBeanCompleter.generateBeanCompleter(configs, locations, BEAN_COMPLETER);
        beanCompleter.save();

        SpecificationFile beanCompleter2=compilerBeanCompleter2.generateBeanCompleter2(configs, locations, BEAN_COMPLETER2);
        beanCompleter2.save();

        SpecificationFile beanCompleter2Composite=compilerBeanCompleter2Composite.generateBeanCompleter2Composite(configs, locations, BEAN_COMPLETER2_COMPOSITE);
        beanCompleter2Composite.save();

        SpecificationFile typeConverter=compilerTypeConverter.generateTypeConverter(configs, locations, TYPE_CONVERTER);
        typeConverter.save();

        SpecificationFile beanEnactor=compilerBeanEnactor.generateBeanEnactor(configs, locations, BEAN_ENACTOR);
        beanEnactor.save();

        SpecificationFile beanEnactor2=compilerBeanEnactor2.generateBeanEnactor2(configs, locations, BEAN_ENACTOR2);
        beanEnactor2.save();

        SpecificationFile queryComposer= compilerQueryInvoker.generateQueryInvoker(configs, locations, true, QUERY_INVOKER );
        queryComposer.save();

        SpecificationFile queryComposer3= compilerQueryInvoker.generateQueryInvoker(configs, locations, false, QUERY_INVOKER3 );
        queryComposer3.save();

        SpecificationFile beanChecker= compilerBeanChecker.generateBeanChecker(configs, locations, BeanDirection.COMMON, null, BEAN_CHECKER);
        beanChecker.save();

        SpecificationFile beanDelegator= compilerDelegator.generateDelegator(configs, locations, DELEGATOR );
        beanDelegator.save();

        SpecificationFile configurationSql= compilerConfigurations.generateSqlConfigurator(configs,SQL_CONFIGURATOR, locations, locations.configurator_dir, SQL_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationSql.save();

        SpecificationFile configurationPropertyOrder= compilerConfigurations.generatePropertyOrderConfigurator(configs,PROPERTY_ORDER_CONFIGURATOR, locations, locations.configurator_dir, PROPERTY_ORDER_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationPropertyOrder.save();

        SpecificationFile configurationCsv= compilerConfigurations.generateCsvConfigurator(configs,CSV_CONFIGURATOR, locations,locations.configurator_dir, CSV_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationCsv.save();

        SpecificationFile configurationBuilder= compilerConfigurations.generateBuilderConfigurator(configs,BUILDER_CONFIGURATOR, locations, locations.configurator_dir, BUILDER_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationBuilder.save();

        SpecificationFile configurationSqlInsert = compilerConfigurations.generateSqlInsertConfigurator(configs,SQL_INSERT_CONFIGURATOR, locations, locations.configurator_dir, SQL_INSERT_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationSqlInsert.save();

        SpecificationFile configurationConverter= compilerConfigurations.generateConverterConfigurator(configs,CONVERTER_CONFIGURATOR, locations, locations.configurator_dir, CONVERTER_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationConverter.save();

        SpecificationFile configurationEnactor= compilerConfigurations.generateEnactorConfigurator(configs,ENACTOR_CONFIGURATOR, locations, locations.configurator_dir, ENACTOR_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationEnactor.save();

        SpecificationFile compositeConfigurationEnactor= compilerCompositeConfigurations.generateCompositeEnactorConfigurator(configs, locations, COMPOSITE_ENACTOR_CONFIGURATOR);
        compositeConfigurationEnactor.save();

        SpecificationFile configurationEnactor2= compilerConfigurations.generateEnactorConfigurator2(configs, ENACTOR_CONFIGURATOR3, locations.getFilePackage(BeanDirection.INPUTS), locations, locations.convertToDirectory(locations.getFilePackage(ENACTOR_CONFIGURATOR3)), ENACTOR_CONFIGURATOR3+ DOT_JAVA_EXTENSION);
        configurationEnactor2.save();
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

    public void doGenerateServerForEntry(SimpleTemplateCompilerConfig config, TemplatesCompilerConfig configs, Locations locations, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

        Document doc;
        try {
            doc = readDocumentFromFile(config);
            generate(doc, locations, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.sbean, configs.logger, configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, new LinkedList<>(), null, configs);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            System.out.println("could not find Interop Framework");
            e.printStackTrace();

            System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema));
        }
    }

    public void doGenerateServerForEntry(CompositeTemplateCompilerConfig compositeTemplateCompilerConfig, SimpleTemplateCompilerConfig config, TemplatesCompilerConfig configs, Locations locations, String cli_src_dir, String l2p_src_dir, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

        Document doc;
        try {
            doc = readDocumentFromFile(config);
            generate(doc, locations, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.sbean, configs.logger, configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, compositeTemplateCompilerConfig.sharing, compositeTemplateCompilerConfig.consistsOf, configs);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            System.out.println("could not find Interop Framework");
            e.printStackTrace();

            System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema));
        }
    }

    public void doGenerateServerForEntry1(Document doc, SimpleTemplateCompilerConfig config, TemplatesCompilerConfig configs, Locations locations, String cli_src_dir, String l2p_src_dir, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        try {
            generate(doc, locations, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.sbean, configs.logger, configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, new LinkedList<>(), null, configs);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean generate(Document doc, Locations locations, String templateName, String packageName, String cli_src_dir, String l2p_src_dir, String resource, boolean sbean, String logger, String jsonschema, String documentation, JsonNode bindings_schema,
                            TemplateBindingsSchema bindingsSchema, Map<String, Map<String, String>> sqlTables, String cli_webjar_dir, boolean inComposition, List<String> sharing, String consistsOf, TemplatesCompilerConfig configs) {
        try {
            String bn= compilerUtil.templateNameClass(templateName);
            String bnI= compilerUtil.templateNameClass(templateName)+"Interface";
            String bnTM= compilerUtil.templateNameClass(templateName)+"TypeManagement";
            String bnTR= compilerUtil.templateNameClass(templateName)+"TypedRecord";
            String bean=compilerUtil.commonNameClass(templateName);
            String outputs=compilerUtil.outputsNameClass(templateName);
            String inputs=compilerUtil.inputsNameClass(templateName);
            String integratorBuilder=compilerUtil.integratorBuilderNameClass(templateName);
            String compositeBeanNameClass=compilerUtil.commonNameClass(templateName);




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
                Pair<SpecificationFile, Map<Integer, List<Integer>>> tmp = compilerCommon.generateCommonLib(configs, locations, doc, bn, templateName, locations.getFilePackage(BeanDirection.COMMON), bindingsSchema, indexed, logger, BeanKind.SIMPLE, bn + DOT_JAVA_EXTENSION);
                SpecificationFile spec2 = tmp.getLeft();
                val2=spec2.save();
                Map<Integer, List<Integer>> successorTable = tmp.getRight();

                //ensure type declaration code is executed
                SpecificationFile spec5 = compilerTypeManagement.generateTypeDeclaration(configs, locations, doc, bn, templateName, packageName, bindings_schema, bindingsSchema, locations.getFileBackendPackage(bnTM), bnTM + DOT_JAVA_EXTENSION);
                // before propagation generation
                SpecificationFile spec0 = compilerExpansionBuilder.generateBuilderSpecification(configs, locations, doc, bn, templateName, packageName, bindings_schema, bindingsSchema, successorTable, locations.getFileBackendPackage(bn), bn + DOT_JAVA_EXTENSION);
                val0 = spec0.save();

                SpecificationFile spec1 = compilerExpansionBuilder.generateBuilderInterfaceSpecification(configs, locations, doc, bn, templateName, packageName, bindingsSchema, locations.getFileBackendPackage(bnI), bnI + DOT_JAVA_EXTENSION);
                val1= spec1.save();

                SpecificationFile spec2b = compilerCommon.generateSQLInterface(configs, locations, SQL_INTERFACE);
                val2b = spec2b.save();
                val2 = val2 & val2b;


                SpecificationFile spec6 = compilerTypedRecord.generatedTypedRecordConstructor(configs, locations, doc, bn, templateName, packageName, resource, bindings_schema, bindingsSchema, locations.getFileBackendPackage(bnTR), bnTR + DOT_JAVA_EXTENSION);
                val5 = spec5.save();
                val6 = spec6.save();


            }


            if (sbean) {
                String integratorPackage = locations.getFilePackage(BeanDirection.OUTPUTS);
                String integratorDir = locations.convertToDirectory(integratorPackage);
                if (!inComposition) {
                    SpecificationFile spec3 = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema, BeanKind.SIMPLE, BeanDirection.COMMON, null, null, null, bean + DOT_JAVA_EXTENSION);
                    val3 = spec3.save();

                    SpecificationFile spec3b = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema, BeanKind.SIMPLE, BeanDirection.OUTPUTS, null, null, null, outputs + DOT_JAVA_EXTENSION);
                    val3 = val3 & spec3b.save();

                    SpecificationFile spec3c = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema, BeanKind.SIMPLE, BeanDirection.INPUTS, null, null, null, inputs + DOT_JAVA_EXTENSION);
                    val3 = val3 & spec3c.save();

                    SpecificationFile spec7 = compilerIntegrator.generateIntegrator(locations, templateName, integratorPackage, bindingsSchema, logger, BeanKind.SIMPLE, consistsOf, integratorDir, integratorBuilder + DOT_JAVA_EXTENSION);
                    val3 = val3 & spec7.save();

                    SpecificationFile spec4 = compilerProcessor.generateProcessor(locations, templateName, locations.getFilePackage(BeanDirection.COMMON), bindingsSchema, !IN_INTEGRATOR, compilerUtil.processorNameClass(templateName) + DOT_JAVA_EXTENSION);
                    val4 = spec4.save();

                    SpecificationFile spec4b = compilerProcessor.generateProcessor(locations, templateName, locations.getFilePackage(BeanDirection.INPUTS), bindingsSchema, IN_INTEGRATOR, compilerUtil.integratorNameClass(templateName) + DOT_JAVA_EXTENSION);
                    val4 = val4 & spec4b.save();
                }

                if (!inComposition) {

                    compilerJsonSchema.generateJSonSchema(templateName, bindingsSchema);
                    compilerSQL.generateSQL(jsonschema + SQL_INTERFACE, templateName, cli_src_dir + "/../sql", bindingsSchema);
                    compilerSQL.generateSQLInsertFunction(jsonschema + SQL_INTERFACE, templateName, null, cli_src_dir + "/../sql", bindingsSchema, Arrays.asList());

                    compilerSQL.generateSQLPrimitiveTables(sqlTables);
                }

                if (inComposition) {



                    if (consistsOf==null) {
                        throw new NullPointerException("No composed class has been specified for composite " + templateName);
                    }

                    compilerSQL.generateSQLInsertFunction(jsonschema + SQL_INTERFACE, templateName, consistsOf, cli_src_dir + "/../sql", bindingsSchema, sharing);

                    SimpleTemplateCompilerConfig config = new SimpleTemplateCompilerConfig();
                    config.name = compositeBeanNameClass;
                    config.package_ = packageName;
                    config.bindings = "openprovenance:composite-bean.json";
                    config.template = "openprovenance:composite-bean.provn";
                    TemplateBindingsSchema bindingsSchema2 = compilerUtil.getBindingsSchema(config);

                    SpecificationFile spec7b = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.COMMON, consistsOf, null, null, compositeBeanNameClass + DOT_JAVA_EXTENSION);
                    if (spec7b!=null) {
                        val3 = val3 & spec7b.save();
                    }
                    SpecificationFile spec7c = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.OUTPUTS, consistsOf, null, null, outputs + DOT_JAVA_EXTENSION);
                    if (spec7c!=null) {
                        val3 = val3 & spec7c.save();
                    }
                    SpecificationFile spec7d = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.INPUTS, consistsOf, sharing, null, inputs + DOT_JAVA_EXTENSION);
                    if (spec7d!=null) {
                        val3 = val3 & spec7d.save();
                    }

                    Pair<SpecificationFile, Map<Integer, List<Integer>>> tmp = compilerCommon.generateCommonLib(configs, locations, doc, bn, templateName, locations.getFilePackage(BeanDirection.COMMON), bindingsSchema2, indexed, logger, BeanKind.COMPOSITE, bn + DOT_JAVA_EXTENSION);
                    if (tmp.getLeft()!=null) {
                        SpecificationFile spec2 = tmp.getLeft();
                        val3 = val3 & spec2.save();
                    }

                    SpecificationFile spec7 = compilerIntegrator.generateIntegrator(locations, templateName, integratorPackage, bindingsSchema2, logger, BeanKind.COMPOSITE, consistsOf, integratorDir, integratorBuilder + DOT_JAVA_EXTENSION);
                    val3 = val3 & spec7.save();

                }

                if (!inComposition) {
                    final String cli_webjar_html_dir = cli_webjar_dir + "/html";
                    new File(cli_webjar_html_dir).mkdirs();
                    compilerDocumentation.generateDocumentation(documentation, templateName, cli_webjar_html_dir, bindingsSchema);
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
