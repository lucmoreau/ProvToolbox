package org.openprovenance.prov.template;

import java.util.Set;

import javax.lang.model.element.Modifier;

import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;


public class GeneratorUtil {
    

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
            Set<QualifiedName> vars2=ExpandUtil.freeAttributeVariables(statement, pFactory);
            allAtts.addAll(vars2);
        }
    }
    
    public Builder generateClassBuilder(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(BindingsBean.class)
                .addField(Bindings.class, "bindings", Modifier.PRIVATE, Modifier.FINAL)
                .addField(ProvFactory.class, "pf", Modifier.PRIVATE, Modifier.FINAL);
    }
    public Builder generateClassBuilder2(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
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
    public MethodSpec generateConstructor2() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProvFactory.class, "pf")
                .addStatement("this.$N = $N", "pf", "pf")
                .build();
    }
      
    public String camelcase(String s) { 
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s); 
    }

}
