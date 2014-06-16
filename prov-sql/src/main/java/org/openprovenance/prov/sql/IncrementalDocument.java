package org.openprovenance.prov.sql;

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


@javax.persistence.Entity(name = "IDocument")
@Table(name = "IDOCUMENT")
@Inheritance(strategy = InheritanceType.JOINED)

public class IncrementalDocument {

	public IncrementalDocument() {
		// TODO Auto-generated constructor stub
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
    public void setPrevious(IncrementalDocument latest) {
    	this.previous=previous;
    }
    
    

}
