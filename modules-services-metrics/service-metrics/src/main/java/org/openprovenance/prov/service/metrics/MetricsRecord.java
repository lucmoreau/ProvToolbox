package org.openprovenance.prov.service.metrics;

import java.util.Map;

public class MetricsRecord {
    public  String id;
    public  String artifact;
    public  Object traffic;
    public  Object validationReport;
    public  Object features;
    public  Object metrics;
    public  Object hash;
    public  String created_at;

    public MetricsRecord(String id, String artifact, Object metrics, Object features, Object validationReport, Object traffic, Object hash) {
        this.id = id;
        this.artifact = artifact;
        this.metrics = metrics;
        this.features = features;
        this.validationReport = validationReport;
        this.traffic = traffic;
        this.hash = hash;
    }

    public MetricsRecord() {
    }

    @Override
    public String toString() {
        return "MetricsRecord{" +
                "id='" + id + '\'' +
                ", artifact='" + artifact + '\'' +
                ", traffic=" + traffic +
                ", validationReport=" + validationReport +
                ", features=" + features +
                ", metrics=" + metrics +
                ", hash=" + hash +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
