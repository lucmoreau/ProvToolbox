package org.openprovenance.prov.sql;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.openprovenance.prov.xml.Document;

public class PersistenceUtility {
    static Logger logger = Logger.getLogger(PersistenceUtility.class);
    final private EntityManagerFactory emf;                                                                                                                   

    public PersistenceUtility() {
       
        this.emf=createEntityManagerFactory();  
        this.entityManager=createEntityManager();          


    }


    protected EntityManagerFactory createEntityManagerFactory() {
         
             try {
               final Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/persistence.xml");
               while (resources.hasMoreElements()) {
                 final URL resource = resources.nextElement();
                 logger.debug("Detected [" + resource + "].");
               }
         
             }
             catch (IOException ignored) {
         
             }
         
             final Properties properties = getEntityManagerFactoryProperties();
         
             if (properties == null) {
               return javax.persistence.Persistence.createEntityManagerFactory(getPersistenceUnitName());
             }
             else {
               return javax.persistence.Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties);
             }
           }
         
    

    public Properties getEntityManagerFactoryProperties() {

        try {
            final Enumeration<URL> resources = getClass().getClassLoader().getResources(getEntityManagerFactoryPropertiesResourceName());
            
            if (!resources.hasMoreElements()) {
                logger.debug("Entity manager factory properties are not set.");
                return null;
                
            }
            else {
                logger.debug("Loading entity manager factory properties.");
                final Properties properties = new Properties();
                while (resources.hasMoreElements()) {
                    final URL resource = resources.nextElement();
                    logger.debug("Loading entity manager factory properties from [" + resource + "].");
                    
                    if (resource == null) {
                        return null;
                    }
                    else {
                        InputStream is = null;
                        try {
                            is = resource.openStream();
                            properties.load(is);
                            return properties;
                        }
                        catch (IOException ex) {
                            return null;
                        }
                        finally {
                            if (is != null) {
                                try {
                                    is.close();
                                }
                                catch (IOException ex) {
                                    // Ignore
                                }
                            }
                        }
                    }
                }
                return properties;
            }
        }
        catch (IOException ex) {
            return null;
        }
    }


    public String getEntityManagerFactoryPropertiesResourceName() {
        return "persistence.properties";
    }

             
    String getPersistenceUnitName() {
        return "org.openprovenance.prov.sql";
    }

    final private EntityManager entityManager;      
    
    public EntityManager createEntityManager () {                                                                                                                                        
        EntityManager em=emf.createEntityManager();                                                                                                                                      
        return em;                                                                                                                                                                       
    }                                                                                                                                                                                    
          
    /*
    static Map<String,String> makeProperties(String filename) {                                                                                                                          
        Map<String,String> properties=new HashMap<String, String>();                                                                                                                     
        properties.put("hibernate.ejb.cfgfile",filename);                                                                                                                                
        properties.put("javax.persistence.provider","org.hibernate.ejb.HibernatePersistence");                                                                                           
        logger.debug(properties);                                                                                                                                                        
        return properties;                                                                                                                                                               
    }                                                                                                                                                                                    
   */
    
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
