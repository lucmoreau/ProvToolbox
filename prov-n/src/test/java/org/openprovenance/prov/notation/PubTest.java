package org.openprovenance.prov.notation;
import java.io.File;
import java.util.Hashtable;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;
import org.openprovenance.prov.xml.Bundle;
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

    static public Bundle graph1;

    public void testReadASNSaveXML() throws java.io.IOException, java.lang.Throwable {
        String file="src/test/resources/prov/w3c-publication1.prov-asn";
        testReadASNSaveXML(file,"target/w3c-publication1.prov-xml");
    }

    public void testReadASNSaveXML(String file, String file2) throws java.io.IOException, java.lang.Throwable {

        Utility u=new Utility();
        CommonTree tree = u.convertASNToTree(file);

        Object o2=u.convertTreeToJavaBean(tree);

        graph1=(Bundle)o2;

        String o3=u.convertTreeToASN(tree);

        

        try {
            ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
            serial.serialiseBundle(new File(file2),(Bundle)o2,true);

            System.out.println("tree is " + o3);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}

