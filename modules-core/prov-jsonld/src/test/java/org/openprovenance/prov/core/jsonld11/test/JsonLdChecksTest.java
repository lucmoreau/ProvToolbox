package org.openprovenance.prov.core.jsonld11.test;

import jakarta.json.JsonArray;
import junit.framework.TestCase;
import org.openprovenance.prov.core.test.JsonLdExpansion;
import org.openprovenance.prov.core.test.Schemas;

import java.io.File;
import java.util.List;

/** The two checks on PROV-JSONLD output, on the workflow the module writes in its own PC1 test. */
public class JsonLdChecksTest extends TestCase {

    public void testPc1ConformsToSchema() throws Exception {
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSONLD, new File("target/pc1-full.jsonld")));
    }

    public void testPc1Expands() throws Exception {
        JsonArray expanded = JsonLdExpansion.expand(new File("target/pc1-full.jsonld"));
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

    /**
     * The published context defines no {@code Bundle} term, so a bundle's type expands to nothing: the one known
     * gap. When the context gains {@code "Bundle": {"@id": "prov:Bundle"}}, this fails, and the allowance in
     * {@link JsonLdExpansion#KNOWN_CONTEXT_GAPS} goes.
     */
    public void testBundleTypeIsTheContextsKnownGap() throws Exception {
        JsonArray expanded = JsonLdExpansion.expand(new File("target/bundle1.jsonld"));
        assertEquals(List.of(), JsonLdExpansion.problems(expanded));
        assertEquals(List.of("type is not an IRI: Bundle", "type is not an IRI: Bundle"), JsonLdExpansion.allProblems(expanded));
    }
}
