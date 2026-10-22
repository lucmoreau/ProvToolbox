package org.openprovenance.prov.service.core;

import org.junit.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PooledConnectionsTest {

    /** A stand-in connection: records the SQL executed on it, valid until told otherwise. */
    static final class Fake {
        final List<String> executed = Collections.synchronizedList(new ArrayList<>());
        final AtomicBoolean valid = new AtomicBoolean(true);
        final AtomicBoolean closed = new AtomicBoolean();
        final AtomicBoolean autoCommit = new AtomicBoolean(true);
        final AtomicInteger commits = new AtomicInteger();
        final AtomicInteger rollbacks = new AtomicInteger();
        final Connection conn = (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Connection.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isValid" -> valid.get() && !closed.get();
                    case "getAutoCommit" -> autoCommit.get();
                    case "setAutoCommit" -> { autoCommit.set((Boolean) args[0]); yield null; }
                    case "commit" -> { commits.incrementAndGet(); yield null; }
                    case "rollback" -> { rollbacks.incrementAndGet(); yield null; }
                    case "close" -> { closed.set(true); yield null; }
                    case "createStatement" -> Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Statement.class},
                            (p2, m2, a2) -> switch (m2.getName()) {
                                case "execute" -> { executed.add((String) a2[0]); yield true; }
                                case "close" -> null;
                                default -> throw new UnsupportedOperationException(m2.getName());
                            });
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    @Test
    public void opensLazilyReusesAndDiscardsTemporaryTablesOnReturn() throws SQLException {
        List<Fake> made = new ArrayList<>();
        PooledConnections pool = new PooledConnections(() -> { Fake f = new Fake(); made.add(f); return f.conn; }, 3);
        assertEquals("nothing opened before the first read", 0, made.size());
        Connection first = pool.with(c -> c);
        Connection second = pool.with(c -> c);
        assertEquals("one connection serves reads in turn", 1, made.size());
        assertSame(first, second);
        assertEquals("each return resets the session's temporary tables", List.of("DISCARD TEMP", "DISCARD TEMP"), made.get(0).executed);
        pool.close();
        assertTrue(made.get(0).closed.get());
    }

    @Test
    public void anInvalidConnectionIsReplaced() throws SQLException {
        List<Fake> made = new ArrayList<>();
        PooledConnections pool = new PooledConnections(() -> { Fake f = new Fake(); made.add(f); return f.conn; }, 2);
        pool.with(c -> c);
        made.get(0).valid.set(false);              // the server dropped it
        Connection next = pool.with(c -> c);
        assertEquals(2, made.size());
        assertSame(made.get(1).conn, next);
        assertTrue("the dead one closed", made.get(0).closed.get());
    }

    @Test
    public void neverMoreThanItsSizeAtOnceAndReadsRunSideBySide() throws Exception {
        AtomicInteger opened = new AtomicInteger();
        PooledConnections pool = new PooledConnections(() -> { opened.incrementAndGet(); return new Fake().conn; }, 2);
        AtomicInteger inUse = new AtomicInteger();
        AtomicInteger peak = new AtomicInteger();
        CountDownLatch bothIn = new CountDownLatch(2);
        ExecutorService exec = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            exec.submit(() -> pool.with(c -> {
                peak.accumulateAndGet(inUse.incrementAndGet(), Math::max);
                bothIn.countDown();
                try {
                    bothIn.await(2, TimeUnit.SECONDS);   // two reads hold their connections at the same time
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                inUse.decrementAndGet();
                return null;
            }));
        }
        exec.shutdown();
        assertTrue(exec.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals("two at once, never three", 2, peak.get());
        assertEquals(2, opened.get());
    }

    @Test
    public void aPoolNeedsAConnection() {
        assertThrows(IllegalArgumentException.class, () -> new PooledConnections(() -> null, 0));
    }

    @Test
    public void theSizeComesFromTheConfigurationElseNone() {
        assertEquals(8, TemplateService.poolSize(8));
        assertEquals(4, TemplateService.poolSize("4"));
        assertEquals(0, TemplateService.poolSize("many"));
        assertEquals(0, TemplateService.poolSize(-3));
    }

    @Test
    public void aTransactionCommitsWhenItReturnsAndRollsBackWhenItThrows() throws SQLException {
        List<Fake> made = new ArrayList<>();
        PooledConnections pool = new PooledConnections(() -> { Fake f = new Fake(); made.add(f); return f.conn; }, 1);
        assertEquals("done", pool.inTransaction(c -> "done"));
        Fake f = made.get(0);
        assertEquals(1, f.commits.get());
        assertEquals(0, f.rollbacks.get());
        assertTrue("autocommit restored for the next borrower", f.autoCommit.get());
        IllegalStateException boom = new IllegalStateException("insert failed");
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> pool.inTransaction(c -> { throw boom; }));
        assertSame("the original exception, unwrapped", boom, thrown);
        assertEquals(1, f.commits.get());
        assertEquals(1, f.rollbacks.get());
        assertTrue(f.autoCommit.get());
    }

    @Test
    public void aSubmissionRunsOnItsOwnConnectionAndNestedOnesJoinIt() {
        Fake service = new Fake();
        List<Fake> made = new ArrayList<>();
        Querier querier = new Querier(new Storage(), service.conn,
                new PooledConnections(() -> { Fake f = new Fake(); made.add(f); return f.conn; }, 2));
        assertSame("outside a submission: the service's connection", service.conn, querier.currentConnection());
        Connection inside = querier.inTransaction(() -> {
            assertTrue(querier.inTransactionNow());
            Connection nested = querier.inTransaction(querier::currentConnection);
            assertSame("a nested submission joins the transaction", querier.currentConnection(), nested);
            return querier.currentConnection();
        });
        assertSame(made.get(0).conn, inside);
        assertEquals(1, made.get(0).commits.get());
        assertTrue(!querier.inTransactionNow());
        assertSame(service.conn, querier.currentConnection());
    }

    @Test
    public void withoutAPoolASubmissionRunsAsBefore() {
        Fake service = new Fake();
        Querier querier = new Querier(new Storage(), service.conn);
        assertSame(service.conn, querier.inTransaction(querier::currentConnection));
        assertEquals("no transaction without a pool", 0, service.commits.get());
    }
}
