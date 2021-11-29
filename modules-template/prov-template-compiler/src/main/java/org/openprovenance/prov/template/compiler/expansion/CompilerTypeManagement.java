package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.CompilerClient;
import org.openprovenance.prov.template.compiler.CompilerUtil;

import javax.lang.model.element.Modifier;
import java.lang.reflect.ParameterizedType;
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



        StatementTypeAction action = new StatementTypeAction(pFactory, allVars, allAtts, null, null, "__C_document.getStatementOrBundle()", bindings_schema, knownTypes, unknownTypes);
        for (StatementOrBundle s : doc.getStatementOrBundle()) {
            u.doAction(s, action);
        }


        for (QualifiedName q : allVars) {
            mbuilder.addParameter(QualifiedName.class, q.getLocalPart());

            mbuilder.addComment("Declare $N", q.getLocalPart());
            knownTypes.getOrDefault(q.getUri(), new LinkedList<>()).forEach( type -> {
                mbuilder.addStatement("knownTypeMap.computeIfAbsent($N, k -> new $T<>())", q.getLocalPart(), HashSet.class);
                mbuilder.addStatement("knownTypeMap.get($N).add($S)", q.getLocalPart(), type);
            });

            unknownTypes.getOrDefault(q.getUri(), new LinkedList<>()).forEach( type -> {
                    mbuilder.addStatement("unknownTypeMap.computeIfAbsent($N, k -> new $T<>())", q.getLocalPart(), HashSet.class);
                    mbuilder.addStatement("unknownTypeMap.get($N).add((($T)$N).getUri())", q.getLocalPart(), QualifiedName.class,type.substring(type.indexOf("#")+1));
            });
        }
        for (QualifiedName q : allAtts) {
            if (allVars.contains(q)) {
                // no need to redeclare
            } else {
                mbuilder.addParameter(Object.class, q.getLocalPart()); // without type declaration, any object may be accepted, assuming this is not a q also in allVars.
            }
        }




        mbuilder.addStatement("return null");


        MethodSpec.Builder cbuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        cbuilder.addStatement("this.knownTypeMap=knownTypeMap");
        cbuilder.addStatement("this.unknownTypeMap=unknownTypeMap");

        cbuilder.addParameter(Map_QN_S_of_String,"knownTypeMap");
        cbuilder.addParameter(Map_QN_S_of_String,"unknownTypeMap");



        TypeSpec.Builder builder = compilerUtil.generateTypeManagementClass(name);
        builder.addTypeVariable(TypeVariableName.get("T"));

        final ParameterizedTypeName superinterface=ParameterizedTypeName.get(ClassName.get(packge,name + "Interface"),TypeVariableName.get("T"));
        builder.addSuperinterface(superinterface);




        builder.addField(Map_QN_S_of_String,"knownTypeMap", Modifier.PRIVATE);
        builder.addField(Map_QN_S_of_String,"unknownTypeMap", Modifier.PRIVATE);
        builder.addMethod(cbuilder.build());
        builder.addMethod(mbuilder.build());

        TypeSpec bean = builder.build();



        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", getClass().getName()+"generateTypeDeclaration_aux()", templateName)
                .build();

        return myfile;
    }


}
