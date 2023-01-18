package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleTemplateCompilerConfig extends TemplateCompilerConfig {

    public String template;

    public String bindings;

    @Override
    @JsonProperty("@type")
    public String getType_() {
        return type_;
    }

    @Override
    @JsonProperty("@type")
    public void setType_(String type_) {
        this.type_ = type_;
    }

    @Override
    public String toString() {
        return "SimpleTemplateCompilerConfig{" +
                "@type='" + type_ + '\'' +
                ", name='" + name + '\'' +
                ", template='" + template + '\'' +
                ", package_='" + package_ + '\'' +
                ", bindings='" + bindings + '\'' +
                '}';
    }

    @JsonIgnore
    public boolean inComposition=false;


    public SimpleTemplateCompilerConfig cloneAsInstanceInComposition(String newName) {
        SimpleTemplateCompilerConfig clone= new SimpleTemplateCompilerConfig();
        clone.template=template;
        clone.type_=type_;
        clone.name= newName; //this.name +"_shared";
        clone.package_=package_;
        clone.bindings=bindings;
        clone.inComposition=true;
        return clone;
    }
}
