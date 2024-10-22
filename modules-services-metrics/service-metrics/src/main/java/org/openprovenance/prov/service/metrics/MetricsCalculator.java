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
import scala.Tuple2;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

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


    MetricsCalculator() {
        this(new DummyMetricsQuery(),null);
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
            String explanation="This composite metrics is a weighted sum of traffic lights for various metrics related to triangles.";
            TrafficLightResult patternTrafficLightResult=new TrafficLightResult("Triangle Pattern Rating", trafficLight, explanation);


            List<TrafficLightResult> trafficLightForValidation=new LinkedList<>();
            if (validationReport instanceof ValidationReport) {
                trafficLightForValidation.addAll(getTrafficLightForValidation((ValidationReport)validationReport));
            }
            String explanation2="This composite metrics is a weighted sum of traffic lights for various validation issues.";
            TrafficLightResult patternTrafficLightValidationResult=new TrafficLightResult("Validation Rating", trafficLightForValidation, explanation2);


            List<TrafficLightResult> res=getTrafficLightForType(features);
            String explanation3="This composite metrics is a weighted sum of traffic lights for various type issues.";
            TrafficLightResult typeTrafficLightResult=new TrafficLightResult("Type Rating",res, explanation3);


            List<TrafficLightResult> trafficLightSubResults = List.of(patternTrafficLightResult, patternTrafficLightValidationResult, typeTrafficLightResult);
            String overallExplanation="This composite metrics is a weighted sum of traffic lights for various metrics related to patterns, validation and types.";
            TrafficLightResult overallTrafficLightResult=new TrafficLightResult("Overall Rating",trafficLightSubResults,overallExplanation);

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

        double der=ip.countAverageDerivationPathLength(scalaFeatures);

        System.out.println("Average Derivation Path Length: " + der);


        List<TrafficLightResult> trafficLightForType=new LinkedList<>();
        trafficLightForType.add(forEntityAttribution(issues1, entitiesNumber));
        trafficLightForType.add(forDerivations(der));

        return trafficLightForType;

    }

    private TrafficLightResult forDerivations(double length) {
        String comment="Average Derivation length";
        TrafficLight.TrafficLightColor color;
        if (length>=2.5) {
            color = TrafficLight.TrafficLightColor.GREEN;
        } else if (length>=1) {
            color = TrafficLight.TrafficLightColor.ORANGE;
        } else {
            color = TrafficLight.TrafficLightColor.RED;
        }
        String explanation="The average derivation length is the average number of steps in a derivation path. A derivation path is formed by a sequence of derivations, each linking an entity to the entity it was transitively derived from, where a derivation path can be seen in a typing of an entity (note that typing information give us evidence of existence of such path; it does not indicate their cardinality).  It is desirable for a derivation path to be long, as it indicates that deep provenance connecting entities to their original source is available. To increase this metrics, add derivations to deeper provenance.";
        TrafficLightResult trafficLightResult = new TrafficLightResult(comment, length, TrafficLightResult.ResultKind.ABSOLUTE, color, explanation);
        trafficLightResult.weight=1.0/3.0*2.0;
        return trafficLightResult;
    }

    public TrafficLightResult forEntityAttribution (int entitiesWithoutAttribution, int totalEntities) {

        double ratio=100.0 * entitiesWithoutAttribution / totalEntities;
        String comment="Percentage Entities Without Attribution"; // wat or wgb/waw
        TrafficLight.TrafficLightColor color;
        String explanation="The percentage of entities without attribution is the number of entities without attribution divided by the total number of entities. An entity without attribution is an entity that is not attributed to any agent directly, or is not generated by an activity associated with an agent. It is desirable for the percentage of entities without attribution to be small. To increase this metrics, add an attribution to entities or associate activities with agents.";
        if (ratio>=60) {
            color = TrafficLight.TrafficLightColor.RED;
        } else if (ratio>=30) {
            color = TrafficLight.TrafficLightColor.ORANGE;
        } else {
            color = TrafficLight.TrafficLightColor.GREEN;
        }
        return new TrafficLightResult(comment, ratio, color, explanation);

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
        String explanation="The number of derivation cycles is the number of cycles in a provenance graph. A cycle is a set of derivation that form a cycle, where a cycle is formed by a sequence of derivations that transitively link an entity to itself. W3C PROV Constraints requires valid provenance to be free of derivation cycles. In practice, it is desirable for the number of derivation cycles to be very small. To increase this metrics, remove cycles in the provenance graph.  (Note that provenance summaries may introduce cycles.)";
        trafficLightForValidation.add(new TrafficLightResult(heading, ratio, colour, explanation));
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
        String explanation="This metric is given by the number of reflective specializations in a provenance graph (after applying their transitive closure).  W3C PROV Constraints requires valid provenance to be free of reflexive specializations. In practice, it is desirable for the number of specializations to be very small. To increase this metrics, remove reflexive specializations in the provenance graph.";
        trafficLightForValidation.add(new TrafficLightResult(heading, ratio, colour, explanation));
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

    public Object getSignature(SignatureService signatureService, org.openprovenance.prov.model.Document doc) {
        return MetricsPostService.getSignature(signatureService, doc);
    }


}
