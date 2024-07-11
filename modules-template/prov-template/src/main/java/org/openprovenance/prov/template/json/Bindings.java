package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.template.expander.deprecated.BindingsBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Bindings {
    public Map<String, Descriptors> var;
    public Map<String, Descriptors> vargen;
    public Map<String, String> context;

    @Override
    public String toString() {
        return "Bindings{" +
                "var=" + var +
                ", vargen=" + vargen +
                ", context=" + context +
                ", linked=" + linked +
                ", template='" + template + '\'' +
                '}';
    }

    public Map<String, String> linked;
    public String template;

    public BindingsBean toBean() {
        BindingsBean bindingsBean = new BindingsBean();
        if (var!=null) bindingsBean.var=var.keySet().stream().collect(Collectors.toMap(k -> k, k -> var.get(k).toList()));
        if (vargen!=null) bindingsBean.vargen=vargen.keySet().stream().collect(Collectors.toMap(k -> k, k -> vargen.get(k).toList()));
        bindingsBean.context=context;
        bindingsBean.template=template;
        bindingsBean.linked=linked;
        return bindingsBean;
    }

    public static Bindings fromStream(ObjectMapper om, InputStream is) throws IOException {
        return om.readValue(is, Bindings.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bindings bindings = (Bindings) o;
        return Objects.equals(var, bindings.var) && Objects.equals(vargen, bindings.vargen) && Objects.equals(context, bindings.context) && Objects.equals(linked, bindings.linked) && Objects.equals(template, bindings.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(var, vargen, context, linked, template);
    }
}