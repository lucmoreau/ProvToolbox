package org.openprovenance.prov.service.validation;


import org.openprovenance.prov.storage.api.DocumentResource;

public interface ValidationResource  extends DocumentResource {

    String VALIDATION = "VALIDATION";

    static String getResourceKind() {
        return VALIDATION;
    }

    void setReport(String reportId);
    String getReport();
    boolean getCompleted();
    void setCompleted(boolean completed);

    String getMatrix();
    void setMatrix(String matrix);


    static public String COMPLETE="complete";
    static public String REPORT="report";
    static public String BUNDLE="bundle";

    void setPngMatrix(String matrixPngId);
    String getPngMatrix();

    void setJsonReport(String jsonReportStorageId);
    String getJsonReportStorageId();
}
