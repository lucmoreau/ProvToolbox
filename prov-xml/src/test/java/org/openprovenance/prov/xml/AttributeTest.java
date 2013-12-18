package org.openprovenance.prov.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasLocation;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.HasRole;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.QualifiedName;
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
    public static org.openprovenance.prov.model.Name name;


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
    
    DOMProcessing dom=new DOMProcessing(pFactory);
    
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
	 {"un lieu",name.XSD_STRING},
	 
         {pFactory.newInternationalizedString("un lieu","fr"),name.XSD_STRING},

         {pFactory.newInternationalizedString("a place","en"),name.XSD_STRING}
        };


    public Object[][] attributeValues_long =
        {
	 {"un lieu",name.XSD_STRING},
	 
         {pFactory.newInternationalizedString("un lieu","fr"),name.PROV_LANG_STRING},

         {pFactory.newInternationalizedString("a place","en"),name.PROV_LANG_STRING},
           
         {1,name.XSD_INT},

         {1,name.XSD_LONG},

         {1,name.XSD_SHORT},

         {2.0,name.XSD_DOUBLE},

         {1.0,name.XSD_FLOAT},

         {10,name.XSD_DECIMAL},

         {true,name.XSD_BOOLEAN},

         {false,name.XSD_BOOLEAN},

  //    FIXME   {"yes",name.QNAME_XSD_BOOLEAN},

  //   FIXME    {"no",name.QNAME_XSD_BOOLEAN},
         
         {10,name.XSD_BYTE},

         {10,name.XSD_UNSIGNED_INT},

         {10,name.XSD_UNSIGNED_LONG},

         {10,name.XSD_INTEGER},

         {10,name.XSD_UNSIGNED_SHORT},

         {10,name.XSD_NON_NEGATIVE_INTEGER},

         {-10,name.XSD_NON_POSITIVE_INTEGER},

         {10,name.XSD_POSITIVE_INTEGER},

         {10,name.XSD_UNSIGNED_BYTE},

         {"http://example.org",name.XSD_ANY_URI},


         // Consider following cases for QNames
         // - declared namespace, with declared prefix
         // - declared namespace, with other prefix
         // - declared namespace, as default namespace

         // - undeclared namespace, with declared prefix
         // - undeclared namespace, with other prefix
         // - undeclared namespace, as default namespace

         {pFactory.newQualifiedName(EX_NS, "abc", EX_PREFIX), name.XSD_QNAME},
         
         {pFactory.newQualifiedName(EX_NS, "abcd", "other"), name.XSD_QNAME},
         
         {pFactory.newQualifiedName(EX_NS, "abcde",null), name.XSD_QNAME},
         
         {pFactory.newQualifiedName("http://example4.org/", "zabc", EX_PREFIX), name.XSD_QNAME},
         
         {pFactory.newQualifiedName("http://example4.org/", "zabcd", "other"), name.XSD_QNAME},
             
         {pFactory.newQualifiedName("http://example4.org/", "zabcde",null), name.XSD_QNAME},
         
         
         {pFactory.newTimeNow(),name.XSD_DATETIME},

         {pFactory.newYear(2013),name.XSD_GYEAR},

         {pFactory.newGMonth(01),name.XSD_GMONTH},

         {pFactory.newGDay(30),name.XSD_GDAY},
         
         {pFactory.newGMonthDay(12,25),name.XSD_GMONTH_DAY},

         
         {pFactory.newDuration(12225),name.XSD_DURATION},

         {pFactory.newDuration("P2Y6M"),name.XSD_YEAR_MONTH_DURATION},
         {pFactory.newDuration("P2147483647DT2147483647H2147483647M123456789012345.123456789012345S"),name.XSD_DAY_TIME_DURATION},

         { new byte[] {0,1,2,34,5,6}, name.XSD_HEX_BINARY},
         { new byte[] {0,1,2,34,5,6}, name.XSD_BASE64_BINARY},
         { new byte[1023], name.XSD_BASE64_BINARY},
         
         {"en",name.XSD_LANGUAGE},
         {"normal",name.XSD_NORMALIZED_STRING},
         {"TOK",name.XSD_TOKEN},
         {"NMTOK",name.XSD_NMTOKEN},
         {"name",name.XSD_NAME},
         {"NCName",name.XSD_NCNAME},
         
         {createXMLLiteral(),name.RDF_LITERAL}
         
        };
    
   
    
    public Object[][] attributeValues =attributeValues_long;

    public void addLocations(HasLocation hl){
        for (Object [] pair: attributeValues) {
            Object value=pair[0];
            QualifiedName type=(QualifiedName) pair[1];
            hl.getLocation().add(pFactory.newLocation(value,type));
         }

    }
    public void addTypes(HasType hl){
        for (Object [] pair: attributeValues) {
            Object value=pair[0];
            QualifiedName type=(QualifiedName) pair[1];
            hl.getType().add(pFactory.newType(value,type));
         }

    }
    public void addRoles(HasRole hl){
        for (Object [] pair: attributeValues) {
            Object value=pair[0];
            QualifiedName type=(QualifiedName) pair[1];
            hl.getRole().add(pFactory.newRole(value,type));
         }

    }
    public void addOthers(HasOther ho, org.openprovenance.prov.model.QualifiedName elementName) {
	for (Object [] pair: attributeValues) {
	    Object value=pair[0];
	    QualifiedName type=(QualifiedName) pair[1];
	    if (value instanceof QualifiedName) {
		QualifiedName qq=(QualifiedName)value;
		if ((qq.getPrefix()!=null)
		    && 
		    ((qq.getPrefix().equals(elementName.getPrefix())))
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

    public void testEntityWithOneTypeAttribute(int i)  {
 	Entity a = pFactory.newEntity(q("et" + i));
 	

 	Object [] pair= attributeValues[i];
 	Object value=pair[0];
 	QualifiedName type=(QualifiedName) pair[1];
 	a.getType().add(pFactory.newType(value,type));
 	makeDocAndTest(a,"target/attr_entity_one_attr"+i);
     }
    

    public void testEntityWithOneValueAttribute(int i)  {
 	Entity a = pFactory.newEntity(q("en_v" + i));
 	

 	Object [] pair= attributeValues[i];
 	Object value=pair[0];
 	QualifiedName type=(QualifiedName) pair[1];
 	a.setValue(pFactory.newValue(value,type));
 	makeDocAndTest(a,"target/attr_entity_one_value_attr"+i);
     }

    public void testAssociationWithOneRoleAttribute(int i)  {
 	WasAssociatedWith a = pFactory.newWasAssociatedWith(q("ass_r" + i),
 	                                                    q("a1"),
 	                                                    q("ag1"));

 	Object [] pair= attributeValues[i];
 	Object value=pair[0];
 	QualifiedName type=(QualifiedName) pair[1];
 	a.getRole().add(pFactory.newRole(value,type));
 	makeDocAndTest(a,"target/attr_association_one_role_attr"+i);
     }

    public void testEntityWithOneLocationAttribute(int i)  {
  	Entity a = pFactory.newEntity(q("en_l" + i));
  	

  	Object [] pair= attributeValues[i];
  	Object value=pair[0];
  	QualifiedName type=(QualifiedName) pair[1];
  	a.getLocation().add(pFactory.newLocation(value,type));
  	makeDocAndTest(a,"target/attr_entity_one_location_attr"+i);
      }

    public void testEntityWithOneOtherAttribute(int i)  {
 	Entity a = pFactory.newEntity(q("en_o" + i));
 	

 	Object [] pair= attributeValues[i];
 	Object value=pair[0];
 	QualifiedName type=(QualifiedName) pair[1];
 	a.getOther().add(pFactory.newOther(pFactory.newQualifiedName(EX_NS,  "tag2", "exo"), value,type));
 	makeDocAndTest(a,"target/attr_entity_one_other_attr"+i);
     }
    
    // VALUE
    public void testEntityWithOneValueAttribute0 ()  {
	testEntityWithOneValueAttribute(0);
    }
    public void testEntityWithOneValueAttribute1 ()  {
	testEntityWithOneValueAttribute(1);
    }
    public void testEntityWithOneValueAttribute2 ()  {
	testEntityWithOneValueAttribute(2);
    }
    public void testEntityWithOneValueAttribute3 ()  {
	testEntityWithOneValueAttribute(3);
    }
    public void testEntityWithOneValueAttribute4 ()  {
	testEntityWithOneValueAttribute(4);
    }
    public void testEntityWithOneValueAttribute5 ()  {
	testEntityWithOneValueAttribute(5);
    }
    public void testEntityWithOneValueAttribute6 ()  {
	testEntityWithOneValueAttribute(6);
    }
    public void testEntityWithOneValueAttribute7 ()  {
	testEntityWithOneValueAttribute(7);
    }
    public void testEntityWithOneValueAttribute8 ()  {
	testEntityWithOneValueAttribute(8);
    }
    public void testEntityWithOneValueAttribute9 ()  {
	testEntityWithOneValueAttribute(9);
    }
    public void testEntityWithOneValueAttribute10 ()  {
	testEntityWithOneValueAttribute(10);
    }
    public void testEntityWithOneValueAttribute11 ()  {
	testEntityWithOneValueAttribute(11);
    }
    public void testEntityWithOneValueAttribute12 ()  {
	testEntityWithOneValueAttribute(12);
    }
    public void testEntityWithOneValueAttribute13 ()  {
	testEntityWithOneValueAttribute(13);
    }
    public void testEntityWithOneValueAttribute14 ()  {
	testEntityWithOneValueAttribute(14);
    }
    public void testEntityWithOneValueAttribut15 ()  {
	testEntityWithOneValueAttribute(15);
    }
    public void testEntityWithOneValueAttribute16 ()  {
	testEntityWithOneValueAttribute(16);
    }
    public void testEntityWithOneValueAttribute17 ()  {
	testEntityWithOneValueAttribute(17);
    }
    public void testEntityWithOneValueAttribute18 ()  {
	testEntityWithOneValueAttribute(18);
    }
    public void testEntityWithOneValueAttribute19 ()  {
	testEntityWithOneValueAttribute(19);
    }
    public void testEntityWithOneValueAttribute20 ()  {
	testEntityWithOneValueAttribute(20);
    }
    public void testEntityWithOneValueAttribute21 ()  {
	testEntityWithOneValueAttribute(21);
    }
    public void testEntityWithOneValueAttribute22 ()  {
	testEntityWithOneValueAttribute(22);
    }
    public void testEntityWithOneValueAttribute23 ()  {
	testEntityWithOneValueAttribute(23);
    }
    public void testEntityWithOneValueAttribute24 ()  {
	testEntityWithOneValueAttribute(24);
    }
    public void testEntityWithOneValueAttribute25 ()  {
	testEntityWithOneValueAttribute(25);
    }
    public void testEntityWithOneValueAttribute26 ()  {
	testEntityWithOneValueAttribute(26);
    }
    public void testEntityWithOneValueAttribute27 ()  {
	testEntityWithOneValueAttribute(27);
    }
    public void testEntityWithOneValueAttribute28 ()  {
	testEntityWithOneValueAttribute(28);
    }
    public void testEntityWithOneValueAttribute29 ()  {
	testEntityWithOneValueAttribute(29);
    }
    public void testEntityWithOneValueAttribute30 ()  {
	testEntityWithOneValueAttribute(30);
    }
    public void testEntityWithOneValueAttribute31 ()  {
	testEntityWithOneValueAttribute(31);
    }
    public void testEntityWithOneValueAttribute32 ()  {
	testEntityWithOneValueAttribute(32);
    }
    public void testEntityWithOneValueAttribute33 ()  {
	testEntityWithOneValueAttribute(33);
    }
    public void testEntityWithOneValueAttribute34 ()  {
	testEntityWithOneValueAttribute(34);
    }
    public void testEntityWithOneValueAttribute35 ()  {
	testEntityWithOneValueAttribute(35);
    }
    public void testEntityWithOneValueAttribute36 ()  {
	testEntityWithOneValueAttribute(36);
    }
    public void testEntityWithOneValueAttribute37 ()  {
	testEntityWithOneValueAttribute(37);
    }
    public void testEntityWithOneValueAttribute38 ()  {
	testEntityWithOneValueAttribute(38);
    }
    public void testEntityWithOneValueAttribute39 ()  {
	testEntityWithOneValueAttribute(39);
    }
    public void testEntityWithOneValueAttribute40 ()  {
	testEntityWithOneValueAttribute(40);
    }
    public void testEntityWithOneValueAttribute41 ()  {
	testEntityWithOneValueAttribute(41);
    }
    public void testEntityWithOneValueAttribute42 ()  {
	testEntityWithOneValueAttribute(42);
    }
    public void testEntityWithOneValueAttribute43 ()  {
	testEntityWithOneValueAttribute(43);
    }
    public void testEntityWithOneValueAttribute44 ()  {
	testEntityWithOneValueAttribute(44);
    }

    // LOCATION
    public void testEntityWithOneLocationAttribute0 ()  {
	testEntityWithOneLocationAttribute(0);
    }
    public void testEntityWithOneLocationAttribute1 ()  {
	testEntityWithOneLocationAttribute(1);
    }
    public void testEntityWithOneLocationAttribute2 ()  {
	testEntityWithOneLocationAttribute(2);
    }
    public void testEntityWithOneLocationAttribute3 ()  {
	testEntityWithOneLocationAttribute(3);
    }
    public void testEntityWithOneLocationAttribute4 ()  {
	testEntityWithOneLocationAttribute(4);
    }
    public void testEntityWithOneLocationAttribute5 ()  {
	testEntityWithOneLocationAttribute(5);
    }
    public void testEntityWithOneLocationAttribute6 ()  {
	testEntityWithOneLocationAttribute(6);
    }
    public void testEntityWithOneLocationAttribute7 ()  {
	testEntityWithOneLocationAttribute(7);
    }
    public void testEntityWithOneLocationAttribute8 ()  {
	testEntityWithOneLocationAttribute(8);
    }
    public void testEntityWithOneLocationAttribute9 ()  {
	testEntityWithOneLocationAttribute(9);
    }
    public void testEntityWithOneLocationAttribute10 ()  {
	testEntityWithOneLocationAttribute(10);
    }
    public void testEntityWithOneLocationAttribute11 ()  {
	testEntityWithOneLocationAttribute(11);
    }
    public void testEntityWithOneLocationAttribute12 ()  {
	testEntityWithOneLocationAttribute(12);
    }
    public void testEntityWithOneLocationAttribute13 ()  {
	testEntityWithOneLocationAttribute(13);
    }
    public void testEntityWithOneLocationAttribute14 ()  {
	testEntityWithOneLocationAttribute(14);
    }
    public void testEntityWithOneLocationAttribut15 ()  {
	testEntityWithOneLocationAttribute(15);
    }
    public void testEntityWithOneLocationAttribute16 ()  {
	testEntityWithOneLocationAttribute(16);
    }
    public void testEntityWithOneLocationAttribute17 ()  {
	testEntityWithOneLocationAttribute(17);
    }
    public void testEntityWithOneLocationAttribute18 ()  {
	testEntityWithOneLocationAttribute(18);
    }
    public void testEntityWithOneLocationAttribute19 ()  {
	testEntityWithOneLocationAttribute(19);
    }
    public void testEntityWithOneLocationAttribute20 ()  {
	testEntityWithOneLocationAttribute(20);
    }
    public void testEntityWithOneLocationAttribute21 ()  {
	testEntityWithOneLocationAttribute(21);
    }
    public void testEntityWithOneLocationAttribute22 ()  {
	testEntityWithOneLocationAttribute(22);
    }
    public void testEntityWithOneLocationAttribute23 ()  {
	testEntityWithOneLocationAttribute(23);
    }
    public void testEntityWithOneLocationAttribute24 ()  {
	testEntityWithOneLocationAttribute(24);
    }
    public void testEntityWithOneLocationAttribute25 ()  {
	testEntityWithOneLocationAttribute(25);
    }
    public void testEntityWithOneLocationAttribute26 ()  {
	testEntityWithOneLocationAttribute(26);
    }
    public void testEntityWithOneLocationAttribute27 ()  {
	testEntityWithOneLocationAttribute(27);
    }
    public void testEntityWithOneLocationAttribute28 ()  {
	testEntityWithOneLocationAttribute(28);
    }
    public void testEntityWithOneLocationAttribute29 ()  {
	testEntityWithOneLocationAttribute(29);
    }
    public void testEntityWithOneLocationAttribute30 ()  {
	testEntityWithOneLocationAttribute(30);
    }
    public void testEntityWithOneLocationAttribute31 ()  {
	testEntityWithOneLocationAttribute(31);
    }
    public void testEntityWithOneLocationAttribute32 ()  {
	testEntityWithOneLocationAttribute(32);
    }
    public void testEntityWithOneLocationAttribute33 ()  {
	testEntityWithOneLocationAttribute(33);
    }
    public void testEntityWithOneLocationAttribute34 ()  {
	testEntityWithOneLocationAttribute(34);
    }
    public void testEntityWithOneLocationAttribute35 ()  {
	testEntityWithOneLocationAttribute(35);
    }
    public void testEntityWithOneLocationAttribute36 ()  {
	testEntityWithOneLocationAttribute(36);
    }
    public void testEntityWithOneLocationAttribute37 ()  {
	testEntityWithOneLocationAttribute(37);
    }
    public void testEntityWithOneLocationAttribute38 ()  {
	testEntityWithOneLocationAttribute(38);
    }
    public void testEntityWithOneLocationAttribute39 ()  {
	testEntityWithOneLocationAttribute(39);
    }
    public void testEntityWithOneLocationAttribute40 ()  {
	testEntityWithOneLocationAttribute(40);
    }
    public void testEntityWithOneLocationAttribute41 ()  {
	testEntityWithOneLocationAttribute(41);
    }
    public void testEntityWithOneLocationAttribute42 ()  {
	testEntityWithOneLocationAttribute(42);
    }
    public void testEntityWithOneLocationAttribute43 ()  {
	testEntityWithOneLocationAttribute(43);
    }
    public void testEntityWithOneLocationAttribute44 ()  {
	testEntityWithOneLocationAttribute(44);
    }

    // OTHER
    public void testEntityWithOneOtherAttribute0 ()  {
	testEntityWithOneOtherAttribute(0);
    }
    public void testEntityWithOneOtherAttribute1 ()  {
	testEntityWithOneOtherAttribute(1);
    }
    public void testEntityWithOneOtherAttribute2 ()  {
	testEntityWithOneOtherAttribute(2);
    }
    public void testEntityWithOneOtherAttribute3 ()  {
	testEntityWithOneOtherAttribute(3);
    }
    public void testEntityWithOneOtherAttribute4 ()  {
	testEntityWithOneOtherAttribute(4);
    }
    public void testEntityWithOneOtherAttribute5 ()  {
	testEntityWithOneOtherAttribute(5);
    }
    public void testEntityWithOneOtherAttribute6 ()  {
	testEntityWithOneOtherAttribute(6);
    }
    public void testEntityWithOneOtherAttribute7 ()  {
	testEntityWithOneOtherAttribute(7);
    }
    public void testEntityWithOneOtherAttribute8 ()  {
	testEntityWithOneOtherAttribute(8);
    }
    public void testEntityWithOneOtherAttribute9 ()  {
	testEntityWithOneOtherAttribute(9);
    }
    public void testEntityWithOneOtherAttribute10 ()  {
	testEntityWithOneOtherAttribute(10);
    }
    public void testEntityWithOneOtherAttribute11 ()  {
	testEntityWithOneOtherAttribute(11);
    }
    public void testEntityWithOneOtherAttribute12 ()  {
	testEntityWithOneOtherAttribute(12);
    }
    public void testEntityWithOneOtherAttribute13 ()  {
	testEntityWithOneOtherAttribute(13);
    }
    public void testEntityWithOneOtherAttribute14 ()  {
	testEntityWithOneOtherAttribute(14);
    }
    public void testEntityWithOneOtherAttribut15 ()  {
	testEntityWithOneOtherAttribute(15);
    }
    public void testEntityWithOneOtherAttribute16 ()  {
	testEntityWithOneOtherAttribute(16);
    }
    public void testEntityWithOneOtherAttribute17 ()  {
	testEntityWithOneOtherAttribute(17);
    }
    public void testEntityWithOneOtherAttribute18 ()  {
	testEntityWithOneOtherAttribute(18);
    }
    public void testEntityWithOneOtherAttribute19 ()  {
	testEntityWithOneOtherAttribute(19);
    }
    public void testEntityWithOneOtherAttribute20 ()  {
	testEntityWithOneOtherAttribute(20);
    }
    public void testEntityWithOneOtherAttribute21 ()  {
	testEntityWithOneOtherAttribute(21);
    }
    public void testEntityWithOneOtherAttribute22 ()  {
	testEntityWithOneOtherAttribute(22);
    }
    public void testEntityWithOneOtherAttribute23 ()  {
	testEntityWithOneOtherAttribute(23);
    }
    public void testEntityWithOneOtherAttribute24 ()  {
	testEntityWithOneOtherAttribute(24);
    }
    public void testEntityWithOneOtherAttribute25 ()  {
	testEntityWithOneOtherAttribute(25);
    }
    public void testEntityWithOneOtherAttribute26 ()  {
	testEntityWithOneOtherAttribute(26);
    }
    public void testEntityWithOneOtherAttribute27 ()  {
	testEntityWithOneOtherAttribute(27);
    }
    public void testEntityWithOneOtherAttribute28 ()  {
	testEntityWithOneOtherAttribute(28);
    }
    public void testEntityWithOneOtherAttribute29 ()  {
	testEntityWithOneOtherAttribute(29);
    }
    public void testEntityWithOneOtherAttribute30 ()  {
	testEntityWithOneOtherAttribute(30);
    }
    public void testEntityWithOneOtherAttribute31 ()  {
	testEntityWithOneOtherAttribute(31);
    }
    public void testEntityWithOneOtherAttribute32 ()  {
	testEntityWithOneOtherAttribute(32);
    }
    public void testEntityWithOneOtherAttribute33 ()  {
	testEntityWithOneOtherAttribute(33);
    }
    public void testEntityWithOneOtherAttribute34 ()  {
	testEntityWithOneOtherAttribute(34);
    }
    public void testEntityWithOneOtherAttribute35 ()  {
	testEntityWithOneOtherAttribute(35);
    }
    public void testEntityWithOneOtherAttribute36 ()  {
	testEntityWithOneOtherAttribute(36);
    }
    public void testEntityWithOneOtherAttribute37 ()  {
	testEntityWithOneOtherAttribute(37);
    }
    public void testEntityWithOneOtherAttribute38 ()  {
	testEntityWithOneOtherAttribute(38);
    }
    public void testEntityWithOneOtherAttribute39 ()  {
	testEntityWithOneOtherAttribute(39);
    }
    public void testEntityWithOneOtherAttribute40 ()  {
	testEntityWithOneOtherAttribute(40);
    }
    public void testEntityWithOneOtherAttribute41 ()  {
	testEntityWithOneOtherAttribute(41);
    }
    public void testEntityWithOneOtherAttribute42 ()  {
	testEntityWithOneOtherAttribute(42);
    }
    public void testEntityWithOneOtherAttribute43 ()  {
	testEntityWithOneOtherAttribute(43);
    }
    public void testEntityWithOneOtherAttribute44 ()  {
	testEntityWithOneOtherAttribute(44);
    }


    // TYPE

    public void testEntityWithOneAttribute0 ()  {
	testEntityWithOneTypeAttribute(0);
    }
    public void testEntityWithOneAttribute1 ()  {
	testEntityWithOneTypeAttribute(1);
    }
    public void testEntityWithOneAttribute2 ()  {
	testEntityWithOneTypeAttribute(2);
    }
    public void testEntityWithOneAttribute3 ()  {
	testEntityWithOneTypeAttribute(3);
    }
    public void testEntityWithOneAttribute4 ()  {
	testEntityWithOneTypeAttribute(4);
    }
    public void testEntityWithOneAttribute5 ()  {
	testEntityWithOneTypeAttribute(5);
    }
    public void testEntityWithOneAttribute6 ()  {
	testEntityWithOneTypeAttribute(6);
    }
    public void testEntityWithOneAttribute7 ()  {
	testEntityWithOneTypeAttribute(7);
    }
    public void testEntityWithOneAttribute8 ()  {
	testEntityWithOneTypeAttribute(8);
    }
    public void testEntityWithOneAttribute9 ()  {
	testEntityWithOneTypeAttribute(0);
    }
    public void testEntityWithOneAttribute10 ()  {
	testEntityWithOneTypeAttribute(10);
    }
    public void testEntityWithOneAttribute11 ()  {
	testEntityWithOneTypeAttribute(11);
    }
    public void testEntityWithOneAttribute12 ()  {
	testEntityWithOneTypeAttribute(12);
    }
    public void testEntityWithOneAttribute13 ()  {
	testEntityWithOneTypeAttribute(13);
    }
    public void testEntityWithOneAttribute14 ()  {
	testEntityWithOneTypeAttribute(14);
    }
    public void testEntityWithOneAttribute15 ()  {
	testEntityWithOneTypeAttribute(15);
    }
    public void testEntityWithOneAttribute16 ()  {
	testEntityWithOneTypeAttribute(16);
    }
    public void testEntityWithOneAttribute17 ()  {
	testEntityWithOneTypeAttribute(17);
    }
    public void testEntityWithOneAttribute18 ()  {
	testEntityWithOneTypeAttribute(18);
    }
    public void testEntityWithOneAttribute19 ()  {
	testEntityWithOneTypeAttribute(19);
    }
    public void testEntityWithOneAttribute20 ()  {
	testEntityWithOneTypeAttribute(20);
    }
    public void testEntityWithOneAttribute21 ()  {
	testEntityWithOneTypeAttribute(21);
    }
    public void testEntityWithOneAttribute22 ()  {
	testEntityWithOneTypeAttribute(22);
    }
    public void testEntityWithOneAttribute23 ()  {
	testEntityWithOneTypeAttribute(23);
    }
    public void testEntityWithOneAttribute24 ()  {
   	testEntityWithOneTypeAttribute(24);
    }
    public void testEntityWithOneAttribute25 ()  {
	testEntityWithOneTypeAttribute(25);
    }
    public void testEntityWithOneAttribute26 ()  {
	testEntityWithOneTypeAttribute(26);
    }
    public void testEntityWithOneAttribute27 ()  {
	testEntityWithOneTypeAttribute(27);
    }
    public void testEntityWithOneAttribute28 ()  {
	testEntityWithOneTypeAttribute(28);
    }
    public void testEntityWithOneAttribute29 ()  {
	testEntityWithOneTypeAttribute(29);
    }
    public void testEntityWithOneAttribute30 ()  {
	testEntityWithOneTypeAttribute(30);
    }
    public void testEntityWithOneAttribute31 ()  {
	testEntityWithOneTypeAttribute(31);
    }
    public void testEntityWithOneAttribute32 ()  {
	testEntityWithOneTypeAttribute(32);
    }
    public void testEntityWithOneAttribute33()  {
	testEntityWithOneTypeAttribute(33);
    }
    public void testEntityWithOneAttribute34 ()  {
	testEntityWithOneTypeAttribute(34);
    }
    public void testEntityWithOneAttribute35 ()  {
	testEntityWithOneTypeAttribute(35);
    }
    public void testEntityWithOneAttribute36 ()  {
	testEntityWithOneTypeAttribute(36);
    }
    public void testEntityWithOneAttribute37 ()  {
	testEntityWithOneTypeAttribute(37);
    }
    public void testEntityWithOneAttribute38 ()  {
	testEntityWithOneTypeAttribute(38);
    }
    public void testEntityWithOneAttribute39 ()  {
	testEntityWithOneTypeAttribute(39);
    }
    public void testEntityWithOneAttribute40 ()  {
	testEntityWithOneTypeAttribute(40);
    }
    public void testEntityWithOneAttribute41 ()  {
	testEntityWithOneTypeAttribute(41);
    }
    public void testEntityWithOneAttribute42 ()  {
	testEntityWithOneTypeAttribute(42);
    }
    public void testEntityWithOneAttribute43 ()  {
	testEntityWithOneTypeAttribute(43);
    }
    public void testEntityWithOneAttribute44 ()  {
	testEntityWithOneTypeAttribute(44);
    }

    public void testAssociationWithOneRoleAttribute0 ()  {
	testAssociationWithOneRoleAttribute(0);
    }
    public void testAssociationWithOneRoleAttribute1 ()  {
	testAssociationWithOneRoleAttribute(1);
    }
    public void testAssociationWithOneRoleAttribute2 ()  {
	testAssociationWithOneRoleAttribute(2);
    }
    public void testAssociationWithOneRoleAttribute3 ()  {
	testAssociationWithOneRoleAttribute(3);
    }
    public void testAssociationWithOneRoleAttribute4 ()  {
	testAssociationWithOneRoleAttribute(4);
    }
    public void testAssociationWithOneRoleAttribute5 ()  {
	testAssociationWithOneRoleAttribute(5);
    }
    public void testAssociationWithOneRoleAttribute6 ()  {
	testAssociationWithOneRoleAttribute(6);
    }
    public void testAssociationWithOneRoleAttribute7 ()  {
	testAssociationWithOneRoleAttribute(7);
    }
    public void testAssociationWithOneRoleAttribute8 ()  {
	testAssociationWithOneRoleAttribute(8);
    }
    public void testAssociationWithOneRoleAttribute9 ()  {
	testAssociationWithOneRoleAttribute(9);
    }
    public void testAssociationWithOneRoleAttribute10 ()  {
	testAssociationWithOneRoleAttribute(10);
    }
    public void testAssociationWithOneRoleAttribute11 ()  {
	testAssociationWithOneRoleAttribute(11);
    }
    public void testAssociationWithOneRoleAttribute12 ()  {
	testAssociationWithOneRoleAttribute(12);
    }
    public void testAssociationWithOneRoleAttribute13 ()  {
	testAssociationWithOneRoleAttribute(13);
    }
    public void testAssociationWithOneRoleAttribute14 ()  {
	testAssociationWithOneRoleAttribute(14);
    }
    public void testAssociationWithOneRoleAttribute15 ()  {
	testAssociationWithOneRoleAttribute(15);
    }
    public void testAssociationWithOneRoleAttribute16 ()  {
	testAssociationWithOneRoleAttribute(16);
    }
    public void testAssociationWithOneRoleAttribute17 ()  {
	testAssociationWithOneRoleAttribute(17);
    }
    public void testAssociationWithOneRoleAttribute18 ()  {
	testAssociationWithOneRoleAttribute(18);
    }
    public void testAssociationWithOneRoleAttribute19 ()  {
	testAssociationWithOneRoleAttribute(19);
    }
    public void testAssociationWithOneRoleAttribute20 ()  {
	testAssociationWithOneRoleAttribute(20);
    }
    public void testAssociationWithOneRoleAttribute21 ()  {
	testAssociationWithOneRoleAttribute(21);
    }
    public void testAssociationWithOneRoleAttribute22 ()  {
	testAssociationWithOneRoleAttribute(22);
    }
    public void testAssociationWithOneRoleAttribute23 ()  {
	testAssociationWithOneRoleAttribute(23);
    }
    public void testAssociationWithOneRoleAttribute24 ()  {
	testAssociationWithOneRoleAttribute(24);
    }
    public void testAssociationWithOneRoleAttribute25 ()  {
	testAssociationWithOneRoleAttribute(25);
    }
    public void testAssociationWithOneRoleAttribute26 ()  {
	testAssociationWithOneRoleAttribute(26);
    }
    public void testAssociationWithOneRoleAttribute27 ()  {
	testAssociationWithOneRoleAttribute(27);
    }
    public void testAssociationWithOneRoleAttribute28 ()  {
	testAssociationWithOneRoleAttribute(28);
    }
    public void testAssociationWithOneRoleAttribute29 ()  {
	testAssociationWithOneRoleAttribute(29);
    }
    public void testAssociationWithOneRoleAttribute30 ()  {
	testAssociationWithOneRoleAttribute(30);
    }
    public void testAssociationWithOneRoleAttribute31 ()  {
	testAssociationWithOneRoleAttribute(31);
    }
    public void testAssociationWithOneRoleAttribute32 ()  {
	testAssociationWithOneRoleAttribute(32);
    }
    public void testAssociationWithOneRoleAttribute33 ()  {
	testAssociationWithOneRoleAttribute(33);
    }
    public void testAssociationWithOneRoleAttribute34 ()  {
	testAssociationWithOneRoleAttribute(34);
    }
    public void testAssociationWithOneRoleAttribute35 ()  {
	testAssociationWithOneRoleAttribute(35);
    }
    public void testAssociationWithOneRoleAttribute36 ()  {
	testAssociationWithOneRoleAttribute(36);
    }
    public void testAssociationWithOneRoleAttribute37 ()  {
	testAssociationWithOneRoleAttribute(37);
    }
    public void testAssociationWithOneRoleAttribute38 ()  {
	testAssociationWithOneRoleAttribute(38);
    }
    public void testAssociationWithOneRoleAttribute39 ()  {
	testAssociationWithOneRoleAttribute(39);
    }
    public void testAssociationWithOneRoleAttribute40 ()  {
	testAssociationWithOneRoleAttribute(40);
    }
    public void testAssociationWithOneRoleAttribute41 ()  {
	testAssociationWithOneRoleAttribute(41);
    }
    public void testAssociationWithOneRoleAttribute42 ()  {
	testAssociationWithOneRoleAttribute(42);
    }
    public void testAssociationWithOneRoleAttribute43 ()  {
	testAssociationWithOneRoleAttribute(43);
    }
    public void testAssociationWithOneRoleAttribute44 ()  {
	testAssociationWithOneRoleAttribute(44);
    }
  
    
    public void testEntity0()  {
	Entity a = pFactory.newEntity(q("e0"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);
	addLocations(a);

	a.setValue(pFactory.newValue(10,name.XSD_BYTE));
	a.setValue(pFactory.newValue("10",name.XSD_STRING));


	makeDocAndTest(a,"target/attr_entity0");
    }
    
    

    public void testActivity0()  {
	Activity a = pFactory.newActivity(q("a0"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);
	addLocations(a);

	makeDocAndTest(a,"target/attr_activity0");
    }

    

    public void testAgent0()  {
	Agent a = pFactory.newAgent(q("ag0"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);
	addLocations(a);

	makeDocAndTest(a,"target/attr_agent0");
    }


    public QualifiedName q(String n) {
	return new org.openprovenance.prov.xml.QualifiedName(EX_NS, n, EX_PREFIX);
    }
    
    public void testGeneration0()  {
	WasGeneratedBy a = pFactory.newWasGeneratedBy((QualifiedName)null,
	                                              q("e1"),
	                                              null,
	                                              q("a1"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));
	
	addLabels(a);
	addRoles(a);
	addTypes(a);	
	addLocations(a);
		
	makeDocAndTest(a,"target/attr_generation0");
    }

    public void testInvalidation0()  {

 	WasInvalidatedBy a = pFactory.newWasInvalidatedBy((QualifiedName)null,
   							q("e1"),
   							q("a1"));
  	
  	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
  	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
  	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
  	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

  	addLabels(a);
  	addRoles(a);
  	addTypes(a);	
  	addLocations(a);
  	

  	makeDocAndTest(a,"target/attr_invalidation0");
      }

    public void testUsage0()  {

 	Used a = pFactory.newUsed((QualifiedName)null,
 	                          q("a1"),
 	                          null,
 	                          q("e1"));
  	
  	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
  	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
  	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
  	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

  	addLabels(a);
  	addRoles(a);
  	addTypes(a);	
  	addLocations(a);
  	

  	makeDocAndTest(a,"target/attr_usage0");
      }
    
   public void testAssociation0()  {


	WasAssociatedWith a = pFactory.newWasAssociatedWith(q("assoc0"), 
	                                                    q("a1"),
	                                                    q("ag1"));
 	
 	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
 	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
 	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
 	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

 	addLabels(a);
 	addRoles(a);
 	addTypes(a);	
 	

 	makeDocAndTest(a,"target/attr_association0");
     }
     
   
  public void testAttribution0()  {


	WasAttributedTo a = pFactory.newWasAttributedTo(q("assoc0"), 
	                                                    q("e1"),
	                                                    q("ag1"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);	
	

	makeDocAndTest(a,"target/attr_attribution0");
    }

  public void testDerivation0()  {
 

 	WasDerivedFrom a = pFactory.newWasDerivedFrom(q("der0"), 
 	                                              q("e2"),
 	                                              q("e1"));
 	
 	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
 	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
 	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
 	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

 	addLabels(a);
 	addTypes(a);	
 	

 	makeDocAndTest(a,"target/attr_derivation0");
  }
  
  public void testEnd0()  {

	WasEndedBy a = pFactory.newWasEndedBy((QualifiedName)null,
							q("a1"),
							q("e1"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addRoles(a);
	addTypes(a);	
	addLocations(a);
	

	makeDocAndTest(a,"target/attr_end0");
   }

  public void testStart0()  {

	WasStartedBy a = pFactory.newWasStartedBy((QualifiedName)null,
	                                          q("a1"),
	                                          q("e1"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addRoles(a);
	addTypes(a);	
	addLocations(a);
	

	makeDocAndTest(a,"target/attr_start0");
   }
  
  public void testInfluence0()  {


 	WasInfluencedBy a = pFactory.newWasInfluencedBy(q("infl0"), 
 	                                                q("e1"),
 	                                                q("e2"));
 	
 	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
 	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
 	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
 	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

 	addLabels(a);
 	addTypes(a);	
 	

 	makeDocAndTest(a,"target/attr_influence0");
     }

   
  public void testCommunication0()  {


	WasInformedBy a = pFactory.newWasInformedBy(q("com0"), 
	                                            q("a1"),
	                                            q("a2"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);	
	

	makeDocAndTest(a,"target/attr_communication0");
   }

 
  public void testDelegation0() {


	ActedOnBehalfOf a = pFactory.newActedOnBehalfOf(q("del0"), 
	                                                q("a1"),
	                                                q("a2"),
	                                                q("a3"));
	
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", "ex4"));
	addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

	addLabels(a);
	addTypes(a);	
	

	makeDocAndTest(a,"target/attr_delegation0");
   }

 

  public void testDictionaryInsertionWithOneKey(int i)  {
      Object [] pair= attributeValues[i];
      Object value=pair[0];
      QualifiedName type=(QualifiedName) pair[1];
	
      List<org.openprovenance.prov.model.Entry> ll = new LinkedList<org.openprovenance.prov.model.Entry>();
      
      org.openprovenance.prov.model.Entry p1 = pFactory.newEntry(pFactory.newKey(value, type),
                                                                 q("e0"));
      ll.add(p1);
      DerivedByInsertionFrom a = pFactory.newDerivedByInsertionFrom(q("dins_"+i), 
                                                                    q("d2"), 
                                                                    q("d1"), 
                                                                    ll, null);
	


      makeDocAndTest(a,"target/attr_dict_insert_one_key"+i);
    }
  
  public void testDictionaryInsertionWithOneKey0() {
      testDictionaryInsertionWithOneKey(0);
  }
  public void testDictionaryInsertionWithOneKey1() {
      testDictionaryInsertionWithOneKey(1);
  }
  public void testDictionaryInsertionWithOneKey2() {
      testDictionaryInsertionWithOneKey(2);
  }
  public void testDictionaryInsertionWithOneKey3() {
      testDictionaryInsertionWithOneKey(3);
  }
  public void testDictionaryInsertionWithOneKey4() {
      testDictionaryInsertionWithOneKey(4);
  }
  public void testDictionaryInsertionWithOneKey5() {
      testDictionaryInsertionWithOneKey(5);
  }
  public void testDictionaryInsertionWithOneKey6() {
      testDictionaryInsertionWithOneKey(6);
  }
  public void testDictionaryInsertionWithOneKey7() {
      testDictionaryInsertionWithOneKey(7);
  }
  public void testDictionaryInsertionWithOneKey8() {
      testDictionaryInsertionWithOneKey(8);
  }
  public void testDictionaryInsertionWithOneKey9() {
      testDictionaryInsertionWithOneKey(9);
  }
  public void testDictionaryInsertionWithOneKey10() {
      testDictionaryInsertionWithOneKey(10);
  }
  public void testDictionaryInsertionWithOneKey11() {
      testDictionaryInsertionWithOneKey(11);
  }
  public void testDictionaryInsertionWithOneKey12() {
      testDictionaryInsertionWithOneKey(12);
  }
  public void testDictionaryInsertionWithOneKey13() {
      testDictionaryInsertionWithOneKey(13);
  }
  public void testDictionaryInsertionWithOneKey14() {
      testDictionaryInsertionWithOneKey(14);
  }
  public void testDictionaryInsertionWithOneKey15() {
      testDictionaryInsertionWithOneKey(15);
  }
  public void testDictionaryInsertionWithOneKey16() {
      testDictionaryInsertionWithOneKey(16);
  }
  public void testDictionaryInsertionWithOneKey17() {
      testDictionaryInsertionWithOneKey(17);
  }
  public void testDictionaryInsertionWithOneKey18() {
      testDictionaryInsertionWithOneKey(18);
  }
  public void testDictionaryInsertionWithOneKey19() {
      testDictionaryInsertionWithOneKey(19);
  }
  public void testDictionaryInsertionWithOneKey20() {
      testDictionaryInsertionWithOneKey(20);
  }
  public void testDictionaryInsertionWithOneKey21() {
      testDictionaryInsertionWithOneKey(21);
  }
  public void testDictionaryInsertionWithOneKey22() {
      testDictionaryInsertionWithOneKey(22);
  }
  public void testDictionaryInsertionWithOneKey23() {
      testDictionaryInsertionWithOneKey(23);
  }
  public void testDictionaryInsertionWithOneKey24() {
      testDictionaryInsertionWithOneKey(24);
  }
  public void testDictionaryInsertionWithOneKey25() {
      testDictionaryInsertionWithOneKey(25);
  }
  public void testDictionaryInsertionWithOneKey26() {
      testDictionaryInsertionWithOneKey(26);
  }
  public void testDictionaryInsertionWithOneKey27() {
      testDictionaryInsertionWithOneKey(27);
  }
  public void testDictionaryInsertionWithOneKey28() {
      testDictionaryInsertionWithOneKey(28);
  }
  public void testDictionaryInsertionWithOneKey29() {
      testDictionaryInsertionWithOneKey(29);
  }
  public void testDictionaryInsertionWithOneKey30() {
      testDictionaryInsertionWithOneKey(30);
  }
  public void testDictionaryInsertionWithOneKey31() {
      testDictionaryInsertionWithOneKey(31);
  }
  public void testDictionaryInsertionWithOneKey32() {
      testDictionaryInsertionWithOneKey(32);
  }
  public void testDictionaryInsertionWithOneKey33() {
      testDictionaryInsertionWithOneKey(33);
  }
  public void testDictionaryInsertionWithOneKey34() {
      testDictionaryInsertionWithOneKey(34);
  }
  public void testDictionaryInsertionWithOneKey35() {
      testDictionaryInsertionWithOneKey(35);
  }
  public void testDictionaryInsertionWithOneKey36() {
      testDictionaryInsertionWithOneKey(36);
  }
  public void testDictionaryInsertionWithOneKey37() {
      testDictionaryInsertionWithOneKey(37);
  }
  public void testDictionaryInsertionWithOneKey38() {
      testDictionaryInsertionWithOneKey(38);
  }
  public void testDictionaryInsertionWithOneKey39() {
      testDictionaryInsertionWithOneKey(39);
  }
  public void testDictionaryInsertionWithOneKey40() {
      testDictionaryInsertionWithOneKey(40);
  }
  public void testDictionaryInsertionWithOneKey41() {
      testDictionaryInsertionWithOneKey(41);
  }
  public void testDictionaryInsertionWithOneKey42() {
      testDictionaryInsertionWithOneKey(42);
  }
  public void testDictionaryInsertionWithOneKey43() {
      testDictionaryInsertionWithOneKey(43);
  }
  public void testDictionaryInsertionWithOneKey44() {
      testDictionaryInsertionWithOneKey(44);
  }

}
