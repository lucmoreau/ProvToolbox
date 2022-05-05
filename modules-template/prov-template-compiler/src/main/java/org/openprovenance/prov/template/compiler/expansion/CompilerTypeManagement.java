package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.CompilerClient;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.log2prov.interfaces.TriFunction;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.BiFunction;

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
    private Map<String,Collection<String>> knownTypes;
    private Map<String,Collection<String>> unknownTypes;

    public JavaFile generateTypeDeclaration(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {
        knownTypes=new HashMap<>();
        unknownTypes=new HashMap<>();


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateTypeDeclaration_aux(doc, allVars, allAtts, name, templateName, packge, resource, bindings_schema);

    }


    static public final ParameterizedTypeName Map_QN_S_of_String=ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(QualifiedName.class),ParameterizedTypeName.get(ClassName.get(Set.class),TypeName.get(String.class)));

    static public final ParameterizedTypeName Map_QN_Map_String_S_of_String=ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(QualifiedName.class),ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(String.class),ParameterizedTypeName.get(ClassName.get(Set.class),TypeName.get(String.class))));



    // BiFunction<Object, String Collection<String>>
    static public final ParameterizedTypeName Function_O_Col_S =
                    ParameterizedTypeName.get(ClassName.get(BiFunction.class), TypeName.get(Object.class), TypeName.get(String.class), ParameterizedTypeName.get(ClassName.get(Collection.class),TypeName.get(String.class)));


    static public final ParameterizedTypeName CollectionOfPairsOfStringAndString =ParameterizedTypeName.get(ClassName.get(Collection.class),ParameterizedTypeName.get(ClassName.get(Pair.class), TypeName.get(String.class), ParameterizedTypeName.get(ClassName.get(Collection.class),TypeName.get(String.class))));

    // TriFunction<Object, String, String, Pair<String, Collection<String>>>
    static public final ParameterizedTypeName TriFunction_O_Col_S = ParameterizedTypeName.get(ClassName.get(TriFunction.class), TypeName.get(Object.class), TypeName.get(String.class), TypeName.get(String.class), CollectionOfPairsOfStringAndString);

    // Map<String, Function<Object,  Collection<String>>>
    static public final ParameterizedTypeName Map_S_to_Function =
            ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), Function_O_Col_S);

    // Map<String, Map<String, Function<Object, Collection<String>>>> propertyConverters
    static public final ParameterizedTypeName Map_S_Map_S_to_Function =
            ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class),Map_S_to_Function);

    // Map<String, Function<Object,  Collection<String>>>
    static public final ParameterizedTypeName Map_S_to_TriFunction =
            ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), TriFunction_O_Col_S);

    // Map<String, Map<String, Function<Object, Collection<String>>>> propertyConverters
    static public final ParameterizedTypeName Map_S_Map_S_to_TriFunction =
            ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class),Map_S_to_TriFunction);


    public Map<String, Collection<String>> getKnownTypes() {
        return knownTypes;
    }

    public Map<String, Collection<String>> getUnknownTypes() {
        return unknownTypes;
    }



    public JavaFile generateTypeDeclaration_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {
        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("call")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get("T"));

        if (debugComment) mbuilder.addComment("Generated by method $N", getClass().getName()+".generateTypeDeclaration_aux()");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        compilerUtil.generateDocumentSpecializedParameters(mbuilder, the_var);


        StatementTypeAction action = new StatementTypeAction(pFactory, allVars, allAtts, null, null, "__C_document.getStatementOrBundle()", bindings_schema, knownTypes, unknownTypes, mbuilder,compilerUtil);
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
        cbuilder.addStatement("this.propertyConverters=propertyConverters");

        cbuilder.addStatement("this.idata=idata");
        cbuilder.addStatement("this.idataConverters=idataConverters");


        cbuilder.addStatement("this.pf=org.openprovenance.prov.interop.InteropFramework.getDefaultFactory()");


        cbuilder.addParameter(Map_QN_S_of_String,"knownTypeMap");
        cbuilder.addParameter(Map_QN_S_of_String,"unknownTypeMap");
        cbuilder.addParameter(Map_S_Map_S_to_Function, "propertyConverters");

        cbuilder.addParameter(Map_QN_Map_String_S_of_String,"idata");
        cbuilder.addParameter(Map_S_Map_S_to_TriFunction, "idataConverters");

        TypeSpec.Builder builder = compilerUtil.generateTypeManagementClass(name);
        builder.addTypeVariable(TypeVariableName.get("T"));

        final ParameterizedTypeName superinterface=ParameterizedTypeName.get(ClassName.get(packge,name + "Interface"),TypeVariableName.get("T"));
        builder.addSuperinterface(superinterface);

        builder.addField(ProvFactory.class,"pf", Modifier.PRIVATE);



        builder.addField(Map_QN_S_of_String,"knownTypeMap", Modifier.PRIVATE);
        builder.addField(Map_QN_S_of_String,"unknownTypeMap", Modifier.PRIVATE);
        builder.addField(Map_S_Map_S_to_Function,"propertyConverters", Modifier.PRIVATE);

        builder.addField(Map_QN_Map_String_S_of_String,"idata", Modifier.PRIVATE);
        builder.addField(Map_S_Map_S_to_TriFunction,"idataConverters", Modifier.PRIVATE);

        builder.addMethod(cbuilder.build());
        builder.addMethod(mbuilder.build());

        TypeSpec bean = builder.build();



        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", getClass().getName()+".generateTypeDeclaration_aux()", templateName)
                .build();

        return myfile;
    }


}
