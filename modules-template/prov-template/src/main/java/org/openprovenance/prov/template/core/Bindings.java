package org.openprovenance.prov.template.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.template.json.Descriptors;
import org.openprovenance.prov.template.json.QDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

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



    public static final ObjectMapper mapper = new ObjectMapper();

    public static Bindings importBindings(JsonNode json) {

        try {
            return mapper.treeToValue(json,Bindings.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON conversion to bean failed", e);
        }
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

    public void addVargen(String var, QDescriptor qDescriptor) {

        if (vargen==null) {
            vargen= new java.util.HashMap<>();
        }
        if (!vargen.containsKey(var)) {
            Descriptors value = new Descriptors();
            value.values = new java.util.ArrayList<>();
            value.values.add(qDescriptor);
            vargen.put(var, value);
        } else {
            vargen.get(var).values.add(qDescriptor);
        }
    }
}