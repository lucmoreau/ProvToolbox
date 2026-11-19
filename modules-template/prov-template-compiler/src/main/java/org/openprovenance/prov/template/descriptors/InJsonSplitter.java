package org.openprovenance.prov.template.descriptors;

import java.util.*;

/**
 * T36 (R1, ruled): splits the {@code @inJson} values out of a bindings schema, once, where it is read. After the split
 * {@link TemplateBindingsSchema#getVar()} holds the ordinary variables only, so every generator sees what it saw before
 * T36, and {@link TemplateBindingsSchema#getInJson()} holds the in-JSON values in the order of the file.
 * <p>
 * Every refusal names the bindings file and the variable.
 * <p><b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice.
 */
public class InJsonSplitter {

    /** The types a value can be declared with (those {@code CompilerUtil.getPastClassForType} knows). */
    static final Set<String> TYPES = Set.of("xsd:int", "xsd:long", "xsd:string", "xsd:boolean", "xsd:float", "xsd:double",
                                           "xsd:dateTime", "xsd:date", "json");
    /** The types a carrier can have: a text. */
    static final Set<String> CARRIER_TYPES = Set.of("xsd:string", "json");

    /** @param origin where the schema was read from, for messages. */
    public static void split(TemplateBindingsSchema schema, String origin) {
        Map<String, List<Descriptor>> var = schema.getVar();
        Map<String, InJsonValue> found = new LinkedHashMap<>();

        for (Map.Entry<String, List<Descriptor>> e : var.entrySet()) {
            String name = e.getKey();
            List<Descriptor> descriptors = e.getValue();
            if (descriptors == null) continue;
            for (int i = 0; i < descriptors.size(); i++) {
                Descriptor d = descriptors.get(i);
                if (d instanceof NameDescriptor) {
                    if (((NameDescriptor) d).getInJson() != null) {
                        throw refusal(origin, schema, name, "@inJson is for a value, not a node: a node is a variable of the template's graph");
                    }
                } else if (d instanceof AttributeDescriptorList && ((AttributeDescriptorList) d).getItems() != null) {
                    List<AttributeDescriptor> items = ((AttributeDescriptorList) d).getItems();
                    for (int j = 0; j < items.size(); j++) {
                        AttributeDescriptor ad = items.get(j);
                        if (ad.getInJson() == null && ad.getInJsonKey() == null) continue;
                        if (i != 0 || j != 0) {
                            throw refusal(origin, schema, name, "@inJson is on a descriptor after the first, which no generator reads");
                        }
                        found.put(name, check(origin, schema, name, ad));
                    }
                }
            }
        }
        if (found.isEmpty()) return;

        Set<String> seen = new HashSet<>();
        for (InJsonValue v : found.values()) {
            String carrier = v.getCarrier();
            if (found.containsKey(carrier)) {
                throw refusal(origin, schema, v.getName(), "its carrier " + carrier + " is itself @inJson: a carrier is an ordinary text variable");
            }
            List<Descriptor> cd = var.get(carrier);
            if (cd == null || cd.isEmpty()) {
                throw refusal(origin, schema, v.getName(), "its carrier " + carrier + " is not a variable of the template");
            }
            if (!(cd.get(0) instanceof AttributeDescriptorList)) {
                throw refusal(origin, schema, v.getName(), "its carrier " + carrier + " is a node, not a value");
            }
            String carrierType = ((AttributeDescriptorList) cd.get(0)).getItems().get(0).getType();
            if (!CARRIER_TYPES.contains(carrierType)) {
                throw refusal(origin, schema, v.getName(), "its carrier " + carrier + " has type " + carrierType + ", not xsd:string or json");
            }
            if (!seen.add(carrier + "\u0000" + v.getKey())) {
                throw refusal(origin, schema, v.getName(), "the key \"" + v.getKey() + "\" is already declared in the carrier " + carrier);
            }
        }

        for (String name : found.keySet()) var.remove(name);
        schema.setInJson(found);
    }

    private static InJsonValue check(String origin, TemplateBindingsSchema schema, String name, AttributeDescriptor ad) {
        String carrier = ad.getInJson();
        if (carrier == null) {
            throw refusal(origin, schema, name, "@inJson.key without @inJson: it says which key, not in which variable");
        }
        if (carrier.isBlank()) {
            throw refusal(origin, schema, name, "@inJson is empty: it names the text variable the value is encoded in");
        }
        String key = ad.getInJsonKey() == null ? name : ad.getInJsonKey();
        if (key.isBlank()) {
            throw refusal(origin, schema, name, "@inJson.key is empty");
        }
        if (ad.getType() == null || !TYPES.contains(ad.getType())) {
            throw refusal(origin, schema, name, "its @type " + ad.getType() + " is not one of " + new TreeSet<>(TYPES));
        }
        String[] clashing = {
                ad.getSqlType() == null ? null : "@sql.type",
                ad.getSqlForeign() == null ? null : "@sql.foreign",
                ad.isUnique() ? "@unique" : null,
                ad.getEscape() == null ? null : "@escape",
                ad.getOutput() == null ? null : "@output",
                ad.getTransient() == null ? null : "@transient",
                ad.getPseudonym() == null ? null : "@pseudonym" };
        for (String c : clashing) {
            if (c != null) {
                throw refusal(origin, schema, name, "@inJson together with " + c + ": an in-JSON value is no column and no log field; "
                        + (c.equals("@transient") || c.equals("@pseudonym")
                        ? "the annotations of its carrier cover its whole content"
                        : "put " + c + " on the carrier if it is meant"));
            }
        }
        return new InJsonValue(name, carrier, key, ad);
    }

    private static InJsonException refusal(String origin, TemplateBindingsSchema schema, String variable, String why) {
        return new InJsonException("bindings " + origin + (schema.getTemplate() == null ? "" : " (template " + schema.getTemplate() + ")")
                + ", variable " + variable + ": " + why);
    }
}
