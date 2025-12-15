package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class BindingsConfig {
    @Parameter  (property = "prepare-webjar.bindings.directory", required = true)
    @MyParameter(property = "prepare-webjar.bindings.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.bindings.targetPath", defaultValue = META_INF_WEBJARS + "/bindings")
    @MyParameter(property = "prepare-webjar.bindings.targetPath", defaultValue = META_INF_WEBJARS + "/bindings")
    public String targetPath;


    @Override
    public String toString() {
        return "BindingsConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                '}';
    }
}
