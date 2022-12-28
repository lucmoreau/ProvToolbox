
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
        "@id",
        "@type",
        "@documentation",
        "@examplar"
})
public class NameDescriptor implements Descriptor {

    @JsonProperty("@id")
    private String id;
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@documentation")
    private String documentation;
    @JsonProperty("@examplar")
    private String examplar;

    @JsonProperty("@id")
    public String getId() {
        return id;
    }

    @JsonProperty("@id")
    public void setId(String id) {
        this.id = id;
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
        return new HashCodeBuilder().append(id).append(type).append(documentation).append(examplar).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NameDescriptor)) {
            return false;
        }
        NameDescriptor rhs = ((NameDescriptor) other);
        return new EqualsBuilder().append(id, rhs.id).append(type, rhs.type).append(documentation, rhs.documentation).append(examplar, rhs.examplar).isEquals();
    }

    @JsonIgnore
    public DescriptorTypes getDescriptorType () {
        return DescriptorTypes.NAME;
    }

}
