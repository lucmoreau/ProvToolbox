// Generated automatically by ProvToolbox for template 'ptm_mexpanding'
// by class org.openprovenance.prov.template.compiler.CompilerBeanGenerator, method generateBean,
// in file CompilerBeanGenerator.java, at line 28
package org.openprovenance.prov.template.library.ptm.client.integrator;

/**
 * A Simple Bean that only contains the input of this template.
 */
public class Ptm_mexpandingInputs {
  public final String isA = "ptm_mexpanding";

  /**
   * mtemplate: The meta template to be expanded (expected type: xsd:string)
   */
  public String mtemplate;

  /**
   * bindings: The bindings used in expansion (expected type: xsd:string)
   */
  public String bindings;

  /**
   * agent: The agent controlling the expansion (expected type: xsd:int)
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
