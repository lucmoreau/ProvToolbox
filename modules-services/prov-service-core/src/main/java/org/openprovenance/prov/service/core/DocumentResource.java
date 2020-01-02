package org.openprovenance.prov.service.core;

import org.openprovenance.prov.model.Document;

import java.util.Date;
import java.util.Map;


public interface DocumentResource extends AbstractResource {
    public static final String DOCUMENT = "DOCUMENT";
    static public String getResourceKind() {
        return DOCUMENT;
    }

    //Document document();

    //void setDocument(Document doc);

    public Throwable getThrown();

    public void setThrown(Throwable thrown) ;

}
