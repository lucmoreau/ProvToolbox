package org.openprovenance.prov.xml;

import java.io.File;
import org.openprovenance.prov.model.HasExtensibility;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasLocation;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.HasValue;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasStartedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Role;
import org.openprovenance.prov.model.Location;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.net.URI;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.KeyQNamePair;
import org.openprovenance.prov.model.URIWrapper;

import junit.framework.TestCase;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class RoundTripFromJavaTest extends TestCase {

    public static final String EX_NS = "http://example.org/";
    public static final String EX2_NS = "http://example2.org/";
    public static final String EX_PREFIX = "ex";
    public static final String EX2_PREFIX = "ex2";
    public static final String EX3_NS = "http://example3.org/";

    
    static final ProvUtilities util=new ProvUtilities();


    static final Hashtable<String, String> namespaces;

    public static org.openprovenance.prov.model.ProvFactory pFactory;
    public static ValueConverter vconv;

    static Hashtable<String, String> updateNamespaces (Hashtable<String, String> nss) {
        nss.put(EX_PREFIX, EX_NS);
        nss.put(EX2_PREFIX, EX2_NS);
        nss.put("_", EX3_NS);
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
    public RoundTripFromJavaTest(String testName) {
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
	//serial.serialiseDocument(sw, doc, true);
	//System.out.println(sw.toString());
    }

    ///////////////////////////////////////////////////////////////////////


    public void addLabel(HasLabel hl) {
        hl.getLabel().add(pFactory.newInternationalizedString("hello"));
    }

    public void addLabels(HasLabel hl) {
   	hl.getLabel().add(pFactory.newInternationalizedString("hello"));
   	hl.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
   	hl.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
    }
   

    public void addTypes(HasType ht) {
   	ht.getType().add(pFactory.newType("a", ValueConverter.QNAME_XSD_STRING));
   	ht.getType().add(pFactory.newType(1, ValueConverter.QNAME_XSD_INT));
   	ht.getType().add(pFactory.newType(1.0, ValueConverter.QNAME_XSD_FLOAT));
   	ht.getType().add(pFactory.newType(true, ValueConverter.QNAME_XSD_STRING));
   	ht.getType().add(pFactory.newType(new QName(EX_NS, "abc", EX_PREFIX),
					  ValueConverter.QNAME_XSD_QNAME));
   	ht.getType().add(pFactory.newType(pFactory.newTimeNow(),
					  ValueConverter.QNAME_XSD_DATETIME));
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"hello"));
	ht.getType().add(pFactory.newType(w,
					  ValueConverter.QNAME_XSD_ANY_URI));
    }

    public void addLocations(HasLocation hl) {
   	hl.getLocation().add(pFactory.newLocation("London",vconv));
   	hl.getLocation().add(pFactory.newLocation(1,vconv));
   	hl.getLocation().add(pFactory.newLocation(1.0,vconv));
   	hl.getLocation().add(pFactory.newLocation(true,vconv));
	//   	hl.getLocation().add(pFactory.newLocation(new QName(EX_NS, "london", EX_PREFIX),vconv));
   	hl.getLocation().add(pFactory.newLocation(pFactory.newTimeNow(),vconv));
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"london"));
   	hl.getLocation().add(pFactory.newLocation(w,vconv));
   	hl.getLocation().add(pFactory.newLocation(pFactory.newGYear("2002"),vconv));
    }
    
    public void addValue(HasValue hl) {
        hl.setValue(pFactory.newValue(new QName(EX_NS, "avalue", EX_PREFIX),
				      ValueConverter.QNAME_XSD_QNAME));
    }

    public void addFurtherAttributes(HasExtensibility he) {
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag1",EX_PREFIX,"hello", vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "bye", vconv));
	//he.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, pFactory.newInternationalizedString("bonjour","fr"), "xsd:string"));
	he.getAny().add(pFactory.newAttribute(EX2_NS,"tag3",EX2_PREFIX, "hi", vconv));

    }

    
    public void addFurtherAttributes0(HasExtensibility he) {
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag1",EX_PREFIX,"hello", vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "bye", vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, pFactory.newInternationalizedString("bonjour","fr"), ValueConverter.QNAME_XSD_STRING));
	he.getAny().add(pFactory.newAttribute(EX2_NS,"tag3",EX2_PREFIX, "hi", vconv));
	
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Integer(1), vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Long(1), vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Short((short) 1), vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Double(1.0), vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Float(1.0), vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new java.math.BigDecimal(1.0), vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Boolean(true), vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Byte((byte) 123), vconv));
	
	addFurtherAttributesWithQNames(he);
	
	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"london"));
   	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, w, vconv));
	
    }
    
   
    
    ///////////////////////////////////////////////////////////////////////
    
    public void addFurtherAttributesWithQNames(HasExtensibility he) {
        he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new QName(EX2_NS,"newyork", EX2_PREFIX), vconv));
        he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new QName(EX_NS, "london", EX_PREFIX), vconv));
        he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new QName(EX3_NS, "london"), vconv));

    }

    public void NOtestRoles() {
	Role r1=pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING);
	Role r2=pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING);
	Location l1=pFactory.newLocation("otherLocation",ValueConverter.QNAME_XSD_STRING);
	Location l2=pFactory.newLocation("otherLocation",ValueConverter.QNAME_XSD_STRING);
	System.out.println("---------------------------------------------------------------------- " );
	System.out.println("Role 1 " + r1);
	System.out.println("Role 2 " + r2);
	System.out.println("Location 1 " + l1);
	System.out.println("Location 2 " + l2);
	System.out.println("---------------------------------------------------------------------- " );
	System.out.println(r1);
	System.out.println(r2);
	System.out.println(r1.equals(r1));
	System.out.println(r1.equals(r2));
	System.out.println(r2.equals(r1));
	System.out.println(r2.equals(r2));

	System.out.println(l1.equals(l1));
	System.out.println(l1.equals(l2));
	System.out.println(l2.equals(l1));
	System.out.println(l2.equals(l2));
	System.out.println("---------------------------------------------------------------------- " );


	//assertTrue(r1.equals(r1));
	//assertTrue(l1.equals(l2));
    }

    public boolean test=true;

    public void testEntity0() throws JAXBException  {
	setNamespaces();
	Entity a = pFactory.newEntity("ex:e0");
	a.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, pFactory.newInternationalizedString("bonjour","fr"), ValueConverter.QNAME_XSD_STRING));

	if (test) {

	    a.getLocation().add(pFactory.newLocation("un llieu",ValueConverter.QNAME_XSD_STRING));
	    a.getLocation().add(pFactory.newLocation(1,ValueConverter.QNAME_XSD_INT));
	    a.getLocation().add(pFactory.newLocation(2.0,ValueConverter.QNAME_XSD_DOUBLE));


	    //ValueConverter.QNAME_XSD_INT, Note this is problematic for conversion to/from rdf

	    //THis fails

	    /*

	    a.getLocation().add(pFactory.newLocation(new QName(EX_NS, "abc", EX_PREFIX),
						 ValueConverter.QNAME_XSD_QNAME));

	    a.getLocation().add(pFactory.newLocation(new QName("http://foo/", "cde", "foo"),
						 ValueConverter.QNAME_XSD_QNAME));


	    a.getLocation().add(pFactory.newLocation(new QName("http://foo/", "fgh"),
						 ValueConverter.QNAME_XSD_QNAME));

	    */
	    URIWrapper w=new URIWrapper();
	    w.setValue(URI.create(EX_NS+"london"));
	    a.getLocation().add(pFactory.newLocation(w,ValueConverter.QNAME_XSD_ANY_URI));

	    Location loc=pFactory.newLocation(new Long(2),ValueConverter.QNAME_XSD_LONG);
	    //FIXME: Location containing a QName does not work
	    //loc.getAttributes().put(ValueConverter.QNAME_XSD_LONG,"1");
	    a.getLocation().add(loc);

	    // This fails because we don't get to read the type in xsi:type
	    //a.getLocation().add(pFactory.newLocation(2,ValueConverter.QNAME_XSD_UNSIGNED_INT));
	    // problem in prov-n parsing, since  TreeTraversal.convertTypeLiteral generate a java value without type!
	}
	makeDocAndTest(a,"target/entity0");
    }


    
    public void testEntity1() throws JAXBException  {
	setNamespaces();
	Entity a = pFactory.newEntity("ex:e1");
	makeDocAndTest(a,"target/entity1");
    }

    public void testEntity2() throws JAXBException  {
	setNamespaces();
   	Entity a = pFactory.newEntity("ex:e2", "entity2");
   	makeDocAndTest(a,"target/entity2");
    }

    public void testEntity3() throws JAXBException  {
	setNamespaces();
   	Entity a = pFactory.newEntity("ex:e3", "entity3");
   	addValue(a);
   	makeDocAndTest(a,"target/entity3");
    }

    public void testEntity4() throws JAXBException  {
	setNamespaces();
   	Entity a = pFactory.newEntity("ex:e4", "entity4");
	addLabels(a);
   	makeDocAndTest(a,"target/entity4");
    }
   
    
    public void testEntity5() throws JAXBException  {
	setNamespaces();
   	Entity a = pFactory.newEntity("ex:e5", "entity5");
	addTypes(a);
   	makeDocAndTest(a,"target/entity5");
    }

    public void testEntity6() throws JAXBException  {
	setNamespaces();
       	Entity a = pFactory.newEntity("ex:e6", "entity6");
	addLocations(a);
       	makeDocAndTest(a,"target/entity6");
    }
    public void testEntity7() throws JAXBException  {
	setNamespaces();
       	Entity a = pFactory.newEntity("ex:e7", "entity7");
	addTypes(a);
	addLocations(a);
	addLabels(a);
       	makeDocAndTest(a,"target/entity7");
    }
    public void testEntity8() throws JAXBException  {
	setNamespaces();
       	Entity a = pFactory.newEntity("ex:e8", "entity8");
	addTypes(a);
	addTypes(a);
	addLocations(a);
	addLocations(a);
	addLabels(a);
	addLabels(a);
       	makeDocAndTest(a,"target/entity8");
    }

    public void testEntity9() throws JAXBException  {
	setNamespaces();
       	Entity a = pFactory.newEntity("ex:e9", "entity9");
	addTypes(a);
	addLocations(a);
	addLabels(a);
	addFurtherAttributes(a); 
       	makeDocAndTest(a,"target/entity9");
    }

    public void testEntity10() throws JAXBException  {
	setNamespaces();
       	Entity a = pFactory.newEntity("ex:e10", "entity10");
	addTypes(a);
	addLocations(a);
	addLabels(a);
	addFurtherAttributes(a); 
       	makeDocAndTest(a,"target/entity10");
    }


    ///////////////////////////////////////////////////////////////////////
    
    
    public void testActivity1() throws JAXBException  {
	setNamespaces();
	Activity a = pFactory.newActivity("ex:a1");
	makeDocAndTest(a,"target/activity1");
    }
    public void testActivity2() throws JAXBException  {
	setNamespaces();
   	Activity a = pFactory.newActivity("ex:a2", "activity2");
   	makeDocAndTest(a,"target/activity2");
    }

    public void testActivity3() throws JAXBException  {
	setNamespaces();
	Activity a = pFactory.newActivity("ex:a1");
	a.setStartTime(pFactory.newTimeNow());
	a.setEndTime(pFactory.newTimeNow());
	makeDocAndTest(a,"target/activity3");
    }

    public void testActivity4() throws JAXBException  {
	setNamespaces();
   	Activity a = pFactory.newActivity("ex:a2", "activity2");
	addLabels(a);
   	makeDocAndTest(a,"target/activity4");
    }
    public void testActivity5() throws JAXBException  {
	setNamespaces();
   	Activity a = pFactory.newActivity("ex:a2", "activity2");
	addTypes(a);
   	makeDocAndTest(a,"target/activity5");
    }
   
    
    public void testActivity6() throws JAXBException  {
	setNamespaces();
   	Activity a = pFactory.newActivity("ex:a6", "activity6");
	addLocations(a);
   	makeDocAndTest(a,"target/activity6");
    }

    public void testActivity7() throws JAXBException  {
	setNamespaces();
       	Activity a = pFactory.newActivity("ex:a7", "activity7");
	addTypes(a);
	addLocations(a);
	addLabels(a);
       	makeDocAndTest(a,"target/activity7");
    }
    public void testActivity8() throws JAXBException  {
	setNamespaces();
       	Activity a = pFactory.newActivity("ex:a8", "activity8");
	a.setStartTime(pFactory.newTimeNow());
	a.setEndTime(pFactory.newTimeNow());
	addTypes(a);
	addTypes(a);
	addLocations(a);
	addLocations(a);
	addLabels(a);
	addLabels(a);
       	makeDocAndTest(a,"target/activity8");
    }

    public void testActivity9() throws JAXBException  {
	setNamespaces();
       	Activity a = pFactory.newActivity("ex:a9", "activity9");
        addTypes(a);
        addLocations(a);
        addLabels(a);
        addFurtherAttributes(a);
       	makeDocAndTest(a,"target/activity9");
    }


    ///////////////////////////////////////////////////////////////////////
    
    
    public void testAgent1() throws JAXBException  {
	setNamespaces();
	Agent a = pFactory.newAgent("ex:ag1");
	makeDocAndTest(a,"target/agent1");
    }

    public void testAgent2() throws JAXBException  {
	setNamespaces();
   	Agent a = pFactory.newAgent("ex:ag2", "agent2");
   	makeDocAndTest(a,"target/agent2");
    }

    
    public void testAgent3() throws JAXBException  {
	setNamespaces();
   	Agent a = pFactory.newAgent("ex:ag2", "agent2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	makeDocAndTest(a,"target/agent3");
    }
    public void testAgent4() throws JAXBException  {
	setNamespaces();
   	Agent a = pFactory.newAgent("ex:ag2", "agent2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
   	makeDocAndTest(a,"target/agent4");
    }
    public void testAgent5() throws JAXBException  {
	setNamespaces();
   	Agent a = pFactory.newAgent("ex:ag2", "agent2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
   	a.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
   	makeDocAndTest(a,"target/agent5");
    }
   
    
    public void testAgent6() throws JAXBException  {
	setNamespaces();
   	Agent a = pFactory.newAgent("ex:ag6", "agent6");
	addTypes(a);
   	makeDocAndTest(a,"target/agent6");
    }

    public void testAgent7() throws JAXBException  {
	setNamespaces();
       	Agent a = pFactory.newAgent("ex:ag7", "agent7");
	addTypes(a);
       	a.getLabel().add(pFactory.newInternationalizedString("hello"));
       	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
       	a.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
   	a.getLocation().add(pFactory.newLocation("London",vconv));
   	a.getLocation().add(pFactory.newLocation(1,vconv));
   	a.getLocation().add(pFactory.newLocation(1.0,vconv));
   	a.getLocation().add(pFactory.newLocation(true,vconv));
	//   	a.getLocation().add(pFactory.newLocation(new QName(EX_NS, "london", EX_PREFIX),vconv));
   	a.getLocation().add(pFactory.newLocation(pFactory.newTimeNow(),vconv));
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"london"));
   	a.getLocation().add(pFactory.newLocation(w,vconv));
       	makeDocAndTest(a,"target/agent7");
    }
    public void testAgent8() throws JAXBException  {
	setNamespaces();
       	Agent a = pFactory.newAgent("ex:ag8", "agent8");
	/*
       	a.getType().add("a");
       	a.getType().add("a");
       	a.getType().add(1);
       	a.getType().add(1);
       	a.getType().add(1.0);
       	a.getType().add(1.0);
       	a.getType().add(true);
       	a.getType().add(true);
       	a.getType().add(new QName(EX_NS, "abc", EX_PREFIX));
       	a.getType().add(new QName(EX_NS, "abc", EX_PREFIX));
       	a.getType().add(pFactory.newTimeNow());
       	a.getType().add(pFactory.newTimeNow());
       	URIWrapper w=new URIWrapper();
       	w.setValue(URI.create(EX_NS+"hello"));
       	a.getType().add(w);
       	a.getType().add(w);
       	a.getLabel().add(pFactory.newInternationalizedString("hello"));
       	a.getLabel().add(pFactory.newInternationalizedString("hello"));
       	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
       	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
       	a.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
       	a.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
   	a.getLocation().add(pFactory.newLocation("London",vconv));
   	a.getLocation().add(pFactory.newLocation("London",vconv));
   	a.getLocation().add(pFactory.newLocation(1,vconv));
   	a.getLocation().add(pFactory.newLocation(1,vconv));
   	a.getLocation().add(pFactory.newLocation(1.0,vconv));
   	a.getLocation().add(pFactory.newLocation(1.0,vconv));
   	a.getLocation().add(pFactory.newLocation(true,vconv));
   	a.getLocation().add(pFactory.newLocation(true,vconv));
   	a.getLocation().add(pFactory.newLocation(new QName(EX_NS, "london", EX_PREFIX),vconv));
   	a.getLocation().add(pFactory.newLocation(new QName(EX_NS, "london", EX_PREFIX),vconv));
   	a.getLocation().add(pFactory.newLocation(pFactory.newTimeNow(),vconv));
   	a.getLocation().add(pFactory.newLocation(pFactory.newTimeNow(),vconv));
   	URIWrapper w2=new URIWrapper();
   	w2.setValue(URI.create(EX_NS+"london"));*/
	addTypes(a);
	addTypes(a);

   	URIWrapper w2=new URIWrapper();
   	w2.setValue(URI.create(EX_NS+"london"));

   	a.getLocation().add(pFactory.newLocation(w2,vconv));
   	a.getLocation().add(pFactory.newLocation(w2,vconv));

       	makeDocAndTest(a,"target/agent8");
    }


    ///////////////////////////////////////////////////////////////////////
    
    public QName q(String n) {
	return new QName(EX_NS, n, EX_PREFIX);
    }
    
    public void testGeneration1() throws JAXBException  {
	setNamespaces();
	WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen1"),
							pFactory.newIDRef(q("e1")),
							null,
							null);
	makeDocAndTest(gen, "target/generation1");
    }


    public void testGeneration2() throws JAXBException  {
	setNamespaces();
	WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen2"),
							pFactory.newIDRef(q("e1")),
							null,
							pFactory.newIDRef(q("a1")));

	makeDocAndTest(gen, "target/generation2");
    }


    public void testGeneration3() throws JAXBException  {
	setNamespaces();
	WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen3"),
							pFactory.newIDRef(q("e1")),
							"somerole",
							pFactory.newIDRef(q("a1")));
        gen.getRole().add(pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING));
	makeDocAndTest(gen,"target/generation3");
    }


    public void testGeneration4() throws JAXBException  {
	setNamespaces();
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen4"),
                                                        pFactory.newIDRef(q("e1")),
                                                        "somerole",
                                                        pFactory.newIDRef(q("a1")));
        gen.setTime(pFactory.newTimeNow());
        makeDocAndTest(gen,"target/generation4");
    }
    
    public void testGeneration5() throws JAXBException  {
	setNamespaces();
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen4"),
                                                        pFactory.newIDRef(q("e1")),
                                                        "somerole",
                                                        pFactory.newIDRef(q("a1")));
        gen.setTime(pFactory.newTimeNow());
        addTypes(gen);
        addLocations(gen);
        addLabels(gen);
        addFurtherAttributes(gen);

        makeDocAndTest(gen,"target/generation5");
    }
    
    
    public void testGeneration6() throws JAXBException  {
	setNamespaces();
  	WasGeneratedBy gen = pFactory.newWasGeneratedBy((QName)null,
  							pFactory.newIDRef(q("e1")),
  							null,
  							pFactory.newIDRef(q("a1")));
  	makeDocAndTest(gen,"target/generation6");
      }

    public void testGeneration7() throws JAXBException  {
	setNamespaces();
        WasGeneratedBy gen = pFactory.newWasGeneratedBy((QName)null,
                                                        pFactory.newIDRef(q("e1")),
                                                        "somerole",
                                                        pFactory.newIDRef(q("a1")));
        gen.setTime(pFactory.newTimeNow());
        addTypes(gen);
        addLocations(gen);
        addLabels(gen);
        addFurtherAttributes(gen);

        makeDocAndTest(gen,"target/generation7");
    }
    
    //////////////////////////////////
    
    public void testUsage1() throws JAXBException  {
	setNamespaces();
        Used use = pFactory.newUsed(q("use1"),
                                    null,
                                    null,
                                    pFactory.newIDRef(q("e1")));
        makeDocAndTest(use,"target/usage1");
    }

    public void testUsage2() throws JAXBException  {
	setNamespaces();
        Used use = pFactory.newUsed(q("use2"),
                                    pFactory.newIDRef(q("a1")),
                                    null,
                                    pFactory.newIDRef(q("e1")));
        
        makeDocAndTest(use,"target/usage2");
    }

    public void testUsage3() throws JAXBException  {
	setNamespaces();
        Used use = pFactory.newUsed(q("use3"),
                                    pFactory.newIDRef(q("a1")),
                                    "somerole",
                                    pFactory.newIDRef(q("e1")));
        use.getRole().add(pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING));
        
        makeDocAndTest(use,"target/usage3");
    }
    
    public void testUsage4() throws JAXBException  {
	setNamespaces();
        Used use = pFactory.newUsed(q("use4"),
                                    pFactory.newIDRef(q("a1")),
                                    "somerole",
                                    pFactory.newIDRef(q("e1")));
        use.setTime(pFactory.newTimeNow());

        makeDocAndTest(use,"target/usage4");
    }

    public void testUsage5() throws JAXBException  {
	setNamespaces();
        Used use = pFactory.newUsed(q("use4"),
                                    pFactory.newIDRef(q("a1")),
                                    "somerole",
                                    pFactory.newIDRef(q("e1")));
        use.setTime(pFactory.newTimeNow());
        addTypes(use);
        addLocations(use);
        addLabels(use);
        addFurtherAttributes(use);
        
        makeDocAndTest(use,"target/usage5");
    }

    public void testUsage6() throws JAXBException  {
	setNamespaces();
        Used use = pFactory.newUsed((QName)null,
                                    pFactory.newIDRef(q("a1")),
                                    null,
                                    pFactory.newIDRef(q("e1")));

        makeDocAndTest(use,"target/usage6");
    }

    public void testUsage7() throws JAXBException  {
	setNamespaces();
        Used use = pFactory.newUsed((QName)null,
                                    pFactory.newIDRef(q("a1")),
                                    "somerole",
                                    pFactory.newIDRef(q("e1")));
        use.setTime(pFactory.newTimeNow());
        addTypes(use);
        addLocations(use);
        addLabels(use);
        addFurtherAttributes(use);
        
        makeDocAndTest(use,"target/usage7");
    }
    
    // //////////////////////////////////////////////

    public void testInvalidation1() throws JAXBException {
	setNamespaces();
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv1"),
							    pFactory.newIDRef(q("e1")),
							    null);
	makeDocAndTest(inv,  "target/invalidation1");
    }

    public void testInvalidation2() throws JAXBException {
	setNamespaces();
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv2"),
							    pFactory.newIDRef(q("e1")),
							    pFactory.newIDRef(q("a1")));
	makeDocAndTest(inv, "target/invalidation2");
    }

    public void testInvalidation3() throws JAXBException {
	setNamespaces();
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv3"),
							    pFactory.newIDRef(q("e1")),
							    pFactory.newIDRef(q("a1")));
	inv.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
	inv.getRole().add(pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING));
	makeDocAndTest(inv,  "target/invalidation3");
    }

    public void testInvalidation4() throws JAXBException {
	setNamespaces();
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv4"),
							    pFactory.newIDRef(q("e1")),
							    pFactory.newIDRef(q("a1")));
	inv.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
	inv.setTime(pFactory.newTimeNow());
	makeDocAndTest(inv,  "target/invalidation4");
    }

    public void testInvalidation5() throws JAXBException {
	setNamespaces();
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv4"),
							    pFactory.newIDRef(q("e1")),
							    pFactory.newIDRef(q("a1")));
	inv.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
	inv.setTime(pFactory.newTimeNow());
	addTypes(inv);
	addLocations(inv);

	addLabels(inv);
	addFurtherAttributes(inv);

	makeDocAndTest(inv,  "target/invalidation5");
    }

    public void testInvalidation6() throws JAXBException {
	setNamespaces();
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy((QName) null,
							    pFactory.newIDRef(q("e1")),
							    pFactory.newIDRef(q("a1")));
	makeDocAndTest(inv,  "target/invalidation6");
    }

    public void testInvalidation7() throws JAXBException {
	setNamespaces();
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy((QName) null,
							    pFactory.newIDRef(q("e1")),
							    pFactory.newIDRef(q("a1")));
	inv.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
	inv.setTime(pFactory.newTimeNow());
	addTypes(inv);
	addLocations(inv);
	addLabels(inv);
	addFurtherAttributes(inv);

	makeDocAndTest(inv, "target/invalidation7");
    }
    
//////////////////////////////////

    public void testStart1() throws JAXBException {
	setNamespaces();
	WasStartedBy start = pFactory.newWasStartedBy(q("start1"),
						      null,
						      pFactory.newIDRef(q("e1")));
	
	makeDocAndTest(start, "target/start1");
    }

    public void testStart2() throws JAXBException {
	setNamespaces();
	WasStartedBy start = pFactory.newWasStartedBy(q("start2"),
						      pFactory.newIDRef(q("a1")),
						      pFactory.newIDRef(q("e1")));
	
	makeDocAndTest(start, "target/start2");
    }

    public void testStart3() throws JAXBException {
	setNamespaces();
	WasStartedBy start = pFactory.newWasStartedBy(q("start3"),
						      pFactory.newIDRef(q("a1")),
						      null);
	
	makeDocAndTest(start, "target/start3");
    }

    public void testStart4() throws JAXBException {
	setNamespaces();
	WasStartedBy start = pFactory.newWasStartedBy(q("start4"),
						      null,
						      pFactory.newIDRef(q("e1")));
	start.setStarter(pFactory.newIDRef(q("a2")));
	
	makeDocAndTest(start, "target/start4");
    }

    public void testStart5() throws JAXBException {
	setNamespaces();
	WasStartedBy start = pFactory.newWasStartedBy(q("start5"),
						      pFactory.newIDRef(q("a1")),
						      pFactory.newIDRef(q("e1")));
	start.setStarter(pFactory.newIDRef(q("a2")));
	
	makeDocAndTest(start, "target/start5");
    }

    public void testStart6() throws JAXBException {
	setNamespaces();
	WasStartedBy start = pFactory.newWasStartedBy(q("start6"),
						      pFactory.newIDRef(q("a1")),
						      null);
	start.setStarter(pFactory.newIDRef(q("a2")));
	
	makeDocAndTest(start, "target/start6");
    }

    
    public void testStart7() throws JAXBException {
	setNamespaces();
   	WasStartedBy start = pFactory.newWasStartedBy(q("start7"),
   						      pFactory.newIDRef(q("a1")),
   						      null);
   	start.setStarter(pFactory.newIDRef(q("a2")));
   	start.setTime(pFactory.newTimeNow());

   	makeDocAndTest(start, "target/start7");
    }
    
    public void testStart8() throws JAXBException {
	setNamespaces();
   	WasStartedBy start = pFactory.newWasStartedBy(q("start8"),
   						      pFactory.newIDRef(q("a1")),
   						      null);
   	start.setStarter(pFactory.newIDRef(q("a2")));
   	start.setTime(pFactory.newTimeNow());
   	start.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
   	start.getRole().add(pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING));
   	addTypes(start);
	addLocations(start);
	addLabels(start);
	addFurtherAttributes(start);

   	makeDocAndTest(start, "target/start8");
    }
    
    public void testStart9() throws JAXBException {
	setNamespaces();
   	WasStartedBy start = pFactory.newWasStartedBy((QName)null,
   						      pFactory.newIDRef(q("a1")),
   						      pFactory.newIDRef(q("e1")));
   	
   	makeDocAndTest(start, "target/start9");
       }
    
    public void testStart10() throws JAXBException {
	setNamespaces();
   	WasStartedBy start = pFactory.newWasStartedBy((QName)null,
   						      pFactory.newIDRef(q("a1")),
   						      null);
   	start.setStarter(pFactory.newIDRef(q("a2")));
   	start.setTime(pFactory.newTimeNow());
   	start.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
   	start.getRole().add(pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING));
   	addTypes(start);
	addLocations(start);
	addLabels(start);
	addFurtherAttributes(start);

   	makeDocAndTest(start, "target/start10");
    }
    
    // ////////////////////////////////

    public void testEnd1() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy(q("end1"), null,
						pFactory.newIDRef(q("e1")));
	
	makeDocAndTest(end, "target/end1");
    }

    public void testEnd2() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy(q("end2"),
						pFactory.newIDRef(q("a1")),
						pFactory.newIDRef(q("e1")));

	makeDocAndTest(end, "target/end2");
    }

    public void testEnd3() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy(q("end3"),
						pFactory.newIDRef(q("a1")),
						null);
	
	makeDocAndTest(end, "target/end3");
    }

    public void testEnd4() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy(q("end4"), null,
						pFactory.newIDRef(q("e1")));
	end.setEnder(pFactory.newIDRef(q("a2")));
	
	makeDocAndTest(end, "target/end4");
    }

    public void testEnd5() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy(q("end5"),
						pFactory.newIDRef(q("a1")),
						pFactory.newIDRef(q("e1")));
	end.setEnder(pFactory.newIDRef(q("a2")));

	makeDocAndTest(end, "target/end5");
    }

    public void testEnd6() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy(q("end6"),
						pFactory.newIDRef(q("a1")),
						null);
	end.setEnder(pFactory.newIDRef(q("a2")));
	
	makeDocAndTest(end, "target/end6");
    }

    public void testEnd7() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy(q("end7"),
						pFactory.newIDRef(q("a1")),
						null);
	end.setEnder(pFactory.newIDRef(q("a2")));
	end.setTime(pFactory.newTimeNow());
	
	makeDocAndTest(end, "target/end7");
    }

    public void testEnd8() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy(q("end8"),
						pFactory.newIDRef(q("a1")),
						null);
	end.setEnder(pFactory.newIDRef(q("a2")));
	end.setTime(pFactory.newTimeNow());
	end.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
	end.getRole().add(pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING));
	addTypes(end);
	addLocations(end);
	addLabels(end);
	addFurtherAttributes(end);

	makeDocAndTest(end, "target/end8");
    }

    public void testEnd9() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy((QName) null,
						pFactory.newIDRef(q("a1")),
						pFactory.newIDRef(q("e1")));
	
	makeDocAndTest(end, "target/end9");
    }

    public void testEnd10() throws JAXBException {
	setNamespaces();
	WasEndedBy end = pFactory.newWasEndedBy((QName) null,
						pFactory.newIDRef(q("a1")),
						null);
	end.setEnder(pFactory.newIDRef(q("a2")));
	end.setTime(pFactory.newTimeNow());
	end.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
	end.getRole().add(pFactory.newRole("otherRole",ValueConverter.QNAME_XSD_STRING));
	addTypes(end);
	addLocations(end);
	addLabels(end);
	addFurtherAttributes(end);

	makeDocAndTest(end, "target/end10");
    }
    
    
    // ////////////////////////////////

    public void testDerivation1() throws JAXBException {
	setNamespaces();
	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der1"), 
	                                                null,
	                                                pFactory.newIDRef(q("e1")));
	makeDocAndTest(der, "target/derivation1");
    }

    public void testDerivation2() throws JAXBException {
	setNamespaces();
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der2"), 
   	                                                pFactory.newIDRef(q("e2")),
   	                                                null);
   	makeDocAndTest(der, "target/derivation2");
    }
    
    public void testDerivation3() throws JAXBException {
	setNamespaces();
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der3"), 
   	                                                pFactory.newIDRef(q("e2")),
   	                                                pFactory.newIDRef(q("e1")));   	
   	makeDocAndTest(der, "target/derivation3");
    }

    public void testDerivation4() throws JAXBException {
	setNamespaces();
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der4"), 
   	                                                pFactory.newIDRef(q("e2")),
   	                                                pFactory.newIDRef(q("e1")));
   	addLabel(der);
   	makeDocAndTest(der, "target/derivation4");
    }
    
    public void testDerivation5() throws JAXBException {
	setNamespaces();
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der5"), 
   	                                                pFactory.newIDRef(q("e2")),
   	                                                pFactory.newIDRef(q("e1")));
   	der.setActivity(pFactory.newIDRef(q("a")));
   	makeDocAndTest(der, "target/derivation5");
    }
    
    
    public void testDerivation6() throws JAXBException {
	setNamespaces();
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der6"), 
   	                                                pFactory.newIDRef(q("e2")),
   	                                                pFactory.newIDRef(q("e1")));
   	der.setActivity(pFactory.newIDRef(q("a")));
   	der.setUsage(pFactory.newIDRef(q("u")));
   	makeDocAndTest(der, "target/derivation6");
    }
    
    public void testDerivation7() throws JAXBException {
	setNamespaces();
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der7"), 
   	                                                pFactory.newIDRef(q("e2")),
   	                                                pFactory.newIDRef(q("e1")));
   	der.setActivity(pFactory.newIDRef(q("a")));
   	der.setUsage(pFactory.newIDRef(q("u")));
   	der.setGeneration(pFactory.newIDRef(q("g")));
   	
   	makeDocAndTest(der, "target/derivation7");
    }
    
    
    
    public void testDerivation8() throws JAXBException {
	setNamespaces();
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der8"), 
   	                                                pFactory.newIDRef(q("e2")),
   	                                                pFactory.newIDRef(q("e1")));
   	addLabel(der);
   	addTypes(der);
   	addFurtherAttributes(der);
   	
   	makeDocAndTest(der, "target/derivation8");
    }
    
    public void testDerivation9() throws JAXBException {
	setNamespaces();
   	WasDerivedFrom der = pFactory.newWasDerivedFrom((QName)null, 
   	                                                pFactory.newIDRef(q("e2")),
   	                                                null);
   	addTypes(der);
   	makeDocAndTest(der, "target/derivation9");
    }
    
    public void testDerivation10() throws JAXBException {
	setNamespaces();
        WasDerivedFrom der = pFactory.newWasDerivedFrom((QName)null, 
                                                        pFactory.newIDRef(q("e2")),
                                                        pFactory.newIDRef(q("e1")));
        der.setActivity(pFactory.newIDRef(q("a")));
        der.setUsage(pFactory.newIDRef(q("u")));
        der.setGeneration(pFactory.newIDRef(q("g")));
        makeDocAndTest(der, "target/derivation10");
    }
    
    public void testDerivation11() throws JAXBException {
	setNamespaces();
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("rev1"), 
                                                        pFactory.newIDRef(q("e2")),
                                                        pFactory.newIDRef(q("e1")));
        der.setActivity(pFactory.newIDRef(q("a")));
        der.setUsage(pFactory.newIDRef(q("u")));
        der.setGeneration(pFactory.newIDRef(q("g")));
        pFactory.addRevisionType(der);
        makeDocAndTest(der, "target/derivation11");
    }

    public void testDerivation12() throws JAXBException {
	setNamespaces();
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("quo1"), 
                                                        pFactory.newIDRef(q("e2")),
                                                        pFactory.newIDRef(q("e1")));
        der.setActivity(pFactory.newIDRef(q("a")));
        der.setUsage(pFactory.newIDRef(q("u")));
        der.setGeneration(pFactory.newIDRef(q("g")));
        pFactory.addQuotationType(der);
        makeDocAndTest(der, "target/derivation12");
    }

    public void testDerivation13() throws JAXBException {
	setNamespaces();
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("prim1"), 
                                                        pFactory.newIDRef(q("e2")),
                                                        pFactory.newIDRef(q("e1")));
        der.setActivity(pFactory.newIDRef(q("a")));
        der.setUsage(pFactory.newIDRef(q("u")));
        der.setGeneration(pFactory.newIDRef(q("g")));
        pFactory.addPrimarySourceType(der);
        makeDocAndTest(der, "target/derivation13");
    }
    
    // ////////////////////////////////

    public void testAssociation1() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc1"), 
                                                                pFactory.newIDRef(q("a1")),
                                                                null);
        makeDocAndTest(assoc, "target/association1");
    }

    public void testAssociation2() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc2"), 
                                                                null,
                                                                pFactory.newIDRef(q("ag1")));
        makeDocAndTest(assoc, "target/association2");
    }

    public void testAssociation3() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc3"), 
                                                                pFactory.newIDRef(q("a1")),
                                                                pFactory.newIDRef(q("ag1")));
        makeDocAndTest(assoc, "target/association3");
    }


    public void testAssociation4() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc4"), 
                                                                pFactory.newIDRef(q("a1")),
                                                                pFactory.newIDRef(q("ag1")));
        assoc.setPlan(pFactory.newIDRef(q("plan1")));
        makeDocAndTest(assoc, "target/association4");
    }

    
    public void testAssociation5() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith((QName)null, 
                                                                pFactory.newIDRef(q("a1")),
                                                                pFactory.newIDRef(q("ag1")));
        makeDocAndTest(assoc, "target/association5");
    }
    
    

    public void testAssociation6() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc6"), 
                                                                pFactory.newIDRef(q("a1")),
                                                                pFactory.newIDRef(q("ag1")));
        assoc.setPlan(pFactory.newIDRef(q("plan1")));
        addLabels(assoc);
        makeDocAndTest(assoc, "target/association6");
    }

    public void testAssociation7() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc7"), 
                                                                pFactory.newIDRef(q("a1")),
                                                                pFactory.newIDRef(q("ag1")));
        assoc.setPlan(pFactory.newIDRef(q("plan1")));
        addLabels(assoc);
        addTypes(assoc);        
        makeDocAndTest(assoc, "target/association7");
    }


    public void testAssociation8() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc8"), 
                                                                pFactory.newIDRef(q("a1")),
                                                                pFactory.newIDRef(q("ag1")));
        assoc.setPlan(pFactory.newIDRef(q("plan1")));
        assoc.getRole().add(pFactory.newRole("someRole",ValueConverter.QNAME_XSD_STRING));
        assoc.getRole().add(pFactory.newRole("someOtherRole",ValueConverter.QNAME_XSD_STRING));
        makeDocAndTest(assoc, "target/association8");
    }
    

    public void testAssociation9() throws JAXBException {
	setNamespaces();
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc9"), 
                                                                pFactory.newIDRef(q("a1")),
                                                                pFactory.newIDRef(q("ag1")));
        assoc.setPlan(pFactory.newIDRef(q("plan1")));
        addLabels(assoc);
        addTypes(assoc);
        addFurtherAttributes(assoc);
        makeDocAndTest(assoc, "target/association9");
    }

 // ////////////////////////////////

    public void testAttribution1() throws JAXBException {
	setNamespaces();
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr1"), 
                                                           pFactory.newIDRef(q("e1")),
                                                           null);
        makeDocAndTest(attr, "target/attribution1");
    }
    
    public void testAttribution2() throws JAXBException {
	setNamespaces();
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr2"), 
                                                           null,
                                                           pFactory.newIDRef(q("ag1")));
        makeDocAndTest(attr, "target/attribution2");
    }
    
    public void testAttribution3() throws JAXBException {
	setNamespaces();
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr3"), 
                                                           pFactory.newIDRef(q("e1")),
                                                           pFactory.newIDRef(q("ag1")));
        makeDocAndTest(attr, "target/attribution3");
    }


    public void testAttribution4() throws JAXBException {
	setNamespaces();
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr4"), 
                                                           pFactory.newIDRef(q("e1")),
                                                           pFactory.newIDRef(q("ag1")));
        makeDocAndTest(attr, "target/attribution4");
    }

    
    public void testAttribution5() throws JAXBException {
	setNamespaces();
        WasAttributedTo attr = pFactory.newWasAttributedTo((QName)null, 
                                                           pFactory.newIDRef(q("e1")),
                                                           pFactory.newIDRef(q("ag1")));
        makeDocAndTest(attr, "target/attribution5");
    }
    
    

    public void testAttribution6() throws JAXBException {
	setNamespaces();
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr6"), 
                                                           pFactory.newIDRef(q("e1")),
                                                           pFactory.newIDRef(q("ag1")));
        addLabels(attr);
        makeDocAndTest(attr, "target/attribution6");
    }

    public void testAttribution7() throws JAXBException {
	setNamespaces();
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr7"), 
                                                           pFactory.newIDRef(q("e1")),
                                                           pFactory.newIDRef(q("ag1")));
        addLabels(attr);
        addTypes(attr);
        makeDocAndTest(attr, "target/attribution7");
    }


    public void testAttribution8() throws JAXBException {
	setNamespaces();
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr8"), 
                                                           pFactory.newIDRef(q("e1")),
                                                           pFactory.newIDRef(q("ag1")));
        addLabels(attr);
        addTypes(attr);
        addFurtherAttributes(attr);
        makeDocAndTest(attr, "target/attribution8");
    }


    // ////////////////////////////////

       public void testDelegation1() throws JAXBException {
	setNamespaces();
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del1"), 
                                                              pFactory.newIDRef(q("e1")),
                                                              null,
                                                              null);
           makeDocAndTest(del, "target/delegation1");
       }
       
       public void testDelegation2() throws JAXBException {
	setNamespaces();
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del2"), 
                                                              null,
                                                              pFactory.newIDRef(q("ag1")),
                                                              null);
           makeDocAndTest(del, "target/delegation2");
       }
       
       public void testDelegation3() throws JAXBException {
	setNamespaces();
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del3"), 
                                                              pFactory.newIDRef(q("e1")),
                                                              pFactory.newIDRef(q("ag1")),
                                                              null);
           makeDocAndTest(del, "target/delegation3");
       }


       public void testDelegation4() throws JAXBException {
	setNamespaces();
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del4"), 
                                                              pFactory.newIDRef(q("e1")),
                                                              pFactory.newIDRef(q("ag1")),
                                                              pFactory.newIDRef(q("a")));
           
           makeDocAndTest(del, "target/delegation4");
       }

       
       public void testDelegation5() throws JAXBException {
	setNamespaces();
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf((QName)null, 
                                                              pFactory.newIDRef(q("e1")),
                                                              pFactory.newIDRef(q("ag1")),
                                                              null);
           makeDocAndTest(del, "target/delegation5");
       }
       
       

       public void testDelegation6() throws JAXBException {
	setNamespaces();
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del6"), 
                                                              pFactory.newIDRef(q("e1")),
                                                              pFactory.newIDRef(q("ag1")),
                                                              pFactory.newIDRef(q("a")));
           addLabels(del);
           makeDocAndTest(del, "target/delegation6");
       }

       public void testDelegation7() throws JAXBException {
	setNamespaces();
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del7"), 
                                                              pFactory.newIDRef(q("e1")),
                                                              pFactory.newIDRef(q("ag1")),
                                                              pFactory.newIDRef(q("a")));
           addLabels(del);
           addTypes(del);
           makeDocAndTest(del, "target/delegation7");
       }


       public void testDelegation8() throws JAXBException {
	setNamespaces();
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del8"), 
                                                              pFactory.newIDRef(q("e1")),
                                                              pFactory.newIDRef(q("ag1")),
                                                              pFactory.newIDRef(q("a")));
           addLabels(del);
           addTypes(del);
           addFurtherAttributes(del);
           makeDocAndTest(del, "target/delegation8");
       }

       // ////////////////////////////////

       public void testCommunication1() throws JAXBException {
	setNamespaces();
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf1"), 
                                                         pFactory.newIDRef(q("a2")),
                                                         null);
           makeDocAndTest(inf, "target/communication1");
       }
       
       public void testCommunication2() throws JAXBException {
	setNamespaces();
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf2"), 
                                                              null,
                                                              pFactory.newIDRef(q("a1")));
           makeDocAndTest(inf, "target/communication2");
       }
       
       public void testCommunication3() throws JAXBException {
	setNamespaces();
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf3"), 
                                                         pFactory.newIDRef(q("a2")),
                                                         pFactory.newIDRef(q("a1")));
           makeDocAndTest(inf, "target/communication3");
       }


       
       public void testCommunication4() throws JAXBException {
	setNamespaces();
           WasInformedBy inf = pFactory.newWasInformedBy((QName)null, 
                                                         pFactory.newIDRef(q("a2")),
                                                         pFactory.newIDRef(q("a1")));
           makeDocAndTest(inf, "target/communication4");
       }
       
       

       public void testCommunication5() throws JAXBException {
	setNamespaces();
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf5"), 
                                                         pFactory.newIDRef(q("a2")),
                                                         pFactory.newIDRef(q("a1")));
           addLabels(inf);
           makeDocAndTest(inf, "target/communication5");
       }

       public void testCommunication6() throws JAXBException {
	setNamespaces();
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf6"), 
                                                         pFactory.newIDRef(q("a2")),
                                                         pFactory.newIDRef(q("a1")));
           addLabels(inf);
           addTypes(inf);
           makeDocAndTest(inf, "target/communication6");
       }


       public void testCommunication7() throws JAXBException {
	setNamespaces();
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf7"), 
                                                         pFactory.newIDRef(q("a2")),
                                                         pFactory.newIDRef(q("a1")));
           addLabels(inf);
           addTypes(inf);
           addFurtherAttributes(inf);
           makeDocAndTest(inf, "target/communication7");
       }


       // ////////////////////////////////

       public void testInfluence1() throws JAXBException {
	setNamespaces();
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf1"), 
                                                             pFactory.newIDRef(q("a2")),
                                                             null);
           makeDocAndTest(inf, "target/influence1");
       }
       
       public void testInfluence2() throws JAXBException {
	setNamespaces();
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf2"), 
                                                             null,
                                                             pFactory.newIDRef(q("a1")));
           makeDocAndTest(inf, "target/influence2");
       }
       
       public void testInfluence3() throws JAXBException {
	setNamespaces();
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf3"), 
                                                             pFactory.newIDRef(q("a2")),
                                                             pFactory.newIDRef(q("a1")));
           makeDocAndTest(inf, "target/influence3");
       }


       
       public void testInfluence4() throws JAXBException {
	setNamespaces();
           WasInfluencedBy inf = pFactory.newWasInfluencedBy((QName)null, 
                                                             pFactory.newIDRef(q("a2")),
                                                             pFactory.newIDRef(q("a1")));
           makeDocAndTest(inf, "target/influence4");
       }
       
       

       public void testInfluence5() throws JAXBException {
	setNamespaces();
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf5"), 
                                                             pFactory.newIDRef(q("a2")),
                                                             pFactory.newIDRef(q("a1")));
           addLabels(inf);
           makeDocAndTest(inf, "target/influence5");
       }

       public void testInfluence6() throws JAXBException {
	setNamespaces();
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf6"), 
                                                             pFactory.newIDRef(q("a2")),
                                                             pFactory.newIDRef(q("a1")));
           addLabels(inf);
           addTypes(inf);
           makeDocAndTest(inf, "target/influence6");
       }


       public void testInfluence7() throws JAXBException {
	setNamespaces();
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf7"), 
                                                             pFactory.newIDRef(q("a2")),
                                                             pFactory.newIDRef(q("a1")));
           addLabels(inf);
           addTypes(inf);
           addFurtherAttributes(inf);
           makeDocAndTest(inf, "target/influence7");
       }

       

       // ////////////////////////////////

       public void testAlternate1() throws JAXBException {
	setNamespaces();
           AlternateOf alt = pFactory.newAlternateOf(pFactory.newIDRef(q("e2")),
                                                     pFactory.newIDRef(q("e1")));
           makeDocAndTest(alt, "target/alternate1");
       }
       

       public void testSpecialization1() throws JAXBException {
	setNamespaces();
           SpecializationOf spe = pFactory.newSpecializationOf(pFactory.newIDRef(q("e2")),
                                                               pFactory.newIDRef(q("e1")));
           makeDocAndTest(spe, "target/specialization1");
       }
       
       
       public void testMention1() throws JAXBException {
	setNamespaces();
           MentionOf men = pFactory.newMentionOf(pFactory.newIDRef(q("e2")),
                                                 pFactory.newIDRef(q("e1")),
                                                 null);
           makeDocAndTest(men, "target/mention1");
       }
       
       public void testMention2() throws JAXBException {
	setNamespaces();
           MentionOf men = pFactory.newMentionOf(pFactory.newIDRef(q("e2")),
                                                 pFactory.newIDRef(q("e1")),
                                                 pFactory.newIDRef(q("b")));
           makeDocAndTest(men, "target/mention2");
       }
       

       public void testMembership1() throws JAXBException {
	setNamespaces();
           HadMember mem = pFactory.newHadMember(pFactory.newIDRef(q("c")),
                                                 pFactory.newIDRef(q("e1")));
           makeDocAndTest(mem, "target/member1");
       }
       public void testMembership2() throws JAXBException {
	setNamespaces();
           HadMember mem = pFactory.newHadMember(pFactory.newIDRef(q("c")),
                                                 pFactory.newIDRef(q("e1")),
                                                 pFactory.newIDRef(q("e2")));
           //TODO: multiple arguments not supported by toolbox 
           makeDocAndTest(mem, "target/member2");
       }
       public void testMembership3() throws JAXBException {
	setNamespaces();
           HadMember mem = pFactory.newHadMember(pFactory.newIDRef(q("c")),
                                                 pFactory.newIDRef(q("e1")),
                                                 pFactory.newIDRef(q("e2")),
                                                 pFactory.newIDRef(q("e3")));
           //TODO: multiple arguments not supported by toolbox 
           makeDocAndTest(mem, "target/member3");
       }
     
       public void testScruffyGeneration1() throws JAXBException  {
	setNamespaces();
    	   WasGeneratedBy gen1 = pFactory.newWasGeneratedBy(q("gen1"),
					pFactory.newIDRef(q("e1")),
					null,
					pFactory.newIDRef(q("a1")));
	       gen1.setTime(pFactory.newTimeNow());
	       WasGeneratedBy gen2 = pFactory.newWasGeneratedBy(q("gen1"),
			          pFactory.newIDRef(q("e1")),
			        	null,
			          pFactory.newIDRef(q("a1")));
	  	   gen2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { gen1, gen2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-generation1");
       }
       
       public void testScruffyGeneration2() throws JAXBException  {
	setNamespaces();
    	   WasGeneratedBy gen1 = pFactory.newWasGeneratedBy(q("gen1"),
					pFactory.newIDRef(q("e1")),
					null,
					pFactory.newIDRef(q("a1")));
	       gen1.setTime(pFactory.newTimeNow());
	   	   gen1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));

	       WasGeneratedBy gen2 = pFactory.newWasGeneratedBy(q("gen1"),
			          pFactory.newIDRef(q("e1")),
			        	null,
			          pFactory.newIDRef(q("a1")));
	  	   gen2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   gen2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));
   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { gen1, gen2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-generation2");
       }
       
       public void testScruffyInvalidation1() throws JAXBException  {
	setNamespaces();
    	   WasInvalidatedBy inv1 = pFactory.newWasInvalidatedBy(q("inv1"),
					pFactory.newIDRef(q("e1")),
					pFactory.newIDRef(q("a1")));
	       inv1.setTime(pFactory.newTimeNow());
	       WasInvalidatedBy inv2 = pFactory.newWasInvalidatedBy(q("inv1"),
			          pFactory.newIDRef(q("e1")),
			          pFactory.newIDRef(q("a1")));
	  	   inv2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { inv1, inv2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-invalidation1");
       }
       
       public void testScruffyInvalidation2() throws JAXBException  {
	setNamespaces();
    	   WasInvalidatedBy inv1 = pFactory.newWasInvalidatedBy(q("inv1"),
					pFactory.newIDRef(q("e1")),
					pFactory.newIDRef(q("a1")));
	       inv1.setTime(pFactory.newTimeNow());
	   	   inv1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));

	       WasInvalidatedBy inv2 = pFactory.newWasInvalidatedBy(q("inv1"),
			          pFactory.newIDRef(q("e1")),
			          pFactory.newIDRef(q("a1")));
	  	   inv2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   inv2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));

   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { inv1, inv2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-invalidation2");
       }
       
       public void testScruffyUsage1() throws JAXBException  {
	setNamespaces();
    	   Used use1 = pFactory.newUsed(q("use1"),
					pFactory.newIDRef(q("a1")),
					null,
					pFactory.newIDRef(q("e1")));
	       use1.setTime(pFactory.newTimeNow());
	       Used use2 = pFactory.newUsed(q("use1"),
			          pFactory.newIDRef(q("a1")),
			        	null,
			          pFactory.newIDRef(q("e1")));
	  	   use2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { use1, use2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-usage1");
       }
       
       public void testScruffyUsage2() throws JAXBException  {
	setNamespaces();
    	   Used use1 = pFactory.newUsed(q("use1"),
					pFactory.newIDRef(q("a1")),
					null,
					pFactory.newIDRef(q("e1")));
	       use1.setTime(pFactory.newTimeNow());
	   	   use1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));

	       Used use2 = pFactory.newUsed(q("use1"),
			          pFactory.newIDRef(q("a1")),
			        	null,
			          pFactory.newIDRef(q("e1")));
	  	   use2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   use2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));

   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { use1, use2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-usage2");
       }
       
       public void testScruffyStart1() throws JAXBException  {
	setNamespaces();
    	   WasStartedBy start1 = pFactory.newWasStartedBy(q("start1"),
					pFactory.newIDRef(q("a1")),
					pFactory.newIDRef(q("e1")));
	       start1.setTime(pFactory.newTimeNow());

	       WasStartedBy start2 = pFactory.newWasStartedBy(q("start1"),
			          pFactory.newIDRef(q("a1")),
			          pFactory.newIDRef(q("e1")));
	  	   start2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	

   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { start1, start2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-start1");
       }
       
       public void testScruffyStart2() throws JAXBException  {
	setNamespaces();
    	   WasStartedBy start1 = pFactory.newWasStartedBy(q("start1"),
					pFactory.newIDRef(q("a1")),
					pFactory.newIDRef(q("e1")));
	       start1.setTime(pFactory.newTimeNow());
	   	   start1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));

	       WasStartedBy start2 = pFactory.newWasStartedBy(q("start1"),
			          pFactory.newIDRef(q("a1")),
			          pFactory.newIDRef(q("e1")));
	  	   start2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   start2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));

   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { start1, start2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-start2");
       }
       
       public void testScruffyStart3() throws JAXBException  {
	setNamespaces();
    	   WasStartedBy start1 = pFactory.newWasStartedBy(q("start1"),
					pFactory.newIDRef(q("a1")),
					pFactory.newIDRef(q("e1")));
	       start1.setTime(pFactory.newTimeNow());
	   	   start1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));
	   	   start1.setStarter(pFactory.newIDRef(q("a1s")));	

	       WasStartedBy start2 = pFactory.newWasStartedBy(q("start1"),
			          pFactory.newIDRef(q("a1")),
			          pFactory.newIDRef(q("e1")));
	  	   start2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   start2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));
		   start2.setStarter(pFactory.newIDRef(q("a2s")));
		   
   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Activity a2=pFactory.newActivity(q("a2"));
		   Activity a2s=pFactory.newActivity(q("a2s"));
		   Statement [] opt=new Statement[] { e1, a1, a2, a2s };
		   Statement [] statements=new Statement[] { start1, start2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-start3");
       }
       
       public void testScruffyStart4() throws JAXBException  {
	setNamespaces();
    	   WasStartedBy start1 = pFactory.newWasStartedBy(q("start1"),
					pFactory.newIDRef(q("a1")),
					pFactory.newIDRef(q("e1")));
	       start1.setTime(pFactory.newTimeNow());
	   	   start1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));
	   	   start1.setStarter(pFactory.newIDRef(q("a1s")));	

	       WasStartedBy start2 = pFactory.newWasStartedBy(q("start1"),
			          pFactory.newIDRef(q("a2")),
			          pFactory.newIDRef(q("e2")));
	  	   start2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   start2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));
		   start2.setStarter(pFactory.newIDRef(q("a2s")));
		   
		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Activity a1s=pFactory.newActivity(q("a1s"));
		   Entity e2=pFactory.newEntity(q("e2"));
		   Activity a2=pFactory.newActivity(q("a2"));
		   Activity a2s=pFactory.newActivity(q("a2s"));
		   Statement [] opt=new Statement[] { e1, a1, a1s, e2, a2, a2s };
		   Statement [] statements=new Statement[] { start1, start2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-start4");
       }
       
       public void testScruffyEnd1() throws JAXBException  {
	setNamespaces();
    	   WasEndedBy end1 = pFactory.newWasEndedBy(q("end1"),
					pFactory.newIDRef(q("a1")),
					pFactory.newIDRef(q("e1")));
	       end1.setTime(pFactory.newTimeNow());

	       WasEndedBy end2 = pFactory.newWasEndedBy(q("end1"),
			          pFactory.newIDRef(q("a1")),
			          pFactory.newIDRef(q("e1")));
	  	   end2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	

   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { end1, end2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-end1");
       }
       
       public void testScruffyEnd2() throws JAXBException  {
	setNamespaces();
    	   WasEndedBy end1 = pFactory.newWasEndedBy(q("end1"),
					pFactory.newIDRef(q("a1")),
					pFactory.newIDRef(q("e1")));
	       end1.setTime(pFactory.newTimeNow());
	   	   end1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));

	       WasEndedBy end2 = pFactory.newWasEndedBy(q("end1"),
			          pFactory.newIDRef(q("a1")),
			          pFactory.newIDRef(q("e1")));
	  	   end2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   end2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));

   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Statement [] opt=new Statement[] { e1, a1 };
		   Statement [] statements=new Statement[] { end1, end2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-end2");
       }
       
       public void testScruffyEnd3() throws JAXBException  {
	setNamespaces();
    	   WasEndedBy end1 = pFactory.newWasEndedBy(q("end1"),
					pFactory.newIDRef(q("a1")),
					pFactory.newIDRef(q("e1")));
	       end1.setTime(pFactory.newTimeNow());
	   	   end1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));
	   	   end1.setEnder(pFactory.newIDRef(q("a1s")));	

	       WasEndedBy end2 = pFactory.newWasEndedBy(q("end1"),
			          pFactory.newIDRef(q("a1")),
			          pFactory.newIDRef(q("e1")));
	  	   end2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   end2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));
		   end2.setEnder(pFactory.newIDRef(q("a2s")));
		   
   		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Activity a2=pFactory.newActivity(q("a2"));
		   Activity a2s=pFactory.newActivity(q("a2s"));
		   Statement [] opt=new Statement[] { e1, a1, a2, a2s };
		   Statement [] statements=new Statement[] { end1, end2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-end3");
       }
       
       public void testScruffyEnd4() throws JAXBException  {
	setNamespaces();
    	   WasEndedBy end1 = pFactory.newWasEndedBy(q("end1"),
					pFactory.newIDRef(q("a1")),
					pFactory.newIDRef(q("e1")));
	       end1.setTime(pFactory.newTimeNow());
	   	   end1.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hello", ValueConverter.QNAME_XSD_STRING));
	   	   end1.setEnder(pFactory.newIDRef(q("a1s")));	

	       WasEndedBy end2 = pFactory.newWasEndedBy(q("end1"),
			          pFactory.newIDRef(q("a2")),
			          pFactory.newIDRef(q("e2")));
	  	   end2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));	
		   end2.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "hi", ValueConverter.QNAME_XSD_STRING));
		   end2.setEnder(pFactory.newIDRef(q("a2s")));
		   
		   Entity e1=pFactory.newEntity(q("e1"));
		   Activity a1=pFactory.newActivity(q("a1"));
		   Activity a1s=pFactory.newActivity(q("a1s"));
		   Entity e2=pFactory.newEntity(q("e2"));
		   Activity a2=pFactory.newActivity(q("a2"));
		   Activity a2s=pFactory.newActivity(q("a2s"));
		   Statement [] opt=new Statement[] { e1, a1, a1s, e2, a2, a2s };
		   Statement [] statements=new Statement[] { end1, end2 };
    	   makeDocAndTest(statements, opt , "target/scruffy-end4");
       }
       
       public void testBundle1 () throws JAXBException {
	setNamespaces();
           Used use1 = pFactory.newUsed(q("use1"),
                                        pFactory.newIDRef(q("a1")),
                                        null,
                                        pFactory.newIDRef(q("e1")));
           Entity e1=pFactory.newEntity(q("e1"));
           Activity a1=pFactory.newActivity(q("a1"));
           List<Statement> st1=new LinkedList<Statement>(); 
           st1.add(a1);
           st1.add(e1);
           st1.add(use1);

           NamedBundle b1=pFactory.newNamedBundle(q("bundle1"), st1);
           
           Used use2 = pFactory.newUsed(q("use2"),
                                        pFactory.newIDRef(q("aa1")),
                                        null,
                                        pFactory.newIDRef(q("ee1")));
           Entity ee1=pFactory.newEntity(q("ee1"));
           Activity aa1=pFactory.newActivity(q("aa1"));
           List<Statement> st2=new LinkedList<Statement>(); 
           st2.add(aa1);
           st2.add(ee1);
           st2.add(use2);

           NamedBundle b2=pFactory.newNamedBundle(q("bundle2"), st2);
           
           Entity eb1=pFactory.newEntity(q("bundle1"));
           pFactory.addType(eb1, pFactory.newType(pFactory.newQName("prov:Bundle"), ValueConverter.QNAME_XSD_QNAME));
           
           Entity eb2=pFactory.newEntity(q("bundle2"));
           pFactory.addType(eb2, pFactory.newType(pFactory.newQName("prov:Bundle"),ValueConverter.QNAME_XSD_QNAME));

           Statement [] statements=new Statement[] { eb1, eb2,};
           NamedBundle [] bundles=new NamedBundle[] {  b1, b2 };

           makeDocAndTest(statements, bundles, "target/bundle1", null, true);

       }
       
       public void testBundle2 () throws JAXBException {
	setNamespaces();
           Used use1 = pFactory.newUsed(q("use1"),
                                        pFactory.newIDRef(q("a1")),
                                        null,
                                        pFactory.newIDRef(q("e1")));
           Entity e1=pFactory.newEntity(q("e1"));
           Activity a1=pFactory.newActivity(q("a1"));
           List<Statement> st1=new LinkedList<Statement>(); 
           st1.add(a1);
           st1.add(e1);
           st1.add(use1);

           NamedBundle b1=pFactory.newNamedBundle(q("bundle1"), st1);
           
           Used use2 = pFactory.newUsed(q("use2"),
                                        pFactory.newIDRef(q("e1")),
                                        null,
                                        pFactory.newIDRef(q("a1")));
           Entity ee1=pFactory.newEntity(q("a1"));
           Activity aa1=pFactory.newActivity(q("e1"));
           List<Statement> st2=new LinkedList<Statement>(); 
           st2.add(aa1);
           st2.add(ee1);
           st2.add(use2);

           NamedBundle b2=pFactory.newNamedBundle(q("bundle2"), st2);
           
           Entity eb1=pFactory.newEntity(q("bundle1"));
           pFactory.addType(eb1, pFactory.newType(pFactory.newQName("prov:Bundle"),ValueConverter.QNAME_XSD_QNAME));
           
           Entity eb2=pFactory.newEntity(q("bundle2"));
           pFactory.addType(eb2, pFactory.newType(pFactory.newQName("prov:Bundle"),ValueConverter.QNAME_XSD_QNAME));

           Statement [] statements=new Statement[] { eb1, eb2,};
           NamedBundle [] bundles=new NamedBundle[] {  b1, b2 };

           makeDocAndTest(statements, bundles, "target/bundle2", null, true);

       }

   	public void testDictionaryInsertion1() throws JAXBException {
		DerivedByInsertionFrom d1 = pFactory.newDerivedByInsertionFrom(null,
				q("d2"), q("d1"), null, null);

		Statement[] statements = new Statement[]{d1};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryInsertion1");

	}

	public void testDictionaryInsertion2() throws JAXBException {
		DerivedByInsertionFrom d2 = pFactory.newDerivedByInsertionFrom(
				q("deriv"), q("d2"), q("d1"), null, null);

		Statement[] statements = new Statement[]{d2};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryInsertion2");

	}

	public void testDictionaryInsertion3() throws JAXBException {
		List<KeyQNamePair> ll = new LinkedList<KeyQNamePair>();
		KeyQNamePair p = new KeyQNamePair();
		p.key = "a";
		p.name = q("e0");
		ll.add(p);
		DerivedByInsertionFrom d3 = pFactory.newDerivedByInsertionFrom(
				q("deriv3"), q("d2"), q("d1"), ll, null);

		Statement[] statements = new Statement[]{d3};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryInsertion3");

	}

	public void testDictionaryInsertion4() throws JAXBException {
		List<KeyQNamePair> ll = new LinkedList<KeyQNamePair>();
		KeyQNamePair p1 = new KeyQNamePair();
		p1.key = "a";
		p1.name = q("e0");
		ll.add(p1);
		KeyQNamePair p2 = new KeyQNamePair();
		p2.key = 1;
		p2.name = q("e1");
		ll.add(p2);
		DerivedByInsertionFrom d4 = pFactory.newDerivedByInsertionFrom(
				q("deriv4"), q("d2"), q("d1"), ll, null);

		Statement[] statements = new Statement[]{d4};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryInsertion4");
	}

	public void testDictionaryInsertion5() throws JAXBException {
		List<KeyQNamePair> ll = new LinkedList<KeyQNamePair>();
		KeyQNamePair p1 = new KeyQNamePair();
		p1.key = "a";
		p1.name = q("e0");
		ll.add(p1);
		KeyQNamePair p2 = new KeyQNamePair();
		p2.key = 1;
		p2.name = q("e1");
		ll.add(p2);
		KeyQNamePair p3 = new KeyQNamePair();
		p3.key = q("a");
		p3.name = q("e2");
		ll.add(p3);
		DerivedByInsertionFrom d5 = pFactory.newDerivedByInsertionFrom(
				q("deriv5"), q("d2"), q("d1"), ll, null);
		addFurtherAttributes(d5);

		Statement[] statements = new Statement[]{d5};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryInsertion5");

	}
	
	public void testDictionaryInsertion6() throws JAXBException {
		List<KeyQNamePair> ll = new LinkedList<KeyQNamePair>();
		KeyQNamePair p1 = new KeyQNamePair();
		p1.key = "a";
		p1.name = q("e0");
		ll.add(p1);
		KeyQNamePair p2 = new KeyQNamePair();
		p2.key = 1;
		p2.name = q("e1");
		ll.add(p2);
		KeyQNamePair p3 = new KeyQNamePair();
		p3.key = q("a");
		p3.name = q("e2");
		ll.add(p3);
		DerivedByInsertionFrom d5 = pFactory.newDerivedByInsertionFrom(
				null, q("d2"), q("d1"), ll, null);

		Statement[] statements = new Statement[]{d5};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryInsertion6");

	}
	
 	public void testDictionaryRemoval1() throws JAXBException {
		DerivedByRemovalFrom d1 = pFactory.newDerivedByRemovalFrom(null,
				q("d2"), q("d1"), null, null);

		Statement[] statements = new Statement[]{d1};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryRemoval1");

	}

	public void testDictionaryRemoval2() throws JAXBException {
		DerivedByRemovalFrom d2 = pFactory.newDerivedByRemovalFrom(
				q("removal"), q("d2"), q("d1"), null, null);

		Statement[] statements = new Statement[]{d2};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryRemoval2");

	}

	public void testDictionaryRemoval3() throws JAXBException {
		List<Object> ll = new LinkedList<Object>();
		ll.add("a");
		DerivedByRemovalFrom d3 = pFactory.newDerivedByRemovalFrom(
				q("removal3"), q("d2"), q("d1"), ll, null);

		Statement[] statements = new Statement[]{d3};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryRemoval3");

	}

	public void testDictionaryRemoval4() throws JAXBException {
		List<Object> ll = new LinkedList<Object>();
		ll.add("a");
		ll.add(1);
		DerivedByRemovalFrom d4 = pFactory.newDerivedByRemovalFrom(
				q("removal4"), q("d2"), q("d1"), ll, null);

		Statement[] statements = new Statement[]{d4};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryRemoval4");
	}

	public void testDictionaryRemoval5() throws JAXBException {
		List<Object> ll = new LinkedList<Object>();
		ll.add("a");
		ll.add(1);
		ll.add(q("a"));
		DerivedByRemovalFrom d5 = pFactory.newDerivedByRemovalFrom(
				q("removal5"), q("d2"), q("d1"), ll, null);
		addFurtherAttributes(d5);

		Statement[] statements = new Statement[]{d5};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryRemoval5");

	}
	
   	public void NOtestDictionaryMembership1() throws JAXBException { // this makes no sense, member is mandatory.
		DictionaryMembership mem = pFactory.newDictionaryMembership(q("d"), null);

		Statement[] statements = new Statement[]{mem};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryMembership1");
	}
	
   	
   	
	public void testDictionaryMembership2() throws JAXBException {
		List<KeyQNamePair> ll = new LinkedList<KeyQNamePair>();
		KeyQNamePair p1 = new KeyQNamePair();
		p1.key = "a";
		p1.name = q("e0");
		ll.add(p1);
		DictionaryMembership d5 = pFactory.newDictionaryMembership(q("d"),  ll);

		Statement[] statements = new Statement[]{d5};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryMembership2");

	}

	public void testDictionaryMembership3() throws JAXBException {
		List<KeyQNamePair> ll = new LinkedList<KeyQNamePair>();
		KeyQNamePair p1 = new KeyQNamePair();
		p1.key = "a";
		p1.name = q("e0");
		ll.add(p1);
		KeyQNamePair p2 = new KeyQNamePair();
		p2.key = 1;
		p2.name = q("e1");
		ll.add(p2);
		DictionaryMembership d5 = pFactory.newDictionaryMembership(q("d"),  ll);

		Statement[] statements = new Statement[]{d5};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryMembership3");

	}

	public void testDictionaryMembership4() throws JAXBException {
		List<KeyQNamePair> ll = new LinkedList<KeyQNamePair>();
		KeyQNamePair p1 = new KeyQNamePair();
		p1.key = "a";
		p1.name = q("e0");
		ll.add(p1);
		KeyQNamePair p2 = new KeyQNamePair();
		p2.key = 1;
		p2.name = q("e1");
		ll.add(p2);
		KeyQNamePair p3 = new KeyQNamePair();
		p3.key = q("a");
		p3.name = q("e2");
		ll.add(p3);
		DictionaryMembership d5 = pFactory.newDictionaryMembership(q("d"),  ll);

		Statement[] statements = new Statement[]{d5};
		Statement[] opt = new Statement[]{};
		makeDocAndTest(statements, opt, "target/dictionaryMembership4");

	}


}
