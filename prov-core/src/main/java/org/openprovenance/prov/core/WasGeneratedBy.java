package org.openprovenance.prov.core;

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

//@JsonPropertyOrder({ "@id", "entity", "activity", "time" })

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class WasGeneratedBy implements org.openprovenance.prov.model.WasGeneratedBy, Equals, HashCode, ToString, HasAttributes {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    private Optional<QualifiedName> id=Optional.empty();
    private Optional<XMLGregorianCalendar> time=Optional.empty();
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Location> location = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    private List<org.openprovenance.prov.model.Role> role = new LinkedList<>();
    protected QualifiedName activity;
    protected QualifiedName entity;


    final ProvUtilities u=new ProvUtilities();



    private WasGeneratedBy() {}

    public WasGeneratedBy(QualifiedName id,
                          Collection<Attribute> attributes) {
        this.setId(id);
        time=Optional.empty();
        u.populateAttributes(attributes, labels, location, type,role,other,null);
    }

    public WasGeneratedBy(QualifiedName id,
                          QualifiedName entity,
                          QualifiedName activity,
                          Collection<Attribute> attributes) {
        this.setId(id);
        this.activity=activity;
        this.entity=entity;
        time=Optional.empty();
        u.populateAttributes(attributes, labels, location, type,role,other,null);
    }

    public WasGeneratedBy(QualifiedName id,
                          QualifiedName entity,
                          QualifiedName activity,
                          XMLGregorianCalendar time,
                          Collection<Attribute> attributes) {
        this.setId(id);
        this.activity=activity;
        this.entity=entity;
        setTime(time);
        u.populateAttributes(attributes, labels, location, type,role,other,null);
    }


    @Override
    public void setActivity(QualifiedName aid) {
        this.activity=aid;
    }

    @Override
    public void setEntity(QualifiedName eid) {
        this.entity=eid;
    }

    @Override
 //   @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getEntity() {
        return entity;
    }

    @Override
  //  @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity() {
        return activity;
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
  //  @JsonIgnore
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


  //  @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
  //  @JsonProperty("@id")
    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }


  //  @JsonIgnore
    @Override
    public Kind getKind() {
        return Kind.PROV_GENERATION;
    }



   // @JsonProperty("@id")
    @Override
    public void setId(QualifiedName value) {
        id = Optional.ofNullable(value);
    }



 //   @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.LangString> getLabel() {
        return labels;
    }

 //   @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.Location> getLocation() {
        return location;
    }

 //   @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.Type> getType() {
        return type;
    }


  //  @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.Other> getOther() {
        return other;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WasGeneratedBy)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WasGeneratedBy that = ((WasGeneratedBy) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getActivity(), that.getActivity());
        equalsBuilder.append(this.getEntity(), that.getEntity());
        equalsBuilder.append(this.getTime(), that.getTime());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WasGeneratedBy)) {
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
        hashCodeBuilder.append(this.getEntity());
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
            QualifiedName theEntity;
            theEntity = this.getEntity();
            toStringBuilder.append("entity", theEntity);
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

 //   @JsonIgnore
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

 //   @JsonAnySetter
  //  @JsonDeserialize(keyUsing= CustomKeyDeserializer.class)
    public void setIndexedAttributes (Object qn, Set<Attribute> attributes) {
        List<Value> values=new LinkedList<>();
        u.distribute((QualifiedName)qn,attributes,getLabel(),values, getLocation(),getType(),getRole(), getOther());
    }


 //   @JsonAnyGetter
    @Override
 //   @JsonProperty("attributes")
  //  @JsonSerialize(keyUsing= CustomMapSerializer.class, contentUsing = CustomAttributesSerializer.class)
    public Map<QualifiedName, Set<Attribute>> getIndexedAttributes() {
        return u.split(getAttributes());
    }

}
