package org.openprovenance.prov.core.jsonld11.test;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.test.Schemas;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.UncheckedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://github.com/openprov/prov-jsonld/issues/7: a qualified-name value under an extension property. The same
 * agent, {@code ex:arthur}, names its past project {@code ex:shed} in the two forms the issue compares: a node
 * reference {@code {"@id": "ex:shed"}}, which a JSON-LD processor reads as the IRI http://example.org/shed, and a
 * bare string {@code "ex:shed"}, which it reads as a plain literal.
 * <p>
 * As things stand only the bare string is PROV-JSONLD: it conforms to the schema, and the deserialiser reads it as
 * the qualified name ex:shed. The node reference is a syntax error, the schema admitting no {@code @id} in an
 * attribute value, and the deserialiser throws on it.
 */
public class ProvJsonld7Test extends TestCase {

    static final String EX = "http://example.org/";
    static final String FOAF = "http://xmlns.com/foaf/0.1/";
    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();

    static final String DIR = "src/test/resources/prov-jsonld-7/";

    Document read(String file) throws IOException {
        try (FileInputStream in = new FileInputStream(DIR + file)) {
            return new ProvDeserialiser().deserialiseDocument(in);
        }
    }

    /** The single foaf:pastProject attribute of the single agent of the document. */
    static Other pastProject(Document doc) {
        List<Agent> agents = doc.getStatementOrBundle().stream().filter(s -> s instanceof Agent).map(s -> (Agent) s).collect(Collectors.toList());
        assertEquals(1, agents.size());
        Agent arthur = agents.get(0);
        assertEquals(pf.newQualifiedName(EX, "arthur", "ex"), arthur.getId());
        List<Other> others = arthur.getOther();
        assertEquals(others.toString(), 1, others.size());
        Other o = others.get(0);
        assertEquals(pf.newQualifiedName(FOAF, "pastProject", "foaf"), o.getElementName());
        return o;
    }

    static void assertIsShed(Other o) {
        assertEquals(pf.newQualifiedName(EX, "shed", "ex"), o.getValue());
        assertEquals(pf.getName().PROV_QUALIFIED_NAME, o.getType());
    }

    public void testNodeReferenceThrows() throws IOException {
        try {
            read("node-reference.jsonld");
            fail("a node reference as an attribute value was read");
        } catch (UncheckedException e) {
            assertTrue(String.valueOf(e.getCause()), e.getCause() instanceof UnrecognizedPropertyException);
            assertEquals("@id", ((UnrecognizedPropertyException) e.getCause()).getPropertyName());
        }
    }

    public void testString() throws IOException {
        assertIsShed(pastProject(read("string.jsonld")));
    }

    public void testNodeReferenceViolatesSchema() throws IOException {
        List<String> violations = Schemas.violations(new File(DIR + "node-reference.jsonld"));
        assertTrue(String.join("\n", violations), violations.contains(
                "/@graph/1/foaf:pastProject/0: property '@id' is not defined in the schema and the schema does not allow additional properties"));
    }

    public void testStringConformsToSchema() throws IOException {
        assertEquals(List.of(), Schemas.violations(new File(DIR + "string.jsonld")));
    }
}
