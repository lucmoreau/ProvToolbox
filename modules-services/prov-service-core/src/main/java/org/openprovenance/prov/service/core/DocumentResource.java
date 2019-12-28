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

    public String visibleId;
    public String storageId;
    public Date expires;
    public ResourceIndex.StorageKind kind;

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
