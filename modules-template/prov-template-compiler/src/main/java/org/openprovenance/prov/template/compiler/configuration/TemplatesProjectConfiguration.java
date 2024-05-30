package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Templates Project Configuration, defined in schema template-project.json */
public class TemplatesProjectConfiguration {
    public String destination;
       
    public String version;  
    public String name;
    public String group;
    public String description;
    public String documentation;

    public String root_package;

    public String script;
    public String script_dir;
    public String python_dir;

    public boolean integrator;
    public boolean jsweet;
    public boolean sbean;

    public String jsonschema;
    public String sqlFile;
    public List<String> search;


    @Override
    public String toString() {
        return "TemplatesProjectConfiguration{" +
                "destination='" + destination + '\'' +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", description='" + description + '\'' +
                ", documentation='" + documentation + '\'' +
                ", root_package='" + root_package + '\'' +
                ", script='" + script + '\'' +
                ", script_dir='" + script_dir + '\'' +
                ", python_dir='" + python_dir + '\'' +
                ", integrator=" + integrator +
                ", jsweet=" + jsweet +
                ", sbean=" + sbean +
                ", jsonschema='" + jsonschema + '\'' +
                ", sqlFile='" + sqlFile + '\'' +
                ", search=" + search +
                ", templates=" + Arrays.toString(templates) +
                ", sqlTables=" + sqlTables +
                '}';
    }


    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SimpleTemplateCompilerConfig.class, name = "simple"),
            @JsonSubTypes.Type(value = CompositeTemplateCompilerConfig.class, name = "composite")
    })
    @JsonProperty("templates")
    public TemplateCompilerConfig [] templates;

    @JsonProperty("sql.tables")
    public Map<String,Map<String,String>> sqlTables;


}
