package org.openprovenance.prov.notation;
import java.io.File;
import java.util.Hashtable;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.ProvFactory;
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
    public PubTest( String testName )
    {
        super( testName );
    }

    static public Document graph1;

    public void testReadASNSaveXML() throws java.io.IOException, java.lang.Throwable {
        String file="src/test/resources/prov/w3c-publication1.prov-asn";
        testReadASNSaveXML(file,"target/w3c-publication1.prov-xml");
    }

    public void testReadASNSaveXML(String file, String file2) throws java.io.IOException, java.lang.Throwable {

        Utility u=new Utility();
        CommonTree tree = u.convertASNToTree(file);

        Object o2=u.convertTreeToJavaBean(tree);

        graph1=(Document)o2;

        

        try {
            ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
            serial.serialiseDocument(new File(file2),(Document)o2,true);

            assertTrue(true);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}

