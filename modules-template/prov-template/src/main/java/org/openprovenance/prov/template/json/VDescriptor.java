package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VDescriptor implements SingleDescriptor {

    public static final String AT_VALUE = "@value";
    private static final String AT_LANG = "@lang";
    public static final String AT_TYPE = "@type";


    @JsonProperty(AT_VALUE)
    public String value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(AT_TYPE)
    public String type;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(AT_LANG)
    public String language;

    @Override
    public String toString() {
        return "VDescriptor{" +
                "value='" + value + '\'' +
                ((type==null)? "": ", type='" + type + '\'') +
                '}';
    }

    @Override
    public Object toObject() {
        Map<String,String> res=new HashMap<>();
        res.put(AT_VALUE, value);
        if (type!=null) res.put(AT_TYPE, type);
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VDescriptor that = (VDescriptor) o;
        return Objects.equals(value, that.value) && Objects.equals(type, that.type) && Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type, language);
    }
}
