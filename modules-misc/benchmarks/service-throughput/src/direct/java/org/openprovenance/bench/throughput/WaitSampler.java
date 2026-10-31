package org.openprovenance.bench.throughput;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * What the writers' backends are doing, sampled from pg_stat_activity every {@value #PERIOD_MS} ms during the window:
 * per sample, each non-idle backend of the direct writers and relation writers counted under its wait event
 * ({@code type:event}), {@code CPU} when active without one, {@code Client:IdleInTransaction} between the statements of a
 * transaction (the client's time inside it). The harness's own connections are not counted.
 */
final class WaitSampler implements AutoCloseable {

    static final long PERIOD_MS = 100;

    private final Connection c;
    private final Map<String, AtomicLong> counts = new ConcurrentHashMap<>();
    private final AtomicLong samples = new AtomicLong();
    private final AtomicLong backends = new AtomicLong();
    private volatile boolean running = true;
    private final Thread thread;
    private volatile String error;

    WaitSampler(Connection c) {
        this.c = c;
        this.thread = new Thread(this::loop, "wait-sampler");
        thread.setDaemon(true);
        thread.start();
    }

    private void loop() {
        String sql = "SELECT CASE WHEN state = 'idle in transaction' THEN 'Client:IdleInTransaction' "
                + "WHEN wait_event_type IS NULL THEN 'CPU' ELSE wait_event_type || ':' || wait_event END, count(*) "
                + "FROM pg_stat_activity WHERE datname = current_database() AND pid <> pg_backend_pid() "
                + "AND application_name IN ('" + DirectStore.WRITER_APP + "', '" + DirectStore.RELATION_WRITER_APP + "') "
                + "AND state IS DISTINCT FROM 'idle' GROUP BY 1";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            while (running) {
                long n = 0;
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        counts.computeIfAbsent(rs.getString(1), k -> new AtomicLong()).addAndGet(rs.getLong(2));
                        n += rs.getLong(2);
                    }
                }
                samples.incrementAndGet();
                backends.addAndGet(n);
                Thread.sleep(PERIOD_MS);
            }
        } catch (SQLException e) {
            error = e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() throws InterruptedException {
        running = false;
        thread.join(5000);
    }

    String error() {
        return error;
    }

    /** Busy backends on average (the writers' concurrency as the server sees it). */
    double meanBusy() {
        long s = samples.get();
        return s == 0 ? 0 : (double) backends.get() / s;
    }

    /** The top events, "event share%" separated by "; ", share of all busy-backend samples. */
    String top(int n) {
        long total = backends.get();
        if (total == 0) return "";
        List<Map.Entry<String, AtomicLong>> entries = new ArrayList<>(counts.entrySet());
        entries.sort((a, b) -> Long.compare(b.getValue().get(), a.getValue().get()));
        Map<String, String> out = new LinkedHashMap<>();
        for (Map.Entry<String, AtomicLong> e : entries.subList(0, Math.min(n, entries.size()))) {
            out.put(e.getKey(), String.format("%.0f%%", 100.0 * e.getValue().get() / total));
        }
        StringBuilder sb = new StringBuilder();
        out.forEach((k, v) -> sb.append(sb.length() == 0 ? "" : "; ").append(k).append(' ').append(v));
        return sb.toString();
    }
}
