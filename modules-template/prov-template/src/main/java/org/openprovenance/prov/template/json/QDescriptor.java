package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

public class QDescriptor implements SingleDescriptor {
    public static final String AT_ID = "@id";
    @JsonProperty(AT_ID)
    public String id;
    @JsonIgnore
    public String namespace;

    @Override
    public String toString() {
        return "QDescriptor{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public Object toObject() {
        Map<String,String> res=new HashMap<>();
        res.put(AT_ID, id);
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QDescriptor that = (QDescriptor) o;
        return Objects.equals(id, that.id) && Objects.equals(namespace, that.namespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, namespace);
    }
}
