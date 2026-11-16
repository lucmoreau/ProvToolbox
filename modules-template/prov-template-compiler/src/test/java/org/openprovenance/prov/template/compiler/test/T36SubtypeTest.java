package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.SubtypeException;
import org.openprovenance.prov.template.compiler.configuration.SubtypeTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * T36 stage 2: the {@code subtype} kind of the catalogue (R11: a third class that every loop over the templates skips).
 * Its entry is parsed, refused when ill-declared, and adds nothing to the generated tree: a catalogue with subtype
 * entries, wherever they stand in its list and with a composite beside them, generates exactly what it generates
 * without them. The fixture is T31's approval template with in-JSON values (as {@link T36InJsonTest}).
 */
public class T36SubtypeTest extends T36Fixture {

    // ---- the entry ----------------------------------------------------------------------------------------------------

    public void testTheEntryIsParsedAsAThirdKind() throws Exception {
        TemplatesProjectConfiguration configs = parse(catalogue("parse", false, List.of(),
                List.of(subtype("approval_big", "{\"approver_name\":\"Admin\",\"level\":3}", "{\"priceBefore\":\"required\",\"unitCode\":\"optional\"}"))));
        assertEquals(2, configs.templates.length);
        TemplateCompilerConfig t = configs.templates[1];
        assertTrue(t.toString(), t instanceof SubtypeTemplateCompilerConfig);
        SubtypeTemplateCompilerConfig st = (SubtypeTemplateCompilerConfig) t;
        assertEquals("t31.approval_big", st.fullyQualifiedName);
        assertEquals(FQN, st.refines);
        assertEquals(List.of("approver_name", "level"), new ArrayList<>(st.fixed.keySet()));
        assertEquals("Admin", st.fixed.get("approver_name"));
        assertEquals(3, st.fixed.get("level"));
        assertEquals(Map.of("priceBefore", "required", "unitCode", "optional"), st.carries);
    }

    // ---- the refusals -------------------------------------------------------------------------------------------------

    void refused(String title, List<String> subtypes, String... contains) throws Exception {
        TemplatesProjectConfiguration configs = parse(catalogue("refusal", false, List.of(), subtypes));
        try {
            new ConfigProcessor(pf).checkSubtypes(configs, ".");
            fail(title + ": accepted");
        } catch (SubtypeException e) {
            assertTrue(title + ": " + e.getMessage(), e.getMessage().startsWith("T36: subtype t31."));
            for (String m : contains) assertTrue(title + ": " + m + " in " + e.getMessage(), e.getMessage().contains(m));
        }
    }

    public void testRefusals() throws Exception {
        // accepted: fixed constants of each type, both carried kinds
        TemplatesProjectConfiguration ok = parse(catalogue("ok", false, List.of(), List.of(
                subtype("a", "{\"approver_name\":\"Admin\",\"level\":3}", "{\"priceBefore\":\"required\",\"lineCount\":\"optional\"}"),
                subtype("b", "{\"approver_name\":\"Other\"}", "{}"))));
        new ConfigProcessor(pf).checkSubtypes(ok, ".");

        refused("refines nothing", List.of(subtype("x", "{}", "{}").replace(FQN, "t31.Nowhere")), "refines t31.Nowhere", "no template of the catalogue");
        refused("refines a composite", List.of(subtype("x", "{}", "{}").replace("\"refines\":\"" + FQN, "\"refines\":\"t31.ApprovalComposite"),
                "{\"@type\":\"composite\",\"fullyQualifiedName\":\"t31.ApprovalComposite\",\"name\":\"approval_composite\","
                        + "\"package\":\"p\",\"consistsOf\":\"" + FQN + "\",\"sharing\":[\"approving\"]}"), "not a simple template");
        refused("fixes a node", List.of(subtype("x", "{\"approver\":\"ex:a\"}", "{}")), "fixed variable approver", "is a node");
        refused("fixes an output", List.of(subtype("x", "{\"approving\":\"ex:a\"}", "{}")), "fixed variable approving", "is a node");
        refused("fixes an unknown variable", List.of(subtype("x", "{\"nobody\":\"a\"}", "{}")), "nobody is not a variable");
        refused("fixes a carrier", List.of(subtype("x", "{\"customer_hash\":\"a\"}", "{}")), "customer_hash is a carrier");
        refused("fixes an in-JSON value", List.of(subtype("x", "{\"priceBefore\":1.5}", "{}")), "priceBefore is an in-JSON value");
        refused("a constant of the wrong type", List.of(subtype("x", "{\"level\":\"three\"}", "{}")), "level (xsd:int)", "is not an integer");
        refused("a number for a string", List.of(subtype("x", "{\"approver_name\":3}", "{}")), "approver_name (xsd:string)", "is not a string");
        refused("a null constant", List.of(subtype("x", "{\"approver_name\":null}", "{}")), "is null");
        refused("carries an ordinary variable", List.of(subtype("x", "{}", "{\"approver_name\":\"required\"}")), "carries approver_name", "no in-JSON value");
        refused("carries an unknown key", List.of(subtype("x", "{}", "{\"nope\":\"required\"}")), "carries nope");
        refused("carries with a bad mode", List.of(subtype("x", "{}", "{\"priceBefore\":\"always\"}")), "'always'", "'required' or 'optional'");
        refused("two subtypes fixing the same values", List.of(
                subtype("x", "{\"approver_name\":\"Admin\"}", "{}"), subtype("y", "{\"approver_name\":\"Admin\"}", "{\"priceBefore\":\"optional\"}")),
                "subtype t31.y", "same values", "t31.x");
        refused("two subtypes of one name", List.of(subtype("x", "{\"level\":1}", "{}"), subtype("x", "{\"level\":2}", "{}")), "already used");
        refused("a name of the simple template", List.of(subtype("approval", "{}", "{}")), "already used");
        refused("no refines", List.of(subtype("x", "{}", "{}").replace("\"refines\":\"" + FQN + "\",", "")), "refines is required");
    }

    public void testASubtypeNeedsAnIntegratorCatalogue() throws Exception {
        ObjectNode c = catalogue("noint", false, List.of(), List.of(subtype("x", "{}", "{}")));
        c.put("integrator", false);
        try {
            new ConfigProcessor(pf).checkSubtypes(parse(c), ".");
            fail("accepted");
        } catch (SubtypeException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("subtype t31.x") && e.getMessage().contains("\"integrator\": false") && e.getMessage().contains("input bean"));
        }
        c.put("integrator", true);
        new ConfigProcessor(pf).checkSubtypes(parse(c), ".");
    }

    public void testTheSameFixedValuesInTwoTemplatesAreNoClash() throws Exception {
        // the rule is per refined template; a catalogue without subtypes is not checked at all
        TemplatesProjectConfiguration none = parse(catalogue("none", false, List.of(), List.of()));
        new ConfigProcessor(pf).checkSubtypes(none, ".");
    }

    // ---- nothing generated for a subtype -----------------------------------------------------------------------------

    static final List<String> TYPED_FILES = List.of(
            "t36/t36_cli/src/main/java/org/example/t36/approval/client/integrator/Approval_bigInputs.java",
            "t36/t36_cli/src/main/java/org/example/t36/approval/client/integrator/Approval_smallInputs.java",
            "t36/t36_cli/src/main/java/org/example/t36/catalogue/common/Subtypes.java",
            "t36/t36_cli/src/main/java/org/example/t36/catalogue/common/Subtypes.java");
    static final String CLI_POM = "t36/t36_cli/pom.xml";


    /** The tree with subtypes is the tree without, plus exactly the typed beans and the dispatcher, and the client POM's dependency on prov-model. */
    void samePlusTypedBeans(String title, Map<String, String> without, Map<String, String> with, String idWithout, String idWith) throws Exception {
        assertTrue(title + ": the tree is not empty", without.size() > 20);
        Set<String> added = new TreeSet<>(with.keySet());
        added.removeAll(without.keySet());
        assertEquals(title + ": added files", new TreeSet<>(TYPED_FILES), added);
        assertTrue(title + ": removed files", with.keySet().containsAll(without.keySet()));
        Set<String> changed = new TreeSet<>();
        for (String f : without.keySet()) {
            if (f.endsWith("/bindings/approval.json")) continue; // the same bindings; copied verbatim
            if (!without.get(f).equals(with.get(f))) changed.add(f);
        }
        assertEquals(title + ": changed files", Set.of(CLI_POM, DOC, SCHEMA), changed);   // the documentation and the schema describe the subtypes (stage 5)
        String pomWithout = Files.readString(Path.of(OUT + idWithout + "/libs/" + CLI_POM)), pomWith = Files.readString(Path.of(OUT + idWith + "/libs/" + CLI_POM));
        assertFalse(title, pomWithout.contains("prov-model"));
        assertTrue(title + ": the client project depends on prov-model, where JsonText is", pomWith.contains("<artifactId>prov-model</artifactId>"));
    }

    public void testSubtypeEntriesAddTheirTypedBeansAndNothingElse() throws Exception {
        String s1 = subtype("approval_big", "{\"approver_name\":\"Admin\"}", "{\"priceBefore\":\"required\",\"unitCode\":\"optional\"}");
        String s2 = subtype("approval_small", "{\"approver_name\":\"Other\",\"level\":1}", "{\"lineCount\":\"required\"}");

        Map<String, String> plain = generate("plain", false, List.of(), List.of());
        samePlusTypedBeans("after the template", plain, generate("after", false, List.of(), List.of(s1, s2)), "plain", "after");
        samePlusTypedBeans("before the template", plain, generate("before", false, List.of(s1), List.of(s2)), "plain", "before");

        Map<String, String> plainComposite = generate("plainc", true, List.of(), List.of());
        samePlusTypedBeans("with a composite, subtypes between", plainComposite, generate("withc", true, List.of(s1), List.of(s2)), "plainc", "withc");
        assertNotSame("the composite changes the tree", plain.keySet(), plainComposite.keySet());
    }

    public void testNoDispatcherForACatalogueWithoutSubtypes() throws Exception {
        generate("plain", false, List.of(), List.of());
        assertFalse(new File(OUT + "plain/libs/t36/t36_cli/src/main/java/org/example/t36/catalogue/common/Subtypes.java").exists());
    }

    public void testAnIllDeclaredSubtypeStopsGeneration() throws Exception {
        try {
            generate("stopped", false, List.of(), List.of(subtype("x", "{\"level\":\"three\"}", "{}")));
            fail("generated");
        } catch (SubtypeException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("subtype t31.x") && e.getMessage().contains("is not an integer"));
        }
    }

    public void testTheRustBeanHasTheFieldsAndNoConversions() throws Exception {
        String s1 = subtype("approval_big", "{\"approver_name\":\"Admin\"}", "{\"priceBefore\":\"required\",\"unitCode\":\"optional\"}");
        ObjectNode c = catalogue("rust", false, List.of(), List.of(s1));
        c.put("rust_dir", OUT + "rust/libs/rust");
        String dir = OUT + "rust/";
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
        List<Path> rs;
        try (Stream<Path> w = Files.walk(Path.of(dir + "libs/rust"))) {
            rs = w.filter(p -> p.getFileName().toString().equals("approval_big_inputs.rs")).collect(Collectors.toList());
        }
        assertEquals(rs.toString(), 1, rs.size());
        String rust = Files.readString(rs.get(0));
        for (String f : List.of("pub is_a", "pub approver", "pub price_before: Option<f64>", "pub unit_code: Option<String>", "pub level: Option<i32>")) {
            assertTrue(f + " in " + rust, rust.contains(f));
        }
        assertFalse("no conversion", rust.contains("fn to_general") || rust.contains("fn from_general"));
        assertFalse("no constant, no map of other keys", rust.contains("REFINES") && rust.contains("pub const") || rust.contains("customer_hash_other"));
        assertTrue("it derives, as the other beans do: " + rust, rust.contains("#[derive(Debug, Clone, Serialize, Deserialize)]"));
        assertTrue("says why the conversions are missing: " + rust, rust.contains("are not in Rust"));
        assertTrue("no dispatcher in Rust", Files.walk(Path.of(dir + "libs/rust")).noneMatch(p -> p.getFileName().toString().toLowerCase().contains("subtypes")));
    }

    // ---- the typed beans, compiled and run --------------------------------------------------------------------------------

    static final String CLI_SRC = "/libs/t36/t36_cli/src/main/java";
    static final String INTEGRATOR = "org.example.t36.approval.client.integrator.";

    /** Compiles the generated typed beans, their dispatcher and what they refer to; null when JsonText is not on the class path. */
    ClassLoader compileGenerated(String id) throws Exception {
        Class<?> jt = T36JsonTextTest.runtime();
        if (jt == null) {
            System.out.println("[T36] JsonText is not in the prov-model on the classpath: the typed beans are not compiled (build with -am)");
            return null;
        }
        Path src = Path.of(OUT + id + CLI_SRC);
        Path out = Path.of(OUT + id + "/classes");
        Files.createDirectories(out);
        List<String> cp = new ArrayList<>();
        for (String prop : List.of("java.class.path", "jdk.module.path")) {
            String v = System.getProperty(prop);
            if (v != null && !v.isEmpty()) cp.addAll(Arrays.asList(v.split(File.pathSeparator)));
        }
        cp.add(new File(jt.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
        List<String> args = new ArrayList<>(List.of("-proc:none", "-nowarn", "-d", out.toString(), "-sourcepath", src.toString(), "-classpath", String.join(File.pathSeparator, cp)));
        for (String f : List.of("approval/client/integrator/Approval_bigInputs.java", "approval/client/integrator/Approval_smallInputs.java", "catalogue/common/Subtypes.java")) {
            args.add(src.resolve("org/example/t36/" + f).toString());
        }
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        StringWriter messages = new StringWriter();
        int rc = javac.run(null, null, new java.io.OutputStream() {
            public void write(int b) {
                messages.write(b);
            }
        }, args.toArray(new String[0]));
        assertEquals("the generated beans compile: " + messages, 0, rc);
        return new URLClassLoader(new URL[]{out.toUri().toURL()}, getClass().getClassLoader());
    }

    static Object newInstance(ClassLoader l, String name) throws Exception {
        return l.loadClass(name).getDeclaredConstructor().newInstance();
    }

    static void set(Object o, String field, Object value) throws Exception {
        o.getClass().getField(field).set(o, value);
    }

    static Object get(Object o, String field) throws Exception {
        return o.getClass().getField(field).get(o);
    }

    static Object invoke(Object o, Class<?> c, String method, Object... args) throws Exception {
        for (Method m : c.getMethods()) {
            if (m.getName().equals(method) && m.getParameterCount() == args.length) {
                try {
                    return m.invoke(o, args);
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof Exception) throw (Exception) e.getCause();
                    throw e;
                }
            }
        }
        throw new NoSuchMethodException(method);
    }

    /** The JSON object a text holds, for a comparison that ignores key order. */
    static Object json(String text) throws Exception {
        return invoke(null, T36JsonTextTest.runtime(), "read", text);
    }

    @SuppressWarnings("unchecked")
    public void testTypedBeansConvertToTheGeneralBeanAndBack() throws Exception {
        String s1 = subtype("approval_big", "{\"approver_name\":\"Admin\"}",
                "{\"priceBefore\":\"required\",\"unitCode\":\"optional\",\"payload\":\"optional\"}");
        String s2 = subtype("approval_small", "{\"approver_name\":\"Other\",\"level\":1}", "{\"lineCount\":\"required\"}");
        generate("rt", false, List.of(), List.of(s1, s2));
        ClassLoader l = compileGenerated("rt");
        if (l == null) return;
        Class<?> big = l.loadClass(INTEGRATOR + "Approval_bigInputs"), small = l.loadClass(INTEGRATOR + "Approval_smallInputs");
        Class<?> generalClass = l.loadClass(INTEGRATOR + "ApprovalInputs");

        // the typed fields: the fixed input and the carrier are not fields, the carried values and the other keys are
        assertEquals("t31.Approval", big.getField("refines").get(null));
        assertEquals("t31.approval_big", get(newInstance(l, big.getName()), "isA"));
        for (String f : List.of("approver", "approver_ref", "order", "order_note", "level", "priceBefore", "unitCode", "payload", "customer_hashOther")) {
            assertNotNull(f, big.getField(f));
        }
        for (String f : List.of("approver_name", "customer_hash")) {
            try {
                big.getField(f);
                fail("the typed bean has a field " + f);
            } catch (NoSuchFieldException e) {
                // as it must be
            }
        }
        assertEquals(Double.class, big.getField("priceBefore").getType());
        assertEquals(Integer.class, small.getField("lineCount").getType());

        // toGeneral: ordinary fields copied, the fixed value assigned, declared values first and the other keys after
        Object b = newInstance(l, big.getName());
        set(b, "approver", "ex:agent/1");
        set(b, "order", "ex:order/7");
        set(b, "level", 4);
        set(b, "priceBefore", 12.5);
        set(b, "unitCode", "kg");
        set(b, "payload", "{ \"a\" : [1, 2.0] }");
        ((Map<String, Object>) get(b, "customer_hashOther")).put("note", "hello");
        ((Map<String, Object>) get(b, "customer_hashOther")).put("n", 3L);
        Object g = invoke(b, big, "toGeneral");
        assertEquals(generalClass, g.getClass());
        assertEquals("ex:agent/1", get(g, "approver"));
        assertEquals("ex:order/7", get(g, "order"));
        assertEquals(4, get(g, "level"));
        assertEquals("the fixed value", "Admin", get(g, "approver_name"));
        assertEquals("{\"priceBefore\":12.5,\"unit\":\"kg\",\"payload\":{\"a\":[1,2]},\"note\":\"hello\",\"n\":3}", get(g, "customer_hash"));

        // fromGeneral gives it back, the other keys included
        Object b2 = invoke(null, big, "fromGeneral", g);
        assertEquals(12.5, get(b2, "priceBefore"));
        assertEquals("kg", get(b2, "unitCode"));
        assertEquals("{\"a\":[1,2]}", get(b2, "payload"));
        assertEquals(Map.of("note", "hello", "n", 3L), get(b2, "customer_hashOther"));
        assertEquals("ex:agent/1", get(b2, "approver"));
        assertEquals(get(g, "customer_hash"), get(invoke(b2, big, "toGeneral"), "customer_hash"));

        // a general bean whose carrier has the keys in another order and a key nobody declared: same JSON object back
        Object g2 = generalClass.getDeclaredConstructor().newInstance();
        set(g2, "approver_name", "Admin");
        String original = "{\"note\":\"x\",\"priceBefore\":1,\"zzz\":[1,{\"q\":null}],\"unit\":\"m\"}";
        set(g2, "customer_hash", original);
        Object b3 = invoke(null, big, "fromGeneral", g2);
        assertEquals(Map.of("note", "x", "zzz", List.of(1L, new LinkedHashMap<>(Collections.singletonMap("q", null)))), get(b3, "customer_hashOther"));
        String back = (String) get(invoke(b3, big, "toGeneral"), "customer_hash");
        assertEquals("declared keys first", "{\"priceBefore\":1,\"unit\":\"m\",\"note\":\"x\",\"zzz\":[1,{\"q\":null}]}", back);
        assertEquals("the same JSON object", json(original), json(back));

        // an absent optional carrier is an empty object, and absent values are nulls
        Object g3 = generalClass.getDeclaredConstructor().newInstance();
        set(g3, "approver_name", "Admin");
        try {
            invoke(null, big, "fromGeneral", g3);
            fail("a required value is absent");
        } catch (IllegalArgumentException e) {
            assertEquals("T36: subtype t31.approval_big: the required value priceBefore is absent", e.getMessage());
        }
        set(g3, "customer_hash", "{\"priceBefore\":2}");
        Object b4 = invoke(null, big, "fromGeneral", g3);
        assertNull(get(b4, "unitCode"));
        assertNull(get(b4, "payload"));
        assertEquals("absent optional values are not written", "{\"priceBefore\":2}", get(invoke(b4, big, "toGeneral"), "customer_hash"));

        // refusals
        Object bad = newInstance(l, big.getName());
        try {
            invoke(bad, big, "toGeneral");
            fail("a required value is absent");
        } catch (IllegalArgumentException e) {
            assertEquals("T36: subtype t31.approval_big: the required value priceBefore is absent", e.getMessage());
        }
        set(bad, "priceBefore", 1.0);
        ((Map<String, Object>) get(bad, "customer_hashOther")).put("priceBefore", 9);
        try {
            invoke(bad, big, "toGeneral");
            fail("a clash was accepted");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("subtype t31.approval_big, carrier customer_hash") && e.getMessage().contains("key priceBefore"));
        }
        Object other = generalClass.getDeclaredConstructor().newInstance();
        set(other, "approver_name", "Other");
        set(other, "customer_hash", "{\"priceBefore\":1}");
        try {
            invoke(null, big, "fromGeneral", other);
            fail("a bean of another subtype was accepted");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("approver_name is not the subtype's Admin"));
        }
        Object wrongType = generalClass.getDeclaredConstructor().newInstance();
        set(wrongType, "approver_name", "Admin");
        set(wrongType, "customer_hash", "{\"priceBefore\":\"cheap\"}");
        try {
            invoke(null, big, "fromGeneral", wrongType);
            fail("a string was accepted for a number");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("priceBefore"));
        }

        // the second subtype fixes two values and carries an int
        Object sm = newInstance(l, small.getName());
        set(sm, "lineCount", 3);
        Object gs = invoke(sm, small, "toGeneral");
        assertEquals("Other", get(gs, "approver_name"));
        assertEquals(1, get(gs, "level"));
        assertEquals("{\"lineCount\":3}", get(gs, "customer_hash"));
        assertEquals(3, get(invoke(null, small, "fromGeneral", gs), "lineCount"));

        // the dispatcher: the subtype with the most fixed values first, none when none matches
        Class<?> subtypes = l.loadClass("org.example.t36.catalogue.common.Subtypes");
        assertEquals("t31.approval_big", invoke(null, subtypes, "whichApproval", g));
        assertEquals("t31.approval_small", invoke(null, subtypes, "whichApproval", gs));
        assertEquals(big, invoke(null, subtypes, "fromGeneralApproval", g).getClass());
        assertEquals(small, invoke(null, subtypes, "fromGeneralApproval", gs).getClass());
        Object nobody = generalClass.getDeclaredConstructor().newInstance();
        set(nobody, "approver_name", "Nobody");
        assertNull(invoke(null, subtypes, "whichApproval", nobody));
        try {
            invoke(null, subtypes, "fromGeneralApproval", nobody);
            fail("no subtype matches");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("no subtype of t31.Approval"));
        }
    }

    // ---- Python and JavaScript ----------------------------------------------------------------------------------------------

    static final String PY_LIBRARY = "../prov-template-library/src/main/python";

    /** The emitted text of a generated class, by its name. */
    String emitted(String language, String name) {
        for (org.openprovenance.prov.template.compiler.past.Class c : lastProcessor.getCompilerSubtype().getGeneratedClasses()) {
            if (c.name.equals(name)) {
                return language.equals("python")
                        ? new org.openprovenance.prov.template.compiler.past.emitter.Python(null).emit(c).toString()
                        : new org.openprovenance.prov.template.compiler.past.emitter.JavaScript().emit(c).toString();
            }
        }
        throw new IllegalStateException("no generated class " + name + " in " + lastProcessor.getCompilerSubtype().getGeneratedClasses().stream().map(c -> c.name).collect(Collectors.toList()));
    }

    String subtypes() {
        return subtype("approval_big", "{\"approver_name\":\"Admin\"}", "{\"priceBefore\":\"required\",\"unitCode\":\"optional\",\"payload\":\"optional\"}")
                + "\u0000" + subtype("approval_small", "{\"approver_name\":\"Other\",\"level\":1}", "{\"lineCount\":\"required\"}");
    }

    public void testNoClassImportsItselfInPythonOrJavaScript() throws Exception {
        String[] two = subtypes().split("\u0000");
        generate("emit", false, List.of(), List.of(two[0], two[1]));
        String py = emitted("python", "Approval_bigInputs");
        assertFalse(py, py.contains("import Approval_bigInputs"));
        assertTrue("it still imports what it uses", py.contains("import ApprovalInputs") && py.contains("import JsonText"));
        String js = emitted("javascript", "Approval_bigInputs");
        assertFalse(js, js.contains("{ Approval_bigInputs }"));
        assertTrue(js, js.contains("{ ApprovalInputs }") && js.contains("{ JsonText }"));
        Path dir = Path.of(OUT + "emit/lang");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("Approval_bigInputs.js"), js);
        String[] r = run(List.of("node", "--check", dir.resolve("Approval_bigInputs.js").toString()), null);
        if (r == null) System.out.println("[T36] node is not installed: the emitted JavaScript is not parsed");
        else assertEquals("node --check: " + r[1], "0", r[0]);
    }

    public void testTheEmittedPythonRunsAgainstThePort() throws Exception {
        String[] two = subtypes().split("\u0000");
        generate("emit", false, List.of(), List.of(two[0], two[1]));
        Path root = Path.of(OUT + "emit/py");
        FileUtils_deleteQuietly(root);
        Path integrator = root.resolve("org/example/t36/approval/client/integrator");
        Path common = root.resolve("org/example/t36/catalogue/common");
        Files.createDirectories(integrator);
        Files.createDirectories(common);
        Files.writeString(integrator.resolve("ApprovalInputs.py"),
                "class ApprovalInputs:\n    def __init__(self):\n        self.isA = 't31.Approval'\n"
                        + "        self.approver = None\n        self.approver_name = None\n        self.approver_ref = None\n"
                        + "        self.order = None\n        self.order_note = None\n        self.level = None\n        self.customer_hash = None\n");
        Files.writeString(integrator.resolve("Approval_bigInputs.py"), emitted("python", "Approval_bigInputs"));
        Files.writeString(integrator.resolve("Approval_smallInputs.py"), emitted("python", "Approval_smallInputs"));
        Files.writeString(common.resolve("Subtypes.py"), emitted("python", "Subtypes"));
        String[] r = run(List.of("python3", "-I", RES.replace("t31/", "t36/") + "check_typed_beans.py", root.toString(), PY_LIBRARY), new File("."));
        if (r == null) {
            System.out.println("[T36] python3 is not installed: the emitted Python is not run");
            return;
        }
        assertFalse("a wrong answer: " + r[1], r[1].contains("FAIL "));
        assertEquals(r[1], "0", r[0]);
        for (String line : r[1].split("\n")) if (line.startsWith("BLOCKED")) System.out.println("[T36] " + line);
    }

    public void testThePythonPortConformsToTheVectors() throws Exception {
        String[] r = run(List.of("python3", "-I", RES.replace("t31/", "t36/") + "run_json_text_vectors.py", PY_LIBRARY, RES.replace("t31/", "t36/") + "json-text-vectors.json"), new File("."));
        if (r == null) {
            System.out.println("[T36] python3 is not installed: the Python port is not run against the vectors");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("vectors: 58 run, 0 failed"));
    }

    public void testTheJsweetCandyConformsToTheVectors() throws Exception {
        File candy = new File("../prov-jsweet-candy-java/dist/prov-jsweet-candy-java.js");
        String[] r = candy.exists() ? run(List.of("node", RES.replace("t31/", "t36/") + "run_json_text_vectors.js", candy.getPath(), RES.replace("t31/", "t36/") + "json-text-vectors.json"), new File(".")) : null;
        if (r == null) {
            System.out.println("[T36] node or the candy's dist is missing: the JavaScript port is not run against the vectors");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("vectors: 58 run, 0 failed"));
    }

    static void FileUtils_deleteQuietly(Path p) {
        org.apache.commons.io.FileUtils.deleteQuietly(p.toFile());
    }
}
