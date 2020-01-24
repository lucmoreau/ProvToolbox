package org.openprovenance.prov.storage.api;


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
