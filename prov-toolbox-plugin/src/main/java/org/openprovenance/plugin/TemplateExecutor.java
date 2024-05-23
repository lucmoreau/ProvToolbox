package org.openprovenance.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.openprovenance.prov.template.expander.meta.Executor;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Mojo(name = "process-templates", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class TemplateExecutor extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "process-templates.baseDir")
    private String baseDir;
    @Parameter(property = "process-templates.inputBaseDir")
    private String inputBaseDir;
    @Parameter(property = "process-templates.outputBaseDir")
    private String outputBaseDir;


    @Parameter(property = "process-templates.args", required = true)
    private List<String> args = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        String className=Executor.class.getName();

        try {
            if ((baseDir!=null) && (inputBaseDir!=null)) {
                throw new MojoExecutionException("Both baseDir and inputBaseDir are defined");
            }
            if ((baseDir!=null) && (outputBaseDir!=null)) {
                throw new MojoExecutionException("Both baseDir and outputBaseDir are defined");
            }

            List<String> classpathElements = project.getCompileClasspathElements();
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); i++) {
                urls[i] = new URL("file://" + classpathElements.get(i));
            }
            System.out.println("urls=" + urls);
            System.out.println("className=" + className);
            List<String> finalList=new LinkedList<>();
            if (baseDir!=null) {
                finalList.add(baseDir);
            } else {
                if (outputBaseDir==null) outputBaseDir="";
                if (inputBaseDir==null) inputBaseDir="";
                finalList.add(inputBaseDir);
                finalList.add(outputBaseDir);
            }
            finalList.add("-configs");
            finalList.addAll(args);

            System.out.println("args:=" + finalList);
            URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
            Class<?> clazz = Class.forName(className, true, loader);
            Method method = clazz.getMethod("main", String[].class);
            String[] argsArray = finalList.toArray(new String[0]);
            method.invoke(null, (Object) argsArray);  // cast is necessary to avoid varargs issues
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute class " + className, e);
        }
    }
}
