// Generated automatically by ProvToolbox for template 'template_block'
// by class org.openprovenance.prov.template.compiler.expansion.CompilerExpansionBuilder, method generateBuilderInterfaceSpecification_aux,
// in file CompilerExpansionBuilder.java, at line 73
package org.example.templates.block;

import java.lang.Object;
import org.openprovenance.prov.model.QualifiedName;

public interface Template_blockBuilderInterface<T> {
  T call(QualifiedName operation, QualifiedName operation_type, QualifiedName agent,
      QualifiedName consumed1, Object consumed_value1, QualifiedName consumed2,
      Object consumed_value2, QualifiedName produced, QualifiedName produced_type,
      Object produced_value);
}
