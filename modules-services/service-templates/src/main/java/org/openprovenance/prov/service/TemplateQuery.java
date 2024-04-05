package org.openprovenance.prov.service;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.CompilerSQL.sqlify;

public class TemplateQuery {
    private final Querier querier;
    private final ProvFactory pf = new ProvFactory();
    private final TemplateDispatcher templateDispatcher;

    public TemplateQuery(Querier querier, TemplateDispatcher templateDispatcher) {
        this.querier = querier;
        this.templateDispatcher = templateDispatcher;
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
                            record[i] = rs.getObject(columnLabel);
                        }
                        data.add(record);
                    }
                });

        return the_records;
    }
}
