package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
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
@javax.persistence.Entity(name = "Document")
@Table(name = "DOCUMENT")
@Inheritance(strategy = InheritanceType.JOINED)
public class Document
    implements Equals, HashCode, org.openprovenance.prov.model.Document
{

    @XmlElements({
        @XmlElement(name = "entity", type = org.openprovenance.prov.sql.Entity.class),
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
        @XmlElement(name = "collection", type = Collection.class),
        @XmlElement(name = "emptyCollection", type = EmptyCollection.class),
        @XmlElement(name = "hadMember", type = HadMember.class),
        @XmlElement(name = "mentionOf", type = MentionOf.class),
        @XmlElement(name = "bundleContent", type = NamedBundle.class)
    })
    protected List<StatementOrBundle> statementOrBundle;
    @XmlAttribute(name = "pk")
    protected Long pk;

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
     * {@link org.openprovenance.prov.sql.Entity }
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
     * {@link Collection }
     * {@link EmptyCollection }
     * {@link HadMember }
     * {@link MentionOf }
     * {@link NamedBundle }
     * 
     * 
     */
    @ManyToMany(targetEntity = AStatement.class, cascade = {
        CascadeType.ALL
    })
    @JoinTable(name = "DOCUMENT_STATEMENT_JOIN", joinColumns = {
        @JoinColumn(name = "DOCUMENT")
    }, inverseJoinColumns = {
        @JoinColumn(name = "STATEMENT")
    })
    public List<StatementOrBundle> getStatementOrBundle() {
        if (statementOrBundle == null) {
            statementOrBundle = new ArrayList<StatementOrBundle>();
        }
        return this.statementOrBundle;
    }

    /**
     * 
     * 
     */
    public void setStatementOrBundle(List<StatementOrBundle> statementOrBundle) {
        this.statementOrBundle = statementOrBundle;
    }

    /**
     * Gets the value of the pk property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Id
    @Column(name = "PK")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getPk() {
        return pk;
    }

    /**
     * Sets the value of the pk property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPk(Long value) {
        this.pk = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Document)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Document that = ((Document) object);
        {
            List<StatementOrBundle> lhsStatementOrBundle;
            lhsStatementOrBundle = (((this.statementOrBundle!= null)&&(!this.statementOrBundle.isEmpty()))?this.getStatementOrBundle():null);
            List<StatementOrBundle> rhsStatementOrBundle;
            rhsStatementOrBundle = (((that.statementOrBundle!= null)&&(!that.statementOrBundle.isEmpty()))?that.getStatementOrBundle():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "statementOrBundle", lhsStatementOrBundle), LocatorUtils.property(thatLocator, "statementOrBundle", rhsStatementOrBundle), lhsStatementOrBundle, rhsStatementOrBundle)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

   /* added in pom.xml */@javax.xml.bind.annotation.XmlTransient private java.util.Hashtable<String,String> nss=null; public java.util.Hashtable<String,String> getNss() { return nss;} public void setNss(java.util.Hashtable<String,String> s) { nss=s; };public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<StatementOrBundle> theStatementOrBundle;
            theStatementOrBundle = (((this.statementOrBundle!= null)&&(!this.statementOrBundle.isEmpty()))?this.getStatementOrBundle():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "statementOrBundle", theStatementOrBundle), currentHashCode, theStatementOrBundle);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
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
    
    @Transient
    public void setNamespace(Namespace ns) {
        namespace=ns;       
    }
    @Transient
    public Namespace getNamespace() {
        return namespace;
    }

}
