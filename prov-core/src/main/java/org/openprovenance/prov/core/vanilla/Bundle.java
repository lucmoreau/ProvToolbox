package org.openprovenance.prov.core.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Bundle implements org.openprovenance.prov.model.Bundle, Equals, ToString, HashCode {


    private Namespace namespaces=new Namespace();
    private List<Statement> statements=new LinkedList<>();
    private org.openprovenance.prov.model.QualifiedName id;

    public Bundle(org.openprovenance.prov.model.QualifiedName id, Namespace namespace, Collection<Statement> statements) {
        this.id=id;
        if (namespace!=null) {
            this.namespaces=namespace;
        } else {
            this.namespaces=new Namespace();
        }
        if (statements!=null) this.statements.addAll(statements);
    }

    private Bundle(){}


    @Override
    public List<Statement> getStatement() {
        return statements;
    }

    @Override
    public void setNamespace(Namespace namespaces) {
        if (namespaces!=null) this.namespaces=namespaces;
    }

    @Override
    public Namespace getNamespace() {
        return namespaces;
    }

    /**
     * Gets the value of the id property.  A null value means that the object has not been identified.  {@link Entity}, {@link Activity},
     * {@link Agent} have a non-null identifier.
     *
     * @return possible object is
     * {@link QualifiedName }
     */
    @Override
    public org.openprovenance.prov.model.QualifiedName getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link QualifiedName }
     */
    @Override
    public void setId(org.openprovenance.prov.model.QualifiedName value) {
        this.id=value;
    }

    /**
     * Gets the type of a provenance statement
     *
     * @return {@link Kind}
     */
    @Override
    public Kind getKind() {
        return Kind.PROV_BUNDLE;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Bundle)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Bundle that = ((Bundle) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getStatement(), that.getStatement());
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
        hashCodeBuilder.append(this.getId());
        hashCodeBuilder.append(this.getStatement());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<Statement> theStatement;
            theStatement = this.getStatement();
            toStringBuilder.append("id", this.getId());
            toStringBuilder.append("statement", theStatement);
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
