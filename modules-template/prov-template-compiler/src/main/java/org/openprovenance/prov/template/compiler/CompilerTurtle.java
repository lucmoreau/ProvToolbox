package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.SubtypeTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.InJsonValue;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * T36 stage 5 (R7, ruled): the Turtle file of a catalogue. It says what the compiler knows and has checked, in terms of a
 * small vocabulary {@code tpl:}:
 * <ul>
 * <li>a template that keeps values in a JSON text variable, or has subtypes, is an {@code owl:Class};</li>
 * <li>each of its in-JSON values is a datatype property of that class ({@code rdfs:domain}, {@code rdfs:range}), named
 *     {@code <template>-<value>} so that two templates may use one name, with the carrier it is in ({@code tpl:inJson}) and its
 *     key ({@code tpl:key});</li>
 * <li>a subtype is an {@code owl:Class}, named from the subtype's {@code name}, and {@code rdfs:subClassOf} its template: every
 *     record of the subtype is a record of the template. It {@code tpl:fixes} variables to values, {@code tpl:requires} and
 *     {@code tpl:mayCarry} in-JSON values.</li>
 * </ul>
 * The terms are in the namespace the catalogue gives ({@code turtleNamespace}). An application's act types (their labels and
 * comments) stay the application's to declare.
 */
public class CompilerTurtle {

    public static final String TPL = "http://openprovenance.org/ns/tpl#";

    private final CompilerUtil compilerUtil;

    public CompilerTurtle(CompilerUtil compilerUtil) {
        this.compilerUtil = compilerUtil;
    }

    public void generateTurtle(TemplatesProjectConfiguration configs, String root_dir) {
        new File(root_dir).mkdirs();
        try {
            Files.write(new File(root_dir + "/" + configs.turtle).toPath(), render(configs).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String render(TemplatesProjectConfiguration configs) {
        if (configs.turtleNamespace == null || configs.turtleNamespace.trim().isEmpty()) {
            throw new IllegalArgumentException("T36: the catalogue names a Turtle file (" + configs.turtle + ") but no turtleNamespace for its terms");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("# Generated automatically by ProvToolbox (T36) for the catalogue ").append(configs.name).append(".\n");
        sb.append("# The templates that keep values in a JSON text variable or have subtypes, those values, and the subtypes.\n\n");
        sb.append("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n");
        sb.append("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n");
        sb.append("@prefix owl: <http://www.w3.org/2002/07/owl#> .\n");
        sb.append("@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n");
        sb.append("@prefix tpl: <").append(TPL).append("> .\n");
        sb.append("@prefix : <").append(iri(configs.turtleNamespace)).append("> .\n\n");

        vocabulary(sb);

        List<SubtypeTemplateCompilerConfig> subtypes = CompilerSubtype.subtypes(configs);
        if (configs.templates != null) {
            for (TemplateCompilerConfig c : configs.templates) {
                if (!(c instanceof SimpleTemplateCompilerConfig)) continue;
                SimpleTemplateCompilerConfig simple = (SimpleTemplateCompilerConfig) c;
                TemplateBindingsSchema schema = compilerUtil.getBindingsSchema(simple);
                List<SubtypeTemplateCompilerConfig> mine = subtypes.stream().filter(st -> st.refines.equals(simple.fullyQualifiedName)).collect(Collectors.toList());
                if (schema.getInJson().isEmpty() && mine.isEmpty()) continue;
                template(sb, simple, schema, mine);
            }
        }
        return sb.toString();
    }

    private void vocabulary(StringBuilder sb) {
        sb.append("# The vocabulary\n");
        property(sb, "fullyQualifiedName", "owl:DatatypeProperty", "fully qualified name", "The catalogue's fully qualified name of the template or subtype a class stands for.");
        property(sb, "inJson", "owl:DatatypeProperty", "in JSON", "The text variable (the carrier) of the template that holds this value, as a key of its JSON object.");
        property(sb, "key", "owl:DatatypeProperty", "key", "The key of the value in its carrier's JSON object, when it differs from the value's name.");
        property(sb, "fixes", "owl:ObjectProperty", "fixes", "An input of the template that every record of the subtype gives a constant: a node with tpl:variable and tpl:value.");
        property(sb, "variable", "owl:DatatypeProperty", "variable", "The name of the variable a subtype fixes.");
        property(sb, "value", "owl:DatatypeProperty", "value", "The constant a subtype fixes a variable to.");
        property(sb, "requires", "owl:ObjectProperty", "requires", "An in-JSON value every record of the subtype must give.");
        property(sb, "mayCarry", "owl:ObjectProperty", "may carry", "An in-JSON value a record of the subtype may give. A carrier is open: other keys are allowed.");
        sb.append("\n");
    }

    private void property(StringBuilder sb, String name, String type, String label, String comment) {
        sb.append("tpl:").append(name).append(" a ").append(type).append(" ;\n")
                .append("    rdfs:label ").append(literal(label)).append(" ;\n")
                .append("    rdfs:comment ").append(literal(comment)).append(" .\n");
    }

    private void template(StringBuilder sb, SimpleTemplateCompilerConfig simple, TemplateBindingsSchema schema, List<SubtypeTemplateCompilerConfig> subtypes) {
        String cls = ":" + local(simple.name);
        sb.append("# The template ").append(simple.fullyQualifiedName).append("\n");
        sb.append(cls).append(" a owl:Class ;\n")
                .append("    rdfs:label ").append(literal(simple.name)).append(" ;\n");
        if (schema.getDocumentation() != null) sb.append("    rdfs:comment ").append(literal(schema.getDocumentation())).append(" ;\n");
        sb.append("    tpl:fullyQualifiedName ").append(literal(simple.fullyQualifiedName)).append(" .\n\n");

        for (InJsonValue v : schema.getInJson().values()) {
            sb.append(property(simple, v)).append(" a owl:DatatypeProperty ;\n")
                    .append("    rdfs:label ").append(literal(v.getName())).append(" ;\n");
            if (v.getDocumentation() != null) sb.append("    rdfs:comment ").append(literal(v.getDocumentation())).append(" ;\n");
            sb.append("    rdfs:domain ").append(cls).append(" ;\n")
                    .append("    rdfs:range ").append(range(v.getType())).append(" ;\n")
                    .append("    tpl:inJson ").append(literal(v.getCarrier())).append(" ");
            if (!v.getKey().equals(v.getName())) sb.append(";\n    tpl:key ").append(literal(v.getKey())).append(" ");
            sb.append(".\n\n");
        }

        for (SubtypeTemplateCompilerConfig st : subtypes) {
            sb.append("# The subtype ").append(st.fullyQualifiedName).append("\n");
            sb.append(":").append(local(st.name)).append(" a owl:Class ;\n")
                    .append("    rdfs:label ").append(literal(st.name)).append(" ;\n")
                    .append("    rdfs:subClassOf ").append(cls).append(" ;\n");
            for (Map.Entry<String, Object> f : st.fixed.entrySet()) {
                sb.append("    tpl:fixes [ tpl:variable ").append(literal(f.getKey())).append(" ; tpl:value ")
                        .append(constant(schema, f.getKey(), f.getValue())).append(" ] ;\n");
            }
            for (Map.Entry<String, String> k : st.carries.entrySet()) {
                sb.append("    tpl:").append(SubtypeTemplateCompilerConfig.REQUIRED.equals(k.getValue()) ? "requires " : "mayCarry ")
                        .append(property(simple, schema.getInJson().get(k.getKey()))).append(" ;\n");
            }
            sb.append("    tpl:fullyQualifiedName ").append(literal(st.fullyQualifiedName)).append(" .\n\n");
        }
    }

    // ---- terms and literals ------------------------------------------------------------------------------------------------

    private static String property(SimpleTemplateCompilerConfig simple, InJsonValue v) {
        return ":" + local(simple.name) + "-" + local(v.getName());
    }

    /** A local name: letters, digits and underscores; anything else is replaced by an underscore. */
    static String local(String name) {
        String s = name.replaceAll("[^A-Za-z0-9_]", "_");
        return Character.isDigit(s.charAt(0)) ? "_" + s : s;
    }

    private static String iri(String namespace) {
        return namespace.replace(">", "%3E").replace(" ", "%20");
    }

    static String range(String type) {
        switch (type) {
            case "json": return "rdf:JSON";
            default: return type;   // xsd:int, xsd:long, xsd:string, xsd:boolean, xsd:float, xsd:double, xsd:dateTime, xsd:date
        }
    }

    private String constant(TemplateBindingsSchema schema, String variable, Object value) {
        String type = ConfigProcessor.descriptorUtils.getFromDescriptor(schema.getVar().get(variable).get(0),
                org.openprovenance.prov.template.descriptors.AttributeDescriptor::getType, nd -> null);
        switch (type) {
            case "xsd:boolean": return String.valueOf(value);
            case "xsd:string": return literal(String.valueOf(value));
            default: return literal(String.valueOf(value)) + "^^" + type;
        }
    }

    static String literal(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '"': sb.append("\\\""); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default: sb.append(c);
            }
        }
        return sb.append('"').toString();
    }
}
