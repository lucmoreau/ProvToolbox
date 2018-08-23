package org.openprovenance.prov.template;

import javax.lang.model.element.Modifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class TemplateBuilderGenerator {
    
    final private ProvFactory pFactory;


    public TemplateBuilderGenerator(ProvFactory pFactory) {
        System.out.println("hello");
        this.pFactory=pFactory;
    }
    
    static ProvUtilities u= new ProvUtilities();
    
    final GeneratorUtil gu=new GeneratorUtil();
    
    boolean withMain=true; // TODO need to be updatable via command line
   
    public boolean generate(Document doc, String templateName, String packge, String location, String resource) {
        try {
            String bn=templateNameClass(templateName);
            String destinationDir=location + "/" + packge.replace('.', '/') + "/";
            
            String destination=destinationDir + bn + ".java";
            JavaFile spec=generateBuilderSpecification(doc,bn,templateName,packge, resource);
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
    
    
    public String templateNameClass(String templateName) {
        return gu.capitalize(templateName)+"Builder";
    }

   
    public JavaFile generateBuilderSpecification(Document doc, String name, String templateName, String packge, String resource) {


        Bundle bun=u.getBundle(doc).get(0);
        
        Set<QualifiedName> allVars=new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts=new HashSet<QualifiedName>();
        
        gu.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);
        
        return generate(doc, allVars,allAtts,name, templateName, packge, resource);
        
    }
    
   public JavaFile generate(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource) {
        
        
        Builder builder = gu.generateClassBuilder2(name);
        
        builder.addMethod(gu.generateConstructor2());
        
        builder.addMethod(generateTemplateGenerator(allVars, allAtts, doc));
        
        if (withMain) builder.addMethod(generateMain(allVars, allAtts, name));


        System.out.println(allVars);
        
        TypeSpec bean=builder.build();
        
        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox for template $S",templateName)
                .build();

        return myfile;
    }
   
   public MethodSpec generateTemplateGenerator(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, Document doc) {

       MethodSpec.Builder builder = MethodSpec.methodBuilder("generator")
               .addModifiers(Modifier.PUBLIC)
               .returns(Document.class)
               .addStatement("$T nullqn = null", QualifiedName.class)
               .addStatement("$T attrs=null", StatementGeneratorAction.cl_collectionOfAttributes)
               .addStatement("$T document = pf.newDocument()", Document.class)
 
       ;
       for (QualifiedName q: allVars) {
           builder.addParameter(QualifiedName.class, q.getLocalPart());
       }
       
       StatementGeneratorAction action=new StatementGeneratorAction(pFactory, allVars, allAtts, builder, "document.getStatementOrBundle()");
       for (StatementOrBundle s: doc.getStatementOrBundle()) {
           u.doAction(s, action);
           
       }
       builder.addStatement("new $T().updateNamespaces(document)", ProvUtilities.class);

       builder.addStatement("return document");

       MethodSpec method=builder.build();
       
       return method;
   }

   public MethodSpec generateMain(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name) {

       MethodSpec.Builder builder = MethodSpec.methodBuilder("main")
               .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
               .returns(void.class)
               .addParameter(String[].class, "args")
               .addStatement("System.out.println($S + $S)","testing ", name)
               .addStatement("$T pf=org.openprovenance.prov.xml.ProvFactory.getFactory()",ProvFactory.class)
               .addStatement("$N me=new $N(pf)",name,name);

       ;
       for (QualifiedName q: allVars) {
           builder.addStatement("$T $N=pf.newQualifiedName($S,$S,$S)", QualifiedName.class, q.getLocalPart(), "http://example.org/",q.getLocalPart(), "ex");
       }
       
       String args="";
       boolean first=true;
       for (QualifiedName q: allVars) {
           if (first) {
               first=false;
               args=q.getLocalPart();
           } else {
               args=args + ", " + q.getLocalPart();
           } 
       }
       builder.addStatement("$T document=me.generator(" + args + ", pf.newQualifiedName($S,$S,$S)" + ")", Document.class, "http://example.org/","bun123","ex");
       
       builder.addStatement("new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.InteropFramework.ProvFormat.PROVN,document)");
            

       MethodSpec method=builder.build();
       
       return method;
   }




}
