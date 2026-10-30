package org.openprovenance.prov.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openprovenance.prov.model.interop.PrincipalManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A submission's post-processing ({@link TemplateLogic#submitPostProcessing}) under the read-back and hash switches,
 * on a stand-in database: what it reads, what it writes, which header it leaves. Reading through {@link TemplateQuery}'s
 * other entry points (as GET does) keeps the access control, whatever the switches.
 */
public class WritePathSwitchesTest {

    private static final String JOIN = "LEFT JOIN access_control";
    private static final String FILTER = "(record_index.principal='joe' OR access_control.record IS NOT NULL)";

    private FakeDatabase db;
    private TemplateQuery query;
    private TemplateLogic logic;

    @Before
    public void setUp() {
        db = new FakeDatabase();
        db.answers = sql -> {
            if (sql.contains("FROM making_composite")) return List.of(Map.of("composite", 5, "simple", 7));
            if (sql.contains("FROM making")) return List.of(FakeCatalogue.makingRow(7));
            return List.of();
        };
        PrincipalManager principals = new PrincipalManager();
        principals.setPrincipal("joe");
        ObjectMapper om = new ObjectMapper();
        Map<String, TemplateService.Linker> linkers = Map.of(FakeCatalogue.COMPOSITE, new TemplateService.Linker("making_composite_linker", "making"));
        // a pool, so that a submission is a transaction and its relations are written inline, on its connection
        Querier querier = new Querier(new Storage(), db.connection(), new PooledConnections(db::connection, 2));
        query = new TemplateQuery(querier, FakeCatalogue.create(), principals, linkers, om);
        logic = new TemplateLogic(new org.openprovenance.prov.vanilla.ProvFactory(), query, query.getShortNames(),
                FakeCatalogue.create(), principals, null, om, null);
        TemplateService.headerInfo.get().clear();
        db.queries.clear();
        db.statements.clear();
    }

    @After
    public void tearDown() {
        TemplateService.headerInfo.get().clear();
    }

    private void submit(String template, int id) {
        query.inTransaction(() -> logic.submitPostProcessing(id, template));
    }

    private List<String> readBacks() {
        return db.queries.stream().filter(q -> q.contains("template")).collect(Collectors.toList());
    }

    private long updates() {
        return db.statements.stream().filter(s -> s.startsWith("UPDATE record_index")).count();
    }

    private long relationInserts() {
        return db.statements.stream().filter(s -> s.contains("INSERT INTO\n  __PROV_")).count();
    }

    @Test
    public void byDefaultTheServiceAsItWas() {
        submit(FakeCatalogue.MAKING, 7);
        assertEquals(1, readBacks().size());
        assertTrue(readBacks().get(0), readBacks().get(0).contains(JOIN));
        assertTrue(readBacks().get(0), readBacks().get(0).contains(FILTER));
        assertEquals("the hash stored", 1, updates());
        assertEquals("the relations written with the submission", 2, relationInserts());
        assertNotNull(TemplateService.headerInfo.get().get(TemplateLogic.HTTP_HEADER_CONTENT_PROV_HASH));
        assertEquals("http://localhost:8080/ems/provapi/template/t.Making/7", TemplateService.headerInfo.get().get(TemplateLogic.HTTP_HEADER_LOCATION));
    }

    @Test
    public void readBackWithoutAccessControl() {
        logic.setSwitches(new SubmissionSwitches(false, SubmissionSwitches.Hash.UPDATE, SubmissionSwitches.Relations.INLINE, 1, 1, 1));
        submit(FakeCatalogue.MAKING, 7);
        String readBack = readBacks().get(0);
        assertFalse(readBack, readBack.contains("access_control"));
        assertFalse(readBack, readBack.contains("record_index"));
        assertTrue(readBack, readBack.contains("WHERE template.id=7"));
        assertEquals("the rest unchanged", 1, updates());
        assertEquals(2, relationInserts());
    }

    @Test
    public void everyOtherReadingKeepsTheAccessControlWithTheSwitchOff() {
        logic.setSwitches(new SubmissionSwitches(false, SubmissionSwitches.Hash.UPDATE, SubmissionSwitches.Relations.INLINE, 1, 1, 1));
        submit(FakeCatalogue.MAKING, 7);
        db.queries.clear();
        // GET provapi/template/{template}/{id}: TemplateQuery.query, as getTemplateInstanceWithId reads
        assertEquals(1, query.query(FakeCatalogue.MAKING, 7, false, "joe").size());
        query.querySimple(FakeCatalogue.MAKING, 7, false, "joe");
        query.queryComposite(FakeCatalogue.COMPOSITE, 5, false, "joe");
        query.query(FakeCatalogue.COMPOSITE, 5, false, "joe");
        assertEquals(6, db.queries.size());
        for (String q : db.queries) {
            assertTrue(q, q.contains(JOIN));
            assertTrue(q, q.contains(FILTER));
        }
    }

    @Test
    public void aCompositeReadBackWithoutAccessControl() {
        logic.setSwitches(new SubmissionSwitches(false, SubmissionSwitches.Hash.UPDATE, SubmissionSwitches.Relations.INLINE, 1, 1, 1));
        submit(FakeCatalogue.COMPOSITE, 5);
        assertEquals(2, db.queries.size());
        assertTrue(db.queries.get(0), db.queries.get(0).contains("SELECT template.id AS key FROM making_composite AS template"));
        for (String q : db.queries) assertFalse(q, q.contains("access_control"));
        // one element: post-processed as a simple record would be, under the composite's name, which has no relations
        assertEquals(1, updates());
        assertEquals(0, relationInserts());

        setUp();
        submit(FakeCatalogue.COMPOSITE, 5);
        assertEquals("with the access control (the default), the composite's reading as before", 2, db.queries.size());
        for (String q : db.queries) assertTrue(q, q.contains(JOIN));
    }

    @Test
    public void hashSentButNotStored() {
        logic.setSwitches(new SubmissionSwitches(true, SubmissionSwitches.Hash.HEADER, SubmissionSwitches.Relations.INLINE, 1, 1, 1));
        submit(FakeCatalogue.MAKING, 7);
        assertEquals(0, updates());
        assertNotNull(TemplateService.headerInfo.get().get(TemplateLogic.HTTP_HEADER_CONTENT_PROV_HASH));
        assertEquals(2, relationInserts());
    }

    @Test
    public void noHashAtAll() {
        TemplateService.headerInfo.get().put(TemplateLogic.HTTP_HEADER_CONTENT_PROV_HASH, "this thread's previous request's");
        logic.setSwitches(new SubmissionSwitches(true, SubmissionSwitches.Hash.OFF, SubmissionSwitches.Relations.INLINE, 1, 1, 1));
        submit(FakeCatalogue.MAKING, 7);
        assertEquals(0, updates());
        assertNull("no header, not even a stale one", TemplateService.headerInfo.get().get(TemplateLogic.HTTP_HEADER_CONTENT_PROV_HASH));
        assertNotNull(TemplateService.headerInfo.get().get(TemplateLogic.HTTP_HEADER_LOCATION));
        assertEquals(2, relationInserts());
    }

    @Test
    public void asyncRelationsLeaveTheSubmission() {
        FakeDatabase writers = new FakeDatabase();
        AsyncRelationWriter writer = new AsyncRelationWriter(new PooledConnections(writers::connection, 1), 1, 10, 10);
        query.getRelationMapping().setAsyncWriter(writer);
        submit(FakeCatalogue.MAKING, 7);
        writer.close();
        assertEquals("none on the submission's connection", 0, relationInserts());
        assertEquals(1, updates());
        assertEquals(2, writers.statements.stream().mapToLong(s -> s.split("INSERT INTO", -1).length - 1).sum());
    }
}
