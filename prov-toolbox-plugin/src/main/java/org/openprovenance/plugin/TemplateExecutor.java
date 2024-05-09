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
import java.util.List;

@Mojo(name = "tplexec", defaultPhase = LifecyclePhase.INTEGRATION_TEST)
public class TemplateExecutor extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    private String className=Executor.class.getName();

    @Parameter(property = "tplexec.args")
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
            URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
            Class<?> clazz = Class.forName(className, true, loader);
            Method method = clazz.getMethod("main", String[].class);
            String[] argsArray = args.toArray(new String[0]);
            method.invoke(null, (Object) argsArray);  // cast is necessary to avoid varargs issues
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute class " + className, e);
        }
    }
}
