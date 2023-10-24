// Generated automatically by ProvToolbox for template 'ptm_mexpanding'
// by class org.openprovenance.prov.template.compiler.CompilerBeanGenerator, method generateBean,
// in file CompilerBeanGenerator.java, at line 28
package org.openprovenance.prov.template.library.ptm.client.common;

/**
 * A Simple Bean that captures all variables of this template.
 */
public class Ptm_mexpandingBean {
  public final String isA = "ptm_mexpanding";

  /**
   * template: The template resulting from the mtemplate expansion (expected type: xsd:string)
   */
  public String template;

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
   * mexpanding: The activity of expanding the template (expected type: xsd:int)
   */
  public Integer mexpanding;

  /**
   * email: The agent's email (expected type: xsd:string)
   */
  public String email;

  /**
   * time: Time when the transformed file is created (expected type: xsd:dateTime)
   */
  public String time;

  public <T> T process(Ptm_mexpandingProcessor<T> __processor) {
    return __processor.process(template, mtemplate, bindings, agent, mexpanding, email, time);
  }
}
