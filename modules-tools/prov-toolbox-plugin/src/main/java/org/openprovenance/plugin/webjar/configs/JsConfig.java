package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class JsConfig {
    @Parameter  (property = "prepare-webjar.js.directory", required = true)
    @MyParameter(property = "prepare-webjar.js.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.js.targetPath", defaultValue = META_INF_WEBJARS + "/js")
    @MyParameter(property = "prepare-webjar.js.targetPath", defaultValue = META_INF_WEBJARS + "/js")
    public String targetPath;


    @Override
    public String toString() {
        return "JsConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                '}';
    }
}
