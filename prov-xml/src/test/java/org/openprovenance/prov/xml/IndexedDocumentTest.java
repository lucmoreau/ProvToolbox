package org.openprovenance.prov.xml;


import java.io.File;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Bundle;

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

	e2.getType().add(pFactory.newType(q("TYPE"), pFactory.getName().PROV_QUALIFIED_NAME));
	e2.getType().add(pFactory.newType(q("TYPE"), pFactory.getName().PROV_QUALIFIED_NAME));

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

    public Document makeDoc402() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("u1");
	Used u1=pFactory.newUsed(uid,a1.getId(),e1.getId());
	Used u2=pFactory.newUsed(uid,a1.getId(),e1.getId());
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

    public void testDoc402() {
	Document idoc402=new IndexedDocument(pFactory,makeDoc402()).toDocument();
	assertEquals(idoc402.getStatementOrBundle().size(),1);
	Used u=(Used) idoc402.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
    }

    public Document makeDoc403() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("u2");
	Used u1=pFactory.newUsed(uid,a1.getId(),e1.getId());
	Used u2=pFactory.newUsed(uid,a1.getId(),e1.getId());
	u2.getLabel().add(pFactory.newInternationalizedString("hello"));
	u1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	u2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u2);
        doc.getStatementOrBundle().add(u2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc403() {
	Document idoc403=new IndexedDocument(pFactory,makeDoc403()).toDocument();
	assertEquals(idoc403.getStatementOrBundle().size(),1);
	Used u=(Used) idoc403.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
	assertEquals("other",2,u.getOther().size());
    }
    
    public Document makeDoc404() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("u2");
	Used u1=pFactory.newUsed(uid,a1.getId(),e1.getId());
	Used u2=pFactory.newUsed(uid,a1.getId(),e1.getId());
	u1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	u2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
	u1.setTime(pFactory.newTimeNow());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u2);
        doc.getStatementOrBundle().add(u2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc404() {
	Document idoc404=new IndexedDocument(pFactory,makeDoc404()).toDocument();
	assertEquals(idoc404.getStatementOrBundle().size(),2);

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

    public Document makeDoc502() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("g1");
	WasGeneratedBy u1=pFactory.newWasGeneratedBy(uid,e1.getId(),a1.getId());
	WasGeneratedBy u2=pFactory.newWasGeneratedBy(uid,e1.getId(),a1.getId());
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

    public void testDoc502() {
	Document idoc502=new IndexedDocument(pFactory,makeDoc502()).toDocument();
	assertEquals(idoc502.getStatementOrBundle().size(),1);
	WasGeneratedBy u=(WasGeneratedBy) idoc502.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
    }

    public Document makeDoc503() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("g2");
	WasGeneratedBy u1=pFactory.newWasGeneratedBy(uid,e1.getId(),a1.getId());
	WasGeneratedBy u2=pFactory.newWasGeneratedBy(uid,e1.getId(),a1.getId());
	u2.getLabel().add(pFactory.newInternationalizedString("hello"));
	u1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	u2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u2);
        doc.getStatementOrBundle().add(u2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc503() {
	Document idoc503=new IndexedDocument(pFactory,makeDoc503()).toDocument();
	assertEquals(idoc503.getStatementOrBundle().size(),1);
	WasGeneratedBy u=(WasGeneratedBy) idoc503.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
	assertEquals("other",2,u.getOther().size());
    }


    public Document makeDoc600() {
        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e2"));
	Activity a1=pFactory.newActivity(q("a1"));
	WasDerivedFrom wdf1=pFactory.newWasDerivedFrom(null,e2.getId(),e1.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wdf1);
        doc.getStatementOrBundle().add(wdf1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc600() {
	Document idoc600=new IndexedDocument(pFactory,makeDoc600()).toDocument();
	assertEquals(idoc600.getStatementOrBundle().size(),1);
    }

    public Document makeDoc601() {
        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e2"));
	Activity a1=pFactory.newActivity(q("a1"));
	WasDerivedFrom wdf1=pFactory.newWasDerivedFrom(null,e2.getId(),e1.getId());
	WasDerivedFrom wdf2=pFactory.newWasDerivedFrom(null,e2.getId(),e1.getId());
	WasDerivedFrom wdf3=pFactory.newWasDerivedFrom(null,e2.getId(),e1.getId());
	wdf2.getLabel().add(pFactory.newInternationalizedString("hello"));
	wdf3.setActivity(a1.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wdf1);
        doc.getStatementOrBundle().add(wdf1);
        doc.getStatementOrBundle().add(wdf2);
        doc.getStatementOrBundle().add(wdf2);
	doc.getStatementOrBundle().add(wdf3);
	doc.getStatementOrBundle().add(wdf3);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc601() {
	Document idoc601=new IndexedDocument(pFactory,makeDoc601()).toDocument();
	assertEquals("number of wdf statements",3, idoc601.getStatementOrBundle().size());
    }

    public Document makeDoc602() {
        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e2"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("d1");
	WasDerivedFrom u1=pFactory.newWasDerivedFrom(uid,e2.getId(),e1.getId());
	WasDerivedFrom u2=pFactory.newWasDerivedFrom(uid,e2.getId(),e1.getId());
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

    public void testDoc602() {
	Document idoc602=new IndexedDocument(pFactory,makeDoc602()).toDocument();
	assertEquals(idoc602.getStatementOrBundle().size(),1);
	WasDerivedFrom u=(WasDerivedFrom) idoc602.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
    }

    public Document makeDoc603() {
        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e2"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("g2");
	WasDerivedFrom u1=pFactory.newWasDerivedFrom(uid,e2.getId(),e1.getId());
	WasDerivedFrom u2=pFactory.newWasDerivedFrom(uid,e2.getId(),e1.getId());
	u2.getLabel().add(pFactory.newInternationalizedString("hello"));
	u1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	u2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u2);
        doc.getStatementOrBundle().add(u2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc603() {
	Document idoc603=new IndexedDocument(pFactory,makeDoc603()).toDocument();
	assertEquals(idoc603.getStatementOrBundle().size(),1);
	WasDerivedFrom u=(WasDerivedFrom) idoc603.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
	assertEquals("other",2,u.getOther().size());
    }

    public Document makeDoc700() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Activity a1=pFactory.newActivity(q("a1"));
	WasAssociatedWith waw1=pFactory.newWasAssociatedWith(null,a1.getId(),ag1.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc700() {
	Document idoc700=new IndexedDocument(pFactory,makeDoc700()).toDocument();
	assertEquals(idoc700.getStatementOrBundle().size(),1);
    }

    public Document makeDoc701() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Activity a1=pFactory.newActivity(q("a1"));
	WasAssociatedWith waw1=pFactory.newWasAssociatedWith(null,a1.getId(),ag1.getId());
	WasAssociatedWith waw2=pFactory.newWasAssociatedWith(null,a1.getId(),ag1.getId());
	waw2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw2);
        doc.getStatementOrBundle().add(waw2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc701() {
	Document idoc701=new IndexedDocument(pFactory,makeDoc701()).toDocument();
	assertEquals(idoc701.getStatementOrBundle().size(),2);
    }

    public Document makeDoc702() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("waw1");
	WasAssociatedWith waw1=pFactory.newWasAssociatedWith(uid,a1.getId(),ag1.getId());
	WasAssociatedWith waw2=pFactory.newWasAssociatedWith(uid,a1.getId(),ag1.getId());
	waw2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw2);
        doc.getStatementOrBundle().add(waw2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc702() {
	Document idoc702=new IndexedDocument(pFactory,makeDoc702()).toDocument();
	assertEquals(idoc702.getStatementOrBundle().size(),1);
	WasAssociatedWith u=(WasAssociatedWith) idoc702.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
    }

    public Document makeDoc703() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("waw2");
	WasAssociatedWith waw1=pFactory.newWasAssociatedWith(uid,a1.getId(),ag1.getId());
	WasAssociatedWith waw2=pFactory.newWasAssociatedWith(uid,a1.getId(),ag1.getId());
	waw2.getLabel().add(pFactory.newInternationalizedString("hello"));
	waw1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	waw2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw2);
        doc.getStatementOrBundle().add(waw2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc703() {
	Document idoc703=new IndexedDocument(pFactory,makeDoc703()).toDocument();
	assertEquals(idoc703.getStatementOrBundle().size(),1);
	WasAssociatedWith u=(WasAssociatedWith) idoc703.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
	assertEquals("other",2,u.getOther().size());
    }

    public Document makeDoc704() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Activity a1=pFactory.newActivity(q("a1"));
        Entity pl=pFactory.newEntity(q("pl1"));
	QualifiedName uid=q("waw2");
	WasAssociatedWith waw1=pFactory.newWasAssociatedWith(uid,a1.getId(),ag1.getId());
	WasAssociatedWith waw2=pFactory.newWasAssociatedWith(uid,a1.getId(),ag1.getId());
	WasAssociatedWith waw3=pFactory.newWasAssociatedWith(uid,a1.getId(),ag1.getId());
	waw2.getLabel().add(pFactory.newInternationalizedString("hello"));
	waw1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	waw2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
	waw3.setActivity(pl.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw1);
        doc.getStatementOrBundle().add(waw2);
        doc.getStatementOrBundle().add(waw2);
        doc.getStatementOrBundle().add(waw2);
        doc.getStatementOrBundle().add(waw3);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc704() {
	Document idoc704=new IndexedDocument(pFactory,makeDoc704()).toDocument();
	assertEquals(idoc704.getStatementOrBundle().size(),2);
	WasAssociatedWith u=(WasAssociatedWith) idoc704.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
	assertEquals("other",2,u.getOther().size());
	WasAssociatedWith u2=(WasAssociatedWith) idoc704.getStatementOrBundle().get(1);
	assertTrue("activity",u2.getActivity()!=null);
    }

    public Document makeDoc800() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Entity e1=pFactory.newEntity(q("e1"));
	WasAttributedTo wat1=pFactory.newWasAttributedTo(null,e1.getId(),ag1.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wat1);
        doc.getStatementOrBundle().add(wat1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc800() {
	Document idoc800=new IndexedDocument(pFactory,makeDoc800()).toDocument();
	assertEquals(idoc800.getStatementOrBundle().size(),1);
    }

    public Document makeDoc801() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Entity e1=pFactory.newEntity(q("e1"));
	WasAttributedTo wat1=pFactory.newWasAttributedTo(null,e1.getId(),ag1.getId());
	WasAttributedTo wat2=pFactory.newWasAttributedTo(null,e1.getId(),ag1.getId());
	wat2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wat1);
        doc.getStatementOrBundle().add(wat1);
        doc.getStatementOrBundle().add(wat2);
        doc.getStatementOrBundle().add(wat2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc801() {
	Document idoc801=new IndexedDocument(pFactory,makeDoc801()).toDocument();
	assertEquals(idoc801.getStatementOrBundle().size(),2);
    }

    public Document makeDoc802() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Entity e1=pFactory.newEntity(q("e1"));
	QualifiedName uid=q("waw1");
	WasAttributedTo wat1=pFactory.newWasAttributedTo(uid,e1.getId(),ag1.getId());
	WasAttributedTo wat2=pFactory.newWasAttributedTo(uid,e1.getId(),ag1.getId());
	wat2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wat1);
        doc.getStatementOrBundle().add(wat1);
        doc.getStatementOrBundle().add(wat2);
        doc.getStatementOrBundle().add(wat2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc802() {
	Document idoc802=new IndexedDocument(pFactory,makeDoc802()).toDocument();
	assertEquals(idoc802.getStatementOrBundle().size(),1);
	WasAttributedTo u=(WasAttributedTo) idoc802.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
    }

    public Document makeDoc803() {
        Agent ag1=pFactory.newAgent(q("ag1"));
	Entity e1=pFactory.newEntity(q("e1"));
	QualifiedName uid=q("waw2");
	WasAttributedTo wat1=pFactory.newWasAttributedTo(uid,e1.getId(),ag1.getId());
	WasAttributedTo wat2=pFactory.newWasAttributedTo(uid,e1.getId(),ag1.getId());
	wat2.getLabel().add(pFactory.newInternationalizedString("hello"));
	wat1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	wat2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wat1);
        doc.getStatementOrBundle().add(wat1);
        doc.getStatementOrBundle().add(wat2);
        doc.getStatementOrBundle().add(wat2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc803() {
	Document idoc803=new IndexedDocument(pFactory,makeDoc803()).toDocument();
	assertEquals(idoc803.getStatementOrBundle().size(),1);
	WasAttributedTo u=(WasAttributedTo) idoc803.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
	assertEquals("other",2,u.getOther().size());
    }


    public Document makeDoc900() {
        Agent a1=pFactory.newAgent(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	WasInformedBy wib1=pFactory.newWasInformedBy(null,a2.getId(),a1.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wib1);
        doc.getStatementOrBundle().add(wib1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc900() {
	Document idoc900=new IndexedDocument(pFactory,makeDoc900()).toDocument();
	assertEquals(idoc900.getStatementOrBundle().size(),1);
    }

    public Document makeDoc901() {
        Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	WasInformedBy wib1=pFactory.newWasInformedBy(null,a2.getId(),a1.getId());
	WasInformedBy wib2=pFactory.newWasInformedBy(null,a2.getId(),a1.getId());
	wib2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wib1);
        doc.getStatementOrBundle().add(wib1);
        doc.getStatementOrBundle().add(wib2);
        doc.getStatementOrBundle().add(wib2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc901() {
	Document idoc901=new IndexedDocument(pFactory,makeDoc901()).toDocument();
	assertEquals(idoc901.getStatementOrBundle().size(),2);
    }

    public Document makeDoc902() {
        Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	QualifiedName uid=q("wib1");
	WasInformedBy wib1=pFactory.newWasInformedBy(uid,a2.getId(),a1.getId());
	WasInformedBy wib2=pFactory.newWasInformedBy(uid,a2.getId(),a1.getId());
	wib2.getLabel().add(pFactory.newInternationalizedString("hello"));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wib1);
        doc.getStatementOrBundle().add(wib1);
        doc.getStatementOrBundle().add(wib2);
        doc.getStatementOrBundle().add(wib2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc902() {
	Document idoc902=new IndexedDocument(pFactory,makeDoc902()).toDocument();
	assertEquals(idoc902.getStatementOrBundle().size(),1);
	WasInformedBy u=(WasInformedBy) idoc902.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
    }

    public Document makeDoc903() {
        Activity a1=pFactory.newActivity(q("a1"));
	Activity a2=pFactory.newActivity(q("a2"));
	QualifiedName uid=q("wib2");
	WasInformedBy wib1=pFactory.newWasInformedBy(uid,a2.getId(),a1.getId());
	WasInformedBy wib2=pFactory.newWasInformedBy(uid,a2.getId(),a1.getId());
	wib2.getLabel().add(pFactory.newInternationalizedString("hello"));
	wib1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	wib2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wib1);
        doc.getStatementOrBundle().add(wib1);
        doc.getStatementOrBundle().add(wib2);
        doc.getStatementOrBundle().add(wib2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc903() {
	Document idoc903=new IndexedDocument(pFactory,makeDoc903()).toDocument();
	assertEquals(idoc903.getStatementOrBundle().size(),1);
	WasInformedBy u=(WasInformedBy) idoc903.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
	assertEquals("other",2,u.getOther().size());
    }

    public Document makeDoc1000() {
        Entity e1=pFactory.newEntity(q("e1"));
        Entity e2=pFactory.newEntity(q("e2"));
	SpecializationOf spec=pFactory.newSpecializationOf(e1.getId(),e2.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(spec);
        doc.getStatementOrBundle().add(spec);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc1000() {
	Document idoc1000=new IndexedDocument(pFactory,makeDoc1000()).toDocument();
	assertEquals(idoc1000.getStatementOrBundle().size(),1);
    }
    public Document makeDoc1001() {
        Entity e1=pFactory.newEntity(q("e1"));
        Entity e2=pFactory.newEntity(q("e2"));
	AlternateOf alt=pFactory.newAlternateOf(e1.getId(),e2.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(alt);
        doc.getStatementOrBundle().add(alt);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc1001() {
	Document idoc1001=new IndexedDocument(pFactory,makeDoc1001()).toDocument();
	assertEquals(idoc1001.getStatementOrBundle().size(),1);
    }

    public Document makeDoc1100() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	WasInvalidatedBy wgb1=pFactory.newWasInvalidatedBy(null,e1.getId(),a1.getId());
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(wgb1);
        doc.getStatementOrBundle().add(wgb1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc1100() {
	Document idoc1100=new IndexedDocument(pFactory,makeDoc1100()).toDocument();
	assertEquals(idoc1100.getStatementOrBundle().size(),1);
    }

    public Document makeDoc1101() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	WasInvalidatedBy wgb1=pFactory.newWasInvalidatedBy(null,e1.getId(),a1.getId());
	WasInvalidatedBy wgb2=pFactory.newWasInvalidatedBy(null,e1.getId(),a1.getId());
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

    public void testDoc1101() {
	Document idoc1101=new IndexedDocument(pFactory,makeDoc1101()).toDocument();
	assertEquals(idoc1101.getStatementOrBundle().size(),2);
    }

    public Document makeDoc1102() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("g1");
	WasInvalidatedBy u1=pFactory.newWasInvalidatedBy(uid,e1.getId(),a1.getId());
	WasInvalidatedBy u2=pFactory.newWasInvalidatedBy(uid,e1.getId(),a1.getId());
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

    public void testDoc1102() {
	Document idoc1102=new IndexedDocument(pFactory,makeDoc1102()).toDocument();
	assertEquals(idoc1102.getStatementOrBundle().size(),1);
	WasInvalidatedBy u=(WasInvalidatedBy) idoc1102.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
    }

    public Document makeDoc1103() {
        Entity e1=pFactory.newEntity(q("e1"));
	Activity a1=pFactory.newActivity(q("a1"));
	QualifiedName uid=q("g2");
	WasInvalidatedBy u1=pFactory.newWasInvalidatedBy(uid,e1.getId(),a1.getId());
	WasInvalidatedBy u2=pFactory.newWasInvalidatedBy(uid,e1.getId(),a1.getId());
	u2.getLabel().add(pFactory.newInternationalizedString("hello"));
	u1.getOther().add(pFactory.newOther(q("ELEMENT"), 1, pFactory.getName().XSD_INT));
	u2.getOther().add(pFactory.newOther(q("ELEMENT"), 2, pFactory.getName().XSD_INT));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u1);
        doc.getStatementOrBundle().add(u2);
        doc.getStatementOrBundle().add(u2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc1103() {
	Document idoc1103=new IndexedDocument(pFactory,makeDoc1103()).toDocument();
	assertEquals(idoc1103.getStatementOrBundle().size(),1);
	WasInvalidatedBy u=(WasInvalidatedBy) idoc1103.getStatementOrBundle().get(0);
	assertEquals("label",1,u.getLabel().size());
	assertEquals("other",2,u.getOther().size());
    }

    public Document makeDoc2000() {
	Bundle bun1=pFactory.newNamedBundle(q("b1"), null);
	Bundle bun2=pFactory.newNamedBundle(q("b2"), null);

        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e2"));
	bun1.getStatement().add(e1);
	bun1.getStatement().add(e1);
	bun2.getStatement().add(e2);
	bun2.getStatement().add(e2);
	bun2.getStatement().add(e2);
	
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(bun1);
        bun1.setNamespace(Namespace.gatherNamespaces(bun1));
        doc.getStatementOrBundle().add(bun2);
        bun2.setNamespace(Namespace.gatherNamespaces(bun2));
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc2000() throws JAXBException {
	Document doc=makeDoc2000();
	ProvSerialiser.getThreadProvSerialiser().serialiseDocument(new File("target/doc2000a.xml"), doc, true);

	Document idoc2000=new IndexedDocument(pFactory,doc,false).toDocument();
	ProvSerialiser.getThreadProvSerialiser().serialiseDocument(new File("target/doc2000b.xml"), idoc2000, true);
	assertEquals(idoc2000.getStatementOrBundle().size(),2);
    }

    public Document makeDoc2001() {
	Bundle bun1=pFactory.newNamedBundle(q("b1"), null);
	Bundle bun2=pFactory.newNamedBundle(q("b1"), null);

        Entity e1=pFactory.newEntity(q("e1"));
	Entity e2=pFactory.newEntity(q("e2"));
	
	bun1.getStatement().add(e1);
	bun1.getStatement().add(e2);	
	bun2.getStatement().add(e1);
	bun2.getStatement().add(e2);
	
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(bun1);
        doc.getStatementOrBundle().add(bun2);
        Namespace nss=Namespace.gatherNamespaces(doc);
        doc.setNamespace(nss);
        return doc;
    }

    public void testDoc2001() throws JAXBException {
	Document idoc2001=new IndexedDocument(pFactory,makeDoc2001(),false).toDocument();
	assertEquals(idoc2001.getStatementOrBundle().size(),1);
	ProvSerialiser.getThreadProvSerialiser().serialiseDocument(new File("target/doc2001.xml"), idoc2001, true);

    }
    
    public void testDoc2002() throws JAXBException {
   	Document doc=makeDoc2000();
   	// flattening!
   	Document idoc2000=new IndexedDocument(pFactory,doc,true).toDocument();
   	ProvSerialiser.getThreadProvSerialiser().serialiseDocument(new File("target/doc2002.xml"), idoc2000, true);
   	assertEquals(idoc2000.getStatementOrBundle().size(),2);
       }

    

    public void testDoc2003() throws JAXBException {
   	Document doc=makeDoc2000();
   	// flattening!
   	Document idoc2001=new IndexedDocument(pFactory,doc,true).toDocument();
   	ProvSerialiser.getThreadProvSerialiser().serialiseDocument(new File("target/doc2003.xml"), idoc2001, true);
   	assertEquals(idoc2001.getStatementOrBundle().size(),2);
       }
    
    public void testConcat1() throws JAXBException {
	IndexedDocument idoc=new IndexedDocument(pFactory, pFactory.newDocument(),false);
	idoc.merge(makeDoc100());
	idoc.merge(makeDoc200());
	idoc.merge(makeDoc200());
	idoc.merge(makeDoc300());
	idoc.merge(makeDoc400());
	idoc.merge(makeDoc2000());
	
	Document cDoc1=idoc.toDocument();
   	ProvSerialiser.getThreadProvSerialiser().serialiseDocument(new File("target/concat1.xml"), cDoc1, true);
    }

    public void testConcat2() throws JAXBException {
	IndexedDocument idoc=new IndexedDocument(pFactory, pFactory.newDocument(),true);
	idoc.merge(makeDoc100());
	idoc.merge(makeDoc200());
	idoc.merge(makeDoc200());
	idoc.merge(makeDoc300());
	idoc.merge(makeDoc400());
	idoc.merge(makeDoc2000());
	
	Document cDoc2=idoc.toDocument();
   	ProvSerialiser.getThreadProvSerialiser().serialiseDocument(new File("target/concat2.xml"), cDoc2, true);
    }

    
}
