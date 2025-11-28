package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;

import java.util.List;

import static org.openprovenance.plugin.webjar.configs.TemplateConfig.META_INF_WEBJARS;

public class SqlConfig {
    @Parameter  (property = "prepare-webjar.sql.directory", required = true)
    @MyParameter(property = "prepare-webjar.sql.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.sql.targetPath", defaultValue = META_INF_WEBJARS + "/sql")
    @MyParameter(property = "prepare-webjar.sql.targetPath", defaultValue = META_INF_WEBJARS + "/sql")
    public String targetPath;

    @Parameter  (property = "prepare-webjar.sql.includes")
    public List<String> includes=new java.util.ArrayList<>();

    @Override
    public String toString() {
        return "SqlConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", includes=" + includes +
                '}';
    }
}
