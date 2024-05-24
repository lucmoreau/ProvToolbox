package org.openprovenance.plugin;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.openprovenance.prov.template.compiler.CompilerSQL;


import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "validate-template-project", defaultPhase = LifecyclePhase.INTEGRATION_TEST)
public class TemplateProjectSchemaValidator extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "validate-template-project.warning", defaultValue = "false")
    private boolean warning;


    @Parameter(property = "validate-template-project.args")
    private List<String> args = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        try {
            List<String> classpathElements = project.getCompileClasspathElements();
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); i++) {
                urls[i] = new URL("file://" + classpathElements.get(i));
            }
            //System.out.println("urls=" + urls);
            //System.out.println("args=" + args);

            // copy file f to a temporary file with name openprovenance-template-project-schema-${project.version}.json
            String projectVersion = project.getVersion(); // get the project version
            String tempFileName = "openprovenance-template-project-schema-" + projectVersion + ".json"; // create the temp file name

            // create a path for the temp file in the system's default temporary-file directory
            Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), tempFileName);

            IOUtils.copy(CompilerSQL.class.getClassLoader().getResourceAsStream("schema/template-project-schema.json"), Files.newOutputStream(tempFilePath));

            for (String arg : args) {

                // call command line executable ajv with argument f.getAbsolutePath()
                ProcessBuilder pb = new ProcessBuilder("ajv", "validate", "-s", tempFilePath.toString(), "-d", arg);
                pb.inheritIO();
                Process p = pb.start();
                int exitCode = p.waitFor();
                if (exitCode != 0) {
                    if (warning) {
                        getLog().warn("Failed to validate " + arg);
                    } else {
                        throw new MojoExecutionException("Failed to validate " + arg);
                    }
                }


            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute class ", e);
        }
    }
}
