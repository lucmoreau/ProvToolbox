package org.openprovenance.prov.notation.test;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.notation.ProvNReader;

/**
 * The round trip tests of the provext statements (qualified specializationOf, alternateOf, hadMember), where PROV-N is
 * read by the ANTLR 4 parser. Unlike {@link ExtensionRoundTripFromJavaTest}, the documents read are compared with the
 * originals.
 */
public class Antlr4ExtensionRoundTripFromJavaTest extends ExtensionRoundTripFromJavaTest {
	final ProvNReader reader = new ProvNReader(pFactory);

	@Override
	public boolean checkTest(String name) {
		// a qualified alternateOf without identifier and attributes is written as alternateOf (see Utility.isWrittenAsProvextStatement),
		// and so read back as an AlternateOf
		return !name.contains("qualified-alternate2");
	}

	@Override
	public Document readDocument(String file) {
		return reader.readDocument(Antlr4Files.relocate(file));
	}

	@Override
	public void writeDocument(Document doc, String file) {
		super.writeDocument(doc, Antlr4Files.relocate(file));
	}
}
