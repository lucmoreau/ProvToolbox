package org.openprovenance.prov.viz;

/** What nodes and edges have in common, so hints from the data can be applied to either. */
public interface VizStyled {
    Style style();
    String url();
    void setUrl(String url);
    String tooltip();
    void setTooltip(String tooltip);
}
