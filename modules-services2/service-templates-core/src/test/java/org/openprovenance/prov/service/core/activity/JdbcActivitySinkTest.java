package org.openprovenance.prov.service.core.activity;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Exercises {@link JdbcActivitySink} and {@link ActivityQuery} against a real PostgreSQL:
 * the DDL, the adding upsert, the descriptions, the summary and series queries, pruning.
 * Runs only when the system property {@code TPL_ACTIVITY_TEST_JDBC_URL} names a database
 * (for instance {@code jdbc:postgresql://localhost:5432/test?user=u&password=p}); skipped otherwise.
 */
public class JdbcActivitySinkTest {
    static final String PROPERTY = "TPL_ACTIVITY_TEST_JDBC_URL";

    private String url;

    @Before
    public void requireADatabase() {
        url = System.getProperty(PROPERTY);
        Assume.assumeNotNull("set -D" + PROPERTY + " to run this test", url);
    }

    private Connection connect() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Instant T0 = Instant.parse("2026-10-24T10:00:00Z");

    @Test
    public void writesAddUpAndTheQueriesReadThemBack() throws Exception {
        try (Connection c = connect(); Statement st = c.createStatement()) {
            st.execute("DROP TABLE IF EXISTS " + ActivityLogger.TABLE + "; DROP TABLE IF EXISTS " + ActivityLogger.KIND_TABLE);
        }
        JdbcActivitySink sink = new JdbcActivitySink(this::connect, "test");
        sink.initialize();
        sink.initialize();                                              // idempotent

        Instant now = T0.plus(Duration.ofSeconds(35));
        sink.write(List.of(
                new ActivityLogger.Row(T0, "ps.statements", 2),
                new ActivityLogger.Row(T0, "ps.records", 9),
                new ActivityLogger.Row(T0.plusSeconds(10), "ps.statements", 1),
                new ActivityLogger.Row(T0.minus(Duration.ofHours(2)), "ps.statements", 100),
                new ActivityLogger.Row(T0.minus(Duration.ofDays(2)), "ps.statements", 1000),
                new ActivityLogger.Row(T0.minus(Duration.ofDays(10)), "ps.statements", 7)));  // outside the week
        sink.write(List.of(new ActivityLogger.Row(T0, "ps.statements", 3)));    // same bucket: adds, 2 + 3
        sink.describe(Map.of("ps.statements", "messages", "chronicle.events", "events"));
        sink.describe(Map.of("ps.statements", "messages posted"));              // replaces

        ActivityQuery q = new ActivityQuery(connect());
        List<ActivityQuery.KindSummary> summary = q.summary(now);
        assertEquals(3, summary.size());                                        // two counted, one only described
        ActivityQuery.KindSummary chronicle = summary.get(0);
        assertEquals("chronicle.events", chronicle.kind);
        assertEquals("events", chronicle.description);
        assertEquals(0, chronicle.week);
        assertNull(chronicle.lastSeen);
        ActivityQuery.KindSummary records = summary.get(1);
        assertEquals("ps.records", records.kind);
        assertNull(records.description);
        assertEquals(9, records.minute);
        ActivityQuery.KindSummary statements = summary.get(2);
        assertEquals("ps.statements", statements.kind);
        assertEquals("messages posted", statements.description);
        assertEquals(6, statements.minute);                                     // 2 + 3 + 1
        assertEquals(6, statements.hour);
        assertEquals(106, statements.day);
        assertEquals(1106, statements.week);
        assertEquals(T0.plusSeconds(10), statements.lastSeen);

        List<ActivityQuery.Point> minute = q.series(ActivityQuery.Window.MINUTE, now);
        assertEquals(3, minute.size());                                         // (T0, records), (T0, statements), (T0+10, statements)
        assertEquals(T0, minute.get(0).step);
        assertEquals(5, minute.stream().filter(p -> p.kind.equals("ps.statements") && p.step.equals(T0)).findFirst().orElseThrow().count);

        List<ActivityQuery.Point> hour = q.series(ActivityQuery.Window.HOUR, now);
        assertEquals(2, hour.size());                                           // both buckets fold into the minute starting at T0
        assertTrue(hour.stream().allMatch(p -> p.step.equals(T0)));
        assertEquals(6, hour.stream().filter(p -> p.kind.equals("ps.statements")).findFirst().orElseThrow().count);

        sink.prune(T0.minus(Duration.ofDays(7)));
        assertEquals(1106, q.summary(now).get(2).week);                         // the 10-day-old row is gone, the rest untouched
        List<ActivityQuery.Point> week = q.series(ActivityQuery.Window.WEEK, now);
        assertNotNull(week);
        assertEquals(1106 + 9, week.stream().mapToLong(p -> p.count).sum());
        sink.close();
    }
}
