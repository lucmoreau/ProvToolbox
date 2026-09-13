package org.openprovenance.prov.core.jsonld11.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.OpenprovTerms;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The openprov context (https://openprovenance.org/ns/openprov.jsonld) scopes bare terms to three relations:
 * inside an Attribution, a Membership or a Specialization, {@code association}, {@code entity}, ... name
 * openprov:had... properties. The deserialiser is no JSON-LD processor, so it must know those terms, as it
 * knows {@code label} and {@code type}; the serialiser must write them back the same way.
 */
public class OpenprovTermsTest extends TestCase {

    static final String OPENPROV = NamespacePrefixMapper.OPENPROV_NS;
    static final String EX = "http://example.org/";
    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    static final ObjectMapper mapper = new ObjectMapper();

    Document read(String file) throws IOException {
        try (FileInputStream in = new FileInputStream("src/test/resources/openprov/" + file)) {
            return new ProvDeserialiser().deserialiseDocument(in);
        }
    }

    /** The openprov attributes of a statement, by local name of the property, each with its single value. */
    static Map<String, Object> openprov(HasOther s) {
        Map<String, Object> m = new TreeMap<>();
        for (Other o : s.getOther()) {
            if (OPENPROV.equals(o.getElementName().getNamespaceURI())) {
                assertNull("one value for " + o.getElementName(), m.put(o.getElementName().getLocalPart(), o.getValue()));
            }
        }
        return m;
    }

    static QualifiedName ex(String local) {
        return pf.newQualifiedName(EX, local, "ex");
    }

    static <T extends Statement> T only(Document doc, Class<T> kind) {
        List<T> l = doc.getStatementOrBundle().stream().filter(kind::isInstance).map(kind::cast).collect(Collectors.toList());
        assertEquals(kind.getSimpleName(), 1, l.size());
        return l.get(0);
    }

    /** The node of the given JSON-LD type in the document as the serialiser writes it. */
    JsonNode written(Document doc, String type) throws IOException {
        Namespace.withThreadNamespace(doc.getNamespace());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ProvSerialiser().serialiseDocument(out, doc, true);
        JsonNode graph = mapper.readTree(out.toByteArray()).get("@graph");
        for (JsonNode n : graph) if (n.get("@type").asText().equals(type)) return n;
        throw new AssertionError("no " + type + " in " + graph);
    }

    Document reread(Document doc) throws IOException {
        Namespace.withThreadNamespace(doc.getNamespace());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ProvSerialiser().serialiseDocument(out, doc, true);
        return new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(out.toByteArray()));
    }

    public void testAttribution() throws IOException {
        Document doc = read("attribution.jsonld");
        WasAttributedTo att = only(doc, WasAttributedTo.class);
        assertEquals(ex("e1"), att.getEntity());
        assertEquals(ex("ag1"), att.getAgent());
        Map<String, Object> o = openprov(att);
        assertEquals(Set.of("hadActivity", "hadAssociation", "hadGeneration", "hadInvalidation", "hadEntityDerivation", "hadAgentDerivation"), o.keySet());
        assertEquals(ex("a1"), o.get("hadActivity"));
        assertEquals(ex("asc1"), o.get("hadAssociation"));
        assertEquals(ex("gen1"), o.get("hadGeneration"));
        assertEquals(ex("inv1"), o.get("hadInvalidation"));
        assertEquals(ex("der1"), o.get("hadEntityDerivation"));
        assertEquals(ex("der2"), o.get("hadAgentDerivation"));
        // an attribute outside the openprov namespace is untouched
        assertEquals(1, att.getOther().stream().filter(x -> EX.equals(x.getElementName().getNamespaceURI())).count());

        JsonNode json = written(doc, "Attribution");
        assertEquals("ex:asc1", json.get("association").get(0).asText());
        assertEquals("ex:der2", json.get("agentDerivation").get(0).asText());
        assertNull("no compact IRI for a scoped term", json.get("openprov:hadAssociation"));
        assertEquals(doc.getStatementOrBundle(), reread(doc).getStatementOrBundle());
    }

    public void testMembership() throws IOException {
        Document doc = read("membership.jsonld");
        QualifiedHadMember mem = only(doc, QualifiedHadMember.class);
        assertEquals(ex("coll1"), mem.getCollection());
        assertEquals(List.of(ex("item1")), mem.getEntity());
        Map<String, Object> o = openprov(mem);
        assertEquals(Set.of("hadActivity", "hadGeneration", "hadUsage", "hadCollectionGeneration", "hadItemGeneration"), o.keySet());
        assertEquals(ex("inserting"), o.get("hadActivity"));
        assertEquals(ex("gen0"), o.get("hadCollectionGeneration"));
        assertEquals(ex("gen1"), o.get("hadItemGeneration"));

        JsonNode json = written(doc, "Membership");
        assertEquals("ex:gen0", json.get("collectionGeneration").get(0).asText());
        assertEquals("ex:item1", json.get("entity").get(0).asText());
        assertEquals(doc.getStatementOrBundle(), reread(doc).getStatementOrBundle());
    }

    public void testSpecialization() throws IOException {
        Document doc = read("specialization.jsonld");
        QualifiedSpecializationOf spe = only(doc, QualifiedSpecializationOf.class);
        assertEquals(ex("e1"), spe.getSpecificEntity());
        assertEquals(ex("e"), spe.getGeneralEntity());
        Map<String, Object> o = openprov(spe);
        assertEquals(Set.of("hadPreviousEntity", "hadDerivation", "hadSpecialization"), o.keySet());
        assertEquals(ex("e0"), o.get("hadPreviousEntity"));
        assertEquals(ex("der1"), o.get("hadDerivation"));
        assertEquals(ex("spec0"), o.get("hadSpecialization"));

        JsonNode json = written(doc, "Specialization");
        assertEquals("ex:e0", json.get("previousEntity").get(0).asText());
        assertEquals("ex:spec0", json.get("specialization").get(0).asText());
        assertEquals(doc.getStatementOrBundle(), reread(doc).getStatementOrBundle());
    }

    /** Outside the three scopes the bare names mean what the specification says: activity on a Usage is prov:activity. */
    public void testTermsAreScoped() throws IOException {
        String json = "{\"@context\": [{\"ex\": \"http://example.org/\"}, \"https://openprovenance.org/ns/openprov.jsonld\"],"
                + " \"@graph\": [{\"@type\": \"Usage\", \"@id\": \"ex:u\", \"activity\": \"ex:a1\", \"entity\": \"ex:e1\"}]}";
        Document doc = new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json.getBytes()));
        Used u = only(doc, Used.class);
        assertEquals(ex("a1"), u.getActivity());
        assertTrue(u.getOther().isEmpty());
    }

    public void testCommunication() throws IOException {
        Document doc = read("communication.jsonld");
        WasInformedBy com = only(doc, WasInformedBy.class);
        assertEquals(ex("a1"), com.getInformed());
        assertEquals(ex("a0"), com.getInformant());
        Map<String, Object> o = openprov(com);
        assertEquals(Set.of("hadEntity", "hadGeneration", "hadUsage"), o.keySet());
        assertEquals(ex("e"), o.get("hadEntity"));

        JsonNode json = written(doc, "Communication");
        assertEquals("ex:e", json.get("entity").get(0).asText());
        assertEquals("ex:usd", json.get("usage").get(0).asText());
        assertNull(json.get("openprov:hadEntity"));
        assertEquals(doc.getStatementOrBundle(), reread(doc).getStatementOrBundle());
    }

    /**
     * The table the (de)serialisers carry is the openprov context, https://openprovenance.org/ns/openprov.jsonld:
     * for each scoped type, the same bare terms mapping to the same openprov:had... properties, and no other.
     * The copy under src/test/resources/openprov is the published file.
     */
    public void testTableIsTheOpenprovContext() throws IOException {
        JsonNode context = mapper.readTree(new File("src/main/resources/openprov-context/openprov.jsonld")).get("@context").get(1);
        Map<String, Kind> types = Map.of("Attribution", Kind.PROV_ATTRIBUTION, "Membership", Kind.PROV_MEMBERSHIP, "Specialization", Kind.PROV_SPECIALIZATION, "Communication", Kind.PROV_COMMUNICATION);
        for (Map.Entry<String, Kind> e : types.entrySet()) {
            Map<String, String> inContext = new TreeMap<>();
            context.get(e.getKey()).get("@context").fields().forEachRemaining(t -> {
                String id = t.getValue().path("@id").asText();
                if (id.startsWith("openprov:")) inContext.put(t.getKey(), id.substring("openprov:".length()));
            });
            assertEquals(e.getKey(), inContext, new TreeMap<>(OpenprovTerms.scope(e.getValue())));
        }
        assertTrue(OpenprovTerms.scope(Kind.PROV_USAGE).isEmpty());
    }
}
