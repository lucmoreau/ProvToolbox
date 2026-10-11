package org.openprovenance.prov.dot;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.viz.EdgeRole;
import org.openprovenance.prov.viz.ProvUtilitiesForTriangle;
import org.openprovenance.prov.viz.VizEdge;
import org.openprovenance.prov.viz.VizGraph;
import org.openprovenance.prov.viz.VizItem;
import org.openprovenance.prov.viz.VizScope;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The qualified rendering, laid out so that an invalidation reads properly: the
 * serialiser behind the {@code qualified-inv.*} formats.
 *
 * dot takes the ranks from the edges: in a {@code rankdir=BT} graph an edge
 * {@code X -> Y} asks for Y a rank above X. Two things then push an
 * invalidation's point far from the relation it belongs to, leaving its edge to
 * wrap around the drawing to reach the activity:
 *
 * <ol>
 * <li>The dashed links from an n-ary relation's point to its other causes are
 *     edges like any other, so they constrain the ranks too. They say "this
 *     statement is part of that one", not "this is above that", and an
 *     attribution's point can thus drag an invalidation's point a rank above
 *     itself. Here they are drawn without constraining the ranks.</li>
 * <li>An invalidation is emitted as {@code entity -> point -> activity}, which
 *     asks for the point above the entity; but an activity that {@code used}
 *     the entity is already below it, and dot breaks the resulting cycle
 *     wherever suits the rest of the layout. Here the two halves are emitted
 *     the way the layout runs, {@code activity -> point -> entity}, which puts
 *     the point between the two where it belongs. The arrow ends do not move:
 *     the open diamond stays on the entity and the arrowhead stays on the
 *     activity, entering it from below (the {@code s} port), so that the
 *     invalidation reads as a consequence of the activity, not a premise.</li>
 * </ol>
 *
 * Both rules hold for any qualified rendering. They are a separate format
 * rather than a change to {@link ProvToDot} so that drawings already reviewed
 * keep the layout they were reviewed with; see {@code QualifiedFormats}.
 */
public class ProvToDotForInvalidation extends ProvToDot {

    public ProvToDotForInvalidation(ProvFactory pf, Supplier<ProvUtilities> getProvUtilities) {
        super(pf, getProvUtilities);
    }

    /** As {@code ProvToDot.newProvToDot(pf, displayAnnotations)}, with the layout above. */
    public static ProvToDotForInvalidation newProvToDotForInvalidation(ProvFactory pf, boolean displayAnnotations) {
        ProvToDotForInvalidation toDot =
                new ProvToDotForInvalidation(pf, ProvUtilitiesForTriangle::new);
        toDot.displayAnnotations(displayAnnotations);
        return toDot;
    }

    /** As {@code ProvToDot.newProvToDot(pf, exceptions, displayAnnotations)}, with the layout above. */
    public static ProvToDotForInvalidation newProvToDotForInvalidation(ProvFactory pf, List<String> exceptions,
                                                                       boolean displayAnnotations) {
        ProvToDotForInvalidation toDot =
                new ProvToDotForInvalidation(pf, () -> new ProvUtilitiesForTriangle(exceptions));
        toDot.displayAnnotations(displayAnnotations);
        return toDot;
    }

    /** A link to another cause is an annotation: it must not move the nodes. */
    @Override
    public Map<String, String> edgeProperties(VizEdge e) {
        Map<String, String> properties = super.edgeProperties(e);
        if (e.role == EdgeRole.LINK) properties.put("constraint", "false");
        return properties;
    }

    @Override
    public void render(VizGraph graph, PrintStream out) {
        reseatInvalidations(graph);
        super.render(graph, out);
    }

    /** Re-emit each invalidation's two half-edges in the direction the layout runs. */
    private void reseatInvalidations(VizScope scope) {
        List<VizItem> items = scope.items;
        for (int i = 0; i < items.size(); i++) {
            VizItem item = items.get(i);
            if (item instanceof VizScope) {
                reseatInvalidations((VizScope) item);
            } else if (item instanceof VizEdge) {
                VizEdge e = (VizEdge) item;
                if (e.provKind == StatementOrBundle.Kind.PROV_INVALIDATION && e.role == EdgeRole.MAIN) {
                    items.set(i, reversed(e));
                }
            }
        }
    }

    /**
     * The same edge drawn from the other end: the arrow that was at the tail becomes the
     * arrowhead and vice versa, so the picture is unchanged and only the rank direction moves.
     * The half that carries the relation's label is the one that reaches the activity; it now
     * leaves the activity, and leaves it from below.
     */
    private VizEdge reversed(VizEdge e) {
        VizEdge r = new VizEdge(e.target, e.source);
        r.role = e.role;
        r.provKind = e.provKind;
        r.label = e.label;
        r.head = e.tail;
        r.tail = e.head;
        r.directed = e.directed;
        r.headPort = e.tailPort;
        r.tailPort = e.headPort;
        r.style.copyFrom(e.style);
        r.url = e.url;
        r.tooltip = e.tooltip;
        r.extras.putAll(e.extras);
        if (r.label != null) {
            r.tailPort = "s";
            r.headPort = null;
        }
        return r;
    }
}
