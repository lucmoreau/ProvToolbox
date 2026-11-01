package org.openprovenance.prov.template.core.ttf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.template.core.exception.SchemaCheckException;
import org.openprovenance.prov.template.utils.TemplateExtension;
import org.openprovenance.prov.template.utils.TemplateIndex;
import org.openprovenance.prov.template.utils.TemplateIndexPath;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static java.lang.Math.abs;


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

            c = Class.forName("org.openprovenance.prov.viz.MermaidSerialiser");
            cons = c.getConstructor(ProvFactory.class, String.class);
            serializerMap.put("mmd", (ProvSerialiser) cons.newInstance(pf, "mmd"));

            c = Class.forName("org.openprovenance.prov.viz.QualifiedFormats");
            c.getMethod("registerQualifiedSerialisers", ProvFactory.class, Map.class).invoke(null, pf, serializerMap);

            c = Class.forName("org.openprovenance.prov.notation.ProvDeserialiser");
            cons = c.getConstructor(ProvFactory.class);
            deserializerMap.put("provn", (ProvDeserialiser) cons.newInstance(pf));

            c = Class.forName("org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser");
            cons = c.getConstructor();
            serializerMap.put("jsonld", (ProvSerialiser) cons.newInstance());
            System.out.println("serializerMap: " + serializerMap.keySet());
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
        } catch (SchemaCheckException e) {
            throw e;   // a failed schemaCheck fails the execution, where other task errors are logged
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

        indexPath(templateTasksBatch.template_path);   // reads the libraries' indexes, building the missing ones, before the tasks

        logger.info("template_path: " + templateTasksBatch.template_path);
        logger.info("bindings_path: " + templateTasksBatch.bindings_path);
        logger.info("output_dir: " + templateTasksBatch.output_dir);


        // create directory

        new File(templateTasksBatch.output_dir).mkdirs();

        //mkdir(new File(templateTasksBatch.output_dir),true);

        for (ConfigTask task : templateTasksBatch.tasks) {
            logger.info("task: " + task);

            task.execute(templateTasksBatch, this, variableMap, inputBasedir);
        }
        return 0;
    }


    void exportProvenanceAsCsv(TemplateTasksBatch templateTasksBatch, ConfigTask task, String templateId, TemplateIndex outputIndex, List<String> loggedRecords) throws IOException {
        //System.out.println("**** exporting provenance as PROV-CSV to " + task.hasProvenance() + " for index " + outputIndex + " template " + templateId);
        if (task.hasProvenance()!=null) {
            Path documentPath = Path.of(templateTasksBatch.output_dir, task.hasProvenance());
            Files.createDirectories(documentPath.getParent());
            try (FileOutputStream fos = new FileOutputStream(templateTasksBatch.output_dir + "/" + task.hasProvenance())) {
                for (String csvRecord: loggedRecords) {
                    fos.write(csvRecord.getBytes(StandardCharsets.UTF_8));
                    fos.write("\n".getBytes(StandardCharsets.UTF_8));
                }
            }
            if (outputIndex!=null) {
                outputIndex.putProvenanceEntry(templateId, TemplateExtension.PROVCSV.toString(), task.hasProvenance());   // persisted by the task
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

    /**
     * Identifier of a file read by a task, for its provenance record: the file's path relative to
     * the given root, so that a record names a file the same way, whichever run read it.
     */
    static String relativeTo(String root, File file) {
        Path rootPath=Path.of(root).toAbsolutePath().normalize();
        return rootPath.relativize(file.toPath().toAbsolutePath().normalize()).toString();
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

    public void serializeToFile(File file, String format, Document doc) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            serialize(fos, format, doc, false);
        }
    }

    /** Whether a format is a rendering by the dot program (png, svg, pdf, or a qualified variant of them). */
    static boolean isRendering(String format) {
        String ext = format.substring(format.lastIndexOf('.') + 1);
        return ext.equals("png") || ext.equals("svg") || ext.equals("pdf");
    }

    /** Renderings are produced concurrently, unless -Dorg.openprovenance.prov.template.ttf.parallel=false. */
    static final boolean PARALLEL = !"false".equals(System.getProperty("org.openprovenance.prov.template.ttf.parallel"));

    /**
     * Serialises a document to the given files, each in its format: the text formats in turn, then the renderings
     * concurrently, since each is a run of the dot program during which the JVM only waits. The renderings of one
     * document dominate the execution of a template tasks file.
     * @param outputs pairs of a format and the file to write in that format
     */
    public void serializeAll(Document doc, List<Pair<String, File>> outputs) throws IOException {
        List<Pair<String, File>> renderings = new ArrayList<>();
        for (Pair<String, File> output : outputs) {
            Path parent = output.getRight().toPath().toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            if (isRendering(output.getLeft())) {
                renderings.add(output);
            } else {
                serializeToFile(output.getRight(), output.getLeft(), doc);
            }
        }
        if (PARALLEL && renderings.size() > 1) {
            try {
                renderings.parallelStream().forEach(r -> {
                    try {
                        serializeToFile(r.getRight(), r.getLeft(), doc);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            } catch (UncheckedIOException e) {
                throw e.getCause();
            }
        } else {
            for (Pair<String, File> r : renderings) {
                serializeToFile(r.getRight(), r.getLeft(), doc);
            }
        }
    }

    /** The indexes of the template directories named so far, each read or built once, holding the entries the tasks add. */
    private final Map<String, TemplateIndex> indexes = new HashMap<>();

    /** The index of a template directory, read (or built, when it has no index file) the first time it is named. */
    public synchronized TemplateIndex index(String dir) {
        return indexes.computeIfAbsent(Path.of(dir).toAbsolutePath().normalize().toString(), k -> new TemplateIndex(dir, true));
    }

    /** The indexes of the given template directories, in order. */
    public TemplateIndexPath indexPath(List<String> dirs) {
        return new TemplateIndexPath(dirs.stream().map(this::index).collect(Collectors.toList()));
    }

    /**
     * Validates a task's generated output against the version of the PROV-JSONLD schema its schemaCheck names (the
     * task's own, else its batch's), if the output is jsonld and the check is not none.
     * @throws SchemaCheckException listing the violations, when the output does not conform, or when the
     *         schemaCheck names no bundled version
     */
    public void checkSchema(TemplateTasksBatch batch, String taskSchemaCheck, String format, File file, String output) throws IOException {
        if (!InteropMediaType.EXTENSION_JSONLD.equals(format)) return;
        String version;
        try {
            version = SchemaCheck.version(SchemaCheck.effective(batch.schemaCheck, taskSchemaCheck));
        } catch (IllegalArgumentException e) {
            throw new SchemaCheckException(e.getMessage() + " (" + output + "." + format + ")");   // a mistyped value fails too
        }
        if (version == null) return;
        List<String> violations = SchemaCheck.violations(file, version);
        if (!violations.isEmpty()) {
            throw new SchemaCheckException("schemaCheck " + version + ": " + output + "." + format + " does not conform ("
                    + violations.size() + " violations): " + String.join("; ", violations.subList(0, Math.min(10, violations.size())))
                    + (violations.size() > 10 ? "; ..." : ""));
        }
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

    static private final Map<String,Document> templateCache=new HashMap<>();


    public Document getCachedTemplate(String template) {
        return templateCache.get(template);
    }

    public void cacheTemplate(String template, Document doc2) {
        templateCache.put(template, doc2);
    }

    /*int threads = Math.min(Math.max(1, 4), Runtime.getRuntime().availableProcessors());
    ExecutorService serPool = java.util.concurrent.Executors.newFixedThreadPool(threads);
    List<java.util.concurrent.Future<String>> futures = new java.util.ArrayList<>();


     */

}
