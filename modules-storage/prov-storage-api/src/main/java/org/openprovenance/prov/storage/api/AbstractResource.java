package org.openprovenance.prov.storage.api;

import java.util.Date;
import java.util.Map;


public interface AbstractResource {

    String getVisibleId() ;

    void setVisibleId(String visibleId) ;

    String getStorageId() ;

    void setStorageId(String storageId) ;

    Date getExpires() ;

    void setExpires(Date expires);

    ResourceIndex.StorageKind getKind() ;

    void setKind(ResourceIndex.StorageKind kind) ;

    Map<String, Object> getExtension() ;

    void setExtension(Map<String, Object> extension) ;

}
