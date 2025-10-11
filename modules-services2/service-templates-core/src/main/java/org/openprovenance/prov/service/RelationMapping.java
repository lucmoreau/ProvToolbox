package org.openprovenance.prov.service;

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

    ExecutorService executor = Executors.newFixedThreadPool(3);

    private final TemplateQuery templateQuery;
    private final CatalogueDispatcherInterface<FileBuilder> templateDispatcher;

    public RelationMapping(TemplateQuery templateQuery, CatalogueDispatcherInterface<FileBuilder> templateDispatcher, Querier querier) {
        this.templateQuery = templateQuery;
        this.templateDispatcher = templateDispatcher;
        this.querier = querier;
    }

    public void mapGraphToRelations(String template, int id, Object[] record) {
        executor.execute(() -> {
            logger.debug("Template: " + template + " " + id);
            Map<String, Map<String, int[]>> templateRelations = templateDispatcher.getRelation0().get(template);
            String[] foreignTables = templateDispatcher.getForeignTables().get(template);
            Collection<BiConsumer<StringBuilder, Object>> queries = new LinkedList<>();
            if (templateRelations != null) {
                for (String property : templateRelations.keySet()) {
                    Map<String, int[]> propertyRelations = templateRelations.get(property);
                    BiConsumer<StringBuilder, Object> insertForRelations = createInsertForRelations(template, id, record, property, propertyRelations, foreignTables);
                    if (insertForRelations != null) queries.add(insertForRelations);
                }
            }
            queries.forEach(q ->
                    executor.execute(() -> querier.do_statements(null, null, q)));
            logger.debug("COMPLETED for " + template + " " + id );
        });
    }

    BiConsumer<StringBuilder, Object> createInsertForRelations(String template, int id, Object[] record, String relation, Map<String, int[]> propertyRelations, String[] foreignTables) {
        Map<String, List<Integer>> rels = propertyRelations
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> Arrays.stream(propertyRelations.get(k)).boxed().collect(Collectors.toList())));

        switch (StatementOrBundle.Kind.valueOf(relation)) {
            case PROV_DERIVATION:
                return composeQueryDerivation(template, id, record, relation, foreignTables, rels);
            case PROV_GENERATION:
                return composeQueryGeneration(template, id, record, relation, foreignTables, rels);
            case PROV_USAGE:
                return composeQueryUsage(template, id, record, relation, foreignTables, rels);
            case PROV_ASSOCIATION:
                return composeQueryAssociation(template, id, record, relation, foreignTables, rels);
            case PROV_DELEGATION:
                return composeQueryDelegation(template, id, record, relation, foreignTables, rels);
            case PROV_SPECIALIZATION:
                return composeQuerySpecialization(template, id, record, relation, foreignTables, rels);
            case PROV_MEMBERSHIP:
                return composeQueryMembership(template, id, record, relation, foreignTables, rels);

        }
        return null;
    }

    private BiConsumer<StringBuilder, Object> composeQueryMembership(String template, int id, Object[] record, String relation, String[] foreignTables, Map<String, List<Integer>> rels) {
        BiConsumer<StringBuilder, Object> composeQuery_mem = (sb, data) -> {
            sb.append("\nINSERT INTO\n  " + TemplateQuery.PREFIX_REL);
            sb.append(relation);
            sb.append(" (collection, collection_rel, entity, entity_rel, template, template_id, rel) VALUES\n");
            boolean first = true;
            for (String relId : rels.keySet()) {
                List<Integer> rels1 = rels.get(relId);
                if (first) {
                    first = false;
                } else {
                    sb.append(",\n");
                }
                sb.append("  (");
                sb.append(getEntryIfNotMinus1(record, rels1.get(0)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(0)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(1)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(1)));
                sb.append(",'").append(template).append("',").append(id);
                sb.append(",'");
                sb.append(relId);
                sb.append("')");
            }
            sb.append(";\n");

        };
        return composeQuery_mem;
    }

    private BiConsumer<StringBuilder, Object> composeQuerySpecialization(String template, int id, Object[] record, String relation, String[] foreignTables, Map<String, List<Integer>> rels) {
        BiConsumer<StringBuilder, Object> composeQuery_spe = (sb, data) -> {
            sb.append("\nINSERT INTO\n  " + TemplateQuery.PREFIX_REL);
            sb.append(relation);
            sb.append(" (specificEntity, specificEntity_rel, generalEntity, generalEntity_rel, template, template_id, rel) VALUES\n");
            boolean first = true;
            for (String relId : rels.keySet()) {
                List<Integer> rels1 = rels.get(relId);
                if (first) {
                    first = false;
                } else {
                    sb.append(",\n");
                }
                sb.append("  (");
                sb.append(getEntryIfNotMinus1(record, rels1.get(0)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(0)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(1)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(1)));
                sb.append(",'").append(template).append("',").append(id);
                sb.append(",'");
                sb.append(relId);
                sb.append("')");
            }
            sb.append(";\n");

        };
        return composeQuery_spe;
    }

    private BiConsumer<StringBuilder, Object> composeQueryDelegation(String template, int id, Object[] record, String relation, String[] foreignTables, Map<String, List<Integer>> rels) {
        BiConsumer<StringBuilder, Object> composeQuery_aobo = (sb, data) -> {
            sb.append("\nINSERT INTO\n  " + TemplateQuery.PREFIX_REL);
            sb.append(relation);
            sb.append(" (delegate, delegate_rel, responsible, responsible_rel, activity, activity_rel, template, template_id, rel) VALUES\n");
            boolean first = true;
            for (String relId : rels.keySet()) {
                List<Integer> rels1 = rels.get(relId);
                if (first) {
                    first = false;
                } else {
                    sb.append(",\n");
                }
                sb.append("  (");
                sb.append(getEntryIfNotMinus1(record, rels1.get(0)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(0)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(1)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(1)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(2)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(2)));
                sb.append(",'").append(template).append("',").append(id);
                sb.append(",'");
                sb.append(relId);
                sb.append("')");
            }
            sb.append(";\n");

        };
        return composeQuery_aobo;
    }

    private BiConsumer<StringBuilder, Object> composeQueryAssociation(String template, int id, Object[] record, String relation, String[] foreignTables, Map<String, List<Integer>> rels) {
        BiConsumer<StringBuilder, Object> composeQuery_waw = (sb, data) -> {
            sb.append("\nINSERT INTO\n  " + TemplateQuery.PREFIX_REL);
            sb.append(relation);
            sb.append(" (activity, activity_rel, agent, agent_rel, plan, plan_rel, template, template_id, rel) VALUES\n");
            boolean first = true;
            for (String relId : rels.keySet()) {
                List<Integer> rels1 = rels.get(relId);
                if (first) {
                    first = false;
                } else {
                    sb.append(",\n");
                }
                sb.append("  (");
                sb.append(getEntryIfNotMinus1(record, rels1.get(0)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(0)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(1)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(1)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(2)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(2)));
                sb.append(",'").append(template).append("',").append(id);
                sb.append(",'");
                sb.append(relId);
                sb.append("')");
            }
            sb.append(";\n");

        };
        return composeQuery_waw;
    }

    private BiConsumer<StringBuilder, Object> composeQueryUsage(String template, int id, Object[] record, String relation, String[] foreignTables, Map<String, List<Integer>> rels) {
        BiConsumer<StringBuilder, Object> composeQuery_use = (sb, data) -> {
            sb.append("\nINSERT INTO\n  " + TemplateQuery.PREFIX_REL);
            sb.append(relation);
            sb.append(" (activity, activity_rel, entity, entity_rel, template, template_id, rel) VALUES\n");
            boolean first = true;
            for (String relId : rels.keySet()) {
                List<Integer> rels1 = rels.get(relId);
                if (first) {
                    first = false;
                } else {
                    sb.append(",\n");
                }
                sb.append("  (");
                sb.append(getEntryIfNotMinus1(record, rels1.get(0)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(0)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(1)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(1)));
                sb.append(",'").append(template).append("',").append(id);
                sb.append(",'");
                sb.append(relId);
                sb.append("')");
            }
            sb.append(";\n");

        };
        return composeQuery_use;
    }

    private BiConsumer<StringBuilder, Object> composeQueryGeneration(String template, int id, Object[] record, String relation, String[] foreignTables, Map<String, List<Integer>> rels) {
        BiConsumer<StringBuilder, Object> composeQuery_gen = (sb, data) -> {
            sb.append("\nINSERT INTO\n  " + TemplateQuery.PREFIX_REL);
            sb.append(relation);
            sb.append(" (entity, entity_rel, activity, activity_rel, template, template_id, rel) VALUES\n");
            boolean first = true;
            for (String relId : rels.keySet()) {
                List<Integer> rels1 = rels.get(relId);
                if (first) {
                    first = false;
                } else {
                    sb.append(",\n");
                }
                sb.append("  (");
                sb.append(getEntryIfNotMinus1(record, rels1.get(0)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(0)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(1)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(1)));
                sb.append(",'").append(template).append("',").append(id);
                sb.append(",'");
                sb.append(relId);
                sb.append("')");
            }
            sb.append(";\n");

        };
        return composeQuery_gen;
    }

    private BiConsumer<StringBuilder, Object> composeQueryDerivation(String template, int id, Object[] record, String relation, String[] foreignTables, Map<String, List<Integer>> rels) {
        BiConsumer<StringBuilder, Object> composeQuery_wdf = (sb, data) -> {
            sb.append("\nINSERT INTO\n  " + TemplateQuery.PREFIX_REL);
            sb.append(relation);
            sb.append(" (generatedEntity, generatedEntity_rel, usedEntity, usedEntity_rel, activity, activity_rel, template, template_id, rel) VALUES\n");
            boolean first = true;
            for (String relId : rels.keySet()) {
                List<Integer> rels1 = rels.get(relId);
                if (first) {
                    first = false;
                } else {
                    sb.append(",\n");
                }
                sb.append("  (");
                sb.append(getEntryIfNotMinus1(record, rels1.get(0)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(0)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(1)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(1)));
                sb.append(",");
                sb.append(getEntryIfNotMinus1(record, rels1.get(2)));
                sb.append(",");
                sb.append(getTableIfNotMinus1(foreignTables, rels1.get(2)));
                sb.append(",'").append(template).append("',").append(id);
                sb.append(",'");
                sb.append(relId);
                sb.append("')");
            }
            sb.append(";\n");

        };

        return composeQuery_wdf;
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