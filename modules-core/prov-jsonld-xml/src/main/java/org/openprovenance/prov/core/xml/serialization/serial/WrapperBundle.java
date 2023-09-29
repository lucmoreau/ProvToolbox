package org.openprovenance.prov.core.xml.serialization.serial;

import org.openprovenance.prov.core.xml.XML_Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.vanilla.Bundle;

import java.util.List;

public class WrapperBundle implements XML_Bundle, StatementOrBundle {
    private final QualifiedName id;
    private final List<Statement> statements;

    public Namespace getNamespace() {
        return null;
    }

    public void setNamespace(Namespace ns) {

    }

    public List<Statement> getStatement() {
        return statements;
    }

    public org.openprovenance.prov.model.QualifiedName getId() {
        return id;
    }

    public void setId(QualifiedName value) {

    }

    public WrapperBundle(Bundle bun) {
        this.statements=bun.getStatement();
        this.id=bun.getId();
    }

    public StatementOrBundle.Kind getKind() {
        return StatementOrBundle.Kind.PROV_BUNDLE;
    }
}
