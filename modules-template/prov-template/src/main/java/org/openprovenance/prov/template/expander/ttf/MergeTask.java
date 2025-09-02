package org.openprovenance.prov.template.expander.ttf;

import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mergingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mergingBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
        List<Pair<FileInputStream,File>> fileinDirs=inputs.stream().map(input -> {;
            try {
                return executor.findFileinDirs2(updatedTemplatePath, input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        //Pair<FileInputStream,File> fileinDirs1 = executor.findFileinDirs2(updatedTemplatePath, input);
        //Pair<FileInputStream,File> fileinDirs2 = executor.findFileinDirs2(updatedTemplatePath, input2);
        List<Document> docs=fileinDirs.stream().map(fd -> {
            return executor.deserialise(fd.getLeft());
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
        Pair<FileInputStream,File> fileinDirs1 = fileinDirs.get(0);

        for (int i=1; i<fileinDirs.size(); i++) {
            Pair<FileInputStream, File> fileinDirs2 = fileinDirs.get(i);


            for (String format : formats) {
                executor.serialize(new FileOutputStream(templateTasksBatch.output_dir + "/" + output + "." + format), format, doc3, false);
                String csvRecord = createMergeCsvRecord(format, fileinDirs1, fileinDirs2, time, secondsSince2023_01_01);
                loggedRecords.add(csvRecord);
            }
        }


        executor.exportProvenanceAsCsv(templateTasksBatch, this, loggedRecords);


        // option to clean up tmp file
        if (clean2!=null && clean2) {
            for (int i=1; i<fileinDirs.size(); i++) {
                Pair<FileInputStream, File> fileinDirs2 = fileinDirs.get(i);
                String input2=fileinDirs2.getRight().getName();
                for (String format: formats) {
                    for (String dir : updatedTemplatePath) {
                        File f = new File(dir + "/" + input2.replace(".provn", "." + format));
                        if (f.exists()) f.delete();
                    }
                }
            }
        }
    }


    private String createMergeCsvRecord(String format, Pair<FileInputStream, File> fileinDirs1, Pair<FileInputStream, File> fileinDirs2, String time, long secondsSince2023_01_01) {
        Ptm_mergingBean bean=new Ptm_mergingBean();
        bean.provenance=hasProvenance;
        bean.time=time;
        bean.template1= fileinDirs1.getRight().getName();
        bean.template2= fileinDirs2.getRight().getName();
        bean.document= output + "." + format;
        bean.merging=abs(Long.valueOf(secondsSince2023_01_01).intValue());

        return bean.process(mergeBuilder.args2csv());
    }


}
