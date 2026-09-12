package org.openprovenance.prov.viz;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.exception.UncheckedException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Rendering of the visualisation model as a mermaid flowchart (v11 syntax), and its conversion to svg,
 * png or pdf by the {@code mmdc} program when it is installed.
 */
public class ProvToMermaid extends ProvViz {

    public static final String NOTATION_EXTENSION = "mmd";

    /** The mermaid-cli executable; the {@code MMDC} environment variable overrides the default {@code mmdc}. */
    private String mmdc = System.getenv().getOrDefault("MMDC", "mmdc");

    /** Side, in pixels, of the viewport mmdc lays the chart out in: a png or a fitted pdf is trimmed to the chart,
     *  but a chart wider than the viewport (800px by default) is shrunk to fit it. */
    private int viewport = 20000;

    public ProvToMermaid setViewport(int viewport) {
        this.viewport = viewport;
        return this;
    }

    /** Whether a house is drawn as an inline svg image — mermaid has no house shape — or as the trapezoid {@link #house()} names. */
    private boolean houseAsImage = true;

    public ProvToMermaid setHouseAsImage(boolean houseAsImage) {
        this.houseAsImage = houseAsImage;
        return this;
    }

    /** Whether an attribute box is linked from its statement (the box then lies on the cause side of the statement, where
     *  dagre leaves it clear of the statement's other edges) or, as dot has it, links to it. */
    private boolean statementLinksToBox = true;

    public ProvToMermaid setStatementLinksToBox(boolean statementLinksToBox) {
        this.statementLinksToBox = statementLinksToBox;
        return this;
    }

    /** Font of the attribute boxes: fixed-width, so that padding with spaces aligns the values. */
    private String annotationFont = "monospace";

    public ProvToMermaid setAnnotationFont(String annotationFont) {
        this.annotationFont = annotationFont;
        return this;
    }

    /** Width, in characters, of the name column of an attribute box, colon included: {@code label: } and {@code url:   } align. */
    private int minNameWidth = 7;

    public ProvToMermaid setMinNameWidth(int minNameWidth) {
        this.minNameWidth = minNameWidth;
        return this;
    }

    /** Space between a node's text and its border, for every node: mermaid has no per-node padding, and its default 15 leaves an attribute box mostly white. */
    private int nodePadding = 8;

    public ProvToMermaid setNodePadding(int nodePadding) {
        this.nodePadding = nodePadding;
        return this;
    }

    /** Background mmdc paints behind the drawing (its own default is transparent for svg). */
    private String backgroundColour = "white";

    /** Bundles are drawn as subgraphs; without this mermaid fills them with its theme's pale yellow. */
    private String clusterCss = "fill:#FFFFFF,stroke:#000000";

    /** Labels shorter than this are padded with non-breaking spaces on both sides: a two-letter stadium is otherwise a circle. */
    private int minLabelLength = 6;

    public ProvToMermaid setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
        return this;
    }

    public ProvToMermaid setClusterCss(String clusterCss) {
        this.clusterCss = clusterCss;
        return this;
    }

    public ProvToMermaid setMinLabelLength(int minLabelLength) {
        this.minLabelLength = minLabelLength;
        return this;
    }

    /** From a node, the name its mermaid identifier is derived from; made unique by suffixing. */
    private Function<VizNode, String> identifierBase = ProvToMermaid::localNameBase;

    public ProvToMermaid(ProvFactory pf) {
        super(pf);
        addPass(new DeclareMissingNodes());
    }

    public ProvToMermaid(ProvFactory pf, Supplier<ProvUtilities> getProvUtilities) {
        super(pf, getProvUtilities);
        addPass(new DeclareMissingNodes());
    }

    public static ProvToMermaid newProvToMermaid(ProvFactory pf) {
        return newProvToMermaid(pf, false);
    }

    public static ProvToMermaid newProvToMermaid(ProvFactory pf, boolean displayAnnotations) {
        ProvToMermaid m = new ProvToMermaid(pf, ProvUtilitiesForTriangle::new);
        m.displayAnnotations(displayAnnotations);
        return m;
    }

    public static ProvToMermaid newProvToMermaid(ProvFactory pf, List<String> exceptions, boolean displayAnnotations) {
        ProvToMermaid m = new ProvToMermaid(pf, () -> new ProvUtilitiesForTriangle(exceptions));
        m.displayAnnotations(displayAnnotations);
        return m;
    }

    public void setMmdc(String mmdc) {
        this.mmdc = mmdc;
    }

    /** Replace the rule deriving mermaid identifiers from nodes (uniqueness is still ensured afterwards). */
    public void setIdentifierPolicy(Function<VizNode, String> identifierBase) {
        this.identifierBase = identifierBase;
    }

    @Override
    public String notationExtension() {
        return NOTATION_EXTENSION;
    }

    @Override public String ellipse() { return "stadium"; }
    @Override public String rectangle() { return "rect"; }
    @Override public String house() { return "trap-b"; }
    @Override public String note() { return "tag-rect"; }
    @Override public String point() { return "f-circ"; }
    @Override public String folder() { return "docs"; }
    @Override public String plaintext() { return "text"; }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              RUNNING MMDC
    ///
    //////////////////////////////////////////////////////////////////////

    /** Renders to {@code type}: {@code mmd} for the notation itself, otherwise svg, png or pdf via {@code mmdc}. */
    public void convert(Document doc, OutputStream os, String type, String title) {
        if (NOTATION_EXTENSION.equals(type)) {
            convert(doc, os, title);
            return;
        }
        try {
            Path in = Files.createTempFile("temp", "." + NOTATION_EXTENSION);
            Path out = Files.createTempFile("temp", "." + type);
            try {
                convert(doc, in.toFile(), title);
                renderWithMmdc(in, out);
                Files.copy(out, os);
            } finally {
                Files.deleteIfExists(in);
                Files.deleteIfExists(out);
            }
        } catch (IOException e) {
            logger.throwing(e);
            throw new UncheckedException(e);
        }
    }

    /** Runs {@code mmdc -i in -o out}; the output type follows the extension of {@code out}. */
    public void renderWithMmdc(Path in, Path out) throws IOException {
        List<String> command = new ArrayList<>(List.of(mmdc, "-q", "-b", backgroundColour,
                "-w", String.valueOf(viewport), "-H", String.valueOf(viewport), "-i", in.toString(), "-o", out.toString()));
        // without it a pdf is a full page with the chart in a corner
        if (out.toString().endsWith(".pdf")) command.add("--pdfFit");
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        String output = new String(proc.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        try {
            int code = proc.waitFor();
            if (code != 0) {
                throw new UncheckedException(mmdc + " failed (" + code + "): " + output, new IOException(output));
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new UncheckedException("convert exception", ie);
        }
    }

    /** Whether {@code mmdc} can be run here. */
    public boolean mmdcAvailable() {
        try {
            Process proc = new ProcessBuilder(mmdc, "--version").redirectErrorStream(true).start();
            proc.getInputStream().readAllBytes();
            return proc.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              MERMAID GENERATION
    ///
    //////////////////////////////////////////////////////////////////////

    /** Mermaid identifiers: unique, safe, and readable when the policy allows. */
    public class Identifiers {
        private final Map<String, String> byKey = new LinkedHashMap<>();
        private final Set<String> taken = new HashSet<>();

        public String declare(VizNode n) {
            return byKey.computeIfAbsent(n.key, k -> unique(sanitise(identifierBase.apply(n))));
        }

        public String declareCluster(VizCluster c) {
            return byKey.computeIfAbsent(c.key, k -> unique(sanitise("cluster_" + (c.label == null ? c.key : c.label))));
        }

        /** The identifier of a key an edge refers to; a key no node declared gets one derived from itself. */
        public String of(String key) {
            String id = byKey.get(key);
            if (id != null) return id;
            id = unique(sanitise(DeclareMissingNodes.localPart(key)));
            byKey.put(key, id);
            return id;
        }

        private String unique(String base) {
            String id = base;
            int i = 2;
            while (taken.contains(id)) id = base + "_" + (i++);
            taken.add(id);
            return id;
        }
    }

    static final Set<String> RESERVED = Set.of("end", "subgraph", "graph", "flowchart", "style", "class", "classDef",
            "click", "linkStyle", "direction", "default", "call", "href", "interpolate");

    /** The default policy: the local part of the node's name, or its key for generated nodes. */
    public static String localNameBase(VizNode n) {
        if (n.id != null) return n.id.getLocalPart();
        return n.key.startsWith("-") ? n.key.substring(1) : DeclareMissingNodes.localPart(n.key);
    }

    /** Letters, digits and underscores only, starting with a letter or underscore, never a mermaid keyword. */
    public static String sanitise(String base) {
        String s = base.replaceAll("[^A-Za-z0-9_]", "_");
        if (s.isEmpty() || !(Character.isLetter(s.charAt(0)) || s.charAt(0) == '_')) s = "n_" + s;
        if (RESERVED.contains(s)) s = s + "_";
        return s;
    }

    /** Text mermaid shows: entity codes for its own punctuation, line breaks as {@code <br/>}. */
    public static String escapeText(String text) {
        if (text == null) return "";
        return text
                .replace("#", "#35;")
                .replace("&", "#amp;")
                .replace("\"", "#quot;")
                .replace("<", "#lt;")
                .replace(">", "#gt;")
                .replace("\n", "<br/>");
    }

    /** Text inside the quoted label of a {@code @{ shape: ..., label: "..." }} block, which mermaid reads as YAML: backslashes escaped. */
    public static String escapeLabel(String text) {
        return escapeText(text).replace("\\", "\\\\");
    }

    /** Text between the bars of an edge label. */
    public static String escapeEdgeLabel(String text) {
        return escapeText(text).replace("|", "#124;");
    }

    static final Map<String, String> X11_TO_CSS = Map.ofEntries(
            Map.entry("chocolate4", "#8B4513"),
            Map.entry("gray", "#808080"),
            Map.entry("grey", "#808080"),
            Map.entry("lightgray", "#D3D3D3"),
            Map.entry("lightgrey", "#D3D3D3"),
            Map.entry("lightgreen", "#90EE90"),
            Map.entry("lightblue", "#ADD8E6"),
            Map.entry("orange", "#FFA500"),
            Map.entry("transparent", "transparent"));

    /** Graphviz colour names that CSS does not know, as hex; anything else passes through. */
    public static String cssColour(String colour) {
        if (colour == null) return null;
        return X11_TO_CSS.getOrDefault(colour, colour);
    }

    /** The class default of each kind, so per-node style lines are emitted only when a node departs from it. */
    protected Style classStyle(NodeKind kind) {
        Style s = new Style();
        switch (kind) {
            case ENTITY -> { s.fill = ENTITY_FILLCOLOUR; s.stroke = ENTITY_COLOUR; s.style(ENTITY_STYLE); }
            case ACTIVITY -> { s.fill = ACTIVITY_FILL_COLOUR; s.stroke = ACTIVITY_COLOUR; s.style(ACTIVITY_STYLE); }
            case AGENT -> { s.fill = AGENT_FILLCOLOUR; s.stroke = "#000000"; s.style(AGENT_STYLE); }
            case ANNOTATION -> { s.fill = "#FFFFFF"; s.stroke = ANNOTATION_COLOUR; s.fontColour = ANNOTATION_FONT_COLOUR; s.fontSize = 10; }
            case BLANK -> { s.fill = "#333333"; s.stroke = "#333333"; }
            case CUSTOM -> { s.fill = "#FFFFFF"; s.stroke = "#000000"; }
        }
        return s;
    }

    protected String className(NodeKind kind) {
        return kind.name().toLowerCase();
    }

    /** CSS for a node or an edge. */
    public String css(Style s, boolean node) {
        List<String> css = new ArrayList<>();
        if (node) {
            if (s.fill != null) css.add("fill:" + cssColour(s.fill));
            if (s.stroke != null) css.add("stroke:" + cssColour(s.stroke));
            if (s.fontColour != null) css.add("color:" + cssColour(s.fontColour));
            if (s.fontSize != null) css.add("font-size:" + s.fontSize + "px");
        } else {
            if (s.stroke != null) css.add("stroke:" + cssColour(s.stroke));
        }
        if (s.penWidth != null) css.add("stroke-width:" + s.penWidth + "px");
        else if (s.has("bold")) css.add("stroke-width:3px");
        if (s.has("dotted")) css.add("stroke-dasharray:2 2");
        else if (s.has("dashed") && node) css.add("stroke-dasharray:5 2");
        if (s.has("invis")) css.add("opacity:0");
        return String.join(",", css);
    }

    protected static class Output {
        final PrintStream out;
        final String direction;
        final StringBuilder trailer = new StringBuilder();
        final Map<NodeKind, List<String>> classMembers = new EnumMap<>(NodeKind.class);
        int edgeIndex = 0;

        Output(PrintStream out, String direction) {
            this.out = out;
            this.direction = direction;
        }
    }

    @Override
    public void render(VizGraph graph, PrintStream out) {
        Identifiers ids = new Identifiers();
        graph.allNodes().forEach(ids::declare);
        Output o = new Output(out, graph.direction);

        if (graph.title != null) out.println("%% " + graph.title.replace("\n", " "));
        out.println("%%{init: {\"flowchart\": {\"padding\": " + nodePadding + "}}}%%");
        out.println("flowchart " + graph.direction);
        renderScope(graph, ids, o, "    ");

        for (NodeKind kind : NodeKind.values()) {
            String css = css(classStyle(kind), true);
            // an attribute box reads as dot's table did: rows flush left in a fixed-width font, so the padded names make a column
            if (kind == NodeKind.ANNOTATION) css += (css.isEmpty() ? "" : ",") + "text-align:left,font-family:" + annotationFont;
            if (!css.isEmpty()) out.println("    classDef " + className(kind) + " " + css);
        }
        for (Map.Entry<NodeKind, List<String>> entry : o.classMembers.entrySet()) {
            out.println("    class " + String.join(",", entry.getValue()) + " " + className(entry.getKey()));
        }
        out.print(o.trailer);
        out.flush();
    }

    protected void renderScope(VizScope scope, Identifiers ids, Output o, String indent) {
        for (VizItem item : scope.items) {
            if (item instanceof VizNode) {
                emitNode((VizNode) item, ids, o, indent);
            } else if (item instanceof VizEdge) {
                emitEdge((VizEdge) item, ids, o, indent);
            } else if (item instanceof VizCluster) {
                VizCluster c = (VizCluster) item;
                String cid = ids.declareCluster(c);
                o.out.println(indent + "subgraph " + cid + " [\"" + escapeText(c.label == null ? "" : c.label) + "\"]");
                // a subgraph without its own direction is laid out top-down whatever the flowchart says
                o.out.println(indent + "    direction " + o.direction);
                renderScope(c, ids, o, indent + "    ");
                o.out.println(indent + "end");
                if (clusterCss != null && !clusterCss.isEmpty()) o.trailer.append("    style ").append(cid).append(" ").append(clusterCss).append("\n");
                if (c.url != null) o.trailer.append("    click ").append(cid).append(" href \"").append(c.url).append("\"\n");
            }
        }
    }

    public String nodeLabel(VizNode n) {
        if (n.kind == NodeKind.ANNOTATION) {
            // names padded to one width so the values line up in a second column, as in dot's table; boxes of short
            // names share the minimum width, so their columns line up across boxes too
            int width = Math.max(minNameWidth, n.rows.stream().mapToInt(r -> r.name.length()).max().orElse(0) + 1);
            List<String> lines = new ArrayList<>();
            for (VizNode.Row row : n.rows) {
                String name = row.name + ":";
                lines.add(escapeLabel(name) + "#nbsp;".repeat(Math.max(1, width - name.length() + 1)) + escapeLabel(truncate(row.value)));
            }
            return String.join("<br/>", lines);
        }
        if (n.kind == NodeKind.BLANK) return " ";
        return padLabel(escapeLabel(n.label == null ? "" : n.label), n.label == null ? 0 : n.label.length());
    }

    /** Non-breaking spaces either side of a short label, up to {@link #minLabelLength}; plain spaces mermaid would collapse. */
    public String padLabel(String escaped, int length) {
        int missing = minLabelLength - length;
        if (missing <= 0) return escaped;
        int left = missing / 2;
        int right = missing - left;
        return "#nbsp;".repeat(left) + escaped + "#nbsp;".repeat(right);
    }

    public void emitNode(VizNode n, Identifiers ids, Output o, String indent) {
        String id = ids.declare(n);
        NodeShape shape = (n.shape == null) ? defaultShape(n.kind) : n.shape;
        if (shape == NodeShape.HOUSE && houseAsImage) {
            emitHouse(n, id, o, indent);
        } else {
            o.out.println(indent + id + "@{ shape: " + shapeToken(shape) + ", label: \"" + nodeLabel(n) + "\" }");
            o.classMembers.computeIfAbsent(n.kind, k -> new ArrayList<>()).add(id);
            String css = css(n.style, true);
            if (!css.isEmpty() && !css.equals(css(classStyle(n.kind), true))) {
                o.trailer.append("    style ").append(id).append(" ").append(css).append("\n");
            }
        }
        if (n.url != null && n.kind != NodeKind.BLANK) {
            o.trailer.append("    click ").append(id).append(" href \"").append(n.url).append("\"");
            if (n.tooltip != null) o.trailer.append(" \"").append(escapeText(n.tooltip)).append("\"");
            o.trailer.append("\n");
        }
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              HOUSES
    ///
    //////////////////////////////////////////////////////////////////////

    /** Height of a house image, and of its roof, in pixels; the label's font is the flowchart's, 16px. */
    public int houseHeight = 44;
    public int roofHeight = 14;
    public int houseFontSize = 16;
    /** Width per character of the label, an estimate of the flowchart font's average advance. */
    public double houseCharWidth = 8.5;

    /** An agent as an image node showing a house with its label inside, mermaid's own frame around the image made invisible. */
    public void emitHouse(VizNode n, String id, Output o, String indent) {
        String label = n.label == null ? "" : truncate(n.label);
        int width = (int) Math.max(60, Math.ceil(houseCharWidth * label.length()) + 24);
        String fill = n.style.fill != null ? cssColour(n.style.fill) : AGENT_FILLCOLOUR;
        String stroke = n.style.stroke != null ? cssColour(n.style.stroke) : "#000000";
        String svg = houseSvg(label, width, houseHeight, fill, stroke, n.style.fontColour != null ? cssColour(n.style.fontColour) : "#000000");
        String uri = "data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));
        o.out.println(indent + id + "@{ img: \"" + uri + "\", w: " + width + ", h: " + houseHeight + ", constraint: \"on\", label: \"\", pos: \"b\" }");
        o.trailer.append("    style ").append(id).append(" fill:none,stroke:none\n");
    }

    public String houseSvg(String label, int width, int height, String fill, String stroke, String fontColour) {
        double mid = width / 2.0;
        String points = "1," + (height - 1) + " 1," + roofHeight + " " + mid + ",1 " + (width - 1) + "," + roofHeight + " " + (width - 1) + "," + (height - 1);
        double baseline = (height + roofHeight) / 2.0 + houseFontSize * 0.35;
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + width + "\" height=\"" + height + "\" viewBox=\"0 0 " + width + " " + height + "\">"
                + "<polygon points=\"" + points + "\" fill=\"" + fill + "\" stroke=\"" + stroke + "\" stroke-width=\"1\"/>"
                + "<text x=\"" + mid + "\" y=\"" + baseline + "\" text-anchor=\"middle\" fill=\"" + fontColour + "\""
                + " font-family=\"trebuchet ms,verdana,arial,sans-serif\" font-size=\"" + houseFontSize + "\">" + xmlEscape(label) + "</text></svg>";
    }

    static String xmlEscape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    /** Mermaid has no arrow tails and few heads: an edge is an arrow or a line, solid or dashed. */
    public String connector(VizEdge e) {
        boolean dashed = e.style.has("dashed") || e.style.has("dotted");
        boolean arrow = e.directed && e.head != ArrowKind.NONE;
        if (dashed) return arrow ? "-.->" : "-.-";
        return arrow ? "-->" : "---";
    }

    public void emitEdge(VizEdge e, Identifiers ids, Output o, String indent) {
        boolean flip = statementLinksToBox && e.role == EdgeRole.ANNOTATION;
        StringBuilder sb = new StringBuilder(indent);
        sb.append(ids.of(flip ? e.target : e.source)).append(" ").append(connector(e));
        if (e.label != null && !e.label.isEmpty()) sb.append("|").append(escapeEdgeLabel(e.label)).append("|");
        sb.append(" ").append(ids.of(flip ? e.source : e.target));
        o.out.println(sb);
        String css = css(e.style, false);
        if (!css.isEmpty()) {
            o.trailer.append("    linkStyle ").append(o.edgeIndex).append(" ").append(css).append("\n");
        }
        o.edgeIndex++;
    }
}
