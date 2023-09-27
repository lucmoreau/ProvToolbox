package org.openprovenance.prov.core.jsonld11;


import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;


import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;


import java.io.*;

/**
 * Unit test for simple Provenance Challenge 1 like workflow.
 */
public class PC1FullTest extends org.openprovenance.prov.model.PC1FullTest {


	String filename="target/pc1-full.jsonld";


	public void subtestPC1Full() throws FileNotFoundException {
		Document graph = makePC1FullGraph(pFactory);

		ProvSerialiser serial = new ProvSerialiser();
		Namespace.withThreadNamespace(graph.getNamespace());

		System.out.println("writing to " + filename);

		serial.serialiseDocument(new FileOutputStream(filename), graph, true);

		graph1 = graph;
		assertTrue(true);

	}



	public void subtestCopyPC1Full() throws FileNotFoundException, IOException {

		System.out.println(" reading from " + filename);

		ProvDeserialiser deserial = new ProvDeserialiser();
		Document c = deserial.deserialiseDocument(new FileInputStream(filename));
		graph2 = c;

        assertEquals("self graph1 differ", graph1, graph1);

        assertEquals("self c differ", c, c);

        assertEquals("graph1 c differ", graph1, c);

	}


}
