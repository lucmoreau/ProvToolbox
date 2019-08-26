package org.openprovenance.prov.core;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.core.serialization.CustomAttributesSerializer;
import org.openprovenance.prov.core.serialization.CustomKeyDeserializer;
import org.openprovenance.prov.core.serialization.CustomMapSerializer;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.util.*;
import java.util.stream.Collectors;

@JsonPropertyOrder({ "specificEntity", "generalEntity"})

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecializationOf implements org.openprovenance.prov.model.SpecializationOf, Equals, HashCode, ToString {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;

    protected QualifiedName specificEntity;
    protected QualifiedName generalEntity;


    final ProvUtilities u=new ProvUtilities();



    private SpecializationOf() {}



    public SpecializationOf(QualifiedName specificEntity,
                            QualifiedName generalEntity ) {
        this.specificEntity = specificEntity;
        this.generalEntity = generalEntity;
    }



    @Override
    public void setSpecificEntity(QualifiedName aid) {
        this.specificEntity =aid;
    }

    @Override
    public void setGeneralEntity(QualifiedName eid) {
        this.generalEntity =eid;
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getGeneralEntity() {
        return generalEntity;
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getSpecificEntity() {
        return specificEntity;
    }


    @JsonIgnore
    @Override
    public Kind getKind() {
        return Kind.PROV_SPECIALIZATION;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SpecializationOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final SpecializationOf that = ((SpecializationOf) object);
        equalsBuilder.append(this.getSpecificEntity(), that.getSpecificEntity());
        equalsBuilder.append(this.getGeneralEntity(), that.getGeneralEntity());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SpecializationOf)) {
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
        hashCodeBuilder.append(this.getSpecificEntity());
        hashCodeBuilder.append(this.getGeneralEntity());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {



        {
            QualifiedName theEntity;
            theEntity = this.getGeneralEntity();
            toStringBuilder.append("generalEntity", theEntity);
        }

        {
            QualifiedName theAgent;
            theAgent = this.getSpecificEntity();
            toStringBuilder.append("specificEntity", theAgent);
        }






    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }


}
