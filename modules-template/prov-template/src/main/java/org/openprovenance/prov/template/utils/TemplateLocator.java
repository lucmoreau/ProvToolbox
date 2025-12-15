package org.openprovenance.prov.template.utils;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateLocator {
    private String jsonld;
    private String json;
    private String provn;
    private String png;
    private String svg;
    private String qualifiedPng;
    private String provCsv;
    private TemplateProvenanceLocator hasProvenance;

    public TemplateLocator() { }

    public String getJsonld() {
        return jsonld;
    }

    public void setJsonld(String jsonld) {
        this.jsonld = jsonld;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getProvn() {
        return provn;
    }

    public void setProvn(String provn) {
        this.provn = provn;
    }


    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    public String getProvCsv() {
        return provCsv;
    }

    public void setProvCsv(String provCsv) {
        this.provCsv = provCsv;
    }

    public String getQualifiedPng() {
        return qualifiedPng;
    }

    public void setQualifiedPng(String qualifiedPng) {
        this.qualifiedPng = qualifiedPng;
    }

    public TemplateProvenanceLocator getHasProvenance() {
        return hasProvenance;
    }

    public void setHasProvenance(TemplateProvenanceLocator hasProvenance) {
        this.hasProvenance = hasProvenance;
    }

    public String get(TemplateExtension extension) {
        if (extension == null) {
            throw new TemplateLocatorException("get(): Extension cannot be null");
        }
        return switch (extension) {
            case JSONLD -> getJsonld();
            case PROVN -> getProvn();
            case JSON -> getJson();
            case PNG -> getPng();
            case SVG -> getSvg();
            case PROVCSV -> getProvCsv();
            case QUALIFIED_PNG -> getQualifiedPng();
            case HAS_PROVENANCE -> throw new TemplateLocatorException("get(): Cannot get HAS_PROVENANCE path from TemplateLocator");
        };
    }

    public String getProvenance(TemplateExtension extension) {
        if (extension == null) {
            throw new TemplateLocatorException("getProvenance(): Extension cannot be null");
        }
        return switch (extension) {
            case JSONLD -> getHasProvenance().getJsonld();
            case PROVN -> getHasProvenance().getProvn();
            case JSON -> getHasProvenance().getJson();
            case PNG -> getHasProvenance().getPng();
            case SVG -> getHasProvenance().getSvg();
            case PROVCSV -> getHasProvenance().getProvCsv();
            case QUALIFIED_PNG -> getHasProvenance().getQualifiedPng();
            case HAS_PROVENANCE -> throw new TemplateLocatorException("getProvenance(): Cannot get HAS_PROVENANCE path from TemplateLocator");
        };
    }


    public String get(List<TemplateExtension> extensions) {
        return extensions.stream().map(this::get).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public void set(TemplateExtension extension, String path) {
        if (extension == null) {
            throw new TemplateLocatorException("Extension cannot be null");
        }
        switch (extension) {
            case JSONLD -> setJsonld(path);
            case PROVN -> setProvn(path);
            case PNG -> setPng(path);
            case SVG -> setSvg(path);
            case JSON -> setJson(path);
            case PROVCSV -> setProvCsv(path);
            case QUALIFIED_PNG -> setQualifiedPng(path);
            case HAS_PROVENANCE -> throw new TemplateLocatorException("Cannot set HAS_PROVENANCE path in TemplateLocator");
        }
    }


    public void setProvenance(TemplateExtension extension, String path) {
        if (extension == null) {
            throw new TemplateLocatorException("setProvenance(): Extension cannot be null");
        }
        if (getHasProvenance() == null) {
            setHasProvenance(new TemplateProvenanceLocator());
        }
        switch (extension) {
            case JSONLD -> getHasProvenance().setJsonld(path);
            case PROVN -> getHasProvenance().setProvn(path);
            case PNG -> getHasProvenance().setPng(path);
            case SVG -> getHasProvenance().setSvg(path);
            case JSON -> getHasProvenance().setJson(path);
            case PROVCSV -> getHasProvenance().setProvCsv(path);
            case QUALIFIED_PNG -> getHasProvenance().setQualifiedPng(path);
            case HAS_PROVENANCE -> throw new TemplateLocatorException("Cannot set HAS_PROVENANCE path in TemplateLocator");
        }
    }

    // Backwards-compatible string-based methods delegate to enum-based ones
    public String get(String format) {
        return get(TemplateExtension.from(format));
    }

    public void set(String format, String path) {
        set(TemplateExtension.from(format), path);
    }

    @Override
    public String toString() {
        return "TemplateLocator{" +
                "jsonld='" + jsonld + '\'' +
                ", json='" + json + '\'' +
                ", provn='" + provn + '\'' +
                ", png='" + png + '\'' +
                ", svg='" + svg + '\'' +
                ", qualifiedPng='" + qualifiedPng + '\'' +
                ", provCsv='" + provCsv + '\'' +
                ", hasProvenance=" + hasProvenance +
                '}';
    }
}