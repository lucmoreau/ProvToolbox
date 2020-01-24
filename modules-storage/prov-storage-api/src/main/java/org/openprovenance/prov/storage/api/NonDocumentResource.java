package org.openprovenance.prov.storage.api;

public interface NonDocumentResource extends  AbstractResource {
    String NON_DOCUMENT = "NON_DOCUMENT";
    static String getResourceKind() {
        return NON_DOCUMENT;
    }

    String getMediaType();

    void setMediaType(String type);

}
