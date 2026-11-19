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
        refused("fixes a node with a prefixed name", List.of(subtype("x", "{\"approver\":\"ex:a\"}", "{}")), "fixed variable approver", "is a node", "local part", "without prefix");
        refused("fixes a node with a number", List.of(subtype("x", "{\"approver\":3}", "{}")), "fixed variable approver", "is a node", "local part");
        refused("fixes an output", List.of(subtype("x", "{\"approving\":\"a\"}", "{}")), "fixed variable approving", "is not an input");
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

    /** A record of a definition of the schema: each required property given a value of its type. */
    ObjectNode recordOf(JsonNode definition, String isA) {
        ObjectNode r = om.createObjectNode();
        for (JsonNode name : definition.get("required")) {
            String p = name.asText();
            if (p.equals("isA")) { r.put(p, isA); continue; }
            String type = definition.get("properties").get(p).get("type").asText();
            switch (type) {
                case "integer": r.put(p, 1); break;
                case "number": r.put(p, 1.5); break;
                case "object": r.set(p, om.createObjectNode()); break;
                default: r.put(p, "x");
            }
        }
        return r;
    }

    /**
     * T40: the root schema accepts a record of any template or subtype of the catalogue, and refuses an {@code isA} that none of them has.
     * It had the pattern of the last template written, so a record of any other failed.
     */
    public void testTheRootSchemaAcceptsARecordOfAnyTemplateOrSubtype() throws Exception {
        String s1 = subtype("approval_big", "{\"approver_name\":\"Admin\"}", "{\"priceBefore\":\"required\",\"unitCode\":\"optional\"}");
        String s2 = subtype("approval_small", "{\"approver_name\":\"Other\",\"level\":1}", "{\"lineCount\":\"required\"}");
        generate("t40", false, List.of(), List.of(s1, s2));
        Path schemaFile = Path.of(OUT + "t40/libs/t36/t36_cli/src/main/resources/schema.json");
        JsonNode schema = om.readTree(Files.readString(schemaFile));
        assertEquals("one alternative for each definition that has an if/then block, in order, the dots escaped",
                "^(t31\\.Approval|t31\\.approval_big|t31\\.approval_small)$", schema.get("properties").get("isA").get("pattern").asText());
        assertEquals(3, schema.get("allOf").size());

        Map<String, String> records = new LinkedHashMap<>();
        for (String name : List.of("t31.Approval", "t31.approval_big", "t31.approval_small")) {
            Path rec = Path.of(OUT + "t40/" + name + ".json");
            Files.writeString(rec, recordOf(schema.get("definitions").get(name), name).toString());
            records.put(name, rec.toString());
        }
        ObjectNode unknown = recordOf(schema.get("definitions").get("t31.Approval"), "t31.nothing");
        Path unknownFile = Path.of(OUT + "t40/unknown.json");
        Files.writeString(unknownFile, unknown.toString());

        List<String> command = new ArrayList<>(List.of("node", RES.replace("t31/", "t36/") + "validate_schema.js", schemaFile.toString()));
        command.addAll(records.values());
        command.add(unknownFile.toString());
        String[] r = run(command, new File("."));
        if (r == null || r[1].startsWith("NO-AJV")) {
            System.out.println("[T36] node or ajv is not installed: the root schema is not run against records");
            return;
        }
        for (Map.Entry<String, String> e : records.entrySet()) {
            assertTrue("a record of " + e.getKey() + " satisfies the root schema: " + r[1], r[1].contains(e.getValue() + " valid"));
        }
        assertTrue("an isA that is no template's fails: " + r[1], r[1].contains(unknownFile + " invalid") && r[1].contains(".isA should match pattern"));
    }

    /**
     * T38: an integrator catalogue with {@code python_dir} set. The generated {@code LocalEnactor}'s constructor calls its superclass's
     * constructor ({@code super(...)}, a super call with no method name), on which the Python emitter asserted; it writes
     * {@code super().__init__(...)}, and every file written is valid Python.
     */
    public void testAnIntegratorCatalogueGeneratesPython() throws Exception {
        generate("t38", false, List.of(), List.of(), c -> c.put("python_dir", OUT + "t38/libs/python"));
        Path python = Path.of(OUT + "t38/libs/python");
        List<Path> files;
        try (java.util.stream.Stream<Path> w = Files.walk(python)) {
            files = w.filter(p -> p.toString().endsWith(".py")).collect(java.util.stream.Collectors.toList());
        }
        assertFalse("python files are written", files.isEmpty());
        Path enactor = files.stream().filter(p -> p.getFileName().toString().equals("LocalEnactor.py")).findFirst().orElseThrow(() -> new AssertionError("no LocalEnactor.py in " + files));
        String text = Files.readString(enactor);
        assertTrue(text, text.contains("super().__init__("));
        assertFalse(text, text.contains("super().None"));
        List<String> command = new ArrayList<>(List.of("python3", "-m", "py_compile"));
        files.forEach(p -> command.add(p.toString()));
        String[] r = run(command, null);
        if (r == null) {
            System.out.println("[T36] python3 is not installed: the generated Python is not compiled");
            return;
        }
        assertEquals("py_compile: " + r[1], "0", r[0]);
    }

    /**
     * T34: the JSweet build of a client project, which transpiled nothing ({@code jsweet/jsweet-pom.xml} had {@code jsweet/} for its base directory,
     * and a script set {@code JAVA_HOME} to a JDK 12). It is {@code pom-jsweet.xml} at the project's root, run by antrun with the {@code --add-opens}
     * jsweet needs on a recent JDK. The generated project is built with Maven (when Maven, the jsweet plugin and j4ts are available offline), which
     * transpiles it into {@code dist}, and the bundle, loaded in node with j4ts and the candy, writes a record through the generated builder.
     */
    public void testTheJsweetBuildOfAClientTranspilesAndTheBundleRunsInNode() throws Exception {
        generate("t34", false, List.of(), List.of(), c -> c.put("jsweet", true));
        Path libs = Path.of(OUT + "t34/libs/t36");
        Path cli = libs.resolve("t36_cli");
        assertTrue("the jsweet POM is at the project's root", Files.exists(cli.resolve("pom-jsweet.xml")));
        assertFalse("no script, no jsweet directory", Files.exists(cli.resolve("jsweet")));
        String pom = Files.readString(cli.resolve("pom.xml")), root = Files.readString(libs.resolve("pom.xml")), jsweet = Files.readString(cli.resolve("pom-jsweet.xml"));
        assertFalse("no JDK 12: " + pom, pom.contains("java12") || root.contains("java12") || pom.contains("jsweet.sh"));
        assertTrue(pom, pom.contains("--add-opens jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED") && pom.contains("-f pom-jsweet.xml") && pom.contains("-Dmaven.repo.local=${settings.localRepository}"));
        assertTrue(jsweet, jsweet.contains("<compileSourceRootsOverride>${project.basedir}/src/main/java</compileSourceRootsOverride>") && jsweet.contains("<bundle>true</bundle>")
                && jsweet.contains("<module>none</module>") && jsweet.contains("<artifactId>j4ts</artifactId>") && jsweet.contains("<artifactId>prov-jsweet-candy-java</artifactId>"));

        Path repo = Path.of(System.getProperty("maven.repo.local", System.getProperty("user.home") + "/.m2/repository"));
        Path j4tsJar = repo.resolve("org/jsweet/j4ts/2.0.0/j4ts-2.0.0.jar");
        Path candy = Path.of("../prov-jsweet-candy-java/dist/prov-jsweet-candy-java.js");
        if (!Files.exists(repo.resolve("org/jsweet/jsweet-maven-plugin/3.1.0")) || !Files.exists(j4tsJar) || !Files.exists(candy)) {
            System.out.println("[T36] the jsweet plugin, j4ts or the candy's dist is not available: the generated client is not built with jsweet");
            return;
        }
        String[] build = run(List.of("mvn", "-o", "-pl", "t36_cli", "package", "-DskipTests", "-Djsweet.mvn.options=-o", "-Dmaven.repo.local=" + repo.toAbsolutePath()), libs.toFile());
        if (build == null) {
            System.out.println("[T36] mvn is not installed: the generated client is not built with jsweet");
            return;
        }
        assertEquals("the generated project builds: " + build[1], "0", build[0]);
        assertTrue(build[1], build[1].contains("transpilation successfully completed"));
        Path bundle = cli.resolve("dist/t36_cli.js");
        assertTrue("the bundle is in dist", Files.exists(bundle) && Files.size(bundle) > 10000);
        assertTrue("and its typings", Files.size(cli.resolve("dist/t36_cli.d.ts")) > 1000);

        Path j4ts = Path.of("target/t34/j4ts-bundle.js");
        Files.createDirectories(j4ts.getParent());
        try (java.util.zip.ZipFile z = new java.util.zip.ZipFile(j4tsJar.toFile())) {
            Files.copy(z.getInputStream(z.getEntry("META-INF/resources/webjars/j4ts/2.0.0/bundle.js")), j4ts, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        String[] node = run(List.of("node", RES.replace("t31/", "t36/") + "run_client_bundle.js", j4ts.toString(), candy.toString(), bundle.toString()), new File("."));
        if (node == null) {
            System.out.println("[T36] node is not installed: the bundle is not run");
            return;
        }
        assertEquals("the bundle runs: " + node[1], "0", node[0]);
        assertTrue(node[1], node[1].contains("log: t31.Approval,a1,ex:agent,Ann,ref-1,o1,a note,{\"k\":1},3"));
    }

    /**
     * T33: the Python emitter wrote Java's {@code null} as {@code 0}, so a generated Python logger, which tests each value with {@code if (x == null)}
     * in Java, tested it against 0: an absent value was written as the text {@code None}, and an absent {@code @escape} value reached
     * {@code escapeCsv(None)} and crashed. It writes {@code None} and {@code is None}. The generated Python logger writes the records the generated Java
     * one writes, absent values and an escaped one included, and the Python and JavaScript {@code escapeCsv} give Java's answers on shared vectors.
     */
    public void testTheGeneratedPythonLoggerWritesTheRecordsTheJavaOneDoes() throws Exception {
        keepEscape = true;
        String s1 = subtype("approval_big", "{\"approver_name\":\"Admin\"}", "{\"priceBefore\":\"required\",\"unitCode\":\"optional\",\"payload\":\"optional\"}");
        String s2 = subtype("approval_small", "{\"approver_name\":\"Other\",\"level\":1}", "{\"lineCount\":\"required\"}");
        generate("t33", false, List.of(), List.of(s1, s2), c -> c.put("python_dir", OUT + "t33/libs/python"));
        String python = Files.readString(Path.of(OUT + "t33/libs/python/org/example/t36/approval/client/common/ApprovalBuilder.py"));
        assertTrue(python, python.contains("if approving is None:") && python.contains("StringEscapeUtils.escapeCsv(order_note)"));
        assertFalse("null is no longer 0: " + python, python.contains("approving == 0") || python.contains("level == 0"));
        assertTrue("record2bean tests for an absent value", python.contains("None if record[8] is None else"));

        String absent = "t31.Approval,,ex:agent,,,o1,,,3";
        String present = "t31.Approval,a1,ex:agent,Ann,ref-1,o1,\"a, \"\"note\"\"\",{},3";
        String[] r = run(List.of("python3", RES.replace("t31/", "t33/") + "run_python_logger.py", "../prov-template-library/src/main/python", OUT + "t33/libs/python"), new File("."));
        if (r == null) {
            System.out.println("[T33] python3 is not installed: the generated Python logger is not run");
        } else {
            assertEquals(r[1], "0", r[0]);
            assertTrue(r[1], r[1].contains("absent: " + absent + "\n") && r[1].contains("present: " + present + "\n"));
        }

        // the Java client writes the same
        ClassLoader l = compileSources("t33", List.of("org/example/t36/catalogue/client/logger/Logger.java"));
        if (l != null) {
            Class<?> logger = l.loadClass("org.example.t36.catalogue.client.logger.Logger");
            Method m = logger.getMethod("logApproval", String.class, String.class, String.class, String.class, String.class, String.class, String.class, Integer.class);
            assertEquals(absent, m.invoke(null, null, "ex:agent", null, null, "o1", null, null, 3));
            assertEquals(present, m.invoke(null, "a1", "ex:agent", "Ann", "ref-1", "o1", "a, \"note\"", "{}", 3));
        }
    }

    /** T33: the Python and JavaScript escapeCsv, and Java's (Commons Lang's, prov-model's), give the answers of the shared vectors. */
    public void testEscapeCsvGivesJavasAnswersInEveryPort() throws Exception {
        String vectors = RES.replace("t31/", "t33/") + "escape-csv-vectors.json";
        JsonNode list = om.readTree(Files.readString(Path.of(vectors))).get("vectors");
        for (JsonNode v : list) {
            assertEquals("Java: " + v.get("title").asText(), v.get("output").asText(), org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeCsv(v.get("input").asText()));
        }
        assertNull("Java: null is null", org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeCsv(null));
        String[] py = run(List.of("python3", RES.replace("t31/", "t33/") + "run_escape_vectors.py", "../prov-template-library/src/main/python", vectors), new File("."));
        if (py == null) System.out.println("[T33] python3 is not installed: the Python escapeCsv is not run against the vectors");
        else {
            assertEquals(py[1], "0", py[0]);
            assertTrue(py[1], py[1].contains("escape vectors: " + (list.size() + 1) + " run, 0 failed"));
        }
        File candy = new File("../prov-jsweet-candy-java/dist/prov-jsweet-candy-java.js");
        String[] js = candy.exists() ? run(List.of("node", RES.replace("t31/", "t33/") + "run_escape_vectors.js", candy.getPath(), vectors), new File(".")) : null;
        if (js == null) System.out.println("[T33] node or the candy's dist is missing: the JavaScript escapeCsv is not run against the vectors");
        else {
            assertEquals(js[1], "0", js[0]);
            assertTrue(js[1], js[1].contains("escape vectors: " + list.size() + " run, 0 failed"));
        }
    }

    public void testAnIllDeclaredSubtypeStopsGeneration() throws Exception {
        try {
            generate("stopped", false, List.of(), List.of(subtype("x", "{\"level\":\"three\"}", "{}")));
            fail("generated");
        } catch (SubtypeException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("subtype t31.x") && e.getMessage().contains("is not an integer"));
        }
    }

    /**
     * T46: the Rust typed bean of a subtype has its conversions, the runtime is written beside it, and the whole is compiled and run by
     * cargo with the Rust counterpart of {@link #testTypedBeansConvertToTheGeneralBeanAndBack} (same values and texts).
     */
    public void testTheRustBeanConvertsAsTheJavaOneDoes() throws Exception {
        String s1 = subtype("approval_big", "{\"approver_name\":\"Admin\"}",
                "{\"priceBefore\":\"required\",\"unitCode\":\"optional\",\"payload\":\"optional\"}");
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
        Path rustDir = Path.of(dir + "libs/rust");
        List<Path> rs;
        try (Stream<Path> w = Files.walk(rustDir)) {
            rs = w.filter(p -> p.getFileName().toString().equals("approval_big_inputs.rs")).collect(Collectors.toList());
        }
        assertEquals(rs.toString(), 1, rs.size());
        String rust = Files.readString(rs.get(0));
        for (String f : List.of("pub is_a", "pub approver", "pub price_before: Option<f64>", "pub unit_code: Option<String>", "pub level: Option<i32>",
                "pub customer_hash_other: Object", "#[serde(with = \"crate::json_text_serde\")]", "pub fn to_general(&self) -> ApprovalInputs",
                "pub fn from_general(general: ApprovalInputs) -> ApprovalBigInputs", "pub const REFINES: &str = \"t31.Approval\";")) {
            assertTrue(f + " in " + rust, rust.contains(f));
        }
        assertTrue("it derives, as the other beans do: " + rust, rust.contains("#[derive(Debug, Clone, Serialize, Deserialize)]"));
        assertFalse("says nothing of missing conversions: " + rust, rust.contains("are not in Rust"));
        assertTrue("the runtime is beside the generated files", Files.exists(rustDir.resolve("json_text.rs")) && Files.exists(rustDir.resolve("json_text_serde.rs")));
        assertTrue("the dispatcher is in Rust", Files.walk(rustDir).anyMatch(p -> p.getFileName().toString().equals("subtypes.rs")));

        String[] r = cargoTestGenerated(rustDir, "t36-rust-bean", RES.replace("t31/", "t36/") + "rust-bean/convert.rs");
        if (r == null) {
            System.out.println("[T36] cargo is not installed: the Rust typed bean is not compiled and run");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("test result: ok. 8 passed"));
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
        assertTrue(r[1], r[1].contains("vectors: 123 run, 0 failed"));
    }

    /** T42: the Rust port (src/main/resources/rs/json_text.rs, standard library only) runs the same vectors, by cargo, in a crate of its own. */
    public void testTheRustPortConformsToTheVectors() throws Exception {
        String[] r = run(List.of("cargo", "test", "--offline", "--manifest-path", RES.replace("t31/", "t36/") + "rust/Cargo.toml",
                "--target-dir", "target/t36-rust", "--", "--nocapture"), new File("."));
        if (r == null) {
            System.out.println("[T36] cargo is not installed: the Rust port is not run against the vectors");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("vectors: 123 run, 0 failed"));
    }

    public void testTheJsweetCandyConformsToTheVectors() throws Exception {
        File candy = new File("../prov-jsweet-candy-java/dist/prov-jsweet-candy-java.js");
        String[] r = candy.exists() ? run(List.of("node", RES.replace("t31/", "t36/") + "run_json_text_vectors.js", candy.getPath(), RES.replace("t31/", "t36/") + "json-text-vectors.json"), new File(".")) : null;
        if (r == null) {
            System.out.println("[T36] node or the candy's dist is missing: the JavaScript port is not run against the vectors");
            return;
        }
        assertEquals(r[1], "0", r[0]);
        assertTrue(r[1], r[1].contains("vectors: 123 run, 0 failed"));
    }

    static void FileUtils_deleteQuietly(Path p) {
        org.apache.commons.io.FileUtils.deleteQuietly(p.toFile());
    }
}
