package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class CBindingsConfig {
    @Parameter  (property = "prepare-webjar.cbindings.directory", required = true)
    @MyParameter(property = "prepare-webjar.cbindings.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.cbindings.targetPath", defaultValue = META_INF_WEBJARS + "/cbindings")
    @MyParameter(property = "prepare-webjar.cbindings.targetPath", defaultValue = META_INF_WEBJARS + "/cbindings")
    public String targetPath;


    @Override
    public String toString() {
        return "CBindingsConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                '}';
    }
}
