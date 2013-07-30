package org.openprovenance.prov.sql;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.sql.PersistenceUtility;

public class RoundTripFromJavaTest extends org.openprovenance.prov.xml.RoundTripFromJavaTest {
    final PersistenceUtility u=new PersistenceUtility();

    public RoundTripFromJavaTest(String name) {
	super(name);
    }
    
    
    public boolean checkTest(String name) {
	return true;
    }
    
    @Override
    public Document readDocument(String file1) {

        return null;
    }

    @Override
    public void writeDocument(Document doc, String file) {
	Document doc2=u.persist(doc);
	//u.writeTextToFile(s, file);
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
