package org.openprovenance.prov.service.translator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.interop.ApiUriFragments;
import org.openprovenance.prov.model.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.service.core.readers.VanillaDocumentMessageBodyReader;
import org.openprovenance.prov.service.client.StringMessageBodyReader;
import org.openprovenance.prov.service.client.ClientConfig;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.model.test.RoundTripFromJavaTest;
import org.openprovenance.prov.vanilla.ProvFactory;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.*;

import static org.openprovenance.prov.model.interop.InteropMediaType.*;

// no storage layer configured to process this.
abstract public class TranslateIT extends RoundTripFromJavaTest implements ApiUriFragments  {

    final static Logger logger = LogManager.getLogger(TranslateIT.class);
    final static ClientConfig config=new ClientConfig(TranslateIT.class);
    private final Client client;
    private final DocumentEquality documentEquality;
    final private VanillaDocumentMessageBodyWriter bodyWriter;
    private final InteropFramework intf;
    private final ProvFactory pf;

    public TranslateIT() {
        this.documentEquality = new DocumentEquality(mergeDuplicateProperties(),null);
        pf = new ProvFactory();
        intf = new InteropFramework(pf);
        this.bodyWriter = new VanillaDocumentMessageBodyWriter(intf);
        this.client=getClient();

    }

    public Client getClient() {
        Client client= ClientBuilder.newBuilder().build();
        client.register(bodyWriter);
        client.register(new VanillaDocumentMessageBodyReader(pf));
        return client;
    }


    static public class Pair {
        Document document;
        ProvFormat format;
        Pair(Document document, ProvFormat format) {
            this.document=document;
            this.format=format;
        }
    }

    @Override
    public void compareDocAndFile(Document doc, String file, boolean check) {
        file=file+extension();
        writeDocument(doc, file);
        List<Pair> docs=readDocuments(file);
        for (Pair pair: docs) {
            Document doc3=pair.document;
            ProvFormat format=pair.format;
            compareDocuments(doc, doc3, check && checkTest(file,format));
        }
    }
    public boolean checkTest(String name, ProvFormat format)
    {

        if (name.endsWith("bundle4"+extension()) && format.equals(ProvFormat.PROVX)) {
            System.out.println(escapeRed("########## Skipping  comparison for bundle4 and PROVX"));
            return false;
        }
        if (name.endsWith("bundle5"+extension()) &&  format.equals(ProvFormat.PROVX)) {
            System.out.println(escapeRed("########## Skipping  comparison for bundle5 and PROVX"));
            return false;
        }

        return true;
    }


    // FOR RDF only
    public boolean mergeDuplicateProperties()
    {
        return true;
    }



    public List<Pair> readDocuments(String file) {

        String location=fileToUri.get(file);
        Document doc2 = readDocument(location,MEDIA_APPLICATION_JSON,           ".json");
        Document doc3 = readDocument(location,MEDIA_TEXT_PROVENANCE_NOTATION,   ".provn");
        Document doc1 = readDocument(location,MEDIA_APPLICATION_PROVENANCE_XML, ".provx");
        Document doc5 = readDocument(location,MEDIA_APPLICATION_JSONLD,         ".jsonld");
        @SuppressWarnings("unused")
        Object o1=readObject(location, MEDIA_IMAGE_SVG_XML);
        List<Pair> ll= new LinkedList<>();
        ll.add(new Pair(doc1, ProvFormat.PROVX));
        ll.add(new Pair(doc2, ProvFormat.JSON));
        ll.add(new Pair(doc3, ProvFormat.PROVN));
        ll.add(new Pair(doc5, ProvFormat.JSONLD));
        return ll;
    }

    public Document readDocument(String location, String media, String extension) {
        WebTarget target=client.target(location + extension);
        Response response2=target.request(media).get();
        org.openprovenance.prov.vanilla.Document doc = response2.readEntity(org.openprovenance.prov.vanilla.Document.class);
        return doc;
    }



    @SuppressWarnings("unused")
    public String readAsString(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        client.register(StringMessageBodyReader.class);
        WebTarget target=client.target(location);
        Response response2=target.request(media).get();
        String o=response2.readEntity(String.class);
        client.close();
        return o;
    }

    public Object readObject(String location, String media) {
        WebTarget target=client.target(location);
        Response response2=target.request(media).get();
        return response2.readEntity(InputStream.class);
    }

    static Map<String,String> fileToUri= new HashMap<>();

    @Override
    public void writeDocument(Document doc, String file) {
        logger.info("translating: " + file);

        WebTarget target = client.target(config.postURL);
        Response response=target.request(MEDIA_TEXT_PROVENANCE_NOTATION).post(Entity.entity(doc, MEDIA_TEXT_PROVENANCE_NOTATION));
        String location=response.getHeaderString("Location");
        location=location.replace(".provn","");
        fileToUri.put(file,location);
    }

    @Override
    public boolean checkSchema(String name) {
        return false;
    }


    public void compareDocuments(Document doc, Document doc2, boolean check) {
        assertEquals("self doc equality", doc, doc);
        assertEquals("self doc2 equality", doc2, doc2);
        if (check) {
            boolean result=this.documentEquality.check(doc, doc2);
            if (!result) {
                System.out.println("Pre-write graph: "+doc);
                System.out.println("Read graph: "+doc2);
            }
            assertTrue("doc equals doc2", result);
        }
    }



    public void testDictionaryInsertion1() {}
    public void testDictionaryInsertion2() {}
    public void testDictionaryInsertion3() {}
    public void testDictionaryInsertion4() {}
    public void testDictionaryInsertion5() {}
    public void testDictionaryInsertion6() {}
    public void testDictionaryInsertion7() {}
    public void testDictionaryRemoval1() {}
    public void testDictionaryRemoval2() {}
    public void testDictionaryRemoval3() {}
    public void testDictionaryRemoval4() {}
    public void testDictionaryRemoval5() {}



   // public void testMembership1() {} //ok in service.light

    public void testMembership2() {}
    public void testMembership3() {}



    public void testQualifiedAlternateOf1(){}
    public void testQualifiedAlternateOf2(){}
    public void testQualifiedHadMember1(){}
    //public void testQualifiedHadMember2(){} //ok in service.light
    public void testQualifiedSpecializationOf1(){}

    @Override
    public void addFurtherAttributes(HasOther he) {
        he.getOther().add(pFactory.newOther("http://example.org/", "tag1", "ex4", "hello", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example.org/", "tag1", "ex4", "bonjour", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example.org/", "tag1", "ex4", pFactory.newGYear(2002), name.XSD_GYEAR));
        he.getOther().add(pFactory.newOther("http://example.org/", "tag2", "ex4", "bye", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example2.org/", "tag3", "ex2", "hi", name.XSD_STRING));
        //FIXME: PROBLEM WITH SERIALIZATION of MULTI-LINE STRINGS
      //he.getOther().add(pFactory.newOther("http://example.org/", "tag1", "ex", "hello\nover\nmore\nlines", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example.org/", this.get0tagWithDigit(), "ex4", "hello", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example.org/", this.get0tagWithDigit(), "ex4", "hello2", name.XSD_STRING));
    }

    @Override
    public void testEntity101() {
        System.out.println(escapeRed("########## Skipping testEntity101 in TranslatorIT.java"));
    }
    @Override
    public void testDefault1() {
        System.out.println(escapeRed("########## Skipping testing for default1 in PROVX"));
        //super.testDefault1();
    }

    @Override
    public void testDictionaryMembership1() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership1 (service)"));
    }
    @Override
    public void testDictionaryMembership2() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership2 (service)"));
    }
    @Override public void testDictionaryMembership3() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership3 (service)"));
    }
    @Override public void testDictionaryMembership4() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership4 (service)"));
    }


}
