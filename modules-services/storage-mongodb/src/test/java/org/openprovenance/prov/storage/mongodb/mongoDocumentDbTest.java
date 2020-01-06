package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.JLD_LangString;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.LangString;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;

public class mongoDocumentDbTest extends TestCase {

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

        ds.writeDocument(id, Formats.ProvFormat.JSONLD, doc);


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
        ds.writeDocument(id1, Formats.ProvFormat.JSONLD, doc1);


        Document doc3=ds.readDocument(id1);

        assertNotNull(doc3);

        assertEquals(doc1,doc3);

    }


}
