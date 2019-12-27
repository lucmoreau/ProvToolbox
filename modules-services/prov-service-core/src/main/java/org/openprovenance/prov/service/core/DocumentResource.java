package org.openprovenance.prov.service.core;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.model.Document;


public class DocumentResource {
    final static private Map<String,DocumentResource> table=new HashMap<String,DocumentResource>();
    private static ResourceIndex theIndexer=new ResourceIndexAsMap(table);


    static public ResourceIndex getResourceIndex() {
        return theIndexer;
    }

    public String graphId;
    public Document bundle;
    public String mimeType;
    public String filepath;
    public String graphpath;

    public String dotFilepath;
    public String svgFilepath;
    public String pdfFilepath;
    public String url;

    public Throwable thrown;
    
    public ProvFormat format;

    public Document document;


    public String complete;

    public String reportFile;

    public String jsonFile;

    public String deposited=null;


    public Date expires;

    public Map<String,Object> extension=new HashMap<>();
    
}
