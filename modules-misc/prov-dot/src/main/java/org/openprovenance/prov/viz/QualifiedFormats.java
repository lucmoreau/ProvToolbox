package org.openprovenance.prov.viz;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvSerialiser;

import java.util.List;
import java.util.Map;

/**
 * The qualified renderings: every relation drawn as a node with its attribute box, values cut short.
 * {@code qualified.png}, {@code qualified.svg} and {@code qualified.pdf} come from graphviz,
 * {@code qualified.mmd} is mermaid text.
 */
public final class QualifiedFormats {
    private QualifiedFormats() {}

    public static final String QUALIFIED_PNG = "qualified.png";
    public static final String QUALIFIED_SVG = "qualified.svg";
    public static final String QUALIFIED_PDF = "qualified.pdf";
    public static final String QUALIFIED_MMD = "qualified.mmd";

    public static final List<String> DOT_FORMATS = List.of(QUALIFIED_PNG, QUALIFIED_SVG, QUALIFIED_PDF);
    public static final List<String> MERMAID_FORMATS = List.of(QUALIFIED_MMD);
    public static final List<String> ALL_FORMATS = List.of(QUALIFIED_PNG, QUALIFIED_SVG, QUALIFIED_PDF, QUALIFIED_MMD);

    /** Attribute values longer than this are truncated with an ellipsis in the qualified renderings. */
    public static final int QUALIFIED_MAX_STRING_LENGTH = 20;

    /** {@code qualified.svg} → {@code svg}. */
    public static String typeOf(String qualifiedFormat) {
        return qualifiedFormat.substring(qualifiedFormat.lastIndexOf('.') + 1);
    }

    public static ProvSerialiser newQualifiedSerialiser(ProvFactory pf, String qualifiedFormat) {
        if (DOT_FORMATS.contains(qualifiedFormat)) {
            return new org.openprovenance.prov.dot.ProvSerialiser(pf, typeOf(qualifiedFormat), QUALIFIED_MAX_STRING_LENGTH, true);
        }
        if (MERMAID_FORMATS.contains(qualifiedFormat)) {
            return new MermaidSerialiser(pf, typeOf(qualifiedFormat), QUALIFIED_MAX_STRING_LENGTH, true);
        }
        throw new IllegalArgumentException("not a qualified format: " + qualifiedFormat + ", expected one of " + ALL_FORMATS);
    }

    /** Registers a serialiser for each of the {@link #ALL_FORMATS} under its format name. */
    public static Map<String, ProvSerialiser> registerQualifiedSerialisers(ProvFactory pf, Map<String, ProvSerialiser> serializerMap) {
        for (String format : ALL_FORMATS) {
            serializerMap.put(format, newQualifiedSerialiser(pf, format));
        }
        return serializerMap;
    }
}
