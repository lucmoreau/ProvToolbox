package org.openprovenance.bench.throughput;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** The JSONL mode's lines: a record's complete bindings; and the check of the files it wrote. */
public class JsonlTest {

    private static final ObjectMapper OM = new ObjectMapper();

    private static Family.Template named(Family f, String isA) {
        return f.templates.stream().filter(t -> t.isA.endsWith(isA)).findFirst().orElseThrow();
    }

    @Test
    public void aSimpleRecordsBindingsCarryItsIdAndItsOutputs() throws Exception {
        Family fs = Family.fs(3);
        AtomicLong ids = new AtomicLong(100);
        Family.Template t = named(fs, ".FileTransforming");
        JsonNode b = OM.readTree(OM.writeValueAsBytes(fs.bindings(t, fs.entry(t), ids)));
        assertEquals("org.openprovenance.templates.fs.FileTransforming", b.get("isA").asText());
        assertEquals(101, b.get("ID").asLong());
        assertEquals(102, b.get("transformed_file").asLong());
        assertEquals(103, b.get("transforming").asLong());
        assertTrue(b.get("filename").asText().startsWith("filename-"));
        assertTrue(b.has("file") && b.has("method") && b.has("engineer") && b.has("path") && b.has("time"));
    }

    @Test
    public void aCompositesElementsShareItsSharedOutputs() throws Exception {
        Family tr = Family.tr(3);
        AtomicLong ids = new AtomicLong();
        Family.Template t = named(tr, ".PackingComposite");
        JsonNode b = OM.readTree(OM.writeValueAsBytes(tr.bindings(t, tr.entry(t), ids)));
        JsonNode elements = b.get("__elements");
        assertEquals(3, elements.size());
        Set<Long> recordIds = new HashSet<>();
        Set<Long> item1 = new HashSet<>();
        recordIds.add(b.get("ID").asLong());
        for (JsonNode e : elements) {
            assertEquals(elements.get(0).get("container1").asLong(), e.get("container1").asLong());   // shared: one id
            assertEquals(elements.get(0).get("adding").asLong(), e.get("adding").asLong());
            assertTrue("a shared output is an id, not the token", e.get("container1").asLong() > 0);
            recordIds.add(e.get("ID").asLong());
            item1.add(e.get("item1").asLong());                                                      // not shared: one each
        }
        assertEquals(4, recordIds.size());
        assertEquals(3, item1.size());
    }

    @Test
    public void everySimpleTemplateHasItsOutputs() {
        for (Family f : List.of(Family.fs(3), Family.tr(3))) {
            for (Family.Template t : f.templates) {
                String simple = t.composite() ? t.element.isA : t.isA;
                assertTrue(simple, Family.OUTPUTS.containsKey(simple));
            }
        }
    }

    @Test
    public void theFilesCheckCountsBytesLinesAndBadLines() throws Exception {
        File dir = Files.createTempDirectory("provbench-jsonl").toFile();
        File good = new File(dir, "provbench-fs-w0.jsonl");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2500; i++) sb.append(OM.writeValueAsString(Map.of("isA", "t", "ID", i))).append('\n');
        Files.writeString(good.toPath(), sb.toString(), StandardCharsets.UTF_8);
        long[] c = JsonlRun.count(List.of(good));
        assertArrayEquals(new long[]{good.length(), 2500, 0}, c);
        File bad = new File(dir, "provbench-fs-w1.jsonl");
        Files.writeString(bad.toPath(), "{\"isA\":\"t\"}\n{\"isA\":", StandardCharsets.UTF_8);   // unterminated
        assertEquals(1, JsonlRun.count(List.of(bad))[2]);
    }
}
