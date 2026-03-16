package org.openprovenance.plugin.ttf;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.ProvDeserialiser;
import org.openprovenance.prov.model.ProvSerialiser;

import java.nio.file.Path;
import java.util.*;

@Mojo(name = "execute-ttf", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class BatchExecutor extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "validate-ttf.debug", defaultValue = "false")
    private boolean debug;

    @Parameter(property = "execute-ttf.warning", defaultValue = "false")
    private boolean warning;

    @Parameter(property = "execute-ttf.inputBaseDir")
    private String inputBaseDir;

    @Parameter(property = "execute-ttf.outputBaseDir")
    private String outputBaseDir;


    @Parameter(property = "execute-ttf.args", required = true)
    private List<String> args = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        InteropFramework interop= new InteropFramework();

        Map<String, ProvDeserialiser> deserializerMap2=new HashMap<>();
        Map<String, ProvSerialiser> serializerMap2=new HashMap<>();


        interop.populateSerializerDeserializerMaps(deserializerMap2, serializerMap2);

        org.openprovenance.prov.dot.ProvSerialiser dotSerial=new org.openprovenance.prov.dot.ProvSerialiser(org.openprovenance.prov.vanilla.ProvFactory.getFactory(), "png", 20, true);
        serializerMap2.put("qualified.png", dotSerial);

        if (debug) {
            getLog().info(getClass().getName());
        }


        // String className= org.openprovenance.prov.template.expander.ttf.BatchExecutor.class.getName();

        try {
            if ((inputBaseDir==null) || (inputBaseDir.isEmpty())) {
                throw new MojoExecutionException("BatchExecutor: inputBaseDir not defined");
            }
            if ((outputBaseDir==null) || (outputBaseDir.isEmpty())) {
                throw new MojoExecutionException("BatchExecutor: outputBaseDir not defined");
            }

            if (debug  && (args==null || args.isEmpty())) {
                getLog().info("BatchExecutor args is empty");
            }

            List<String> finalList=new LinkedList<>();

            finalList.add(inputBaseDir);
            finalList.add(outputBaseDir);

            finalList.add("-configs");
            Path inputBasePath=java.nio.file.Paths.get(inputBaseDir);
            for (String path: args) {
                finalList.add(inputBasePath.resolve(path).toString());
            }

            if (debug) {
                getLog().info("BatchExecutor args: " + finalList);
            }


            org.openprovenance.prov.template.core.ttf.BatchExecutor executor=new org.openprovenance.prov.template.core.ttf.BatchExecutor( serializerMap2, deserializerMap2);
            //Class<?> clazz = Class.forName(className, true, loader);
            //Method method = clazz.getMethod("main", String[].class);
            String[] argsArray = finalList.toArray(new String[0]);
            //method.invoke(null, (Object) argsArray);  // cast is necessary to avoid varargs issues
            executor.execute(argsArray);

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new MojoExecutionException("Failed to execute execute-ttf " , e);
        }
    }


}
