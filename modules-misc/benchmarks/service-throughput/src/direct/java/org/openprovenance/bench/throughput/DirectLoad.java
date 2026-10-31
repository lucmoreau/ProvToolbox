package org.openprovenance.bench.throughput;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * N writer threads, each writing transactions of B records of one template in turn, for a fixed time, on its own
 * connection: the store without the web service. The messages are the HTTP mode's (same {@link Family}: weights,
 * composites, unique values, references to ids returned earlier); a message's B entries are one transaction here,
 * where the service commits each entry on its own.
 */
final class DirectLoad {

    /** What a phase did, as the HTTP mode's {@link Load.Outcome} (messages = transactions), with the rows expected. */
    static final class Outcome {
        final Load.Outcome o = new Load.Outcome();
        long expectedRows;
    }

    private final DirectStore store;
    private final Family family;
    private final int writers;
    private final int recordsPerTransaction;

    DirectLoad(DirectStore store, Family family, int writers, int recordsPerTransaction) {
        this.store = store;
        this.family = family;
        this.writers = writers;
        this.recordsPerTransaction = recordsPerTransaction;
    }

    /** @param lagProbe a connection to watch relation rows appear on (one record every 200 ms), or null */
    Outcome run(double seconds, Connection lagProbe) throws InterruptedException {
        Outcome out = new Outcome();
        Load.Outcome o = out.o;
        AtomicLong transactions = new AtomicLong();
        AtomicLong entries = new AtomicLong();
        AtomicLong records = new AtomicLong();
        AtomicLong failures = new AtomicLong();
        AtomicLong expectedRows = new AtomicLong();
        List<long[]> perWriter = Collections.synchronizedList(new ArrayList<>());
        BlockingQueue<Load.Probe> probes = new ArrayBlockingQueue<>(1);
        AtomicLong nextProbeAt = new AtomicLong(0);
        AtomicBoolean done = new AtomicBoolean();

        long start = System.nanoTime();
        long end = start + (long) (seconds * 1e9);
        List<Thread> threads = new ArrayList<>();
        for (int w = 0; w < writers; w++) {
            Thread t = new Thread(() -> {
                store.bindThread();
                long[] lat = new long[1024];
                int count = 0;
                while (System.nanoTime() < end) {
                    Family.Template template = family.pick();
                    List<Map<String, Object>> batch = new ArrayList<>(recordsPerTransaction);
                    int recs = 0;
                    for (int i = 0; i < recordsPerTransaction; i++) {
                        batch.add(family.entry(template));
                        recs += family.records(template);
                    }
                    long t0 = System.nanoTime();
                    DirectStore.Written written;
                    try {
                        written = store.write(template, batch);
                    } catch (RuntimeException e) {
                        failures.incrementAndGet();
                        if (o.failureSamples.size() < 5) o.failureSamples.add(template.isA + ": " + rootMessage(e));
                        continue;
                    }
                    long t1 = System.nanoTime();
                    for (long id : written.outputs) family.remember(id);
                    transactions.incrementAndGet();
                    entries.addAndGet(recordsPerTransaction);
                    records.addAndGet(recs);
                    expectedRows.addAndGet(written.expectedRows);
                    if (count == lat.length) lat = Arrays.copyOf(lat, lat.length * 2);
                    lat[count++] = t1 - t0;
                    if (lagProbe != null && written.firstSimpleId > 0 && t1 >= nextProbeAt.get()) {
                        if (probes.offer(new Load.Probe(written.firstSimpleTemplate, written.firstSimpleId, t1))) nextProbeAt.set(t1 + 200_000_000L);
                    }
                }
                perWriter.add(Arrays.copyOf(lat, count));
            }, "direct-writer-" + w);
            threads.add(t);
            t.start();
        }
        Thread prober = null;
        if (lagProbe != null) {
            prober = new Thread(() -> Load.probe(lagProbe, probes, done, o), "lag-probe");
            prober.start();
        }
        for (Thread t : threads) t.join();
        o.seconds = (System.nanoTime() - start) / 1e9;
        done.set(true);
        if (prober != null) prober.join(60_000);

        o.messages = transactions.get();
        o.entries = entries.get();
        o.records = records.get();
        o.failures = failures.get();
        out.expectedRows = expectedRows.get();
        int total = 0;
        synchronized (perWriter) {
            for (long[] l : perWriter) total += l.length;
            o.latencyNanos = new long[total];
            int k = 0;
            for (long[] l : perWriter) {
                System.arraycopy(l, 0, o.latencyNanos, k, l.length);
                k += l.length;
            }
        }
        Arrays.sort(o.latencyNanos);
        return out;
    }

    static String rootMessage(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null && t.getCause() != t) t = t.getCause();
        String m = t.getClass().getSimpleName() + ": " + t.getMessage();
        return m.length() > 300 ? m.substring(0, 300) : m;
    }
}
