// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerConfigurations, method generateConfigurator,
// in file CompilerConfigurations.java, at line 60
package org.openprovenance.prov.template.library.ptm.client.configurator;

import org.openprovenance.prov.template.library.ptm.client.common.Ptm_expandingBuilder;
import org.openprovenance.prov.template.library.ptm.client.common.Ptm_mexpandingBuilder;

public class PropertyOrderConfigurator implements TableConfigurator<String[]> {
  public final String[] ptm_expanding(Ptm_expandingBuilder builder) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerConfigurations, method generateConfigurator
    // in file CompilerConfigurations.java, at line 95
    return builder.getPropertyOrder();
  }

  public final String[] ptm_mexpanding(Ptm_mexpandingBuilder builder) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerConfigurations, method generateConfigurator
    // in file CompilerConfigurations.java, at line 95
    return builder.getPropertyOrder();
  }
}
