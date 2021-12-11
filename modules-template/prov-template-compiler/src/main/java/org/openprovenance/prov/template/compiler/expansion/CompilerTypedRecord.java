package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.CompilerClient;
import org.openprovenance.prov.template.compiler.CompilerUtil;

import javax.lang.model.element.Modifier;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;

public class CompilerTypedRecord {
    private final CompilerUtil compilerUtil=new CompilerUtil();
    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerClient compilerClient;
    private final boolean debugComment;


    public CompilerTypedRecord(boolean withMain, CompilerClient compilerClient, ProvFactory pFactory, boolean debugComment) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerClient=compilerClient;
        this.debugComment=debugComment;
    }

    public JavaFile generatedTypedRecordConstructor(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateTypeDeclaration_aux(doc, allVars, allAtts, name, templateName, packge, resource, bindings_schema);

    }



    public JavaFile generateTypeDeclaration_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {
        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("call")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get(Object[].class));



        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        compilerUtil.generateDocumentSpecializedParameters(mbuilder, the_var);

        String allArgs= compilerUtil.generateArgumentsListForCall(the_var,null);



        mbuilder.addStatement("return new Object[] { $S , " + allArgs + "}", templateName );



        TypeSpec.Builder builder = compilerUtil.generateTypedRecordClass(name);

        final ParameterizedTypeName superinterface=ParameterizedTypeName.get(ClassName.get(packge,name + "Interface"),TypeName.get(Object[].class));
        builder.addSuperinterface(superinterface);


        builder.addMethod(mbuilder.build());

        TypeSpec bean = builder.build();



        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", getClass().getName()+"generateTypeDeclaration_aux()", templateName)
                .build();

        return myfile;
    }


}
