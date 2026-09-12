package org.openprovenance.prov.viz;

/** See <a href="https://www.w3.org/2011/prov/wiki/Diagrams">https://www.w3.org/2011/prov/wiki/Diagrams</a> for the list of properties that can be used. */
public interface RecommendedProvVisualProperties {
    String ACTIVITY_FILL_COLOUR = "#9FB1FC";
    String ACTIVITY_COLOUR = "#0000FF";
    String ACTIVITY_STYLE = "filled";
    String ENTITY_STYLE = "filled";
    String ENTITY_FILLCOLOUR = "#FFFC87";
    String ENTITY_COLOUR = "#808080";
    String AGENT_STYLE = "filled";
    String AGENT_FILLCOLOUR = "#FDB266";
    /** Attribute boxes, and the dashed link tying them to their statement. */
    String ANNOTATION_COLOUR = "gray";
    String ANNOTATION_FONT_COLOUR = "black";
    /** Boxes and links of qualified relations: the id box, and the dashed lines to the other causes. */
    String QUALIFIED_COLOUR = "chocolate4";
}
