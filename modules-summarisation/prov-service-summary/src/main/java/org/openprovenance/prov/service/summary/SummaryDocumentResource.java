package org.openprovenance.prov.service.summary;

import org.openprovenance.prov.scala.summary.Level0Mapper;
import org.openprovenance.prov.scala.summary.SummaryIndex;
import org.openprovenance.prov.storage.api.DocumentResource;

public interface SummaryDocumentResource extends DocumentResource {
  //  public SummaryIndex getsIndex();

   // public void setsIndex(SummaryIndex sIndex);

  //  public Level0Mapper getConfig() ;

  //  public void setConfig(Level0Mapper config) ;

  //  public int getLevel();

  //  public void setLevel(int level);

    void setConfigId(String serialConfigId);
    String getConfigId();

    void setPtypesId(String ptypesId);
    String getPtypesId();
}
