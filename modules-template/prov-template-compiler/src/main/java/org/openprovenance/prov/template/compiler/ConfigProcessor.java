package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public class ConfigProcessor {
    public static final String PREFIX_LOG_VAR = "___";
    public static final String GET_NODES_METHOD = "getNodes";
    public static final String BUILDER_INTERFACE = "Builder";
    public static final String INIT = "Init";
    public static final String BUILDERS = "builders";
    public static final String PF = "pf";
    public static final String GET_SUCCESSOR_METHOD = "getSuccessors";
    public static final String GET_NAME = "getName";
    public static final String LOGGER_INTERFACE = "LoggerInterface";
    public static final String TESTER_FILE = "ExampleTest";
    public static final String GET_BUILDERS_METHOD = "getBuilders";
    public static final String CLIENT_PACKAGE = "org.openprovenance.prov.client";
    private final ProvFactory pFactory;

    boolean withMain=true; // TODO need to be updatable via command line

    public static final ObjectMapper objectMapper = new ObjectMapper();

    private final CompilerUtil compilerUtil     = new CompilerUtil();
    private final CompilerLogger compilerLogger = new CompilerLogger();
    private final CompilerMaven compilerMaven   = new CompilerMaven(this);
    private final CompilerClient compilerClient;
    private final CompilerBuilder compilerBuilder;
    private final CompilerBuilderInit compilerBuilderInit;
    private final CompilerSimpleBean compilerSimpleBean;
    private final CompilerContinuation compilerContinuation;

    public ConfigProcessor(ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.compilerClient= new CompilerClient(pFactory);
        this.compilerBuilder= new CompilerBuilder(withMain,compilerClient,pFactory);
        this.compilerBuilderInit= new CompilerBuilderInit(pFactory);
        this.compilerSimpleBean =new CompilerSimpleBean(pFactory);
        this.compilerContinuation =new CompilerContinuation(pFactory);
    }

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
            new File(l2p_src_dir).mkdirs(); 
            new File(cli_src_dir).mkdirs(); 
            new File(l2p_test_src_dir).mkdirs(); 

            //TemplateCompiler tc=new TemplateCompiler(pFactory);

            compilerMaven.makeRootPom(configs, root_dir, cli_lib, l2p_lib);
            compilerMaven.makeSubPom(configs, cli_dir,  cli_lib, false);
            compilerMaven.makeSubPom(configs, l2p_dir,  l2p_lib, true);


            for (TemplateCompilerConfig config: configs.templates) {
                System.out.println(config.toString());

                doProcessEntry(config,configs,cli_src_dir,l2p_src_dir,pFactory);
            }
            
            final String init_dir=l2p_src_dir+ "/" + configs.init_package.replace('.', '/') + "/";;
            final String test_dir=l2p_test_src_dir+ "/" + configs.init_package.replace('.', '/') + "/";;
   
            JavaFile init=compilerBuilderInit.generateInitializer(configs);
            compilerUtil.saveToFile(init_dir, init_dir+INIT + ".java", init);
            
            
            final String logger_dir=cli_src_dir+ "/" + configs.logger_package.replace('.', '/') + "/";;
            final String openprovenance_dir=cli_src_dir+ "/" + CLIENT_PACKAGE.replace('.', '/') + "/";;

            JavaFile logger=compilerLogger.generateLogger(configs);
            compilerUtil.saveToFile(logger_dir, logger_dir+configs.logger+ ".java", logger);

            JavaFile intface=compilerLogger.generateBuilderInterface(configs);
            compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir+BUILDER_INTERFACE+ ".java", intface);

            JavaFile intface2=compilerLogger.generateLoggerInterface(configs);
            compilerUtil.saveToFile(openprovenance_dir, openprovenance_dir+LOGGER_INTERFACE+ ".java", intface2);
 
            JavaFile testfile=compilerMaven.generateTestFile(configs);
            compilerUtil.saveToFile(test_dir, test_dir+TESTER_FILE+ ".java", testfile);

            compilerMaven.generateScript(configs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public JsonNode readTree(File file) throws IOException {
        return objectMapper.readTree(file);
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



    public void doProcessEntry (TemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory) {
        JsonNode bindings_schema = compilerUtil.get_bindings_schema(config);

        Document doc;
        try {
            doc = readDocumentFromFile(config);
            generate(doc, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            System.out.println("could not find Interop Framework");
            e.printStackTrace();

            System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource", bindings_schema));
        }
    }


    public boolean generate(Document doc, String templateName, String packge, String cli_src_dir, String l2p_src_dir, String resource,  JsonNode bindings_schema) {
        try {
            String bn= compilerUtil.templateNameClass(templateName);
            String bean=compilerUtil.beanNameClass(templateName);
            String continuation=compilerUtil.continuationNameClass(templateName);

            String destinationDir=l2p_src_dir + "/" + packge.replace('.', '/') + "/";
            String destinationDir2=cli_src_dir + "/" + packge.replace('.', '/') + "/" + "client" + "/";

            String destination=destinationDir + bn + ".java";
            String destination2=destinationDir2 + bn + ".java";
            String destination3=destinationDir2 + bean + ".java";
            String destination4=destinationDir2 + continuation + ".java";

            JavaFile spec= compilerBuilder.generateBuilderSpecification(doc, bn, templateName, packge, resource, bindings_schema);
            boolean val1=compilerUtil.saveToFile(destinationDir, destination, spec);

            JavaFile spec2=compilerClient.generateClientLib(doc,bn,templateName,packge+ ".client", resource, bindings_schema);
            boolean val2=compilerUtil.saveToFile(destinationDir2, destination2, spec2);

            JavaFile spec3= compilerSimpleBean.generateSimpleBean(doc,bean,templateName,packge+ ".client", resource, bindings_schema);
            boolean val3=compilerUtil.saveToFile(destinationDir2, destination3, spec3);

            JavaFile spec4=compilerContinuation.generateContinuation(doc,continuation,templateName,packge+ ".client", resource, bindings_schema);
            boolean val4=compilerUtil.saveToFile(destinationDir2, destination4, spec4);




            return val1 & val2 & val3 & val4;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }








}
