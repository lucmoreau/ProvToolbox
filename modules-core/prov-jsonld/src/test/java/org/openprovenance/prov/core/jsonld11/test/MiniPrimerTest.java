package org.openprovenance.prov.core.jsonld11.test;

import jakarta.json.JsonArray;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.core.test.JsonLdExpansion;
import org.openprovenance.prov.core.test.Schemas;
import org.openprovenance.prov.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

/** The specification's short primer, as PROV-JSONLD 2024-08-25 has it: schema-valid, expands, reads into the model, and round-trips. */
public class MiniPrimerTest extends TestCase {

    static final String PRIMER = "src/test/resources/mini-primer.jsonld";

    public void testPrimerConformsToSchemaAndExpands() throws Exception {
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSONLD, new File(PRIMER)));
        JsonArray expanded = JsonLdExpansion.expand(new File(PRIMER));
        assertEquals(List.of(), JsonLdExpansion.problems(expanded));
        assertTrue(expanded.toString().contains("http://www.w3.org/ns/prov#Derivation"));
    }

    public void testPrimerReadsAndRoundTrips() throws Exception {
        Document doc;
        try (FileInputStream in = new FileInputStream(PRIMER)) {
            doc = new ProvDeserialiser().deserialiseDocument(in);
        }
        assertEquals(8, doc.getStatementOrBundle().size());
        List<Class<?>> kinds = doc.getStatementOrBundle().stream().map(s -> s.getClass().getInterfaces()[0]).collect(Collectors.toList());
        assertEquals(List.of(Entity.class, Entity.class, WasDerivedFrom.class, Agent.class, WasAssociatedWith.class, Activity.class, Used.class, WasGeneratedBy.class), kinds);
        Agent derek = (Agent) doc.getStatementOrBundle().get(3);
        assertEquals("derek", derek.getId().getLocalPart());
        assertEquals(doc.getNamespace().stringToQualifiedName("prov:Person", org.openprovenance.prov.vanilla.ProvFactory.getFactory()), derek.getType().get(0).getValue());
        Entity article = (Entity) doc.getStatementOrBundle().get(1);
        assertEquals("EN", ((LangString) article.getOther().get(0).getValue()).getLang());

        String out = "target/mini-primer.roundtrip.jsonld";
        Namespace.withThreadNamespace(doc.getNamespace());
        try (FileOutputStream os = new FileOutputStream(out)) {
            new ProvSerialiser().serialiseDocument(os, doc, true);
        }
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSONLD, new File(out)));
        try (FileInputStream in = new FileInputStream(out)) {
            assertEquals(doc, new ProvDeserialiser().deserialiseDocument(in));
        }
    }
}
