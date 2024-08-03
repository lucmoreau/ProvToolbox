
package org.openprovenance.prov.service.dispatch;

import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.client.configurator.TableConfigurator;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableConfiguratorForSuccessors implements TableConfigurator<Map<String, List<String>>> {

  private final Map<String, FileBuilder> documentBuilderDispatcher;

  public TableConfiguratorForSuccessors(Map<String, FileBuilder> documentBuilderDispatcher) {
    this.documentBuilderDispatcher=documentBuilderDispatcher;
  }

  public Map<String, List<String>> plead_transforming(Plead_transformingBuilder builder) {
    String[] order=Plead_transformingBuilder.propertyOrder;
    return Plead_transformingBuilder.__successors.keySet().stream()
            .collect(Collectors.toMap(
                    k -> order[k],
                    k -> Arrays.stream(Plead_transformingBuilder.__successors.get(k))
                            .mapToObj(v -> order[v])
                            .collect(Collectors.toList())));
  }

  public Map<String, List<String>> plead_filtering(Plead_filteringBuilder builder) {
    String[] order=Plead_filteringBuilder.propertyOrder;
    return Plead_filteringBuilder.__successors.keySet().stream()
            .collect(Collectors.toMap(
                    k -> order[k],
                    k -> Arrays.stream(Plead_filteringBuilder.__successors.get(k))
                            .mapToObj(v -> order[v])
                            .collect(Collectors.toList())));

  }

  public Map<String, List<String>> plead_training(Plead_trainingBuilder builder) {
    String[] order=Plead_trainingBuilder.propertyOrder;
    return Plead_trainingBuilder.__successors.keySet().stream()
            .collect(Collectors.toMap(
                    k -> order[k],
                    k -> Arrays.stream(Plead_trainingBuilder.__successors.get(k))
                            .mapToObj(v -> order[v])
                            .collect(Collectors.toList())));
  }

  public Map<String, List<String>> plead_validating(Plead_validatingBuilder builder) {
    String[] order=Plead_validatingBuilder.propertyOrder;
    return Plead_validatingBuilder.__successors.keySet().stream()
            .collect(Collectors.toMap(
                    k -> order[k],
                    k -> Arrays.stream(Plead_validatingBuilder.__successors.get(k))
                            .mapToObj(v -> order[v])
                            .collect(Collectors.toList())));
  }

  public Map<String, List<String>> plead_approving(Plead_approvingBuilder builder) {
    String[] order=Plead_approvingBuilder.propertyOrder;
    return Plead_approvingBuilder.__successors.keySet().stream()
            .collect(Collectors.toMap(
                    k -> order[k],
                    k -> Arrays.stream(Plead_approvingBuilder.__successors.get(k))
                            .mapToObj(v -> order[v])
                            .collect(Collectors.toList())));

  }

  public Map<String, List<String>> plead_splitting(Plead_splittingBuilder builder) {
    String[] order=Plead_splittingBuilder.propertyOrder;
    return Plead_splittingBuilder.__successors.keySet().stream()
            .collect(Collectors.toMap(
                    k -> order[k],
                    k -> Arrays.stream(Plead_splittingBuilder.__successors.get(k))
                            .mapToObj(v -> order[v])
                            .collect(Collectors.toList())));
  }

  public Map<String, List<String>> plead_transforming_composite(
      Plead_transforming_compositeBuilder builder) {
    return null;
  }
}
