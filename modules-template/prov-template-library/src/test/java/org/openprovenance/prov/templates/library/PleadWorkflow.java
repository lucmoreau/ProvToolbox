package org.openprovenance.prov.templates.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.template.library.plead.client.integrator.*;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.util.Arrays;
import java.util.List;

public class PleadWorkflow {
    Integer organization = 1;
    Integer engineer;

    private final InputOutputProcessor templateInvoker;

    private final ProvFactory pf = ProvFactory.getFactory();

    public PleadWorkflow(InputOutputProcessor templateInvoker) {
        this.templateInvoker = templateInvoker;
    }



    public List<Object> workflow(String filenameRoot, Integer oldFileId, Integer tmethod, Integer fmethod, Integer n_rows, Integer n_cols, String path, String start, String end) {

        Plead_transformingInputs transformingInputs = new Plead_transformingInputs();
        transformingInputs.filename = filenameRoot + "-transformed.csv";
        transformingInputs.file = oldFileId;
        transformingInputs.method = tmethod;
        transformingInputs.organization = organization;
        transformingInputs.engineer = engineer;
        transformingInputs.n_rows = n_rows;
        transformingInputs.n_cols = n_cols;
        transformingInputs.path = path;
        transformingInputs.time = pf.newTimeNow().toString();
        transformingInputs.start = start;
        transformingInputs.end = end;
        Plead_transformingOutputs transformingOutputs = templateInvoker.process(transformingInputs);

        Plead_filteringInputs filteringInputs = new Plead_filteringInputs();
        filteringInputs.filename = filenameRoot + "-filtered.csv";
        filteringInputs.file = transformingOutputs.transformed_file;
        filteringInputs.method = fmethod;
        filteringInputs.organization = organization;
        filteringInputs.engineer = engineer;
        filteringInputs.n_rows = n_rows;
        filteringInputs.n_cols = n_cols;
        filteringInputs.path = path;
        filteringInputs.time = pf.newTimeNow().toString();
        filteringInputs.start = start;
        filteringInputs.end = end;
        Plead_filteringOutputs filteringOutputs = templateInvoker.process(filteringInputs);

        Plead_splittingInputs splittingInputs = new Plead_splittingInputs();
        splittingInputs.filename1 = filenameRoot + "-training.csv";
        splittingInputs.filename2 = filenameRoot + "-validation.csv";
        splittingInputs.file = filteringOutputs.filtered_file;
        splittingInputs.organization = organization;
        splittingInputs.engineer = engineer;
        splittingInputs.path1 = path;
        splittingInputs.time = pf.newTimeNow().toString();
        Plead_splittingOutputs splittingOutputs = templateInvoker.process(splittingInputs);

        Plead_trainingInputs trainingInputs = new Plead_trainingInputs();
        trainingInputs.filename = filenameRoot + ".pipeline";
        trainingInputs.training_dataset = splittingOutputs.split_file1;
        trainingInputs.organization = organization;
        trainingInputs.engineer = engineer;
        trainingInputs.path = path;
        trainingInputs.time = pf.newTimeNow().toString();
        Plead_trainingOutputs trainingOutputs = templateInvoker.process(trainingInputs);

        Plead_validatingInputs validatingInputs = new Plead_validatingInputs();
        //random value between 0 and 1
        validatingInputs.score_value = Math.random();
        validatingInputs.testing_dataset = splittingOutputs.split_file2;
        validatingInputs.organization = organization;
        validatingInputs.engineer = engineer;
        validatingInputs.path = path;
        validatingInputs.time = pf.newTimeNow().toString();
        Plead_validatingOutputs validatingOutputs = templateInvoker.process(validatingInputs);

        Plead_approvingInputs approvingInputs = new Plead_approvingInputs();
        approvingInputs.pipeline = trainingOutputs.pipeline;
        approvingInputs.filename = filenameRoot + ".approved-pipeline";
        approvingInputs.score = validatingOutputs.validating;
        approvingInputs.signature="signature";
        approvingInputs.organization = organization;
        approvingInputs.path = path;
        approvingInputs.time = pf.newTimeNow().toString();
        Plead_approvingOutputs approvingOutputs = templateInvoker.process(approvingInputs);

        return Arrays.asList(transformingOutputs, filteringOutputs, splittingOutputs, trainingOutputs, validatingOutputs, approvingOutputs);



    }

}
