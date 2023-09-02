package org.openprovenance.prov.notation;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;


/**
 * Unit test for simple Provenance Challenge 1 like workflow.
 */
public class PC1FullTest extends org.openprovenance.prov.model.PC1FullTest
{
    
    public static ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();



    public void subtestPC1Full() throws IOException {
        Document graph = makePC1FullGraph(pFactory);

        Namespace.withThreadNamespace(graph.getNamespace());

        ProvSerialiser serial = new ProvSerialiser(pFactory);
        serial.serialiseDocument(Files.newOutputStream(new File("target/pc1-full.provn").toPath()), graph, true);

        ProvDeserialiser deserial=new ProvDeserialiser(pFactory);
        Document graph2=deserial.deserialiseDocument(Files.newInputStream(new File("target/pc1-full.provn").toPath()));


        assertEquals("self graph differ", graph, graph);
        assertEquals("self graph2 differ", graph2, graph2);

        assertEquals("self graph/graph2 differ", graph, graph2);




    }

    @Override
    public void subtestCopyPC1Full() throws IOException {
    }
}
