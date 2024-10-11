package org.openprovenance.prov.java.typemap;

public interface TypeMapProcessor<T> {
    T process(String map, String set, String list);
}
