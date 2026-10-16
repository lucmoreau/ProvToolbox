package org.openprovenance.prov.viz;

import org.openprovenance.prov.model.StatementOrBundle;

import java.util.LinkedHashMap;
import java.util.Map;

/** An edge between two node keys. */
public class VizEdge implements VizItem, VizStyled {
    public final String source;
    public final String target;
    public EdgeRole role = EdgeRole.MAIN;
    /** The PROV kind of the relation the edge draws, when it draws one. */
    public StatementOrBundle.Kind provKind;
    /** Plain text, not escaped. */
    public String label;
    public ArrowKind head = ArrowKind.NORMAL;
    public ArrowKind tail = ArrowKind.NONE;
    public boolean directed = true;
    /** Compass points or port names, for backends that anchor edges. */
    public String headPort;
    public String tailPort;
    public final Style style = new Style();
    public String url;
    public String tooltip;
    public final Map<String, Object> extras = new LinkedHashMap<>();

    public VizEdge(String source, String target) {
        this.source = source;
        this.target = target;
    }

    @Override public Style style() { return style; }
    @Override public String url() { return url; }
    @Override public void setUrl(String url) { this.url = url; }
    @Override public String tooltip() { return tooltip; }
    @Override public void setTooltip(String tooltip) { this.tooltip = tooltip; }

    @Override
    public String toString() {
        return "VizEdge{" + role + " " + source + " -> " + target + (label == null ? "" : " '" + label + "'") + "}";
    }
}
