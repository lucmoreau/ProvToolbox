// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerTableConfigurator, method generateTableConfigurator,
// in file CompilerTableConfigurator.java, at line 29
package org.openprovenance.prov.template.library.ptm_copy.client.configurator;

import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_instantiatingBuilder;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mexpandingBuilder;

public interface TableConfigurator<T> {
  T ptm_instantiating(Ptm_instantiatingBuilder builder);

  T ptm_mexpanding(Ptm_mexpandingBuilder builder);
}
