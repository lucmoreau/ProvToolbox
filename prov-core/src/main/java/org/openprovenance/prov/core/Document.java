package org.openprovenance.prov.core;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.core.serialization.Constants;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.StatementOrBundle;

import java.util.LinkedList;
import java.util.List;

//@JsonPropertyOrder({ "namespace"})

public class Document implements org.openprovenance.prov.model.Document, Equals, ToString, HashCode, Constants, org.openprovenance.prov.core.jsonld.Document {

    private List<StatementOrBundle> statementsOrBundle;
    private Namespace namespace;

    public Document(List<StatementOrBundle> statementsOrBundle) {
        this.statementsOrBundle=statementsOrBundle;
    }

    public Document() {
        this.statementsOrBundle=new LinkedList<>();
    }

    @Override
  //  @JsonFilter("nsFilter")
    public Namespace getNamespace() {
        return namespace;
    }

  /*  @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = PROPERTY_AT_TYPE)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = WasDerivedFrom.class,     name = PROPERTY_PROV_DERIVATION),
            @JsonSubTypes.Type(value = AlternateOf.class,        name = PROPERTY_PROV_ALTERNATE),
            @JsonSubTypes.Type(value = SpecializationOf.class,   name = PROPERTY_PROV_SPECIALIZATION),
            @JsonSubTypes.Type(value = WasAttributedTo.class,    name = PROPERTY_PROV_ATTRIBUTION),
            @JsonSubTypes.Type(value = WasAssociatedWith.class,  name = PROPERTY_PROV_ASSOCIATION),
            @JsonSubTypes.Type(value = WasGeneratedBy.class,     name = PROPERTY_PROV_GENERATION),
            @JsonSubTypes.Type(value = Used.class,               name = PROPERTY_PROV_USED),
            @JsonSubTypes.Type(value = Activity.class,           name = PROPERTY_PROV_ACTIVITY),
            @JsonSubTypes.Type(value = Agent.class,              name = PROPERTY_PROV_AGENT),
            @JsonSubTypes.Type(value = Entity.class,             name = PROPERTY_PROV_ENTITY)
    })

   */
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