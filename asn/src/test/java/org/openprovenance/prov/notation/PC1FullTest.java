package org.openprovenance.prov.notation;
import java.io.File;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.net.URI;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.openprovenance.prov.xml.BeanTraversal;
import org.openprovenance.prov.xml.Container;
import org.openprovenance.prov.xml.ProvDeserialiser;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.NamespacePrefixMapper;

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
        namespaces=new Hashtable();
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
        Container c=deserial.deserialiseContainer(new File("../xml/target/pc1-full.xml"));
        Utility u=new Utility();


        String[] schemaFiles=new String[1];
        schemaFiles[0]="../xml/src/test/resources/pc1.xsd";
        deserial.validateContainer(schemaFiles,new File("../xml/target/pc1-full.xml"));
        
        String s=u.convertBeanToASN(c);
        System.out.println(s);

        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();

        c.setNss(namespaces);
        Container c2=(Container)u.convertJavaBeanToJavaBean(c);
        c2.setNss(namespaces);

        serial.serialiseContainer(new File("target/pc1-full-2.xml"),c2,true);

    }
        


}
