package org.openprovenance.prov.service.metrics;

import java.util.Map;

public class MetricsRecord {
    public  String id;
    public  Object traffic;
    public  Object validationReport;
    public  Object features;
    public  Object metrics;
    public  String artifact;

    public MetricsRecord(String id, String artifact, Object metrics, Map<String, Integer> features, Object validationReport, Object traffic) {
        this.id = id;
        this.artifact = artifact;
        this.metrics = metrics;
        this.features = features;
        this.validationReport = validationReport;
        this.traffic = traffic;
        
    }

    public MetricsRecord() {
    }

    @Override
    public String toString() {
        return "MetricsRecord{" +
                "id='" + id + '\'' +
                ", traffic=" + traffic +
                ", validationReport=" + validationReport +
                ", features=" + features +
                ", metrics=" + metrics +
                ", artifact='" + artifact + '\'' +
                '}';
    }
}
