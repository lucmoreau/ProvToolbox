package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * T36 part E: the fixture of the flow tests, T36's own. A backend template {@code changing} (two nodes it makes, a document and a line,
 * each the inputs of a later record; the act's keys and the line's keys as two carriers) with the subtype {@code line_change}; a user
 * template {@code inquiry} with the subtype {@code reprice} (a request of a line number and a price, a reply of an outcome, a code and a
 * result); and the flow between them, which uses each kind of origin.
 */
public abstract class T36FlowFixture extends TestCase {

    static final String RES = "src/test/resources/t36/flow/";
    static final String OUT = "target/t36f/";
    static final String CLI = "/libs/t36/t36_cli/src/main/";
    static final String JAVA = CLI + "java/";
    static final String PKG = "org.example.t36flow";

    final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    final ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();
    ConfigProcessor lastProcessor;

    @Override
    protected void tearDown() {
        SpecificationFile.resetTypeCheckCoordinator();
        SpecificationFile.resetCodeGenCoordinator();
    }

    static final String LINE_CHANGE = "{\"@type\":\"subtype\",\"name\":\"line_change\",\"fullyQualifiedName\":\"t36.LineChange\",\"package\":\"" + PKG + ".backend\","
            + "\"refines\":\"t36.Changing\",\"fixed\":{},\"carries\":{\"priceUnit\":\"optional\",\"priceBefore\":\"optional\",\"overwrite\":\"optional\",\"items\":\"optional\","
            + "\"estimate\":\"optional\",\"partLine\":\"required\",\"partPriceUnit\":\"required\",\"partPriceBefore\":\"optional\",\"partProduct\":\"required\","
            + "\"partQuantity\":\"required\",\"partPriceSource\":\"required\"}}";

    static final String REPRICE = "{\"@type\":\"subtype\",\"name\":\"reprice\",\"fullyQualifiedName\":\"t36.Reprice\",\"package\":\"" + PKG + ".actions\","
            + "\"refines\":\"t36.Inquiry\",\"fixed\":{\"atype\":\"Reprice\"},\"carries\":{\"line\":\"required\",\"unitPrice\":\"required\",\"reason\":\"optional\"}}";

    /** The flow of the fixture: each kind of origin at least once. */
    static final String FLOW = "{\"@type\":\"flow\",\"name\":\"reprice_flow\",\"fullyQualifiedName\":\"" + PKG + ".flows.RepriceFlow\",\"package\":\"" + PKG + ".flows\","
            + "\"from\":\"t36.Reprice\",\"to\":\"t36.LineChange\","
            + "\"stood\":{\"document\":{\"node\":\"document0\",\"madeBy\":\"document1\",\"addressed\":\"subject0\"},"
            + "            \"line\":{\"node\":\"part0\",\"madeBy\":\"part1\",\"identity\":{\"user\":\"line\",\"backend\":\"partLine\"}}},"
            + "\"ask\":{\"agent\":{\"supplied\":\"caller\"},\"time\":{\"supplied\":\"clock\"}},"
            + "\"record\":{\"agent\":\"asked\",\"time\":\"asked\","
            + "  \"partLine\":{\"asked\":\"line\",\"as\":\"text\"},\"priceUnit\":{\"asked\":\"unitPrice\",\"as\":\"text\"},\"partPriceUnit\":{\"asked\":\"unitPrice\",\"as\":\"text\"},"
            + "  \"priceBefore\":{\"carried\":\"line.partPriceUnit\"},\"partPriceBefore\":{\"carried\":\"line.partPriceUnit\"},"
            + "  \"atype\":{\"constant\":\"Repricing\"},\"overwrite\":{\"constant\":\"true\"},\"partPriceSource\":{\"constant\":\"repriced\"},"
            + "  \"estimate\":{\"supplied\":\"estimate\"},\"erpId1\":{\"supplied\":\"version\"},\"attributesOther\":\"absent\"},"
            + "\"carry\":{\"line\":\"rest\",\"document\":[\"items\",\"name1\",\"state1\"]},"
            + "\"carryOpen\":{\"pattributes\":\"line\"},"
            + "\"reply\":{\"answer\":{\"outcome\":{\"when\":{\"made\":\"document1\"},\"is\":\"accepted\",\"otherwise\":\"failed\"},\"result\":{\"recorded\":\"erpId1\"},\"code\":\"absent\"},"
            + "          \"refused\":{\"outcome\":{\"constant\":\"refused\"},\"code\":{\"argument\":true},\"result\":\"absent\"},"
            + "          \"failed\":{\"outcome\":{\"constant\":\"failed\"},\"code\":\"absent\",\"result\":\"absent\"}}}";

    static final String ANSWERS = "\"reply\":{\"answer\":{\"outcome\":{\"when\":{\"made\":\"document1\"},\"is\":\"accepted\",\"otherwise\":\"failed\"},\"result\":{\"recorded\":\"erpId1\"},\"code\":\"absent\"},"
            + "          \"refused\":{\"outcome\":{\"constant\":\"refused\"},\"code\":{\"argument\":true},\"result\":\"absent\"},"
            + "          \"failed\":{\"outcome\":{\"constant\":\"failed\"},\"code\":\"absent\",\"result\":\"absent\"}}";

    /**
     * NS (T41, design §14.1): a request that stands on nothing, a line made from nothing: no stood thing, the nodes supplied by the application
     * (or asked, from a user value), the values asked, constant, supplied or absent.
     */
    static final String FLOW_NS = "{\"@type\":\"flow\",\"name\":\"create_flow\",\"fullyQualifiedName\":\"" + PKG + ".flows.CreateFlow\",\"package\":\"" + PKG + ".flows\","
            + "\"from\":\"t36.Reprice\",\"to\":\"t36.LineChange\","
            + "\"ask\":{\"agent\":{\"supplied\":\"caller\"},\"time\":{\"supplied\":\"clock\"},\"subject0\":{\"supplied\":\"documentNode\"}},"
            + "\"record\":{\"agent\":\"asked\",\"time\":\"asked\",\"document0\":{\"asked\":\"subject0\"},\"part0\":{\"supplied\":\"lineNode\"},"
            + "  \"atype\":{\"constant\":\"Creating\"},"
            + "  \"partLine\":{\"asked\":\"line\",\"as\":\"text\"},\"priceUnit\":{\"asked\":\"unitPrice\",\"as\":\"text\"},\"partPriceUnit\":{\"asked\":\"unitPrice\",\"as\":\"text\"},"
            + "  \"partProduct\":{\"supplied\":\"product\"},\"partQuantity\":{\"constant\":\"1\"},\"overwrite\":{\"constant\":\"false\"},\"partPriceSource\":{\"constant\":\"catalogue\"},"
            + "  \"erpId1\":{\"supplied\":\"version\"},\"items\":{\"supplied\":\"count\"},"
            + "  \"name1\":\"absent\",\"state1\":\"absent\",\"ptype\":\"absent\",\"priceBefore\":\"absent\",\"partPriceBefore\":\"absent\",\"estimate\":\"absent\","
            + "  \"attributesOther\":\"absent\",\"pattributesOther\":\"absent\"},"
            + ANSWERS + "}";

    /** A user subtype for a request that sends a document, and the backend subtype of the template that exchanges it. */
    static final String ASK_SEND = "{\"@type\":\"subtype\",\"name\":\"ask_send\",\"fullyQualifiedName\":\"t36.AskSend\",\"package\":\"" + PKG + ".actions\","
            + "\"refines\":\"t36.Inquiry\",\"fixed\":{\"atype\":\"Sending\"},\"carries\":{\"vendor\":\"required\",\"reference\":\"required\"}}";

    static final String SENDING = "{\"@type\":\"subtype\",\"name\":\"sending\",\"fullyQualifiedName\":\"t36.Sending\",\"package\":\"" + PKG + ".backend\","
            + "\"refines\":\"t36.Exchanging\",\"fixed\":{},\"carries\":{\"vendor\":\"required\"}}";

    /**
     * XT (T41, design §14.2): the document a request sends was made by another template (the one that changes lines): the stood record is typed by
     * its own template, {@code of} naming its subtype, and the guards and the carrying read its bean. {@code OF} is replaced by the template's name
     * for the variant whose previous record is of a template with no subtype.
     */
    static final String FLOW_XT = "{\"@type\":\"flow\",\"name\":\"send_flow\",\"fullyQualifiedName\":\"" + PKG + ".flows.SendFlow\",\"package\":\"" + PKG + ".flows\","
            + "\"from\":\"t36.AskSend\",\"to\":\"t36.Sending\","
            + "\"stood\":{\"document\":{\"node\":\"document0\",\"madeBy\":\"document1\",\"addressed\":\"subject0\",\"of\":\"OF\","
            + "              \"identity\":{\"user\":\"reference\",\"backend\":\"name1\"}}},"
            + "\"ask\":{\"agent\":{\"supplied\":\"caller\"},\"time\":{\"supplied\":\"clock\"}},"
            + "\"record\":{\"agent\":\"asked\",\"time\":\"asked\",\"atype\":{\"constant\":\"Sending\"},\"vendor\":\"asked\",\"attributesOther\":\"absent\"},"
            + "\"carry\":{\"document\":[\"name1\"]},"
            + "\"reply\":{\"answer\":{\"outcome\":{\"when\":{\"made\":\"document1\"},\"is\":\"accepted\",\"otherwise\":\"failed\"},\"result\":{\"recorded\":\"name1\"},\"code\":\"absent\"},"
            + "          \"refused\":{\"outcome\":{\"constant\":\"refused\"},\"code\":{\"argument\":true},\"result\":\"absent\"},"
            + "          \"failed\":{\"outcome\":{\"constant\":\"failed\"},\"code\":\"absent\",\"result\":\"absent\"}}}";

    /** The catalogue: the two templates, their subtypes and the given flow entries (before the others when {@code first}). */
    ObjectNode catalogue(String id, List<String> entries) throws Exception {
        String dir = OUT + id + "/";
        FileUtils.deleteDirectory(new File(dir));
        new File(dir).mkdirs();
        Files.copy(Path.of(RES + "changing-bindings.json"), Path.of(dir + "changing.json"));
        Files.copy(Path.of(RES + "inquiry-bindings.json"), Path.of(dir + "inquiry.json"));
        Files.copy(Path.of(RES + "exchanging-bindings.json"), Path.of(dir + "exchanging.json"));
        ObjectNode c = (ObjectNode) om.readTree("{\n"
                + "  \"group\": \"org.example\", \"name\": \"t36\", \"destination\": \"" + dir + "libs\",\n"
                + "  \"description\": \"T36 part E\", \"version\": \"0.1.0\", \"integrator\": true,\n"
                + "  \"root_package\": \"" + PKG + ".catalogue\", \"jsweet\": false,\n"
                + "  \"script\": \"t36_run\", \"script_dir\": \"" + dir + "libs/bin\",\n"
                + "  \"jsonschema\": \"schema.json\", \"sqlFile\": \"template.sql\", \"documentation\": \"doc.html\",\n"
                + "  \"templates\": [ ] }");
        ArrayNode templates = (ArrayNode) c.get("templates");
        templates.add(om.readTree("{ \"@type\": \"simple\", \"fullyQualifiedName\": \"t36.Changing\", \"name\": \"changing\","
                + " \"package\": \"" + PKG + ".backend\", \"template\": \"t36.changing\", \"cbindings\": \"" + dir + "changing.json\" }"));
        templates.add(om.readTree("{ \"@type\": \"simple\", \"fullyQualifiedName\": \"t36.Inquiry\", \"name\": \"inquiry\","
                + " \"package\": \"" + PKG + ".actions\", \"template\": \"t36.inquiry\", \"cbindings\": \"" + dir + "inquiry.json\" }"));
        templates.add(om.readTree("{ \"@type\": \"simple\", \"fullyQualifiedName\": \"t36.Exchanging\", \"name\": \"exchanging\","
                + " \"package\": \"" + PKG + ".backend\", \"template\": \"t36.exchanging\", \"cbindings\": \"" + dir + "exchanging.json\" }"));
        for (String e : entries) templates.add(om.readTree(e));
        return c;
    }

    /** Runs the whole pipeline on the catalogue; the log goes to the catalogue's directory. */
    void generate(String id, List<String> entries, java.util.function.Consumer<ObjectNode> tweak) throws Exception {
        ObjectNode c = catalogue(id, entries);
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
    }

    void generate(String id, List<String> entries) throws Exception {
        generate(id, entries, c -> {});
    }

    String read(String id, String relative) throws Exception {
        return Files.readString(Path.of(OUT + id + "/libs/t36/t36_cli/src/main/" + relative));
    }

    /** The generated tree of a catalogue, normalised as the other T36 tests do: path to text. */
    Map<String, String> tree(String id) throws Exception {
        Map<String, String> res = new TreeMap<>();
        Path root = Path.of(OUT + id + "/libs");
        try (java.util.stream.Stream<Path> s = Files.walk(root)) {
            for (Path p : s.filter(Files::isRegularFile).collect(java.util.stream.Collectors.toList())) {
                if (p.toString().contains("/classes/")) continue;
                String text = new String(Files.readAllBytes(p), StandardCharsets.UTF_8).replace("t36f/" + id + "/", "t36f/CASE/");
                text = text.replaceAll("buildDate = \"[^\"]*\"", "buildDate = \"N\"").replaceAll("tmp_Conv[0-9]+", "tmp_Conv<n>").replaceAll("at line [0-9]+", "at line N");
                res.put(root.relativize(p).toString(), text);
            }
        }
        return res;
    }

    ClassLoader compile(String id, List<String> relativePaths) throws Exception {
        return T36Fixture.compileIn(OUT, id, relativePaths, getClass().getClassLoader());
    }
}
