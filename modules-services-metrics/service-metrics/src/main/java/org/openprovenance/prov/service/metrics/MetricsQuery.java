package org.openprovenance.prov.service.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamConstraintsException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.rules.SimpleMetrics;
import org.openprovenance.prov.rules.TrafficLightResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.openprovenance.prov.validation.report.json.Serialization.deserializeValidationReport;

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

    public String insertTypeMap(String map, String set, String list) {
        AtomicReference<String> id = new AtomicReference<>();
        querier.do_query(null,
                null,
                (sb, data) -> {
                    sb.append("INSERT INTO typemap (map, set, list) \n");

                    sb.append(" values ('");
                    sb.append(map);
                    sb.append("', '");
                    sb.append(set);
                    sb.append("', '");
                    sb.append(list);
                    sb.append("')\n");
                    sb.append(" RETURNING ID;");


                },
                (rs, data) -> {
                    while (rs.next()) {
                        id.set(rs.getString("ID"));
                    }
                });
        return id.get();
    }

    final static TypeReference<Map<String,Object>> countMetrics = new TypeReference<>() {};
    final static TypeReference<Map<String,Integer>> featureMetrics = new TypeReference<>() {};

    public String insertMetricsRecord(String artifact, String url, String features, String counts, String validity, String traffic, String hash) {
        AtomicReference<String> id = new AtomicReference<>();
        querier.do_query(null,
                null,
                (sb, data) -> {
                    sb.append("insert\n");
                    sb.append("into metrics (artifact, url, features, counts, validity, traffic, hash)\n");
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
                    sb.append("', '");
                    sb.append(hash);
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

    ObjectMapper om=new ObjectMapper();


    public MetricsRecord getMetricsRecord(String id) {
        MetricsRecord metricsRecord = new MetricsRecord();
        return querier.do_query(metricsRecord,
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
                        updateMetricsBean(rs, data);
                    }
                });
    }

    private void updateMetricsBean(ResultSet rs, MetricsRecord data) throws SQLException {
        data.id= rs.getString("ID");
        data.artifact = rs.getString("artifact");
        try {
            data.metrics=om.readValue(rs.getString("counts"), countMetrics);
        } catch (JsonProcessingException e) {
            HashMap<String,String> metricsError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            metricsError.put("error", outputStream.toString());
            data.metrics=metricsError;
        }
        String featuresString= rs.getString("features");
        try {
            data.features= om.readValue(featuresString, featureMetrics);
        } catch (StreamConstraintsException e) {
            Map<String,String> featureError = new HashMap<>();
            featureError.put("error",featuresString);
            data.features=featureError;
        } catch (JsonProcessingException e) {
            Map<String,List<String>> featureError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            featureError.put("error", Arrays.stream(outputStream.toString().split("\n")).collect(Collectors.toList()));
            data.features=featureError;
        }
        try {
            data.validationReport=deserializeValidationReport(rs.getString("validity"));
        } catch (IOException e) {
            HashMap<String,String> validationError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            validationError.put("error", outputStream.toString());
            data.validationReport=validationError;
        }
        try {
            data.traffic=om.readValue(rs.getString("traffic"), Object.class);
        } catch (JsonProcessingException e) {
            HashMap<String,String> trafficError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            trafficError.put("error", outputStream.toString());
            data.traffic=trafficError;
        }
        try {
            data.hash=om.readValue(rs.getString("hash"), Object.class);
        } catch (JsonProcessingException e) {
            HashMap<String,String> hashError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            hashError.put("error", outputStream.toString());
            data.hash=hashError;
        }
        data.created_at= rs.getString("created_at");
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

    public TmpTypeMap getTypeMap1() {
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

    public TmpTypeMap getTypeMap() {
        TmpTypeMap tmp = new TmpTypeMap();
        return querier.do_query(tmp,
                null,
                (sb, data) -> {
                    sb.append("SELECT *\n");
                    sb.append("FROM typemap\n");
                    sb.append("ORDER by created_at DESC LIMIT 1;");
                },
                (rs, data) -> {
                    while (rs.next()) {
                        data.ID=rs.getString("ID");
                        data.map=rs.getString("map");
                        data.set=rs.getString("set");
                        data.list=rs.getString("list");
                    }
                });
    }

    public List<SimpleMetrics> getAllMetricsReport() {
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

    public List<TrafficLightResult> getAllTrafficLightReport() {
        List<TrafficLightResult> report=new ArrayList<>();
        return querier.do_query(report,
                null,
                (sb, data) -> {
                    sb.append("""
                    select traffic
                    FROM metrics
                """);
                },
                (rs, data) -> {
                    while (rs.next()) {
                        try {
                            //System.out.println(rs.getString("ID"));
                            TrafficLightResult traffic=om.readValue(rs.getString("traffic"), TrafficLightResult.class);
                            data.add(traffic);
                        } catch (Throwable e) {
                            String explanation = "No traffic information was obtained.";
                            TrafficLightResult traffic = new TrafficLightResult("Traffic Rating", explanation, 0.0);
                            //data.add(traffic);
                        }
                    }
                });
    }

}
