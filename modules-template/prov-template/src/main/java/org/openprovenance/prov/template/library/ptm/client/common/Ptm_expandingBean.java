// Generated automatically by ProvToolbox for template 'ptm_expanding'
// by class org.openprovenance.prov.template.compiler.CompilerBeanGenerator, method generateBean,
// in file CompilerBeanGenerator.java, at line 28
package org.openprovenance.prov.template.library.ptm.client.common;

/**
 * A Simple Bean that captures all variables of this template.
 */
public class Ptm_expandingBean {
  public final String isA = "ptm_expanding";

  /**
   * document: The document resulting from template expansion (expected type: xsd:string)
   */
  public String document;

  /**
   * template: The template to be expanded (expected type: xsd:string)
   */
  public String template;

  /**
   * bindings: The bindings used in expansion (expected type: xsd:string)
   */
  public String bindings;

  /**
   * agent: The agent controlling the expansion (expected type: xsd:int)
   */
  public Integer agent;

  /**
   * expanding: The activity of expanding the template (expected type: xsd:int)
   */
  public Integer expanding;

  /**
   * email: The agent's email (expected type: xsd:string)
   */
  public String email;

  /**
   * time: Time when the transformed file is created (expected type: xsd:dateTime)
   */
  public String time;

  public <T> T process(Ptm_expandingProcessor<T> __processor) {
    return __processor.process(document, template, bindings, agent, expanding, email, time);
  }
}
