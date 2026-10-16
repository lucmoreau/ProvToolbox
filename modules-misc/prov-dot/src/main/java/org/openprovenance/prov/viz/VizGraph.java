package org.openprovenance.prov.viz;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** The whole visualisation: what {@link ProvViz#build} produces, passes transform, and a backend renders. */
public class VizGraph extends VizScope {
    public String title;
    /** Rank direction, in graphviz's vocabulary: BT, TB, LR, RL. */
    public String direction = "BT";
    /** Layout engine hint (dot, sfdp, ...); null for the backend's default. */
    public String layout;

    public VizGraph(String title) {
        this.title = title;
    }

    public Optional<VizNode> findNode(String key) {
        return allNodes().filter(n -> n.key.equals(key)).findFirst();
    }

    /** Nodes by key; the first declaration wins when a key is declared twice. */
    public Map<String, VizNode> nodeIndex() {
        return allNodes().collect(Collectors.toMap(n -> n.key, Function.identity(), (a, b) -> a));
    }

    @Override
    public String toString() {
        return "VizGraph{'" + title + "' nodes=" + allNodes().count() + " edges=" + allEdges().count() + " clusters=" + allClusters().count() + "}";
    }
}
