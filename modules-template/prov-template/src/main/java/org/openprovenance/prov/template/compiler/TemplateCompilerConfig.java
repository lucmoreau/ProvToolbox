package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateCompilerConfig {
    // provconvert -infile templates/grow.provn -template grow -builder -package foo -bindings bindings/grow_bs.json -bindver 3 -outfile src/main/java
    public String name;
    public String template;
    @JsonProperty("package")
    public String package_;
    public String bindings;
    
   
    @Override
    public String toString() {
        return "TemplateCompilerConfig [name=" + name + ", template=" + template + ", package_="
                + package_ + ", bindings=" + bindings + "]";
    }
    

}
