package org.openprovenance.prov.sql;

import java.io.File;
import java.util.Hashtable;

import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.sql.Document;

//import org.openprovenance.prov.sql.PersistenceUtility;

public class RoundTripFromJavaSQLTest extends RoundTripFromJavaTest {
    final PersistenceUtility u = new PersistenceUtility();

    public RoundTripFromJavaSQLTest(String name) {
	super(name);
	u.setUp();
    }

    public boolean checkTest(String name) {
	return true;
    }

    @Override
    public void setUp() {
	// u.setUp();
    }

    @Override
    public Document readDocument(String file1) {
	Long key = dbKeys.get(file1);
	if (key == null)
	    return null;
	Document doc = u.find(Document.class, key);
	try {
	    Namespace.withThreadNamespace(doc.getNamespace());
	    ProvSerialiser.getThreadProvSerialiser()
			  .serialiseDocument(new File(file1 + "-persist"), doc,
					     true);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	System.out.println("read document " + doc.getPk() + " for " + file1);
	return doc;

    }

    static Hashtable<String, Long> dbKeys = new Hashtable<String, Long>();

    @Override
    public void writeDocument(org.openprovenance.prov.model.Document doc,
			      String file) {
    	@SuppressWarnings("unused")
	Document doc2 = u.persist((org.openprovenance.prov.sql.Document) doc);
    	dbKeys.put(file, ((org.openprovenance.prov.sql.Document) doc).getPk());
    	System.out.println("saved document "
    			+ ((org.openprovenance.prov.sql.Document) doc).getPk()
    			+ " for " + file);
    }

    public boolean doOptional(Statement[] opt) {
  	return false;
      }

   
    

    public org.openprovenance.prov.model.QualifiedName q(String n) {
		return new QualifiedName(EX_NS, n, EX_PREFIX);
       }


}
