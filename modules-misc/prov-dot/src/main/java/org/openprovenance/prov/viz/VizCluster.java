package org.openprovenance.prov.viz;

import org.openprovenance.prov.model.QualifiedName;

/** A bundle, drawn as a box around its contents. */
public class VizCluster extends VizScope implements VizItem {
    public final String key;
    public QualifiedName id;
    public String label;
    public String url;

    public VizCluster(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "VizCluster{" + key + " items=" + items.size() + "}";
    }
}
