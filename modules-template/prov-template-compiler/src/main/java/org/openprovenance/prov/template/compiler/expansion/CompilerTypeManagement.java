package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;
import org.openprovenance.prov.template.log2prov.interfaces.TriFunction;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.BiFunction;

import static org.openprovenance.prov.template.compiler.CompilerUtil.typeT;
import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;

public class CompilerTypeManagement {
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerCommon compilerCommon;
    private final boolean debugComment;


    public CompilerTypeManagement(boolean withMain, CompilerCommon compilerCommon, ProvFactory pFactory, boolean debugComment) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerCommon = compilerCommon;
        this.debugComment=debugComment;
        this.compilerUtil=new CompilerUtil(pFactory);

    }
    private Map<String,Collection<String>> knownTypes;
    private Map<String,Collection<String>> unknownTypes;

    public SpecificationFile generateTypeDeclaration(TemplatesCompilerConfig configs, Locations locations, Document doc, String name, String templateName, String packge, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        knownTypes=new HashMap<>();
        unknownTypes=new HashMap<>();


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateTypeDeclaration_aux(configs, locations, doc, allVars, allAtts, name, templateName, packge, bindings_schema, bindingsSchema, directory, fileName);

    }


    static public final ParameterizedTypeName Map_QN_S_of_String=ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(QualifiedName.class),ParameterizedTypeName.get(ClassName.get(Set.class),TypeName.get(String.class)));
    static public final ParameterizedTypeName Map_QN_Col_of_String=ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(QualifiedName.class),ParameterizedTypeName.get(ClassName.get(Collection.class),TypeName.get(String.class)));

    static public final ParameterizedTypeName Map_QN_Map_String_C_of_String =ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(QualifiedName.class),ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(String.class),ParameterizedTypeName.get(ClassName.get(Collection.class),TypeName.get(String.class))));



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



    public SpecificationFile generateTypeDeclaration_aux(TemplatesCompilerConfig configs, Locations locations, Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("call")
                .addModifiers(Modifier.PUBLIC)
                .returns(typeT);

        compilerUtil.specWithComment(mbuilder);

        JsonNode the_var = bindings_schema.get("var");


        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);


        compilerUtil.generateDocumentSpecializedParameters(mbuilder, theVar, variables);


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
        compilerUtil.specWithComment(cbuilder);

        cbuilder.addStatement("this.knownTypeMap=knownTypeMap");
        cbuilder.addStatement("this.unknownTypeMap=unknownTypeMap");
        cbuilder.addStatement("this.propertyConverters=propertyConverters");

        cbuilder.addStatement("this.idata=idata");
        cbuilder.addStatement("this.idataConverters=idataConverters");


        cbuilder.addStatement("this.pf=pf");


        cbuilder.addParameter(ProvFactory.class,"pf");
        cbuilder.addParameter(Map_QN_S_of_String,"knownTypeMap");
        cbuilder.addParameter(Map_QN_S_of_String,"unknownTypeMap");
        cbuilder.addParameter(Map_S_Map_S_to_Function, "propertyConverters");

        cbuilder.addParameter(Map_QN_Map_String_C_of_String,"idata");
        cbuilder.addParameter(Map_S_Map_S_to_TriFunction, "idataConverters");

        TypeSpec.Builder builder = compilerUtil.generateTypeManagementClass(name);
        builder.addTypeVariable(TypeVariableName.get("T"));

        final ParameterizedTypeName superinterface=ParameterizedTypeName.get(ClassName.get(packge,name + "Interface"),TypeVariableName.get("T"));
        builder.addSuperinterface(superinterface);

        builder.addField(ProvFactory.class,"pf", Modifier.PRIVATE);



        builder.addField(Map_QN_S_of_String,"knownTypeMap", Modifier.PRIVATE);
        builder.addField(Map_QN_S_of_String,"unknownTypeMap", Modifier.PRIVATE);
        builder.addField(Map_S_Map_S_to_Function,"propertyConverters", Modifier.PRIVATE);

        builder.addField(Map_QN_Map_String_C_of_String,"idata", Modifier.PRIVATE);
        builder.addField(Map_S_Map_S_to_TriFunction,"idataConverters", Modifier.PRIVATE);

        builder.addMethod(cbuilder.build());
        builder.addMethod(mbuilder.build());

        TypeSpec bean = builder.build();


        JavaFile myfile = compilerUtil.specWithComment(bean, templateName, packge, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, packge);
    }


}
