package org.openprovenance.prov.service.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.rules.SimpleMetrics;
import org.openprovenance.prov.scala.typemap.IncrementalProcessor;
import scala.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsCalculator extends Rules {
    final private static Logger logger = LogManager.getLogger(MetricsCalculator.class);

    final IncrementalProcessor ip;
    private final MetricsQuery querier;

    public MetricsCalculator(MetricsQuery querier) {
        this.querier=querier;
        if (querier==null) {
            ip=new IncrementalProcessor();
        } else {
            TmpTypeMap tmp = querier.getTypeMap();
            //logger.info("Loaded type map: " + tmp.map);
            ip=new IncrementalProcessor(tmp.map,tmp.set,tmp.list);
            //ip.printme();
        }
    }

    ObjectMapper om=new ObjectMapper();



    @Override
    public Object getMetrics(Document document, ProvFactory pFactory) {


        Tuple2<Map<String, Integer>, Map<List<Tuple2<Integer, Integer>>, Integer>> features=ip.process(document);

        System.out.println("Features: " + features._1);
        System.out.println("Features: " + features._2);



        Object simpleMetrics = super.getMetrics(document, pFactory);
        persistTypeMap();
        try {
            querier.insertMetrics("document", null,  om.writeValueAsString(features._1), om.writeValueAsString(simpleMetrics));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new HashMap<String, Object>() {{
            put("artifact", "document");
            put("counts", simpleMetrics);
            put("features", features._2);
        }};
    }
    
    public org.openprovenance.prov.scala.summary.types.ProvType getType(int depth, int index) {
        return ip.getProvType(depth, index);
    }

    public void persistTypeMap() {
        ip.process((map, set, list) -> {
            querier.updateTypeMap(map, set, list);
            return null;
        });
    }

    public String getTypeMap() {
        return ip.processMapOfStrings( x -> x);
    }

    public List<SimpleMetrics> getTypeMapReport() {
        return querier.getTypeMapReport();
    }



}
