package org.openprovenance.prov.scala.jsonld11;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.scala.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.scala.jsonld11.serialization.ProvSerialiser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://github.com/openprov/prov-jsonld/issues/7, as the Scala model reads and writes PROV-JSONLD: a node reference
 * {@code {"@id": "ex:shed"}} is the qualified name ex:shed, a bare string {@code "ex:shed"} under a key the context
 * does not coerce to {@code @id} is a string, and a qualified name is written back as a node reference. The two
 * documents are prov-jsonld's.
 */
public class ProvJsonld7Test extends TestCase {

    static final String EX = "http://example.org/";
    static final String FOAF = "http://xmlns.com/foaf/0.1/";
    static final ProvFactory pf = ProvFactory.pf();
    static final ObjectMapper mapper = new ObjectMapper();

    static Document read(String file) throws IOException {
        try (FileInputStream in = new FileInputStream("src/test/resources/prov-jsonld-7/" + file)) {
            return new ProvDeserialiser().deserialiseDocument(in);
        }
    }

    static byte[] write(Document doc) {
        Namespace.withThreadNamespace(doc.getNamespace());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ProvSerialiser().serialiseDocument(out, doc, true);
        return out.toByteArray();
    }

    /** The single foaf:pastProject attribute of the single agent of the document. */
    static Other pastProject(Document doc) {
        List<Agent> agents = doc.getStatementOrBundle().stream().filter(s -> s instanceof Agent).map(s -> (Agent) s).collect(Collectors.toList());
        assertEquals(1, agents.size());
        assertEquals(pf.newQualifiedName(EX, "arthur", "ex"), agents.get(0).getId());
        List<Other> others = agents.get(0).getOther();
        assertEquals(1, others.size());
        Other o = others.get(0);
        assertEquals(pf.newQualifiedName(FOAF, "pastProject", "foaf"), o.getElementName());
        return o;
    }

    /** The agent's foaf:pastProject as written. */
    static JsonNode writtenPastProject(byte[] json) throws IOException {
        for (JsonNode n : mapper.readTree(json).get("@graph")) {
            if (n.path("@id").asText().equals("ex:arthur")) return n.get("foaf:pastProject");
        }
        throw new AssertionError("no ex:arthur in " + new String(json));
    }

    public void testNodeReference() throws IOException {
        Document doc = read("node-reference.jsonld");
        Other o = pastProject(doc);
        assertEquals(pf.newQualifiedName(EX, "shed", "ex"), o.getValue());
        assertEquals(pf.getName().PROV_QUALIFIED_NAME, o.getType());

        byte[] json = write(doc);
        assertEquals("[{\"@id\":\"ex:shed\"}]", writtenPastProject(json).toString());
        assertEquals(o, pastProject(new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json))));
    }

    public void testString() throws IOException {
        Document doc = read("string.jsonld");
        Other o = pastProject(doc);
        assertEquals(pf.newInternationalizedString("ex:shed", null), o.getValue());
        assertEquals(pf.getName().XSD_STRING, o.getType());

        byte[] json = write(doc);
        assertEquals(o, pastProject(new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json))));
    }
}
