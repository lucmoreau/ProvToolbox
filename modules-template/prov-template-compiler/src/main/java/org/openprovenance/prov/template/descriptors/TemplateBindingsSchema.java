
package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
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
