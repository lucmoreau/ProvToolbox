package org.openprovenance.prov.service;

import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.client.configurator.TableConfigurator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuccessorConfigurator implements TableConfigurator<Map<String, List<String>>> {

    @Override
    public Map<String, List<String>> plead_transforming(Plead_transformingBuilder builder) {
        Map<Integer, int[]>  successors = builder.getSuccessors();
        String [] order = builder.getPropertyOrder();
        return successors.keySet().stream().filter(k -> successors.get(k).length!=0).collect(Collectors.toMap(k -> order[k], k -> Arrays.stream(successors.get(k)).mapToObj(v -> order[v]).collect(Collectors.toList())));
    }

    @Override
    public Map<String, List<String>> plead_filtering(Plead_filteringBuilder builder) {
        Map<Integer, int[]>  successors = builder.getSuccessors();
        String [] order = builder.getPropertyOrder();
        return successors.keySet().stream().collect(Collectors.toMap(k -> order[k], k -> Arrays.stream(successors.get(k)).mapToObj(v -> order[v]).collect(Collectors.toList())));
    }

    @Override
    public Map<String, List<String>> plead_training(Plead_trainingBuilder builder) {
        Map<Integer, int[]>  successors = builder.getSuccessors();
        String [] order = builder.getPropertyOrder();
        return successors.keySet().stream().collect(Collectors.toMap(k -> order[k], k -> Arrays.stream(successors.get(k)).mapToObj(v -> order[v]).collect(Collectors.toList())));
    }

    @Override
    public Map<String, List<String>> plead_validating(Plead_validatingBuilder builder) {
        Map<Integer, int[]>  successors = builder.getSuccessors();
        String [] order = builder.getPropertyOrder();
        return successors.keySet().stream().collect(Collectors.toMap(k -> order[k], k -> Arrays.stream(successors.get(k)).mapToObj(v -> order[v]).collect(Collectors.toList())));
    }

    @Override
    public Map<String, List<String>> plead_approving(Plead_approvingBuilder builder) {
        Map<Integer, int[]>  successors = builder.getSuccessors();
        String [] order = builder.getPropertyOrder();
        return successors.keySet().stream().collect(Collectors.toMap(k -> order[k], k -> Arrays.stream(successors.get(k)).mapToObj(v -> order[v]).collect(Collectors.toList())));
    }

    @Override
    public Map<String, List<String>> plead_splitting(Plead_splittingBuilder builder) {
        Map<Integer, int[]>  successors = builder.getSuccessors();
        String [] order = builder.getPropertyOrder();
        return successors.keySet().stream().collect(Collectors.toMap(k -> order[k], k -> Arrays.stream(successors.get(k)).mapToObj(v -> order[v]).collect(Collectors.toList())));
    }

    @Override
    public Map<String, List<String>> plead_transforming_composite(Plead_transforming_compositeBuilder builder) {
        return null;
    }

}
