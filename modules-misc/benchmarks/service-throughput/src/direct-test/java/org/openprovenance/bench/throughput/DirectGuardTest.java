package org.openprovenance.bench.throughput;

import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * The guard in the direct JDBC mode: it runs before anything is opened, reset or written; the mode's connections go
 * only to the benchmark's store; the service's classes, running in-process, see none of the caller's environment.
 */
public class DirectGuardTest {

    /** A store whose guard refuses (as for a foreign PostgreSQL on 55432), recording what is asked of it. */
    static final class RefusingStore extends Store {
        final List<String> calls = new ArrayList<>();

        RefusingStore(Settings s) {
            super(s);
        }

        @Override
        String guard() {
            calls.add("guard");
            throw new Guard.Refused("the cluster on port 55432 holds databases other than provbench: [bookptm]");
        }

        @Override
        void reset() {
            calls.add("reset");
        }
    }

    @Test
    public void aRefusingGuardStopsTheRunBeforeAnyConnectionOrReset() throws Exception {
        Settings s = new Settings(new String[]{"LAYER=insert", "N=4"});
        RefusingStore store = new RefusingStore(s);
        List<String> opened = new ArrayList<>();
        DirectRun run = new DirectRun(s, store, application -> {
            opened.add(application);
            throw new AssertionError("a connection was opened");
        });
        try {
            run.execute("t1");
            fail("the run went on");
        } catch (Guard.Refused expected) {
            assertTrue(expected.getMessage().startsWith("GUARD: refused"));
        }
        assertEquals(List.of("guard"), store.calls);
        assertEquals(List.of(), opened);
    }

    @Test
    public void theDirectModeConnectsToTheBenchmarksStoreOnly() throws Exception {
        for (String db : new String[]{"bookptm", "templates", "chronicle_ledger", "provbenchx"}) {
            try (Connection c = Store.connect(db, DirectStore.WRITER_APP)) {
                fail("connected to " + db);
            } catch (Guard.Refused expected) {
                // refused before any network
            }
        }
        assertEquals("jdbc:postgresql://localhost:55432/provbench", Guard.jdbcUrl(Settings.JDBC_URL));
    }

    @Test
    public void theServiceClassesSeeNoneOfTheCallersEnvironment() {
        assertTrue(DirectMain.isServiceVariable("TPL_DB_USER"));
        assertTrue(DirectMain.isServiceVariable("TPL_DB_PASS"));
        assertTrue(DirectMain.isServiceVariable("TPL_SECURITY_CONFIG"));
        assertTrue(DirectMain.isServiceVariable("PROV_STORAGE_CONFIG"));
        assertTrue(DirectMain.isServiceVariable("TEMPLATE_CONFIG"));
        assertTrue(DirectMain.isServiceVariable("JDBC_POOL_SIZE"));
        assertFalse(DirectMain.isServiceVariable("PATH"));
        assertTrue(DirectMain.needsCleanEnvironment(Map.of("PATH", "/bin", "TPL_DB_USER", "someone")));
        assertFalse(DirectMain.needsCleanEnvironment(Map.of("PATH", "/bin", "HOME", "/home")));
    }

    @Test
    public void aBatchWithPostProcessingIsRefusedBeforeAnyConnection() throws Exception {
        Settings s = new Settings(new String[]{"LAYER=post", "JDBC_BATCH=1"});
        List<String> opened = new ArrayList<>();
        try {
            new DirectStore(s, Family.fs(3), 1, application -> {
                opened.add(application);
                return null;
            });
            fail("accepted");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("JDBC_BATCH"));
        }
        assertEquals(List.of(), opened);
    }

    @Test
    public void theSettingsOfTheLayers() {
        refused(new String[]{"LAYER=everything"}, "LAYER");
        refused(new String[]{"KEY=maybe"}, "KEY");
        assertEquals("on", new Settings(new String[]{}).get("KEY"));
        assertEquals("post", DirectStore.layer(new Settings(new String[]{})));
        assertEquals("submission.readback.access_control=off submission.hash=header relations.write=async relations.async.writers=1 "
                        + "relations.async.queue=10000 relations.async.batch=200",
                DirectStore.switchesOf(new Settings(new String[]{"READBACK=off", "HASH=header", "RELATIONS=async", "WRITERS=1"})).toString());
        try {
            DirectStore.switchesOf(new Settings(new String[]{"HASH=sometimes"}));
            fail("accepted");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("submission.hash"));
        }
    }

    private static void refused(String[] args, String named) {
        try {
            Settings s = new Settings(args);
            DirectStore.layer(s);
            DirectStore.onOff(s, "KEY");
            fail("accepted " + String.join(" ", args));
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage(), expected.getMessage().contains(named));
        }
    }

    @Test
    public void aRecordIsItsTemplatesValuesInPropertyOrderAsJsonWouldGiveThem() {
        Object[] r = DirectStore.record(Map.of("isA", "t.Making", "thing0", 12L, "name", "n"), new String[]{"isA", "ID", "thing0", "name"});
        assertArrayEquals(new Object[]{"t.Making", null, 12, "n"}, r);
        assertEquals(Integer.class, r[2].getClass());
    }
}
