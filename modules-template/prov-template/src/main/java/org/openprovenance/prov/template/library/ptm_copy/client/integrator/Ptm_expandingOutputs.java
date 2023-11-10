// Generated automatically by ProvToolbox for template 'ptm_expanding'
// by class org.openprovenance.prov.template.compiler.CompilerBeanGenerator, method generateBean,
// in file CompilerBeanGenerator.java, at line 33
package org.openprovenance.prov.template.library.ptm_copy.client.integrator;

/**
 * A Simple Bean that only contains the outputs of this template.
 */
public class Ptm_expandingOutputs {
  public final String isA = "ptm_expanding";

  /**
   * Allows for database key to be returned.
   */
  public Integer ID;

  /**
   * document: The document resulting from template expansion (expected type: xsd:string)
   */
  public String document;

  /**
   * provenance: The provenance of the document resulting from the template expansion (expected type: xsd:string)
   */
  public String provenance;

  /**
   * expanding: The activity of expanding the template (expected type: xsd:int)
   */
  public Integer expanding;
}
