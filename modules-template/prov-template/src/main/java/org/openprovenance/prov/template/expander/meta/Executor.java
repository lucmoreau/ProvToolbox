package org.openprovenance.prov.template.expander.meta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.model.ProvDeserialiser;
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


            if (task.type.equals("expansion")) {
                logger.info("expanding " + task.input);
                logger.info("using bindings " + task.bindings);
                logger.info("output " + task.output);
                logger.info("formats " + task.formats);

                executeExpandTask(config, task, variableMap);
            } else {
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
        Document doc=expand.expander(deserialise(findFileinDirs(task, mtemplate_dir)), om.readValue(is, Bindings.class));
        for (String format: task.formats) {
            serialize(new FileOutputStream(config.expand_dir + "/" + task.output + "." + format), format, doc, false);
            if (task.copyinput != null && task.copyinput) {
                serialize(new FileOutputStream(config.expand_dir + "/" + task.input.replace(".provn","."+format)), format, doc, false);
            }
        }
    }

    private FileInputStream findFileinDirs(Config.ConfigTask task, List<String> mtemplate_dir) throws FileNotFoundException {
        for (String dir: mtemplate_dir) {
            File f=new File(dir + "/" + task.input);
            if (f.exists()) {
                return new FileInputStream(f);
            }
        }
        throw new FileNotFoundException(task.input);
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
