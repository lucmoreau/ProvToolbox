
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
        "@examplar",
        "@input",
        "@output",
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
    @JsonProperty("@input")
    private InputFieldValue input;
    @JsonProperty("@output")
    private OutputFieldValue output;

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

    @JsonProperty("@input")
    public InputFieldValue getInput() {
        return input;
    }

    @JsonProperty("@input")
    public void setInput(InputFieldValue input) {
        this.input = input;
    }

    @JsonProperty("@output")
    public OutputFieldValue getOutput() {
        return output;
    }

    @JsonProperty("@output")
    public void setOutput(OutputFieldValue output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NameDescriptor that = (NameDescriptor) o;

        return new EqualsBuilder().append(id, that.id).append(type, that.type).append(documentation, that.documentation).append(examplar, that.examplar).append(input, that.input).append(output, that.output).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(type).append(documentation).append(examplar).append(input).append(output).toHashCode();
    }

    @JsonIgnore
    public DescriptorTypes getDescriptorType () {
        return DescriptorTypes.NAME;
    }

}
