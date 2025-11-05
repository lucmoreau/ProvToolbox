package org.openprovenance.prov.template.service.dispatch;

import org.openprovenance.prov.client.Builder;
import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.configurator.BuilderConfigurator;
import org.openprovenance.prov.template.library.plead.configurator.BuilderProcessorConfigurator;
import org.openprovenance.prov.template.library.plead.configurator.RelationConfigurator;
import org.openprovenance.prov.template.library.plead.configurator.TableConfigurator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SuccessorConfigurator extends BuilderProcessorConfigurator<Map<String, List<String>>> implements TableConfigurator<Map<String, List<String>>> {

    public SuccessorConfigurator(Function<Builder, Map<String, List<String>>> _processor) {
        super(_processor);
    }

    public SuccessorConfigurator() {
        this(SuccessorConfigurator::convert);
    }

    static public Map<String, List<String>> convert(Builder builder) {
        Map<Integer, int[]>  successors = builder.getSuccessors();
        String [] order = builder.getPropertyOrder();
        return successors
                .keySet()
                .stream()
                .filter(k -> successors.get(k).length!=0)
                .collect(Collectors.toMap(
                        k -> order[k],
                        k ->
                                Arrays.stream(successors.get(k))
                                        .mapToObj(v -> order[v])
                                        .collect(Collectors.toList())));
    }

}
