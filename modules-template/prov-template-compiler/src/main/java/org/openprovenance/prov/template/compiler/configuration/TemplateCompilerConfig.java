package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleTemplateCompilerConfig.class, name = "simple"),
        @JsonSubTypes.Type(value = CompositeTemplateCompilerConfig.class, name = "composite")
})
abstract public class TemplateCompilerConfig {
    public String type_;
    @JsonProperty("name")
    public String name;
    @JsonProperty("fullyQualifiedName")
    public String fullyQualifiedName;
    @JsonProperty("package")
    public String package_;

    @JsonProperty("@type")
    public String getType_() {
        return type_;
    }

    @JsonProperty("@type")
    public void setType_(String type_) {
        this.type_ = type_;
    }
}
