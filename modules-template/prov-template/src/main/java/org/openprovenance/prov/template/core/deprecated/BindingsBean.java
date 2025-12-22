package org.openprovenance.prov.template.core.deprecated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openprovenance.prov.template.core.Bindings;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Deprecated
public class BindingsBean {
    public Map<String, List<Object>> var;
    public Map<String, List<Object>> vargen;
    public Map<String, String> context;
    public String template;
    public Map<String, String> linked;

    static public BindingsBean toBean(Bindings bindings) {
        BindingsBean bindingsBean = new BindingsBean();
        if (bindings.var!=null) bindingsBean.var=bindings.var.keySet().stream().collect(Collectors.toMap(k -> k, k -> bindings.var.get(k).toList()));
        if (bindings.vargen!=null) bindingsBean.vargen=bindings.vargen.keySet().stream().collect(Collectors.toMap(k -> k, k -> bindings.vargen.get(k).toList()));
        bindingsBean.context=bindings.context;
        bindingsBean.template=bindings.template;
        bindingsBean.linked=bindings.linked;
        return bindingsBean;
    }

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