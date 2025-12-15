package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import java.util.List;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class CssConfig {
    @Parameter  (property = "prepare-webjar.css.directory", required = true)
    @MyParameter(property = "prepare-webjar.css.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.css.targetPath", defaultValue = META_INF_WEBJARS + "/css")
    @MyParameter(property = "prepare-webjar.css.targetPath", defaultValue = META_INF_WEBJARS + "/css")
    public String targetPath;

    @Parameter  (property = "prepare-webjar.css.includes")
    public List<String> includes=new java.util.ArrayList<>();

    @Override
    public String toString() {
        return "CssConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", includes=" + includes +
                '}';
    }
}
