package org.openprovenance.prov.core;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@JsonPropertyOrder({ "@id", "startTime", "endTime" })

public class Activity implements org.openprovenance.prov.model.Activity, Equals, HashCode, ToString {

    private Optional<org.openprovenance.prov.model.QualifiedName> id;
    private Optional<XMLGregorianCalendar> startTime;
    private Optional<XMLGregorianCalendar> endTime;
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Location> location = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();

    static public org.openprovenance.prov.model.Activity newActivity(org.openprovenance.prov.model.QualifiedName id,
                                                                     XMLGregorianCalendar startTime,
                                                                     XMLGregorianCalendar endTime,
                                                                     Collection<Attribute> attributes) {
        Activity res = new Activity();
        res.setId(id);
        res.setStartTime(startTime);
        res.setEndTime(endTime);

        return res;

    }




    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonProperty("@id")
    @Override
    public org.openprovenance.prov.model.QualifiedName getId() {
        return id.orElse(null);
    }


    @JsonIgnore
    @Override
    public Kind getKind() {
        return Kind.PROV_ACTIVITY;
    }



    @JsonProperty("@id")
    @Override
    public void setId(org.openprovenance.prov.model.QualifiedName value) {
        id = Optional.ofNullable(value);
    }



    @Override
    public XMLGregorianCalendar getStartTime() {
        return startTime.orElse(null);
    }

    @Override
    public void setStartTime(XMLGregorianCalendar value) {
        startTime = Optional.ofNullable(value);
    }

    @Override
    public XMLGregorianCalendar getEndTime() {
        return endTime.orElse(null);
    }

    @Override
    public void setEndTime(XMLGregorianCalendar value) {
        endTime = Optional.ofNullable(value);
    }

    @JsonDeserialize(contentAs = LangString.class)
    @Override
    public List<org.openprovenance.prov.model.LangString> getLabel() {
        return labels;
    }

    @JsonDeserialize(contentAs = Location.class)
    @Override
    public List<org.openprovenance.prov.model.Location> getLocation() {
        return location;
    }

    @JsonDeserialize(contentAs = Type.class)
    @Override
    public List<org.openprovenance.prov.model.Type> getType() {
        return type;
    }


    @JsonDeserialize(contentAs = Other.class)
    @Override
    public List<org.openprovenance.prov.model.Other> getOther() {
        return other;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Activity)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Activity that = ((Activity) object);
        equalsBuilder.append(this.getStartTime(), that.getStartTime());
        equalsBuilder.append(this.getEndTime(), that.getEndTime());
        equalsBuilder.append(this.getLabel(), that.getLabel());
        equalsBuilder.append(this.getLocation(), that.getLocation());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getOther(), that.getOther());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Activity)) {
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
        hashCodeBuilder.append(this.getStartTime());
        hashCodeBuilder.append(this.getEndTime());
        hashCodeBuilder.append(this.getLabel());
        hashCodeBuilder.append(this.getLocation());
        hashCodeBuilder.append(this.getType());
        hashCodeBuilder.append(this.getOther());
        hashCodeBuilder.append(this.getId());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            XMLGregorianCalendar theStartTime;
            theStartTime = this.getStartTime();
            toStringBuilder.append("startTime", theStartTime);
        }
        {
            XMLGregorianCalendar theEndTime;
            theEndTime = this.getEndTime();
            toStringBuilder.append("endTime", theEndTime);
        }
        {
            List<org.openprovenance.prov.model.LangString> theLabel;
            theLabel = this.getLabel();
            toStringBuilder.append("label", theLabel);
        }
        {
            List<org.openprovenance.prov.model.Location> theLocation;
            theLocation = this.getLocation();
            toStringBuilder.append("location", theLocation);
        }
        {
            List<org.openprovenance.prov.model.Type> theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
        {
            List<org.openprovenance.prov.model.Other> theOthers;
            theOthers = this.getOther();
            toStringBuilder.append("others", theOthers);
        }
        {
            org.openprovenance.prov.model.QualifiedName theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }
    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}