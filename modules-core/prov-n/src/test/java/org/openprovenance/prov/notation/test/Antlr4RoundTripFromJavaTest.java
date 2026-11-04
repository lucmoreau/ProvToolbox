package org.openprovenance.prov.notation.test;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.notation.ProvNReader;
import org.openprovenance.prov.model.test.ProvFrameworkTest;

/**
 * The round trip tests of {@link RoundTripFromJavaTest} (Java, written as PROV-N, read back, and compared with the
 * original), where PROV-N is read by the ANTLR 4 parser.
 */
public class Antlr4RoundTripFromJavaTest extends RoundTripFromJavaTest {
	final ProvNReader reader = new ProvNReader(ProvFrameworkTest.pFactory);

	@Override
	public Document readDocument(String file) {
		return reader.readDocument(Antlr4Files.relocate(file));
	}

	@Override
	public void writeDocument(Document doc, String file) {
		super.writeDocument(doc, Antlr4Files.relocate(file));
	}
}
