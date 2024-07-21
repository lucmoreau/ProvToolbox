
package org.openprovenance.prov.service;

import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.client.configurator.TableConfigurator;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.util.Map;
import java.util.function.Function;

public class TableConfiguratorForObjectRecordMaker implements TableConfigurator<Function<Object[],Object[]>> {

  private final Map<String, FileBuilder> documentBuilderDispatcher;

  public TableConfiguratorForObjectRecordMaker(Map<String, FileBuilder> documentBuilderDispatcher) {
    this.documentBuilderDispatcher=documentBuilderDispatcher;
  }

  public Function<Object[],Object[]> plead_transforming(Plead_transformingBuilder builder) {
    org.openprovenance.prov.template.library.plead.Plead_transformingBuilder templateBuilder = (org.openprovenance.prov.template.library.plead.Plead_transformingBuilder) documentBuilderDispatcher.get("plead_transforming");
    return record -> templateBuilder.make(record, templateBuilder.getTypedRecord());
  }

  public Function<Object[],Object[]> plead_filtering(Plead_filteringBuilder builder) {
    org.openprovenance.prov.template.library.plead.Plead_filteringBuilder templateBuilder = (org.openprovenance.prov.template.library.plead.Plead_filteringBuilder) documentBuilderDispatcher.get("plead_filtering");
    return record -> templateBuilder.make(record, templateBuilder.getTypedRecord());
  }

  public Function<Object[],Object[]> plead_training(Plead_trainingBuilder builder) {
    org.openprovenance.prov.template.library.plead.Plead_trainingBuilder templateBuilder = (org.openprovenance.prov.template.library.plead.Plead_trainingBuilder) documentBuilderDispatcher.get("plead_training");
    return record -> templateBuilder.make(record, templateBuilder.getTypedRecord());
  }

  public Function<Object[],Object[]> plead_validating(Plead_validatingBuilder builder) {
    org.openprovenance.prov.template.library.plead.Plead_validatingBuilder templateBuilder = (org.openprovenance.prov.template.library.plead.Plead_validatingBuilder) documentBuilderDispatcher.get("plead_validating");
    return record -> templateBuilder.make(record, templateBuilder.getTypedRecord());
  }

  public Function<Object[],Object[]> plead_approving(Plead_approvingBuilder builder) {
    org.openprovenance.prov.template.library.plead.Plead_approvingBuilder templateBuilder = (org.openprovenance.prov.template.library.plead.Plead_approvingBuilder) documentBuilderDispatcher.get("plead_approving");
    return record -> templateBuilder.make(record, templateBuilder.getTypedRecord());
  }

  public Function<Object[],Object[]> plead_splitting(Plead_splittingBuilder builder) {
    org.openprovenance.prov.template.library.plead.Plead_splittingBuilder templateBuilder = (org.openprovenance.prov.template.library.plead.Plead_splittingBuilder) documentBuilderDispatcher.get("plead_splitting");
    return record -> templateBuilder.make(record, templateBuilder.getTypedRecord());
  }

  public Function<Object[],Object[]> plead_transforming_composite(
      Plead_transforming_compositeBuilder builder) {
    return null;
  }
}
