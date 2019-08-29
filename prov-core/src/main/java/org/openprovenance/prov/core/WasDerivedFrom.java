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

@JsonPropertyOrder({ "@id", "generatedEntity", "usedEntity", "activity" })

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WasDerivedFrom implements org.openprovenance.prov.model.WasDerivedFrom, Equals, HashCode, ToString, HasAttributes {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    private Optional<QualifiedName> id=Optional.empty();
    final private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    final private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    final private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    protected QualifiedName generatedEntity;
    protected QualifiedName usedEntity;
    protected Optional<QualifiedName> activity=Optional.empty();
    protected Optional<QualifiedName> generation=Optional.empty();
    protected Optional<QualifiedName> usage=Optional.empty();


    final ProvUtilities u=new ProvUtilities();



    private WasDerivedFrom() {}

    public WasDerivedFrom(QualifiedName id,
                          Collection<Attribute> attributes) {
        this.setId(id);
        u.populateAttributes(attributes, labels, Collections.EMPTY_LIST, type, Collections.EMPTY_LIST, other,null);
    }

    public WasDerivedFrom(QualifiedName id,
                          QualifiedName generatedEntity,
                          QualifiedName usedEntity,
                          Collection<Attribute> attributes) {
        this.setId(id);
        this.usedEntity=usedEntity;
        this.generatedEntity=generatedEntity;
        u.populateAttributes(attributes, labels, Collections.EMPTY_LIST, type, Collections.EMPTY_LIST, other,null);
    }

    public WasDerivedFrom(QualifiedName id,
                          QualifiedName generatedEntity,
                          QualifiedName usedEntity,
                          QualifiedName activity,
                          Collection<Attribute> attributes) {
        this.setId(id);
        this.setActivity(activity);
        this.usedEntity=usedEntity;
        this.generatedEntity=generatedEntity;
        u.populateAttributes(attributes, labels, Collections.EMPTY_LIST, type,Collections.EMPTY_LIST,  other,null);
    }

    public WasDerivedFrom(QualifiedName id,
                          QualifiedName generatedEntity,
                          QualifiedName usedEntity,
                          QualifiedName activity,
                          QualifiedName generation,
                          QualifiedName usage,
                          Collection<Attribute> attributes) {
        this.setId(id);
        this.setActivity(activity);
        this.usedEntity=usedEntity;
        this.generatedEntity=generatedEntity;
        this.setGeneration(generation);
        this.setUsage(usage);
        u.populateAttributes(attributes, labels, Collections.EMPTY_LIST, type,Collections.EMPTY_LIST,  other,null);
    }



    @Override
    public void setActivity(QualifiedName aid) {
        this.activity = Optional.ofNullable(aid);
    }

    @Override
    public void setGeneration(QualifiedName gen) {
        this.generation = Optional.ofNullable(gen);
    }

    @Override
    public void setUsage(QualifiedName use) {
        this.usage = Optional.ofNullable(use);

    }

    @Override
    public void setUsedEntity(QualifiedName eid) {
        this.usedEntity=eid;
    }

    @Override
    public void setGeneratedEntity(QualifiedName eid) {
        this.generatedEntity=eid;
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getUsedEntity() {
        return usedEntity;
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getUsage() {
        return usage.orElse(null);
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getGeneratedEntity() {
        return generatedEntity;
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getGeneration() {
        return generation.orElse(null);
    }

    @Override
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity() {
        return activity.orElse(null);
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
        return Kind.PROV_DERIVATION;
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
    public List<org.openprovenance.prov.model.Type> getType() {
        return type;
    }


    @JsonIgnore
    @Override
    public List<org.openprovenance.prov.model.Other> getOther() {
        return other;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WasDerivedFrom)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WasDerivedFrom that = ((WasDerivedFrom) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getGeneratedEntity(), that.getGeneratedEntity());
        equalsBuilder.append(this.getUsedEntity(), that.getUsedEntity());
        equalsBuilder.append(this.getActivity(), that.getActivity());
        equalsBuilder.append(this.getUsage(), that.getUsage());
        equalsBuilder.append(this.getGeneration(), that.getGeneration());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WasDerivedFrom)) {
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
        hashCodeBuilder.append(this.getGeneratedEntity());
        hashCodeBuilder.append(this.getUsedEntity());
        hashCodeBuilder.append(this.getActivity());
        hashCodeBuilder.append(this.getGeneration());
        hashCodeBuilder.append(this.getUsage());
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
            QualifiedName theEntity;
            theEntity = this.getGeneratedEntity();
            toStringBuilder.append("generatedEntity", theEntity);
        }



        {
            QualifiedName theEntity;
            theEntity = this.getUsedEntity();
            toStringBuilder.append("usedEntity", theEntity);
        }

        {
            QualifiedName theActivity;
            theActivity = this.getActivity();
            toStringBuilder.append("activity", theActivity);
        }


        {
            QualifiedName theActivity;
            theActivity = this.getGeneration();
            toStringBuilder.append("generation", theActivity);
        }


        {
            QualifiedName theActivity;
            theActivity = this.getUsage();
            toStringBuilder.append("usage", theActivity);
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
        result.addAll(getOther().stream().map(o -> (Attribute)o).collect(Collectors.toList())); //TODO: collect directly into result
        return result;
    }

    @JsonAnySetter
    @JsonDeserialize(keyUsing= CustomKeyDeserializer.class)
    public void setIndexedAttributes (Object qn, Set<Attribute> attributes) {
        List<Value> values=new LinkedList<>();
        u.distribute((QualifiedName)qn,attributes,getLabel(),values, Collections.EMPTY_LIST,getType(),Collections.EMPTY_LIST, getOther());
    }


    @JsonAnyGetter
    @Override
    @JsonProperty("attributes")
    @JsonSerialize(keyUsing= CustomMapSerializer.class, contentUsing = CustomAttributesSerializer.class)
    public Map<QualifiedName, Set<Attribute>> getIndexedAttributes() {
        return u.split(getAttributes());
    }

}
