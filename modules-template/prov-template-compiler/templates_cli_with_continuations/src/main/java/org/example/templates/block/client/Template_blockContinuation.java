// Generated Automatically by ProvToolbox (org.openprovenance.prov.template.compiler.CompilerContinuation) for template template_block
package org.example.templates.block.client;

import java.lang.Integer;
import java.lang.String;

public interface Template_blockContinuation<T> {
  /**
   * No @documentation
   *
   * @param operation The operation identifier (expected type: xsd:string)
   * @param operation_type The operation type (expected type: xsd:string)
   * @param agent The agent identifier (expected type: xsd:string)
   * @param consumed1 The identifier of the first consumed entity (expected type: xsd:string)
   * @param consumed_value1 The value of the first consumed entity (expected type: xsd:string)
   * @param consumed2 The identifier of the second consumed entity (expected type: xsd:string)
   * @param consumed_value2 The value of the second consumed entity (expected type: xsd:int)
   * @param produced The identifier of the produced entity (expected type: xsd:string)
   * @param produced_type The type of the produced entity (expected type: xsd:string)
   * @param produced_value The value of the produced entity (expected type: xsd:int)
   * @return not specified */
  T invoke(String operation, String operation_type, String agent, String consumed1,
      String consumed_value1, String consumed2, Integer consumed_value2, String produced,
      String produced_type, Integer produced_value);
}
