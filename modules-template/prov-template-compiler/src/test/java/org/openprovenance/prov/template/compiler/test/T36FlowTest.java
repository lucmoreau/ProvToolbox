package org.openprovenance.prov.template.compiler.test;

import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.FlowException;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * T36 part E (stage 9, design §13): the flow. The generated class is compiled and run against stand-ins of the application's rules and of
 * the previous records; each guard, each kind of origin, the reply, the descriptions and each refusal are tested, and a catalogue without
 * the entry generates what it generated.
 */
public class T36FlowTest extends T36FlowFixture {

    static final String FLOWS = "org/example/t36flow/flows/";
    static final String FLOWS_PKG = "org.example.t36flow.flows.";
    static final String ACTIONS_PKG = "org.example.t36flow.actions.client.integrator.";
    static final String BACKEND_PKG = "org.example.t36flow.backend.client.integrator.";
    static final List<String> SOURCES = List.of(FLOWS + "RepriceFlow.java", FLOWS + "RepriceFlowRules.java",
            "org/example/t36flow/backend/client/integrator/Line_changeInputs.java", "org/example/t36flow/backend/client/integrator/ChangingOutputs.java",
            "org/example/t36flow/actions/client/integrator/RepriceInputs.java", "org/example/t36flow/actions/client/integrator/RepriceRequest.java",
            "org/example/t36flow/actions/client/integrator/RepriceReply.java");

    /**
     * T46: the other shapes of flow (nothing stood, a record of another template, several records, no request, a composite record)
     * are in Rust too and compile; the composite one is run by cargo with the Rust counterpart of
     * {@link #testACompositeRecordIsBuiltInPartsAndTheApplicationLoops}.
     */
    @SuppressWarnings("unchecked")
    public void testTheOtherShapesOfFlowAreInRust() throws Exception {
        String[] r;
        for (Object[] shape : new Object[][]{
                {"rs_ns", List.of(LINE_CHANGE, REPRICE, FLOW_NS), "create_flow"},
                {"rs_xt", List.of(LINE_CHANGE, ASK_SEND, SENDING, xt("t36.LineChange")), "send_flow"},
                {"rs_mr", List.of(LINE_CHANGE, LINE_REGISTERING, REPRICE, FLOW_MR), "add_flow"},
                {"rs_noask", List.of(LINE_CHANGE, ASK_SEND, SENDING, CONFIRM, FLOW_NOASK), "confirm_flow"}}) {
            String id = (String) shape[0];
            generate(id, (List<String>) shape[1], c -> c.put("rust_dir", OUT + id + "/libs/rust"));
            Path dir = Path.of(OUT + id + "/libs/rust/org/example/t36flow/flows");
            assertTrue(dir + " has the flow and its rules", Files.exists(dir.resolve(shape[2] + ".rs")) && Files.exists(dir.resolve(shape[2] + "_rules.rs")));
            r = T36Fixture.cargoTestGenerated(Path.of(OUT + id + "/libs/rust"), "t36-" + id.replace('_', '-'), "src/test/resources/t36/rust-bean/compiles.rs");
            if (r == null) {
                System.out.println("[T36] cargo is not installed: the Rust flows are not compiled");
                return;
            }
            assertEquals(id + ": " + r[1], "0", r[0]);
        }
        generate("rs_comp", List.of(LINE_CHANGE, CONFIRM, FLOW_COMPOSITE), c -> c.put("rust_dir", OUT + "rs_comp/libs/rust"));
        r = T36Fixture.cargoTestGenerated(Path.of(OUT + "rs_comp/libs/rust"), "t36-rs-comp", "src/test/resources/t36/rust-bean/flow_composite.rs");
        if (r == null) return;
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("test result: ok. 1 passed"));
    }

    /**
     * T47: the role accessors of a bean (a variable that declares {@code @role}): the getter returns the field's {@code Option}, the setter
     * is {@code &mut self} and writes {@code self.field}. They did not compile in Rust (found in the first application's catalogue).
     */
    public void testTheRoleAccessorsOfABeanCompileAndWorkInRust() throws Exception {
        Path bindings = Path.of("target/t47/changing-roles.json");
        Files.createDirectories(bindings.getParent());
        ObjectNode b = (ObjectNode) om.readTree(Files.readString(Path.of(RES + "changing-bindings.json")));
        ((ObjectNode) b.get("var").get("document1").get(0)).put("@role", "effect");
        ((ObjectNode) b.get("var").get("changing").get(0)).put("@role", "general");
        om.writerWithDefaultPrettyPrinter().writeValue(bindings.toFile(), b);
        generate("roles", List.of(LINE_CHANGE, REPRICE, FLOW), c -> {
            c.put("rust_dir", OUT + "roles/libs/rust");
            for (tools.jackson.databind.JsonNode t : c.get("templates")) {
                if (t.has("cbindings") && t.get("cbindings").asText().endsWith("/changing.json")) ((ObjectNode) t).put("cbindings", bindings.toString());
            }
        });
        Path rustDir = Path.of(OUT + "roles/libs/rust");
        String outputs = Files.readString(rustDir.resolve("org/example/t36flow/backend/client/integrator/changing_outputs.rs"));
        assertTrue(outputs, outputs.contains("pub fn get_effect(&self) -> Option<String>"));
        assertTrue(outputs, outputs.contains("pub fn set_effect(&mut self, value: &str)") && outputs.contains("self.document1 = Some(value.to_string());"));
        String[] r = T36Fixture.cargoTestGenerated(rustDir, "t36-rust-roles", "src/test/resources/t36/rust-bean/roles.rs");
        if (r == null) {
            System.out.println("[T36] cargo is not installed: the role accessors are not compiled");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("test result: ok. 1 passed"));
    }

    /**
     * T46: the flow and its rules are in Rust too: compiled, and run by cargo with the Rust counterparts of the three tests above
     * (the origins, a changed request value, the guards) and of the reply's.
     */
    public void testTheRustFlowDoesWhatTheJavaOneDoes() throws Exception {
        generate("flowrust", List.of(LINE_CHANGE, REPRICE, FLOW), c -> c.put("rust_dir", OUT + "flowrust/libs/rust"));
        Path rustDir = Path.of(OUT + "flowrust/libs/rust");
        String flow = Files.readString(rustDir.resolve("org/example/t36flow/flows/reprice_flow.rs"));
        for (String f : List.of("pub fn ask(request: RepriceRequest, rules: impl RepriceFlowRules + 'static, document_made: ChangingOutputs) -> RepriceInputs",
                "// SUPPLIED      agent: the application's rule caller", "// CHAINED       subject0:", "pub fn refused(mut asked: RepriceInputs, code: &str) -> RepriceReply")) {
            assertTrue(f + " in " + flow, flow.contains(f));
        }
        String rules = Files.readString(rustDir.resolve("org/example/t36flow/flows/reprice_flow_rules.rs"));
        assertTrue(rules, rules.contains("pub trait RepriceFlowRules") && rules.contains("fn caller(&self, asked: &RepriceInputs, document_made: &ChangingOutputs) -> String;"));
        String[] r = T36Fixture.cargoTestGenerated(rustDir, "t36-rust-flow", "src/test/resources/t36/rust-bean/flow.rs");
        if (r == null) {
            System.out.println("[T36] cargo is not installed: the Rust flow is not compiled and run");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("test result: ok. 4 passed"));
    }

    // ---- the generated Java, compiled and run ---------------------------------------------------------------------------------

    static Object make(ClassLoader l, String name, Object... fieldsAndValues) throws Exception {
        Object o = T36SubtypeTest.newInstance(l, name);
        for (int i = 0; i < fieldsAndValues.length; i += 2) T36SubtypeTest.set(o, (String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        return o;
    }

    /** The application's rules, as a stand-in: what it was called with is kept. */
    static final class Calls {
        final List<String> names = new ArrayList<>();
        final Map<String, Object[]> arguments = new LinkedHashMap<>();
    }

    static Object rules(ClassLoader l, Calls calls) throws Exception {
        Class<?> rules = l.loadClass(FLOWS_PKG + "RepriceFlowRules");
        return Proxy.newProxyInstance(l, new Class<?>[]{rules}, (proxy, method, args) -> {
            calls.names.add(method.getName());
            calls.arguments.put(method.getName(), args);
            switch (method.getName()) {
                case "caller": return "agent-7";
                case "clock": return "2026-11-16T10:00:00Z";
                case "estimate": return "estimate-after";
                case "version": return "v2";
                default: throw new IllegalStateException(method.getName());
            }
        });
    }

    /** What stood: the record that made the document, and the one that made the line (two records), each with what the store made of it. */
    static final class Stood {
        Object line, lineMade, document, documentMade;
    }

    static Stood stood(ClassLoader l, String lineNumber, String documentMade) throws Exception {
        Stood s = new Stood();
        s.line = make(l, BACKEND_PKG + "Line_changeInputs", "partLine", lineNumber, "partPriceUnit", "10", "partProduct", "bolt", "partQuantity", "100", "ptype", "part");
        Map<String, Object> open = new LinkedHashMap<>();
        open.put("colour", "red");
        T36SubtypeTest.set(s.line, "pattributesOther", open);
        s.lineMade = make(l, BACKEND_PKG + "ChangingOutputs", "part1", "P1");
        s.document = make(l, BACKEND_PKG + "Line_changeInputs", "items", "5", "name1", "RFQ-1", "state1", "open", "attributesOther", new LinkedHashMap<String, Object>(Map.of("act", "removal")));
        s.documentMade = make(l, BACKEND_PKG + "ChangingOutputs", "document1", documentMade);
        return s;
    }

    Object request(ClassLoader l, int line, double price) throws Exception {
        return make(l, ACTIONS_PKG + "RepriceRequest", "line", line, "unitPrice", price, "reason", "cheaper");
    }

    Object runAsk(ClassLoader l, Class<?> flow, Object request, Object rules, Stood s) throws Exception {
        return T36SubtypeTest.invoke(null, flow, "ask", request, rules, s.documentMade);
    }

    Object runRecord(ClassLoader l, Class<?> flow, Object asked, Object rules, Stood s) throws Exception {
        return T36SubtypeTest.invoke(null, flow, "record", asked, s.document, s.documentMade, s.line, s.lineMade, rules);
    }

    static Map<String, Object> fieldsOf(Object o) throws Exception {
        Map<String, Object> res = new TreeMap<>();
        for (java.lang.reflect.Field f : o.getClass().getFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers()) || f.getName().equals("isA")) continue;
            res.put(f.getName(), f.get(o));
        }
        return res;
    }

    public void testTheGeneratedFlowRunsAndFillsEveryValueFromItsOrigin() throws Exception {
        generate("run", List.of(LINE_CHANGE, REPRICE, FLOW));
        ClassLoader l = compile("run", SOURCES);
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "RepriceFlow");
        Calls calls = new Calls();
        Object rules = rules(l, calls);
        Stood s = stood(l, "3", "D1");

        // 1. ask: the request becomes the user bean; the rest of it is given
        Object asked = runAsk(l, flow, request(l, 3, 12.5), rules, s);
        assertEquals(3, T36SubtypeTest.get(asked, "line"));
        assertEquals(12.5, T36SubtypeTest.get(asked, "unitPrice"));
        assertEquals("cheaper", T36SubtypeTest.get(asked, "reason"));
        assertEquals("CHAINED: the node of the document the request addresses is the one the previous record made", "D1", T36SubtypeTest.get(asked, "subject0"));
        assertEquals("SUPPLIED", "agent-7", T36SubtypeTest.get(asked, "agent"));
        assertEquals("2026-11-16T10:00:00Z", T36SubtypeTest.get(asked, "time"));
        assertNull("the reply's values are not given yet", T36SubtypeTest.get(asked, "outcome"));
        assertSame("a rule is given the beans in scope, not strings picked out of them", asked, calls.arguments.get("caller")[0]);
        assertSame(s.documentMade, calls.arguments.get("caller")[1]);

        // 2. record: every value of the backend's bean has its origin
        Object recorded = runRecord(l, flow, asked, rules, s);
        Map<String, Object> expected = new TreeMap<>();
        expected.put("agent", "agent-7");                // AS ASKED
        expected.put("time", "2026-11-16T10:00:00Z");    // AS ASKED
        expected.put("priceUnit", "12.5");               // AS ASKED, as text
        expected.put("partLine", "3");                   // AS ASKED, as text
        expected.put("partPriceUnit", "12.5");           // AS ASKED, as text
        expected.put("document0", "D1");                 // CHAINED
        expected.put("part0", "P1");                     // CHAINED
        expected.put("name1", "RFQ-1");                  // CARRIED OVER, from the document's record
        expected.put("state1", "open");
        expected.put("items", "5");
        expected.put("ptype", "part");                   // CARRIED OVER, from the line's record ("rest")
        expected.put("partProduct", "bolt");
        expected.put("partQuantity", "100");
        expected.put("priceBefore", "10");               // CARRIED OVER, under another name
        expected.put("partPriceBefore", "10");
        expected.put("atype", "Repricing");              // CONSTANT
        expected.put("overwrite", "true");
        expected.put("partPriceSource", "repriced");
        expected.put("estimate", "estimate-after");      // SUPPLIED
        expected.put("erpId1", "v2");
        Map<String, Object> got = fieldsOf(recorded);
        Map<String, Object> pattributesOther = (Map<String, Object>) got.remove("pattributesOther");
        Map<String, Object> attributesOther = (Map<String, Object>) got.remove("attributesOther");
        for (Map.Entry<String, Object> e : got.entrySet()) {
            assertEquals("the value " + e.getKey() + " has the origin the flow says (null when it has none)", expected.get(e.getKey()), e.getValue());
        }
        assertEquals("nothing else is filled, nothing is missing", expected.keySet(), got.entrySet().stream().filter(e -> e.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toSet()));
        assertEquals("the line's other keys are carried over", Map.of("colour", "red"), pattributesOther);
        assertNotSame("as a copy", T36SubtypeTest.get(s.line, "pattributesOther"), pattributesOther);
        assertEquals("the act's other keys are not carried: a removal's reason would leak into the next reprice", Map.of(), attributesOther);
        Object[] estimateArgs = calls.arguments.get("estimate");
        assertSame(asked, estimateArgs[0]);
        assertSame("the rule sees the bean so far, and every stood thing's inputs and outputs", recorded, estimateArgs[1]);
        assertSame(s.document, estimateArgs[2]);
        assertSame(s.documentMade, estimateArgs[3]);
        assertSame(s.line, estimateArgs[4]);
        assertSame(s.lineMade, estimateArgs[5]);
        assertEquals("the supplied values are asked of the rules, once each: " + calls.names, List.of("caller", "clock", "version", "estimate"), calls.names);

        // 3. answer: what the store made becomes the reply
        Object made = make(l, BACKEND_PKG + "ChangingOutputs", "document1", "D2");
        Object reply = T36SubtypeTest.invoke(null, flow, "answer", asked, recorded, made);
        assertEquals("accepted", T36SubtypeTest.get(reply, "outcome"));
        assertEquals("AS MADE: the backend's own value, given back", "v2", T36SubtypeTest.get(reply, "result"));
        assertNull(T36SubtypeTest.get(reply, "code"));
        Object nothing = make(l, BACKEND_PKG + "ChangingOutputs");
        assertEquals("nothing made", "failed", T36SubtypeTest.get(T36SubtypeTest.invoke(null, flow, "answer", asked, recorded, nothing), "outcome"));
        Object refused = T36SubtypeTest.invoke(null, flow, "refused", make(l, ACTIONS_PKG + "RepriceInputs"), "NO_CHANGE");
        assertEquals("refused", T36SubtypeTest.get(refused, "outcome"));
        assertEquals("NO_CHANGE", T36SubtypeTest.get(refused, "code"));
        assertNull(T36SubtypeTest.get(refused, "result"));
        Object failed = T36SubtypeTest.invoke(null, flow, "failed", make(l, ACTIONS_PKG + "RepriceInputs"));
        assertEquals("failed", T36SubtypeTest.get(failed, "outcome"));
        assertNull(T36SubtypeTest.get(failed, "code"));
    }

    @SuppressWarnings("unchecked")
    public void testAChangedRequestValueChangesExactlyTheValuesThatCameFromIt() throws Exception {
        generate("run", List.of(LINE_CHANGE, REPRICE, FLOW));
        ClassLoader l = compile("run", SOURCES);
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "RepriceFlow");
        Object rules = rules(l, new Calls());
        Stood s = stood(l, "3", "D1");
        Map<String, Object> at12 = fieldsOf(runRecord(l, flow, runAsk(l, flow, request(l, 3, 12.5), rules, s), rules, s));
        Map<String, Object> at13 = fieldsOf(runRecord(l, flow, runAsk(l, flow, request(l, 3, 13.5), rules, s), rules, s));
        Set<String> differ = new TreeSet<>();
        for (String k : at12.keySet()) if (!Objects.equals(at12.get(k), at13.get(k))) differ.add(k);
        assertEquals("the price is the only thing that came from the price: the record answers a request for 12.5 and no other", Set.of("priceUnit", "partPriceUnit"), differ);

        // the known limit of a double: 2.50 is the double 2.5, and is recorded as 2.5 (until a decimal type, R6); 5.0 is 5
        assertEquals("2.5", fieldsOf(runRecord(l, flow, runAsk(l, flow, request(l, 3, 2.50), rules, s), rules, s)).get("priceUnit"));
        assertEquals("5", fieldsOf(runRecord(l, flow, runAsk(l, flow, request(l, 3, 5.0), rules, s), rules, s)).get("priceUnit"));
    }

    public void testEachGuardRefusesAMismatchBeforeAnythingIsBuilt() throws Exception {
        generate("run", List.of(LINE_CHANGE, REPRICE, FLOW));
        ClassLoader l = compile("run", SOURCES);
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "RepriceFlow");
        Object rules = rules(l, new Calls());

        // the previous record is of line 4, the request of line 3
        Stood wrongLine = stood(l, "4", "D1");
        Object asked = runAsk(l, flow, request(l, 3, 12.5), rules, wrongLine);
        try {
            runRecord(l, flow, asked, rules, wrongLine);
            fail("a previous record of another line");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("the previous record of the line is of another partLine than the request's line"));
        }
        // the request addresses document D1, the previous record made D2
        Stood s = stood(l, "3", "D1");
        Object addressesD1 = runAsk(l, flow, request(l, 3, 12.5), rules, s);
        Stood madeD2 = stood(l, "3", "D2");
        try {
            runRecord(l, flow, addressesD1, rules, madeD2);
            fail("a previous record that made another document");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("addresses another document than the one the previous record made"));
        }
        // the document has no record that made it: nothing to address
        Stood none = stood(l, "3", null);
        Object addressesNothing = runAsk(l, flow, request(l, 3, 12.5), rules, none);
        assertNull(T36SubtypeTest.get(addressesNothing, "subject0"));
        try {
            runRecord(l, flow, addressesNothing, rules, none);
            fail("a request that addresses no document");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("addresses no document"));
        }
    }

    // ---- T45: spread ------------------------------------------------------------------------------------------------------------

    static final String FLOW_SPREAD = FLOW.replace("\"attributesOther\":\"absent\"", "\"attributesOther\":{\"spread\":\"reason\"}");

    @SuppressWarnings("unchecked")
    public void testASpreadGivesTheMembersOfAJsonTextTheCallerGaveAsTheOtherKeys() throws Exception {
        assertNotSame("the fixture's flow has the spread", FLOW, FLOW_SPREAD);
        generate("spread", List.of(LINE_CHANGE, REPRICE, FLOW_SPREAD));
        ClassLoader l = compile("spread", SOURCES);
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "RepriceFlow");
        Object rules = rules(l, new Calls());
        Stood s = stood(l, "3", "D1");
        Object asked = runAsk(l, flow, request(l, 3, 12.5), rules, s);
        T36SubtypeTest.set(asked, "reason", "{\"colour\":\"red\",\"n\":2,\"nested\":{\"a\":[1]}}");
        Object recorded = runRecord(l, flow, asked, rules, s);
        Map<String, Object> other = (Map<String, Object>) T36SubtypeTest.get(recorded, "attributesOther");
        assertEquals("the members of the object are the other keys", Map.of("colour", "red", "n", 2L, "nested", Map.of("a", List.of(1L))), other);

        // an absent or blank text is no other key; a text that is no object is refused
        T36SubtypeTest.set(asked, "reason", null);
        assertEquals(Map.of(), T36SubtypeTest.get(runRecord(l, flow, asked, rules, s), "attributesOther"));
        T36SubtypeTest.set(asked, "reason", "  ");
        assertEquals(Map.of(), T36SubtypeTest.get(runRecord(l, flow, asked, rules, s), "attributesOther"));
        T36SubtypeTest.set(asked, "reason", "[1,2]");
        try {
            runRecord(l, flow, asked, rules, s);
            fail("an array is no object");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("JSON text is not an object"));
        }

        // said beside the assignment, in the table and in the Turtle's words
        String java = read("spread", "java/" + FLOWS + "RepriceFlow.java");
        assertTrue(java, java.contains("// SPREAD") && java.contains("attributesOther: the members of the JSON object in the user bean's reason: the other keys"));
        assertTrue(java, java.contains("recorded.attributesOther=JsonText.read(asked.reason);"));
    }

    /** T45 in Rust: the same flow with a spread, compiled and run by cargo. */
    public void testASpreadIsInTheRustFlowToo() throws Exception {
        generate("rs_spread", List.of(LINE_CHANGE, REPRICE, FLOW_SPREAD), c -> c.put("rust_dir", OUT + "rs_spread/libs/rust"));
        Path rustDir = Path.of(OUT + "rs_spread/libs/rust");
        String flow = Files.readString(rustDir.resolve("org/example/t36flow/flows/reprice_flow.rs"));
        assertTrue(flow, flow.contains("recorded.attributes_other = JsonText::read(&asked.reason).unwrap();"));
        String[] r = T36Fixture.cargoTestGenerated(rustDir, "t36-rust-spread", "src/test/resources/t36/rust-bean/flow_spread.rs");
        if (r == null) {
            System.out.println("[T36] cargo is not installed: the Rust spread is not compiled and run");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("test result: ok. 3 passed"));
    }

    public void testASpreadIsRefusedWhereItCannotWork() throws Exception {
        refused("a spread into a value that is no map of other keys", f -> obj(f, "record").set("agent", om.createObjectNode().put("spread", "reason")),
                "record.agent.spread fills the map of other keys of a carrier");
        refused("a spread of a value that is not there", f -> obj(f, "record").set("attributesOther", om.createObjectNode().put("spread", "nothing")),
                "record.attributesOther.spread names nothing", "no request value or own value");
        refused("a spread of a number", f -> obj(f, "record").set("attributesOther", om.createObjectNode().put("spread", "unitPrice")),
                "holds no JSON text");
        refused("a spread of a reply value", f -> obj(f, "record").set("attributesOther", om.createObjectNode().put("spread", "outcome")),
                "record.attributesOther.spread names outcome");
    }

    // ---- the kind written beside each assignment, in every language -----------------------------------------------------------

    static final Map<String, String> KINDS = new LinkedHashMap<>();

    static {
        for (String v : List.of("agent", "time", "priceUnit", "partLine", "partPriceUnit")) KINDS.put(v, "AS ASKED");
        for (String v : List.of("document0", "part0")) KINDS.put(v, "CHAINED");
        for (String v : List.of("name1", "state1", "items", "ptype", "partProduct", "partQuantity", "priceBefore", "partPriceBefore", "pattributesOther")) KINDS.put(v, "CARRIED OVER");
        for (String v : List.of("atype", "overwrite", "partPriceSource")) KINDS.put(v, "CONSTANT");
        for (String v : List.of("estimate", "erpId1")) KINDS.put(v, "SUPPLIED");
        KINDS.put("attributesOther", "ABSENT");
    }

    String emitted(String language, String name) {
        for (org.openprovenance.prov.template.compiler.past.Class c : lastProcessor.getCompilerFlow().getGeneratedClasses()) {
            if (c.name.equals(name)) {
                return language.equals("python")
                        ? new org.openprovenance.prov.template.compiler.past.emitter.Python(null).emit(c).toString()
                        : new org.openprovenance.prov.template.compiler.past.emitter.JavaScript().emit(c).toString();
            }
        }
        throw new IllegalStateException("no generated class " + name);
    }

    /** Each assignment into the recorded bean is preceded by a comment that names its kind and the value. */
    void kindsBesideAssignments(String language, String text, String commentMark, String assignMarker) {
        List<String> lines = Arrays.stream(text.split("\n")).map(String::trim).collect(Collectors.toList());
        for (Map.Entry<String, String> e : KINDS.entrySet()) {
            String variable = e.getKey();
            int comment = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(commentMark) && lines.get(i).contains(e.getValue()) && lines.get(i).contains(" " + variable + ":")) comment = i;
            }
            assertTrue(language + ": a comment " + e.getValue() + " for " + variable, comment >= 0);
            if (e.getValue().equals("ABSENT")) continue;   // nothing is assigned
            // the assignment follows, past the null test a copied map has
            boolean found = false;
            for (int i = comment + 1; i < Math.min(lines.size(), comment + 4); i++) {
                if (lines.get(i).replace(" ", "").startsWith(assignMarker + variable + "=")) found = true;
            }
            assertTrue(language + ": the assignment of " + variable + " follows its comment", found);
        }
    }

    public void testTheKindOfEachAssignmentIsWrittenBesideItInEveryLanguage() throws Exception {
        generate("kinds", List.of(LINE_CHANGE, REPRICE, FLOW));
        String java = read("kinds", "java/" + FLOWS + "RepriceFlow.java");
        kindsBesideAssignments("java", java, "//", "recorded.");
        String py = emitted("python", "RepriceFlow");
        kindsBesideAssignments("python", py, "#", "recorded.");
        String js = emitted("javascript", "RepriceFlow");
        kindsBesideAssignments("javascript", js, "//", "recorded.");
        for (String text : List.of(java, py, js)) {
            assertTrue("the conversion goes through the runtime class: " + text, text.contains("JsonText.text("));
            assertTrue("and so does the copy of the open keys", text.contains("JsonText.others("));
        }
        // the six kinds, each once at least, in the ask and answer steps too
        for (String kind : List.of("AS ASKED", "CHAINED", "CARRIED OVER", "CONSTANT", "SUPPLIED", "AS MADE")) {
            assertTrue(kind, java.contains(kind));
        }
        // the emitted Python and JavaScript are well formed
        Path dir = Path.of(OUT + "kinds/emit");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("RepriceFlow.py"), py);
        Files.writeString(dir.resolve("RepriceFlowRules.py"), emitted("python", "RepriceFlowRules"));
        Files.writeString(dir.resolve("RepriceFlow.js"), js);
        Files.writeString(dir.resolve("RepriceFlowRules.js"), emitted("javascript", "RepriceFlowRules"));
        String[] p = T36SubtypeTest.run(List.of("python3", "-I", "-m", "py_compile", dir.resolve("RepriceFlow.py").toString(), dir.resolve("RepriceFlowRules.py").toString()), null);
        if (p == null) System.out.println("[T36] python3 is not installed: the emitted Python is not compiled");
        else assertEquals("py_compile: " + p[1], "0", p[0]);
        for (String f : List.of("RepriceFlow.js", "RepriceFlowRules.js")) {
            String[] n = T36SubtypeTest.run(List.of("node", "--check", dir.resolve(f).toString()), null);
            if (n == null) System.out.println("[T36] node is not installed: the emitted JavaScript is not parsed");
            else assertEquals("node --check " + f + ": " + n[1], "0", n[0]);
        }
        assertTrue("the rules are an interface with a method per supplied value", emitted("python", "RepriceFlowRules").contains("def estimate("));
    }

    // ---- a catalogue without the entry --------------------------------------------------------------------------------------

    public void testACatalogueWithoutAFlowIsAsItWas() throws Exception {
        generate("noflow", List.of(LINE_CHANGE, REPRICE));
        generate("withflow", List.of(LINE_CHANGE, REPRICE, FLOW));
        Map<String, String> without = tree("noflow"), with = tree("withflow");
        Set<String> added = new TreeSet<>(with.keySet());
        added.removeAll(without.keySet());
        assertEquals(Set.of("t36/t36_cli/src/main/java/" + FLOWS + "RepriceFlow.java", "t36/t36_cli/src/main/java/" + FLOWS + "RepriceFlowRules.java"), added);
        assertEquals("nothing removed", Set.of(), without.keySet().stream().filter(k -> !with.containsKey(k)).collect(Collectors.toSet()));
        Set<String> changed = new TreeSet<>();
        for (String k : without.keySet()) if (!with.get(k).equals(without.get(k))) changed.add(k);
        assertEquals("only the documentation says anything of the flow", Set.of("t36/t36_cli/src/main/resources/META-INF/resources/webjars/t36_cli/0.1.0/html/doc.html"), changed);
        assertFalse(without.get(changed.iterator().next()).contains("csv_flow"));
        // a flow that uses no rule has no interface of rules
        assertTrue(Files.exists(Path.of(OUT + "withflow/libs/t36/t36_cli/src/main/java/" + FLOWS + "RepriceFlowRules.java")));
    }

    // ---- the descriptions ---------------------------------------------------------------------------------------------------

    static final String DOC = "t36/t36_cli/src/main/resources/META-INF/resources/webjars/t36_cli/0.1.0/html/doc.html";

    public void testTheDocumentationIsATableOfWhereEachValueComesFrom() throws Exception {
        generate("doc", List.of(LINE_CHANGE, REPRICE, FLOW));
        String doc = Files.readString(Path.of(OUT + "doc/libs/" + DOC));
        assertTrue(doc, doc.contains("class='csv_flow'") && doc.contains("Flow org.example.t36flow.flows.RepriceFlow"));
        assertTrue(doc, doc.contains("from t36.Reprice to t36.LineChange"));
        for (String row : List.of(
                "<tr><td>record</td><td>priceUnit</td><td class='csv_kind'>asked</td><td>the user bean's unitPrice, as text</td></tr>",
                "<tr><td>record</td><td>document0</td><td class='csv_kind'>chained</td><td>the document as it stood: document1 of the record that made it</td></tr>",
                "<tr><td>record</td><td>priceBefore</td><td class='csv_kind'>carried over</td><td>the line's partPriceUnit, unchanged</td></tr>",
                "<tr><td>record</td><td>atype</td><td class='csv_kind'>constant</td><td>the constant Repricing</td></tr>",
                "<tr><td>record</td><td>estimate</td><td class='csv_kind'>supplied</td><td>the application's rule estimate</td></tr>",
                "<tr><td>record</td><td>attributesOther</td><td class='csv_kind'>absent</td><td>left unset</td></tr>",
                "<tr><td>ask</td><td>line</td><td class='csv_kind'>asked</td><td>the request's line</td></tr>",
                "<tr><td>ask</td><td>subject0</td><td class='csv_kind'>chained</td><td>the document as it stood: document1 of the record that made it</td></tr>",
                "<tr><td>answer</td><td>outcome</td><td class='csv_kind'>as made</td><td>accepted when the store made document1, otherwise failed</td></tr>",
                "<tr><td>answer</td><td>result</td><td class='csv_kind'>as made</td><td>the backend's erpId1 as recorded</td></tr>",
                "<tr><td>refused</td><td>code</td><td class='csv_kind'>supplied</td><td>an argument of the method</td></tr>")) {
            assertTrue(row + " in the page", doc.contains(row));
        }
        // one row for each assignment the code makes
        int rows = doc.split("<tr><td>", -1).length - 1;
        int items = lastProcessor.getCompilerFlow().getModels().get(0).steps.values().stream().mapToInt(List::size).sum();
        assertEquals(items, rows);
    }

    static final String NS = "http://example.org/t36#";

    public void testTheTurtleSaysWhereEachValueComesFrom() throws Exception {
        generate("ttl", List.of(LINE_CHANGE, REPRICE, FLOW), c -> {
            c.put("turtle", "catalogue.ttl");
            c.put("turtleNamespace", NS);
        });
        String ttl = read("ttl", "resources/catalogue.ttl");
        assertTrue(ttl, ttl.contains(":reprice_flow a tpl:Flow ;") && ttl.contains("tpl:from :reprice ;") && ttl.contains("tpl:to :line_change ;"));
        assertTrue(ttl, ttl.contains("tpl:origin [ tpl:step \"record\" ; tpl:target \"priceUnit\" ; tpl:kind \"asked\" ; tpl:source \"the user bean's unitPrice, as text\" ]"));
        assertTrue(ttl, ttl.contains("tpl:Flow a owl:Class") && ttl.contains("tpl:origin a owl:ObjectProperty"));
        int items = lastProcessor.getCompilerFlow().getModels().get(0).steps.values().stream().mapToInt(List::size).sum();
        String[] r = T36SubtypeTest.run(List.of("python3", "-I", "-c",
                "import sys\nfrom rdflib import Graph, Namespace, RDF\ng=Graph(); g.parse(sys.argv[1], format='turtle')\nT=Namespace('http://openprovenance.org/ns/tpl#'); N=Namespace(sys.argv[2])\n"
                        + "f=N.reprice_flow\nok=(f,RDF.type,T.Flow) in g and (f,T['from'],N.reprice) in g and (f,T.to,N.line_change) in g\n"
                        + "origins=list(g.objects(f,T.origin))\nkinds={str(g.value(o,T.target))+'@'+str(g.value(o,T.step)):str(g.value(o,T.kind)) for o in origins}\n"
                        + "print('FAIL' if not ok or len(origins)!=int(sys.argv[3]) or kinds['priceUnit@record']!='asked' or kinds['document0@record']!='chained' or kinds['code@refused']!='supplied' else 'ok')\n",
                OUT + "ttl/libs/t36/t36_cli/src/main/resources/catalogue.ttl", NS, String.valueOf(items)), new File("."));
        if (r == null || r[1].contains("No module named")) {
            System.out.println("[T36] python3 or rdflib is not installed: the Turtle file is not parsed");
        } else {
            assertEquals(r[1], "0", r[0]);
            assertFalse("a wrong answer: " + r[1], r[1].contains("FAIL"));
        }
        // without a flow the vocabulary has none of its terms
        generate("ttl0", List.of(LINE_CHANGE, REPRICE), c -> {
            c.put("turtle", "catalogue.ttl");
            c.put("turtleNamespace", NS);
        });
        String without = read("ttl0", "resources/catalogue.ttl");
        assertFalse(without.contains("Flow") || without.contains("tpl:origin"));
    }

    // ---- the refusals (design §13.4) ----------------------------------------------------------------------------------------

    void refused(String title, Consumer<ObjectNode> edit, String... contains) throws Exception {
        ObjectNode flow = (ObjectNode) om.readTree(FLOW);
        edit.accept(flow);
        ObjectNode c = catalogue("refusal", List.of(LINE_CHANGE, REPRICE, om.writeValueAsString(flow)));
        TemplatesProjectConfiguration configs = ConfigProcessor.objectMapper.readValue(om.writeValueAsString(c), TemplatesProjectConfiguration.class);
        try {
            new ConfigProcessor(pf).checkSubtypes(configs, ".");
            fail(title + ": accepted");
        } catch (FlowException e) {
            assertTrue(title + ": " + e.getMessage(), e.getMessage().startsWith("T36: flow org.example.t36flow.flows.RepriceFlow: "));
            for (String m : contains) assertTrue(title + ": " + m + " in " + e.getMessage(), e.getMessage().contains(m));
        }
    }

    static ObjectNode obj(ObjectNode flow, String member) {
        return (ObjectNode) flow.get(member);
    }

    public void testTheFlowThatIsDeclaredIsAccepted() throws Exception {
        ObjectNode c = catalogue("accepted", List.of(LINE_CHANGE, REPRICE, FLOW));
        new ConfigProcessor(pf).checkSubtypes(ConfigProcessor.objectMapper.readValue(om.writeValueAsString(c), TemplatesProjectConfiguration.class), ".");
    }

    public void testAnInputLeftOutOrNamedTwiceIsRefused() throws Exception {
        // without "rest", an input left out is refused; with it, the input is carried over from the thing that has it (what "rest" says)
        refused("an input left out", f -> {
            obj(f, "carry").set("line", om.createArrayNode().add("ptype").add("partProduct").add("partQuantity"));
            obj(f, "record").remove("estimate");
        }, "inputs [estimate] are not accounted for");
        refused("an input left out, but not carried by rest", f -> {
            obj(f, "carry").set("line", om.createArrayNode().add("ptype").add("partProduct").add("partQuantity"));
            obj(f, "record").remove("erpId1");
        }, "inputs [erpId1] are not accounted for");
        refused("a value named twice", f -> obj(f, "record").put("items", "absent"), "items is accounted for twice", "record.items", "carry.document");
        refused("a node named twice", f -> obj(f, "record").set("document0", om.createObjectNode().put("constant", "x")), "document0 is accounted for twice", "stood.document.node");
        refused("the same value from two places", f -> {
            ((ArrayNode) obj(f, "carry").get("document")).add("ptype");
            obj(f, "record").put("ptype", "absent");
        }, "ptype is accounted for twice");
        refused("an input that is no input", f -> obj(f, "record").put("nosuch", "absent"), "record.nosuch is no input of t36.LineChange");
        refused("the open keys of a carrier left out", f -> obj(f, "carryOpen").removeAll(), "pattributesOther", "not accounted for");
        refused("the open keys of a carrier that is no carrier", f -> obj(f, "carryOpen").put("nocarrier", "line"), "nocarrier");
        refused("rest for two things", f -> obj(f, "carry").put("document", "rest"), "\"rest\" is used for two stood things");
        refused("a carry from nothing", f -> obj(f, "carry").set("nothing", om.createArrayNode().add("items")), "carry.nothing names no stood thing");
        refused("an own value of the user bean left out", f -> obj(f, "ask").remove("time"), "own values [time] are not accounted for");
        refused("a request value in ask", f -> obj(f, "ask").set("line", om.createObjectNode().put("constant", 1)), "ask.line is no own value");
        refused("a reply value left out", f -> obj((ObjectNode) obj(f, "reply"), "answer").remove("code"), "reply.answer does not account for the reply values [code]");
    }

    public void testWhatIsGivenWrongIsRefused() throws Exception {
        refused("absent for a compulsory value", f -> obj(f, "record").put("agent", "absent"), "agent is compulsory");
        refused("a conversion the compiler does not have", f -> ((ObjectNode) obj(f, "record").get("partLine")).put("as", "int"), "cannot be given");
        refused("a number given as text without saying so", f -> obj(f, "record").set("priceUnit", om.createObjectNode().put("asked", "unitPrice")), "say \"as\": \"text\"");
        refused("asked of a value the user bean has not", f -> obj(f, "record").set("priceUnit", om.createObjectNode().put("asked", "nothing")), "no request value or own value");
        refused("asked of a reply value", f -> obj(f, "record").set("priceUnit", om.createObjectNode().put("asked", "outcome")), "no request value or own value");
        refused("a rule used for two values", f -> obj(f, "record").set("erpId1", om.createObjectNode().put("supplied", "estimate")), "the rule estimate is already the rule");
        refused("a constant of the wrong type", f -> obj(f, "record").set("overwrite", om.createObjectNode().put("constant", 3)), "the constant 3");
        refused("a node's constant with a prefix", f -> obj(f, "record").set("atype", om.createObjectNode().put("constant", "ex:Repricing")), "local part", "without prefix");
        refused("carried from a value the record has not", f -> obj(f, "record").set("priceBefore", om.createObjectNode().put("carried", "line.nosuch")), "does not exist");
        refused("carried from a thing that did not stand", f -> obj(f, "record").set("priceBefore", om.createObjectNode().put("carried", "nothing.partPriceUnit")), "no stood thing");
        refused("a malformed form", f -> obj(f, "record").set("priceBefore", om.createObjectNode().put("carried", "x").put("constant", "y")), "record.priceBefore is");
    }

    public void testWhatStoodIsRefusedWhenItIsNoNodeOrNoOutput() throws Exception {
        refused("madeBy is no output", f -> ((ObjectNode) obj(f, "stood").get("document")).put("madeBy", "name1"), "madeBy is name1", "no output node");
        refused("node is no node", f -> ((ObjectNode) obj(f, "stood").get("line")).put("node", "time"), "no node input");
        refused("addressed is no own node", f -> ((ObjectNode) obj(f, "stood").get("document")).put("addressed", "unitPrice"), "no node among the user bean's own values");
        refused("identity of no request value", f -> ((ObjectNode) ((ObjectNode) obj(f, "stood").get("line")).get("identity")).put("user", "agent"), "no request value");
        refused("a member that is not one", f -> ((ObjectNode) obj(f, "stood").get("line")).put("whatever", "x"), "unknown member whatever");
        refused("from is no subtype", f -> f.put("from", "t36.Nope"), "from names t36.Nope", "no subtype");
        refused("to is a template, not a subtype", f -> f.put("to", "t36.Changing"), "to names t36.Changing");
        refused("a stood thing with no node", f -> obj(f, "stood").set("x", om.createObjectNode()), "stood.x: node and madeBy are required");
    }

    public void testTheReplyIsGivenOnlyFromWhatWasMade() throws Exception {
        refused("a refused reply that says what the store made", f -> obj((ObjectNode) obj(f, "reply"), "refused").set("result",
                        om.readTree("{\"when\":{\"made\":\"document1\"},\"is\":\"a\",\"otherwise\":\"b\"}")),
                "nothing was recorded or made in a refused reply");
        refused("a reply value that is none", f -> obj((ObjectNode) obj(f, "reply"), "answer").set("line", om.createObjectNode().put("constant", "x")), "is no reply value");
        refused("an output that is none", f -> obj((ObjectNode) obj(f, "reply"), "answer").set("result", om.createObjectNode().put("made", "nothing")), "no output of");
        refused("an outcome left out of a failed reply", f -> obj((ObjectNode) obj(f, "reply"), "failed").put("outcome", "absent"), "outcome is compulsory");
        refused("a reply block that is none", f -> obj(f, "reply").set("accepted", om.createObjectNode()), "is not one of");
    }

    // ---- NS: nothing stood (T41, design §14.1) ----------------------------------------------------------------------------------

    static final List<String> NS_SOURCES = List.of(FLOWS + "CreateFlow.java", FLOWS + "CreateFlowRules.java",
            "org/example/t36flow/backend/client/integrator/Line_changeInputs.java", "org/example/t36flow/backend/client/integrator/ChangingOutputs.java",
            "org/example/t36flow/actions/client/integrator/RepriceInputs.java", "org/example/t36flow/actions/client/integrator/RepriceRequest.java",
            "org/example/t36flow/actions/client/integrator/RepriceReply.java");

    /** A stand-in for the rules of a flow, by the interface's name: each method answers from the map; what it was called with is kept. */
    static Object rulesOf(ClassLoader l, String name, Map<String, Object> answers, Calls calls) throws Exception {
        Class<?> rules = l.loadClass(FLOWS_PKG + name);
        return Proxy.newProxyInstance(l, new Class<?>[]{rules}, (proxy, method, args) -> {
            calls.names.add(method.getName());
            calls.arguments.put(method.getName(), args);
            if (!answers.containsKey(method.getName())) throw new IllegalStateException("no answer for the rule " + method.getName());
            return answers.get(method.getName());
        });
    }

    public void testAFlowThatStandsOnNothingTakesItsNodesFromTheApplication() throws Exception {
        generate("ns", List.of(LINE_CHANGE, REPRICE, FLOW_NS));
        ClassLoader l = compile("ns", NS_SOURCES);
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "CreateFlow");
        // no stood thing: ask takes the request and the rules, record the user bean and the rules, and nothing of a previous record
        assertEquals(2, flow.getMethod("ask", l.loadClass(ACTIONS_PKG + "RepriceRequest"), l.loadClass(FLOWS_PKG + "CreateFlowRules")).getParameterCount());
        assertEquals(2, flow.getMethod("record", l.loadClass(ACTIONS_PKG + "RepriceInputs"), l.loadClass(FLOWS_PKG + "CreateFlowRules")).getParameterCount());

        Map<String, Object> answers = new LinkedHashMap<>();
        answers.put("caller", "agent-7");
        answers.put("clock", "2026-11-16T10:00:00Z");
        answers.put("documentNode", "D-new");
        answers.put("lineNode", "P-new");
        answers.put("product", "bolt");
        answers.put("version", "v1");
        answers.put("count", "1");
        Calls calls = new Calls();
        Object rules = rulesOf(l, "CreateFlowRules", answers, calls);
        Object asked = T36SubtypeTest.invoke(null, flow, "ask", request(l, 3, 12.5), rules);
        assertEquals("SUPPLIED: the node the request addresses is the application's", "D-new", T36SubtypeTest.get(asked, "subject0"));
        assertEquals("agent-7", T36SubtypeTest.get(asked, "agent"));
        Object recorded = T36SubtypeTest.invoke(null, flow, "record", asked, rules);
        Map<String, Object> expected = new TreeMap<>();
        expected.put("agent", "agent-7");
        expected.put("time", "2026-11-16T10:00:00Z");
        expected.put("document0", "D-new");          // AS ASKED: the user bean's node
        expected.put("part0", "P-new");              // SUPPLIED
        expected.put("atype", "Creating");           // CONSTANT
        expected.put("partLine", "3");
        expected.put("priceUnit", "12.5");
        expected.put("partPriceUnit", "12.5");
        expected.put("partProduct", "bolt");
        expected.put("partQuantity", "1");
        expected.put("overwrite", "false");
        expected.put("partPriceSource", "catalogue");
        expected.put("erpId1", "v1");
        expected.put("items", "1");
        Map<String, Object> got = fieldsOf(recorded);
        assertEquals(Map.of(), got.remove("attributesOther"));
        assertNull("absent", got.remove("pattributesOther") == null ? null : ((Map<?, ?>) got.get("pattributesOther")));
        for (Map.Entry<String, Object> e : got.entrySet()) assertEquals(e.getKey(), expected.get(e.getKey()), e.getValue());
        assertEquals(expected.keySet(), got.entrySet().stream().filter(e -> e.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toSet()));
        // the reply is given as before
        Object made = make(l, BACKEND_PKG + "ChangingOutputs", "document1", "D2");
        assertEquals("accepted", T36SubtypeTest.get(T36SubtypeTest.invoke(null, flow, "answer", asked, recorded, made), "outcome"));
    }

    public void testWhatIsLeftToTheApplicationWhenNothingStoodIsSaidInTheTable() throws Exception {
        generate("nsdoc", List.of(LINE_CHANGE, REPRICE, FLOW_NS));
        String doc = Files.readString(Path.of(OUT + "nsdoc/libs/" + DOC));
        assertTrue(doc, doc.contains("<tr><td>ask</td><td>subject0</td><td class='csv_kind'>supplied</td><td>the application's rule documentNode</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record</td><td>part0</td><td class='csv_kind'>supplied</td><td>the application's rule lineNode</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record</td><td>document0</td><td class='csv_kind'>asked</td><td>the user bean's subject0</td></tr>"));
        assertFalse("nothing is chained", doc.contains("<td class='csv_kind'>chained</td>"));
    }

    public void testNothingStoodStillAccountsForEveryInputExactlyOnce() throws Exception {
        refusedNs("a node left out", f -> obj(f, "record").remove("part0"), "inputs [part0] are not accounted for");
        refusedNs("a compulsory node left absent", f -> obj(f, "record").put("part0", "absent"), "part0 is compulsory");
        refusedNs("nothing to carry from", f -> obj(f, "carry").put("line", "rest"), "carry.line names no stood thing");
        refusedNs("an own node of the user bean left out", f -> obj(f, "ask").remove("subject0"), "own values [subject0] are not accounted for");
    }

    void refusedNs(String title, Consumer<ObjectNode> edit, String... contains) throws Exception {
        ObjectNode flow = (ObjectNode) om.readTree(FLOW_NS);
        flow.set("carry", om.createObjectNode());
        edit.accept(flow);
        ObjectNode c = catalogue("refusal", List.of(LINE_CHANGE, REPRICE, om.writeValueAsString(flow)));
        TemplatesProjectConfiguration configs = ConfigProcessor.objectMapper.readValue(om.writeValueAsString(c), TemplatesProjectConfiguration.class);
        try {
            new ConfigProcessor(pf).checkSubtypes(configs, ".");
            fail(title + ": accepted");
        } catch (FlowException e) {
            for (String m : contains) assertTrue(title + ": " + m + " in " + e.getMessage(), e.getMessage().contains(m));
        }
    }

    // ---- XT: a stood record made by another template (T41, design §14.2) ---------------------------------------------------------

    static List<String> xtSources() {
        return List.of(FLOWS + "SendFlow.java", FLOWS + "SendFlowRules.java",
                "org/example/t36flow/backend/client/integrator/SendingInputs.java", "org/example/t36flow/backend/client/integrator/ExchangingOutputs.java",
                "org/example/t36flow/backend/client/integrator/Line_changeInputs.java", "org/example/t36flow/backend/client/integrator/ChangingInputs.java",
                "org/example/t36flow/backend/client/integrator/ChangingOutputs.java",
                "org/example/t36flow/actions/client/integrator/Ask_sendInputs.java", "org/example/t36flow/actions/client/integrator/Ask_sendRequest.java",
                "org/example/t36flow/actions/client/integrator/Ask_sendReply.java");
    }

    static String xt(String of) {
        return FLOW_XT.replace("\"OF\"", "\"" + of + "\"");
    }

    /** Runs the sending of a document: the previous record is of the type the flow names, and made the document D1. */
    void sendsADocument(String id, String of, String previousClass) throws Exception {
        generate(id, List.of(LINE_CHANGE, ASK_SEND, SENDING, xt(of)));
        List<String> sources = new ArrayList<>(xtSources());
        ClassLoader l = compile(id, sources);
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "SendFlow");
        // the previous record is typed by its own template, not by the one made: its inputs and its outputs
        java.lang.reflect.Method record = null;
        for (java.lang.reflect.Method m : flow.getMethods()) if (m.getName().equals("record")) record = m;
        assertEquals(BACKEND_PKG + previousClass, record.getParameterTypes()[1].getName());
        assertEquals("what the store made of the previous record is the outputs bean of ITS template", BACKEND_PKG + "ChangingOutputs", record.getParameterTypes()[2].getName());
        assertEquals("the record made is of the template the flow ends in", BACKEND_PKG + "SendingInputs", record.getReturnType().getName());

        Map<String, Object> answers = new LinkedHashMap<>();
        answers.put("caller", "agent-7");
        answers.put("clock", "2026-11-16T10:00:00Z");
        Calls calls = new Calls();
        Object rules = rulesOf(l, "SendFlowRules", answers, calls);
        Object previous = make(l, BACKEND_PKG + previousClass, "name1", "RFQ-1");
        Object previousMade = make(l, BACKEND_PKG + "ChangingOutputs", "document1", "D1");
        Object request = make(l, ACTIONS_PKG + "Ask_sendRequest", "vendor", "acme", "reference", "RFQ-1");
        Object asked = T36SubtypeTest.invoke(null, flow, "ask", request, rules, previousMade);
        assertEquals("CHAINED from the other template's outputs", "D1", T36SubtypeTest.get(asked, "subject0"));
        Object recorded = T36SubtypeTest.invoke(null, flow, "record", asked, previous, previousMade);   // no rule is used in this record
        Map<String, Object> expected = new TreeMap<>();
        expected.put("agent", "agent-7");
        expected.put("time", "2026-11-16T10:00:00Z");
        expected.put("document0", "D1");     // CHAINED
        expected.put("atype", "Sending");    // CONSTANT
        expected.put("vendor", "acme");      // AS ASKED
        expected.put("name1", "RFQ-1");      // CARRIED OVER from the other template's bean
        Map<String, Object> got = fieldsOf(recorded);
        assertEquals(Map.of(), got.remove("attributesOther"));
        for (Map.Entry<String, Object> e : got.entrySet()) assertEquals(e.getKey(), expected.get(e.getKey()), e.getValue());
        assertEquals(expected.keySet(), got.entrySet().stream().filter(e -> e.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toSet()));

        // the guards read the other template's bean and outputs
        try {
            T36SubtypeTest.invoke(null, flow, "record", asked, make(l, BACKEND_PKG + previousClass, "name1", "RFQ-2"), previousMade);
            fail("a previous record of another reference");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("the previous record of the document is of another name1 than the request's reference"));
        }
        try {
            T36SubtypeTest.invoke(null, flow, "record", asked, previous, make(l, BACKEND_PKG + "ChangingOutputs", "document1", "D2"));
            fail("a document the previous record did not make");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("addresses another document than the one the previous record made"));
        }
        // the reply, from the outputs of the template the flow ends in
        Object made = make(l, BACKEND_PKG + "ExchangingOutputs", "document1", "D2");
        Object reply = T36SubtypeTest.invoke(null, flow, "answer", asked, recorded, made);
        assertEquals("accepted", T36SubtypeTest.get(reply, "outcome"));
        assertEquals("RFQ-1", T36SubtypeTest.get(reply, "result"));
    }

    public void testAStoodRecordMadeByAnotherTemplateIsTypedByItsOwnSubtype() throws Exception {
        sendsADocument("xt", "t36.LineChange", "Line_changeInputs");
    }

    public void testAStoodRecordMayBeOfATemplateThatHasNoSubtype() throws Exception {
        sendsADocument("xt2", "t36.Changing", "ChangingInputs");
    }

    public void testTheTableSaysWhereTheOtherTemplatesValuesCameFrom() throws Exception {
        generate("xtdoc", List.of(LINE_CHANGE, ASK_SEND, SENDING, xt("t36.LineChange")));
        String doc = Files.readString(Path.of(OUT + "xtdoc/libs/" + DOC));
        assertTrue(doc, doc.contains("<tr><td>record</td><td>document0</td><td class='csv_kind'>chained</td><td>the document as it stood: document1 of the record that made it</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record</td><td>name1</td><td class='csv_kind'>carried over</td><td>the document's name1, unchanged</td></tr>"));
    }

    void refusedXt(String title, Consumer<ObjectNode> edit, String... contains) throws Exception {
        ObjectNode flow = (ObjectNode) om.readTree(xt("t36.LineChange"));
        edit.accept(flow);
        ObjectNode c = catalogue("refusal", List.of(LINE_CHANGE, ASK_SEND, SENDING, om.writeValueAsString(flow)));
        TemplatesProjectConfiguration configs = ConfigProcessor.objectMapper.readValue(om.writeValueAsString(c), TemplatesProjectConfiguration.class);
        try {
            new ConfigProcessor(pf).checkSubtypes(configs, ".");
            fail(title + ": accepted");
        } catch (FlowException e) {
            for (String m : contains) assertTrue(title + ": " + m + " in " + e.getMessage(), e.getMessage().contains(m));
        }
    }

    public void testWhatAStoodRecordOfAnotherTemplateMustHave() throws Exception {
        refusedXt("of names nothing", f -> ((ObjectNode) obj(f, "stood").get("document")).put("of", "t36.Nope"), "stood.document.of names t36.Nope", "no subtype or template");
        refusedXt("madeBy is no output of THAT template", f -> ((ObjectNode) obj(f, "stood").get("document")).put("madeBy", "exchanging"),
                "madeBy is exchanging", "no output node of t36.Changing");
        refusedXt("identity of a value the other template's bean has not", f -> ((ObjectNode) ((ObjectNode) obj(f, "stood").get("document")).get("identity")).put("backend", "vendor"),
                "identity.backend is vendor");
        refusedXt("a value to carry that the other template's bean has not", f -> {
            obj(f, "record").remove("vendor");
            obj(f, "carry").set("document", om.createArrayNode().add("vendor"));
        },
                "the document's vendor does not exist", "t36.LineChange");
    }

    // ---- MR: several records in one flow (T41, design §14.3) ---------------------------------------------------------------------

    static final List<String> MR_SOURCES = List.of(FLOWS + "AddFlow.java", FLOWS + "AddFlowRules.java",
            "org/example/t36flow/backend/client/integrator/Line_registeringInputs.java", "org/example/t36flow/backend/client/integrator/RegisteringOutputs.java",
            "org/example/t36flow/backend/client/integrator/Line_changeInputs.java", "org/example/t36flow/backend/client/integrator/ChangingOutputs.java",
            "org/example/t36flow/actions/client/integrator/RepriceInputs.java", "org/example/t36flow/actions/client/integrator/RepriceRequest.java",
            "org/example/t36flow/actions/client/integrator/RepriceReply.java");

    public void testALaterRecordChainsFromTheOutputsOfAnEarlierOne() throws Exception {
        generate("mr", List.of(LINE_CHANGE, LINE_REGISTERING, REPRICE, FLOW_MR));
        ClassLoader l = compile("mr", MR_SOURCES);
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "AddFlow");
        // one method for each record, in the order the flow declares them, then the reply
        List<String> names = Arrays.stream(flow.getMethods()).filter(m -> java.lang.reflect.Modifier.isStatic(m.getModifiers())).map(java.lang.reflect.Method::getName).sorted().collect(Collectors.toList());
        assertEquals(List.of("answer", "ask", "failed", "recordChange", "recordRegister", "refused"), names);
        java.lang.reflect.Method change = null;
        for (java.lang.reflect.Method m : flow.getMethods()) if (m.getName().equals("recordChange")) change = m;
        // the second record stands on the document's previous record and on the FIRST record's bean and outputs
        assertEquals(List.of(ACTIONS_PKG + "RepriceInputs", BACKEND_PKG + "Line_changeInputs", BACKEND_PKG + "ChangingOutputs",
                BACKEND_PKG + "Line_registeringInputs", BACKEND_PKG + "RegisteringOutputs", FLOWS_PKG + "AddFlowRules"),
                Arrays.stream(change.getParameterTypes()).map(Class::getName).collect(Collectors.toList()));

        Map<String, Object> answers = new LinkedHashMap<>();
        answers.put("caller", "agent-7");
        answers.put("clock", "2026-11-16T10:00:00Z");
        answers.put("product", "bolt");
        answers.put("estimate", "est");
        answers.put("version", "v3");
        answers.put("count", "4");
        Calls calls = new Calls();
        Object rules = rulesOf(l, "AddFlowRules", answers, calls);
        Stood s = stood(l, "3", "D1");
        Object asked = T36SubtypeTest.invoke(null, flow, "ask", request(l, 3, 12.5), rules, s.documentMade);

        // 1. the first record: a line registered
        Object registered = T36SubtypeTest.invoke(null, flow, "recordRegister", asked, rules);
        Map<String, Object> got1 = fieldsOf(registered);
        Map<String, Object> expected1 = new TreeMap<>();
        expected1.put("agent", "agent-7");
        expected1.put("time", "2026-11-16T10:00:00Z");
        expected1.put("atype", "Registering");
        expected1.put("partLine", "3");
        expected1.put("partPriceUnit", "12.5");
        expected1.put("partProduct", "bolt");
        expected1.put("partQuantity", "1");
        expected1.put("partPriceSource", "catalogue");
        assertEquals(null, got1.remove("pattributesOther") == null ? null : null);
        for (Map.Entry<String, Object> e : got1.entrySet()) assertEquals(e.getKey(), expected1.get(e.getKey()), e.getValue());
        assertEquals(expected1.keySet(), got1.entrySet().stream().filter(e -> e.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toSet()));

        // the store is the application's, between the calls: it made the line P9
        Object registeredMade = make(l, BACKEND_PKG + "RegisteringOutputs", "part1", "P9");

        // 2. the second record chains from the first's outputs and carries its values
        Object changed = T36SubtypeTest.invoke(null, flow, "recordChange", asked, s.document, s.documentMade, registered, registeredMade, rules);
        Map<String, Object> got2 = fieldsOf(changed);
        Map<String, Object> expected2 = new TreeMap<>();
        expected2.put("agent", "agent-7");
        expected2.put("time", "2026-11-16T10:00:00Z");
        expected2.put("document0", "D1");                // CHAINED from a previous request's record
        expected2.put("part0", "P9");                    // CHAINED from the first record of this flow
        expected2.put("atype", "Adding");
        expected2.put("priceUnit", "12.5");
        expected2.put("overwrite", "false");
        expected2.put("estimate", "est");
        expected2.put("erpId1", "v3");
        expected2.put("items", "4");
        expected2.put("partLine", "3");                  // CARRIED OVER from the first record
        expected2.put("partPriceUnit", "12.5");
        expected2.put("partProduct", "bolt");
        expected2.put("partQuantity", "1");
        expected2.put("partPriceSource", "catalogue");
        expected2.put("name1", "RFQ-1");                 // CARRIED OVER from the document's previous record
        expected2.put("state1", "open");
        got2.remove("pattributesOther");
        assertEquals(Map.of(), got2.remove("attributesOther"));
        for (Map.Entry<String, Object> e : got2.entrySet()) assertEquals(e.getKey(), expected2.get(e.getKey()), e.getValue());
        assertEquals(expected2.keySet(), got2.entrySet().stream().filter(e -> e.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toSet()));

        // the rule of the second record sees the first record's bean and outputs
        Object[] estimateArgs = calls.arguments.get("estimate");
        assertSame(asked, estimateArgs[0]);
        assertSame(changed, estimateArgs[1]);
        assertSame("the first record's bean", registered, estimateArgs[4]);
        assertSame("and what the store made of it", registeredMade, estimateArgs[5]);

        // one reply at the end: the outcome from what the store made of the LAST record, the result from its bean
        Object madeChange = make(l, BACKEND_PKG + "ChangingOutputs", "document1", "D2");
        Object reply = T36SubtypeTest.invoke(null, flow, "answer", asked, registered, registeredMade, changed, madeChange);
        assertEquals("accepted", T36SubtypeTest.get(reply, "outcome"));
        assertEquals("v3", T36SubtypeTest.get(reply, "result"));
        Object failedReply = T36SubtypeTest.invoke(null, flow, "answer", asked, registered, registeredMade, changed, make(l, BACKEND_PKG + "ChangingOutputs"));
        assertEquals("failed", T36SubtypeTest.get(failedReply, "outcome"));
        assertEquals("NO_CHANGE", T36SubtypeTest.get(T36SubtypeTest.invoke(null, flow, "refused", make(l, ACTIONS_PKG + "RepriceInputs"), "NO_CHANGE"), "code"));
    }

    public void testTheKindsAndTheStepAreWrittenForEachRecord() throws Exception {
        generate("mrdoc", List.of(LINE_CHANGE, LINE_REGISTERING, REPRICE, FLOW_MR));
        String doc = Files.readString(Path.of(OUT + "mrdoc/libs/" + DOC));
        assertTrue(doc, doc.contains("from t36.Reprice to t36.LineRegistering, t36.LineChange"));
        assertTrue(doc, doc.contains("<tr><td>record register</td><td>partLine</td><td class='csv_kind'>asked</td><td>the user bean's line, as text</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record change</td><td>part0</td><td class='csv_kind'>chained</td><td>the line as it stood: part1 of the record that made it</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record change</td><td>partLine</td><td class='csv_kind'>carried over</td><td>the line's partLine, unchanged</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>answer</td><td>outcome</td><td class='csv_kind'>as made</td><td>accepted when the store made change.document1, otherwise failed</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>answer</td><td>result</td><td class='csv_kind'>as made</td><td>the backend's change.erpId1 as recorded</td></tr>"));
        String java = read("mrdoc", "java/" + FLOWS + "AddFlow.java");
        assertTrue(java, java.contains("// CHAINED       part0: the line as it stood: part1 of the record that made it;") && java.contains("recorded.part0=lineMade.part1;"));
        generate("mrttl", List.of(LINE_CHANGE, LINE_REGISTERING, REPRICE, FLOW_MR), c -> {
            c.put("turtle", "catalogue.ttl");
            c.put("turtleNamespace", NS);
        });
        String ttl = read("mrttl", "resources/catalogue.ttl");
        assertTrue(ttl, ttl.contains("tpl:to :line_registering ;") && ttl.contains("tpl:to :line_change ;") && ttl.contains("tpl:step \"record register\""));
    }

    void refusedMr(String title, Consumer<ObjectNode> edit, String... contains) throws Exception {
        ObjectNode flow = (ObjectNode) om.readTree(FLOW_MR);
        edit.accept(flow);
        ObjectNode c = catalogue("refusal", List.of(LINE_CHANGE, LINE_REGISTERING, REPRICE, om.writeValueAsString(flow)));
        TemplatesProjectConfiguration configs = ConfigProcessor.objectMapper.readValue(om.writeValueAsString(c), TemplatesProjectConfiguration.class);
        try {
            new ConfigProcessor(pf).checkSubtypes(configs, ".");
            fail(title + ": accepted");
        } catch (FlowException e) {
            for (String m : contains) assertTrue(title + ": " + m + " in " + e.getMessage(), e.getMessage().contains(m));
        }
    }

    static ObjectNode rec(ObjectNode flow, int i) {
        return (ObjectNode) flow.get("records").get(i);
    }

    public void testEveryInputOfEveryRecordIsAccountedForExactlyOnce() throws Exception {
        refusedMr("an input of the first record left out", f -> obj(rec(f, 0), "record").remove("partProduct"), "inputs [partProduct] are not accounted for");
        refusedMr("an input of the second record left out", f -> obj(rec(f, 1), "record").remove("erpId1"), "inputs [erpId1] are not accounted for");
        refusedMr("a value named twice in the second record", f -> obj(rec(f, 1), "record").set("partQuantity", om.readTree("{\"constant\":\"2\"}")), "partQuantity is accounted for twice");
        refusedMr("the open keys of the second record left out", f -> obj(rec(f, 1), "carryOpen").removeAll(), "pattributesOther", "not accounted for");
    }

    public void testWhatARecordMayStandOn() throws Exception {
        refusedMr("a record that is declared twice", f -> rec(f, 1).put("name", "register"), "the record register is declared twice");
        refusedMr("an earlier record that does not exist", f -> ((ObjectNode) obj(rec(f, 1), "stood").get("line")).put("record", "nothing"), "stood.line.record is nothing", "no record before change");
        refusedMr("the first record chains from a later one", f -> {
            ObjectNode line = (ObjectNode) obj(rec(f, 1), "stood").remove("line");
            ObjectNode stood = om.createObjectNode();
            stood.set("later", line.put("node", "part0"));
            rec(f, 0).set("stood", stood);
        }, "stood.later.record is register", "no record before register");
        refusedMr("a record of the flow with an address to check", f -> ((ObjectNode) obj(rec(f, 1), "stood").get("line")).put("addressed", "subject0"),
                "nothing the request could differ from");
        refusedMr("the same stood name in two records", f -> rec(f, 0).set("stood", om.readTree("{\"document\":{\"node\":\"atype\",\"madeBy\":\"part1\"}}")), "named twice in the flow");
        refusedMr("both a record and a template", f -> ((ObjectNode) obj(rec(f, 1), "stood").get("line")).put("of", "t36.Changing"), "has both record and of");
        refusedMr("records and the shorthand together", f -> f.put("to", "t36.LineChange"), "records, and also to, stood, record, carry or carryOpen");
        refusedMr("a record with no name", f -> rec(f, 0).remove("name"), "records[0].name is required");
    }

    public void testAReplyNamesTheRecordItReads() throws Exception {
        refusedMr("a value without its record", f -> obj((ObjectNode) obj(f, "reply"), "answer").set("result", om.readTree("{\"recorded\":\"erpId1\"}")),
                "is ambiguous", "say record.erpId1");
        refusedMr("a record the flow has not", f -> obj((ObjectNode) obj(f, "reply"), "answer").set("result", om.readTree("{\"recorded\":\"nothing.erpId1\"}")),
                "nothing is no record of the flow");
        refusedMr("a value the record's bean has not", f -> obj((ObjectNode) obj(f, "reply"), "answer").set("result", om.readTree("{\"recorded\":\"register.erpId1\"}")),
                "recorded is erpId1, which is no value of the bean of t36.LineRegistering");
    }

    // ---- the other languages, for each shape of flow (T41) -----------------------------------------------------------------------

    void wellFormed(String id, List<String> entries, String... classes) throws Exception {
        generate(id, entries);
        Path dir = Path.of(OUT + id + "/emit");
        Files.createDirectories(dir);
        List<String> py = new ArrayList<>(List.of("python3", "-I", "-m", "py_compile"));
        for (String c : classes) {
            String python = emitted("python", c), js = emitted("javascript", c);
            Files.writeString(dir.resolve(c + ".py"), python);
            Files.writeString(dir.resolve(c + ".js"), js);
            py.add(dir.resolve(c + ".py").toString());
            if (c.endsWith("Rules")) continue;
            assertTrue(c + ": a comment names the kind beside an assignment", python.contains("# AS ASKED") || python.contains("# SUPPLIED"));
            assertTrue(c + ": and in JavaScript", js.contains("// AS ASKED") || js.contains("// SUPPLIED"));
        }
        String[] p = T36SubtypeTest.run(py, null);
        if (p == null) System.out.println("[T36] python3 is not installed: the emitted Python is not compiled");
        else assertEquals("py_compile: " + p[1], "0", p[0]);
        for (String c : classes) {
            String[] n = T36SubtypeTest.run(List.of("node", "--check", dir.resolve(c + ".js").toString()), null);
            if (n == null) System.out.println("[T36] node is not installed: the emitted JavaScript is not parsed");
            else assertEquals("node --check " + c + ": " + n[1], "0", n[0]);
        }
    }

    public void testEachShapeOfFlowEmitsWellFormedPythonAndJavaScript() throws Exception {
        wellFormed("emitns", List.of(LINE_CHANGE, REPRICE, FLOW_NS), "CreateFlow", "CreateFlowRules");
        wellFormed("emitxt", List.of(LINE_CHANGE, ASK_SEND, SENDING, xt("t36.LineChange")), "SendFlow", "SendFlowRules");
        wellFormed("emitmr", List.of(LINE_CHANGE, LINE_REGISTERING, REPRICE, FLOW_MR), "AddFlow", "AddFlowRules");
        wellFormed("emitsp", List.of(LINE_CHANGE, REPRICE, FLOW_SPREAD), "RepriceFlow", "RepriceFlowRules");
    }

    // ---- the generator's own type checker (T41, finding of the first application) --------------------------------------------------

    public void testTheTypeCheckerKnowsTheTypesOfTheJsonTextRuntime() throws Exception {
        generate("typecheck", List.of(LINE_CHANGE, LINE_REGISTERING, REPRICE, FLOW_MR));
        String log = Files.readString(Path.of(OUT + "typecheck/generation.log"));
        assertFalse("no 'cannot assign Object to Map' for JsonText.newObject(): " + log.lines().filter(l -> l.contains("Incompatible")).limit(3).collect(Collectors.toList()),
                log.contains("Incompatible types"));
        assertFalse(log, log.contains("  ERROR:"));
    }

    // ---- a request that gives nothing (T41, continuation) ---------------------------------------------------------------------

    public void testARequestThatGivesNothingHasNoRequestClassAndAskTakesNoRequest() throws Exception {
        generate("noask", List.of(LINE_CHANGE, ASK_SEND, SENDING, CONFIRM, FLOW_NOASK));
        Path src = Path.of(OUT + "noask/libs/t36/t36_cli/src/main/java/org/example/t36flow/actions/client/integrator/");
        assertTrue(Files.exists(src.resolve("ConfirmInputs.java")));
        assertTrue(Files.exists(src.resolve("ConfirmReply.java")));
        assertFalse("no request value, no request class", Files.exists(src.resolve("ConfirmRequest.java")));
        ClassLoader l = compile("noask", List.of(FLOWS + "ConfirmFlow.java", FLOWS + "ConfirmFlowRules.java",
                "org/example/t36flow/backend/client/integrator/SendingInputs.java", "org/example/t36flow/backend/client/integrator/ExchangingOutputs.java",
                "org/example/t36flow/backend/client/integrator/Line_changeInputs.java", "org/example/t36flow/backend/client/integrator/ChangingOutputs.java",
                "org/example/t36flow/actions/client/integrator/ConfirmInputs.java", "org/example/t36flow/actions/client/integrator/ConfirmReply.java"));
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "ConfirmFlow");
        Class<?> rulesClass = l.loadClass(FLOWS_PKG + "ConfirmFlowRules");
        assertEquals("ask takes the rules and what the stood thing made, and no request", 2, flow.getMethod("ask", rulesClass, l.loadClass(BACKEND_PKG + "ChangingOutputs")).getParameterCount());
        Map<String, Object> answers = new LinkedHashMap<>();
        answers.put("caller", "agent-7");
        answers.put("clock", "2026-11-16T10:00:00Z");
        answers.put("vendor", "acme");
        Object rules = rulesOf(l, "ConfirmFlowRules", answers, new Calls());
        Object made = make(l, BACKEND_PKG + "ChangingOutputs", "document1", "D1");
        Object asked = T36SubtypeTest.invoke(null, flow, "ask", rules, made);
        assertEquals("D1", T36SubtypeTest.get(asked, "subject0"));
        assertEquals("agent-7", T36SubtypeTest.get(asked, "agent"));
        Object recorded = T36SubtypeTest.invoke(null, flow, "record", asked, make(l, BACKEND_PKG + "Line_changeInputs", "name1", "RFQ-1"), made, rules);
        assertEquals("acme", T36SubtypeTest.get(recorded, "vendor"));
        assertEquals("RFQ-1", T36SubtypeTest.get(recorded, "name1"));
        assertEquals("accepted", T36SubtypeTest.get(T36SubtypeTest.invoke(null, flow, "answer", asked, recorded, make(l, BACKEND_PKG + "ExchangingOutputs", "document1", "D2")), "outcome"));
    }

    // ---- a flow narrows a shared subtype (T41, G7a) ----------------------------------------------------------------------------

    public void testAFlowLeavesWhatItDoesNotGiveAbsentWithRestAbsent() throws Exception {
        ObjectNode flow = (ObjectNode) om.readTree(FLOW_NS);
        ObjectNode record = (ObjectNode) flow.get("record");
        for (String v : List.of("name1", "state1", "ptype", "priceBefore", "partPriceBefore", "estimate", "attributesOther", "pattributesOther")) record.remove(v);
        record.put("rest", "absent");
        generate("rest", List.of(LINE_CHANGE, REPRICE, om.writeValueAsString(flow)));
        String doc = Files.readString(Path.of(OUT + "rest/libs/" + DOC));
        for (String v : List.of("name1", "ptype", "priceBefore", "estimate", "pattributesOther")) {
            assertTrue(v + " is a row of the table, absent", doc.contains("<tr><td>record</td><td>" + v + "</td><td class='csv_kind'>absent</td><td>left unset</td></tr>"));
        }
        // a compulsory input is not left unset by it
        ObjectNode flow2 = (ObjectNode) om.readTree(FLOW_NS);
        ((ObjectNode) flow2.get("record")).remove("partProduct");
        ((ObjectNode) flow2.get("record")).put("rest", "absent");
        ObjectNode c = catalogue("refusal", List.of(LINE_CHANGE, REPRICE, om.writeValueAsString(flow2)));
        try {
            new ConfigProcessor(pf).checkSubtypes(ConfigProcessor.objectMapper.readValue(om.writeValueAsString(c), TemplatesProjectConfiguration.class), ".");
            fail("a compulsory value left unset");
        } catch (FlowException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("partProduct is compulsory"));
        }
    }

    // ---- a composite record (T41, G6, design §14.12) ----------------------------------------------------------------------------

    static final List<String> COMPOSITE_SOURCES = List.of(FLOWS + "BundleFlow.java", FLOWS + "BundleFlowRules.java",
            "org/example/t36flow/backend/client/integrator/Exchanging_compositeInputs.java", "org/example/t36flow/backend/client/integrator/ExchangingInputs_1.java",
            "org/example/t36flow/backend/client/integrator/Exchanging_compositeOutputs.java", "org/example/t36flow/backend/client/integrator/ExchangingOutputs.java",
            "org/example/t36flow/backend/client/integrator/Line_changeInputs.java", "org/example/t36flow/backend/client/integrator/ChangingOutputs.java",
            "org/example/t36flow/actions/client/integrator/ConfirmInputs.java", "org/example/t36flow/actions/client/integrator/ConfirmReply.java");

    public void testACompositeRecordIsBuiltInPartsAndTheApplicationLoops() throws Exception {
        generate("comp", List.of(LINE_CHANGE, CONFIRM, FLOW_COMPOSITE));
        ClassLoader l = compile("comp", COMPOSITE_SOURCES);
        if (l == null) return;
        Class<?> flow = l.loadClass(FLOWS_PKG + "BundleFlow");
        List<String> names = Arrays.stream(flow.getMethods()).filter(m -> java.lang.reflect.Modifier.isStatic(m.getModifiers())).map(java.lang.reflect.Method::getName).sorted().collect(Collectors.toList());
        assertEquals(List.of("answer", "ask", "failed", "recordBundle", "recordBundleElement", "recordBundleShare", "refused"), names);

        Map<String, Object> answers = new LinkedHashMap<>();
        answers.put("caller", "agent-7");
        answers.put("clock", "2026-11-16T10:00:00Z");
        answers.put("documentNode", "D-new");
        answers.put("count", 2);
        answers.put("attributes", "{}");
        answers.put("witness", 7);
        Object rules = rulesOf(l, "BundleFlowRules", answers, new Calls());
        Object asked = T36SubtypeTest.invoke(null, flow, "ask", rules);
        Object bundle = T36SubtypeTest.invoke(null, flow, "recordBundle", asked, rules);
        assertEquals(2, T36SubtypeTest.get(bundle, "count"));
        assertEquals("exchanging", T36SubtypeTest.get(bundle, "type"));
        assertEquals("no element yet: the application adds them", 0, ((List<?>) T36SubtypeTest.get(bundle, "__elements")).size());

        // the application loops: one element for each previous record it cites, each standing on its own
        for (String[] cited : new String[][]{{"D1", "RFQ-1"}, {"D2", "RFQ-2"}}) {
            Object previous = make(l, BACKEND_PKG + "Line_changeInputs", "name1", cited[1]);
            Object previousMade = make(l, BACKEND_PKG + "ChangingOutputs", "document1", cited[0]);
            Object element = T36SubtypeTest.invoke(null, flow, "recordBundleElement", asked, bundle, previous, previousMade, rules);
            assertEquals("CHAINED from the element's own previous record", cited[0], T36SubtypeTest.get(element, "document0"));
            assertEquals("CARRIED OVER from it", cited[1], T36SubtypeTest.get(element, "name1"));
            assertEquals("CONSTANT", "Citing", T36SubtypeTest.get(element, "atype"));
            assertEquals("AS ASKED: the user bean's agent", "agent-7", T36SubtypeTest.get(element, "agent"));
            assertEquals("AS ASKED: the user bean's time", "2026-11-16T10:00:00Z", T36SubtypeTest.get(element, "time"));
            assertNull("the shared witness is not the element's own to give: the sharing applies it", T36SubtypeTest.get(element, "witness"));
            T36SubtypeTest.invoke(bundle, bundle.getClass(), "addElements", element);
        }
        Object shared = T36SubtypeTest.invoke(null, flow, "recordBundleShare", asked, bundle, rules);
        assertSame(bundle, shared);
        for (Object element : (List<?>) T36SubtypeTest.get(bundle, "__elements")) {
            assertEquals("the shared variable is given to every element", 7, T36SubtypeTest.get(element, "witness"));
            assertEquals("a shared OUTPUT of the element template, a placeholder (a constant): the same new node for every element", -1, T36SubtypeTest.get(element, "receipt"));
        }
        // what the store made of the composite: one outputs bean for each element; the shared output is the same in every one, and is read from the first (G8)
        Object madeBundle = make(l, BACKEND_PKG + "Exchanging_compositeOutputs");
        Object reply = T36SubtypeTest.invoke(null, flow, "answer", asked, bundle, madeBundle);
        assertEquals("no element made: failed", "failed", T36SubtypeTest.get(reply, "outcome"));
        T36SubtypeTest.invoke(madeBundle, madeBundle.getClass(), "addElements", make(l, BACKEND_PKG + "ExchangingOutputs", "receipt", 5));
        T36SubtypeTest.invoke(madeBundle, madeBundle.getClass(), "addElements", make(l, BACKEND_PKG + "ExchangingOutputs", "receipt", 5));
        reply = T36SubtypeTest.invoke(null, flow, "answer", asked, bundle, madeBundle);
        assertEquals("accepted", T36SubtypeTest.get(reply, "outcome"));
        assertEquals("2", T36SubtypeTest.get(reply, "result"));
    }

    public void testTheTableSaysWhatIsGivenForEachElement() throws Exception {
        generate("compdoc", List.of(LINE_CHANGE, CONFIRM, FLOW_COMPOSITE));
        String doc = Files.readString(Path.of(OUT + "compdoc/libs/" + DOC));
        assertTrue(doc, doc.contains("<tr><td>record bundle</td><td>count</td><td class='csv_kind'>supplied</td><td>the application's rule count</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record bundle element</td><td>document0</td><td class='csv_kind'>chained</td><td>the cited as it stood: document1 of the record that made it</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record bundle element</td><td>name1</td><td class='csv_kind'>carried over</td><td>the cited's name1, unchanged</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>answer</td><td>outcome</td><td class='csv_kind'>as made</td><td>accepted when the store made receipt, otherwise failed</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record bundle shared</td><td>receipt</td><td class='csv_kind'>constant</td><td>the constant -1</td></tr>"));
        assertTrue(doc, doc.contains("<tr><td>record bundle shared</td><td>witness</td><td class='csv_kind'>supplied</td><td>the application's rule witness</td></tr>"));
    }

    void refusedComposite(String title, Consumer<ObjectNode> edit, String... contains) throws Exception {
        ObjectNode flow = (ObjectNode) om.readTree(FLOW_COMPOSITE);
        edit.accept(flow);
        ObjectNode c = catalogue("refusal", List.of(LINE_CHANGE, CONFIRM, om.writeValueAsString(flow)));
        TemplatesProjectConfiguration configs = ConfigProcessor.objectMapper.readValue(om.writeValueAsString(c), TemplatesProjectConfiguration.class);
        try {
            new ConfigProcessor(pf).checkSubtypes(configs, ".");
            fail(title + ": accepted");
        } catch (FlowException e) {
            for (String m : contains) assertTrue(title + ": " + m + " in " + e.getMessage(), e.getMessage().contains(m));
        }
    }

    public void testEveryInputOfTheCompositeAndOfItsElementsIsAccountedForOnce() throws Exception {
        refusedComposite("an input of the composite left out", f -> obj(rec(f, 0), "record").remove("count"), "inputs [count] are not accounted for");
        refusedComposite("an input of the element left out", f -> obj(obj(rec(f, 0), "elements"), "record").remove("atype"), "inputs [atype] are not accounted for");
        refusedComposite("a shared variable left out", f -> obj(rec(f, 0), "shared").removeAll(), "[witness, receipt], are not accounted for", "shared");
        refusedComposite("a variable that is not shared", f -> obj(rec(f, 0), "shared").set("time", om.readTree("\"asked\"")), "shared.time is not shared");
        refusedComposite("a shared variable also said by the element", f -> obj(obj(rec(f, 0), "elements"), "record").put("witness", "absent"), "no input of");
        refusedComposite("an element that stands on a thing it cannot name", f -> ((ObjectNode) obj(obj(rec(f, 0), "elements"), "stood").get("cited")).remove("of"),
                "a stood thing of a composite record says its previous record");
        refusedComposite("an element with an address to check", f -> ((ObjectNode) obj(obj(rec(f, 0), "elements"), "stood").get("cited")).put("addressed", "subject0"), "no addressed, no identity");
        refusedComposite("a reply that reads an output the composite does not share", f -> obj((ObjectNode) obj(f, "reply"), "answer").set("result", om.readTree("{\"made\":\"bundle.document1\"}")),
                "made is document1", "no output of");
        refusedComposite("a composite that is none", f -> rec(f, 0).put("composite", "t36.Nope"), "is no composite of the catalogue");
        refusedComposite("a composite without elements", f -> rec(f, 0).remove("elements"), "says how one element is built");
        refusedComposite("elements with a subtype", f -> {
            rec(f, 0).remove("composite");
            rec(f, 0).put("to", "t36.LineChange");
        }, "elements and shared are for a composite record");
    }

    public void testACompositeRecordEmitsWellFormedPythonAndJavaScript() throws Exception {
        wellFormed("emitcomp", List.of(LINE_CHANGE, CONFIRM, FLOW_COMPOSITE), "BundleFlow", "BundleFlowRules");
    }

    public void testAnElementThatStandsOnARecordOfTheFlowIsRefusedByName() throws Exception {
        // an element that chains from a record before it used to crash (a sublist of -1); a record that is a composite has no outputs
        refusedComposite("an element that stands on its own composite", f -> { ObjectNode c = (ObjectNode) obj(obj(rec(f, 0), "elements"), "stood").get("cited"); c.remove("of"); c.put("record", "bundle"); },
                "stood.cited.record is bundle, a composite record", "nothing to stand on");
        refusedComposite("an element that stands on a record that does not exist", f -> { ObjectNode c = (ObjectNode) obj(obj(rec(f, 0), "elements"), "stood").get("cited"); c.remove("of"); c.put("record", "nothing"); },
                "no record before bundle");
    }
}
