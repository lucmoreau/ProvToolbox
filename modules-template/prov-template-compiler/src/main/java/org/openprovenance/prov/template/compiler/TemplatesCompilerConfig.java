package org.openprovenance.prov.template.compiler;

import java.util.Arrays;

public class TemplatesCompilerConfig {
    public String destination;
       
    public String version;  
    public String name;
    public String group;
    public String description;
    public String init_package;
    public String logger;
    public String templateBuilders;
    public String tableConfigurator;
    public String logger_package;
    public String script;
    public String script_dir;
    public boolean jsweet;
    public boolean sbean;
    public String jsonschema;
    public String sqlFile;
    public String documentation;

    @Override
    public String toString() {
        return "TemplatesCompilerConfig{" +
                "destination='" + destination + '\'' +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", description='" + description + '\'' +
                ", init_package='" + init_package + '\'' +
                ", logger='" + logger + '\'' +
                ", templateBuilders='" + templateBuilders + '\'' +
                ", tableConfigurator='" + tableConfigurator + '\'' +
                ", logger_package='" + logger_package + '\'' +
                ", script='" + script + '\'' +
                ", script_dir='" + script_dir + '\'' +
                ", jsweet=" + jsweet +
                ", sbean=" + sbean +
                ", jsonschema='" + jsonschema + '\'' +
                ", sqlFile='" + sqlFile + '\'' +
                ", documentation='" + documentation + '\'' +
                ", templates=" + Arrays.toString(templates) +
                '}';
    }


    public TemplateCompilerConfig [] templates;


}
