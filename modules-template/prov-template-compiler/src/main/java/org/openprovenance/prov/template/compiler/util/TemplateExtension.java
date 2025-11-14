package org.openprovenance.prov.template.compiler.util;

import java.util.List;

public enum TemplateExtension {
    JSONLD("jsonld"),
    PROVN("provn"),
    JSON("json"),
    PNG("png"),
    SVG("svg"),
    PROVCSV("prov-csv");

    private final String id;

    TemplateExtension(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

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
