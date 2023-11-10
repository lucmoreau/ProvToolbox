// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerLogger, method generateBuilderInterface,
// in file CompilerLogger.java, at line 156
package org.openprovenance.prov.client_copy;

import java.util.Map;

public interface Builder {
  int[] getNodes();

  Map<Integer, int[]> getSuccessors();

  Map<Integer, int[]> getTypedSuccessors();

  String getName();

  ProcessorArgsInterface<String> record2csv(Object[] record);

  String[] getPropertyOrder();
}
