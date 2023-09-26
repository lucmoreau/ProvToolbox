package org.openprovenance.prov.service.translator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.DocumentEquality;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.RoundTripFromJavaTest;
import org.openprovenance.prov.notation.ProvSerialiser;
import org.openprovenance.prov.service.DocumentMessageBodyReader;
import org.openprovenance.prov.service.StringMessageBodyReader;
import org.openprovenance.prov.service.core.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.vanilla.ProvFactory;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.*;

import static org.openprovenance.prov.interop.InteropMediaType.*;
import static org.openprovenance.prov.service.translator.DateIT.escapeRed;


public class TranslateIT extends RoundTripFromJavaTest {

    static Logger logger = LogManager.getLogger(TranslateIT.class);

    private final Client client;
    private final DocumentEquality documentEquality;
    final private VanillaDocumentMessageBodyWriter bodyWriter;

    public TranslateIT() {
        this.documentEquality = new DocumentEquality(mergeDuplicateProperties(),null);
        this.bodyWriter = new VanillaDocumentMessageBodyWriter(new ProvSerialiser(new ProvFactory()));
        this.client=getClient();

    }

    public Client getClient() {
        Client client= ClientBuilder.newBuilder().build();
        client.register(bodyWriter);
        client.register(DocumentMessageBodyReader.class);
        return client;
    }



    final static Properties properties = Objects.requireNonNull(Configuration.getPropertiesFromClasspath(TranslateIT.class, "config.properties"));
    final static String port= properties.getProperty("service.port");
    final static String context= properties.getProperty("service.context");
    final static String host= properties.getProperty("service.host");
    final static String protocol= properties.getProperty("service.protocol");

    final static String hostURLprefix= protocol + "://" + host + ":" + port + context;
    final static String postURL=hostURLprefix + "/provapi/documents2/";
    final static String expansionURL=hostURLprefix + "/provapi/documents/";
    final static String resourcesURLprefix=hostURLprefix + "/provapi/resources/";
    final static String validationURL=hostURLprefix + "/provapi/documents/";
    final static String htmlURL=hostURLprefix + "/contact.html";





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

        if (name.endsWith("bundle4"+extension()) && format.toString().equals("JSON")) {
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
        //Document doc4 = readDocument(location,MEDIA_APPLICATION_TRIG,           ".trig");
        Document doc5 = readDocument(location,MEDIA_APPLICATION_JSONLD,         ".jsonld");
        @SuppressWarnings("unused")
        Object o1=readObject(location, MEDIA_IMAGE_SVG_XML);
        List<Pair> ll= new LinkedList<>();
        ll.add(new Pair(doc1, ProvFormat.PROVX));
        ll.add(new Pair(doc2, ProvFormat.JSON));
        ll.add(new Pair(doc3, ProvFormat.PROVN));
        //ll.add(new Pair(doc4, ProvFormat.TRIG));
        ll.add(new Pair(doc5, ProvFormat.JSONLD));
        return ll;
    }

    public Document readDocument(String location, String media, String extension) {
        WebTarget target=client.target(location + extension);
        Response response2=target.request(media).get();
        return response2.readEntity(org.openprovenance.prov.vanilla.Document.class);
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

        WebTarget target = client.target(postURL);
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
    public void testDictionaryMembership2() {}
    public void testDictionaryMembership3() {}
    public void testDictionaryMembership4() {}


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
        he.getOther().add(pFactory.newOther("http://example.org/", "tag1", "ex", "hello", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example.org/", "tag1", "ex", "bonjour", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example.org/", "tag1", "ex", pFactory.newGYear(2002), name.XSD_GYEAR));
        he.getOther().add(pFactory.newOther("http://example.org/", "tag2", "ex", "bye", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example2.org/", "tag3", "ex2", "hi", name.XSD_STRING));
        //FIXME: PROBLEM WITH SERIALIZATION of MULTI-LINE STRINGS
      //he.getOther().add(pFactory.newOther("http://example.org/", "tag1", "ex", "hello\nover\nmore\nlines", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example.org/", this.get0tagWithDigit(), "ex", "hello", name.XSD_STRING));
        he.getOther().add(pFactory.newOther("http://example.org/", this.get0tagWithDigit(), "ex", "hello2", name.XSD_STRING));
    }

    @Override
    public void testEntity101() {
        System.out.println("########## Skipping testEntity101 in TranslatorIT.java");
    }
    @Override
    public void testBundle5() {
        //super.testBundle5();
        System.out.println(escapeRed("########## Skipping testBundle5 in TranslatorIT.java"));
    }
    public void testBundle4() {
        //
        // .testBundle5();
        System.out.println(escapeRed("########## Skipping testBundle4 in TranslatorIT.java"));
    }
}
