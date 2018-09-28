package org.openprovenance.prov.template.compiler;

import java.util.Arrays;

public class TemplatesCompilerConfig {
    public String destination;
       
    public String version;  
    public String name;
    public String group;
    public String description;
    
    public TemplateCompilerConfig [] templates;
   
    @Override
    public String toString() {
        return "TemplatesCompilerConfig [destination=" + destination + ", version=" + version
                + ", name=" + name + ", group=" + group + ", description=" + description
                + ", templates=" + Arrays.toString(templates) + "]";
    }
    

}
