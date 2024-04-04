package org.openprovenance.prov.service;

import java.util.List;

public interface RecordsProcessor<T> {
    List<T> process(List<Object[]> ll);
}
