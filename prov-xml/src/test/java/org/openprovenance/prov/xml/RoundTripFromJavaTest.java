package org.openprovenance.prov.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Hashtable;
import java.net.URI;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * Unit test for simple Provenance Challenge 1 like workflow.
 */
public class RoundTripFromJavaTest extends TestCase {

    public static final String EX_NS = "http://example.org/";
    public static final String EX_PREFIX = "ex";
     
    static final ProvUtilities util=new ProvUtilities();


    static final Hashtable<String, String> namespaces;

    public static ProvFactory pFactory;
    
    static Hashtable<String, String> updateNamespaces (Hashtable<String, String> nss) {
	nss.put(EX_PREFIX, EX_NS);
	nss.put("xml", "http://www.w3.org/XML/1998/namespace");
	return nss;
    }
    
    
    static {
	namespaces = updateNamespaces(new Hashtable<String, String>());
	pFactory = new ProvFactory(namespaces);
    }

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public RoundTripFromJavaTest(String testName) {
	super(testName);
    }

    public boolean urlFlag = true;

    /**
     * @return the suite of tests being tested
     */

    void updateNamespaces(Document doc) {
	Hashtable<String, String> nss = new Hashtable<String, String>();
	updateNamespaces(nss);
	doc.setNss(nss);
    }
   
    
    public void makeDocAndTest(Statement stment, String file) throws JAXBException {
	Document doc = pFactory.newDocument();
	doc.getEntityOrActivityOrWasGeneratedBy().add(stment);
	updateNamespaces(doc);

	writeXMLDocument(doc, file);
	
	Document doc2=readXMLDocument(file);
	assertTrue("self doc differ", doc.equals(doc));
	assertTrue("self doc2 differ", doc2.equals(doc2));
	assertTrue("doc differs doc2", doc.equals(doc2));
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
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    
    public void testEntity1() throws JAXBException  {
	Entity a = pFactory.newEntity("ex:e1");
	makeDocAndTest(a,"target/entity1.xml");
    }
    public void testEntity2() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e2", "entity2");
   	makeDocAndTest(a,"target/entity2.xml");
    }

    
    public void testEntity3() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e2", "entity2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	makeDocAndTest(a,"target/entity3.xml");
    }
    public void testEntity4() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e2", "entity2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
   	makeDocAndTest(a,"target/entity4.xml");
    }
    public void testEntity5() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e2", "entity2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
   	a.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
   	makeDocAndTest(a,"target/entity5.xml");
    }
   
    
    public void testEntity6() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e6", "entity6");
   	a.getType().add("a");
   	a.getType().add(1);
   	a.getType().add(1.0);
   	a.getType().add(true);
   	a.getType().add(new QName(EX_NS, "abc", EX_PREFIX));
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"hello"));
   	a.getType().add(w);
   	makeDocAndTest(a,"target/entity6.xml");
       }

    public void testEntity7() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e7", "entity7");
   	a.getType().add("a");
   	a.getType().add(1);
   	a.getType().add(1.0);
   	a.getType().add(true);
   	a.getType().add(new QName(EX_NS, "abc", EX_PREFIX));
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"hello"));
   	a.getType().add(w);
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
   	a.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
   	makeDocAndTest(a,"target/entity7.xml");
       }


}
