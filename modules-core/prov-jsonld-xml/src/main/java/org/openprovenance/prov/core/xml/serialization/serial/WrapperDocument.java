package org.openprovenance.prov.core.xml.serialization.serial;

import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.vanilla.Document;

import java.util.List;

public class WrapperDocument {
    public Namespace getNamespace() {
        return null;
    }
    public List<StatementOrBundle> getStatementOrBundle() {
        return statementOrBundle;
    }

    private final List<StatementOrBundle> statementOrBundle;

    public WrapperDocument(Document doc) {
        this.statementOrBundle=doc.getStatementOrBundle();
    }
}
