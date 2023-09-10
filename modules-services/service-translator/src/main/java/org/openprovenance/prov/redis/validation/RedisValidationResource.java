package org.openprovenance.prov.redis.validation;

import org.openprovenance.prov.service.validation.ValidationResource;
import org.openprovenance.prov.storage.redis.RedisDocumentResource;

import java.util.Map;

public class RedisValidationResource extends RedisDocumentResource implements ValidationResource {

    public RedisValidationResource(Map<String, String> m) {
        super(m);
    }

    @Override
    public String getReport() {
        return getMap().get(RedisValidationResourceIndex.FIELD_REPORT_ID);
    }

    @Override
    public boolean getCompleted() {
        return Boolean.valueOf(getMap().get(RedisValidationResourceIndex.FIELD_COMPLETED));
    }

    @Override
    public void setCompleted(boolean completed) {
        getMap().put(RedisValidationResourceIndex.FIELD_COMPLETED, Boolean.toString(completed));
    }

    @Override
    public String getMatrix() {
        return getMap().get(RedisValidationResourceIndex.FIELD_MATRIX_ID);
    }

    @Override
    public void setMatrix(String matrix) {
        getMap().put(RedisValidationResourceIndex.FIELD_MATRIX_ID,matrix);
    }

    @Override
    public void setPngMatrix(String matrixPngId) {
        getMap().put(RedisValidationResourceIndex.FIELD_MATRIX_PNG_ID,matrixPngId);
    }

    @Override
    public String getPngMatrix() {
        return getMap().get(RedisValidationResourceIndex.FIELD_MATRIX_PNG_ID);
    }

    @Override
    public void setJsonReport(String jsonReportStorageId) {
        getMap().put(RedisValidationResourceIndex.FIELD_JSON_REPORT_ID,jsonReportStorageId);
    }

    @Override
    public String getJsonReportStorageId() {
        return getMap().get(RedisValidationResourceIndex.FIELD_JSON_REPORT_ID);
    }

    @Override
    public void setReport(String reportId) {
        getMap().put(RedisValidationResourceIndex.FIELD_REPORT_ID,reportId);

    }

    @Override
    public String toString() {
        return "<<RedisValidationResource : "  + getMap() + ">>";
    }
}


