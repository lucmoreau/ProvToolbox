// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerInputOutputProcessor, method generateInputOutputProcessor,
// in file CompilerInputOutputProcessor.java, at line 25
package org.openprovenance.prov.template.library.ptm_copy.client.integrator;

public interface InputOutputProcessor {
  Ptm_instantiatingOutputs process(Ptm_instantiatingInputs bean);

  Ptm_mexpandingOutputs process(Ptm_mexpandingInputs bean);
}
