package org.openprovenance.prov.storage.mongodb;

import junit.framework.TestCase;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;

public class MongoJLDDocumentDbTest extends TestCase {

    public static final String EX_NS = "http://example.org";
    public static final String EX_PREFIX = "ex";

    public void testMongoStorage() throws IOException {
     //   System.out.println("Creating a DS");

        MongoDocumentResourceStorage ds=new MongoDocumentResourceStorage("provtest");
     //   System.out.println("Creating a new storeID");

        String id=ds.newStore(Formats.ProvFormat.JSONLD);

    ///    System.out.println("object is " + id);

        assertNotNull(id);


        ProvFactory pf=ProvFactory.getFactory();
        Entity e1=pf.newEntity(pf.newQualifiedName(EX_NS, "e1", EX_PREFIX));
        Document doc=pf.newDocument();
        doc.getStatementOrBundle().add(e1);
        Namespace ns=new Namespace();
        ns.addKnownNamespaces();
        ns.getPrefixes().put(EX_PREFIX, EX_NS);
        doc.setNamespace(ns);

        ds.writeDocument(id, doc, Formats.ProvFormat.JSONLD);


        Document doc2=ds.readDocument(id);

        assertNotNull(doc2);

        assertEquals(doc,doc2);

        org.openprovenance.prov.model.Entity e0 = pf.newEntity(pf.newQualifiedName(EX_NS, "e0", EX_PREFIX));
        e0.getOther()
                .add(pf.newOther(pf.newQualifiedName(EX_NS, "tag2", EX_PREFIX),
                        pf.newInternationalizedString("bonjour", "fr"),
                        pf.getName().PROV_LANG_STRING));
        e0.getOther()
                .add(pf.newOther(pf.newQualifiedName(EX_NS, "tag2", EX_PREFIX),
                        //new LangString("bonjour", "fr"),
                        100,
                        pf.getName().XSD_INT));


        Document doc1=pf.newDocument();
        doc1.getStatementOrBundle().add(e0);
        Namespace ns1=new Namespace();
        ns1.addKnownNamespaces();
        ns1.getPrefixes().put(EX_PREFIX, EX_NS);
        doc1.setNamespace(ns);


        String id1=ds.newStore(Formats.ProvFormat.JSONLD);
        ds.writeDocument(id1, doc1, Formats.ProvFormat.JSONLD);


        Document doc3=ds.readDocument(id1);

        assertNotNull(doc3);

        assertEquals(doc1,doc3);



       // Template_blockBuilder maker=new Template_blockBuilder(org.openprovenance.prov.xml.ProvFactory.getFactory());
        Template_blockBuilder maker=new Template_blockBuilder(pf);
        Document graph1=maker.make("op", "Adder", "john", "co1", "10", "co2", 20, "prod", "T", 30);


        String id5=ds.newStore(Formats.ProvFormat.JSONLD);
        ds.writeDocument(id5, graph1, Formats.ProvFormat.JSONLD);


        Document graph2=ds.readDocument(id5);

        assertNotNull(graph2);

        assertEquals(graph1,graph2);



    }




}
