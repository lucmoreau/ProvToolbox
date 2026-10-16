package org.openprovenance.prov.viz;

import java.util.ArrayList;
import java.util.List;

/**
 * Visual properties shared by nodes and edges. Colours and style words use graphviz's vocabulary
 * ({@code #RRGGBB} or X11 names; {@code filled}, {@code dashed}, {@code dotted}, {@code bold}, {@code invis}),
 * which is also what {@code dot:*} attributes in the data carry; other backends translate.
 */
public class Style {
    public String fill;
    public String stroke;
    public String fontColour;
    public Integer fontSize;
    public Double penWidth;
    /** Node width in inches (dot); other backends may ignore it. */
    public Double width;
    public final List<String> styles = new ArrayList<>();

    public Style style(String word) {
        if (!styles.contains(word)) styles.add(word);
        return this;
    }

    public boolean has(String word) {
        return styles.contains(word);
    }

    public Style copyFrom(Style other) {
        fill = other.fill;
        stroke = other.stroke;
        fontColour = other.fontColour;
        fontSize = other.fontSize;
        penWidth = other.penWidth;
        width = other.width;
        styles.clear();
        styles.addAll(other.styles);
        return this;
    }
}
