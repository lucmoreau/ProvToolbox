package org.openprovenance.prov.template.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.Runner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import javax.lang.model.element.Modifier;

public class ConfigProcessor {
    public static final String BUILDERS = "builders";
    public static final String PF = "pf";
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
            final String cli_src_dir=cli_dir+"/src/main/java";
            new File(l2p_src_dir).mkdirs(); 
            new File(cli_src_dir).mkdirs(); 

            TemplateCompiler tbg=new TemplateCompiler(pFactory);
          
            cp.makeRootPom(configs, root_dir, cli_lib, l2p_lib);
            cp.makeSubPom(configs, cli_dir,  cli_lib, false);
            cp.makeSubPom(configs, l2p_dir,  l2p_lib, true);
            for (TemplateCompilerConfig config: configs.templates) {
                System.out.println(config.toString());

                cp.doProcessEntry(tbg,config,configs,cli_src_dir,l2p_src_dir,pFactory);
            }
            
            final String init_dir=l2p_src_dir+ "/" + configs.init_package.replace('.', '/') + "/";;
   
            JavaFile init=cp.generateInitializer(tbg, configs);
            tbg.saveToFile(init_dir, init_dir+"Init.java", init);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public void doProcessEntry ( TemplateCompiler tbg, TemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory) {
        JsonNode bindings_schema=null;
        if (config.bindings != null) {
            try {
                bindings_schema = objectMapper.readTree(new File(config.bindings));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        Document doc;
        try {
            doc = readDocumentFromFile(config);
            tbg.generate(doc, config.name, config.package_, cli_src_dir, l2p_src_dir, "resource",bindings_schema);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
     
     
        
    }


    public Document readDocumentFromFile(TemplateCompilerConfig config) throws ClassNotFoundException,
                                                                        NoSuchMethodException,
                                                                        SecurityException,
                                                                        InstantiationException,
                                                                        IllegalAccessException,
                                                                        IllegalArgumentException,
                                                                        InvocationTargetException {
        Object interop=gu.getInteropFramework();
        return gu.readDocumentFromFile(config.template);
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
            addProvDependency("prov-template", model);
            addProvDependency("prov-interop", model);

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
    
    public String getProvVersion() {
        return "0.7.4-SNAPSHOT"; // need to get actual version
    }
    
    final CompilerUtil gu=new CompilerUtil();

    
    private JavaFile generateInitializer(TemplateCompiler tbg, TemplatesCompilerConfig configs) {
        
        int size=configs.templates.length;
        
  
    
        Builder builder = gu.generateClassInit("Init");
        
        builder.addField(String[].class,BUILDERS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        builder.addField(org.openprovenance.prov.model.ProvFactory.class,PF, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        com.squareup.javapoet.CodeBlock.Builder block = CodeBlock.builder();
        block.addStatement("$N = new String[$L]",BUILDERS, size);
        int count=0;
        for (TemplateCompilerConfig config: configs.templates) {
            block.addStatement("$N[$L]=$S",BUILDERS,count,config.package_+"."+tbg.templateNameClass(config.name));
            count++;
        }    
        block.addStatement("pf=$T.getFactory()", org.openprovenance.prov.xml.ProvFactory.class);

        
        
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

}
