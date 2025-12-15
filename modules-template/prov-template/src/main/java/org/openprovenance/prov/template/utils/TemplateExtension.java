package org.openprovenance.prov.template.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TemplateExtension {
    JSONLD("jsonld"),
    PROVN("provn"),
    JSON("json"),
    PNG("png"),
    SVG("svg"),
    QUALIFIED_PNG("qualified.png"),
    PROVCSV("prov-csv"),
    HAS_PROVENANCE("hasProvenance");

    private final String id;

    TemplateExtension(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    static final List<String> extensions= Arrays.stream(TemplateExtension.values()).map(Object::toString).collect(Collectors.toList());

    public static TemplateExtension from(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Extension cannot be null");
        }
        String v = s.trim().toLowerCase();
        for (TemplateExtension e : values()) {
            if (e.id.equals(v)) return e;
        }
        throw new IllegalArgumentException("Unsupported extension: " + s);
    }

    static public List<TemplateExtension> preferredExtensions() {
        return List.of(JSONLD, PROVN, JSON);
    }
}
