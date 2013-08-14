package org.openprovenance.prov.sql;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.MapKeyColumn;
import javax.persistence.JoinColumn;
import javax.persistence.CollectionTable;
import javax.persistence.Transient;
import javax.xml.namespace.QName;

//@Entity(name="IdentifierManagement")
//@Table(name = "IdentifierManagement")

public class IdentifierManagement {
    /*
    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @ElementCollection
    @MapKeyColumn(name="uri")
 //   @Column(name="value")
    @CollectionTable(name="IdentifierManagementEntries", joinColumns=@JoinColumn(name="HJID"))
    
    private Map<String, EntityRef> identifiers=new HashMap<String, EntityRef>();
    
    public Map<String, EntityRef> getIdentifiers() {
        return identifiers;
    }
    
    @Transient
    private Map<String, QName> qnames=new HashMap<String, QName>();
    
    public void add(String key, QName q, EntityRef newref) {
	EntityRef old=identifiers.get(key);
	if (old!=null) {
	       System.out.println("*---- seeing again " + key + " (id=" + old.getHjid() + ")");

	       //newref.setHjid(old.getHjid());
	    //System.out.println("*** reusing " + old.getHjid());
	} else {
	    getIdentifiers().put(key, newref);
	    qnames.put(key, q);
	    System.out.println("*** storing " + key+ " (id=" + newref.getHjid() + ")");
	}
    }
    
    public void add(QName q,EntityRef newref) {
	String uri=q.getNamespaceURI()+q.getLocalPart();
	add(uri,q, newref);
    }
    
    static IdentifierManagement it;
    
    static {
        System.out.println("$$$$$$$$$$  creating IdentifierManagement instance");
	it=new IdentifierManagement();
    }
    */


}
