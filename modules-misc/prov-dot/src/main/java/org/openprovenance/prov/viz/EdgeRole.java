package org.openprovenance.prov.viz;

/** Why an edge is there. */
public enum EdgeRole {
    /** The arrow carrying a relation, labelled with its shorthand. */
    MAIN,
    /** Dashed line from an n-ary relation's point to one of its other causes. */
    LINK,
    /** Dashed line tying an attribute box to the statement it describes. */
    ANNOTATION
}
