package org.openprovenance.prov.core.jsonld11.test;

import jakarta.json.JsonArray;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.core.test.JsonLdExpansion;
import org.openprovenance.prov.core.test.Schemas;
import org.openprovenance.prov.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/** The two checks on PROV-JSONLD output — schema and expansion — on documents this test writes itself. */
public class JsonLdChecksTest extends TestCase {

    static final ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();
    static final String EX = "http://example.org/";

    static File write(Document doc, String file) throws IOException {
        Namespace.withThreadNamespace(doc.getNamespace());
        try (FileOutputStream out = new FileOutputStream(file)) {
            new ProvSerialiser().serialiseDocument(out, doc, true);
        }
        return new File(file);
    }

    static QualifiedName q(String local) {
        return pFactory.newQualifiedName(EX, local, "ex");
    }

    /** The Provenance Challenge 1 workflow, as the model's own test builds it. */
    static File pc1() throws IOException {
        Document doc = new org.openprovenance.prov.model.test.PC1FullTest().makePC1FullGraph(pFactory);
        return write(doc, "target/checks-pc1-full.jsonld");
    }

    /** A document of one bundle holding a usage. */
    static File bundle() throws IOException {
        Namespace ns = new Namespace();
        ns.addKnownNamespaces();
        ns.register("ex", EX);
        Bundle bundle = pFactory.newNamedBundle(q("bundle1"), ns,
                List.of(pFactory.newActivity(q("a1")), pFactory.newEntity(q("e1")), pFactory.newUsed(q("use1"), q("a1"), q("e1"))));
        Document doc = pFactory.newDocument(ns, List.of(pFactory.newEntity(q("bundle1"))), List.of(bundle));
        bundle.getNamespace().setParent(doc.getNamespace());
        return write(doc, "target/checks-bundle.jsonld");
    }

    public void testPc1ConformsToSchema() throws Exception {
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSONLD, pc1()));
    }

    public void testPc1Expands() throws Exception {
        JsonArray expanded = JsonLdExpansion.expand(pc1());
        assertTrue(expanded.size() > 10);
        assertEquals(List.of(), JsonLdExpansion.problems(expanded));
        String text = expanded.toString();
        // the context expands to the PROV-O vocabulary, relations in their qualified form
        assertTrue(text.contains("http://www.w3.org/ns/prov#Entity"));
        assertTrue(text.contains("http://www.w3.org/ns/prov#Activity"));
        assertTrue(text.contains("http://www.w3.org/ns/prov#Usage"));
        assertTrue(text.contains("http://www.w3.org/ns/prov#Generation"));
        assertTrue(text.contains("http://www.w3.org/ns/prov#qualifiedUsage"));
    }

    /** The violations of a document of one entity identified by the given qualified name. */
    static List<String> entityIdViolations(String id) throws IOException {
        String json = "{\"@context\": [{\"ex\": \"http://example.org/\"}, \"https://openprovenance.org/prov-jsonld/context.jsonld\"],"
                + " \"@graph\": [{\"@type\": \"Entity\", \"@id\": " + new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(id) + "}]}";
        return Schemas.violations(Schemas.PROV_JSONLD, Schemas.parse(json));
    }

    /**
     * The 2026-10-14 schema's qualified name: an optional prefix, then a local name with no colon, except one escaped
     * as PROV-N escapes it ({@code \:}); the 2024-08-25 schema took any string after the prefix.
     */
    public void testQualifiedNameLocalNameHasNoColon() throws IOException {
        for (String ok : List.of("ex:shed", "shed", "ex:101-generalEntity", "ex2:\\=\\'\\(\\)\\,-\\:\\;\\[\\]\\.")) {
            assertEquals(ok, List.of(), entityIdViolations(ok));
        }
        for (String bad : List.of("ex:a:b", "ex:a\\", "urn:uuid:1234")) {
            List<String> violations = entityIdViolations(bad);
            assertTrue(bad + ": " + violations, violations.stream().anyMatch(v -> v.startsWith("/@graph/0/@id: does not match the regex pattern")));
        }
    }

    /** The context, since 2026-09-12, defines {@code Bundle} as {@code prov:Bundle}: a bundle's type expands to it. */
    public void testBundleTypeExpandsToProvBundle() throws Exception {
        JsonArray expanded = JsonLdExpansion.expand(bundle());
        assertEquals(List.of(), JsonLdExpansion.problems(expanded));
        assertTrue(expanded.toString().contains("http://www.w3.org/ns/prov#Bundle"));
    }
}
