// Generated automatically by ProvToolbox for template 'ptm_expanding'
// by class org.openprovenance.prov.template.compiler.CompilerProcessor, method generateProcessor,
// in file CompilerProcessor.java, at line 37
package org.openprovenance.prov.template.library.ptm.client.integrator;

public interface Ptm_expandingIntegrator<T> {
  /**
   * TODO doc.
   *
   * @param template: The template to be expanded (expected type: xsd:string)
   * @param bindings: The bindings used in expansion (expected type: xsd:string)
   * @param agent: The agent controlling the expansion (expected type: xsd:int)
   * @param email: The agent's email (expected type: xsd:string)
   * @param time: Time when the transformed file is created (expected type: xsd:dateTime)
   * @return not specified
   */
  T process(String template, String bindings, Integer agent, String email, String time);
}
