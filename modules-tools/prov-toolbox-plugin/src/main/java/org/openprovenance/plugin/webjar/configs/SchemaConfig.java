package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import java.util.List;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class SchemaConfig {
    @Parameter  (property = "prepare-webjar.schema.directory", required = true)
    @MyParameter(property = "prepare-webjar.schema.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.schema.targetPath", defaultValue = META_INF_WEBJARS + "/schema")
    @MyParameter(property = "prepare-webjar.schema.targetPath", defaultValue = META_INF_WEBJARS + "/schema")
    public String targetPath;

    @Parameter  (property = "prepare-webjar.schema.includes")
    public List<String> includes=new java.util.ArrayList<>();


    @Override
    public String toString() {
        return "SchemaConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", includes=" + includes +
                '}';
    }
}
