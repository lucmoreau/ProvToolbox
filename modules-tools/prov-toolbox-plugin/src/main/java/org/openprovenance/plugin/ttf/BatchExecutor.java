package org.openprovenance.plugin.ttf;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.openprovenance.prov.interop.DeserializerFunction;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.SerializerFunction;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvDeserialiser;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.model.interop.Formats;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

@Mojo(name = "execute-ttf", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class BatchExecutor extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "execute-ttf.baseDir")
    private String baseDir;
    @Parameter(property = "execute-ttf.inputBaseDir")
    private String inputBaseDir;
    @Parameter(property = "execute-ttf.outputBaseDir")
    private String outputBaseDir;


    @Parameter(property = "execute-ttf.configs", required = true)
    private List<String> configs = new ArrayList<>();

    public void executeOld() throws MojoExecutionException {
        String className= org.openprovenance.prov.template.expander.ttf.BatchExecutor.class.getName();

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
            finalList.addAll(configs);

            System.out.println("configs:=" + finalList);
            URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
            Class<?> clazz = Class.forName(className, true, loader);
            Method method = clazz.getMethod("main", String[].class);
            String[] argsArray = finalList.toArray(new String[0]);
            method.invoke(null, (Object) argsArray);  // cast is necessary to avoid varargs issues
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute class " + className, e);
        }
    }
    public void execute() throws MojoExecutionException {
        InteropFramework interop= new InteropFramework();

        Map<Formats.ProvFormat, DeserializerFunction> deserializerMap1=interop.getDeserializerMap();
        Map<String, ProvDeserialiser> deserializerMap2=new HashMap<>();
        for (Formats.ProvFormat k: deserializerMap1.keySet()) {
            deserializerMap2.put(interop.getExtensionMap().get(k),deserializerMap1.get(k).apply());
        }
        Map<Formats.ProvFormat, SerializerFunction> serializerMap1=interop.getSerializerMap();
        Map<String, ProvSerialiser> serializerMap2=new HashMap<>();
        for (Formats.ProvFormat k: serializerMap1.keySet()) {
            serializerMap2.put(interop.getExtensionMap().get(k),
                    (out, document, formatted) -> serializerMap1.get(k).apply().serialiseDocument(out,document,formatted));

        }


       // String className= org.openprovenance.prov.template.expander.ttf.BatchExecutor.class.getName();

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
            //System.out.println("className=" + className);
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
            finalList.addAll(configs);

            System.out.println("configs:=" + finalList);
            //URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
            org.openprovenance.prov.template.expander.ttf.BatchExecutor executor=new org.openprovenance.prov.template.expander.ttf.BatchExecutor( serializerMap2, deserializerMap2);
            //Class<?> clazz = Class.forName(className, true, loader);
            //Method method = clazz.getMethod("main", String[].class);
            String[] argsArray = finalList.toArray(new String[0]);
            //method.invoke(null, (Object) argsArray);  // cast is necessary to avoid varargs issues
            executor.execute(argsArray);

        } catch (DependencyResolutionRequiredException | MalformedURLException e) {
            e.printStackTrace();
            throw new MojoExecutionException("Failed to execute execute-ttf " , e);
        }
    }
}
