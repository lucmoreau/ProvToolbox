package org.openprovenance.prov.service.core;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.model.Document;


public class DocumentResource {
    final static private Map<String,DocumentResource> table=new HashMap<String,DocumentResource>();
    //private static ResourceIndex theIndexer=new ResourceIndexAsMap(table);
    private static ResourceIndex theIndexer=new ResourceIndexAsMap(table);

    static public ResourceIndex getResourceIndex() {
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
    public Map<String,Object> extension=new HashMap<>();

    // public ProvFormat format;

    private Document doc;
    public Document document() {
        return doc;
    };

    public void setDocument(Document doc){
        this.doc=doc;
    }


    //public Document bundle;
    //public String mimeType;
    //public String graphpath;
    //public String dotFilepath;
    //public String svgFilepath;
    //public String pdfFilepath;
    //public String url;
    //public String complete;
    //public String reportFile;
    //public String jsonFile;
    //public String deposited=null;


}
