package org.openprovenance.prov.service.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests {@link TraversalBounds}: how the navigator's and the slicer's request parameters become
 * the two trailing arguments of a bounded traversal call.  Pure parsing, no database.
 */
public class TraversalBoundsTest {

    private static Map<String, String> params(String... kv) {
        Map<String, String> m = new HashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            m.put(kv[i], kv[i + 1]);
        }
        return m;
    }

    @Test
    public void absentKeysTakeTheNavigatorDefaults() {
        TraversalBounds b = TraversalBounds.fromParameters(params("direction", "forward"));
        assertEquals(Integer.valueOf(100), b.maxDepth());
        assertEquals(Integer.valueOf(2000), b.maxNodes());
        assertEquals("100, 2000", b.sqlArguments());
    }

    @Test
    public void noParametersAtAllTakeTheNavigatorDefaults() {
        assertEquals("100, 2000", TraversalBounds.fromParameters(null).sqlArguments());
    }

    @Test
    public void explicitValuesAreUsed() {
        assertEquals("5, 50", TraversalBounds.fromParameters(params("max-depth", "5", "max-nodes", " 50 ")).sqlArguments());
    }

    @Test
    public void noneOrBlankLiftsABound() {
        TraversalBounds b = TraversalBounds.fromParameters(params("max-depth", "none", "max-nodes", ""));
        assertNull(b.maxDepth());
        assertNull(b.maxNodes());
        assertEquals("NULL, NULL", b.sqlArguments());
        assertEquals("NULL, 2000", TraversalBounds.fromParameters(params("max-depth", "NONE")).sqlArguments());
    }

    @Test
    public void malformedValuesAreRejectedBeforeReachingSql() {
        for (String bad : new String[]{"0", "-3", "ten", "5; DROP TABLE x", "1.5", "2147483648"}) {
            try {
                TraversalBounds.fromParameters(params("max-nodes", bad));
                fail("accepted max-nodes=" + bad);
            } catch (IllegalArgumentException expected) {
                assertTrue(expected.getMessage(), expected.getMessage().startsWith("max-nodes must be a positive integer"));
            }
        }
    }

    @Test
    public void theCutMessageNamesTheBounds() {
        TraversalBounds b = TraversalBounds.fromParameters(params("max-nodes", "2000"));
        assertTrue(b.cutMessage(false), b.cutMessage(false).contains("depth 100, 2,000 nodes"));
        assertTrue(b.cutMessage(true), b.cutMessage(true).contains("may miss paths"));
        assertEquals("no depth limit, no node limit", new TraversalBounds(null, null).describe());
    }
}
