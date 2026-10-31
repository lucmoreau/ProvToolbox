package org.openprovenance.prov.notation.test;

import org.openprovenance.prov.model.Document;

/**
 * The round trip tests of the provext statements (qualified specializationOf, alternateOf, hadMember), where PROV-N is
 * read by the ANTLR 4 parser. Unlike {@link ExtensionRoundTripFromJavaTest}, the documents read are compared with the
 * originals. Each file is also read by the ANTLR 3 parser, which must not construct a different document.
 */
public class Antlr4ExtensionRoundTripFromJavaTest extends ExtensionRoundTripFromJavaTest {
	final ParserComparison comparison = new ParserComparison(pFactory);

	@Override
	public boolean checkTest(String name) {
		// a qualified alternateOf without identifier and attributes is written as alternateOf (see Utility.isWrittenAsProvextStatement),
		// and so read back as an AlternateOf
		return !name.contains("qualified-alternate2");
	}

	@Override
	public Document readDocument(String file) {
		return comparison.readCheckingOldParser(Antlr4Files.relocate(file));
	}

	@Override
	public void writeDocument(Document doc, String file) {
		super.writeDocument(doc, Antlr4Files.relocate(file));
	}
}
