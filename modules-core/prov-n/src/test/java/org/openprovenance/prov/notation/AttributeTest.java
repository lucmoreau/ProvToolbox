package org.openprovenance.prov.notation;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.UncheckedTestException;

public class AttributeTest extends org.openprovenance.prov.model.AttributeTest {
	final Utility u = new Utility();

	public AttributeTest(String testName) {
		super(testName);
	}
	

	@Override
	public String extension() {
		return ".provn";
	}
		
	@Override
	public Document readDocument(String file1) {
		try {
			return (Document) u.convertSyntaxTreeToJavaBean(file1,pFactory);
		} catch (Throwable e) {
			throw new UncheckedTestException(e);
		}
	}

	@Override
	public void writeDocument(Document doc, String file) {
		Namespace.withThreadNamespace(doc.getNamespace());
		String s = u.convertBeanToSyntaxTree(doc,pFactory);
		u.writeTextToFile(s, file);
	}

	@Override
        public boolean checkSchema(String name) {
                return false;
        }


}
