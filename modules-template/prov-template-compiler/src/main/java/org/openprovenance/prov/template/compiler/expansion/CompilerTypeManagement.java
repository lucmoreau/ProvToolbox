package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.CompilerClient;
import org.openprovenance.prov.template.compiler.CompilerUtil;

import javax.lang.model.element.Modifier;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;

public class CompilerTypeManagement {
    private final CompilerUtil compilerUtil=new CompilerUtil();
    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerClient compilerClient;
    private final boolean debugComment;


    public CompilerTypeManagement(boolean withMain, CompilerClient compilerClient, ProvFactory pFactory, boolean debugComment) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerClient=compilerClient;
        this.debugComment=debugComment;
    }

    public JavaFile generateTypeDeclaration(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateTypeDeclaration_aux(doc, allVars, allAtts, name, templateName, packge, resource, bindings_schema);

    }


    static public final ParameterizedTypeName Map_QN_S_of_String=ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(QualifiedName.class),ParameterizedTypeName.get(ClassName.get(Set.class),TypeName.get(String.class)));

    public Map<String, Collection<String>> getKnownTypes() {
        return knownTypes;
    }

    public Map<String, Collection<String>> getUnknownTypes() {
        return unknownTypes;
    }

    final Map<String,Collection<String>> knownTypes=new HashMap<>();
    final Map<String,Collection<String>> unknownTypes=new HashMap<>();


    public JavaFile generateTypeDeclaration_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {
        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("call")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get("T"));

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        compilerUtil.generateDocumentSpecializedParameters(mbuilder, the_var);


        StatementTypeAction action = new StatementTypeAction(pFactory, allVars, allAtts, null, null, "__C_document.getStatementOrBundle()", bindings_schema, knownTypes, unknownTypes, mbuilder);
        for (StatementOrBundle s : doc.getStatementOrBundle()) {
            u.doAction(s, action);
        }


        for (QualifiedName q : allVars) {

            final String key = q.getLocalPart();
            final JsonNode entry = the_var.path(key);
            if (entry != null && !(entry instanceof MissingNode)) {

                mbuilder.addComment("Declare $N", q.getLocalPart());
                knownTypes.getOrDefault(q.getUri(), new LinkedList<>()).forEach(type -> {

                    mbuilder.beginControlFlow("if ($N!=null) ", q.getLocalPart());
                    mbuilder.addStatement("knownTypeMap.computeIfAbsent($N, k -> new $T<>())", q.getLocalPart(), HashSet.class);
                    mbuilder.addStatement("knownTypeMap.get($N).add($S)", q.getLocalPart(), type);
                    mbuilder.endControlFlow();

                });

                unknownTypes.getOrDefault(q.getUri(), new LinkedList<>()).forEach(type -> {
                    mbuilder.beginControlFlow("if ($N!=null) ", q.getLocalPart());
                    mbuilder.addComment(type);
                    final String typeVar = type.substring(type.indexOf("#") + 1);
                    mbuilder.beginControlFlow("if ($N!=null) ", typeVar);
                    mbuilder.addStatement("unknownTypeMap.computeIfAbsent($N, k -> new $T<>())", q.getLocalPart(), HashSet.class);
                    mbuilder.addStatement("unknownTypeMap.get($N).add((($T)$N).getUri())", q.getLocalPart(), QualifiedName.class, typeVar);
                    mbuilder.endControlFlow();
                    mbuilder.endControlFlow();

                });
            } else {

            }
        }





        mbuilder.addStatement("return null");


        MethodSpec.Builder cbuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        cbuilder.addStatement("this.knownTypeMap=knownTypeMap");
        cbuilder.addStatement("this.unknownTypeMap=unknownTypeMap");
        cbuilder.addStatement("this.pf=org.openprovenance.prov.interop.InteropFramework.getDefaultFactory()");


        cbuilder.addParameter(Map_QN_S_of_String,"knownTypeMap");
        cbuilder.addParameter(Map_QN_S_of_String,"unknownTypeMap");



        TypeSpec.Builder builder = compilerUtil.generateTypeManagementClass(name);
        builder.addTypeVariable(TypeVariableName.get("T"));

        final ParameterizedTypeName superinterface=ParameterizedTypeName.get(ClassName.get(packge,name + "Interface"),TypeVariableName.get("T"));
        builder.addSuperinterface(superinterface);

        builder.addField(ProvFactory.class,"pf", Modifier.PRIVATE);



        builder.addField(Map_QN_S_of_String,"knownTypeMap", Modifier.PRIVATE);
        builder.addField(Map_QN_S_of_String,"unknownTypeMap", Modifier.PRIVATE);
        builder.addMethod(cbuilder.build());
        builder.addMethod(mbuilder.build());

        TypeSpec bean = builder.build();



        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", getClass().getName()+".generateTypeDeclaration_aux()", templateName)
                .build();

        return myfile;
    }


}
