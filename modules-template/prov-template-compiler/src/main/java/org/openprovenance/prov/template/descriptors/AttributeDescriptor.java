
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
        "@examplar",
        "@input",
        "@output",
        "@sql.type"
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
    private Object examplar;
    @JsonProperty("@input")
    private InputFieldValue input;
    @JsonProperty("@output")
    private OutputFieldValue output;
    @JsonProperty("@sql.type")
    private String sqlType;


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
    public Object getExamplar() {
        return examplar;
    }

    @JsonProperty("@examplar")
    public void setExamplar(Object examplar) {
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

    @JsonProperty("@sql.type")
    public String getSqlType() {
        return sqlType;
    }
    @JsonProperty("@sql.type")
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AttributeDescriptor that = (AttributeDescriptor) o;

        return new EqualsBuilder().append(value, that.value).append(type, that.type).append(documentation, that.documentation).append(escape, that.escape).append(examplar, that.examplar).append(input, that.input).append(output, that.output).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).append(type).append(documentation).append(escape).append(examplar).append(input).append(output).toHashCode();
    }

    @JsonIgnore
    public DescriptorTypes getDescriptorType () {
        return DescriptorTypes.ATTRIBUTE;
    }

}
