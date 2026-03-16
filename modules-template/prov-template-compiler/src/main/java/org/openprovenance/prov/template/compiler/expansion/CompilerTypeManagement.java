package org.openprovenance.prov.template.compiler.expansion;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Comment;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.Statement;
import org.openprovenance.prov.template.compiler.past.emitter.Poet;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.template.log2prov.interfaces.TriFunction;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constant.getNull;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerTypeManagement {

    // JavaPoet type constants retained for backward compatibility with oldstuff/ classes
    static public final ParameterizedTypeName Map_QN_S_of_String=ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.TypeName.get(QualifiedName.class),ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Set.class), com.squareup.javapoet.TypeName.get(String.class)));
    static public final ParameterizedTypeName Map_QN_Col_of_String=ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.TypeName.get(QualifiedName.class),ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Collection.class), com.squareup.javapoet.TypeName.get(String.class)));
    static public final ParameterizedTypeName Map_QN_Map_String_C_of_String =ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.TypeName.get(QualifiedName.class),ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.TypeName.get(String.class),ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Collection.class), com.squareup.javapoet.TypeName.get(String.class))));
    static public final ParameterizedTypeName Function_O_Col_S =
                    ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(BiFunction.class), com.squareup.javapoet.TypeName.get(Object.class), com.squareup.javapoet.TypeName.get(String.class), ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Collection.class), com.squareup.javapoet.TypeName.get(String.class)));
    static public final ParameterizedTypeName CollectionOfPairsOfStringAndString =ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Collection.class),ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Pair.class), com.squareup.javapoet.TypeName.get(String.class), ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Collection.class), com.squareup.javapoet.TypeName.get(String.class))));
    static public final ParameterizedTypeName TriFunction_O_Col_S = ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(TriFunction.class), com.squareup.javapoet.TypeName.get(Object.class), com.squareup.javapoet.TypeName.get(String.class), com.squareup.javapoet.TypeName.get(String.class), CollectionOfPairsOfStringAndString);
    static public final ParameterizedTypeName Map_S_to_Function =
            ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.TypeName.get(String.class), Function_O_Col_S);
    static public final ParameterizedTypeName Map_S_Map_S_to_Function =
            ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.TypeName.get(String.class),Map_S_to_Function);
    static public final ParameterizedTypeName Map_S_to_TriFunction =
            ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.TypeName.get(String.class), TriFunction_O_Col_S);
    static public final ParameterizedTypeName Map_S_Map_S_to_TriFunction =
            ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Map.class), com.squareup.javapoet.TypeName.get(String.class),Map_S_to_TriFunction);

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

    public SpecificationFile generateTypeDeclaration(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        knownTypes=new HashMap<>();
        unknownTypes=new HashMap<>();


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateTypeDeclaration_aux(configs, locations, doc, allVars, allAtts, name, templateName, packge, bindingsSchema, directory, fileName);

    }


    public Map<String, Collection<String>> getKnownTypes() {
        return knownTypes;
    }

    public Map<String, Collection<String>> getUnknownTypes() {
        return unknownTypes;
    }



    public SpecificationFile generateTypeDeclaration_aux(TemplatesProjectConfiguration configs, Locations locations, Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        // Create the "call" method using PAST
        org.openprovenance.prov.template.compiler.past.Method callMethod = METHOD("call")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(TypeVariable.T())
                .commentFileLocation();

        // Add specialized parameters to the call method
        compilerUtil.generateDocumentSpecializedParameters(callMethod, theVar, variables);

        // Collect PAST statements from the StatementTypeAction
        List<Statement> bodyStatements = new ArrayList<>();

        StatementTypeAction action = new StatementTypeAction(pFactory, allVars, allAtts, null, bindingsSchema, knownTypes, unknownTypes, bodyStatements, compilerUtil);
        for (StatementOrBundle s : doc.getStatementOrBundle()) {
            u.doAction(s, action);
        }

        // Add all collected body statements to the call method
        for (Statement stmt : bodyStatements) {
            callMethod.addStatement(stmt);
        }


        for (QualifiedName q : allVars) {

            final String key = q.getLocalPart();

            List<Descriptor> descriptors=theVar.get(key);
            if (descriptors==null) continue;
            callMethod.addStatement(new Comment("Declare $N", q.getLocalPart()));
            knownTypes.getOrDefault(q.getUri(), new LinkedList<>()).forEach(type -> {

                // if (q.localPart != null) { knownTypeMap.computeIfAbsent(q.localPart, k -> new HashSet<>()); knownTypeMap.get(q.localPart).add(type) }
                callMethod.addStatement(IF(BINARY_OP(VARIABLE(q.getLocalPart()), "!=", getNull()))
                        .THEN(
                                METHOD_CALL(VARIABLE("knownTypeMap"), "computeIfAbsent",
                                        List.of(VARIABLE(q.getLocalPart()), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_SET_GENERICS, List.of()))))),
                                METHOD_CALL(METHOD_CALL(VARIABLE("knownTypeMap"), "get", List.of(VARIABLE(q.getLocalPart()))), "add",
                                        List.of(CONSTANT(type)))
                        ));
            });

            unknownTypes.getOrDefault(q.getUri(), new LinkedList<>()).forEach(type -> {
                final String typeVar = type.substring(type.indexOf("#") + 1);
                // if (q.localPart != null) { // type comment; if (typeVar != null) { unknownTypeMap.computeIfAbsent(...); unknownTypeMap.get(...).add(((QualifiedName)typeVar).getUri()) } }
                callMethod.addStatement(IF(BINARY_OP(VARIABLE(q.getLocalPart()), "!=", getNull()))
                        .THEN(
                                new Comment(type),
                                IF(BINARY_OP(VARIABLE(typeVar), "!=", getNull()))
                                        .THEN(
                                                METHOD_CALL(VARIABLE("unknownTypeMap"), "computeIfAbsent",
                                                        List.of(VARIABLE(q.getLocalPart()), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_SET_GENERICS, List.of()))))),
                                                METHOD_CALL(METHOD_CALL(VARIABLE("unknownTypeMap"), "get", List.of(VARIABLE(q.getLocalPart()))), "add",
                                                        List.of(METHOD_CALL(CAST(PROV_QUALIFIED_NAME, VARIABLE(typeVar)), "getUri", List.of())))
                                        )
                        ));
            });

        }


        callMethod.addStatement(RETURN(getNull()));

        // Create the constructor using PAST
        org.openprovenance.prov.template.compiler.past.Constructor cbuilder = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC);
        compilerUtil.debugFileLocation(cbuilder);

        cbuilder.BODY(
                ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "knownTypeMap"), VARIABLE("knownTypeMap")),
                ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "unknownTypeMap"), VARIABLE("unknownTypeMap")),
                ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "propertyConverters"), VARIABLE("propertyConverters")),
                ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "idata"), VARIABLE("idata")),
                ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "idataConverters"), VARIABLE("idataConverters")),
                ASSIGNMENT(METHOD_CALL(VARIABLE("this"), "pf"), VARIABLE("pf"))
        );

        cbuilder.PARAMETER(PROV_FACTORY, "pf");
        cbuilder.PARAMETER(MAP_QUALIFIEDNAME_STRING_SET, "knownTypeMap");
        cbuilder.PARAMETER(MAP_QUALIFIEDNAME_STRING_SET, "unknownTypeMap");
        cbuilder.PARAMETER(MAP_STRING_MAP_STRING_BIFUNCTION, "propertyConverters");
        cbuilder.PARAMETER(MAP_QUALIFIEDNAME_MAP_STRING_COLLECTION_OF_STRING, "idata");
        cbuilder.PARAMETER(MAP_STRING_MAP_STRING_TRIFUNCTION, "idataConverters");

        // Build the PAST class
        org.openprovenance.prov.template.compiler.past.type.ClassName superinterface =
                org.openprovenance.prov.template.compiler.past.type.ClassName.get(name + "Interface", packge);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType parameterizedSuperinterface =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(superinterface, TypeVariable.T());

        PastFactory pastFactory = new PastFactory();
        Class pastClass = pastFactory.CLASS(name + "TypeManagement")
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(TypeVariable.get("T"))
                .INTERFACES(parameterizedSuperinterface)
                .FIELDS(
                        FIELD("pf", PROV_FACTORY).MODIFIERS(Modifier.PRIVATE),
                        FIELD("knownTypeMap", MAP_QUALIFIEDNAME_STRING_SET).MODIFIERS(Modifier.PRIVATE),
                        FIELD("unknownTypeMap", MAP_QUALIFIEDNAME_STRING_SET).MODIFIERS(Modifier.PRIVATE),
                        FIELD("propertyConverters", MAP_STRING_MAP_STRING_BIFUNCTION).MODIFIERS(Modifier.PRIVATE),
                        FIELD("idata", MAP_QUALIFIEDNAME_MAP_STRING_COLLECTION_OF_STRING).MODIFIERS(Modifier.PRIVATE),
                        FIELD("idataConverters", MAP_STRING_MAP_STRING_TRIFUNCTION).MODIFIERS(Modifier.PRIVATE)
                );

        pastClass.CONSTRUCTOR(cbuilder);
        pastClass.METHOD(callMethod);

        Supplier<Boolean> pythonGenerator=() -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, configs, fileName + DOT_JAVA_EXTENSION, directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator,pythonGenerator);


        /*
        // Emit PAST class to TypeSpec.Builder
        TypeSpec.Builder builder = new Poet().emitBuilder(pastClass);

        TypeSpec bean = builder.build();


        JavaFile myfile = compilerUtil.specWithComment(bean, templateName, packge, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, packge);

         */
    }


}
