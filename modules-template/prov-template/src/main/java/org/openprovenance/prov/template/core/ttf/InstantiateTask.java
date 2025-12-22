package org.openprovenance.prov.template.core.ttf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.template.core.Instantiater;
import org.openprovenance.prov.template.json.Bindings;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_expandingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_expandingBuilder;
import org.openprovenance.prov.template.utils.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static org.openprovenance.prov.template.core.ttf.BatchExecutor.*;
import static org.openprovenance.prov.template.core.ttf.MergeTask.simplify;

public class InstantiateTask implements ConfigTask {
    public String type;
    public String description;
    public String template;
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


    static private final Ptm_expandingBuilder expandBuilder =new Ptm_expandingBuilder();

    @Override
    public String toString() {
        return "InstantiateTask{" +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", template='" + template + '\'' +
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

        List<String> updatedTemplatePath;
        if (template_path ==null) {
            updatedTemplatePath= templateTasksBatch.template_path;
        } else {
            updatedTemplatePath=new LinkedList<>();
            updatedTemplatePath.addAll(template_path);
            updatedTemplatePath.addAll(templateTasksBatch.template_path);
        }

        String outputId=(outputFullyQualifiedName ==null)?output: outputFullyQualifiedName;


        TemplateIndexPath templateLibraryPath =new TemplateIndexPath(updatedTemplatePath.stream().map(loc-> new TemplateIndex(loc,true)).collect(Collectors.toList()));
        //System.out.println("InstantiateTask: templateLibraryPath=" + templateLibraryPath);


        Pair<FileInputStream, File> pair=executor.findFileinDirs2(templateTasksBatch.bindings_path, bindings);
        String bindingsFilename=pair.getRight().getAbsolutePath();



        //String bindingsFilename = config.bindings_path + "/" + task.bindings;
        InputStream is=substituteVariablesInFile(variableMap, bindingsFilename);

        Instantiater instantiater = new Instantiater(pf, false, false);

        String foundTemplate=templateLibraryPath.getStrict(template, TemplateExtension.preferredExtensions());
        File templateFile=new File(foundTemplate);


        //System.out.println("InstantiateTask: foundTemplate=" + foundTemplate);
        //Pair<FileInputStream, File> fileinDirs = executor.findFileinDirs2(the_template_path, template);
        //System.out.println("InstantiateTask: fileInDirs=" + fileinDirs.getRight().getAbsolutePath());

        long secondsSince2023_01_01 = (System.currentTimeMillis() - 1672531200000L);
        String time=pf.newTimeNow().toString();

        List<String> loggedRecords=new LinkedList<>();

        TemplateIndex outputIndex=new TemplateIndex(templateTasksBatch.output_dir, true);

        // extract extension from filename
        String informat = executor.getFormat(templateFile);
        Document doc= instantiater.instantiate(executor.deserialise(new FileInputStream(templateFile),informat), executor.om.readValue(is, Bindings.class), bindingsFilename, foundTemplate);
        for (String format: formats) {
            Path documentPath = Path.of(templateTasksBatch.output_dir, output + "." + format);

            outputIndex.addEntry(outputId,format, outputIndex.relativize(documentPath).toString());
            Files.createDirectories(documentPath.getParent());
            executor.serialize(new FileOutputStream(documentPath.toFile()), format, doc, false);
            if (copyinput != null && copyinput) {
                executor.serialize(new FileOutputStream(templateTasksBatch.output_dir + "/" + template.replace(".provn","."+format)), format, doc, false);
                outputIndex.addEntry(template, format, template.replace(".provn","."+format));
            }
            String csvRecord = createExpansionCsvRecord(format, template, time, secondsSince2023_01_01);
            loggedRecords.add(csvRecord);
        }

        Optional<StatementChecker> statementChecker;

        if (variableCheck!=null) {
            ProvVariables pv=new ObjectMapper().readValue(new File(TemplateTasksBatch.addBaseDir(inputBasedir, variableCheck)), ProvVariables.class);
            statementChecker= Optional.of(new StatementChecker(new VariableChecker(pv)));
        } else {
            statementChecker=Optional.empty();
        }
        statementChecker.ifPresent(sc -> System.out.println("***** Variable Checker: " + sc.getVariableChecker()  + " (" + simplify(output) + ")"));
        statementChecker.ifPresent(sc -> new ProvUtilities().forAllStatementOrBundle(doc.getStatementOrBundle(), sc));


        executor.exportProvenanceAsCsv(templateTasksBatch, this, outputId, outputIndex, loggedRecords);


    }



    private String createExpansionCsvRecord(String format, String templateFile, String time, long secondsSince2023_01_01) {
        Ptm_expandingBean bean=new Ptm_expandingBean();
        bean.bindings= bindings;
        bean.provenance=hasProvenance;
        bean.time=time;
        bean.template= templateFile;
        bean.document= output + "." + format;
        bean.expanding=abs(Long.valueOf(secondsSince2023_01_01).intValue());

        return bean.process(expandBuilder.args2csv());
    }

}
