package org.openprovenance.prov.sql;

import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity(name = "Namespace")
@Table(name = "NAMESPACE")

public class Namespace extends org.openprovenance.prov.model.Namespace {

	public Namespace() {
	}
	
	@Override
    @Basic
    public String getDefaultNamespace () {
    	return super.getDefaultNamespace();
    }
	
    @ElementCollection

	@MapKeyColumn(name="prefix")
    @Column(name="prefix_value")
    @CollectionTable(name="prefix_map", joinColumns=@JoinColumn(name="pk"))
	
	public Map<String, String> getPrefixes() {
		return super.getPrefixes();
	}
    
    protected Long pk;
    
    public void setPrefixes(Map<String,String> prefixes) {
    	this.prefixes=prefixes;
    }
    
    @ElementCollection
	@MapKeyColumn(name="namespaces")
    @Column(name="namespace_value")
    @CollectionTable(name="namespace_map", joinColumns=@JoinColumn(name="pk"))
	
    public Map<String, String> getNamespaces() {
    	return super.getNamespaces();
    }
    
    public void setNamespaces(Map<String,String> namespaces) {
    	this.namespaces=namespaces;
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
    //@GeneratedValue(strategy = GenerationType.TABLE)
    // see http://stackoverflow.com/questions/916169/cannot-use-identity-column-key-generation-with-union-subclass-table-per-clas
    public Long getPk() {
        return pk;
    }
    public void setPk(Long pk) {
    	this.pk=pk;
    }

    public Namespace(org.openprovenance.prov.model.Namespace other) {
    	super(other);
    }


    @Override
    public void setParent(org.openprovenance.prov.model.Namespace parent) {
	 super.setParent(parent);
    }
    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.Namespace.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "PARENT")
    public org.openprovenance.prov.model.Namespace getParent() {
    	return super.getParent();
    }
}
