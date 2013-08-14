package org.openprovenance.prov.model;

import java.util.Hashtable;
import java.util.List;

public interface NamedBundle extends Identifiable, StatementOrBundle {

    List<Statement> getStatement();

    void setNss(Hashtable<String, String> namespaces);

    Hashtable<String, String> getNss();

}
