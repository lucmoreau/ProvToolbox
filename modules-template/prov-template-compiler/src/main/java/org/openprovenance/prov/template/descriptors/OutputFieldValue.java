package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum OutputFieldValue {
    True,
    Optional,
    False;

    private static final Map<String, OutputFieldValue> namesMap = new HashMap<>(3);

    static {
        namesMap.put("true", True);
        namesMap.put("optional", Optional);
        namesMap.put("false", False);
    }

    @JsonCreator
    public static OutputFieldValue forValue(String value) {
        OutputFieldValue inputFieldValue = namesMap.get(StringUtils.lowerCase(value));
        if (inputFieldValue==null) throw new UnsupportedOperationException("Unknown output field value " + value);
        return inputFieldValue;
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, OutputFieldValue> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null; // or fail
    }

    @JsonIgnore
    static public boolean isOutput (OutputFieldValue outputFieldValue) {
        if (outputFieldValue == null) return false;
        switch (outputFieldValue) {
            case True:
                return true;
            case Optional:
                return true;
            case False:
                return false;
        }
        throw new UnsupportedOperationException("never reaching this point");
    }


}
