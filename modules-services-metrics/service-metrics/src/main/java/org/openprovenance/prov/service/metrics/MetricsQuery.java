package org.openprovenance.prov.service.metrics;

import org.openprovenance.prov.rules.SimpleMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MetricsQuery {
    private final Querier querier;

    public MetricsQuery(Querier querier) {
        this.querier = querier;
    }

    public void updateTypeMap(String map, String set, String list) {
        querier.do_statements(null,
                null,
                (sb, data) -> {
                    sb.append("UPDATE typemap");
                    sb.append(" SET map='");
                    sb.append(map);
                    sb.append("', set='");
                    sb.append(set);
                    sb.append("', list='");
                    sb.append(list);
                    sb.append("'");
                });
    }

    public String insertMetrics(String artifact, String url, String features, String counts, String validity, String traffic) {
        AtomicReference<String> id = new AtomicReference<>();
        querier.do_query(null,
                null,
                (sb, data) -> {
                    sb.append("insert\n");
                    sb.append("into metrics (artifact, url, features, counts, validity, traffic)\n");
                    sb.append("values ('");
                    sb.append(artifact);
                    sb.append("', ");
                    sb.append((url==null)?"null":"'"+url+"'");
                    sb.append(", '");
                    sb.append(features);
                    sb.append("', '");
                    sb.append(counts);
                    sb.append("', '");
                    sb.append(validity);
                    sb.append("', '");
                    sb.append(traffic);
                    sb.append("')\n");
                    sb.append("RETURNING ID;");
                },
                (rs, data) -> {
                    while (rs.next()) {
                        id.set(rs.getString("ID"));
                    }
                });
        return id.get();
    }

    public SimpleMetrics getMetrics(String id) {
        SimpleMetrics metrics = new SimpleMetrics();
        return querier.do_query(metrics,
                null,
                (sb, data) -> {
                    sb.append("SELECT *\n");
                    sb.append("FROM metrics\n");
                    sb.append("where ID=");
                    sb.append(id);
                    sb.append(";");
                },
                (rs, data) -> {
                    while (rs.next()) {
                        data.countEntities=Integer.valueOf(rs.getInt("countEntities"));
                        data.countAgents=rs.getInt("countAgents");
                        data.countActivities=rs.getInt("countActivities");
                        data.countUsed=rs.getInt("countUsed");
                        data.countWasGeneratedBy=rs.getInt("countWasGeneratedBy");
                        data.countWasDerivedFrom=rs.getInt("countWasDerivedFrom");
                        data.countWasAssociatedWith=rs.getInt("countWasAssociatedWith");
                        data.countWasAttributedTo=rs.getInt("countWasAttributedTo");
                        data.countActedOnBehalfOf=rs.getInt("countActedOnBehalfOf");
                        data.countWasEndedBy=rs.getInt("countWasEndedBy");
                        data.countWasInformedBy=rs.getInt("countWasInformedBy");
                        data.countWasInvalidatedBy=rs.getInt("countWasInvalidatedBy");
                        data.countWasStartedBy=rs.getInt("countWasStartedBy");
                        data.countWasInfluencedBy=rs.getInt("countWasInfluencedBy");
                        data.countSpecializationOf=rs.getInt("countSpecializationOf");
                        data.countAlternateOf=rs.getInt("countAlternateOf");
                        data.countHadMember=rs.getInt("countHadMember");
                    }
                });
    }

    public TmpTypeMap getTypeMap() {
        TmpTypeMap tmp = new TmpTypeMap();
        return querier.do_query(tmp,
                null,
                (sb, data) -> {
                    sb.append("SELECT *\n");
                    sb.append("FROM typemap\n");
                    sb.append("where ID=1;");
                },
                (rs, data) -> {
                    while (rs.next()) {
                        data.map=rs.getString("map");
                        data.set=rs.getString("set");
                        data.list=rs.getString("list");
                    }
                });
    }


    public List<SimpleMetrics> getTypeMapReport() {
        List<SimpleMetrics> report=new ArrayList<>();
        return querier.do_query(report,
                null,
                (sb, data) -> {
                    sb.append("""
                    SELECT metrics->'countEntities' as entity, metrics -> 'countAgents' as agent, metrics -> 'countActivities' as activity, 
                           metrics -> 'countUsed' as used, metrics -> 'countWasGeneratedBy' as wasGeneratedBy, metrics -> 'countWasDerivedFrom' as wasDerivedFrom, 
                           metrics -> 'countWasAssociatedWith' as wasAssociatedWith, metrics -> 'countWasAttributedTo' as wasAttributedTo, metrics -> 'countActedOnBehalfOf' as actedOnBehalfOf,
                            metrics -> 'countWasEndedBy' as wasEndedBy, metrics -> 'countWasInformedBy' as wasInformedBy, metrics -> 'countWasInvalidatedBy' as wasInvalidatedBy,
                            metrics -> 'countWasStartedBy' as wasStartedBy, metrics -> 'countWasInfluencedBy' as wasInfluencedBy, 
                            metrics -> 'countSpecializationOf' as specializationOf, metrics -> 'countAlternateOf' as alternateOf, metrics -> 'countHadMember' as hadMember
                    FROM (select counts->'simpleMetrics' as metrics
                          FROM metrics)
                          as m
                """);
                },
                (rs, data) -> {
                    while (rs.next()) {
                        SimpleMetrics line=new SimpleMetrics();
                        line.countEntities=Integer.valueOf(rs.getInt("entity"));
                        line.countAgents=rs.getInt("agent");
                        line.countActivities=rs.getInt("activity");
                        line.countUsed=rs.getInt("used");
                        line.countWasGeneratedBy=rs.getInt("wasGeneratedBy");
                        line.countWasDerivedFrom=rs.getInt("wasDerivedFrom");
                        line.countWasAssociatedWith=rs.getInt("wasAssociatedWith");
                        line.countWasAttributedTo=rs.getInt("wasAttributedTo");
                        line.countActedOnBehalfOf=rs.getInt("actedOnBehalfOf");
                        line.countWasEndedBy=rs.getInt("wasEndedBy");
                        line.countWasInformedBy=rs.getInt("wasInformedBy");
                        line.countWasInvalidatedBy=rs.getInt("wasInvalidatedBy");
                        line.countWasStartedBy=rs.getInt("wasStartedBy");
                        line.countWasInfluencedBy=rs.getInt("wasInfluencedBy");
                        line.countSpecializationOf=rs.getInt("specializationOf");
                        line.countAlternateOf=rs.getInt("alternateOf");
                        line.countHadMember=rs.getInt("hadMember");

                        data.add(line);
                    }
                });
    }
}
