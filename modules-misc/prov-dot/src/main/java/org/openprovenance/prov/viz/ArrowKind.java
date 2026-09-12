package org.openprovenance.prov.viz;

/** Arrow ends, named after graphviz's arrow shapes; backends without a shape fall back to plain arrows or none. */
public enum ArrowKind {
    NONE("none"),
    NORMAL("normal"),
    /** Open triangle. */
    ONORMAL("onormal"),
    /** Open inverted triangle (start). */
    OINV("oinv"),
    /** Open diamond (end, invalidation). */
    ODIAMOND("odiamond"),
    EMPTY("empty"),
    INVEMPTY("invempty");

    public final String dotName;

    ArrowKind(String dotName) {
        this.dotName = dotName;
    }
}
