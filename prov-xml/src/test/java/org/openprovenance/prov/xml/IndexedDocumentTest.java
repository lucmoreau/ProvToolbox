package org.openprovenance.prov.xml;

import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.Namespace;

import junit.framework.TestCase;

public class IndexedDocumentTest extends TestCase {
    public static final String EXAMPLE_NS = "http://example.com/";
    public static final String EXAMPLE2_NS = "http://example2.com/";
    public static final String EXAMPLE3_NS = "http://example3.com/";
    public static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";
    public static final String EX_PREFIX = "ex";

    public QualifiedName q(String n) {
	return new QualifiedName(EXAMPLE_NS, n, EX_PREFIX);
    }
    

    public static ProvFactory pFactory;
    
    public static ProvUtilities pUtil=new ProvUtilities();
    
    

    static {
	
	pFactory=new ProvFactory();
    }
    
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public IndexedDocumentTest(String testName)
    {
	super(testName);
    }
    
    public Document makeDoc100() {
        Activity a1=pFactory.newActivity(q("a1"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        doc.getStatementOrBundle().add(a1);
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }
    
    public void testDoc100() {
	Document idoc100=new IndexedDocument(pFactory,makeDoc100()).toDocument();
	assertEquals(idoc100.getStatementOrBundle().size(),1);
	
    }
    
    public Document makeDoc101() {
        Activity a1=pFactory.newActivity(q("a1"));
        Activity a2=pFactory.newActivity(q("a2"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        doc.getStatementOrBundle().add(a1);
        doc.getStatementOrBundle().add(a2);
        doc.getStatementOrBundle().add(a2);
        doc.getStatementOrBundle().add(a2);
        doc.getStatementOrBundle().add(a2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }
    
    public void testDoc101() {
	Document idoc101=new IndexedDocument(pFactory,makeDoc101()).toDocument();
	assertEquals(idoc101.getStatementOrBundle().size(),2);
	
    }

    public Document makeDoc200() {
        Entity e1=pFactory.newEntity(q("e1"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(e1);
        doc.getStatementOrBundle().add(e1);
        doc.getStatementOrBundle().add(e1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }
    
    public void testDoc200() {
	Document idoc200=new IndexedDocument(pFactory,makeDoc200()).toDocument();
	assertEquals(idoc200.getStatementOrBundle().size(),1);
	
    }
    public Document makeDoc201() {
        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e2"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(e1);
	doc.getStatementOrBundle().add(e2);
        doc.getStatementOrBundle().add(e1);
	doc.getStatementOrBundle().add(e2);
        doc.getStatementOrBundle().add(e1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }
    
    public void testDoc201() {
	Document idoc201=new IndexedDocument(pFactory,makeDoc201()).toDocument();
	assertEquals(idoc201.getStatementOrBundle().size(),2);
	
    }


    public Document makeDoc202() {
        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e1"));
	e2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(e1);
	doc.getStatementOrBundle().add(e2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }
    
    public void testDoc202() {
	Document idoc202=new IndexedDocument(pFactory,makeDoc202()).toDocument();
	assertEquals(idoc202.getStatementOrBundle().size(),1);
	Entity e=(Entity)idoc202.getStatementOrBundle().get(0);
	assertEquals(e.getLabel().size(),1);
    }

    public Document makeDoc203() {
        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e1"));
	e2.getLabel().add(pFactory.newInternationalizedString("hello"));
	e2.getLabel().add(pFactory.newInternationalizedString("hello"));
	e2.getLocation().add(pFactory.newLocation("liege", pFactory.getName().XSD_STRING));
	e2.getLocation().add(pFactory.newLocation("liege", pFactory.getName().XSD_STRING));
	e2.getLocation().add(pFactory.newLocation("liege", pFactory.getName().XSD_STRING));

	e2.getType().add(pFactory.newType(q("TYPE"), pFactory.getName().XSD_QNAME));
	e2.getType().add(pFactory.newType(q("TYPE"), pFactory.getName().XSD_QNAME));

	e2.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	e2.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));

	
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(e1);
	doc.getStatementOrBundle().add(e2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }
    
    public void testDoc203() {
	Document idoc203=new IndexedDocument(pFactory,makeDoc203()).toDocument();
	assertEquals(idoc203.getStatementOrBundle().size(),1);
	Entity e=(Entity)idoc203.getStatementOrBundle().get(0);
	assertEquals("label", e.getLabel().size(),1);
	assertEquals("location", e.getLocation().size(),1);
	assertEquals("type", e.getType().size(),1);
	assertEquals("other", e.getOther().size(),1);
    }


    public Document makeDoc300() {
        Agent ag1=pFactory.newAgent(q("ag1"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(ag1);
        doc.getStatementOrBundle().add(ag1);
        doc.getStatementOrBundle().add(ag1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }
    
    public void testDoc300() {
	Document idoc300=new IndexedDocument(pFactory,makeDoc300()).toDocument();
	assertEquals(idoc300.getStatementOrBundle().size(),1);
	
    }
    public Document makeDoc301() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Agent ag2=pFactory.newAgent(q("ag2"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(ag1);
	doc.getStatementOrBundle().add(ag2);
        doc.getStatementOrBundle().add(ag1);
	doc.getStatementOrBundle().add(ag2);
        doc.getStatementOrBundle().add(ag1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }
    
    public void testDoc301() {
	Document idoc301=new IndexedDocument(pFactory,makeDoc301()).toDocument();
	assertEquals(idoc301.getStatementOrBundle().size(),2);
	
    }

    public Document makeDoc400() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Used u1=pFactory.newUsed(a1.getId(),e1.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc400() {
	Document idoc400=new IndexedDocument(pFactory,makeDoc400()).toDocument();
	assertEquals(idoc400.getStatementOrBundle().size(),1);
    }

    public Document makeDoc401() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	Used u1=pFactory.newUsed(a1.getId(),e1.getId());
	Used u2=pFactory.newUsed(a1.getId(),e1.getId());
	u2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u2);
        doc.getStatementOrBundle().add(u2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc401() {
	Document idoc401=new IndexedDocument(pFactory,makeDoc401()).toDocument();
	assertEquals(idoc401.getStatementOrBundle().size(),2);
    }


    public Document makeDoc500() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	WasGeneratedBy wgb1=pFactory.newWasGeneratedBy(null,e1.getId(),a1.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wgb1);
        doc.getStatementOrBundle().add(wgb1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc500() {
	Document idoc500=new IndexedDocument(pFactory,makeDoc500()).toDocument();
	assertEquals(idoc500.getStatementOrBundle().size(),1);
    }

    public Document makeDoc501() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	WasGeneratedBy wgb1=pFactory.newWasGeneratedBy(null,e1.getId(),a1.getId());
	WasGeneratedBy wgb2=pFactory.newWasGeneratedBy(null,e1.getId(),a1.getId());
	wgb2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wgb1);
        doc.getStatementOrBundle().add(wgb1);
        doc.getStatementOrBundle().add(wgb2);
        doc.getStatementOrBundle().add(wgb2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc501() {
	Document idoc501=new IndexedDocument(pFactory,makeDoc501()).toDocument();
	assertEquals(idoc501.getStatementOrBundle().size(),2);
    }

    
}
