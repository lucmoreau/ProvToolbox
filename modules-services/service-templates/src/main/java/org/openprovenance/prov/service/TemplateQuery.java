package org.openprovenance.prov.service;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.CompilerSQL.sqlify;

public class TemplateQuery {
    public static final String[] COMPOSITE_LINKER_COLUMNS = new String[]{"composite","simple"};
    private final Querier querier;
    private final ProvFactory pf = new ProvFactory();
    private final TemplateDispatcher templateDispatcher;
    private final Map<String, TemplateService.Linker> compositeLinker;

    public TemplateQuery(Querier querier, TemplateDispatcher templateDispatcher, Map<String, TemplateService.Linker> compositeLinker) {
        this.querier = querier;
        this.templateDispatcher = templateDispatcher;
        this.compositeLinker = compositeLinker;
    }

    public Document constructDocument(Map<String, FileBuilder> documentBuilderDispatcher, List<Object[]> the_records) {
            IndexedDocument iDoc = new IndexedDocument(pf, pf.newDocument());
            for (Object[] record : the_records) {
                FileBuilder builder = documentBuilderDispatcher.get((String)record[0]);
                if (builder != null) {
                    Document doc = builder.make(record);
                    iDoc.merge(doc);

                } else {
                    throw new UnsupportedOperationException("unknown record " + record[0] + " " + Arrays.asList(record));
                }
            }
            return iDoc.toDocument();
        }

    public List<Object[]> query(String template, Integer id, boolean withTitles) {
        if (isComposite(template)) {
            return queryComposite(template, id, withTitles);
        } else {
            return querySimple(template, id, withTitles);
        }
    }

    private boolean isComposite(String template) {
        return compositeLinker.containsKey(template);
    }

    public List<Object[]> querySimple(String template, Integer id, boolean withTitles) {
        List<Object[]> the_records = new LinkedList<>();
        String[] propertyOrder=templateDispatcher.getPropertyOrder().get(template);
        System.out.println("propertyOrder = " + Arrays.toString(propertyOrder));
        querier.do_query(the_records,
                null,
                (sb, data) -> {
                    sb.append("SELECT * FROM ");
                    sb.append(template);
                    sb.append(" WHERE id=");
                    sb.append(id);
                },
                (rs, data) -> {
                    while (rs.next()) {
                        Object[] record = new Object[propertyOrder.length];
                        record[0]=template;
                        for (int i = 1; i < record.length; i++) {
                            // ISSUE, these are the sql names, not the property names
                            String columnLabel = sqlify(propertyOrder[i]);
                            Object o = rs.getObject(columnLabel);
                            if (o instanceof Timestamp) {
                                o = o.toString();
                            }
                            record[i] = o;
                        }
                        data.add(record);
                    }
                });

        return the_records;
    }

    private static class RecordEntry {
        public String table;
        public Integer key;
        @Override
        public String toString() {
            return "RecordEntry{" +
                    "table='" + table + '\'' +
                    ", key=" + key +
                    '}';
        }
    }
    public List<Object[]> queryComposite(String template, Integer id, boolean withTitles) {
        List<RecordEntry> linked_records = new LinkedList<>();
        TemplateService.Linker linker = compositeLinker.get(template);

        querier.do_query(linked_records,
                null,
                (sb, data) -> {
                    sb.append("SELECT * FROM ");
                    sb.append(linker.table);
                    sb.append(" WHERE composite=");
                    sb.append(id);
                },
                (rs, data) -> {
                    while (rs.next()) {
                        RecordEntry record = new RecordEntry();
                        record.table= linker.linked;
                        int i=1;
                        for (String colum: COMPOSITE_LINKER_COLUMNS) {
                            Integer o = rs.getObject(colum, Integer.class);
                            record.key = o;
                            i++;
                        }
                        data.add(record);
                    }
                });

        System.out.println("linked_records = " + linked_records);

        List<Object[]> the_records = new LinkedList<>();
        for (RecordEntry linked_record : linked_records) {
            Integer simple = linked_record.key;
            List<Object[]> simple_records = querySimple(linked_record.table, simple, withTitles);
            the_records.addAll(simple_records);
        }

        return the_records;
    }

    public List<Object[]> queryTemplates(TableKeyList tableKeyList, boolean withTitles) {
        List<Object[]> result = new LinkedList<>();
        for (TableKey tableKey : tableKeyList.key) {
            List<Object[]> tmp=query(tableKey.isA, tableKey.ID, withTitles);
            result.addAll(tmp);
        }
        return result;
    }
}
