package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;
import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.Runner;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Iterator;

public class ConfigProcessor {
    private static final String PREFIX_LOG_VAR = "___";
    private static final String GET_NODES_METHOD = "getNodes";
    private static final String BUILDER_INTERFACE = "Builder";
    private static final String INIT = "Init";
    public static final String BUILDERS = "builders";
    public static final String PF = "pf";
    public static final String GET_SUCCESSOR_METHOD = "getSuccessors";
    public static final String GET_NAME = "getName";
    private static final String LOGGER_INTERFACE = "LoggerInterface";
    private static final String TESTER_FILE = "ExampleTest";
    private static final String GET_BUILDERS_METHOD = "getBuilders";
    public static final String CLIENT_PACKAGE = "org.openprovenance.prov.client";
    static ObjectMapper objectMapper = new ObjectMapper();

    public static int processTemplateGenerationConfig(String template_builder, ProvFactory pFactory) {
        TemplatesCompilerConfig configs;
        ConfigProcessor cp=new ConfigProcessor();
                
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

            TemplateCompiler tc=new TemplateCompiler(pFactory);
          
            cp.makeRootPom(configs, root_dir, cli_lib, l2p_lib);
            cp.makeSubPom(configs, cli_dir,  cli_lib, false);
            cp.makeSubPom(configs, l2p_dir,  l2p_lib, true);
            for (TemplateCompilerConfig config: configs.templates) {
                System.out.println(config.toString());

                cp.doProcessEntry(tc,config,configs,cli_src_dir,l2p_src_dir,pFactory);
            }
            
            final String init_dir=l2p_src_dir+ "/" + configs.init_package.replace('.', '/') + "/";;
            final String test_dir=l2p_test_src_dir+ "/" + configs.init_package.replace('.', '/') + "/";;
   
            JavaFile init=cp.generateInitializer(tc, configs);
            tc.saveToFile(init_dir, init_dir+INIT + ".java", init);
            
            
            final String logger_dir=cli_src_dir+ "/" + configs.logger_package.replace('.', '/') + "/";;
            final String openprovenance_dir=cli_src_dir+ "/" + CLIENT_PACKAGE.replace('.', '/') + "/";;

            JavaFile logger=cp.generateLogger(tc, configs);
            tc.saveToFile(logger_dir, logger_dir+configs.logger+ ".java", logger);

            JavaFile intface=cp.generateBuilderInterface(tc, configs);
            tc.saveToFile(openprovenance_dir, openprovenance_dir+BUILDER_INTERFACE+ ".java", intface);

            JavaFile intface2=cp.generateLoggerInterface(tc, configs);
            tc.saveToFile(openprovenance_dir, openprovenance_dir+LOGGER_INTERFACE+ ".java", intface2);
 
            JavaFile testfile=cp.generateTestFile(tc, configs);
            tc.saveToFile(test_dir, test_dir+TESTER_FILE+ ".java", testfile);

            cp.generateScript(configs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public JavaFile generateTestFile(TemplateCompiler tc, TemplatesCompilerConfig configs) {

		 Builder builder = cu.generateClassInitExtends(TESTER_FILE,"junit.framework","TestCase");
		 
	     MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("testMain")
	               .addModifiers(Modifier.PUBLIC)
	                .addException(Exception.class)
	               .returns(void.class)
	               .addStatement("$T pf=org.openprovenance.prov.interop.InteropFramework.getDefaultFactory()",ProvFactory.class)
	       ;
	     
	     for (TemplateCompilerConfig template: configs.templates) {
	    	 String bn=tc.templateNameClass(template.name);
	    	 mbuilder.addStatement("System.setOut(new java.io.PrintStream(\"target/" + template.name + ".provn\"))");
	    	 mbuilder.addStatement("$T.main(null)", ClassName.get(template.package_, bn));
	     }
	     
	     MethodSpec method=mbuilder.build();
	     
	     builder.addMethod(method);
	     
	     TypeSpec theInitializer=builder.build();
		 
		 JavaFile myfile = JavaFile.builder(configs.init_package, theInitializer)
	                .addFileComment("Generated Automatically by ProvToolbox for templates config $S",configs.name)
	                .build();

	     return myfile;	     
	}


	public void doProcessEntry ( TemplateCompiler tbg, TemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory) {
        JsonNode bindings_schema = get_bindings_schema(config);
        
        Document doc;
        try {
            doc = readDocumentFromFile(config);
            tbg.generate(doc, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource",bindings_schema);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
           System.out.println("could not find Interop Framework");
           e.printStackTrace();

           System.out.println(Arrays.asList("doc", config.name, config.package_, cli_src_dir, l2p_src_dir, "resource",bindings_schema));
        }
     
     
        
    }


    public JsonNode get_bindings_schema(TemplateCompilerConfig config) {
        JsonNode bindings_schema=null;
        if (config.bindings != null) {
            try {
                bindings_schema = objectMapper.readTree(new File(config.bindings));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bindings_schema;
    }


    public Document readDocumentFromFile(TemplateCompilerConfig config) throws ClassNotFoundException,
                                                                        NoSuchMethodException,
                                                                        SecurityException,
                                                                        InstantiationException,
                                                                        IllegalAccessException,
                                                                        IllegalArgumentException,
                                                                        InvocationTargetException {
        return cu.readDocumentFromFile(config.template);
    }


                           
    
    public boolean makeRootPom(TemplatesCompilerConfig configs, String root_dir, String cli_lib, String l2p_lib) {
        Model model = new Model();
        model.setGroupId(configs.group);
        model.setArtifactId(configs.name);
        model.setVersion(configs.version);
        model.setName(configs.name);
        model.setPackaging("pom");
        model.setDescription(configs.description);
        model.setModelVersion("4.0.0");
        model.addModule(cli_lib);
        model.addModule(l2p_lib);

        addCompilerDeclaration(model);


        try {
            new MavenXpp3Writer().write(new FileWriter(root_dir + "/pom.xml"),model);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
    }
    
    public boolean makeSubPom(TemplatesCompilerConfig configs, String dir, String name, boolean dependencies) {
        Model model = new Model();
        model.setArtifactId(name);
        model.setName(name);
        model.setPackaging("jar");
        model.setDescription(configs.description + " (" + name + ")");
        Parent parent=new Parent();
        parent.setArtifactId(configs.name);
        parent.setGroupId(configs.group);
        parent.setVersion(configs.version);
        model.setParent(parent);
        model.setModelVersion("4.0.0");
        
        if (dependencies) {

            addProvDependency("prov-model", model);
            addProvDependency("prov-n", model);
            //addProvDependency("prov-json", model);
            addProvDependency("prov-template-compiler", model);
            addProvDependency("prov-interop-light", model);
            addJunitDependency(model);

        }


        try {
            new MavenXpp3Writer().write(new FileWriter(dir + "/pom.xml"),model);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
    }



    public String getProvPackageId() {
        return "org.openprovenance.prov";
    }
    
 
    public void addProvDependency(String artifact, Model model) {
        Dependency dep=new Dependency();
        dep.setArtifactId(artifact);
        dep.setGroupId(getProvPackageId());
        dep.setVersion(getProvVersion()); 
        model.addDependency(dep);
    }  
    
    public void addJunitDependency(Model model) {
        Dependency dep=new Dependency();
        dep.setArtifactId("junit");
        dep.setGroupId("junit");
        dep.setVersion("4.11"); 
        dep.setScope("test");
        model.addDependency(dep);
    }

        /*
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>3.1.1</version>
                <configuration>
                    <source>1.8</source>
                </configuration>
            </plugin>

         */


    public void addCompilerDeclaration(Model model) {
        Plugin plugin1=new Plugin();
        plugin1.setGroupId("org.apache.maven.plugins");
        plugin1.setArtifactId("maven-compiler-plugin");
        plugin1.setVersion("3.8.1");

        StringBuilder configString1 = new StringBuilder()
                .append("<configuration>")
                .append("<source>1.8</source>")
                .append("<target>1.8</target>")
                .append("</configuration>");

        Plugin plugin2=new Plugin();
        plugin2.setGroupId("org.apache.maven.plugins");
        plugin2.setArtifactId("maven-javadoc-plugin");
        plugin2.setVersion("3.1.1");

        StringBuilder configString2 = new StringBuilder()
                .append("<configuration>")
                .append("<source>1.8</source>")
                .append("</configuration>");

        Xpp3Dom config1 = null;
        Xpp3Dom config2 = null;
        try {
            config1 = Xpp3DomBuilder.build(new StringReader(configString1.toString()));
            config2 = Xpp3DomBuilder.build(new StringReader(configString2.toString()));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating config for enforcer plugin", ex);
        }

        PluginManagement pm=new PluginManagement();
        pm.addPlugin(plugin1);
        plugin1.setConfiguration(config1);
        pm.addPlugin(plugin2);
        plugin2.setConfiguration(config2);

        Build build=new Build();
        model.setBuild(build);

        build.setPluginManagement(pm);

    }
    
    public String getProvVersion() {
        return Configuration.toolboxVersion;
    }
    
    final CompilerUtil cu=new CompilerUtil();

    
    private JavaFile generateInitializer(TemplateCompiler tc, TemplatesCompilerConfig configs) {
        
        int size=configs.templates.length;
        
  
    
        Builder builder = cu.generateClassInit(INIT);
        
        builder.addField(String[].class,BUILDERS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        builder.addField(org.openprovenance.prov.model.ProvFactory.class,PF, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        com.squareup.javapoet.CodeBlock.Builder block = CodeBlock.builder();
        block.addStatement("$N = new String[$L]",BUILDERS, size);
        int count=0;
        for (TemplateCompilerConfig config: configs.templates) {
            block.addStatement("$N[$L]=$S",BUILDERS,count,config.package_+"."+tc.templateNameClass(config.name));
            count++;
        }    
        block.addStatement("pf=$T.getFactory()", org.openprovenance.prov.vanilla.ProvFactory.class);

        
        
        builder.addStaticBlock(block.build());
        
        
        builder.addMethod(MethodSpec.methodBuilder("init")
                          .addStatement("return $T.registerBuilders($N,$N)",FileBuilder.class,BUILDERS,PF)
                          .returns(boolean.class)
                          .addModifiers(Modifier.STATIC)
                          .addModifiers(Modifier.PUBLIC)
                          .build());
        
        builder.addMethod(generateMain());
        
        
        TypeSpec theInitializer=builder.build();
        
        

        JavaFile myfile = JavaFile.builder(configs.init_package, theInitializer)
                .addFileComment("Generated Automatically by ProvToolbox for templates config $S",configs.name)
                .build();

        return myfile;
    }
    
    public MethodSpec generateMain() {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addException(Exception.class)
                .addStatement("init()")
                .addStatement("$T.main($N)",Runner.class,"args")
        ;
        
        return builder.build();
  
    }
    

    
    private JavaFile generateLogger(TemplateCompiler tc, TemplatesCompilerConfig configs) {
        
        Builder builder = cu.generateClassInit(configs.logger);
        builder.addSuperinterface(ClassName.get(CLIENT_PACKAGE,"LoggerInterface"));

        String packge=null;
        for (TemplateCompilerConfig config: configs.templates) {
            final String templateNameClass = tc.templateNameClass(config.name);
            packge = config.package_+".client";
            final ClassName className = ClassName.get(packge,templateNameClass);
            FieldSpec fspec = FieldSpec.builder(className, PREFIX_LOG_VAR + config.name)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("new $T()", className)
                    .build();
            
            builder.addField(fspec);
        }   
        
        String names="";
        boolean first=true;
        for (TemplateCompilerConfig config: configs.templates) {
            if (first) {
                first=false;
            } else {
                names=names+", ";
            }
            names=names + PREFIX_LOG_VAR + config.name;
        }   
        ClassName cln=ClassName.get(CLIENT_PACKAGE,"Builder");
        ArrayTypeName builderArrayType = ArrayTypeName.of(cln);
        FieldSpec fspec = FieldSpec.builder(builderArrayType, "__builders")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("new $T[] {" + names + "}" , cln)
                .build();
        
        builder.addField(fspec);  
        
        builder.addMethod(generateGetBuilderMethod(builderArrayType));

        
        for (TemplateCompilerConfig config: configs.templates) {
            builder.addMethod(generateStaticLogMethod(config,tc));
        }
   
        
        TypeSpec theLogger=builder.build();
        
        JavaFile myfile = JavaFile.builder(configs.logger_package, theLogger )
                .addFileComment("Generated Automatically by ProvToolbox for templates config $S",configs.name)
                .build();
        return myfile;
    }

    private JavaFile generateBuilderInterface(TemplateCompiler tc, TemplatesCompilerConfig configs) {

        Builder builder = cu.generateInterfaceInit(BUILDER_INTERFACE);



        MethodSpec.Builder builder2=MethodSpec.methodBuilder(GET_NODES_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int[].class);
        builder.addMethod(builder2.build());


        MethodSpec.Builder builder3=MethodSpec.methodBuilder(GET_SUCCESSOR_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TemplateCompiler.hashmapType);
        builder.addMethod(builder3.build());

        
        MethodSpec.Builder builder4=MethodSpec.methodBuilder(GET_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class);
        builder.addMethod(builder4.build());

             

        TypeSpec theInterface=builder.build();

        JavaFile myfile = JavaFile.builder(CLIENT_PACKAGE, theInterface )
                .addFileComment("Generated Automatically by ProvToolbox for templates config $S",configs.name)
                .build();
        return myfile;
    }

    private JavaFile generateLoggerInterface(TemplateCompiler tc, TemplatesCompilerConfig configs) {

        Builder builder = cu.generateInterfaceInit(LOGGER_INTERFACE);

        ClassName cln=ClassName.get(CLIENT_PACKAGE,"Builder");
        ArrayTypeName builderArrayType = ArrayTypeName.of(cln);

        MethodSpec.Builder builder2=MethodSpec.methodBuilder(GET_BUILDERS_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(builderArrayType);
        builder.addMethod(builder2.build());

             

        TypeSpec theInterface=builder.build();

        JavaFile myfile = JavaFile.builder(CLIENT_PACKAGE, theInterface )
                .addFileComment("Generated Automatically by ProvToolbox for templates config $S",configs.name)
                .build();
        return myfile;
    }   
    
    public MethodSpec generateStaticLogMethod(TemplateCompilerConfig config, TemplateCompiler tc) {
        final String loggerName = tc.loggerName(config.name);

        MethodSpec.Builder builder = MethodSpec.methodBuilder(loggerName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.STATIC)
                .returns(String.class)
        ;
        
        JsonNode bindings_schema = get_bindings_schema(config);
    
        JsonNode the_var=bindings_schema.get("var");
        JsonNode the_context=bindings_schema.get("context");
        JsonNode the_documentation=bindings_schema.get("@documentation");
        tc.generateSpecializedParameters(builder, the_var);
        tc.generateSpecializedParametersJavadoc(builder, the_var, the_documentation);
        
        
        int count=1;
        Iterator<String> iter=the_var.fieldNames();
        String args="";
        while(iter.hasNext()){
            String key=iter.next();
            if (count > 1) args=args + ", ";
            args=args+key;
            count++;
        }
        builder.addStatement("return $N." + loggerName + "(" + args + ")",PREFIX_LOG_VAR+ config.name);  

        
        return builder.build();
  
    }

    public MethodSpec generateGetBuilderMethod(ArrayTypeName builderArrayType) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getBuilders")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderArrayType)
        ;
        
        
        builder.addStatement("return __builders");  

        
        return builder.build();
  
    }  
    public void generateScript(TemplatesCompilerConfig configs) {
        new File(configs.script_dir).mkdirs(); 
        try {
            final String path = configs.script_dir+"/"+configs.script;
            PrintStream os=new PrintStream(path);
            InputStream in=this.getClass().getResourceAsStream("script.sh");

            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            String line=reader.readLine();
            while (line!=null) {
                line=line.replace("${SCRIPT}",configs.script);
                line=line.replace("${VERSION}",configs.version);
                line=line.replace("${NAME}",configs.name);
                line=line.replace("${PACKAGE}",configs.init_package.replace(".","/"));
                line=line.replace("${INIT}",configs.init_package+"." + INIT);
                os.println(line);
                line=reader.readLine();
            }
            os.close();
            in.close();
            Files.setPosixFilePermissions(new File(path).toPath(), PosixFilePermissions.fromString("rwxr-xr-x"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    

}
