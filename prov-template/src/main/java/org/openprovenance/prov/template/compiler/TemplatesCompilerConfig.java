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
    public String logger_package;
    
    public TemplateCompilerConfig [] templates;
   
    @Override
    public String toString() {
        return "TemplatesCompilerConfig [destination=" + destination + ", version=" + version
                + ", name=" + name + ", group=" + group + ", description=" + description
                + ", init_package=" + init_package + ", logger=" + logger + ", logger_package="
                + logger_package + ", templates=" + Arrays.toString(templates) + "]";
    }
    

}
