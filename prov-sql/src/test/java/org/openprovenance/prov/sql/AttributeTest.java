package org.openprovenance.prov.sql;

import java.io.File;
import java.util.Hashtable;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.xml.ValueConverter;

public class AttributeTest extends org.openprovenance.prov.xml.AttributeTest {
  

    
    final PersistenceUtility u = new PersistenceUtility();

    public AttributeTest(String testName) {
	super(testName);
	pFactory = new ProvFactory(namespaces);
	vconv=new ValueConverter(pFactory);

	u.setUp();
    }

    @Override
    public String extension() {
	return ".xml";
    }

    @Override
    public org.openprovenance.prov.sql.Document readDocument(String file1) {
	Long key = dbKeys.get(file1);
	if (key == null)
	    return null;
	Document doc = u.find(org.openprovenance.prov.sql.Document.class, key);
	try {
	    ProvSerialiser.getThreadProvSerialiser()
			  .serialiseDocument(new File(file1 + "-persist"), doc,
					     true);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	System.out.println("read document " + ((org.openprovenance.prov.sql.Document)doc).getHjid() + " for " + file1);
	return (org.openprovenance.prov.sql.Document) doc;

    }

    static Hashtable<String, Long> dbKeys = new Hashtable<String, Long>();

    public void writeDocument(org.openprovenance.prov.model.Document doc,
			      String file) {
	Namespace.withThreadNamespace(doc.getNamespace());

	Document doc2 = u.persist((org.openprovenance.prov.sql.Document) doc);
	dbKeys.put(file, ((org.openprovenance.prov.sql.Document) doc).getHjid());
	System.out.println("saved document "
		+ ((org.openprovenance.prov.sql.Document) doc).getHjid()
		+ " for " + file);
    }
    
 

}
