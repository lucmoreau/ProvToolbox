package org.openprovenance.prov.service.core;

import org.junit.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/** The service's own connection is reopened once the driver has closed it (nextgen T-531). */
public class QuerierReconnectTest {

    /** A stand-in connection: open until closed (as pgjdbc closes one whose server went away). */
    static final class Fake {
        final AtomicBoolean closed = new AtomicBoolean();
        final AtomicInteger pings = new AtomicInteger();
        final Connection conn = (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Connection.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "isClosed" -> closed.get();
                    case "isValid" -> { pings.incrementAndGet(); yield !closed.get(); }
                    case "close" -> { closed.set(true); yield null; }
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    @Test
    public void keepsAnOpenConnectionWithoutPingingIt() {
        Fake first = new Fake();
        List<Fake> opened = new ArrayList<>();
        Querier q = new Querier(new Storage(), first.conn, null, () -> { Fake f = new Fake(); opened.add(f); return f.conn; });
        assertSame(first.conn, q.currentConnection());
        assertSame(first.conn, q.currentConnection());
        assertEquals("nothing reopened while the connection is open", 0, opened.size());
        assertEquals("never pinged: a busy connection is not taken for dead", 0, first.pings.get());
    }

    @Test
    public void reopensAClosedConnectionOnceAndKeepsTheNewOne() {
        Fake first = new Fake();
        List<Fake> opened = new ArrayList<>();
        Querier q = new Querier(new Storage(), first.conn, null, () -> { Fake f = new Fake(); opened.add(f); return f.conn; });
        first.closed.set(true);                              // the database restarted: the driver closed the connection
        Connection next = q.currentConnection();
        assertEquals(1, opened.size());
        assertSame(opened.get(0).conn, next);
        assertSame("the new one kept", next, q.currentConnection());
        assertSame(next, q.serviceConnection());
        assertEquals(1, opened.size());
    }

    @Test
    public void returnsTheClosedConnectionWhileTheDatabaseIsStillDown() {
        Fake first = new Fake();
        AtomicInteger attempts = new AtomicInteger();
        AtomicBoolean up = new AtomicBoolean(false);
        Fake later = new Fake();
        Querier q = new Querier(new Storage(), first.conn, null, () -> { attempts.incrementAndGet(); return up.get() ? later.conn : null; });
        first.closed.set(true);
        assertSame("still down: the closed one, whose statement fails", first.conn, q.currentConnection());
        up.set(true);
        assertSame("back: the new one", later.conn, q.currentConnection());
        assertEquals(2, attempts.get());
    }

    @Test
    public void withoutAReopenerTheFirstConnectionIsKeptAsBefore() {
        Fake first = new Fake();
        Querier q = new Querier(new Storage(), first.conn, null);
        first.closed.set(true);
        assertSame(first.conn, q.currentConnection());
        assertTrue(first.closed.get());
    }
}
