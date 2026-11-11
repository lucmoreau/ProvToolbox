package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.core.Bindings;
import org.openprovenance.prov.template.core.Instantiater;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * T32: a round trip through log2prov. Generates the catalogue {@code t32/catalogue.json} (one simple template,
 * {@code t32.Review}), compiles the generated client (cli) and log2prov (l2p) code with a driver, and runs the driver in
 * a child JVM: it writes records with the generated client, adds a record of an unknown template and one keyed by the
 * template's short name (as logs were written before the client wrote the fully qualified name), and reads the log
 * back with {@code FileBuilder.reader}, with a {@code RecordSQLProcessor}. The documents read must equal the template's
 * expansions of the same values; the SQL must hold the client's own inserts; the unknown record must be skipped with
 * a message.
 * <p>
 * The driver runs twice: on a plain class path, as log2prov's launcher runs it, and with the compiler and its
 * dependencies on the module path and the generated code on the class path, which needs
 * {@code org.openprovenance.prov.template.log2prov.interfaces} exported. It runs in a child JVM because in this JVM the
 * built-in loaders resolve the compiler's packages to its named module.
 */
public class T32Log2ProvTest extends TestCase {

    static final String RES = "src/test/resources/t32/";
    static final String OUT = "target/t32/";
    static final String LIBS = OUT + "libs/t32/";

    /** The records the driver writes with the generated client: reviewing, reviewer, reviewer_name, paper, score, comment. */
    static final String[][] RECORDS = {
            {"r1", "4711", "Jane Smith", "p9", "7", "good, \"clear\""},
            {"r2", "4712", "John Doe", "p10", null, null},
    };
    /** A record keyed by the template's short name, as written before the client wrote the fully qualified name. */
    static final String[] OLD_RECORD = {"r3", "4713", "Ann Lee", "p11", "3", null};
    static final String UNKNOWN = "t32.Unknown";

    static final String DRIVER = String.join("\n",
            "import java.io.*;",
            "import java.nio.charset.StandardCharsets;",
            "import java.nio.file.*;",
            "import java.util.*;",
            "import org.openprovenance.prov.model.Document;",
            "import org.openprovenance.prov.template.log2prov.*;",
            "import org.example.t32.review.client.common.ReviewBuilder;",
            "public class T32Main {",
            "  public static void main(String[] args) throws Exception {",
            "    Path out = Paths.get(args[0]);",
            "    Files.createDirectories(out);",
            "    org.openprovenance.prov.model.ProvFactory pf = org.example.t32.catalogue.Init.pf;",
            "    if (!org.example.t32.catalogue.Init.init()) throw new IllegalStateException(\"Init.init() failed\");",
            "    System.out.println(\"T32: log2prov in module \" + FileBuilder.class.getModule().getName());",
            "    System.out.println(\"T32: registry keys \" + new TreeSet<>(FileBuilder.registry.keySet()));",
            "    ReviewBuilder client = new ReviewBuilder();",
            "    StringBuilder log = new StringBuilder();",
            "    List<Object[]> records = new ArrayList<>();",
            "    String[][] values = { %RECORDS% };",
            "    for (String[] v : values) {",
            "      StringBuilder sb = new StringBuilder();",
            "      client.logReview(sb, v[0], v[1], v[2], v[3], %SCORE%, v[5]);",
            "      log.append(sb).append('\\n');",
            "      Object[] r = new Object[v.length + 1]; r[0] = client.getFullyQualifiedName(); System.arraycopy(v, 0, r, 1, v.length);",
            "      records.add(r);",
            "    }",
            "    log.append(\"%UNKNOWN%,x,y\\n\");",
            "    String[] old = { %OLD% };",
            "    log.append(\"review\"); for (String s : old) log.append(',').append(s == null ? \"\" : s); log.append('\\n');",
            "    Object[] r = new Object[old.length + 1]; r[0] = \"review\"; System.arraycopy(old, 0, r, 1, old.length); records.add(r);",
            "    Files.write(out.resolve(\"log.csv\"), log.toString().getBytes(StandardCharsets.UTF_8));",
            "    // what the SQL path should produce: the client's own insert head and tuples",
            "    StringBuilder expected = new StringBuilder(client.getSQLInsert()).append('\\n');",
            "    for (Object[] rec : records) expected.append(client.record2bean(rec).process(client.bean2sql())).append('\\n');",
            "    Files.write(out.resolve(\"expected.sql\"), expected.toString().getBytes(StandardCharsets.UTF_8));",
            "    List<Document> docs = new ArrayList<>();",
            "    RecordSQLProcessor rp = new RecordSQLProcessor(null);",
            "    FileBuilder.reader(new ByteArrayInputStream(log.toString().getBytes(StandardCharsets.UTF_8)), new DocumentProcessor() {",
            "      public void process(Document d) { docs.add(d); }",
            "      public Document getDocument() { return docs.isEmpty() ? null : docs.get(0); }",
            "    }, rp);",
            "    Files.write(out.resolve(\"read.sql\"), rp.getDocument().toString().getBytes(StandardCharsets.UTF_8));",
            "    org.openprovenance.prov.notation.Utility u = new org.openprovenance.prov.notation.Utility();",
            "    for (int i = 0; i < docs.size(); i++) Files.write(out.resolve(\"doc\" + i + \".provn\"), u.convertBeanToSyntaxTree(docs.get(i), pf).getBytes(StandardCharsets.UTF_8));",
            "    System.out.println(\"T32: \" + docs.size() + \" document(s) read\");",
            "  }",
            "}");

    final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    final ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();

    public void testRoundTrip() throws Exception {
        FileUtils.deleteDirectory(new File(OUT));
        String all = join(System.getProperty("jdk.module.path"), System.getProperty("java.class.path"));

        // generated in a child JVM, as the compiler's Maven plugin would: a generation leaves state in the JVM that
        // breaks a later one (the Python emitter's NPE noted in T31), and this test must not break CompilerTest
        String generation = java(List.of("-cp", all, ConfigProcessor.class.getName(), RES + "catalogue.json", ".", ".", ".", RES));
        assertTrue("the catalogue was generated in " + LIBS + ":\n" + generation,
                new File(LIBS + "t32_l2p/src/main/java/org/example/t32/catalogue/Init.java").isFile());

        File classes = compile();

        // on a plain class path, as log2prov's launcher runs it
        check("classpath", null, classes, List.of("-cp", join(classes.getPath(), all)));

        // the compiler and its dependencies on the module path, the generated code on the class path
        List<String> mp = split(all).stream().filter(p -> !p.endsWith("test-classes")).collect(Collectors.toList());
        check("modulepath", "prov.template.compiler", classes, List.of("--module-path", String.join(File.pathSeparator, mp),
                "--add-modules", "prov.template.compiler,org.apache.commons.text",
                "-cp", classes.getPath()));
    }

    /** Compiles the generated cli and l2p code with the driver. */
    File compile() throws IOException {
        File classes = new File(OUT + "classes");
        File src = new File(OUT + "src");
        classes.mkdirs();
        src.mkdirs();
        Files.writeString(Path.of(src.getPath(), "T32Main.java"), DRIVER
                .replace("%RECORDS%", Arrays.stream(RECORDS).map(T32Log2ProvTest::javaArray).collect(Collectors.joining(", ")))
                .replace("%OLD%", javaStrings(OLD_RECORD))
                .replace("%UNKNOWN%", UNKNOWN)
                .replace("%SCORE%", "v[4] == null ? null : Integer.valueOf(v[4])"));

        List<String> args = new ArrayList<>(List.of("-d", classes.getPath(), "-proc:none", "-nowarn",
                "-cp", join(System.getProperty("jdk.module.path"), System.getProperty("java.class.path"))));
        args.addAll(sources(LIBS + "t32_cli/src/main/java"));
        args.addAll(sources(LIBS + "t32_l2p/src/main/java"));
        args.add(src.getPath() + "/T32Main.java");
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int rc = javac.run(null, null, err, args.toArray(new String[0]));
        assertEquals("the generated code compiles:\n" + err, 0, rc);
        return classes;
    }

    /** Runs the driver in a child JVM with the given path options, and checks what it read; log2prov must be in the module named (null: unnamed). */
    void check(String run, String module, File classes, List<String> pathOptions) throws Exception {
        File dir = new File(OUT + run);
        List<String> cmd = new ArrayList<>(pathOptions);
        cmd.add("T32Main");
        cmd.add(dir.getPath());
        String output = java(cmd);
        Files.createDirectories(dir.toPath());
        Files.writeString(Path.of(dir.getPath(), "output.txt"), output);
        assertTrue(run + ": log2prov loaded in module " + module + ":\n" + output, output.contains("T32: log2prov in module " + module + "\n"));

        // the unknown record is skipped, with a message
        assertTrue(run + ": the unknown record is reported:\n" + output, output.contains("unknown record " + UNKNOWN + ": skipped"));

        // the documents equal the template's expansions of the same values
        List<String[]> expected = new ArrayList<>(Arrays.asList(RECORDS));
        expected.add(OLD_RECORD);
        assertTrue(run + ": " + expected.size() + " documents read:\n" + output, output.contains("T32: " + expected.size() + " document(s) read"));
        Document template = new Utility().readDocument(RES + "review.provn", pf);
        for (int i = 0; i < expected.size(); i++) {
            String read = Files.readString(Path.of(dir.getPath(), "doc" + i + ".provn"));
            String expansion = new Utility().convertBeanToSyntaxTree(new Instantiater(pf).instantiate(template, bindings(expected.get(i))), pf);
            assertEquals(run + ": document " + i + " is the template's expansion of " + Arrays.toString(expected.get(i)),
                    statements(expansion), statements(read));
        }

        // the SQL path yields the client's own inserts
        String sql = Files.readString(Path.of(dir.getPath(), "read.sql"));
        List<String> inserts = Files.readAllLines(Path.of(dir.getPath(), "expected.sql"));
        assertTrue(run + ": the SQL has the insert head " + inserts.get(0) + ":\n" + sql, sql.contains(inserts.get(0) + " VALUES"));
        for (String tuple : inserts.subList(1, inserts.size())) {
            assertTrue(run + ": the SQL has " + tuple + ":\n" + sql, sql.contains("  " + tuple));
        }
        assertTrue(run + ": the SQL has the name in clear:\n" + sql, sql.contains("Jane Smith"));
    }

    /** Runs a child JVM with the test's log4j configuration; asserts it exits normally; returns its output. */
    static String java(List<String> args) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add(System.getProperty("java.home") + "/bin/java");
        cmd.add("-Dlog4j2.configurationFile=" + new File("src/test/config/log4j2.xml").getAbsolutePath());
        cmd.addAll(args);
        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        String output = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exit = p.waitFor();
        assertEquals("the child JVM ran " + args.get(args.size() - 2) + " " + args.get(args.size() - 1) + ":\n" + output, 0, exit);
        return output;
    }

    /** The bindings of the template's variables to a record's values, as the generated builder binds them. */
    Bindings bindings(String[] v) {
        StringBuilder sb = new StringBuilder("{\"var\": {");
        sb.append("\"reviewing\": [{\"@id\": \"ex:").append(v[0]).append("\"}],");
        sb.append("\"reviewer\": [{\"@id\": \"ex:agent/").append(v[1]).append("\"}],");
        sb.append("\"reviewer_name\": [[{\"@value\": ").append(om.writeValueAsString(v[2])).append(", \"@type\": \"xsd:string\"}]],");
        sb.append("\"paper\": [{\"@id\": \"ex:").append(v[3]).append("\"}]");
        if (v[4] != null) sb.append(", \"score\": [[{\"@value\": ").append(om.writeValueAsString(v[4])).append(", \"@type\": \"xsd:int\"}]]");
        if (v[5] != null) sb.append(", \"comment\": [[{\"@value\": ").append(om.writeValueAsString(v[5])).append(", \"@type\": \"xsd:string\"}]]");
        sb.append("}, \"vargen\": {}, \"context\": {\"ex\": \"http://example.org/\"}}");
        return Bindings.importBindings(om.readTree(sb.toString()));
    }

    /** A document's statements, sorted, without the document's and bundle's own lines (a bundle's name is generated). */
    static List<String> statements(String provn) {
        return provn.lines().map(String::trim)
                .filter(l -> !l.isEmpty() && !l.startsWith("document") && !l.startsWith("endDocument") && !l.startsWith("prefix")
                        && !l.startsWith("bundle") && !l.startsWith("endBundle"))
                .sorted().collect(Collectors.toList());
    }

    static String javaArray(String[] v) {
        return "{" + javaStrings(v) + "}";
    }

    static String javaStrings(String[] v) {
        return Arrays.stream(v).map(s -> s == null ? "null" : "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(", "));
    }

    static String join(String... paths) {
        return Stream.of(paths).filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(File.pathSeparator));
    }

    static List<String> split(String path) {
        return Stream.of(path.split(File.pathSeparator)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    static List<String> sources(String dir) throws IOException {
        try (Stream<Path> s = Files.walk(Path.of(dir))) {
            return s.filter(p -> p.toString().endsWith(".java")).map(Path::toString).collect(Collectors.toList());
        }
    }
}
