package org.openprovenance.prov.service.core.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Persists activity counts in PostgreSQL, in the two tables below, over a connection
 * of its own (the service's connection is shared by every request thread; the flushing
 * thread must not queue behind them, nor they behind it).
 *
 * <pre>
 * activity_log  (bucket timestamptz, kind text, count bigint, PRIMARY KEY (bucket, kind))
 * activity_kind (kind text PRIMARY KEY, description text, updated_at timestamptz)
 * </pre>
 *
 * <p>{@code bucket} is the start of the time bucket (UTC), {@code kind} names the type of
 * information counted (see {@link ActivityLogger}), {@code count} the number of
 * occurrences in that bucket. A write is {@code INSERT ... ON CONFLICT DO UPDATE SET
 * count = count + EXCLUDED.count}, so any number of services (the provenance store,
 * chronicle, ...) can add to the same table, even to the same bucket of the same kind.
 *
 * <p>The connection is opened on first use and dropped on any failure; the next
 * call reopens it.
 */
public class JdbcActivitySink implements ActivitySink {
    static final Logger logger = LogManager.getLogger(JdbcActivitySink.class);

    /** Idempotent schema; every service writing to the store may run it at start-up. */
    public static final String DDL =
            "CREATE TABLE IF NOT EXISTS " + ActivityLogger.TABLE + "\n" +
            "(\n" +
            "  bucket timestamp with time zone NOT NULL,\n" +
            "  kind   text NOT NULL,\n" +
            "  count  bigint NOT NULL,\n" +
            "  PRIMARY KEY (bucket, kind)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS " + ActivityLogger.KIND_TABLE + "\n" +
            "(\n" +
            "  kind        text PRIMARY KEY,\n" +
            "  description text NOT NULL,\n" +
            "  updated_at  timestamp with time zone NOT NULL DEFAULT NOW()\n" +
            ");\n";

    public static final String UPSERT =
            "INSERT INTO " + ActivityLogger.TABLE + " (bucket, kind, count) VALUES (?, ?, ?)\n" +
            "ON CONFLICT (bucket, kind) DO UPDATE SET count = " + ActivityLogger.TABLE + ".count + EXCLUDED.count";

    public static final String DESCRIBE =
            "INSERT INTO " + ActivityLogger.KIND_TABLE + " (kind, description, updated_at) VALUES (?, ?, NOW())\n" +
            "ON CONFLICT (kind) DO UPDATE SET description = EXCLUDED.description, updated_at = NOW()";

    public static final String PRUNE =
            "DELETE FROM " + ActivityLogger.TABLE + " WHERE bucket < ?";

    private final Supplier<Connection> connector;
    private final String name;
    private Connection conn;

    /**
     * @param connector opens a new connection, or returns null when it cannot
     * @param name      what to call this sink in logs (the JDBC URL without credentials, typically)
     */
    public JdbcActivitySink(Supplier<Connection> connector, String name) {
        this.connector = connector;
        this.name = name;
    }

    private synchronized Connection connection() throws SQLException {
        if (conn == null) {
            conn = connector.get();
            if (conn == null) throw new SQLException("no connection to " + name);
        }
        return conn;
    }

    private synchronized void dropConnection() {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { logger.debug("closing activity connection: " + e); }
            conn = null;
        }
    }

    @Override
    public synchronized void initialize() throws SQLException {
        try (Statement st = connection().createStatement()) {
            st.execute(DDL);
        } catch (SQLException e) {
            dropConnection();
            throw e;
        }
    }

    @Override
    public synchronized void write(List<ActivityLogger.Row> rows) throws SQLException {
        if (rows.isEmpty()) return;
        try (PreparedStatement ps = connection().prepareStatement(UPSERT)) {
            for (ActivityLogger.Row r : rows) {
                ps.setTimestamp(1, Timestamp.from(r.bucketStart()));
                ps.setString(2, r.kind());
                ps.setLong(3, r.count());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            dropConnection();
            throw e;
        }
    }

    @Override
    public synchronized void describe(Map<String, String> descriptions) throws SQLException {
        if (descriptions.isEmpty()) return;
        try (PreparedStatement ps = connection().prepareStatement(DESCRIBE)) {
            for (Map.Entry<String, String> e : descriptions.entrySet()) {
                ps.setString(1, e.getKey());
                ps.setString(2, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            dropConnection();
            throw e;
        }
    }

    @Override
    public synchronized void prune(Instant before) throws SQLException {
        try (PreparedStatement ps = connection().prepareStatement(PRUNE)) {
            ps.setTimestamp(1, Timestamp.from(before));
            int n = ps.executeUpdate();
            if (n > 0) logger.info("Activity log: pruned " + n + " rows older than " + before);
        } catch (SQLException e) {
            dropConnection();
            throw e;
        }
    }

    @Override
    public synchronized void close() {
        dropConnection();
    }

    @Override
    public String toString() {
        return "JdbcActivitySink{" + name + "}";
    }
}
