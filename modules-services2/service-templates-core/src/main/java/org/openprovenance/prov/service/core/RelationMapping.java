package org.openprovenance.prov.service.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class RelationMapping {
    private static final Logger logger = LogManager.getLogger(RelationMapping.class);
    private final Querier querier;
    private volatile AsyncRelationWriter asyncWriter;

    ExecutorService executor = Executors.newFixedThreadPool(3);

    private final TemplateQuery templateQuery;
    private final CatalogueDispatcherInterface<FileBuilder> templateDispatcher;

    public RelationMapping(TemplateQuery templateQuery, CatalogueDispatcherInterface<FileBuilder> templateDispatcher, Querier querier) {
        this.templateQuery = templateQuery;
        this.templateDispatcher = templateDispatcher;
        this.querier = querier;
    }

    /**
     * The relation rows of a record just written: inside a submission's transaction, written now on its connection;
     * outside one, on the executor (fire-and-forget, as before); with an {@link AsyncRelationWriter}, queued once the
     * submission has committed (never for one rolled back) and written in batches by its writers.
     */
    public void mapGraphToRelations(String template, int id, Object[] record) {
        if (asyncWriter != null) {
            List<RelationRows> rows = relationRows(template, id, record);
            if (!rows.isEmpty()) {
                querier.afterCommit(() -> asyncWriter.enqueue(rows));
            }
            return;
        }
        if (querier.inTransactionNow()) {
            // Inside a submission's transaction: the relations are written now, on its connection, and commit with it —
            // a reader never sees the record without them, and a failure rolls the whole submission back.
            relationQueries(template, id, record).forEach(q -> querier.do_statements(null, null, q));
            return;
        }
        executor.execute(() -> {
            Collection<BiConsumer<StringBuilder, Object>> queries = relationQueries(template, id, record);
            queries.forEach(q ->
                    executor.execute(() -> querier.do_statements(null, null, q)));
            logger.debug("COMPLETED for " + template + " " + id );
        });
    }

    /** Writes the relations after each submission's commit, batched, instead of with it; null (the default) for inline. */
    public void setAsyncWriter(AsyncRelationWriter asyncWriter) {
        this.asyncWriter = asyncWriter;
    }

    private Collection<BiConsumer<StringBuilder, Object>> relationQueries(String template, int id, Object[] record) {
        Collection<BiConsumer<StringBuilder, Object>> queries = new LinkedList<>();
        for (RelationRows rows : relationRows(template, id, record)) {
            queries.add((sb, data) -> rows.appendInsert(sb));
        }
        return queries;
    }

    /** The rows a record adds to the relation tables, one {@link RelationRows} per relation kind (one INSERT each). */
    List<RelationRows> relationRows(String template, int id, Object[] record) {
        logger.debug("Template: " + template + " " + id);
        Map<String, Map<String, int[]>> templateRelations = templateDispatcher.getRelation0().get(template);
        String[] foreignTables = templateDispatcher.getForeignTables().get(template);
        List<RelationRows> result = new LinkedList<>();
        if (templateRelations != null) {
            for (String property : templateRelations.keySet()) {
                Map<String, int[]> propertyRelations = templateRelations.get(property);
                RelationRows rows = createRowsForRelations(template, id, record, property, propertyRelations, foreignTables);
                if (rows != null) result.add(rows);
            }
        }
        return result;
    }

    /** The columns of each relation table, after which come the template, template_id and rel columns. */
    static String[] relationColumns(StatementOrBundle.Kind kind) {
        switch (kind) {
            case PROV_DERIVATION:     return new String[]{"generatedEntity", "usedEntity", "activity"};
            case PROV_GENERATION:     return new String[]{"entity", "activity"};
            case PROV_USAGE:          return new String[]{"activity", "entity"};
            case PROV_ASSOCIATION:    return new String[]{"activity", "agent", "plan"};
            case PROV_DELEGATION:     return new String[]{"delegate", "responsible", "activity"};
            case PROV_SPECIALIZATION: return new String[]{"specificEntity", "generalEntity"};
            case PROV_MEMBERSHIP:     return new String[]{"collection", "entity"};
            default:                  return null;
        }
    }

    RelationRows createRowsForRelations(String template, int id, Object[] record, String relation, Map<String, int[]> propertyRelations, String[] foreignTables) {
        Map<String, List<Integer>> rels = propertyRelations
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> Arrays.stream(propertyRelations.get(k)).boxed().collect(Collectors.toList())));

        String[] columns = relationColumns(StatementOrBundle.Kind.valueOf(relation));
        if (columns == null || rels.isEmpty()) {
            return null;
        }
        StringBuilder header = new StringBuilder();
        for (String column : columns) {
            header.append(column).append(", ").append(column).append("_rel, ");
        }
        header.append("template, template_id, rel");
        List<String> tuples = new ArrayList<>(rels.size());
        for (String relId : rels.keySet()) {
            List<Integer> rels1 = rels.get(relId);
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < columns.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(i)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(i)));
            }
            sb.append(",'").append(template).append("',").append(id);
            sb.append(",'");
            sb.append(relId);
            sb.append("')");
            tuples.add(sb.toString());
        }
        return new RelationRows(TemplateQuery.PREFIX_REL + relation, header.toString(), tuples);
    }

    /** The rows one record adds to one relation table: a multi-row INSERT, or a part of a batch's ({@link #appendInsert(StringBuilder, String, String, List)}). */
    public static final class RelationRows {
        public final String table;
        public final String columns;
        public final List<String> tuples;

        RelationRows(String table, String columns, List<String> tuples) {
            this.table = table;
            this.columns = columns;
            this.tuples = tuples;
        }

        /** The INSERT of these rows, as the service always wrote it. */
        public void appendInsert(StringBuilder sb) {
            RelationRows.appendInsert(sb, table, columns, tuples);
        }

        static void appendInsert(StringBuilder sb, String table, String columns, List<String> tuples) {
            sb.append("\nINSERT INTO\n  ").append(table);
            sb.append(" (").append(columns).append(") VALUES\n");
            boolean first = true;
            for (String tuple : tuples) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",\n");
                }
                sb.append("  ").append(tuple);
            }
            sb.append(";\n");
        }
    }

    private String getEntryIfNotMinus1(Object[] record, Integer in) {
        if ((in==null) || (in==-1)) {
            return "null";
        }
        Object o = record[in];
        if (o == null) return "null";
        return o.toString();
    }
    private String getTableIfNotMinus1(Object[] record, Integer in) {
        if ((in==null) || (in==-1)) {
            return "null";
        }
        Object o = record[in];
        if (o == null) return "''";
        return "'" + o.toString() + "'";
    }

}