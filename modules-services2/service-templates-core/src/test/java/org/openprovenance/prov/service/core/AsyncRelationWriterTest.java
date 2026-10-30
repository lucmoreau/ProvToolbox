package org.openprovenance.prov.service.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * relations.write=async: the rows of a submission are queued only once it has committed (never for one rolled back),
 * written in batches on the writers' own connections, with back-pressure when the queue is full, and drained on close.
 */
public class AsyncRelationWriterTest {

    private final RelationMapping rowsOnly = new RelationMapping(null, FakeCatalogue.create(), new Querier(new Storage(), null));

    private List<RelationMapping.RelationRows> rows(int id) {
        Map<String, Object> r = FakeCatalogue.makingRow(id);
        return rowsOnly.relationRows(FakeCatalogue.MAKING, id,
                new Object[]{FakeCatalogue.MAKING, r.get("ID"), r.get("thing0"), r.get("thing1"), r.get("making")});
    }

    private static long count(List<String> statements, String needle) {
        return statements.stream().mapToLong(s -> s.split(java.util.regex.Pattern.quote(needle), -1).length - 1).sum();
    }

    @Test
    public void queuedOnlyOnceTheSubmissionHasCommitted() {
        FakeDatabase requests = new FakeDatabase();
        FakeDatabase writers = new FakeDatabase();
        Querier querier = new Querier(new Storage(), requests.connection(), new PooledConnections(requests::connection, 2));
        AsyncRelationWriter writer = new AsyncRelationWriter(new PooledConnections(writers::connection, 1), 1, 100, 10);
        RelationMapping mapping = new RelationMapping(null, FakeCatalogue.create(), querier);
        mapping.setAsyncWriter(writer);

        // a submission that commits: nothing written on its own connection, its rows queued after its commit
        Object[] queuedInside = {null};
        querier.inTransaction(() -> {
            mapping.mapGraphToRelations(FakeCatalogue.MAKING, 1, new Object[]{FakeCatalogue.MAKING, 1, 101, 201, 301});
            queuedInside[0] = writer.status().get("queued");
            return null;
        });
        assertEquals("nothing queued before the commit", 0L, queuedInside[0]);
        // a submission that rolls back: its rows never queued
        try {
            querier.inTransaction(() -> {
                mapping.mapGraphToRelations(FakeCatalogue.MAKING, 2, new Object[]{FakeCatalogue.MAKING, 2, 102, 202, 302});
                throw new IllegalStateException("the submission fails");
            });
        } catch (IllegalStateException expected) {
            // rolled back
        }
        writer.close();
        assertEquals("no relation written with the submissions", 0, count(requests.statements, "__PROV_"));
        assertEquals(1L, writer.status().get("queued"));
        assertEquals(1L, writer.status().get("written"));
        assertEquals(1, count(writers.statements, "'t.Making',1,'gen1'"));
        assertEquals("the rolled-back submission's rows never written", 0, count(writers.statements, "'t.Making',2,"));
        assertEquals(1, requests.rollbacks.get());
    }

    @Test
    public void withoutAPoolTheRowsAreQueuedAtOnce() {
        FakeDatabase shared = new FakeDatabase();
        FakeDatabase writers = new FakeDatabase();
        Querier querier = new Querier(new Storage(), shared.connection());   // every statement autocommits
        AsyncRelationWriter writer = new AsyncRelationWriter(new PooledConnections(writers::connection, 1), 1, 100, 10);
        RelationMapping mapping = new RelationMapping(null, FakeCatalogue.create(), querier);
        mapping.setAsyncWriter(writer);
        mapping.mapGraphToRelations(FakeCatalogue.MAKING, 1, new Object[]{FakeCatalogue.MAKING, 1, 101, 201, 301});
        writer.close();
        assertEquals(0, count(shared.statements, "__PROV_"));
        assertEquals(1L, writer.status().get("written"));
    }

    @Test
    public void batchedOneInsertPerTableAndDrainedOnClose() throws InterruptedException {
        FakeDatabase writers = new FakeDatabase();
        CountDownLatch release = new CountDownLatch(1);
        writers.beforeStatement = () -> {
            try {
                release.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        AsyncRelationWriter writer = new AsyncRelationWriter(new PooledConnections(writers::connection, 1), 1, 1000, 50);
        for (int i = 1; i <= 120; i++) writer.enqueue(rows(i));
        release.countDown();
        writer.close();
        assertEquals(120L, writer.status().get("written"));
        assertEquals(0, writer.depth());
        assertEquals("every record's rows written once", 120, count(writers.statements, "'gen1'"));
        long batches = (Long) writer.status().get("batches");
        assertTrue("batched: " + batches, batches <= 4);   // the first record alone, then batches of at most 50
        assertEquals("one INSERT per table per batch", 2 * batches, count(writers.statements, "INSERT INTO"));
        assertEquals("one transaction per batch", batches, writers.commits.get());
    }

    @Test
    public void aFullQueueMakesTheSubmitterWait() throws InterruptedException {
        FakeDatabase writers = new FakeDatabase();
        CountDownLatch release = new CountDownLatch(1);
        writers.beforeStatement = () -> {
            try {
                release.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        AsyncRelationWriter writer = new AsyncRelationWriter(new PooledConnections(writers::connection, 1), 1, 2, 1);
        writer.enqueue(rows(1));                       // taken by the writer, which blocks on the database
        while (writer.depth() > 0) Thread.sleep(5);
        writer.enqueue(rows(2));
        writer.enqueue(rows(3));                       // the queue is full now
        AtomicBoolean fourthQueued = new AtomicBoolean();
        Thread submitter = new Thread(() -> {
            writer.enqueue(rows(4));
            fourthQueued.set(true);
        });
        submitter.start();
        Thread.sleep(200);
        assertFalse("the submitter waits for room", fourthQueued.get());
        assertEquals(1L, writer.status().get("blocked"));
        release.countDown();
        submitter.join(5000);
        assertTrue(fourthQueued.get());
        writer.close();
        assertEquals(4L, writer.status().get("written"));
    }

    @Test
    public void aFailingBatchIsRetriedRecordByRecord() {
        FakeDatabase writers = new FakeDatabase();
        writers.failing = sql -> sql.contains("'t.Making',3,");    // the third record's rows are refused
        CountDownLatch release = new CountDownLatch(1);
        writers.beforeStatement = () -> {
            try {
                release.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        AsyncRelationWriter writer = new AsyncRelationWriter(new PooledConnections(writers::connection, 1), 1, 100, 100);
        List<Integer> ids = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            writer.enqueue(rows(i));
            ids.add(i);
        }
        release.countDown();
        writer.close();
        assertEquals(4L, writer.status().get("written"));
        assertEquals(1L, writer.status().get("failed"));
        for (int i : ids) {
            assertEquals("record " + i, i == 3 ? 0 : 1, count(writers.statements, "'t.Making'," + i + ",'gen1'"));
        }
    }
}
