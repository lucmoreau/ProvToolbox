package org.openprovenance.prov.service.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.service.core.progress.ProgressListener;
import org.openprovenance.prov.service.core.progress.VizStages;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.viz.templates.TemplateConnectionGraph;

import java.io.*;
import java.util.*;

/**
 * The service's face of the template-connection drawings: it turns query results into a
 * {@link TemplateConnectionGraph} and lets the prov-dot drawing code render it.
 */
public class TemplatesToDot extends org.openprovenance.prov.viz.templates.TemplatesToDot {

    private static final Logger logger = LogManager.getLogger(TemplatesToDot.class);
    private final List<TemplateQuery.TemplateConnection> templateConnections;
    private final CatalogueDispatcherInterface<FileBuilder> templateDispatcher;
    private final Map<String, Map<String, Map<String, String>>> ioMap;
    private final Map<String, Map<String, String>> baseTypes;
    private final String style;
    private final TemplateQuery templateQuery;
    private final String principal;
    private final String provAPI;
    private final Map<String, String> parameters;
    private final Map<String, Map<String, List<String>>> selectedSuccessors;
    /** The record the visualisation was ASKED for (fully qualified name and id),
     *  or null when the caller did not say. A record with no connection to any
     *  other appears in no connection row, so without this the graph would be
     *  empty — the walk shows the starting point even when it stands alone. */
    private final String startTemplate;
    private final Integer startTemplateId;

    public TemplatesToDot(List<TemplateQuery.TemplateConnection> templateConnections,
                          String style,
                          boolean withIcons,
                          String iconDirectory,
                          Map<String, String> parameters,
                          Map<String, Map<String, String>> baseTypes,
                          Map<String, Map<String, Map<String, String>>> ioMap,
                          CatalogueDispatcherInterface<FileBuilder> templateDispatcher,
                          Map<String, Map<String, List<String>>> selectedSuccessors,
                          ProvFactory pf,
                          TemplateQuery templateQuery,
                          String principal,
                          String provAPI) {
        this(templateConnections, style, withIcons, iconDirectory, parameters, baseTypes, ioMap,
             templateDispatcher, selectedSuccessors, pf, templateQuery, principal, provAPI, null, null);
    }

    public TemplatesToDot(List<TemplateQuery.TemplateConnection> templateConnections,
                          String style,
                          boolean withIcons,
                          String iconDirectory,
                          Map<String, String> parameters,
                          Map<String, Map<String, String>> baseTypes,
                          Map<String, Map<String, Map<String, String>>> ioMap,
                          CatalogueDispatcherInterface<FileBuilder> templateDispatcher,
                          Map<String, Map<String, List<String>>> selectedSuccessors,
                          ProvFactory pf,
                          TemplateQuery templateQuery,
                          String principal,
                          String provAPI,
                          String startTemplate,
                          Integer startTemplateId) {
        super(pf, withIcons, iconDirectory);
        this.startTemplate=startTemplate;
        this.startTemplateId=startTemplateId;
        this.templateConnections = templateConnections;
        this.templateDispatcher = templateDispatcher;
        this.ioMap = ioMap;
        this.baseTypes = baseTypes;
        this.style=style;
        this.templateQuery=templateQuery;
        this.principal=principal;
        this.provAPI=provAPI;
        this.parameters=parameters;
        this.selectedSuccessors=selectedSuccessors;
    }

    /** What the drawing needs, gathered from the query results. */
    public TemplateConnectionGraph connectionGraph() {
        TemplateConnectionGraph g = new TemplateConnectionGraph();
        for (TemplateQuery.TemplateConnection c : templateConnections) {
            g.connections.add(TemplateConnectionGraph.Connection.of(
                    c.in_template, c.in_id, c.in_property, c.in_type,
                    c.out_template, c.out_id, c.out_property, c.out_type));
        }
        Map<String, String> shortNames = templateQuery.getShortNames();
        g.shortNames.putAll(shortNames);
        if (ioMap.get("input") != null) g.inputs.putAll(ioMap.get("input"));
        if (ioMap.get("output") != null) g.outputs.putAll(ioMap.get("output"));
        if (baseTypes != null) g.baseTypes.putAll(baseTypes);
        if (selectedSuccessors != null) g.selectedSuccessors.putAll(selectedSuccessors);
        g.provAPI = provAPI;
        g.startTemplate = startTemplate;
        g.startTemplateId = startTemplateId;
        if (startTemplate != null && startTemplateId != null && !connectionsMention(startTemplate, startTemplateId)) {
            // only a start no connection brought needs its type looked up: a connected one carries its row's
            g.startSemanticType = templateQuery.semanticTypeOf(shortNames.get(startTemplate), startTemplateId, principal);
        }
        return g;
    }

    private boolean connectionsMention(String template, Integer id) {
        return templateConnections.stream().anyMatch(c ->
                (template.equals(c.in_template) && id.equals(c.in_id)) || (template.equals(c.out_template) && id.equals(c.out_id)));
    }

    public void convert(Document graph, OutputStream os, String title, ProgressListener listener) {
        File dotFile;

        listener.started(VizStages.PROV_BUILD);
        long provStart = System.nanoTime();
        try {
            dotFile = File.createTempFile("temp", ".dot");
            logger.debug("dotFile: " + dotFile);
            convert(graph, new PrintStream(new FileOutputStream(dotFile)), title);
            listener.done(VizStages.PROV_BUILD, (System.nanoTime() - provStart) / 1_000_000);
        } catch (IOException e) {
            listener.failed(VizStages.PROV_BUILD, e);
            logger.throwing(e);
            throw new UncheckedException(e);
        } catch (RuntimeException e) {
            listener.failed(VizStages.PROV_BUILD, e);
            throw e;
        }
        listener.detail(VizStages.PROV_BUILD, "DOT " + dotFile.length() + " bytes");

        listener.started(VizStages.RENDER);
        long renderStart = System.nanoTime();
        long svgBytes;
        try {
            Runtime runtime = Runtime.getRuntime();
            java.lang.Process proc = runtime.exec("dot  -Tsvg " + dotFile);
            InputStream is = proc.getInputStream();
            svgBytes = org.apache.commons.io.IOUtils.copyLarge(is, os);
            listener.done(VizStages.RENDER, (System.nanoTime() - renderStart) / 1_000_000);
        } catch (IOException e) {
            listener.failed(VizStages.RENDER, e);
            logger.throwing(e);
            throw new UncheckedException(e);
        } catch (RuntimeException e) {
            listener.failed(VizStages.RENDER, e);
            throw e;
        }
        listener.detail(VizStages.RENDER, "SVG " + svgBytes + " bytes");

        logger.info("finished conversion to svg");
        @SuppressWarnings("unused")
        boolean resultCode = dotFile.delete();
    }

    @Override
    public void convert(Document ignore, PrintStream out, String title) {
        switch (style) {
            case "template":
                convertTemplate(connectionGraph(), out, title);
                break;
            case "prov":
                convert_prov(ignore, out, title);
                break;
            case "entities":
                convertEntities(connectionGraph(), out, title);
                break;
            default:
                throw new UnsupportedOperationException("style not supported: " + style);
        }
    }

    public void convert_prov(Document ignore, PrintStream out, String title) {
        Set<TemplateQuery.RecordEntry> the_templates = new HashSet<>();

        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {
            TemplateQuery.RecordEntry entry_in=new TemplateQuery.RecordEntry();
            entry_in.table=templateConnection.in_template;
            entry_in.key=templateConnection.in_id;
            the_templates.add(entry_in);

            TemplateQuery.RecordEntry entry_out=new TemplateQuery.RecordEntry();
            entry_out.table=templateConnection.out_template;
            entry_out.key=templateConnection.out_id;
            the_templates.add(entry_out);
        }

        List<Object[]> the_records = new LinkedList<>();
        for (TemplateQuery.RecordEntry linked_record : the_templates) {
            Integer simple = linked_record.key;
            List<Object[]> simple_records = templateQuery.querySimple(linked_record.table, simple, false, principal);
            the_records.addAll(simple_records);
        }

        Document result=templateQuery.constructDocument(the_records);

        super.convert(result, out, title);
    }
}
