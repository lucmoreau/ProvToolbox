package org.openprovenance.prov.notation;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.UncheckedTestException;

public class InternationalizationTest extends
        org.openprovenance.prov.model.InternationalizationTest {
    final Utility u = new Utility();

    public InternationalizationTest(String name) {
        super(name);
    }

    public String extension() {
        return ".provn";
    }

    public boolean checkTest(String name) {
        return true;
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