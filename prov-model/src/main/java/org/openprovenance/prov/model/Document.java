package org.openprovenance.prov.model;

import java.util.Hashtable;
import java.util.List;

public interface Document {

    List<StatementOrBundle> getStatementOrBundle();

    void setNss(Hashtable<String, String> namespaces);

    Hashtable<String, String> getNss();

}
