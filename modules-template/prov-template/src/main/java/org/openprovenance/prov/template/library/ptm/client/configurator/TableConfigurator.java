// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerTableConfigurator, method generateTableConfigurator,
// in file CompilerTableConfigurator.java, at line 29
package org.openprovenance.prov.template.library.ptm.client.configurator;

import org.openprovenance.prov.template.library.ptm.client.common.Ptm_expandingBuilder;
import org.openprovenance.prov.template.library.ptm.client.common.Ptm_mexpandingBuilder;

public interface TableConfigurator<T> {
  T ptm_expanding(Ptm_expandingBuilder builder);

  T ptm_mexpanding(Ptm_mexpandingBuilder builder);
}
