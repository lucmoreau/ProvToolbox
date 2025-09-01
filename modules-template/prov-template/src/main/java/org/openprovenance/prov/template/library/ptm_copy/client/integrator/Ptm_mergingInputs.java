// Generated automatically by ProvToolbox for template 'ptm_merging'
// by class org.openprovenance.prov.template.compiler.CompilerBeanGenerator, method generateBean,
// in file CompilerBeanGenerator.java, at line 38
package org.openprovenance.prov.template.library.ptm_copy.client.integrator;

/**
 * A Simple Bean that only contains the input of this template.
 */
public class Ptm_mergingInputs {
  /**
   * The template name
   */
  public final String isA = "ptm_merging";

  /**
   * template1: The first template to be merged (expected type: xsd:string)
   */
  public String template1;

  /**
   * template2: The second template to be merged (expected type: xsd:string)
   */
  public String template2;

  /**
   * agent: The agent controlling the merge (expected type: xsd:int)
   */
  public Integer agent;

  /**
   * email: The agent's email (expected type: xsd:string)
   */
  public String email;

  /**
   * time: Time when the transformed file is created (expected type: xsd:dateTime)
   */
  public String time;
}
