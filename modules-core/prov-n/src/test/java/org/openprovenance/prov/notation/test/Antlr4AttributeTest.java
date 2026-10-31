package org.openprovenance.prov.notation.test;

import org.openprovenance.prov.model.Document;

/**
 * The attribute tests of {@link AttributeTest}, where PROV-N is read by the ANTLR 4 parser. Each file is also read by
 * the ANTLR 3 parser, which must not construct a different document.
 */
public class Antlr4AttributeTest extends AttributeTest {
	final ParserComparison comparison = new ParserComparison(pFactory);

	@Override
	public Document readDocument(String file) {
		return comparison.readCheckingOldParser(Antlr4Files.relocate(file));
	}

	@Override
	public void writeDocument(Document doc, String file) {
		super.writeDocument(doc, Antlr4Files.relocate(file));
	}
}
