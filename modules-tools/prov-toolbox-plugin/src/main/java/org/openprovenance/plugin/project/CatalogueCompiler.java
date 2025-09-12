package org.openprovenance.plugin.project;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.openprovenance.prov.template.compiler.ConfigProcessor;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "compile-catalogue", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class CatalogueCompiler extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "compile-catalogue.baseDir")
    private String baseDir;
    @Parameter(property = "compile-catalogue.inputBaseDir")
    private String inputBaseDir;
    @Parameter(property = "compile-catalogue.outputBaseDir")
    private String outputBaseDir;

    @Parameter(property = "compile-catalogue.configs", required = true)
    private List<String> configs = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        String className= ConfigProcessor.class.getName();
        try {

            if ((baseDir!=null) && (inputBaseDir!=null)) {
                throw new MojoExecutionException("Both baseDir and inputBaseDir are defined");
            }
            if ((baseDir!=null) && (outputBaseDir!=null)) {
                throw new MojoExecutionException("Both baseDir and outputBaseDir are defined");
            }
            if (baseDir!=null) {
                inputBaseDir=baseDir;
                outputBaseDir=baseDir;
            }

            List<String> classpathElements = project.getCompileClasspathElements();
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); i++) {
                urls[i] = new URL("file://" + classpathElements.get(i));
            }
            //System.out.println("urls=" + urls);
            //System.out.println("configs=" + configs);





            for (String config : configs) {
                String [] argArray=new String[3];
                argArray[0]=config;
                argArray[1]=inputBaseDir;
                argArray[2]=outputBaseDir;

                URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
                Class<?> clazz = Class.forName(className, true, loader);
                Method method = clazz.getMethod("main", String[].class);
                method.invoke(null, (Object) argArray);  // cast is necessary to avoid varargs issues

            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute class ", e);
        }
    }
}
