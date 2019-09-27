package org.openprovenance.prov.notation;

import java.io.IOException;
import java.util.Arrays;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.xml.UncheckedTestException;

public class RoundTripFromJavaTest extends
		org.openprovenance.prov.xml.RoundTripFromJavaTest {
	final Utility u = new Utility();

	public RoundTripFromJavaTest(String name) {
		super(name);
	}

	public String extension() {
		return ".provn";
	}

	public boolean checkTest(String name) {
		// TODO: prov-n does not support hadMember with multiple entities
		return !(name.contains("member2") || name.contains("member3")
				|| name.contains("Membership3") || name.contains("Membership4"));
	}

	@Override
	public Document readDocument(String file1) {
		try {
			return (Document) u.convertASNToJavaBean(file1,pFactory);
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

	public void NOmakeDocAndTest(Statement[] statements, String file,
								 Statement[] opt, boolean check) {
		Document doc = pFactory.newDocument();
		for (int i = 0; i < statements.length; i++) {
			doc.getStatementOrBundle().add(statements[i]);
		}
		updateNamespaces(doc);

		check = check && checkTest(file);

		String file1 = (opt == null) ? file : file + "-S";
		file1 = file1 + extension();

		String s = u.convertBeanToASN(doc,pFactory);
		u.writeTextToFile(s, file1);

		Document doc2;
		try {
			doc2 = (Document) u.convertASNToJavaBean(file1,pFactory);
			compareDocuments(doc, doc2, check && checkTest(file1));

			if (opt != null) {
				doc.getStatementOrBundle().addAll(Arrays.asList(opt));
				String file2 = file + "-M";
				file2 = file2 + extension();

				String s2 = u.convertBeanToASN(doc,pFactory);
				u.writeTextToFile(s2, file2);

				Document doc3 = (Document) u.convertASNToJavaBean(file2,pFactory);
				compareDocuments(doc, doc3, check && checkTest(file2));
			}

			return;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(false);

	}
	public void testEntity101() {
		Entity e = pFactory.newEntity(q("101-entity"), "entity101");
		e.getOther().add(pFactory.newOther(EX_NS, "a01b\\[c", EX_PREFIX,
				pFactory.newQualifiedName(EX2_NS, "\\=\\'\\(\\)\\,-\\:\\;\\[\\]\\.",
						EX2_PREFIX),
				name.PROV_QUALIFIED_NAME));
		e.getOther().add(pFactory.newOther(EX_NS, "a01bc", EX_PREFIX,
				pFactory.newQualifiedName(EX2_NS, "\\=\\'\\(\\)\\,-\\:\\;\\[\\]\\.",
						EX2_PREFIX),
				name.PROV_QUALIFIED_NAME));
		e.getOther().add(pFactory.newOther(EX_NS, "unicode", EX_PREFIX,
				pFactory.newQualifiedName(EX2_NS, "À-ÖØ-öø-Ͱͽ",
						EX2_PREFIX),
				name.PROV_QUALIFIED_NAME));
		e.getOther().add(pFactory.newOther(EX_NS, "À-ÖØ-öø-Ͱͽ", EX_PREFIX,
				pFactory.newQualifiedName(EX2_NS,"unicode",
						EX2_PREFIX),
				name.PROV_QUALIFIED_NAME));
		e.getOther().add(pFactory.newOther(EX_NS, "?a\\=b", EX_PREFIX,
				1,
				name.XSD_INT));
		e.getOther().add(pFactory.newOther(EX_NS, "123", EX_PREFIX,
				"mystring",
				name.XSD_STRING));
		e.getOther().add(pFactory.newOther(EX_NS, "123", EX_PREFIX,
				pFactory.newInternationalizedString("ma chaine", "fr"),
				name.PROV_LANG_STRING));
		makeDocAndTest(e, "target/entity101");

		System.out.println("*** test " + pFactory.newQualifiedName(EX2_NS, "À-ÖØ-öø-Ͱͽ",
				EX2_PREFIX).getUri());
	}


}
