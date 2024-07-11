package org.openprovenance.plugin.toolbox;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openprovenance.prov.interop.CommandLineArguments;

@Mojo(name = "provconvert", defaultPhase = LifecyclePhase.INTEGRATION_TEST)
public class ProvToolboxExecutor extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    private String className=CommandLineArguments.class.getName();

    @Parameter(property = "provconvert.args")
    private List<String> args = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        try {
            List<String> classpathElements = project.getCompileClasspathElements();
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); i++) {
                urls[i] = new URL("file://" + classpathElements.get(i));
            }
            System.out.println("urls=" + urls);
            System.out.println("className=" + className);
            System.out.println("args=" + args);

            // is it necessary to load class dynamically? why not call it directly?

            //URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
            //Class<?> clazz = Class.forName(className, true, loader);
            //Method method = clazz.getMethod("mainNoExit", String[].class);
            String[] argsArray = args.toArray(new String[0]);
            CommandLineArguments.mainNoExit(argsArray);

            //method.invoke(null, (Object) argsArray);  // cast is necessary to avoid varargs issues

        } catch (Throwable e) {
            throw new MojoExecutionException("Failed to execute class " + className, e);
        }
    }
}
