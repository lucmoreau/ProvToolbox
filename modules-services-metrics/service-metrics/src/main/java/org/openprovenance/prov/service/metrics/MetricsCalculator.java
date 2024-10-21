package org.openprovenance.prov.service.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.rules.SimpleMetrics;
import org.openprovenance.prov.rules.TrafficLight;
import org.openprovenance.prov.rules.TrafficLightResult;
import org.openprovenance.prov.rules.counters.EntityActivityDerivationCounter;
import org.openprovenance.prov.scala.summary.TypePropagator;
import org.openprovenance.prov.scala.summary.types.ProvType;
import org.openprovenance.prov.scala.typemap.IncrementalProcessor;
import org.openprovenance.prov.service.signature.SignatureService;
import org.openprovenance.prov.service.validation.ValidationObjectMaker;
import org.openprovenance.prov.validation.Config;
import org.openprovenance.prov.validation.Validate;
import org.openprovenance.prov.validation.report.Dependencies;
import org.openprovenance.prov.validation.report.MergeReport;
import org.openprovenance.prov.validation.report.TypeOverlap;
import org.openprovenance.prov.validation.report.ValidationReport;
import scala.Function1;
import scala.Tuple2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.service.metrics.MetricsPostService.getSignature;
import static org.openprovenance.prov.validation.report.json.Serialization.registerMissingNamespace;
import static org.openprovenance.prov.validation.report.json.Serialization.serializeValidationReportAsString;

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
            logger.info("Loaded type map: " + tmp.ID);
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
        Object traffic = getTrafficLightOrError(metrics,validationReport,features);
        Object hash= getSignature(signatureService,document);

        String id=null;

        try{
            id=querier.insertMetricsRecord("document",
                    null,
                    TypePropagator.om().writeValueAsString(features),
                    om.writeValueAsString(metrics),
                    serializeValidatiorReportOrErrorAsString(validationReport),
                    om.writeValueAsString(traffic),
                    om.writeValueAsString(hash));

            System.out.println("=========== ID: " + id);

            String mapId=persistTypeMap();
            System.out.println("=========== mapId: " + mapId);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new MetricsRecord(id, "document", metrics, features, validationReport, traffic, hash);
    }

    public String serializeValidatiorReportOrErrorAsString(Object validationReport) throws JsonProcessingException {
        if (validationReport instanceof ValidationReport) {
            registerMissingNamespace((ValidationReport) validationReport);
            return serializeValidationReportAsString((ValidationReport) validationReport);
        } else {
            return om.writeValueAsString(validationReport);
        }
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

    private Object getTrafficLightOrError(Object metrics, Object validationReport, Object features) {
        try {
            Map<String, Object> m1=(Map<String, Object>) metrics;
            Map<String, Object> m2=(Map<String, Object>)m1.get(PATTERN_METRICS);

            if (m2==null) {
                return new HashMap<String,String>() {{
                    put("error", "No pattern metrics found");
                }};
            }

            EntityActivityDerivationCounter count=(EntityActivityDerivationCounter)m2.get(COUNT_DERIVATIONS_AND_GENERATIONS_AND_USAGES);
            List<TrafficLightResult> trafficLight = TrafficLight.getTrafficLightForCounts(count);
            TrafficLightResult patternTrafficLightResult=new TrafficLightResult("Triangle Pattern Rating", trafficLight);


            List<TrafficLightResult> trafficLightForValidation=new LinkedList<>();
            if (validationReport instanceof ValidationReport) {
                trafficLightForValidation.addAll(getTrafficLightForValidation((ValidationReport)validationReport));
            }
            TrafficLightResult patternTrafficLightValidationResult=new TrafficLightResult("Validation Rating", trafficLightForValidation);

            List<TrafficLightResult> typeTrafficLightSubResults=List.of(
                    new TrafficLightResult("Type1", 66, TrafficLight.TrafficLightColor.ORANGE),
                    new TrafficLightResult("Type2", 70, TrafficLight.TrafficLightColor.GREEN));
            List<TrafficLightResult> res=getTrafficLightForType(features);
            TrafficLightResult typeTrafficLightResult=new TrafficLightResult("Type Rating",res);


            List<TrafficLightResult> trafficLightSubResults = List.of(patternTrafficLightResult, patternTrafficLightValidationResult, typeTrafficLightResult);
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

    private List<TrafficLightResult> getTrafficLightForType(Object featuresObject) {
        if (featuresObject==null || !(featuresObject instanceof Map) || ((Map)featuresObject).get("error")!=null) {
            logger.info("No features found");
            return new LinkedList<>();
        }

        Map<List<Tuple2<Integer, Integer>>, Integer> reconstructedFeatures=(  Map<List<Tuple2<Integer, Integer>>, Integer>) featuresObject;

        scala.collection.immutable.Map<scala.collection.immutable.Set<ProvType>, Object> scalaFeatures = ip.backToScala(reconstructedFeatures);
        int issues1=ip.countEntitiesWithoutAttribution(scalaFeatures);

        int entitiesNumber=ip.countEntities(scalaFeatures);

        List<TrafficLightResult> trafficLightForType=new LinkedList<>();
        trafficLightForType.add(forEntityAttribution(issues1, entitiesNumber));

        return trafficLightForType;

    }

    public TrafficLightResult forEntityAttribution (int entitiesWithoutAttibution, int totalEntities) {

        double ratio=100.0 * entitiesWithoutAttibution / totalEntities;
        String comment="Percentage Entities Without Attribution"; // wat or wgb/waw
        TrafficLight.TrafficLightColor color;
        if (ratio>=60) {
            color = TrafficLight.TrafficLightColor.RED;
        } else if (ratio>=30) {
            color = TrafficLight.TrafficLightColor.ORANGE;
        } else {
            color = TrafficLight.TrafficLightColor.GREEN;
        }
        return new TrafficLightResult(comment, ratio, color);

    }

    private List<TrafficLightResult> getTrafficLightForValidation(ValidationReport validationReport) {
        List<TrafficLightResult> trafficLightForValidation=new LinkedList<>();

        List<Dependencies> dependencies1 = (validationReport.getCycle()!=null)? validationReport.getCycle(): new LinkedList<>();
        getTrafficLightForValidationDerivationCycle("Derivation Cycle", dependencies1, trafficLightForValidation);

        List<SpecializationOf> specializations = (validationReport.getSpecializationReport()!=null)? validationReport.getSpecializationReport().getSpecializationOf(): new LinkedList<>();
        getTrafficLightForValidationIssues("Specialization Not reflexive", specializations, trafficLightForValidation);

        List<MergeReport> failedMerges = (validationReport.getFailedMerge()!=null)? validationReport.getFailedMerge(): new LinkedList<>();
        getTrafficLightForValidationIssues("Failed Merges", failedMerges, trafficLightForValidation);

        List<TypeOverlap> typeOverlaps = (validationReport.getTypeOverlap()!=null)? validationReport.getTypeOverlap(): new LinkedList<>();
        getTrafficLightForValidationIssues("Type Overlaps", typeOverlaps, trafficLightForValidation);

        return trafficLightForValidation;
    }

    private void getTrafficLightForValidationDerivationCycle(String heading, List<Dependencies> dependencies, List<TrafficLightResult> trafficLightForValidation) {
        Set<Set<Statement>> dependenciesAsSets= dependencies.stream().map(dep -> new HashSet<>(dep.getStatement())).collect(Collectors.toSet());
        int count = dependenciesAsSets.size();
        TrafficLight.TrafficLightColor colour;
        double ratio=100.0;
        switch (count) {
            case 0:
                colour=TrafficLight.TrafficLightColor.GREEN;
                break;
            case 1:
                colour=TrafficLight.TrafficLightColor.ORANGE;
                ratio=60.0;
                break;
            case 2:
                colour=TrafficLight.TrafficLightColor.ORANGE;
                ratio=50.0;
                break;
            case 3:
                colour=TrafficLight.TrafficLightColor.RED;
                ratio=40.0;
                break;
            case 4:
                colour=TrafficLight.TrafficLightColor.RED;
                ratio=30.0;
                break;
            case 5:
                colour=TrafficLight.TrafficLightColor.RED;
                ratio=20.0;
                break;
            default:
                colour=TrafficLight.TrafficLightColor.RED;
                ratio=10.0;
                break;
        }
        trafficLightForValidation.add(new TrafficLightResult(heading, ratio, colour));
    }
    private void getTrafficLightForValidationIssues(String heading, List<?> specializations, List<TrafficLightResult> trafficLightForValidation) {
        int count = specializations.size();
        TrafficLight.TrafficLightColor colour;
        double ratio=100.0;
        switch (count) {
            case 0:
                colour=TrafficLight.TrafficLightColor.GREEN;
                break;
            case 1:
                colour=TrafficLight.TrafficLightColor.ORANGE;
                ratio=60.0;
                break;
            case 2:
                colour=TrafficLight.TrafficLightColor.ORANGE;
                ratio=50.0;
                break;
            case 3:
                colour=TrafficLight.TrafficLightColor.RED;
                ratio=40.0;
                break;
            case 4:
                colour=TrafficLight.TrafficLightColor.RED;
                ratio=30.0;
                break;
            case 5:
                colour=TrafficLight.TrafficLightColor.RED;
                ratio=20.0;
                break;
            default:
                colour=TrafficLight.TrafficLightColor.RED;
                ratio=10.0;
                break;
        }
        trafficLightForValidation.add(new TrafficLightResult(heading, ratio, colour));
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

    public String persistTypeMap() {
        return ip.process((map, set, list) -> {
            //querier.updateTypeMap(map, set, list);
            String id=querier.insertTypeMap(map, set, list);
            return id;
        });
    }

    public String getTypeMap() {
        return ip.processMapOfStrings( x -> x);
    }

    public List<SimpleMetrics> getAllMetricsReport() {
        return querier.getAllMetricsReport();
    }




}
