// Generated Automatically by ProvToolbox (org.openprovenance.prov.template.compiler.CompilerSimpleBean) for template template_block
package org.example.templates.block.client;

import java.lang.Integer;
import java.lang.String;

public class Template_blockBean {
  public final String isA = "template_block";

  /**
   * operation: The operation identifier (expected type: xsd:string)
   */
  public String operation;

  /**
   * operation_type: The operation type (expected type: xsd:string)
   */
  public String operation_type;

  /**
   * agent: The agent identifier (expected type: xsd:string)
   */
  public String agent;

  /**
   * consumed1: The identifier of the first consumed entity (expected type: xsd:string)
   */
  public String consumed1;

  /**
   * consumed_value1: The value of the first consumed entity (expected type: xsd:string)
   */
  public String consumed_value1;

  /**
   * consumed2: The identifier of the second consumed entity (expected type: xsd:string)
   */
  public String consumed2;

  /**
   * consumed_value2: The value of the second consumed entity (expected type: xsd:int)
   */
  public Integer consumed_value2;

  /**
   * produced: The identifier of the produced entity (expected type: xsd:string)
   */
  public String produced;

  /**
   * produced_type: The type of the produced entity (expected type: xsd:string)
   */
  public String produced_type;

  /**
   * produced_value: The value of the produced entity (expected type: xsd:int)
   */
  public Integer produced_value;

  public <T> T invoke(Template_blockContinuation<T> continuation) {
    return continuation.invoke(operation, operation_type, agent, consumed1, consumed_value1, consumed2, consumed_value2, produced, produced_type, produced_value);
  }
}
