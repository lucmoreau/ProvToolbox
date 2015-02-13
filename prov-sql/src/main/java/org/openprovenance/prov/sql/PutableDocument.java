package org.openprovenance.prov.sql;

import java.util.Date;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvUtilities;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * PutableDocument allows the history of previous values for a document name to be maintained.
 */

@javax.persistence.Entity(name = "PDocument")
@Table(name = "PDOCUMENT")
@Inheritance(strategy = InheritanceType.JOINED)

public class PutableDocument {

    public PutableDocument() {
    }
    
    Long pk;
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
    
    /*
    
    private PutableDocument latest;
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.PutableDocument.class, cascade = {
                                                                                            CascadeType.ALL
    })
    @JoinColumn(name = "LATEST")
    
    public PutableDocument getLatest() {
    	return latest;
    }
    public void setLatest(PutableDocument latest) {
    	this.latest=latest;
    }
    
    
*/
 

    private PutableDocument previous;
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.PutableDocument.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "PREVIOUS")
    
    public PutableDocument getPrevious() {
    	return previous;
    }
    public void setPrevious(PutableDocument previous) {
    	this.previous=previous;
    }
    
  

    
    
    private Document document;
    public void setDocument(Document template) {
    	this.document=template;
    }
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.Document.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "DOCUMENT")
    public Document getDocument() {
    	return document;
    }
    
    
    protected XMLGregorianCalendar dateTime;


    /**
     * Gets the value of the dateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Transient
    public XMLGregorianCalendar getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of the dateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateTime(XMLGregorianCalendar value) {
        this.dateTime = value;
    }
    @Basic
    @Column(name = "DATETIMEITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateTimeItem() {
        return ProvUtilities.toDate(this.getDateTime());

    }

    public void setDateTimeItem(Date target) {
        setDateTime(ProvUtilities.toXMLGregorianCalendar(target));
    }
    /*
    public Date getDateTimeItem() {
        return XmlAdapterUtils.unmarshall(XMLGregorianCalendarAsDateTime.class, this.getDateTime());
    }

    public void setDateTimeItem(Date target) {
        setDateTime(XmlAdapterUtils.marshall(XMLGregorianCalendarAsDateTime.class, target));
    }*/


    // Same provenance information as IncrementalDocument, obtained by template expansion. 
    private IncrementalDocument provenance;
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.IncrementalDocument.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "PROVENANCE")
    
    public IncrementalDocument getProvenance() {
    	return provenance;
    }
    public void setProvenance(IncrementalDocument provenance) {
    	this.provenance=provenance;
    }
 
    
}
