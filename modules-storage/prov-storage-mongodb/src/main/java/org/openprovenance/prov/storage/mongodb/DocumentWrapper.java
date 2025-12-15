package org.openprovenance.prov.storage.mongodb;

import org.mongojack.Id;

public class DocumentWrapper {
    @Id
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public org.openprovenance.prov.vanilla.Document document;
}
