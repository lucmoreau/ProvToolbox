package org.openprovenance.prov.template.core.ttf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mergingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mergingBuilder;
import org.openprovenance.prov.template.utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class MergeTask implements ConfigTask {
    public String type;
    public String description;
    public List<String> inputs;
    public List<String> template_path;
    public String output;
    public String outputFullyQualifiedName;
    public String bindings;
    public List<String> formats;
    public Boolean copyinput;
    public Boolean clean2;
    public String hasProvenance;
    public List<String> variableMaps;
    public boolean addOutputDirToInputPath=false;
    public String variableCheck;
    /** This task's schemaCheck, overriding its batch's (see {@link TemplateTasksBatch#schemaCheck}). */
    public String schemaCheck;


    static private final Ptm_mergingBuilder mergeBuilder =new Ptm_mergingBuilder();


    @Override
    public String toString() {
        return "MergeTask{" +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", inputs=" + inputs +
                ", template_path=" + template_path +
                ", output='" + output + '\'' +
                ", outputFullyQualifiedName='" + outputFullyQualifiedName + '\'' +
                ", bindings='" + bindings + '\'' +
                ", formats=" + formats +
                ", copyinput=" + copyinput +
                ", clean2=" + clean2 +
                ", hasProvenance='" + hasProvenance + '\'' +
                ", variableMaps=" + variableMaps +
                ", addOutputDirToInputPath=" + addOutputDirToInputPath +
                ", variableCheck='" + variableCheck + '\'' +
                ", schemaCheck='" + schemaCheck + '\'' +
                '}';
    }

    @Override
    public String hasProvenance() {
        return hasProvenance;
    }

    public void execute(TemplateTasksBatch templateTasksBatch, BatchExecutor executor, Map<String, String> variableMap, String inputBasedir) throws IOException {

        template_path = TemplateTasksBatch.addBaseDir(inputBasedir, template_path);
        if (addOutputDirToInputPath) {
            if (template_path==null) {
                template_path=new LinkedList<>();
            }
            template_path.add(templateTasksBatch.output_dir);
        }

        List<String> updatedTemplatePath;
        if (template_path ==null) {
            updatedTemplatePath= templateTasksBatch.template_path;
        } else {
            updatedTemplatePath=new LinkedList<>();
            updatedTemplatePath.addAll(template_path);
            updatedTemplatePath.addAll(templateTasksBatch.template_path);
        }

        TemplateIndexPath templateLibraryPath = executor.indexPath(updatedTemplatePath);


        /*
        List<Pair<FileInputStream,File>> fileinDirs=inputs.stream().map(input -> {;
            try {
                return executor.findFileinDirs2(updatedTemplatePath, input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

         */

        // for the provenance record: each input as the path of the file read, relative to its library root,
        // the way the task that generated it named its output
        List<String> inputLocations=inputs.stream().map(input -> templateLibraryPath.get(input, TemplateExtension.preferredExtensions())).collect(Collectors.toList());

        List<File> foundTemplates=inputs.stream().map(input -> {
            String foundTemplate=templateLibraryPath.getStrict(input, TemplateExtension.preferredExtensions());
            File templateFile=new File(foundTemplate);
            return templateFile;
        }).collect(Collectors.toList());

        //System.out.println("MergeTask: foundTemplate=" + foundTemplates);


        //Pair<FileInputStream,File> fileinDirs1 = executor.findFileinDirs2(updatedTemplatePath, input);
        //Pair<FileInputStream,File> fileinDirs2 = executor.findFileinDirs2(updatedTemplatePath, input2);
        List<Document> docs=foundTemplates.stream().map(fd -> {
            try {
                String informat = executor.getFormat(fd);

                return executor.deserialise(new FileInputStream(fd),informat);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

       //Document doc1 = executor.deserialise(fileinDirs1.getLeft());
        //Document doc2 = executor.deserialise(fileinDirs2.getLeft());
        List<String> loggedRecords=new LinkedList<>();

        long secondsSince2023_01_01 = (System.currentTimeMillis() - 1672531200000L);
        String time= BatchExecutor.pf.newTimeNow().toString();

        IndexedDocument iDocument=new IndexedDocument(BatchExecutor.pf,new org.openprovenance.prov.vanilla.Document(),false);
        for (Document doc: docs) {
            iDocument.merge(doc);
        }

        Document doc3=iDocument.toDocument();
        String location1 = inputLocations.get(0);

        TemplateIndex outputIndex=executor.index(templateTasksBatch.output_dir);

        String outputId=(outputFullyQualifiedName ==null)?output: outputFullyQualifiedName;

        // the merged document is written once per format, whatever the number of inputs
        List<Pair<String, File>> files = new ArrayList<>();
        for (String format : formats) {
            files.add(Pair.of(format, Path.of(templateTasksBatch.output_dir, output + "." + format).toFile()));
        }
        executor.serializeAll(doc3, files);
        for (String format : formats) {
            Path documentPath = Path.of(templateTasksBatch.output_dir, output + "." + format);
            outputIndex.putEntry(outputId,format, outputIndex.relativize(documentPath).toString());
            executor.checkSchema(templateTasksBatch, schemaCheck, format, documentPath.toFile(), output);
        }

        // its provenance: a merging record per format for the first input and each other input
        for (int i=1; i<foundTemplates.size(); i++) {
            String location2 = inputLocations.get(i);
            for (String format : formats) {
                String csvRecord = createMergeCsvRecord(format, location1, location2, time, secondsSince2023_01_01);
                loggedRecords.add(csvRecord);
            }
        }

        Optional<StatementChecker> statementChecker;

        if (variableCheck!=null) {
            ProvVariables pv=new ObjectMapper().readValue(new File(TemplateTasksBatch.addBaseDir(inputBasedir, variableCheck)), ProvVariables.class);
            statementChecker= Optional.of(new StatementChecker(new VariableChecker(pv)));
        } else {
            statementChecker=Optional.empty();
        }
        statementChecker.ifPresent(sc -> System.out.println("***** Variable Checker: " + sc + " (" + simplify(output) + ")"));
        statementChecker.ifPresent(sc -> new ProvUtilities().forAllStatementOrBundle(doc3.getStatementOrBundle(), sc));


        executor.exportProvenanceAsCsv(templateTasksBatch, this, outputId, outputIndex, loggedRecords);
        outputIndex.persist();   // once, for every entry of this task


        // option to clean up tmp file
        if (clean2!=null && clean2) {
            for (int i=1; i<foundTemplates.size(); i++) {
                File file_i = foundTemplates.get(i);
                String input2=file_i.getName();
                for (String format: formats) {
                    for (String dir : updatedTemplatePath) {
                        File f = new File(dir + "/" + input2.replace(".provn", "." + format));
                        if (f.exists()) f.delete();
                    }
                }
            }
        }
    }

    static String simplify(String output) {
        // return suffix after last /
        int lastSlash=output.lastIndexOf('/');
        if (lastSlash>=0) {
            return output.substring(lastSlash+1);
        } else {
            return output;
        }
    }


    private String createMergeCsvRecord(String format, String location1, String location2, String time, long secondsSince2023_01_01) {
        Ptm_mergingBean bean=new Ptm_mergingBean();
        bean.provenance=hasProvenance;
        bean.time=time;
        bean.template1= location1;
        bean.template2= location2;
        bean.document= output + "." + format;
        bean.merging=abs(Long.valueOf(secondsSince2023_01_01).intValue());

        return bean.process(mergeBuilder.args2csv());
    }


}
