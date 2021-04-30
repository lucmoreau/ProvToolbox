package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;

public class CompilerProcessor {
    private final ProvFactory pFactory;
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerProcessor(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }

    public JavaFile generateProcessor(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun=u.getBundle(doc).get(0);

        Set<QualifiedName> allVars=new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts=new HashSet<QualifiedName>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);



        IndexedDocument indexed=new IndexedDocument(pFactory, pFactory.newDocument(),true);
        u.forAllStatement(bun.getStatement(), indexed);


        return generateProcessor_aux(doc, allVars,allAtts,name, templateName, packge, resource, bindings_schema, indexed);
    }

    public TypeSpec.Builder generateProcessorClassInit(String name, String packge, String supername) {
        return TypeSpec.interfaceBuilder(name).addTypeVariable(TypeVariableName.get("T"))
              //  .addSuperinterface(ClassName.get(packge,supername))
                .addModifiers(Modifier.PUBLIC);
    }

    private JavaFile generateProcessor_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema, IndexedDocument indexed) {

        TypeSpec.Builder builder = generateProcessorClassInit(name,ConfigProcessor.CLIENT_PACKAGE,"Bean");

        final String loggerName = compilerUtil.loggerName(templateName);
        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("process")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(String.class);
        //if (debugComment) builder.addComment("Generated by method $N", getClass().getName()+".generateClientMethod()");


        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        CodeBlock.Builder jdoc = CodeBlock.builder();

        JsonNode the_documentation = bindings_schema.get("@documentation");
        JsonNode the_return = bindings_schema.get("@return");
        String docString = compilerUtil.noNode(the_documentation) ? "No @documentation" : the_documentation.textValue();
        String retString = compilerUtil.noNode(the_return) ? "@return not specified" : the_return.textValue();
        jdoc.add(docString);
        jdoc.add("\n\n");


        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();


            mbuilder.addParameter(compilerUtil.getJavaTypeForDeclaredType(the_var, key), key);


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
                jdoc.add("@param $N $L (expected type: $L)\n", key, documentation, type);
            } else {
                jdoc.add("@param $N -- no bindings schemas \n", key);
            }

        }
        jdoc.add(retString);

        mbuilder.addJavadoc(jdoc.build());

        builder.addMethod(mbuilder.build());

        TypeSpec bean=builder.build();
        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N",getClass().getName(), templateName)
                .build();

        return myfile;

    }

}
