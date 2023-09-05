package org.openprovenance.prov.template.json;

import org.openprovenance.prov.template.expander.BindingsBean;

import java.util.Map;
import java.util.stream.Collectors;

public class Bindings {
    public Map<String, Descriptors> var;
    public Map<String, Descriptors> vargen;
    public Map<String, String> context;
    public String template;

    @Override
    public String toString() {
        return "TestBean{" +
                "var=" + var +
                ", vargen=" + vargen +
                ", context=" + context +
                ", template='" + template + '\'' +
                '}';
    }

    public BindingsBean toBean() {
        BindingsBean bindingsBean = new BindingsBean();
        bindingsBean.var=var.keySet().stream().collect(Collectors.toMap(k -> k, k -> var.get(k).toList()));
        bindingsBean.vargen=vargen.keySet().stream().collect(Collectors.toMap(k -> k, k -> vargen.get(k).toList()));
        bindingsBean.context=context;
        bindingsBean.template=template;
        return bindingsBean;
    }
}