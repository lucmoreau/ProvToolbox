package org.openprovenance.prov.notation;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.UncheckedTestException;

public class ExtensionRoundTripFromJavaTest extends org.openprovenance.prov.model.ExtensionRoundTripFromJavaTest {
	final Utility u = new Utility();

	public String extension() {
		return ".provn";
	}

	public boolean checkTest(String name) {
		// TODO: prov-n does not support hadMember with multiple entities
		return !(name.contains("qualified")
				|| name.contains("member2")
				|| name.contains("member3")
				|| name.contains("Membership3")
				|| name.contains("Membership4"));
	}

	@Override
	public Document readDocument(String file1) {
		System.out.println(" reading (BUT NOT COMPARING) " + file1);
		try {
			return (Document) u.convertSyntaxTreeToJavaBean(file1,pFactory);
		} catch (Throwable e) {
			throw new UncheckedTestException(e);
		}
	}

	@Override
	public void writeDocument(Document doc, String file) {
		System.out.println("writing of  " + file);
		Namespace.withThreadNamespace(doc.getNamespace());
		String s = u.convertBeanToSyntaxTree(doc,pFactory);
		u.writeTextToFile(s, file);
	}

	@Override
	public boolean checkSchema(String name) {
		return false;
	}


}
