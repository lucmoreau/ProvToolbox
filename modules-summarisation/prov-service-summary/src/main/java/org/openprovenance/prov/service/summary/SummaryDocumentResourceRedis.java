package org.openprovenance.prov.service.summary;

import org.openprovenance.prov.service.core.memory.DocumentResourceInMemory;
import org.openprovenance.prov.storage.api.DocumentResource;

public class SummaryDocumentResourceRedis extends DocumentResourceInMemory implements SummaryDocumentResource, Cloneable {

    private String configId;

    private String ptypesId;


    @Override
    public void setConfigId(String serialConfigId) {
        this.configId=serialConfigId;
    }

    @Override
    public String getConfigId() {
        return configId;
    }

    @Override
    public void setPtypesId(String ptypesId) {
        this.ptypesId=ptypesId;
    }

    @Override
    public String getPtypesId() {
        return ptypesId;
    }

    public SummaryDocumentResourceRedis(DocumentResource d) {
        this.setVisibleId(d.getVisibleId());
        this.setStorageId(d.getStorageId());
        this.setExpires(d.getExpires());
        this.setThrown(d.getThrown());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
