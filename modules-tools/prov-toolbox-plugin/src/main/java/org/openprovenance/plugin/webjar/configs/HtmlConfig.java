package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import java.util.List;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class HtmlConfig {
    @Parameter  (property = "prepare-webjar.html.directory", required = true)
    @MyParameter(property = "prepare-webjar.html.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.html.targetPath", defaultValue = META_INF_WEBJARS + "/html")
    @MyParameter(property = "prepare-webjar.html.targetPath", defaultValue = META_INF_WEBJARS + "/html")
    public String targetPath;

    @Parameter  (property = "prepare-webjar.html.includes")
    public List<String> includes=new java.util.ArrayList<>();

    @Override
    public String toString() {
        return "HtmlConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", includes=" + includes +
                '}';
    }
}
