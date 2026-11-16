
package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "var",
    "context",
    "template",
    "@documentation"
})
public class TemplateBindingsSchema {

    @JsonProperty("var")
    private Map<String, List<Descriptor>> var=new HashMap<>();
    @JsonProperty("context")
    private Map<String,String> context=new HashMap<>();
    @JsonProperty("template")
    private String template;
    @JsonProperty("@documentation")
    private String documentation;
    @JsonProperty("@return")
    private String returnValue;

    /** T36: the values kept inside a text variable, split out of {@code var} when the file is read; see {@link InJsonSplitter}. */
    @JsonIgnore
    private Map<String, InJsonValue> inJson = new LinkedHashMap<>();

    /** T36: the {@code @inJson} values, by name, in the order of the file. Empty for a template that declares none. */
    @JsonIgnore
    public Map<String, InJsonValue> getInJson() {
        return inJson;
    }

    @JsonIgnore
    public void setInJson(Map<String, InJsonValue> inJson) {
        this.inJson = inJson;
    }

    /** T36: the in-JSON values of a carrier, in the order of the file. */
    @JsonIgnore
    public List<InJsonValue> getInJsonOf(String carrier) {
        List<InJsonValue> res = new java.util.ArrayList<>();
        for (InJsonValue v : inJson.values()) if (v.getCarrier().equals(carrier)) res.add(v);
        return res;
    }

    @JsonProperty("var")
    public Map<String, List<Descriptor>> getVar() {
        return var;
    }

    @JsonProperty("var")
    public void setVar(Map<String, List<Descriptor>> var) {
        this.var = var;
    }

    @JsonProperty("context")
    public Map<String,String> getContext() {
        return context;
    }

    @JsonProperty("context")
    public void setContext(Map<String,String> context) {
        this.context = context;
    }

    @JsonProperty("template")
    public String getTemplate() {
        return template;
    }

    @JsonProperty("template")
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty("@documentation")
    public String getDocumentation() {
        return documentation;
    }

    @JsonProperty("@documentation")
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @JsonProperty("@return")
    public String getReturnValue() {
        return returnValue;
    }

    @JsonProperty("@return")
    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(var).append(context).append(template).append(documentation).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TemplateBindingsSchema) == false) {
            return false;
        }
        TemplateBindingsSchema rhs = ((TemplateBindingsSchema) other);
        return new EqualsBuilder().append(var, rhs.var).append(context, rhs.context).append(template, rhs.template).append(documentation, rhs.documentation).isEquals();
    }

}
