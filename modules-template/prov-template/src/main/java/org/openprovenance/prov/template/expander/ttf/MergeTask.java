package org.openprovenance.prov.template.expander.ttf;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mergingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mergingBuilder;
import org.openprovenance.prov.template.utils.TemplateExtension;
import org.openprovenance.prov.template.utils.TemplateIndex;
import org.openprovenance.prov.template.utils.TemplateIndexPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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


    static private final Ptm_mergingBuilder mergeBuilder =new Ptm_mergingBuilder();


    @Override
    public String toString() {
        return "MergeTask{" +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", inputs='" + inputs + '\'' +
                ", template_path=" + template_path +
                ", output='" + output + '\'' +
                ", bindings='" + bindings + '\'' +
                ", formats=" + formats +
                ", copyinput=" + copyinput +
                ", clean2=" + clean2 +
                ", hasProvenance='" + hasProvenance + '\'' +
                ", variableMaps=" + variableMaps +
                ", addOutputDirToInputPath=" + addOutputDirToInputPath +
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

        TemplateIndexPath templateLibraryPath =new TemplateIndexPath(updatedTemplatePath.stream().map(loc-> new TemplateIndex(loc,true)).collect(Collectors.toList()));
        System.out.println("MergeTask: templateLibraryPath=" + templateLibraryPath);


        /*
        List<Pair<FileInputStream,File>> fileinDirs=inputs.stream().map(input -> {;
            try {
                return executor.findFileinDirs2(updatedTemplatePath, input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

         */

        List<File> foundTemplates=inputs.stream().map(input -> {
            String foundTemplate=templateLibraryPath.getStrict(input, TemplateExtension.preferredExtensions());
            File templateFile=new File(foundTemplate);
            return templateFile;
        }).collect(Collectors.toList());

        System.out.println("MergeTask: foundTemplate=" + foundTemplates);


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
        String time=executor.pf.newTimeNow().toString();

        IndexedDocument iDocument=new IndexedDocument(executor.pf,new org.openprovenance.prov.vanilla.Document(),false);
        for (Document doc: docs) {
            iDocument.merge(doc);
        }

        Document doc3=iDocument.toDocument();
        File file1 = foundTemplates.get(0);

        TemplateIndex outputIndex=new TemplateIndex(templateTasksBatch.output_dir, true);

        String outputId=(outputFullyQualifiedName ==null)?output: outputFullyQualifiedName;

        for (int i=1; i<foundTemplates.size(); i++) {
            File file2 = foundTemplates.get(i);


            for (String format : formats) {
                Path documentPath = Path.of(templateTasksBatch.output_dir, output + "." + format);

                outputIndex.addEntry(outputId,format, outputIndex.relativize(documentPath).toString());

                executor.serialize(new FileOutputStream(templateTasksBatch.output_dir + "/" + output + "." + format), format, doc3, false);
                String csvRecord = createMergeCsvRecord(format, file1, file2, time, secondsSince2023_01_01);
                loggedRecords.add(csvRecord);
            }
        }

        executor.exportProvenanceAsCsv(templateTasksBatch, this, outputId, outputIndex, loggedRecords);


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


    private String createMergeCsvRecord(String format, File fileinDirs1, File fileinDirs2, String time, long secondsSince2023_01_01) {
        Ptm_mergingBean bean=new Ptm_mergingBean();
        bean.provenance=hasProvenance;
        bean.time=time;
        bean.template1= fileinDirs1.getName();
        bean.template2= fileinDirs2.getName();
        bean.document= output + "." + format;
        bean.merging=abs(Long.valueOf(secondsSince2023_01_01).intValue());

        return bean.process(mergeBuilder.args2csv());
    }


}
