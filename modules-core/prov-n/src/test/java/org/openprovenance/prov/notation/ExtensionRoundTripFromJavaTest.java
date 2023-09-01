package org.openprovenance.prov.notation;

import java.util.Arrays;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.UncheckedTestException;

abstract public class ExtensionRoundTripFromJavaTest extends org.openprovenance.prov.model.ExtensionRoundTripFromJavaTest {
	final Utility u = new Utility();

	public ExtensionRoundTripFromJavaTest(String name) {
		super(name);
	}

	public String extension() {
		return ".provn";
	}

	public boolean checkTest(String name) {
		// TODO: prov-n does not support hadMember with multiple entities
		return !(name.contains("qualified") || name.contains("member2") || name.contains("member3")
				|| name.contains("Membership3") || name.contains("Membership4"));
	}

	@Override
	public Document readDocument(String file1) {
		try {
			//return (Document) u.convertASNToJavaBean(file1,pFactory);
			return null;
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

	public void makeDocAndTest(Statement[] statements, String file,
								 Statement[] opt, boolean check) {
		Document doc = pFactory.newDocument();
        for (Statement statement : statements) {
            doc.getStatementOrBundle().add(statement);
        }
		updateNamespaces(doc);

		check = check && checkTest(file);

		String file1 = (opt == null) ? file : file + "-S";
		file1 = file1 + extension();

		String s = u.convertBeanToSyntaxTree(doc,pFactory);
		u.writeTextToFile(s, file1);

		Document doc2;
		try {
			doc2 = (Document) u.convertSyntaxTreeToJavaBean(file1,pFactory);
			compareDocuments(doc, doc2, check && checkTest(file1));

			if (opt != null) {
				doc.getStatementOrBundle().addAll(Arrays.asList(opt));
				String file2 = file + "-M";
				file2 = file2 + extension();

				String s2 = u.convertBeanToSyntaxTree(doc,pFactory);
				u.writeTextToFile(s2, file2);

				Document doc3 = (Document) u.convertSyntaxTreeToJavaBean(file2,pFactory);
				compareDocuments(doc, doc3, check && checkTest(file2));
			}

			return;
		}  catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(false);

	}


}
