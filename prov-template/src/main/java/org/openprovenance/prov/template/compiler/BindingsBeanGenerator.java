package org.openprovenance.prov.template.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.expander.Bindings;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class BindingsBeanGenerator {
    
    final private ProvFactory pFactory;
   


    public BindingsBeanGenerator(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }
    
    static ProvUtilities u= new ProvUtilities();
    
    final GeneratorUtil gu=new GeneratorUtil();

    
    
    public boolean generate(Document doc, String templateName, String packge, String location, String resource) {
        try {
            String bn=beanName(templateName);
            String destinationDir=location + "/" + packge.replace('.', '/') + "/";
            
            String destination=destinationDir + bn + ".java";
            JavaFile spec=generateSpecification(doc,bn,templateName,packge, resource);
            PrintWriter out;
            try {
                File dir=new File(destinationDir);
                if (!dir.exists() && !dir.mkdirs()) {
                    System.err.println("failed to create directory " + destinationDir);
                    return false;
                };
                out = new PrintWriter(destination);
                out.print(spec);
                out.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }


    public String beanName(String templateName) {
        return gu.capitalize(templateName)+"BindingsBean";
    }


    
    public JavaFile generateSpecification(Document doc, String name, String templateName, String packge, String resource) {


        Bundle bun=u.getBundle(doc).get(0);
        
        Set<QualifiedName> allVars=new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts=new HashSet<QualifiedName>();
        
        gu.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);
        
        return generate(allVars,allAtts,name, templateName, packge, resource);
        
    }


    
    public JavaFile generate(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource) {
        
        
        Builder builder = gu.generateClassBuilder(name);
        
        builder.addMethod(gu.generateConstructor());
        
        for (QualifiedName q: allVars) {
            builder.addMethod(generateVarMutator(q));
        }
        
        for (QualifiedName q: allAtts) {
            builder.addMethod(generateAttMutator(q,QualifiedName.class));
            builder.addMethod(generateAttMutator(q,String.class));
        }
        
        builder.addMethod(generateBindingsGetter());
        
        builder.addMethod(generateTemplateResourceGetter(resource));
        
        TypeSpec bean=builder.build();
        
        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox for template $S",templateName)
                .build();

        return myfile;
    }


    
    public MethodSpec generateVarMutator(QualifiedName v) {
        final String local=v.getLocalPart();
        final String localCamel= gu.camelcase(local);
        MethodSpec method = MethodSpec.methodBuilder("add" + localCamel)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(QualifiedName.class, "arg")
                .addStatement("bindings.addVariable($S,arg)",local)
                .build();
        
        return method;
    }
    
    public MethodSpec generateAttMutator(QualifiedName v, Class typ) {
        final String local=v.getLocalPart();
        final String localCamel= gu.camelcase(local);
        MethodSpec method = MethodSpec.methodBuilder("add" + localCamel)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(typ, "arg")
                .addStatement("bindings.addAttribute($S,arg)",local)
                .build();
        
        return method;
    }
    
    public MethodSpec generateBindingsGetter() {
        MethodSpec method = MethodSpec.methodBuilder("getBindings")
                .addModifiers(Modifier.PUBLIC)
                .returns(Bindings.class)
                .addStatement("return bindings")
                .build();
        
        return method;
    }
    
    public MethodSpec generateTemplateResourceGetter(String resource) {
        MethodSpec method = MethodSpec.methodBuilder("getTemplate")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S",resource)
                .build();
        
        return method;
    }
    


}
