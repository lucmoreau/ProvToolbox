package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.squareup.javapoet.*;
import org.apache.commons.text.StringSubstitutor;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.ExpandAction;
import org.openprovenance.prov.template.expander.ExpandUtil;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBuilder {
    private final CompilerClient compilerClient;
    private final boolean withMain;
    private final CompilerUtil compilerUtil=new CompilerUtil();
    
    private final ProvFactory pFactory;

    public CompilerBuilder(boolean withMain, CompilerClient compilerClient, ProvFactory pFactory) {
        this.compilerClient = compilerClient;
        this.pFactory=pFactory;
        this.withMain=withMain;
    }


    public JavaFile generateBuilderSpecification(Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts = new HashSet<QualifiedName>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateBuilderSpecification_aux(doc, allVars, allAtts, name, templateName, packge, resource, bindings_schema);

    }

    JavaFile generateBuilderSpecification_aux(Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema) {


        TypeSpec.Builder builder = compilerUtil.generateClassBuilder2(name);

        Hashtable<QualifiedName, String> vmap = generateQualifiedNames(doc, builder);

        builder.addMethod(compilerUtil.generateConstructor2(vmap));

        builder.addMethod(generateTemplateGenerator(allVars, allAtts, doc, vmap, bindings_schema));

        builder.addMethod(compilerClient.nameAccessorGenerator(templateName));

        builder.addMethod(compilerClient.clientAccessorGenerator(templateName,packge+".client"));

        if (withMain) builder.addMethod(generateMain(allVars, allAtts, name, bindings_schema));

        if (bindings_schema != null) {
            builder.addMethod(generateFactoryMethod(allVars, allAtts, name, bindings_schema));
            builder.addMethod(generateFactoryMethodWithArray(allVars, allAtts, name, bindings_schema));
        }


        // System.out.println(allVars);

        TypeSpec bean = builder.build();

        JavaFile myfile = JavaFile.builder(packge, bean)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", getClass().getName(), templateName)
                .build();

        return myfile;
    }

    public MethodSpec generateTemplateGenerator(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, Document doc, Hashtable<QualifiedName, String> vmap, JsonNode bindings_schema) {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("generator")
                .addModifiers(Modifier.PUBLIC)
                .returns(Document.class)
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

    public Hashtable<QualifiedName, String> generateQualifiedNames(Document doc, TypeSpec.Builder builder) {
        Bundle bun = u.getBundle(doc).get(0);
        Set<QualifiedName> set = new HashSet<QualifiedName>();
        compilerUtil.allQualifiedNames(bun, set, pFactory);
        set.remove(pFactory.newQualifiedName(ExpandUtil.TMPL_NS, ExpandUtil.LABEL, ExpandUtil.TMPL_PREFIX));
        set.add(pFactory.getName().PROV_LABEL);
        Hashtable<QualifiedName, String> qnVariables = new Hashtable<QualifiedName, String>();
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

    public MethodSpec generateFactoryMethod(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PUBLIC)
                .returns(Document.class)
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
        Set<String> seen = new HashSet<String>();
        for (QualifiedName q : allVars) {
            final String key = q.getLocalPart();
            seen.add(key);
            final String newName = compilerUtil.varPrefix(key);
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




    public MethodSpec generateFactoryMethodWithArray(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, JsonNode bindings_schema) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PUBLIC)
                .returns(Document.class);

        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");

        builder.addParameter(Object[].class, "record");

        int count = 1;
        Iterator<String> iter = the_var.fieldNames();
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();
            final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
            final String converter = compilerUtil.getConverterForDeclaredType(atype);
            if (converter == null) {
                String statement = "$T $N=($T) record[" + count + "]";
                builder.addStatement(statement, atype, key, atype);
            } else {
                String statement = "$T $N=$N(record[" + count + "])";
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



    public MethodSpec generateMain(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, JsonNode bindings_schema) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T pf=org.openprovenance.prov.interop.InteropFramework.getDefaultFactory()", ProvFactory.class)
                .addStatement("$N me=new $N(pf)", name, name);

        ;
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
        Set<String> seen = new HashSet<String>();
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
        builder.addStatement("new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.Formats.ProvFormat.PROVN,document)"); //TODO make it load dynamically


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
            builder.addStatement("new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.Formats.ProvFormat.PROVN,document)");


        }


        MethodSpec method = builder.build();

        return method;
    }



}