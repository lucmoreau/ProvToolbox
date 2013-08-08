package org.openprovenance.prov.notation;
import java.io.File;
import java.util.Hashtable;
import junit.framework.TestCase;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvDeserialiser;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.ProvFactory;

/**
 * Unit test for simple Provenance Challenge 1 like workflow.
 */
public class PC1FullTest 
    extends TestCase
{

    public static final String PC1_NS="http://www.ipaw.info/pc1/";
    public static final String PC1_PREFIX="pc1";
    public static final String PRIM_NS="http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX="prim";
    


    static final Hashtable<String,String> namespaces;

    
    public static ProvFactory pFactory;

    static {
        namespaces=new Hashtable<String, String>();
        namespaces.put("pc1",PC1_NS);
        namespaces.put("xsd",NamespacePrefixMapper.XSD_NS);
        namespaces.put("prim","http://openprovenance.org/primitives#");
        pFactory=new ProvFactory(namespaces);
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PC1FullTest( String testName )
    {
        super( testName );
    }


    

    public void testReadXMLGraph() throws javax.xml.bind.JAXBException,  org.xml.sax.SAXException, java.io.IOException {
        
        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();
        Document c=deserial.deserialiseDocument(new File("../prov-xml/target/pc1-full.xml"));
        Utility u=new Utility();


        String[] schemaFiles=new String[1];
        schemaFiles[0]="../prov-xml/src/test/resources/pc1.xsd";
	
	// TODO: now failing because of QName
        //deserial.validateDocument(schemaFiles,new File("../prov-xml/target/pc1-full.xml"));
        
        //String s=u.convertBeanToASN(c);
        //System.out.println(s);

        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();

        c.setNss(namespaces);
        Document c2=(Document)u.convertJavaBeanToJavaBean(c);
        c2.setNss(namespaces);

        serial.serialiseDocument(new File("target/pc1-full-2.xml"),c2,true);

    }
        


}
