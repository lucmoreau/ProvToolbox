package org.openprovenance.prov.notation.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.notation.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * PROV-N writes a qualified specialization, membership or alternate as a provext statement,
 * {@code provext:specializationOf(id; ...)}. A document built in memory, by a template for instance, need not
 * declare provext; the writer declares it then, or what it writes cannot be read back.
 */
public class ProvExtDeclarationTest extends TestCase {

    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    static final String EX = "http://example.org/";
    static final String DECLARATION = "prefix provext <" + NamespacePrefixMapper.PROV_EXT_NS + ">";

    QualifiedName ex(String local) {
        return pf.newQualifiedName(EX, local, "ex");
    }

    Document document(Namespace ns, boolean withOpenprov) {
        List<Attribute> attrs = withOpenprov
                ? List.of(pf.newOther(NamespacePrefixMapper.OPENPROV_NS, "hadPreviousEntity", NamespacePrefixMapper.OPENPROV_PREFIX, ex("e0"), pf.getName().PROV_QUALIFIED_NAME))
                : List.of();
        Document doc = pf.newDocument();
        doc.setNamespace(ns);
        doc.getStatementOrBundle().add(pf.newEntity(ex("e1")));
        doc.getStatementOrBundle().add(pf.newEntity(ex("e2")));
        doc.getStatementOrBundle().add(pf.newQualifiedSpecializationOf(ex("spe"), ex("e1"), ex("e2"), attrs));
        return doc;
    }

    Namespace namespace() {
        Namespace ns = new Namespace();
        ns.addKnownNamespaces();
        ns.register("ex", EX);
        return ns;
    }

    String write(Document doc) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new Utility().writeDocument(doc, out, pf);
        return out.toString(StandardCharsets.UTF_8);
    }

    Document read(String provn) {
        return new Utility().readDocument(new ByteArrayInputStream(provn.getBytes(StandardCharsets.UTF_8)), pf);
    }

    public void testUndeclaredProvextIsDeclaredWhenAProvextStatementIsWritten() {
        String provn = write(document(namespace(), false));
        assertTrue(provn, provn.contains(DECLARATION));
        assertTrue(provn, provn.contains("provext:specializationOf(ex:spe;"));
        Document back = read(provn);
        assertEquals(1, back.getStatementOrBundle().stream().filter(s -> s instanceof QualifiedSpecializationOf).count());
    }

    public void testOpenprovIsDeclaredTooWhenItsAttributesAreWritten() {
        String provn = write(document(namespace(), true));
        assertTrue(provn, provn.contains(DECLARATION));
        assertTrue(provn, provn.contains("prefix openprov <" + NamespacePrefixMapper.OPENPROV_NS + ">"));
        assertTrue(provn, provn.contains("openprov:previousEntity = 'ex:e0'"));
        Document back = read(provn);
        QualifiedSpecializationOf spe = (QualifiedSpecializationOf) back.getStatementOrBundle().stream().filter(s -> s instanceof QualifiedSpecializationOf).findFirst().orElseThrow();
        assertEquals("hadPreviousEntity", spe.getOther().get(0).getElementName().getLocalPart());
    }

    public void testADeclaredProvextIsLeftAlone() {
        Namespace ns = namespace();
        ns.register("provext", NamespacePrefixMapper.LEGACY_PROV_EXT_NS);
        String provn = write(document(ns, false));
        assertFalse(provn, provn.contains(DECLARATION));
        assertTrue(provn, provn.contains("prefix provext <" + NamespacePrefixMapper.LEGACY_PROV_EXT_NS + ">"));
        assertEquals(1, provn.split("prefix provext ", -1).length - 1);
        read(provn);
    }

    public void testNoProvextStatementNoDeclaration() {
        Document doc = pf.newDocument();
        doc.setNamespace(namespace());
        doc.getStatementOrBundle().add(pf.newEntity(ex("e1")));
        doc.getStatementOrBundle().add(pf.newEntity(ex("e2")));
        doc.getStatementOrBundle().add(pf.newSpecializationOf(ex("e1"), ex("e2")));
        String provn = write(doc);
        assertFalse(provn, provn.contains("provext"));
    }

    public void testAProvextStatementInABundleIsDeclaredAtDocumentLevel() {
        Namespace bundleNs = new Namespace();
        bundleNs.register("ex", EX);
        Document inner = document(bundleNs, false);
        Bundle bundle = pf.newNamedBundle(ex("b"), bundleNs, List.of());
        for (StatementOrBundle s : inner.getStatementOrBundle()) bundle.getStatement().add((Statement) s);
        Document doc = pf.newDocument();
        doc.setNamespace(namespace());
        doc.getStatementOrBundle().add(bundle);
        String provn = write(doc);
        assertTrue(provn, provn.contains(DECLARATION));
        assertEquals(provn, 1, provn.split("prefix provext ", -1).length - 1);
        read(provn);
    }
}
