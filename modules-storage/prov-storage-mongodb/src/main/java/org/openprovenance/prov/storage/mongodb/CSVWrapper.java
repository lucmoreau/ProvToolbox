package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import java.util.List;

public class CSVWrapper {
    @Id
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public List<String>[] csv;
}
