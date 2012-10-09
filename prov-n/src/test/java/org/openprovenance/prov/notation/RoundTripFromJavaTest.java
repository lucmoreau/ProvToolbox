package org.openprovenance.prov.notation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.Statement;


public class RoundTripFromJavaTest extends org.openprovenance.prov.xml.RoundTripFromJavaTest {

    public RoundTripFromJavaTest(String name) {
	super(name);
    }
    
    public String extension() {
	return ".provn";
    }

    
    public void makeDocAndTest(Statement statement, String file)  {
	makeDocAndTest(statement, file, true);
    }
    
    public boolean checkTest(String name) {
	return !(name.equals("target/member2") 
		|| name.equals("target/member3"));
    }
    
    public void makeDocAndTest(Statement statement, String file, boolean check) {
	Document doc = pFactory.newDocument();
	doc.getEntityOrActivityOrWasGeneratedBy().add(statement);
	updateNamespaces(doc);
	Utility u=new Utility();
	
	check= check && checkTest(file);
	
	String s=u.convertBeanToASN(doc);
	file=file+extension();
	
	writeTextToFile(s, file);
	
	Document doc2;
	try {
	    doc2 = (Document) u.convertASNToJavaBean(file);
	    compareDocuments(doc, doc2, check);
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
    

    public void writeTextToFile(String text,
                                String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(text);
        }
        catch (IOException e) {
        }
        finally {
            try {
                if (writer != null)
                    writer.close( );
            }
            catch (IOException e) {}
        }
    }
    

 }
