package org.openprovenance.prov.template;

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
import org.openprovenance.prov.model.Statement;

import com.google.common.base.CaseFormat;
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

    
    
    public boolean generate(Document doc, String templateName, String packge, String location) {
        try {
            String bn=beanName(templateName);
            String destination=location + "/" + bn + ".java";
            JavaFile spec=generateSpecification(doc,bn,templateName,packge);
            System.out.println(spec);
            PrintWriter out;
            try {
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
        return capitalize(templateName)+"BindingsBean";
    }


    public String capitalize(String templateName) {
        return templateName.substring(0, 1).toUpperCase()+templateName.substring(1);
    }

    
    public JavaFile generateSpecification(Document doc, String name, String templateName, String packge) {


        Bundle bun=u.getBundle(doc).get(0);
        
        Set<QualifiedName> allVars=new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts=new HashSet<QualifiedName>();
        
        for (Statement statement: bun.getStatement()) {
            Set<QualifiedName> vars=ExpandUtil.freeVariables(statement);
            allVars.addAll(vars);
            Set<QualifiedName> vars2=ExpandUtil.freeAttributeVariables(statement, pFactory);
            allAtts.addAll(vars2);
        }
        
        return generate(allVars,allAtts,name, templateName, packge);
        
    }
    
    public JavaFile generate(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge) {
        
        
        Builder builder = generateClassBuilder(name);
        
        builder.addMethod(generateConstructor());
        
        for (QualifiedName q: allVars) {
            builder.addMethod(generateVarMutator(q));
        }
        
        for (QualifiedName q: allAtts) {
            builder.addMethod(generateAttMutator(q,QualifiedName.class));
            builder.addMethod(generateAttMutator(q,String.class));
        }
        
        builder.addMethod(generateBindingsGetter());
        
        TypeSpec bean=builder.build();
        
        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox for template $S",templateName)
                .build();


        System.out.println(bean);
        return myfile;
    }

    public Builder generateClassBuilder(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(BindingsBean.class)
                .addField(Bindings.class, "bindings", Modifier.PRIVATE, Modifier.FINAL)
                .addField(ProvFactory.class, "pf", Modifier.PRIVATE, Modifier.FINAL);
    }

    public MethodSpec generateConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProvFactory.class, "pf")
                .addStatement("this.$N = $N", "pf", "pf")
                .addStatement("this.bindings = new $T($N)", Bindings.class, "pf")
                .build();
    }
    
    public String camelcase(String s) { 
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s); 
    }
    
    public MethodSpec generateVarMutator(QualifiedName v) {
        final String local=v.getLocalPart();
        final String localCamel= camelcase(local);
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
        final String localCamel= camelcase(local);
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
    


}
