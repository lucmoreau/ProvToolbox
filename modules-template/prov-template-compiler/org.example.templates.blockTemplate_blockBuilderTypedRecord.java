// Generated automatically by ProvToolbox for template 'template_block'
// by class org.openprovenance.prov.template.compiler.expansion.CompilerTypedRecord, method generateTypeDeclaration_aux,
// in file CompilerTypedRecord.java, at line 52
package org.example.templates.block;

import java.lang.Object;
import org.openprovenance.prov.model.QualifiedName;

public class Template_blockBuilderTypedRecord implements Template_blockBuilderInterface<Object[]> {
  public Object[] call(QualifiedName operation, QualifiedName operation_type, QualifiedName agent,
      QualifiedName consumed1, Object consumed_value1, QualifiedName consumed2,
      Object consumed_value2, QualifiedName produced, QualifiedName produced_type,
      Object produced_value) {
    return new Object[] { "template_block" , operation, operation_type, agent, consumed1, consumed_value1, consumed2, consumed_value2, produced, produced_type, produced_value};
  }
}
