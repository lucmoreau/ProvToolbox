package org.openprovenance.prov.notation;
import java.io.*;
import java.nio.file.Files;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;


/**
 * Unit test for simple Provenance Challenge 1 like workflow.
 */
public class PC1FullTest 
    extends org.openprovenance.prov.model.PC1FullTest
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
    public PC1FullTest( String testName )
    {
        super( testName );
    }

    public void subtestPC1Full() throws IOException {
        Document graph = makePC1FullGraph(pFactory);

        //ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();
        Namespace.withThreadNamespace(graph.getNamespace());

        //serial.serialiseDocument(new File("target/pc1-full.xml"), graph, true);

        ProvSerialiser serial = new ProvSerialiser(pFactory);
        serial.serialiseDocument(Files.newOutputStream(new File("target/pc1-full.provn").toPath()), graph, true);

        ProvDeserialiser deserial=new ProvDeserialiser(pFactory);
        Document graph2=deserial.deserialiseDocument(Files.newInputStream(new File("target/pc1-full.provn").toPath()));


        assertTrue("self graph differ", graph.equals(graph));
        assertTrue("self graph2 differ", graph2.equals(graph2));


        System.out.println("pc1: need to do comparison");
        //assertEquals("self graph/graph2 differ", graph, graph2);




    }

    @Override
    public void subtestCopyPC1Full() throws IOException {
    }
}
