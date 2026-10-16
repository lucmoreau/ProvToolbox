package org.openprovenance.prov.service.core;

import java.util.Map;

/**
 * The two bounds of a closure walk, as the navigator and the slicer request them: the
 * depth cap ({@code max_depth} of the traversal functions) and the node budget
 * ({@code max_nodes}).
 *
 * <p>They arrive in the free-form {@code parameters} map of a viz or slice request, under
 * {@link #MAX_DEPTH} and {@link #MAX_NODES}.  A key that is absent takes the navigator's
 * default ({@link #DEFAULT_MAX_DEPTH} hops, {@link #DEFAULT_MAX_NODES} nodes — Graphviz
 * cannot lay out a closure of hundreds of thousands of nodes, so the interactive walk is
 * bounded where the SQL functions' own node default is none); the value {@code none} or a
 * blank value lifts that bound; any other value must be a positive integer.  Only parsed
 * integers ever reach the SQL text, which is built by concatenation.</p>
 */
public final class TraversalBounds {

    private final Integer maxDepth;
    private final Integer maxNodes;

    /** @param maxDepth the depth cap, null for none
     *  @param maxNodes the node budget, null for none */
    public TraversalBounds(Integer maxDepth, Integer maxNodes) {
        this.maxDepth = maxDepth;
        this.maxNodes = maxNodes;
    }

    /** The depth cap, null for none. */
    public Integer maxDepth() {
        return maxDepth;
    }

    /** The node budget, null for none. */
    public Integer maxNodes() {
        return maxNodes;
    }

    /** Parameter key of the depth cap. */
    public static final String MAX_DEPTH = "max-depth";

    /** Parameter key of the node budget. */
    public static final String MAX_NODES = "max-nodes";

    /** The navigator's depth cap when none is requested — the traversal functions' own default. */
    public static final int DEFAULT_MAX_DEPTH = 100;

    /** The navigator's node budget when none is requested (the SQL functions' own default is none). */
    public static final int DEFAULT_MAX_NODES = 2000;

    /** The value that lifts a bound. */
    public static final String NONE = "none";

    /**
     * Reads the bounds from a request's parameters.
     *
     * @throws IllegalArgumentException when a value is neither {@code none}, blank, nor a positive integer
     */
    public static TraversalBounds fromParameters(Map<String, String> parameters) {
        return new TraversalBounds(
                parse(parameters, MAX_DEPTH, DEFAULT_MAX_DEPTH),
                parse(parameters, MAX_NODES, DEFAULT_MAX_NODES));
    }

    private static Integer parse(Map<String, String> parameters, String key, int dflt) {
        if (parameters == null || !parameters.containsKey(key)) {
            return dflt;
        }
        String value = parameters.get(key);
        if (value == null || value.isBlank() || NONE.equalsIgnoreCase(value.trim())) {
            return null;
        }
        try {
            int n = Integer.parseInt(value.trim());
            if (n < 1) {
                throw new IllegalArgumentException(key + " must be a positive integer or '" + NONE + "', got " + n);
            }
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a positive integer or '" + NONE + "', got '" + value + "'");
        }
    }

    /** The two trailing SQL arguments of a bounded traversal call: {@code max_depth, max_nodes}. */
    public String sqlArguments() {
        return sql(maxDepth) + ", " + sql(maxNodes);
    }

    private static String sql(Integer n) {
        return n == null ? "NULL" : Integer.toString(n);
    }

    /** The bounds in words, e.g. {@code "depth 100, 2,000 nodes"}. */
    public String describe() {
        return (maxDepth == null ? "no depth limit" : "depth " + String.format("%,d", maxDepth))
                + ", "
                + (maxNodes == null ? "no node limit" : String.format("%,d", maxNodes) + " nodes");
    }

    /**
     * The notice shown when a bound cut the walk.
     *
     * @param slice whether the walk was a slice, whose cut side may or may not have hidden a path
     */
    public String cutMessage(boolean slice) {
        return slice
                ? "The traversal bounds (" + describe() + ") cut one side of this slice, so it may miss paths. "
                  + "Raise the bounds, or leave them empty, to see more."
                : "The traversal bounds (" + describe() + ") cut this graph: the closure is larger. "
                  + "Raise the bounds, or leave them empty, to see more.";
    }
}
