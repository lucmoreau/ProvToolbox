package org.openprovenance.prov.core.json.test;

import com.fasterxml.jackson.databind.JsonNode;
import junit.framework.TestCase;
import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PROV-JSON, like PROV-N and PROV-JSONLD, writes the openprov attributes of an attribution, a membership or a
 * specialization without had — openprov:association for the property openprov:hadAssociation — and reads them
 * back as the properties. The three serialisations agree on one document.
 */
public class OpenprovAttributesJsonTest extends TestCase {

    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    static final String OPENPROV = NamespacePrefixMapper.OPENPROV_NS;

    static QualifiedName ex(String local) {
        return pf.newQualifiedName("http://example.org/", local, "ex");
    }

    static Attribute openprovAttribute(String property, String value) {
        return pf.newOther(pf.newQualifiedName(OPENPROV, property, "openprov"), ex(value), pf.getName().PROV_QUALIFIED_NAME);
    }

    /** The document as the model holds it: the openprov properties, with had, on the three relations. */
    static Document document() {
        Namespace ns = new Namespace();
        ns.addKnownNamespaces();
        ns.register("ex", "http://example.org/");
        ns.register("openprov", OPENPROV);
        ns.register("provext", NamespacePrefixMapper.PROV_EXT_NS);
        List<Statement> statements = new ArrayList<>(List.of(pf.newEntity(ex("e1")), pf.newEntity(ex("e2")), pf.newEntity(ex("c")), pf.newAgent(ex("ag1")), pf.newActivity(ex("a1"))));
        statements.add(pf.newWasAttributedTo(ex("att"), ex("e1"), ex("ag1"),
                List.of(openprovAttribute("hadActivity", "a1"), openprovAttribute("hadAssociation", "asc1"), openprovAttribute("hadGeneration", "gen1"))));
        statements.add(pf.newQualifiedHadMember(ex("mem"), ex("c"), List.of(ex("e1")),
                List.of(openprovAttribute("hadActivity", "adding"), openprovAttribute("hadCollectionGeneration", "gen0"), openprovAttribute("hadItemGeneration", "gen1"))));
        statements.add(pf.newQualifiedSpecializationOf(ex("spe"), ex("e1"), ex("e2"),
                List.of(openprovAttribute("hadEntity", "e0"), openprovAttribute("hadDerivation", "der1"), openprovAttribute("hadSpecialization", "spe0"))));
        return pf.newDocument(ns, statements, List.of());
    }

    static Map<String, String> openprov(HasOther s) {
        Map<String, String> m = new TreeMap<>();
        for (Other o : s.getOther()) {
            if (OPENPROV.equals(o.getElementName().getNamespaceURI())) m.put(o.getElementName().getLocalPart(), ((QualifiedName) o.getValue()).getLocalPart());
        }
        return m;
    }

    static <T> T one(Document doc, Class<T> kind) {
        List<T> l = doc.getStatementOrBundle().stream().filter(kind::isInstance).map(kind::cast).collect(Collectors.toList());
        assertEquals(kind.getSimpleName(), 1, l.size());
        return l.get(0);
    }

    Document fromProvn() {
        return document();
    }

    JsonNode json(Document doc) {
        return new ProvSerialiser().toJson(doc);
    }

    public void testWrittenWithoutHad() {
        JsonNode json = json(fromProvn());
        JsonNode att = json.get("wasAttributedTo").get("ex:att");
        assertEquals("{\"$\":\"ex:asc1\",\"type\":\"prov:QUALIFIED_NAME\"}", att.get("openprov:association").toString());
        assertNull(att.get("openprov:hadAssociation"));
        assertEquals("ex:gen0", json.get("hadMember").get("ex:mem").get("openprov:collectionGeneration").get("$").asText());
        assertEquals("ex:e0", json.get("specializationOf").get("ex:spe").get("openprov:entity").get("$").asText());
        assertFalse(json.toString(), json.toString().contains("openprov:had"));
    }

    public void testReadAsProperties() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ProvSerialiser().serialiseDocument(out, fromProvn(), false);
        Document doc = new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(Map.of("hadActivity", "a1", "hadAssociation", "asc1", "hadGeneration", "gen1"), openprov(one(doc, WasAttributedTo.class)));
        assertEquals(Map.of("hadActivity", "adding", "hadCollectionGeneration", "gen0", "hadItemGeneration", "gen1"), openprov(one(doc, QualifiedHadMember.class)));
        assertEquals(Map.of("hadEntity", "e0", "hadDerivation", "der1", "hadSpecialization", "spe0"), openprov(one(doc, QualifiedSpecializationOf.class)));
        assertEquals(fromProvn().getStatementOrBundle(), doc.getStatementOrBundle());
    }

    /** The attribute of another statement keeps its written name: openprov:association on an entity is not a triangle's. */
    public void testUnscopedNameIsItself() {
        String json = "{\"prefix\": {\"ex\": \"http://example.org/\", \"openprov\": \"" + OPENPROV + "\"}, \"entity\": {\"ex:e\": {\"openprov:association\": {\"$\": \"ex:x\", \"type\": \"prov:QUALIFIED_NAME\"}}}}";
        Document doc = new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        assertEquals(Map.of("association", "x"), openprov(one(doc, Entity.class)));
    }

    /** PROV-JSON and PROV-JSONLD write the same model to documents that read back to the same statements. */
    public void testThreeSerialisationsAgree() throws Exception {
        Document fromProvn = fromProvn();
        ByteArrayOutputStream json = new ByteArrayOutputStream();
        new ProvSerialiser().serialiseDocument(json, fromProvn, false);
        Document fromJson = new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json.toByteArray()));
        Namespace.withThreadNamespace(fromProvn.getNamespace());
        ByteArrayOutputStream jsonld = new ByteArrayOutputStream();
        new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser().serialiseDocument(jsonld, fromProvn, false);
        Document fromJsonld = new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(jsonld.toByteArray()));
        assertEquals(fromProvn.getStatementOrBundle(), fromJson.getStatementOrBundle());
        assertEquals(fromProvn.getStatementOrBundle(), fromJsonld.getStatementOrBundle());
        JsonNode ld = new com.fasterxml.jackson.databind.ObjectMapper().readTree(jsonld.toByteArray());
        assertEquals("https://openprovenance.org/ns/openprov.jsonld", ld.get("@context").get(1).asText());
        for (JsonNode n : ld.get("@graph")) {
            if ("Attribution".equals(n.get("@type").asText())) assertEquals("ex:asc1", n.get("association").get(0).asText());
        }
    }
}
