/**
 * Visualisation of PROV documents, in two steps: {@link org.openprovenance.prov.viz.ProvViz} turns a
 * document into a {@link org.openprovenance.prov.viz.VizGraph} — nodes, edges, clusters, attribute boxes,
 * with the qualified-relation logic — which {@link org.openprovenance.prov.viz.VizPass}es may transform,
 * and a backend renders in its notation: {@link org.openprovenance.prov.dot.ProvToDot} for graphviz,
 * {@link org.openprovenance.prov.viz.ProvToMermaid} for mermaid.
 */
package org.openprovenance.prov.viz;
