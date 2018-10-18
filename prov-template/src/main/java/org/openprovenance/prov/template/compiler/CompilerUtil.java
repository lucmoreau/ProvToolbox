package org.openprovenance.prov.template.compiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.element.Modifier;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.template.expander.BindingsBean;
import org.openprovenance.prov.template.expander.ExpandUtil;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;


public class CompilerUtil {
    

    public String capitalize(String templateName) {
        return templateName.substring(0, 1).toUpperCase()+templateName.substring(1);
    }

    public void extractVariablesAndAttributes(Bundle bundle,
                                              Set<QualifiedName> allVars,
                                              Set<QualifiedName> allAtts,
                                              ProvFactory pFactory) {
        for (Statement statement: bundle.getStatement()) {
            Set<QualifiedName> vars=ExpandUtil.freeVariables(statement);
            allVars.addAll(vars);
            allVars.addAll(ExpandUtil.freeVariables(bundle));
            Set<QualifiedName> vars2=ExpandUtil.freeAttributeVariables(statement, pFactory);
            allAtts.addAll(vars2);
        }
    }
    
    public Builder generateClassInit(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }
    public Builder generateClassInit(String name, String packge, String supername) {
        return TypeSpec.classBuilder(name)
                .addSuperinterface(ClassName.get(packge,supername))
                .addModifiers(Modifier.PUBLIC);
    }
    public Builder generateInterfaceInit(String name) {
        return TypeSpec.interfaceBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }
  
    public Builder generateClassBuilder3(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }
   
    public Builder generateClassBuilder2(String name) {
        return TypeSpec.classBuilder(name)
                .superclass(FileBuilder.class)
                .addModifiers(Modifier.PUBLIC)
                .addField(ProvFactory.class, "pf", Modifier.PRIVATE, Modifier.FINAL)
                .addField(ValueConverter.class, "vc", Modifier.PRIVATE, Modifier.FINAL);
    }
    
    public MethodSpec generateConstructor2(Hashtable<QualifiedName, String> vmap) {
        com.squareup.javapoet.MethodSpec.Builder builder= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProvFactory.class, "pf")
                .addStatement("this.$N = $N", "pf", "pf");
        for (Entry<QualifiedName, String> e: vmap.entrySet()) {
            final QualifiedName q = e.getKey();
            builder.addStatement("this.$N = pf.newQualifiedName($S,$S,$S)", e.getValue(),q.getNamespaceURI(),q.getLocalPart(),q.getPrefix());
        }
        builder.addStatement("this.vc = new ValueConverter(pf)");
        builder.addStatement("register(this)");
        return builder .build();
    }
      
    public String camelcase(String s) { 
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s); 
    }
    
    static ProvUtilities u = new ProvUtilities();
        

    public Set<QualifiedName> allQualifiedNames(Statement statement) {
        HashSet<QualifiedName> result = new HashSet<QualifiedName>();
        for (int i = 0; i < ExpandUtil.getFirstTimeIndex(statement); i++) {
            Object o = u.getter(statement, i);
            if (o instanceof QualifiedName) {
                QualifiedName name = (QualifiedName) o;
                result.add(name);
            } else {
                if (o instanceof List) {
                    List<QualifiedName> ll = (List<QualifiedName>) o;
                    for (QualifiedName name : ll) {
                        result.add(name);
                    }
                }
            }
        }
        return result;
    }   
    

    public HashSet<QualifiedName> allQualifiedNamesInAttributes(Statement statement, ProvFactory pf) {
        HashSet<QualifiedName> result = new HashSet<QualifiedName>();
        Collection<Attribute> ll = pf.getAttributes(statement);
        for (Attribute attr : ll) {
            result.add(attr.getElementName());
            if (attr.getType()!=null) result.add(attr.getType());
            if (attr.getValue() instanceof QualifiedName) result.add((QualifiedName)attr.getValue());
        }
        return result;
    }

    public void allQualifiedNames(Bundle bundle,
                                  Set<QualifiedName> result,
                                  ProvFactory pFactory) {
        result.add(bundle.getId());
        for (Statement statement: bundle.getStatement()) {
            Set<QualifiedName> vars=allQualifiedNames(statement);
            result.addAll(vars);
            Set<QualifiedName> vars2=allQualifiedNamesInAttributes(statement, pFactory);
            result.addAll(vars2);
        }
    }

    public Document readDocumentFromFile(String file) throws ClassNotFoundException,
                                                             NoSuchMethodException,
                                                             SecurityException,
                                                             InstantiationException,
                                                             IllegalAccessException,
                                                             IllegalArgumentException,
                                                             InvocationTargetException {
        Object interop=getInteropFramework();
        Method method = interop.getClass().getMethod("readDocumentFromFile", String.class);
        Document doc=(Document)method.invoke(interop,file);
        return doc;
    }

    public void writeDocument(String file, Document doc) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object interop=getInteropFramework();
        Method method = interop.getClass().getMethod("writeDocument", String.class, Document.class);
        method.invoke(interop,file,doc);
    }


    public Object getInteropFramework() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
                                               IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = Class.forName("org.openprovenance.prov.interop.InteropFramework");
        Constructor<?> ctor = clazz.getConstructor();
        return ctor.newInstance(new Object[] { }); 
    }

    
    


}
