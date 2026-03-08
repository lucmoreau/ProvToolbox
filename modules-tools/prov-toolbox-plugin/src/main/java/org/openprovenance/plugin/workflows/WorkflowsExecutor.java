package org.openprovenance.plugin.workflows;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.openprovenance.prov.interop.CommandLineArguments;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mojo(name = "generate-workflows", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class WorkflowsExecutor extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;


    @Parameter(property = "clazzName", required = true)
    private String clazzName;

    @Parameter(property = "args")
    private List<String> args = new ArrayList<>();

    @Parameter(property = "directory", defaultValue = "${project.build.directory}/classes/")
    private String directory;


    public void execute() throws MojoExecutionException {
        try {

            List<String> classpathElements = project.getCompileClasspathElements();

            classpathElements.add(directory);
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); i++) {
                urls[i] = new URL("file://" + classpathElements.get(i));
            }
            System.out.println("urls=" + Arrays.toString(urls));
            System.out.println("className=" + clazzName);
            System.out.println("args=" + args);

            new CommandLineArguments(); // to load the class and its static fields

            URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
            Class<?> clazz = Class.forName(clazzName, true, loader);
            Method method = clazz.getMethod("main", String[].class);
            String[] argsArray = args.toArray(new String[0]);

            method.invoke(null, (Object) argsArray);  // cast is necessary to avoid varargs issues

        } catch (Throwable e) {
            e.printStackTrace();
            throw new MojoExecutionException("Failed to execute class " + clazzName, e);
        }
    }
}
