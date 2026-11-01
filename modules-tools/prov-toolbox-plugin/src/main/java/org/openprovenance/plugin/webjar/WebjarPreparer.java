package org.openprovenance.plugin.webjar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    @Parameter(property = "validate-catalogue.icon")
    private List<IconConfig> icon = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        try {
            String baseDir = project.getBasedir().getAbsolutePath();
            String project_build_dir=project.getBuild().getDirectory();
            String artifactId=project.getArtifactId();


            Properties properties = project.getProperties();
            Properties projectProperties = new Properties();
            projectProperties.putAll(properties);

            // add a few of the maven variables
            projectProperties.put("project.version", project.getVersion());
            projectProperties.put("project.basedir", baseDir);
            projectProperties.put("project.build.directory", project_build_dir);
            projectProperties.put("project.artifactId", artifactId);
            // other maven variables can be added as needed


            templates.forEach(t -> applyDefaults(t, projectProperties));
            bindings.forEach(t -> applyDefaults(t, projectProperties));
            cbindings.forEach(t -> applyDefaults(t, projectProperties));
            js.forEach(t -> applyDefaults(t, projectProperties));
            ts.forEach(t -> applyDefaults(t, projectProperties));
            schema.forEach(t -> applyDefaults(t, projectProperties));
            css.forEach(t -> applyDefaults(t, projectProperties));
            html.forEach(t -> applyDefaults(t, projectProperties));
            sql.forEach(t -> applyDefaults(t, projectProperties));
            icon.forEach(t -> applyDefaults(t, projectProperties));


            if (warning) System.out.println("Templates: " + templates);
            if (warning) System.out.println("Bindings: " + bindings);

            // for each template, copy the file structure under 'directory' into the target path; several template
            // directories share one target, so their index.json files are merged rather than the last one kept
            for (TemplateConfig t: templates) {
                String source = t.directory;
                String target = t.targetPath;
                copyFromSourceToDestination(source, target, null, true);
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
            for (IconConfig t: icon) {
                String source = t.directory;
                String target = t.targetPath;
                List<String> includes = t.includes;
                copyFromSourceToDestination(source, target, includes);
            }

        } catch (Throwable e) {
            throw new MojoExecutionException("Failed to execute class ", e);
        }
    }

    public void copyFromSourceToDestination(String source, String target, List<String> includes) throws IOException {
        copyFromSourceToDestination(source, target, includes, false);
    }

    /**
     * Copies the files under {@code source} into {@code target}, replacing existing ones; with {@code mergeIndex}, an
     * {@code index.json} meeting one already in the target is merged into it instead (see {@link #mergeIndex}).
     */
    public void copyFromSourceToDestination(String source, String target, List<String> includes, boolean mergeIndex) throws IOException {
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
                    if (mergeIndex && INDEX.equals(sourcePath.getFileName().toString()) && Files.exists(targetPath)) {
                        mergeIndex(sourcePath, targetPath);
                    } else {
                        Files.copy(sourcePath, targetPath, REPLACE_EXISTING);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error copying files: " + e.getMessage());
            }
        });
    }

    static final String INDEX = "index.json";

    /**
     * Merges the template index {@code source} into {@code target}: the union of their template ids. An id both name
     * with the same entry is kept once; an id they name with different entries is an error, naming both, since one of
     * the two directories would otherwise lose its template silently.
     */
    static void mergeIndex(Path source, Path target) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode into = mapper.readTree(target.toFile());
        JsonNode from = mapper.readTree(source.toFile());
        if (!(into instanceof ObjectNode) || !(from instanceof ObjectNode)) {
            throw new IOException("template index is not a JSON object: " + (into instanceof ObjectNode ? source : target));
        }
        ObjectNode merged = (ObjectNode) into;
        for (java.util.Iterator<java.util.Map.Entry<String, JsonNode>> it = from.fields(); it.hasNext(); ) {
            java.util.Map.Entry<String, JsonNode> e = it.next();
            JsonNode had = merged.get(e.getKey());
            if (had == null) {
                merged.set(e.getKey(), e.getValue());
            } else if (!had.equals(e.getValue())) {
                throw new IOException("template id " + e.getKey() + " has two entries: " + had + " in " + target
                        + ", " + e.getValue() + " in " + source);
            }
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(target.toFile(), merged);
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
