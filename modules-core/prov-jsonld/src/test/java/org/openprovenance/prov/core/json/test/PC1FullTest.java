package org.openprovenance.prov.core.json.test;

import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvUtilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Unit test for simple Provenance Challenge 1 like workflow.
 */
public class PC1FullTest extends org.openprovenance.prov.model.test.PC1FullTest {


	String filename="target/pc1-full.json";


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

		try {
			assertEquals("graph1 and graph2 differ", graph1, c);
            fail();
		} catch (AssertionError e) {
			// expected
			System.out.println("********* pc1-full.json: graph1 and graph2 differ  *********");
		}

	}

	ProvUtilities util=new ProvUtilities();

	public void subtestReadXMLGraph() throws IOException {

		ProvDeserialiser deserial = new ProvDeserialiser();
		Document c = deserial.deserialiseDocument(new FileInputStream(filename));
		graph2 = c;

		graph2.setNamespace(graph1.getNamespace());
		ProvSerialiser serial = new ProvSerialiser();
		Namespace.withThreadNamespace(graph2.getNamespace());

		serial.serialiseDocument(new FileOutputStream("target/pc1-full2.xml"), graph2, true);

		// System.out.println("a0" + graph1.getRecords().getActivity().get(0));
		// System.out.println("a0" + graph2.getRecords().getActivity().get(0));

		assertTrue("graph1 a* and graph2 a* differ", util.getActivity(graph1).equals(util.getActivity(graph2)));

		// failing because of comparison of Elements <pc1:url>...</pc1:url>
		assertFalse("graph1 e* and graph2 e* differ", util.getEntity(graph1).equals(util.getEntity(graph2)));

		assertFalse("graph1 and graph2 differ", graph1.equals(graph2));

		Document c2 = deserial.deserialiseDocument(new FileInputStream("target/pc1-full.xml"));
		c2.setNamespace(graph1.getNamespace());

		assertFalse("c e* and c2 e* differ",
				util.getEntity(c)
						.equals(util.getEntity(c2)));
		assertFalse("c and c2 differ", c.equals(c2));

	}


}
