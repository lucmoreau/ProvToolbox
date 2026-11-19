package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * T37: the bindings JSON schema of {@code prov-template} (what the plugin's {@code validate-bindings} hands to ajv) names every member the
 * compiler's descriptors read. It named seven of the twenty-one: 195 of the 222 bindings schemas of the first application, the
 * provenance-templates library, nextgen and ProvToolbox failed it, and a member added to a descriptor was never added to it.
 */
public class T37BindingsSchemaTest extends TestCase {

    static final String SCHEMA = "../prov-template/src/main/resources/jsonschema/bindings-schema.json";
    static final String DESCRIPTORS = "src/main/java/org/openprovenance/prov/template/descriptors/";
    static final String VALIDATOR = "src/test/resources/t36/validate_schema.js";

    final ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();

    /** The members a descriptor class reads: the names of its {@code @JsonProperty} annotations. */
    static Set<String> members(String file) throws Exception {
        Set<String> res = new TreeSet<>();
        Matcher m = Pattern.compile("@JsonProperty\\(\"(@[^\"]+)\"\\)").matcher(Files.readString(Path.of(DESCRIPTORS + file)));
        while (m.find()) res.add(m.group(1));
        return res;
    }

    public void testTheSchemaNamesEveryMemberTheDescriptorsRead() throws Exception {
        JsonNode schema = om.readTree(Files.readString(Path.of(SCHEMA)));
        Set<String> id = new TreeSet<>(), value = new TreeSet<>();
        schema.get("definitions").get("An@id").get("properties").propertyNames().forEach(id::add);
        schema.get("definitions").get("An@value").get("properties").propertyNames().forEach(value::add);

        Set<String> name = members("NameDescriptor.java");
        // read by the node's descriptor only to refuse them with a message that says what is wrong (@inJson, @performative are for a value, not a node)
        name.removeAll(Set.of("@inJson", "@performative"));
        Set<String> missingId = new TreeSet<>(name);
        missingId.removeAll(id);
        assertEquals("members NameDescriptor reads that the schema does not give a node (An@id)", Set.of(), missingId);

        Set<String> attribute = members("AttributeDescriptor.java");
        Set<String> missingValue = new TreeSet<>(attribute);
        missingValue.removeAll(value);
        assertEquals("members AttributeDescriptor reads that the schema does not give a value (An@value)", Set.of(), missingValue);
    }

    /** The top-level members of a bindings schema ({@code @documentation}, {@code @return}, ...) are in the schema too. */
    public void testTheSchemaNamesTheTopLevelMembersTheBindingsSchemaClassReads() throws Exception {
        JsonNode schema = om.readTree(Files.readString(Path.of(SCHEMA)));
        Set<String> top = new TreeSet<>();
        schema.get("properties").propertyNames().forEach(top::add);
        Set<String> read = members("TemplateBindingsSchema.java");
        read.removeAll(top);
        assertEquals("top-level members TemplateBindingsSchema reads that the schema does not name", Set.of(), read);
    }

    /** Runs the schema against files; the result says valid or invalid for each. Null when node or ajv is not there. */
    String validate(List<String> files) throws Exception {
        List<String> command = new ArrayList<>(List.of("node", VALIDATOR, SCHEMA));
        command.addAll(files);
        String[] r = T36Fixture.run(command, new File("."));
        if (r == null || r[1].startsWith("NO-AJV")) {
            System.out.println("[T37] node or ajv is not installed: the bindings schema is not run against bindings");
            return null;
        }
        return r[1];
    }

    Path bindings(String id, String variable) throws Exception {
        Path dir = Path.of("target/t37");
        Files.createDirectories(dir);
        Path p = dir.resolve(id + ".json");
        Files.writeString(p, "{\"var\":{\"x\":[[" + variable + "]]},\"context\":{\"ex\":\"http://example.org/\"}}");
        return p;
    }

    public void testBindingsThatUseTheMembersAreValidAndBadOnesAreRefused() throws Exception {
        Map<String, String> valid = new LinkedHashMap<>();
        valid.put("value-all", "{\"@value\":\"*\",\"@type\":\"xsd:double\",\"@documentation\":\"d\",\"@examplar\":1.5,\"@input\":\"optional\",\"@output\":\"optional\","
                + "\"@sql.type\":\"nullableTEXT\",\"@escape\":true,\"@unique\":true,\"@transient\":\"pt:drop\",\"@pseudonym\":\"pt:hash\","
                + "\"@sql.foreign\":{\"type\":\"t\",\"attribute\":\"a\",\"item\":\"i\"},\"@inJson\":\"attributes\",\"@inJson.key\":\"k\",\"@performative\":\"request\"}");
        valid.put("value-boolean-example", "{\"@value\":\"*\",\"@type\":\"xsd:boolean\",\"@examplar\":true}");
        valid.put("value-long-date-json", "{\"@value\":\"*\",\"@type\":\"xsd:long\"}");
        valid.put("value-date", "{\"@value\":\"*\",\"@type\":\"xsd:date\"}");
        valid.put("value-json", "{\"@value\":\"*\",\"@type\":\"json\"}");
        valid.put("node-all", "{\"@id\":\"ex:*\",\"@type\":\"xsd:int\",\"@documentation\":\"d\",\"@examplar\":\"e\",\"@input\":\"false\",\"@output\":\"true\","
                + "\"@sql.table\":\"t\",\"@sql.type\":\"SERIAL\",\"@escape\":\"true\",\"@role\":\"effect2\",\"@semantic.type\":\"true\","
                + "\"@sql.new.inputs\":{\"a\":\"TEXT\"},\"@sql.also.outputs\":{\"b\":\"TEXT\"},\"@unique\":true,\"@transient\":true,\"@pseudonym\":\"pt:hash\"}");
        Map<String, String> invalid = new LinkedHashMap<>();
        invalid.put("unknown-member", "{\"@value\":\"*\",\"@nonsense\":1}");
        invalid.put("bad-role", "{\"@id\":\"ex:*\",\"@role\":\"middle\"}");
        invalid.put("role-on-a-value", "{\"@value\":\"*\",\"@role\":\"effect\"}");
        invalid.put("injson-on-a-node", "{\"@id\":\"ex:*\",\"@inJson\":\"attributes\"}");
        invalid.put("performative-on-a-node", "{\"@id\":\"ex:*\",\"@performative\":\"request\"}");
        invalid.put("bad-performative", "{\"@value\":\"*\",\"@performative\":\"both\"}");
        invalid.put("unique-as-text", "{\"@value\":\"*\",\"@unique\":\"yes\"}");
        invalid.put("foreign-with-an-unknown-member", "{\"@value\":\"*\",\"@sql.foreign\":{\"table\":\"t\"}}");

        List<String> files = new ArrayList<>();
        for (Map.Entry<String, String> e : valid.entrySet()) files.add(bindings("valid-" + e.getKey(), e.getValue()).toString());
        for (Map.Entry<String, String> e : invalid.entrySet()) files.add(bindings("invalid-" + e.getKey(), e.getValue()).toString());
        String out = validate(files);
        if (out == null) return;
        for (String k : valid.keySet()) assertTrue("valid " + k + ": " + out, out.contains("target/t37/valid-" + k + ".json valid"));
        for (String k : invalid.keySet()) assertTrue("invalid " + k + ": " + out, out.contains("target/t37/invalid-" + k + ".json invalid"));
    }

    public void testATopLevelReturnIsATextAndNothingElseIs() throws Exception {
        Path dir = Path.of("target/t37");
        Files.createDirectories(dir);
        String vars = "\"var\":{\"x\":[[{\"@value\":\"*\"}]]},\"context\":{\"ex\":\"http://example.org/\"}";
        Path ok = dir.resolve("top-return.json"), bad = dir.resolve("top-return-number.json"), unknown = dir.resolve("top-unknown.json");
        Files.writeString(ok, "{" + vars + ",\"@documentation\":\"d\",\"@return\":\"the logged line\"}");
        Files.writeString(bad, "{" + vars + ",\"@return\":5}");
        Files.writeString(unknown, "{" + vars + ",\"@returns\":\"x\"}");
        String out = validate(List.of(ok.toString(), bad.toString(), unknown.toString()));
        if (out == null) return;
        assertTrue(out, out.contains(ok + " valid"));
        assertTrue(out, out.contains(bad + " invalid"));
        assertTrue(out, out.contains(unknown + " invalid"));
    }

    public void testTheBindingsOfTheCompilersOwnTestsAreValid() throws Exception {
        List<String> files = new ArrayList<>();
        for (String f : new File("src/test/resources/t36/flow").list((d, n) -> n.endsWith("-bindings.json"))) files.add("src/test/resources/t36/flow/" + f);
        files.add("src/test/resources/t31/approval-base.json");
        String out = validate(files);
        if (out == null) return;
        for (String f : files) assertTrue(f + ": " + out, out.contains(f + " valid"));
    }
}
