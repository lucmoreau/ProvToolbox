package org.openprovenance.prov.viz;

/**
 * The shapes of the PROV diagram conventions (<a href="https://www.w3.org/2011/prov/wiki/Diagrams">prov wiki</a>),
 * named after graphviz's; each backend says what it draws for them ({@link ProvViz#ellipse()} and siblings).
 */
public enum NodeShape {
    /** Entities. */
    ELLIPSE,
    /** Activities. */
    RECTANGLE,
    /** Agents. */
    HOUSE,
    /** Attribute boxes. */
    NOTE,
    /** The point standing for an n-ary relation. */
    POINT,
    /** Dictionaries. */
    FOLDER,
    /** No shape at all, the label is the node. */
    PLAINTEXT
}
