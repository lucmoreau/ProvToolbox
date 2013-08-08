package org.openprovenance.prov.model;

import java.util.Hashtable;
import java.util.List;

public interface Document {

    List<StatementOrBundle> getEntityAndActivityAndWasGeneratedBy();

    void setNss(Hashtable<String, String> namespaces);

    Hashtable<String, String> getNss();

}
