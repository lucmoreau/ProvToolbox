package org.openprovenance.prov.service.core.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * Counts the activity of a service (messages received, records written, ...) at
 * next to no cost on the request path, and persists the counts in the
 * {@code activity_log} table of the provenance store through an {@link ActivitySink}.
 *
 * <p><b>Request path.</b> {@link #count(String)} and {@link #count(String, long)} do one
 * {@code currentTimeMillis}, two concurrent-map lookups and a {@link LongAdder}
 * increment: no lock, no I/O, no allocation once a bucket and a kind exist.
 *
 * <p><b>Persistence.</b> Counts are aggregated in memory per time bucket (default 10 s)
 * and per {@code kind}, and a single daemon thread flushes them every
 * {@code flushSeconds} (default 10 s) as one batched upsert: at most one row per
 * bucket and per kind reaches the database, however many messages arrived. A bucket
 * that is still open is flushed too, so a reader is at most one flush behind; the
 * upsert <em>adds</em> to the stored count, so a bucket flushed twice loses nothing.
 * Buckets with no activity produce no row.
 *
 * <p><b>Kinds.</b> A kind is a short dotted name, {@code <service>.<what>}, naming the
 * type of information counted: {@code ps.statements} (messages posted to
 * {@code /statements}), {@code ps.records} (template records written), and, for
 * another service writing to the same store, for instance {@code chronicle.events}.
 * {@link #register(String, String)} stores a human description of a kind in the
 * {@code activity_kind} table so the dashboard can label it; counting an unregistered
 * kind is allowed.
 *
 * <p><b>Failure.</b> A sink that cannot write (database down) is logged once per outage;
 * the counts of that flush are folded back and retried at the next one, bounded by
 * {@link #MAX_PENDING_BUCKETS} so a long outage cannot grow the heap without limit.
 * The service itself never fails because of the logger.
 *
 * @see ActivitySink
 * @see JdbcActivitySink
 */
public class ActivityLogger implements AutoCloseable {
    static final Logger logger = LogManager.getLogger(ActivityLogger.class);

    /** Name of the table holding the counts. */
    public static final String TABLE = "activity_log";
    /** Name of the table describing the kinds. */
    public static final String KIND_TABLE = "activity_kind";

    /** Environment variable or system property: seconds between two flushes; 0 disables the logger. */
    public static final String TPL_ACTIVITY_FLUSH_SECONDS = "TPL_ACTIVITY_FLUSH_SECONDS";
    /** Environment variable or system property: width of a time bucket, in seconds. */
    public static final String TPL_ACTIVITY_BUCKET_SECONDS = "TPL_ACTIVITY_BUCKET_SECONDS";
    /** Environment variable or system property: rows older than this many days are deleted; 0 keeps them for ever. */
    public static final String TPL_ACTIVITY_RETENTION_DAYS = "TPL_ACTIVITY_RETENTION_DAYS";

    public static final int DEFAULT_FLUSH_SECONDS = 10;
    public static final int DEFAULT_BUCKET_SECONDS = 10;
    public static final int DEFAULT_RETENTION_DAYS = 400;

    /** Upper bound on buckets kept in memory while the sink is unavailable. */
    public static final int MAX_PENDING_BUCKETS = 10_000;

    /** A logger that counts nothing: used when the store is not configured. */
    public static final ActivityLogger NOOP = new ActivityLogger(null, DEFAULT_BUCKET_SECONDS, 0, 0, Clock.systemUTC()) {
        @Override public void count(String kind, long n) { }
        @Override public void register(String kind, String description) { }
        @Override public String toString() { return "ActivityLogger.NOOP"; }
    };

    private static volatile ActivityLogger shared = NOOP;

    /**
     * The logger of the running service, for code that has no handle on it (generated
     * enactors, filters). {@link #NOOP} until a service installs one with {@link #setShared}.
     */
    public static ActivityLogger shared() { return shared; }

    public static void setShared(ActivityLogger l) { shared = (l == null) ? NOOP : l; }

    /** One persisted row: the count of {@code kind} in the bucket starting at {@code bucketStart}. */
    public static final class Row {
        private final Instant bucketStart;
        private final String kind;
        private final long count;

        public Row(Instant bucketStart, String kind, long count) {
            this.bucketStart = bucketStart;
            this.kind = kind;
            this.count = count;
        }

        public Instant bucketStart() { return bucketStart; }
        public String kind() { return kind; }
        public long count() { return count; }

        @Override
        public String toString() { return "Row{" + bucketStart + ", " + kind + ", " + count + "}"; }
    }

    private final ActivitySink sink;
    private final long bucketMillis;
    private final int flushSeconds;
    private final int retentionDays;
    private final Clock clock;
    /** bucket start (epoch millis) -> kind -> count. Sorted so flushing walks old buckets first. */
    private final ConcurrentSkipListMap<Long, ConcurrentHashMap<String, LongAdder>> buckets = new ConcurrentSkipListMap<>();
    private final Map<String, String> descriptions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler;
    private volatile boolean sinkDown = false;
    private volatile Instant lastPrune = Instant.EPOCH;
    private volatile Instant lastFlush = null;

    /**
     * Builds a logger with the configuration found in the environment
     * ({@link #TPL_ACTIVITY_FLUSH_SECONDS}, {@link #TPL_ACTIVITY_BUCKET_SECONDS},
     * {@link #TPL_ACTIVITY_RETENTION_DAYS}) and starts its flushing thread.
     * Returns {@link #NOOP} when the sink is null or flushing is disabled.
     */
    public static ActivityLogger fromEnvironment(ActivitySink sink) {
        int flush = intSetting(TPL_ACTIVITY_FLUSH_SECONDS, DEFAULT_FLUSH_SECONDS);
        int bucket = intSetting(TPL_ACTIVITY_BUCKET_SECONDS, DEFAULT_BUCKET_SECONDS);
        int retention = intSetting(TPL_ACTIVITY_RETENTION_DAYS, DEFAULT_RETENTION_DAYS);
        if (sink == null || flush <= 0) {
            logger.info("Activity logging disabled (" + (sink == null ? "no store" : TPL_ACTIVITY_FLUSH_SECONDS + "=0") + ")");
            return NOOP;
        }
        ActivityLogger l = new ActivityLogger(sink, bucket, flush, retention, Clock.systemUTC());
        l.start();
        return l;
    }

    static int intSetting(String name, int dflt) {
        String v = System.getProperty(name);
        if (v == null) v = System.getenv(name);
        if (v == null || v.isBlank()) return dflt;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            logger.warn(name + "=" + v + " is not a number, using " + dflt);
            return dflt;
        }
    }

    /**
     * @param sink          where rows go; null makes a logger that only counts in memory (tests)
     * @param bucketSeconds width of a time bucket
     * @param flushSeconds  period of the flushing thread; 0 means no thread ({@link #flush()} is then manual)
     * @param retentionDays rows older than this are pruned at flush time; 0 never prunes
     * @param clock         the clock buckets are computed from
     */
    public ActivityLogger(ActivitySink sink, int bucketSeconds, int flushSeconds, int retentionDays, Clock clock) {
        if (bucketSeconds <= 0) throw new IllegalArgumentException("bucketSeconds must be positive");
        this.sink = sink;
        this.bucketMillis = bucketSeconds * 1000L;
        this.flushSeconds = flushSeconds;
        this.retentionDays = retentionDays;
        this.clock = clock;
        this.scheduler = (flushSeconds > 0)
                ? Executors.newSingleThreadScheduledExecutor(r -> {
                    Thread t = new Thread(r, "activity-logger");
                    t.setDaemon(true);
                    return t;
                })
                : null;
    }

    /** Creates the tables (idempotent) and starts the periodic flush. */
    public ActivityLogger start() {
        if (sink != null) {
            try {
                sink.initialize();
            } catch (Exception e) {
                logger.warn("Activity log: cannot initialize the store, will retry at the first flush: " + e);
                sinkDown = true;
            }
        }
        if (scheduler != null) {
            scheduler.scheduleAtFixedRate(this::safeFlush, flushSeconds, flushSeconds, TimeUnit.SECONDS);
        }
        logger.info("Activity logging: bucket " + (bucketMillis / 1000) + " s, flush every " + flushSeconds
                + " s, retention " + (retentionDays > 0 ? retentionDays + " days" : "unlimited"));
        return this;
    }

    public int getBucketSeconds() { return (int) (bucketMillis / 1000); }
    public int getFlushSeconds() { return flushSeconds; }
    public int getRetentionDays() { return retentionDays; }
    public Instant getLastFlush() { return lastFlush; }
    public boolean isSinkDown() { return sinkDown; }
    public Map<String, String> getDescriptions() { return Map.copyOf(descriptions); }

    /** Counts one occurrence of {@code kind}. */
    public void count(String kind) {
        count(kind, 1);
    }

    /** Counts {@code n} occurrences of {@code kind}; nothing happens for {@code n <= 0}. */
    public void count(String kind, long n) {
        if (n <= 0 || kind == null) return;
        long now = clock.millis();
        long start = now - Math.floorMod(now, bucketMillis);
        buckets.computeIfAbsent(start, k -> new ConcurrentHashMap<>())
               .computeIfAbsent(kind, k -> new LongAdder())
               .add(n);
    }

    /**
     * Declares what {@code kind} counts; written to {@code activity_kind} at the next flush
     * (and again after an outage). The description is free text shown by the dashboard.
     */
    public void register(String kind, String description) {
        if (kind == null || description == null) return;
        descriptions.put(kind, description);
        pendingDescriptions = true;
    }

    private volatile boolean pendingDescriptions = false;

    /**
     * Takes the counts accumulated so far out of memory and hands them to the sink.
     * Called by the flushing thread; a reader wanting exact figures may call it before
     * querying. Thread-safe; cheap when nothing was counted.
     *
     * @return the rows written (empty when nothing was counted or the sink is down)
     */
    public synchronized List<Row> flush() {
        long now = clock.millis();
        long currentStart = now - Math.floorMod(now, bucketMillis);
        List<Row> rows = new ArrayList<>();
        for (Map.Entry<Long, ConcurrentHashMap<String, LongAdder>> e : buckets.entrySet()) {
            long start = e.getKey();
            for (Map.Entry<String, LongAdder> k : e.getValue().entrySet()) {
                long c = k.getValue().sumThenReset();
                if (c > 0) rows.add(new Row(Instant.ofEpochMilli(start), k.getKey(), c));
            }
            // a bucket is dropped once drained and closed for a full bucket width:
            // a count() that computed its bucket start just before the bucket closed
            // still finds the map for one more flush, so nothing is lost
            if (start < currentStart - bucketMillis) buckets.remove(start, e.getValue());
        }
        if (sink == null) return rows;
        try {
            if (pendingDescriptions || sinkDown) {
                if (sinkDown) sink.initialize();
                sink.describe(descriptions);
                pendingDescriptions = false;
            }
            if (!rows.isEmpty()) sink.write(rows);
            if (sinkDown) logger.info("Activity log: store available again");
            sinkDown = false;
            lastFlush = Instant.ofEpochMilli(now);
            prune(now);
        } catch (Exception ex) {
            if (!sinkDown) logger.warn("Activity log: cannot write to the store, keeping the counts in memory: " + ex);
            sinkDown = true;
            foldBack(rows);
        }
        return rows;
    }

    /** Puts rows the sink refused back into memory, oldest buckets dropped beyond the bound. */
    private void foldBack(List<Row> rows) {
        for (Row r : rows) {
            buckets.computeIfAbsent(r.bucketStart().toEpochMilli(), k -> new ConcurrentHashMap<>())
                   .computeIfAbsent(r.kind(), k -> new LongAdder())
                   .add(r.count());
        }
        while (buckets.size() > MAX_PENDING_BUCKETS) {
            buckets.pollFirstEntry();
        }
    }

    private void prune(long now) throws Exception {
        if (retentionDays <= 0) return;
        Instant t = Instant.ofEpochMilli(now);
        if (Duration.between(lastPrune, t).toHours() < 1) return;
        sink.prune(t.minus(Duration.ofDays(retentionDays)));
        lastPrune = t;
    }

    private void safeFlush() {
        try {
            flush();
        } catch (Throwable t) {       // never kill the scheduled task
            logger.warn("Activity log: flush failed: " + t);
        }
    }

    /** Number of buckets currently held in memory (for tests and diagnostics). */
    public int pendingBuckets() { return buckets.size(); }

    /** Flushes once more and stops the flushing thread. */
    @Override
    public void close() {
        if (scheduler != null) scheduler.shutdownNow();
        safeFlush();
        if (sink != null) {
            try { sink.close(); } catch (Exception e) { logger.debug("Activity log: closing sink: " + e); }
        }
    }

    @Override
    public String toString() {
        return "ActivityLogger{bucket=" + bucketMillis / 1000 + "s, flush=" + flushSeconds + "s, retention=" + retentionDays + "d, sink=" + sink + "}";
    }
}
