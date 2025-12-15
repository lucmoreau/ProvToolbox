package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.Id;
import org.mongojack.ObjectId;

public class TypeWrapper<TYPE>{
    static public final String VALUE="value";

    @Id
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty(VALUE)
    public TYPE value;
}
