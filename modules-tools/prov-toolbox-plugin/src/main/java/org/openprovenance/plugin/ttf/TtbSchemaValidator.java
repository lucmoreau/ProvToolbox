package org.openprovenance.plugin.ttf;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.openprovenance.prov.template.core.Instantiater;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/** Validation of a Template Tasks File (TTF) */

@Mojo(name = "validate-ttf", defaultPhase = LifecyclePhase.INTEGRATION_TEST)
public class TtbSchemaValidator extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "validate-ttf.debug", defaultValue = "false")
    private boolean debug;

    @Parameter(property = "validate-ttf.warning", defaultValue = "false")
    private boolean warning;

    @Parameter(property = "validate-ttf.inputBaseDir")
    private String inputBaseDir;

    @Parameter(property = "validate-ttf.args")
    private List<String> args = new ArrayList<>();

    public void execute() throws MojoExecutionException {

        if (debug) {
            getLog().info(getClass().getName());
        }

        if ((inputBaseDir==null) || (inputBaseDir.isEmpty())) {
            throw new MojoExecutionException("Validate TTF: inputBaseDir is not defined " + args);
        }
        try {
            List<String> classpathElements = project.getCompileClasspathElements();
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); i++) {
                urls[i] = new URL("file://" + classpathElements.get(i));
            }

            // copy file f to a temporary file with name openprovenance-ttf-schema-${project.version}.json
            String projectVersion = project.getVersion(); // get the project version
            String tempFileName = "openprovenance-ttf-schema-" + projectVersion + ".json"; // create the temp file name

            // create a path for the temp file in the system's default temporary-file directory
            Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), tempFileName);

            IOUtils.copy(Instantiater.class.getClassLoader().getResourceAsStream("jsonschema/template-transformation-file-schema.json"), Files.newOutputStream(tempFilePath));
            Path inputBasePath=java.nio.file.Paths.get(inputBaseDir);

            for (String arg : args) {
                Path absolutePath=inputBasePath.resolve(arg);
                // check file exists
                if (!Files.exists(absolutePath)) {
                    if (warning) {
                        getLog().warn("File does not exist: " + absolutePath);
                        continue;
                    } else {
                        throw new MojoExecutionException("File does not exist: " + absolutePath);
                    }
                }


                // call command line executable ajv with argument f.getAbsolutePath()
                ProcessBuilder pb = new ProcessBuilder("ajv", "validate", "-s", tempFilePath.toString(), "-d", absolutePath.toString());
                pb.inheritIO();
                Process p = pb.start();
                int exitCode = p.waitFor();
                if (exitCode != 0) {
                    if (warning) {
                        getLog().warn("Failed to validate " + absolutePath);
                    } else {
                        throw new MojoExecutionException("Failed to validate " + absolutePath);
                    }
                }


            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute class ", e);
        }
    }
}
