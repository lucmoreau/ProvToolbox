package org.openprovenance.prov.service.validation.memory;

import org.openprovenance.prov.service.core.memory.DocumentResourceInMemory;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.service.validation.ValidationResource;

public class ValidationResourceInMemory extends DocumentResourceInMemory implements ValidationResource, Cloneable {
    private String report;
    private boolean completed;
    private String matrixStoreId;
    private String matrixPngStoreId;
    private String jsonReportStorageId;


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ValidationResourceInMemory() {}

    public ValidationResourceInMemory(DocumentResource d) {
        this.setVisibleId(d.getVisibleId());
        this.setStorageId(d.getStorageId());
        this.setExpires(d.getExpires());
        this.setThrown(d.getThrown());
    }


    @Override
    public String getReport() {
        return report;
    }

    @Override
    public boolean getCompleted() {
        return completed;
    }

    @Override
    public void setCompleted(boolean completed) {
        this.completed=completed;
    }

    @Override
    public String getMatrix() {
        return matrixStoreId;
    }

    @Override
    public void setMatrix(String matrix) {
        matrixStoreId=matrix;
    }

    @Override
    public void setPngMatrix(String matrixPngId) {
        this.matrixPngStoreId=matrixPngId;
    }

    @Override
    public String getPngMatrix() {
        return matrixPngStoreId;
    }

    @Override
    public void setJsonReport(String jsonReportStorageId) {
        this.jsonReportStorageId=jsonReportStorageId;
    }

    @Override
    public String getJsonReportStorageId() {
        return jsonReportStorageId;
    }

    @Override
    public void setReport(String report) {
        this.report=report;
    }

}
