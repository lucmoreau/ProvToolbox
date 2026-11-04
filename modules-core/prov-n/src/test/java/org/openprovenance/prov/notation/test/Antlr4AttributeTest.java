package org.openprovenance.prov.notation.test;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.notation.ProvNReader;

/**
 * The attribute tests of {@link AttributeTest}, where PROV-N is read by the ANTLR 4 parser.
 */
public class Antlr4AttributeTest extends AttributeTest {
	final ProvNReader reader = new ProvNReader(pFactory);

	@Override
	public Document readDocument(String file) {
		return reader.readDocument(Antlr4Files.relocate(file));
	}

	@Override
	public void writeDocument(Document doc, String file) {
		super.writeDocument(doc, Antlr4Files.relocate(file));
	}
}
