package org.openprovenance.prov.template.compiler.test;

import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.descriptors.InJsonSplitter;
import org.openprovenance.prov.template.descriptors.PerformativeChecker;
import org.openprovenance.prov.template.descriptors.PerformativeException;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * T36 part D (stage 7): {@code @performative} and the request and reply classes of a subtype. The fixture is the stage 3
 * one with, on its values: {@code order_note}, {@code priceBefore}, {@code unitCode} and a new in-JSON {@code memo} of the
 * request; {@code approver_ref} and {@code lineCount} of the reply; {@code level}, {@code payload}, the nodes and the rest
 * the service's own. The carrier {@code customer_hash} holds values of the request, of the reply and of the service, the new
 * carrier {@code extra} only a value of the request (R13).
 */
public class T36PerformativeTest extends T36Fixture {

    /** Whether the fixture's bindings carry the annotations: the same catalogue is generated with and without them. */
    boolean performatives = true;
    /** More variables for the fixture's bindings (an ill-declared one, to see generation stop). */
    ObjectNode more = null;

    @Override
    ObjectNode bindings() throws Exception {
        ObjectNode b = super.bindings();
        ObjectNode vars = (ObjectNode) b.get("var");
        vars.set("extra", om.readTree("[[{\"@type\":\"xsd:string\",\"@documentation\":\"A second carrier\",\"@input\":\"optional\"}]]"));
        vars.set("memo", om.readTree("[[{\"@type\":\"xsd:string\",\"@documentation\":\"A free note\",\"@inJson\":\"extra\"}]]"));
        if (performatives) {
            for (String request : List.of("order_note", "priceBefore", "unitCode", "memo")) performative(vars, request, "request");
            for (String reply : List.of("approver_ref", "lineCount")) performative(vars, reply, "reply");
        }
        if (more != null) vars.setAll(more);
        return b;
    }

    static void performative(ObjectNode vars, String name, String value) {
        ((ObjectNode) vars.get(name).get(0).get(0)).put("@performative", value);
    }

    /** A subtype that fixes the approver's name and carries what a repricing needs; another that carries nothing. */
    String reprice() {
        return subtype("reprice", "{\"approver_name\":\"Admin\"}",
                "{\"priceBefore\":\"required\",\"unitCode\":\"optional\",\"lineCount\":\"optional\",\"payload\":\"optional\",\"memo\":\"optional\"}");
    }

    String plain() {
        return subtype("plain", "{\"approver_name\":\"Other\"}", "{}");
    }

    // ---- the refusals (design §12.3) ------------------------------------------------------------------------------------

    TemplateBindingsSchema read(JsonNode bindings) throws Exception {
        return ConfigProcessor.objectMapper.readValue(om.writeValueAsString(bindings), TemplateBindingsSchema.class);
    }

    /** Loads the fixture with one more variable, {@code bad}, the way the compiler does: splits the in-JSON values, then checks the performatives. */
    void check(ObjectNode b) throws Exception {
        TemplateBindingsSchema s = read(b);
        InJsonSplitter.split(s, "the-bindings.json");
        PerformativeChecker.check(s, "the-bindings.json");
    }

    void refused(String title, String json, String... messageContains) throws Exception {
        ObjectNode b = bindings();
        ((ObjectNode) b.get("var")).set("bad", om.readTree(json));
        try {
            check(b);
            fail(title + ": accepted");
        } catch (PerformativeException e) {
            assertTrue(title + ": " + e.getMessage(), e.getMessage().startsWith("T36: "));
            assertTrue(title + ": names the file: " + e.getMessage(), e.getMessage().contains("the-bindings.json"));
            assertTrue(title + ": names the variable: " + e.getMessage(), e.getMessage().contains("variable bad"));
            assertTrue(title + ": names the template: " + e.getMessage(), e.getMessage().contains("approval.provn"));
            for (String m : messageContains) assertTrue(title + ": " + m + " in " + e.getMessage(), e.getMessage().contains(m));
        }
    }

    public void testRefusals() throws Exception {
        refused("a node", "[{\"@id\":\"ex:*\",\"@input\":\"compulsory\",\"@performative\":\"request\"}]", "not a node");
        refused("neither word", "[[{\"@type\":\"xsd:int\",\"@input\":\"optional\",\"@performative\":\"ask\"}]]", "\"ask\"", "\"request\" or \"reply\"");
        refused("the wrong case", "[[{\"@type\":\"xsd:int\",\"@input\":\"optional\",\"@performative\":\"Request\"}]]", "\"Request\"");
        refused("an output", "[[{\"@type\":\"xsd:int\",\"@output\":\"true\",\"@performative\":\"reply\"}]]", "an output");
        refused("an optional output", "[[{\"@type\":\"xsd:int\",\"@output\":\"optional\",\"@performative\":\"reply\"}]]", "an output");
        refused("no input", "[[{\"@type\":\"xsd:int\",\"@performative\":\"request\"}]]", "no input");
        refused("an in-JSON value of neither word",
                "[[{\"@type\":\"xsd:int\",\"@input\":\"optional\",\"@inJson\":\"customer_hash\",\"@performative\":\"both\"}]]", "\"both\"");

        // a carrier that holds in-JSON values: each of its values says its own
        ObjectNode b = bindings();
        performative((ObjectNode) b.get("var"), "customer_hash", "request");
        try {
            check(b);
            fail("a carrier of in-JSON values: accepted");
        } catch (PerformativeException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("variable customer_hash") && e.getMessage().contains("carrier that holds in-JSON values")
                    && e.getMessage().contains("priceBefore"));
        }
    }

    public void testWhatIsAccepted() throws Exception {
        check(bindings());   // the fixture: ordinary inputs and in-JSON values, both words
        ObjectNode b = bindings();
        // a text variable that is no carrier is an ordinary input, and has a performative of its own
        performative((ObjectNode) b.get("var"), "level", "request");
        performative((ObjectNode) b.get("var"), "order_note", "reply");
        check(b);
        // no annotation at all: nothing to refuse
        performatives = false;
        check(bindings());
    }

    // ---- what is generated ----------------------------------------------------------------------------------------------

    static final String CLI = "/libs/t36/t36_cli/src/main/";
    static final String JAVA = CLI + "java/org/example/t36/approval/client/integrator/";
    static final String BINDINGS_COPY = "t36/t36_cli/src/main/resources/META-INF/resources/webjars/t36_cli/0.1.0/bindings/approval.json";
    static final String INTEGRATOR = "org.example.t36.approval.client.integrator.";

    List<String> classes(Map<String, String> tree, String suffix) {
        return tree.keySet().stream().filter(k -> k.endsWith(suffix + ".java")).map(k -> k.substring(k.lastIndexOf('/') + 1)).sorted().collect(Collectors.toList());
    }

    public void testPerformativesAddTheClassesAndTheDescriptionsAndNothingElse() throws Exception {
        performatives = false;
        Map<String, String> without = generate("perf0", false, List.of(), List.of(reprice(), plain()));
        performatives = true;
        Map<String, String> with = generate("perf1", false, List.of(), List.of(reprice(), plain()));
        assertEquals("no performative, no request or reply class", List.of(), classes(without, "Request"));
        assertEquals(List.of(), classes(without, "Reply"));

        Set<String> added = new TreeSet<>(with.keySet());
        added.removeAll(without.keySet());
        assertEquals(Set.of(JAVA.substring(1) + "PlainReply.java", JAVA.substring(1) + "PlainRequest.java",
                        JAVA.substring(1) + "RepriceReply.java", JAVA.substring(1) + "RepriceRequest.java").stream().map(f -> "t36/t36_cli" + f.substring(f.indexOf("/src"))).collect(Collectors.toSet()),
                added);
        assertEquals("nothing removed", Set.of(), without.keySet().stream().filter(k -> !with.containsKey(k)).collect(Collectors.toSet()));
        Set<String> changed = new TreeSet<>();
        for (String k : without.keySet()) if (!with.get(k).equals(without.get(k))) changed.add(k);
        // the documentation and the schema say the performatives, and the copy of the bindings schema is the declaration, which carries them
        assertEquals(Set.of(BINDINGS_COPY, DOC, SCHEMA), changed);
    }

    // ---- the classes, compiled and run ------------------------------------------------------------------------------------

    /** Compiles the typed beans, the request and the reply classes and what they refer to; null when JsonText is not on the class path. */
    ClassLoader compile(String id) throws Exception {
        Class<?> jt = T36JsonTextTest.runtime();
        if (jt == null) {
            System.out.println("[T36] JsonText is not in the prov-model on the classpath: the classes are not compiled (build with -am)");
            return null;
        }
        Path src = Path.of(OUT + id + CLI + "java");
        Path out = Path.of(OUT + id + "/classes");
        Files.createDirectories(out);
        List<String> cp = new ArrayList<>();
        for (String prop : List.of("java.class.path", "jdk.module.path")) {
            String v = System.getProperty(prop);
            if (v != null && !v.isEmpty()) cp.addAll(Arrays.asList(v.split(File.pathSeparator)));
        }
        cp.add(new File(jt.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
        List<String> args = new ArrayList<>(List.of("-proc:none", "-nowarn", "-d", out.toString(), "-sourcepath", src.toString(), "-classpath", String.join(File.pathSeparator, cp)));
        for (String f : List.of("RepriceInputs", "RepriceRequest", "RepriceReply", "PlainInputs", "PlainRequest", "PlainReply")) {
            args.add(src.resolve("org/example/t36/approval/client/integrator/" + f + ".java").toString());
        }
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        StringWriter messages = new StringWriter();
        int rc = javac.run(null, null, new java.io.OutputStream() {
            public void write(int b) {
                messages.write(b);
            }
        }, args.toArray(new String[0]));
        assertEquals("the generated classes compile: " + messages, 0, rc);
        return new URLClassLoader(new URL[]{out.toUri().toURL()}, getClass().getClassLoader());
    }

    static Set<String> fields(Class<?> c) {
        return Arrays.stream(c.getFields()).filter(f -> !java.lang.reflect.Modifier.isStatic(f.getModifiers())).map(java.lang.reflect.Field::getName).collect(Collectors.toCollection(TreeSet::new));
    }

    @SuppressWarnings("unchecked")
    public void testTheRequestAndTheReplyCopyTheirValuesAndNoOther() throws Exception {
        generate("perfj", false, List.of(), List.of(reprice(), plain()));
        ClassLoader l = compile("perfj");
        if (l == null) return;
        Class<?> request = l.loadClass(INTEGRATOR + "RepriceRequest"), reply = l.loadClass(INTEGRATOR + "RepriceReply");
        Class<?> typed = l.loadClass(INTEGRATOR + "RepriceInputs");

        // the fields: what the caller gives, with the other keys of the carrier only the caller's values are in; what the service returns
        assertEquals(Set.of("order_note", "priceBefore", "unitCode", "memo", "extraOther"), fields(request));
        assertEquals(Set.of("approver_ref", "lineCount"), fields(reply));
        assertFalse("R13: the carrier customer_hash also holds values of the service, so no map of other keys in the request",
                fields(request).contains("customer_hashOther"));
        assertTrue("it is in the typed bean", fields(typed).contains("customer_hashOther"));
        for (String serviceOwn : List.of("approver", "order", "level", "payload", "approver_name", "customer_hash", "extra")) {
            assertFalse(serviceOwn + " is neither the caller's nor returned", fields(request).contains(serviceOwn) || fields(reply).contains(serviceOwn));
        }
        assertEquals("the other subtype: one value each", Set.of("order_note"), fields(l.loadClass(INTEGRATOR + "PlainRequest")));
        assertEquals(Set.of("approver_ref"), fields(l.loadClass(INTEGRATOR + "PlainReply")));

        // into copies its fields into the bean it is given and returns it, and touches no other field
        Object t = newInstanceOf(typed);
        T36SubtypeTest.set(t, "approver", "ex:agent");
        T36SubtypeTest.set(t, "level", 3);
        T36SubtypeTest.set(t, "payload", "{\"p\":1}");
        Object asked = newInstanceOf(request);
        T36SubtypeTest.set(asked, "order_note", "please");
        T36SubtypeTest.set(asked, "priceBefore", 2.5);
        T36SubtypeTest.set(asked, "unitCode", "EA");
        T36SubtypeTest.set(asked, "memo", "m");
        Map<String, Object> other = new LinkedHashMap<>();
        other.put("colour", "red");
        other.put("nested", List.of(1, Map.of("q", true)));
        T36SubtypeTest.set(asked, "extraOther", other);
        assertSame(t, T36SubtypeTest.invoke(asked, request, "into", t));
        assertEquals(2.5, T36SubtypeTest.get(t, "priceBefore"));
        assertEquals("m", T36SubtypeTest.get(t, "memo"));
        assertEquals(other, T36SubtypeTest.get(t, "extraOther"));
        assertEquals("ex:agent", T36SubtypeTest.get(t, "approver"));
        assertEquals(3, T36SubtypeTest.get(t, "level"));
        assertEquals("{\"p\":1}", T36SubtypeTest.get(t, "payload"));
        assertNull("the reply's values are not the request's to set", T36SubtypeTest.get(t, "approver_ref"));

        // of(typed) gives back what into copied
        Object back = T36SubtypeTest.invoke(null, request, "of", t);
        for (String f : fields(request)) assertEquals(f, T36SubtypeTest.get(asked, f), T36SubtypeTest.get(back, f));

        // an absent map of other keys leaves the typed bean's own
        Object t2 = newInstanceOf(typed);
        T36SubtypeTest.invoke(newInstanceOf(request), request, "into", t2);
        assertEquals(Map.of(), T36SubtypeTest.get(t2, "extraOther"));

        // the reply, the same
        Object answered = newInstanceOf(reply);
        T36SubtypeTest.set(answered, "approver_ref", "ref-1");
        T36SubtypeTest.set(answered, "lineCount", 7);
        Object t3 = newInstanceOf(typed);
        T36SubtypeTest.set(t3, "priceBefore", 9.0);
        T36SubtypeTest.invoke(answered, reply, "into", t3);
        assertEquals("ref-1", T36SubtypeTest.get(t3, "approver_ref"));
        assertEquals(7, T36SubtypeTest.get(t3, "lineCount"));
        assertEquals("the request's value is left", 9.0, T36SubtypeTest.get(t3, "priceBefore"));
        Object backReply = T36SubtypeTest.invoke(null, reply, "of", t3);
        for (String f : fields(reply)) assertEquals(f, T36SubtypeTest.get(answered, f), T36SubtypeTest.get(backReply, f));

        // the request feeds the one record: the typed bean writes each carrier, the request's other keys among them
        Object general = T36SubtypeTest.invoke(t, typed, "toGeneral");
        assertEquals(T36SubtypeTest.json("{\"memo\":\"m\",\"colour\":\"red\",\"nested\":[1,{\"q\":true}]}"), T36SubtypeTest.json((String) T36SubtypeTest.get(general, "extra")));
        // payload is the service's own: the application put it in the typed bean, and it is written with the caller's values
        assertEquals(T36SubtypeTest.json("{\"priceBefore\":2.5,\"unit\":\"EA\",\"payload\":{\"p\":1}}"), T36SubtypeTest.json((String) T36SubtypeTest.get(general, "customer_hash")));
    }

    static Object newInstanceOf(Class<?> c) throws Exception {
        return c.getDeclaredConstructor().newInstance();
    }

    /** The point of part D (design §12.1): an application's JSON reader fills the request from a body and can set nothing else. */
    public void testABodyCannotSetWhatTheCallerDoesNotGive() throws Exception {
        generate("perfj", false, List.of(), List.of(reprice(), plain()));
        ClassLoader l = compile("perfj");
        if (l == null) return;
        Class<?> request = l.loadClass(INTEGRATOR + "RepriceRequest"), typed = l.loadClass(INTEGRATOR + "RepriceInputs");
        String body = "{\"priceBefore\":2,\"order_note\":\"n\",\"approver\":\"ex:99\",\"approver_ref\":\"evil\",\"lineCount\":9,\"level\":7}";

        // Jackson 3 with its defaults, which ignores a member it has no field for
        tools.jackson.databind.json.JsonMapper reader = tools.jackson.databind.json.JsonMapper.builder().build();
        Object asTyped = reader.readValue(body, typed);
        assertEquals("the typed bean is filled with whatever the body says (the problem)", "ex:99", T36SubtypeTest.get(asTyped, "approver"));
        assertEquals("evil", T36SubtypeTest.get(asTyped, "approver_ref"));

        Object asked = reader.readValue(body, request);
        assertEquals(2.0, T36SubtypeTest.get(asked, "priceBefore"));
        Object t = T36SubtypeTest.invoke(asked, request, "into", newInstanceOf(typed));
        for (String f : List.of("approver", "approver_ref", "lineCount", "level")) {
            assertNull(f + " is not the caller's: the body cannot set it", T36SubtypeTest.get(t, f));
        }
        assertEquals("n", T36SubtypeTest.get(t, "order_note"));

        // a reader that refuses a member it has no field for refuses the body
        tools.jackson.databind.json.JsonMapper strict = tools.jackson.databind.json.JsonMapper.builder()
                .enable(tools.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();
        try {
            strict.readValue(body, request);
            fail("a strict reader accepted a member the request has no field for");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("approver"));
        }
    }

    // ---- an ill-declared performative stops generation ---------------------------------------------------------------------

    public void testAnIllDeclaredPerformativeStopsGeneration() throws Exception {
        more = (ObjectNode) om.readTree("{\"bad\":[[{\"@type\":\"xsd:int\",\"@input\":\"optional\",\"@performative\":\"ask\"}]]}");
        try {
            generate("perfbad", false, List.of(), List.of(reprice()));
            fail("generation went on");
        } catch (PerformativeException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("variable bad") && e.getMessage().contains("\"ask\""));
        }
    }

    // ---- the descriptions (design §12.5) -----------------------------------------------------------------------------------

    public void testTheDocumentationSaysThePerformatives() throws Exception {
        performatives = false;
        generate("perfd0", false, List.of(), List.of(reprice(), plain()));
        assertFalse("without @performative the page says nothing of it", T36DescriptionsTest.read("perfd0", DOC).contains("csv_performative"));
        performatives = true;
        generate("perfd1", false, List.of(), List.of(reprice(), plain()));
        String doc = T36DescriptionsTest.read("perfd1", DOC);
        assertTrue(doc, doc.contains("<span class='csv_performative'>request</span>") && doc.contains("<span class='csv_performative'>reply</span>"));
        // each value of the subtype by whose content it is
        assertTrue(doc, doc.contains("<span class='csv_performative'>of the request</span> <span class='csv_doc'>order_note, priceBefore, unitCode, memo</span>"));
        assertTrue(doc, doc.contains("<span class='csv_performative'>of the reply</span> <span class='csv_doc'>approver_ref, lineCount</span>"));
        assertTrue(doc, doc.contains("<span class='csv_performative'>the service&#x27;s own</span>") || doc.contains("<span class='csv_performative'>the service's own</span>"));
    }

    String definition(JsonNode schema, String name) {
        JsonNode d = schema.get("definitions").get(name);
        assertNotNull(name, d);
        return d.toString();
    }

    public void testTheJsonSchemaHasTheRequestAndTheReply() throws Exception {
        generate("perfs", false, List.of(), List.of(reprice(), plain()));
        JsonNode schema = om.readTree(T36DescriptionsTest.read("perfs", SCHEMA));
        JsonNode req = schema.get("definitions").get("t31.repriceRequest"), rep = schema.get("definitions").get("t31.repriceReply");
        assertNotNull(req);
        assertNotNull(rep);
        assertEquals(Set.of("order_note", "priceBefore", "unitCode", "memo", "extraOther"), req.get("properties").propertyNames().stream().collect(Collectors.toSet()));
        assertEquals(Set.of("approver_ref", "lineCount"), rep.get("properties").propertyNames().stream().collect(Collectors.toSet()));
        assertFalse("no isA: T40 does not touch them", req.get("properties").has("isA") || rep.get("properties").has("isA"));
        assertFalse(req.get("additionalProperties").asBoolean());
        assertEquals("a required value of the request is required", Set.of("priceBefore"), req.get("required").valueStream().map(JsonNode::asText).collect(Collectors.toSet()));
        assertEquals("a compulsory input of the reply is required", Set.of("approver_ref"), rep.get("required").valueStream().map(JsonNode::asText).collect(Collectors.toSet()));
        assertTrue(req.get("properties").get("priceBefore").get("description").asText().contains("[@performative: request]"));
        assertEquals("number", req.get("properties").get("priceBefore").get("type").asText());
        assertTrue("the general template's schema says the performative of a value",
                schema.get("definitions").get("t31.Approval").get("properties").get("approver_ref").get("description").asText().contains("[@performative: reply]"));
        assertTrue(schema.get("definitions").get("t31.Approval").get("properties").get("customer_hash").get("description").asText().contains("priceBefore (number, request)"));
        for (JsonNode block : schema.get("allOf")) assertFalse("no if-isA block for a request or a reply: " + block, block.toString().contains("Request") || block.toString().contains("Reply"));
        assertEquals("the other subtype has its own", Set.of("order_note"), schema.get("definitions").get("t31.plainRequest").get("properties").propertyNames().stream().collect(Collectors.toSet()));
        assertEquals(Set.of("approver_ref"), schema.get("definitions").get("t31.plainReply").get("properties").propertyNames().stream().collect(Collectors.toSet()));

        // an application publishes the request's schema as the schema of its request body
        ObjectNode wrapper = (ObjectNode) om.readTree("{\"$ref\":\"#/definitions/t31.repriceRequest\"}");
        wrapper.set("definitions", schema.get("definitions"));
        String path = OUT + "perfs/wrapper.json";
        om.writerWithDefaultPrettyPrinter().writeValue(new File(path), wrapper);
        com.networknt.schema.Schema compiled = new org.openprovenance.prov.template.compiler.CompilerJsonSchema(pf).setupJsonSchemaFromFile(path);
        assertEquals("valid", List.of(), errors(compiled, "{\"priceBefore\":2.5,\"order_note\":\"n\",\"extraOther\":{\"colour\":[1]}}"));
        assertFalse("a required value is missing", errors(compiled, "{\"order_note\":\"n\"}").isEmpty());
        assertFalse("a value of the reply is not a property of the request", errors(compiled, "{\"priceBefore\":2,\"approver_ref\":\"x\"}").isEmpty());
        assertFalse("nor one of the service's", errors(compiled, "{\"priceBefore\":2,\"approver\":\"ex:99\"}").isEmpty());
        assertFalse("a string for a number", errors(compiled, "{\"priceBefore\":\"cheap\"}").isEmpty());
    }

    List<String> errors(com.networknt.schema.Schema schema, String instance) throws Exception {
        return schema.validate(om.readTree(instance)).stream().map(Object::toString).collect(Collectors.toList());
    }

    static final String NS = "http://example.org/t36#";

    public void testTheTurtleSaysThePerformatives() throws Exception {
        performatives = true;
        generate("perft", false, List.of(), List.of(reprice(), plain()), c -> {
            c.put("turtle", "catalogue.ttl");
            c.put("turtleNamespace", NS);
        });
        String ttl = T36DescriptionsTest.read("perft", "t36/t36_cli/src/main/resources/catalogue.ttl");
        assertTrue(ttl, ttl.contains("tpl:performative a owl:DatatypeProperty"));
        assertTrue("an in-JSON value", ttl.contains(":approval-priceBefore a owl:DatatypeProperty") && ttl.contains("tpl:performative \"request\""));
        // R15: an ordinary value that has a performative has a term, without tpl:inJson
        assertTrue(ttl, ttl.contains(":approval-order_note a owl:DatatypeProperty") && ttl.contains(":approval-approver_ref a owl:DatatypeProperty"));
        String orderNote = ttl.substring(ttl.indexOf(":approval-order_note a owl:DatatypeProperty"));
        orderNote = orderNote.substring(0, orderNote.indexOf(" .\n"));
        assertTrue(orderNote, orderNote.contains("rdfs:domain :approval") && orderNote.contains("rdfs:range xsd:string") && orderNote.contains("tpl:performative \"request\"") && !orderNote.contains("tpl:inJson"));
        assertFalse("a value with none has no such term", ttl.contains(":approval-level a"));
        String[] r = run(List.of("python3", "-I", "-c",
                "import sys\nfrom rdflib import Graph, Namespace, Literal\ng=Graph(); g.parse(sys.argv[1], format='turtle')\nN=Namespace(sys.argv[2]); T=Namespace('http://openprovenance.org/ns/tpl#')\n"
                        + "want={'approval-order_note':'request','approval-priceBefore':'request','approval-unitCode':'request','approval-memo':'request','approval-approver_ref':'reply','approval-lineCount':'reply'}\n"
                        + "got={str(s).replace(sys.argv[2],''):str(o) for s,o in g.subject_objects(T.performative)}\n"
                        + "print('FAIL '+str(got) if got!=want else 'ok')\n",
                OUT + "perft/libs/t36/t36_cli/src/main/resources/catalogue.ttl", NS), new File("."));
        if (r == null || r[1].contains("No module named")) {
            System.out.println("[T36] python3 or rdflib is not installed: the Turtle file is not parsed");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertFalse("a wrong answer: " + r[1], r[1].contains("FAIL "));

        // a catalogue without a performative declares no such term
        performatives = false;
        generate("perft0", false, List.of(), List.of(reprice(), plain()), c -> {
            c.put("turtle", "catalogue.ttl");
            c.put("turtleNamespace", NS);
        });
        assertFalse(T36DescriptionsTest.read("perft0", "t36/t36_cli/src/main/resources/catalogue.ttl").contains("performative"));
    }

    // ---- the other languages ----------------------------------------------------------------------------------------------

    static final String PY_LIBRARY = "../prov-template-library/src/main/python";

    public void testTheEmittedPythonRunsAgainstAStandInTypedBean() throws Exception {
        generate("perfy", false, List.of(), List.of(reprice(), plain()));
        Path root = Path.of(OUT + "perfy/py");
        T36SubtypeTest.FileUtils_deleteQuietly(root);
        Path integrator = root.resolve("org/example/t36/approval/client/integrator");
        Files.createDirectories(integrator);
        Files.writeString(integrator.resolve("RepriceRequest.py"), emitted("python", "RepriceRequest"));
        Files.writeString(integrator.resolve("RepriceReply.py"), emitted("python", "RepriceReply"));
        String[] r = run(List.of("python3", "-I", "src/test/resources/t36/check_request_reply.py", root.toString(), PY_LIBRARY), new File("."));
        if (r == null) {
            System.out.println("[T36] python3 is not installed: the emitted Python is not run");
            return;
        }
        assertFalse("a wrong answer: " + r[1], r[1].contains("FAIL "));
        assertEquals(r[1], "0", r[0]);
        for (String line : r[1].split("\n")) if (line.startsWith("BLOCKED")) System.out.println("[T36] " + line);
    }

    public void testTheEmittedJavaScriptRunsAgainstAStandInTypedBean() throws Exception {
        generate("perfy", false, List.of(), List.of(reprice(), plain()));
        Path dir = Path.of(OUT + "perfy/js");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("RepriceRequest.js"), emitted("javascript", "RepriceRequest"));
        Files.writeString(dir.resolve("RepriceReply.js"), emitted("javascript", "RepriceReply"));
        String[] r = run(List.of("node", "src/test/resources/t36/check_request_reply.js", dir.resolve("RepriceRequest.js").toString(), dir.resolve("RepriceReply.js").toString()), new File("."));
        if (r == null) {
            System.out.println("[T36] node is not installed: the emitted JavaScript is not run");
            return;
        }
        assertFalse("a wrong answer: " + r[1], r[1].contains("FAIL "));
        assertEquals(r[1], "0", r[0]);
    }

    /** Rust: the fields and no method. The emitter writes the copy of one bean's field into another's as {@code Some(x.to_string())} or {@code Some(x)}, which does not compile for an Option field (checked with cargo on the generated file, by hand), and it is not extended here. */
    public void testTheRustClassesHaveTheFieldsAndNoMethods() throws Exception {
        ObjectNode c = catalogue("perfr", false, List.of(), List.of(reprice(), plain()));
        c.put("rust_dir", OUT + "perfr/libs/rust");
        String dir = OUT + "perfr/";
        File catalogueFile = new File(dir + "catalogue.json");
        om.writerWithDefaultPrettyPrinter().writeValue(catalogueFile, c);
        try (PrintStream log = new PrintStream(new FileOutputStream(dir + "generation.log"), true, StandardCharsets.UTF_8)) {
            PrintStream out = System.out, err = System.err;
            System.setOut(log);
            System.setErr(log);
            try {
                new ConfigProcessor(pf).processTemplateGenerationConfig(catalogueFile.getPath(), ".", ".", ".", List.of(RES), pf);
            } finally {
                System.setOut(out);
                System.setErr(err);
            }
        }
        String request = Files.readString(Path.of(dir + "libs/rust/org/example/t36/approval/client/integrator/reprice_request.rs"));
        String reply = Files.readString(Path.of(dir + "libs/rust/org/example/t36/approval/client/integrator/reprice_reply.rs"));
        for (String f : List.of("pub order_note: Option<String>", "pub price_before: Option<f64>", "pub unit_code: Option<String>", "pub memo: Option<String>")) {
            assertTrue(f + " in " + request, request.contains(f));
        }
        assertTrue(reply, reply.contains("pub approver_ref: Option<String>") && reply.contains("pub line_count: Option<i32>"));
        for (String rs : List.of(request, reply)) {
            assertFalse("no method: " + rs, rs.contains("fn into") || rs.contains("fn of"));
            assertTrue("it derives, as the other beans do: " + rs, rs.contains("#[derive(Debug, Clone, Serialize, Deserialize)]"));
            assertTrue("says why: " + rs, rs.contains("are not in Rust"));
        }
        assertFalse("no map of other keys in Rust", request.contains("extra_other"));
    }

    /** The emitted text of a generated class, by its name. */
    String emitted(String language, String name) {
        for (org.openprovenance.prov.template.compiler.past.Class c : lastProcessor.getCompilerSubtype().getGeneratedClasses()) {
            if (c.name.equals(name)) {
                return language.equals("python")
                        ? new org.openprovenance.prov.template.compiler.past.emitter.Python(null).emit(c).toString()
                        : new org.openprovenance.prov.template.compiler.past.emitter.JavaScript().emit(c).toString();
            }
        }
        throw new IllegalStateException("no generated class " + name);
    }
}
