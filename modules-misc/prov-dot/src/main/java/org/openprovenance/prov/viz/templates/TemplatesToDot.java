package org.openprovenance.prov.viz.templates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.viz.*;
import org.openprovenance.prov.viz.templates.TemplateConnectionGraph.Connection;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.model.NamespacePrefixMapper.DOT_NS;

/**
 * Drawings of the connections between template records: each record as a table of its ports
 * ({@link #convertTemplate}), or the connected entities alone ({@link #convertEntities}).
 */
public class TemplatesToDot extends ProvToDot {

    private static final Logger logger = LogManager.getLogger(TemplatesToDot.class);

    protected final boolean withIcons;
    protected final String iconDirectory;

    public TemplatesToDot(ProvFactory pf, boolean withIcons, String iconDirectory) {
        super(pf);
        this.withIcons = withIcons;
        this.iconDirectory = iconDirectory;
    }

    public static String createHtmlTable(TemplateInfo templateInfo,
                                         boolean withIcons,
                                         String iconDirectory,
                                         List<String> inputsNames,
                                         List<String> inputsPorts,
                                         List<String> inputsColors,
                                         List<String> outputsNames,
                                         List<String> outputsPorts,
                                         List<String> outputColors) {
        StringBuilder html = new StringBuilder();

        html.append("<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n");

        // First row with rowspan and input cells
        html.append("  <TR>\n");

        if (withIcons) {
            // IMPORTANT NOTE
            // While graphviz documentation indicates that svg images <IMG> are permitted, in practice it does not seem to be the case.
            // Thus, we refer to png file.
            // Such image files MUST be on the file system.
            // When generating an SVG, graphviz does not embed the images, but instead links to them.
            // This is problematic, when the visualisation is served by a service to a browser, because the browser will not be able to dereference a file image.
            // For this to work, the client needs to rewrite the url.
            // This is implemented by function rewriteImageHrefs(svgElement) in form.html
            String iconImage = "<IMG SRC=\"" + iconDirectory + "/" + templateInfo.template + ".png\"/>";
            html.append(String.format("    <TD ROWSPAN=\"3\" HREF=\"%s\"  TARGET=\"_blank\">%s</TD>\n", templateInfo.url, iconImage));
        }

        String typeInfo = "";
        if (templateInfo.semanticType != null && !templateInfo.semanticType.equals("None1")) {
            typeInfo += "<BR/>(";
            typeInfo += templateInfo.semanticType;
            typeInfo += ")";
        }

        html.append(String.format("    <TD ROWSPAN=\"3\" HREF=\"%s\"  TARGET=\"_blank\">%s %s</TD>\n", templateInfo.url, templateInfo.templateId, typeInfo));

        if (inputsNames.isEmpty()) {
            html.append("    <TD></TD>\n");
        }
        for (int i = 0; i < inputsNames.size(); i++) {
            html.append(String.format("    <TD PORT=\"%s\" BGCOLOR=\"%s\" HREF=\"%s\" TARGET=\"_blank\">%s</TD>\n",
                    inputsPorts.get(i), inputsColors.get(i), templateInfo.url.replace(".svg", "/" + inputsNames.get(i)), inputsNames.get(i)));
        }
        html.append("  </TR>\n");

        // Second row for outputs
        html.append("  <TR>\n");

        for (int i = 0; i < outputsNames.size(); i++) {
            html.append(String.format("    <TD PORT=\"%s\" BGCOLOR=\"%s\"  HREF=\"%s\"  TARGET=\"_blank\">%s</TD>\n",
                    outputsPorts.get(i), outputColors.get(i), templateInfo.url.replace(".svg", "/" + outputsNames.get(i)), outputsNames.get(i)));
        }
        html.append("  </TR>\n");

        html.append("</TABLE>");

        return html.toString();
    }

    final Map<String, String> provcolors = new HashMap<>() {{
        put("http://www.w3.org/ns/prov#Entity", ENTITY_FILLCOLOUR);
        put("http://www.w3.org/ns/prov#Activity", ACTIVITY_FILL_COLOUR);
        put("http://www.w3.org/ns/prov#Agent", AGENT_FILLCOLOUR);
    }};

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              ENTITIES STYLE
    ///
    //////////////////////////////////////////////////////////////////////

    /** The entities the connections relate, each derived from the one before it along the selected successors. */
    public Document entitiesDocument(TemplateConnectionGraph g) {
        // creates a map from in to out
        Map<QualifiedName, QualifiedName> map = new HashMap<>();
        for (Connection c : g.connections) {
            QualifiedName outQn = qualifiedPortNameAsQn(g.provAPI, c.outTemplate, String.valueOf(c.outId), c.outProperty);
            QualifiedName inQn = qualifiedPortNameAsQn(g.provAPI, c.inTemplate, String.valueOf(c.inId), c.inProperty);
            map.put(inQn, outQn);
        }

        Document doc = pf.newDocument();

        Set<QualifiedName> seen = new HashSet<>();
        for (Connection c : g.connections) {
            String template = c.inTemplate;
            String templateId = String.valueOf(c.inId);
            String property = c.inProperty;
            Map<String, List<String>> successors = g.selectedSuccessors.get(template);
            List<String> next = (successors == null) ? null : successors.get(property);
            if (next != null) {
                for (String n : next) {
                    QualifiedName older = map.get(qualifiedPortNameAsQn(g.provAPI, template, templateId, property));
                    QualifiedName newer = qualifiedPortNameAsQn(g.provAPI, template, templateId, n);

                    if (!seen.contains(older)) {
                        seen.add(older);
                        doc.getStatementOrBundle().add(pf.newEntity(older));
                    }
                    if (!seen.contains(newer)) {
                        seen.add(newer);
                        doc.getStatementOrBundle().add(pf.newEntity(newer));
                    }

                    List<Attribute> attrs = new LinkedList<>();
                    attrs.add(pf.newAttribute(pf.newQualifiedName(DOT_NS, "style", "dot"), "dashed", pf.getName().XSD_STRING));
                    WasDerivedFrom edge = pf.newWasDerivedFrom(null, newer, older, null, null, null, attrs);
                    doc.getStatementOrBundle().add(edge);
                }
            }
        }
        return doc;
    }

    public void convertEntities(TemplateConnectionGraph g, PrintStream out, String title) {
        convert(entitiesDocument(g), out, title);
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              TEMPLATE STYLE
    ///
    //////////////////////////////////////////////////////////////////////

    /** The model of the template style: one port table per record, an edge per connection. */
    public VizGraph buildTemplateGraph(TemplateConnectionGraph g, String title) {
        if (title != null) name = title;
        VizGraph graph = new VizGraph(name);

        // pairs <template, templateInstance>
        Set<TemplateInfo> allTemplates = new HashSet<>();
        Map<String, String> shortNames = g.shortNames;
        for (Connection c : g.connections) {
            allTemplates.add(TemplateInfo.of(c.inTemplate, templateName(shortNames.get(c.inTemplate), c.inId), url(g.provAPI, c.inTemplate, c.inId), c.inType));
            allTemplates.add(TemplateInfo.of(c.outTemplate, templateName(shortNames.get(c.outTemplate), c.outId), url(g.provAPI, c.outTemplate, c.outId), c.outType));
        }
        // The record the walk STARTS from appears in a connection row only if
        // it is connected to another; a template preceded by nothing (and
        // followed by nothing along the selected relations) is in none, and
        // the graph would come out empty. Add it explicitly — but only when
        // the connections did not already bring it, so a connected start is
        // still described by its own row's semantic type, and graphviz never
        // receives the same node twice.
        if (g.startTemplate != null && g.startTemplateId != null
                && allTemplates.stream().noneMatch(t -> g.startTemplate.equals(t.template)
                && templateName(shortNames.get(g.startTemplate), g.startTemplateId).equals(t.templateId))) {
            allTemplates.add(TemplateInfo.of(g.startTemplate,
                    templateName(shortNames.get(g.startTemplate), g.startTemplateId),
                    url(g.provAPI, g.startTemplate, g.startTemplateId),
                    g.startSemanticType));
        }

        Map<String, Map<String, String>> inputs = g.inputs;
        Map<String, Map<String, String>> outputs = new HashMap<>(g.outputs);
        Set<String> overlayTemplates = new HashSet<>();
        // ── Virtual outputs for all-input (decorator/overlay) templates ──
        // Templates where ALL properties are inputs are stripped from the
        // output map by removeIf() in getIoMap(), so they never appear in
        // output_table and the loop below can never "arrive at" them.
        // Fix: for any template present in input_table (it references an
        // entity from this table) but absent from output_table (no declared
        // outputs), inject it into output_table using its input properties
        // as virtual output keys.  The traversal can then hop through the
        // template to follow predecessor_table derivation edges onward.
        for (String template : inputs.keySet()) {
            if (!outputs.containsKey(template)) {
                outputs.put(template, inputs.get(template));
                overlayTemplates.add(template);
            }
        }

        for (TemplateInfo templateInfo : allTemplates) {
            String templateFullyQualifiedName = templateInfo.template;
            String templateId = templateInfo.templateId;
            Map<String, String> templateBaseTypes = g.baseTypes.get(templateFullyQualifiedName);

            String template = shortNames.get(templateFullyQualifiedName);

            // a start template with neither declared inputs nor outputs (and
            // no base types) is still a node worth drawing — an absent map
            // means "no ports", never a failure
            Map<String, String> portTypes = (templateBaseTypes == null) ? Map.of() : templateBaseTypes;

            Map<String, String> templateInputs = inputs.get(template);
            List<String> inputsNames = (templateInputs == null) ? List.of() : new ArrayList<>(templateInputs.keySet());
            List<String> inputPorts = inputsNames.stream().map(s -> portName(template, templateId, s)).collect(Collectors.toList());
            List<String> inputsColors = inputsNames.stream().map(s -> provcolors.get(portTypes.get(s))).collect(Collectors.toList());

            Map<String, String> templateOutputs = outputs.get(template);
            List<String> outputsNames = (templateOutputs == null) ? List.of() : new ArrayList<>(templateOutputs.keySet());
            List<String> outputsPorts = outputsNames.stream().map(s -> portName(template, templateId, s)).collect(Collectors.toList());
            List<String> outputsColors = outputsNames.stream().map(s -> provcolors.get(portTypes.get(s))).collect(Collectors.toList());

            String html = createHtmlTable(templateInfo, withIcons, iconDirectory, inputsNames, inputPorts, inputsColors, outputsNames, outputsPorts, outputsColors);
            if (overlayTemplates.contains(template)) {
                logger.debug("Overlay template: " + html);
            }
            graph.add(templateNode(templateId, html));
        }

        for (Connection c : g.connections) {
            String inTemplate = shortNames.get(c.inTemplate);
            String inId = templateName(inTemplate, c.inId);
            String outTemplate = shortNames.get(c.outTemplate);
            String outId = templateName(outTemplate, c.outId);
            graph.add(connectionEdge(inId, portName(inTemplate, inId, c.inProperty),
                    outId, portName(outTemplate, outId, c.outProperty)));
        }
        return graph;
    }

    public void convertTemplate(TemplateConnectionGraph g, PrintStream out, String title) {
        render(process(buildTemplateGraph(g, title)), out);
    }

    ArrowKind headstyle = ArrowKind.INVEMPTY;
    ArrowKind tailstyle = ArrowKind.EMPTY;

    /** A template record: a node that is nothing but its HTML table of ports. */
    public VizNode templateNode(String templateId, String htmlTable) {
        VizNode n = new VizNode(templateId, NodeKind.CUSTOM);
        n.shape = NodeShape.PLAINTEXT;
        n.label = templateId;
        n.rawLabel = "<" + htmlTable + ">";
        return n;
    }

    /** An edge between two ports, leaving the source northwards and entering the destination from the south. */
    public VizEdge connectionEdge(String sourceId, String sourcePort, String destinationId, String destinationPort) {
        VizEdge e = new VizEdge(sourceId, destinationId);
        e.tailPort = sourcePort + ":n";
        e.headPort = destinationPort + ":s";
        e.head = headstyle;
        e.tail = tailstyle;
        return e;
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              NAMING
    ///
    //////////////////////////////////////////////////////////////////////

    public String portName(String template, String templateId, String property) {
        return template + "_" + templateId + "_" + property;
    }

    public QualifiedName qualifiedPortNameAsQn(String provAPI, String template, String templateId, String property) {
        return pf.newQualifiedName(provAPI + "/template/", template + "/" + templateId + "/" + property, "ex");
    }

    public String templateName(String template, Integer id) {
        return template + "_" + id;
    }

    public String url(String provAPI, String template, Integer id) {
        return provAPI + "/template/" + template + "/" + id + ".svg";
    }

    public static class TemplateInfo {
        public final String template;
        public final String templateId;
        public final String url;
        public final String semanticType;

        private TemplateInfo(String template, String templateId, String url, String semanticType) {
            this.template = template;
            this.templateId = templateId;
            this.url = url;
            this.semanticType = semanticType;
        }

        static public TemplateInfo of(String template, String templateId, String url, String semanticType) {
            return new TemplateInfo(template, templateId, url, semanticType);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            TemplateInfo that = (TemplateInfo) o;
            return Objects.equals(template, that.template) && Objects.equals(templateId, that.templateId) && Objects.equals(url, that.url) && Objects.equals(semanticType, that.semanticType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(template, templateId, url, semanticType);
        }

        @Override
        public String toString() {
            return "TemplateInfo{" +
                    "template='" + template + '\'' +
                    ", templateId='" + templateId + '\'' +
                    ", url='" + url + '\'' +
                    ", semanticType='" + semanticType + '\'' +
                    '}';
        }
    }
}
