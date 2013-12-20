package org.openprovenance.prov.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
import org.openprovenance.prov.model.StatementOrBundle;


/**
 * <p>Java class for Document complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;group ref="{http://www.w3.org/ns/prov#}documentElements"/>
 *           &lt;element name="bundleContent" type="{http://www.w3.org/ns/prov#}NamedBundle"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
    "statementOrBundle"
})
public class Document
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.Document
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
        @XmlElement(name = "others", type = Others.class),
        @XmlElement(name = "bundleContent", type = NamedBundle.class)
    })
    protected List<StatementOrBundle> statementOrBundle;

    /**
     * Gets the value of the statementOrBundle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statementOrBundle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatementOrBundle().add(newItem);
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
     * {@link NamedBundle }
     * 
     * 
     */
    public List<StatementOrBundle> getStatementOrBundle() {
        if (statementOrBundle == null) {
            statementOrBundle = new ArrayList<StatementOrBundle>();
        }
        return this.statementOrBundle;
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
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getStatementOrBundle());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<StatementOrBundle> theStatementOrBundle;
            theStatementOrBundle = this.getStatementOrBundle();
            toStringBuilder.append("statementOrBundle", theStatementOrBundle);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }



   transient Namespace namespace;
   @Override
   public void setNamespace(Namespace ns) {
       namespace=ns;       
   }
   
   @Override
   public Namespace getNamespace() {
       return namespace;
   }

}
