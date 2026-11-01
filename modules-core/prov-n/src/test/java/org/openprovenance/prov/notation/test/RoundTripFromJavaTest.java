package org.openprovenance.prov.notation.test;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.model.test.ProvFrameworkTest;
import org.openprovenance.prov.model.test.UncheckedTestException;

public class RoundTripFromJavaTest extends org.openprovenance.prov.model.test.RoundTripFromJavaTest {
	final Utility u = new Utility(DateTimeOption.PRESERVE, null);

	public String extension() {
		return ".provn";
	}

	public boolean checkTest(String name) {
		// TODO: prov-n does not support hadMember with multiple entities
		if (name.contains("member2") || name.contains("member3")
				|| name.contains("Membership3") || name.contains("Membership4") ) {
			System.out.println(escapeRed("########## Skipping  comparison for " + name + ", not supported in PROV-N"));
			return false;
		}
		return true;
	}

	@Override
	public Document readDocument(String file1) {
		try {
			System.out.println(" reading from " + file1);
			return (Document) u.convertSyntaxTreeToJavaBean(file1, ProvFrameworkTest.pFactory);
		} catch (Throwable e) {
			throw new UncheckedTestException(e);
		}
	}

	@Override
	public void writeDocument(Document doc, String file) {
		System.out.println("writing to " + file);
		Namespace.withThreadNamespace(doc.getNamespace());
		String s = u.convertBeanToSyntaxTree(doc, ProvFrameworkTest.pFactory);
		u.writeTextToFile(s, file);
	}

	@Override
	public boolean checkSchema(String name) {
		return false;
	}


	@Override
	public void testQualifiedAlternateOf2() {
	}

	@Override
	public void testQualifiedSpecializationOf2() {
	}

	@Override
	public void testQualifiedHadMember2() {
	}

	@Override
	public void testQualifiedAlternateOf1() {
	}

	@Override
	public void testQualifiedSpecializationOf1() {
	}

	@Override
	public void testQualifiedHadMember1() {
	}
	@Override
	public void testDictionaryMembership1() {
		// a membership without entry is written as no statement at all
		System.out.println(escapeRed("########## Skipping testDictionaryMembership1 (provn): a membership without entry"));
	}
	// PROV-N has one key-entity pair per prov:hadDictionaryMember: a membership with several entries is written as
	// several statements, and so read back as several memberships
	@Override public void testDictionaryMembership3() {
		System.out.println(escapeRed("########## Skipping testDictionaryMembership3 (provn): several entries in a membership"));
	}
	@Override public void testDictionaryMembership4() {
		System.out.println(escapeRed("########## Skipping testDictionaryMembership4 (provn): several entries in a membership"));
	}

}
