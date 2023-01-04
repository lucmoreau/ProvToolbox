package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum TemplateConfigurationEnum {
    SIMPLE,
    COMPOSITE;
/*
    private static final Map<String, TemplateConfigurationEnum> namesMap = new HashMap<>(2);

    static {
        namesMap.put("simple", SIMPLE);
        namesMap.put("composite", COMPOSITE);
    }

    @JsonCreator
    public static TemplateConfigurationEnum forValue(String value) {
        TemplateConfigurationEnum inputTypeValue = namesMap.get(StringUtils.lowerCase(value));
        if (inputTypeValue==null) throw new UnsupportedOperationException("Unknown input type value " + value);
        System.out.println("JSOn Creator " + value);
        return inputTypeValue;
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, TemplateConfigurationEnum> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null; // or fail
    }


    @JsonIgnore
    static public boolean isSimple (TemplateConfigurationEnum inputTypeValue) {
        if (inputTypeValue == null) return false;
        return inputTypeValue==SIMPLE;
    }
    @JsonIgnore
    static public boolean isComposite (TemplateConfigurationEnum inputTypeValue) {
        if (inputTypeValue == null) return false;
        return inputTypeValue==COMPOSITE;
    }
*/
    @JsonIgnore
    static public boolean isSimple (TemplateCompilerConfig config) {
        return config instanceof SimpleTemplateCompilerConfig;
    }

}
