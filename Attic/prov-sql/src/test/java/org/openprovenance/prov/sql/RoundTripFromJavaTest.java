package org.openprovenance.prov.sql;

import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;
import javax.xml.bind.JAXBException;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.xml.ProvUtilities;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.xml.UncheckedTestException;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class RoundTripFromJavaTest extends org.openprovenance.prov.xml.RoundTripFromJavaTest {

    public static final String EX_NS = "http://example.org/";
    public static final String EX2_NS = "http://example2.org/";
    public static final String EX_PREFIX = "ex";
    public static final String EX2_PREFIX = "ex2";
    public static final String EX3_NS = "http://example3.org/";

    static final ProvUtilities util=new ProvUtilities();



    static  void setNamespaces() {
    }

    static {
	pFactory = new ProvFactory();
	name=pFactory.getName();
    }
	private DocumentEquality documentEquality;

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public RoundTripFromJavaTest(String testName) {
	super(testName);
	this.documentEquality = new DocumentEquality(mergeDuplicateProperties());
    }

    public boolean urlFlag = true;

    /**
     * @return the suite of tests being tested
     */

    public String extension() {
	return ".xml";
    }
    

    public org.openprovenance.prov.model.QualifiedName q(String n) {
        return new QualifiedName(EX_NS, n, EX_PREFIX);
    }

    public void testEntity0 () {
	System.out.println("Starting test Entity0");
	super.testEntity0();
	System.out.println("Ending test Entity0");
    }
    
    public void testDictionaryInsertion1() {}
    public void testDictionaryInsertion2() {}
    public void testDictionaryInsertion3() {}
    public void testDictionaryInsertion4() {}
    public void testDictionaryInsertion5() {}
    public void testDictionaryInsertion6() {}
    public void testDictionaryInsertion7() {}
    public void testDictionaryRemoval1() {}
    public void testDictionaryRemoval2() {}
    public void testDictionaryRemoval3() {}
    public void testDictionaryRemoval4() {}
    public void testDictionaryRemoval5() {}
    public void testDictionaryMembership2() {}
    public void testDictionaryMembership3() {}
    public void testDictionaryMembership4() {}

    public void makeDocAndTest(Statement []stment, Bundle[] bundles, String file, Statement[] opt, boolean check) {
	Document doc = pFactory.newDocument();
	for (int i=0; i< stment.length; i++) {
	   doc.getStatementOrBundle().add(stment[i]);
	}
	if (bundles!=null) {
	    for (int j=0; j<bundles.length; j++) {
	        doc.getStatementOrBundle().add(bundles[j]);
	    }
	}
	updateNamespaces(doc);
	
	String file1=(opt==null) ? file : file+"-S";
	compareDocAndFile(doc, file1, check);
	
	if (opt!=null) {
	    String file2=file+"-M";
            doc.getStatementOrBundle().addAll(Arrays.asList(opt));
	    compareDocAndFile(doc, file2, check);
	}
    }

    public void compareDocAndFile(Document doc, String file, boolean check) {
        file=file+extension();
        writeDocument(doc, file);
        Document doc3=readDocument(file);
        compareDocuments(doc, doc3, check && checkTest(file));
    }

    public Document readDocument(String file1) {
        try {
            return readXMLDocument(file1);
        } catch (JAXBException e) {
            throw new UncheckedTestException(e);
        }
    }

    public void writeDocument(Document doc, String file2) {
        try {
            writeXMLDocument(doc, file2);
        } catch (JAXBException e) {
            throw new UncheckedTestException(e);
        }
    }
   

    public void compareDocuments(Document doc, Document doc2, boolean check) {
	assertTrue("self doc equality", doc.equals(doc));
	assertTrue("self doc2 equality", doc2.equals(doc2));
	if (check) {
	    boolean result=this.documentEquality.check(doc, doc2);
	    if (!result) {
	    System.out.println("Pre-write graph: "+doc);
		System.out.println("Read graph: "+doc2);
	    }
	    assertTrue("doc equals doc2", result);
	} else {
	    assertFalse("doc distinct from doc2", doc.equals(doc2));
	}
    }
    
    public boolean checkTest(String name) {
	// all tests successful in this file
	return true;
    }

    public boolean mergeDuplicateProperties() {
    	return false;
    }
    
    public Document readXMLDocument(String file) throws javax.xml.bind.JAXBException {
        
	ProvDeserialiser deserial = ProvDeserialiser
	        .getThreadProvDeserialiser();
	Document c = deserial.deserialiseDocument(new File(file));
	return c;
	
    }
    
    public void writeXMLDocument(Document doc, String file) throws JAXBException {
        
	ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();
	Namespace.withThreadNamespace(doc.getNamespace());
	serial.serialiseDocument(new File(file), (org.openprovenance.prov.sql.Document)doc, true);
	StringWriter sw = new StringWriter();
	serial.serialiseDocument(sw, (org.openprovenance.prov.sql.Document)doc, true);
	//System.out.println(sw.toString());
	 
	 
    }
    @Override
    public boolean checkSchema(String name) {
	return false;
    }
    ///////////////////////////////////////////////////////////////////////

    @Override
    public void updateNamespaces(org.openprovenance.prov.model.Document doc) {
	Namespace ns=Namespace.gatherNamespaces(doc);
	Namespace ns2=new org.openprovenance.prov.sql.Namespace(ns);
	doc.setNamespace(ns2);
    }


}
