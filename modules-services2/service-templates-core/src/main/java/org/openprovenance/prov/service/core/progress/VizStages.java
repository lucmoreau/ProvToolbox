package org.openprovenance.prov.service.core.progress;

/**
 * Stage-name constants used by the {@code /templates/viz} pipeline when
 * reporting progress through a {@link ProgressListener}.
 *
 * <p>Names are hierarchical, dot-separated strings so other features can
 * adopt the same convention without coupling to this class.
 */
public final class VizStages {

    private VizStages() { }

    /** SQL traversal — {@code backwardtraversal_star_typed} and the recursive walk. */
    public static final String SQL        = "viz.sql";

    /** Build the PROV document from the traversal results and serialise it to DOT. */
    public static final String PROV_BUILD = "viz.prov-build";

    /** Invoke Graphviz ({@code dot -Tsvg}) and stream the SVG to the response. */
    public static final String RENDER     = "viz.render";
}
