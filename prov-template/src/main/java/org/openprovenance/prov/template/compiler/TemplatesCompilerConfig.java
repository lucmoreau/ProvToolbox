package org.openprovenance.prov.template.compiler;

import java.util.Arrays;

public class TemplatesCompilerConfig {
    public String destination;
       
    public String version;  
    public String name;
    public String group;
    public String description;
    public String init_package;
    
    public TemplateCompilerConfig [] templates;
   
    @Override
    public String toString() {
        return "TemplatesCompilerConfig [destination=" + destination + ", version=" + version
                + ", name=" + name + ", group=" + group + ", description=" + description
                + ", init_package=" + init_package + ", templates=" + Arrays.toString(templates)
                + "]";
    }
    

}
