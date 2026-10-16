package org.openprovenance.prov.viz;

/** A transformation of the model between building and rendering. */
@FunctionalInterface
public interface VizPass {
    VizGraph apply(VizGraph graph);
}
