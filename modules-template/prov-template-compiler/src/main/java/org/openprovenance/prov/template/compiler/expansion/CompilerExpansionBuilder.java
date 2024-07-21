package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.openprovenance.prov.model.interop.Formats;
import org.openprovenance.prov.model.interop.Framework;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.InvalidCaseException;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.template.compiler.*;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;
import org.openprovenance.prov.template.expander.ExpandAction;
import org.openprovenance.prov.template.expander.ExpandUtil;
import org.openprovenance.prov.template.expander.exception.MissingAttributeValue;
import org.openprovenance.prov.template.types.TypesRecordProcessor;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerUtil.typeT;
import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.CompilerCommon.makeArgsList;
import static org.openprovenance.prov.template.compiler.expansion.CompilerTypeManagement.*;
import static org.openprovenance.prov.template.expander.ExpandUtil.*;

public class CompilerExpansionBuilder {
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerCommon compilerCommon;
    private final boolean debugComment;
    private final CompilerTypeManagement compilerTypeManagement;

    private TypeName processorInterfaceType(String template, String packge) {
        return ParameterizedTypeName.get(ClassName.get(packge,compilerUtil.templateNameClass(template)+"Interface"),TypeVariableName.get("T"));
    }


    public CompilerExpansionBuilder(boolean withMain, CompilerCommon compilerCommon, ProvFactory pFactory, boolean debugComment, CompilerTypeManagement compilerTypeManagement) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerCommon = compilerCommon;
        this.debugComment=debugComment;
        this.compilerTypeManagement=compilerTypeManagement;
        this.compilerUtil=new CompilerUtil(pFactory);
    }




    public SpecificationFile generateBuilderInterfaceSpecification(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateBuilderInterfaceSpecification_aux(configs, locations, name, templateName, packge, bindingsSchema, directory, fileName);

    }

    SpecificationFile generateBuilderInterfaceSpecification_aux(TemplatesProjectConfiguration configs, Locations locations, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInitParameter(name+"Interface", "T");

        builder.addMethod(generateTemplateGeneratorInterface(bindingsSchema));

        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, templateName, packge, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, packge);
    }


    public SpecificationFile generateBuilderSpecification(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String packge, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, Map<Integer, List<Integer>> successorTable, String directory, String fileName) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateBuilderSpecification_aux(configs, locations, doc, new ArrayList<>(allVars), new ArrayList<>(allAtts), name, templateName, packge, bindings_schema, bindingsSchema, successorTable, directory, fileName);

    }


    public MethodSpec generateTemplateGeneratorInterface(TemplateBindingsSchema bindingsSchema) {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("call")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(typeT);


        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);
        compilerUtil.generateDocumentSpecializedParameters(builder, theVar, variables);

        return builder.build();
    }

    SpecificationFile generateBuilderSpecification_aux(TemplatesProjectConfiguration configs, Locations locations, Document doc, Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, String templateName, String packge, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, Map<Integer, List<Integer>> successorTable, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateClassBuilder2(name);

        Hashtable<QualifiedName, String> vmap = generateQualifiedNames(doc, builder);

        builder.addMethod(compilerUtil.generateConstructor2(vmap));

        builder.addMethod(generateTemplateGenerator(allVars, allAtts, doc, vmap, bindings_schema));


        builder.addMethod(compilerCommon.generateNameAccessor(templateName));

        builder.addMethod(compilerCommon.commonAccessorGenerator(templateName,locations.getFilePackage(BeanDirection.COMMON)));

        builder.addMethod(typeManagerGenerator(templateName,packge));
       // builder.addMethod(compilerClient.typePropagateGenerator(templateName,packge));
        builder.addMethod(compilerCommon.typedRecordGenerator(templateName,packge));


        builder.addMethod(generateTypePropagator(allVars, allAtts, doc, vmap, packge+".client", bindings_schema,successorTable));

        //builder.addMethod(generateTypePropagatorN_OLD());
        builder.addMethod(generateTypePropagatorN_new());


        if (withMain) builder.addMethod(generateMain(allVars, allAtts, name, bindings_schema, bindingsSchema));

        if (bindings_schema != null) {
            builder.addMethod(generateFactoryMethod(allVars, allAtts, name, bindings_schema, bindingsSchema));
            builder.addMethod(generateFactoryMethodWithContinuation(allVars, allAtts, name,  templateName, packge, bindings_schema));
            builder.addMethod(generateFactoryMethodWithArray(allVars, allAtts, name, bindings_schema));
            builder.addMethod(generateFactoryMethodWithArrayAndContinuation(name,  templateName, packge, bindings_schema));
            builder.addMethod(generateUniqueRecordFactoryMethodWithArrayAndContinuation(name,  templateName, packge, bindingsSchema));
        }



        TypeSpec bean = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(bean, templateName, packge, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, packge);


    }


    public MethodSpec generateTemplateGenerator(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, Document doc, Hashtable<QualifiedName, String> vmap, JsonNode bindings_schema) {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("generator")
                .addModifiers(Modifier.PUBLIC)
                .returns(Document.class);

        compilerUtil.specWithComment(builder);



        builder
                .addStatement("$T nullqn = null", QualifiedName.class)
                .addStatement("$T attrs=null", StatementCompilerAction.cl_collectionOfAttributes)
                .addStatement("$T __C_document = pf.newDocument()", Document.class);
        for (QualifiedName q : allVars) {
            builder.addParameter(QualifiedName.class, q.getLocalPart());
        }
        for (QualifiedName q : allAtts) {
            if (allVars.contains(q)) {
                // no need to redeclare
            } else {
                builder.addParameter(Object.class, q.getLocalPart()); // without type declaration, any object may be accepted, assuming this is not a q also in allVars.
            }
        }
        for (QualifiedName q : allVars) {
            if (ExpandUtil.isGensymVariable(q)) {
                final String vgen = q.getLocalPart();
                builder.addStatement("if ($N==null) $N=$T.getUUIDQualifiedName2(pf)", vgen, vgen, ExpandAction.class);
            }
        }


        StatementCompilerAction action = new StatementCompilerAction(pFactory, allVars, allAtts, vmap, builder, "__C_document.getStatementOrBundle()", bindings_schema);
        for (StatementOrBundle s : doc.getStatementOrBundle()) {
            u.doAction(s, action);
        }

        builder.addStatement("new $T().updateNamespaces(__C_document)", ProvUtilities.class);

        builder.addStatement("return __C_document");

        MethodSpec method = builder.build();

        return method;
    }


    static final ArrayTypeName recordType = ArrayTypeName.of(ClassName.get(Object.class));
    public static final ParameterizedTypeName levelNMapType = ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), TypeName.get(Integer.class));
    static final ParameterizedTypeName levelNP1MapType = ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), TypeName.get(int[].class));
    public static final ParameterizedTypeName levelNP1CMapType = ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), ParameterizedTypeName.get(ClassName.get(Collection.class), TypeName.get(int[].class)));


    static final ParameterizedTypeName successorType = ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(Integer.class), TypeName.get(int[].class));



    public MethodSpec generateTypePropagator(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, Document doc, Hashtable<QualifiedName, String> vmap, String packge, JsonNode bindings_schema, Map<Integer, List<Integer>> successorTable) {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("propagateTypes")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        builder.addParameter(ParameterSpec.builder(recordType,"record").build());
        builder.addParameter(ParameterSpec.builder(levelNMapType,"mapLevelN").build());
        builder.addParameter(ParameterSpec.builder(levelNP1CMapType,"mapLevelNP1").build());
        builder.addParameter(ParameterSpec.builder(levelNMapType,"mapLevel0").build());
        builder.addParameter(ParameterSpec.builder(levelNMapType,"uniqId").build());

        compilerUtil.specWithComment(builder);


        builder.addComment(successorTable.toString());

        //builder.addStatement("$T builder=getClientBuilder()", ClassName.get("org.openprovenance.prov.client","Builder"));

        int count=1; // ignore the tempalte name
        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");
        Iterator<String> iter = the_var.fieldNames();

        Map<String, Set<Pair<QualifiedName, WasDerivedFrom>>>  successors1= compilerCommon.getSuccessors1();
        Map<String, Set<Pair<QualifiedName, WasAttributedTo>>> successors2= compilerCommon.getSuccessors2();
        Map<String, Set<Pair<QualifiedName, HadMember>>>       successors3= compilerCommon.getSuccessors3();
        Map<String, Set<Pair<QualifiedName, QualifiedHadMember>>> successors3b= compilerCommon.getSuccessors3b();
        Map<String, Set<Pair<QualifiedName, SpecializationOf>>>successors4= compilerCommon.getSuccessors4();


        Map<String, Collection<String>> knownTypes   = compilerTypeManagement.getKnownTypes();
        Map<String, Collection<String>> unknownTypes = compilerTypeManagement.getUnknownTypes();

        while (iter.hasNext()) {
            String key = iter.next();
            if (compilerUtil.isVariableDenotingQualifiedName(key,the_var)) {
                builder.addComment("Variable: " + key);
                builder.addComment("Count: " + count);

                if ((successors1.get(key) != null) || (successors2.get(key) != null) || (successors3.get(key) != null) || (successors3b.get(key) != null) || (successors4.get(key) != null)) {
                    //generateStatementForRelation_OLD(builder, count, the_var, successors1,  knownTypes, unknownTypes, key);
                    //generateStatementForRelation_OLD(builder, count, the_var, successors3b, knownTypes, unknownTypes, key);

                    List<Integer> rowValues=successorTable.get(count);
                    if (rowValues==null || rowValues.isEmpty()) throw new InvalidCaseException("successor table incorrect");

                    for (int i=0; i<rowValues.size()/2; i++) {
                        int successor = rowValues.get(i * 2);
                        int relation = rowValues.get(i * 2 + 1);

                        if (relation == StatementOrBundle.Kind.PROV_DERIVATION.ordinal()) {
                            generateStatementForRelation_NEW(builder, count, successor, relation, the_var, successors1, knownTypes, unknownTypes, key);
                        } else if (relation == StatementOrBundle.Kind.PROV_MEMBERSHIP.ordinal()) {
                            generateStatementForRelation_NEW(builder, count, successor, relation, the_var, successors3b, knownTypes, unknownTypes, key);
                        } else {
                            builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$L,uniqId)", count, successor, relation, -1);
                        }
                    }
                } else {
                    builder.addComment("No successor for: " + count);
                }
            }
            builder.addComment("");

            count++;
        }
        return builder.build();
    }
/*
    private <ARELATION extends Identifiable> void generateStatementForRelation_OLD(MethodSpec.Builder builder, int count, JsonNode the_var, Map<String, Set<Pair<QualifiedName, ARELATION>>> successors, Map<String, Collection<String>> knownTypes, Map<String, Collection<String>> unknownTypes, String key) {
        if (successors.get(key) != null) {

            final List<ARELATION> relations = successors.get(key).stream().map(Pair::getRight).collect(Collectors.toList());
            final List<QualifiedName> identifiers = relations.stream().map(Identifiable::getId).collect(Collectors.toList());

            builder.addComment("Identifiers: " + identifiers);
            builder.addComment("KnownTypes: "   + successors.get(key).stream().map(p -> knownTypes.get(p.getRight().getId().getUri())).collect(Collectors.toList()));
            builder.addComment("UnknownTypes: " + successors.get(key).stream().map(p -> unknownTypes.get(p.getRight().getId().getUri())).collect(Collectors.toList()));

            final List<Collection<QualifiedName>> optionalActivityTypes = relations.stream().map(p -> doCollectElementVariables((Statement)p, ACTIVITY_TYPE_URI)).collect(Collectors.toList());
            final List<Collection<QualifiedName>> optionalActivities    = relations.stream().map(p -> doCollectElementVariables((Statement)p, TMPL_ACTIVITY_URI)).collect(Collectors.toList());

            builder.addComment("ActivityTypes: " + optionalActivityTypes);
            builder.addComment("Activities: "    + optionalActivities);

            if (optionalActivityTypes.isEmpty() || optionalActivityTypes.get(0) == null) {
                builder.addStatement("propagateTypes_n_old(record,mapLevelN,mapLevelNP1,$L,$L)", count, -1);
            } else {

                final QualifiedName firstRelationIdentifier = identifiers.get(0);
                final ARELATION firstRelation = relations.get(0);

                final Optional<QualifiedName> firstActivityType = optionalActivityTypes.get(0).stream().findFirst();
                if (firstActivityType.isEmpty()) {

                    builder.addStatement("propagateTypes_n_old(record,mapLevelN,mapLevelNP1,$L,$L)", count, -1);

                } else {

                    if (optionalActivities.isEmpty() || optionalActivities.get(0) == null)
                        throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in " + firstRelation);
                    final Optional<QualifiedName> firstActivity = optionalActivities.get(0).stream().findFirst();
                    if (firstActivity.isEmpty())
                        throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in " + firstRelation);

                    builder.addComment("propagating for $N", key);
                    builder.addComment("URI: " + firstActivity.get().getUri());

                    //builder.addStatement("System.out.println(\"maplevelN \" + mapLevelN.get($S))",identifiers.get(0).getUri());
                    final String tmpa = "l0a_" + count;
                    final String tmpb = "l0b_" + count;

                    builder.addComment("Position: " + findPosition(TypesRecordProcessor.localName(firstActivity.get().getUri()), the_var));

                    //TypesRecordProcessor.localName(firstActivity.get().getUri())
                    builder.addStatement("$T $N=mapLevel0.get($S + (($T)record[$L]).getLocalPart())", Integer.class, tmpa, firstRelationIdentifier.getUri() + ".", QualifiedName.class, findPosition(TypesRecordProcessor.localName(firstActivity.get().getUri()), the_var));
                    builder.addStatement("int $N=($N==null)?-1:$N", tmpb, tmpa, tmpa);

                    builder.addStatement("propagateTypes_n_old(record,mapLevelN,mapLevelNP1,$L,$N)", count, tmpb);
                }
            }
        } else {
            builder.addStatement("propagateTypes_n_old(record,mapLevelN,mapLevelNP1,$L,$L)", count, -1);
        }
    }


 */
    private <ARELATION extends Identifiable> void generateStatementForRelation_NEW(MethodSpec.Builder builder, int count, int successor, int relation, JsonNode the_var, Map<String, Set<Pair<QualifiedName, ARELATION>>> successors, Map<String, Collection<String>> knownTypes, Map<String, Collection<String>> unknownTypes, String key) {
        if (successors.get(key) != null) {

            final List<ARELATION> relations = successors.get(key).stream().map(Pair::getRight).collect(Collectors.toList());
            final List<QualifiedName> identifiers = relations.stream().map(Identifiable::getId).collect(Collectors.toList());

            builder.addComment("Identifiers: " + identifiers);
            builder.addComment("KnownTypes: "   + successors.get(key).stream().map(p -> knownTypes.get(p.getRight().getId().getUri())).collect(Collectors.toList()));
            builder.addComment("UnknownTypes: " + successors.get(key).stream().map(p -> unknownTypes.get(p.getRight().getId().getUri())).collect(Collectors.toList()));

            final List<Collection<QualifiedName>> optionalActivityTypes = relations.stream().map(p -> doCollectElementVariables((Statement)p, ACTIVITY_TYPE_URI)).collect(Collectors.toList());
            final List<Collection<QualifiedName>> optionalActivities    = relations.stream().map(p -> doCollectElementVariables((Statement)p, TMPL_ACTIVITY_URI)).collect(Collectors.toList());

            builder.addComment("ActivityTypes: " + optionalActivityTypes);
            builder.addComment("Activities: "    + optionalActivities);

            if (optionalActivityTypes.isEmpty() || optionalActivityTypes.get(0) == null) {
                builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$L,uniqId)", count, successor, relation,-1);
            } else {

                final QualifiedName firstRelationIdentifier = identifiers.get(0);
                final ARELATION firstRelation = relations.get(0);

                final Optional<QualifiedName> firstActivityType = optionalActivityTypes.get(0).stream().findFirst();
                if (firstActivityType.isEmpty()) {

                    builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$L,uniqId)", count, successor, relation, -1);

                } else {

                    if (optionalActivities.isEmpty() || optionalActivities.get(0) == null)
                        throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in " + firstRelation);
                    final Optional<QualifiedName> firstActivity = optionalActivities.get(0).stream().findFirst();
                    if (firstActivity.isEmpty())
                        throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in " + firstRelation);

                    builder.addComment("propagating for $N", key);
                    builder.addComment("URI: " + firstActivity.get().getUri());

                    //builder.addStatement("System.out.println(\"maplevelN \" + mapLevelN.get($S))",identifiers.get(0).getUri());
                    final String tmpa = "l1a_" + count;
                    final String tmpb = "l1b_" + count;

                    builder.addComment("Position: " + findPosition(TypesRecordProcessor.localName(firstActivity.get().getUri()), the_var));

                    //TypesRecordProcessor.localName(firstActivity.get().getUri())
                    builder.addStatement("$T $N=mapLevel0.get($S + (($T)record[$L]).getLocalPart())", Integer.class, tmpa, firstRelationIdentifier.getUri() + ".", QualifiedName.class, findPosition(TypesRecordProcessor.localName(firstActivity.get().getUri()), the_var));
                    builder.addStatement("int $N=($N==null)?-1:$N", tmpb, tmpa, tmpa);

                    builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$N, uniqId)", count, successor, relation, tmpb);
                }
            }
        } else {
            builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$L,uniqId)", count, successor, relation,-1);
        }
    }

    private int findPosition(String name, JsonNode the_var) {
        Iterator<String> iter = the_var.fieldNames();

        int count=1;
        while (iter.hasNext()) {
            String key = iter.next();
            if (key.equals(name)) return count;
            count++;
        }
        return count;
    }

    static public String escape (QualifiedName qn) {
        String uri=qn.getUri();
        return uri.replace("/","_").replace("#","_").replace(":","_").replace(".","_");
    }


    public Collection<QualifiedName> doCollectElementVariables(Statement s, String search) {
        return doCollectElementVariables(pFactory,s,search);
    }


    static public Collection<QualifiedName> doCollectElementVariables(ProvFactory pFactory, Statement s, String search) {
        Collection<Attribute> attributes = pFactory.getAttributes(s);
        if (!(attributes.isEmpty())) {
            boolean found=false;
            Collection<QualifiedName> res=new LinkedList<>();
            for (Attribute attribute:attributes) {
                QualifiedName element=attribute.getElementName();
                Object value=attribute.getValue();
                if (value instanceof QualifiedName) {
                    QualifiedName vq=(QualifiedName) value;
                    if (search.equals(element.getUri())) {
                        res.add(vq);
                        found=true;
                    }
                }
            }
            if (found) return res;
        }
        return null;
    }
    /*
    public MethodSpec generateTypePropagatorN_new1() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("propagateTypes_n")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        final String var_successor = "successor";
        final String var_genericRelation = "genericRelation";

        builder.addParameter(ParameterSpec.builder(recordType, "record").build());
        builder.addParameter(ParameterSpec.builder(levelNMapType, "mapLevelN").build());
        builder.addParameter(ParameterSpec.builder(levelNP1CMapType, "mapLevelNP1").build());
        builder.addParameter(ParameterSpec.builder(Integer.class, "count").build());
        builder.addParameter(ParameterSpec.builder(int.class, var_successor).build());
        builder.addParameter(ParameterSpec.builder(int.class, var_genericRelation).build());
        builder.addParameter(ParameterSpec.builder(int.class, "specificRelation").build());
        return builder.build();
    }
         */
    public MethodSpec generateTypePropagatorN_new() {



        MethodSpec.Builder builder = MethodSpec.methodBuilder("propagateTypes_n")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        compilerUtil.specWithComment(builder);
        final String var_successor = "successor";
        final String var_genericRelation = "genericRelation";
        final String var_record = "record";
        final String var_specificRelation = "specificRelation";
        final String var_count = "count";
        final String var_in_type="in_type";


        builder.addParameter(ParameterSpec.builder(recordType, var_record).build());
        builder.addParameter(ParameterSpec.builder(levelNMapType, "mapLevelN").build());
        builder.addParameter(ParameterSpec.builder(levelNP1CMapType, "mapLevelNP1").build());
        builder.addParameter(ParameterSpec.builder(Integer.class, var_count).build());
        builder.addParameter(ParameterSpec.builder(int.class, var_successor).build());
        builder.addParameter(ParameterSpec.builder(int.class, var_genericRelation).build());
        builder.addParameter(ParameterSpec.builder(int.class, var_specificRelation).build());
        builder.addParameter(ParameterSpec.builder(levelNMapType, "uniqId").build());


        builder.beginControlFlow("if ($N[$N]!=null)", var_record, var_count);
        builder.addStatement("String uri=(($T)($N[$L])).getUri()",QualifiedName.class, var_record, var_count);
        builder.addStatement("Integer $N=mapLevelN.get(uri)", var_in_type);
        builder.beginControlFlow("if ($N!=null)",var_in_type);
        builder.beginControlFlow("if ($N[$N]!=null)", var_record, var_successor);
        builder.addStatement("String uri2=(($T)($N[$N])).getUri()",QualifiedName.class, var_record, var_successor);
        builder.addStatement("mapLevelNP1.computeIfAbsent(uri2, k -> new $T<>())",  LinkedList.class); //store in Lists initially
        builder.addStatement("mapLevelNP1.get(uri2).add(new int[] { $N, $N, $N, $N, $N, uniqId.get($N)})", var_successor, var_genericRelation, var_specificRelation, var_in_type, var_count, "uri");
        builder.endControlFlow();
        builder.endControlFlow();
        builder.endControlFlow();
        return builder.build();
    }

/*

    public MethodSpec generateTypePropagatorN_OLD() {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("propagateTypes_n_old")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        builder.addParameter(ParameterSpec.builder(recordType,"record").build());
        builder.addParameter(ParameterSpec.builder(levelNMapType,"mapLevelN").build());
        builder.addParameter(ParameterSpec.builder(levelNP1CMapType,"mapLevelNP1").build());
        builder.addParameter(ParameterSpec.builder(Integer.class,"count").build());
        builder.addParameter(ParameterSpec.builder(int.class,"rel").build());

        if (debugComment)
            builder.addComment("Generated by method $N", getClass().getName() + ".generateTypePropagatorN_OLD()");

        builder.addStatement("$T builder=getClientBuilder()", ClassName.get("org.openprovenance.prov.client","Builder"));
        builder.addStatement("$T successors=builder.getTypedSuccessors()", successorType);


        String tmpVar="tmp";

        builder.beginControlFlow("if ($N[$N]!=null)", "record", "count");
        builder.addStatement("String uri=(($T)($N[$L])).getUri()",QualifiedName.class, "record", "count");
        builder.addStatement("Integer $N=mapLevelN.get(uri)", tmpVar);
        builder.beginControlFlow("if ($N!=null)",tmpVar);
        builder.addStatement("$T the_type=successors.get($N)", TypeName.get(int[].class), "count");
        builder.beginControlFlow("if (the_type!=null && the_type.length!=0)");
        builder.addStatement("int theLength=the_type.length/2");
        builder.beginControlFlow("for (int _count=0; _count<theLength; _count++) ");
        //builder.addStatement("System.out.println(\"count is \" + count + \" and _count is \" + _count)");

        builder.beginControlFlow("if ($N[the_type[_count*2+0]]!=null)", "record");
        builder.addStatement("String uri2=(($T)($N[the_type[_count*2+0]])).getUri()",QualifiedName.class, "record");

        builder.addStatement("mapLevelNP1.computeIfAbsent(uri2, k -> new $T<>())",  LinkedList.class); //store in Lists initially

        builder.addStatement("mapLevelNP1.get(uri2).add(new int[] { the_type[_count*2+0], the_type[_count*2+1], rel, $N, $N })", tmpVar, "count");
        builder.endControlFlow();
        builder.endControlFlow();
        builder.endControlFlow();
        builder.endControlFlow();
        builder.endControlFlow();


        return builder.build();
    }



 */
    public Hashtable<QualifiedName, String> generateQualifiedNames(Document doc, TypeSpec.Builder builder) {
        Bundle bun = u.getBundle(doc).get(0);
        Set<QualifiedName> set = new HashSet<>();
        compilerUtil.allQualifiedNames(bun, set, pFactory);
        set.remove(pFactory.newQualifiedName(ExpandUtil.TMPL_NS, ExpandUtil.LABEL, ExpandUtil.TMPL_PREFIX));
        set.add(pFactory.getName().PROV_LABEL);
        Hashtable<QualifiedName, String> qnVariables = new Hashtable<>();
        for (QualifiedName qn : set) {
            if (!(ExpandUtil.isVariable(qn))) {
                final String v = variableForQualifiedName(qn);
                qnVariables.put(qn, v);

                builder.addField(QualifiedName.class, v, Modifier.PUBLIC, Modifier.FINAL);
            }


        }
        return qnVariables;

    }

    public String variableForQualifiedName(QualifiedName qn) {
        return "_Q_" + qn.getPrefix() + "_" + qn.getLocalPart();
    }

    public MethodSpec generateFactoryMethod(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PUBLIC)
                .returns(Document.class);

        compilerUtil.specWithComment(builder);

        builder
                .addStatement("$T __C_document = null", Document.class)
                .addStatement("$T __C_ns = new Namespace()", Namespace.class)
                .addStatement("$T subst= new StringSubstitutor(getVariableMap())", StringSubstitutor.class);

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        compilerUtil.generateSpecializedParameters(builder, the_var);

        Iterator<String> iter2 = the_context.fieldNames();
        while (iter2.hasNext()) {
            String prefix = iter2.next();
            String uri = the_context.get(prefix).textValue();
            builder.addStatement("__C_ns.register($S,subst.replace($S))", prefix, uri);  // TODO: needs substitution here, to expand the URI potentially containing *
        }


        String args = "";
        boolean first = true;
        Set<String> seen = new HashSet<>();
        for (QualifiedName q : allVars) {
            final String key = q.getLocalPart();
            seen.add(key);
            final String newName = compilerUtil.varPrefix(key);
            final JsonNode entry = the_var.path(key);
            if (entry != null && !(entry instanceof MissingNode)) {
                String s = entry.get(0).get("@id").textValue();
                // substring of s preceding first occurrence of :
                int existsColumn = s.indexOf(":");
                if (existsColumn >= 0) {
                    String pre = s.substring(0, existsColumn);
                    if (pre != null && !pre.isEmpty() && the_context.get(pre) == null) {
                        throw new InvalidCaseException("CompilerExpansionBuilder (line 629): Reference to prefix '" + pre + "' in '" + s + "' for key '" + key + "', not available in context " + the_context.toPrettyString());
                    }
                }
                JsonNode toEscapeEntry = entry.get(0).get("@escape");
                boolean toEscape = toEscapeEntry != null && toEscapeEntry.textValue() != null && "true".equals(toEscapeEntry.textValue());
                String s2 = "\"" + s.replace("*", "\" + $N + \"") + "\"";
                if (toEscape) {
                    builder.addStatement("$T $N=($N==null)?null:__C_ns.stringToQualifiedName(" + s2 + ",pf,false)", QualifiedName.class, newName, key, key);
                } else {
                    builder.addStatement("$T $N=($N==null)?null:__C_ns.stringToQualifiedName(" + s2 + ",pf)", QualifiedName.class, newName, key, key);

                }
            } else {
                // TODO: check if it was a gensym, because then i can generate it!
                builder.addStatement("$T $N=null", QualifiedName.class, newName);
            }
            if (first) {
                first = false;
                args = newName;
            } else {
                args = args + ", " + newName;
            }
        }

        for (QualifiedName q : allAtts) {
            final String key = q.getLocalPart();
            String newName = key;
            if (!(seen.contains(key))) {
                final JsonNode entry = the_var.path(key);
                JsonNode jentry;
                if (entry != null && !(entry instanceof MissingNode) && ((jentry = entry.get(0).get("@id")) != null)) {
                    String s = jentry.textValue();
                    String s2 = "\"" + s.replace("*", "\" + $N + \"") + "\"";
                    newName = compilerUtil.attPrefix(key);
                    builder.addStatement("$T $N=($N==null)?null:__C_ns.stringToQualifiedName(" + s2 + ",pf)", QualifiedName.class, newName, key, key);
                }
                if (first) {
                    first = false;
                    args = newName;
                } else {
                    args = args + ", " + newName;
                }
            }
        }

        builder.addStatement("__C_document = generator(" + args + ")");


        builder.addStatement("return __C_document");


        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateFactoryMethodWithContinuation(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name,  String template, String packge, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get("T"))
                .addTypeVariable(TypeVariableName.get("T"));

        compilerUtil.specWithComment(builder);

        builder
                .addStatement("$T __C_result = null", TypeVariableName.get("T"))
                .addStatement("$T __C_ns = new Namespace()", Namespace.class)
                .addStatement("$T subst= new StringSubstitutor(getVariableMap())", StringSubstitutor.class);

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        compilerUtil.generateSpecializedParameters(builder, the_var);

        builder.addParameter(processorInterfaceType(template, packge), "processor");

        Iterator<String> iter2 = the_context.fieldNames();
        while (iter2.hasNext()) {
            String prefix = iter2.next();
            String uri = the_context.get(prefix).textValue();
            builder.addStatement("__C_ns.register($S,subst.replace($S))", prefix, uri);  // TODO: needs substitution here, to expand the URI potentially containing *
        }


        Map<String,String> translator=new HashMap<>();

        Set<String> seen = new HashSet<>();
        for (QualifiedName q : allVars) {
            final String key = q.getLocalPart();
            seen.add(key);
            final String newName = compilerUtil.varPrefix(key);
            translator.put(key,newName);
            final JsonNode entry = the_var.path(key);
            if (entry != null && !(entry instanceof MissingNode)) {
                String s = entry.get(0).get("@id").textValue();
                JsonNode toEscapeEntry = entry.get(0).get("@escape");
                boolean toEscape = toEscapeEntry != null && toEscapeEntry.textValue() != null && "true".equals(toEscapeEntry.textValue());
                String s2 = "\"" + s.replace("*", "\" + $N + \"") + "\"";
                if (toEscape) {
                    builder.addStatement("$T $N=($N==null)?null:__C_ns.stringToQualifiedName(" + s2 + ",pf,false)", QualifiedName.class, newName, key, key);
                } else {
                    builder.addStatement("$T $N=($N==null)?null:__C_ns.stringToQualifiedName(" + s2 + ",pf)", QualifiedName.class, newName, key, key);

                }
            } else {
                // TODO: check if it was a gensym, because then i can generate it!
                builder.addStatement("$T $N=null", QualifiedName.class, newName);
            }
        }

        for (QualifiedName q : allAtts) {
            final String key = q.getLocalPart();
            if (!(seen.contains(key))) {
                final JsonNode entry = the_var.path(key);
                JsonNode jentry;
                if (entry != null && !(entry instanceof MissingNode) && ((jentry = entry.get(0).get("@id")) != null)) {
                    String s = jentry.textValue();
                    String s2 = "\"" + s.replace("*", "\" + $N + \"") + "\"";
                    final String newName = compilerUtil.attPrefix(key);
                    translator.put(key,newName);
                    builder.addStatement("$T $N=($N==null)?null:__C_ns.stringToQualifiedName(" + s2 + ",pf)", QualifiedName.class, newName, key, key);
                }
            }
        }

        final String args = compilerUtil.generateArgumentsListForCall(the_var,translator);

        builder.addStatement("__C_result = processor.call(" + args + ")");


        builder.addStatement("return __C_result");


        MethodSpec method = builder.build();

        return method;
    }



    public MethodSpec generateFactoryMethodWithArray(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PUBLIC)
                .returns(Document.class);

        compilerUtil.specWithComment(builder);


        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        builder.addParameter(Object[].class, "__record");

        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
            final String converter = compilerUtil.getConverterForDeclaredType(atype);
            if (converter == null) {
                String statement = "$T $N=($T) __record[" + count + "]";
                builder.addStatement(statement, atype, key, atype);
            } else {
                String statement = "$T $N=$N(__record[" + count + "])";
                builder.addStatement(statement, atype, key, converter);
            }
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }
        builder.addStatement("return make(" + args + ")");


        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateFactoryMethodWithArrayAndContinuation(String name, String template, String packge, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get("T"))
                .addTypeVariable(TypeVariableName.get("T"));

        compilerUtil.specWithComment(builder);




        JsonNode the_var = bindings_schema.get("var");

        builder.addParameter(Object[].class, "__record");
        builder.addParameter(processorInterfaceType(template, packge), Constants.PROCESSOR);

        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
            final String converter = compilerUtil.getConverterForDeclaredType(atype);
            if (converter == null) {
                String statement = "$T $N=($T) __record[" + count + "]";
                builder.addStatement(statement, atype, key, atype);
            } else {
                String statement = "$T $N=$N(__record[" + count + "])";
                builder.addStatement(statement, atype, key, converter);
            }
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }

        builder.addStatement("return make(" + args + ",_processor)");


        MethodSpec method = builder.build();

        return method;
    }

    public MethodSpec generateUniqueRecordFactoryMethodWithArrayAndContinuation(String name, String template, String packge, TemplateBindingsSchema bindingsSchema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get("T"))
                .addTypeVariable(TypeVariableName.get("T"));

        compilerUtil.specWithComment(builder);

        builder.addParameter(processorInterfaceType(template, packge), Constants.PROCESSOR);

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        int count = 1;
        for (String variable: variables) {
            List<Descriptor> descriptors = theVar.get(variable);
            if (descriptors!=null) {
                for (Descriptor descriptor: descriptors) {
                    final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(theVar, variable);
                    final String converter = compilerUtil.getConverterForDeclaredType(atype);
                    if (descriptor instanceof NameDescriptor) {

                        if (converter == null) {
                            String statement = "$T $N=($T) \"" + count + "\"";
                            builder.addStatement(statement, atype, variable, atype);
                        } else {
                            String statement = "$T $N=$N(" + count + ")";
                            builder.addStatement(statement, atype, variable, converter);
                        }
                    } else {
                        String statement = "$T $N=$N /* $L */";
                        builder.addStatement(statement, atype, variable, "null", count);
                    }
                    count++;
                }
            }
        }
        CodeBlock argsList=makeArgsList(variables);
        builder.addStatement("return make($L,$N)", argsList, Constants.PROCESSOR);

        MethodSpec method = builder.build();

        return method;
    }


    public MethodSpec generateMain(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args");

        compilerUtil.specWithComment(builder);

        builder
                .addStatement("$T fr=$T.dynamicLoad()", Framework.class, Framework.class)
                .addStatement("$T pf=fr.getFactory()", ProvFactory.class)
                //.addStatement("$T pf2=org.openprovenance.prov.interop.InteropFramework.getDefaultFactory()", ProvFactory.class)
                .addStatement("$N me=new $N(pf)", name, name);


        for (QualifiedName q : allVars) {
            builder.addStatement("$T $N=pf.newQualifiedName($S,$S,$S)", QualifiedName.class, compilerUtil.varPrefix(q.getLocalPart()), "http://example.org/", q.getLocalPart(), "ex");
        }

        JsonNode the_var2 = (bindings_schema == null) ? null : bindings_schema.get("var");

        for (QualifiedName q : allAtts) {
            String declaredType = null;
            if (the_var2 != null) {
                Iterator<String> iter = the_var2.fieldNames();

                while (iter.hasNext()) {
                    String key = iter.next();
                    if (q.getLocalPart().equals(key)) {
                        declaredType = compilerUtil.getDeclaredType(the_var2, key);
                    }
                }
            }
            String example = compilerUtil.generateExampleForType(declaredType, q.getLocalPart(), pFactory);

            builder.addStatement("$T $N=$S", String.class, compilerUtil.attPrefix(q.getLocalPart()), example);
        }

        String args = "";
        boolean first = true;
        Set<String> seen = new HashSet<>();
        for (QualifiedName q : allVars) {
            if (first) {
                first = false;
                args = compilerUtil.varPrefix(q.getLocalPart());
            } else {
                args = args + ", " + compilerUtil.varPrefix(q.getLocalPart());
            }
            seen.add(q.getLocalPart());
        }


        for (QualifiedName q : allAtts) {
            if (!(seen.contains(q.getLocalPart()))) {
                final String key = compilerUtil.attPrefix(q.getLocalPart());
                if (first) {
                    first = false;
                    args = key;
                } else {
                    args = args + ", " + key;
                }
            }
        }


        builder.addStatement("$T document=me.generator(" + args + ")", Document.class);
        builder.addStatement("fr.writeDocument($T.out,document,$T.PROVN)", System.class, Formats.ProvFormat.class);


        if (bindings_schema != null) {
            JsonNode the_var = bindings_schema.get("var");

            Iterator<String> iter = the_var.fieldNames();
            args = "";
            first = true;
            int count = 0;
            while (iter.hasNext()) {
                String key = iter.next();
                if (first) {
                    first = false;
                    args = compilerUtil.createExamplar(the_var, key, count++, pFactory);
                } else {
                    args = args + ", " + compilerUtil.createExamplar(the_var, key, count++, pFactory);
                }
            }


            builder.addStatement("document=me.make(" + args + ")");
            builder.addStatement("fr.writeDocument($T.out,document,$T.PROVN)", System.class, Formats.ProvFormat.class);


        }


        MethodSpec method = builder.build();

        return method;
    }


    // move to expansion subpackage

    public MethodSpec typeManagerGenerator(String templateName, String packge) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getTypeManager")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packge,compilerUtil.templateNameClass(templateName)+"TypeManagement"));

        compilerUtil.specWithComment(builder);

        builder.addParameter(ParameterSpec.builder(Map_QN_S_of_String,"knownTypeMap").build());
        builder.addParameter(ParameterSpec.builder(Map_QN_S_of_String,"unknownTypeMap").build());
        builder.addParameter(ParameterSpec.builder(Map_S_Map_S_to_Function,"propertyConverters").build());

        builder.addParameter(ParameterSpec.builder(Map_QN_Col_of_String,"idata").build());
        builder.addParameter(ParameterSpec.builder(Map_S_Map_S_to_Function,"idataConverters").build());


        builder.addStatement(
                "return new $T($N,$N,$N,$N,$N,$N)",
                ClassName.get(packge,compilerUtil.templateNameClass(templateName)+"TypeManagement"),
                "pf",
                "knownTypeMap",
                "unknownTypeMap",
                "propertyConverters",
                "idata",
                "idataConverters");

        return builder.build();

    }


}
