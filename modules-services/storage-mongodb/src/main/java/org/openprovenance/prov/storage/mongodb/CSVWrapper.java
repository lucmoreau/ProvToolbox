package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.ObjectId;
import org.openprovenance.prov.template.expander.BindingsJson.BindingsBean;

import java.util.List;

public class CSVWrapper {
    private String id;
    @ObjectId
    @JsonProperty("_id")
    public String getId() {
        return id;
    }
    @ObjectId
    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }
    public List<String>[] csv;
}
