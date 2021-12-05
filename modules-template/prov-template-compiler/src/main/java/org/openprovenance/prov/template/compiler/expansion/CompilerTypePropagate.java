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
import static org.openprovenance.prov.template.compiler.expansion.CompilerExpansionBuilder.*;

public class CompilerTypePropagate {
    public static final String ENTRY = "entry";
    private final CompilerUtil compilerUtil=new CompilerUtil();
    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerClient compilerClient;
    private final boolean debugComment;


    public CompilerTypePropagate(boolean withMain, CompilerClient compilerClient, ProvFactory pFactory, boolean debugComment) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerClient=compilerClient;
        this.debugComment=debugComment;
    }

    public JavaFile generateTypeDeclaration(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts = new HashSet<QualifiedName>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateTypeDeclaration_aux(doc, allVars, allAtts, name, templateName, packge, resource, bindings_schema);

    }


    static public final ParameterizedTypeName Map_QN_S_of_String=ParameterizedTypeName.get(ClassName.get(Map.class),TypeName.get(QualifiedName.class),ParameterizedTypeName.get(ClassName.get(Set.class),TypeName.get(String.class)));

    public JavaFile generateTypeDeclaration_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {
        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("call")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get("T"));

        final Map<String,Collection<String>> knownTypes=new HashMap<>();
        final Map<String,Collection<String>> unknownTypes=new HashMap<>();


        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        compilerUtil.generateDocumentSpecializedParameters(mbuilder, the_var);

        StatementTypeAction action = new StatementTypeAction(pFactory, allVars, allAtts, null, null, "__C_document.getStatementOrBundle()", bindings_schema, knownTypes, unknownTypes);
        for (StatementOrBundle s : doc.getStatementOrBundle()) {
            u.doAction(s, action);
        }


        int count=1;
        for (QualifiedName q : allVars) {

            final String key = q.getLocalPart();
            final JsonNode entry = the_var.path(key);
            if (entry != null && !(entry instanceof MissingNode)) {
                mbuilder.beginControlFlow("if ($N!=null) ", q.getLocalPart());
                mbuilder.addStatement("propagateTypes_n($N,mapLevelN,mapLevelNP1,$L)", q.getLocalPart(), count);
                mbuilder.endControlFlow();
                count++;
            } else {
            }
        }



        mbuilder.addStatement("return null");


        MethodSpec.Builder cbuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        cbuilder.addStatement("this.record=record");
        cbuilder.addStatement("this.mapLevelN=mapLevelN");
        cbuilder.addStatement("this.mapLevelNP1=mapLevelNP1");
        cbuilder.addStatement("this.builder=builder");
        cbuilder.addStatement("this.successors=builder.getTypedSuccessors()");


        cbuilder.addParameter(ParameterSpec.builder(recordType,"record").build());
        cbuilder.addParameter(ParameterSpec.builder(levelNMapType,"mapLevelN").build());
        cbuilder.addParameter(ParameterSpec.builder(levelNP1CMapType,"mapLevelNP1").build());
        cbuilder.addParameter(ClassName.get("org.openprovenance.prov.client","Builder"),"builder").build();



        TypeSpec.Builder builder = compilerUtil.generateTypePropagateClass(name);
        builder.addTypeVariable(TypeVariableName.get("T"));

        final ParameterizedTypeName superinterface=ParameterizedTypeName.get(ClassName.get(packge,name + "Interface"),TypeVariableName.get("T"));
        builder.addSuperinterface(superinterface);



        builder.addField(recordType, "record",Modifier.PRIVATE);
        builder.addField(levelNMapType, "mapLevelN",Modifier.PRIVATE);
        builder.addField(levelNP1CMapType, "mapLevelNP1",Modifier.PRIVATE);
        builder.addField(ClassName.get("org.openprovenance.prov.client","Builder"),"builder", Modifier.PRIVATE);
        builder.addField(successorType, "successors",Modifier.PRIVATE);


        builder.addMethod(cbuilder.build());
        builder.addMethod(mbuilder.build());
        builder.addMethod(generateTypePropagatorN());


        TypeSpec bean = builder.build();



        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", getClass().getName()+".generateTypeDeclaration_aux()", templateName)
                .build();

        return myfile;
    }


    public MethodSpec generateTypePropagatorN() {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("propagateTypes_n")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        builder.addParameter(ParameterSpec.builder(QualifiedName.class, ENTRY).build());
        builder.addParameter(ParameterSpec.builder(levelNMapType,"mapLevelN").build());
        builder.addParameter(ParameterSpec.builder(levelNP1CMapType,"mapLevelNP1").build());
        builder.addParameter(ParameterSpec.builder(Integer.class,"count").build());

        if (debugComment)
            builder.addComment("Generated by method $N", getClass().getName() + ".generateTypePropagatorN()");



        String tmpVar="tmp";

        builder.beginControlFlow("if ($N!=null)", ENTRY);
        builder.addStatement("String uri=$N.getUri()",ENTRY);
        builder.addStatement("Integer $N=mapLevelN.get(uri)", tmpVar);
        builder.beginControlFlow("if ($N!=null)",tmpVar);
        builder.addStatement("System.err.println(\"tmp \" + $N)",tmpVar);
        builder.addStatement("$T the_type=successors.get($N)", TypeName.get(int[].class), "count");
        builder.beginControlFlow("if (the_type!=null && the_type.length!=0)");
        builder.addStatement("System.err.println(\"the_type \" + the_type[0])");
        builder.addStatement("System.err.println(\"the_type \" + the_type[1])");
        builder.addStatement("String uri2=(($T)($N[the_type[0]])).getUri()",QualifiedName.class, "record");

        builder.addStatement("mapLevelNP1.computeIfAbsent(uri2, k -> new $T<>())",  LinkedList.class); //store in Lists initially

        builder.addStatement("mapLevelNP1.get(uri2).add(new int[] { the_type[0], the_type[1], $N, $N })", tmpVar, "count");
        builder.endControlFlow();
        builder.endControlFlow();
        builder.endControlFlow();


        return builder.build();
    }



}
