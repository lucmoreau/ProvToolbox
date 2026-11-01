package org.openprovenance.prov.notation.test;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.test.ProvFrameworkTest;

/**
 * The round trip tests of {@link RoundTripFromJavaTest} (Java, written as PROV-N, read back, and compared with the
 * original), where PROV-N is read by the ANTLR 4 parser. Each file is also read by the ANTLR 3 parser, which must not
 * construct a different document.
 */
public class Antlr4RoundTripFromJavaTest extends RoundTripFromJavaTest {
	final ParserComparison comparison = new ParserComparison(ProvFrameworkTest.pFactory);

	@Override
	public Document readDocument(String file) {
		return comparison.readCheckingOldParser(Antlr4Files.relocate(file));
	}

	@Override
	public void writeDocument(Document doc, String file) {
		super.writeDocument(doc, Antlr4Files.relocate(file));
	}
}
