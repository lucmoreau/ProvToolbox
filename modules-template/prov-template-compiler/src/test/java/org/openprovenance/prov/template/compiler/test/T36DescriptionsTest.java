package org.openprovenance.prov.template.compiler.test;

import org.openprovenance.prov.template.compiler.CompilerJsonSchema;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * T36 stage 5: what the compiler writes to describe subtypes: a list under each template in the documentation, the schema of each
 * subtype's typed bean in the JSON schema, and (when the catalogue names a file and a namespace) the Turtle file (R7).
 */
public class T36DescriptionsTest extends T36Fixture {

    static final String RESOURCES = "t36/t36_cli/src/main/resources/";
    static final String BIG_FIXES_AMP = "{\"approver_name\":\"A&B <Admin>\"}";

    String big() {
        return subtype("approval_big", BIG_FIXES_AMP, "{\"priceBefore\":\"required\",\"unitCode\":\"optional\",\"payload\":\"optional\"}");
    }

    String small() {
        return subtype("approval_small", "{\"approver_name\":\"Other\",\"level\":1}", "{\"lineCount\":\"required\"}");
    }

    static String read(String id, String relative) throws Exception {
        return Files.readString(Path.of(OUT + id + "/libs/" + relative));
    }

    // ---- the documentation ----------------------------------------------------------------------------------------------

    public void testTheDocumentationListsTheSubtypesUnderTheirTemplate() throws Exception {
        generate("plain", false, List.of(), List.of());
        generate("doc", false, List.of(), List.of(big(), small()));
        String plain = read("plain", DOC), doc = read("doc", DOC);
        assertFalse("a template without subtypes has none listed", plain.contains("csv_subtype"));

        int subtypes = doc.indexOf("<div class='csv_subtypes'>");
        assertTrue(doc, subtypes > doc.indexOf("<h2>t31.Approval</h2>"));
        assertTrue("after the template's variables", subtypes > doc.indexOf("<span class='csv_field'>customer_hash</span>"));
        String list = doc.substring(subtypes);
        assertTrue(list, list.contains("<span class='csv_subtype'>t31.approval_big</span>") && list.contains("<span class='csv_subtype'>t31.approval_small</span>"));
        assertTrue("typed bean named", list.contains("typed bean Approval_bigInputs"));
        assertTrue("what is fixed, html escaped: " + list, list.contains("<span class='csv_fixed'>approver_name = A&amp;B &lt;Admin&gt;</span>"));
        assertTrue("two fixed values", list.contains("<span class='csv_fixed'>level = 1</span>"));
        assertTrue("what is carried, with its key, carrier, type and mode: " + list,
                list.contains("<span class='csv_carried'>priceBefore in customer_hash</span> <span class='csv_type'>number</span> <span class='csv_input'>required</span>")
                        && list.contains("<span class='csv_carried'>unit in customer_hash</span> <span class='csv_type'>string</span> <span class='csv_input'>optional</span>")
                        && list.contains("<span class='csv_carried'>lineCount in customer_hash</span> <span class='csv_type'>integer</span> <span class='csv_input'>required</span>"));
        // taking the subtypes out leaves the page without them
        int end = doc.indexOf("</ul>\n</div>", subtypes) + "</ul>\n".length();
        assertEquals("only the subtypes were added", plain, doc.substring(0, subtypes) + doc.substring(end));
    }

    // ---- the JSON schema ------------------------------------------------------------------------------------------------

    public void testTheJsonSchemaDescribesTheTypedBeanOfASubtype() throws Exception {
        generate("sch", false, List.of(), List.of(big(), small()));
        JsonNode schema = om.readTree(read("sch", SCHEMA));
        JsonNode big = schema.get("definitions").get("t31.approval_big");
        assertNotNull(big);
        Set<String> props = big.get("properties").propertyNames().stream().collect(Collectors.toSet());
        assertEquals(Set.of("isA", "approver", "approver_ref", "order", "order_note", "level", "priceBefore", "unitCode", "payload", "customer_hashOther"), props);
        assertFalse("the fixed value is not a property", props.contains("approver_name"));
        assertFalse("nor is the carrier: its content is", props.contains("customer_hash"));
        assertEquals("number", big.get("properties").get("priceBefore").get("type").asText());
        assertEquals("integer", big.get("properties").get("level").get("type").asText());
        assertEquals("a json value is JSON text", "string", big.get("properties").get("payload").get("type").asText());
        assertTrue(big.get("properties").get("payload").get("description").asText().contains("JSON text"));
        assertTrue("the key of its own is said", big.get("properties").get("unitCode").get("description").asText().contains("the key \"unit\" of customer_hash, optional"));
        JsonNode other = big.get("properties").get("customer_hashOther");
        assertEquals("object", other.get("type").asText());
        assertTrue("its members are free", other.get("additionalProperties").asBoolean());
        assertFalse("no other property", big.get("additionalProperties").asBoolean());
        Set<String> required = big.get("required").valueStream().map(JsonNode::asText).collect(Collectors.toSet());
        assertTrue(required.toString(), required.containsAll(Set.of("isA", "priceBefore", "approver", "approver_ref", "order")));
        assertFalse("an optional value is not required", required.contains("unitCode") || required.contains("payload"));
        assertTrue("the general template is as it was: its carrier is one string",
                "string".equals(schema.get("definitions").get("t31.Approval").get("properties").get("customer_hash").get("type").asText()));

        // the schema is valid, and a record of the subtype is checked against it through its isA, as a template's is
        // The catalogue's root schema constrains isA to the pattern of the last template written (an existing property of the schema,
        // T40: it cannot be satisfied by a record of any other template, a subtype's included), so a record is checked against its definition.
        ObjectNode wrapper = (ObjectNode) om.readTree("{\"$ref\":\"#/definitions/t31.approval_big\"}");
        wrapper.set("definitions", schema.get("definitions"));
        com.networknt.schema.Schema compiled = new CompilerJsonSchema(pf).setupJsonSchemaFromFile(writeWrapper(wrapper));
        String base = "\"isA\":\"t31.approval_big\",\"approver\":\"ex:a\",\"approver_ref\":\"r\",\"order\":\"ex:o\",\"order_note\":\"n\",\"level\":3";
        assertEquals("valid", List.of(), errors(compiled, "{" + base + ",\"priceBefore\":1.5,\"unitCode\":\"kg\",\"customer_hashOther\":{\"zzz\":[1,{\"q\":null}]}}"));
        assertFalse("a required value is missing", errors(compiled, "{" + base + "}").isEmpty());
        assertFalse("a string for a number", errors(compiled, "{" + base + ",\"priceBefore\":\"cheap\"}").isEmpty());
        assertFalse("a fixed value is not a property", errors(compiled, "{" + base + ",\"priceBefore\":1,\"approver_name\":\"Admin\"}").isEmpty());
        assertFalse("the carrier's own string is not a property", errors(compiled, "{" + base + ",\"priceBefore\":1,\"customer_hash\":\"{}\"}").isEmpty());
        assertFalse("other keys are an object", errors(compiled, "{" + base + ",\"priceBefore\":1,\"customer_hashOther\":3}").isEmpty());
    }

    String writeWrapper(ObjectNode wrapper) throws Exception {
        String path = OUT + "sch/wrapper.json";
        om.writerWithDefaultPrettyPrinter().writeValue(new File(path), wrapper);
        return path;
    }

    List<String> errors(com.networknt.schema.Schema schema, String instance) throws Exception {
        return schema.validate(om.readTree(instance)).stream().map(Object::toString).collect(Collectors.toList());
    }

    // ---- the Turtle -----------------------------------------------------------------------------------------------------

    static final String NS = "http://example.org/t36#";

    void withTurtle(ObjectNode c) {
        c.put("turtle", "catalogue.ttl");
        c.put("turtleNamespace", NS);
    }

    public void testTheTurtleSaysWhatTheCompilerKnows() throws Exception {
        generate("ttl", false, List.of(), List.of(big(), small()), this::withTurtle);
        String ttl = read("ttl", RESOURCES + "catalogue.ttl");
        assertTrue(ttl, ttl.contains(":approval a owl:Class") && ttl.contains(":approval_big a owl:Class") && ttl.contains("rdfs:subClassOf :approval ;"));
        assertTrue(ttl, ttl.contains("tpl:fixes [ tpl:variable \"approver_name\" ; tpl:value \"A&B <Admin>\" ]"));
        assertTrue(ttl, ttl.contains("tpl:requires :approval-priceBefore ;") && ttl.contains("tpl:mayCarry :approval-unitCode ;"));
        assertTrue(ttl, ttl.contains("rdfs:range rdf:JSON"));
        // read with an RDF parser, when rdflib is installed
        String[] r = run(List.of("python3", "-I", "src/test/resources/t36/check_turtle.py", OUT + "ttl/libs/" + RESOURCES + "catalogue.ttl", NS), new File("."));
        if (r == null || r[1].contains("No module named")) {
            System.out.println("[T36] python3 or rdflib is not installed: the Turtle file is not parsed");
            return;
        }
        assertFalse("a wrong answer: " + r[1], r[1].contains("FAIL "));
        assertEquals(r[1], "0", r[0]);
    }

    public void testNoTurtleUnlessTheCatalogueNamesOne() throws Exception {
        generate("nottl", false, List.of(), List.of(big()));
        assertFalse(new File(OUT + "nottl/libs/" + RESOURCES + "catalogue.ttl").exists());
    }

    public void testATurtleFileNeedsANamespace() throws Exception {
        try {
            generate("nons", false, List.of(), List.of(big()), c -> c.put("turtle", "catalogue.ttl"));
            fail("generated");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("T36: ") && e.getMessage().contains("turtleNamespace"));
        }
    }

    public void testATemplateWithInJsonValuesAndNoSubtypeIsDescribedToo() throws Exception {
        generate("ttl2", false, List.of(), List.of(), this::withTurtle);
        String ttl = read("ttl2", RESOURCES + "catalogue.ttl");
        assertTrue(ttl, ttl.contains(":approval-priceBefore a owl:DatatypeProperty") && !ttl.contains("rdfs:subClassOf"));
    }
}
