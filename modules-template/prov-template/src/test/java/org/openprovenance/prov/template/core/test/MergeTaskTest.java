package org.openprovenance.prov.template.core.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.template.core.ttf.BatchExecutor;
import org.openprovenance.prov.template.utils.TemplateIndex;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A merge task writes its output once per format, whatever the number of inputs, and records one merging per other
 * input; the tasks of a batch share the index of their output directory, written once per task.
 */
public class MergeTaskTest extends TestCase {

    /** The schema-check fixtures (a template and its bindings) copied to a fresh directory, with a TTF of three instantiates and their merge. */
    static Path batch() throws IOException {
        Path dir = Files.createTempDirectory("merge-ttf");
        Path fixtures = Path.of("src/test/resources/schema-check");
        try (Stream<Path> files = Files.walk(fixtures)) {
            for (Path p : (Iterable<Path>) files::iterator) {
                Path target = dir.resolve(fixtures.relativize(p).toString());
                if (Files.isDirectory(p)) Files.createDirectories(target); else Files.copy(p, target);
            }
        }
        StringBuilder tasks = new StringBuilder();
        for (String out : List.of("a", "b", "c")) {
            tasks.append("{\"type\": \"instantiate\", \"template\": \"sc/entity-project\", \"bindings\": \"sc/entity-project.json\","
                    + " \"output\": \"sc/" + out + "\", \"formats\": [\"jsonld\"]}, ");
        }
        tasks.append("{\"type\": \"merge\", \"inputs\": [\"sc/a\", \"sc/b\", \"sc/c\"], \"addOutputDirToInputPath\": true,"
                + " \"output\": \"sc/merged\", \"formats\": [\"jsonld\"], \"hasProvenance\": \"sc/prov/merged.prov-csv\"}");
        String config = "{\"template_path\": [\"templates\"], \"bindings_path\": [\"bindings\"], \"output_dir\": \"out\", \"tasks\": [" + tasks + "]}";
        Files.writeString(dir.resolve("config.json"), config, StandardCharsets.UTF_8);
        return dir;
    }

    /** Counts the documents it serialises. */
    static class CountingSerialiser extends ProvSerialiser {
        int count = 0;
        @Override
        public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
            count++;
            super.serialiseDocument(out, document, formatted);
        }
    }

    public void testMergeWritesEachFormatOnce() throws IOException {
        Path dir = batch();
        CountingSerialiser serialiser = new CountingSerialiser();
        BatchExecutor executor = new BatchExecutor(Map.of("jsonld", serialiser), Map.of("jsonld", new ProvDeserialiser()));
        assertEquals(0, executor.execute(dir.toString(), dir.toString(), "config.json"));

        // three instantiated documents and the merged one: no rewrite of the merged document per input
        assertEquals(4, serialiser.count);
        String merged = Files.readString(dir.resolve("out/sc/merged.jsonld"), StandardCharsets.UTF_8);
        assertTrue(merged, merged.contains("ex:shed"));

        // one merging record per other input (and per format)
        List<String> records = Files.readAllLines(dir.resolve("out/sc/prov/merged.prov-csv"), StandardCharsets.UTF_8);
        assertEquals(records.toString(), 2, records.size());
        assertTrue(records.get(0), records.get(0).contains("sc/merged.jsonld"));

        // the shared index of the output directory holds every task's entries
        Map<String, Map<String, Object>> index = new ObjectMapper().readValue(dir.resolve("out/index.json").toFile(), Map.class);
        for (String id : List.of("sc/a", "sc/b", "sc/c", "sc/merged")) {
            assertEquals(id, "sc/" + id.substring(3) + ".jsonld", index.get(id).get("jsonld"));
        }
        assertNotNull(index.get("sc/merged").get("hasProvenance"));
    }

    /** An entry put is held until the index is persisted; an entry added is written at once. */
    public void testIndexPersistedOnDemand() throws IOException {
        Path dir = Files.createTempDirectory("index");
        TemplateIndex index = new TemplateIndex(dir.toString(), true);
        assertTrue(Files.exists(dir.resolve("index.json")));   // built, as the directory had none

        index.putEntry("x/y", "provn", "x/y.provn");
        assertNull(new TemplateIndex(dir.toString(), true).get("x/y", List.of(org.openprovenance.prov.template.utils.TemplateExtension.PROVN)));
        index.persist();
        assertEquals("x/y.provn", new TemplateIndex(dir.toString(), true).get("x/y", List.of(org.openprovenance.prov.template.utils.TemplateExtension.PROVN)));

        index.addEntry("x/z", "provn", "x/z.provn");
        assertEquals("x/z.provn", new TemplateIndex(dir.toString(), true).get("x/z", List.of(org.openprovenance.prov.template.utils.TemplateExtension.PROVN)));
    }
}
