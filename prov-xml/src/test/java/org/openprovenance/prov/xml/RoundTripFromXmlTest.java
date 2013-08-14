package org.openprovenance.prov.xml;

import java.io.File;


import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.net.URI;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.Document;

import junit.framework.TestCase;
/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class RoundTripFromXmlTest extends TestCase {

    public static final String EX_NS = "http://example.org/";
    public static final String EX2_NS = "http://example2.org/";
    public static final String EX_PREFIX = "ex";
    public static final String EX2_PREFIX = "ex2";
    public static final String EX3_NS = "http://example3.org/";

    static final ProvUtilities util=new ProvUtilities();


    static final Hashtable<String, String> namespaces;

    public static ProvFactory pFactory;
    public static ValueConverter vconv;

    static Hashtable<String, String> updateNamespaces (Hashtable<String, String> nss) {
        nss.put(EX_PREFIX, EX_NS);
        nss.put(EX2_PREFIX, EX2_NS);
        nss.put("_", EX3_NS);
        nss.put("dct","http://purl.org/dc/terms/");
        nss.put("foaf","http://xmlns.com/foaf/0.1/");
        
	return nss;
    }
    static  void setNamespaces() {
	pFactory.resetNamespaces();
	pFactory.getNss().putAll(updateNamespaces(new Hashtable<String, String>()));
    }

    static {
	namespaces = updateNamespaces(new Hashtable<String, String>());
	pFactory = new ProvFactory(namespaces);
	vconv=new ValueConverter(pFactory);
    }
	private DocumentEquality documentEquality;

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public RoundTripFromXmlTest(String testName) {
	super(testName);
	this.documentEquality = new DocumentEquality(mergeDuplicateProperties());
    }

    public boolean urlFlag = true;

    /**
     * @return the suite of tests being tested
     */

    public void updateNamespaces(Document doc) {
	Hashtable<String, String> nss = new Hashtable<String, String>();
	updateNamespaces(nss);
	doc.setNss(nss);
    }
   
    public String extension() {
	return ".xml";
    }
    

    public void makeDocAndTest(Statement stment, String file) {
	makeDocAndTest(stment, file, null, true);
    }
    public void makeDocAndTest(Statement stment, String file, boolean check) {
	makeDocAndTest(stment, file, null, check);
    }
    public void makeDocAndTest(Statement stment, Statement[] opt, String file) {
    	makeDocAndTest(stment, file, opt, true);
        }
    public void makeDocAndTest(Statement [] stment, Statement[] opt, String file) {
    	makeDocAndTest(stment, file, opt, true);
        }
    
    public void makeDocAndTest(Statement stment, String file, Statement[] opt, boolean check) {
    	makeDocAndTest(new Statement[] {stment}, file, opt, check);
    }
    public void makeDocAndTest(Statement []stment, String file, Statement[] opt, boolean check) {
        makeDocAndTest(stment, null, file, opt, check);
    }

    public void makeDocAndTest(Statement []stment, NamedBundle[] bundles, String file, Statement[] opt, boolean check) {
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
	    boolean result=this.documentEquality.check(doc,  doc2);
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
	serial.serialiseDocument(new File(file), doc, true);
	StringWriter sw = new StringWriter();
	serial.serialiseDocument(sw, doc, true);
	//System.out.println(sw.toString());
    }

    public Document loadFromXmlSaveAndReload(String file, boolean compare) throws Exception {
	Document doc=readXMLDocument("src/test/resources/" + file + ".xml");
	String file2 = "target/" + file + "-2" + ".xml";
	updateNamespaces(doc);

	writeDocument(doc, file2);
	Document doc2=readXMLDocument(file2);
	
	if (compare) {
	    compareDocuments(doc, doc2, compare);
	}
	return doc;
	

	
    }
    
    ///////////////////////////////////////////////////////////////////////
    

    private Document testFile(String file, boolean check) throws Exception {
	return loadFromXmlSaveAndReload(file, check);
    }
    
    public String serialize(org.w3c.dom.Element el) throws Exception {
	Transformer transformer = TransformerFactory.newInstance().newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//	initialize StreamResult with File object to save to file
	StreamResult result = new StreamResult(new StringWriter());
	DOMSource source = new DOMSource(el);
	transformer.transform(source, result);
	String xmlString = result.getWriter().toString();
	System.out.println(xmlString);
	return xmlString;
    }
    
    public void testIssue() throws Exception {
  	Document doc=testFile("issue-type", false);
  	
	Agent ag=(Agent)doc.getStatementOrBundle().get(0);
	System.out.println("agent" +ag);
	System.out.println("agent type " +ag.getType());
	System.out.println("agent type " +ag.getType().get(0));
	System.out.println("agent type " +ag.getType().get(0).getClass());
	
	//org.w3c.dom.Element el=(org.w3c.dom.Element)ag.getType().get(0);
	//serialize(el);
      }
    public void NotestPrimer() throws Exception {
  	testFile("primer-prov-xml-examples", true);
      }

    public void NOtestTypedObject() throws Exception {
  	Document doc=testFile("typedObject", true);
  	
	/*	Agent ag=(Agent)doc.getEntityAndActivityAndWasGeneratedBy().get(0);
	System.out.println("agent" +ag);
	System.out.println("agent type " +ag.getType());
	System.out.println("agent type " +ag.getType().get(0));
	System.out.println("agent type " +ag.getType().get(0).getClass());
	
	org.w3c.dom.Element el=(org.w3c.dom.Element)ag.getType().get(0);
	serialize(el);
	*/
      }



}
