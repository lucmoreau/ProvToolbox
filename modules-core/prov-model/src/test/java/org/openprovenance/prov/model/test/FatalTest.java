package org.openprovenance.prov.model.test;

import junit.framework.TestCase;
import org.openprovenance.prov.configuration.Fatal;

import java.sql.SQLException;
import java.time.Instant;

public class FatalTest extends TestCase {

    final Instant at = Instant.parse("2026-10-28T16:05:07.123Z");

    public void testTheLine() {
        String l = Fatal.line("store", "the init SQL /init.sql could not be run", new SQLException("relation exists"), at);
        assertEquals("FATAL service=store at=2026-10-28T16:05:07Z reason=\"the init SQL /init.sql could not be run\""
                + " cause=\"SQLException: relation exists\"", l);
    }

    public void testOneLineWhateverTheMessage() {
        String l = Fatal.line("prov store", "a \"quoted\"\nreason", new IllegalStateException("two\r\nlines"), at);
        assertFalse(l.contains("\n"));
        assertFalse(l.contains("\r"));
        assertTrue(l, l.startsWith("FATAL service=prov-store at="));
        assertTrue(l, l.contains("reason=\"a 'quoted' reason\""));
    }

    public void testNoCause() {
        assertEquals("FATAL service=unknown at=2026-10-28T16:05:07Z reason=\"\"", Fatal.line(null, null, null, at));
    }

    public void testALongCauseIsCut() {
        String l = Fatal.line("store", "r", new RuntimeException("x".repeat(1000)), at);
        assertTrue(l.length() < 400);
        assertTrue(l.endsWith("…\""));
    }
}
