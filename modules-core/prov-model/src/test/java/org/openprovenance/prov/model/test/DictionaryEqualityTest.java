package org.openprovenance.prov.model.test;

import java.util.List;

import junit.framework.TestCase;
import org.openprovenance.prov.model.*;

/** Dictionary statements, and their entries and keys, are equal when their contents are, as other statements are. */
public class DictionaryEqualityTest extends TestCase {

    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    static final Name name = pf.getName();

    static QualifiedName q(String local) {
        return pf.newQualifiedName("http://example.org/", local, "ex");
    }

    static Entry entry(String key, String entity) {
        return pf.newEntry(pf.newKey(key, name.XSD_STRING), q(entity));
    }

    static List<Attribute> attributes(String label, String type) {
        return List.of(pf.newAttribute(name.PROV_LABEL, pf.newInternationalizedString(label), name.PROV_LANG_STRING),
                       pf.newAttribute(name.PROV_TYPE, q(type), name.PROV_QUALIFIED_NAME));
    }

    static void assertEqualObjects(Object o1, Object o2) {
        assertNotSame(o1, o2);
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    public void testEntry() {
        assertEqualObjects(entry("a", "e1"), entry("a", "e1"));
        assertFalse(entry("a", "e1").equals(entry("b", "e1")));
        assertFalse(entry("a", "e1").equals(entry("a", "e2")));
        assertFalse(entry("1", "e1").equals(pf.newEntry(pf.newKey(1, name.XSD_INT), q("e1"))));
    }

    public void testDerivedByInsertionFrom() {
        DerivedByInsertionFrom d1 = pf.newDerivedByInsertionFrom(q("id"), q("d2"), q("d1"), List.of(entry("a", "e1"), entry("b", "e2")), attributes("l", "t"));
        DerivedByInsertionFrom d2 = pf.newDerivedByInsertionFrom(q("id"), q("d2"), q("d1"), List.of(entry("a", "e1"), entry("b", "e2")), attributes("l", "t"));
        assertEqualObjects(d1, d2);

        assertFalse(d1.equals(pf.newDerivedByInsertionFrom(null, q("d2"), q("d1"), List.of(entry("a", "e1"), entry("b", "e2")), attributes("l", "t"))));
        assertFalse(d1.equals(pf.newDerivedByInsertionFrom(q("id"), q("d3"), q("d1"), List.of(entry("a", "e1"), entry("b", "e2")), attributes("l", "t"))));
        assertFalse(d1.equals(pf.newDerivedByInsertionFrom(q("id"), q("d2"), q("d0"), List.of(entry("a", "e1"), entry("b", "e2")), attributes("l", "t"))));
        assertFalse(d1.equals(pf.newDerivedByInsertionFrom(q("id"), q("d2"), q("d1"), List.of(entry("a", "e1")), attributes("l", "t"))));
        assertFalse(d1.equals(pf.newDerivedByInsertionFrom(q("id"), q("d2"), q("d1"), List.of(entry("a", "e1"), entry("b", "e2")), attributes("l2", "t"))));
        assertFalse(d1.equals(pf.newDerivedByInsertionFrom(q("id"), q("d2"), q("d1"), List.of(entry("a", "e1"), entry("b", "e2")), attributes("l", "t2"))));
    }

    public void testDerivedByRemovalFrom() {
        List<Key> keys = List.of(pf.newKey("a", name.XSD_STRING), pf.newKey(1, name.XSD_INT));
        DerivedByRemovalFrom d1 = pf.newDerivedByRemovalFrom(q("id"), q("d2"), q("d1"), keys, attributes("l", "t"));
        DerivedByRemovalFrom d2 = pf.newDerivedByRemovalFrom(q("id"), q("d2"), q("d1"),
                                                             List.of(pf.newKey("a", name.XSD_STRING), pf.newKey(1, name.XSD_INT)), attributes("l", "t"));
        assertEqualObjects(d1, d2);

        assertFalse(d1.equals(pf.newDerivedByRemovalFrom(q("id"), q("d2"), q("d1"), List.of(pf.newKey("a", name.XSD_STRING)), attributes("l", "t"))));
        assertFalse(d1.equals(pf.newDerivedByRemovalFrom(q("id"), q("d2"), q("d0"), keys, attributes("l", "t"))));
        assertFalse(d1.equals(pf.newDerivedByRemovalFrom(q("id"), q("d2"), q("d1"), keys, List.of())));
    }

    public void testDictionaryMembership() {
        // DictionaryMembership compared its entries, which were compared by identity
        assertEqualObjects(pf.newDictionaryMembership(q("d"), List.of(entry("a", "e1"))),
                           pf.newDictionaryMembership(q("d"), List.of(entry("a", "e1"))));
        assertFalse(pf.newDictionaryMembership(q("d"), List.of(entry("a", "e1")))
                      .equals(pf.newDictionaryMembership(q("d"), List.of(entry("a", "e2")))));
    }
}
