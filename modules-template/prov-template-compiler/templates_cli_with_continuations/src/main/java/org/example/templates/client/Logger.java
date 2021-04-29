// Generated Automatically by ProvToolbox (org.openprovenance.prov.template.compiler.CompilerLogger) for templates config templates
package org.example.templates.client;

import java.lang.Integer;
import java.lang.String;
import org.example.templates.block.client.Template_blockBean;
import org.example.templates.block.client.Template_blockBuilder;
import org.openprovenance.prov.client.Builder;
import org.openprovenance.prov.client.LoggerInterface;

public class Logger implements LoggerInterface {
  public static final Template_blockBuilder ___template_block = new Template_blockBuilder();

  public static final Builder[] __builders = new Builder[] {___template_block};

  public Builder[] getBuilders() {
    return __builders;
  }

  /**
   * No @documentation.
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
   * @return not documented. */
  public static String logTemplate_block(String operation, String operation_type, String agent,
      String consumed1, String consumed_value1, String consumed2, Integer consumed_value2,
      String produced, String produced_type, Integer produced_value) {
      return ___template_block.csvConverter().invoke(operation, operation_type, agent, consumed1, consumed_value1, consumed2, consumed_value2, produced, produced_type, produced_value);
  }

  /**
   * No @documentation.
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
   * @return not documented. */
  public static Template_blockBean beanTemplate_block(String operation, String operation_type,
      String agent, String consumed1, String consumed_value1, String consumed2,
      Integer consumed_value2, String produced, String produced_type, Integer produced_value) {
      return Template_blockBuilder.beanConverter().invoke(operation, operation_type, agent, consumed1, consumed_value1, consumed2, consumed_value2, produced, produced_type, produced_value);
  }
}
