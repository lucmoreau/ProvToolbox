package org.openprovenance.prov.generator;

import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.ProvSerialiser;
import org.openprovenance.prov.notation.Utility;

import junit.framework.TestCase;

public class GeneratorTest extends TestCase {

	static final String EX_NS = "http://example.org/";

	ProvFactory pf=org.openprovenance.prov.vanilla.ProvFactory.getFactory();

	public GeneratorTest(String testName) {
		super(testName);
	}


	public void testGenerator1() {

		GraphGenerator gg=new GraphGenerator(50, 4, GraphGenerator.FIRST_NODE_AS_ENTITY, EX_NS, pf, null, "e1");
		gg.generateElements();
		Document doc=gg.getDocument();
		Namespace.withThreadNamespace(doc.getNamespace());
		new Utility(DateTimeOption.PRESERVE,null).writeDocument(doc, "target/graph50-4.provn", pf);
	}




	public void testGenerator2() {

		long seed = 1234567L;
		GraphGenerator gg1=new GraphGenerator(50, 4, GraphGenerator.FIRST_NODE_AS_ENTITY, EX_NS, pf, seed, "e1");
		GraphGenerator gg2=new GraphGenerator(50, 4, GraphGenerator.FIRST_NODE_AS_ENTITY, EX_NS, pf, seed, "e1");
		gg1.generateElements();
		gg2.generateElements();
		Document doc1=gg1.getDocument();
		Document doc2=gg2.getDocument();
		Namespace.withThreadNamespace(doc1.getNamespace());
		new Utility(DateTimeOption.PRESERVE,null).writeDocument(doc1, "target/graph50-4-seed1.provn", pf);
		Namespace.withThreadNamespace(doc2.getNamespace());
		new Utility(DateTimeOption.PRESERVE,null).writeDocument(doc2, "target/graph50-4-seed2.provn", pf);
		assertEquals(doc1, doc2);


	}

	public void testGenerator3() {
		for (int i=0; i<50; i++) {
			GraphGenerator gg=new GraphGenerator(500, 8, GraphGenerator.FIRST_NODE_AS_ENTITY, EX_NS, pf, null, "e1");
			gg.generateElements();
			Document doc=gg.getDocument();
			Namespace.withThreadNamespace(doc.getNamespace());
			new Utility(DateTimeOption.PRESERVE,null).writeDocument(doc, "target/graph50-4-test" + i + ".provn", pf);
		}
	}



}
