package org.openprovenance.prov.core.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.core.jsonld.serialization.Constants;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Document implements org.openprovenance.prov.model.Document, Equals, ToString, HashCode, Constants {

    private List<StatementOrBundle> statementsOrBundle;
    private Namespace namespace;

    public Document(List<StatementOrBundle> statementsOrBundle) {
        this.statementsOrBundle=statementsOrBundle;
    }
    public Document(Namespace namespace, List<StatementOrBundle> statementsOrBundle) {
        this.namespace=namespace;
        this.statementsOrBundle=statementsOrBundle;
    }
    public Document(Namespace namespace, Collection<Statement> statements, Collection<Bundle> bundles) {
        this.namespace=namespace;
        this.statementsOrBundle=new LinkedList<>();
        if (statements!=null) statementsOrBundle.addAll(statements);
        if (bundles!=null) statementsOrBundle.addAll(bundles);
    }


    public Document() {
        this.statementsOrBundle=new LinkedList<>();
    }

    @Override
    public Namespace getNamespace() {
        return namespace;
    }


    @Override
    public List<StatementOrBundle> getStatementOrBundle() {
        return statementsOrBundle;
    }

    @Override
    public void setNamespace(Namespace namespace) {
        this.namespace=namespace;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Document)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Document that = ((Document) object);
        equalsBuilder.append(this.getStatementOrBundle(), that.getStatementOrBundle());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Document)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getStatementOrBundle());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<StatementOrBundle> theStatementOrBundle;
            theStatementOrBundle = this.getStatementOrBundle();
            toStringBuilder.append("statementOrBundle", theStatementOrBundle);
            Namespace theNamespace=this.getNamespace();
            toStringBuilder.append("namespace", theNamespace);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }


}
