package org.openprovenance.prov.service.core;

import org.junit.Test;
import org.openprovenance.prov.service.core.TemplateQuery.TemplateConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link TemplateQuery#shortestPath}, which narrows a slice to a single path when
 * the slicer is asked for one path rather than all of them.
 *
 * <p>Pure graph logic over an already-materialised connection list: no database, no
 * service context.
 */
public class TemplateQuerySlicePathTest {

    /** A connection: entity produced by {@code producer} at {@code out}, consumed by {@code consumer} at {@code in}. */
    private static TemplateConnection connection(String producerTemplate, Integer producerId, String out,
                                                 String consumerTemplate, Integer consumerId, String in) {
        TemplateConnection c = new TemplateConnection();
        c.out_template = producerTemplate;
        c.out_id       = producerId;
        c.out_property = out;
        c.in_template  = consumerTemplate;
        c.in_id        = consumerId;
        c.in_property  = in;
        return c;
    }

    /** Shorthand for a connection between two records of the same template. */
    private static TemplateConnection hop(int producerId, int consumerId) {
        return connection("T", producerId, "document1", "T", consumerId, "document0");
    }

    /** Renders a path as "producer>consumer producer>consumer …" for readable assertions. */
    private static String render(List<TemplateConnection> path) {
        StringBuilder sb = new StringBuilder();
        for (TemplateConnection c : path) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(c.out_template).append(c.out_id).append('>').append(c.in_template).append(c.in_id);
        }
        return sb.toString();
    }

    private static String pathThrough(List<TemplateConnection> slice, Integer fromId, Integer toId) {
        return render(TemplateQuery.shortestPath(slice, "T", fromId, "T", toId));
    }

    @Test
    public void wholeChainIsThePathWhenTheSliceIsAChain() {
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(hop(1, 2), hop(2, 3), hop(3, 4)));
        assertEquals("T1>T2 T2>T3 T3>T4", pathThrough(slice, 1, 4));
    }

    @Test
    public void shorterRouteWinsOverLongerOne() {
        // 1 -> 4 directly, and 1 -> 2 -> 3 -> 4 the long way round.
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(hop(1, 2), hop(2, 3), hop(3, 4), hop(1, 4)));
        assertEquals("T1>T4", pathThrough(slice, 1, 4));
    }

    @Test
    public void equalLengthRoutesResolveToTheSamePathWhateverTheInputOrder() {
        // Two routes of equal length, through B and through A.
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(
                connection("T", 1, "document1", "B", 9, "document0"),
                connection("B", 9, "document1", "T", 4, "document0"),
                connection("T", 1, "document1", "A", 8, "document0"),
                connection("A", 8, "document1", "T", 4, "document0")));

        String expected = "T1>A8 A8>T4"; // ordered on the connection sort, so A before B
        assertEquals(expected, pathThrough(slice, 1, 4));

        for (long seed : new long[]{1L, 42L, 12345L}) {
            List<TemplateConnection> shuffled = new ArrayList<>(slice);
            Collections.shuffle(shuffled, new Random(seed));
            assertEquals("shuffled with seed " + seed, expected, pathThrough(shuffled, 1, 4));
        }
    }

    @Test
    public void sideInputsOffThePathAreLeftOut() {
        // The 1 -> 2 -> 3 chain, with records feeding into 2 and 3 from elsewhere.
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(
                hop(1, 2),
                hop(2, 3),
                connection("SIDE", 100, "document1", "T", 2, "part0"),
                connection("SIDE", 101, "document1", "T", 3, "part0")));
        assertEquals("T1>T2 T2>T3", pathThrough(slice, 1, 3));
    }

    @Test
    public void noPathWhenTheAnchorsAreNotConnectedInThatDirection() {
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(hop(1, 2), hop(2, 3)));
        assertEquals("", pathThrough(slice, 3, 1));
    }

    @Test
    public void noPathWhenBothAnchorsAreTheSameRecord() {
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(hop(1, 2), hop(2, 3)));
        assertEquals("", pathThrough(slice, 1, 1));
    }

    @Test
    public void emptySliceYieldsEmptyPath() {
        assertEquals("", pathThrough(new ArrayList<>(), 1, 4));
    }

    @Test
    public void missingAnchorYieldsEmptyPathRatherThanFailing() {
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(hop(1, 2), hop(2, 3)));
        assertEquals("", render(TemplateQuery.shortestPath(slice, "T", null, "T", 3)));
        assertEquals("", render(TemplateQuery.shortestPath(slice, "T", 1, null, 3)));
        assertEquals("", render(TemplateQuery.shortestPath(null, "T", 1, "T", 3)));
    }

    @Test
    public void aCycleInTheSliceTerminates() {
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(hop(1, 2), hop(2, 1), hop(2, 3)));
        assertEquals("T1>T2 T2>T3", pathThrough(slice, 1, 3));
    }

    @Test
    public void connectionsCarryingNoPropertyDoNotUpsetTheOrdering() {
        // Dead-end connections come back with a null property; sorting must tolerate them.
        List<TemplateConnection> slice = new ArrayList<>(Arrays.asList(
                connection("T", 1, null, "T", 2, null),
                hop(2, 3)));
        assertEquals("T1>T2 T2>T3", pathThrough(slice, 1, 3));
    }
}
