package org.openprovenance.prov.dot;

import org.apache.commons.io.IOUtils;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.viz.*;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** Rendering of the visualisation model in graphviz's dot notation, and its conversion to svg, png, pdf by the {@code dot} program. */
public class ProvToDot extends ProvViz implements DotProperties {

    public static final String QUALIFIED_PNG = "qualified.png";
    public static final String QUALIFIED_SVG = "qualified.svg";
    public static final String QUALIFIED_PDF = "qualified.pdf";
    /** The qualified renderings the {@code dot} program produces; see {@link org.openprovenance.prov.viz.QualifiedFormats} for all of them. */
    public static final List<String> QUALIFIED_FORMATS = List.of(QUALIFIED_PNG, QUALIFIED_SVG, QUALIFIED_PDF);
    public static final int QUALIFIED_MAX_STRING_LENGTH = QualifiedFormats.QUALIFIED_MAX_STRING_LENGTH;

    /** Serialiser for one of the {@link #QUALIFIED_FORMATS}, e.g. {@code qualified.svg} renders via {@code dot -Tsvg}. */
    public static ProvSerialiser newQualifiedSerialiser(ProvFactory pf, String qualifiedFormat) {
        if (!QUALIFIED_FORMATS.contains(qualifiedFormat)) {
            throw new IllegalArgumentException("not a qualified format: " + qualifiedFormat + ", expected one of " + QUALIFIED_FORMATS);
        }
        return new ProvSerialiser(pf, QualifiedFormats.typeOf(qualifiedFormat), QUALIFIED_MAX_STRING_LENGTH, true);
    }

    /** Registers a serialiser for every qualified format, dot's and mermaid's alike. */
    public static Map<String, org.openprovenance.prov.model.ProvSerialiser> registerQualifiedSerialisers(ProvFactory pf, Map<String, org.openprovenance.prov.model.ProvSerialiser> serializerMap) {
        return QualifiedFormats.registerQualifiedSerialisers(pf, serializerMap);
    }

    /** Set to true to have relation labels at the tail. */
    boolean tailLabel = false;

    public ProvToDot(ProvFactory pf) {
        super(pf);
    }

    public ProvToDot(ProvFactory pf, Supplier<ProvUtilities> getProvUtilities) {
        super(pf, getProvUtilities);
    }

    public static ProvToDot newProvToDot(ProvFactory pf) {
        return newProvToDot(pf, false);
    }

    public static ProvToDot newProvToDot(ProvFactory pf, List<String> exceptions) {
        return newProvToDot(pf, exceptions, false);
    }

    public static ProvToDot newProvToDot(ProvFactory pf, boolean displayAnnotations) {
        ProvToDot toDot = new ProvToDot(pf, ProvUtilitiesForTriangle::new);
        toDot.displayAnnotations(displayAnnotations);
        return toDot;
    }

    public static ProvToDot newProvToDot(ProvFactory pf, List<String> exceptions, boolean displayAnnotations) {
        ProvToDot toDot = new ProvToDot(pf, () -> new ProvUtilitiesForTriangle(exceptions));
        toDot.displayAnnotations(displayAnnotations);
        return toDot;
    }

    @Override
    public String notationExtension() {
        return "dot";
    }

    @Override public String ellipse() { return "ellipse"; }
    @Override public String rectangle() { return ACTIVITY_SHAPE; }
    @Override public String house() { return AGENT_SHAPE; }
    @Override public String note() { return ANNOTATION_SHAPE; }
    @Override public String point() { return BLANK_SHAPE; }
    @Override public String folder() { return FOLDER_SHAPE; }
    @Override public String plaintext() { return PLAINTEXT_SHAPE; }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              RUNNING DOT
    ///
    //////////////////////////////////////////////////////////////////////

    public void convert(Document graph, String dotFile, String pdfFile, String title) throws IOException {
        convert(graph, new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        @SuppressWarnings("unused")
        Process proc = runtime.exec("dot -o " + pdfFile + " -Tpdf " + dotFile);
    }

    public void convert(Document graph, String dotFile, OutputStream pdfStream, String title) throws IOException {
        convert(graph, new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("dot  -Tpdf " + dotFile);
        InputStream is = proc.getInputStream();
        IOUtils.copy(is, pdfStream);
    }

    public void convert(Document graph, String dotFile, String aFile, String type, String title) throws IOException {
        convert(graph, new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("dot -o " + aFile + " -T" + type + " " + dotFile);
        try {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String s_error = errorReader.readLine();
            if (s_error != null) {
                System.out.println("Error:  " + s_error);
            }
            proc.waitFor();
        } catch (InterruptedException ie) {
            throw new UncheckedException("convert exception", ie);
        }
    }

    /** Renders to {@code type} (svg, png, pdf, ... anything {@code dot -T} accepts; {@code dot} itself for the notation) onto {@code os}. */
    public void convert(Document graph, OutputStream os, String type, String title) {
        if (notationExtension().equals(type)) {
            convert(graph, os, title);
            return;
        }
        try {
            File dotFile = File.createTempFile("temp", ".dot");
            convert(graph, dotFile, title);
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("dot  -T" + type + " " + dotFile);
            InputStream is = proc.getInputStream();
            IOUtils.copy(is, os);
            @SuppressWarnings("unused")
            boolean resultCode = dotFile.delete();
        } catch (IOException e) {
            logger.throwing(e);
            throw new UncheckedException(e);
        }
    }

    public void convert(Document graph, String dotFile, OutputStream os, String type, String title) throws IOException {
        convert(graph, new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("dot  -T" + type + " " + dotFile);
        InputStream is = proc.getInputStream();
        IOUtils.copy(is, os);
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              DOT FORMAT GENERATION
    ///
    //////////////////////////////////////////////////////////////////////

    @Override
    public void render(VizGraph graph, PrintStream out) {
        prelude(graph, out);
        renderScope(graph, out);
        postlude(graph, out);
    }

    protected void renderScope(VizScope scope, PrintStream out) {
        for (VizItem item : scope.items) {
            if (item instanceof VizNode) {
                out.println();
                emitNode((VizNode) item, out);
            } else if (item instanceof VizEdge) {
                out.println();
                emitEdge((VizEdge) item, out);
            } else if (item instanceof VizCluster) {
                VizCluster c = (VizCluster) item;
                prelude(c, out);
                renderScope(c, out);
                postlude(c, out);
            }
        }
    }

    protected void prelude(VizGraph graph, PrintStream out) {
        out.println("digraph " + dotify(graph.title) + " { rankdir=\"" + graph.direction + "\"; ");
        if (graph.layout != null) {
            out.println("layout=\"" + graph.layout + "\"; ");
        }
    }

    protected void postlude(VizGraph ignoredGraph, PrintStream out) {
        out.println("}");
        out.close();
    }

    protected void prelude(VizCluster cluster, PrintStream out) {
        out.println("subgraph " + dotify(cluster.key) + " { ");
        if (cluster.label != null) out.println("  label=" + dotify(cluster.label) + ";");
        if (cluster.url != null) out.println("  URL=" + dotify(cluster.url) + ";");
    }

    protected void postlude(VizCluster ignoredCluster, PrintStream out) {
        out.println("}");
    }

    public void emitNode(VizNode n, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        sb.append(dotify(n.key));
        if (n.provKind != null && n.kind != NodeKind.BLANK) emitComment(n, sb);
        emitProperties(sb, nodeProperties(n));
        out.println(sb);
    }

    /** The dot attributes of a node, in a stable order. */
    public Map<String, String> nodeProperties(VizNode n) {
        Map<String, String> p = new LinkedHashMap<>();
        if (n.kind == NodeKind.ANNOTATION) {
            p.put(DOT_LABEL, annotationTable(n));
        } else if (n.kind == NodeKind.BLANK) {
            p.put(DOT_LABEL, "");
        } else if (n.rawLabel != null) {
            p.put(DOT_LABEL, n.rawLabel);
        } else if (n.label != null) {
            p.put(DOT_LABEL, n.label);
        }
        NodeShape shape = (n.shape == null) ? defaultShape(n.kind) : n.shape;
        String token = shapeToken(shape);
        p.put(DOT_SHAPE, token);
        if ("polygon".equals(token)) p.put(DOT_SIDES, ACTIVITY_SIDES);
        styleProperties(n.style, p);
        if (n.url != null) p.put(DOT_URL, htmlify(n.url));
        if (n.tooltip != null) p.put(DOT_TOOLTIP, n.tooltip);
        return p;
    }

    public void styleProperties(Style s, Map<String, String> p) {
        if (s.fill != null) p.put(DOT_FILLCOLOUR, s.fill);
        if (s.stroke != null) p.put(DOT_COLOUR, s.stroke);
        if (!s.styles.isEmpty()) p.put(DOT_STYLE, String.join(",", s.styles));
        if (s.fontColour != null) p.put(DOT_FONTCOLOUR, s.fontColour);
        if (s.fontSize != null) p.put(DOT_FONTSIZE, "" + s.fontSize);
        if (s.penWidth != null) p.put(DOT_PENWIDTH, "" + s.penWidth);
        if (s.width != null) p.put(DOT_WIDTH, "" + s.width);
    }

    /** The HTML table dot draws inside an attribute box. */
    public String annotationTable(VizNode n) {
        StringBuilder label = new StringBuilder();
        label.append("<<TABLE cellpadding=\"0\" border=\"0\">\n");
        for (VizNode.Row row : n.rows) {
            label.append("	<TR>\n");
            label.append("	    <TD align=\"left\">").append(htmlify(row.name)).append(":</TD>\n");
            label.append("	    <TD align=\"left\">").append(htmlify(truncate(row.value))).append("</TD>\n");
            label.append("	</TR>\n");
        }
        label.append("    </TABLE>>");
        return label.toString();
    }

    public void emitEdge(VizEdge e, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        sb.append(dotify(e.source));
        if (e.tailPort != null) sb.append(":").append(e.tailPort);
        sb.append(e.directed ? DOT_DIRECTED_EDGE : DOT_UNDIRECTED_EDGE);
        sb.append(dotify(e.target));
        if (e.headPort != null) sb.append(":").append(e.headPort);
        if (e.provKind != null) sb.append(" [comment=\"").append(e.provKind).append(" _:_\"]");
        emitProperties(sb, edgeProperties(e));
        out.println(sb);
    }

    public Map<String, String> edgeProperties(VizEdge e) {
        Map<String, String> p = new LinkedHashMap<>();
        if (e.label != null) relationName(e.label, p);
        if (e.head != ArrowKind.NORMAL) p.put(DOT_ARROWHEAD, e.head.dotName);
        if (e.tail != ArrowKind.NONE) {
            p.put(DOT_ARROWTAIL, e.tail.dotName);
            p.put(DOT_DIR, (e.head == ArrowKind.NONE) ? "back" : "both");
        }
        styleProperties(e.style, p);
        if (e.url != null) p.put(DOT_URL, htmlify(e.url));
        if (e.tooltip != null) p.put(DOT_TOOLTIP, e.tooltip);
        return p;
    }

    void relationName(String l, Map<String, String> properties) {
        if (tailLabel) {
            properties.put(DOT_TAILLABEL, l);
            properties.put(DOT_LABELANGLE, "60.0");
            properties.put(DOT_LABELDISTANCE, "1.0");
            properties.put(DOT_ROTATION, "20");
            properties.put(DOT_LABELFONTSIZE, "8");
        } else {
            properties.put(DOT_LABEL, l);
            properties.put(DOT_LABELFONTSIZE, "8");
        }
    }

    private void emitComment(VizNode n, StringBuilder out) {
        String prefix = (n.id == null) ? "_" : n.id.getPrefix();
        String localPart = (n.id == null) ? "_" : n.id.getLocalPart();
        out.append(" [comment=\"").append(n.provKind).append(" ").append(prefix).append(":").append(localPart).append("\"]");
    }

    public void emitProperties(StringBuilder sb, Map<String, String> properties) {
        sb.append(" [");
        boolean first = true;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            String value = entry.getValue();
            sb.append(entry.getKey());
            if (value.startsWith("<")) {
                sb.append("=");
                sb.append(value);
            } else {
                sb.append("=\"");
                sb.append(value.replace("\"", "\\\""));
                sb.append("\"");
            }
        }
        sb.append("]");
    }

    /** Make a name acceptable to dot: quoted. */
    public String dotify(String name) {
        return "\"" + name.replace("\"", "\\\"") + "\"";
    }

    public String htmlify(String name) {
        if (name == null) return "";
        return name
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
