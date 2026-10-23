package org.openprovenance.prov.service.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openprovenance.prov.template.compiler.common.Constants.INPUT;
import static org.openprovenance.prov.template.compiler.common.Constants.OUTPUT;

/**
 * Tests {@link TemplateQuery#collectBackwardDispatchRows(Map, Map)}: which cells a backward walk
 * may arrive at. Pure derivation from an I/O map, no database.
 *
 * <p>A small catalogue over two entity tables, {@code thing} and {@code plan}:
 * <ul>
 *   <li>{@code making}: produces {@code thing1} from {@code thing0};</li>
 *   <li>{@code annotating}: an overlay — produces only its activity, but asserts a derivation
 *       of {@code thing1} (an input) from {@code note0}, so {@code thing1} is a subject;</li>
 *   <li>{@code running}: produces {@code result1} (in {@code thing}) under the plan {@code plan0},
 *       which it merely uses;</li>
 *   <li>{@code publishing}: produces {@code plan1} from {@code plan0}.</li>
 * </ul>
 * Every template also takes an {@code actor}, in the {@code actor} table, where none produces anything.
 */
public class BackwardDispatchRowsTest {

    private static Map<String, String> vars(String... kv) {
        Map<String, String> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) m.put(kv[i], kv[i + 1]);
        return m;
    }

    private static Map<String, Map<String, Map<String, String>>> catalogue() {
        Map<String, Map<String, String>> input = new LinkedHashMap<>();
        Map<String, Map<String, String>> output = new LinkedHashMap<>();
        input.put("making",     vars("thing0", "thing", "actor", "actor"));
        output.put("making",    vars("thing1", "thing", "makingActivity", "activity"));
        input.put("annotating", vars("thing1", "thing", "note0", "note", "actor", "actor"));
        output.put("annotating", vars("annotatingActivity", "activity"));
        input.put("running",    vars("thing0", "thing", "plan0", "plan", "actor", "actor"));
        output.put("running",   vars("result1", "thing", "runningActivity", "activity"));
        input.put("publishing", vars("plan0", "plan", "actor", "actor"));
        output.put("publishing", vars("plan1", "plan", "publishingActivity", "activity"));
        Map<String, Map<String, Map<String, String>>> ioMap = new HashMap<>();
        ioMap.put(INPUT, input);
        ioMap.put(OUTPUT, output);
        return ioMap;
    }

    /** The subjects of each template's relations: what it derives, attributes or specialises. */
    private static Map<String, Set<String>> subjects() {
        Map<String, Set<String>> s = new HashMap<>();
        s.put("making",     new HashSet<>(List.of("thing1")));
        s.put("annotating", new HashSet<>(List.of("thing1")));          // asserted about an INPUT
        s.put("running",    new HashSet<>(List.of("result1")));          // plan0 is used, never a subject
        s.put("publishing", new HashSet<>(List.of("plan1")));
        return s;
    }

    private static Set<String> rows(Map<String, Set<String>> subjects) {
        return TemplateQuery.collectBackwardDispatchRows(catalogue(), subjects).stream()
                .map(r -> String.join(" ", r))
                .collect(Collectors.toSet());
    }

    @Test
    public void aDeclaredOutputIsAlwaysATarget() {
        Set<String> rows = rows(subjects());
        assertTrue(rows.contains("running thing0 making thing1"));
        assertTrue(rows.contains("annotating thing1 making thing1"));
        assertTrue(rows.contains("running plan0 publishing plan1"));   // a plan leads to its publishing
        assertTrue(rows.contains("making thing0 running result1"));
    }

    @Test
    public void anOverlayIsReachedThroughTheInputItAssertsAbout() {
        Set<String> rows = rows(subjects());
        assertTrue(rows.contains("making thing0 annotating thing1"));
        assertTrue(rows.contains("running thing0 annotating thing1"));
    }

    @Test
    public void aUsedInputIsNeverATarget() {
        Set<String> rows = rows(subjects());
        // the plan is an input of running and of publishing: no input-to-input hop between them
        assertFalse(rows.contains("publishing plan0 running plan0"));
        // thing0 is an input of making and of running: two consumers of one thing are siblings
        assertFalse(rows.contains("making thing0 running thing0"));
        assertFalse(rows.contains("running thing0 making thing0"));
        // the overlay's other inputs are not subjects either
        assertFalse(rows.contains("making actor annotating actor"));
        assertFalse(rows.contains("running actor annotating actor"));
    }

    @Test
    public void noTemplateProducesAnActorSoTheActorTableYieldsNoRow() {
        Set<String> rows = rows(subjects());
        assertTrue(rows.stream().noneMatch(r -> r.endsWith(" actor")));
    }

    @Test
    public void onlySubjectInputsAreTargetsAcrossTheWholeCatalogue() {
        Map<String, Map<String, String>> output = catalogue().get(OUTPUT);
        Map<String, Set<String>> subjects = subjects();
        for (String[] r : TemplateQuery.collectBackwardDispatchRows(catalogue(), subjects)) {
            String target = r[2], property = r[3];
            boolean declared = output.get(target).containsKey(property);
            boolean subject = subjects.get(target).contains(property);
            assertTrue(String.join(" ", r) + " arrives at neither a declared output nor a subject", declared || subject);
        }
    }

    @Test
    public void withoutSubjectsTheOverlayIsUnreachableAndNothingElseChanges() {
        Set<String> with = rows(subjects());
        Set<String> without = rows(null);
        assertEquals(Set.of("making thing0 annotating thing1", "running thing0 annotating thing1"),
                     with.stream().filter(r -> !without.contains(r)).collect(Collectors.toSet()));
        assertTrue(with.containsAll(without));
    }

    @Test
    public void rowsAreUniqueAndSelfJoinsOnOneCellAreSkipped() {
        List<String[]> rows = TemplateQuery.collectBackwardDispatchRows(catalogue(), subjects());
        assertEquals(rows.size(), rows.stream().map(r -> String.join(" ", r)).distinct().count());
        assertTrue(rows.stream().noneMatch(r -> r[0].equals(r[2]) && r[1].equals(r[3])));
    }
}
