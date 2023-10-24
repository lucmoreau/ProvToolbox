// Generated automatically by ProvToolbox for template 'ptm_expanding'
// by class org.openprovenance.prov.template.compiler.CompilerProcessor, method generateProcessor,
// in file CompilerProcessor.java, at line 37
package org.openprovenance.prov.template.library.ptm.client.common;

public interface Ptm_expandingProcessor<T> {
  /**
   * TODO doc.
   *
   * @param document: The document resulting from template expansion (expected type: xsd:string)
   * @param template: The template to be expanded (expected type: xsd:string)
   * @param bindings: The bindings used in expansion (expected type: xsd:string)
   * @param agent: The agent controlling the expansion (expected type: xsd:int)
   * @param expanding: The activity of expanding the template (expected type: xsd:int)
   * @param email: The agent's email (expected type: xsd:string)
   * @param time: Time when the transformed file is created (expected type: xsd:dateTime)
   * @return not specified
   */
  T process(String document, String template, String bindings, Integer agent, Integer expanding,
      String email, String time);
}
