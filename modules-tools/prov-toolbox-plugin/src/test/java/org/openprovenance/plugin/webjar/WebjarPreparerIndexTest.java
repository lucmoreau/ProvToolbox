package org.openprovenance.plugin.webjar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * prepare-webjar copies several template directories into one target (a library's hand-written templates, then its
 * generated ones). Their index.json files are merged: before, the last one replaced the others, and the
 * provenance-templates-library jar's index lost every primitive template its source index named (nextgen T-543).
 */
public class WebjarPreparerIndexTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private final ObjectMapper mapper = new ObjectMapper();

    private File dir(String name, String index, String... files) throws IOException {
        File d = tmp.newFolder(name);
        Files.writeString(d.toPath().resolve("index.json"), index, StandardCharsets.UTF_8);
        for (String f : files) {
            Path p = d.toPath().resolve(f);
            Files.createDirectories(p.getParent());
            Files.writeString(p, name + ":" + f, StandardCharsets.UTF_8);
        }
        return d;
    }

    private JsonNode copyBoth(File first, File second, File target) throws IOException {
        WebjarPreparer w = new WebjarPreparer();
        w.copyFromSourceToDestination(first.getPath(), target.getPath(), null, true);
        w.copyFromSourceToDestination(second.getPath(), target.getPath(), null, true);
        return mapper.readTree(target.toPath().resolve("index.json").toFile());
    }

    @Test
    public void theIndexesOfTwoTemplateDirectoriesAreMerged() throws IOException {
        File hand = dir("hand", "{\"t.primitive.Entity0\": {\"provn\": \"t/primitive/entity0.provn\"}}", "t/primitive/entity0.provn");
        File gen = dir("gen", "{\"t.generic.Transforming1\": {\"jsonld\": \"t/generic/transforming1.jsonld\"}}", "t/generic/transforming1.jsonld");
        File target = tmp.newFolder("target");
        JsonNode index = copyBoth(hand, gen, target);
        assertEquals(2, index.size());
        assertEquals("t/primitive/entity0.provn", index.path("t.primitive.Entity0").path("provn").asText());
        assertEquals("t/generic/transforming1.jsonld", index.path("t.generic.Transforming1").path("jsonld").asText());
        assertTrue(new File(target, "t/primitive/entity0.provn").exists());
        assertTrue(new File(target, "t/generic/transforming1.jsonld").exists());
    }

    @Test
    public void anIdenticalEntryIsKeptOnce() throws IOException {
        String same = "{\"t.primitive.Entity0\": {\"provn\": \"t/primitive/entity0.provn\"}}";
        JsonNode index = copyBoth(dir("a", same), dir("b", same), tmp.newFolder("target"));
        assertEquals(1, index.size());
    }

    @Test
    public void anIdNamedWithTwoDifferentEntriesIsRefused() throws IOException {
        File a = dir("a", "{\"t.primitive.Entity0\": {\"provn\": \"t/primitive/entity0.provn\"}}");
        File b = dir("b", "{\"t.primitive.Entity0\": {\"provn\": \"elsewhere/entity0.provn\"}}");
        try {
            copyBoth(a, b, tmp.newFolder("target"));
            fail("two entries for one template id were accepted");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("t.primitive.Entity0"));
        }
    }

    @Test
    public void theOldCopyKeepsOnlyTheLastIndex() throws IOException {
        // the behaviour before the merge, still that of every other kind of file the mojo copies (bindings, js, …)
        File hand = dir("hand", "{\"t.primitive.Entity0\": {\"provn\": \"t/primitive/entity0.provn\"}}");
        File gen = dir("gen", "{\"t.generic.Transforming1\": {\"jsonld\": \"t/generic/transforming1.jsonld\"}}");
        File target = tmp.newFolder("target");
        WebjarPreparer w = new WebjarPreparer();
        w.copyFromSourceToDestination(hand.getPath(), target.getPath(), null);
        w.copyFromSourceToDestination(gen.getPath(), target.getPath(), null);
        JsonNode index = mapper.readTree(target.toPath().resolve("index.json").toFile());
        assertFalse("the plain copy replaces the index", index.has("t.primitive.Entity0"));
    }
}
