package org.openprovenance.prov.sql;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;


/**
 * Data structure containing associations between names and documents. 
 * Works in conjunction with PutableDocument, allowing the history of previous associations to be maintained.
 * 
 */
@Entity(name = "NamedDocument")
@NamedQuery(name="NamedDocument.Find", query="SELECT e FROM NamedDocument e WHERE e.name LIKE :name")


@javax.persistence.Cacheable 
@Table(name = "NAMEDDOCUMENT", uniqueConstraints=@javax.persistence.UniqueConstraint(columnNames={"NAME"}))
@Inheritance(strategy = InheritanceType.JOINED)
public class NamedDocument    
{

    public NamedDocument() {} 

    @XmlAttribute(name = "pk")
    protected Long pk;

   
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

    protected String name;
 
    @Basic
    @Column(name = "NAME")
    public String getName() {
	return name;
    } 
    
    
    public void setName(String name) {
        this.name=name;
    } 
    
    protected PutableDocument document;
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.PutableDocument.class, 
            cascade = {
                       CascadeType.ALL
    })
    @JoinColumn(name = "VALUE")
    public PutableDocument getDocument() {
        return document;
    }
    public void setDocument(PutableDocument document) {
        this.document=document;
    }
   
    public String toString() {
	return "<namedDocument " + name + ">";
    }

    	     
}
