package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * F5 (T31 findings): a round trip through log2prov. Compiles the client (cli) and log2prov (l2p) code generated for
 * T31's case A0 (by T31CasesTest, in target/t31/A0) with a small driver, and runs the driver in a child JVM on a plain
 * class path, as log2prov's launcher would: in this JVM the module system refuses the generated builder, whose
 * interfaces (log2prov.interfaces) prov.template.compiler doesn't export. The driver writes a record with the
 * generated client and reads it back with FileBuilder.reader. Prints what happens; asserts only that it ran.
 */
public class F5Log2ProvTest extends TestCase {

    static final String LIBS = "target/t31/A0/libs/t31/";

    static final String DRIVER = String.join("\n",
            "import java.io.ByteArrayInputStream;",
            "import java.nio.charset.StandardCharsets;",
            "import java.util.ArrayList;",
            "import java.util.List;",
            "import org.openprovenance.prov.model.Document;",
            "import org.openprovenance.prov.template.log2prov.*;",
            "public class F5Main {",
            "  public static void main(String[] args) throws Exception {",
            "    org.openprovenance.prov.model.ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();",
            "    StringBuilder sb = new StringBuilder();",
            "    new org.example.t31.approval.client.common.ApprovalBuilder().logApproval(sb, \"1\", \"4711\", \"Jane Smith\", \"hr:E1\", \"o9\", \"a note\", \"k:abc\");",
            "    String record = sb.toString();",
            "    org.example.t31.approval.ApprovalBuilder l2p = new org.example.t31.approval.ApprovalBuilder(pf);   // registers itself, as Init does",
            "    System.out.println(\"F5: record written by the generated client: \" + record);",
            "    System.out.println(\"F5: builder name \" + l2p.getName() + \", fully qualified name \" + l2p.getFullyQualifiedName() + \"; registry keys \" + FileBuilder.registry.keySet());",
            "    step(\"F5 part 1: the record as written\", record, null);",
            "    String withName = l2p.getName() + record.substring(record.indexOf(','));",
            "    step(\"F5 part 1, control: the record keyed by the short name\", withName, null);",
            "    step(\"F5 part 2: the SQL path, on the record keyed by the short name\", withName, new RecordSQLProcessor(null));",
            "  }",
            "  static void step(String what, String csv, RecordProcessor rp) {",
            "    try {",
            "      System.out.println(what + \": \" + read(csv, rp) + \" document(s)\");",
            "    } catch (Throwable t) {",
            "      Throwable c = t; while (c.getCause() != null) c = c.getCause();",
            "      System.out.println(what + \": failed: \" + c);",
            "      for (StackTraceElement e : c.getStackTrace()) { if (e.getClassName().startsWith(\"F5Main\")) break; System.out.println(\"F5   at \" + e); }",
            "    }",
            "  }",
            "  static int read(String csv, RecordProcessor rp) throws Exception {",
            "    List<Document> docs = new ArrayList<>();",
            "    FileBuilder.reader(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)), new DocumentProcessor() {",
            "      public void process(Document d) { docs.add(d); }",
            "      public Document getDocument() { return docs.isEmpty() ? null : docs.get(0); }",
            "    }, rp);",
            "    return docs.size();",
            "  }",
            "}");

    public void testRoundTrip() throws Exception {
        assertTrue("run T31CasesTest first: " + LIBS + " is missing", new File(LIBS).isDirectory());
        File out = new File("target/f5/classes");
        File src = new File("target/f5/src");
        out.mkdirs();
        src.mkdirs();
        Files.writeString(Path.of(src.getPath(), "F5Main.java"), DRIVER);

        String cp = classpath();
        List<String> args = new ArrayList<>(List.of("-d", out.getPath(), "-proc:none", "-nowarn", "-cp", cp));
        args.addAll(sources(LIBS + "t31_cli/src/main/java"));
        args.addAll(sources(LIBS + "t31_l2p/src/main/java"));
        args.add(src.getPath() + "/F5Main.java");
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int rc = javac.run(null, null, err, args.toArray(new String[0]));
        assertEquals("the generated code compiles:\n" + err, 0, rc);

        String java = System.getProperty("java.home") + "/bin/java";
        Process p = new ProcessBuilder(java, "-cp", out.getPath() + File.pathSeparator + cp, "F5Main")
                .redirectErrorStream(true).start();
        String output = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exit = p.waitFor();
        output.lines().filter(l -> l.startsWith("F5") || l.contains("unknown method") || l.contains("Exception"))
                .forEach(System.out::println);
        Files.writeString(Path.of("target/f5/output.txt"), output);
        assertEquals("the driver ran:\n" + output, 0, exit);
    }

    static String classpath() {
        return Stream.of(System.getProperty("jdk.module.path"), System.getProperty("java.class.path"))
                .filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(File.pathSeparator));
    }

    static List<String> sources(String dir) throws Exception {
        try (Stream<Path> s = Files.walk(Path.of(dir))) {
            return s.filter(p -> p.toString().endsWith(".java")).map(Path::toString).collect(Collectors.toList());
        }
    }
}
