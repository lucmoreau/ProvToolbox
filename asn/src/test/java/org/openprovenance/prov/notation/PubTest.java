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
import  org.antlr.runtime.tree.CommonTree;

/**
 * Provenance of a w3c tech report
 */
public class PubTest 
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
    public PubTest( String testName )
    {
        super( testName );
    }

    static public Container graph1;

    public void testReadASNSaveXML() throws java.io.IOException, java.lang.Throwable {
        String file="src/test/resources/prov/w3c-publication1.prov-asn";
        testReadASNSaveXML(file,"target/w3c-publication.prov-xml");
    }

    public void testReadASNSaveXML(String file, String file2) throws java.io.IOException, java.lang.Throwable {

        Utility u=new Utility();
        CommonTree tree = u.convertASNToTree(file);

        Object o2=u.convertTreeToJavaBean(tree);

        graph1=(Container)o2;

        String o3=u.convertTreeToASN(tree);

        

        try {
            ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
            serial.serialiseContainer(new File(file2),(Container)o2,true);

            System.out.println("tree is " + o3);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}

