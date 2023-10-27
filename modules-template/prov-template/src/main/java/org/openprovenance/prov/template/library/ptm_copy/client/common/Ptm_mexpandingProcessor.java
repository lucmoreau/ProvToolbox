// Generated automatically by ProvToolbox for template 'ptm_mexpanding'
// by class org.openprovenance.prov.template.compiler.CompilerProcessor, method generateProcessor,
// in file CompilerProcessor.java, at line 38
package org.openprovenance.prov.template.library.ptm_copy.client.common;

public interface Ptm_mexpandingProcessor<T> {
  /**
   * TODO doc.
   *
   * @param template: The template resulting from the mtemplate expansion (expected type: xsd:string)
   * @param provenance: The provenance of the document resulting from the template expansion (expected type: xsd:string)
   * @param mtemplate: The meta template to be expanded (expected type: xsd:string)
   * @param bindings: The bindings used in expansion (expected type: xsd:string)
   * @param agent: The agent controlling the expansion (expected type: xsd:int)
   * @param mexpanding: The activity of expanding the template (expected type: xsd:int)
   * @param email: The agent's email (expected type: xsd:string)
   * @param time: Time when the transformed file is created (expected type: xsd:dateTime)
   * @return not specified
   */
  T process(String template, String provenance, String mtemplate, String bindings, Integer agent,
      Integer mexpanding, String email, String time);
}
