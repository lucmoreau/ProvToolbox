// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerInputOutputProcessor, method generateInputOutputProcessor,
// in file CompilerInputOutputProcessor.java, at line 23
package org.openprovenance.prov.template.library.ptm.client.integrator;

public interface InputOutputProcessor {
  Ptm_expandingOutputs process(Ptm_expandingInputs bean);

  Ptm_mexpandingOutputs process(Ptm_mexpandingInputs bean);
}
