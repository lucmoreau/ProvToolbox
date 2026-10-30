package org.openprovenance.prov.service.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.model.interop.PrincipalManager;
import org.openprovenance.prov.scala.iface.Explainer;
import org.openprovenance.prov.scala.iface.XFactory;
import org.openprovenance.prov.service.core.dispatch.EnactCsvRecords;
import org.openprovenance.prov.service.core.progress.ProgressListener;
import org.openprovenance.prov.service.core.readers.TemplatesSliceConfig;
import org.openprovenance.prov.service.core.readers.TemplatesVizConfig;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.openprovenance.prov.model.interop.InteropMediaType.MEDIA_TEXT_CSV;
import static org.openprovenance.prov.service.core.TemplateService.*;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class TemplateLogic {


    public static final String HTTP_HEADER_CONTENT_PROV_HASH = "PROV-Hash";
    public static final String HTTP_HEADER_LOCATION = "Location";
    public static final String HTTP_HEADER_ACCEPT_PROV_HASH = "Accept-PROV-hash";
    public static final String HTTP_HEADER_ACCEPT_PROV_EXPLANATION = "Accept-PROV-explanation";
    public static final String HTTP_HEADER_IDEMPOTENCY_KEY = "Idempotency-Key";
    static Logger logger = LogManager.getLogger(TemplateLogic.class);
    private final ProvFactory pf;
    private final CatalogueDispatcherInterface<FileBuilder> templateDispatcher;
    private final Map<String, FileBuilder> documentBuilderDispatcher;
    private final ServiceUtils utils;
    private final ObjectMapper om;

    private final EnactCsvRecords<Object> enactCsvRecords= new EnactCsvRecords<>();
    private final TemplateQuery templateQuery;
    private final Map<String, Map<String, Set<String>>> typeAssignment;
    private final Map<String, Map<String, List<String>>> successors;
    private final PrincipalManager principalManager;
    private final Map<String, String> shortNames;
    private final Map<String, String> semanticType;
    private final String nlgXplanStrategy;
    private volatile SubmissionSwitches switches = SubmissionSwitches.DEFAULTS;

    /** Thread-scoped Idempotency-Key set by the service per request; the
     *  duplicate-recovery path below consults it. State is a static
     *  thread-local, so this instance mirrors the one in TemplateService. */
    private final org.openprovenance.prov.model.interop.SubmissionKeyManager submissionKeyManager =
            new org.openprovenance.prov.model.interop.SubmissionKeyManager();


    public TemplateLogic(ProvFactory pf, TemplateQuery templateQuery, Map<String, String> shortNames, CatalogueDispatcherInterface<FileBuilder> templateDispatcher, PrincipalManager principalManager, ServiceUtils utils, ObjectMapper om, String nlgXplanStrategy) {
        this.pf = pf;
        this.templateDispatcher = templateDispatcher;
        this.documentBuilderDispatcher = templateDispatcher.getDocumentBuilderDispatcher();
        this.utils = utils;
        this.om = om;
        om.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        this.templateQuery = templateQuery;
        this.typeAssignment = templateDispatcher.getTypeAssignment();
        this.successors = templateDispatcher.getSuccessors();
        this.principalManager = principalManager;
        this.shortNames=shortNames;
        this.semanticType=templateDispatcher.getSemanticType();
        this.nlgXplanStrategy = nlgXplanStrategy;
    }

    public List<Object> processIncomingJson(List<Map<String, Object>> entries, String acceptHeader) {
        //logger.info("Processing incoming JSON " + entries);
        final String isA=(String) entries.get(0).get(IS_A);
        List<Object> recordsResult=new LinkedList<>();
        Map<String, String[]> properties= templateDispatcher.getPropertyOrder();
        Map<String, Function<Object[],?>> enactorConverters= templateDispatcher.getEnactorConverter();
        Map<String, Function<List<Object[]>,?>> compositeEnactorConverters= templateDispatcher.getCompositeEnactorConverter();
        Map<String, Function<Object,String>> csvConverter4Outputs= templateDispatcher.getCsvConverter4Outputs();
        Map<String, Function<Object,String>> csvConverter4OutputsComposite= templateDispatcher.getCsvConverter4OutputsComposite();

        String[] props=properties.get(isA);
        Function<Object[],?> enactor=enactorConverters.get(isA);
        Function<Object,String> csv_processor_1=csvConverter4Outputs.get(isA);
        Function<Object,String> csv_processor_Composite=csvConverter4OutputsComposite.get(isA);

        if (enactor!=null) {
            for (Map<String, Object> entry : entries) {
                Object[] array = convertToArray(entry, props);
                if (acceptHeader.equals(APPLICATION_VND_KCL_PROV_TEMPLATE_JSON)) {
                    recordsResult.add(enactWithDuplicateRecovery(() -> templateQuery.inTransaction(() -> enactor.apply(array)), -1));
                } else {
                    // csv here
                    recordsResult.add(csv_processor_1.apply(templateQuery.inTransaction(() -> enactor.apply(array))));
                }
            }
        } else {
            for (Map<String, Object> composite : entries) {
                List<Object[]> objects = new LinkedList<>();
                Object[] array = convertToArray(composite, props);
                objects.add(array);
                for (Map<String, Object> composee: (List<Map<String, Object>>) composite.get(ELEMENTS)) {
                    String is2A=(String) composee.get(IS_A);
                    String[] props2=properties.get(is2A);
                    Object[] array2 = convertToArray(composee, props2);
                    objects.add(array2);
                }
                Function<List<Object[]>,?> compositeEnactor = compositeEnactorConverters.get(isA);
                if (acceptHeader.equals(APPLICATION_VND_KCL_PROV_TEMPLATE_JSON)) {
                    recordsResult.add(enactWithDuplicateRecovery(() -> templateQuery.inTransaction(() -> compositeEnactor.apply(objects)), objects.size() - 1));
                } else {
                    // csv here
                    String csvLines = csv_processor_Composite.apply(templateQuery.inTransaction(() -> compositeEnactor.apply(objects)));
                    logger.info(csvLines);
                    Collections.addAll(recordsResult, csvLines.split("\\\\n"));
                }
            }
        }
        return recordsResult;
    }

    /**
     * Statement-POST idempotency (the {@code Idempotency-Key} header): a keyed
     * duplicate raises a unique violation on {@code record_index_submission_key},
     * which aborts the whole composed statement — template insert included — so
     * nothing was duplicated and the original submission's rows are intact.
     * This catches exactly that violation and replays the original response
     * from the store ({@link TemplateQuery#recoverBySubmissionKey}); every
     * other failure propagates unchanged, as does every failure of a keyless
     * enactment.
     *
     * <p>A re-shaped resend is refused, never answered with the first response:
     * a composite resent under its key with another number of elements than the
     * one the store holds raises {@link ReshapedResendException} (the service
     * answers 409 Conflict). Replaying the stored response would hand the client
     * fewer (or more) elements than it sent, and a client reading them by
     * position reads past the end — nextgen T-518, where a ledger posting gained
     * lines after it was first posted.</p>
     *
     * @param sentElements the composite's element count, or -1 for a simple template
     */
    private Object enactWithDuplicateRecovery(Supplier<Object> enactment, int sentElements) {
        String submissionKey = submissionKeyManager.getSubmissionKey();
        if (submissionKey == null) {
            return enactment.get();
        }
        try {
            return enactment.get();
        } catch (RuntimeException e) {
            if (!isSubmissionKeyConflict(e)) {
                throw e;
            }
            Map<String, Object> original = templateQuery.recoverBySubmissionKey(submissionKey);
            if (sentElements >= 0) {
                Object stored = original.get(ELEMENTS);
                int held = stored instanceof List ? ((List<?>) stored).size() : 0;
                if (held != sentElements) {
                    logger.warn("re-shaped resend for key '" + submissionKey + "': " + sentElements
                            + " element(s) sent, " + held + " held — refused");
                    throw new ReshapedResendException("submission key '" + submissionKey + "' names a composite of " + held
                            + " element(s); this resend carries " + sentElements
                            + " — a re-shaped resend is refused, never answered with the first response");
                }
            }
            logger.info("duplicate submission for key '" + submissionKey
                    + "' — replaying the original response");
            return original;
        }
    }

    /** A keyed resend whose shape differs from the submission the store holds under that key (answered 409 Conflict). */
    public static class ReshapedResendException extends RuntimeException {
        public ReshapedResendException(String message) {
            super(message);
        }
    }

    private static boolean isSubmissionKeyConflict(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof java.sql.SQLException) {
                java.sql.SQLException sql = (java.sql.SQLException) t;
                if ("23505".equals(sql.getSQLState())
                        && String.valueOf(sql.getMessage()).contains("record_index_submission_key")) {
                    return true;
                }
            }
        }
        return false;
    }

    public Object[] convertToArray(Map<String, Object> entry, String[] props) {
        Object[] array = new Object[props.length];
        for (int i = 0; i < props.length; i++) {
            String prop = props[i];
            array[i] = entry.getOrDefault(prop, null);
        }

        return array;
    }

    private void debugDisplay(String msg, Object object) {
        try {
            System.out.println(msg + om.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> processIncomingCsv(CSVParser csv, String acceptHeader) {
        Collection<CSVRecord> collection=csv.getRecords();
        Map<String, Function<Object[],?>> enactors = templateDispatcher.getEnactorConverter();
        Map<String, Function<Object[],Object>> enactors2= enactors.entrySet().stream().filter(e->e.getValue()!=null).collect(Collectors.toMap(Map.Entry::getKey, e -> (Function<Object[],Object>) e.getValue()));

        Map<String, Function<List<Object[]>,?>> compositeEnactors= templateDispatcher.getCompositeEnactorConverter();
        Map<String, Function<Object,String>> csvConverter4Outputs= templateDispatcher.getCsvConverter4Outputs();
        Map<String, Function<Object,String>> csvConverter4OutputsComposite= templateDispatcher.getCsvConverter4OutputsComposite();

        Map<String, Function<List<Object[]>,Object>> compositeEnactors2= compositeEnactors.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (Function<List<Object[]>,Object>) e.getValue()));
        return switch (acceptHeader) {
            case MEDIA_TEXT_CSV ->
                    enactCsvRecords.process(collection, enactors2, compositeEnactors2, csvConverter4Outputs, csvConverter4OutputsComposite);
            case APPLICATION_VND_KCL_PROV_TEMPLATE_JSON ->
                    enactCsvRecords.process(collection, enactors2, compositeEnactors2);
            default -> throw new IllegalStateException("Unsupported accept header " + acceptHeader);
        };
    }

    @NotNull
    public StreamingOutput streamOutRecordsToCSV(List<Object> newRecords) {
        return out -> newRecords.forEach(record -> {
            try {
                out.write(((String)record).getBytes(StandardCharsets.UTF_8));
                out.write('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void generateViz(TemplatesVizConfig config, String principal, String iconsFolderForGraphviz, OutputStream out, ProgressListener listener) {

        Map<String,Map<String,String>> baseTypes = computeBaseTypes();

        //logger.info("baseTypes " + baseTypes);
        templateQuery.generateViz(config.id, config.template, config.property, config.style, config.parameters, baseTypes, iconsFolderForGraphviz, semanticType, principal, out, listener);
    }

    /**
     * Renders the slice between the config's upstream anchor ({@code id}/{@code template}/
     * {@code property}, an input variable) and its downstream anchor
     * ({@code downstreamId}/{@code downstreamTemplate}/{@code downstreamProperty}, an
     * output variable).
     *
     * @see TemplateQuery#generateSlice
     */
    public void generateSlice(TemplatesSliceConfig config, String principal, String iconsFolderForGraphviz, OutputStream out, ProgressListener listener) {

        Map<String,Map<String,String>> baseTypes = computeBaseTypes();

        templateQuery.generateSlice(config.id, config.template, config.property,
                                    config.downstreamId, config.downstreamTemplate, config.downstreamProperty,
                                    config.style, config.parameters, baseTypes, iconsFolderForGraphviz,
                                    semanticType, principal, out, listener);
    }

    /** Template → variable → preferred base type, as the dot renderer colours nodes by. */
    private Map<String,Map<String,String>> computeBaseTypes() {

        typeAssignment.entrySet().removeIf(entry -> entry.getValue() ==null || entry.getValue().isEmpty());

        //logger.info("typeAssignment " + typeAssignment);

        return typeAssignment
                .keySet()
                .stream()
                .collect(Collectors
                        .toMap(tpl -> tpl,
                                tpl ->
                                        typeAssignment
                                                .get(tpl)
                                                .keySet()
                                                .stream()
                                                .collect(Collectors
                                                        .toMap(var->var,
                                                                var -> preferredType(typeAssignment
                                                                        .get(tpl)
                                                                        .get(var))))));
    }


    private int colorValue(String s) {
        if (s==null) return 0;
        switch (s) {
            case "http://www.w3.org/ns/prov#Entity":
                return 1;
            case "http://www.w3.org/ns/prov#Activity":
                return 2;
            case "http://www.w3.org/ns/prov#Agent":
                return 3;
            default:
                return 0;
        }
    }

    private String preferredType(Set<String> value) {
        if (value==null) return "none";
        return value.stream().max(Comparator.comparingInt(this::colorValue)).orElse("none");
    }


    public List<TemplateQuery.RecordEntry2> generateLiveNode(String relation, Integer id, String principal) {
        List<TemplateQuery.RecordEntry2> records=templateQuery.queryTemplatesRecordsById(relation, id, 30, principal);
        //System.out.println("records " + records);
        return records;
    }

    /** The switches of the write path (read-back access control, hash); the service's own behaviour by default. */
    public void setSwitches(SubmissionSwitches switches) {
        this.switches = switches;
    }

    public SubmissionSwitches getSwitches() {
        return switches;
    }

    public Object submitPostProcessing(int id, String templateFullyQualifiedName) {
        logger.debug("postProcessing " + id + " " + templateFullyQualifiedName);

        String principal=principalManager.getPrincipal();
        List<Object[]> records = templateQuery.queryJustWritten(templateFullyQualifiedName, id, principal, switches.readbackAccessControl);

        logger.debug("PROV_API " + provAPI);
        logger.debug("records " + records.size() + " " + records.stream().map(Arrays::toString).collect(Collectors.joining(",")));
        if (!records.isEmpty()) {
            boolean hashed = switches.hash != SubmissionSwitches.Hash.OFF;
            if (records.size()>1) {
                // a composite: its elements' relations were written by their own post-processing, as each was enacted
                Map<String, String> hash = hashed ? templateQuery.computeHash(templateFullyQualifiedName, id, records) : null;
                storeHash(templateFullyQualifiedName, id, hash, principal);
            } else {
                Object[] record = records.get(0);
                Map<String, String> hash = hashed ? templateQuery.computeHash(templateFullyQualifiedName, id, record) : null;
                templateQuery.getRelationMapping().mapGraphToRelations(templateFullyQualifiedName,id,record);
                storeHash(templateFullyQualifiedName, id, hash, principal);
            }
            headerInfo.get().put(HTTP_HEADER_LOCATION,provAPI + "/template/" + templateFullyQualifiedName + "/" + id);

        }
        return null;
    }

    /** The hash stored in record_index (submission.hash=update) and sent in the header (update, header); none when off. */
    private void storeHash(String templateFullyQualifiedName, int id, Map<String, String> hash, String principal) {
        if (hash == null) {
            headerInfo.get().remove(HTTP_HEADER_CONTENT_PROV_HASH);   // the thread's previous request's must not be sent
            return;
        }
        if (switches.hash == SubmissionSwitches.Hash.UPDATE) {
            templateQuery.updateHash(shortNames.get(templateFullyQualifiedName), id, hash, principal);
            logger.debug("update hash for " + id + " " + templateFullyQualifiedName + ": " + hash);
        }
        headerInfo.get().put(HTTP_HEADER_CONTENT_PROV_HASH, getContentProvHash(hash));
    }
    Base64.Encoder encoder = Base64.getEncoder();

    private String getContentProvHash(Map<String, String> hash) {
        return encoder.encodeToString(templateQuery.makeHashRecord(hash).getBytes());
    }

    final ProvFactory pFactoryS = org.openprovenance.prov.scala.immutable.ProvFactory.pf();
    final XFactory factory = new XFactory();
    final Explainer explainer=factory.makeExplainer();
    final org.openprovenance.prov.scala.iface.Narrator narrator=factory.makeNarrator();

    public Map<String, String> generateExplanation(String templateFullyQualifiedName, Integer id, String headerAcceptExplanation, Document doc, int format_option) {

        String template=shortNames.get(templateFullyQualifiedName).replace("_","-");

        org.openprovenance.prov.scala.immutable.Document sdoc = (org.openprovenance.prov.scala.immutable.Document) new BeanTraversal(pFactoryS, pFactoryS).doAction(doc);

        XplainerConfig config = (Objects.equals(nlgXplanStrategy, "template-based"))? makeXplainerConfig(template,format_option): makeXplainerConfig();

        logger.info("generateExplanation " + id + " " + template + " " + config + " " +  headerAcceptExplanation + " " + nlgXplanStrategy);

        scala.collection.immutable.Map<String, scala.collection.immutable.List<String>> result = narrator.getTextOnly(explainer.explain(sdoc, config));

        // convert result to java map
        Map<String, String> javaResult = new HashMap<>();
        for (Map.Entry<String, scala.collection.immutable.List<String>> entry : scala.jdk.CollectionConverters.MapHasAsJava(result).asJava().entrySet()) {
            javaResult.put(entry.getKey(), entry.getValue().mkString("\n"));
        }

        //result.put("default", "No explanation available for " + template + " " + id);
        return javaResult;
    }

    public @NonNull XplainerConfig makeXplainerConfig() {
        System.out.println("calling default XplainerConfig (1)");
        return new XplainerConfig();
    }
    public @NonNull XplainerConfig makeXplainerConfig(String template,  int format_option) {
        System.out.println("calling default XplainerConfig (2)");
        return new XplainerConfig();
    }
}
