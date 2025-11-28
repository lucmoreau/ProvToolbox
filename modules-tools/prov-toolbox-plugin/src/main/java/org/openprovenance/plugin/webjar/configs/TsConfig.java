package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class TsConfig {
    @Parameter  (property = "prepare-webjar.ts.directory", required = true)
    @MyParameter(property = "prepare-webjar.ts.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.ts.targetPath", defaultValue = META_INF_WEBJARS + "/ts")
    @MyParameter(property = "prepare-webjar.ts.targetPath", defaultValue = META_INF_WEBJARS + "/ts")
    public String targetPath;


    @Override
    public String toString() {
        return "TsConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                '}';
    }
}
