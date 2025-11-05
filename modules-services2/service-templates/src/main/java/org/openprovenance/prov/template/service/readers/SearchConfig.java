package org.openprovenance.prov.template.service.readers;

public class SearchConfig {
    public String base_relation;
    public String from_date;
    public String to_date;
    public Integer limit;

    @Override
    public String toString() {
        return "SearchConfig{" +
                "base_relation='" + base_relation + '\'' +
                ", from_date='" + from_date + '\'' +
                ", to_date='" + to_date + '\'' +
                ", limit=" + limit +
                '}';
    }
}