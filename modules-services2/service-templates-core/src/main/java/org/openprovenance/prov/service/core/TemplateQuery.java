package org.openprovenance.prov.service.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.model.interop.PrincipalManager;
import org.openprovenance.prov.service.core.readers.TableKey;
import org.openprovenance.prov.service.core.readers.TableKeyList;
import org.openprovenance.prov.service.core.readers.SearchConfig;
import org.openprovenance.prov.template.compiler.sql.QueryBuilder;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.postgresql.util.PGobject;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerSQL.sqlify;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.*;
import static org.openprovenance.prov.template.compiler.sql.QueryBuilder.unquote;

public class TemplateQuery {
    // declare logger
    private static final Logger logger = LogManager.getLogger(TemplateQuery.class);
    public static final String PARAM_ID = "__param_id";
    public static final String PARAM_PROPERTY = "__param_property";
    public static final String PARAM_TEMPLATE = "__param_template";
    public static final String IN_TEMPLATE = "in_template";
    public static final String IN_PROPERTY = "in_property";
    public static final String IN_ID = "in_id";
    public static final String OUT_ID = "out_id";
    public static final String OUT_TEMPLATE = "out_template";
    public static final String OUT_PROPERTY = "out_property";

    public static final String[] COMPOSITE_LINKER_COLUMNS = new String[]{"composite","simple"};
    public static final String SHA_3_512 = "SHA3-512";
    public static final String PREFIX_REL = "__";
    private final Querier querier;
    private final ProvFactory pf = new ProvFactory();
    private final CatalogueDispatcherInterface<FileBuilder> templateDispatcher;
    private final Map<String, TemplateService.Linker> compositeLinker;
    private final ObjectMapper om;
    private final Map<String, Map<String, Map<String, String>>> ioMap;
    private final Map<String, FileBuilder> documentBuilderDispatcher;


    static TypeReference<Map<String,Map<String, Map<String, String>>>> typeRef = new TypeReference<>() {};
    private final Map<String, Map<String, List<String>>> successors;
    private final RelationMapping relationMapping;


    public TemplateQuery(Querier querier, CatalogueDispatcherInterface<FileBuilder> templateDispatcher, PrincipalManager principalManager, Map<String, TemplateService.Linker> compositeLinker, ObjectMapper om) {
        this.querier = querier;
        this.templateDispatcher = templateDispatcher;
        this.compositeLinker = compositeLinker;
        this.om = om;
        this.documentBuilderDispatcher = templateDispatcher.getDocumentBuilderDispatcher();
        this.ioMap = getIoMap(templateDispatcher.getIoMap());
        this.successors = templateDispatcher.getSuccessors();
        this.relationMapping = new RelationMapping(this,templateDispatcher,querier);

        logger.info("ioMap = " + ioMap);

        generateTraversalMethods(querier, this.ioMap);
    }

    RelationMapping getRelationMapping() {
        return relationMapping;
    }


    String recursiveQuery="CREATE OR REPLACE FUNCTION backwardtraversal_star(\n" +
            "    __param_id integer, \n" +
            "    __param_template text, \n" +
            "    __param_property text\n" +
            ") RETURNS TABLE(\n" +
            "    in_id integer, \n" +
            "    in_template text, \n" +
            "    in_property text, \n" +
            "    out_id integer, \n" +
            "    out_template text, \n" +
            "    out_property text\n" +
            ")\n" +
            "AS $$\n" +
            "WITH RECURSIVE recurse_traverse AS (\n" +
            "    -- Initial query from backwardTraversal function with an additional column for recursion depth\n" +
            "    SELECT in_id, in_template, in_property, out_id, out_template, out_property, 1 AS depth\n" +
            "    FROM backwardTraversal(__param_id, __param_template, __param_property)\n" +
            "\n" +
            "    UNION \n" +
            "\n" +
            "\t-- Recursive step using the output of backwardTraversal called on new parameters obtained from predecessor_table\n" +
            "    SELECT bt.in_id, bt.in_template, bt.in_property, bt.out_id, bt.out_template, bt.out_property, rt.depth + 1 AS depth\n" +
            "    FROM recurse_traverse rt\n" +
            "    JOIN predecessor_table pt ON rt.out_template = pt.template AND rt.out_property = pt.output\n" +
            "    CROSS JOIN LATERAL backwardTraversal(rt.out_id, rt.out_template, pt.input) AS bt\n" +
            "    WHERE pt.input IS NOT NULL AND rt.depth < 100 -- Ensuring pt.input is not null and limiting recursion depth\n" +
            "\n" +
            ")\n" +
            "SELECT in_id, in_template, in_property, out_id, out_template, out_property\n" +
            "FROM recurse_traverse\n" +
            "$$ LANGUAGE SQL;";

    /* now provides an output for the template, rather than input. */
    String recursiveQuery2="CREATE OR REPLACE FUNCTION backwardtraversal_star(\n" +
            "    __param_id integer, \n" +
            "    __param_template text, \n" +
            "    __param_property text\n" +
            ") RETURNS TABLE(\n" +
            "    in_id integer, \n" +
            "    in_template text, \n" +
            "    in_property text, \n" +
            "    out_id integer, \n" +
            "    out_template text, \n" +
            "    out_property text\n" +
            ")\n" +
            "AS $$\n" +
            "WITH RECURSIVE recurse_traverse AS (\n" +
            "    -- Initial query from backwardTraversal function with an additional column for recursion depth\n" +
            "\t\n" +
            "    SELECT \n" +
            "        bt.in_id, \n" +
            "        bt.in_template, \n" +
            "        bt.in_property, \n" +
            "        bt.out_id, \n" +
            "        bt.out_template, \n" +
            "        bt.out_property, \n" +
            "        1 AS depth\n" +
            "    FROM \n" +
            "        predecessor_table as pt \n" +
            "        CROSS JOIN LATERAL backwardTraversal(__param_id, __param_template, pt.input) AS bt\n" +
            "    WHERE \n" +
            "        pt.template = __param_template \n" +
            "        AND pt.output = __param_property\n" +
            "\t\n" +
            "    UNION \n" +
            "\n" +
            "\t-- Recursive step using the output of backwardTraversal called on new parameters obtained from predecessor_table\n" +
            "    SELECT bt.in_id, bt.in_template, bt.in_property, bt.out_id, bt.out_template, bt.out_property, rt.depth + 1 AS depth\n" +
            "    FROM recurse_traverse rt\n" +
            "    JOIN predecessor_table pt ON rt.out_template = pt.template AND rt.out_property = pt.output\n" +
            "    CROSS JOIN LATERAL backwardTraversal(rt.out_id, rt.out_template, pt.input) AS bt\n" +
            "    WHERE pt.input IS NOT NULL AND rt.depth < 100 -- Ensuring pt.input is not null and limiting recursion depth\n" +
            "\n" +
            ")\n" +
            "SELECT in_id, in_template, in_property, out_id, out_template, out_property\n" +
            "FROM recurse_traverse\n" +
            "$$ LANGUAGE SQL;";

    private void generateTraversalMethods(Querier querier,  Map<String,Map<String, Map<String, String>>> ioMap) {

        querier.do_statements(null,
                null,
                (sb, data) -> {
                    sb.append(generateBackwardTemplateTraversal(ioMap));
                    sb.append(recursiveQuery2);
                });


    }

    public void generateViz(Integer id, String template, String property, String style, Map<String, Map<String, String>> baseTypes, String principal, OutputStream out) {

        //logger.info("generateViz " + id + " " + template + " " + property);

        List<TemplateConnection> templateConnections = recursiveTraversal(id, template, property, principal);
        // reverse list
        Collections.reverse(templateConnections);

        //logger.info("templateConnections: " + templateConnections.stream().map(TemplateConnection::toString).collect(Collectors.joining("\n")));

        new TemplatesToDot(templateConnections, style, baseTypes, ioMap, templateDispatcher, successors, pf, this, principal).convert(null, out, "template_connections");
    }

    final DigestUtils sha512 = new DigestUtils(DigestUtils.getSha3_512Digest());

    public Map<String, String> computeHash(String template, int id, Object[] record) {

        StringBuilder sb=new StringBuilder();
        sb.append(id).append(",");
        sb.append(templateDispatcher.getCsvConverter().get((String)record[0]).apply(record));

        String csv = sb.toString();
        String hash2=sha512.digestAsHex(csv);
        Map<String,String> map=new LinkedHashMap<>();
        map.put(SHA_3_512, hash2);
        map.put("csv", csv);
        return map;
    }

    public Map<String, String> computeHash(String template, int id, List<Object[]> records) {
        StringBuilder sb=new StringBuilder();
        sb.append(id).append(",").append(template).append(",").append(records.size());
        for (Object[] record: records) {
            sb.append("\n");
            sb.append(templateDispatcher.getCsvConverter().get((String)record[0]).apply(record));
        }

        String csv = sb.toString();
        String hash2=sha512.digestAsHex(csv);
        Map<String,String> map=new LinkedHashMap<>();
        map.put(SHA_3_512, hash2);
        map.put("csv", csv);
        return map;
    }


    public void updateHash(String template, int id, Map<String,String> hash, String principal) {
        querier.do_statements(null,
                null,
                (sb, data) -> {
                    sb.append("UPDATE record_index");
                    sb.append(" SET hash='");
                    sb.append(makeHashRecord(hash));
                    sb.append("' WHERE key=");
                    sb.append(id);
                    sb.append(" AND principal='");
                    sb.append(principal);
                    sb.append("'");
                    sb.append(" AND table_name='");
                    sb.append(template);
                    sb.append("'");
                });
    }

    public Map<String, String> retrieveHash(String template, int id, String principal) {

        Map<String,String> map=new HashMap<>();

        return querier.do_query(map,
                null,
                (sb, data) -> {
                    sb.append("SELECT hash FROM record_index WHERE key=");
                    sb.append(id);
                    sb.append(" AND principal='");
                    sb.append(principal);
                    sb.append("'");
                    sb.append(" AND table_name='");
                    sb.append(template);
                    sb.append("'");
                },
                (rs, data) -> {
                    while (rs.next()) {
                        String hash1 = rs.getString("hash");
                        try {
                            Map<String, String> hash2 = om.readValue(hash1, Map.class);
                            data.putAll(hash2);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }



     String makeHashRecord(Map<String,String> persistedMap) {
        Map<String, String> map = new HashMap<>();
        map.putAll(persistedMap);
        map.remove("csv");
        try {
            return om.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> makeSignatureMap(List<String> hash) {
        Map<String,String> map=new LinkedHashMap<>();
        map.put(SHA_3_512, hash.get(0));
        return map;
    }


    static public class TemplateConnection {
        public Integer in_id;
        public String in_template;
        public String in_property;
        public Integer out_id;
        public String out_template;
        public String out_property;
    }

    public List<TemplateConnection> recursiveTraversal(Integer id, String template, String property, String principal) {
        List<TemplateConnection> the_records = new LinkedList<>();
        querier.do_query(the_records,
                null,
                (sb, data) -> {
                    sb.append("SELECT * FROM ");
                    sb.append("backwardtraversal_star(");
                    sb.append(id);
                    sb.append(",'");
                    sb.append(template);
                    sb.append("','");
                    sb.append(property);
                    sb.append("') as template_connection\n");
                    joinAccessControl("template_connection.in_template", principal, sb, "template_connection", "in_id");
                    andAccessControl(principal, sb);
                },
                (rs, data) -> {
                    while (rs.next()) {
                        TemplateConnection record = new TemplateConnection();
                        record.in_id=rs.getObject("in_id", Integer.class);
                        record.in_template=rs.getObject("in_template", String.class);
                        record.in_property=rs.getObject("in_property", String.class);
                        record.out_id=rs.getObject("out_id", Integer.class);
                        record.out_template=rs.getObject("out_template", String.class);
                        record.out_property=rs.getObject("out_property", String.class);
                        data.add(record);
                    }
                });

        return the_records;
    }

    Document constructDocument(Collection<Object[]> the_records) {
        return constructDocument(documentBuilderDispatcher, the_records);
    }


    public Document constructDocument(Map<String, FileBuilder> documentBuilderDispatcher, Collection<Object[]> the_records) {
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

    public List<Object[]> query(String template, Integer id, boolean withTitles, String principal) {
        if (isComposite(template)) {
            return queryComposite(template, id, withTitles, principal);
        } else {
            return querySimple(template, id, withTitles, principal);
        }
    }

    private boolean isComposite(String template) {
        return compositeLinker.containsKey(template);
    }

    public List<Object[]> querySimple(String template, Integer id, boolean withTitles, String principal) {
        List<Object[]> the_records = new LinkedList<>();
        String[] propertyOrder=templateDispatcher.getPropertyOrder().get(template);
        //System.out.println("propertyOrder = " + Arrays.toString(propertyOrder));
        querier.do_query(the_records,
                null,
                (sb, data) -> {
            /* generate following query
                    SELECT template.*
                    FROM plead_transforming AS template
                    LEFT JOIN record_index
                    ON record_index.key = template.id
                    AND record_index.table_name = 'plead_transforming'
                    AND record_index.principal IS NOT NULL
                    LEFT JOIN access_control
                    ON access_control.record = record_index.id
                    AND access_control.authorized = 'joe'
                    WHERE template.id = 478
                    AND (record_index.principal = 'joe' OR access_control.record IS NOT NULL);

             */

                    sb.append("SELECT template.*\n FROM ");
                    sb.append(template);
                    sb.append(" as template ");
                    joinAccessControl(template, principal, sb);
                    sb.append("\n WHERE template.id=");
                    sb.append(id);
                    andAccessControl(principal, sb);

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
                                o = ((Timestamp)o).toInstant().toString();
                            }
                            record[i] = o;
                        }
                        data.add(record);
                    }
                });

        return the_records;
    }

    public void whereAccessControl(String principal, StringBuilder sb) {
        sb.append("\n WHERE (record_index.principal='");
        sb.append(principal);
        sb.append("' OR access_control.record IS NOT NULL)");
    }
    public void andAccessControl(String principal, StringBuilder sb) {
        sb.append("\n AND (record_index.principal='");
        sb.append(principal);
        sb.append("' OR access_control.record IS NOT NULL)");
    }

    public void joinAccessControl(String template, String principal, StringBuilder sb) {
        joinAccessControl(template, principal, sb, "template", "id");
    }

    public void joinAccessControl(String template, String principal, StringBuilder sb, String label, String id) {
        sb.append("\n LEFT JOIN record_index ON record_index.key=" + label + "." + id);
        if (template.startsWith(label)) {
            sb.append("\n AND record_index.table_name=");
            sb.append(template);
        } else {
            sb.append("\n AND record_index.table_name='");
            sb.append(template);
            sb.append("'");
        }
        sb.append("\n AND record_index.principal IS NOT NULL");
        sb.append("\n LEFT JOIN access_control\n ON access_control.record=record_index.id");
        sb.append("\n AND access_control.authorized='");
        sb.append(principal);
        sb.append("'");
    }

    public List<RecordEntry2> queryTemplatesRecords(SearchConfig config, String principal, boolean includeComposite) {
        String base_relation = config.base_relation;
        String from_date = config.from_date;
        String to_date = config.to_date;
        Integer limit = config.limit;

        if (from_date != null) {
            from_date = "'" + from_date + "'";
        }
        if (to_date != null) {
            to_date = "'" + to_date + "'";
        }


        return queryTemplatesRecords(base_relation, from_date, to_date, limit, principal, includeComposite);
    }

    private List<RecordEntry2> queryTemplatesRecords(String base_relation, String from_date, String to_date, Integer limit, String principal, boolean includeComposite) {
        List<RecordEntry2> linked_records = new LinkedList<>();
        Set<Integer> seen= new HashSet<>();

        querier.do_query(linked_records,
                null,
                (sb, data) -> {
                    StringBuffer aggregator= new StringBuffer();
                    aggregator.append("json_build_array(");
                    final int[] count0 = {0};
                    compositeLinker.forEach((k,v) -> {
                        if (includeComposite) {
                            count0[0]++;
                            if (count0[0]>1) {
                                aggregator.append(",");
                            }
                            aggregator.append("json_agg(");
                            aggregator.append("link").append(count0[0]).append(".composite)");
                            aggregator.append(",");
                            aggregator.append("'").append(k).append("'");
                        }});
                     aggregator.append(")");

                    sb.append("SELECT search_record.*,record_index.principal,json_agg(ac2.authorized) as authorized,").append(aggregator.toString()).append(" as parent, record_index.hash as hash\n FROM ");
                    sb.append("search_records_for_" + base_relation + "(").append(from_date).append(",").append(to_date).append(") as search_record ");
                    joinAccessControl("search_record.table_name", principal, sb, "search_record", "key");
                    sb.append("\n LEFT JOIN access_control as ac2  ON ac2.record=record_index.id");
                    final int[] count = {0};
                    compositeLinker.forEach((k,v) -> {
                        if (includeComposite) {
                            count[0]++;
                            sb.append("\n LEFT JOIN ");
                            sb.append(v.table);
                            sb.append(" as link").append(count[0]);
                            sb.append("\n ON search_record.key=link").append(count[0]).append(".simple");
                        }
                    });

                    //sb.append("\n LEFT JOIN plead_transforming_composite_linker as link\n");
                   // sb.append("\n ON record_index.key=simple");
                    whereAccessControl(principal, sb);
                    sb.append("\n group by search_record.id, search_record.created_at, search_record.table_name, search_record.key, record_index.principal, record_index.hash\n");
                    sb.append("\n limit ").append(limit);
                    System.out.println("sb = " + sb.toString());
                },
                (rs, data) -> {
                    while (rs.next()) {
                        PGobject parent1=rs.getObject("parent", PGobject.class);
                        Integer parent=null;
                        String parent_relation=null;
                        try {
                            List<Object> parents =(parent1==null)?null:om.readValue(parent1.getValue(), List.class);
                            if (parents!=null && !parents.isEmpty()) {
                                parent=((List<Integer>)(parents.get(0))).get(0);
                                parent_relation=(String)(parents.get(1));
                            }
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        if (parent!=null && !seen.contains(parent)) {
                            seen.add(parent);
                            RecordEntry2 parentRecord = new RecordEntry2();
                            parentRecord.key = parent;
                            parentRecord.base_relation = base_relation;
                            parentRecord.id = rs.getObject("ID", Integer.class);
                            parentRecord.created_at = rs.getObject("created_at", Timestamp.class).toInstant().toString();
                            parentRecord.table_name= parent_relation;//rs-.getObject("table_name", String.class)+"_composite"; // HACK
                            data.add(parentRecord);
                        }

                        RecordEntry2 record = new RecordEntry2();
                        record.key = rs.getObject("key", Integer.class);
                        record.created_at = rs.getObject("created_at", Timestamp.class).toInstant().toString();
                        record.base_relation = base_relation;
                        record.table_name = rs.getObject("table_name", String.class);
                        record.id = rs.getObject("ID", Integer.class);
                        record.principal = rs.getObject("principal", String.class);
                        String authorized1 = rs.getString("authorized");
                        String hash1= rs.getString("hash");
                        try {
                            List<String> authorized=(authorized1==null)?null:om.readValue(authorized1.getBytes(), List.class);
                            Map<String,String> hash=(hash1==null)?null:om.readValue(hash1, Map.class);
                            if (authorized!=null && !authorized.isEmpty() && authorized.get(0)==null) {
                                authorized=List.of();
                            }
                            record.authorized=authorized;
                            record.hash=hash;
                        } catch (IOException e) {
                            throw new RuntimeException("failed to parse authorized field: " + authorized1, e);
                        }

                        data.add(record);
                    }
                });

        return linked_records;
    }

    public List<RecordEntry2> queryTemplatesRecordsById(String base_relation, Integer id, Integer limit, String principal) {
        List<RecordEntry2> linked_records = new LinkedList<>();

        querier.do_query(linked_records,
                null,
                (sb, data) -> {
                    sb.append("SELECT * FROM ");
                    sb.append("search_records_by_id_for_" + base_relation + "(" + id + ") as search_record");
                    joinAccessControl("search_record.table_name", principal, sb, "search_record", "key");
                    whereAccessControl(principal, sb);
                    sb.append("\n limit ").append(limit);
                    //System.out.println("sb = " + sb.toString());
                },
                (rs, data) -> {
                    while (rs.next()) {
                        RecordEntry2 record = new RecordEntry2();
                        record.key = rs.getObject("key", Integer.class);
                        record.created_at = rs.getObject("created_at", Timestamp.class).toInstant().toString();
                        record.base_relation = base_relation;
                        record.table_name = rs.getObject(TABLE_NAME_COLUMN, String.class);
                        record.property = rs.getObject(PROPERTY_COLUMN, String.class);
                        record.id = rs.getObject("ID", Integer.class);

                        data.add(record);
                    }
                });

        return linked_records;
    }

    static class RecordEntry {
        public String table;
        public Integer key;
        @Override
        public String toString() {
            return "RecordEntry{" +
                    "table='" + table + '\'' +
                    ", key=" + key +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RecordEntry that = (RecordEntry) o;
            return Objects.equals(table, that.table) && Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(table, key);
        }
    }
    public static class RecordEntry2 {
        public Integer key;
        public String property;
        public String base_relation;
        public String created_at;
        public String table_name;
        public Integer id;
        public String principal;
        public List<String> authorized;
        public Map<String, String> hash;


        @Override
        public String toString() {
            return "RecordEntry2{" +
                    "key=" + key +
                    ", property='" + property + '\'' +
                    ", base_relation='" + base_relation + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", table_name='" + table_name + '\'' +
                    ", id=" + id +
                    ", principal='" + principal + '\'' +
                    ", authorized=" + authorized +
                    ", hash=" + hash +
                    '}';
        }


    }
    public List<Object[]> queryComposite(String template, Integer id, boolean withTitles, String principal) {
        List<RecordEntry> linked_records = new LinkedList<>();
        TemplateService.Linker linker = compositeLinker.get(template);

        querier.do_query(linked_records,
                null,
                (sb, data) -> {
                    sb.append("SELECT * FROM (");
                    sb.append("SELECT * FROM ");
                    sb.append(template); // was linker.table
                    sb.append(" AS template ");
                    joinAccessControl(template, principal, sb);
                    sb.append("\n WHERE key=");
                    sb.append(id);
                    andAccessControl(principal, sb);
                    sb.append(") as combo ");
                    sb.append("\n LEFT JOIN ").append(linker.table).append(" AS linker ");
                    sb.append(" ON combo.key = linker.composite");
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
            List<Object[]> simple_records = querySimple(linked_record.table, simple, withTitles, principal);
            the_records.addAll(simple_records);
        }

        return the_records;
    }

    public List<Object[]> queryTemplates(TableKeyList tableKeyList, boolean withTitles, String principal) {
        List<Object[]> result = new LinkedList<>();
        for (TableKey tableKey : tableKeyList.key) {
            logger.info("tableKey = " + tableKey);
            List<Object[]> tmp=query(tableKey.isA, tableKey.ID, withTitles, principal);
            result.addAll(tmp);
        }
        return result;
    }


    public Map<String, Map<String, Map<String, String>>> getIoMap(String ioMapString) {
        List<String> toExclude = List.of("plead_transforming_composite");
        try {
            Map<String, Map<String, Map<String, String>>> ioMap = om.readValue(ioMapString, typeRef);
            ioMap.get(INPUT). entrySet().removeIf( entry -> toExclude.contains(entry.getKey()) || entry.getValue() == null || entry.getValue().isEmpty());
            ioMap.get(OUTPUT).entrySet().removeIf( entry -> toExclude.contains(entry.getKey()) || entry.getValue() == null || entry.getValue().isEmpty());
            return ioMap;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateBackwardTemplateTraversal(Map<String,Map<String, Map<String, String>>> ioMap) {
        String backwardTraversalFunctionName="backwardTraversal";

        Map<String,?> funParams=new LinkedHashMap<>() {{
            put(PARAM_ID,       unquote("INT"));
            put(PARAM_TEMPLATE, unquote("text"));
            put(PARAM_PROPERTY, unquote("text"));

        }};
        Map<String,Object> functionReturns= new LinkedHashMap<>() {{

            put(IN_ID,           unquote("INT"));
            put(IN_TEMPLATE,     unquote("text"));
            put(IN_PROPERTY,     unquote("text"));
            put(OUT_ID,          unquote("INT"));
            put(OUT_TEMPLATE,    unquote("text"));
            put(OUT_PROPERTY,    unquote("text"));
        }};


        QueryBuilder fun=
                new QueryBuilder()
                        .comment("Generated by method " + getClass().getName()+ ".generateSQLSearchRecordFunction")
                        .next(createFunction(backwardTraversalFunctionName))
                        .params(funParams)
                        .returns("table", functionReturns)
                        .bodyStart("");
        Set<String> allTables=ioMap.get(OUTPUT).values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toSet());
        allTables.addAll(ioMap.get(INPUT).values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toSet()));


        Map<String, Map<String, String>> input = ioMap.get(INPUT);
        Map<String, Map<String, String>> output = ioMap.get(OUTPUT);

        boolean before=false;

        for (String table: allTables) {

            //System.out.println("table = " + table);



            Map<String, Map<String, String>> input_table=
                    filterMapAccordingToTable(table, input);
            Map<String, Map<String, String>> output_table=
                    filterMapAccordingToTable(table, output);


            for (String in_template: input_table.keySet()) {
                if (!input_table.get(in_template).keySet().isEmpty()) {
                    fun.comment(" + in_template = " + in_template);
                    for (String in_property : input_table.get(in_template).keySet()) {
                        for (String out_template : output_table.keySet()) {
                            if (!output_table.get(out_template).keySet().isEmpty()) {
                                for (String out_property : output_table.get(out_template).keySet()) {
                                    String in_templatex=in_template;
                                    String out_templatex=out_template;

                                    if(in_template.equals(out_template)) {
                                        in_templatex = "_" + in_template + "_in";
                                        out_templatex = "_" + out_template + "_out";
                                    }

                                    String [] args={
                                            String.format("%s as %s",    PARAM_ID, IN_ID),
                                            String.format("'%s' as %s",  in_template,   IN_TEMPLATE),
                                            String.format("'%s' as %s",  in_property,   IN_PROPERTY),
                                            String.format("%s.id as %s", out_templatex, OUT_ID),
                                            String.format("'%s' as %s",  out_template,  OUT_TEMPLATE),
                                            String.format("'%s' as %s",  out_property,  OUT_PROPERTY)
                                    };
                                    if (before) {
                                        fun.newline().union(pp -> select((Object[]) args).apply(pp));
                                    } else {
                                        fun.selectExp(args);
                                        before = true;
                                    }
                                    fun.from(in_template);
                                    if (!in_template.equals(in_templatex)) {
                                        fun.alias(in_templatex);
                                    }
                                    fun.join(out_template);
                                    if (!out_template.equals(out_templatex)) {
                                        fun.alias(out_templatex);
                                    }
                                    fun.on (out_templatex + "." +  out_property + " = " + in_templatex  + "." +  in_property )
                                            .and(           unquote(PARAM_PROPERTY) + " = '" + in_property + "'")
                                            .and(           unquote(PARAM_TEMPLATE) + " = '" + in_templatex + "'")
                                            .and( in_templatex + ".id=" + PARAM_ID);
                                }
                            }
                        }
                    }
                }
            }

        }

        return fun.bodyEnd("").getSQL();

    }

    private Map<String, Map<String, String>> filterMapAccordingToTable(String table, Map<String, Map<String, String>> input) {
        return input.keySet().stream().collect(Collectors.toMap(k -> k, k -> input.get(k).keySet().stream().filter(k2 -> input.get(k).get(k2).equals(table)).collect(Collectors.toMap(k2 -> k2, k2 -> input.get(k).get(k2)))));
    }

}
