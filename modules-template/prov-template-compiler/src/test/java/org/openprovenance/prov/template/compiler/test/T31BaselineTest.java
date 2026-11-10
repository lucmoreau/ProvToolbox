package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * T31, phase 1: what the compiler does today with the test configurations of {@code t31/cases.json}.
 * <p>
 * For each case, the bindings of {@code t31/approval-base.json} are given the case's annotations and the catalogue its
 * privacy policy, and the compiler is run as {@link CompilerTest} runs it (without the Maven build of the output). The
 * report {@code target/t31/baseline.md} records, per case: whether the catalogue and the bindings are read, whether
 * generation succeeds, and which generated files differ from case A0's; and, for A0, every generated file that mentions
 * each variable (where a value lands today). Nothing here asserts the design's expectations: those are for phase 2.
 */
public class T31BaselineTest extends TestCase {

    static final String RES = "src/test/resources/t31/";
    static final String OUT = "target/t31/";
    static final String FQN = "t31.Approval";
    static final List<String> VARS = List.of("approving", "approver", "approver_name", "approver_ref", "order", "order_note", "customer_hash");

    final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    final ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();

    final StringBuilder report = new StringBuilder();

    public void testBaseline() throws Exception {
        JsonNode cases = om.readTree(new File(RES + "cases.json")).get("cases");
        Map<String, JsonNode> byId = new LinkedHashMap<>();
        for (JsonNode c : cases) byId.put(c.get("id").asText(), c);

        FileUtils.deleteDirectory(new File(OUT));
        report.append("# T31 baseline: today's compiler on the test configurations\n\n");

        Map<String, Map<String, String>> hashes = new LinkedHashMap<>();
        for (JsonNode c : cases) {
            String id = c.get("id").asText();
            hashes.put(id, runCase(id, c, byId));
        }
        // determinism: A0 a second time
        hashes.put("A0bis", runCase("A0bis", byId.get("A0"), byId));

        report.append("\n## Differences from A0\n\n");
        Map<String, String> a0 = hashes.get("A0");
        for (Map.Entry<String, Map<String, String>> e : hashes.entrySet()) {
            if (e.getKey().equals("A0") || e.getValue() == null) continue;
            report.append("- **").append(e.getKey()).append("**: ").append(diff(a0, e.getValue())).append("\n");
        }

        report.append("\n## Where each variable lands today (A0)\n\n");
        inventory(new File(OUT + "A0/libs"));

        Files.writeString(Path.of(OUT + "baseline.md"), report.toString());
        System.out.println(report);
        assertNotNull("A0 must generate", a0);
        assertEquals("generation must be deterministic", "identical", diff(a0, hashes.get("A0bis")));
    }

    /** @return normalised file hashes of the generated tree, or null when generation did not happen. */
    Map<String, String> runCase(String id, JsonNode c, Map<String, JsonNode> byId) throws Exception {
        String dir = OUT + id + "/";
        new File(dir).mkdirs();
        report.append("## ").append(id).append(": ").append(c.get("title").asText()).append("\n\n");

        // bindings: base + annotations along the 'base' chain
        ObjectNode bindings = (ObjectNode) om.readTree(new File(RES + "approval-base.json"));
        for (JsonNode k : chain(c, byId)) {
            JsonNode ann = k.get("annotations");
            if (ann == null) continue;
            for (Map.Entry<String, JsonNode> v : ann.properties()) {
                JsonNode desc = bindings.get("var").get(v.getKey()).get(0);
                if (desc.isArray()) desc = desc.get(0);
                for (Map.Entry<String, JsonNode> m : v.getValue().properties()) ((ObjectNode) desc).set(m.getKey(), m.getValue());
            }
        }
        String cbindings = dir + "approval.json";
        om.writerWithDefaultPrettyPrinter().writeValue(new File(cbindings), bindings);

        ObjectNode catalogue = (ObjectNode) om.readTree(catalogueJson(id, cbindings));
        JsonNode policy = c.get("policy");
        if (policy != null) {
            om.writerWithDefaultPrettyPrinter().writeValue(new File(dir + "privacy.json"), policy);
            catalogue.put("privacy", dir + "privacy.json");
        }
        File catalogueFile = new File(dir + "catalogue.json");
        om.writerWithDefaultPrettyPrinter().writeValue(catalogueFile, catalogue);

        TemplatesProjectConfiguration configs;
        try {
            configs = ConfigProcessor.objectMapper.readValue(catalogueFile, TemplatesProjectConfiguration.class);
            report.append("- catalogue: read\n");
        } catch (Exception e) {
            report.append("- catalogue: **refused**: ").append(firstLine(e)).append("\n");
            catalogue.remove("privacy");
            om.writerWithDefaultPrettyPrinter().writeValue(catalogueFile, catalogue);
            configs = ConfigProcessor.objectMapper.readValue(catalogueFile, TemplatesProjectConfiguration.class);
            report.append("- catalogue without `privacy`: read (the rest of this case runs without the policy)\n");
        }

        try {
            ConfigProcessor.objectMapper.readValue(new File(cbindings), org.openprovenance.prov.template.descriptors.TemplateBindingsSchema.class);
            report.append("- bindings: read\n");
        } catch (Exception e) {
            report.append("- bindings: **refused**: ").append(firstLine(e)).append("\n");
        }

        // A fresh JVM per case: a second generation in one JVM fails today (NPE in the Python emitter: state is
        // shared between ConfigProcessor instances), which is not what this baseline is about.
        String error = generateInFreshJvm(catalogueFile, dir);
        if (error == null) {
            report.append("- generation: done\n\n");
        } else {
            report.append("- generation: **failed**: ").append(error).append("\n\n");
            return null;
        }
        return hashTree(new File(dir + "libs"), id);
    }

    List<JsonNode> chain(JsonNode c, Map<String, JsonNode> byId) {
        LinkedList<JsonNode> res = new LinkedList<>();
        for (JsonNode k = c; k != null; k = k.has("base") ? byId.get(k.get("base").asText()) : null) res.addFirst(k);
        return res;
    }

    String catalogueJson(String id, String cbindings) {
        return "{\n" +
                "  \"group\": \"org.example\", \"name\": \"t31\", \"destination\": \"" + OUT + id + "/libs\",\n" +
                "  \"description\": \"T31 test configurations\", \"version\": \"0.1.0\",\n" +
                "  \"root_package\": \"org.example.t31.catalogue\", \"jsweet\": false,\n" +
                "  \"script\": \"t31_run\", \"script_dir\": \"" + OUT + id + "/libs/bin\", \"python_dir\": \"" + OUT + id + "/libs/python\",\n" +
                "  \"jsonschema\": \"schema.json\", \"sqlFile\": \"template.sql\", \"documentation\": \"doc.html\",\n" +
                "  \"templates\": [ { \"@type\": \"simple\", \"fullyQualifiedName\": \"" + FQN + "\", \"name\": \"approval\",\n" +
                "                   \"package\": \"org.example.t31.approval\", \"template\": \"" + RES + "approval.provn\",\n" +
                "                   \"cbindings\": \"" + cbindings + "\" } ]\n" +
                "}\n";
    }

    /** @return null on success, else the failure as reported by the child JVM. */
    String generateInFreshJvm(File catalogueFile, String dir) throws Exception {
        String cp = System.getProperty("java.class.path", "");
        String mp = System.getProperty("jdk.module.path", "");
        String path = Stream.of(cp, mp).filter(x -> !x.isEmpty()).collect(Collectors.joining(File.pathSeparator));
        File log = new File(dir + "generation.log");
        Process p = new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "-cp", path,
                T31BaselineTest.class.getName(), catalogueFile.getPath(), dir + "generation-error.txt")
                .redirectErrorStream(true).redirectOutput(log).start();
        int code = p.waitFor();
        if (code == 0) return null;
        File err = new File(dir + "generation-error.txt");
        return err.exists() ? Files.readString(err.toPath()) : "exit " + code + ", see " + log;
    }

    /** Child JVM: generate one catalogue; on failure, write the exception summary to args[1] and exit 1. */
    public static void main(String[] args) throws Exception {
        try {
            TemplatesProjectConfiguration configs = ConfigProcessor.objectMapper.readValue(new File(args[0]), TemplatesProjectConfiguration.class);
            new T31BaselineTest().generate(configs);
            System.exit(0);
        } catch (Throwable e) {
            Files.writeString(Path.of(args[1]), firstLine(e) + frames(e));
            System.exit(1);
        }
    }

    /** As {@link CompilerTest#doTestCompilerWithSingleTemplate}, without building the generated projects. */
    void generate(TemplatesProjectConfiguration configs) throws IOException {
        ConfigProcessor cp = new ConfigProcessor(pf);
        SimpleTemplateCompilerConfig config = (SimpleTemplateCompilerConfig) configs.templates[0];
        final String root_dir = configs.destination + "/" + configs.name;
        final String cli_lib = configs.name + "_cli";
        final String cli_dir = root_dir + "/" + cli_lib;
        final String l2p_lib = configs.name + "_l2p";
        final String l2p_dir = root_dir + "/" + l2p_lib;
        final String l2p_src_dir = l2p_dir + "/src/main/java";
        final String l2p_test_src_dir = l2p_dir + "/src/test/java";
        final String cli_src_dir = cli_dir + "/src/main/java";
        final String cli_test_src_dir = cli_dir + "/src/test/java";
        final String cli_webjar_dir = cli_dir + "/src/main/resources/META-INF/resources/webjars/" + configs.name + "_cli/" + configs.version;
        for (String d : List.of(l2p_src_dir, cli_src_dir, l2p_test_src_dir, cli_test_src_dir, cli_webjar_dir + "/bindings", cli_webjar_dir + "/templates")) {
            new File(d).mkdirs();
        }
        HashMap<String, String> packages = new HashMap<>();
        packages.put(config.fullyQualifiedName, config.package_);
        packages.put(configs.name, configs.root_package);
        HashMap<String, String> shortNames = new HashMap<>();
        shortNames.put(config.fullyQualifiedName, config.name);
        Locations locations = new Locations(configs, packages, shortNames, List.of(RES), cli_src_dir, l2p_src_dir);
        locations.registerTemplate(config.fullyQualifiedName, config.template);
        locations.registerCBindings(config.fullyQualifiedName, config.cbindings);

        Document doc = new Utility().readDocument(config.template, pf);
        cp.doGenerateServerForEntry1(doc, config, configs, locations, cli_src_dir, l2p_src_dir, cli_webjar_dir);
        FileUtils.copyFileToDirectory(new File(config.template), new File(cli_webjar_dir + "/templates"));
        FileUtils.copyFileToDirectory(new File(config.cbindings), new File(cli_webjar_dir + "/bindings"));
        cp.doGenerateProject(configs, locations, root_dir, cli_lib, l2p_lib, l2p_dir, l2p_src_dir, l2p_test_src_dir, cli_test_src_dir, cli_webjar_dir);
        cp.doGenerateClientAndProject(configs, locations, cli_lib, cli_dir, cli_src_dir);
        cp.generateJSonSchemaEnd(configs, cli_src_dir);
        cp.generateSQLEnd(configs, cli_src_dir);
        cp.generateDocumentationEnd(configs, cli_webjar_dir);
    }

    Map<String, String> hashTree(File root, String id) throws Exception {
        Map<String, String> res = new TreeMap<>();
        try (Stream<Path> s = Files.walk(root.toPath())) {
            for (Path p : s.filter(Files::isRegularFile).collect(Collectors.toList())) {
                String text = normalise(p, new String(Files.readAllBytes(p), StandardCharsets.ISO_8859_1).replace("t31/" + id + "/", "t31/CASE/"));
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                res.put(root.toPath().relativize(p).toString(), new java.math.BigInteger(1, md.digest(text.getBytes(StandardCharsets.ISO_8859_1))).toString(16));
            }
        }
        return res;
    }

    static final Pattern BUILD_DATE = Pattern.compile("buildDate = \"[^\"]*\"");
    final ObjectMapper sorted = JsonMapper.builderWithJackson2Defaults()
            .configure(tools.jackson.databind.SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true).build();

    /** Two things vary from one run to the next today, whatever the input: the logger's buildDate, and the key order
     *  of the generated JSON schema. Both are neutralised, so that a difference is one the input made. */
    String normalise(Path p, String text) {
        text = BUILD_DATE.matcher(text).replaceAll("buildDate = \"<normalised>\"");
        if (p.toString().endsWith(".json")) {
            try {
                text = sorted.writeValueAsString(sorted.treeToValue(sorted.readTree(text), Object.class));
            } catch (Exception e) {
                // not JSON after all: compared as text
            }
        }
        return text;
    }

    String diff(Map<String, String> a, Map<String, String> b) {
        if (b == null) return "not generated";
        List<String> changed = new ArrayList<>(), added = new ArrayList<>(), removed = new ArrayList<>();
        for (String f : a.keySet()) if (!b.containsKey(f)) removed.add(f); else if (!a.get(f).equals(b.get(f))) changed.add(f);
        for (String f : b.keySet()) if (!a.containsKey(f)) added.add(f);
        if (changed.isEmpty() && added.isEmpty() && removed.isEmpty()) return "identical";
        return "changed " + changed + (added.isEmpty() ? "" : ", added " + added) + (removed.isEmpty() ? "" : ", removed " + removed);
    }

    void inventory(File root) throws IOException {
        List<Path> files;
        try (Stream<Path> s = Files.walk(root.toPath())) {
            files = s.filter(Files::isRegularFile).sorted().collect(Collectors.toList());
        }
        for (String v : VARS) {
            // snake_case as declared, and the camel-cased forms of generated Java (approverName, getApprover_name, ...)
            String camel = Pattern.compile("_([a-z])").matcher(v).replaceAll(m -> m.group(1).toUpperCase());
            Pattern p = Pattern.compile("\\b(" + Pattern.quote(v) + "|" + Pattern.quote(camel) + ")\\b", Pattern.CASE_INSENSITIVE);
            report.append("- `").append(v).append("`: ");
            List<String> hits = new ArrayList<>();
            for (Path f : files) {
                String text = new String(Files.readAllBytes(f), StandardCharsets.ISO_8859_1);
                long n = p.matcher(text).results().count();
                if (n > 0) hits.add(root.toPath().relativize(f) + " (" + n + ")");
            }
            report.append(String.join(", ", hits)).append("\n");
        }
    }

    static String frames(Throwable e) {
        Throwable root = e;
        while (root.getCause() != null) root = root.getCause();
        if (root instanceof tools.jackson.core.JacksonException) return "";
        return Arrays.stream(root.getStackTrace()).limit(4).map(f -> "\n  - at `" + f + "`").collect(Collectors.joining());
    }

    static String firstLine(Throwable e) {
        String m = e.getClass().getSimpleName() + ": " + e.getMessage();
        int i = m.indexOf('\n');
        return i < 0 ? m : m.substring(0, i);
    }
}
