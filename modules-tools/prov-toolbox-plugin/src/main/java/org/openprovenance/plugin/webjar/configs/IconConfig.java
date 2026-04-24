package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import java.util.List;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class IconConfig {
    @Parameter  (property = "prepare-webjar.icon.directory", required = true)
    @MyParameter(property = "prepare-webjar.icon.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.icon.targetPath", defaultValue = META_INF_WEBJARS + "/icons")
    @MyParameter(property = "prepare-webjar.icon.targetPath", defaultValue = META_INF_WEBJARS + "/icons")
    public String targetPath;

    @Parameter  (property = "prepare-webjar.icon.includes")
    public List<String> includes=new java.util.ArrayList<>();


    @Override
    public String toString() {
        return "IconConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", includes=" + includes +
                '}';
    }
}
