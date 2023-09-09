package org.openprovenance.prov.validation.report;

import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.apache.commons.lang.builder.Equals;
import org.openprovenance.apache.commons.lang.builder.HashCode;
import org.openprovenance.apache.commons.lang.builder.ToString;

import java.util.ArrayList;
import java.util.List;



public class Dependencies
    implements Equals, HashCode, ToString {

    private List<Statement> usedOrWasGeneratedByOrWasStartedBy;

    /**
     * Gets the value of the usedOrWasGeneratedByOrWasStartedBy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the usedOrWasGeneratedByOrWasStartedBy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsedOrWasGeneratedByOrWasStartedBy().add(newItem);
     * </pre>
     *
     *
     * 
     */
    public List<Statement> getStatement() {
        if (usedOrWasGeneratedByOrWasStartedBy == null) {
            usedOrWasGeneratedByOrWasStartedBy = new ArrayList<Statement>();
        }
        return this.usedOrWasGeneratedByOrWasStartedBy;
    }

    public void setStatement(List<Statement> l) {
        this.usedOrWasGeneratedByOrWasStartedBy=l;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Dependencies)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Dependencies that = ((Dependencies) object);
        equalsBuilder.append(this.getStatement(), that.getStatement());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Dependencies)) {
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
        hashCodeBuilder.append(this.getStatement());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<Statement> theUsedOrWasGeneratedByOrWasStartedBy;
            theUsedOrWasGeneratedByOrWasStartedBy = this.getStatement();
            toStringBuilder.append("usedOrWasGeneratedByOrWasStartedBy", theUsedOrWasGeneratedByOrWasStartedBy);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}
