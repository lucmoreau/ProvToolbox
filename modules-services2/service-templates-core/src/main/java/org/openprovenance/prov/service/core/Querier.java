package org.openprovenance.prov.service.core;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.service.core.inface.BiConsumerWithException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Querier  {
    final private static Logger logger = LogManager.getLogger(Querier.class);

    final private Storage storage;
    /** The service's own connection: start-up scripts, and every request when no pool is configured. */
    private Connection defaultConn;
    /**
     * Opens a new connection in place of {@link #defaultConn} once the driver has closed it — the database restarted, or
     * the connection broke (nextgen T-531: the service otherwise answered every request 500 until it was restarted
     * itself). Null keeps the first connection for good, as before.
     */
    final private Supplier<Connection> reopen;
    /** Requests run on connections of their own when configured; null keeps every request on {@link #defaultConn}. */
    final private PooledConnections pool;
    /** The connection of the submission this thread is inside ({@link #inTransaction}), if any. */
    final private ThreadLocal<Connection> bound = new ThreadLocal<>();
    /** What runs once the submission this thread is inside has committed ({@link #afterCommit}). */
    final private ThreadLocal<List<Runnable>> afterCommit = new ThreadLocal<>();

    public Querier(Storage storage, Connection conn) {
        this(storage, conn, null);
    }

    public Querier(Storage storage, Connection conn, PooledConnections pool) {
        this(storage, conn, pool, null);
    }

    public Querier(Storage storage, Connection conn, PooledConnections pool, Supplier<Connection> reopen) {
        this.storage = storage;
        this.defaultConn = conn;
        this.pool = pool;
        this.reopen = reopen;
    }

    /** The connection a statement of this thread runs on: its submission's, else the service's own. */
    public Connection currentConnection() {
        Connection c = bound.get();
        return c != null ? c : serviceConnection();
    }

    /**
     * The service's own connection, reopened first when the driver has closed it. The statement that met the broken
     * connection has failed already (the driver closes the connection on that error); the statements after it run on
     * the new one. No ping on the way: a connection busy with another thread's long reading is never taken for dead.
     * When it cannot be reopened (the database still down), the closed one is returned, and its statement fails.
     */
    public synchronized Connection serviceConnection() {
        if (reopen == null || !isClosed(defaultConn)) {
            return defaultConn;
        }
        Connection fresh = reopen.get();
        if (fresh == null) {
            logger.warn("the service's database connection is closed, and a new one could not be opened yet");
            return defaultConn;
        }
        if (defaultConn != null) {
            try {
                defaultConn.close();
            } catch (SQLException ignored) {
                // already closed
            }
        }
        logger.warn("the service's database connection was closed: a new one opened in its place");
        defaultConn = fresh;
        return fresh;
    }

    private static boolean isClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    /** Whether this thread is inside a submission's transaction. */
    public boolean inTransactionNow() {
        return bound.get() != null;
    }

    /**
     * A submission: with a pool, every statement {@code work} issues on this thread runs in ONE transaction on a
     * connection of its own — committed together, or rolled back together when {@code work} throws (the exception
     * propagates unchanged). Without a pool, or nested inside a submission, {@code work} just runs.
     */
    public <T> T inTransaction(java.util.function.Supplier<T> work) {
        if (pool == null || bound.get() != null) {
            return work.get();
        }
        List<Runnable> committed = new ArrayList<>();
        T result;
        try {
            result = pool.inTransaction(c -> {
                bound.set(c);
                afterCommit.set(committed);
                try {
                    return work.get();
                } finally {
                    bound.remove();
                    afterCommit.remove();
                }
            });
        } catch (SQLException e) {
            throw new org.openprovenance.prov.model.exception.UncheckedException("Issue committing a submission", e);
        }
        committed.forEach(Runnable::run);   // only once committed: never for a submission rolled back
        return result;
    }

    /**
     * Runs {@code action} once this thread's submission has committed, on this thread, after its connection has gone
     * back to the pool — or now, outside a submission, where every statement has committed already (autocommit).
     * Dropped when the submission rolls back.
     */
    public void afterCommit(Runnable action) {
        List<Runnable> pending = afterCommit.get();
        if (pending == null) {
            action.run();
        } else {
            pending.add(action);
        }
    }

    /**
     * As {@link #do_query}, for a request that only READS: inside a submission on the submission's own connection (it
     * sees its own writes), otherwise on a pooled connection when there is a pool, so that a long reading holds
     * nothing behind it; on the service's own connection when there is none.
     */
    public <T> T do_read_query(T data,
                               Consumer<T> check,
                               BiConsumer<StringBuilder,T> composeQuery,
                               BiConsumerWithException<ResultSet,T> completeData) {
        if (pool == null || bound.get() != null) {
            return do_query(data, check, composeQuery, completeData);
        }
        if (check!=null) check.accept(data);
        StringBuilder sb=new StringBuilder();
        composeQuery.accept(sb,data);
        String statement=sb.toString();
        try {
            return pool.with(readConn -> {
                ResultSet rs = storage.executeQuery(readConn, statement);
                try (java.sql.Statement st = rs.getStatement(); rs) {    // the statement too: the connection lives on
                    completeData.accept(rs, data);
                }
                return data;
            });
        } catch (SQLException e) {
            e.printStackTrace();
            throw new org.openprovenance.prov.model.exception.UncheckedException("Issue in enactment " + statement, e);
        }
    }

    public <T> T do_query(T data,
                          Consumer<T> check,
                          BiConsumer<StringBuilder,T> composeQuery,
                          BiConsumerWithException<ResultSet,T> completeData) {
        if (check!=null) check.accept(data);
        StringBuilder sb=new StringBuilder();
        composeQuery.accept(sb,data);
        String statement=sb.toString();
        //logger.info("Executing query: " + statement);
        ResultSet rs;
        try {
            rs=storage.executeQuery(currentConnection(),statement);
            completeData.accept(rs, data);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new org.openprovenance.prov.model.exception.UncheckedException("Issue in enactment " + statement, e);
        }
        return data;
    }

    public <T> boolean do_statements(T data,
                                     Consumer<T> check,
                                     BiConsumer<StringBuilder,T> composeQuery) {
        if (check!=null) check.accept(data);
        StringBuilder sb=new StringBuilder();
        composeQuery.accept(sb,data);
        String statement=sb.toString();
        logger.debug("Executing statement: " + statement);
        boolean rs;
        try {
            rs=storage.executeStatements(currentConnection(),statement);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new org.openprovenance.prov.model.exception.UncheckedException("Issue in enactment " + statement, e);
        }
        return rs;
    }



    public long storeBlobs(InputStream descriptorStream, InputStream binaryStream, String mediaType, String name, String insertStatement) throws SQLException, IOException {
        try (descriptorStream; binaryStream) {
            PreparedStatement statement = currentConnection().prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);

            statement.setBinaryStream(1, descriptorStream);
            statement.setBinaryStream(2, binaryStream);
            statement.setString(3, mediaType);
            statement.setString(4, name);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating resource_data, no rows affected.");
            }

            long id;

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            } finally {
                statement.close();
            }
            return id;
        }
    }

    public Triple<InputStream,String,String> getBlobs3(long id, String query) {
        try (PreparedStatement statement = currentConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    InputStream binaryStream = rs.getBinaryStream(1);
                    String mediaType = rs.getString(2);
                    String name = rs.getString(3);
                    return Triple.of(binaryStream, mediaType, name);
                }
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public InputStream getBlobs(long id, String query) {
        try (PreparedStatement statement = currentConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    InputStream binaryStream = rs.getBinaryStream(1);
                    return binaryStream;
                }
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
