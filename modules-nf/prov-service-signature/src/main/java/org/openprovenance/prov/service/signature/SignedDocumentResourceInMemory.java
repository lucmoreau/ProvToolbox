package org.openprovenance.prov.service.signature;

import org.openprovenance.prov.service.core.memory.DocumentResourceInMemory;
import org.openprovenance.prov.storage.api.DocumentResource;

public class SignedDocumentResourceInMemory extends DocumentResourceInMemory implements SignedDocumentResource  {

    private String signedfilepath;

    public SignedDocumentResourceInMemory(DocumentResource d) {
        this.setVisibleId(d.getVisibleId());
        this.setStorageId(d.getStorageId());
        this.setExpires(d.getExpires());
        this.setThrown(d.getThrown());
    }

    @Override
    public String getSignedfilepath() {
        return signedfilepath;
    }

    @Override
    public void setSignedfilepath(String signedfilepath) {
        this.signedfilepath=signedfilepath;
    }
}
