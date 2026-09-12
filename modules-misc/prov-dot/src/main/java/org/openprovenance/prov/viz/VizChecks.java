package org.openprovenance.prov.viz;

import java.util.*;

/** Invariants a well-formed model satisfies; the list of violations is empty for a good graph. */
public class VizChecks {

    public static List<String> violations(VizGraph graph) {
        return violations(graph, true);
    }

    /** @param requireDeclaredEndpoints false for backends that, like graphviz, invent a node for an undeclared edge end */
    public static List<String> violations(VizGraph graph, boolean requireDeclaredEndpoints) {
        List<String> out = new ArrayList<>();
        Set<String> keys = new HashSet<>();
        graph.allNodes().forEach(n -> {
            keys.add(n.key);
            if (n.kind == NodeKind.ANNOTATION && n.rows.isEmpty()) out.add("empty attribute box: " + n.key);
            if (n.kind != NodeKind.BLANK && n.kind != NodeKind.ANNOTATION && n.label == null && n.rawLabel == null)
                out.add("node without label: " + n.key);
            if (n.shape == null) out.add("node without shape: " + n.key);
        });
        if (requireDeclaredEndpoints) {
            graph.allEdges().forEach(e -> {
                if (!keys.contains(e.source)) out.add("edge from undeclared node: " + e);
                if (!keys.contains(e.target)) out.add("edge to undeclared node: " + e);
            });
        }
        return out;
    }

    /** A pass that refuses a graph violating an invariant. */
    public static VizPass asPass() {
        return graph -> {
            List<String> v = violations(graph);
            if (!v.isEmpty()) throw new IllegalStateException("ill-formed visualisation model: " + v);
            return graph;
        };
    }
}
