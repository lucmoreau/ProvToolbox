package org.openprovenance.prov.service.dispatch;

import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.client.configurator.TableConfigurator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Relation0Configurator implements TableConfigurator<Map<String, Map<String, int[]>>> {

    @Override
    public Map<String, Map<String, int[]>> plead_transforming(Plead_transformingBuilder builder) {
        return Plead_transformingBuilder.__relations;
    }


    @Override
    public Map<String, Map<String, int[]>> plead_filtering(Plead_filteringBuilder builder) {
        return Plead_filteringBuilder.__relations;
    }


    @Override
    public Map<String, Map<String, int[]>> plead_training(Plead_trainingBuilder builder) {
        return Plead_trainingBuilder.__relations;
    }

    @Override
    public Map<String, Map<String, int[]>> plead_validating(Plead_validatingBuilder builder) {
        return Plead_validatingBuilder.__relations;
    }

    @Override
    public Map<String, Map<String, int[]>> plead_approving(Plead_approvingBuilder builder) {
        return Plead_approvingBuilder.__relations;
    }

    @Override
    public Map<String, Map<String, int[]>> plead_splitting(Plead_splittingBuilder builder) {
        return Plead_splittingBuilder.__relations;
    }

    @Override
    public Map<String, Map<String, int[]>> plead_transforming_composite(Plead_transforming_compositeBuilder builder) {
        return null;
    }


}
