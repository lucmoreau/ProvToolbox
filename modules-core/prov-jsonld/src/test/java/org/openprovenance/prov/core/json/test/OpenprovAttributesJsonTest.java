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
                List.of(openprovAttribute("hadPreviousEntity", "e0"), openprovAttribute("hadDerivation", "der1"), openprovAttribute("hadSpecialization", "spe0"))));
        statements.add(pf.newWasInformedBy(ex("com"), ex("a1"), ex("a0"),
                List.of(openprovAttribute("hadEntity", "e1"), openprovAttribute("hadGeneration", "gen1"), openprovAttribute("hadUsage", "usd1"))));
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
        assertEquals("ex:e0", json.get("specializationOf").get("ex:spe").get("openprov:previousEntity").get("$").asText());
        assertEquals("ex:e1", json.get("wasInformedBy").get("ex:com").get("openprov:entity").get("$").asText());
        assertFalse(json.toString(), json.toString().contains("openprov:had"));
    }

    public void testReadAsProperties() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ProvSerialiser().serialiseDocument(out, fromProvn(), false);
        Document doc = new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(Map.of("hadActivity", "a1", "hadAssociation", "asc1", "hadGeneration", "gen1"), openprov(one(doc, WasAttributedTo.class)));
        assertEquals(Map.of("hadActivity", "adding", "hadCollectionGeneration", "gen0", "hadItemGeneration", "gen1"), openprov(one(doc, QualifiedHadMember.class)));
        assertEquals(Map.of("hadPreviousEntity", "e0", "hadDerivation", "der1", "hadSpecialization", "spe0"), openprov(one(doc, QualifiedSpecializationOf.class)));
        assertEquals(Map.of("hadEntity", "e1", "hadGeneration", "gen1", "hadUsage", "usd1"), openprov(one(doc, WasInformedBy.class)));
        assertEquals(fromProvn().getStatementOrBundle(), doc.getStatementOrBundle());
    }

    /** The attribute of another statement keeps its written name: openprov:association on an entity is not a triangle's. */
    public void testUnscopedNameIsItself() {
        String json = "{\"prefix\": {\"ex\": \"http://example.org/\", \"openprov\": \"" + OPENPROV + "\"}, \"entity\": {\"ex:e\": {\"openprov:association\": {\"$\": \"ex:x\", \"type\": \"prov:QUALIFIED_NAME\"}}}}";
        Document doc = new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        assertEquals(Map.of("association", "x"), openprov(one(doc, Entity.class)));
    }

    /**
     * The document as a template expansion, or any code that names the attributes as a document does, leaves it:
     * the terms, without had, on the three relations.
     */
    static Document documentInTermForm() {
        Namespace ns = document().getNamespace();
        List<Statement> statements = new ArrayList<>(List.of(pf.newEntity(ex("e1")), pf.newEntity(ex("e2")), pf.newEntity(ex("c")), pf.newAgent(ex("ag1")), pf.newActivity(ex("a1"))));
        statements.add(pf.newWasAttributedTo(ex("att"), ex("e1"), ex("ag1"),
                List.of(openprovAttribute("activity", "a1"), openprovAttribute("association", "asc1"), openprovAttribute("generation", "gen1"))));
        statements.add(pf.newQualifiedHadMember(ex("mem"), ex("c"), List.of(ex("e1")),
                List.of(openprovAttribute("activity", "adding"), openprovAttribute("collectionGeneration", "gen0"), openprovAttribute("itemGeneration", "gen1"))));
        statements.add(pf.newQualifiedSpecializationOf(ex("spe"), ex("e1"), ex("e2"),
                List.of(openprovAttribute("previousEntity", "e0"), openprovAttribute("derivation", "der1"), openprovAttribute("specialization", "spe0"))));
        statements.add(pf.newWasInformedBy(ex("com"), ex("a1"), ex("a0"),
                List.of(openprovAttribute("entity", "e1"), openprovAttribute("generation", "gen1"), openprovAttribute("usage", "usd1"))));
        return pf.newDocument(ns, statements, List.of());
    }

    /**
     * The serialisers are the counterpart of the deserialisers: as those read a term into its property, these write a
     * term as they write the property, so a model holding the term form serialises exactly like one holding the
     * properties, and reads back to the properties.
     */
    public void testTermFormSerialisesAsTheProperties() throws Exception {
        Document terms = documentInTermForm();
        Document properties = fromProvn();
        Namespace.withThreadNamespace(properties.getNamespace());
        assertEquals(json(properties).toString(), json(terms).toString());
        ByteArrayOutputStream fromTerms = new ByteArrayOutputStream(), fromProperties = new ByteArrayOutputStream();
        new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser().serialiseDocument(fromTerms, terms, false);
        new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser().serialiseDocument(fromProperties, properties, false);
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        assertEquals(mapper.readTree(fromProperties.toByteArray()), mapper.readTree(fromTerms.toByteArray()));
        assertFalse(fromTerms.toString(), fromTerms.toString().contains("openprov:previousEntity") || fromTerms.toString().contains("openprov:entity"));
        Document back = new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(fromTerms.toByteArray()));
        assertEquals(properties.getStatementOrBundle(), back.getStatementOrBundle());
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
            if ("Communication".equals(n.get("@type").asText())) assertEquals("ex:e1", n.get("entity").get(0).asText());
        }
    }
}
