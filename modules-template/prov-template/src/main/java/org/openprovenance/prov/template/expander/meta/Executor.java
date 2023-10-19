package org.openprovenance.prov.template.expander.meta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.Expand;
import org.openprovenance.prov.template.json.Bindings;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.core.util.FileUtils.mkdir;

public class Executor {

    private final Map<String, org.openprovenance.prov.model.ProvSerialiser> serializerMap=new HashMap<>();
    private final Map<String, org.openprovenance.prov.model.ProvDeserialiser> deserializerMap=new HashMap<>();
    static Logger logger = LogManager.getLogger(Executor.class);
    static private final ProvFactory pf= org.openprovenance.prov.vanilla.ProvFactory.getFactory();
    static final private ObjectMapper om = new ObjectMapper();
    private ProvDeserialiser deserialiser;


    public Executor () {
        initializeSerializerDispatcher();
    }

    public void initializeSerializerDispatcher()  {
        try {
            Class<?> c = Class.forName("org.openprovenance.prov.notation.ProvSerialiser");
            Constructor<?> cons = c.getConstructor(ProvFactory.class);
            ProvSerialiser serialiser = (ProvSerialiser) cons.newInstance(pf);
            this.serializerMap.put("provn", serialiser);

            c = Class.forName("org.openprovenance.prov.dot.ProvSerialiser");
            cons = c.getConstructor(ProvFactory.class, String.class);
            ProvSerialiser serialiser2 = (ProvSerialiser) cons.newInstance(pf, "png");
            this.serializerMap.put("png", serialiser2);

            c = Class.forName("org.openprovenance.prov.notation.ProvDeserialiser");
            cons = c.getConstructor(ProvFactory.class);
            this.deserialiser = (ProvDeserialiser) cons.newInstance(pf);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.throwing(e);
        }
    }

    public static Config load(String configurationPath, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(new File(configurationPath), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int execute(String configurationPath) {
        try {

            System.out.println("In executor " + configurationPath);

            Config config = load(configurationPath, om);

            String localdir = new File(configurationPath).getParent();

            Map<String,String> variableMap=new HashMap<>();
            if (config.variableMaps!=null) {
                for (String variableMapPath: config.variableMaps) {
                    variableMapPath=localdir + "/" + variableMapPath;

                    System.out.println("loading variable map " +  variableMapPath);
                    variableMap.putAll(om.readValue(new FileInputStream(variableMapPath), Map.class));
                }
            }
            return execute(config, variableMap);
        } catch (Exception e) {
            logger.error("Error while executing", e);
            return 1;
        }
    }


    public int execute(Config config, Map<String, String> variableMap) throws IOException {
        logger.info("mtemplate_dirs: " + config.mtemplate_dir);
        logger.info("mbindings_dir: " + config.mbindings_dir);
        logger.info("expand_dir: " + config.expand_dir);

        mkdir(new File(config.expand_dir),true);

        for (Config.ConfigTask task : config.tasks) {
            logger.info("task: " + task);

            switch (task.type) {
                case "expansion":
                    executeExpandTask(config, task, variableMap);
                    break;
                case "merge":
                    executeMergeTask(config, task, variableMap);
                    break;
                default:
                    logger.error("Unknown task type " + task.type);
                    return 1;
            }

        }


        return 0;


    }

    public void executeExpandTask(Config config, Config.ConfigTask task, Map<String, String> variableMap) throws IOException {
        InputStream is=substituteVariablesInFile(variableMap, config.mbindings_dir + "/" + task.bindings);

        Expand expand = new Expand(pf, false, false);
        List<String> mtemplate_dir = (task.mtemplate_dir==null)? config.mtemplate_dir : task.mtemplate_dir;
        Document doc=expand.expander(deserialise(findFileinDirs(mtemplate_dir, task.input)), om.readValue(is, Bindings.class));
        for (String format: task.formats) {
            serialize(new FileOutputStream(config.expand_dir + "/" + task.output + "." + format), format, doc, false);
            if (task.copyinput != null && task.copyinput) {
                serialize(new FileOutputStream(config.expand_dir + "/" + task.input.replace(".provn","."+format)), format, doc, false);
            }
        }
    }

    public void executeMergeTask(Config config, Config.ConfigTask task, Map<String, String> variableMap) throws IOException {

        Expand expand = new Expand(pf, false, false);
        List<String> mtemplate_dir = (task.mtemplate_dir==null)? config.mtemplate_dir : task.mtemplate_dir;
        Document doc1 = deserialise(findFileinDirs(mtemplate_dir, task.input));
        Document doc2 = deserialise(findFileinDirs(mtemplate_dir, task.input2));
        Document doc3=new IndexedDocument(pf,doc1,false).merge(doc2).toDocument();
        for (String format: task.formats) {
            serialize(new FileOutputStream(config.expand_dir + "/" + task.output + "." + format), format, doc3, false);
        }
        // option to clean up tmp file
        if (task.clean2!=null && task.clean2) {
            for (String format: task.formats) {
                for (String dir : mtemplate_dir) {
                    File f = new File(dir + "/" + task.input2.replace(".provn", "." + format));
                    if (f.exists()) f.delete();
                }
            }
        }
    }

    private FileInputStream findFileinDirs(List<String> mtemplate_dir, String filename) throws FileNotFoundException {
        for (String dir: mtemplate_dir) {
            File f=new File(dir + "/" + filename);
            if (f.exists()) {
                return new FileInputStream(f);
            }
        }
        throw new FileNotFoundException(filename);
    }

    private InputStream substituteVariablesInFile(Map<String, String> variableMap, String filename) throws IOException {
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

    public Document deserialise(FileInputStream fileInputStream) {
        try {
            return deserialiser.deserialiseDocument(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void serialize(OutputStream fileOutputStream, String format, Document doc, boolean b) {
        serializerMap.get(format).serialiseDocument(fileOutputStream, doc, b);
    }



    public static void main(String [] args) {
        Executor executor=new Executor();
        for (String arg: args) {
            executor.execute(arg);
        }
    }
}
