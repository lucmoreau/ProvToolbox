package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;

public class CompilerSimpleBean {
    private final ProvFactory pFactory;
    private final CompilerUtil compilerUtil = new CompilerUtil();

    public CompilerSimpleBean(ProvFactory pFactory) {
        this.pFactory = pFactory;
    }

    public JavaFile generateSimpleBean(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts = new HashSet<QualifiedName>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);


        IndexedDocument indexed = new IndexedDocument(pFactory, pFactory.newDocument(), true);
        u.forAllStatement(bun.getStatement(), indexed);


        return generateSimpleBean_aux(doc, allVars, allAtts, name, templateName, packge, resource, bindings_schema, indexed);
    }

    public TypeSpec.Builder generateBeanClassInit(String name, String packge, String supername) {
        return TypeSpec.classBuilder(name)
                //  .addSuperinterface(ClassName.get(packge,supername))
                .addModifiers(Modifier.PUBLIC);
    }

    private JavaFile generateSimpleBean_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema, IndexedDocument indexed) {

        TypeSpec.Builder builder = generateBeanClassInit(name, ConfigProcessor.CLIENT_PACKAGE, "Bean");


        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        FieldSpec.Builder b0 = FieldSpec.builder(String.class, "isA");
        b0.addModifiers(Modifier.PUBLIC);
        b0.addModifiers(Modifier.FINAL);
        b0.initializer("$S",templateName);

        builder.addField(b0.build());




        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();


            FieldSpec.Builder b = FieldSpec.builder(compilerUtil.getJavaTypeForDeclaredType(the_var, key), key);
            b.addModifiers(Modifier.PUBLIC);

            final JsonNode entry = the_var.path(key);
            if (entry != null && !(entry instanceof MissingNode)) {
                JsonNode firstNode = entry.get(0);
                if (firstNode instanceof ArrayNode) {
                    firstNode = ((ArrayNode) firstNode).get(0);
                }
                final JsonNode jsonNode = firstNode.get("@documentation");
                String documentation = compilerUtil.noNode(jsonNode) ? "-- no @documentation" : jsonNode.textValue();
                final JsonNode jsonNode2 = firstNode.get("@type");
                String type = compilerUtil.noNode(jsonNode2) ? "xsd:string" : jsonNode2.textValue();
                b.addJavadoc("$N: $L (expected type: $L)\n", key, documentation, type);
            } else {
                b.addJavadoc("$N -- no bindings schemas \n", key);
            }

            builder.addField(b.build());

        }

        MethodSpec mbuild = generateInvokeProcessor(allVars, allAtts, name, templateName, packge, bindings_schema);

        builder.addMethod(mbuild);


        TypeSpec bean = builder.build();

        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", getClass().getName(), templateName)
                .build();

        return myfile;

    }

    public MethodSpec generateInvokeProcessor(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String template, String packge, JsonNode bindings_schema) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("process")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class);

        final String loggerName = compilerUtil.loggerName(template);

        builder.addParameter(ClassName.get(packge, compilerUtil.processorNameClass(template)), "processor");

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            if (count > 1) args = args + ", ";
            args = args + key;
            count++;
        }

        builder.addStatement("return processor.process("  + args + ")");

        return builder.build();

    }


}
