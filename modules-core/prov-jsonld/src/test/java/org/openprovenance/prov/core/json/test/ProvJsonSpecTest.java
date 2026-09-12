package org.openprovenance.prov.core.json.test;

import com.fasterxml.jackson.databind.JsonNode;
import junit.framework.TestCase;
import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvJsonException;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.core.test.Schemas;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PROV-JSON as the W3C submission writes it — every relation with its property names, literals in all their
 * forms, blank identifiers, a bundle — read into the model and checked statement by statement; then what the
 * serialiser writes, checked against the submission's schema and for the canonical form of each literal.
 */
public class ProvJsonSpecTest extends TestCase {

    static final String EXAMPLES = "src/test/resources/prov-json-spec-examples.json";
    static final String EX = "http://example.org/";
    static final String EXX = "http://example.org/ex#";
    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    static final Name name = pf.getName();

    Document doc;

    @Override
    protected void setUp() throws IOException {
        doc = new ProvDeserialiser().deserialiseDocument(new File(EXAMPLES));
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              READING
    ///
    //////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    <T extends StatementOrBundle> List<T> all(Class<T> kind) {
        return doc.getStatementOrBundle().stream().filter(kind::isInstance).map(s -> (T) s).collect(Collectors.toList());
    }

    <T extends Statement> T one(Class<T> kind, String localId) {
        for (T s : all(kind)) {
            if (s instanceof Identifiable && ((Identifiable) s).getId() != null && localId.equals(((Identifiable) s).getId().getLocalPart())) return s;
        }
        throw new AssertionError("no " + kind.getSimpleName() + " " + localId);
    }

    <T extends Statement> T onlyBlank(Class<T> kind) {
        List<T> blanks = all(kind).stream().filter(s -> !(s instanceof Identifiable) || ((Identifiable) s).getId() == null).collect(Collectors.toList());
        assertEquals(kind.getSimpleName() + " without identifier", 1, blanks.size());
        return blanks.get(0);
    }

    static Map<String, List<Other>> others(HasOther s) {
        return s.getOther().stream().collect(Collectors.groupingBy(o -> o.getElementName().getLocalPart()));
    }

    static Other only(Map<String, List<Other>> others, String local) {
        List<Other> l = others.get(local);
        assertNotNull(local, l);
        assertEquals(local, 1, l.size());
        return l.get(0);
    }

    static QualifiedName qn(String ns, String local) {
        return pf.newQualifiedName(ns, local, null);
    }

    /** The instant a time in the file denotes, as the model reads it: it prints with milliseconds and a zone. */
    static javax.xml.datatype.XMLGregorianCalendar at(String time) {
        return pf.newISOTime(time, DateTimeOption.PRESERVE, null);
    }

    public void testPrefixes() {
        assertEquals(EX, doc.getNamespace().getDefaultNamespace());
        assertEquals(EXX, doc.getNamespace().getPrefixes().get("ex"));
        assertEquals("http://xmlns.com/foaf/0.1/", doc.getNamespace().getPrefixes().get("foaf"));
        // implicitly declared
        assertEquals(NamespacePrefixMapper.PROV_NS, doc.getNamespace().getPrefixes().get("prov"));
        assertEquals(NamespacePrefixMapper.XSD_NS, doc.getNamespace().getPrefixes().get("xsd"));
    }

    public void testStatementCounts() {
        assertEquals(4, all(Entity.class).size());
        assertEquals(2, all(Activity.class).size());
        assertEquals(2, all(Agent.class).size());
        assertEquals(1, all(WasGeneratedBy.class).size());
        assertEquals(1, all(Used.class).size());
        assertEquals(1, all(WasInformedBy.class).size());
        assertEquals(1, all(WasStartedBy.class).size());
        assertEquals(1, all(WasEndedBy.class).size());
        assertEquals(1, all(WasInvalidatedBy.class).size());
        assertEquals(1, all(WasDerivedFrom.class).size());
        assertEquals(1, all(WasAttributedTo.class).size());
        assertEquals(1, all(WasAssociatedWith.class).size());
        assertEquals(1, all(ActedOnBehalfOf.class).size());
        assertEquals(1, all(WasInfluencedBy.class).size());
        assertEquals(2, all(SpecializationOf.class).size());
        assertEquals(1, all(AlternateOf.class).size());
        assertEquals(2, all(HadMember.class).size());
        assertEquals(1, all(Bundle.class).size());
    }

    public void testEntityLiterals() {
        Entity e1 = one(Entity.class, "e1");
        assertEquals(qn(EX, "e1"), e1.getId());
        assertEquals(List.of("an entity"), e1.getLabel().stream().map(LangString::getValue).collect(Collectors.toList()));
        assertEquals(1, e1.getType().size());
        assertEquals(name.PROV_QUALIFIED_NAME, e1.getType().get(0).getType());
        assertEquals(name.PROV_COLLECTION, e1.getType().get(0).getValue());
        assertEquals(qn(EXX, "v"), e1.getValue().getValue());
        assertEquals("London", ((LangString) e1.getLocation().get(0).getValue()).getValue());

        Map<String, List<Other>> o = others(e1);
        // native JSON literals
        assertLiteral(only(o, "count"), "3", name.XSD_INT);
        assertLiteral(only(o, "ratio"), "0.5", name.XSD_DECIMAL);
        assertLiteral(only(o, "flag"), "true", name.XSD_BOOLEAN);
        // typed and language-tagged objects
        assertLiteral(only(o, "size"), "1034", name.XSD_POSITIVE_INTEGER);
        Other desc = only(o, "desc");
        assertEquals(name.PROV_LANG_STRING, desc.getType());
        assertEquals("une entité", ((LangString) desc.getValue()).getValue());
        assertEquals("fr", ((LangString) desc.getValue()).getLang());
        // an array is several values of one attribute
        List<Other> values = o.get("values");
        assertEquals(4, values.size());
        assertLiteral(values.get(0), "1034", name.XSD_POSITIVE_INTEGER);
        assertLiteral(values.get(1), "2", name.XSD_INT);
        assertLiteral(values.get(2), "82.5", name.XSD_DECIMAL);
        assertLiteral(values.get(3), "text", name.XSD_STRING);
    }

    static void assertLiteral(Other o, String value, QualifiedName type) {
        assertEquals(type, o.getType());
        Object v = o.getValue();
        assertEquals(value, v instanceof LangString ? ((LangString) v).getValue() : String.valueOf(v));
    }

    public void testActivityTimes() {
        Activity a1 = one(Activity.class, "a1");
        assertEquals(at("2011-11-16T16:05:00"), a1.getStartTime());
        assertEquals(at("2011-11-16T16:06:00"), a1.getEndTime());
        assertEquals(qn(EXX, "edit"), a1.getType().get(0).getValue());
        assertLiteral(only(others(a1), "host"), "server.example.org", name.XSD_STRING);
        Activity a2 = one(Activity.class, "a2");
        assertNull(a2.getStartTime());
        assertNull(a2.getEndTime());
    }

    public void testAgent() {
        Agent ag1 = one(Agent.class, "ag1");
        assertEquals(name.PROV_PERSON, ag1.getType().get(0).getValue());
        assertLiteral(only(others(ag1), "name"), "Alice", name.XSD_STRING);
        assertEquals("http://xmlns.com/foaf/0.1/", ag1.getOther().get(0).getElementName().getNamespaceURI());
    }

    public void testGenerationUsageInvalidation() {
        WasGeneratedBy gen = one(WasGeneratedBy.class, "gen1");
        assertEquals(qn(EX, "e1"), gen.getEntity());
        assertEquals(qn(EX, "a1"), gen.getActivity());
        assertEquals(at("2011-11-16T16:06:00"), gen.getTime());
        assertEquals("output", ((LangString) gen.getRole().get(0).getValue()).getValue());

        Used use = one(Used.class, "use1");
        assertEquals(qn(EX, "a1"), use.getActivity());
        assertEquals(qn(EX, "e2"), use.getEntity());
        assertEquals(at("2011-11-16T16:05:00"), use.getTime());

        WasInvalidatedBy inv = onlyBlank(WasInvalidatedBy.class);
        assertEquals(qn(EX, "e2"), inv.getEntity());
        assertEquals(qn(EX, "a2"), inv.getActivity());
        assertNotNull(inv.getTime());
    }

    public void testStartEndCommunication() {
        WasStartedBy start = one(WasStartedBy.class, "start1");
        assertEquals(qn(EX, "a2"), start.getActivity());
        assertEquals(qn(EX, "e2"), start.getTrigger());
        assertEquals(qn(EX, "a1"), start.getStarter());
        assertNotNull(start.getTime());

        WasEndedBy end = one(WasEndedBy.class, "end1");
        assertEquals(qn(EX, "a2"), end.getActivity());
        assertEquals(qn(EX, "e1"), end.getTrigger());
        assertEquals(qn(EX, "a1"), end.getEnder());

        WasInformedBy inf = onlyBlank(WasInformedBy.class);
        assertEquals(qn(EX, "a2"), inf.getInformed());
        assertEquals(qn(EX, "a1"), inf.getInformant());
    }

    public void testDerivation() {
        WasDerivedFrom der = one(WasDerivedFrom.class, "der1");
        assertEquals(qn(EX, "e1"), der.getGeneratedEntity());
        assertEquals(qn(EX, "e2"), der.getUsedEntity());
        assertEquals(qn(EX, "a1"), der.getActivity());
        assertEquals(qn(EXX, "gen1"), der.getGeneration());
        assertEquals(qn(EXX, "use1"), der.getUsage());
        assertEquals(name.PROV_REVISION, der.getType().get(0).getValue());
    }

    public void testAgentRelations() {
        WasAttributedTo att = onlyBlank(WasAttributedTo.class);
        assertEquals(qn(EX, "e1"), att.getEntity());
        assertEquals(qn(EX, "ag1"), att.getAgent());
        // a plain string is an xsd:string, prov:type included
        assertEquals("authorship", ((LangString) att.getType().get(0).getValue()).getValue());

        WasAssociatedWith assoc = onlyBlank(WasAssociatedWith.class);
        assertEquals(qn(EX, "a1"), assoc.getActivity());
        assertEquals(qn(EX, "ag1"), assoc.getAgent());
        assertEquals(qn(EX, "plan"), assoc.getPlan());
        assertEquals("editor", ((LangString) assoc.getRole().get(0).getValue()).getValue());

        ActedOnBehalfOf del = onlyBlank(ActedOnBehalfOf.class);
        assertEquals(qn(EX, "ag1"), del.getDelegate());
        assertEquals(qn(EX, "ag2"), del.getResponsible());
        assertEquals(qn(EX, "a1"), del.getActivity());

        WasInfluencedBy infl = onlyBlank(WasInfluencedBy.class);
        assertEquals(qn(EX, "e1"), infl.getInfluencee());
        assertEquals(qn(EX, "ag2"), infl.getInfluencer());
    }

    public void testSpecializationAlternateMembership() {
        List<SpecializationOf> specs = all(SpecializationOf.class);
        SpecializationOf plain = specs.stream().filter(s -> !(s instanceof QualifiedSpecializationOf) || ((QualifiedSpecializationOf) s).getId() == null).findFirst().orElseThrow();
        assertEquals(qn(EX, "e1"), plain.getSpecificEntity());
        assertEquals(qn(EX, "e2"), plain.getGeneralEntity());
        // an identifier makes it the qualified extension
        QualifiedSpecializationOf qualified = (QualifiedSpecializationOf) one(SpecializationOf.class, "spe2");
        assertEquals(qn(EXX, "spe2"), qualified.getId());
        assertEquals(qn(EX, "c"), qualified.getSpecificEntity());

        AlternateOf alt = all(AlternateOf.class).get(0);
        assertEquals(qn(EX, "e1"), alt.getAlternate1());
        assertEquals(qn(EX, "e2"), alt.getAlternate2());

        List<HadMember> members = all(HadMember.class);
        assertEquals(List.of(qn(EX, "e1")), members.get(0).getEntity());
        assertEquals(List.of(qn(EX, "e1"), qn(EX, "e2")), members.get(1).getEntity());
        assertEquals(qn(EX, "c"), members.get(1).getCollection());
        assertFalse(members.get(0) instanceof QualifiedHadMember);
    }

    public void testBundle() {
        Bundle b1 = all(Bundle.class).get(0);
        assertEquals(qn(EXX, "b1"), b1.getId());
        assertEquals("http://example.org/bundle#", b1.getNamespace().getPrefixes().get("b"));
        assertSame(doc.getNamespace(), b1.getNamespace().getParent());
        assertEquals(2, b1.getStatement().size());
        Entity e3 = (Entity) b1.getStatement().get(0);
        assertEquals(qn("http://example.org/bundle#", "e3"), e3.getId());
        assertEquals("fr", e3.getLabel().get(0).getLang());
        WasGeneratedBy g3 = (WasGeneratedBy) b1.getStatement().get(1);
        assertNull(g3.getId());
        // the document's default namespace reaches into the bundle
        assertEquals(qn(EX, "a1"), g3.getActivity());
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              WRITING
    ///
    //////////////////////////////////////////////////////////////////////

    JsonNode written() throws IOException {
        return new ProvSerialiser().toJson(doc);
    }

    public void testWrittenConformsToSchema() throws IOException {
        JsonNode json = written();
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSON, json));
        // the sections as the file had them
        assertEquals(fieldNames(Schemas.read(EXAMPLES)), fieldNames(json));
    }

    static List<String> fieldNames(JsonNode n) {
        List<String> names = new java.util.ArrayList<>();
        n.fieldNames().forEachRemaining(names::add);
        return names;
    }

    public void testWrittenLiterals() throws IOException {
        JsonNode e1 = written().get("entity").get("e1");
        assertEquals("\"an entity\"", e1.get("prov:label").toString());
        assertEquals("{\"$\":\"prov:Collection\",\"type\":\"prov:QUALIFIED_NAME\"}", e1.get("prov:type").toString());
        assertEquals("{\"$\":\"3\",\"type\":\"xsd:int\"}", e1.get("ex:count").toString());
        assertEquals("{\"$\":\"0.5\",\"type\":\"xsd:decimal\"}", e1.get("ex:ratio").toString());
        assertEquals("{\"$\":\"true\",\"type\":\"xsd:boolean\"}", e1.get("ex:flag").toString());
        assertEquals("{\"$\":\"une entité\",\"lang\":\"fr\"}", e1.get("ex:desc").toString());
        assertEquals("\"London\"", e1.get("prov:location").toString());
        assertTrue(e1.get("ex:values").isArray());
        assertEquals(4, e1.get("ex:values").size());
        assertEquals("\"text\"", e1.get("ex:values").get(3).toString());
    }

    public void testWrittenRelations() throws IOException {
        JsonNode json = written();
        JsonNode gen = json.get("wasGeneratedBy").get("ex:gen1");
        assertEquals(List.of("prov:entity", "prov:activity", "prov:time", "prov:role"), fieldNames(gen));
        assertEquals(at("2011-11-16T16:06:00").toXMLFormat(), gen.get("prov:time").asText());
        JsonNode start = json.get("wasStartedBy").get("ex:start1");
        assertEquals(List.of("prov:activity", "prov:trigger", "prov:starter", "prov:time"), fieldNames(start));
        JsonNode der = json.get("wasDerivedFrom").get("ex:der1");
        assertEquals(List.of("prov:generatedEntity", "prov:usedEntity", "prov:activity", "prov:generation", "prov:usage", "prov:type"), fieldNames(der));
        assertEquals("ex:gen1", der.get("prov:generation").asText());
        JsonNode assoc = json.get("wasAssociatedWith").elements().next();
        assertEquals("plan", assoc.get("prov:plan").asText());
        // a blank identifier for a relation without one
        assertTrue(json.get("wasAttributedTo").fieldNames().next().startsWith("_:"));
        // the qualified specialization is a specializationOf under its identifier
        assertEquals("c", json.get("specializationOf").get("ex:spe2").get("prov:specificEntity").asText());
        assertNull(json.get("qualifiedSpecializationOf"));
        // one member is a string, several an array
        List<JsonNode> members = new java.util.ArrayList<>();
        json.get("hadMember").elements().forEachRemaining(members::add);
        assertTrue(members.get(0).get("prov:entity").isTextual());
        assertEquals(2, members.get(1).get("prov:entity").size());
    }

    public void testWrittenBundle() throws IOException {
        JsonNode b1 = written().get("bundle").get("ex:b1");
        assertEquals("http://example.org/bundle#", b1.get("prefix").get("b").asText());
        assertNull(b1.get("@id"));
        assertEquals("{\"$\":\"dans le lot\",\"lang\":\"fr\"}", b1.get("entity").get("b:e3").get("prov:label").toString());
    }

    public void testWrittenReadsBack() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ProvSerialiser().serialiseDocument(out, doc, false);
        Document again = new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(out.toByteArray()));
        // the writer groups statements by kind, so the order differs from the file's: the same statements, and the same JSON again
        assertEquals(doc.getStatementOrBundle().size(), again.getStatementOrBundle().size());
        assertEquals(new java.util.HashSet<>(doc.getStatementOrBundle()), new java.util.HashSet<>(again.getStatementOrBundle()));
        // blank identifiers are numbered in statement order and mean nothing
        assertEquals(written().toString().replaceAll("_:n\\d+", "_:n"), new ProvSerialiser().toJson(again).toString().replaceAll("_:n\\d+", "_:n"));
    }

    /**
     * Attributes on a specialization, alternate or membership are ProvToolbox's extension of PROV-DM: PROV-JSON
     * carries them as it carries any relation's attributes, though the submission's schema knows none there.
     */
    public void testExtensionAttributesRoundTrip() throws IOException {
        String json = "{\"prefix\": {\"ex\": \"http://example.org/\"}, \"specializationOf\": {\"ex:spe\": {\"prov:specificEntity\": \"ex:e1\", \"prov:generalEntity\": \"ex:e2\", \"ex:note\": \"identified and described\"}},"
                + " \"hadMember\": {\"_:m\": {\"prov:collection\": \"ex:c\", \"prov:entity\": [\"ex:e1\"], \"prov:type\": \"ex:Part\"}}}";
        Document d = new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        QualifiedSpecializationOf spe = (QualifiedSpecializationOf) d.getStatementOrBundle().get(0);
        assertLiteral(only(others(spe), "note"), "identified and described", name.XSD_STRING);
        QualifiedHadMember mem = (QualifiedHadMember) d.getStatementOrBundle().get(1);
        assertNull(mem.getId());
        assertEquals("ex:Part", ((LangString) mem.getType().get(0).getValue()).getValue());
        JsonNode out = new ProvSerialiser().toJson(d);
        assertEquals("identified and described", out.get("specializationOf").get("ex:spe").get("ex:note").asText());
        assertEquals("ex:Part", out.get("hadMember").elements().next().get("prov:type").asText());
        assertEquals(d.getStatementOrBundle(), new ProvDeserialiser().deserialiseDocument(out).getStatementOrBundle());
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              REJECTIONS
    ///
    //////////////////////////////////////////////////////////////////////

    static ProvJsonException rejects(String json) {
        try {
            new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
        } catch (ProvJsonException e) {
            return e;
        }
        throw new AssertionError("accepted: " + json);
    }

    public void testRejectsUnknownSection() {
        assertTrue(rejects("{\"prefix\": {\"ex\": \"http://example.org/\"}, \"wasGeneratedby\": {}}").getMessage().contains("wasGeneratedby"));
    }

    public void testRejectsUndeclaredPrefix() {
        assertTrue(rejects("{\"entity\": {\"ex:e1\": {}}}").getMessage().contains("ex:e1"));
    }

    public void testRejectsLiteralWithoutValue() {
        assertTrue(rejects("{\"prefix\": {\"ex\": \"http://example.org/\"}, \"entity\": {\"ex:e1\": {\"ex:a\": {\"type\": \"xsd:int\"}}}}").getMessage().contains("$"));
    }

    public void testRejectsNestedBundle() {
        assertTrue(rejects("{\"prefix\": {\"ex\": \"http://example.org/\"}, \"bundle\": {\"ex:b\": {\"bundle\": {\"ex:c\": {}}}}}").getMessage().contains("bundle"));
    }

    public void testRejectsRecordThatIsNotAnObject() {
        assertTrue(rejects("{\"prefix\": {\"ex\": \"http://example.org/\"}, \"entity\": {\"ex:e1\": \"oops\"}}").getMessage().contains("ex:e1"));
    }

    public void testRejectsBadTime() {
        assertTrue(rejects("{\"prefix\": {\"ex\": \"http://example.org/\"}, \"activity\": {\"ex:a\": {\"prov:startTime\": \"yesterday\"}}}").getMessage().contains("prov:startTime"));
    }
}
