package org.openprovenance.prov.service.dispatch;

import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.client.configurator.TableConfigurator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelationConfigurator implements TableConfigurator<Map<String, Map<String, List<String>>>> {

    @Override
    public Map<String, Map<String, List<String>>> plead_transforming(Plead_transformingBuilder builder) {
        Map<String, Map<String, int[]>> relations = Plead_transformingBuilder.__relations;
        String [] order = builder.getPropertyOrder();
        return convert(relations, order);
    }


    @Override
    public Map<String, Map<String, List<String>>> plead_filtering(Plead_filteringBuilder builder) {
        Map<String, Map<String, int[]>> relations = Plead_filteringBuilder.__relations;
        String [] order = builder.getPropertyOrder();
        return convert(relations, order);
    }


    @Override
    public Map<String, Map<String, List<String>>> plead_training(Plead_trainingBuilder builder) {
        Map<String, Map<String, int[]>> relations = Plead_trainingBuilder.__relations;
        String [] order = builder.getPropertyOrder();
        return convert(relations, order);
    }

    @Override
    public Map<String, Map<String, List<String>>> plead_validating(Plead_validatingBuilder builder) {
        Map<String, Map<String, int[]>> relations = Plead_validatingBuilder.__relations;
        String [] order = builder.getPropertyOrder();
        return convert(relations, order);
    }

    @Override
    public Map<String, Map<String, List<String>>> plead_approving(Plead_approvingBuilder builder) {
        Map<String, Map<String, int[]>> relations = Plead_approvingBuilder.__relations;
        String [] order = builder.getPropertyOrder();
        return convert(relations, order);
    }

    @Override
    public Map<String, Map<String, List<String>>> plead_splitting(Plead_splittingBuilder builder) {
        Map<String, Map<String, int[]>> relations = Plead_splittingBuilder.__relations;
        String [] order = builder.getPropertyOrder();
        return convert(relations, order);
    }

    @Override
    public Map<String, Map<String, List<String>>> plead_transforming_composite(Plead_transforming_compositeBuilder builder) {
        return null;
    }


    private Map<String, Map<String, List<String>>> convert(Map<String, Map<String, int[]>> relations, String[] order) {
        return relations
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> relations.get(k).keySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        v -> v,
                                        v -> Arrays.stream(relations.get(k).get(v))
                                                .mapToObj(w -> (w==-1)?null:order[w])
                                                .collect(Collectors.toList())
                                ))));
    }

}
