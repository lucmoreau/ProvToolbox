// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerConfigurations, method generateConfigurator,
// in file CompilerConfigurations.java, at line 62
package org.openprovenance.prov.template.library.ptm_copy.client.configurator;

import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_expandingBuilder;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mexpandingBuilder;

public class CsvRecord2ObjectRecordConfigurator implements TableConfigurator<CsvRecord2ObjectRecordConfigurator.Record2Record> {
  public final Record2Record ptm_expanding(Ptm_expandingBuilder builder) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerConfigurations, method generateConfigurator
    // in file CompilerConfigurations.java, at line 97
    return x -> builder.aRecord2BeanConverter.process(x).process(builder.aArgs2RecordConverter());
  }

  public final Record2Record ptm_mexpanding(Ptm_mexpandingBuilder builder) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerConfigurations, method generateConfigurator
    // in file CompilerConfigurations.java, at line 97
    return x -> builder.aRecord2BeanConverter.process(x).process(builder.aArgs2RecordConverter());
  }

  public interface Record2Record {
    Object[] process(Object[] args);
  }
}
