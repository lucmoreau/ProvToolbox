package org.openprovenance.prov.service.core.activity;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Where an {@link ActivityLogger} persists its counts. The production sink is
 * {@link JdbcActivitySink}; tests use an in-memory one.
 */
public interface ActivitySink extends AutoCloseable {

    /** Creates the tables if they do not exist; idempotent, called at start and after an outage. */
    void initialize() throws Exception;

    /** Adds the rows' counts to the store: an existing (bucket, kind) row is incremented, not replaced. */
    void write(List<ActivityLogger.Row> rows) throws Exception;

    /** Stores the human description of each kind; an existing description is replaced. */
    void describe(Map<String, String> descriptions) throws Exception;

    /** Deletes the rows whose bucket starts before {@code before}. */
    void prune(Instant before) throws Exception;

    @Override
    default void close() throws Exception { }
}
