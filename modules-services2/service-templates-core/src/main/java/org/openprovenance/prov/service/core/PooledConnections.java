package org.openprovenance.prov.service.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;

/**
 * A small pool of database connections, so that requests run side by side instead of in turn on one connection (a JDBC
 * connection runs one statement at a time): a read borrows one in autocommit ({@link #with}), a submission borrows one
 * for a single transaction ({@link #inTransaction}) — all of its statements commit together or not at all.
 *
 * <p>Connections are opened lazily, up to {@code size}, validated when borrowed (an invalid one is replaced), and
 * returned with {@code DISCARD TEMP}: a reading that creates temporary tables leaves nothing behind for the next
 * borrower. A request waits for a free connection when all are in use.</p>
 */
public class PooledConnections implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(PooledConnections.class);

    /** Work on a borrowed connection. */
    @FunctionalInterface
    public interface Work<T> {
        T apply(Connection conn) throws SQLException;
    }

    private final Supplier<Connection> opener;
    private final BlockingQueue<Connection> idle;
    private final Semaphore permits;
    private final List<Connection> opened = new ArrayList<>();
    private final int size;

    public PooledConnections(Supplier<Connection> opener, int size) {
        if (size < 1) {
            throw new IllegalArgumentException("a read pool needs at least one connection: " + size);
        }
        this.opener = opener;
        this.size = size;
        this.idle = new ArrayBlockingQueue<>(size);
        this.permits = new Semaphore(size, true);
    }

    public int size() {
        return size;
    }

    public <T> T with(Work<T> work) throws SQLException {
        try {
            permits.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("interrupted while waiting for a read connection", e);
        }
        Connection conn = null;
        try {
            conn = borrow();
            return work.apply(conn);
        } finally {
            if (conn != null) {
                giveBack(conn);
            }
            permits.release();
        }
    }

    /**
     * Work in ONE transaction on a borrowed connection: committed when it returns, rolled back when it throws (the
     * original exception propagates, unwrapped, so its causes can still be inspected).
     */
    public <T> T inTransaction(Work<T> work) throws SQLException {
        return with(conn -> {
            conn.setAutoCommit(false);
            boolean committed = false;
            try {
                T result = work.apply(conn);
                conn.commit();
                committed = true;
                return result;
            } finally {
                if (!committed) {
                    try {
                        conn.rollback();
                    } catch (SQLException e) {
                        logger.warn("rollback failed: " + e.getMessage());
                    }
                }
                conn.setAutoCommit(true);
            }
        });
    }

    private Connection borrow() throws SQLException {
        Connection conn = idle.poll();
        if (conn != null && isValid(conn)) {
            return conn;
        }
        if (conn != null) {
            forget(conn);
        }
        Connection fresh = opener.get();
        if (fresh == null) {
            throw new SQLException("could not open a read connection");
        }
        synchronized (opened) {
            opened.add(fresh);
        }
        return fresh;
    }

    private static boolean isValid(Connection conn) {
        try {
            return conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    private void giveBack(Connection conn) {
        try {
            if (!conn.getAutoCommit()) {
                conn.rollback();
                conn.setAutoCommit(true);
            }
            try (Statement st = conn.createStatement()) {
                st.execute("DISCARD TEMP");
            }
            if (!idle.offer(conn)) {
                forget(conn);
            }
        } catch (SQLException e) {
            logger.warn("a read connection could not be reset, closing it: " + e.getMessage());
            forget(conn);
        }
    }

    private void forget(Connection conn) {
        synchronized (opened) {
            opened.remove(conn);
        }
        try {
            conn.close();
        } catch (SQLException ignored) {
            // already unusable
        }
    }

    @Override
    public void close() {
        synchronized (opened) {
            for (Connection conn : opened) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                    // closing anyway
                }
            }
            opened.clear();
        }
        idle.clear();
    }
}
