package org.openprovenance.plugin.webjar;

import org.apache.commons.text.StringSubstitutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.openprovenance.plugin.webjar.configs.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Mojo(name = "prepare-webjar", defaultPhase = LifecyclePhase.VERIFY)
public class WebjarPreparer extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "prepare-webjar.warning", defaultValue = "false")
    private boolean warning;


    @Parameter(property = "prepare-webjar.args")
    private List<String> args = new ArrayList<>();

    @Parameter(property = "validate-catalogue.templates")
    private List<TemplateConfig> templates = new ArrayList<>();
    @Parameter(property = "validate-catalogue.bindings")
    private List<BindingsConfig> bindings = new ArrayList<>();
    @Parameter(property = "validate-catalogue.cbindings")
    private List<CBindingsConfig> cbindings = new ArrayList<>();
    @Parameter(property = "validate-catalogue.js")
    private List<JsConfig> js = new ArrayList<>();
    @Parameter(property = "validate-catalogue.ts")
    private List<TsConfig> ts = new ArrayList<>();
    @Parameter(property = "validate-catalogue.schema")
    private List<SchemaConfig> schema = new ArrayList<>();
    @Parameter(property = "validate-catalogue.css")
    private List<CssConfig> css = new ArrayList<>();
    @Parameter(property = "validate-catalogue.html")
    private List<HtmlConfig> html = new ArrayList<>();
    @Parameter(property = "validate-catalogue.sql")
    private List<SqlConfig> sql = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        try {
            //String projectVersion = project.getVersion(); // get the project version

            //System.out.println("variable value: " + project.getProperties().getProperty("project.build.directory"));

            templates.forEach(t -> applyDefaults(t,project.getProperties()));
            bindings.forEach(t -> applyDefaults(t,project.getProperties()));
            cbindings.forEach(t -> applyDefaults(t,project.getProperties()));
            js.forEach(t -> applyDefaults(t,project.getProperties()));
            ts.forEach(t -> applyDefaults(t,project.getProperties()));
            schema.forEach(t -> applyDefaults(t,project.getProperties()));
            css.forEach(t -> applyDefaults(t,project.getProperties()));
            html.forEach(t -> applyDefaults(t,project.getProperties()));
            sql.forEach(t -> applyDefaults(t,project.getProperties()));


            if (warning) System.out.println("Templates: " + templates);
            if (warning) System.out.println("Bindings: " + bindings);

            // for each template, copy the file structure under 'directory' into the target path
            for (TemplateConfig t: templates) {
                String source = t.directory;
                String target = t.targetPath;
                copyFromSourceToDestination(source, target, null);
            }
            for (BindingsConfig t: bindings) {
                String source = t.directory;
                String target = t.targetPath;
                copyFromSourceToDestination(source, target, null);
            }
            for (CBindingsConfig t: cbindings) {
                String source = t.directory;
                String target = t.targetPath;
                copyFromSourceToDestination(source, target, null);
            }
            for (JsConfig t: js) {
                String source = t.directory;
                String target = t.targetPath;
                copyFromSourceToDestination(source, target, null);
            }
            for (TsConfig t: ts) {
                String source = t.directory;
                String target = t.targetPath;
                copyFromSourceToDestination(source, target, null);
            }
            for (SchemaConfig t: schema) {
                String source = t.directory;
                String target = t.targetPath;
                List<String> includes=t.includes;
                copyFromSourceToDestination(source, target, includes);
            }
            for (CssConfig t: css) {
                String source = t.directory;
                String target = t.targetPath;
                List<String> includes=t.includes;
                copyFromSourceToDestination(source, target, null);
            }
            for (HtmlConfig t: html) {
                String source = t.directory;
                String target = t.targetPath;
                List<String> includes=t.includes;
                copyFromSourceToDestination(source, target, includes);
            }
            for (SqlConfig t: sql) {
                String source = t.directory;
                String target = t.targetPath;
                List<String> includes=t.includes;
                copyFromSourceToDestination(source, target, includes);
            }

        } catch (Throwable e) {
            throw new MojoExecutionException("Failed to execute class ", e);
        }
    }

    public void copyFromSourceToDestination(String source, String target, List<String> includes) throws IOException {
        Path sourceDir= Paths.get(source);
        Path targetDir=Paths.get(target);
        if (warning) System.out.println("Copying files from " + sourceDir + "\n  to " + targetDir);
        Files.walk(sourceDir).filter(f -> includes == null || includes.stream().anyMatch(e -> {
            // remove prefix sourceDir from f
            Path relativePath = sourceDir.relativize(f);
            String pathStr = relativePath.toString().replace("\\", "/"); // normalize to forward
            boolean matches = pathStr.matches(e);
            /*
            if (!matches) {
                System.out.println(" - Excluding file: " + f + " for pattern " + e);
            } else {
                if (warning) System.out.println(" + Including file: " + f);
            }

             */
            return matches;
        })).forEach(sourcePath -> {
            try {
                Path targetPath = targetDir.resolve(sourceDir.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                } else {
                    //if (warning) System.out.println(" * Copying to file: " + targetPath);
                    //if (warning) System.out.println(" *  from file: " + sourcePath);
                    targetPath.getParent().toFile().mkdirs();
                    Files.copy(sourcePath, targetPath, REPLACE_EXISTING);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error copying files: " + e.getMessage());
            }
        });
    }

    // write a method that updates parameters if they are null, with the default value in @Parameter declaration if it exists
    static public void applyDefaults(Object o, Properties properties) {
        //System.out.println("Applying defaults to: " + o);
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                //System.out.println("Inspecting field: " + field.getName());
                MyParameter param = field.getAnnotation(MyParameter.class);
                if (param == null) {
                    //System.out.println(field.getName() + " has no @Parameter annotation");
                    continue;
                }
                String defaultVal = param.defaultValue();
                // skip if no meaningful default provided
                if (defaultVal == null || defaultVal.isEmpty() || "##default".equals(defaultVal)) {
                    // System.out.println(field.getName() + " has no default value");
                    continue;
                }
                field.setAccessible(true);
                Object current = field.get(o);
                // check if the default value contains property references like ${...}
                defaultVal= StringSubstitutor.replace(defaultVal, properties);
                if (current == null) {
                    // set the raw default string from the annotation
                    // System.out.println("Applying default for field " + field.getName() + ": " + defaultVal);
                    field.set(o, defaultVal);
                } else {
                    //System.out.println("Field " + field.getName() + " already set to: " + current);
                }
            }
        } catch (Exception e) {
            System.out.println("Error applying defaults: " + e.getMessage());
        }
    }
}
