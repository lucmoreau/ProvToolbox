package org.openprovenance.prov.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.StatementOrBundle;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class RelationMapping {
    private static final Logger logger = LogManager.getLogger(RelationMapping.class);


    private final TemplateQuery templateQuery;
    private final TemplateDispatcher templateDispatcher;

    public RelationMapping(TemplateQuery templateQuery, TemplateDispatcher templateDispatcher) {
        this.templateQuery = templateQuery;
        this.templateDispatcher = templateDispatcher;
    }

    public void mapGraphToRelations(String template, int id, Object[] record) {
        logger.info("mapGraphToRelations " + template + " " + id + " " + Arrays.asList(record));
        Map<String, Map<String, int[]>> templateRelations = templateDispatcher.getRelations0().get(template);
        String[] foreignTables = templateDispatcher.getForeignTables().get(template);
        Collection<BiConsumer<StringBuilder, Object>> queries = new LinkedList<BiConsumer<StringBuilder, Object>>();
        if (templateRelations != null) {
            for (String property : templateRelations.keySet()) {
                Map<String, int[]> propertyRelations = templateRelations.get(property);
                BiConsumer<StringBuilder, Object> insertForRelations = createInsertForRelations(template, id, record, property, propertyRelations, foreignTables);
                if (insertForRelations != null) queries.add(insertForRelations);
            }
        }
        StringBuilder sb = new StringBuilder();
        queries.forEach(q -> q.accept(sb, null));
        System.out.println("composeQuery = " + sb.toString());

    }

    BiConsumer<StringBuilder, Object> createInsertForRelations(String template, int id, Object[] record, String relation, Map<String, int[]> propertyRelations, String[] foreignTables) {
        Map<String, List<Integer>> rels = propertyRelations
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> Arrays.stream(propertyRelations.get(k)).boxed().collect(Collectors.toList())));
        logger.info("mapGraphToRelation " + template + " " + id + " " + Arrays.asList(record) + " " + relation + " " + rels);

        switch (StatementOrBundle.Kind.valueOf(relation)) {
            case PROV_DERIVATION:

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

            case PROV_GENERATION:
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

            case PROV_USAGE:
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

            case PROV_ASSOCIATION:
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

            case PROV_DELEGATION:
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

            case PROV_SPECIALIZATION:
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

            case PROV_MEMBERSHIP:
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
        return null;
    }

    private String getEntryIfNotMinus1(Object[] record, Integer in) {
        if ((in==null) || (in==-1)) {
            return "";
        }
        Object o = record[in];
        if (o ==null) return "";
        return o.toString();
    }
    private String getTableIfNotMinus1(Object[] record, Integer in) {
        if ((in==null) || (in==-1)) {
            return "";
        }
        Object o = record[in];
        if (o ==null) return "";
        return "'" + o.toString() + "'";
    }

}