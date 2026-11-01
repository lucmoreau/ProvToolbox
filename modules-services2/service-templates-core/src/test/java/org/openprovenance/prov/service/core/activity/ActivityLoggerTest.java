package org.openprovenance.prov.service.core.activity;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link ActivityLogger}'s in-memory bucketing and its contract with an
 * {@link ActivitySink}: no database, a hand-driven clock, no flushing thread.
 */
public class ActivityLoggerTest {

    /** A clock the test moves by hand. */
    static class ManualClock extends Clock {
        long millis;
        ManualClock(long millis) { this.millis = millis; }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return Instant.ofEpochMilli(millis); }
        @Override public long millis() { return millis; }
    }

    /** A sink that records what it is given, and fails on demand. */
    static class RecordingSink implements ActivitySink {
        final List<List<ActivityLogger.Row>> writes = new ArrayList<>();
        final Map<String, String> described = new HashMap<>();
        final List<Instant> prunes = new ArrayList<>();
        int initialized = 0;
        boolean failing = false;

        @Override public void initialize() throws Exception {
            if (failing) throw new RuntimeException("store down");
            initialized++;
        }
        @Override public void write(List<ActivityLogger.Row> rows) throws Exception {
            if (failing) throw new RuntimeException("store down");
            writes.add(new ArrayList<>(rows));
        }
        @Override public void describe(Map<String, String> descriptions) throws Exception {
            if (failing) throw new RuntimeException("store down");
            described.putAll(descriptions);
        }
        @Override public void prune(Instant before) { prunes.add(before); }

        /** kind -> bucket start -> total written, over every write. */
        Map<String, Map<Instant, Long>> totals() {
            Map<String, Map<Instant, Long>> t = new TreeMap<>();
            for (List<ActivityLogger.Row> w : writes) {
                for (ActivityLogger.Row r : w) {
                    t.computeIfAbsent(r.kind(), k -> new TreeMap<>()).merge(r.bucketStart(), r.count(), Long::sum);
                }
            }
            return t;
        }
    }

    private static final long T0 = 1_700_000_000_000L;   // a multiple of 10 s

    @Test
    public void countsAreAggregatedPerBucketAndKind() {
        ManualClock clock = new ManualClock(T0 + 3_000);
        RecordingSink sink = new RecordingSink();
        ActivityLogger l = new ActivityLogger(sink, 10, 0, 0, clock).start();

        l.count("ps.statements");
        l.count("ps.statements");
        l.count("ps.records", 7);
        clock.millis = T0 + 9_999;                        // same bucket
        l.count("ps.statements");
        clock.millis = T0 + 10_000;                       // next bucket
        l.count("ps.statements");

        List<ActivityLogger.Row> rows = l.flush();
        assertEquals(3, rows.size());
        Map<String, Map<Instant, Long>> t = sink.totals();
        assertEquals(Long.valueOf(3), t.get("ps.statements").get(Instant.ofEpochMilli(T0)));
        assertEquals(Long.valueOf(1), t.get("ps.statements").get(Instant.ofEpochMilli(T0 + 10_000)));
        assertEquals(Long.valueOf(7), t.get("ps.records").get(Instant.ofEpochMilli(T0)));
        assertEquals(1, sink.initialized);
        assertFalse(l.isSinkDown());
    }

    @Test
    public void aSecondFlushWritesNothingAndDropsClosedBuckets() {
        ManualClock clock = new ManualClock(T0);
        RecordingSink sink = new RecordingSink();
        ActivityLogger l = new ActivityLogger(sink, 10, 0, 0, clock).start();

        l.count("k");
        assertEquals(1, l.flush().size());
        assertEquals(0, l.flush().size());
        assertEquals(1, sink.writes.size());              // an empty flush does not touch the sink
        assertEquals(1, l.pendingBuckets());              // the open bucket stays

        clock.millis = T0 + 10_000;                       // the bucket is closed but not yet a full width old
        l.flush();
        assertEquals(1, l.pendingBuckets());
        clock.millis = T0 + 20_000;
        l.flush();
        assertEquals(0, l.pendingBuckets());
    }

    @Test
    public void aLateCountOnAJustClosedBucketIsNotLost() {
        ManualClock clock = new ManualClock(T0 + 9_990);
        RecordingSink sink = new RecordingSink();
        ActivityLogger l = new ActivityLogger(sink, 10, 0, 0, clock).start();

        l.count("k");                                     // bucket T0
        clock.millis = T0 + 10_005;
        l.flush();                                        // bucket T0 drained, kept one more width
        clock.millis = T0 + 9_995;                        // a thread that computed its bucket before the close
        l.count("k");
        clock.millis = T0 + 15_000;
        l.flush();
        assertEquals(Long.valueOf(2), sink.totals().get("k").get(Instant.ofEpochMilli(T0)));
    }

    @Test
    public void countsSurviveAnOutageOfTheSink() {
        ManualClock clock = new ManualClock(T0);
        RecordingSink sink = new RecordingSink();
        ActivityLogger l = new ActivityLogger(sink, 10, 0, 0, clock).start();

        sink.failing = true;
        l.count("k", 5);
        assertEquals(1, l.flush().size());                // attempted ...
        assertTrue(l.isSinkDown());
        assertEquals(0, sink.writes.size());              // ... refused ...
        l.count("k", 2);
        clock.millis = T0 + 10_000;
        l.count("k", 1);                                  // another bucket meanwhile

        sink.failing = false;
        l.flush();                                        // ... and written in full once the store is back
        assertFalse(l.isSinkDown());
        assertEquals(2, sink.initialized);                // re-initialized after the outage
        Map<Instant, Long> k = sink.totals().get("k");
        assertEquals(Long.valueOf(7), k.get(Instant.ofEpochMilli(T0)));
        assertEquals(Long.valueOf(1), k.get(Instant.ofEpochMilli(T0 + 10_000)));
    }

    @Test
    public void descriptionsReachTheSinkAtTheNextFlush() {
        ManualClock clock = new ManualClock(T0);
        RecordingSink sink = new RecordingSink();
        ActivityLogger l = new ActivityLogger(sink, 10, 0, 0, clock).start();

        l.register("ps.statements", "messages posted to /statements");
        assertTrue(sink.described.isEmpty());
        l.flush();                                        // nothing counted, the description goes anyway
        assertEquals("messages posted to /statements", sink.described.get("ps.statements"));
        assertEquals(1, l.getDescriptions().size());
    }

    @Test
    public void pruningRunsAtMostOncePerHourAndOnlyWithARetention() {
        ManualClock clock = new ManualClock(T0);
        RecordingSink sink = new RecordingSink();
        ActivityLogger l = new ActivityLogger(sink, 10, 0, 30, clock).start();
        l.flush();
        l.flush();
        assertEquals(1, sink.prunes.size());
        assertEquals(Instant.ofEpochMilli(T0).minusSeconds(30L * 86_400), sink.prunes.get(0));
        clock.millis = T0 + 3_600_000L;
        l.flush();
        assertEquals(2, sink.prunes.size());

        RecordingSink sink2 = new RecordingSink();
        new ActivityLogger(sink2, 10, 0, 0, clock).start().flush();
        assertTrue(sink2.prunes.isEmpty());
    }

    @Test
    public void nothingIsCountedForNullKindsNonPositiveCountsOrTheNoopLogger() {
        ManualClock clock = new ManualClock(T0);
        RecordingSink sink = new RecordingSink();
        ActivityLogger l = new ActivityLogger(sink, 10, 0, 0, clock).start();
        l.count(null);
        l.count("k", 0);
        l.count("k", -3);
        assertTrue(l.flush().isEmpty());

        ActivityLogger.NOOP.count("k");
        ActivityLogger.NOOP.register("k", "ignored");
        assertTrue(ActivityLogger.NOOP.flush().isEmpty());
        assertTrue(ActivityLogger.NOOP.getDescriptions().isEmpty());
        assertNull(ActivityLogger.NOOP.getLastFlush());
    }

    @Test
    public void sharedLoggerDefaultsToNoop() {
        ActivityLogger.setShared(null);
        assertSame(ActivityLogger.NOOP, ActivityLogger.shared());
        ActivityLogger l = new ActivityLogger(null, 10, 0, 0, Clock.systemUTC());
        ActivityLogger.setShared(l);
        assertSame(l, ActivityLogger.shared());
        ActivityLogger.setShared(null);
    }

    @Test
    public void settingsComeFromSystemPropertiesWithDefaults() {
        String name = "TPL_ACTIVITY_TEST_SETTING";
        System.clearProperty(name);
        assertEquals(10, ActivityLogger.intSetting(name, 10));
        System.setProperty(name, " 42 ");
        assertEquals(42, ActivityLogger.intSetting(name, 10));
        System.setProperty(name, "soon");
        assertEquals(10, ActivityLogger.intSetting(name, 10));
        System.clearProperty(name);
    }

    @Test
    public void fromEnvironmentIsNoopWithoutASinkOrWithFlushingOff() {
        assertSame(ActivityLogger.NOOP, ActivityLogger.fromEnvironment(null));
        System.setProperty(ActivityLogger.TPL_ACTIVITY_FLUSH_SECONDS, "0");
        try {
            assertSame(ActivityLogger.NOOP, ActivityLogger.fromEnvironment(new RecordingSink()));
        } finally {
            System.clearProperty(ActivityLogger.TPL_ACTIVITY_FLUSH_SECONDS);
        }
    }

    @Test
    public void windowsParseAndAlign() {
        assertEquals(ActivityQuery.Window.HOUR, ActivityQuery.Window.parse(null, ActivityQuery.Window.HOUR));
        assertEquals(ActivityQuery.Window.WEEK, ActivityQuery.Window.parse(" Week ", ActivityQuery.Window.HOUR));
        assertEquals(ActivityQuery.Window.HOUR, ActivityQuery.Window.parse("fortnight", ActivityQuery.Window.HOUR));
        assertEquals("minute", ActivityQuery.Window.MINUTE.key());
        assertEquals(Instant.ofEpochSecond(1_700_000_000L - (1_700_000_000L % 1800)),
                ActivityQuery.alignDown(Instant.ofEpochSecond(1_700_000_000L).plusMillis(999), 1800));
    }
}
