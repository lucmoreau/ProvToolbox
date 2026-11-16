package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.SubtypeTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.*;

import java.util.*;

/**
 * T36 stage 2: the compile-time refusals of the {@code subtype} entries of a catalogue (design §5.1). Generation stops
 * with a {@link SubtypeException} naming the subtype and what is wrong with it. Nothing is generated for a subtype here.
 */
public class SubtypeChecker {

    private final CompilerUtil compilerUtil;
    private final String cbindingsBaseDir;

    public SubtypeChecker(CompilerUtil compilerUtil, String cbindingsBaseDir) {
        this.compilerUtil = compilerUtil;
        this.cbindingsBaseDir = cbindingsBaseDir;
    }

    public void check(TemplatesProjectConfiguration configs) {
        if (configs.templates == null) return;

        // names are unique among all the entries, so that a lookup by name finds one template
        Map<String, Integer> names = new HashMap<>(), fqns = new HashMap<>();
        for (TemplateCompilerConfig c : configs.templates) {
            if (c.name != null) names.merge(c.name, 1, Integer::sum);
            if (c.fullyQualifiedName != null) fqns.merge(c.fullyQualifiedName, 1, Integer::sum);
        }
        for (TemplateCompilerConfig c : configs.templates) {
            if (!(c instanceof SubtypeTemplateCompilerConfig)) continue;
            SubtypeTemplateCompilerConfig st = (SubtypeTemplateCompilerConfig) c;
            if (st.name == null || st.fullyQualifiedName == null || st.package_ == null) {
                throw refusal(st, "name, fullyQualifiedName and package are all required");
            }
            if (st.refines == null) throw refusal(st, "refines is required: the simple template this is a subtype of");
            if (names.get(st.name) > 1 || fqns.get(st.fullyQualifiedName) > 1) {
                throw refusal(st, "its name or fullyQualifiedName is already used by another entry of the catalogue");
            }
        }

        // the typed bean converts to and from the template's input bean, which the compiler generates for an integrator catalogue only
        if (!configs.integrator && Arrays.stream(configs.templates).anyMatch(t -> t instanceof SubtypeTemplateCompilerConfig)) {
            SubtypeTemplateCompilerConfig first = (SubtypeTemplateCompilerConfig) Arrays.stream(configs.templates)
                    .filter(t -> t instanceof SubtypeTemplateCompilerConfig).findFirst().get();
            throw refusal(first, "the catalogue has \"integrator\": false: a subtype's typed bean converts to the template's input bean, "
                    + "which is generated only when the catalogue is an integrator");
        }

        Map<String, Map<Map<String, String>, String>> fixedBy = new HashMap<>();
        for (TemplateCompilerConfig c : configs.templates) {
            if (!(c instanceof SubtypeTemplateCompilerConfig)) continue;
            SubtypeTemplateCompilerConfig st = (SubtypeTemplateCompilerConfig) c;

            TemplateCompilerConfig target = Arrays.stream(configs.templates)
                    .filter(t -> Objects.equals(t.fullyQualifiedName, st.refines)).findFirst().orElse(null);
            if (target == null) throw refusal(st, "refines " + st.refines + ", which is no template of the catalogue");
            if (!(target instanceof SimpleTemplateCompilerConfig)) {
                throw refusal(st, "refines " + st.refines + ", which is not a simple template (a subtype refines a simple template)");
            }
            SimpleTemplateCompilerConfig simple = (SimpleTemplateCompilerConfig) target;
            TemplateBindingsSchema schema = compilerUtil.getBindingsSchema(ConfigProcessor.addBaseDirIfRelative(simple.cbindings, cbindingsBaseDir));
            Map<String, List<Descriptor>> var = schema.getVar();
            DescriptorUtils du = ConfigProcessor.descriptorUtils;

            Map<String, String> fixedText = new TreeMap<>();
            for (Map.Entry<String, Object> f : st.fixed.entrySet()) {
                String v = f.getKey();
                Object constant = f.getValue();
                if (schema.getInJson().containsKey(v)) {
                    throw refusal(st, "fixed variable " + v + " is an in-JSON value of " + st.refines + ": fix an input, and carry the in-JSON values");
                }
                if (schema.getInJson().values().stream().anyMatch(i -> i.getCarrier().equals(v))) {
                    throw refusal(st, "fixed variable " + v + " is a carrier of in-JSON values: the subtype carries values in it, it does not fix it");
                }
                if (!var.containsKey(v) || var.get(v) == null) {
                    throw refusal(st, "fixed variable " + v + " is not a variable of " + st.refines);
                }
                if (!du.getAttributeVariable(v, var.get(v).get(0)).isPresent()) {
                    throw refusal(st, "fixed variable " + v + " is a node, not a value");
                }
                if (!du.isInput(v, schema)) throw refusal(st, "fixed variable " + v + " is not an input of " + st.refines);
                if (du.isOutput(v, schema)) throw refusal(st, "fixed variable " + v + " is an output of " + st.refines);
                String type = du.getFromDescriptor(var.get(v).get(0), AttributeDescriptor::getType, nd -> null);
                String why = misfit(type, constant);
                if (why != null) throw refusal(st, "fixed variable " + v + " (" + type + "): the constant " + constant + " " + why);
                fixedText.put(v, String.valueOf(constant));
            }

            for (Map.Entry<String, String> k : st.carries.entrySet()) {
                if (!schema.getInJson().containsKey(k.getKey())) {
                    throw refusal(st, "carries " + k.getKey() + ", which is no in-JSON value of " + st.refines
                            + (var.containsKey(k.getKey()) ? " (it is an ordinary variable; fix it, or leave it to the general record)" : ""));
                }
                if (!SubtypeTemplateCompilerConfig.REQUIRED.equals(k.getValue()) && !SubtypeTemplateCompilerConfig.OPTIONAL.equals(k.getValue())) {
                    throw refusal(st, "carries " + k.getKey() + " as '" + k.getValue() + "': it is either 'required' or 'optional'");
                }
                InJsonValue v = schema.getInJson().get(k.getKey());
                if (!du.isInput(v.getCarrier(), schema)) {
                    throw refusal(st, "carries " + k.getKey() + ", in the carrier " + v.getCarrier() + ", which is not an input of " + st.refines
                            + ": the input bean has no field to write the JSON text in");
                }
            }

            String clash = fixedBy.computeIfAbsent(st.refines, x -> new HashMap<>()).put(fixedText, st.fullyQualifiedName);
            if (clash != null) {
                throw refusal(st, "fixes the same values (" + fixedText + ") as the subtype " + clash + " of " + st.refines
                        + ": a record of the general template could not tell them apart");
            }
        }
    }

    /** @return why a constant does not fit a declared type, or null when it does. */
    static String misfit(String type, Object c) {
        if (c == null) return "is null: a fixed value is a constant";
        switch (type == null ? "" : type) {
            case "xsd:string": case "xsd:date": case "xsd:dateTime": case "json":
                return c instanceof String ? null : "is not a string";
            case "xsd:boolean":
                return c instanceof Boolean ? null : "is not true or false";
            case "xsd:int":
                return (c instanceof Integer || c instanceof Short || c instanceof Byte) ? null : "is not an integer that fits an int";
            case "xsd:long":
                return (c instanceof Integer || c instanceof Long || c instanceof Short || c instanceof Byte) ? null : "is not an integer that fits a long";
            case "xsd:float": case "xsd:double":
                return c instanceof Number ? null : "is not a number";
            default:
                return "has a type no constant is checked for";
        }
    }

    private static SubtypeException refusal(SubtypeTemplateCompilerConfig st, String why) {
        return new SubtypeException("subtype " + st.fullyQualifiedName + ": " + why);
    }
}
