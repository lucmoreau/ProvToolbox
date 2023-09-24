package org.openprovenance.prov.notation;

import org.openprovenance.prov.model.*;

public class RoundTripFromJavaTest extends org.openprovenance.prov.model.RoundTripFromJavaTest {
	final Utility u = new Utility(DateTimeOption.PRESERVE, null);

	public String extension() {
		return ".provn";
	}

	public boolean checkTest(String name) {
		// TODO: prov-n does not support hadMember with multiple entities
		return !(name.contains("member2") || name.contains("member3")
				|| name.contains("Membership3") || name.contains("Membership4") );
	}

	@Override
	public Document readDocument(String file1) {
		try {
			System.out.println(" reading from " + file1);
			return (Document) u.convertSyntaxTreeToJavaBean(file1,pFactory);
		} catch (Throwable e) {
			throw new UncheckedTestException(e);
		}
	}

	@Override
	public void writeDocument(Document doc, String file) {
		System.out.println("writing to " + file);
		Namespace.withThreadNamespace(doc.getNamespace());
		String s = u.convertBeanToSyntaxTree(doc,pFactory);
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
}
