package org.openprovenance.prov.notation.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.notation.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * PROV-N writes the openprov attributes of an attribution, a membership or a specialization without had, as it
 * writes prov:type or prov:role: openprov:association on a wasAttributedTo is the property
 * openprov:hadAssociation, which the model holds, and which PROV-N writes back as openprov:association.
 * The same name on any other statement is an attribute like any other.
 */
public class OpenprovAttributesTest extends TestCase {

    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    static final String OPENPROV = NamespacePrefixMapper.OPENPROV_NS;

    static final String PROVN = "document\n"
            + "prefix ex <http://example.org/>\n"
            + "prefix openprov <" + OPENPROV + ">\n"
            + "prefix provext <" + NamespacePrefixMapper.PROV_EXT_NS + ">\n"
            + "entity(ex:e1, [openprov:association = 'ex:not-a-triangle'])\n"
            + "entity(ex:e2)\nentity(ex:c)\nagent(ex:ag1)\nactivity(ex:a1)\n"
            + "wasAttributedTo(ex:att; ex:e1, ex:ag1, [openprov:activity = 'ex:a1', openprov:association = 'ex:asc1', openprov:generation = 'ex:gen1', ex:note = \"kept\"])\n"
            + "provext:hadMember(ex:mem; ex:c, ex:e1, [openprov:activity = 'ex:adding', openprov:collectionGeneration = 'ex:gen0', openprov:itemGeneration = 'ex:gen1'])\n"
            + "provext:specializationOf(ex:spe; ex:e1, ex:e2, [openprov:previousEntity = 'ex:e0', openprov:derivation = 'ex:der1', openprov:specialization = 'ex:spe0'])\n"
            + "activity(ex:a0)\n"
            + "wasInformedBy(ex:com; ex:a1, ex:a0, [openprov:entity = 'ex:e1', openprov:generation = 'ex:gen1', openprov:usage = 'ex:usd1'])\n"
            + "endDocument\n";

    Document parse(String provn) {
        return new Utility().readDocument(new ByteArrayInputStream(provn.getBytes(StandardCharsets.UTF_8)), pf);
    }

    String write(Document doc) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new Utility().writeDocument(doc, out, pf);
        return out.toString(StandardCharsets.UTF_8);
    }

    /** The openprov attributes of a statement by local name, each with its value's local name. */
    static Map<String, String> openprov(HasOther s) {
        Map<String, String> m = new TreeMap<>();
        for (Other o : s.getOther()) {
            if (OPENPROV.equals(o.getElementName().getNamespaceURI())) {
                assertNull(m.put(o.getElementName().getLocalPart(), ((QualifiedName) o.getValue()).getLocalPart()));
            }
        }
        return m;
    }

    <T> T one(Document doc, Class<T> kind) {
        List<T> l = doc.getStatementOrBundle().stream().filter(kind::isInstance).map(kind::cast).collect(Collectors.toList());
        assertEquals(kind.getSimpleName(), 1, l.size());
        return l.get(0);
    }

    public void testReadAsProperties() {
        Document doc = parse(PROVN);
        assertEquals(Map.of("hadActivity", "a1", "hadAssociation", "asc1", "hadGeneration", "gen1"), openprov(one(doc, WasAttributedTo.class)));
        assertEquals(Map.of("hadActivity", "adding", "hadCollectionGeneration", "gen0", "hadItemGeneration", "gen1"), openprov(one(doc, QualifiedHadMember.class)));
        assertEquals(Map.of("hadPreviousEntity", "e0", "hadDerivation", "der1", "hadSpecialization", "spe0"), openprov(one(doc, QualifiedSpecializationOf.class)));
        assertEquals(Map.of("hadEntity", "e1", "hadGeneration", "gen1", "hadUsage", "usd1"), openprov(one(doc, WasInformedBy.class)));
        // an ordinary attribute beside them is untouched
        WasAttributedTo att = one(doc, WasAttributedTo.class);
        assertEquals(1, att.getOther().stream().filter(o -> "note".equals(o.getElementName().getLocalPart())).count());
    }

    public void testOnAnotherStatementTheNameIsItself() {
        Document doc = parse(PROVN);
        Entity e1 = doc.getStatementOrBundle().stream().filter(s -> s instanceof Entity).map(s -> (Entity) s).findFirst().orElseThrow();
        assertEquals(Map.of("association", "not-a-triangle"), openprov(e1));
    }

    public void testWrittenWithoutHadAndReadBackEqual() {
        Document doc = parse(PROVN);
        String written = write(doc);
        assertTrue(written, written.contains("openprov:association = 'ex:asc1'"));
        assertTrue(written, written.contains("openprov:collectionGeneration = 'ex:gen0'"));
        assertTrue(written, written.contains("openprov:previousEntity = 'ex:e0'"));
        assertTrue(written, written.contains("openprov:entity = 'ex:e1'"));
        assertFalse(written, written.contains("openprov:had"));
        Document again = parse(written);
        assertEquals(doc.getStatementOrBundle(), again.getStatementOrBundle());
    }

    /** What earlier documents wrote, provext:association under the old namespace, is an attribute in that namespace still. */
    public void testLegacyProvextAttributeIsLeftAlone() {
        String provn = "document\nprefix ex <http://example.org/>\nprefix provext <" + NamespacePrefixMapper.LEGACY_PROV_EXT_NS + ">\n"
                + "entity(ex:e1)\nagent(ex:ag1)\n"
                + "wasAttributedTo(ex:att; ex:e1, ex:ag1, [provext:association = 'ex:asc1'])\nendDocument\n";
        WasAttributedTo att = one(parse(provn), WasAttributedTo.class);
        Other o = att.getOther().get(0);
        assertEquals(NamespacePrefixMapper.LEGACY_PROV_EXT_NS, o.getElementName().getNamespaceURI());
        assertEquals("association", o.getElementName().getLocalPart());
    }
}
