package org.openprovenance.prov.template.expander.ttf;

import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.template.expander.Expand;
import org.openprovenance.prov.template.json.Bindings;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_expandingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_expandingBuilder;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;
import static org.openprovenance.prov.template.expander.ttf.BatchExecutor.*;

public class InstanteTask implements ConfigTask {
    public String type;
    public String description;
    public String template;
    public List<String> template_path;
    public String output;
    public String bindings;
    public List<String> formats;
    public Boolean copyinput;
    public Boolean clean2;
    public String hasProvenance;
    public List<String> variableMaps;
    public boolean addOutputDirToInputPath=false;

    static private final Ptm_expandingBuilder expandBuilder =new Ptm_expandingBuilder();

    @Override
    public String toString() {
        return "ExpandTask{" +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", template='" + template + '\'' +
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

    @Override
    public void execute(TemplateTasksBatch templateTasksBatch, BatchExecutor executor, Map<String, String> variableMap, String inputBasedir) throws IOException {
        template_path = TemplateTasksBatch.addBaseDir(inputBasedir, template_path);
        if (addOutputDirToInputPath) {
            if (template_path==null) {
                template_path=new LinkedList<>();
            }
            template_path.add(templateTasksBatch.output_dir);
        }

        Pair<FileInputStream, File> pair=executor.findFileinDirs2(templateTasksBatch.bindings_path, bindings);
        String bindingsFilename=pair.getRight().getAbsolutePath();



        //String bindingsFilename = config.bindings_path + "/" + task.bindings;
        InputStream is=substituteVariablesInFile(variableMap, bindingsFilename);

        Expand expand = new Expand(pf, false, false);
        List<String> the_template_path = (template_path==null)? templateTasksBatch.template_path : template_path;
        Pair<FileInputStream, File> fileinDirs = executor.findFileinDirs2(the_template_path, template);

        long secondsSince2023_01_01 = (System.currentTimeMillis() - 1672531200000L);
        String time=pf.newTimeNow().toString();

        List<String> loggedRecords=new LinkedList<>();

        Document doc=expand.expander(executor.deserialise(fileinDirs.getLeft()), executor.om.readValue(is, Bindings.class));
        for (String format: formats) {
            String documentFilename = templateTasksBatch.output_dir + "/" + output + "." + format;
            executor.serialize(new FileOutputStream(documentFilename), format, doc, false);
            if (copyinput != null && copyinput) {
                executor.serialize(new FileOutputStream(templateTasksBatch.output_dir + "/" + template.replace(".provn","."+format)), format, doc, false);
            }
            String csvRecord = createExpansionCsvRecord(format, fileinDirs, time, secondsSince2023_01_01);
            loggedRecords.add(csvRecord);
        }


        executor.exportProvenanceAsCsv(templateTasksBatch, this, loggedRecords);


    }

    private String createExpansionCsvRecord(String format, Pair<FileInputStream, File> fileinDirs, String time, long secondsSince2023_01_01) {
        Ptm_expandingBean bean=new Ptm_expandingBean();
        bean.bindings= bindings;
        bean.provenance=hasProvenance;
        bean.time=time;
        bean.template= fileinDirs.getRight().getName();
        bean.document= output + "." + format;
        bean.expanding=abs(Long.valueOf(secondsSince2023_01_01).intValue());

        return bean.process(expandBuilder.args2csv());
    }

}
