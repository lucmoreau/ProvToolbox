package org.openprovenance.prov.service.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.service.core.RelationMapping.RelationRows;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The {@code __PROV_*} relation rows of submissions, written after each submission's commit instead of with it
 * ({@code relations.write = async}, see {@link SubmissionSwitches}): a bounded queue of records' rows, drained by
 * {@code writers} threads, each taking up to {@code batch} records at a time and writing them as one multi-row INSERT
 * per relation table, all committed together, on connections of their own (never the service's shared one).
 *
 * <p>Back-pressure: a submission whose rows find the queue full waits for room (after its commit, its connection
 * already returned). On shutdown the queue is drained ({@link #close}). A batch that fails is retried record by record,
 * so one bad record loses only its own rows; those are counted ({@link #failed}) and logged.</p>
 *
 * <p>The lag of a record is the time from its submission's commit (when it was queued) to the commit of the batch
 * holding its rows: from then on a reader sees its relations.</p>
 */
public class AsyncRelationWriter implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(AsyncRelationWriter.class);

    /** How long {@link #close} waits for the queue to drain. */
    static final long DRAIN_SECONDS = 120;

    /** A record's rows, and when they were queued. */
    static final class Item {
        final List<RelationRows> rows;
        final long queuedNanos;

        Item(List<RelationRows> rows, long queuedNanos) {
            this.rows = rows;
            this.queuedNanos = queuedNanos;
        }
    }

    private final PooledConnections connections;
    private final BlockingQueue<Item> queue;
    private final int capacity;
    private final int batch;
    private final List<Thread> threads = new ArrayList<>();
    private volatile boolean running = true;

    private final AtomicLong queued = new AtomicLong();
    private final AtomicLong written = new AtomicLong();
    private final AtomicLong failed = new AtomicLong();
    private final AtomicLong batches = new AtomicLong();
    private final AtomicLong blocked = new AtomicLong();
    private final AtomicLong lagCount = new AtomicLong();
    private final AtomicLong lagTotalNanos = new AtomicLong();
    private final AtomicLong lagMaxNanos = new AtomicLong();
    private final AtomicLong lagLastNanos = new AtomicLong();

    /**
     * @param connections the writers' connections (at least {@code writers} of them, so no writer waits for another)
     * @param writers     the number of writer threads
     * @param capacity    the queue's capacity, in records
     * @param batch       the most records a writer takes at a time
     */
    public AsyncRelationWriter(PooledConnections connections, int writers, int capacity, int batch) {
        if (writers < 1 || capacity < 1 || batch < 1) {
            throw new IllegalArgumentException("writers, capacity and batch must be at least 1: " + writers + ", " + capacity + ", " + batch);
        }
        this.connections = connections;
        this.capacity = capacity;
        this.batch = batch;
        this.queue = new ArrayBlockingQueue<>(capacity);
        for (int i = 0; i < writers; i++) {
            Thread t = new Thread(this::drain, "relation-writer-" + i);
            t.setDaemon(true);   // the shutdown hook drains; a daemon never holds the JVM up on its own
            threads.add(t);
            t.start();
        }
    }

    /** Drains the queue when the JVM stops. */
    public AsyncRelationWriter withShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close, "relation-writer-drain"));
        return this;
    }

    /** Queues a record's rows, waiting for room when the queue is full (back-pressure). */
    public void enqueue(List<RelationRows> rows) {
        if (!running) {
            throw new IllegalStateException("the relation writer is closed");
        }
        Item item = new Item(rows, System.nanoTime());
        if (!queue.offer(item)) {
            blocked.incrementAndGet();
            try {
                queue.put(item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("interrupted while waiting for room in the relation queue", e);
            }
        }
        queued.incrementAndGet();
    }

    private void drain() {
        List<Item> taken = new ArrayList<>(batch);
        while (running || !queue.isEmpty()) {
            try {
                Item first = queue.poll(100, TimeUnit.MILLISECONDS);
                if (first == null) continue;
                taken.add(first);
                queue.drainTo(taken, batch - 1);
                write(taken);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (RuntimeException e) {
                logger.error("relation writer: " + e.getMessage(), e);
            } finally {
                taken.clear();
            }
        }
    }

    /** One batch: one INSERT per relation table, committed together; on failure, each record on its own. */
    void write(List<Item> items) {
        try {
            commit(items);
        } catch (SQLException | RuntimeException e) {
            if (items.size() == 1) {
                failed.incrementAndGet();
                logger.error("relation rows lost for one record: " + e.getMessage());
                return;
            }
            logger.warn("a batch of " + items.size() + " records' relations failed (" + e.getMessage() + "), retried record by record");
            for (Item item : items) {
                write(List.of(item));
            }
        }
    }

    private void commit(List<Item> items) throws SQLException {
        String sql = batchStatements(items);
        connections.inTransaction(conn -> {
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
            return null;
        });
        long now = System.nanoTime();
        for (Item item : items) {
            long lag = now - item.queuedNanos;
            lagTotalNanos.addAndGet(lag);
            lagMaxNanos.accumulateAndGet(lag, Math::max);
            lagLastNanos.set(lag);
        }
        lagCount.addAndGet(items.size());
        written.addAndGet(items.size());
        batches.incrementAndGet();
    }

    /** The statements of a batch: the records' rows merged, one multi-row INSERT per relation table and column list. */
    static String batchStatements(List<Item> items) {
        Map<String, List<String>> tuples = new LinkedHashMap<>();
        Map<String, RelationRows> heads = new LinkedHashMap<>();
        for (Item item : items) {
            for (RelationRows rows : item.rows) {
                String key = rows.table + " (" + rows.columns + ")";
                heads.putIfAbsent(key, rows);
                tuples.computeIfAbsent(key, k -> new ArrayList<>()).addAll(rows.tuples);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, RelationRows> e : heads.entrySet()) {
            RelationRows.appendInsert(sb, e.getValue().table, e.getValue().columns, tuples.get(e.getKey()));
        }
        return sb.toString();
    }

    /** Stops taking new rows and waits (up to {@value #DRAIN_SECONDS} s) for the queued ones to be written. */
    @Override
    public void close() {
        if (!running) return;
        running = false;
        int left = queue.size();
        if (left > 0) logger.info("relation writer: draining " + left + " queued record(s)");
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(DRAIN_SECONDS);
        for (Thread t : threads) {
            try {
                t.join(Math.max(1, TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime())));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        if (!queue.isEmpty()) {
            logger.error("relation writer: " + queue.size() + " record(s) left unwritten at shutdown");
        }
        logger.info("relation writer closed: " + status());
        connections.close();
    }

    public int depth() {
        return queue.size();
    }

    public long failed() {
        return failed.get();
    }

    /** The queue and its lag, as {@code provapi/submission/status.json} reports them. */
    public Map<String, Object> status() {
        Map<String, Object> m = new LinkedHashMap<>();
        long n = lagCount.get();
        m.put("depth", queue.size());
        m.put("capacity", capacity);
        m.put("batch", batch);
        m.put("writers", threads.size());
        m.put("queued", queued.get());
        m.put("written", written.get());
        m.put("failed", failed.get());
        m.put("batches", batches.get());
        m.put("blocked", blocked.get());
        m.put("lag_ms_mean", n == 0 ? 0.0 : lagTotalNanos.get() / 1e6 / n);
        m.put("lag_ms_max", lagMaxNanos.get() / 1e6);
        m.put("lag_ms_last", lagLastNanos.get() / 1e6);
        return m;
    }
}
