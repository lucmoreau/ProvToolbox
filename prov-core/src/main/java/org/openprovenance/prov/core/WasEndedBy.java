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
import org.openprovenance.prov.model.Value;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;
import java.util.stream.Collectors;

@JsonPropertyOrder({ "@id", "activity", "trigger", "ender", "time" })

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WasEndedBy implements org.openprovenance.prov.model.WasEndedBy, Equals, HashCode, ToString, HasAttributes {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    private Optional<QualifiedName> id=Optional.empty();
    private Optional<XMLGregorianCalendar> time=Optional.empty();
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Location> location = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    private List<org.openprovenance.prov.model.Role> role = new LinkedList<>();
    protected QualifiedName activity;
    protected Optional<QualifiedName> trigger= Optional.empty();
    protected Optional<QualifiedName> ender=Optional.empty();


    final ProvUtilities u=new ProvUtilities();



    private WasEndedBy() {}

    public WasEndedBy(QualifiedName id,
                      Collection<Attribute> attributes) {
        this.setId(id);
        time=Optional.empty();
        u.populateAttributes(attributes, location, type,role);
    }

    public WasEndedBy(QualifiedName id,
                      QualifiedName activity,
                      QualifiedName trigger,
                      QualifiedName ender,
                      XMLGregorianCalendar time,
                      Collection<Attribute> attributes) {
        this.setId(id);
        this.activity=activity;
        this.trigger=Optional.ofNullable(trigger);
        this.ender=Optional.ofNullable(ender);
        this.time=Optional.ofNullable(time);
        u.populateAttributes(attributes, location, type,role);
    }




    @Override
    public void setActivity(QualifiedName aid) {
        this.activity=aid;
    }

    @Override
    public void setTrigger(QualifiedName eid) {
        this.trigger=Optional.ofNullable(eid);
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getTrigger() {
        return trigger.orElse(null);
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity() {
        return activity;
    }


    @Override
    public void setEnder(QualifiedName aid) {
        this.ender=Optional.ofNullable(aid);
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getEnder() {
        return ender.orElse(null);
    }



    /**
     * Gets the value of the role property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore, any modification made to the
     * returned list will be present inside the object.
     * This is why there is not a <CODE>set</CODE> method for the role property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRole().add(newItem);
     * </pre>
     *
     * @return a list of objects of type
     * {@link Role }
     */
    @Override
    @JsonIgnore
    public List<org.openprovenance.prov.model.Role> getRole() {
        return role;
    }

    /**
     * Get time instant
     *
     * @return {@link XMLGregorianCalendar}
     */
    @Override
    public XMLGregorianCalendar getTime() {
        return time.orElse(null);
    }

    /**
     * Set time instant
     *
     * @param time
     */
    @Override
    public void setTime(XMLGregorianCalendar time) {
        this.time = Optional.ofNullable(time);
    }


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonProperty("@id")
    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }


    @JsonIgnore
    @Override
    public Kind getKind() {
        return Kind.PROV_END;
    }



    @JsonProperty("@id")
    @Override
    public void setId(QualifiedName value) {
        id = Optional.ofNullable(value);
    }



    @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.LangString> getLabel() {
        return labels;
    }

    @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.Location> getLocation() {
        return location;
    }

    @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.Type> getType() {
        return type;
    }


    @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.Other> getOther() {
        return other;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WasEndedBy)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WasEndedBy that = ((WasEndedBy) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getActivity(), that.getActivity());
        equalsBuilder.append(this.getTrigger(), that.getTrigger());
        equalsBuilder.append(this.getEnder(), that.getEnder());
        equalsBuilder.append(this.getTime(), that.getTime());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WasEndedBy)) {
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
        hashCodeBuilder.append(this.getActivity());
        hashCodeBuilder.append(this.getTrigger());
        hashCodeBuilder.append(this.getEnder());
        hashCodeBuilder.append(this.getTime());
        hashCodeBuilder.append(this.getIndexedAttributes());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {


        {
            QualifiedName theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }

        {
            QualifiedName theActivity;
            theActivity = this.getActivity();
            toStringBuilder.append("activity", theActivity);
        }

        {
            QualifiedName theTrigger;
            theTrigger = this.getTrigger();
            toStringBuilder.append("trigger", theTrigger);
        }

        {
            QualifiedName theStarter;
            theStarter = this.getTrigger();
            toStringBuilder.append("ender", theStarter);
        }


        {
            XMLGregorianCalendar theTime;
            theTime = this.getTime();
            toStringBuilder.append("time", theTime);
        }

       {
            Map<QualifiedName, Set<Attribute>> theAttributes;
            theAttributes = this.getIndexedAttributes();
            toStringBuilder.append("attributes", theAttributes);
        }



    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    @JsonIgnore
    @Override
    public Collection<Attribute> getAttributes() {
        LinkedList<Attribute> result=new LinkedList<>();
        result.addAll(getLabel().stream().map(s -> new Label(QUALIFIED_NAME_XSD_STRING,s)).collect(Collectors.toList()));
        result.addAll(getType());
        result.addAll(getLocation());
        result.addAll(getRole());
        result.addAll(getOther().stream().map(o -> (Attribute)o).collect(Collectors.toList())); //TODO: collect directly into result
        return result;
    }

    @JsonAnySetter
    @JsonDeserialize(keyUsing= CustomKeyDeserializer.class)
    public void setIndexedAttributes (Object qn, Set<Attribute> attributes) {
        List<Value> values=new LinkedList<>();
        u.distribute((QualifiedName)qn,attributes,getLabel(),values, getLocation(),getType(),getRole(), getOther());
    }


    @JsonAnyGetter
    @Override
    @JsonProperty("attributes")
    @JsonSerialize(keyUsing= CustomMapSerializer.class, contentUsing = CustomAttributesSerializer.class)
    public Map<QualifiedName, Set<Attribute>> getIndexedAttributes() {
        return u.split(getAttributes());
    }

}
