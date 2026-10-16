package org.openprovenance.prov.viz;

/** What a node stands for; each backend picks a shape and a default style per kind. */
public enum NodeKind {
    ENTITY, ACTIVITY, AGENT,
    /** The attribute box drawn beside an element or a qualified relation. */
    ANNOTATION,
    /** The point standing for an n-ary relation, from which its edges fan out. */
    BLANK,
    /** A node whose look is entirely given by its {@link VizNode#rawLabel}. */
    CUSTOM
}
