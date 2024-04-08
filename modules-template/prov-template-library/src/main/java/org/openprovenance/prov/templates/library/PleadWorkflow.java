package org.openprovenance.prov.templates.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.template.library.plead.client.integrator.*;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.util.Arrays;
import java.util.List;

public class PleadWorkflow {
    private final List<Object> inputs;
    private final List<Object> outputs;
    Integer organization = 111;
    Integer engineer=222;
    Integer manager=333;

    private final InputOutputProcessor templateInvoker;

    private final ProvFactory pf = ProvFactory.getFactory();

    public PleadWorkflow(InputOutputProcessor templateInvoker, List<Object> inputs, List<Object> outputs) {
        this.templateInvoker = templateInvoker;
        this.inputs = inputs;
        this.outputs = outputs;
    }



    public void workflow(String filenameRoot, Integer oldFileId, Integer tmethod, Integer fmethod, Integer n_rows, Integer n_cols, String path, String start, String end) {

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

        if (inputs!=null) inputs.add(transformingInputs);
        Plead_transformingOutputs transformingOutputs = templateInvoker.process(transformingInputs);
        if (outputs!=null) outputs.add(transformingOutputs);

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

        if (inputs!=null) inputs.add(filteringInputs);
        Plead_filteringOutputs filteringOutputs = templateInvoker.process(filteringInputs);
        if (outputs!=null) outputs.add(filteringOutputs);

        Plead_splittingInputs splittingInputs = new Plead_splittingInputs();
        splittingInputs.filename1 = filenameRoot + "-training.csv";
        splittingInputs.filename2 = filenameRoot + "-validation.csv";
        splittingInputs.file = filteringOutputs.filtered_file;
        splittingInputs.organization = organization;
        splittingInputs.engineer = engineer;
        splittingInputs.path1 = path;
        splittingInputs.time = pf.newTimeNow().toString();

        if (inputs!=null) inputs.add(splittingInputs);
        Plead_splittingOutputs splittingOutputs = templateInvoker.process(splittingInputs);
        if (outputs!=null) outputs.add(splittingOutputs);

        Plead_trainingInputs trainingInputs = new Plead_trainingInputs();
        trainingInputs.filename = filenameRoot + ".pipeline";
        trainingInputs.training_dataset = splittingOutputs.split_file1;
        trainingInputs.organization = organization;
        trainingInputs.engineer = engineer;
        trainingInputs.path = path;
        trainingInputs.time = pf.newTimeNow().toString();

        if (inputs!=null) inputs.add(trainingInputs);
        Plead_trainingOutputs trainingOutputs = templateInvoker.process(trainingInputs);
        if (outputs!=null) outputs.add(trainingOutputs);

        Plead_validatingInputs validatingInputs = new Plead_validatingInputs();
        //random value between 0 and 1
        validatingInputs.score_value = Math.random();
        validatingInputs.testing_dataset = splittingOutputs.split_file2;
        validatingInputs.organization = organization;
        validatingInputs.engineer = engineer;
        validatingInputs.path = path;
        validatingInputs.time = pf.newTimeNow().toString();

        if (inputs!=null) inputs.add(validatingInputs);
        Plead_validatingOutputs validatingOutputs = templateInvoker.process(validatingInputs);
        if (outputs!=null) outputs.add(validatingOutputs);

        Plead_approvingInputs approvingInputs = new Plead_approvingInputs();
        approvingInputs.pipeline = trainingOutputs.pipeline;
        approvingInputs.filename = filenameRoot + ".approved-pipeline";
        approvingInputs.score = validatingOutputs.score;
        approvingInputs.signature="signature";
        approvingInputs.organization = organization;
        approvingInputs.manager = manager;
        approvingInputs.path = path;
        approvingInputs.time = pf.newTimeNow().toString();

        if (inputs!=null) inputs.add(approvingInputs);
        Plead_approvingOutputs approvingOutputs = templateInvoker.process(approvingInputs);
        if (outputs!=null) outputs.add(approvingOutputs);

    }

}
