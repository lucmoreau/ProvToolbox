package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import org.openprovenance.prov.template.compiler.sql.CompilerSqlIntegration;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.DescriptorUtils;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.expansion.StatementTypeAction.gensym;


public class ConfigProcessor implements Constants {
    public static final TypeVariableName typeResult = TypeVariableName.get("RESULT");
    public static final TypeVariableName typeOutput = TypeVariableName.get("OUTPUT");
    public static final TypeVariableName typeOut = TypeVariableName.get("OUT");
    public static final TypeVariableName typeIn = TypeVariableName.get("IN");

    static final TypeVariableName typeT = TypeVariableName.get("T");
    static final TypeName biconsumerType2=ParameterizedTypeName.get(ClassName.get(BiConsumer.class), typeResult, typeT);
    public static final TypeName biconsumerTypeOut=ParameterizedTypeName.get(ClassName.get(BiConsumer.class), typeResult, typeOut);
    static final TypeName consumerT=ParameterizedTypeName.get(ClassName.get(Consumer.class), typeT);
    public static final TypeName consumerIn=ParameterizedTypeName.get(ClassName.get(Consumer.class), typeIn);
    static final TypeName biconsumerType=ParameterizedTypeName.get(ClassName.get(BiConsumer.class),ClassName.get(StringBuilder.class), typeT);
    public static final TypeName biconsumerTypeIn=ParameterizedTypeName.get(ClassName.get(BiConsumer.class),ClassName.get(StringBuilder.class), typeIn);
    static final TypeName listTypeT=ParameterizedTypeName.get(ClassName.get(List.class), typeT);
    private final ProvFactory pFactory;
    private final CompilerSQL compilerSQL;
    private final boolean debugComment;
    public static final DescriptorUtils descriptorUtils;
    private final CompilerIntegrator compilerIntegrator;
    boolean withMain=true; // TODO need to be updatable via command line
    public static final ObjectMapper objectMapper = new ObjectMapper();
    private final CompilerUtil compilerUtil;
    private final CompilerLogger compilerLogger ;
    private final CompilerTemplateBuilders compilerTemplateBuilders ;
    private final CompilerTableConfigurator compilerTableConfigurator;
    private final CompilerTableConfiguratorWithMap compilerTableConfiguratorWithMap ;
    private final CompilerTableConfiguratorForTypes compilerTableConfiguratorForTypes;
    private final CompilerBeanProcessor compilerBeanProcessor;
    private final CompilerInputOutputProcessor compilerInputOutputProcessor;
    private final CompilerTemplateInvoker compilerTemplateInvoker;
    private final CompilerBeanCompleter compilerBeanCompleter;
    private final CompilerBeanCompleter2 compilerBeanCompleter2;
    private final CompilerBeanCompleter3 compilerBeanCompleter3;

    private final CompilerBeanCompleter2Composite compilerBeanCompleter2Composite;
    private final CompilerTypeConverter compilerTypeConverter ;
    private final CompilerBeanEnactor compilerBeanEnactor;
    private final CompilerBeanEnactor2 compilerBeanEnactor2 ;
    private final CompilerBeanLocalEnactor2 compilerBeanLocalEnactor2 ;

    private final CompilerBeanEnactor2WithPrincipal compilerBeanEnactor2WP ;
    private final CompilerSqlIntegration compilerSqlIntegration;
    private final CompilerBeanEnactor2Composite compilerBeanEnactor2composite;
    private final CompilerBeanEnactor2CompositeWithPrincipal compilerBeanEnactor2compositeWP;
    private final CompilerQueryInvoker compilerQueryInvoker ;
    private final CompilerQueryInvokerWithPrincipal compilerQueryInvokerWithPrincipal ;
    private final CompilerBeanChecker compilerBeanChecker;
    private final CompilerDelegator compilerDelegator;
    private final CompilerConfigurations compilerConfigurations ;
    private final CompilerCompositeConfigurations compilerCompositeConfigurations ;
    private final CompilerMaven compilerMaven ;
    private final CompilerScript compilerScript   = new CompilerScript(this);
    private final CompilerDocumentation compilerDocumentation ;
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
        this.compilerUtil= new CompilerUtil(pFactory);
        this.compilerTypeConverter=new CompilerTypeConverter(pFactory);
        this.compilerBeanCompleter=new CompilerBeanCompleter(pFactory);
        this.compilerSQL=new CompilerSQL(pFactory, "ID");
        this.compilerCommon = new CompilerCommon(pFactory,compilerSQL);
        this.compilerBeanGenerator =new CompilerBeanGenerator(pFactory);
        this.compilerIntegrator=new CompilerIntegrator(pFactory, compilerCommon,compilerBeanGenerator);
        this.compilerTypeManagement= new CompilerTypeManagement(withMain, compilerCommon,pFactory,debugComment);
        this.compilerExpansionBuilder= new CompilerExpansionBuilder(withMain, compilerCommon,pFactory,debugComment,compilerTypeManagement);
        this.compilerTypedRecord = new CompilerTypedRecord(withMain, compilerCommon,pFactory,debugComment);
        this.compilerBuilderInit= new CompilerBuilderInit(pFactory);
        this.compilerBeanChecker= new CompilerBeanChecker(pFactory);
        this.compilerProcessor =new CompilerProcessor(pFactory);
        this.compilerJsonSchema=new CompilerJsonSchema(pFactory);
        this.compilerClientTest =new CompilerClientTest(pFactory);
        this.compilerTemplateInvoker = new CompilerTemplateInvoker(pFactory);
        this.compilerBeanEnactor2 = new CompilerBeanEnactor2(pFactory);
        this.compilerBeanLocalEnactor2 = new CompilerBeanLocalEnactor2(pFactory);
        this.compilerBeanEnactor2WP = new CompilerBeanEnactor2WithPrincipal(pFactory);
        this.compilerBeanEnactor2composite = new CompilerBeanEnactor2Composite(pFactory);
        this.compilerBeanEnactor = new CompilerBeanEnactor(pFactory);
        this.compilerBeanCompleter2 = new CompilerBeanCompleter2(pFactory);
        this.compilerBeanCompleter3 = new CompilerBeanCompleter3(pFactory);
        this.compilerBeanCompleter2Composite  = new CompilerBeanCompleter2Composite(pFactory);
        this.compilerBeanProcessor  = new CompilerBeanProcessor(pFactory);
        this.compilerCompositeConfigurations = new CompilerCompositeConfigurations(pFactory);
        this.compilerConfigurations = new CompilerConfigurations(pFactory);
        this.compilerDelegator =  new CompilerDelegator(pFactory);
        this.compilerDocumentation = new CompilerDocumentation(pFactory);
        this.compilerInputOutputProcessor =  new CompilerInputOutputProcessor(pFactory);
        this.compilerLogger = new CompilerLogger(pFactory);
        this.compilerMaven =   new CompilerMaven(pFactory, this);
        this.compilerQueryInvoker = new CompilerQueryInvoker(pFactory);
        this.compilerTableConfigurator =  new CompilerTableConfigurator(pFactory);
        this.compilerTableConfiguratorWithMap = new CompilerTableConfiguratorWithMap(pFactory);
        this.compilerTemplateBuilders = new CompilerTemplateBuilders(pFactory);
        this.compilerTableConfiguratorForTypes = new CompilerTableConfiguratorForTypes(pFactory);
        this.compilerSqlIntegration = new CompilerSqlIntegration(pFactory);
        this.compilerQueryInvokerWithPrincipal = new CompilerQueryInvokerWithPrincipal(pFactory);
        this.compilerBeanEnactor2compositeWP = new CompilerBeanEnactor2CompositeWithPrincipal(pFactory);
    }

   // public String readCompilerVersion() {
   //     return Configuration.getPropertiesFromClasspath(getClass(),"compiler.properties").getProperty("compiler.version");
 //   }
 //   final String compilerVersion=readCompilerVersion();
    public int processTemplateGenerationConfig(String template_builder, String inputBaseDir, String outputBaseDir, ProvFactory pFactory) {
        TemplatesProjectConfiguration configs;

        try {
            configs = objectMapper.readValue(new File(addBaseDirIfRelative(template_builder, inputBaseDir)), TemplatesProjectConfiguration.class);
            //System.out.println(configs);
            final String root_dir = outputBaseDir + "/" + configs.destination + "/" + configs.name;
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


            Locations locations=new Locations(configs,cli_src_dir,l2p_src_dir);


            for (TemplateCompilerConfig aconfig: configs.templates) {
                if (TemplateConfigurationEnum.isSimple(aconfig)) {
                    SimpleTemplateCompilerConfig config=(SimpleTemplateCompilerConfig) aconfig;
                    config.template=addBaseDirIfRelative(config.template, inputBaseDir);
                    config.bindings=addBaseDirIfRelative(config.bindings, inputBaseDir);
                    locations.updateWithConfig(config);
                    doGenerateServerForEntry(config, configs, locations, cli_src_dir, l2p_src_dir, pFactory, cli_webjar_dir);
                    FileUtils.copyFileToDirectory(new File(config.template), new File(cli_webjar_templates_dir));
                    FileUtils.copyFileToDirectory(new File(config.bindings), new File(cli_webjar_bindings_dir));
                } else {
                    CompositeTemplateCompilerConfig config=(CompositeTemplateCompilerConfig) aconfig;



                    String simple=config.consistsOf;
                    List<String> sharing=config.sharing;
                    boolean found=false;
                    //System.out.println("==> Found " + config);
                    for (TemplateCompilerConfig aconfig2: configs.templates) {
                        if (Objects.equals(aconfig2.name, simple)) {
                            found=true;

                            //System.out.println("==> Found " + aconfig2);
                            SimpleTemplateCompilerConfig sc=(SimpleTemplateCompilerConfig) aconfig2;
                            sc.template=addBaseDirIfRelative(sc.template, inputBaseDir);
                            sc.bindings=addBaseDirIfRelative(sc.bindings, inputBaseDir);
                            SimpleTemplateCompilerConfig sc2=sc.cloneAsInstanceInComposition(config.name,config.sharing);
                            doGenerateServerForEntry(config, sc2, configs, locations, cli_src_dir, l2p_src_dir, cli_webjar_dir);
                        }
                    }
                    if (!found) throw new UnsupportedOperationException("Composite template configuration referencing unknown template " + simple);
                }
            }

            compilerBeanGenerator.generateSimpleConfigsWithVariants(locations, configs);



            if (configs.integrator) {
                SpecificationFile beanChecker3 = compilerBeanChecker.generateBeanChecker(configs, locations, BeanDirection.INPUTS, compilerBeanGenerator.variantTable, BEAN_CHECKER2);
                beanChecker3.save();
            }


            generateJSonSchemaEnd(configs, cli_src_dir);

            generateSQLEnd(configs, cli_src_dir);
            generateDocumentationEnd(configs, cli_src_dir);


            doGenerateProject(configs, locations, root_dir, cli_lib, l2p_lib, l2p_dir, l2p_src_dir, l2p_test_src_dir, cli_test_src_dir, cli_webjar_dir);

            doGenerateClientAndProject(configs, locations, cli_lib, cli_dir, cli_src_dir);

            System.out.println(objectMapper.writeValueAsString(getInputOutputMaps()));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String addBaseDirIfRelative(String template_builder, String baseDir) {
        if (template_builder.startsWith("/")) return template_builder;
        return baseDir + "/" + template_builder;
    }

    public Set<String> getReferencedSqlTables() {
        return referencedSqlTables;
    }

    private Set<String> referencedSqlTables=new HashSet<>();

    public void findSqlTableReferences(TemplateBindingsSchema config) {
        Map<String, List<Descriptor>> vars=config.getVar();
        for (String var: vars.keySet()) {
            List<Descriptor> descriptors=vars.get(var);
            if (descriptors==null) continue;
            for (Descriptor descriptor: descriptors) {
                if (descriptor instanceof NameDescriptor) {
                    NameDescriptor nd=(NameDescriptor) descriptor;
                    if (nd.getTable()!=null) {
                        referencedSqlTables.add(nd.getTable());
                    }
                }
            }
        }
    }


    public void generateJSonSchemaEnd(TemplatesProjectConfiguration configs, String cli_src_dir) {
        if (configs.jsonschema!=null) compilerJsonSchema.generateJSonSchemaEnd(configs.jsonschema, cli_src_dir +"/../resources");
    }

    public void generateSQLEnd(TemplatesProjectConfiguration configs, String cli_src_dir) {
        if (configs.sqlFile!=null) compilerSQL.generateSQLEnd(configs.sqlFile, cli_src_dir +"/../resources", getReferencedSqlTables());
    }

    public void generateDocumentationEnd(TemplatesProjectConfiguration configs, String cli_webjar_dir) {
        if (configs.documentation!=null) compilerDocumentation.generateDocumentationEnd(configs,cli_webjar_dir);
    }

    public void doGenerateProject(TemplatesProjectConfiguration configs, Locations locations, String root_dir, String cli_lib, String l2p_lib, String l2p_dir, String l2p_src_dir, String l2p_test_src_dir, String cli_test_src_dir, String cli_webjar_dir) {
        final String init_dir= l2p_src_dir + "/" + configs.root_package.replace('.', '/') + "/";
        final String l2p_test_dir= l2p_test_src_dir + "/" + configs.root_package.replace('.', '/') + "/";
        final String cli_test_dir= cli_test_src_dir + "/" + configs.root_package.replace('.', '/') + "/";


        SpecificationFile init=compilerBuilderInit.generateInitializer(configs, locations, init_dir, INIT + DOT_JAVA_EXTENSION);
        init.save();

        SpecificationFile testfile=compilerMaven.generateTestFile_l2p(configs, l2p_test_dir, TESTER_FILE+ DOT_JAVA_EXTENSION);
        testfile.save();

        compilerMaven.makeRootPom(configs, root_dir, cli_lib, l2p_lib);
        compilerMaven.makeSubPom(configs, l2p_dir, l2p_lib, true, false, false, false);

        SpecificationFile testfile2= compilerClientTest.generateTestFile_cli(configs, locations, cli_test_dir, TESTER_FILE+ DOT_JAVA_EXTENSION);
        testfile2.save();

    }

    public void doGenerateClientAndProject(TemplatesProjectConfiguration configs, Locations locations, String cli_lib, String cli_dir, String cli_src_dir) {


        final String openprovenance_dir= cli_src_dir + "/" + CLIENT_PACKAGE.replace('.', '/') + "/";




        compilerMaven.makeSubPom(configs, cli_dir, cli_lib, false, configs.jsweet, true, compilerCommon.getFoundEscape());
        compilerMaven.makeSubPomJweet(configs, cli_dir, cli_lib, configs.jsweet);


        SpecificationFile logger=compilerLogger.generateLogger(configs, locations, LOGGER, getInputOutputMaps());
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

        SpecificationFile templateBuilders=compilerTemplateBuilders.generateTemplateBuilders(configs, locations, TEMPLATE_BUILDERS);
        templateBuilders.save();


        SpecificationFile tableConfigurator=compilerTableConfigurator.generateTableConfigurator(configs, locations);
        tableConfigurator.save();


        SpecificationFile tableConfigurator2=compilerTableConfigurator.generateCompositeTableConfigurator(configs, locations);
        tableConfigurator2.save();

        SpecificationFile beanProcessor=compilerBeanProcessor.generateBeanProcessor(configs, locations, BEAN_PROCESSOR);
        beanProcessor.save();

        String integrator_package = locations.getFilePackage(BeanDirection.OUTPUTS);
        String integrator_dir=locations.convertToDirectory(integrator_package);


        SpecificationFile beanCompleter=compilerBeanCompleter.generateBeanCompleter(configs, locations, BEAN_COMPLETER);
        beanCompleter.save();


        if (configs.integrator) {
            SpecificationFile inputOutputProcessor=compilerInputOutputProcessor.generateInputOutputProcessor(configs, locations, integrator_package, CompilerInputOutputProcessor.ProcessorType.INPUT_OUTPUT,integrator_dir, INPUT_OUTPUT_PROCESSOR + DOT_JAVA_EXTENSION);
            inputOutputProcessor.save();

            SpecificationFile inputProcessor=compilerInputOutputProcessor.generateInputOutputProcessor(configs, locations, integrator_package, CompilerInputOutputProcessor.ProcessorType.INPUT, integrator_dir, INPUT_PROCESSOR + DOT_JAVA_EXTENSION);
            inputProcessor.save();

            SpecificationFile outputProcessor=compilerInputOutputProcessor.generateInputOutputProcessor(configs, locations, integrator_package, CompilerInputOutputProcessor.ProcessorType.OUTPUT, integrator_dir, OUTPUT_PROCESSOR + DOT_JAVA_EXTENSION);
            outputProcessor.save();

            SpecificationFile templateInvoker = compilerTemplateInvoker.generateTemplateInvoker(configs, locations, TEMPLATE_INVOKER);
            templateInvoker.save();

            SpecificationFile beanCompleter2 = compilerBeanCompleter2.generateBeanCompleter2(configs, locations, BEAN_COMPLETER2);
            beanCompleter2.save();

            SpecificationFile beanCompleter3 = compilerBeanCompleter3.generateBeanCompleter3(configs, locations, BEAN_COMPLETER3);
            beanCompleter3.save();

            SpecificationFile beanCompleter2Composite = compilerBeanCompleter2Composite.generateBeanCompleter2Composite(configs, locations, COMPOSITE_BEAN_COMPLETER2);
            beanCompleter2Composite.save();
        }

        SpecificationFile typeConverter=compilerTypeConverter.generateTypeConverter(configs, locations, TYPE_CONVERTER);
        typeConverter.save();

        SpecificationFile beanEnactor=compilerBeanEnactor.generateBeanEnactor(configs, locations, BEAN_ENACTOR);
        beanEnactor.save();

        if (configs.integrator) {
            SpecificationFile beanEnactor2 = compilerBeanEnactor2.generateBeanEnactor2(configs, locations, BEAN_ENACTOR2);
            beanEnactor2.save();

            SpecificationFile beanLocalEnactor2 = compilerBeanLocalEnactor2.generateBeanLocalEnactor2(configs, locations, BEAN_LOCAL_ENACTOR2);
            beanLocalEnactor2.save();
            SpecificationFile beanEnactor2WP = compilerBeanEnactor2WP.generateBeanEnactor2WithPrincipal(configs, locations, BEAN_ENACTOR2_WP);
            beanEnactor2WP.save();

            SpecificationFile beanEnactor2Composite = compilerBeanEnactor2composite.generateBeanEnactor2Composite(configs, locations, BEAN_ENACTOR2_COMPOSITE);
            beanEnactor2Composite.save();

            SpecificationFile beanEnactor2CompositeWP = compilerBeanEnactor2compositeWP.generateBeanEnactor2CompositeWithPrincipal(configs, locations, BEAN_ENACTOR2_COMPOSITE_WP);
            beanEnactor2CompositeWP.save();

            SpecificationFile queryComposer3 = compilerQueryInvoker.generateQueryInvoker(configs, locations, false, QUERY_INVOKER2);
            queryComposer3.save();

            SpecificationFile queryComposerWP= compilerQueryInvokerWithPrincipal.generateQueryInvokerWithPrincipal(configs, locations, QUERY_INVOKER2WP );
            queryComposerWP.save();

            if (configs.sqlFile!=null) {
                SpecificationFile generateSqlIntegration_beanCompleter = compilerSqlIntegration.generateSqlIntegration_BeanCompleter(configs, locations, SQL_BEAN_COMPLETER);
                generateSqlIntegration_beanCompleter.save();

                SpecificationFile generateSqlIntegration_compositeBeanCompleter = compilerSqlIntegration.generateSqlIntegration_CompositeBeanCompleter(configs, locations, SQL_COMPOSITE_BEAN_COMPLETER);
                generateSqlIntegration_compositeBeanCompleter.save();

                SpecificationFile generateSqlIntegration_beanCompleter3 = compilerSqlIntegration.generateSqlIntegration_BeanCompleter3(configs, locations, SQL_BEAN_COMPLETER3);
                generateSqlIntegration_beanCompleter3.save();
                SpecificationFile generateSqlIntegration_beanCompleter4 = compilerSqlIntegration.generateSqlIntegration_BeanCompleter4(configs, locations, SQL_BEAN_COMPLETER4);
                generateSqlIntegration_beanCompleter4.save();

                SpecificationFile generateSqlIntegration_enactorImplementation = compilerSqlIntegration.generateSqlIntegration_EnactorImplementation(configs, locations, SQL_ENACTOR_IMPLEMENTATION);
                generateSqlIntegration_enactorImplementation.save();

                SpecificationFile generateSqlIntegration_enactorImplementation3 = compilerSqlIntegration.generateSqlIntegration_IntegratorEnactorImplementation(configs, locations, SQL_ENACTOR_IMPLEMENTATION3);
                generateSqlIntegration_enactorImplementation3.save();

                SpecificationFile generateSqlIntegration_enactorImplementation4 = compilerSqlIntegration.generateSqlIntegration_IntegratorEnactorImplementation4(configs, locations, SQL_ENACTOR_IMPLEMENTATION4);
                generateSqlIntegration_enactorImplementation4.save();

                SpecificationFile generateSqlIntegration_beanEnactorImplementation = compilerSqlIntegration.generateSqlIntegration_CompositeEnactorImplementation(configs, locations, SQL_COMPOSITE_ENACTOR_IMPLEMENTATION);
                generateSqlIntegration_beanEnactorImplementation.save();

                SpecificationFile generateSqlIntegration_compositeBeanEnactor = compilerSqlIntegration.generateSqlIntegration_CompositeBeanEnactor(configs, locations, SQL_COMPOSITE_BEAN_ENACTOR);
                generateSqlIntegration_compositeBeanEnactor.save();

                SpecificationFile generateSqlIntegration_beanEnactor = compilerSqlIntegration.generateSqlIntegration_BeanEnactor(configs, locations, SQL_BEAN_ENACTOR);
                generateSqlIntegration_beanEnactor.save();

                SpecificationFile generateSqlIntegration_beanEnactor3 = compilerSqlIntegration.generateSqlIntegration_BeanEnactor3(configs, locations, SQL_BEAN_ENACTOR3);
                generateSqlIntegration_beanEnactor3.save();
                SpecificationFile generateSqlIntegration_beanEnactor4 = compilerSqlIntegration.generateSqlIntegration_BeanEnactor4(configs, locations, SQL_BEAN_ENACTOR4);
                generateSqlIntegration_beanEnactor4.save();

                SpecificationFile generateSqlIntegration_compositeEnactorImplementation3 = compilerSqlIntegration.generateSqlIntegration_CompositeEnactorImplementation3(configs, locations, SQL_COMPOSITE_ENACTOR_IMPLEMENTATION3);
                generateSqlIntegration_compositeEnactorImplementation3.save();

                SpecificationFile generateSqlIntegration_compositeEnactorImplementation4 = compilerSqlIntegration.generateSqlIntegration_CompositeEnactorImplementation4(configs, locations, SQL_COMPOSITE_ENACTOR_IMPLEMENTATION4);
                generateSqlIntegration_compositeEnactorImplementation4.save();


                SpecificationFile generatedSqlIntegration_compositeBeanCompleter3 = compilerSqlIntegration.generateSqlIntegration_CompositeBeanCompleter3(configs, locations, SQL_COMPOSITE_BEAN_COMPLETER3);
                generatedSqlIntegration_compositeBeanCompleter3.save();

                SpecificationFile generatedSqlIntegration_compositeBeanCompleter4 = compilerSqlIntegration.generateSqlIntegration_CompositeBeanCompleter4(configs, locations, SQL_COMPOSITE_BEAN_COMPLETER4);
                generatedSqlIntegration_compositeBeanCompleter4.save();

                SpecificationFile generateSqlIntegration_compositeBeanEnactor3 = compilerSqlIntegration.generateSqlIntegration_CompositeBeanEnactor3(configs, locations, SQL_COMPOSITE_BEAN_ENACTOR3);
                generateSqlIntegration_compositeBeanEnactor3.save();

                SpecificationFile generateSqlIntegration_compositeBeanEnactor4 = compilerSqlIntegration.generateSqlIntegration_CompositeBeanEnactor4(configs, locations, SQL_COMPOSITE_BEAN_ENACTOR4);
                generateSqlIntegration_compositeBeanEnactor4.save();

                SpecificationFile generatedSqlIntegration_compositeEnactorConfigurator3 = compilerSqlIntegration.generateSqlIntegration_CompositeEnactorConfigurator3(configs, locations, SQL_COMPOSITE_ENACTOR_CONFIGURATOR3);
                generatedSqlIntegration_compositeEnactorConfigurator3.save();

                SpecificationFile generatedSqlIntegration_compositeEnactorConfigurator4 = compilerSqlIntegration.generateSqlIntegration_CompositeEnactorConfigurator4(configs, locations, SQL_COMPOSITE_ENACTOR_CONFIGURATOR4);
                generatedSqlIntegration_compositeEnactorConfigurator4.save();

                SpecificationFile generatedSqlIntegration_enactorConfigurator3 = compilerSqlIntegration.generateSqlIntegration_EnactorConfigurator3(configs, locations, SQL_ENACTOR_CONFIGURATOR3);
                generatedSqlIntegration_enactorConfigurator3.save();

                SpecificationFile generatedSqlIntegration_enactorConfigurator4 = compilerSqlIntegration.generateSqlIntegration_EnactorConfigurator4(configs, locations, SQL_ENACTOR_CONFIGURATOR4);
                generatedSqlIntegration_enactorConfigurator4.save();
            }
        }

        SpecificationFile queryComposer= compilerQueryInvoker.generateQueryInvoker(configs, locations, true, QUERY_INVOKER );
        queryComposer.save();




        SpecificationFile beanChecker= compilerBeanChecker.generateBeanChecker(configs, locations, BeanDirection.COMMON, null, BEAN_CHECKER);
        beanChecker.save();

        SpecificationFile beanDelegator= compilerDelegator.generateDelegator(configs, locations, DELEGATOR );
        beanDelegator.save();

        if (configs.sqlFile!=null) {
            SpecificationFile configurationSql = compilerConfigurations.generateSqlConfigurator(configs, SQL_CONFIGURATOR, locations, locations.convertToDirectory(locations.getFilePackage(SQL_CONFIGURATOR)), SQL_CONFIGURATOR + DOT_JAVA_EXTENSION);
            configurationSql.save();
        }

        SpecificationFile configurationPropertyOrder= compilerConfigurations.generatePropertyOrderConfigurator(configs,PROPERTY_ORDER_CONFIGURATOR, locations, locations.convertToDirectory(locations.getFilePackage(PROPERTY_ORDER_CONFIGURATOR)), PROPERTY_ORDER_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationPropertyOrder.save();

        SpecificationFile configurationCsv= compilerConfigurations.generateCsvConfigurator(configs,CSV_CONFIGURATOR, locations,locations.convertToDirectory(locations.getFilePackage(CSV_CONFIGURATOR)), CSV_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationCsv.save();

        SpecificationFile configurationBuilder= compilerConfigurations.generateBuilderConfigurator(configs,BUILDER_CONFIGURATOR, locations,locations.convertToDirectory(locations.getFilePackage(BUILDER_CONFIGURATOR)), BUILDER_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationBuilder.save();

        if (configs.sqlFile!=null) {
            SpecificationFile configurationSqlInsert = compilerConfigurations.generateSqlInsertConfigurator(configs, SQL_INSERT_CONFIGURATOR, locations, locations.convertToDirectory(locations.getFilePackage(SQL_INSERT_CONFIGURATOR)), SQL_INSERT_CONFIGURATOR + DOT_JAVA_EXTENSION);
            configurationSqlInsert.save();
        }

        SpecificationFile configurationConverter= compilerConfigurations.generateConverterConfigurator(configs,CONVERTER_CONFIGURATOR, locations, locations.convertToDirectory(locations.getFilePackage(CONVERTER_CONFIGURATOR)), CONVERTER_CONFIGURATOR + DOT_JAVA_EXTENSION);
        configurationConverter.save();

        SpecificationFile record2recordConverter= compilerConfigurations.generateRecord2RecordConfiguration(configs,RECORD_2_RECORD_CONFIGURATOR, locations, locations.convertToDirectory(locations.getFilePackage(RECORD_2_RECORD_CONFIGURATOR)), RECORD_2_RECORD_CONFIGURATOR + DOT_JAVA_EXTENSION);
        record2recordConverter.save();

        if (configs.integrator) {
            SpecificationFile configurationInputPropertyOrder = compilerConfigurations.generateInputsConfigurator(configs, INPUTS_CONFIGURATOR, locations, locations.convertToDirectory(locations.getFilePackage(INPUTS_CONFIGURATOR)), INPUTS_CONFIGURATOR + DOT_JAVA_EXTENSION);
            configurationInputPropertyOrder.save();

            SpecificationFile configurationOutputPropertyOrder = compilerConfigurations.generateOutputsConfigurator(configs, OUTPUTS_CONFIGURATOR, locations, locations.convertToDirectory(locations.getFilePackage(OUTPUTS_CONFIGURATOR)), OUTPUTS_CONFIGURATOR + DOT_JAVA_EXTENSION);
            configurationOutputPropertyOrder.save();

            SpecificationFile configurationEnactor= compilerConfigurations.generateEnactorConfigurator(configs,ENACTOR_CONFIGURATOR, locations, locations.convertToDirectory(locations.getFilePackage(ENACTOR_CONFIGURATOR)), ENACTOR_CONFIGURATOR + DOT_JAVA_EXTENSION);
            configurationEnactor.save();

            SpecificationFile configurationEnactor2= compilerConfigurations.generateEnactorConfigurator2(configs, ENACTOR_CONFIGURATOR2, locations.getFilePackage(BeanDirection.INPUTS), locations, locations.convertToDirectory(locations.getFilePackage(ENACTOR_CONFIGURATOR2)), ENACTOR_CONFIGURATOR2 + DOT_JAVA_EXTENSION);
            configurationEnactor2.save();

            SpecificationFile compositeConfigurationEnactor2= compilerCompositeConfigurations.generateCompositeEnactorConfigurator2(configs, locations, COMPOSITE_ENACTOR_CONFIGURATOR2);
            compositeConfigurationEnactor2.save();
        }

        SpecificationFile compositeConfigurationEnactor= compilerCompositeConfigurations.generateCompositeEnactorConfigurator(configs, locations, COMPOSITE_ENACTOR_CONFIGURATOR);
        compositeConfigurationEnactor.save();

    }



    private void exportMiscFiles(TemplatesProjectConfiguration configs, String cli_dir, String cli_lib) {
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
            InvocationTargetException, FileNotFoundException {
        return compilerUtil.readDocumentFromFile(config.template);
    }

    public void doGenerateServerForEntry(SimpleTemplateCompilerConfig config, TemplatesProjectConfiguration configs, Locations locations, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

        findSqlTableReferences(bindingsSchema);

        Document doc;
        try {
            doc = readDocumentFromFile(config);
            generate(doc, locations, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, config.sharing, null, configs);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | FileNotFoundException e) {
            System.out.println("could not find Interop Framework");
            e.printStackTrace();

            System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema));
        }
    }

    public void doGenerateServerForEntry(CompositeTemplateCompilerConfig compositeTemplateCompilerConfig, SimpleTemplateCompilerConfig config, TemplatesProjectConfiguration configs, Locations locations, String cli_src_dir, String l2p_src_dir, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);

        Document doc;
        try {
            doc = readDocumentFromFile(config);
            generate(doc, locations, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, compositeTemplateCompilerConfig.sharing, compositeTemplateCompilerConfig.consistsOf, configs);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException | FileNotFoundException e) {
            System.out.println("could not find Interop Framework");
            e.printStackTrace();

            System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema));
        }
    }

    public void doGenerateServerForEntry1(Document doc, SimpleTemplateCompilerConfig config, TemplatesProjectConfiguration configs, Locations locations, String cli_src_dir, String l2p_src_dir, String cli_webjar_dir) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);
        TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(config);
        try {
            generate(doc, locations, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", configs.jsonschema, configs.documentation, bindings_schema, bindingsSchema, configs.sqlTables, cli_webjar_dir, config.inComposition, config.sharing, null, configs);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean generate(Document doc, Locations locations, String templateName, String packageName, String cli_src_dir, String l2p_src_dir, String resource, String jsonschema, String documentation, JsonNode bindings_schema,
                            TemplateBindingsSchema bindingsSchema, Map<String, Map<String, String>> sqlTables, String cli_webjar_dir, boolean inComposition, List<String> sharing, String consistsOf, TemplatesProjectConfiguration configs) {
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


            // ignore the composite templates when building join table.
            if (consistsOf==null) buildJoinTable(templateName, bindingsSchema);


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
                Pair<SpecificationFile, Map<Integer, List<Integer>>> tmp = compilerCommon.generateCommonLib(configs, locations, doc, bn, templateName, locations.getFilePackage(BeanDirection.COMMON), bindingsSchema, indexed, BeanKind.SIMPLE, bn + DOT_JAVA_EXTENSION, consistsOf);
                SpecificationFile spec2 = tmp.getLeft();
                val2=spec2.save();
                Map<Integer, List<Integer>> successorTable = tmp.getRight();

                //ensure type declaration code is executed
                SpecificationFile spec5 = compilerTypeManagement.generateTypeDeclaration(configs, locations, doc, bn, templateName, packageName, bindings_schema, bindingsSchema, locations.convertToDirectory(l2p_src_dir,locations.getFileBackendPackage(bnTM)), bnTM + DOT_JAVA_EXTENSION);
                // before propagation generation
                SpecificationFile spec0 = compilerExpansionBuilder.generateBuilderSpecification(configs, locations, doc, bn, templateName, packageName, bindings_schema, bindingsSchema, successorTable, locations.convertToDirectory(l2p_src_dir,locations.getFileBackendPackage(bn)), bn + DOT_JAVA_EXTENSION);
                val0 = spec0.save();

                SpecificationFile spec1 = compilerExpansionBuilder.generateBuilderInterfaceSpecification(configs, locations, doc, bn, templateName, packageName, bindingsSchema, locations.convertToDirectory(l2p_src_dir,locations.getFileBackendPackage(bnI)), bnI + DOT_JAVA_EXTENSION);
                val1= spec1.save();

                SpecificationFile spec2b = compilerCommon.generateSQLInterface(configs, locations, SQL_INTERFACE);
                val2b = spec2b.save();
                val2 = val2 & val2b;


                SpecificationFile spec6 = compilerTypedRecord.generatedTypedRecordConstructor(configs, locations, doc, bn, templateName, packageName, resource, bindings_schema, bindingsSchema, locations.convertToDirectory(l2p_src_dir,locations.getFileBackendPackage(bnTR)), bnTR + DOT_JAVA_EXTENSION);
                val5 = spec5.save();
                val6 = spec6.save();


                SpecificationFile tableConfiguratorWithMap=compilerTableConfiguratorWithMap.generateTableConfigurator(configs, locations, locations.convertToDirectory(l2p_src_dir,locations.getFileBackendPackage(bnI)));
                tableConfiguratorWithMap.save();

                SpecificationFile tableConfiguratorForTypes=compilerTableConfiguratorForTypes.generateTableConfigurator(configs, locations, locations.convertToDirectory(l2p_src_dir,locations.getFileBackendPackage(bnI)));
                tableConfiguratorForTypes.save();

            }


            String integratorPackage = locations.getFilePackage(BeanDirection.OUTPUTS);
            String integratorDir = locations.convertToDirectory(integratorPackage);
            if (!inComposition) {
                SpecificationFile spec3 = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema, BeanKind.SIMPLE, BeanDirection.COMMON, null, null, null, bean + DOT_JAVA_EXTENSION);
                val3 = spec3.save();

                if (configs.integrator) {

                    SpecificationFile spec3b = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema, BeanKind.SIMPLE, BeanDirection.OUTPUTS, null, null, null, outputs + DOT_JAVA_EXTENSION);
                    val3 = val3 & spec3b.save();

                    SpecificationFile spec3c = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema, BeanKind.SIMPLE, BeanDirection.INPUTS, null, null, null, inputs + DOT_JAVA_EXTENSION);
                    val3 = val3 & spec3c.save();

                    SpecificationFile spec7 = compilerIntegrator.generateIntegrator(locations, templateName, integratorPackage, bindingsSchema, LOGGER, BeanKind.SIMPLE, consistsOf, integratorDir, integratorBuilder + DOT_JAVA_EXTENSION);
                    val3 = val3 & spec7.save();

                    SpecificationFile spec4b = compilerProcessor.generateProcessor(locations, templateName, locations.getFilePackage(BeanDirection.INPUTS), bindingsSchema, IN_INTEGRATOR, compilerUtil.integratorNameClass(templateName) + DOT_JAVA_EXTENSION, consistsOf);
                    val4 = val4 & spec4b.save();
                }

                SpecificationFile spec4 = compilerProcessor.generateProcessor(locations, templateName, locations.getFilePackage(BeanDirection.COMMON), bindingsSchema, !IN_INTEGRATOR, compilerUtil.processorNameClass(templateName) + DOT_JAVA_EXTENSION, consistsOf);
                val4 = spec4.save();


            }

            if (!inComposition) {


                compilerJsonSchema.generateJSonSchema(templateName, bindingsSchema, null, "#/definitions/", sharing);

                if (configs.sqlFile!=null) {
                    compilerSQL.generateSQL(templateName, bindingsSchema);
                    compilerSQL.generateSQLInsertFunction(jsonschema + SQL_INTERFACE, templateName, null, cli_src_dir + "/../sql", bindingsSchema, Arrays.asList(), getInputOutputMaps(), configs.search);
                    compilerSQL.generateSQLPrimitiveTables(sqlTables,bindingsSchema);
                }
            }

            if (inComposition) {

                if (consistsOf==null) {
                    throw new NullPointerException("No composed class has been specified for composite " + templateName);
                }


                // generate the composite bean schema
                compilerJsonSchema.generateJSonSchema(templateName+"_1", bindingsSchema, null, "#/definitions/", null);


                if (configs.sqlFile!=null) compilerSQL.generateSQLInsertFunction(jsonschema + SQL_INTERFACE, templateName, consistsOf, cli_src_dir + "/../sql", bindingsSchema, sharing, getInputOutputMaps(), configs.search);

                SimpleTemplateCompilerConfig config = new SimpleTemplateCompilerConfig();
                config.name = compositeBeanNameClass;
                config.package_ = packageName;

                config.bindings = OPENPROVENANCE_COMPOSITE_BEAN_JSON; //"openprovenance:composite-bean.json";
                config.template = "openprovenance:composite-bean.provn";
                TemplateBindingsSchema bindingsSchema2 = compilerUtil.getBindingsSchema(config);
                compilerJsonSchema.generateJSonSchema(templateName, bindingsSchema2, consistsOf, "#/definitions/", sharing);

                if (configs.sqlFile!=null) compilerSQL.generateSQL(templateName, bindingsSchema2);


                // LUC: FIXME: not generating processor fully, with composite subbean
                SpecificationFile spec4 = compilerProcessor.generateProcessor(locations, templateName, locations.getFilePackage(BeanDirection.COMMON), bindingsSchema2, !IN_INTEGRATOR, compilerUtil.processorNameClass(templateName)  + DOT_JAVA_EXTENSION, consistsOf);
                val4 = spec4.save();

                SpecificationFile spec7b = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.COMMON, consistsOf, null, null, compositeBeanNameClass + DOT_JAVA_EXTENSION);
                if (spec7b!=null) {
                    val3 = val3 & spec7b.save();
                }

                if (configs.integrator) {
                    SpecificationFile spec7c = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.OUTPUTS, consistsOf, null, null, outputs + DOT_JAVA_EXTENSION);
                    if (spec7c != null) {
                        val3 = val3 & spec7c.save();
                    }
                    SpecificationFile spec7d = compilerBeanGenerator.generateBean(configs, locations, templateName, bindingsSchema2, BeanKind.COMPOSITE, BeanDirection.INPUTS, consistsOf, sharing, null, inputs + DOT_JAVA_EXTENSION);
                    if (spec7d != null) {
                        val3 = val3 & spec7d.save();
                    }
                }

                Pair<SpecificationFile, Map<Integer, List<Integer>>> tmp = compilerCommon.generateCommonLib(configs, locations, doc, bn, templateName, locations.getFilePackage(BeanDirection.COMMON), bindingsSchema2, indexed, BeanKind.COMPOSITE, bn + DOT_JAVA_EXTENSION,consistsOf);
                if (tmp.getLeft()!=null) {
                    SpecificationFile spec2 = tmp.getLeft();
                    val3 = val3 & spec2.save();
                }


                if (configs.integrator) {
                    SpecificationFile spec7 = compilerIntegrator.generateIntegrator(locations, templateName, integratorPackage, bindingsSchema2, LOGGER, BeanKind.COMPOSITE, consistsOf, integratorDir, integratorBuilder + DOT_JAVA_EXTENSION);
                    val3 = val3 & spec7.save();
                }

            }

            if (!inComposition) {
                final String cli_webjar_html_dir = cli_webjar_dir + "/html";
                new File(cli_webjar_html_dir).mkdirs();
                compilerDocumentation.generateDocumentation(documentation, templateName, cli_webjar_html_dir, bindingsSchema, null);
            } else {
                final String cli_webjar_html_dir = cli_webjar_dir + "/html";
                new File(cli_webjar_html_dir).mkdirs();
                compilerDocumentation.generateDocumentation(documentation, templateName, cli_webjar_html_dir, bindingsSchema, sharing);
            }


            return val1 & val2 & val3 & val4;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    private final Map<String, Map<String,String>> inputMap =new HashMap<>();
    private final Map<String, Map<String,String>> outputMap=new HashMap<>();
    private HashMap<String, Map<String, Map<String, String>>> ioMap=null;


    public Map<String,Map<String, Map<String, String>>> getInputOutputMaps() {
        if (ioMap!=null) return ioMap;
        HashMap<String, Map<String, Map<String, String>>> map = new HashMap<>() {{
            put(INPUT, inputMap);
            put(OUTPUT, outputMap);
        }};
        this.ioMap=map;
        return map;
    }

    public void buildJoinTable(String templateName, TemplateBindingsSchema bindingsSchema) {
            for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
                Optional<String> sqlRelationName=descriptorUtils.getSqlTable(key, bindingsSchema);
                if (descriptorUtils.isOutput(key, bindingsSchema)) {
                    sqlRelationName.ifPresent(rel -> {
                        Map<String, String> map = outputMap.computeIfAbsent(templateName, any -> new HashMap<>());
                        map.put(key, rel);
                    });
                } else if (descriptorUtils.isInput(key, bindingsSchema)) {
                    sqlRelationName.ifPresent(rel -> {
                        Map<String, String> map = inputMap.computeIfAbsent(templateName, any -> new HashMap<>());
                        map.put(key, rel);
                    });

                }


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

    public static void main(String [] args) {
        //System.out.println(Arrays.asList(args));

        if (args.length!=3 && args.length!=2) {
            // display args
            throw new IllegalArgumentException("Usage: ConfigProcessor <config-file> <inputBaseDir> <outputBaseDir>");
        }
        org.openprovenance.prov.vanilla.ProvFactory pf=new org.openprovenance.prov.vanilla.ProvFactory();
        ConfigProcessor cp=new ConfigProcessor(pf);
        String inputBaseDir=args[1];
        String outputBaseDir=(args.length==3)?args[2]:inputBaseDir;
        cp.processTemplateGenerationConfig(args[0],inputBaseDir, outputBaseDir,pf);
    }

}
