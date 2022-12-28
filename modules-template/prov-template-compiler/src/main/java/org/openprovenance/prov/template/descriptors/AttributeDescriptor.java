
package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@value",
    "@type",
    "@documentation",
        "@escape",
        "@examplar"
})
public class AttributeDescriptor implements Descriptor {

    @JsonProperty("@value")
    private String value;
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@documentation")
    private String documentation;
    @JsonProperty("@escape")
    private String escape;
    @JsonProperty("@examplar")
    private String examplar;

    @JsonProperty("@value")
    public String getValue() {
        return value;
    }

    @JsonProperty("@value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("@type")
    public String getType() {
        return type;
    }

    @JsonProperty("@type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("@documentation")
    public String getDocumentation() {
        return documentation;
    }

    @JsonProperty("@documentation")
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @JsonProperty("@escape")
    public String getEscape() {
        return escape;
    }

    @JsonProperty("@escape")
    public void setEscape(String escape) {
        this.escape = escape;
    }

    @JsonProperty("@examplar")
    public String getExamplar() {
        return examplar;
    }

    @JsonProperty("@examplar")
    public void setExamplar(String examplar) {
        this.examplar = examplar;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(value).append(type).append(documentation).append(escape).append(examplar).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AttributeDescriptor)) {
            return false;
        }
        AttributeDescriptor rhs = ((AttributeDescriptor) other);
        return new EqualsBuilder().append(value, rhs.value).append(type, rhs.type).append(documentation, rhs.documentation).append(escape, rhs.escape).append(examplar,rhs.examplar).isEquals();
    }

    @JsonIgnore
    public DescriptorTypes getDescriptorType () {
        return DescriptorTypes.ATTRIBUTE;
    }

}
