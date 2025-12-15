// Generated automatically by ProvToolbox for template 'ptm_merging'
// by class org.openprovenance.prov.template.compiler.CompilerBeanGenerator, method generateBean,
// in file CompilerBeanGenerator.java, at line 38
package org.openprovenance.prov.template.library.ptm_copy.client.integrator;

/**
 * A Simple Bean that only contains the outputs of this template.
 */
public class Ptm_mergingOutputs {
  /**
   * The template name
   */
  public final String isA = "ptm_merging";

  /**
   * Allows for database key to be returned.
   */
  public Integer ID;

  /**
   * document: The document resulting from template merging (expected type: xsd:string)
   */
  public String document;

  /**
   * provenance: The provenance of the document resulting from the template merging (expected type: xsd:string)
   */
  public String provenance;

  /**
   * merging: The activity of merging the templates (expected type: xsd:int)
   */
  public Integer merging;
}
