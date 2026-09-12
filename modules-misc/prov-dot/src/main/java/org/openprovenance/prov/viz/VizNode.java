package org.openprovenance.prov.viz;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** A node of the visualisation model: an element, an attribute box, or the point of an n-ary relation. */
public class VizNode implements VizItem, VizStyled {

    /** One line of an attribute box. */
    public static class Row {
        public final String name;
        public final String value;

        public Row(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    /** Unique within the graph, and what edges refer to: the URI of an element, a generated name otherwise. */
    public final String key;
    public NodeKind kind;
    public NodeShape shape;
    /** The element's name; null for generated nodes. */
    public QualifiedName id;
    /** The PROV kind of the statement, when the node stands for one. */
    public StatementOrBundle.Kind provKind;
    /** Plain text, not escaped. */
    public String label;
    /** A label in the backend's own syntax, used verbatim instead of {@link #label} by backends that understand it. */
    public String rawLabel;
    public final List<Row> rows = new ArrayList<>();
    public final Style style = new Style();
    public String url;
    public String tooltip;
    /** Anything a subclass or a pass wants to carry to its renderer. */
    public final Map<String, Object> extras = new LinkedHashMap<>();

    public VizNode(String key, NodeKind kind) {
        this.key = key;
        this.kind = kind;
    }

    @Override public Style style() { return style; }
    @Override public String url() { return url; }
    @Override public void setUrl(String url) { this.url = url; }
    @Override public String tooltip() { return tooltip; }
    @Override public void setTooltip(String tooltip) { this.tooltip = tooltip; }

    @Override
    public String toString() {
        return "VizNode{" + kind + " " + key + (label == null ? "" : " '" + label + "'") + (rows.isEmpty() ? "" : " rows=" + rows.size()) + "}";
    }
}
