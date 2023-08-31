package org.openprovenance.prov.sql;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;
import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.StatementOrBundle;


/**
 * <p>Java class for Document complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Document"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;group ref="{http://www.w3.org/ns/prov#}documentElements"/&gt;
 *           &lt;element name="bundleContent" type="{http://www.w3.org/ns/prov#}NamedBundle"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.Document
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
        @XmlElement(name = "bundleContent", type = Bundle.class)
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
     * {@link Bundle }
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
            Namespace theNamespace=this.getNamespace();
            toStringBuilder.append("namespace", theNamespace);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }



    transient Namespace namespace;
    

    @ManyToOne(targetEntity = org.openprovenance.prov.sql.Namespace.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "NS")
    public Namespace getNamespace() {
        return namespace;
    }
    
    public void setNamespace(Namespace ns) {
        namespace=ns;       
    }
    
    
    public enum Kind {
        DOCUMENT,  //0
        TEMPLATE,  //1
        BINDINGS,  //2
        LOG        //3
    }
    
    
    @Enumerated(EnumType.ORDINAL)
    transient private Kind kind;
    
    public Kind getKind() {
        return kind;
    }
    
    public void setKind(Kind kind) {
        this.kind=kind;
    }
    
    public Document () {
        kind=Kind.DOCUMENT; // default value
    }


}
