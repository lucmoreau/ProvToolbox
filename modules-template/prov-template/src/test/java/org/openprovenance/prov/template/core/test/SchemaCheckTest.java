package org.openprovenance.prov.template.core.test;

import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.template.core.exception.SchemaCheckException;
import org.openprovenance.prov.template.core.ttf.BatchExecutor;
import org.openprovenance.prov.template.core.ttf.SchemaCheck;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A TTF's schemaCheck: none (or absent) checks nothing, default is the version ProvToolbox writes, a date names a
 * bundled version of the PROV-JSONLD schema; a generated jsonld output that does not conform fails the execution.
 */
public class SchemaCheckTest extends TestCase {

    /** An openprov document with a node reference under an extension property: 2026-10-14 only. */
    static final String OPENPROV_DOC = "{\"@context\": [{\"ex\": \"http://example.org/\"}, \"https://openprovenance.org/ns/openprov.jsonld\"],"
            + " \"@graph\": [{\"@type\": \"Attribution\", \"@id\": \"ex:att\", \"entity\": \"ex:e\", \"agent\": \"ex:ag\","
            + " \"association\": \"ex:asc\", \"ex:project\": [{\"@id\": \"ex:shed\"}]}]}";

    /** A PROV-JSONLD document whose type merely contains Entity: 2024-08-25 only (anchored patterns from 2026-09-12). */
    static final String SPEC_DOC = "{\"@context\": [{\"ex\": \"http://example.org/\"}, \"https://openprovenance.org/prov-jsonld/context.jsonld\"],"
            + " \"@graph\": [{\"@type\": \"EntityX\", \"@id\": \"ex:e\"}]}";

    static File file(String json) throws IOException {
        Path p = Files.createTempFile("schema-check", ".jsonld");
        Files.writeString(p, json, StandardCharsets.UTF_8);
        p.toFile().deleteOnExit();
        return p.toFile();
    }

    public void testVersion() {
        assertNull(SchemaCheck.version(null));
        assertNull(SchemaCheck.version("none"));
        assertEquals(Constants.JSONLD_SCHEMA_DEFAULT_VERSION, SchemaCheck.version("default"));
        for (String v : Constants.JSONLD_SCHEMA_VERSIONS) assertEquals(v, SchemaCheck.version(v));
        try {
            SchemaCheck.version("2025-01-01");
            fail("an unbundled version was accepted");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("none, default, 2024-08-25, 2026-09-12, 2026-10-14"));
        }
    }

    public void testTaskOverridesBatch() {
        assertEquals("2024-08-25", SchemaCheck.effective("default", "2024-08-25"));
        assertEquals("none", SchemaCheck.effective("default", "none"));
        assertEquals("default", SchemaCheck.effective("default", null));
        assertNull(SchemaCheck.effective(null, null));
    }

    public void testNodeReferenceInOpenprovDocument() throws IOException {
        File f = file(OPENPROV_DOC);
        assertEquals(List.of(), SchemaCheck.violations(f, "2026-10-14"));
        assertFalse(SchemaCheck.violations(f, "2026-09-12").isEmpty());
        assertFalse(SchemaCheck.violations(f, "2024-08-25").isEmpty());
    }

    public void testAnchoredTypePatterns() throws IOException {
        File f = file(SPEC_DOC);
        assertEquals(List.of(), SchemaCheck.violations(f, "2024-08-25"));
        assertFalse(SchemaCheck.violations(f, "2026-09-12").isEmpty());
        assertFalse(SchemaCheck.violations(f, "2026-10-14").isEmpty());
    }

    /** A TTF with one instantiate task, its template and bindings copied to a fresh directory; the output is jsonld. */
    static Path batch(String batchSchemaCheck, String taskSchemaCheck) throws IOException {
        Path dir = Files.createTempDirectory("schema-check-ttf");
        Path fixtures = Path.of("src/test/resources/schema-check");
        try (Stream<Path> files = Files.walk(fixtures)) {
            for (Path p : (Iterable<Path>) files::iterator) {
                Path target = dir.resolve(fixtures.relativize(p).toString());
                if (Files.isDirectory(p)) Files.createDirectories(target); else Files.copy(p, target);
            }
        }
        String config = "{\"template_path\": [\"templates\"], \"bindings_path\": [\"bindings\"], \"output_dir\": \"out\","
                + (batchSchemaCheck == null ? "" : " \"schemaCheck\": \"" + batchSchemaCheck + "\",")
                + " \"tasks\": [{\"type\": \"instantiate\", \"template\": \"sc/entity-project\", \"bindings\": \"sc/entity-project.json\","
                + " \"output\": \"sc/entity-project\", \"formats\": [\"jsonld\"]"
                + (taskSchemaCheck == null ? "" : ", \"schemaCheck\": \"" + taskSchemaCheck + "\"") + "}]}";
        Files.writeString(dir.resolve("config.json"), config, StandardCharsets.UTF_8);
        return dir;
    }

    static int run(Path dir) {
        BatchExecutor executor = new BatchExecutor(Map.of("jsonld", new ProvSerialiser()), Map.of("jsonld", new ProvDeserialiser()));
        return executor.execute(dir.toString(), dir.toString(), "config.json");
    }

    public void testInstantiateTask() throws IOException {
        String output = "out/sc/entity-project.jsonld";
        for (String[] passing : new String[][] { {null, null}, {"none", null}, {"2026-10-14", null}, {"default", null}, {"2024-08-25", "default"} }) {
            Path dir = batch(passing[0], passing[1]);
            assertEquals(String.join("/", String.valueOf(passing[0]), String.valueOf(passing[1])), 0, run(dir));
            String written = Files.readString(dir.resolve(output), StandardCharsets.UTF_8);
            assertTrue(written, written.contains("\"@id\" : \"ex:shed\""));
        }
        // the task's 2024-08-25 overrides the batch's default: its node reference does not conform
        Path dir = batch("default", "2024-08-25");
        try {
            run(dir);
            fail("a non-conforming output passed schemaCheck 2024-08-25");
        } catch (SchemaCheckException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("schemaCheck 2024-08-25: sc/entity-project.jsonld does not conform"));
        }
    }
}
