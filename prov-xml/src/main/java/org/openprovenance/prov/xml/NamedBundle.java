package org.openprovenance.prov.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;


/**
 * <p>Java class for NamedBundle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NamedBundle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;group ref="{http://www.w3.org/ns/prov#}documentElements"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NamedBundle", propOrder = {
    "statement"
})
public class NamedBundle
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.NamedBundle
{

    @XmlElements({
        @XmlElement(name = "entity", type = Entity.class),
        @XmlElement(name = "activity", type = Activity.class),
        @XmlElement(name = "wasGeneratedBy", type = WasGeneratedBy.class),
        @XmlElement(name = "used", type = Used.class),
        @XmlElement(name = "wasInformedBy", type = WasInformedBy.class),
        @XmlElement(name = "wasStartedBy", type = WasStartedBy.class),
        @XmlElement(name = "wasEndedBy", type = WasEndedBy.class),
        @XmlElement(name = "wasInvalidatedBy", type = WasInvalidatedBy.class),
        @XmlElement(name = "wasDerivedFrom", type = WasDerivedFrom.class),
        @XmlElement(name = "agent", type = Agent.class),
        @XmlElement(name = "wasAttributedTo", type = WasAttributedTo.class),
        @XmlElement(name = "wasAssociatedWith", type = WasAssociatedWith.class),
        @XmlElement(name = "actedOnBehalfOf", type = ActedOnBehalfOf.class),
        @XmlElement(name = "wasInfluencedBy", type = WasInfluencedBy.class),
        @XmlElement(name = "specializationOf", type = SpecializationOf.class),
        @XmlElement(name = "alternateOf", type = AlternateOf.class),
        @XmlElement(name = "hadMember", type = HadMember.class),
        @XmlElement(name = "mentionOf", type = MentionOf.class),
        @XmlElement(name = "plan", type = Plan.class),
        @XmlElement(name = "wasRevisionOf", type = Revision.class),
        @XmlElement(name = "wasQuotedFrom", type = Quotation.class),
        @XmlElement(name = "hadPrimarySource", type = PrimarySource.class),
        @XmlElement(name = "person", type = Person.class),
        @XmlElement(name = "organization", type = Organization.class),
        @XmlElement(name = "softwareAgent", type = SoftwareAgent.class),
        @XmlElement(name = "bundle", type = Bundle.class),
        @XmlElement(name = "collection", type = Collection.class),
        @XmlElement(name = "emptyCollection", type = EmptyCollection.class),
        @XmlElement(name = "dictionary", type = Dictionary.class),
        @XmlElement(name = "emptyDictionary", type = EmptyDictionary.class),
        @XmlElement(name = "hadDictionaryMember", type = DictionaryMembership.class),
        @XmlElement(name = "derivedByInsertionFrom", type = DerivedByInsertionFrom.class),
        @XmlElement(name = "derivedByRemovalFrom", type = DerivedByRemovalFrom.class),
        @XmlElement(name = "others", type = Others.class)
    })
    protected List<Statement> statement;

    @XmlAttribute(name = "id", namespace = "http://www.w3.org/ns/prov#")
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(QualifiedNameAdapter.class)
    protected org.openprovenance.prov.model.QualifiedName id;


    /**
     * Gets the value of the statement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Entity }
     * {@link Activity }
     * {@link WasGeneratedBy }
     * {@link Used }
     * {@link WasInformedBy }
     * {@link WasStartedBy }
     * {@link WasEndedBy }
     * {@link WasInvalidatedBy }
     * {@link WasDerivedFrom }
     * {@link Agent }
     * {@link WasAttributedTo }
     * {@link WasAssociatedWith }
     * {@link ActedOnBehalfOf }
     * {@link WasInfluencedBy }
     * {@link SpecializationOf }
     * {@link AlternateOf }
     * {@link HadMember }
     * {@link MentionOf }
     * {@link Plan }
     * {@link Revision }
     * {@link Quotation }
     * {@link PrimarySource }
     * {@link Person }
     * {@link Organization }
     * {@link SoftwareAgent }
     * {@link Bundle }
     * {@link Collection }
     * {@link EmptyCollection }
     * {@link Dictionary }
     * {@link EmptyDictionary }
     * {@link DictionaryMembership }
     * {@link DerivedByInsertionFrom }
     * {@link DerivedByRemovalFrom }
     * {@link Others }
     * 
     * 
     */
    public List<Statement> getStatement() {
        if (statement == null) {
            statement = new ArrayList<Statement>();
        }
        return this.statement;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link org.openprovenance.prov.model.QualifiedName }
     *     
     */
    public org.openprovenance.prov.model.QualifiedName getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openprovenance.prov.model.QualifiedName }
     *     
     */
    public void setId(org.openprovenance.prov.model.QualifiedName value) {
        this.id = value;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof NamedBundle)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final NamedBundle that = ((NamedBundle) object);
        equalsBuilder.append(this.getStatement(), that.getStatement());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof NamedBundle)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getStatement());
        hashCodeBuilder.append(this.getId());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<Statement> theStatement;
            theStatement = this.getStatement();
            toStringBuilder.append("statement", theStatement);
        }
        {
            org.openprovenance.prov.model.QualifiedName theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }
    }

    
    @javax.xml.bind.annotation.XmlTransient 
    private Namespace namespace=null; 

    @Override
    public Namespace getNamespace() { 
	return namespace;
    } 
    
    @Override
    public void setNamespace(Namespace namespace) { 
	this.namespace=namespace; 
    }
   
    
    public String toString() {
	final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
	toString(toStringBuilder);
	return toStringBuilder.toString();
    }
    

   public Kind getKind() {
  	return org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE;
   }


}
