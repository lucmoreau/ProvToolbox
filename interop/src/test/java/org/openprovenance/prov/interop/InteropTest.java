package org.openprovenance.prov.interop;
import java.io.File;
import java.io.StringWriter;
import java.util.Hashtable;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for interoperability framework
 */
public class InteropTest 
    extends TestCase
{

    public static final String PC1_NS="http://www.ipaw.info/pc1/";
    public static final String PC1_PREFIX="pc1";
    public static final String PRIM_NS="http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX="prim";
    


    
    //    public static ProvFactory pFactory;

    static {

        // namespaces.put("xsd",NamespacePrefixMapper.XSD_NS);
        // namespaces.put("prim","http://openprovenance.org/primitives#");
        //pFactory=new ProvFactory(namespaces);
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public InteropTest( String testName )
    {
        super( testName );
    }



    public void testXML2XML() throws javax.xml.bind.JAXBException,  org.xml.sax.SAXException, java.io.IOException {

        InteropFramework interop=new InteropFramework();

        String[] schemaFiles=new String[1];
        schemaFiles[0]="../xml/src/test/resources/pc1.xsd";

        Hashtable<String,String> namespaces;
        namespaces=new Hashtable();
        namespaces.put("pc1",PC1_NS);

        interop.xml2xml("../xml/target/pc1-full.xml",
                        "target/pc1-full.prov-xml",
                        schemaFiles,
                        namespaces);
        
    }


    public void testXML2ASN() throws javax.xml.bind.JAXBException,  org.xml.sax.SAXException, java.io.IOException {

        InteropFramework interop=new InteropFramework();

        String[] schemaFiles=new String[1];
        schemaFiles[0]="../xml/src/test/resources/pc1.xsd";


        interop.xml2asn("../xml/target/pc1-full.xml",
                        "target/pc1-full.prov-asn",
                        schemaFiles);
        
    }


    public void testASN2RDF() throws javax.xml.bind.JAXBException,  org.xml.sax.SAXException, java.io.IOException, Throwable {

        InteropFramework interop=new InteropFramework();

        interop.asn2rdf("../asn/src/test/resources/prov/w3c-publication1.prov-asn",
                        "target/w3c-publication1.ttl");
        
    }
        


}
