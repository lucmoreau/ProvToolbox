
package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "@value",
        "@type",
        "@documentation",
        "@escape",
        "@examplar",
        "@input",
        "@output",
        "@sql.type",
        "@sql.foreign"

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

    @JsonProperty("@sql.foreign")
    private SqlForeign sqlForeign;



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

    @JsonProperty("@sql.foreign")
    public SqlForeign getSqlForeign() {
        return sqlForeign;
    }

    @JsonProperty("@sql.foreign")
    public void setSqlForeign(SqlForeign sqlForeign) {
        this.sqlForeign = sqlForeign;
    }

    @Override
    public String toString() {
        return "AttributeDescriptor{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", documentation='" + documentation + '\'' +
                ", escape='" + escape + '\'' +
                ", examplar=" + examplar +
                ", input=" + input +
                ", output=" + output +
                ", sqlType='" + sqlType + '\'' +
                ", sqlForeign=" + sqlForeign +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeDescriptor that = (AttributeDescriptor) o;
        return Objects.equals(value, that.value) && Objects.equals(type, that.type) && Objects.equals(documentation, that.documentation) && Objects.equals(escape, that.escape) && Objects.equals(examplar, that.examplar) && input == that.input && output == that.output && Objects.equals(sqlType, that.sqlType) && Objects.equals(sqlForeign, that.sqlForeign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type, documentation, escape, examplar, input, output, sqlType, sqlForeign);
    }

    @JsonIgnore
    public DescriptorTypes getDescriptorType () {
        return DescriptorTypes.ATTRIBUTE;
    }

    public static class SqlForeign {
        private String type;
        private String attribute;
        private String item;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return "SqlForeign{" +
                    "type='" + type + '\'' +
                    ", attribute='" + attribute + '\'' +
                    ", item='" + item + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SqlForeign that = (SqlForeign) o;
            return Objects.equals(type, that.type) && Objects.equals(attribute, that.attribute) && Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, attribute, item);
        }

    }
}
