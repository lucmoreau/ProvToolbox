package org.openprovenance.prov.service.core;

import org.junit.Test;
import org.openprovenance.prov.model.StatementOrBundle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/** A record's relation rows: the INSERTs as the service always wrote them, and merged per table for a batch. */
public class RelationRowsTest {

    private final RelationMapping mapping = new RelationMapping(null, FakeCatalogue.create(), new Querier(new Storage(), null));

    private static Object[] making(int id) {
        Map<String, Object> row = FakeCatalogue.makingRow(id);
        return new Object[]{FakeCatalogue.MAKING, row.get("ID"), row.get("thing0"), row.get("thing1"), row.get("making")};
    }

    private static String sql(RelationMapping.RelationRows rows) {
        StringBuilder sb = new StringBuilder();
        rows.appendInsert(sb);
        return sb.toString();
    }

    @Test
    public void oneInsertPerRelationKindAsBefore() {
        List<RelationMapping.RelationRows> rows = mapping.relationRows(FakeCatalogue.MAKING, 7, making(7));
        Map<String, String> byTable = rows.stream().collect(Collectors.toMap(r -> r.table, RelationRowsTest::sql));
        assertEquals("\nINSERT INTO\n  __PROV_GENERATION (entity, entity_rel, activity, activity_rel, template, template_id, rel) VALUES\n"
                        + "  (207,'thing',307,'activity','t.Making',7,'gen1');\n",
                byTable.get("__PROV_GENERATION"));
        assertEquals("\nINSERT INTO\n  __PROV_USAGE (activity, activity_rel, entity, entity_rel, template, template_id, rel) VALUES\n"
                        + "  (307,'activity',107,'thing','t.Making',7,'use1');\n",
                byTable.get("__PROV_USAGE"));
    }

    @Test
    public void columnsOfEveryRelationTable() {
        assertEquals(List.of("generatedEntity", "usedEntity", "activity"), List.of(RelationMapping.relationColumns(StatementOrBundle.Kind.PROV_DERIVATION)));
        assertEquals(List.of("activity", "agent", "plan"), List.of(RelationMapping.relationColumns(StatementOrBundle.Kind.PROV_ASSOCIATION)));
        assertEquals(List.of("delegate", "responsible", "activity"), List.of(RelationMapping.relationColumns(StatementOrBundle.Kind.PROV_DELEGATION)));
        assertEquals(List.of("specificEntity", "generalEntity"), List.of(RelationMapping.relationColumns(StatementOrBundle.Kind.PROV_SPECIALIZATION)));
        assertEquals(List.of("collection", "entity"), List.of(RelationMapping.relationColumns(StatementOrBundle.Kind.PROV_MEMBERSHIP)));
        assertNull(RelationMapping.relationColumns(StatementOrBundle.Kind.PROV_ENTITY));
    }

    @Test
    public void anAbsentPositionIsNull() {
        RelationMapping.RelationRows rows = mapping.createRowsForRelations("t.Running", 9, new Object[]{"t.Running", 9, 5, 6},
                "PROV_ASSOCIATION", Map.of("assoc1", new int[]{2, 3, -1}), new String[]{null, null, "activity", "agent"});
        assertEquals("\nINSERT INTO\n  __PROV_ASSOCIATION (activity, activity_rel, agent, agent_rel, plan, plan_rel, template, template_id, rel) VALUES\n"
                + "  (5,'activity',6,'agent',null,null,'t.Running',9,'assoc1');\n", sql(rows));
    }

    @Test
    public void aBatchMergesTheRecordsRowsPerTable() {
        List<AsyncRelationWriter.Item> items = List.of(
                new AsyncRelationWriter.Item(mapping.relationRows(FakeCatalogue.MAKING, 1, making(1)), 0),
                new AsyncRelationWriter.Item(mapping.relationRows(FakeCatalogue.MAKING, 2, making(2)), 0));
        String batch = AsyncRelationWriter.batchStatements(items);
        assertEquals("one INSERT per table", 2, batch.split("INSERT INTO").length - 1);
        assertEquals(batch, true, batch.contains("  (201,'thing',301,'activity','t.Making',1,'gen1'),\n  (202,'thing',302,'activity','t.Making',2,'gen1');\n"));
        assertEquals(batch, true, batch.contains("  (301,'activity',101,'thing','t.Making',1,'use1'),\n  (302,'activity',102,'thing','t.Making',2,'use1');\n"));
    }
}
