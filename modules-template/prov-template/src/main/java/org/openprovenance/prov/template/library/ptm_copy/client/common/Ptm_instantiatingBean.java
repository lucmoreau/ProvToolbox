// Generated automatically by ProvToolbox for template 'ptm_instantiating'
// by class org.openprovenance.prov.template.compiler.CompilerBeanGenerator, method generateBean,
// in file CompilerBeanGenerator.java, at line 33
package org.openprovenance.prov.template.library.ptm_copy.client.common;

/**
 * A Simple Bean that captures all variables of this template.
 */
public class Ptm_instantiatingBean {
  public final String isA = "ptm_instantiating";

  /**
   * document: The document resulting from template expansion (expected type: xsd:string)
   */
  public String document;

  /**
   * provenance: The provenance of the document resulting from the template expansion (expected type: xsd:string)
   */
  public String provenance;

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
   * instantiating: The activity of instantiating the template (expected type: xsd:int)
   */
  public Integer instantiating;

  /**
   * email: The agent's email (expected type: xsd:string)
   */
  public String email;

  /**
   * time: Time when the transformed file is created (expected type: xsd:dateTime)
   */
  public String time;

  public <T> T process(Ptm_instantiatingProcessor<T> __processor) {
    return __processor.process(document, provenance, template, bindings, agent, instantiating, email, time);
  }
}
