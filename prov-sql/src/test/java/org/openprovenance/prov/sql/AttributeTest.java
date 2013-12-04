package org.openprovenance.prov.sql;

import java.io.File;
import java.util.Hashtable;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;

public class AttributeTest extends org.openprovenance.prov.xml.AttributeTest {
  

    
    final PersistenceUtility u = new PersistenceUtility();

    public AttributeTest(String testName) {
	super(testName);
	pFactory = new ProvFactory();

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

	@SuppressWarnings("unused")
	Document doc2 = u.persist((org.openprovenance.prov.sql.Document) doc);
	dbKeys.put(file, ((org.openprovenance.prov.sql.Document) doc).getHjid());
	System.out.println("saved document "
		+ ((org.openprovenance.prov.sql.Document) doc).getHjid()
		+ " for " + file);
    }
    
    @Override
    public boolean checkSchema(String name) {
            return false;
    }
    
    public org.openprovenance.prov.model.QualifiedName q(String n) {
		return new QualifiedName(EX_NS, n, EX_PREFIX);
       }

}
