package org.openprovenance.prov.notation.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.notation.Utility;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * provext is the PROV-JSONLD extension's namespace, https://openprovenance.org/ns/provext#; ProvToolbox used to
 * declare it as http://openprovenance.org/prov/extension#. A qualified relation is recognised under either, so
 * every document written before the change still reads; what is written declares the current one.
 */
public class ProvExtNamespaceTest extends TestCase {

    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();

    Document parse(String provextIri) {
        String provn = "document\n"
                + "prefix ex <http://example.org/>\n"
                + "prefix provext <" + provextIri + ">\n"
                + "entity(ex:e1)\nentity(ex:e2)\nentity(ex:c)\n"
                + "provext:specializationOf(ex:spe; ex:e1, ex:e2, [ex:note = \"qualified\"])\n"
                + "provext:hadMember(ex:mem; ex:c, ex:e1)\n"
                + "endDocument\n";
        return new Utility().readDocument(new ByteArrayInputStream(provn.getBytes(StandardCharsets.UTF_8)), pf);
    }

    void assertQualified(Document doc) {
        QualifiedSpecializationOf spe = (QualifiedSpecializationOf) doc.getStatementOrBundle().get(3);
        assertEquals("spe", spe.getId().getLocalPart());
        assertEquals("e1", spe.getSpecificEntity().getLocalPart());
        assertEquals(1, spe.getOther().size());
        QualifiedHadMember mem = (QualifiedHadMember) doc.getStatementOrBundle().get(4);
        assertEquals("mem", mem.getId().getLocalPart());
        assertEquals("c", mem.getCollection().getLocalPart());
    }

    public void testCurrentNamespace() {
        assertEquals("https://openprovenance.org/ns/provext#", NamespacePrefixMapper.PROV_EXT_NS);
        assertQualified(parse(NamespacePrefixMapper.PROV_EXT_NS));
    }

    public void testLegacyNamespaceStillReads() {
        assertEquals("http://openprovenance.org/prov/extension#", NamespacePrefixMapper.LEGACY_PROV_EXT_NS);
        assertQualified(parse(NamespacePrefixMapper.LEGACY_PROV_EXT_NS));
    }

    /** An extension statement in any other namespace is not the qualified relation: the parser leaves it out, as it always has. */
    public void testAnotherNamespaceIsNotProvext() {
        Document doc = parse("http://example.org/notprovext#");
        assertEquals(3, doc.getStatementOrBundle().size());
        assertTrue(doc.getStatementOrBundle().stream().allMatch(s -> s instanceof Entity));
    }

    public void testWhatIsWrittenDeclaresTheCurrentNamespace() {
        Document doc = parse(NamespacePrefixMapper.LEGACY_PROV_EXT_NS);
        QualifiedName minted = pf.getName().newProvExtQualifiedName("Specialization");
        assertEquals(NamespacePrefixMapper.PROV_EXT_NS, minted.getNamespaceURI());
        assertEquals("provext", minted.getPrefix());
        // the parsed document keeps the declaration it came with: accepted on input, not rewritten
        assertEquals(NamespacePrefixMapper.LEGACY_PROV_EXT_NS, doc.getNamespace().getPrefixes().get("provext"));
    }
}
