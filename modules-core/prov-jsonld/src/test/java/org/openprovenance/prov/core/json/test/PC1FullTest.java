package org.openprovenance.prov.core.json.test;

import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.core.test.Schemas;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/** The Provenance Challenge 1 workflow as PROV-JSON: schema-valid, and read back equal. */
public class PC1FullTest extends org.openprovenance.prov.model.test.PC1FullTest {

    String filename = "target/pc1-full.json";

    public void subtestPC1Full() throws IOException {
        Document graph = makePC1FullGraph(pFactory);
        Namespace.withThreadNamespace(graph.getNamespace());
        try (FileOutputStream out = new FileOutputStream(filename)) {
            new ProvSerialiser().serialiseDocument(out, graph, true);
        }
        graph1 = graph;
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSON, new File(filename)));
    }

    public void subtestCopyPC1Full() throws IOException {
        Document c;
        try (FileInputStream in = new FileInputStream(filename)) {
            c = new ProvDeserialiser().deserialiseDocument(in);
        }
        graph2 = c;
        assertEquals("graph1 and graph2 differ", graph1, c);
    }
}
