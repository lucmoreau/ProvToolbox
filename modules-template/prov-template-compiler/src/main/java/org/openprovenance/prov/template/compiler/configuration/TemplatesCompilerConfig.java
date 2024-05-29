package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import  org.openprovenance.prov.template.compiler.common.Constants;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TemplatesCompilerConfig {
    public String destination;
       
    public String version;  
    public String name;
    public String group;
    public String description;
    public String logger;
    public String templateBuilders;
    public String tableConfigurator;
    public String beanProcessor;
    public String root_package;

    public String configurator_folder=Constants.CONFIGURATOR;
    public String logger_folder=Constants.LOGGER;

    public List<String> search;
    public String script;
    public String script_dir;
    public boolean integrator;
    public boolean jsweet;
    public boolean sbean;
    public String jsonschema;
    public String sqlFile;
    public String documentation;
    public String python_dir;

    @Override
    public String toString() {
        return "TemplatesCompilerConfig{" +
                "destination='" + destination + '\'' +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", description='" + description + '\'' +
                ", logger='" + logger + '\'' +
                ", templateBuilders='" + templateBuilders + '\'' +
                ", tableConfigurator='" + tableConfigurator + '\'' +
                ", beanProcessor='" + beanProcessor + '\'' +
                ", root_package='" + root_package + '\'' +
                ", configurator_folder='" + configurator_folder + '\'' +
                ", logger_folder='" + logger_folder + '\'' +
                ", search=" + search +
                ", script='" + script + '\'' +
                ", script_dir='" + script_dir + '\'' +
                ", integrator=" + integrator +
                ", jsweet=" + jsweet +
                ", sbean=" + sbean +
                ", jsonschema='" + jsonschema + '\'' +
                ", sqlFile='" + sqlFile + '\'' +
                ", documentation='" + documentation + '\'' +
                ", python_dir='" + python_dir + '\'' +
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
