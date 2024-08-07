package org.openprovenance.prov.notation.test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import junit.framework.TestCase;

import org.openprovenance.prov.model.*;
import  org.antlr.runtime.tree.CommonTree;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.notation.Utility;

/**
 * Provenance of a w3c tech report
 */
abstract public class PubTest extends TestCase
{

    public static final String PC1_NS="http://www.ipaw.info/pc1/";
    public static final String PC1_PREFIX="pc1";
    public static final String PRIM_NS="http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX="prim";
    
    
    public static ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();

    
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

    public void auxTestReadASNSaveXML() throws java.io.IOException {
        String file="src/test/resources/prov/w3c-publication1.prov-asn";
        auxTestReadASNSaveXML(file,"target/w3c-publication1.prov-xml");
    }

    public void auxTestReadASNSaveXML(String file, String file2) throws IOException {

        Utility u=new Utility(DateTimeOption.PRESERVE, null);
        CommonTree tree = u.convertSyntaxTreeToTree(file);

        Object o2=u.convertTreeToJavaBean(tree,pFactory);

        graph1=(Document)o2;

        graph1.setNamespace(Namespace.gatherNamespaces(graph1));


        ProvSerialiser serial=new org.openprovenance.prov.notation.ProvSerialiser(pFactory);
        Namespace.withThreadNamespace(graph1.getNamespace());

        serial.serialiseDocument(Files.newOutputStream(new File(file2).toPath()),(Document)o2,true);

        assertTrue(true);
    }

}

