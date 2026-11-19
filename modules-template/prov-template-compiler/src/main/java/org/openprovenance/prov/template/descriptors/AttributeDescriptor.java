
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
        "@sql.foreign",
        "@inJson",
        "@inJson.key",
        "@performative"

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

    @JsonProperty("@unique")
    private boolean unique;

    /** T36: the text variable (carrier) this value is encoded in, as a key of its JSON object. Never a bean field or a column. Experimental: may change without notice (T36, T41). */
    @JsonProperty("@inJson")
    private String inJson;
    /** T36 (R2): the key in the carrier when it differs from the variable's name. Experimental: may change without notice (T36, T41). */
    @JsonProperty("@inJson.key")
    private String inJsonKey;

    /** T36 part D (R12, R14): {@code "request"} or {@code "reply"}: whose content this value is. Absent: the service's own. Checked by {@link PerformativeChecker}. Experimental: may change without notice (T36, T41). */
    @JsonProperty("@performative")
    private String performative;

    /** T31: {@code true}, {@code false} or a transient method name, as declared in the bindings schema. */
    @JsonProperty("@transient")
    private Object transientDeclared;
    /** T31: a pseudonym method name, as declared in the bindings schema. */
    @JsonProperty("@pseudonym")
    private String pseudonym;
    /** T31: the annotation in effect once the application's policy is applied; set by the compiler, never serialised. */
    @JsonIgnore
    private EffectivePrivacy effectivePrivacy;

    @JsonProperty("@performative")
    public String getPerformative() {
        return performative;
    }

    @JsonProperty("@performative")
    public void setPerformative(String performative) {
        this.performative = performative;
    }

    @JsonProperty("@inJson")
    public String getInJson() {
        return inJson;
    }

    @JsonProperty("@inJson")
    public void setInJson(String inJson) {
        this.inJson = inJson;
    }

    @JsonProperty("@inJson.key")
    public String getInJsonKey() {
        return inJsonKey;
    }

    @JsonProperty("@inJson.key")
    public void setInJsonKey(String inJsonKey) {
        this.inJsonKey = inJsonKey;
    }

    @JsonProperty("@transient")
    public Object getTransient() {
        return transientDeclared;
    }

    @JsonProperty("@transient")
    public void setTransient(Object transientDeclared) {
        this.transientDeclared = transientDeclared;
    }

    @JsonProperty("@pseudonym")
    public String getPseudonym() {
        return pseudonym;
    }

    @JsonProperty("@pseudonym")
    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    @JsonIgnore
    public EffectivePrivacy getEffectivePrivacy() {
        return effectivePrivacy;
    }

    @JsonIgnore
    public void setEffectivePrivacy(EffectivePrivacy effectivePrivacy) {
        this.effectivePrivacy = effectivePrivacy;
    }

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


    @JsonProperty("@unique")
    public boolean isUnique() {
        return unique;
    }

    @JsonProperty("@unique")
    public void setUnique(boolean unique) {
        this.unique = unique;
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
        return Objects.equals(value, that.value) && Objects.equals(type, that.type) && Objects.equals(documentation, that.documentation) && Objects.equals(escape, that.escape) && Objects.equals(examplar, that.examplar) && input == that.input && output == that.output && Objects.equals(sqlType, that.sqlType) && Objects.equals(sqlForeign, that.sqlForeign) && Objects.equals(inJson, that.inJson) && Objects.equals(inJsonKey, that.inJsonKey) && Objects.equals(performative, that.performative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type, documentation, escape, examplar, input, output, sqlType, sqlForeign, inJson, inJsonKey, performative);
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
