package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum InputFieldValue {
    Compulsory,
    Optional,
    False;

    private static final Map<String, InputFieldValue> namesMap = new HashMap<>(3);

    static {
        namesMap.put("compulsory", Compulsory);
        namesMap.put("optional", Optional);
        namesMap.put("false", False);
    }

    @JsonCreator
    public static InputFieldValue forValue(String value) {
        InputFieldValue inputFieldValue = namesMap.get(StringUtils.lowerCase(value));
        if (inputFieldValue==null) throw new UnsupportedOperationException("Unknown input field value " + value);
        return inputFieldValue;
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, InputFieldValue> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null; // or fail
    }

    @JsonIgnore
    static public boolean isInput (InputFieldValue inputFieldValue) {
        if (inputFieldValue == null) return false;
        switch (inputFieldValue) {
            case Compulsory:
                return true;
            case Optional:
                return true;
            case False:
                return false;
        }
        throw new UnsupportedOperationException("never reaching this point");
    }

}
