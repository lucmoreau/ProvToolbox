package org.openprovenance.prov.service.core.readers;

public class TableKey {
    public String isA;
    public Integer ID;

    @Override
    public String toString() {
        return "TableKey{" +
                "isA='" + isA + '\'' +
                ", ID=" + ID +
                '}';
    }
}
