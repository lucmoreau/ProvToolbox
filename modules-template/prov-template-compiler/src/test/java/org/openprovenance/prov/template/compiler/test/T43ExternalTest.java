package org.openprovenance.prov.template.compiler.test;

import tools.jackson.databind.node.ObjectNode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * T43: a template of the catalogue that belongs to another library ({@code "external": true}) has its classes written under
 * {@code src/external/java}, not {@code src/main/java}, and its packages listed in {@code external-packages.txt}. Nothing else changes:
 * the same files with the same contents, the ones of the external packages moved.
 */
public class T43ExternalTest extends T36Fixture {

    static final String PACKAGE_DIR = "org/example/t36/approval/";

    static void markExternal(ObjectNode catalogue) {
        catalogue.get("templates").forEach(t -> ((ObjectNode) t).put("external", true));
    }

    static String libs(String id) {
        return OUT + id + "/libs/";
    }

    public void testTheClassesOfAnExternalTemplateAreWrittenApartAndNothingElseChanges() throws Exception {
        Map<String, String> plain = generate("t43plain", true, List.of(), List.of());
        Map<String, String> external = generate("t43ext", true, List.of(), List.of(), T43ExternalTest::markExternal);

        // the classes of the package are in the main roots without the flag, and in none of them with it
        Map<String, String> expected = new TreeMap<>();
        int moved = 0;
        for (Map.Entry<String, String> e : plain.entrySet()) {
            String key = e.getKey();
            if (key.contains("/src/main/java/" + PACKAGE_DIR)) {
                moved++;
                key = key.replace("/src/main/java/" + PACKAGE_DIR, "/src/external/java/" + PACKAGE_DIR);
            }
            expected.put(key, e.getValue());
        }
        assertTrue("some classes are of the package (" + moved + ")", moved > 10);

        Map<String, String> actual = new TreeMap<>(external);
        assertEquals("the list of the packages is the only file added", "t36/external-packages.txt", onlyIn(actual, expected));
        actual.remove("t36/external-packages.txt");
        // the two modules' POMs differ by the external root, which is asserted below
        for (String module : List.of("t36_cli", "t36_l2p")) {
            assertFalse(module + "'s POM says where the external root is", expected.remove("t36/" + module + "/pom.xml").equals(actual.remove("t36/" + module + "/pom.xml")));
        }
        assertEquals("the same files, with the same contents, the package's moved", expected, actual);
        assertEquals("org/example/t36/approval\n", Files.readString(Path.of(libs("t43ext") + "t36/external-packages.txt")));
        assertFalse("nothing is listed without the flag", Files.exists(Path.of(libs("t43plain") + "t36/external-packages.txt")));
        assertFalse("no external root without the flag", Files.exists(Path.of(libs("t43plain") + "t36/t36_cli/src/external")));
        assertTrue(Files.isDirectory(Path.of(libs("t43ext") + "t36/t36_cli/src/external/java/" + PACKAGE_DIR)));
        assertTrue(Files.isDirectory(Path.of(libs("t43ext") + "t36/t36_l2p/src/external/java/" + PACKAGE_DIR)));
        assertFalse("not left in the main root", Files.exists(Path.of(libs("t43ext") + "t36/t36_cli/src/main/java/" + PACKAGE_DIR)));
        assertFalse("not left in the backend's main root", Files.exists(Path.of(libs("t43ext") + "t36/t36_l2p/src/main/java/" + PACKAGE_DIR)));

        // the generated modules compile the external root with the others, as they compiled everything in src/main/java before
        for (String module : List.of("t36_cli", "t36_l2p")) {
            String pom = Files.readString(Path.of(libs("t43ext") + "t36/" + module + "/pom.xml"));
            assertTrue(module + " adds the external root", pom.contains("${project.basedir}/src/external/java"));
            assertFalse(module + " has no such root without the flag", Files.readString(Path.of(libs("t43plain") + "t36/" + module + "/pom.xml")).contains("src/external"));
        }
    }

    /** The one key of {@code actual} that {@code expected} does not have. */
    static String onlyIn(Map<String, String> actual, Map<String, String> expected) {
        List<String> extra = actual.keySet().stream().filter(k -> !expected.containsKey(k)).toList();
        assertEquals("one file added: " + extra, 1, extra.size());
        return extra.get(0);
    }

    public void testATemplateInTheSamePackageThatIsNotExternalIsRefused() throws Exception {
        // the fixture's subtype is in the package of the approval template
        try {
            generate("t43clash", false, List.of(), List.of(subtype("approval_small", "{\"approver_name\":\"Other\",\"level\":1}", "{\"lineCount\":\"required\"}")),
                    c -> ((ObjectNode) c.get("templates").get(0)).put("external", true));
            fail("a subtype written into an external package would be left out with it");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("approval_small") || e.getMessage().contains("t31.approval_small"));
            assertTrue(e.getMessage(), e.getMessage().contains("inside the package org.example.t36.approval"));
        }
    }

    public void testWithoutTheFlagTheOutputIsWhatItWas() throws Exception {
        Map<String, String> plain = generate("t43plain2", false, List.of(), List.of());
        assertTrue(plain.keySet().stream().noneMatch(k -> k.contains("/src/external/") || k.endsWith("external-packages.txt")));
    }
}
