package org.openprovenance.prov.notation;

import java.io.IOException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.xml.UncheckedTestException;

public class AttributeTest extends org.openprovenance.prov.xml.AttributeTest {
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
			return (Document) u.convertASNToJavaBean(file1,pFactory);
		} catch (IOException e) {
			throw new UncheckedTestException(e);
		} catch (Throwable e) {
			throw new UncheckedTestException(e);
		}
	}

	@Override
	public void writeDocument(Document doc, String file) {
		Namespace.withThreadNamespace(doc.getNamespace());
		String s = u.convertBeanToASN(doc,pFactory);
		u.writeTextToFile(s, file);
	}

	@Override
        public boolean checkSchema(String name) {
                return false;
        }


}
