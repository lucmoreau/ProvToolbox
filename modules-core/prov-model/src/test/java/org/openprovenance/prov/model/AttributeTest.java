package org.openprovenance.prov.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;

import static org.openprovenance.prov.model.ExtensionRoundTripFromJavaTest.deepCopy;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class AttributeTest extends ProvFrameworkTest {

	Logger logger = LogManager.getLogger(AttributeTest.class);


	public static final String EX_NS = "http://example.org/";
	public static final String EX2_NS = "http://example2.org/";
	public static final String EX_PREFIX = "ex";
	public static final String EX2_PREFIX = "ex2";
	public static final String EX4_PREFIX = "ex4";


	public static ProvFactory pFactory;
	public static Name name;
	public static boolean warned=false;

	static {
		pFactory = new org.openprovenance.prov.vanilla.ProvFactory();
		name=pFactory.getName();

	}


	public AttributeTest() {
		this.documentEquality = new DocumentEquality(mergeDuplicateProperties(),null);
		if (!warned) {
			warned = true;

			logger.warn("AttributeTest not supporting RDF_LITERAL type for attributes");
			logger.warn("AttributeTest not supporting dictionaries");
			logger.warn("AttributeTest not supporting MentionOf");
		}
	}

	public boolean urlFlag = true;

	/**
	 * @return the suite of tests being tested
	 */



	public void updateNamespaces(Document doc) {
		Namespace ns=Namespace.gatherNamespaces(doc);
		doc.setNamespace(ns);
	}


    public void compareDocAndFile(Document doc, String file, boolean check) {
		file=file+extension();
		writeDocument(doc, file);
		if (check) conditionalCheckSchema(file);

		Document doc3=readDocument(file);
		documentComparator.compareDocuments(doc, doc3, check && checkTest(file));
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
		return table.get(file1);
	}


	Map<String,Document> table=new Hashtable<>();
	public void writeDocument(Document doc, String file2) {
		logger.info("deep copy of " + file2);
		Namespace.withThreadNamespace(doc.getNamespace());
		Document doc2 = deepCopy(doc);
		table.put(file2, doc2);
	}




    ///////////////////////////////////////////////////////////////////////







	public boolean test=true;

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


	public Object[][] attributeValues =
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

					{pFactory.newQualifiedName(EX_NS, "abc", EX_PREFIX), name.PROV_QUALIFIED_NAME},

					{pFactory.newQualifiedName(EX_NS, "abcd", "other"), name.PROV_QUALIFIED_NAME},

					{pFactory.newQualifiedName(EX_NS, "abcde",null), name.PROV_QUALIFIED_NAME},

					{pFactory.newQualifiedName("http://example4.org/", "zabc", EX_PREFIX), name.PROV_QUALIFIED_NAME},

					{pFactory.newQualifiedName("http://example4.org/", "zabcd", "other"), name.PROV_QUALIFIED_NAME},

					{pFactory.newQualifiedName("http://example4.org/", "zabcde",null), name.PROV_QUALIFIED_NAME},


					{pFactory.newTimeNow(),name.XSD_DATETIME},

					{pFactory.newYear(2013),name.XSD_GYEAR},

					{pFactory.newGMonth(01),name.XSD_GMONTH}, //FIXME: an old-standing bug in the spec results in incorrect serialization.

					{pFactory.newGDay(30),name.XSD_GDAY},

					{pFactory.newGMonthDay(11,07),name.XSD_GMONTH_DAY},  // month 0-11


					{pFactory.newDuration(12225),name.XSD_DURATION},
					{pFactory.newDuration(1222),name.XSD_DURATION},

					{pFactory.newDuration("P2Y6M"),name.XSD_YEAR_MONTH_DURATION}, //FIXME: not in xml 1.0

					{pFactory.newDuration("P2147483647DT2147483647H2147483647M123456789012345.123456789012345S"),name.XSD_DAY_TIME_DURATION},

					{ new byte[] {0,1,2,34,5,6}, name.XSD_HEX_BINARY},
					{ new byte[] {0,1,2,34,5,6}, name.XSD_BASE64_BINARY},
					{ new byte[1023], name.XSD_BASE64_BINARY},

					{"en",name.XSD_LANGUAGE},
					{"normal",name.XSD_NORMALIZED_STRING},
					{"TOK",name.XSD_TOKEN},
					{"NMTOK",name.XSD_NMTOKEN},
					{"name",name.XSD_NAME},
					{"NCName",name.XSD_NCNAME}

				//	{createXMLLiteral(),name.RDF_LITERAL}

			};



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
	public void addOthers(HasOther ho, QualifiedName elementName) {
		for (Object [] pair: attributeValues) {
			Object value=pair[0];
			QualifiedName type=(QualifiedName) pair[1];
			if (value instanceof QualifiedName) {
				QualifiedName qq=(QualifiedName)value;
                if ((qq.getPrefix() == null)
                        ||
                        ((!qq.getPrefix().equals(elementName.getPrefix())))
                        ||
                        (qq.getNamespaceURI().equals(elementName.getNamespaceURI()))) {
                            ho.getOther().add(pFactory.newOther(elementName, value, type));
                        }

			} else {
				ho.getOther().add(pFactory.newOther(elementName, value, type));
			}
		}
	}

	public void doEntityWithOneTypeAttribute(int i)  {
		Entity a = pFactory.newEntity(q("et" + i));
		Object [] pair= attributeValues[i];
		Object value=pair[0];
		QualifiedName type=(QualifiedName) pair[1];
		a.getType().add(pFactory.newType(value,type));
		makeDocAndTest(a,"target/attr_entity_one_attr"+i);
	}


	public void doTestEntityWithOneValueAttribute(int i)  {
		Entity a = pFactory.newEntity(q("en_v" + i));


		Object [] pair= attributeValues[i];
		Object value=pair[0];
		QualifiedName type=(QualifiedName) pair[1];
		a.setValue(pFactory.newValue(value,type));
		makeDocAndTest(a,"target/attr_entity_one_value_attr"+i);
	}

	public void doTestAssociationWithOneRoleAttribute(int i)  {
		WasAssociatedWith a = pFactory.newWasAssociatedWith(q("ass_r" + i),
				q("a1"),
				q("ag1"));

		Object [] pair= attributeValues[i];
		Object value=pair[0];
		QualifiedName type=(QualifiedName) pair[1];
		a.getRole().add(pFactory.newRole(value,type));
		makeDocAndTest(a,"target/attr_association_one_role_attr"+i);
	}

	public void doTestEntityWithOneLocationAttribute(int i)  {
		Entity a = pFactory.newEntity(q("en_l" + i));
		Object [] pair= attributeValues[i];
		Object value=pair[0];
		QualifiedName type=(QualifiedName) pair[1];
		a.getLocation().add(pFactory.newLocation(value,type));
		makeDocAndTest(a,"target/attr_entity_one_location_attr"+i);
	}

	public void doTestEntityWithOneOtherAttribute(int i)  {
		Entity a = pFactory.newEntity(q("en_o" + i));


		Object [] pair= attributeValues[i];
		Object value=pair[0];
		QualifiedName type=(QualifiedName) pair[1];
		a.getOther().add(pFactory.newOther(pFactory.newQualifiedName(EX_NS,  "tag2", "exo"), value,type));
		makeDocAndTest(a,"target/attr_entity_one_other_attr"+i);
	}

	// VALUE
	public void testEntityWithOneValueAttribute0 ()  {
		doTestEntityWithOneValueAttribute(0);
	}
	public void testEntityWithOneValueAttribute1 ()  {
		doTestEntityWithOneValueAttribute(1);
	}
	public void testEntityWithOneValueAttribute2 ()  {
		doTestEntityWithOneValueAttribute(2);
	}
	public void testEntityWithOneValueAttribute3 ()  {
		doTestEntityWithOneValueAttribute(3);
	}
	public void testEntityWithOneValueAttribute4 ()  {
		doTestEntityWithOneValueAttribute(4);
	}
	public void testEntityWithOneValueAttribute5 ()  {
		doTestEntityWithOneValueAttribute(5);
	}
	public void testEntityWithOneValueAttribute6 ()  {
		doTestEntityWithOneValueAttribute(6);
	}
	public void testEntityWithOneValueAttribute7 ()  {
		doTestEntityWithOneValueAttribute(7);
	}
	public void testEntityWithOneValueAttribute8 ()  {
		doTestEntityWithOneValueAttribute(8);
	}
	public void testEntityWithOneValueAttribute9 ()  {
		doTestEntityWithOneValueAttribute(9);
	}
	public void testEntityWithOneValueAttribute10 ()  {
		doTestEntityWithOneValueAttribute(10);
	}
	public void testEntityWithOneValueAttribute11 ()  {
		doTestEntityWithOneValueAttribute(11);
	}
	public void testEntityWithOneValueAttribute12 ()  {
		doTestEntityWithOneValueAttribute(12);
	}
	public void testEntityWithOneValueAttribute13 ()  {
		doTestEntityWithOneValueAttribute(13);
	}
	public void testEntityWithOneValueAttribute14 ()  {
		doTestEntityWithOneValueAttribute(14);
	}
	public void testEntityWithOneValueAttribut15 ()  {
		doTestEntityWithOneValueAttribute(15);
	}
	public void testEntityWithOneValueAttribute16 ()  {
		doTestEntityWithOneValueAttribute(16);
	}
	public void testEntityWithOneValueAttribute17 ()  {
		doTestEntityWithOneValueAttribute(17);
	}
	public void testEntityWithOneValueAttribute18 ()  {
		doTestEntityWithOneValueAttribute(18);
	}
	public void testEntityWithOneValueAttribute19 ()  {
		doTestEntityWithOneValueAttribute(19);
	}
	public void testEntityWithOneValueAttribute20 ()  {
		doTestEntityWithOneValueAttribute(20);
	}
	public void testEntityWithOneValueAttribute21 ()  {
		doTestEntityWithOneValueAttribute(21);
	}
	public void testEntityWithOneValueAttribute22 ()  {
		doTestEntityWithOneValueAttribute(22);
	}
	public void testEntityWithOneValueAttribute23 ()  {
		doTestEntityWithOneValueAttribute(23);
	}
	public void testEntityWithOneValueAttribute24 ()  {
		doTestEntityWithOneValueAttribute(24);
	}
	public void testEntityWithOneValueAttribute25 ()  {
		doTestEntityWithOneValueAttribute(25);
	}
	public void testEntityWithOneValueAttribute26 ()  {
		doTestEntityWithOneValueAttribute(26);
	}
	public void testEntityWithOneValueAttribute27 ()  {
		doTestEntityWithOneValueAttribute(27);
	}
	public void testEntityWithOneValueAttribute28 ()  {
		doTestEntityWithOneValueAttribute(28);
	}
	public void testEntityWithOneValueAttribute29 ()  {
		doTestEntityWithOneValueAttribute(29);
	}
	public void testEntityWithOneValueAttribute30 ()  {
		doTestEntityWithOneValueAttribute(30);
	}
	public void testEntityWithOneValueAttribute31 ()  {
		doTestEntityWithOneValueAttribute(31);
	}
	public void testEntityWithOneValueAttribute32 ()  {
		doTestEntityWithOneValueAttribute(32);
	}
	public void testEntityWithOneValueAttribute33 ()  {
		doTestEntityWithOneValueAttribute(33);
	}
	public void testEntityWithOneValueAttribute34 ()  {
		doTestEntityWithOneValueAttribute(34);
	}
	public void testEntityWithOneValueAttribute35 ()  {
		doTestEntityWithOneValueAttribute(35);
	}
	public void testEntityWithOneValueAttribute36 ()  {
		doTestEntityWithOneValueAttribute(36);
	}
	public void testEntityWithOneValueAttribute37 ()  {
		doTestEntityWithOneValueAttribute(37);
	}
	public void testEntityWithOneValueAttribute38 ()  {
		doTestEntityWithOneValueAttribute(38);
	}
	public void testEntityWithOneValueAttribute39 ()  {
		doTestEntityWithOneValueAttribute(39);
	}
	public void testEntityWithOneValueAttribute40 ()  {
		doTestEntityWithOneValueAttribute(40);
	}
	public void testEntityWithOneValueAttribute41 ()  {
		doTestEntityWithOneValueAttribute(41);
	}
	public void testEntityWithOneValueAttribute42 ()  {
		doTestEntityWithOneValueAttribute(42);
	}


	// LOCATION
	public void testEntityWithOneLocationAttribute0 ()  {
		doTestEntityWithOneLocationAttribute(0);
	}
	public void testEntityWithOneLocationAttribute1 ()  {
		doTestEntityWithOneLocationAttribute(1);
	}
	public void testEntityWithOneLocationAttribute2 ()  {
		doTestEntityWithOneLocationAttribute(2);
	}
	public void testEntityWithOneLocationAttribute3 ()  {
		doTestEntityWithOneLocationAttribute(3);
	}
	public void testEntityWithOneLocationAttribute4 ()  {
		doTestEntityWithOneLocationAttribute(4);
	}
	public void testEntityWithOneLocationAttribute5 ()  {
		doTestEntityWithOneLocationAttribute(5);
	}
	public void testEntityWithOneLocationAttribute6 ()  {
		doTestEntityWithOneLocationAttribute(6);
	}
	public void testEntityWithOneLocationAttribute7 ()  {
		doTestEntityWithOneLocationAttribute(7);
	}
	public void testEntityWithOneLocationAttribute8 ()  {
		doTestEntityWithOneLocationAttribute(8);
	}
	public void testEntityWithOneLocationAttribute9 ()  {
		doTestEntityWithOneLocationAttribute(9);
	}
	public void testEntityWithOneLocationAttribute10 ()  {
		doTestEntityWithOneLocationAttribute(10);
	}
	public void testEntityWithOneLocationAttribute11 ()  {
		doTestEntityWithOneLocationAttribute(11);
	}
	public void testEntityWithOneLocationAttribute12 ()  {
		doTestEntityWithOneLocationAttribute(12);
	}
	public void testEntityWithOneLocationAttribute13 ()  {
		doTestEntityWithOneLocationAttribute(13);
	}
	public void testEntityWithOneLocationAttribute14 ()  {
		doTestEntityWithOneLocationAttribute(14);
	}
	public void testEntityWithOneLocationAttribut15 ()  {
		doTestEntityWithOneLocationAttribute(15);
	}
	public void testEntityWithOneLocationAttribute16 ()  {
		doTestEntityWithOneLocationAttribute(16);
	}
	public void testEntityWithOneLocationAttribute17 ()  {
		doTestEntityWithOneLocationAttribute(17);
	}
	public void testEntityWithOneLocationAttribute18 ()  {
		doTestEntityWithOneLocationAttribute(18);
	}
	public void testEntityWithOneLocationAttribute19 ()  {
		doTestEntityWithOneLocationAttribute(19);
	}
	public void testEntityWithOneLocationAttribute20 ()  {
		doTestEntityWithOneLocationAttribute(20);
	}
	public void testEntityWithOneLocationAttribute21 ()  {
		doTestEntityWithOneLocationAttribute(21);
	}
	public void testEntityWithOneLocationAttribute22 ()  {
		doTestEntityWithOneLocationAttribute(22);
	}
	public void testEntityWithOneLocationAttribute23 ()  {
		doTestEntityWithOneLocationAttribute(23);
	}
	public void testEntityWithOneLocationAttribute24 ()  {
		doTestEntityWithOneLocationAttribute(24);
	}
	public void testEntityWithOneLocationAttribute25 ()  {
		doTestEntityWithOneLocationAttribute(25);
	}
	public void testEntityWithOneLocationAttribute26 ()  {
		doTestEntityWithOneLocationAttribute(26);
	}
	public void testEntityWithOneLocationAttribute27 ()  {
		doTestEntityWithOneLocationAttribute(27);
	}
	public void testEntityWithOneLocationAttribute28 ()  {
		doTestEntityWithOneLocationAttribute(28);
	}
	public void testEntityWithOneLocationAttribute29 ()  {
		doTestEntityWithOneLocationAttribute(29);
	}
	public void testEntityWithOneLocationAttribute30 ()  {
		doTestEntityWithOneLocationAttribute(30);
	}
	public void testEntityWithOneLocationAttribute31 ()  {
		doTestEntityWithOneLocationAttribute(31);
	}
	public void testEntityWithOneLocationAttribute32 ()  {
		doTestEntityWithOneLocationAttribute(32);
	}
	public void testEntityWithOneLocationAttribute33 ()  {
		doTestEntityWithOneLocationAttribute(33);
	}
	public void testEntityWithOneLocationAttribute34 ()  {
		doTestEntityWithOneLocationAttribute(34);
	}
	public void testEntityWithOneLocationAttribute35 ()  {
		doTestEntityWithOneLocationAttribute(35);
	}
	public void testEntityWithOneLocationAttribute36 ()  {
		doTestEntityWithOneLocationAttribute(36);
	}
	public void testEntityWithOneLocationAttribute37 ()  {
		doTestEntityWithOneLocationAttribute(37);
	}
	public void testEntityWithOneLocationAttribute38 ()  {
		doTestEntityWithOneLocationAttribute(38);
	}
	public void testEntityWithOneLocationAttribute39 ()  {
		doTestEntityWithOneLocationAttribute(39);
	}
	public void testEntityWithOneLocationAttribute40 ()  {
		doTestEntityWithOneLocationAttribute(40);
	}
	public void testEntityWithOneLocationAttribute41 ()  {
		doTestEntityWithOneLocationAttribute(41);
	}
	public void testEntityWithOneLocationAttribute42 ()  {
		doTestEntityWithOneLocationAttribute(42);
	}


	// OTHER
	public void testEntityWithOneOtherAttribute0 ()  {
		doTestEntityWithOneOtherAttribute(0);
	}
	public void testEntityWithOneOtherAttribute1 ()  {
		doTestEntityWithOneOtherAttribute(1);
	}
	public void testEntityWithOneOtherAttribute2 ()  {
		doTestEntityWithOneOtherAttribute(2);
	}
	public void testEntityWithOneOtherAttribute3 ()  {
		doTestEntityWithOneOtherAttribute(3);
	}
	public void testEntityWithOneOtherAttribute4 ()  {
		doTestEntityWithOneOtherAttribute(4);
	}
	public void testEntityWithOneOtherAttribute5 ()  {
		doTestEntityWithOneOtherAttribute(5);
	}
	public void testEntityWithOneOtherAttribute6 ()  {
		doTestEntityWithOneOtherAttribute(6);
	}
	public void testEntityWithOneOtherAttribute7 ()  {
		doTestEntityWithOneOtherAttribute(7);
	}
	public void testEntityWithOneOtherAttribute8 ()  {
		doTestEntityWithOneOtherAttribute(8);
	}
	public void testEntityWithOneOtherAttribute9 ()  {
		doTestEntityWithOneOtherAttribute(9);
	}
	public void testEntityWithOneOtherAttribute10 ()  {
		doTestEntityWithOneOtherAttribute(10);
	}
	public void testEntityWithOneOtherAttribute11 ()  {
		doTestEntityWithOneOtherAttribute(11);
	}
	public void testEntityWithOneOtherAttribute12 ()  {
		doTestEntityWithOneOtherAttribute(12);
	}
	public void testEntityWithOneOtherAttribute13 ()  {
		doTestEntityWithOneOtherAttribute(13);
	}
	public void testEntityWithOneOtherAttribute14 ()  {
		doTestEntityWithOneOtherAttribute(14);
	}
	public void testEntityWithOneOtherAttribut15 ()  {
		doTestEntityWithOneOtherAttribute(15);
	}
	public void testEntityWithOneOtherAttribute16 ()  {
		doTestEntityWithOneOtherAttribute(16);
	}
	public void testEntityWithOneOtherAttribute17 ()  {
		doTestEntityWithOneOtherAttribute(17);
	}
	public void testEntityWithOneOtherAttribute18 ()  {
		doTestEntityWithOneOtherAttribute(18);
	}
	public void testEntityWithOneOtherAttribute19 ()  {
		doTestEntityWithOneOtherAttribute(19);
	}
	public void testEntityWithOneOtherAttribute20 ()  {
		doTestEntityWithOneOtherAttribute(20);
	}
	public void testEntityWithOneOtherAttribute21 ()  {
		doTestEntityWithOneOtherAttribute(21);
	}
	public void testEntityWithOneOtherAttribute22 ()  {
		doTestEntityWithOneOtherAttribute(22);
	}
	public void testEntityWithOneOtherAttribute23 ()  {
		doTestEntityWithOneOtherAttribute(23);
	}
	public void testEntityWithOneOtherAttribute24 ()  {
		doTestEntityWithOneOtherAttribute(24);
	}
	public void testEntityWithOneOtherAttribute25 ()  {
		doTestEntityWithOneOtherAttribute(25);
	}
	public void testEntityWithOneOtherAttribute26 ()  {
		doTestEntityWithOneOtherAttribute(26);
	}
	public void testEntityWithOneOtherAttribute27 ()  {
		doTestEntityWithOneOtherAttribute(27);
	}
	public void testEntityWithOneOtherAttribute28 ()  {
		doTestEntityWithOneOtherAttribute(28);
	}
	public void testEntityWithOneOtherAttribute29 ()  {
		doTestEntityWithOneOtherAttribute(29);
	}
	public void testEntityWithOneOtherAttribute30 ()  {
		doTestEntityWithOneOtherAttribute(30);
	}
	public void testEntityWithOneOtherAttribute31 ()  {
		doTestEntityWithOneOtherAttribute(31);
	}
	public void testEntityWithOneOtherAttribute32 ()  {
		doTestEntityWithOneOtherAttribute(32);
	}
	public void testEntityWithOneOtherAttribute33 ()  {
		doTestEntityWithOneOtherAttribute(33);
	}
	public void testEntityWithOneOtherAttribute34 ()  {
		doTestEntityWithOneOtherAttribute(34);
	}
	public void testEntityWithOneOtherAttribute35 ()  {
		doTestEntityWithOneOtherAttribute(35);
	}
	public void testEntityWithOneOtherAttribute36 ()  {
		doTestEntityWithOneOtherAttribute(36);
	}
	public void testEntityWithOneOtherAttribute37 ()  {
		doTestEntityWithOneOtherAttribute(37);
	}
	public void testEntityWithOneOtherAttribute38 ()  {
		doTestEntityWithOneOtherAttribute(38);
	}
	public void testEntityWithOneOtherAttribute39 ()  {
		doTestEntityWithOneOtherAttribute(39);
	}
	public void testEntityWithOneOtherAttribute40 ()  {
		doTestEntityWithOneOtherAttribute(40);
	}
	public void testEntityWithOneOtherAttribute41 ()  {
		doTestEntityWithOneOtherAttribute(41);
	}
	public void testEntityWithOneOtherAttribute42 ()  {
		doTestEntityWithOneOtherAttribute(42);
	}

	// TYPE

	public void testEntityWithOneAttribute0 ()  {
		doEntityWithOneTypeAttribute(0);
	}
	public void testEntityWithOneAttribute1 ()  {
		doEntityWithOneTypeAttribute(1);
	}
	public void testEntityWithOneAttribute2 ()  {
		doEntityWithOneTypeAttribute(2);
	}
	public void testEntityWithOneAttribute3 ()  {
		doEntityWithOneTypeAttribute(3);
	}
	public void testEntityWithOneAttribute4 ()  {
		doEntityWithOneTypeAttribute(4);
	}
	public void testEntityWithOneAttribute5 ()  {
		doEntityWithOneTypeAttribute(5);
	}
	public void testEntityWithOneAttribute6 ()  {
		doEntityWithOneTypeAttribute(6);
	}
	public void testEntityWithOneAttribute7 ()  {
		doEntityWithOneTypeAttribute(7);
	}
	public void testEntityWithOneAttribute8 ()  {
		doEntityWithOneTypeAttribute(8);
	}
	public void testEntityWithOneAttribute9 ()  {
		doEntityWithOneTypeAttribute(0);
	}
	public void testEntityWithOneAttribute10 ()  {
		doEntityWithOneTypeAttribute(10);
	}
	public void testEntityWithOneAttribute11 ()  {
		doEntityWithOneTypeAttribute(11);
	}
	public void testEntityWithOneAttribute12 ()  {
		doEntityWithOneTypeAttribute(12);
	}
	public void testEntityWithOneAttribute13 ()  {
		doEntityWithOneTypeAttribute(13);
	}
	public void testEntityWithOneAttribute14 ()  {
		doEntityWithOneTypeAttribute(14);
	}
	public void testEntityWithOneAttribute15 ()  {
		doEntityWithOneTypeAttribute(15);
	}
	public void testEntityWithOneAttribute16 ()  {
		doEntityWithOneTypeAttribute(16);
	}
	public void testEntityWithOneAttribute17 ()  {
		doEntityWithOneTypeAttribute(17);
	}
	public void testEntityWithOneAttribute18 ()  {
		doEntityWithOneTypeAttribute(18);
	}
	public void testEntityWithOneAttribute19 ()  {
		doEntityWithOneTypeAttribute(19);
	}
	public void testEntityWithOneAttribute20 ()  {
		doEntityWithOneTypeAttribute(20);
	}
	public void testEntityWithOneAttribute21 ()  {
		doEntityWithOneTypeAttribute(21);
	}
	public void testEntityWithOneAttribute22 ()  {
		doEntityWithOneTypeAttribute(22);
	}
	public void testEntityWithOneAttribute23 ()  {
		doEntityWithOneTypeAttribute(23);
	}
	public void testEntityWithOneAttribute24 ()  {
		doEntityWithOneTypeAttribute(24);
	}
	public void testEntityWithOneAttribute25 ()  {
		doEntityWithOneTypeAttribute(25);
	}
	public void testEntityWithOneAttribute26 ()  {
		doEntityWithOneTypeAttribute(26);
	}
	public void testEntityWithOneAttribute27 ()  {
		doEntityWithOneTypeAttribute(27);
	}
	public void testEntityWithOneAttribute28 ()  {
		doEntityWithOneTypeAttribute(28);
	}
	public void testEntityWithOneAttribute29 ()  {
		doEntityWithOneTypeAttribute(29);
	}
	public void testEntityWithOneAttribute30 ()  {
		doEntityWithOneTypeAttribute(30);
	}
	public void testEntityWithOneAttribute31 ()  {
		doEntityWithOneTypeAttribute(31);
	}
	public void testEntityWithOneAttribute32 ()  {
		doEntityWithOneTypeAttribute(32);
	}
	public void testEntityWithOneAttribute33()  {
		doEntityWithOneTypeAttribute(33);
	}
	public void testEntityWithOneAttribute34 ()  {
		doEntityWithOneTypeAttribute(34);
	}
	public void testEntityWithOneAttribute35 ()  {
		doEntityWithOneTypeAttribute(35);
	}
	public void testEntityWithOneAttribute36 ()  {
		doEntityWithOneTypeAttribute(36);
	}
	public void testEntityWithOneAttribute37 ()  {
		doEntityWithOneTypeAttribute(37);
	}
	public void testEntityWithOneAttribute38 ()  {
		doEntityWithOneTypeAttribute(38);
	}
	public void testEntityWithOneAttribute39 ()  {
		doEntityWithOneTypeAttribute(39);
	}
	public void testEntityWithOneAttribute40 ()  {
		doEntityWithOneTypeAttribute(40);
	}
	public void testEntityWithOneAttribute41 ()  {
		doEntityWithOneTypeAttribute(41);
	}
	public void testEntityWithOneAttribute42 ()  {
		doEntityWithOneTypeAttribute(42);
	}


	public void testAssociationWithOneRoleAttribute0 ()  {
		doTestAssociationWithOneRoleAttribute(0);
	}
	public void testAssociationWithOneRoleAttribute1 ()  {
		doTestAssociationWithOneRoleAttribute(1);
	}
	public void testAssociationWithOneRoleAttribute2 ()  {
		doTestAssociationWithOneRoleAttribute(2);
	}
	public void testAssociationWithOneRoleAttribute3 ()  {
		doTestAssociationWithOneRoleAttribute(3);
	}
	public void testAssociationWithOneRoleAttribute4 ()  {
		doTestAssociationWithOneRoleAttribute(4);
	}
	public void testAssociationWithOneRoleAttribute5 ()  {
		doTestAssociationWithOneRoleAttribute(5);
	}
	public void testAssociationWithOneRoleAttribute6 ()  {
		doTestAssociationWithOneRoleAttribute(6);
	}
	public void testAssociationWithOneRoleAttribute7 ()  {
		doTestAssociationWithOneRoleAttribute(7);
	}
	public void testAssociationWithOneRoleAttribute8 ()  {
		doTestAssociationWithOneRoleAttribute(8);
	}
	public void testAssociationWithOneRoleAttribute9 ()  {
		doTestAssociationWithOneRoleAttribute(9);
	}
	public void testAssociationWithOneRoleAttribute10 ()  {
		doTestAssociationWithOneRoleAttribute(10);
	}
	public void testAssociationWithOneRoleAttribute11 ()  {
		doTestAssociationWithOneRoleAttribute(11);
	}
	public void testAssociationWithOneRoleAttribute12 ()  {
		doTestAssociationWithOneRoleAttribute(12);
	}
	public void testAssociationWithOneRoleAttribute13 ()  {
		doTestAssociationWithOneRoleAttribute(13);
	}
	public void testAssociationWithOneRoleAttribute14 ()  {
		doTestAssociationWithOneRoleAttribute(14);
	}
	public void testAssociationWithOneRoleAttribute15 ()  {
		doTestAssociationWithOneRoleAttribute(15);
	}
	public void testAssociationWithOneRoleAttribute16 ()  {
		doTestAssociationWithOneRoleAttribute(16);
	}
	public void testAssociationWithOneRoleAttribute17 ()  {
		doTestAssociationWithOneRoleAttribute(17);
	}
	public void testAssociationWithOneRoleAttribute18 ()  {
		doTestAssociationWithOneRoleAttribute(18);
	}
	public void testAssociationWithOneRoleAttribute19 ()  {
		doTestAssociationWithOneRoleAttribute(19);
	}
	public void testAssociationWithOneRoleAttribute20 ()  {
		doTestAssociationWithOneRoleAttribute(20);
	}
	public void testAssociationWithOneRoleAttribute21 ()  {
		doTestAssociationWithOneRoleAttribute(21);
	}
	public void testAssociationWithOneRoleAttribute22 ()  {
		doTestAssociationWithOneRoleAttribute(22);
	}
	public void testAssociationWithOneRoleAttribute23 ()  {
		doTestAssociationWithOneRoleAttribute(23);
	}
	public void testAssociationWithOneRoleAttribute24 ()  {
		doTestAssociationWithOneRoleAttribute(24);
	}
	public void testAssociationWithOneRoleAttribute25 ()  {
		doTestAssociationWithOneRoleAttribute(25);
	}
	public void testAssociationWithOneRoleAttribute26 ()  {
		doTestAssociationWithOneRoleAttribute(26);
	}
	public void testAssociationWithOneRoleAttribute27 ()  {
		doTestAssociationWithOneRoleAttribute(27);
	}
	public void testAssociationWithOneRoleAttribute28 ()  {
		doTestAssociationWithOneRoleAttribute(28);
	}
	public void testAssociationWithOneRoleAttribute29 ()  {
		doTestAssociationWithOneRoleAttribute(29);
	}
	public void testAssociationWithOneRoleAttribute30 ()  {
		doTestAssociationWithOneRoleAttribute(30);
	}
	public void testAssociationWithOneRoleAttribute31 ()  {
		doTestAssociationWithOneRoleAttribute(31);
	}
	public void testAssociationWithOneRoleAttribute32 ()  {
		doTestAssociationWithOneRoleAttribute(32);
	}
	public void testAssociationWithOneRoleAttribute33 ()  {
		doTestAssociationWithOneRoleAttribute(33);
	}
	public void testAssociationWithOneRoleAttribute34 ()  {
		doTestAssociationWithOneRoleAttribute(34);
	}
	public void testAssociationWithOneRoleAttribute35 ()  {
		doTestAssociationWithOneRoleAttribute(35);
	}
	public void testAssociationWithOneRoleAttribute36 ()  {
		doTestAssociationWithOneRoleAttribute(36);
	}
	public void testAssociationWithOneRoleAttribute37 ()  {
		doTestAssociationWithOneRoleAttribute(37);
	}
	public void testAssociationWithOneRoleAttribute38 ()  {
		doTestAssociationWithOneRoleAttribute(38);
	}
	public void testAssociationWithOneRoleAttribute39 ()  {
		doTestAssociationWithOneRoleAttribute(39);
	}
	public void testAssociationWithOneRoleAttribute40 ()  {
		doTestAssociationWithOneRoleAttribute(40);
	}
	public void testAssociationWithOneRoleAttribute41 ()  {
		doTestAssociationWithOneRoleAttribute(41);
	}
	public void testAssociationWithOneRoleAttribute42 ()  {
		doTestAssociationWithOneRoleAttribute(42);
	}



	public void testEntity0()  {
		Entity a = pFactory.newEntity(q("e0"));

		addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
		addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

		addLabels(a);
		addTypes(a);
		addLocations(a);

		makeDocAndTest(a,"target/attr_agent0");
	}


	public QualifiedName q(String n) {
		return new org.openprovenance.prov.vanilla.QualifiedName(EX_NS, n, EX_PREFIX);
	}

	public void testGeneration0()  {
		WasGeneratedBy a = pFactory.newWasGeneratedBy((QualifiedName)null,
				q("e1"),
				null,
				q("a1"));

		addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag2", EX_PREFIX));
		addOthers(a, pFactory.newQualifiedName(EX_NS,  "tag3", EX2_PREFIX));
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
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
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag4", EX4_PREFIX));
		addOthers(a, pFactory.newQualifiedName(EX2_NS, "tag5", EX_PREFIX));

		addLabels(a);
		addTypes(a);


		makeDocAndTest(a,"target/attr_delegation0");
	}



	public void doTestDictionaryInsertionWithOneKey(int i)  {
		// Dictionary not supported in vanilla PROV model
		if (false) {
			Object[] pair = attributeValues[i];
			Object value = pair[0];
			QualifiedName type = (QualifiedName) pair[1];

			List<Entry> ll = new LinkedList<Entry>();

			Entry p1 = pFactory.newEntry(pFactory.newKey(value, type),
					q("e0"));
			ll.add(p1);
			DerivedByInsertionFrom a = pFactory.newDerivedByInsertionFrom(q("dins_" + i),
					q("d2"),
					q("d1"),
					ll, null);


			makeDocAndTest(a, "target/attr_dict_insert_one_key" + i);
		}
	}

	public void testDictionaryInsertionWithOneKey0() {
		doTestDictionaryInsertionWithOneKey(0);
	}
	public void testDictionaryInsertionWithOneKey1() {
		doTestDictionaryInsertionWithOneKey(1);
	}
	public void testDictionaryInsertionWithOneKey2() {
		doTestDictionaryInsertionWithOneKey(2);
	}
	public void testDictionaryInsertionWithOneKey3() {
		doTestDictionaryInsertionWithOneKey(3);
	}
	public void testDictionaryInsertionWithOneKey4() {
		doTestDictionaryInsertionWithOneKey(4);
	}
	public void testDictionaryInsertionWithOneKey5() {
		doTestDictionaryInsertionWithOneKey(5);
	}
	public void testDictionaryInsertionWithOneKey6() {
		doTestDictionaryInsertionWithOneKey(6);
	}
	public void testDictionaryInsertionWithOneKey7() {
		doTestDictionaryInsertionWithOneKey(7);
	}
	public void testDictionaryInsertionWithOneKey8() {
		doTestDictionaryInsertionWithOneKey(8);
	}
	public void testDictionaryInsertionWithOneKey9() {
		doTestDictionaryInsertionWithOneKey(9);
	}
	public void testDictionaryInsertionWithOneKey10() {
		doTestDictionaryInsertionWithOneKey(10);
	}
	public void testDictionaryInsertionWithOneKey11() {
		doTestDictionaryInsertionWithOneKey(11);
	}
	public void testDictionaryInsertionWithOneKey12() {
		doTestDictionaryInsertionWithOneKey(12);
	}
	public void testDictionaryInsertionWithOneKey13() {
		doTestDictionaryInsertionWithOneKey(13);
	}
	public void testDictionaryInsertionWithOneKey14() {
		doTestDictionaryInsertionWithOneKey(14);
	}
	public void testDictionaryInsertionWithOneKey15() {
		doTestDictionaryInsertionWithOneKey(15);
	}
	public void testDictionaryInsertionWithOneKey16() {
		doTestDictionaryInsertionWithOneKey(16);
	}
	public void testDictionaryInsertionWithOneKey17() {
		doTestDictionaryInsertionWithOneKey(17);
	}
	public void testDictionaryInsertionWithOneKey18() {
		doTestDictionaryInsertionWithOneKey(18);
	}
	public void testDictionaryInsertionWithOneKey19() {
		doTestDictionaryInsertionWithOneKey(19);
	}
	public void testDictionaryInsertionWithOneKey20() {
		doTestDictionaryInsertionWithOneKey(20);
	}
	public void testDictionaryInsertionWithOneKey21() {
		doTestDictionaryInsertionWithOneKey(21);
	}
	public void testDictionaryInsertionWithOneKey22() {
		doTestDictionaryInsertionWithOneKey(22);
	}
	public void testDictionaryInsertionWithOneKey23() {
		doTestDictionaryInsertionWithOneKey(23);
	}
	public void testDictionaryInsertionWithOneKey24() {
		doTestDictionaryInsertionWithOneKey(24);
	}
	public void testDictionaryInsertionWithOneKey25() {
		doTestDictionaryInsertionWithOneKey(25);
	}
	public void testDictionaryInsertionWithOneKey26() {
		doTestDictionaryInsertionWithOneKey(26);
	}
	public void testDictionaryInsertionWithOneKey27() {
		doTestDictionaryInsertionWithOneKey(27);
	}
	public void testDictionaryInsertionWithOneKey28() {
		doTestDictionaryInsertionWithOneKey(28);
	}
	public void testDictionaryInsertionWithOneKey29() {
		doTestDictionaryInsertionWithOneKey(29);
	}
	public void testDictionaryInsertionWithOneKey30() {
		doTestDictionaryInsertionWithOneKey(30);
	}
	public void testDictionaryInsertionWithOneKey31() {
		doTestDictionaryInsertionWithOneKey(31);
	}
	public void testDictionaryInsertionWithOneKey32() {
		doTestDictionaryInsertionWithOneKey(32);
	}
	public void testDictionaryInsertionWithOneKey33() {
		doTestDictionaryInsertionWithOneKey(33);
	}
	public void testDictionaryInsertionWithOneKey34() {
		doTestDictionaryInsertionWithOneKey(34);
	}
	public void testDictionaryInsertionWithOneKey35() {
		doTestDictionaryInsertionWithOneKey(35);
	}
	public void testDictionaryInsertionWithOneKey36() {
		doTestDictionaryInsertionWithOneKey(36);
	}
	public void testDictionaryInsertionWithOneKey37() {
		doTestDictionaryInsertionWithOneKey(37);
	}
	public void testDictionaryInsertionWithOneKey38() {
		doTestDictionaryInsertionWithOneKey(38);
	}
	public void testDictionaryInsertionWithOneKey39() {
		doTestDictionaryInsertionWithOneKey(39);
	}
	public void testDictionaryInsertionWithOneKey40() {
		doTestDictionaryInsertionWithOneKey(40);
	}
	public void testDictionaryInsertionWithOneKey41() {
		doTestDictionaryInsertionWithOneKey(41);
	}
	public void testDictionaryInsertionWithOneKey42() {
		doTestDictionaryInsertionWithOneKey(42);
	}

}
