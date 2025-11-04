package org.openprovenance.prov.templates.library;

import org.openprovenance.prov.template.library.plead.client.integrator.*;
import org.openprovenance.prov.template.library.plead.integrator.InputOutputProcessor;

public abstract class LocalEnactor implements InputOutputProcessor {


    public abstract Integer incrementCounter(String counter);


    @Override
    public Plead_transformingOutputs process(Plead_transformingInputs bean) {
        Plead_transformingOutputs pleadTransformingOutputs = new Plead_transformingOutputs();
        pleadTransformingOutputs.transforming = incrementCounter("activity");
        pleadTransformingOutputs.transformed_file = incrementCounter("file");
        return pleadTransformingOutputs;
    }

    @Override
    public Plead_filteringOutputs process(Plead_filteringInputs inputs) {
        Plead_filteringOutputs pleadFilteringOutputs = new Plead_filteringOutputs();
        pleadFilteringOutputs.filtering =incrementCounter("activity");
        pleadFilteringOutputs.filtered_file = incrementCounter("file");
        return pleadFilteringOutputs;
    }

    @Override
    public Plead_trainingOutputs process(Plead_trainingInputs bean) {
        Plead_trainingOutputs pleadTrainingOutputs = new Plead_trainingOutputs();
        pleadTrainingOutputs.training = incrementCounter("activity");
        pleadTrainingOutputs.pipeline = incrementCounter("file");
        return pleadTrainingOutputs;
    }

    @Override
    public Plead_validatingOutputs process(Plead_validatingInputs bean) {
        Plead_validatingOutputs pleadValidatingOutputs = new Plead_validatingOutputs();
        pleadValidatingOutputs.validating = incrementCounter("activity");
        pleadValidatingOutputs.score = incrementCounter("score");
        return pleadValidatingOutputs;
    }

    @Override
    public Plead_approvingOutputs process(Plead_approvingInputs bean) {
        Plead_approvingOutputs pleadApprovingOutputs = new Plead_approvingOutputs();
        pleadApprovingOutputs.approving = incrementCounter("activity");
        pleadApprovingOutputs.approved_pipeline = incrementCounter("file"); ;
        pleadApprovingOutputs.approval_record = incrementCounter("approval");
        return pleadApprovingOutputs;
    }

    @Override
    public Plead_splittingOutputs process(Plead_splittingInputs bean) {
        Plead_splittingOutputs pleadSplittingOutputs = new Plead_splittingOutputs();
        pleadSplittingOutputs.splitting = incrementCounter("activity");
        pleadSplittingOutputs.split_file1 = incrementCounter("file");
        pleadSplittingOutputs.split_file2 = incrementCounter("file");
        return pleadSplittingOutputs;
    }

    @Override
    public Plead_transforming_compositeOutputs process(Plead_transforming_compositeInputs bean) {
        throw new UnsupportedOperationException("Plead_transforming_composite not supported");
    }
}

