package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleTemplateCompilerConfig.class, name = "simple"),
        @JsonSubTypes.Type(value = CompositeTemplateCompilerConfig.class, name = "composite"),
        @JsonSubTypes.Type(value = SubtypeTemplateCompilerConfig.class, name = "subtype"),
        @JsonSubTypes.Type(value = FlowTemplateCompilerConfig.class, name = "flow")
})
abstract public class TemplateCompilerConfig {
    public String type_;
    @JsonProperty("name")
    public String name;
    @JsonProperty("fullyQualifiedName")
    public String fullyQualifiedName;
    @JsonProperty("package")
    public String package_;

    /**
     * T43: the template (a simple one or a composite) belongs to another library, of which this catalogue holds an annotated copy.
     * The compiler still writes its classes, which the catalogue's own classes import, but under {@code src/external/java} of
     * the generated modules and not {@code src/main/java}, and lists their packages in {@code external-packages.txt}, so that an
     * application leaves them to that library (a second copy of a package is a split package on the module path and a silent
     * shadowing on the class path).
     */
    @JsonProperty("external")
    public boolean external;

    @JsonProperty("@type")
    public String getType_() {
        return type_;
    }

    @JsonProperty("@type")
    public void setType_(String type_) {
        this.type_ = type_;
    }
}
