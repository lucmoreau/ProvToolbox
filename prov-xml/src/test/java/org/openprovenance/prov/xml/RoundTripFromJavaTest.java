package org.openprovenance.prov.xml;

import java.io.File;
import java.util.Hashtable;
import java.net.URI;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import junit.framework.TestCase;

/**
 * Unit test for simple Provenance Challenge 1 like workflow.
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

    public void updateNamespaces(Document doc) {
	Hashtable<String, String> nss = new Hashtable<String, String>();
	updateNamespaces(nss);
	doc.setNss(nss);
    }
   
    public String extension() {
	return ".xml";
    }

    public void makeDocAndTest(Statement stment, String file) throws JAXBException {
	makeDocAndTest(stment, file,true);
    }
    public void makeDocAndTest(Statement stment, String file, boolean check) throws JAXBException {
	Document doc = pFactory.newDocument();
	doc.getEntityOrActivityOrWasGeneratedBy().add(stment);
	updateNamespaces(doc);
	file=file+extension();

	writeXMLDocument(doc, file);
	
	Document doc2=readXMLDocument(file);
	compareDocuments(doc, doc2, check);
    }

    public void compareDocuments(Document doc, Document doc2, boolean check) {
	assertTrue("self doc differ", doc.equals(doc));
	assertTrue("self doc2 differ", doc2.equals(doc2));
	if (check) {
	    System.out.println("Found " + doc);
	    System.out.println("Found " + doc2);
	    //System.out.println("Found " + util.getEntity(doc).get(0).getAny());
	    //System.out.println("Found " + util.getEntity(doc2).get(0).getAny());

	    assertTrue("doc differs doc2", doc.equals(doc2));
	} else {
	    assertFalse("doc differs doc2", doc.equals(doc2));
	}
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
	makeDocAndTest(gen,"target/generation1");
    }


    public void testGeneration2() throws JAXBException  {
	WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen2"),
							pFactory.newEntityRef(q("e1")),
							null,
							pFactory.newActivityRef(q("a1")));
	makeDocAndTest(gen,"target/generation2");
    }


    public void testGeneration3() throws JAXBException  {
	WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen3"),
							pFactory.newEntityRef(q("e1")),
							"somerole",
							pFactory.newActivityRef(q("a1")));
        gen.getRole().add("otherRole");
	makeDocAndTest(gen,"target/generation3");
    }


    public void testGeneration4() throws JAXBException  {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen4"),
                                                        pFactory.newEntityRef(q("e1")),
                                                        "somerole",
                                                        pFactory.newActivityRef(q("a1")));
        gen.setTime(pFactory.newTimeNow());
        makeDocAndTest(gen,"target/generation4");
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
        
        makeDocAndTest(gen,"target/generation5");
    }
    
    
    public void testGeneration6() throws JAXBException  {
  	WasGeneratedBy gen = pFactory.newWasGeneratedBy((QName)null,
  							pFactory.newEntityRef(q("e1")),
  							null,
  							pFactory.newActivityRef(q("a1")));
  	makeDocAndTest(gen,"target/generation6");
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
        
        makeDocAndTest(gen,"target/generation7");
    }
    
    //////////////////////////////////
    
    public void testUsage1() throws JAXBException  {
        Used use = pFactory.newUsed(q("use1"),
                                    null,
                                    null,
                                    pFactory.newEntityRef(q("e1")));
        makeDocAndTest(use,"target/usage1");
    }

    public void testUsage2() throws JAXBException  {
        Used use = pFactory.newUsed(q("use2"),
                                    pFactory.newActivityRef(q("a1")),
                                    null,
                                    pFactory.newEntityRef(q("e1")));
        makeDocAndTest(use,"target/usage2");
    }

    public void testUsage3() throws JAXBException  {
        Used use = pFactory.newUsed(q("use3"),
                                    pFactory.newActivityRef(q("a1")),
                                    "somerole",
                                    pFactory.newEntityRef(q("e1")));
        use.getRole().add("otherRole");
        makeDocAndTest(use,"target/usage3");
    }
    
    public void testUsage4() throws JAXBException  {
        Used use = pFactory.newUsed(q("use4"),
                                    pFactory.newActivityRef(q("a1")),
                                    "somerole",
                                    pFactory.newEntityRef(q("e1")));
        use.setTime(pFactory.newTimeNow());

        makeDocAndTest(use,"target/usage4");
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
        makeDocAndTest(use,"target/usage5");
    }

    public void testUsage6() throws JAXBException  {
        Used use = pFactory.newUsed((QName)null,
                                    pFactory.newActivityRef(q("a1")),
                                    null,
                                    pFactory.newEntityRef(q("e1")));
        makeDocAndTest(use,"target/usage6");
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
        makeDocAndTest(use,"target/usage7");
    }
    
    // //////////////////////////////////////////////

    public void testInvalidation1() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv1"),
							    pFactory.newEntityRef(q("e1")),
							    null);
	makeDocAndTest(inv, "target/invalidation1");
    }

    public void testInvalidation2() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv2"),
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	makeDocAndTest(inv, "target/invalidation2");
    }

    public void testInvalidation3() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv3"),
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	inv.getRole().add("someRole");
	inv.getRole().add("otherRole");
	makeDocAndTest(inv, "target/invalidation3");
    }

    public void testInvalidation4() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv4"),
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	inv.getRole().add("someRole");
	inv.setTime(pFactory.newTimeNow());
	makeDocAndTest(inv, "target/invalidation4");
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

	makeDocAndTest(inv, "target/invalidation5");
    }

    public void testInvalidation6() throws JAXBException {
	WasInvalidatedBy inv = pFactory.newWasInvalidatedBy((QName) null,
							    pFactory.newEntityRef(q("e1")),
							    pFactory.newActivityRef(q("a1")));
	makeDocAndTest(inv, "target/invalidation6");
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

	makeDocAndTest(inv, "target/invalidation7");
    }
    
//////////////////////////////////

    public void testStart1() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start1"),
						      null,
						      pFactory.newEntityRef(q("e1")));
	makeDocAndTest(start, "target/start1");
    }

    public void testStart2() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start2"),
						      pFactory.newActivityRef(q("a1")),
						      pFactory.newEntityRef(q("e1")));
	makeDocAndTest(start, "target/start2");
    }

    public void testStart3() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start3"),
						      pFactory.newActivityRef(q("a1")),
						      null);
	makeDocAndTest(start, "target/start3");
    }

    public void testStart4() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start4"),
						      null,
						      pFactory.newEntityRef(q("e1")));
	start.setStarter(pFactory.newActivityRef(q("a2")));
	makeDocAndTest(start, "target/start4");
    }

    public void testStart5() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start5"),
						      pFactory.newActivityRef(q("a1")),
						      pFactory.newEntityRef(q("e1")));
	start.setStarter(pFactory.newActivityRef(q("a2")));
	makeDocAndTest(start, "target/start5");
    }

    public void testStart6() throws JAXBException {
	WasStartedBy start = pFactory.newWasStartedBy(q("start6"),
						      pFactory.newActivityRef(q("a1")),
						      null);
	start.setStarter(pFactory.newActivityRef(q("a2")));
	makeDocAndTest(start, "target/start6");
    }

    
    public void testStart7() throws JAXBException {
   	WasStartedBy start = pFactory.newWasStartedBy(q("start7"),
   						      pFactory.newActivityRef(q("a1")),
   						      null);
   	start.setStarter(pFactory.newActivityRef(q("a2")));
   	start.setTime(pFactory.newTimeNow());
   	makeDocAndTest(start, "target/start7");
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
   	
   	makeDocAndTest(start, "target/start8");
    }
    
    public void testStart9() throws JAXBException {
   	WasStartedBy start = pFactory.newWasStartedBy((QName)null,
   						      pFactory.newActivityRef(q("a1")),
   						      pFactory.newEntityRef(q("e1")));
   	makeDocAndTest(start, "target/start9");
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
   	
   	makeDocAndTest(start, "target/start10");
    }
    
    // ////////////////////////////////

    public void testEnd1() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end1"), null,
						pFactory.newEntityRef(q("e1")));
	makeDocAndTest(end, "target/end1");
    }

    public void testEnd2() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end2"),
						pFactory.newActivityRef(q("a1")),
						pFactory.newEntityRef(q("e1")));
	makeDocAndTest(end, "target/end2");
    }

    public void testEnd3() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end3"),
						pFactory.newActivityRef(q("a1")),
						null);
	makeDocAndTest(end, "target/end3");
    }

    public void testEnd4() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end4"), null,
						pFactory.newEntityRef(q("e1")));
	end.setEnder(pFactory.newActivityRef(q("a2")));
	makeDocAndTest(end, "target/end4");
    }

    public void testEnd5() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end5"),
						pFactory.newActivityRef(q("a1")),
						pFactory.newEntityRef(q("e1")));
	end.setEnder(pFactory.newActivityRef(q("a2")));
	makeDocAndTest(end, "target/end5");
    }

    public void testEnd6() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end6"),
						pFactory.newActivityRef(q("a1")),
						null);
	end.setEnder(pFactory.newActivityRef(q("a2")));
	makeDocAndTest(end, "target/end6");
    }

    public void testEnd7() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy(q("end7"),
						pFactory.newActivityRef(q("a1")),
						null);
	end.setEnder(pFactory.newActivityRef(q("a2")));
	end.setTime(pFactory.newTimeNow());
	makeDocAndTest(end, "target/end7");
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

	makeDocAndTest(end, "target/end8");
    }

    public void testEnd9() throws JAXBException {
	WasEndedBy end = pFactory.newWasEndedBy((QName) null,
						pFactory.newActivityRef(q("a1")),
						pFactory.newEntityRef(q("e1")));
	makeDocAndTest(end, "target/end9");
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

	makeDocAndTest(end, "target/end10");
    }
    
    
    // ////////////////////////////////

    public void testDerivation1() throws JAXBException {
	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der1"), 
	                                                null,
	                                                pFactory.newEntityRef(q("e1")));
	makeDocAndTest(der, "target/derivation1");
    }

    public void testDerivation2() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der2"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                null);
   	makeDocAndTest(der, "target/derivation2");
    }
    
    public void testDerivation3() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der3"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	makeDocAndTest(der, "target/derivation3");
    }

    public void testDerivation4() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der4"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	addLabel(der);
   	makeDocAndTest(der, "target/derivation4");
    }
    
    public void testDerivation5() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der5"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	der.setActivity(pFactory.newActivityRef(q("a")));
   	makeDocAndTest(der, "target/derivation5");
    }
    
    
    public void testDerivation6() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der6"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	der.setActivity(pFactory.newActivityRef(q("a")));
   	der.setUsage(pFactory.newUsageRef(q("u")));
   	makeDocAndTest(der, "target/derivation6");
    }
    
    public void testDerivation7() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der7"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	der.setActivity(pFactory.newActivityRef(q("a")));
   	der.setUsage(pFactory.newUsageRef(q("u")));
   	der.setGeneration(pFactory.newGenerationRef(q("g")));
   	makeDocAndTest(der, "target/derivation7");
    }
    
    
    
    public void testDerivation8() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der8"), 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                pFactory.newEntityRef(q("e1")));
   	addLabel(der);
   	addTypes(der);
   	addFurtherAttributes(der);
   	makeDocAndTest(der, "target/derivation8");
    }
    
    public void testDerivation9() throws JAXBException {
   	WasDerivedFrom der = pFactory.newWasDerivedFrom((QName)null, 
   	                                                pFactory.newEntityRef(q("e2")),
   	                                                null);
   	makeDocAndTest(der, "target/derivation9");
    }
    
    public void testDerivation10() throws JAXBException {
        WasDerivedFrom der = pFactory.newWasDerivedFrom((QName)null, 
                                                        pFactory.newEntityRef(q("e2")),
                                                        pFactory.newEntityRef(q("e1")));
        der.setActivity(pFactory.newActivityRef(q("a")));
        der.setUsage(pFactory.newUsageRef(q("u")));
        der.setGeneration(pFactory.newGenerationRef(q("g")));
        makeDocAndTest(der, "target/derivation10");
    }
    
    public void testDerivation11() throws JAXBException {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("rev1"), 
                                                        pFactory.newEntityRef(q("e2")),
                                                        pFactory.newEntityRef(q("e1")));
        der.setActivity(pFactory.newActivityRef(q("a")));
        der.setUsage(pFactory.newUsageRef(q("u")));
        der.setGeneration(pFactory.newGenerationRef(q("g")));
        pFactory.addRevisionType(der);
        makeDocAndTest(der, "target/derivation11");
    }

    public void testDerivation12() throws JAXBException {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("quo1"), 
                                                        pFactory.newEntityRef(q("e2")),
                                                        pFactory.newEntityRef(q("e1")));
        der.setActivity(pFactory.newActivityRef(q("a")));
        der.setUsage(pFactory.newUsageRef(q("u")));
        der.setGeneration(pFactory.newGenerationRef(q("g")));
        pFactory.addQuotationType(der);
        makeDocAndTest(der, "target/derivation12");
    }

    public void testDerivation13() throws JAXBException {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("prim1"), 
                                                        pFactory.newEntityRef(q("e2")),
                                                        pFactory.newEntityRef(q("e1")));
        der.setActivity(pFactory.newActivityRef(q("a")));
        der.setUsage(pFactory.newUsageRef(q("u")));
        der.setGeneration(pFactory.newGenerationRef(q("g")));
        pFactory.addPrimarySourceType(der);
        makeDocAndTest(der, "target/derivation13");
    }
    
    // ////////////////////////////////

    public void testAssociation1() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc1"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                null);
        makeDocAndTest(assoc, "target/association1");
    }

    public void testAssociation2() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc2"), 
                                                                null,
                                                                pFactory.newAgentRef(q("ag1")));
        makeDocAndTest(assoc, "target/association2");
    }

    public void testAssociation3() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc3"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        makeDocAndTest(assoc, "target/association3");
    }


    public void testAssociation4() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc4"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        makeDocAndTest(assoc, "target/association4");
    }

    
    public void testAssociation5() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith((QName)null, 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        makeDocAndTest(assoc, "target/association5");
    }
    
    

    public void testAssociation6() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc6"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        addLabels(assoc);
        makeDocAndTest(assoc, "target/association6");
    }

    public void testAssociation7() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc7"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        addLabels(assoc);
        addTypes(assoc);
        makeDocAndTest(assoc, "target/association7");
    }


    public void testAssociation8() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc8"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        assoc.getRole().add("someRole");
        assoc.getRole().add("someOtherRole");
        makeDocAndTest(assoc, "target/association8");
    }
    

    public void testAssociation9() throws JAXBException {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc9"), 
                                                                pFactory.newActivityRef(q("a1")),
                                                                pFactory.newAgentRef(q("ag1")));
        assoc.setPlan(pFactory.newEntityRef(q("plan1")));
        addLabels(assoc);
        addTypes(assoc);
        addFurtherAttributes(assoc);
        makeDocAndTest(assoc, "target/association9");
    }

 // ////////////////////////////////

    public void testAttribution1() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr1"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           null);
        makeDocAndTest(attr, "target/attribution1");
    }
    
    public void testAttribution2() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr2"), 
                                                           null,
                                                           pFactory.newAgentRef(q("ag1")));
        makeDocAndTest(attr, "target/attribution2");
    }
    
    public void testAttribution3() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr3"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        makeDocAndTest(attr, "target/attribution3");
    }


    public void testAttribution4() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr4"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        makeDocAndTest(attr, "target/attribution4");
    }

    
    public void testAttribution5() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo((QName)null, 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        makeDocAndTest(attr, "target/attribution5");
    }
    
    

    public void testAttribution6() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr6"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        addLabels(attr);
        makeDocAndTest(attr, "target/attribution6");
    }

    public void testAttribution7() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr7"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        addLabels(attr);
        addTypes(attr);
        makeDocAndTest(attr, "target/attribution7");
    }


    public void testAttribution8() throws JAXBException {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr8"), 
                                                           pFactory.newEntityRef(q("e1")),
                                                           pFactory.newAgentRef(q("ag1")));
        addLabels(attr);
        addTypes(attr);
        addFurtherAttributes(attr);
        makeDocAndTest(attr, "target/attribution8");
    }


    // ////////////////////////////////

       public void testDelegation1() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del1"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              null,
                                                              null);
           makeDocAndTest(del, "target/delegation1");
       }
       
       public void testDelegation2() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del2"), 
                                                              null,
                                                              pFactory.newAgentRef(q("ag1")),
                                                              null);
           makeDocAndTest(del, "target/delegation2");
       }
       
       public void testDelegation3() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del3"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              null);
           makeDocAndTest(del, "target/delegation3");
       }


       public void testDelegation4() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del4"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              pFactory.newActivityRef(q("a")));
           makeDocAndTest(del, "target/delegation4");
       }

       
       public void testDelegation5() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf((QName)null, 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              null);
           makeDocAndTest(del, "target/delegation5");
       }
       
       

       public void testDelegation6() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del6"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              pFactory.newActivityRef(q("a")));
           addLabels(del);
           makeDocAndTest(del, "target/delegation6");
       }

       public void testDelegation7() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del7"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              pFactory.newActivityRef(q("a")));
           addLabels(del);
           addTypes(del);
           makeDocAndTest(del, "target/delegation7");
       }


       public void testDelegation8() throws JAXBException {
           ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del8"), 
                                                              pFactory.newAgentRef(q("e1")),
                                                              pFactory.newAgentRef(q("ag1")),
                                                              pFactory.newActivityRef(q("a")));
           addLabels(del);
           addTypes(del);
           addFurtherAttributes(del);
           makeDocAndTest(del, "target/delegation8");
       }

    
}
