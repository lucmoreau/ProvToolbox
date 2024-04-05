package org.openprovenance.prov.templates.library;

import org.openprovenance.prov.template.library.plead.client.integrator.*;

public class LocalEnactor implements InputOutputProcessor {

    static private int activityId = 1000;
    static private Integer fileId = 100;
    static private int scoreId = 10;
    static private int approvalID=10000;

    @Override
    public Plead_transformingOutputs process(Plead_transformingInputs bean) {
        Plead_transformingOutputs pleadTransformingOutputs = new Plead_transformingOutputs();
        pleadTransformingOutputs.transforming = activityId++;
        pleadTransformingOutputs.transformed_file = fileId++;
        return pleadTransformingOutputs;
    }

    @Override
    public Plead_filteringOutputs process(Plead_filteringInputs inputs) {
        Plead_filteringOutputs pleadFilteringOutputs = new Plead_filteringOutputs();
        pleadFilteringOutputs.filtering = activityId++;
        pleadFilteringOutputs.filtered_file = fileId++;
        return pleadFilteringOutputs;
    }

    @Override
    public Plead_trainingOutputs process(Plead_trainingInputs bean) {
        Plead_trainingOutputs pleadTrainingOutputs = new Plead_trainingOutputs();
        pleadTrainingOutputs.training = activityId++;
        pleadTrainingOutputs.pipeline = fileId++;
        return pleadTrainingOutputs;
    }

    @Override
    public Plead_validatingOutputs process(Plead_validatingInputs bean) {
        Plead_validatingOutputs pleadValidatingOutputs = new Plead_validatingOutputs();
        pleadValidatingOutputs.validating = activityId++;
        pleadValidatingOutputs.score = scoreId++;
        return pleadValidatingOutputs;
    }

    @Override
    public Plead_approvingOutputs process(Plead_approvingInputs bean) {
        Plead_approvingOutputs pleadApprovingOutputs = new Plead_approvingOutputs();
        pleadApprovingOutputs.approving = activityId++;
        pleadApprovingOutputs.approved_pipeline = fileId++ ;
        pleadApprovingOutputs.approval_record = approvalID++;
        return pleadApprovingOutputs;
    }

    @Override
    public Plead_splittingOutputs process(Plead_splittingInputs bean) {
        Plead_splittingOutputs pleadSplittingOutputs = new Plead_splittingOutputs();
        pleadSplittingOutputs.splitting = activityId++;
        pleadSplittingOutputs.split_file1 = fileId++;
        pleadSplittingOutputs.split_file2 = fileId++;
        return pleadSplittingOutputs;
    }
}

