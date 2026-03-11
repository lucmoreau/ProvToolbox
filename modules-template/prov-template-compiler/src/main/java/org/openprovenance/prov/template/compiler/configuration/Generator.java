package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Generator {
    @JsonProperty("class")
    public String clazz;
    public List<String> classpath;
    public Map<String,Object> parameters;


}
