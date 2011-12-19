package org.openprovenance.prov.asn;
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
        // currently, no prefix used, all qnames map to PC1_NS
        namespaces.put("_",PC1_NS);
        namespaces.put("xsd",NamespacePrefixMapper.XSD_NS);
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



    public void testReadXMLGraph() throws javax.xml.bind.JAXBException {
        
        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();
        Container c=deserial.deserialiseContainer(new File("../xml/target/pc1-full.xml"));
        
        BeanTraversal bt=new BeanTraversal(new BeanASNConstructor(new ASNConstructor()));
        Object o=bt.convert(c);
        System.out.println(""+ o);
    }
        


}
