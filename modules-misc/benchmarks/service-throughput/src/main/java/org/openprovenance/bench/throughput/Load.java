package org.openprovenance.bench.throughput;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * N clients, each posting messages of B entries to {@code provapi/statements} in turn, for a fixed time: what a client
 * sees. Each message is of one template (the service takes a message's template from its first entry); the mix is
 * across messages. With relations written asynchronously, a probe measures how long after a response the record's
 * relations become visible.
 */
final class Load {

    static final String MEDIA = "application/vnd.kcl.prov-template+json";
    private static final ObjectMapper OM = new ObjectMapper();

    /** What a phase did. */
    static final class Outcome {
        long messages;
        long entries;
        long records;
        long failures;
        long hashHeaders;
        double seconds;
        long[] latencyNanos = new long[0];
        final List<String> failureSamples = Collections.synchronizedList(new ArrayList<>());
        final List<Long> lagNanos = Collections.synchronizedList(new ArrayList<>());
        long lagTimeouts;

        double percentileMillis(double p) {
            if (latencyNanos.length == 0) return Double.NaN;
            int i = (int) Math.ceil(p / 100.0 * latencyNanos.length) - 1;
            return latencyNanos[Math.max(0, Math.min(latencyNanos.length - 1, i))] / 1e6;
        }

        double lagPercentileMillis(double p) {
            List<Long> l;
            synchronized (lagNanos) {
                l = new ArrayList<>(lagNanos);
            }
            if (l.isEmpty()) return Double.NaN;
            Collections.sort(l);
            int i = (int) Math.ceil(p / 100.0 * l.size()) - 1;
            return l.get(Math.max(0, Math.min(l.size() - 1, i))) / 1e6;
        }
    }

    private final Family family;
    private final int clients;
    private final int entriesPerMessage;
    private final boolean idempotencyKey;
    private final URI statements;
    private final HttpClient http;

    Load(Family family, URI serviceBase, int clients, int entriesPerMessage, boolean idempotencyKey) {
        if (idempotencyKey && entriesPerMessage != 1) {
            throw new IllegalArgumentException("an Idempotency-Key names one entry: the service refuses it on a message of " + entriesPerMessage);
        }
        this.family = family;
        this.clients = clients;
        this.entriesPerMessage = entriesPerMessage;
        this.idempotencyKey = idempotencyKey;
        this.statements = URI.create(Guard.serviceUrl(serviceBase.toString()) + "/provapi/statements");
        this.http = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(10)).build();
    }

    /** A record whose relations the probe watches for: its template, its id, when its response arrived. */
    private static final class Probe {
        final String template;
        final long id;
        final long respondedNanos;

        Probe(String template, long id, long respondedNanos) {
            this.template = template;
            this.id = id;
            this.respondedNanos = respondedNanos;
        }
    }

    /**
     * Runs the clients for {@code seconds}; a client finishes the message it is sending when the time is up.
     *
     * @param lagProbe a connection to watch relation rows appear on (one record every 200 ms), or null
     */
    Outcome run(double seconds, Connection lagProbe) throws InterruptedException {
        Outcome o = new Outcome();
        AtomicLong messages = new AtomicLong();
        AtomicLong entries = new AtomicLong();
        AtomicLong records = new AtomicLong();
        AtomicLong failures = new AtomicLong();
        AtomicLong hashHeaders = new AtomicLong();
        List<long[]> perClient = Collections.synchronizedList(new ArrayList<>());
        BlockingQueue<Probe> probes = new ArrayBlockingQueue<>(1);
        AtomicLong nextProbeAt = new AtomicLong(0);
        AtomicBoolean done = new AtomicBoolean();

        long start = System.nanoTime();
        long end = start + (long) (seconds * 1e9);
        List<Thread> threads = new ArrayList<>();
        for (int c = 0; c < clients; c++) {
            Thread t = new Thread(() -> {
                long[] lat = new long[1024];
                int count = 0;
                while (System.nanoTime() < end) {
                    Family.Template template = family.pick();
                    List<Map<String, Object>> message = new ArrayList<>(entriesPerMessage);
                    int recs = 0;
                    for (int i = 0; i < entriesPerMessage; i++) {
                        message.add(family.entry(template));
                        recs += family.records(template);
                    }
                    long t0 = System.nanoTime();
                    HttpResponse<String> response;
                    try {
                        HttpRequest.Builder rb = HttpRequest.newBuilder(statements)
                                .timeout(Duration.ofSeconds(120))
                                .header("Content-Type", MEDIA)
                                .header("Accept", MEDIA)
                                .header("Accept-PROV-hash", "true")
                                .POST(HttpRequest.BodyPublishers.ofByteArray(OM.writeValueAsBytes(message)));
                        if (idempotencyKey) rb.header("Idempotency-Key", UUID.randomUUID().toString());
                        response = http.send(rb.build(), HttpResponse.BodyHandlers.ofString());
                    } catch (Exception e) {
                        failures.incrementAndGet();
                        if (o.failureSamples.size() < 5) o.failureSamples.add(template.isA + ": " + e);
                        continue;
                    }
                    long t1 = System.nanoTime();
                    if (response.statusCode() / 100 != 2) {
                        failures.incrementAndGet();
                        if (o.failureSamples.size() < 5) {
                            String body = response.body();
                            o.failureSamples.add(template.isA + " -> " + response.statusCode() + ": " + body.substring(0, Math.min(300, body.length())));
                        }
                        continue;
                    }
                    if (response.headers().firstValue("PROV-Hash").isPresent()) hashHeaders.incrementAndGet();
                    long firstId = remember(response.body());
                    messages.incrementAndGet();
                    entries.addAndGet(entriesPerMessage);
                    records.addAndGet(recs);
                    if (count == lat.length) lat = Arrays.copyOf(lat, lat.length * 2);
                    lat[count++] = t1 - t0;
                    if (lagProbe != null && !template.composite() && firstId > 0 && t1 >= nextProbeAt.get()) {
                        if (probes.offer(new Probe(template.isA, firstId, t1))) nextProbeAt.set(t1 + 200_000_000L);
                    }
                }
                perClient.add(Arrays.copyOf(lat, count));
            }, "client-" + c);
            threads.add(t);
            t.start();
        }
        Thread prober = null;
        if (lagProbe != null) {
            prober = new Thread(() -> probe(lagProbe, probes, done, o), "lag-probe");
            prober.start();
        }
        for (Thread t : threads) t.join();
        o.seconds = (System.nanoTime() - start) / 1e9;
        done.set(true);
        if (prober != null) prober.join(60_000);

        o.messages = messages.get();
        o.entries = entries.get();
        o.records = records.get();
        o.failures = failures.get();
        o.hashHeaders = hashHeaders.get();
        int total = 0;
        synchronized (perClient) {
            for (long[] l : perClient) total += l.length;
            o.latencyNanos = new long[total];
            int k = 0;
            for (long[] l : perClient) {
                System.arraycopy(l, 0, o.latencyNanos, k, l.length);
                k += l.length;
            }
        }
        Arrays.sort(o.latencyNanos);
        return o;
    }

    /** Remembers the ids the response returned (outputs, for later records to refer to); the first entry's ID. */
    private long remember(String body) {
        long firstId = -1;
        try {
            JsonNode array = OM.readTree(body);
            if (array.isArray() && array.size() > 0) firstId = array.get(0).path("ID").asLong(-1);
            Iterator<JsonNode> it = array.elements();
            int kept = 0;
            while (it.hasNext() && kept < 4) {
                JsonNode entry = it.next();
                Iterator<Map.Entry<String, JsonNode>> fields = entry.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> f = fields.next();
                    if (f.getValue().isInt() && !f.getKey().equals("ID") && !f.getKey().equals("count")) {
                        family.remember(f.getValue().asLong());
                        kept++;
                    }
                }
            }
        } catch (Exception e) {
            // a response we cannot read: the records still count; nothing to remember
        }
        return firstId;
    }

    /** Watches each probed record until one of its relation rows is visible; the lag from its response. */
    private static void probe(Connection c, BlockingQueue<Probe> probes, AtomicBoolean done, Outcome o) {
        try (PreparedStatement ps = Store.relationProbe(c)) {
            while (!done.get() || !probes.isEmpty()) {
                Probe p = probes.poll(100, TimeUnit.MILLISECONDS);
                if (p == null) continue;
                long deadline = System.nanoTime() + 30_000_000_000L;
                boolean seen = false;
                while (System.nanoTime() < deadline) {
                    for (int i = 0; i < 7; i++) {
                        ps.setLong(2 * i + 1, p.id);
                        ps.setString(2 * i + 2, p.template);
                    }
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            o.lagNanos.add(System.nanoTime() - p.respondedNanos);
                            seen = true;
                            break;
                        }
                    }
                    Thread.sleep(5);
                }
                if (!seen) o.lagTimeouts++;
            }
        } catch (SQLException e) {
            o.failureSamples.add("lag probe: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
