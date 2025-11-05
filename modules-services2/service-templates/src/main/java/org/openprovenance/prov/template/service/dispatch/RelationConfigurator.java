package org.openprovenance.prov.template.service.dispatch;

import org.openprovenance.prov.template.library.plead.client.common.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class RelationConfigurator extends org.openprovenance.prov.template.library.plead.configurator.RelationConfigurator<Map<String, Map<String, List<String>>>> {

    public RelationConfigurator(BiFunction<Map<String, Map<String, int[]>>, String[], Map<String, Map<String, List<String>>>> converter) {
        super(converter);
    }

    public RelationConfigurator() {
        this(RelationConfigurator::convert);
    }

    static private Map<String, Map<String, List<String>>> convert(Map<String, Map<String, int[]>> relations, String[] order) {
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
