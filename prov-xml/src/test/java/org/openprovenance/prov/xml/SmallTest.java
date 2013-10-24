package org.openprovenance.prov.xml;

import java.io.File;

import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.HasExtensibility;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasLocation;
import org.openprovenance.prov.model.HasOtherAttribute;
import org.openprovenance.prov.model.HasRole;
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
import javax.xml.parsers.DocumentBuilder;

import org.openprovenance.prov.model.KeyQNamePair;
import org.openprovenance.prov.model.URIWrapper;

import junit.framework.TestCase;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class SmallTest extends TestCase {

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
    public SmallTest(String testName) {
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
	updateNamespaces(doc3);
	writeDocument(doc3, file + "-2");
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
   

    public void addTypes_old(HasType ht) {
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

    public void addLocations_old(HasLocation hl) {
   	hl.getLocation().add(pFactory.newLocation("London",vconv));
   	hl.getLocation().add(pFactory.newLocation(1,vconv));
   	hl.getLocation().add(pFactory.newLocation(1.0,vconv));
   	hl.getLocation().add(pFactory.newLocation(true,vconv));
	//   	hl.getLocation().add(pFactory.newLocation(new QName(EX_NS, "london", EX_PREFIX),vconv));
   	hl.getLocation().add(pFactory.newLocation(pFactory.newTimeNow(),vconv));
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"london"));
   	hl.getLocation().add(pFactory.newLocation(w,vconv));
   	hl.getLocation().add(pFactory.newLocation(pFactory.newGYear(2002),vconv));
    }
    
    
    public void addValue(HasValue hl) {
        hl.setValue(pFactory.newValue(new QName(EX_NS, "avalue", EX_PREFIX),
				      ValueConverter.QNAME_XSD_QNAME));
    }

    public void addFurtherAttributes(HasExtensibility he) {
	if (true) return;
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag1",EX_PREFIX,"hello", vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "bye", vconv));
	//he.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, pFactory.newInternationalizedString("bonjour","fr"), "xsd:string"));
	he.getAny().add(pFactory.newAttribute(EX2_NS,"tag3",EX2_PREFIX, "hi", vconv));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag1",EX_PREFIX,"hello\nover\nmore\nlines", vconv));

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
    
    DOMProcessing dom=new DOMProcessing();
    
    org.w3c.dom.Element createXMLLiteral() {
        DocumentBuilder builder=dom.builder;
        org.w3c.dom.Document doc=builder.newDocument();
        org.w3c.dom.Element el1=doc.createElementNS("http://app/","ap:aaa");
        org.w3c.dom.Element el2=doc.createElementNS("http://app/","ap:bbb");
        org.w3c.dom.Element el3=doc.createElementNS("http://app/","ap:ccc");
        el2.appendChild(el3);
        el1.appendChild(el2);
        return el1;        
    }
    public Object[][] attributeValues_small =
        {
	 {"un lieu",ValueConverter.QNAME_XSD_STRING},
	 
         {pFactory.newInternationalizedString("un lieu","fr"),ValueConverter.QNAME_XSD_STRING},

         {pFactory.newInternationalizedString("a place","EN"),ValueConverter.QNAME_XSD_STRING}
        };
  
    public Object[][] attributeValues =
        {
	 {"un lieu",ValueConverter.QNAME_XSD_STRING},
	 
         {pFactory.newInternationalizedString("un lieu","fr"),ValueConverter.QNAME_XSD_STRING},

         {pFactory.newInternationalizedString("a place","EN"),ValueConverter.QNAME_XSD_STRING},
           
         {1,ValueConverter.QNAME_XSD_INT},

         {1,ValueConverter.QNAME_XSD_LONG},

         {1,ValueConverter.QNAME_XSD_SHORT},

         {2.0,ValueConverter.QNAME_XSD_DOUBLE},

         {1.0,ValueConverter.QNAME_XSD_FLOAT},

         {10,ValueConverter.QNAME_XSD_DECIMAL},

         {true,ValueConverter.QNAME_XSD_BOOLEAN},

         {false,ValueConverter.QNAME_XSD_BOOLEAN},

  //    FIXME   {"yes",ValueConverter.QNAME_XSD_BOOLEAN},

  //   FIXME    {"no",ValueConverter.QNAME_XSD_BOOLEAN},
         
         {10,ValueConverter.QNAME_XSD_BYTE},

         {10,ValueConverter.QNAME_XSD_UNSIGNED_INT},

         {10,ValueConverter.QNAME_XSD_UNSIGNED_LONG},

         {10,ValueConverter.QNAME_XSD_INTEGER},

         {10,ValueConverter.QNAME_XSD_UNSIGNED_SHORT},

         {10,ValueConverter.QNAME_XSD_NON_NEGATIVE_INTEGER},

         {-10,ValueConverter.QNAME_XSD_NON_POSITIVE_INTEGER},

         {10,ValueConverter.QNAME_XSD_POSITIVE_INTEGER},

         {10,ValueConverter.QNAME_XSD_UNSIGNED_BYTE},

         {"http://example.org",ValueConverter.QNAME_XSD_ANY_URI},


         // Consider following cases for QNames
         // - declared namespace, with declared prefix
         // - declared namespace, with other prefix
         // - declared namespace, as default namespace

         // - undeclared namespace, with declared prefix
         // - undeclared namespace, with other prefix
         // - undeclared namespace, as default namespace

         {new QName(EX_NS, "abc", EX_PREFIX), ValueConverter.QNAME_XSD_QNAME},
         
         {new QName(EX_NS, "abc", "other"), ValueConverter.QNAME_XSD_QNAME},
         
         {new QName(EX_NS, "abc"), ValueConverter.QNAME_XSD_QNAME},
         
         {new QName("http://example4.org/", "abc", EX_PREFIX), ValueConverter.QNAME_XSD_QNAME},
         
         {new QName("http://example4.org/", "abc", "other"), ValueConverter.QNAME_XSD_QNAME},
             
         {new QName("http://example4.org/", "abc"), ValueConverter.QNAME_XSD_QNAME},
         
         
         {pFactory.newTimeNow(),ValueConverter.QNAME_XSD_DATETIME},

         {pFactory.newYear(2013),ValueConverter.QNAME_XSD_GYEAR},

         {pFactory.newGMonth(01),ValueConverter.QNAME_XSD_GMONTH},

         {pFactory.newGDay(30),ValueConverter.QNAME_XSD_GDAY},
         
         {pFactory.newGMonthDay(12,25),ValueConverter.QNAME_XSD_GMONTH_DAY},

         
         {pFactory.newDuration(12225),ValueConverter.QNAME_XSD_DURATION},

         {pFactory.newDuration(0),ValueConverter.QNAME_XSD_YEAR_MONTH_DURATION},
         {pFactory.newDuration("P2147483647DT2147483647H2147483647M123456789012345.123456789012345S"),ValueConverter.QNAME_XSD_DAY_TIME_DURATION},

         { new byte[] {0,1,2,34,5,6}, ValueConverter.QNAME_XSD_HEX_BINARY},
         { new byte[] {0,1,2,34,5,6}, ValueConverter.QNAME_XSD_BASE64_BINARY},
         { new byte[1023], ValueConverter.QNAME_XSD_BASE64_BINARY},
         
         {"EN",ValueConverter.QNAME_XSD_LANGUAGE},
         {"normal",ValueConverter.QNAME_XSD_NORMALIZED_STRING},
         {"TOK",ValueConverter.QNAME_XSD_TOKEN},
         {"NMTOK",ValueConverter.QNAME_XSD_NMTOKEN},
         {"name",ValueConverter.QNAME_XSD_NAME},
         {"NCName",ValueConverter.QNAME_XSD_NCNAME},
         
         {createXMLLiteral(),ValueConverter.QNAME_RDF_LITERAL}
         
        };
    
    public void addLocations(HasLocation hl){
        for (Object [] pair: attributeValues) {
            Object value=pair[0];
            QName type=(QName) pair[1];
            hl.getLocation().add(pFactory.newLocation(value,type));
         }

    }
    public void addTypes(HasType hl){
        for (Object [] pair: attributeValues) {
            Object value=pair[0];
            QName type=(QName) pair[1];
            hl.getType().add(pFactory.newType(value,type));
         }

    }
    public void addRoles(HasRole hl){
        for (Object [] pair: attributeValues) {
            Object value=pair[0];
            QName type=(QName) pair[1];
            hl.getRole().add(pFactory.newRole(value,type));
         }

    }
    public void addOthers(HasOtherAttribute ho, QName elementName) {
	for (Object [] pair: attributeValues) {
	    Object value=pair[0];
	    QName type=(QName) pair[1];
	    if (value instanceof QName) {
		QName qq=(QName)value;
		if ((qq.getPrefix().equals(elementName.getPrefix()))
			&&
			(!(qq.getNamespaceURI().equals(elementName.getNamespaceURI())))) {
		    // ignore this case
		} else {
		    ho.getOthers().add(pFactory.newOther(elementName, value, type));
		}			
	    } else {
		ho.getOthers().add(pFactory.newOther(elementName, value, type));
	    }
	}
    }

    public void testEntity0() throws JAXBException  {
	setNamespaces();
	Entity a = pFactory.newEntity("ex:e0");
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);
	addLocations(a);

	a.setValue(pFactory.newValue(10,ValueConverter.QNAME_XSD_BYTE));
	a.setValue(pFactory.newValue("10",ValueConverter.QNAME_XSD_STRING));


	makeDocAndTest(a,"target/attr_entity0");
    }
    
    

    public void testActivity0() throws JAXBException  {
	setNamespaces();
	Activity a = pFactory.newActivity("ex:a0");
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);
	addLocations(a);

	makeDocAndTest(a,"target/attr_activity0");
    }

    

    public void testAgent0() throws JAXBException  {
	setNamespaces();
	Agent a = pFactory.newAgent("ex:ag0");
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);
	addLocations(a);

	makeDocAndTest(a,"target/attr_agent0");
    }

    public QName q(String n) {
	return new QName(EX_NS, n, EX_PREFIX);
    }
    
    public void testGeneration0() throws JAXBException  {
 	setNamespaces();

	WasGeneratedBy a = pFactory.newWasGeneratedBy((QName)null,
  							pFactory.newIDRef(q("e1")),
  							null,
  							pFactory.newIDRef(q("a1")));
 	
 	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
 	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
 	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
 	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

 	addLabels(a);
 	addRoles(a);
 	addTypes(a);	
 	addLocations(a);
 	

 	makeDocAndTest(a,"target/attr_generation0");
     }

    public void testInvalidation0() throws JAXBException  {
  	setNamespaces();

 	WasInvalidatedBy a = pFactory.newWasInvalidatedBy((QName)null,
   							pFactory.newIDRef(q("e1")),
   							pFactory.newIDRef(q("a1")));
  	
  	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
  	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
  	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
  	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

  	addLabels(a);
  	addRoles(a);
  	addTypes(a);	
  	addLocations(a);
  	

  	makeDocAndTest(a,"target/attr_invalidation0");
      }

    public void testUsage0() throws JAXBException  {
  	setNamespaces();

 	Used a = pFactory.newUsed((QName)null,
 	                          pFactory.newIDRef(q("a1")),
 	                          null,
 	                          pFactory.newIDRef(q("e1")));
  	
  	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
  	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
  	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
  	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

  	addLabels(a);
  	addRoles(a);
  	addTypes(a);	
  	addLocations(a);
  	

  	makeDocAndTest(a,"target/attr_usage0");
      }
    
   public void testAssociation0() throws JAXBException  {
 	setNamespaces();


	WasAssociatedWith a = pFactory.newWasAssociatedWith(q("assoc0"), 
	                                                    pFactory.newIDRef(q("a1")),
	                                                    pFactory.newIDRef(q("ag1")));
 	
 	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
 	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
 	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
 	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

 	addLabels(a);
 	addRoles(a);
 	addTypes(a);	
 	

 	makeDocAndTest(a,"target/attr_association0");
     }
     
   
  public void testAttribution0() throws JAXBException  {
	setNamespaces();


	WasAttributedTo a = pFactory.newWasAttributedTo(q("assoc0"), 
	                                                    pFactory.newIDRef(q("e1")),
	                                                    pFactory.newIDRef(q("ag1")));
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);	
	

	makeDocAndTest(a,"target/attr_attribution0");
    }

  public void testDerivation0() throws JAXBException  {
 	setNamespaces();


 	WasDerivedFrom a = pFactory.newWasDerivedFrom(q("der0"), 
 	                                              pFactory.newIDRef(q("e2")),
 	                                              pFactory.newIDRef(q("e1")));
 	
 	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
 	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
 	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
 	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

 	addLabels(a);
 	addTypes(a);	
 	

 	makeDocAndTest(a,"target/attr_derivation0");
  }
  
  public void testEnd0() throws JAXBException  {
	setNamespaces();

	WasEndedBy a = pFactory.newWasEndedBy((QName)null,
							pFactory.newIDRef(q("a1")),
							pFactory.newIDRef(q("e1")));
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addRoles(a);
	addTypes(a);	
	addLocations(a);
	

	makeDocAndTest(a,"target/attr_end0");
   }

  public void testStart0() throws JAXBException  {
	setNamespaces();

	WasStartedBy a = pFactory.newWasStartedBy((QName)null,
	                                          pFactory.newIDRef(q("a1")),
	                                          pFactory.newIDRef(q("e1")));
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addRoles(a);
	addTypes(a);	
	addLocations(a);
	

	makeDocAndTest(a,"target/attr_start0");
   }
  
  public void testInfluence0() throws JAXBException  {
 	setNamespaces();


 	WasInfluencedBy a = pFactory.newWasInfluencedBy(q("infl0"), 
 	                                                pFactory.newIDRef(q("e1")),
 	                                                pFactory.newIDRef(q("e2")));
 	
 	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
 	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
 	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
 	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

 	addLabels(a);
 	addTypes(a);	
 	

 	makeDocAndTest(a,"target/attr_influence0");
     }

   
  public void testCommunication0() throws JAXBException  {
	setNamespaces();


	WasInformedBy a = pFactory.newWasInformedBy(q("com0"), 
	                                            pFactory.newIDRef(q("a1")),
	                                            pFactory.newIDRef(q("a2")));
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);	
	

	makeDocAndTest(a,"target/attr_communication0");
   }

 
  public void testDelegation0() throws JAXBException  {
	setNamespaces();


	ActedOnBehalfOf a = pFactory.newActedOnBehalfOf(q("del0"), 
	                                                pFactory.newIDRef(q("a1")),
	                                                pFactory.newIDRef(q("a2")),
	                                                pFactory.newIDRef(q("a3")));
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);	
	

	makeDocAndTest(a,"target/attr_delegation0");
   }

 

}
