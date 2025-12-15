// Generated automatically by ProvToolbox for template 'ptm_merging'
// by class org.openprovenance.prov.template.compiler.CompilerBeanGenerator, method generateBean,
// in file CompilerBeanGenerator.java, at line 38
package org.openprovenance.prov.template.library.ptm_copy.client.common;


/**
 * A Simple Bean that captures all variables of this template.
 */
public class Ptm_mergingBean {
  /**
   * The template name
   */
  public final String isA = "ptm_merging";

  /**
   * document: The document resulting from template merging (expected type: xsd:string)
   */
  public String document;

  /**
   * provenance: The provenance of the document resulting from the template merging (expected type: xsd:string)
   */
  public String provenance;

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
   * merging: The activity of merging the templates (expected type: xsd:int)
   */
  public Integer merging;

  /**
   * email: The agent's email (expected type: xsd:string)
   */
  public String email;

  /**
   * time: Time when the transformed file is created (expected type: xsd:dateTime)
   */
  public String time;

  public <T> T process(Ptm_mergingProcessor<T> __processor) {
    return __processor.process(document,provenance,template1,template2,agent,merging,email,time);
  }
}
