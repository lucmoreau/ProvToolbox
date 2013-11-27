package org.openprovenance.prov.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasLocation;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.HasRole;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;
import org.xml.sax.SAXException;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class AttributeTest extends TestCase {

 
    public static final String EX_NS = "http://example.org/";
    public static final String EX2_NS = "http://example2.org/";
    public static final String EX_PREFIX = "ex";
    public static final String EX2_PREFIX = "ex2";
    public static final String EX3_NS = "http://example3.org/";

    
    static final ProvUtilities util=new ProvUtilities();



    public static org.openprovenance.prov.model.ProvFactory pFactory;


    static {
	pFactory = new ProvFactory();
    }
	private DocumentEquality documentEquality;

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AttributeTest(String testName) {
	super(testName);
	this.documentEquality = new DocumentEquality(mergeDuplicateProperties());
    }

    public boolean urlFlag = true;

    /**
     * @return the suite of tests being tested
     */

  
    
    public void updateNamespaces(Document doc) {
	Namespace ns=Namespace.gatherNamespaces(doc);
	doc.setNamespace(ns);
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
        if (check) conditionalCheckSchema(file);

        Document doc3=readDocument(file);
        compareDocuments(doc, doc3, check && checkTest(file));
	updateNamespaces(doc3);
	writeDocument(doc3, file + "-2");
    }

    
    public void conditionalCheckSchema(String file) {
	if (checkSchema(file)) doCheckSchema1(file);
    }
    
    
    public boolean checkSchema(String name)  
    {  // disable schema checking for the following because it relies on xsd1.1
	if(name.endsWith("33"+extension())
		|| name.endsWith("33"+extension())
		|| name.endsWith("34"+extension())
		|| 
		name.endsWith("attr_delegation0"+extension())
		|| name.endsWith("attr_end0"+extension())
		|| name.endsWith("attr_attribution0"+extension())
		|| name.endsWith("attr_generation0"+extension())
		|| name.endsWith("attr_derivation0"+extension())
		|| name.endsWith("attr_activity0"+extension())
		|| name.endsWith("attr_influence0"+extension())
		|| name.endsWith("attr_invalidation0"+extension())
		|| name.endsWith("attr_agent0"+extension())
		|| name.endsWith("attr_start0"+extension())
		|| name.endsWith("attr_usage0"+extension())
		|| name.endsWith("attr_association0"+extension())
		|| name.endsWith("attr_communication0"+extension())
		|| name.endsWith("attr_entity0"+extension())
		)
	{
	    return false;
	}
	
	return true;
    }

    
    public void doCheckSchema1(String file) {
	
	String[] schemaFiles = new String[1];
	schemaFiles[0] = "src/main/resources/ex.xsd";
	try {
	    ProvDeserialiser.getThreadProvDeserialiser().validateDocumentNew(schemaFiles, new File(file));
	    assertTrue(true);
	} catch (JAXBException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    assertTrue(false);
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    assertTrue(false);

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    assertTrue(false);

	}
    }

    
    public void doCheckSchema2(String file) {
	//String command="xmllint --schema src/main/resources/w3c/prov.xsd --schema src/main/resources/w3c/xml.xsd --schema src/main/resources/ex.xsd " +file; //--noout
	String command="xmllint --schema src/main/resources/ex.xsd " +file; //--noout
	try {
	    Process proc=Runtime.getRuntime().exec(command);
	    proc.waitFor();
	    int code=proc.exitValue();
	    if (code!=0) {
		BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		String s_error=errorReader.readLine();
		if (s_error!=null) {
		    System.out.println("Error:  " + s_error);
		}
		BufferedReader outReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String s_out=outReader.readLine();
		if (s_out!=null) {
		    System.out.println("Out:  " + s_out);
		}
	    }
	    //System.out.println("out " + proc.getOutputStream().toString());
	    //System.err.println("err " + proc.getErrorStream().toString());
	    assertTrue(code==0);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public Document readDocument(String file1) {
        try {
            return readXMLDocument(file1);
        } catch (JAXBException e) {
            throw new UncheckedTestException(e);
        }
    }

    public void writeDocument(Document doc, String file2) {
	Namespace.withThreadNamespace(doc.getNamespace());
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
	// sw = new StringWriter();
	//serial.serialiseDocument(sw, doc, true);
	//System.out.println(sw.toString());
    }

    ///////////////////////////////////////////////////////////////////////


    public void addLabel(HasLabel hl) {
        hl.getLabel().add(pFactory.newInternationalizedString("hello"));
    }

    public void addLabels(HasLabel hl) {
   	hl.getLabel().add(pFactory.newInternationalizedString("hello"));
   	hl.getLabel().add(pFactory.newInternationalizedString("bye","en"));
   	hl.getLabel().add(pFactory.newInternationalizedString("bonjour","fr"));
    }
   


   
  

    public boolean test=true;
    
    DOMProcessing dom=new DOMProcessing();
    
    org.w3c.dom.Element createXMLLiteral() {
        DocumentBuilder builder=DOMProcessing.builder;
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
	 {"un lieu",Name.QNAME_XSD_STRING},
	 
         {pFactory.newInternationalizedString("un lieu","fr"),Name.QNAME_XSD_STRING},

         {pFactory.newInternationalizedString("a place","en"),Name.QNAME_XSD_STRING}
        };


    public Object[][] attributeValues_long =
        {
	 {"un lieu",Name.QNAME_XSD_STRING},
	 
         {pFactory.newInternationalizedString("un lieu","fr"),Name.QNAME_PROV_INTERNATIONALIZED_STRING},

         {pFactory.newInternationalizedString("a place","en"),Name.QNAME_PROV_INTERNATIONALIZED_STRING},
           
         {1,Name.QNAME_XSD_INT},

         {1,Name.QNAME_XSD_LONG},

         {1,Name.QNAME_XSD_SHORT},

         {2.0,Name.QNAME_XSD_DOUBLE},

         {1.0,Name.QNAME_XSD_FLOAT},

         {10,Name.QNAME_XSD_DECIMAL},

         {true,Name.QNAME_XSD_BOOLEAN},

         {false,Name.QNAME_XSD_BOOLEAN},

  //    FIXME   {"yes",Name.QNAME_XSD_BOOLEAN},

  //   FIXME    {"no",Name.QNAME_XSD_BOOLEAN},
         
         {10,Name.QNAME_XSD_BYTE},

         {10,Name.QNAME_XSD_UNSIGNED_INT},

         {10,Name.QNAME_XSD_UNSIGNED_LONG},

         {10,Name.QNAME_XSD_INTEGER},

         {10,Name.QNAME_XSD_UNSIGNED_SHORT},

         {10,Name.QNAME_XSD_NON_NEGATIVE_INTEGER},

         {-10,Name.QNAME_XSD_NON_POSITIVE_INTEGER},

         {10,Name.QNAME_XSD_POSITIVE_INTEGER},

         {10,Name.QNAME_XSD_UNSIGNED_BYTE},

         {"http://example.org",Name.QNAME_XSD_ANY_URI},


         // Consider following cases for QNames
         // - declared namespace, with declared prefix
         // - declared namespace, with other prefix
         // - declared namespace, as default namespace

         // - undeclared namespace, with declared prefix
         // - undeclared namespace, with other prefix
         // - undeclared namespace, as default namespace

         {new QName(EX_NS, "abc", EX_PREFIX), Name.QNAME_XSD_QNAME},
         
         {new QName(EX_NS, "abcd", "other"), Name.QNAME_XSD_QNAME},
         
         {new QName(EX_NS, "abcde"), Name.QNAME_XSD_QNAME},
         
         {new QName("http://example4.org/", "zabc", EX_PREFIX), Name.QNAME_XSD_QNAME},
         
         {new QName("http://example4.org/", "zabcd", "other"), Name.QNAME_XSD_QNAME},
             
         {new QName("http://example4.org/", "zabcde"), Name.QNAME_XSD_QNAME},
         
         
         {pFactory.newTimeNow(),Name.QNAME_XSD_DATETIME},

         {pFactory.newYear(2013),Name.QNAME_XSD_GYEAR},

         {pFactory.newGMonth(01),Name.QNAME_XSD_GMONTH},

         {pFactory.newGDay(30),Name.QNAME_XSD_GDAY},
         
         {pFactory.newGMonthDay(12,25),Name.QNAME_XSD_GMONTH_DAY},

         
         {pFactory.newDuration(12225),Name.QNAME_XSD_DURATION},

         {pFactory.newDuration("P2Y6M"),Name.QNAME_XSD_YEAR_MONTH_DURATION},
         {pFactory.newDuration("P2147483647DT2147483647H2147483647M123456789012345.123456789012345S"),Name.QNAME_XSD_DAY_TIME_DURATION},

         { new byte[] {0,1,2,34,5,6}, Name.QNAME_XSD_HEX_BINARY},
         { new byte[] {0,1,2,34,5,6}, Name.QNAME_XSD_BASE64_BINARY},
         { new byte[1023], Name.QNAME_XSD_BASE64_BINARY},
         
         {"en",Name.QNAME_XSD_LANGUAGE},
         {"normal",Name.QNAME_XSD_NORMALIZED_STRING},
         {"TOK",Name.QNAME_XSD_TOKEN},
         {"NMTOK",Name.QNAME_XSD_NMTOKEN},
         {"name",Name.QNAME_XSD_NAME},
         {"NCName",Name.QNAME_XSD_NCNAME},
         
         {createXMLLiteral(),Name.QNAME_RDF_LITERAL}
         
        };
    
   
    
    public Object[][] attributeValues =attributeValues_long;

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
    public void addOthers(HasOther ho, QName elementName) {
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
		    ho.getOther().add(pFactory.newOther(elementName, value, type));
		}			
	    } else {
		ho.getOther().add(pFactory.newOther(elementName, value, type));
	    }
	}
    }

    public void testEntityWithOneTypeAttribute(int i) throws JAXBException  {
 	Entity a = pFactory.newEntity(qq("et" + i));
 	

 	Object [] pair= attributeValues[i];
 	Object value=pair[0];
 	QName type=(QName) pair[1];
 	a.getType().add(pFactory.newType(value,type));
 	makeDocAndTest(a,"target/attr_entity_one_attr"+i);
     }
    

    public void testEntityWithOneValueAttribute(int i) throws JAXBException  {
 	Entity a = pFactory.newEntity(qq("en_v" + i));
 	

 	Object [] pair= attributeValues[i];
 	Object value=pair[0];
 	QName type=(QName) pair[1];
 	a.setValue(pFactory.newValue(value,type));
 	makeDocAndTest(a,"target/attr_entity_one_value_attr"+i);
     }

    public void testAssociationWithOneRoleAttribute(int i) throws JAXBException  {
 	WasAssociatedWith a = pFactory.newWasAssociatedWith(qq("ass_r" + i),
 	                                                    pFactory.newIDRef(q("a1")),
 	                                                    pFactory.newIDRef(q("ag1")));

 	Object [] pair= attributeValues[i];
 	Object value=pair[0];
 	QName type=(QName) pair[1];
 	a.getRole().add(pFactory.newRole(value,type));
 	makeDocAndTest(a,"target/attr_association_one_role_attr"+i);
     }

    public void testEntityWithOneLocationAttribute(int i) throws JAXBException  {
  	Entity a = pFactory.newEntity(qq("en_l" + i));
  	

  	Object [] pair= attributeValues[i];
  	Object value=pair[0];
  	QName type=(QName) pair[1];
  	a.getLocation().add(pFactory.newLocation(value,type));
  	makeDocAndTest(a,"target/attr_entity_one_location_attr"+i);
      }

    public void testEntityWithOneOtherAttribute(int i) throws JAXBException  {
 	Entity a = pFactory.newEntity(qq("en_o" + i));
 	

 	Object [] pair= attributeValues[i];
 	Object value=pair[0];
 	QName type=(QName) pair[1];
 	a.getOther().add(pFactory.newOther(new QName(EX_NS,  "tag2", "exo"), value,type));
 	makeDocAndTest(a,"target/attr_entity_one_other_attr"+i);
     }
    
    // VALUE
    public void testEntityWithOneValueAttribute0 () throws JAXBException  {
	testEntityWithOneValueAttribute(0);
    }
    public void testEntityWithOneValueAttribute1 () throws JAXBException  {
	testEntityWithOneValueAttribute(1);
    }
    public void testEntityWithOneValueAttribute2 () throws JAXBException  {
	testEntityWithOneValueAttribute(2);
    }
    public void testEntityWithOneValueAttribute3 () throws JAXBException  {
	testEntityWithOneValueAttribute(3);
    }
    public void testEntityWithOneValueAttribute4 () throws JAXBException  {
	testEntityWithOneValueAttribute(4);
    }
    public void testEntityWithOneValueAttribute5 () throws JAXBException  {
	testEntityWithOneValueAttribute(5);
    }
    public void testEntityWithOneValueAttribute6 () throws JAXBException  {
	testEntityWithOneValueAttribute(6);
    }
    public void testEntityWithOneValueAttribute7 () throws JAXBException  {
	testEntityWithOneValueAttribute(7);
    }
    public void testEntityWithOneValueAttribute8 () throws JAXBException  {
	testEntityWithOneValueAttribute(8);
    }
    public void testEntityWithOneValueAttribute9 () throws JAXBException  {
	testEntityWithOneValueAttribute(9);
    }
    public void testEntityWithOneValueAttribute10 () throws JAXBException  {
	testEntityWithOneValueAttribute(10);
    }
    public void testEntityWithOneValueAttribute11 () throws JAXBException  {
	testEntityWithOneValueAttribute(11);
    }
    public void testEntityWithOneValueAttribute12 () throws JAXBException  {
	testEntityWithOneValueAttribute(12);
    }
    public void testEntityWithOneValueAttribute13 () throws JAXBException  {
	testEntityWithOneValueAttribute(13);
    }
    public void testEntityWithOneValueAttribute14 () throws JAXBException  {
	testEntityWithOneValueAttribute(14);
    }
    public void testEntityWithOneValueAttribut15 () throws JAXBException  {
	testEntityWithOneValueAttribute(15);
    }
    public void testEntityWithOneValueAttribute16 () throws JAXBException  {
	testEntityWithOneValueAttribute(16);
    }
    public void testEntityWithOneValueAttribute17 () throws JAXBException  {
	testEntityWithOneValueAttribute(17);
    }
    public void testEntityWithOneValueAttribute18 () throws JAXBException  {
	testEntityWithOneValueAttribute(18);
    }
    public void testEntityWithOneValueAttribute19 () throws JAXBException  {
	testEntityWithOneValueAttribute(19);
    }
    public void testEntityWithOneValueAttribute20 () throws JAXBException  {
	testEntityWithOneValueAttribute(20);
    }
    public void testEntityWithOneValueAttribute21 () throws JAXBException  {
	testEntityWithOneValueAttribute(21);
    }
    public void testEntityWithOneValueAttribute22 () throws JAXBException  {
	testEntityWithOneValueAttribute(22);
    }
    public void testEntityWithOneValueAttribute23 () throws JAXBException  {
	testEntityWithOneValueAttribute(23);
    }
    public void testEntityWithOneValueAttribute24 () throws JAXBException  {
	testEntityWithOneValueAttribute(24);
    }
    public void testEntityWithOneValueAttribute25 () throws JAXBException  {
	testEntityWithOneValueAttribute(25);
    }
    public void testEntityWithOneValueAttribute26 () throws JAXBException  {
	testEntityWithOneValueAttribute(26);
    }
    public void testEntityWithOneValueAttribute27 () throws JAXBException  {
	testEntityWithOneValueAttribute(27);
    }
    public void testEntityWithOneValueAttribute28 () throws JAXBException  {
	testEntityWithOneValueAttribute(28);
    }
    public void testEntityWithOneValueAttribute29 () throws JAXBException  {
	testEntityWithOneValueAttribute(29);
    }
    public void testEntityWithOneValueAttribute30 () throws JAXBException  {
	testEntityWithOneValueAttribute(30);
    }
    public void testEntityWithOneValueAttribute31 () throws JAXBException  {
	testEntityWithOneValueAttribute(31);
    }
    public void testEntityWithOneValueAttribute32 () throws JAXBException  {
	testEntityWithOneValueAttribute(32);
    }
    public void testEntityWithOneValueAttribute33 () throws JAXBException  {
	testEntityWithOneValueAttribute(33);
    }
    public void testEntityWithOneValueAttribute34 () throws JAXBException  {
	testEntityWithOneValueAttribute(34);
    }
    public void testEntityWithOneValueAttribute35 () throws JAXBException  {
	testEntityWithOneValueAttribute(35);
    }
    public void testEntityWithOneValueAttribute36 () throws JAXBException  {
	testEntityWithOneValueAttribute(36);
    }
    public void testEntityWithOneValueAttribute37 () throws JAXBException  {
	testEntityWithOneValueAttribute(37);
    }
    public void testEntityWithOneValueAttribute38 () throws JAXBException  {
	testEntityWithOneValueAttribute(38);
    }
    public void testEntityWithOneValueAttribute39 () throws JAXBException  {
	testEntityWithOneValueAttribute(39);
    }
    public void testEntityWithOneValueAttribute40 () throws JAXBException  {
	testEntityWithOneValueAttribute(40);
    }
    public void testEntityWithOneValueAttribute41 () throws JAXBException  {
	testEntityWithOneValueAttribute(41);
    }
    public void testEntityWithOneValueAttribute42 () throws JAXBException  {
	testEntityWithOneValueAttribute(42);
    }
    public void testEntityWithOneValueAttribute43 () throws JAXBException  {
	testEntityWithOneValueAttribute(43);
    }
    public void testEntityWithOneValueAttribute44 () throws JAXBException  {
	testEntityWithOneValueAttribute(44);
    }

    // LOCATION
    public void testEntityWithOneLocationAttribute0 () throws JAXBException  {
	testEntityWithOneLocationAttribute(0);
    }
    public void testEntityWithOneLocationAttribute1 () throws JAXBException  {
	testEntityWithOneLocationAttribute(1);
    }
    public void testEntityWithOneLocationAttribute2 () throws JAXBException  {
	testEntityWithOneLocationAttribute(2);
    }
    public void testEntityWithOneLocationAttribute3 () throws JAXBException  {
	testEntityWithOneLocationAttribute(3);
    }
    public void testEntityWithOneLocationAttribute4 () throws JAXBException  {
	testEntityWithOneLocationAttribute(4);
    }
    public void testEntityWithOneLocationAttribute5 () throws JAXBException  {
	testEntityWithOneLocationAttribute(5);
    }
    public void testEntityWithOneLocationAttribute6 () throws JAXBException  {
	testEntityWithOneLocationAttribute(6);
    }
    public void testEntityWithOneLocationAttribute7 () throws JAXBException  {
	testEntityWithOneLocationAttribute(7);
    }
    public void testEntityWithOneLocationAttribute8 () throws JAXBException  {
	testEntityWithOneLocationAttribute(8);
    }
    public void testEntityWithOneLocationAttribute9 () throws JAXBException  {
	testEntityWithOneLocationAttribute(9);
    }
    public void testEntityWithOneLocationAttribute10 () throws JAXBException  {
	testEntityWithOneLocationAttribute(10);
    }
    public void testEntityWithOneLocationAttribute11 () throws JAXBException  {
	testEntityWithOneLocationAttribute(11);
    }
    public void testEntityWithOneLocationAttribute12 () throws JAXBException  {
	testEntityWithOneLocationAttribute(12);
    }
    public void testEntityWithOneLocationAttribute13 () throws JAXBException  {
	testEntityWithOneLocationAttribute(13);
    }
    public void testEntityWithOneLocationAttribute14 () throws JAXBException  {
	testEntityWithOneLocationAttribute(14);
    }
    public void testEntityWithOneLocationAttribut15 () throws JAXBException  {
	testEntityWithOneLocationAttribute(15);
    }
    public void testEntityWithOneLocationAttribute16 () throws JAXBException  {
	testEntityWithOneLocationAttribute(16);
    }
    public void testEntityWithOneLocationAttribute17 () throws JAXBException  {
	testEntityWithOneLocationAttribute(17);
    }
    public void testEntityWithOneLocationAttribute18 () throws JAXBException  {
	testEntityWithOneLocationAttribute(18);
    }
    public void testEntityWithOneLocationAttribute19 () throws JAXBException  {
	testEntityWithOneLocationAttribute(19);
    }
    public void testEntityWithOneLocationAttribute20 () throws JAXBException  {
	testEntityWithOneLocationAttribute(20);
    }
    public void testEntityWithOneLocationAttribute21 () throws JAXBException  {
	testEntityWithOneLocationAttribute(21);
    }
    public void testEntityWithOneLocationAttribute22 () throws JAXBException  {
	testEntityWithOneLocationAttribute(22);
    }
    public void testEntityWithOneLocationAttribute23 () throws JAXBException  {
	testEntityWithOneLocationAttribute(23);
    }
    public void testEntityWithOneLocationAttribute24 () throws JAXBException  {
	testEntityWithOneLocationAttribute(24);
    }
    public void testEntityWithOneLocationAttribute25 () throws JAXBException  {
	testEntityWithOneLocationAttribute(25);
    }
    public void testEntityWithOneLocationAttribute26 () throws JAXBException  {
	testEntityWithOneLocationAttribute(26);
    }
    public void testEntityWithOneLocationAttribute27 () throws JAXBException  {
	testEntityWithOneLocationAttribute(27);
    }
    public void testEntityWithOneLocationAttribute28 () throws JAXBException  {
	testEntityWithOneLocationAttribute(28);
    }
    public void testEntityWithOneLocationAttribute29 () throws JAXBException  {
	testEntityWithOneLocationAttribute(29);
    }
    public void testEntityWithOneLocationAttribute30 () throws JAXBException  {
	testEntityWithOneLocationAttribute(30);
    }
    public void testEntityWithOneLocationAttribute31 () throws JAXBException  {
	testEntityWithOneLocationAttribute(31);
    }
    public void testEntityWithOneLocationAttribute32 () throws JAXBException  {
	testEntityWithOneLocationAttribute(32);
    }
    public void testEntityWithOneLocationAttribute33 () throws JAXBException  {
	testEntityWithOneLocationAttribute(33);
    }
    public void testEntityWithOneLocationAttribute34 () throws JAXBException  {
	testEntityWithOneLocationAttribute(34);
    }
    public void testEntityWithOneLocationAttribute35 () throws JAXBException  {
	testEntityWithOneLocationAttribute(35);
    }
    public void testEntityWithOneLocationAttribute36 () throws JAXBException  {
	testEntityWithOneLocationAttribute(36);
    }
    public void testEntityWithOneLocationAttribute37 () throws JAXBException  {
	testEntityWithOneLocationAttribute(37);
    }
    public void testEntityWithOneLocationAttribute38 () throws JAXBException  {
	testEntityWithOneLocationAttribute(38);
    }
    public void testEntityWithOneLocationAttribute39 () throws JAXBException  {
	testEntityWithOneLocationAttribute(39);
    }
    public void testEntityWithOneLocationAttribute40 () throws JAXBException  {
	testEntityWithOneLocationAttribute(40);
    }
    public void testEntityWithOneLocationAttribute41 () throws JAXBException  {
	testEntityWithOneLocationAttribute(41);
    }
    public void testEntityWithOneLocationAttribute42 () throws JAXBException  {
	testEntityWithOneLocationAttribute(42);
    }
    public void testEntityWithOneLocationAttribute43 () throws JAXBException  {
	testEntityWithOneLocationAttribute(43);
    }
    public void testEntityWithOneLocationAttribute44 () throws JAXBException  {
	testEntityWithOneLocationAttribute(44);
    }

    // OTHER
    public void testEntityWithOneOtherAttribute0 () throws JAXBException  {
	testEntityWithOneOtherAttribute(0);
    }
    public void testEntityWithOneOtherAttribute1 () throws JAXBException  {
	testEntityWithOneOtherAttribute(1);
    }
    public void testEntityWithOneOtherAttribute2 () throws JAXBException  {
	testEntityWithOneOtherAttribute(2);
    }
    public void testEntityWithOneOtherAttribute3 () throws JAXBException  {
	testEntityWithOneOtherAttribute(3);
    }
    public void testEntityWithOneOtherAttribute4 () throws JAXBException  {
	testEntityWithOneOtherAttribute(4);
    }
    public void testEntityWithOneOtherAttribute5 () throws JAXBException  {
	testEntityWithOneOtherAttribute(5);
    }
    public void testEntityWithOneOtherAttribute6 () throws JAXBException  {
	testEntityWithOneOtherAttribute(6);
    }
    public void testEntityWithOneOtherAttribute7 () throws JAXBException  {
	testEntityWithOneOtherAttribute(7);
    }
    public void testEntityWithOneOtherAttribute8 () throws JAXBException  {
	testEntityWithOneOtherAttribute(8);
    }
    public void testEntityWithOneOtherAttribute9 () throws JAXBException  {
	testEntityWithOneOtherAttribute(9);
    }
    public void testEntityWithOneOtherAttribute10 () throws JAXBException  {
	testEntityWithOneOtherAttribute(10);
    }
    public void testEntityWithOneOtherAttribute11 () throws JAXBException  {
	testEntityWithOneOtherAttribute(11);
    }
    public void testEntityWithOneOtherAttribute12 () throws JAXBException  {
	testEntityWithOneOtherAttribute(12);
    }
    public void testEntityWithOneOtherAttribute13 () throws JAXBException  {
	testEntityWithOneOtherAttribute(13);
    }
    public void testEntityWithOneOtherAttribute14 () throws JAXBException  {
	testEntityWithOneOtherAttribute(14);
    }
    public void testEntityWithOneOtherAttribut15 () throws JAXBException  {
	testEntityWithOneOtherAttribute(15);
    }
    public void testEntityWithOneOtherAttribute16 () throws JAXBException  {
	testEntityWithOneOtherAttribute(16);
    }
    public void testEntityWithOneOtherAttribute17 () throws JAXBException  {
	testEntityWithOneOtherAttribute(17);
    }
    public void testEntityWithOneOtherAttribute18 () throws JAXBException  {
	testEntityWithOneOtherAttribute(18);
    }
    public void testEntityWithOneOtherAttribute19 () throws JAXBException  {
	testEntityWithOneOtherAttribute(19);
    }
    public void testEntityWithOneOtherAttribute20 () throws JAXBException  {
	testEntityWithOneOtherAttribute(20);
    }
    public void testEntityWithOneOtherAttribute21 () throws JAXBException  {
	testEntityWithOneOtherAttribute(21);
    }
    public void testEntityWithOneOtherAttribute22 () throws JAXBException  {
	testEntityWithOneOtherAttribute(22);
    }
    public void testEntityWithOneOtherAttribute23 () throws JAXBException  {
	testEntityWithOneOtherAttribute(23);
    }
    public void testEntityWithOneOtherAttribute24 () throws JAXBException  {
	testEntityWithOneOtherAttribute(24);
    }
    public void testEntityWithOneOtherAttribute25 () throws JAXBException  {
	testEntityWithOneOtherAttribute(25);
    }
    public void testEntityWithOneOtherAttribute26 () throws JAXBException  {
	testEntityWithOneOtherAttribute(26);
    }
    public void testEntityWithOneOtherAttribute27 () throws JAXBException  {
	testEntityWithOneOtherAttribute(27);
    }
    public void testEntityWithOneOtherAttribute28 () throws JAXBException  {
	testEntityWithOneOtherAttribute(28);
    }
    public void testEntityWithOneOtherAttribute29 () throws JAXBException  {
	testEntityWithOneOtherAttribute(29);
    }
    public void testEntityWithOneOtherAttribute30 () throws JAXBException  {
	testEntityWithOneOtherAttribute(30);
    }
    public void testEntityWithOneOtherAttribute31 () throws JAXBException  {
	testEntityWithOneOtherAttribute(31);
    }
    public void testEntityWithOneOtherAttribute32 () throws JAXBException  {
	testEntityWithOneOtherAttribute(32);
    }
    public void testEntityWithOneOtherAttribute33 () throws JAXBException  {
	testEntityWithOneOtherAttribute(33);
    }
    public void testEntityWithOneOtherAttribute34 () throws JAXBException  {
	testEntityWithOneOtherAttribute(34);
    }
    public void testEntityWithOneOtherAttribute35 () throws JAXBException  {
	testEntityWithOneOtherAttribute(35);
    }
    public void testEntityWithOneOtherAttribute36 () throws JAXBException  {
	testEntityWithOneOtherAttribute(36);
    }
    public void testEntityWithOneOtherAttribute37 () throws JAXBException  {
	testEntityWithOneOtherAttribute(37);
    }
    public void testEntityWithOneOtherAttribute38 () throws JAXBException  {
	testEntityWithOneOtherAttribute(38);
    }
    public void testEntityWithOneOtherAttribute39 () throws JAXBException  {
	testEntityWithOneOtherAttribute(39);
    }
    public void testEntityWithOneOtherAttribute40 () throws JAXBException  {
	testEntityWithOneOtherAttribute(40);
    }
    public void testEntityWithOneOtherAttribute41 () throws JAXBException  {
	testEntityWithOneOtherAttribute(41);
    }
    public void testEntityWithOneOtherAttribute42 () throws JAXBException  {
	testEntityWithOneOtherAttribute(42);
    }
    public void testEntityWithOneOtherAttribute43 () throws JAXBException  {
	testEntityWithOneOtherAttribute(43);
    }
    public void testEntityWithOneOtherAttribute44 () throws JAXBException  {
	testEntityWithOneOtherAttribute(44);
    }


    // TYPE

    public void testEntityWithOneAttribute0 () throws JAXBException  {
	testEntityWithOneTypeAttribute(0);
    }
    public void testEntityWithOneAttribute1 () throws JAXBException  {
	testEntityWithOneTypeAttribute(1);
    }
    public void testEntityWithOneAttribute2 () throws JAXBException  {
	testEntityWithOneTypeAttribute(2);
    }
    public void testEntityWithOneAttribute3 () throws JAXBException  {
	testEntityWithOneTypeAttribute(3);
    }
    public void testEntityWithOneAttribute4 () throws JAXBException  {
	testEntityWithOneTypeAttribute(4);
    }
    public void testEntityWithOneAttribute5 () throws JAXBException  {
	testEntityWithOneTypeAttribute(5);
    }
    public void testEntityWithOneAttribute6 () throws JAXBException  {
	testEntityWithOneTypeAttribute(6);
    }
    public void testEntityWithOneAttribute7 () throws JAXBException  {
	testEntityWithOneTypeAttribute(7);
    }
    public void testEntityWithOneAttribute8 () throws JAXBException  {
	testEntityWithOneTypeAttribute(8);
    }
    public void testEntityWithOneAttribute9 () throws JAXBException  {
	testEntityWithOneTypeAttribute(0);
    }
    public void testEntityWithOneAttribute10 () throws JAXBException  {
	testEntityWithOneTypeAttribute(10);
    }
    public void testEntityWithOneAttribute11 () throws JAXBException  {
	testEntityWithOneTypeAttribute(11);
    }
    public void testEntityWithOneAttribute12 () throws JAXBException  {
	testEntityWithOneTypeAttribute(12);
    }
    public void testEntityWithOneAttribute13 () throws JAXBException  {
	testEntityWithOneTypeAttribute(13);
    }
    public void testEntityWithOneAttribute14 () throws JAXBException  {
	testEntityWithOneTypeAttribute(14);
    }
    public void testEntityWithOneAttribute15 () throws JAXBException  {
	testEntityWithOneTypeAttribute(15);
    }
    public void testEntityWithOneAttribute16 () throws JAXBException  {
	testEntityWithOneTypeAttribute(16);
    }
    public void testEntityWithOneAttribute17 () throws JAXBException  {
	testEntityWithOneTypeAttribute(17);
    }
    public void testEntityWithOneAttribute18 () throws JAXBException  {
	testEntityWithOneTypeAttribute(18);
    }
    public void testEntityWithOneAttribute19 () throws JAXBException  {
	testEntityWithOneTypeAttribute(19);
    }
    public void testEntityWithOneAttribute20 () throws JAXBException  {
	testEntityWithOneTypeAttribute(20);
    }
    public void testEntityWithOneAttribute21 () throws JAXBException  {
	testEntityWithOneTypeAttribute(21);
    }
    public void testEntityWithOneAttribute22 () throws JAXBException  {
	testEntityWithOneTypeAttribute(22);
    }
    public void testEntityWithOneAttribute23 () throws JAXBException  {
	testEntityWithOneTypeAttribute(23);
    }
    public void testEntityWithOneAttribute24 () throws JAXBException  {
   	testEntityWithOneTypeAttribute(24);
    }
    public void testEntityWithOneAttribute25 () throws JAXBException  {
	testEntityWithOneTypeAttribute(25);
    }
    public void testEntityWithOneAttribute26 () throws JAXBException  {
	testEntityWithOneTypeAttribute(26);
    }
    public void testEntityWithOneAttribute27 () throws JAXBException  {
	testEntityWithOneTypeAttribute(27);
    }
    public void testEntityWithOneAttribute28 () throws JAXBException  {
	testEntityWithOneTypeAttribute(28);
    }
    public void testEntityWithOneAttribute29 () throws JAXBException  {
	testEntityWithOneTypeAttribute(29);
    }
    public void testEntityWithOneAttribute30 () throws JAXBException  {
	testEntityWithOneTypeAttribute(30);
    }
    public void testEntityWithOneAttribute31 () throws JAXBException  {
	testEntityWithOneTypeAttribute(31);
    }
    public void testEntityWithOneAttribute32 () throws JAXBException  {
	testEntityWithOneTypeAttribute(32);
    }
    public void testEntityWithOneAttribute33() throws JAXBException  {
	testEntityWithOneTypeAttribute(33);
    }
    public void testEntityWithOneAttribute34 () throws JAXBException  {
	testEntityWithOneTypeAttribute(34);
    }
    public void testEntityWithOneAttribute35 () throws JAXBException  {
	testEntityWithOneTypeAttribute(35);
    }
    public void testEntityWithOneAttribute36 () throws JAXBException  {
	testEntityWithOneTypeAttribute(36);
    }
    public void testEntityWithOneAttribute37 () throws JAXBException  {
	testEntityWithOneTypeAttribute(37);
    }
    public void testEntityWithOneAttribute38 () throws JAXBException  {
	testEntityWithOneTypeAttribute(38);
    }
    public void testEntityWithOneAttribute39 () throws JAXBException  {
	testEntityWithOneTypeAttribute(39);
    }
    public void testEntityWithOneAttribute40 () throws JAXBException  {
	testEntityWithOneTypeAttribute(40);
    }
    public void testEntityWithOneAttribute41 () throws JAXBException  {
	testEntityWithOneTypeAttribute(41);
    }
    public void testEntityWithOneAttribute42 () throws JAXBException  {
	testEntityWithOneTypeAttribute(42);
    }
    public void testEntityWithOneAttribute43 () throws JAXBException  {
	testEntityWithOneTypeAttribute(43);
    }
    public void testEntityWithOneAttribute44 () throws JAXBException  {
	testEntityWithOneTypeAttribute(44);
    }

    public void testAssociationWithOneRoleAttribute0 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(0);
    }
    public void testAssociationWithOneRoleAttribute1 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(1);
    }
    public void testAssociationWithOneRoleAttribute2 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(2);
    }
    public void testAssociationWithOneRoleAttribute3 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(3);
    }
    public void testAssociationWithOneRoleAttribute4 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(4);
    }
    public void testAssociationWithOneRoleAttribute5 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(5);
    }
    public void testAssociationWithOneRoleAttribute6 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(6);
    }
    public void testAssociationWithOneRoleAttribute7 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(7);
    }
    public void testAssociationWithOneRoleAttribute8 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(8);
    }
    public void testAssociationWithOneRoleAttribute9 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(9);
    }
    public void testAssociationWithOneRoleAttribute10 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(10);
    }
    public void testAssociationWithOneRoleAttribute11 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(11);
    }
    public void testAssociationWithOneRoleAttribute12 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(12);
    }
    public void testAssociationWithOneRoleAttribute13 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(13);
    }
    public void testAssociationWithOneRoleAttribute14 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(14);
    }
    public void testAssociationWithOneRoleAttribute15 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(15);
    }
    public void testAssociationWithOneRoleAttribute16 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(16);
    }
    public void testAssociationWithOneRoleAttribute17 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(17);
    }
    public void testAssociationWithOneRoleAttribute18 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(18);
    }
    public void testAssociationWithOneRoleAttribute19 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(19);
    }
    public void testAssociationWithOneRoleAttribute20 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(20);
    }
    public void testAssociationWithOneRoleAttribute21 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(21);
    }
    public void testAssociationWithOneRoleAttribute22 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(22);
    }
    public void testAssociationWithOneRoleAttribute23 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(23);
    }
    public void testAssociationWithOneRoleAttribute24 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(24);
    }
    public void testAssociationWithOneRoleAttribute25 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(25);
    }
    public void testAssociationWithOneRoleAttribute26 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(26);
    }
    public void testAssociationWithOneRoleAttribute27 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(27);
    }
    public void testAssociationWithOneRoleAttribute28 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(28);
    }
    public void testAssociationWithOneRoleAttribute29 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(29);
    }
    public void testAssociationWithOneRoleAttribute30 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(30);
    }
    public void testAssociationWithOneRoleAttribute31 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(31);
    }
    public void testAssociationWithOneRoleAttribute32 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(32);
    }
    public void testAssociationWithOneRoleAttribute33 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(33);
    }
    public void testAssociationWithOneRoleAttribute34 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(34);
    }
    public void testAssociationWithOneRoleAttribute35 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(35);
    }
    public void testAssociationWithOneRoleAttribute36 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(36);
    }
    public void testAssociationWithOneRoleAttribute37 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(37);
    }
    public void testAssociationWithOneRoleAttribute38 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(38);
    }
    public void testAssociationWithOneRoleAttribute39 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(39);
    }
    public void testAssociationWithOneRoleAttribute40 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(40);
    }
    public void testAssociationWithOneRoleAttribute41 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(41);
    }
    public void testAssociationWithOneRoleAttribute42 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(42);
    }
    public void testAssociationWithOneRoleAttribute43 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(43);
    }
    public void testAssociationWithOneRoleAttribute44 () throws JAXBException  {
	testAssociationWithOneRoleAttribute(44);
    }
  
    
    public void testEntity0() throws JAXBException  {
	Entity a = pFactory.newEntity(qq("e0"));
	
	addOthers(a, new QName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, new QName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, new QName(EX2_NS, "tag4", "ex4"));
	addOthers(a, new QName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);
	addLocations(a);

	a.setValue(pFactory.newValue(10,Name.QNAME_XSD_BYTE));
	a.setValue(pFactory.newValue("10",Name.QNAME_XSD_STRING));


	makeDocAndTest(a,"target/attr_entity0");
    }
    
    

    public void testActivity0() throws JAXBException  {
	Activity a = pFactory.newActivity(qq("a0"));
	
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
	Agent a = pFactory.newAgent(qq("ag0"));
	
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
    public org.openprovenance.prov.model.QualifiedName qq(String n) {
		return new QualifiedName(EX_NS, n, EX_PREFIX);
       }
    
    public void testGeneration0() throws JAXBException  {

	WasGeneratedBy a = pFactory.newWasGeneratedBy((QualifiedName)null,
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

 	WasInvalidatedBy a = pFactory.newWasInvalidatedBy((QualifiedName)null,
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

 	Used a = pFactory.newUsed((QualifiedName)null,
 	                          qq("a1"),
 	                          null,
 	                          qq("e1"));
  	
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


	WasAssociatedWith a = pFactory.newWasAssociatedWith(qq("assoc0"), 
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


	WasAttributedTo a = pFactory.newWasAttributedTo(qq("assoc0"), 
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
 

 	WasDerivedFrom a = pFactory.newWasDerivedFrom(qq("der0"), 
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

	WasEndedBy a = pFactory.newWasEndedBy((QualifiedName)null,
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

	WasStartedBy a = pFactory.newWasStartedBy((QualifiedName)null,
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


 	WasInfluencedBy a = pFactory.newWasInfluencedBy(qq("infl0"), 
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


	WasInformedBy a = pFactory.newWasInformedBy(qq("com0"), 
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


	ActedOnBehalfOf a = pFactory.newActedOnBehalfOf(qq("del0"), 
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
