package org.openprovenance.prov.sql;

import java.util.Date;

import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XMLGregorianCalendarAsDateTime;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XmlAdapterUtils;
import org.openprovenance.prov.model.Document;

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


@javax.persistence.Entity(name = "IncrementalDocument")
@Table(name = "IDOCUMENT")
@Inheritance(strategy = InheritanceType.JOINED)

public class IncrementalDocument {

	public IncrementalDocument() {
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


    private IncrementalDocument latest;
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.IncrementalDocument.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "LATEST")
    
    public IncrementalDocument getLatest() {
    	return latest;
    }
    public void setLatest(IncrementalDocument latest) {
    	this.latest=latest;
    }
    
    

 

    private IncrementalDocument previous;
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.IncrementalDocument.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "PREVIOUS")
    
    public IncrementalDocument getPrevious() {
    	return previous;
    }
    public void setPrevious(IncrementalDocument previous) {
    	this.previous=previous;
    }
    
  
    
    ////////
    
   
    
    private Document bindings;
    public void setBindings(Document bindings) {
    	this.bindings=bindings;
    }
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.Document.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "BINDINGS")
    public Document getBindings() {
    	return bindings;
    }
   
    ///////////


    
    
    private Document template;
    public void setTemplate(Document template) {
    	this.template=template;
    }
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.Document.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "TEMPLATE")
    public Document getTemplate() {
    	return template;
    }
    
    
    /////////////
    
    

    
    private Document logBinding;
    public void setLog(Document logBinding) {
    	this.logBinding=logBinding;
    }
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.Document.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "LOG")
    
    public Document getLog() {
    	return logBinding;
    }
    
    
    ///////////
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
        return XmlAdapterUtils.unmarshall(XMLGregorianCalendarAsDateTime.class, this.getDateTime());
    }

    public void setDateTimeItem(Date target) {
        setDateTime(XmlAdapterUtils.marshall(XMLGregorianCalendarAsDateTime.class, target));
    }


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
