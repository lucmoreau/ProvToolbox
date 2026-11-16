package org.openprovenance.prov.template.descriptors;

/**
 * T36: a value a template keeps inside a JSON text variable (its <em>carrier</em>), declared by {@code @inJson} on the
 * value's descriptor. It is not a variable of the template's beans, SQL or PROV document: the bindings schema holds it
 * apart from the ordinary variables ({@link TemplateBindingsSchema#getInJson()}).
 */
public class InJsonValue {
    private final String name;
    private final String carrier;
    private final String key;
    private final AttributeDescriptor descriptor;

    public InJsonValue(String name, String carrier, String key, AttributeDescriptor descriptor) {
        this.name = name;
        this.carrier = carrier;
        this.key = key;
        this.descriptor = descriptor;
    }

    /** The name the value has in the bindings file (its variable's name). */
    public String getName() {
        return name;
    }

    /** The text variable of the template this value is encoded in. */
    public String getCarrier() {
        return carrier;
    }

    /** The key of the value in the carrier's JSON object: {@code @inJson.key} when given, else the name. */
    public String getKey() {
        return key;
    }

    /** The declared type, e.g. {@code xsd:double}. */
    public String getType() {
        return descriptor.getType();
    }

    public String getDocumentation() {
        return descriptor.getDocumentation();
    }

    /** T36 part D: {@code "request"}, {@code "reply"} or null (the service's own). */
    public String getPerformative() {
        return descriptor.getPerformative();
    }

    public InputFieldValue getInput() {
        return descriptor.getInput();
    }

    public AttributeDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public String toString() {
        return "InJsonValue{" + name + " in " + carrier + " as \"" + key + "\", " + descriptor.getType() + "}";
    }
}
