package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.Equals;
import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCode;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToString;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.Key;
import org.openprovenance.prov.model.QualifiedName;

public class Entry implements org.openprovenance.prov.model.Entry, Equals, HashCode, ToString {

    private Key key;
    private QualifiedName entity;

    public Entry(Key key, QualifiedName entity) {
        this.key=key;
        this.entity=entity;
    }

    @Override
    public void setKey(Key key) {
        this.key=key;
    }

    @Override
    public void setEntity(QualifiedName entity) {
        this.entity=entity;
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public QualifiedName getEntity() {
        return entity;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Entry)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Entry that = ((Entry) object);
        equalsBuilder.append(this.getKey(), that.getKey());
        equalsBuilder.append(this.getEntity(), that.getEntity());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Entry)) {
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
        hashCodeBuilder.append(this.getKey());
        hashCodeBuilder.append(this.getEntity());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        toStringBuilder.append("key", this.getKey());
        toStringBuilder.append("entity", this.getEntity());
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }
}
