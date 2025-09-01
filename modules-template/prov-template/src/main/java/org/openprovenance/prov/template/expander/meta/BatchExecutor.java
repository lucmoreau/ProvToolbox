package org.openprovenance.prov.template.expander.meta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.Expand;
import org.openprovenance.prov.template.json.Bindings;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_expandingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_expandingBuilder;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mergingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mergingBuilder;

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
    static private final ProvFactory pf= org.openprovenance.prov.vanilla.ProvFactory.getFactory();
    static final private ObjectMapper om = new ObjectMapper();
    private ProvDeserialiser deserialiser;


    public BatchExecutor() {
        Pair<Map<String, ProvSerialiser>, Map<String, ProvDeserialiser>> pair=initializeSerializerDispatcher();
        serializerMap=pair.getLeft();
        deserializerMap=pair.getRight();
        deserialiser=deserializerMap.get("provn");
    }

    public BatchExecutor(Map<String, org.openprovenance.prov.model.ProvSerialiser> serializerMap,
                         Map<String, org.openprovenance.prov.model.ProvDeserialiser> deserializerMap) {
        this.serializerMap=serializerMap;
        this.deserializerMap=deserializerMap;
        deserialiser=deserializerMap.get("provn");
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

            System.out.println("In executor " + configurationPath);

            TemplateTasksBatch templateTasksBatch = load(configurationPath, om);

            String localdir = new File(configurationPath).getParent();

            Map<String,String> variableMap=new HashMap<>();
            if (templateTasksBatch.variableMaps!=null) {
                interpretRelativeDirectoriesInVariableMaps(inputBasedir, templateTasksBatch, localdir);
                for (String variableMapPath: templateTasksBatch.variableMaps) {
                    System.out.println("loading variable map " +  variableMapPath);
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


        System.out.println("template_path: " + templateTasksBatch.template_path);
        System.out.println("bindings_path: " + templateTasksBatch.bindings_path);
        System.out.println("output_dir: " + templateTasksBatch.output_dir);

        // create directory

        new File(templateTasksBatch.output_dir).mkdirs();

        //mkdir(new File(templateTasksBatch.output_dir),true);

        for (TemplateTasksBatch.ConfigTask task : templateTasksBatch.tasks) {
            task.template_path = TemplateTasksBatch.addBaseDir(inputBasedir, task.template_path);
            if (task.addOutputDirToInputPath) {
                if (task.template_path==null) {
                    task.template_path=new LinkedList<>();
                }
                task.template_path.add(templateTasksBatch.output_dir);
            }
            logger.info("task: " + task);

            switch (task.type) {
                case "instantiation":
                    executeExpandTask(templateTasksBatch, task, variableMap);
                    break;
                case "merge":
                    executeMergeTask(templateTasksBatch, task, variableMap);
                    break;
                default:
                    logger.error("Unknown task type " + task.type);
                    return 1;
            }

        }


        return 0;


    }

    Ptm_expandingBuilder expandBuilder =new Ptm_expandingBuilder();
    Ptm_mergingBuilder mergeBuilder =new Ptm_mergingBuilder();
    public void executeExpandTask(TemplateTasksBatch templateTasksBatch, TemplateTasksBatch.ConfigTask task, Map<String, String> variableMap) throws IOException {
        Pair<FileInputStream,File> pair=findFileinDirs2(templateTasksBatch.bindings_path, task.bindings);
        String bindingsFilename=pair.getRight().getAbsolutePath();



        //String bindingsFilename = config.bindings_path + "/" + task.bindings;
        InputStream is=substituteVariablesInFile(variableMap, bindingsFilename);

        Expand expand = new Expand(pf, false, false);
        List<String> template_path = (task.template_path ==null)? templateTasksBatch.template_path : task.template_path;
        Pair<FileInputStream, File> fileinDirs = findFileinDirs2(template_path, task.input);

        long secondsSince2023_01_01 = (System.currentTimeMillis() - 1672531200000L);
        String time=pf.newTimeNow().toString();

        List<String> loggedRecords=new LinkedList<>();

        Document doc=expand.expander(deserialise(fileinDirs.getLeft()), om.readValue(is, Bindings.class));
        for (String format: task.formats) {
            String documentFilename = templateTasksBatch.output_dir + "/" + task.output + "." + format;
            serialize(new FileOutputStream(documentFilename), format, doc, false);
            if (task.copyinput != null && task.copyinput) {
                serialize(new FileOutputStream(templateTasksBatch.output_dir + "/" + task.input.replace(".provn","."+format)), format, doc, false);
            }
            String csvRecord = createExpansionCsvRecord(task, format, fileinDirs, time, secondsSince2023_01_01);
            loggedRecords.add(csvRecord);
        }


        exportProvenanceAsCsv(templateTasksBatch, task, loggedRecords);


    }

    private void exportProvenanceAsCsv(TemplateTasksBatch templateTasksBatch, TemplateTasksBatch.ConfigTask task, List<String> loggedRecords) throws IOException {
        if (task.hasProvenance!=null) {
            try (FileOutputStream fos = new FileOutputStream(templateTasksBatch.output_dir + "/" + task.hasProvenance)) {
                for (String csvRecord: loggedRecords) {
                    fos.write(csvRecord.getBytes(StandardCharsets.UTF_8));
                    fos.write("\n".getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }

    private String createExpansionCsvRecord(TemplateTasksBatch.ConfigTask task, String format, Pair<FileInputStream, File> fileinDirs, String time, long secondsSince2023_01_01) {
        Ptm_expandingBean bean=new Ptm_expandingBean();
        bean.bindings= task.bindings;
        bean.provenance=task.hasProvenance;
        bean.time=time;
        bean.template= fileinDirs.getRight().getName();
        bean.document= task.output + "." + format;
        bean.expanding=abs(Long.valueOf(secondsSince2023_01_01).intValue());

        return bean.process(expandBuilder.args2csv());
    }

    private String createMergeCsvRecord(TemplateTasksBatch.ConfigTask task, String format, Pair<FileInputStream, File> fileinDirs1, Pair<FileInputStream, File> fileinDirs2, String time, long secondsSince2023_01_01) {
        Ptm_mergingBean bean=new Ptm_mergingBean();
        bean.provenance=task.hasProvenance;
        bean.time=time;
        bean.template1= fileinDirs1.getRight().getName();
        bean.template2= fileinDirs2.getRight().getName();
        bean.document= task.output + "." + format;
        bean.merging=abs(Long.valueOf(secondsSince2023_01_01).intValue());

        return bean.process(mergeBuilder.args2csv());
    }


    public void executeMergeTask(TemplateTasksBatch templateTasksBatch, TemplateTasksBatch.ConfigTask task, Map<String, String> variableMap) throws IOException {

        List<String> updatedTemplatePath;
        if (task.template_path ==null) {
            updatedTemplatePath= templateTasksBatch.template_path;
        } else {
            updatedTemplatePath=new LinkedList<>();
            updatedTemplatePath.addAll(task.template_path);
            updatedTemplatePath.addAll(templateTasksBatch.template_path);
        }
        Pair<FileInputStream,File> fileinDirs1 = findFileinDirs2(updatedTemplatePath, task.input);
        Pair<FileInputStream,File> fileinDirs2 = findFileinDirs2(updatedTemplatePath, task.input2);
        Document doc1 = deserialise(fileinDirs1.getLeft());
        Document doc2 = deserialise(fileinDirs2.getLeft());
        List<String> loggedRecords=new LinkedList<>();

        long secondsSince2023_01_01 = (System.currentTimeMillis() - 1672531200000L);
        String time=pf.newTimeNow().toString();


        Document doc3=new IndexedDocument(pf,doc1,false).merge(doc2).toDocument();
        for (String format: task.formats) {
            serialize(new FileOutputStream(templateTasksBatch.output_dir + "/" + task.output + "." + format), format, doc3, false);



            String csvRecord = createMergeCsvRecord(task, format, fileinDirs1,fileinDirs2, time, secondsSince2023_01_01);
            loggedRecords.add(csvRecord);
        }


        exportProvenanceAsCsv(templateTasksBatch, task, loggedRecords);


        // option to clean up tmp file
        if (task.clean2!=null && task.clean2) {
            for (String format: task.formats) {
                for (String dir : updatedTemplatePath) {
                    File f = new File(dir + "/" + task.input2.replace(".provn", "." + format));
                    if (f.exists()) f.delete();
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

    private Pair<FileInputStream, File> findFileinDirs2(List<String> template_path, String filename) throws FileNotFoundException {
        for (String dir: template_path) {
            File f=new File(dir + "/" + filename);
            if (f.exists()) {
                return Pair.of(new FileInputStream(f),f);
            }
        }
        throw new FileNotFoundException(filename + " in paths " + template_path);
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
        System.out.println("Executor: " + Arrays.toString(args));
        BatchExecutor executor=new BatchExecutor();
        // find the position of string "-configs" in args
        int pos=0;
        boolean found=false;
        for (int i=0; i<args.length; i++) {
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
            inputBaseDir=args[0];
            outputBaseDir=args[0];
        } else  {
            inputBaseDir=args[0];
            outputBaseDir=args[1];
        }



        // for all args starting with second execute

        for (int i=pos+1; i<args.length; i++) {
            executor.execute(inputBaseDir, outputBaseDir, args[i]);
        }


    }
}
