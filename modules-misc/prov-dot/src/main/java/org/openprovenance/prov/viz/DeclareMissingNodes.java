package org.openprovenance.prov.viz;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A relation may name an element the document never declares. Graphviz would invent a bare node
 * for it; other backends need it declared. This pass adds a placeholder for every edge end no
 * node declares, labelled with the local part of its name.
 */
public class DeclareMissingNodes implements VizPass {

    @Override
    public VizGraph apply(VizGraph graph) {
        Map<String, VizNode> index = graph.nodeIndex();
        List<VizEdge> edges = graph.allEdges().collect(Collectors.toList());
        for (VizEdge e : edges) {
            declare(graph, index, e.source);
            declare(graph, index, e.target);
        }
        return graph;
    }

    private void declare(VizGraph graph, Map<String, VizNode> index, String key) {
        if (index.containsKey(key)) return;
        VizNode n = new VizNode(key, NodeKind.CUSTOM);
        n.shape = NodeShape.ELLIPSE;
        n.label = localPart(key);
        n.url = key;
        n.extras.put("placeholder", Boolean.TRUE);
        graph.add(n);
        index.put(key, n);
    }

    static String localPart(String uri) {
        int i = uri.lastIndexOf('#');
        int j = uri.lastIndexOf('/');
        return uri.substring(Math.max(i, j) + 1);
    }
}
