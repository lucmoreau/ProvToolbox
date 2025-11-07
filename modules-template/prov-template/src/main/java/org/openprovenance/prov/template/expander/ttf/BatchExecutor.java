package org.openprovenance.prov.template.expander.ttf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

//import static org.apache.logging.log4j.core.util.FileUtils.mkdir;

public class BatchExecutor {

    private final Map<String, org.openprovenance.prov.model.ProvSerialiser> serializerMap;
    private final Map<String, org.openprovenance.prov.model.ProvDeserialiser> deserializerMap;
    static Logger logger = LogManager.getLogger(BatchExecutor.class);
    static final ProvFactory pf= org.openprovenance.prov.vanilla.ProvFactory.getFactory();
    final ObjectMapper om = new ObjectMapper();


    public BatchExecutor() {
        Pair<Map<String, ProvSerialiser>, Map<String, ProvDeserialiser>> pair=initializeSerializerDispatcher();
        serializerMap=pair.getLeft();
        deserializerMap=pair.getRight();
    }

    public BatchExecutor(Map<String, org.openprovenance.prov.model.ProvSerialiser> serializerMap,
                         Map<String, org.openprovenance.prov.model.ProvDeserialiser> deserializerMap) {
        this.serializerMap=serializerMap;
        this.deserializerMap=deserializerMap;
    }

    public Pair<Map<String, ProvSerialiser>, Map<String, ProvDeserialiser>> initializeSerializerDispatcher()  {
        Map<String, org.openprovenance.prov.model.ProvSerialiser> serializerMap=new HashMap<>();
        Map<String, org.openprovenance.prov.model.ProvDeserialiser> deserializerMap=new HashMap<>();

        try {
            Class<?> c = Class.forName("org.openprovenance.prov.notation.ProvSerialiser");
            Constructor<?> cons = c.getConstructor(ProvFactory.class);
            ProvSerialiser serialiser = (ProvSerialiser) cons.newInstance(pf);
            serializerMap.put("provn", serialiser);

            c = Class.forName("org.openprovenance.prov.dot.ProvSerialiser");
            cons = c.getConstructor(ProvFactory.class, String.class);
            ProvSerialiser serialiser2 = (ProvSerialiser) cons.newInstance(pf, "png");
            serializerMap.put("png", serialiser2);

            ProvSerialiser serialiser3 = (ProvSerialiser) cons.newInstance(pf, "svg");
            serializerMap.put("svg", serialiser3);

            c = Class.forName("org.openprovenance.prov.notation.ProvDeserialiser");
            cons = c.getConstructor(ProvFactory.class);
            deserializerMap.put("provn", (ProvDeserialiser) cons.newInstance(pf));
            return Pair.of(serializerMap, deserializerMap);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.throwing(e);
        }
        return null;
    }

    public static TemplateTasksBatch load(String configurationPath, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(new File(configurationPath), TemplateTasksBatch.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int execute(String inputBasedir, String outputBasedir, String configurationPath) {
        try {

            if (!configurationPath.startsWith("/")) {
                configurationPath = inputBasedir + "/" + configurationPath;
            }

            logger.debug("In executor " + configurationPath);

            TemplateTasksBatch templateTasksBatch = load(configurationPath, om);

            String localdir = new File(configurationPath).getParent();

            Map<String,String> variableMap=new HashMap<>();
            if (templateTasksBatch.variableMaps!=null) {
                interpretRelativeDirectoriesInVariableMaps(inputBasedir, templateTasksBatch, localdir);
                for (String variableMapPath: templateTasksBatch.variableMaps) {
                    logger.debug("loading variable map " +  variableMapPath);
                    variableMap.putAll(om.readValue(new FileInputStream(variableMapPath), Map.class));
                }
            }
            templateTasksBatch.template_path = templateTasksBatch.template_path.stream().map(s -> (s.startsWith("."))? localdir+s.substring(1):s).collect(Collectors.toList());
            return execute(templateTasksBatch, inputBasedir, outputBasedir, variableMap);
        } catch (Exception e) {
            logger.error("Error while executing", e);
            return 1;
        }
    }

    private void interpretRelativeDirectoriesInVariableMaps(String inputBasedir, TemplateTasksBatch templateTasksBatch, String localdir) {
        templateTasksBatch.variableMaps = TemplateTasksBatch.addBaseDir(inputBasedir, templateTasksBatch.variableMaps);
        templateTasksBatch.variableMaps = templateTasksBatch.variableMaps.stream().map(s -> (s.startsWith("."))? localdir +s.substring(1):s).collect(Collectors.toList());
    }


    public int execute(TemplateTasksBatch templateTasksBatch, String inputBasedir, String outputBasedir, Map<String, String> variableMap) throws IOException {
        templateTasksBatch.template_path = TemplateTasksBatch.addBaseDir(inputBasedir, templateTasksBatch.template_path);
        templateTasksBatch.bindings_path = TemplateTasksBatch.addBaseDir(inputBasedir, templateTasksBatch.bindings_path);
        templateTasksBatch.output_dir = TemplateTasksBatch.addBaseDir(outputBasedir, templateTasksBatch.output_dir);

        logger.info("template_path: " + templateTasksBatch.template_path);
        logger.info("bindings_path: " + templateTasksBatch.bindings_path);
        logger.info("output_dir: " + templateTasksBatch.output_dir);


    //    System.out.println("template_path: " + templateTasksBatch.template_path);
      //  System.out.println("bindings_path: " + templateTasksBatch.bindings_path);
      //  System.out.println("output_dir: " + templateTasksBatch.output_dir);

        // create directory

        new File(templateTasksBatch.output_dir).mkdirs();

        //mkdir(new File(templateTasksBatch.output_dir),true);

        for (ConfigTask task : templateTasksBatch.tasks) {


            logger.info("task: " + task);

            task.execute(templateTasksBatch, this, variableMap, inputBasedir);


        }


        return 0;


    }


    void exportProvenanceAsCsv(TemplateTasksBatch templateTasksBatch, ConfigTask task, List<String> loggedRecords) throws IOException {
        if (task.hasProvenance()!=null) {
            try (FileOutputStream fos = new FileOutputStream(templateTasksBatch.output_dir + "/" + task.hasProvenance())) {
                for (String csvRecord: loggedRecords) {
                    fos.write(csvRecord.getBytes(StandardCharsets.UTF_8));
                    fos.write("\n".getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }



    private FileInputStream findFileinDirs(List<String> template_path, String filename) throws FileNotFoundException {
        for (String dir: template_path) {
            File f=new File(dir + "/" + filename);
            if (f.exists()) {
                return new FileInputStream(f);
            }
        }
        throw new FileNotFoundException(filename+ " in paths " + template_path);
    }

    Pair<FileInputStream, File> findFileinDirs2(List<String> template_path, String filename) throws FileNotFoundException {
        for (String dir: template_path) {
            File f=new File(dir + "/" + filename);
            if (f.exists()) {
                return Pair.of(new FileInputStream(f),f);
            }
        }
        throw new FileNotFoundException(filename + " in paths " + template_path);
    }

    static InputStream substituteVariablesInFile(Map<String, String> variableMap, String filename) throws IOException {
        StringSubstitutor subst= new StringSubstitutor(variableMap);
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }
        subst.replaceIn(out);
        InputStream is = new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8));
        return is;
    }

    public Document deserialise(FileInputStream fileInputStream, String format) {
        try {
            return deserializerMap.get(format).deserialiseDocument(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void serialize(OutputStream fileOutputStream, String format, Document doc, boolean b) {
        serializerMap.get(format).serialiseDocument(fileOutputStream, doc, b);
    }



    public static void main(String [] args) {
        BatchExecutor executor=new BatchExecutor();
        executor.execute(args);
    }

    public void execute(String[] args) {
       // System.out.println("Executor: " + Arrays.toString(args));
       // find the position of string "-configs" in args

        int pos=0;
        boolean found=false;
        for (int i = 0; i< args.length; i++) {
            if (args[i].equals("-configs")) {
                pos=i;
                found=true;
                break;
            }
        }
        if (!found || pos>2) {
            System.out.println("Usage: Executor [input-basedir] [output-basedir] -configs <ttf1> <ttf2> ...");
            return;
        }
        String inputBaseDir;
        String outputBaseDir;
        if (pos==0) {
            inputBaseDir="";
            outputBaseDir="";
        } else if (pos==1) {
            inputBaseDir= args[0];
            outputBaseDir= args[0];
        } else  {
            inputBaseDir= args[0];
            outputBaseDir= args[1];
        }


        // for all args starting with second execute

        for (int i = pos+1; i< args.length; i++) {
            this.execute(inputBaseDir, outputBaseDir, args[i]);
        }
    }

    String getFormat(File file) throws IOException {
        String informat= file.getName();
        int lastDot=informat.lastIndexOf('.');
        if (lastDot>0) {
            informat=informat.substring(lastDot+1);
        } else {
            throw new IOException("Cannot find extension for file "+ file.getAbsolutePath());
        }
        return informat;
    }
}
