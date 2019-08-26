package org.openprovenance.prov.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "alternate1", "alternate2"})

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlternateOf implements org.openprovenance.prov.model.AlternateOf, Equals, HashCode, ToString {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;

    protected QualifiedName alternate1;
    protected QualifiedName alternate2;


    final ProvUtilities u=new ProvUtilities();



    private AlternateOf() {}



    public AlternateOf(QualifiedName alternate1,
                       QualifiedName alternate2) {
        this.alternate1 = alternate1;
        this.alternate2 = alternate2;
    }



    @Override
    public void setAlternate1(QualifiedName alternate1) {
        this.alternate1 =alternate1;
    }

    @Override
    public void setAlternate2(QualifiedName alternate2) {
        this.alternate2 =alternate2;
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAlternate2() {
        return alternate2;
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAlternate1() {
        return alternate1;
    }


    @JsonIgnore
    @Override
    public Kind getKind() {
        return Kind.PROV_ALTERNATE;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof AlternateOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final AlternateOf that = ((AlternateOf) object);
        equalsBuilder.append(this.getAlternate1(), that.getAlternate1());
        equalsBuilder.append(this.getAlternate2(), that.getAlternate2());
    }

    public boolean equals(Object object) {
        if (!(object instanceof AlternateOf)) {
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
        hashCodeBuilder.append(this.getAlternate1());
        hashCodeBuilder.append(this.getAlternate2());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {



        {
            QualifiedName theEntity;
            theEntity = this.getAlternate2();
            toStringBuilder.append("alternate2", theEntity);
        }

        {
            QualifiedName theAgent;
            theAgent = this.getAlternate1();
            toStringBuilder.append("alternate1", theAgent);
        }






    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }


}
