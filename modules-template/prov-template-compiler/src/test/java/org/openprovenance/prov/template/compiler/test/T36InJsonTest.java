package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.InJsonException;
import org.openprovenance.prov.template.descriptors.InJsonSplitter;
import org.openprovenance.prov.template.descriptors.InJsonValue;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * T36 stage 1: {@code @inJson}. The values it declares are split out of a template's variables where the bindings are
 * read (R1), refused when ill-declared, and listed in the documentation and the JSON schema, and in no other generated
 * file (design §4.3). The fixture is T31's approval template; its bindings, generated here with and without in-JSON
 * values added to the carrier {@code customer_hash}, give two trees that must differ in exactly {@code doc.html} and
 * {@code schema.json}.
 */
public class T36InJsonTest extends TestCase {

    static final String RES = "src/test/resources/t31/";
    static final String OUT = "target/t36/";
    static final String FQN = "t31.Approval";
    static final String CLI = "t36/t36_cli/src/main/";
    static final String DOC = CLI + "resources/META-INF/resources/webjars/t36_cli/0.1.0/html/doc.html";
    static final String SCHEMA = CLI + "resources/schema.json";

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

    // ---- the in-JSON values added to the fixture's carrier --------------------------------------------------------

    /** Adds three in-JSON values to the carrier {@code customer_hash}, one with a key of its own. */
    ObjectNode withInJson(ObjectNode bindings) throws Exception {
        ObjectNode vars = (ObjectNode) bindings.get("var");
        vars.set("priceBefore", om.readTree("[[{\"@type\":\"xsd:double\",\"@documentation\":\"The unit price before the act\","
                + "\"@input\":\"optional\",\"@inJson\":\"customer_hash\"}]]"));
        vars.set("unitCode", om.readTree("[[{\"@type\":\"xsd:string\",\"@documentation\":\"Its unit\","
                + "\"@inJson\":\"customer_hash\",\"@inJson.key\":\"unit\"}]]"));
        vars.set("lineCount", om.readTree("[[{\"@type\":\"xsd:int\",\"@documentation\":\"How many lines\","
                + "\"@input\":\"compulsory\",\"@inJson\":\"customer_hash\"}]]"));
        return bindings;
    }

    ObjectNode base() throws Exception {
        return (ObjectNode) om.readTree(new File(RES + "approval-base.json"));
    }

    // ---- loading and splitting ----------------------------------------------------------------------------------

    TemplateBindingsSchema read(JsonNode bindings) throws Exception {
        return ConfigProcessor.objectMapper.readValue(om.writeValueAsString(bindings), TemplateBindingsSchema.class);
    }

    public void testSplitKeepsOrdinaryVariablesAndHoldsInJsonValuesApart() throws Exception {
        TemplateBindingsSchema plain = read(base());
        List<String> before = new ArrayList<>(plain.getVar().keySet());
        InJsonSplitter.split(plain, "base");
        assertEquals("nothing to split: the variables are untouched", before, new ArrayList<>(plain.getVar().keySet()));
        assertTrue(plain.getInJson().isEmpty());

        TemplateBindingsSchema s = read(withInJson(base()));
        InJsonSplitter.split(s, "variant");
        assertEquals("the ordinary variables are those of the base, in the same order", before, new ArrayList<>(s.getVar().keySet()));
        assertEquals(List.of("priceBefore", "unitCode", "lineCount"), new ArrayList<>(s.getInJson().keySet()));
        InJsonValue unit = s.getInJson().get("unitCode");
        assertEquals("customer_hash", unit.getCarrier());
        assertEquals("@inJson.key gives the key when it differs from the name (R2)", "unit", unit.getKey());
        assertEquals("the key is the variable's name by default", "priceBefore", s.getInJson().get("priceBefore").getKey());
        assertEquals("xsd:int", s.getInJson().get("lineCount").getType());
        assertEquals(List.of("priceBefore", "unitCode", "lineCount"),
                s.getInJsonOf("customer_hash").stream().map(InJsonValue::getName).collect(Collectors.toList()));
        assertTrue(s.getInJsonOf("order_note").isEmpty());
    }

    /** An {@code @inJson} value is declared with {@code member}, in a bindings schema otherwise valid; the refusal says why. */
    void refused(String title, String json, String... messageContains) throws Exception {
        ObjectNode b = base();
        ((ObjectNode) b.get("var")).set("bad", om.readTree(json));
        TemplateBindingsSchema s = read(b);
        try {
            InJsonSplitter.split(s, "the-bindings.json");
            fail(title + ": accepted");
        } catch (InJsonException e) {
            assertTrue(title + ": " + e.getMessage(), e.getMessage().startsWith("T36: "));
            assertTrue(title + ": names the file: " + e.getMessage(), e.getMessage().contains("the-bindings.json"));
            assertTrue(title + ": names the variable: " + e.getMessage(), e.getMessage().contains("variable bad"));
            assertTrue(title + ": names the template: " + e.getMessage(), e.getMessage().contains("approval.provn"));
            for (String m : messageContains) assertTrue(title + ": " + m + " in " + e.getMessage(), e.getMessage().contains(m));
        }
    }

    public void testRefusals() throws Exception {
        refused("carrier absent", "[[{\"@type\":\"xsd:int\",\"@inJson\":\"nowhere\"}]]", "carrier nowhere is not a variable");
        refused("carrier is a node", "[[{\"@type\":\"xsd:int\",\"@inJson\":\"order\"}]]", "carrier order is a node");
        refused("on a node", "[{\"@id\":\"ex:*\",\"@inJson\":\"customer_hash\"}]", "not a node");
        refused("no type", "[[{\"@inJson\":\"customer_hash\"}]]", "@type");
        refused("unknown type", "[[{\"@type\":\"xsd:decimal\",\"@inJson\":\"customer_hash\"}]]", "xsd:decimal");
        refused("empty carrier", "[[{\"@type\":\"xsd:int\",\"@inJson\":\" \"}]]", "@inJson is empty");
        refused("key without carrier", "[[{\"@type\":\"xsd:int\",\"@inJson.key\":\"k\"}]]", "@inJson.key without @inJson");
        refused("empty key", "[[{\"@type\":\"xsd:int\",\"@inJson\":\"customer_hash\",\"@inJson.key\":\"\"}]]", "@inJson.key is empty");
        refused("with @sql.type", "[[{\"@type\":\"xsd:int\",\"@inJson\":\"customer_hash\",\"@sql.type\":\"INT\"}]]", "@sql.type");
        refused("with @unique", "[[{\"@type\":\"xsd:int\",\"@inJson\":\"customer_hash\",\"@unique\":true}]]", "@unique");
        refused("with @escape", "[[{\"@type\":\"xsd:string\",\"@inJson\":\"customer_hash\",\"@escape\":true}]]", "@escape");
        refused("with @output", "[[{\"@type\":\"xsd:int\",\"@inJson\":\"customer_hash\",\"@output\":\"true\"}]]", "@output");
        refused("with @transient (R8)", "[[{\"@type\":\"xsd:string\",\"@inJson\":\"customer_hash\",\"@transient\":true}]]",
                "@transient", "annotations of its carrier cover its whole content");
        refused("with @pseudonym (R8)", "[[{\"@type\":\"xsd:string\",\"@inJson\":\"customer_hash\",\"@pseudonym\":\"pt:keyed-hash\"}]]",
                "@pseudonym", "annotations of its carrier cover its whole content");
    }

    public void testCarrierThatIsItselfInJsonAndDuplicateKeysAreRefused() throws Exception {
        ObjectNode b = base();
        ObjectNode vars = (ObjectNode) b.get("var");
        vars.set("inner", om.readTree("[[{\"@type\":\"xsd:string\",\"@inJson\":\"customer_hash\"}]]"));
        vars.set("deep", om.readTree("[[{\"@type\":\"xsd:int\",\"@inJson\":\"inner\"}]]"));
        try {
            InJsonSplitter.split(read(b), "f.json");
            fail("accepted");
        } catch (InJsonException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("variable deep") && e.getMessage().contains("is itself @inJson"));
        }

        b = base();
        vars = (ObjectNode) b.get("var");
        vars.set("a", om.readTree("[[{\"@type\":\"xsd:int\",\"@inJson\":\"customer_hash\",\"@inJson.key\":\"k\"}]]"));
        vars.set("b", om.readTree("[[{\"@type\":\"xsd:int\",\"@inJson\":\"customer_hash\",\"@inJson.key\":\"k\"}]]"));
        try {
            InJsonSplitter.split(read(b), "f.json");
            fail("accepted");
        } catch (InJsonException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("variable b") && e.getMessage().contains("key \"k\" is already declared in the carrier customer_hash"));
        }

        // the same key in two different carriers is what @inJson.key is for (R2): accepted
        b = base();
        vars = (ObjectNode) b.get("var");
        vars.set("a", om.readTree("[[{\"@type\":\"xsd:int\",\"@inJson\":\"customer_hash\",\"@inJson.key\":\"k\"}]]"));
        vars.set("b", om.readTree("[[{\"@type\":\"xsd:int\",\"@inJson\":\"approver_name\",\"@inJson.key\":\"k\"}]]"));
        TemplateBindingsSchema s = read(b);
        InJsonSplitter.split(s, "f.json");
        assertEquals("k", s.getInJson().get("a").getKey());
        assertEquals("k", s.getInJson().get("b").getKey());
    }

    public void testAnOlderCompilerRefusesAnUnknownMember() throws Exception {
        // what T31 relies on, and T36 relies on again: the descriptors are read so that an unknown member fails.
        ObjectNode b = base();
        ((ObjectNode) b.get("var")).set("x", om.readTree("[[{\"@type\":\"xsd:int\",\"@notAMember\":\"y\"}]]"));
        try {
            read(b);
            fail("an unknown member was accepted");
        } catch (RuntimeException e) {
            // fails closed
        }
    }

    // ---- generation -------------------------------------------------------------------------------------------------

    /** The compiler as {@code ConfigProcessor.processTemplateGenerationConfig} runs it for one simple template. */
    Map<String, String> generate(String id, ObjectNode bindings, String privacyPolicy) throws Exception {
        String dir = OUT + id + "/";
        new File(dir).mkdirs();
        String cbindings = dir + "approval.json";
        om.writerWithDefaultPrettyPrinter().writeValue(new File(cbindings), bindings);
        String catalogue = "{\n"
                + "  \"group\": \"org.example\", \"name\": \"t36\", \"destination\": \"" + dir + "libs\",\n"
                + "  \"description\": \"T36 stage 1\", \"version\": \"0.1.0\",\n"
                + "  \"root_package\": \"org.example.t36.catalogue\", \"jsweet\": false,\n"
                + "  \"script\": \"t36_run\", \"script_dir\": \"" + dir + "libs/bin\", \"python_dir\": \"" + dir + "libs/python\",\n"
                + (privacyPolicy == null ? "" : "  \"privacy\": \"" + dir + "privacy.json\",\n")
                + "  \"jsonschema\": \"schema.json\", \"sqlFile\": \"template.sql\", \"documentation\": \"doc.html\",\n"
                + "  \"templates\": [ { \"@type\": \"simple\", \"fullyQualifiedName\": \"" + FQN + "\", \"name\": \"approval\",\n"
                + "                   \"package\": \"org.example.t36.approval\", \"template\": \"" + RES + "approval.provn\",\n"
                + "                   \"cbindings\": \"" + cbindings + "\" } ]\n"
                + "}\n";
        if (privacyPolicy != null) Files.writeString(Path.of(dir + "privacy.json"), privacyPolicy);
        File catalogueFile = new File(dir + "catalogue.json");
        Files.writeString(catalogueFile.toPath(), catalogue);
        TemplatesProjectConfiguration configs = ConfigProcessor.objectMapper.readValue(catalogueFile, TemplatesProjectConfiguration.class);

        try (PrintStream log = new PrintStream(new FileOutputStream(dir + "generation.log"), true, StandardCharsets.UTF_8)) {
            PrintStream out = System.out, err = System.err;
            System.setOut(log);
            System.setErr(log);
            try {
                generate(configs);
            } finally {
                System.setOut(out);
                System.setErr(err);
            }
        }
        return hashTree(new File(dir + "libs"), id);
    }

    void generate(TemplatesProjectConfiguration configs) throws IOException {
        SpecificationFile.resetTypeCheckCoordinator();
        SpecificationFile.resetCodeGenCoordinator();
        ConfigProcessor cp = new ConfigProcessor(pf);
        cp.setupPrivacy(configs, ".", ".");
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
        cp.generatePrivacyRegister(cli_webjar_dir);
    }

    static final Pattern BUILD_DATE = Pattern.compile("buildDate = \"[^\"]*\"");
    static final Pattern TMP_CONV = Pattern.compile("tmp_Conv[0-9]+");

    Map<String, String> hashTree(File root, String id) throws Exception {
        Map<String, String> res = new TreeMap<>();
        try (Stream<Path> s = Files.walk(root.toPath())) {
            for (Path p : s.filter(Files::isRegularFile).collect(Collectors.toList())) {
                String text = normalise(p, new String(Files.readAllBytes(p), StandardCharsets.UTF_8).replace("t36/" + id + "/", "t36/CASE/"));
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                res.put(root.toPath().relativize(p).toString(), new java.math.BigInteger(1, md.digest(text.getBytes(StandardCharsets.UTF_8))).toString(16));
            }
        }
        return res;
    }

    /** What varies from one run to the next whatever the input (T31's R14): the build date, a counter, the key order of JSON. */
    String normalise(Path p, String text) {
        text = BUILD_DATE.matcher(text).replaceAll("buildDate = \"<normalised>\"");
        text = TMP_CONV.matcher(text).replaceAll("tmp_Conv<n>");
        if (p.toString().endsWith(".json")) {
            try {
                text = sorted.writeValueAsString(sorted.treeToValue(sorted.readTree(text), Object.class));
            } catch (Exception e) {
                // not JSON after all: compared as text
            }
        }
        return text;
    }

    static String read(String id, String relative) throws IOException {
        return Files.readString(Path.of(OUT + id + "/libs/" + relative));
    }

    public void testInJsonValuesChangeTheDocumentationAndTheSchemaAndNothingElse() throws Exception {
        Map<String, String> plain = generate("plain", base(), null);
        Map<String, String> variant = generate("injson", withInJson(base()), null);

        Set<String> changed = new TreeSet<>();
        for (String f : plain.keySet()) {
            assertTrue("a file of the plain tree is missing from the variant's: " + f, variant.containsKey(f));
            if (!plain.get(f).equals(variant.get(f))) changed.add(f);
        }
        for (String f : variant.keySet()) assertTrue("a file only the variant has: " + f, plain.containsKey(f));
        assertTrue("the tree is not empty", plain.size() > 20);
        // the bindings file itself differs, as the input does: it is copied verbatim into the webjar (and, by T31's design, stays so)
        changed.removeIf(f -> f.endsWith("/bindings/approval.json"));
        assertEquals("only the documentation and the JSON schema differ", new TreeSet<>(List.of(DOC, SCHEMA)), changed);

        // no other generated file mentions an in-JSON value: not a bean field, a column, a builder or an invoker parameter
        for (String name : List.of("priceBefore", "unitCode", "lineCount")) {
            try (Stream<Path> s = Files.walk(Path.of(OUT + "injson/libs"))) {
                for (Path p : s.filter(Files::isRegularFile).collect(Collectors.toList())) {
                    String f = p.toString();
                    if (f.endsWith("doc.html") || f.endsWith("schema.json") || f.endsWith("/bindings/approval.json") || f.endsWith("approval.json")) continue;
                    assertFalse(name + " found in " + f, Files.readString(p).contains(name));
                }
            }
        }

        // the documentation lists them under their carrier
        String doc = read("injson", DOC);
        int carrier = doc.indexOf("<span class='csv_field'>customer_hash</span>");
        assertTrue(carrier > 0);
        String tail = doc.substring(carrier);
        tail = tail.substring(0, tail.indexOf("</ul>"));
        assertTrue(tail, tail.contains("<span class='csv_injson_key'>priceBefore</span>") && tail.contains("The unit price before the act"));
        assertTrue("the key of its own, not the variable's name: " + tail, tail.contains("<span class='csv_injson_key'>unit</span>"));
        assertFalse(tail, tail.contains("unitCode"));
        assertTrue(tail, tail.contains("<span class='csv_injson_key'>lineCount</span>") && tail.contains("I*"));
        assertTrue("other keys are said to be allowed", tail.contains("other keys are allowed"));
        assertFalse("the plain tree's page has no such list", read("plain", DOC).contains("csv_injson"));

        // the schema records them on the carrier, whose type stays a string, and gives them no property of their own
        JsonNode schema = om.readTree(read("injson", SCHEMA));
        JsonNode props = schema.get("definitions").get(FQN).get("properties");
        JsonNode carrierProp = props.get("customer_hash");
        assertEquals("string", carrierProp.get("type").asText());
        String d = carrierProp.get("description").asText();
        assertTrue(d, d.contains("priceBefore (number)") && d.contains("unit (string)") && d.contains("lineCount (integer)"));
        for (String name : List.of("priceBefore", "unitCode", "unit", "lineCount")) assertFalse(name, props.has(name));
        assertEquals("the same properties as the plain schema",
                om.readTree(read("plain", SCHEMA)).get("definitions").get(FQN).get("properties").propertyNames(),
                props.propertyNames());
    }

    public void testAPolicyNamingAnInJsonValueIsRefused() throws Exception {
        // R8 and the split before the policy: an in-JSON value is no variable, so a scope naming it is an unknown variable
        try {
            generate("policy", withInJson(base()),
                    "{\"overrides\": {\"variable:" + FQN + "#priceBefore\": {\"decision\": \"ignore\", \"reason\": \"x\"}}}");
            fail("accepted");
        } catch (Exception e) {
            assertTrue(e.toString(), e.getMessage() != null && e.getMessage().contains("unknown variable priceBefore"));
        }
    }

    public void testAnIllDeclaredValueStopsGeneration() throws Exception {
        ObjectNode b = withInJson(base());
        ((ObjectNode) ((ObjectNode) b.get("var")).get("priceBefore").get(0).get(0)).put("@transient", true);
        try {
            generate("refused", b, null);
            fail("generated");
        } catch (InJsonException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("variable priceBefore") && e.getMessage().contains("@transient"));
        }
    }
}
