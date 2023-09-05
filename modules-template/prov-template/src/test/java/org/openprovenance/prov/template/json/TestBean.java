package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class TestBean {
    public Map<String, Descriptors> var;
    public Map<String, List<Object>> vargen;
    public Map<String, String> context;
    public String template;

    @Override
    public String toString() {
        return "TestBean{" +
                "var=" + var +
                ", vargen=" + vargen +
                ", context=" + context +
                ", template='" + template + '\'' +
                '}';
    }
}