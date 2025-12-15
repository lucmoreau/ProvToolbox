// Generated automatically by ProvToolbox for template 'ptm_merging'
// by class org.openprovenance.prov.template.compiler.CompilerProcessor, method generateProcessor,
// in file CompilerProcessor.java, at line 38
package org.openprovenance.prov.template.library.ptm_copy.client.common;

public interface Ptm_mergingProcessor<T> {
  /**
   * TODO doc.
   *
   * @param document: The document resulting from template merging (expected type: xsd:string)
   * @param provenance: The provenance of the document resulting from the template merging (expected type: xsd:string)
   * @param template1: The first template to be merged (expected type: xsd:string)
   * @param template2: The second template to be merged (expected type: xsd:string)
   * @param agent: The agent controlling the merge (expected type: xsd:int)
   * @param merging: The activity of merging the templates (expected type: xsd:int)
   * @param email: The agent's email (expected type: xsd:string)
   * @param time: Time when the transformed file is created (expected type: xsd:dateTime)
   * @return not specified
   */
  T process(String document, String provenance, String template1, String template2, Integer agent,
      Integer merging, String email, String time);
}
