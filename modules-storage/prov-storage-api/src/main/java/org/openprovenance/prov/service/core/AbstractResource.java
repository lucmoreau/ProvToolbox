package org.openprovenance.prov.service.core;

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

    public Map<String, Object> getExtension() ;

    public void setExtension(Map<String, Object> extension) ;

}
