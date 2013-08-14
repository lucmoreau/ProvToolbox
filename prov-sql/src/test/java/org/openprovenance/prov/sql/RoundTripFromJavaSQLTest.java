package org.openprovenance.prov.sql;

import java.io.File;
import java.util.Hashtable;
import org.openprovenance.prov.sql.Document;

//import org.openprovenance.prov.sql.PersistenceUtility;

public class RoundTripFromJavaSQLTest extends RoundTripFromJavaTest {
    final PersistenceUtility u=new PersistenceUtility();

    public RoundTripFromJavaSQLTest(String name) {
	super(name);
	u.setUp();
    }
    
    
    public boolean checkTest(String name) {
	return true;
    }
    
    @Override
    public void setUp () {
	//u.setUp();
    }
    
    @Override
    public Document readDocument(String file1) {
	Long key=dbKeys.get(file1);
	if (key==null) return null;
        Document doc=u.find(Document.class, key);
        try {
            ProvSerialiser.getThreadProvSerialiser().serialiseDocument(new File(file1+"-persist"), doc, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
	System.out.println("read document " + doc.getHjid() + " for " + file1);
	return doc;

    }
    
    static Hashtable<String, Long> dbKeys=new Hashtable<String, Long>();

    @Override
    public void writeDocument(org.openprovenance.prov.model.Document doc, String file) {
	Document doc2=u.persist((org.openprovenance.prov.sql.Document)doc);
	dbKeys.put(file, ((org.openprovenance.prov.sql.Document)doc).getHjid());
	System.out.println("saved document " + ((org.openprovenance.prov.sql.Document)doc).getHjid() + " for " + file);
    }
	
    
    /*

    public void NOmakeDocAndTest(Statement [] statements, String file, Statement [] opt, boolean check) {
	Document doc = pFactory.newDocument();
	for (int i=0; i< statements.length; i++) {
	    doc.getEntityAndActivityAndWasGeneratedBy().add(statements[i]);
	}
	updateNamespaces(doc);
	
	check= check && checkTest(file);
	
	
	String file1=(opt==null) ? file : file+"-S";
	file1=file1+extension();
	
	String s=u.convertBeanToASN(doc);
	u.writeTextToFile(s, file1);
	
	Document doc2;
	try {
	    doc2 = (Document) u.convertASNToJavaBean(file1);
	    compareDocuments(doc, doc2, check && checkTest(file1));
	    
	    if (opt!=null) {
		doc.getEntityAndActivityAndWasGeneratedBy().addAll(Arrays.asList(opt));
		String file2=file+"-M";
		file2=file2+extension();
		
		String s2=u.convertBeanToASN(doc);
		u.writeTextToFile(s2, file2);
		
		Document doc3=(Document) u.convertASNToJavaBean(file2);
		compareDocuments(doc, doc3, check && checkTest(file2));
	    }

	    return;
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Throwable e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	assertTrue(false);
	

    }
    

    */

 }
