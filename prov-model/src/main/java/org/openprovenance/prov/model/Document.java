package org.openprovenance.prov.model;

import java.util.Hashtable;
import java.util.List;

import org.openprovenance.prov.model.Namespace;

public interface Document {

    List<StatementOrBundle> getStatementOrBundle();

    void setNamespace(Namespace ns);
    
    Namespace getNamespace();

}
