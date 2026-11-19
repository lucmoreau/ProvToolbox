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
 * T36: the fixture of the tests of the subtype kind and of what is generated for it: T31's approval template with in-JSON values
 * (as {@link T36InJsonTest} has), catalogues made of it, and the whole pipeline run on them.
 */
public abstract class T36Fixture extends TestCase {

    static final String RES = "src/test/resources/t31/";
    static final String OUT = "target/t36s/";
    static final String FQN = "t31.Approval";
    static final String DOC = "t36/t36_cli/src/main/resources/META-INF/resources/webjars/t36_cli/0.1.0/html/doc.html";
    static final String SCHEMA = "t36/t36_cli/src/main/resources/schema.json";

    ConfigProcessor lastProcessor;
    final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    final ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();
    final ObjectMapper sorted = JsonMapper.builderWithJackson2Defaults()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true).build();

    /** The compiler keeps its coordinators in statics: leave them as a fresh JVM has them for a test that does not reset them (CompilerTest). */
    @Override
    protected void tearDown() {
        org.openprovenance.prov.template.compiler.configuration.SpecificationFile.resetTypeCheckCoordinator();
        org.openprovenance.prov.template.compiler.configuration.SpecificationFile.resetCodeGenCoordinator();
    }

    /** T33: keeps the {@code @escape} of the fixture's {@code order_note}, so that the generated loggers call {@code escapeCsv}. */
    boolean keepEscape = false;

    ObjectNode bindings() throws Exception {
        ObjectNode b = (ObjectNode) om.readTree(new File(RES + "approval-base.json"));
        ObjectNode vars = (ObjectNode) b.get("var");
        vars.set("priceBefore", om.readTree("[[{\"@type\":\"xsd:double\",\"@documentation\":\"Price\",\"@input\":\"optional\",\"@inJson\":\"customer_hash\"}]]"));
        vars.set("unitCode", om.readTree("[[{\"@type\":\"xsd:string\",\"@documentation\":\"Unit\",\"@inJson\":\"customer_hash\",\"@inJson.key\":\"unit\"}]]"));
        vars.set("lineCount", om.readTree("[[{\"@type\":\"xsd:int\",\"@documentation\":\"Lines\",\"@inJson\":\"customer_hash\"}]]"));
        vars.set("payload", om.readTree("[[{\"@type\":\"json\",\"@documentation\":\"Any JSON\",\"@inJson\":\"customer_hash\"}]]"));
        // no @escape in the fixture: a client project then depends on prov-model only because of a subtype (keepEscape: T33 wants the field)
        if (!keepEscape) ((ObjectNode) vars.get("order_note").get(0).get(0)).remove("@escape");
        // an integer input to fix a constant of another type
        vars.set("level", om.readTree("[[{\"@type\":\"xsd:int\",\"@documentation\":\"Level\",\"@input\":\"optional\"}]]"));
        return b;
    }

    String subtype(String name, String fixed, String carries) {
        return "{\"@type\":\"subtype\",\"name\":\"" + name + "\",\"fullyQualifiedName\":\"t31." + name + "\","
                + "\"package\":\"org.example.t36.approval\",\"refines\":\"" + FQN + "\","
                + "\"fixed\":" + fixed + ",\"carries\":" + carries + "}";
    }

    /** The catalogue of the fixture, with the given subtype entries before (first) and after (rest) the simple template. */
    ObjectNode catalogue(String id, boolean composite, List<String> subtypesBefore, List<String> subtypesAfter) throws Exception {
        String dir = OUT + id + "/";
        org.apache.commons.io.FileUtils.deleteDirectory(new File(dir));   // nothing left from an earlier run
        new File(dir).mkdirs();
        om.writerWithDefaultPrettyPrinter().writeValue(new File(dir + "approval.json"), bindings());
        ObjectNode c = (ObjectNode) om.readTree("{\n"
                + "  \"group\": \"org.example\", \"name\": \"t36\", \"destination\": \"" + dir + "libs\",\n"
                + "  \"description\": \"T36 stages 2 and 3\", \"version\": \"0.1.0\", \"integrator\": true,\n"
                + "  \"root_package\": \"org.example.t36.catalogue\", \"jsweet\": false,\n"
                + "  \"script\": \"t36_run\", \"script_dir\": \"" + dir + "libs/bin\",\n"
                // no python_dir by default: most tests do not need Python (T36SubtypeTest.testAnIntegratorCatalogueGeneratesPython sets it)
                + "  \"jsonschema\": \"schema.json\", \"sqlFile\": \"template.sql\", \"documentation\": \"doc.html\",\n"
                + "  \"sql.tables\": {\"order_record\": {\"id\": \"SERIAL\"}},\n"
                + "  \"templates\": [ ] }");
        ArrayNode templates = (ArrayNode) c.get("templates");
        for (String s : subtypesBefore) templates.add(om.readTree(s));
        templates.add(om.readTree("{ \"@type\": \"simple\", \"fullyQualifiedName\": \"" + FQN + "\", \"name\": \"approval\","
                + " \"package\": \"org.example.t36.approval\", \"template\": \"t31.approval\","
                + " \"cbindings\": \"" + dir + "approval.json\" }"));
        for (String s : subtypesAfter) templates.add(om.readTree(s));
        if (composite) {
            templates.add(om.readTree("{\"@type\":\"composite\",\"fullyQualifiedName\":\"t31.ApprovalComposite\",\"name\":\"approval_composite\","
                    + "\"package\":\"org.example.t36.approval\",\"consistsOf\":\"" + FQN + "\",\"sharing\":[\"approving\"]}"));
        }
        return c;
    }

    TemplatesProjectConfiguration parse(ObjectNode catalogue) throws Exception {
        return ConfigProcessor.objectMapper.readValue(om.writeValueAsString(catalogue), TemplatesProjectConfiguration.class);
    }


    static final Pattern BUILD_DATE = Pattern.compile("buildDate = \"[^\"]*\"");
    static final Pattern TMP_CONV = Pattern.compile("tmp_Conv[0-9]+");

    /** The whole pipeline, as the Maven goal runs it; returns the normalised hashes of the generated tree. */
    Map<String, String> generate(String id, boolean composite, List<String> before, List<String> after) throws Exception {
        return generate(id, composite, before, after, c -> {});
    }

    /** As above, after the caller has changed the catalogue (to add an output, say). */
    Map<String, String> generate(String id, boolean composite, List<String> before, List<String> after, java.util.function.Consumer<ObjectNode> tweak) throws Exception {
        ObjectNode c = catalogue(id, composite, before, after);
        tweak.accept(c);
        String dir = OUT + id + "/";
        File catalogueFile = new File(dir + "catalogue.json");
        om.writerWithDefaultPrettyPrinter().writeValue(catalogueFile, c);
        try (PrintStream log = new PrintStream(new FileOutputStream(dir + "generation.log"), true, StandardCharsets.UTF_8)) {
            PrintStream out = System.out, err = System.err;
            System.setOut(log);
            System.setErr(log);
            try {
                lastProcessor = new ConfigProcessor(pf);
                lastProcessor.processTemplateGenerationConfig(catalogueFile.getPath(), ".", ".", ".", List.of(RES), pf);
            } finally {
                System.setOut(out);
                System.setErr(err);
            }
        }
        Map<String, String> res = new TreeMap<>();
        Path root = Path.of(dir + "libs");
        try (Stream<Path> s = Files.walk(root)) {
            for (Path p : s.filter(Files::isRegularFile).collect(Collectors.toList())) {
                String text = new String(Files.readAllBytes(p), StandardCharsets.UTF_8).replace("t36s/" + id + "/", "t36s/CASE/");
                text = BUILD_DATE.matcher(text).replaceAll("buildDate = \"<normalised>\"");
                text = TMP_CONV.matcher(text).replaceAll("tmp_Conv<n>");
                if (p.toString().endsWith(".json")) {
                    try {
                        text = sorted.writeValueAsString(sorted.treeToValue(sorted.readTree(text), Object.class));
                    } catch (Exception e) {
                        // compared as text
                    }
                }
                res.put(root.relativize(p).toString(), new java.math.BigInteger(1, MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8))).toString(16));
            }
        }
        return res;
    }


    /** Runs a command; null when the program is not installed. */
    static String[] run(List<String> command, File dir) throws Exception {
        Process p;
        try {
            p = new ProcessBuilder(command).directory(dir).redirectErrorStream(true).start();
        } catch (java.io.IOException e) {
            return null;
        }
        String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return new String[]{String.valueOf(p.waitFor()), out};
    }


    /**
     * T46: the Rust tree a generation wrote under {@code rustDir} (the generated files and the runtime beside them), as a crate with
     * the module files the generated project writes and the given integration test, run by cargo. Null when cargo is not installed.
     */
    static String[] cargoTestGenerated(java.nio.file.Path rustDir, String crateName, String testResource) throws Exception {
        java.nio.file.Path crate = java.nio.file.Path.of("target/" + crateName);
        org.apache.commons.io.FileUtils.deleteDirectory(crate.toFile());
        java.nio.file.Files.createDirectories(crate.resolve("src"));
        java.nio.file.Files.createDirectories(crate.resolve("tests"));
        java.nio.file.Files.writeString(crate.resolve("Cargo.toml"), "[package]\nname = \"t36_bean\"\nversion = \"0.0.0\"\nedition = \"2021\"\npublish = false\n\n[dependencies]\n"
                + "serde = { version = \"1\", features = [\"derive\"] }\nserde_json = \"1\"\n");
        try (java.util.stream.Stream<java.nio.file.Path> w = java.nio.file.Files.walk(rustDir)) {
            for (java.nio.file.Path p : w.collect(java.util.stream.Collectors.toList())) {
                java.nio.file.Path target = crate.resolve("src").resolve(rustDir.relativize(p).toString());
                if (java.nio.file.Files.isDirectory(p)) java.nio.file.Files.createDirectories(target);
                else java.nio.file.Files.copy(p, target);
            }
        }
        List<java.nio.file.Path> dirs;
        try (java.util.stream.Stream<java.nio.file.Path> w = java.nio.file.Files.walk(crate.resolve("src").resolve("org"))) {
            dirs = w.filter(java.nio.file.Files::isDirectory).collect(java.util.stream.Collectors.toList());
        }
        for (java.nio.file.Path d : dirs) {
            List<String> mods;
            try (java.util.stream.Stream<java.nio.file.Path> w = java.nio.file.Files.list(d)) {
                mods = w.map(p -> p.getFileName().toString()).filter(n -> !n.equals("mod.rs")).map(n -> n.endsWith(".rs") ? n.substring(0, n.length() - 3) : n)
                        .sorted().collect(java.util.stream.Collectors.toList());
            }
            java.nio.file.Files.writeString(d.resolve("mod.rs"), mods.stream().map(m -> "pub mod " + m + ";\n").collect(java.util.stream.Collectors.joining()));
        }
        java.nio.file.Files.writeString(crate.resolve("src/lib.rs"), "pub mod org;\npub mod json_text;\npub mod json_text_serde;\n");
        java.nio.file.Files.copy(java.nio.file.Path.of(testResource), crate.resolve("tests/convert.rs"));
        return run(List.of("cargo", "test", "--offline", "--target-dir", "target/" + crateName + "-build"), crate.toFile());
    }

    /** Compiles the given generated sources (paths below the client project's java directory) with what they refer to; null when JsonText is not on the class path. */
    ClassLoader compileSources(String id, List<String> relativePaths) throws Exception {
        return compileIn(OUT, id, relativePaths, getClass().getClassLoader());
    }

    /** As {@link #compileSources}, for the generated tree of a catalogue below {@code outRoot}. */
    static ClassLoader compileIn(String outRoot, String id, List<String> relativePaths, ClassLoader parent) throws Exception {
        Class<?> jt = T36JsonTextTest.runtime();
        if (jt == null) {
            System.out.println("[T36] JsonText is not in the prov-model on the classpath: the generated classes are not compiled (build with -am)");
            return null;
        }
        Path src = Path.of(outRoot + id + "/libs/t36/t36_cli/src/main/java");
        Path out = Path.of(outRoot + id + "/classes");
        Files.createDirectories(out);
        List<String> cp = new ArrayList<>();
        for (String prop : List.of("java.class.path", "jdk.module.path")) {
            String v = System.getProperty(prop);
            if (v != null && !v.isEmpty()) cp.addAll(Arrays.asList(v.split(File.pathSeparator)));
        }
        cp.add(new File(jt.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
        List<String> args = new ArrayList<>(List.of("-proc:none", "-nowarn", "-d", out.toString(), "-sourcepath", src.toString(), "-classpath", String.join(File.pathSeparator, cp)));
        for (String f : relativePaths) args.add(src.resolve(f).toString());
        StringWriter messages = new StringWriter();
        int rc = ToolProvider.getSystemJavaCompiler().run(null, null, new java.io.OutputStream() {
            public void write(int b) {
                messages.write(b);
            }
        }, args.toArray(new String[0]));
        junit.framework.Assert.assertEquals("the generated classes compile: " + messages, 0, rc);
        return new URLClassLoader(new URL[]{out.toUri().toURL()}, parent);
    }
}
