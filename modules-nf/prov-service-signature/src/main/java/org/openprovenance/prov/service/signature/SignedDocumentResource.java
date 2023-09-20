package org.openprovenance.prov.service.signature;


import org.openprovenance.prov.storage.api.DocumentResource;

public interface SignedDocumentResource extends DocumentResource {

    public String getSignedfilepath() ;

    public void setSignedfilepath(String signedfilepath) ;

}
