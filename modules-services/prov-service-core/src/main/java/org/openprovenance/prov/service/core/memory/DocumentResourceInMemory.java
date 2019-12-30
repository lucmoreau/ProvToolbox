package org.openprovenance.prov.service.core.memory;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.ResourceIndex;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DocumentResourceInMemory implements DocumentResource {
    final static private Map<String, DocumentResource> table = new HashMap<>();
    //private static ResourceIndex theIndexer=new ResourceIndexAsMap(table);
    private static ResourceIndex<DocumentResource> theIndexer = new DocumentResourceIndexInMemory(table);

    public Throwable getThrown() {
        return thrown;
    }

    public void setThrown(Throwable thrown) {
        this.thrown = thrown;
    }

    public Map<String, Object> getExtension() {
        return extension;
    }

    public void setExtension(Map<String, Object> extension) {
        this.extension = extension;
    }

    static public ResourceIndex<DocumentResource> getResourceIndex() {
        return theIndexer;
    }

    public String getVisibleId() {
        return visibleId;
    }

    public void setVisibleId(String visibleId) {
        this.visibleId = visibleId;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public ResourceIndex.StorageKind getKind() {
        return kind;
    }

    public void setKind(ResourceIndex.StorageKind kind) {
        this.kind = kind;
    }

    private String visibleId;
    private String storageId;
    private Date expires;
    private ResourceIndex.StorageKind kind;

    // transient
    public Throwable thrown;
    public Map<String, Object> extension = new HashMap<>();


    private Document doc;

    public Document document() {
        return doc;
    }



    public void setDocument(Document doc) {
        this.doc = doc;
    }

}