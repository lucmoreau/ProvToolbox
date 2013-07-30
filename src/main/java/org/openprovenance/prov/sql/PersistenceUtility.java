package org.openprovenance.prov.sql;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.openprovenance.prov.xml.Document;

public class PersistenceUtility {
    static Logger logger = Logger.getLogger(PersistenceUtility.class);
    final private EntityManagerFactory emf;                                                                                                                   

    public PersistenceUtility() {
        Map<String,String> properties=makeProperties(getUnitName());
        
        this.emf=javax.persistence.Persistence.createEntityManagerFactory(getUnitName(),                                                                                                 
                                                                          properties);  
        this.entityManager=createEntityManager();          


    }
             
    String getUnitName() {
        return "org.openprovenance.prov.sql";
    }

    final private EntityManager entityManager;      
    
    public EntityManager createEntityManager () {                                                                                                                                        
        EntityManager em=emf.createEntityManager();                                                                                                                                      
        return em;                                                                                                                                                                       
    }                                                                                                                                                                                    
                                                                                                                                                                                         
    static Map<String,String> makeProperties(String filename) {                                                                                                                          
        Map<String,String> properties=new HashMap<String, String>();                                                                                                                     
        properties.put("hibernate.ejb.cfgfile",filename);                                                                                                                                
        properties.put("javax.persistence.provider","org.hibernate.ejb.HibernatePersistence");                                                                                           
        logger.debug(properties);                                                                                                                                                        
        return properties;                                                                                                                                                               
    }                                                                                                                                                                                    
   
    
    public EntityTransaction getTransaction() {                                             
        return entityManager.getTransaction();                                              
    }
                                                                           
    
    public EntityTransaction beginTransaction() {                                                                                                                                        
        EntityTransaction tx=entityManager.getTransaction();                                                                                                                             
        tx.begin();                                                                                                                                                                      
        return tx;                                                                                                                                                                       
    }                                                                                                                                                                                    
             
    public void close() {                                                                                                                                                                
        entityManager.close();                                                                                                                                                           
    }          
    

    public Document persist(Document doc) {
        beginTransaction();
        entityManager.persist(doc);
        close();
        return doc;
    }            

}
