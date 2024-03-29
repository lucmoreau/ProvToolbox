package org.openprovenance.prov.template.expander.deprecated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Map;

@Deprecated
public class BindingsBean {
    public Map<String, List<Object>> var;
    public Map<String, List<Object>> vargen;
    public Map<String, String> context;
    public String template;
    public Map<String, String> linked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BindingsBean that = (BindingsBean) o;

        return new EqualsBuilder().append(var, that.var).append(vargen, that.vargen).append(context, that.context).append(template, that.template).append(linked, that.linked).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(var).append(vargen).append(context).append(template).append(linked).toHashCode();
    }

    @Override
    public String toString() {
        return "BindingsBean{" +
                "var=" + var +
                ", vargen=" + vargen +
                ", context=" + context +
                ", template='" + template + '\'' +
                ", linked=" + linked +
                '}';
    }
}