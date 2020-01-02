package org.openprovenance.prov.service.core;

import org.openprovenance.prov.model.Document;

import java.util.Date;
import java.util.Map;


public interface NonDocumentResource extends  AbstractResource {
    public static final String NON_DOCUMENT = "NON_DOCUMENT";
    static public String getResourceKind() {
        return NON_DOCUMENT;
    }

    public String getMediaType();

    public void setMediaType(String type);

}
