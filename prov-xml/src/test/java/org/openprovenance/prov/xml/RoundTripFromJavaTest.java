package org.openprovenance.prov.xml;

import java.io.File;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Hashtable;
import java.net.URI;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import junit.framework.TestCase;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class RoundTripFromJavaTest extends TestCase {

    public static final String EX_NS = "http://example.org/";
    public static final String EX2_NS = "http://example2.org/";
    public static final String EX_PREFIX = "ex";
    public static final String EX2_PREFIX = "ex2";
     
    static final ProvUtilities util=new ProvUtilities();


    static final Hashtable<String, String> namespaces;

    public static ProvFactory pFactory;
    
    static Hashtable<String, String> updateNamespaces (Hashtable<String, String> nss) {
        nss.put(EX_PREFIX, EX_NS);
        nss.put(EX2_PREFIX, EX2_NS);
	return nss;
    }
    
    
    static {
	namespaces = updateNamespaces(new Hashtable<String, String>());
	pFactory = new ProvFactory(namespaces);
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
    public void makeDocAndTest(Statement stment, String file, Statement[] opt, boolean check) {
	Document doc = pFactory.newDocument();
	doc.getEntityOrActivityOrWasGeneratedBy().add(stment);
	updateNamespaces(doc);
	
	String file1=(opt==null) ? file : file+"-S";
	compareDocAndFile(doc, file1, check);
	
	if (opt!=null) {
	    String file2=file+"-M";
            doc.getEntityOrActivityOrWasGeneratedBy().addAll(Arrays.asList(opt));
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
   	ht.getType().add("a");
   	ht.getType().add(1);
   	ht.getType().add(1.0);
   	ht.getType().add(true);
   	ht.getType().add(new QName(EX_NS, "abc", EX_PREFIX));
   	ht.getType().add(pFactory.newTimeNow());
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"hello"));
   	ht.getType().add(w);
    }
    public void addLocations(HasLocation hl) {
   	hl.getLocation().add("London");
   	hl.getLocation().add(1);
   	hl.getLocation().add(1.0);
   	hl.getLocation().add(true);
   	hl.getLocation().add(new QName(EX_NS, "london", EX_PREFIX));
   	hl.getLocation().add(pFactory.newTimeNow());
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"london"));
   	hl.getLocation().add(w);
    }
    public void addValue(HasValue hl) {
        hl.setValue(new QName(EX_NS, "avalue", EX_PREFIX));
    }

    

    
    public void addFurtherAttributes(HasExtensibility he) {
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag1",EX_PREFIX,"hello"));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, "bye"));
	//he.getAny().add(pFactory.newAttribute(EX_NS,"tag2",EX_PREFIX, pFactory.newInternationalizedString("bonjour","FR"), "xsd:string"));
	he.getAny().add(pFactory.newAttribute(EX2_NS,"tag3",EX2_PREFIX, "hi"));
	
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Integer(1)));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Long(1)));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Short((short) 1)));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Double(1.0)));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Float(1.0)));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new java.math.BigDecimal(1.0)));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Boolean(true)));
	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new Byte((byte) 123)));
	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"london"));
   	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, w));
	
    }
    
    public void addFurtherLabelsPROBLEM(HasExtensibility he) {
	
   	he.getAny().add(pFactory.newAttribute(EX_NS,"tag",EX_PREFIX, new QName(EX_NS, "london", EX_PREFIX)));
	
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    
    public void testEntity1() throws JAXBException  {
	Entity a = pFactory.newEntity("ex:e1");
	makeDocAndTest(a,"target/entity1");
    }

    public void testEntity2() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e2", "entity2");
   	makeDocAndTest(a,"target/entity2");
    }

    public void testEntity3() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e3", "entity3");
   	addValue(a);
   	makeDocAndTest(a,"target/entity3");
    }

    public void testEntity4() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e4", "entity4");
	addLabels(a);
   	makeDocAndTest(a,"target/entity4");
    }
   
    
    public void testEntity5() throws JAXBException  {
   	Entity a = pFactory.newEntity("ex:e5", "entity5");
	addTypes(a);
   	makeDocAndTest(a,"target/entity5");
    }

    public void testEntity6() throws JAXBException  {
       	Entity a = pFactory.newEntity("ex:e6", "entity6");
	addLocations(a);
       	makeDocAndTest(a,"target/entity6");
    }
    public void testEntity7() throws JAXBException  {
       	Entity a = pFactory.newEntity("ex:e7", "entity7");
	addTypes(a);
	addLocations(a);
	addLabels(a);
       	makeDocAndTest(a,"target/entity7");
    }
    public void testEntity8() throws JAXBException  {
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
       	Entity a = pFactory.newEntity("ex:e9", "entity9");
	addTypes(a);
	addLocations(a);
	addLabels(a);
	addFurtherAttributes(a); 
       	makeDocAndTest(a,"target/entity9");
    }

    public void testEntity10() throws JAXBException  {
       	Entity a = pFactory.newEntity("ex:e10", "entity10");
	addTypes(a);
	addLocations(a);
	addLabels(a);
	addFurtherAttributes(a); 
	addFurtherLabelsPROBLEM(a);
       	makeDocAndTest(a,"target/entity10",false);
    }


    ///////////////////////////////////////////////////////////////////////
    
    
    public void testActivity1() throws JAXBException  {
	Activity a = pFactory.newActivity("ex:a1");
	makeDocAndTest(a,"target/activity1");
    }
    public void testActivity2() throws JAXBException  {
   	Activity a = pFactory.newActivity("ex:a2", "activity2");
   	makeDocAndTest(a,"target/activity2");
    }

    public void testActivity3() throws JAXBException  {
	Activity a = pFactory.newActivity("ex:a1");
	a.setStartTime(pFactory.newTimeNow());
	a.setEndTime(pFactory.newTimeNow());
	makeDocAndTest(a,"target/activity3");
    }

    public void testActivity4() throws JAXBException  {
   	Activity a = pFactory.newActivity("ex:a2", "activity2");
	addLabels(a);
   	makeDocAndTest(a,"target/activity4");
    }
    public void testActivity5() throws JAXBException  {
   	Activity a = pFactory.newActivity("ex:a2", "activity2");
	addTypes(a);
   	makeDocAndTest(a,"target/activity5");
    }
   
    
    public void testActivity6() throws JAXBException  {
   	Activity a = pFactory.newActivity("ex:a6", "activity6");
	addLocations(a);
   	makeDocAndTest(a,"target/activity6");
    }

    public void testActivity7() throws JAXBException  {
       	Activity a = pFactory.newActivity("ex:a7", "activity7");
	addTypes(a);
	addLocations(a);
	addLabels(a);
       	makeDocAndTest(a,"target/activity7");
    }
    public void testActivity8() throws JAXBException  {
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
       	Activity a = pFactory.newActivity("ex:a9", "activity9");
        addTypes(a);
        addLocations(a);
        addLabels(a);
        addFurtherAttributes(a);
       	makeDocAndTest(a,"target/activity9");
    }


    ///////////////////////////////////////////////////////////////////////
    
    
    public void testAgent1() throws JAXBException  {
	Agent a = pFactory.newAgent("ex:ag1");
	makeDocAndTest(a,"target/agent1");
    }
    public void testAgent2() throws JAXBException  {
   	Agent a = pFactory.newAgent("ex:ag2", "agent2");
   	makeDocAndTest(a,"target/agent2");
    }

    
    public void testAgent3() throws JAXBException  {
   	Agent a = pFactory.newAgent("ex:ag2", "agent2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	makeDocAndTest(a,"target/agent3");
    }
    public void testAgent4() throws JAXBException  {
   	Agent a = pFactory.newAgent("ex:ag2", "agent2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
   	makeDocAndTest(a,"target/agent4");
    }
    public void testAgent5() throws JAXBException  {
   	Agent a = pFactory.newAgent("ex:ag2", "agent2");
   	a.getLabel().add(pFactory.newInternationalizedString("hello"));
   	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
   	a.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
   	makeDocAndTest(a,"target/agent5");
    }
   
    
    public void testAgent6() throws JAXBException  {
   	Agent a = pFactory.newAgent("ex:ag6", "agent6");
   	a.getType().add("a");
   	a.getType().add(1);
   	a.getType().add(1.0);
   	a.getType().add(true);
   	a.getType().add(new QName(EX_NS, "abc", EX_PREFIX));
   	a.getType().add(pFactory.newTimeNow());
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"hello"));
   	a.getType().add(w);
   	makeDocAndTest(a,"target/agent6");
    }

    public void testAgent7() throws JAXBException  {
       	Agent a = pFactory.newAgent("ex:ag7", "agent7");
       	pFactory.addType(a,"a");
       	pFactory.addType(a,1);
       	pFactory.addType(a,1.0);
       	pFactory.addType(a,true);
       	pFactory.addType(a,new QName(EX_NS, "abc", EX_PREFIX));
       	pFactory.addType(a,pFactory.newTimeNow());
       	pFactory.addType(a,URI.create(EX_NS+"hello"));
       	a.getLabel().add(pFactory.newInternationalizedString("hello"));
       	a.getLabel().add(pFactory.newInternationalizedString("bye","EN"));
       	a.getLabel().add(pFactory.newInternationalizedString("bonjour","FR"));
   	a.getLocation().add("London");
   	a.getLocation().add(1);
   	a.getLocation().add(1.0);
   	a.getLocation().add(true);
   	a.getLocation().add(new QName(EX_NS, "london", EX_PREFIX));
   	a.getLocation().add(pFactory.newTimeNow());
   	URIWrapper w=new URIWrapper();
   	w.setValue(URI.create(EX_NS+"london"));
   	a.getLocation().add(w);
       	makeDocAndTest(a,"target/agent7");
    }
    public void testAgent8() throws JAXBException  {
       	Agent a = pFactory.newAgent("ex:ag8", "agent8");
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
   	a.getLocation().add("London");
   	a.getLocation().add("London");
   	a.getLocation().add(1);
   	a.getLocation().add(1);
   	a.getLocation().add(1.0);
   	a.getLocation().add(1.0);
   	a.getLocation().add(true);
   	a.getLocation().add(true);
   	a.getLocation().add(new QName(EX_NS, "london", EX_PREFIX));
   	a.getLocation().add(new QName(EX_NS, "london", EX_PREFIX));
   	a.getLocation().add(pFactory.newTimeNow());
   	a.getLocation().add(pFactory.newTimeNow());
   	URIWrapper w2=new URIWrapper();
   	w2.setValue(URI.create(EX_NS+"london"));
   	a.getLocation().add(w2);
   	a.getLocation().add(w2);

       	makeDocAndTest(a,"target/agent8");
    }


    ///////////////////////////////////////////////////////////////////////
    
    public QName q(String n) {
	return new QName(EX_NS, n, EX_PREFIX);
    }
    
    public void testGeneration1() throws JAXBException  {
	WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen1"),
							pFactory.newEntityRef(q("e1")),
							null,
							null);
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { e1 };
	makeDocAndTest(gen, opt , "target/generation1");
    }


    public void testGeneration2() throws JAXBException  {
	WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen2"),
							pFactory.newEntityRef(q("e1")),
							null,
							pFactory.newActivityRef(q("a1")));
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };

	makeDocAndTest(gen, opt, "target/generation2");
    }


    public void testGeneration3() throws JAXBException  {
	WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen3"),
							pFactory.newEntityRef(q("e1")),
							"somerole",
							pFactory.newActivityRef(q("a1")));
        gen.getRole().add("otherRole");
    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(gen,opt,"target/generation3");
    }


    public void testGeneration4() throws JAXBException  {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen4"),
                                                        pFactory.newEntityRef(q("e1")),
                                                        "somerole",
                                                        pFactory.newActivityRef(q("a1")));
        gen.setTime(pFactory.newTimeNow());
    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(gen,opt,"target/generation4");
    }
    
    public void testGeneration5() throws JAXBException  {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen4"),
                                                        pFactory.newEntityRef(q("e1")),
                                                        "somerole",
                                                        pFactory.newActivityRef(q("a1")));
        gen.setTime(pFactory.newTimeNow());
        addTypes(gen);
        addLocations(gen);
        addLabels(gen);
        addFurtherAttributes(gen);

    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(gen,opt,"target/generation5");
    }
    
    
    public void testGeneration6() throws JAXBException  {
  	WasGeneratedBy gen = pFactory.newWasGeneratedBy((QName)null,
  							pFactory.newEntityRef(q("e1")),
  							null,
  							pFactory.newActivityRef(q("a1")));
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
  	makeDocAndTest(gen,opt,"target/generation6");
      }

    public void testGeneration7() throws JAXBException  {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy((QName)null,
                                                        pFactory.newEntityRef(q("e1")),
                                                        "somerole",
                                                        pFactory.newActivityRef(q("a1")));
        gen.setTime(pFactory.newTimeNow());
        addTypes(gen);
        addLocations(gen);
        addLabels(gen);
        addFurtherAttributes(gen);

    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(gen,opt,"target/generation7");
    }
    
    //////////////////////////////////
    
    public void testUsage1() throws JAXBException  {
        Used use = pFactory.newUsed(q("use1"),
                                    null,
                                    null,
                                    pFactory.newEntityRef(q("e1")));
    	Entity e1=pFactory.newEntity(q("e1"));
    	Statement [] opt=new Statement[] { e1 };
        makeDocAndTest(use,opt,"target/usage1");
    }

    public void testUsage2() throws JAXBException  {
        Used use = pFactory.newUsed(q("use2"),
                                    pFactory.newActivityRef(q("a1")),
                                    null,
                                    pFactory.newEntityRef(q("e1")));
        
    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(use,opt,"target/usage2");
    }

    public void testUsage3() throws JAXBException  {
        Used use = pFactory.newUsed(q("use3"),
                                    pFactory.newActivityRef(q("a1")),
                                    "somerole",
                                    pFactory.newEntityRef(q("e1")));
        use.getRole().add("otherRole");
        
    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(use,opt,"target/usage3");
    }
    
    public void testUsage4() throws JAXBException  {
        Used use = pFactory.newUsed(q("use4"),
                                    pFactory.newActivityRef(q("a1")),
                                    "somerole",
                                    pFactory.newEntityRef(q("e1")));
        use.setTime(pFactory.newTimeNow());

    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(use,opt,"target/usage4");
    }

    public void testUsage5() throws JAXBException  {
        Used use = pFactory.newUsed(q("use4"),
                                    pFactory.newActivityRef(q("a1")),
                                    "somerole",
                                    pFactory.newEntityRef(q("e1")));
        use.setTime(pFactory.newTimeNow());
        addTypes(use);
        addLocations(use);
        addLabels(use);
        addFurtherAttributes(use);
        
    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(use,opt,"target/usage5");
    }

    public void testUsage6() throws JAXBException  {
        Used use = pFactory.newUsed((QName)null,
                                    pFactory.newActivityRef(q("a1")),
                                    null,
                                    pFactory.newEntityRef(q("e1")));

    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(use,opt,"target/usage6");
    }

    public void testUsage7() throws JAXBException  {
        Used use = pFactory.newUsed((QName)null,
                                    pFactory.newActivityRef(q("a1")),
                                    "somerole",
                                    pFactory.newEntityRef(q("e1")));
        use.setTime(pFactory.newTimeNow());
        addTypes(use);
        addLocations(use);
        addLabels(use);
        addFurtherAttributes(use);
        
    	Entity e1=pFactory.newEntity(q("e1"));
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { e1, a1 };
        makeDocAndTest(use,opt,"target/usage7");
    }
    
    // //////////////////////////////////////////////

    public void testInvalidation1() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv1"),
							    pFactory.newEntityRef(q("e1")),
							    null);
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { e1 };
	makeDocAndTest(inv, opt, "target/invalidation1");
    }

    public void testInvalidation2() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv2"),
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(inv, opt, "target/invalidation2");
    }

    public void testInvalidation3() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv3"),
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	inv.getRole().add("someRole");
	inv.getRole().add("otherRole");
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(inv, opt, "target/invalidation3");
    }

    public void testInvalidation4() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv4"),
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	inv.getRole().add("someRole");
	inv.setTime(pFactory.newTimeNow());
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(inv, opt, "target/invalidation4");
    }

    public void testInvalidation5() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv4"),
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	inv.getRole().add("someRole");
	inv.setTime(pFactory.newTimeNow());
	addTypes(inv);
	addLocations(inv);

	addLabels(inv);
	addFurtherAttributes(inv);

	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(inv, opt, "target/invalidation5");
    }

    public void testInvalidation6() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy((QName) null,
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(inv, opt, "target/invalidation6");
    }

    public void testInvalidation7() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy((QName) null,
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	inv.getRole().add("someRole");
	inv.setTime(pFactory.newTimeNow());
	addTypes(inv);
	addLocations(inv);
	addLabels(inv);
	addFurtherAttributes(inv);

	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(inv, opt, "target/invalidation7");
    }
    
//////////////////////////////////

    public void testStart1() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start1"),
						      null,
						      pFactory.newEntityRef(q("e1")));
	
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { e1 };
	makeDocAndTest(start, opt, "target/start1");
    }

    public void testStart2() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start2"),
						      pFactory.newActivityRef(q("a1")),
						      pFactory.newEntityRef(q("e1")));
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(start, opt, "target/start2");
    }

    public void testStart3() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start3"),
						      pFactory.newActivityRef(q("a1")),
						      null);
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { a1 };
	makeDocAndTest(start, opt, "target/start3");
    }

    public void testStart4() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start4"),
						      null,
						      pFactory.newEntityRef(q("e1")));
	start.setStarter(pFactory.newActivityRef(q("a2")));
	
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { e1, a2 };
	makeDocAndTest(start, opt, "target/start4");
    }

    public void testStart5() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start5"),
						      pFactory.newActivityRef(q("a1")),
						      pFactory.newEntityRef(q("e1")));
	start.setStarter(pFactory.newActivityRef(q("a2")));
	
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { e1, a1, a2 };
	makeDocAndTest(start, opt, "target/start5");
    }

    public void testStart6() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start6"),
						      pFactory.newActivityRef(q("a1")),
						      null);
	start.setStarter(pFactory.newActivityRef(q("a2")));
	
	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { a1, a2 };
	makeDocAndTest(start, opt, "target/start6");
    }

    
    public void testStart7() throws JAXBException {
   	WasStartedBy start = pFactory.newWasStartedBy(q("start7"),
   						      pFactory.newActivityRef(q("a1")),
   						      null);
   	start.setStarter(pFactory.newActivityRef(q("a2")));
   	start.setTime(pFactory.newTimeNow());

	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { a1, a2 };
   	makeDocAndTest(start, opt, "target/start7");
    }
    
    public void testStart8() throws JAXBException {
   	WasStartedBy start = pFactory.newWasStartedBy(q("start8"),
   						      pFactory.newActivityRef(q("a1")),
   						      null);
   	start.setStarter(pFactory.newActivityRef(q("a2")));
   	start.setTime(pFactory.newTimeNow());
   	start.getRole().add("someRole");
   	start.getRole().add("otherRole");
   	addTypes(start);
	addLocations(start);
	addLabels(start);
	addFurtherAttributes(start);

	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { a1, a2 };
   	makeDocAndTest(start, opt, "target/start8");
    }
    
    public void testStart9() throws JAXBException {
   	WasStartedBy start = pFactory.newWasStartedBy((QName)null,
   						      pFactory.newActivityRef(q("a1")),
   						      pFactory.newEntityRef(q("e1")));
   	
	Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { e1, a1 };
   	makeDocAndTest(start, opt, "target/start9");
       }
    
    public void testStart10() throws JAXBException {
   	WasStartedBy start = pFactory.newWasStartedBy((QName)null,
   						      pFactory.newActivityRef(q("a1")),
   						      null);
   	start.setStarter(pFactory.newActivityRef(q("a2")));
   	start.setTime(pFactory.newTimeNow());
   	start.getRole().add("someRole");
   	start.getRole().add("otherRole");
   	addTypes(start);
	addLocations(start);
	addLabels(start);
	addFurtherAttributes(start);

	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { a1, a2 };
   	makeDocAndTest(start, opt, "target/start10");
    }
    
    // ////////////////////////////////

    public void testEnd1() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end1"), null,
						pFactory.newEntityRef(q("e1")));
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { e1 };
	makeDocAndTest(end, opt, "target/end1");
    }

    public void testEnd2() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end2"),
						pFactory.newActivityRef(q("a1")),
						pFactory.newEntityRef(q("e1")));

	Activity a1=pFactory.newActivity(q("a1"));
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { a1, e1 };
	
	makeDocAndTest(end, opt, "target/end2");
    }

    public void testEnd3() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end3"),
						pFactory.newActivityRef(q("a1")),
						null);
	Activity a1=pFactory.newActivity(q("a1"));
	Statement [] opt=new Statement[] { a1 };
	makeDocAndTest(end, opt, "target/end3");
    }

    public void testEnd4() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end4"), null,
						pFactory.newEntityRef(q("e1")));
	end.setEnder(pFactory.newActivityRef(q("a2")));
	
	Activity a2=pFactory.newActivity(q("a2"));
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { a2, e1 };
	makeDocAndTest(end, opt, "target/end4");
    }

    public void testEnd5() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end5"),
						pFactory.newActivityRef(q("a1")),
						pFactory.newEntityRef(q("e1")));
	end.setEnder(pFactory.newActivityRef(q("a2")));

	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { a2, e1, a1 };
	makeDocAndTest(end, opt, "target/end5");
    }

    public void testEnd6() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end6"),
						pFactory.newActivityRef(q("a1")),
						null);
	end.setEnder(pFactory.newActivityRef(q("a2")));
	
	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { a2, a1 };
	makeDocAndTest(end, opt, "target/end6");
    }

    public void testEnd7() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end7"),
						pFactory.newActivityRef(q("a1")),
						null);
	end.setEnder(pFactory.newActivityRef(q("a2")));
	end.setTime(pFactory.newTimeNow());
	
	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { a2, a1 };
	makeDocAndTest(end, opt, "target/end7");
    }

    public void testEnd8() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end8"),
						pFactory.newActivityRef(q("a1")),
						null);
	end.setEnder(pFactory.newActivityRef(q("a2")));
	end.setTime(pFactory.newTimeNow());
	end.getRole().add("someRole");
	end.getRole().add("otherRole");
	addTypes(end);
	addLocations(end);
	addLabels(end);
	addFurtherAttributes(end);

	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { a2, a1 };
	makeDocAndTest(end, opt, "target/end8");
    }

    public void testEnd9() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy((QName) null,
						pFactory.newActivityRef(q("a1")),
						pFactory.newEntityRef(q("e1")));
	
	Activity a1=pFactory.newActivity(q("a1"));
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { e1, a1 };
	makeDocAndTest(end, opt, "target/end9");
    }

    public void testEnd10() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy((QName) null,
						pFactory.newActivityRef(q("a1")),
						null);
	end.setEnder(pFactory.newActivityRef(q("a2")));
	end.setTime(pFactory.newTimeNow());
	end.getRole().add("someRole");
	end.getRole().add("otherRole");
	addTypes(end);
	addLocations(end);
	addLabels(end);
	addFurtherAttributes(end);


	Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	Statement [] opt=new Statement[] { a2, a1 };
	makeDocAndTest(end, opt, "target/end10");
    }
    
    
    // ////////////////////////////////

    public void testDerivation1() throws JAXBException {
	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der1"), 
	                                                null,
	                                                pFactory.newEntityRef(q("e1")));
	Entity e1=pFactory.newEntity(q("e1"));
	Statement [] opt=new Statement[] { e1 };
	makeDocAndTest(der, opt, "target/derivation1");
    }

    public void testDerivation2() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der2"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                null);
   	Entity e2=pFactory.newEntity(q("e2"));
   	Statement [] opt=new Statement[] { e2 };
   	makeDocAndTest(der, opt, "target/derivation2");
    }
    
    public void testDerivation3() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der3"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));   	
   	Entity e1=pFactory.newEntity(q("e1"));
   	Entity e2=pFactory.newEntity(q("e2"));
   	Statement [] opt=new Statement[] { e1, e2 };
   	makeDocAndTest(der, opt, "target/derivation3");
    }

    public void testDerivation4() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der4"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	addLabel(der);
   	Entity e1=pFactory.newEntity(q("e1"));
   	Entity e2=pFactory.newEntity(q("e2"));
   	Statement [] opt=new Statement[] { e1, e2 };
   	makeDocAndTest(der, opt, "target/derivation4");
    }
    
    public void testDerivation5() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der5"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	der.setActivity(pFactory.newActivityRef(q("a")));
   	Entity e1=pFactory.newEntity(q("e1"));
   	Entity e2=pFactory.newEntity(q("e2"));
	Activity a=pFactory.newActivity(q("a"));
   	Statement [] opt=new Statement[] { e1, e2, a };
   	makeDocAndTest(der, opt, "target/derivation5");
    }
    
    
    public void testDerivation6() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der6"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	der.setActivity(pFactory.newActivityRef(q("a")));
   	der.setUsage(pFactory.newUsageRef(q("u")));
   	
   	Entity e1=pFactory.newEntity(q("e1"));
   	Entity e2=pFactory.newEntity(q("e2"));
	Activity a=pFactory.newActivity(q("a"));
	Used u=pFactory.newUsed(q("u"));
   	Statement [] opt=new Statement[] { e1, e2, a, u };
   	
   	makeDocAndTest(der, opt, "target/derivation6");
    }
    
    public void testDerivation7() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der7"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	der.setActivity(pFactory.newActivityRef(q("a")));
   	der.setUsage(pFactory.newUsageRef(q("u")));
   	der.setGeneration(pFactory.newGenerationRef(q("g")));
   	

   	Entity e1=pFactory.newEntity(q("e1"));
   	Entity e2=pFactory.newEntity(q("e2"));
	Activity a=pFactory.newActivity(q("a"));
	Used u=pFactory.newUsed(q("u"));
	WasGeneratedBy g=pFactory.newWasGeneratedBy(q("g"));
   	Statement [] opt=new Statement[] { e1, e2, a, u, g };
   	
   	makeDocAndTest(der, opt, "target/derivation7");
    }
    
    
    
    public void testDerivation8() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der8"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	addLabel(der);
   	addTypes(der);
   	addFurtherAttributes(der);
   	

   	Entity e1=pFactory.newEntity(q("e1"));
   	Entity e2=pFactory.newEntity(q("e2"));
   	Statement [] opt=new Statement[] { e1, e2 };
   	
   	makeDocAndTest(der, opt, "target/derivation8");
    }
    
    public void testDerivation9() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom((QName)null, 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                null);
   	addTypes(der);
   	Entity e2=pFactory.newEntity(q("e2"));
   	Statement [] opt=new Statement[] { e2 };
   	
   	makeDocAndTest(der, opt, "target/derivation9");
    }
    
    public void testDerivation10() throws JAXBException {
        WasDerivedFrom der = pFactory.newWasDerivedFrom((QName)null, 
                                                        pFactory.newEntityRef(q("e2")),
                                                        pFactory.newEntityRef(q("e1")));
        der.setActivity(pFactory.newActivityRef(q("a")));
        der.setUsage(pFactory.newUsageRef(q("u")));
        der.setGeneration(pFactory.newGenerationRef(q("g")));

       	Entity e1=pFactory.newEntity(q("e1"));
       	Entity e2=pFactory.newEntity(q("e2"));
    	Activity a=pFactory.newActivity(q("a"));
    	Used u=pFactory.newUsed(q("u"));
    	WasGeneratedBy g=pFactory.newWasGeneratedBy(q("g"));
       	Statement [] opt=new Statement[] { e1, e2, a, u, g };
       	
        makeDocAndTest(der, opt, "target/derivation10");
    }
    
    public void testDerivation11() throws JAXBException {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("rev1"), 
                                                        pFactory.newEntityRef(q("e2")),
                                                        pFactory.newEntityRef(q("e1")));
        der.setActivity(pFactory.newActivityRef(q("a")));
        der.setUsage(pFactory.newUsageRef(q("u")));
        der.setGeneration(pFactory.newGenerationRef(q("g")));
        pFactory.addRevisionType(der);

       	Entity e1=pFactory.newEntity(q("e1"));
       	Entity e2=pFactory.newEntity(q("e2"));
    	Activity a=pFactory.newActivity(q("a"));
    	Used u=pFactory.newUsed(q("u"));
    	WasGeneratedBy g=pFactory.newWasGeneratedBy(q("g"));
       	Statement [] opt=new Statement[] { e1, e2, a, u, g };
       	
        makeDocAndTest(der, opt, "target/derivation11");
    }

    public void testDerivation12() throws JAXBException {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("quo1"), 
                                                        pFactory.newEntityRef(q("e2")),
                                                        pFactory.newEntityRef(q("e1")));
        der.setActivity(pFactory.newActivityRef(q("a")));
        der.setUsage(pFactory.newUsageRef(q("u")));
        der.setGeneration(pFactory.newGenerationRef(q("g")));
        pFactory.addQuotationType(der);

       	Entity e1=pFactory.newEntity(q("e1"));
       	Entity e2=pFactory.newEntity(q("e2"));
    	Activity a=pFactory.newActivity(q("a"));
    	Used u=pFactory.newUsed(q("u"));
    	WasGeneratedBy g=pFactory.newWasGeneratedBy(q("g"));
       	Statement [] opt=new Statement[] { e1, e2, a, u, g };
       	
        makeDocAndTest(der, opt, "target/derivation12");
    }

    public void testDerivation13() throws JAXBException {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("prim1"), 
                                                        pFactory.newEntityRef(q("e2")),
                                                        pFactory.newEntityRef(q("e1")));
        der.setActivity(pFactory.newActivityRef(q("a")));
        der.setUsage(pFactory.newUsageRef(q("u")));
        der.setGeneration(pFactory.newGenerationRef(q("g")));
        pFactory.addPrimarySourceType(der);
        
       	Entity e1=pFactory.newEntity(q("e1"));
       	Entity e2=pFactory.newEntity(q("e2"));
    	Activity a=pFactory.newActivity(q("a"));
    	Used u=pFactory.newUsed(q("u"));
    	WasGeneratedBy g=pFactory.newWasGeneratedBy(q("g"));
       	Statement [] opt=new Statement[] { e1, e2, a, u, g };
       	
        makeDocAndTest(der, opt, "target/derivation13");
    }
    
    // ////////////////////////////////

    public void testAssociation1() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc1"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                null);
    	Activity a1=pFactory.newActivity(q("a1"));
    	Statement [] opt=new Statement[] { a1 };
        makeDocAndTest(assoc, opt, "target/association1");
    }

    public void testAssociation2() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc2"), 
                                                                null,
                                                                pFactory.newAgentRef(q("ag1")));
        Agent ag1=pFactory.newAgent(q("ag1"));
        Statement [] opt = new Statement[] { ag1 };
        makeDocAndTest(assoc, opt, "target/association2");
    }

    public void testAssociation3() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc3"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        Agent ag1=pFactory.newAgent(q("ag1"));
        Activity a1=pFactory.newActivity(q("a1"));
        Statement [] opt = new Statement[] { ag1, a1 };
        makeDocAndTest(assoc, opt, "target/association3");
    }


    public void testAssociation4() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc4"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        Agent ag1=pFactory.newAgent(q("ag1"));
        Activity a1=pFactory.newActivity(q("a1"));
        Entity plan1=pFactory.newEntity(q("plan1"));
        pFactory.addType(plan1, pFactory.newQName("prov:Plan"));
        Statement [] opt = new Statement[] { ag1, a1, plan1 };
        makeDocAndTest(assoc, opt, "target/association4");
    }

    
    public void testAssociation5() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith((QName)null, 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        Agent ag1=pFactory.newAgent(q("ag1"));
        Activity a1=pFactory.newActivity(q("a1"));
        Statement [] opt = new Statement[] { ag1, a1 };
        makeDocAndTest(assoc, opt, "target/association5");
    }
    
    

    public void testAssociation6() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc6"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        addLabels(assoc);
        
        Agent ag1=pFactory.newAgent(q("ag1"));
        Activity a1=pFactory.newActivity(q("a1"));
        Entity plan1=pFactory.newEntity(q("plan1"));
        pFactory.addType(plan1, pFactory.newQName("prov:Plan"));
        Statement [] opt = new Statement[] { ag1, a1, plan1 };
        
        makeDocAndTest(assoc, opt, "target/association6");
    }

    public void testAssociation7() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc7"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        addLabels(assoc);
        addTypes(assoc);        
        
        Agent ag1=pFactory.newAgent(q("ag1"));
        Activity a1=pFactory.newActivity(q("a1"));
        Entity plan1=pFactory.newEntity(q("plan1"));
        pFactory.addType(plan1, pFactory.newQName("prov:Plan"));
        Statement [] opt = new Statement[] { ag1, a1, plan1 };
        
        makeDocAndTest(assoc, opt, "target/association7");
    }


    public void testAssociation8() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc8"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        assoc.getRole().add("someRole");
        assoc.getRole().add("someOtherRole");
        
        Agent ag1=pFactory.newAgent(q("ag1"));
        Activity a1=pFactory.newActivity(q("a1"));
        Entity plan1=pFactory.newEntity(q("plan1"));
        pFactory.addType(plan1, pFactory.newQName("prov:Plan"));
        Statement [] opt = new Statement[] { ag1, a1, plan1 };
        
        makeDocAndTest(assoc, opt, "target/association8");
    }
    

    public void testAssociation9() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc9"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        addLabels(assoc);
        addTypes(assoc);
        addFurtherAttributes(assoc);
        
        Agent ag1=pFactory.newAgent(q("ag1"));
        Activity a1=pFactory.newActivity(q("a1"));
        Entity plan1=pFactory.newEntity(q("plan1"));
        pFactory.addType(plan1, pFactory.newQName("prov:Plan"));
        Statement [] opt = new Statement[] { ag1, a1, plan1 };
        
        makeDocAndTest(assoc, opt, "target/association9");
    }

 // ////////////////////////////////

    public void testAttribution1() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr1"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           null);
        Entity e1=pFactory.newEntity(q("e1"));
    	Statement [] opt=new Statement[] { e1 };
        makeDocAndTest(attr, opt, "target/attribution1");
    }
    
    public void testAttribution2() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr2"), 
                                                           null,
                                                           pFactory.newAgentRef(q("ag1")));
        Agent ag1=pFactory.newAgent(q("ag1"));
        Statement [] opt = new Statement[] { ag1 };
        makeDocAndTest(attr, opt, "target/attribution2");
    }
    
    public void testAttribution3() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr3"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        Agent ag1=pFactory.newAgent(q("ag1"));
        Entity e1=pFactory.newEntity(q("e1"));
        Statement [] opt = new Statement[] { ag1, e1 };
        makeDocAndTest(attr, opt, "target/attribution3");
    }


    public void testAttribution4() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr4"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        Agent ag1=pFactory.newAgent(q("ag1"));
        Entity e1=pFactory.newEntity(q("e1"));
        Statement [] opt = new Statement[] { ag1, e1 };
        makeDocAndTest(attr, opt, "target/attribution4");
    }

    
    public void testAttribution5() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo((QName)null, 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        Agent ag1=pFactory.newAgent(q("ag1"));
        Entity e1=pFactory.newEntity(q("e1"));
        Statement [] opt = new Statement[] { ag1, e1 };
        makeDocAndTest(attr, opt, "target/attribution5");
    }
    
    

    public void testAttribution6() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr6"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        addLabels(attr);
        Agent ag1=pFactory.newAgent(q("ag1"));
        Entity e1=pFactory.newEntity(q("e1"));
        Statement [] opt = new Statement[] { ag1, e1 };
        makeDocAndTest(attr, opt, "target/attribution6");
    }

    public void testAttribution7() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr7"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        addLabels(attr);
        addTypes(attr);
        Agent ag1=pFactory.newAgent(q("ag1"));
        Entity e1=pFactory.newEntity(q("e1"));
        Statement [] opt = new Statement[] { ag1, e1 };
        makeDocAndTest(attr, opt, "target/attribution7");
    }


    public void testAttribution8() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr8"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        addLabels(attr);
        addTypes(attr);
        addFurtherAttributes(attr);
        Agent ag1=pFactory.newAgent(q("ag1"));
        Entity e1=pFactory.newEntity(q("e1"));
        Statement [] opt = new Statement[] { ag1, e1 };
        makeDocAndTest(attr, opt, "target/attribution8");
    }


    // ////////////////////////////////

       public void testDelegation1() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del1"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              null,
                                                              null);
           Agent e1=pFactory.newAgent(q("e1"));
       	Statement [] opt=new Statement[] { e1 };
           makeDocAndTest(del, opt, "target/delegation1");
       }
       
       public void testDelegation2() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del2"), 
                                                              null,
                                                              pFactory.newAgentRef(q("ag1")),
                                                              null);
           Agent ag1=pFactory.newAgent(q("ag1"));
           Statement [] opt = new Statement[] { ag1 };
           makeDocAndTest(del, opt, "target/delegation2");
       }
       
       public void testDelegation3() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del3"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              null);
           Agent ag1=pFactory.newAgent(q("ag1"));
           Agent e1=pFactory.newAgent(q("e1"));
           Statement [] opt = new Statement[] { ag1, e1 };
           makeDocAndTest(del, opt, "target/delegation3");
       }


       public void testDelegation4() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del4"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              pFactory.newActivityRef(q("a")));
           Agent ag1=pFactory.newAgent(q("ag1"));
           Agent e1=pFactory.newAgent(q("e1"));
           Activity a=pFactory.newActivity(q("a"));
           Statement [] opt = new Statement[] { ag1, e1, a };
           
           makeDocAndTest(del, opt, "target/delegation4");
       }

       
       public void testDelegation5() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf((QName)null, 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              null);
           Agent ag1=pFactory.newAgent(q("ag1"));
           Agent e1=pFactory.newAgent(q("e1"));
           Statement [] opt = new Statement[] { ag1, e1 };
           makeDocAndTest(del, opt, "target/delegation5");
       }
       
       

       public void testDelegation6() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del6"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              pFactory.newActivityRef(q("a")));
           addLabels(del);
           Agent ag1=pFactory.newAgent(q("ag1"));
           Agent e1=pFactory.newAgent(q("e1"));
           Activity a=pFactory.newActivity(q("a"));
           Statement [] opt = new Statement[] { ag1, e1, a };
           makeDocAndTest(del, opt, "target/delegation6");
       }

       public void testDelegation7() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del7"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              pFactory.newActivityRef(q("a")));
           addLabels(del);
           addTypes(del);
           Agent ag1=pFactory.newAgent(q("ag1"));
           Agent e1=pFactory.newAgent(q("e1"));
           Activity a=pFactory.newActivity(q("a"));
           Statement [] opt = new Statement[] { ag1, e1, a };
           makeDocAndTest(del, opt, "target/delegation7");
       }


       public void testDelegation8() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del8"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              pFactory.newActivityRef(q("a")));
           addLabels(del);
           addTypes(del);
           addFurtherAttributes(del);
           Agent ag1=pFactory.newAgent(q("ag1"));
           Agent e1=pFactory.newAgent(q("e1"));
           Activity a=pFactory.newActivity(q("a"));
           Statement [] opt = new Statement[] { ag1, e1, a };
           makeDocAndTest(del, opt, "target/delegation8");
       }

       // ////////////////////////////////

       public void testCommunication1() throws JAXBException {
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf1"), 
                                                         pFactory.newActivityRef(q("a2")),
                                                         null);
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a2 };
           makeDocAndTest(inf, opt, "target/communication1");
       }
       
       public void testCommunication2() throws JAXBException {
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf2"), 
                                                              null,
                                                              pFactory.newActivityRef(q("a1")));
           Activity a1=pFactory.newActivity(q("a1"));
          	Statement [] opt=new Statement[] { a1 };
           makeDocAndTest(inf, opt, "target/communication2");
       }
       
       public void testCommunication3() throws JAXBException {
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf3"), 
                                                         pFactory.newActivityRef(q("a2")),
                                                         pFactory.newActivityRef(q("a1")));
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/communication3");
       }


       
       public void testCommunication4() throws JAXBException {
           WasInformedBy inf = pFactory.newWasInformedBy((QName)null, 
                                                         pFactory.newActivityRef(q("a2")),
                                                         pFactory.newActivityRef(q("a1")));
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/communication4");
       }
       
       

       public void testCommunication5() throws JAXBException {
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf5"), 
                                                         pFactory.newActivityRef(q("a2")),
                                                         pFactory.newActivityRef(q("a1")));
           addLabels(inf);
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/communication5");
       }

       public void testCommunication6() throws JAXBException {
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf6"), 
                                                         pFactory.newActivityRef(q("a2")),
                                                         pFactory.newActivityRef(q("a1")));
           addLabels(inf);
           addTypes(inf);
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/communication6");
       }


       public void testCommunication7() throws JAXBException {
           WasInformedBy inf = pFactory.newWasInformedBy(q("inf7"), 
                                                         pFactory.newActivityRef(q("a2")),
                                                         pFactory.newActivityRef(q("a1")));
           addLabels(inf);
           addTypes(inf);
           addFurtherAttributes(inf);
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/communication7");
       }


       // ////////////////////////////////

       public void testInfluence1() throws JAXBException {
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf1"), 
                                                             pFactory.newAnyRef(q("a2")),
                                                             null);
           Activity a2=pFactory.newActivity(q("a2"));
         	Statement [] opt=new Statement[] { a2 };
           makeDocAndTest(inf, opt, "target/influence1");
       }
       
       public void testInfluence2() throws JAXBException {
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf2"), 
                                                             null,
                                                             pFactory.newAnyRef(q("a1")));
           Activity a1=pFactory.newActivity(q("a1"));
          	Statement [] opt=new Statement[] { a1 };
           makeDocAndTest(inf, opt, "target/influence2");
       }
       
       public void testInfluence3() throws JAXBException {
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf3"), 
                                                             pFactory.newAnyRef(q("a2")),
                                                             pFactory.newAnyRef(q("a1")));
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/influence3");
       }


       
       public void testInfluence4() throws JAXBException {
           WasInfluencedBy inf = pFactory.newWasInfluencedBy((QName)null, 
                                                             pFactory.newAnyRef(q("a2")),
                                                             pFactory.newAnyRef(q("a1")));
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/influence4");
       }
       
       

       public void testInfluence5() throws JAXBException {
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf5"), 
                                                             pFactory.newAnyRef(q("a2")),
                                                             pFactory.newAnyRef(q("a1")));
           addLabels(inf);
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/influence5");
       }

       public void testInfluence6() throws JAXBException {
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf6"), 
                                                             pFactory.newAnyRef(q("a2")),
                                                             pFactory.newAnyRef(q("a1")));
           addLabels(inf);
           addTypes(inf);
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/influence6");
       }


       public void testInfluence7() throws JAXBException {
           WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf7"), 
                                                             pFactory.newAnyRef(q("a2")),
                                                             pFactory.newAnyRef(q("a1")));
           addLabels(inf);
           addTypes(inf);
           addFurtherAttributes(inf);
           Activity a1=pFactory.newActivity(q("a1"));
           Activity a2=pFactory.newActivity(q("a2"));
          	Statement [] opt=new Statement[] { a1, a2 };
           makeDocAndTest(inf, opt, "target/influence7");
       }

       

       // ////////////////////////////////

       public void testAlternate1() throws JAXBException {
           AlternateOf alt = pFactory.newAlternateOf(pFactory.newEntityRef(q("e2")),
                                                     pFactory.newEntityRef(q("e1")));
           Entity e1=pFactory.newEntity(q("e1")); 
           Entity e2=pFactory.newEntity(q("e2"));
        	Statement [] opt=new Statement[] { e1, e2 };
        	
        	
           makeDocAndTest(alt, opt, "target/alternate1");
       }
       

       public void testSpecialization1() throws JAXBException {
           SpecializationOf spe = pFactory.newSpecializationOf(pFactory.newEntityRef(q("e2")),
                                                               pFactory.newEntityRef(q("e1")));
           Entity e1=pFactory.newEntity(q("e1")); 
           Entity e2=pFactory.newEntity(q("e2"));
        	Statement [] opt=new Statement[] { e1, e2 };
           makeDocAndTest(spe, opt, "target/specialization1");
       }
       
       
       public void testMention1() throws JAXBException {
           MentionOf men = pFactory.newMentionOf(pFactory.newEntityRef(q("e2")),
                                                 pFactory.newEntityRef(q("e1")),
                                                 null);
           Entity e1=pFactory.newEntity(q("e1")); 
           Entity e2=pFactory.newEntity(q("e2"));
        	Statement [] opt=new Statement[] { e1, e2 };
           makeDocAndTest(men, opt, "target/mention1");
       }
       
       public void testMention2() throws JAXBException {
           MentionOf men = pFactory.newMentionOf(pFactory.newEntityRef(q("e2")),
                                                 pFactory.newEntityRef(q("e1")),
                                                 pFactory.newEntityRef(q("b")));
           Entity e1=pFactory.newEntity(q("e1")); 
           Entity e2=pFactory.newEntity(q("e2"));
           Entity b=pFactory.newEntity(q("b"));
           pFactory.addType(b, pFactory.newQName("prov:Bundle"));
        	Statement [] opt=new Statement[] { e1, e2, b };
           makeDocAndTest(men, opt, "target/mention2");
       }
       

       public void testMembership1() throws JAXBException {
           HadMember mem = pFactory.newHadMember(pFactory.newEntityRef(q("c")),
                                                 pFactory.newEntityRef(q("e1")));
           Entity c=pFactory.newEntity(q("c")); 
          
           Entity e1=pFactory.newEntity(q("e1"));
       	   Statement [] opt=new Statement[] { c, e1 };
           makeDocAndTest(mem, opt, "target/member1");
       }
       public void testMembership2() throws JAXBException {
           HadMember mem = pFactory.newHadMember(pFactory.newEntityRef(q("c")),
                                                 pFactory.newEntityRef(q("e1")),
                                                 pFactory.newEntityRef(q("e2")));
           //TODO: multiple arguments not supported by toolbox 
           Entity c=pFactory.newEntity(q("c")); 
           Entity e1=pFactory.newEntity(q("e1"));
           Entity e2=pFactory.newEntity(q("e2"));
       	   Statement [] opt=new Statement[] { c, e1, e2 };
           makeDocAndTest(mem, opt, "target/member2");
       }
       public void testMembership3() throws JAXBException {
           HadMember mem = pFactory.newHadMember(pFactory.newEntityRef(q("c")),
                                                 pFactory.newEntityRef(q("e1")),
                                                 pFactory.newEntityRef(q("e2")),
                                                 pFactory.newEntityRef(q("e3")));
           //TODO: multiple arguments not supported by toolbox 
           Entity c=pFactory.newEntity(q("c")); 
           Entity e1=pFactory.newEntity(q("e1"));
           Entity e2=pFactory.newEntity(q("e2"));
           Entity e3=pFactory.newEntity(q("e3"));
       	   Statement [] opt=new Statement[] { c, e1, e2, e3 };
           makeDocAndTest(mem, opt, "target/member3");
       }
     
    
}
