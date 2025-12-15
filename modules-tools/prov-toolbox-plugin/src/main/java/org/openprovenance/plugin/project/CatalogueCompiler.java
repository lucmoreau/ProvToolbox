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


    @Parameter(property = "compile-catalogue.inputBaseDir")
    private String inputBaseDir;
    @Parameter(property = "compile-catalogue.outputBaseDir")
    private String outputBaseDir;
    @Parameter(property = "compile-catalogue.cbindingsBaseDir")
    private String cbindingsBaseDir;
    @Parameter(property = "compile-catalogue.templateLibraries", required = true)
    private List<String> templateLibrary =new ArrayList<>();


    @Parameter(property = "compile-catalogue.configs", required = true)
    private List<String> configs = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        String className= ConfigProcessor.class.getName();
        try {

            if (inputBaseDir==null) {
                throw new MojoExecutionException("Input Basedir is null");
            }
            if (outputBaseDir==null) {
                throw new MojoExecutionException("Output Basedir is null");
            }

            if (cbindingsBaseDir==null) {
                throw new MojoExecutionException("CBindings Basedir is null");
            }
            if (templateLibrary.isEmpty()) {
                throw new MojoExecutionException("Template Library Path is empty");
            }

            List<String> classpathElements = project.getCompileClasspathElements();
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); i++) {
                urls[i] = new URL("file://" + classpathElements.get(i));
            }
            //System.out.println("urls=" + urls);
            //System.out.println("configs=" + configs);





            for (String config : configs) {
                String [] argArray=new String[5];
                argArray[0]=config;
                argArray[1]=inputBaseDir;
                argArray[2]=cbindingsBaseDir;
                argArray[3]=outputBaseDir;
                // make a single string from the list of template library paths, separated by :
                argArray[4]=String.join(":", templateLibrary);


                URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
                Class<?> clazz = Class.forName(className, true, loader);
                Method method = clazz.getMethod("main", String[].class);
                method.invoke(null, (Object) argArray);  // cast is necessary to avoid varargs issues

            }
        } catch (Throwable e) {
            throw new MojoExecutionException("\nCatalogueCompiler: Failed to compile catalogue\n", e);
        }
    }
}
