package org.openprovenance.plugin.webjar.configs;

import org.apache.maven.plugins.annotations.Parameter;
import org.openprovenance.plugin.webjar.MyParameter;


public class TemplateConfig {
    public static final String META_INF_WEBJARS="${project.build.directory}/classes/META-INF/resources/webjars/${project.artifactId}/${project.version}";

    @Parameter  (property = "prepare-webjar.template.directory", required = true)
    @MyParameter(property = "prepare-webjar.template.directory", required = true)
    public String directory;

    @Parameter  (property = "prepare-webjar.template.targetPath", defaultValue = META_INF_WEBJARS + "/templates")
    @MyParameter(property = "prepare-webjar.template.targetPath", defaultValue = META_INF_WEBJARS + "/templates")
    public String targetPath;

    @Override
    public String toString() {
        return "TemplateConfig{" +
                "directory='" + directory + '\'' +
                ", targetPath='" + targetPath + '\'' +
                '}';
    }
}
