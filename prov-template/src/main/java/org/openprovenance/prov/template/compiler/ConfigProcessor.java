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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigProcessor {
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

          
            cp.makeRootPom(configs, root_dir, cli_lib, l2p_lib);
            cp.makeSubPom(configs, cli_dir,  cli_lib, false);
            cp.makeSubPom(configs, l2p_dir,  l2p_lib, true);
          for (TemplateCompilerConfig config: configs.templates) {
                System.out.println(config.toString());

                cp.doProcessEntry(config,configs,cli_src_dir,l2p_src_dir,pFactory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public void doProcessEntry (TemplateCompilerConfig config, TemplatesCompilerConfig configs, String cli_src_dir, String l2p_src_dir, ProvFactory pFactory) {
        JsonNode bindings_schema=null;
        if (config.bindings != null) {
            try {
                bindings_schema = objectMapper.readTree(new File(config.bindings));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TemplateCompiler tbg=new TemplateCompiler(pFactory);
        
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
        Object interop=getInteropFramework();
        Method method = interop.getClass().getMethod("readDocumentFromFile", String.class);
        Document doc=(Document)method.invoke(interop,config.template);
        return doc;
    }


    public Object getInteropFramework() throws ClassNotFoundException, NoSuchMethodException,
                                      SecurityException, InstantiationException,
                                      IllegalAccessException, IllegalArgumentException,
                                      InvocationTargetException {
        Class<?> clazz = Class.forName("org.openprovenance.prov.interop.InteropFramework");
        Constructor<?> ctor = clazz.getConstructor();
        return ctor.newInstance(new Object[] { }); 
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

}
