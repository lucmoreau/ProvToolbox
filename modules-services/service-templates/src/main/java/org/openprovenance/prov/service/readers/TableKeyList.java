package org.openprovenance.prov.service.readers;

import java.util.LinkedList;
import java.util.List;

public class TableKeyList {
    public List<TableKey> key=new LinkedList<>();

    @Override
    public String toString() {
        return "TableKeyList{" +
                "key=" + key +
                '}';
    }
}
