
package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "@id",
        "@type",
        "@escape",
        "@role",
        "@documentation",
        "@examplar",
        "@input",
        "@output",
        "@sql.type",
        Descriptor.SQL_TABLE,
        Descriptor.SQL_NEW_INPUTS
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
    @JsonProperty("@sql.type")
    private String sqlType;
    @JsonProperty("@escape")
    private String escape;

    /**
     * Role of this variable within its template statement. One of
     * {@code cause}, {@code effect}, {@code general} (and the numbered
     * variants {@code cause2}, {@code effect2}, {@code general2} for
     * two-chain templates). Drives the generation of uniform
     * {@code get&lt;Role&gt;()} / {@code set&lt;Role&gt;()} accessors on the bean.
     */
    @JsonProperty("@role")
    private String role;


    @JsonProperty("@semantic.type")
    private String semantic;


    @JsonProperty("@sql.table")
    private String table;
    @JsonProperty("@sql.new.inputs")
    private Map<String,String> newInputs;
    @JsonProperty("@sql.also.outputs")
    private Map<String,String> alsoOutputs;
    @JsonProperty("@unique")
    private boolean unique;

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

    @JsonProperty("@sql.table")
    public String getTable() {
        return table;
    }

    @JsonProperty("@sql.new.inputs")
    public Map<String, String> getNewInputs() {
        return newInputs;
    }
    @JsonProperty("@sql.also.outputs")
    public Map<String, String> getAlsoOutputs() {
        return alsoOutputs;
    }

    @JsonProperty("@sql.new.inputs")
    public void setNewInputs(Map<String, String> newInputs) {
        this.newInputs = newInputs;
    }

    @JsonProperty("@sql.also.outputs")
    public void setAlsoOutputs(Map<String, String> alsoOutputs) {
        this.alsoOutputs = alsoOutputs;
    }

    @JsonProperty("@sql.type")
    public String getSqlType() {
        return sqlType;
    }

    @JsonProperty("@sql.type")
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    @JsonProperty("@escape")
    public String getEscape() {
        return escape;
    }

    @JsonProperty("@escape")
    public void setEscape(String escape) {
        this.escape = escape;
    }

    @JsonProperty("@role")
    public String getRole() {
        return role;
    }

    @JsonProperty("@role")
    public void setRole(String role) {
        this.role = role;
    }


    @JsonProperty("@semantic.type")
    public String getSemantic() {
        return semantic;
    }

    @JsonProperty("@semantic.type")
    public void setSemantic(String semantic) {
        this.semantic = semantic;
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
        return "NameDescriptor{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", documentation='" + documentation + '\'' +
                ", examplar='" + examplar + '\'' +
                ", input=" + input +
                ", output=" + output +
                ", sqlType='" + sqlType + '\'' +
                ", escape='" + escape + '\'' +
                ", role='" + role + '\'' +
                ", table='" + table + '\'' +
                ", newInputs=" + newInputs +
                ", alsoOutputs=" + alsoOutputs +
                ", semantic=" + semantic +
                '}';
    }

    @JsonProperty("@sql.table")
    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameDescriptor that = (NameDescriptor) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(documentation, that.documentation) && Objects.equals(examplar, that.examplar) && input == that.input && output == that.output && Objects.equals(sqlType, that.sqlType) && Objects.equals(role, that.role) && Objects.equals(table, that.table) && Objects.equals(newInputs, that.newInputs) && Objects.equals(alsoOutputs, that.alsoOutputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, documentation, examplar, input, output, sqlType, role, table, newInputs, alsoOutputs);
    }

    @JsonIgnore
    public DescriptorTypes getDescriptorType () {
        return DescriptorTypes.NAME;
    }

}
