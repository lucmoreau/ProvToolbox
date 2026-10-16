package org.openprovenance.prov.viz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A container of nodes, edges and nested clusters: the graph itself, or a bundle drawn as a cluster.
 * Items keep their declaration order, which layout engines use to break ties; passes may reorder them.
 */
public abstract class VizScope {
    public final List<VizItem> items = new ArrayList<>();

    public VizNode add(VizNode n) {
        items.add(n);
        return n;
    }

    public VizEdge add(VizEdge e) {
        items.add(e);
        return e;
    }

    public VizCluster add(VizCluster c) {
        items.add(c);
        return c;
    }

    public List<VizNode> nodes() {
        return items.stream().filter(VizNode.class::isInstance).map(VizNode.class::cast).collect(Collectors.toList());
    }

    public List<VizEdge> edges() {
        return items.stream().filter(VizEdge.class::isInstance).map(VizEdge.class::cast).collect(Collectors.toList());
    }

    public List<VizCluster> clusters() {
        return items.stream().filter(VizCluster.class::isInstance).map(VizCluster.class::cast).collect(Collectors.toList());
    }

    /** This scope's nodes, then those of nested clusters, depth first. */
    public Stream<VizNode> allNodes() {
        return Stream.concat(nodes().stream(), clusters().stream().flatMap(VizScope::allNodes));
    }

    /** Every edge, in the order backends draw them: a scope's items in order, clusters expanded in place. */
    public Stream<VizEdge> allEdges() {
        return items.stream().flatMap(i -> {
            if (i instanceof VizEdge) return Stream.of((VizEdge) i);
            if (i instanceof VizCluster) return ((VizCluster) i).allEdges();
            return Stream.empty();
        });
    }

    public Stream<VizCluster> allClusters() {
        return clusters().stream().flatMap(c -> Stream.concat(Stream.of(c), c.allClusters()));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
