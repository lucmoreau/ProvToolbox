package org.openprovenance.prov.service.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.rules.SimpleMetrics;
import org.openprovenance.prov.rules.TrafficLight;
import org.openprovenance.prov.rules.TrafficLightResult;
import org.openprovenance.prov.rules.counters.EntityActivityDerivationCounter;
import org.openprovenance.prov.scala.summary.TypePropagator;
import org.openprovenance.prov.scala.typemap.IncrementalProcessor;
import org.openprovenance.prov.service.signature.SignatureService;
import org.openprovenance.prov.service.validation.ValidationObjectMaker;
import org.openprovenance.prov.validation.Config;
import org.openprovenance.prov.validation.Validate;
import org.openprovenance.prov.validation.report.ValidationReport;
import scala.Tuple2;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.service.metrics.MetricsPostService.getSignature;

public class MetricsCalculator extends Rules {
    final private static Logger logger = LogManager.getLogger(MetricsCalculator.class);

    final IncrementalProcessor ip;
    private final MetricsQuery querier;
    private final InteropFramework interop;
    private final SignatureService signatureService;

    public MetricsCalculator(MetricsQuery querier, SignatureService signatureService) {
        this.querier=querier;
        if (querier==null) {
            ip=new IncrementalProcessor();
        } else {
            TmpTypeMap tmp = querier.getTypeMap();
            //logger.info("Loaded type map: " + tmp.map);
            ip=new IncrementalProcessor(tmp.map,tmp.set,tmp.list);
            //ip.printme();
        }
        this.interop=new InteropFramework();
        this.signatureService=signatureService;
    }

    ObjectMapper om=new ObjectMapper();



    @Override
    public Object computeMetrics(Document document, ProvFactory pFactory) {

        // extract all metrics, intercept all exceptions and return them as part of the result

        Object features = getFeatures(document);
        Object metrics = getMetricsOrError(document, pFactory);
        Object validationReport = getValidationReportOrError(document, pFactory);
        Object traffic = getTrafficLightOrError(metrics);
        Object hash= getSignature(signatureService,document);

        String id=null;

        try{
            id=querier.insertMetricsRecord("document",
                    null,
                    TypePropagator.om().writeValueAsString(features),
                    om.writeValueAsString(metrics),
                    om.writeValueAsString(validationReport),
                    om.writeValueAsString(traffic),
                    om.writeValueAsString(hash));
            System.out.println("=========== ID: " + id);

            persistTypeMap();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        MetricsRecord metricsRecord = new MetricsRecord(id, "document", metrics, features, validationReport, traffic, hash);

        return metricsRecord;

        /*
        String finalId = id;
        return new HashMap<String, Object>() {{
            put("artifact", "document");
            put("id", finalId);
            if (metrics!=null) put("counts", metrics);
            if (features!=null && features._2!=null) put("features", features._2);
            if (validationReport!=null) put("validity", validationReport);
            if (traffic!=null) put("traffic", traffic);
        }};

         */
    }

    private Object getFeatures(Document document) {
        try {
            logger.info("getFeatures");
            Tuple2<Map<String, Integer>, Map<List<Tuple2<Integer, Integer>>, Integer>> features = ip.process(document);
            logger.info("Features: " + features._1);
            logger.info("Features: " + features._2);
            return features._2;
        } catch (Throwable e) {
            HashMap<String,String> featuresError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            featuresError.put("error", outputStream.toString());
            return featuresError;
        }
    }

    private Object getTrafficLightOrError(Object metrics) {
        try {
            Map<String, Object> m1=(Map<String, Object>) metrics;
            Map<String, Object> m2=(Map<String, Object>)m1.get(PATTERN_METRICS);

            if (m2==null) {
                return new HashMap<String,String>() {{
                    put("error", "No pattern metrics found");
                }};
            }

            EntityActivityDerivationCounter count=(EntityActivityDerivationCounter)m2.get(COUNT_DERIVATIONS_AND_GENERATIONS_AND_USAGES);
            List<TrafficLightResult> trafficLight= TrafficLight.getTrafficLight(count);
            TrafficLightResult patternTrafficLightResult=new TrafficLightResult("Triangle Pattern Rating", trafficLight);

            List<TrafficLightResult> typeTrafficLightSubResults=List.of(
                    new TrafficLightResult("Type1", 66, TrafficLight.TrafficLightColor.ORANGE),
                    new TrafficLightResult("Type2", 70, TrafficLight.TrafficLightColor.GREEN));
            TrafficLightResult typeTrafficLightResult=new TrafficLightResult("Type Rating",typeTrafficLightSubResults);


            List<TrafficLightResult> trafficLightSubResults = List.of(patternTrafficLightResult, typeTrafficLightResult);
            TrafficLightResult overallTrafficLightResult=new TrafficLightResult("Overall Rating",trafficLightSubResults);

            return overallTrafficLightResult;
        } catch (Throwable e) {
            HashMap<String,String> trafficError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            trafficError.put("error", outputStream.toString());
            return trafficError;
        }
    }

    private Object getMetricsOrError(Document document, ProvFactory pFactory) {
        try {
            return super.computeMetrics(document, pFactory);
        } catch (Throwable e) {
            HashMap<String,String> metricsError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            metricsError.put("error", outputStream.toString());
            return metricsError;
        }
    }

    private Object getValidationReportOrError(Document document, ProvFactory pFactory) {
        try {
            ValidationReport validationReport;
            Validate validator = new Validate(Config.newYesToAllConfig(pFactory, new ValidationObjectMaker()));
            validationReport = validator.validate(document);
            validationReport.nonStrictCycle = null;
            return validationReport;
        } catch (Throwable e) {
            HashMap<String,String> validationError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            validationError.put("error", outputStream.toString());
            return validationError;
        }
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
