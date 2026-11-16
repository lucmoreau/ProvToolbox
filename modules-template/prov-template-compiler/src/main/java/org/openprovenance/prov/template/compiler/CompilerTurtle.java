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
        generateTurtle(configs, root_dir, List.of());
    }

    public void generateTurtle(TemplatesProjectConfiguration configs, String root_dir, List<FlowModel> flows) {
        new File(root_dir).mkdirs();
        try {
            Files.write(new File(root_dir + "/" + configs.turtle).toPath(), render(configs, flows).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String render(TemplatesProjectConfiguration configs) {
        return render(configs, List.of());
    }

    /** @param flows T36 part E: each flow is a node with its two subtypes and, for each value it fills, where it comes from. */
    public String render(TemplatesProjectConfiguration configs, List<FlowModel> flows) {
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

        List<SubtypeTemplateCompilerConfig> subtypes = CompilerSubtype.subtypes(configs);
        boolean performative = false;   // part D: the term tpl:performative is declared only when some value has one
        if (configs.templates != null) {
            for (TemplateCompilerConfig c : configs.templates) {
                if (c instanceof SimpleTemplateCompilerConfig && !performatives(compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) c)).isEmpty()) performative = true;
            }
        }
        vocabulary(sb, performative, !flows.isEmpty());

        if (configs.templates != null) {
            for (TemplateCompilerConfig c : configs.templates) {
                if (!(c instanceof SimpleTemplateCompilerConfig)) continue;
                SimpleTemplateCompilerConfig simple = (SimpleTemplateCompilerConfig) c;
                TemplateBindingsSchema schema = compilerUtil.getBindingsSchema(simple);
                List<SubtypeTemplateCompilerConfig> mine = subtypes.stream().filter(st -> st.refines.equals(simple.fullyQualifiedName)).collect(Collectors.toList());
                if (schema.getInJson().isEmpty() && mine.isEmpty() && performatives(schema).isEmpty()) continue;
                template(sb, simple, schema, mine);
            }
        }
        for (FlowModel flow : flows) flow(sb, flow);
        return sb.toString();
    }

    /** Part E (E5, ruled): a flow, with its two subtypes and the origin of each value it fills. */
    private void flow(StringBuilder sb, FlowModel flow) {
        sb.append("# The flow ").append(flow.flow.fullyQualifiedName).append("\n");
        sb.append(":").append(local(flow.flow.name)).append(" a tpl:Flow ;\n")
                .append("    rdfs:label ").append(literal(flow.flow.name)).append(" ;\n")
                .append("    tpl:fullyQualifiedName ").append(literal(flow.flow.fullyQualifiedName)).append(" ;\n")
                .append("    tpl:from :").append(local(flow.from.name)).append(" ;\n")
                .append("    tpl:to :").append(local(flow.to.name)).append(" ;\n");
        List<String> origins = new java.util.ArrayList<>();
        for (java.util.Map.Entry<String, List<FlowModel.Item>> step : flow.steps.entrySet()) {
            for (FlowModel.Item i : step.getValue()) {
                origins.add("    tpl:origin [ tpl:step " + literal(i.step) + " ; tpl:target " + literal(i.target) + " ; tpl:kind " + literal(i.kind.word)
                        + " ; tpl:source " + literal(i.describe()) + " ]");
            }
        }
        sb.append(String.join(" ;\n", origins)).append(" .\n\n");
    }

    /** Part D (R15, ruled): the ordinary values of the template that have a performative, by name. They get a term, as in-JSON values do. */
    static Map<String, String> performatives(TemplateBindingsSchema schema) {
        Map<String, String> res = new java.util.LinkedHashMap<>();
        for (Map.Entry<String, List<org.openprovenance.prov.template.descriptors.Descriptor>> e : schema.getVar().entrySet()) {
            org.openprovenance.prov.template.descriptors.Descriptor d = e.getValue().get(0);
            if (d instanceof org.openprovenance.prov.template.descriptors.AttributeDescriptorList) {
                org.openprovenance.prov.template.descriptors.AttributeDescriptor ad = ((org.openprovenance.prov.template.descriptors.AttributeDescriptorList) d).getItems().get(0);
                if (ad.getPerformative() != null) res.put(e.getKey(), ad.getPerformative());
            }
        }
        for (InJsonValue v : schema.getInJson().values()) if (v.getPerformative() != null) res.put(v.getName(), v.getPerformative());
        return res;
    }

    private void vocabulary(StringBuilder sb, boolean performative, boolean flows) {
        sb.append("# The vocabulary\n");
        property(sb, "fullyQualifiedName", "owl:DatatypeProperty", "fully qualified name", "The catalogue's fully qualified name of the template or subtype a class stands for.");
        property(sb, "inJson", "owl:DatatypeProperty", "in JSON", "The text variable (the carrier) of the template that holds this value, as a key of its JSON object.");
        property(sb, "key", "owl:DatatypeProperty", "key", "The key of the value in its carrier's JSON object, when it differs from the value's name.");
        property(sb, "fixes", "owl:ObjectProperty", "fixes", "An input of the template that every record of the subtype gives a constant: a node with tpl:variable and tpl:value.");
        property(sb, "variable", "owl:DatatypeProperty", "variable", "The name of the variable a subtype fixes.");
        property(sb, "value", "owl:DatatypeProperty", "value", "The constant a subtype fixes a variable to.");
        property(sb, "requires", "owl:ObjectProperty", "requires", "An in-JSON value every record of the subtype must give.");
        property(sb, "mayCarry", "owl:ObjectProperty", "may carry", "An in-JSON value a record of the subtype may give. A carrier is open: other keys are allowed.");
        if (flows) {
            property(sb, "Flow", "owl:Class", "flow", "A flow connects the beans of a request (the subtype it is from) to the beans of the backend's record (the subtype it is to): every value has one origin.");
            property(sb, "from", "owl:ObjectProperty", "from", "The user subtype a flow starts from.");
            property(sb, "to", "owl:ObjectProperty", "to", "The backend subtype a flow ends in.");
            property(sb, "origin", "owl:ObjectProperty", "origin", "Where a value a flow fills comes from: a node with tpl:step, tpl:target, tpl:kind and tpl:source.");
            property(sb, "step", "owl:DatatypeProperty", "step", "The step of a flow: ask, record, answer, refused or failed.");
            property(sb, "target", "owl:DatatypeProperty", "target", "The value of a bean an origin fills.");
            property(sb, "kind", "owl:DatatypeProperty", "kind", "asked, chained, carried over, constant, supplied, as made, or absent.");
            property(sb, "source", "owl:DatatypeProperty", "source", "Where the value comes from, in words.");
        }
        if (performative) {
            property(sb, "performative", "owl:DatatypeProperty", "performative", "\"request\" or \"reply\": whose content the value is, the caller's or the service's. A value without it is the service's own.");
        }
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
            if (v.getPerformative() != null) sb.append(";\n    tpl:performative ").append(literal(v.getPerformative())).append(" ");
            sb.append(".\n\n");
        }

        // part D (R15, ruled): an ordinary value that has a performative has a term too, without tpl:inJson
        for (Map.Entry<String, String> p : performatives(schema).entrySet()) {
            if (schema.getInJson().containsKey(p.getKey())) continue;
            org.openprovenance.prov.template.descriptors.AttributeDescriptor ad =
                    ((org.openprovenance.prov.template.descriptors.AttributeDescriptorList) schema.getVar().get(p.getKey()).get(0)).getItems().get(0);
            sb.append(property(simple, p.getKey())).append(" a owl:DatatypeProperty ;\n")
                    .append("    rdfs:label ").append(literal(p.getKey())).append(" ;\n");
            if (ad.getDocumentation() != null) sb.append("    rdfs:comment ").append(literal(ad.getDocumentation())).append(" ;\n");
            sb.append("    rdfs:domain ").append(cls).append(" ;\n")
                    .append("    rdfs:range ").append(range(ad.getType())).append(" ;\n")
                    .append("    tpl:performative ").append(literal(p.getValue())).append(" .\n\n");
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
        return property(simple, v.getName());
    }

    private static String property(SimpleTemplateCompilerConfig simple, String valueName) {
        return ":" + local(simple.name) + "-" + local(valueName);
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
                org.openprovenance.prov.template.descriptors.AttributeDescriptor::getType, org.openprovenance.prov.template.descriptors.NameDescriptor::getType);
        if (type == null) return literal(String.valueOf(value));   // a node with no type: its local part
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
