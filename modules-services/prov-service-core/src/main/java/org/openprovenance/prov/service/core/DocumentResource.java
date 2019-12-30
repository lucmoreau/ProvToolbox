package org.openprovenance.prov.service.core;

import org.openprovenance.prov.model.Document;

import java.util.Date;
import java.util.Map;


public interface DocumentResource {

    String getVisibleId() ;

    void setVisibleId(String visibleId) ;

    String getStorageId() ;

    void setStorageId(String storageId) ;

    Date getExpires() ;

    void setExpires(Date expires);

    ResourceIndex.StorageKind getKind() ;

    void setKind(ResourceIndex.StorageKind kind) ;

    Document document();

    void setDocument(Document doc);

    public Throwable getThrown();

    public void setThrown(Throwable thrown) ;

    public Map<String, Object> getExtension() ;

    public void setExtension(Map<String, Object> extension) ;

}
