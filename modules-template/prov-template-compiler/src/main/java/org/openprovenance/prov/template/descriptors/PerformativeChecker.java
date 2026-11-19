package org.openprovenance.prov.template.descriptors;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * T36 part D (design §12.3, R12 and R14, ruled): the compile-time refusals of {@code @performative}, read where a bindings
 * schema is loaded, after the {@code @inJson} values are split out (so it can tell a carrier). Every refusal names the
 * bindings file, the template and the variable. The annotation is kept on the descriptor: only part D's generator and the
 * descriptions read it, so no existing generator changes.
 * <p><b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice.
 */
public class PerformativeChecker {

    public static final String REQUEST = "request";
    public static final String REPLY = "reply";
    static final Set<String> VALUES = Set.of(REQUEST, REPLY);

    /** @param origin where the schema was read from, for messages. */
    public static void check(TemplateBindingsSchema schema, String origin) {
        for (Map.Entry<String, List<Descriptor>> e : schema.getVar().entrySet()) {
            String name = e.getKey();
            List<Descriptor> descriptors = e.getValue();
            if (descriptors == null) continue;
            for (int i = 0; i < descriptors.size(); i++) {
                Descriptor d = descriptors.get(i);
                if (d instanceof NameDescriptor) {
                    if (((NameDescriptor) d).getPerformative() != null) {
                        throw refusal(origin, schema, name, "@performative is for a value, not a node: a caller gives names, and the service finds the nodes");
                    }
                } else if (d instanceof AttributeDescriptorList && ((AttributeDescriptorList) d).getItems() != null) {
                    List<AttributeDescriptor> items = ((AttributeDescriptorList) d).getItems();
                    for (int j = 0; j < items.size(); j++) {
                        AttributeDescriptor ad = items.get(j);
                        if (ad.getPerformative() == null) continue;
                        if (i != 0 || j != 0) {
                            throw refusal(origin, schema, name, "@performative is on a descriptor after the first, which no generator reads");
                        }
                        ordinary(origin, schema, name, ad);
                    }
                }
            }
        }
        for (InJsonValue v : schema.getInJson().values()) {
            if (v.getPerformative() != null) oneOf(origin, schema, v.getName(), v.getPerformative());
        }
    }

    private static void ordinary(String origin, TemplateBindingsSchema schema, String name, AttributeDescriptor ad) {
        oneOf(origin, schema, name, ad.getPerformative());
        if (OutputFieldValue.isOutput(ad.getOutput())) {
            throw refusal(origin, schema, name, "@performative on an output: the store mints an output, and neither message carries it");
        }
        if (!InputFieldValue.isInput(ad.getInput())) {
            throw refusal(origin, schema, name, "@performative on a value that is no input of the template: it would be in neither message");
        }
        if (!schema.getInJsonOf(name).isEmpty()) {
            throw refusal(origin, schema, name, "@performative on a carrier that holds in-JSON values (" + names(schema.getInJsonOf(name))
                    + "): each of its values says its own");
        }
    }

    private static void oneOf(String origin, TemplateBindingsSchema schema, String name, String performative) {
        if (!VALUES.contains(performative)) {
            throw refusal(origin, schema, name, "@performative is \"" + performative + "\": it is \"" + REQUEST + "\" or \"" + REPLY + "\"");
        }
    }

    private static String names(List<InJsonValue> values) {
        StringBuilder sb = new StringBuilder();
        for (InJsonValue v : values) sb.append(sb.length() == 0 ? "" : ", ").append(v.getName());
        return sb.toString();
    }

    private static PerformativeException refusal(String origin, TemplateBindingsSchema schema, String variable, String why) {
        return new PerformativeException("bindings " + origin + (schema.getTemplate() == null ? "" : " (template " + schema.getTemplate() + ")")
                + ", variable " + variable + ": " + why);
    }
}
