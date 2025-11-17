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
    private String provcsv;

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
        return provcsv;
    }

    public void setProvCsv(String provcsv) {
        this.provcsv = provcsv;
    }


    public String get(TemplateExtension extension) {
        if (extension == null) {
            throw new IllegalArgumentException("Extension cannot be null");
        }
        return switch (extension) {
            case JSONLD -> getJsonld();
            case PROVN -> getProvn();
            case JSON -> getJson();
            case PNG -> getPng();
            case SVG -> getSvg();
            case PROVCSV -> getProvCsv();
        };
    }



    public String get(List<TemplateExtension> extensions) {
        return extensions.stream().map(this::get).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public void set(TemplateExtension extension, String path) {
        if (extension == null) {
            throw new IllegalArgumentException("Extension cannot be null");
        }
        switch (extension) {
            case JSONLD -> setJsonld(path);
            case PROVN -> setProvn(path);
            case PNG -> setPng(path);
            case SVG -> setSvg(path);
            case JSON -> setJson(path);
            case PROVCSV -> setProvCsv(path);
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
                ", provn='" + provn + '\'' +
                ", png='" + png + '\'' +
                ", svg='" + svg + '\'' +
                '}';
    }
}