// Generated automatically by ProvToolbox for template 'ptm_expanding'
// by class org.openprovenance.prov.template.compiler.integration.CompilerIntegrator, method generateIntegrator,
// in file CompilerIntegrator.java, at line 42
package org.openprovenance.prov.template.library.ptm_copy.client.integrator;

import org.openprovenance.prov.client_copy.ProcessorArgsInterface;

public class Ptm_expandingIntegratorBuilder {
  /**
   * Generated by method org.openprovenance.prov.template.compiler.common.CompilerCommon.generateField4aBeanConverter2()
   */
  public final ProcessorArgsInterface<Ptm_expandingInputs> aRecord2InputsConverter =  (Object [] record) -> { return toInputs(record); };

  /**
   * Returns a converter from Processor taking arguments to Processor taking record
   * @param __processor a transformer for this template
   * @param <T> type variable for the result of processor
   * @return Ptm_expandingIntegrator&lt;T&gt;
   */
  public <T> Ptm_expandingIntegrator<T> processorConverter(
      final ProcessorArgsInterface<T> __processor) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateProcessorConverter
    // in file CompilerCommon.java, at line 520
    return (String __template, String __bindings, Integer __agent, String __email, String __time) -> {  return __processor.process(new Object [] { getName(),  null,  null,  __template,  __bindings,  __agent,  null,  __email,  __time}); };
  }

  /**
   * Converter to bean of type Ptm_expandingInputs for template ptm_expanding.
   * @param record an array of objects
   * @return a bean
   */
  public Ptm_expandingInputs toInputs(Object[] record) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateFactoryMethodToBeanWithArray
    // in file CompilerCommon.java, at line 1326
    Ptm_expandingInputs bean=new Ptm_expandingInputs();
    bean.template=(String) record[3];
    bean.bindings=(String) record[4];
    bean.agent=(record[5]==null)?null:((record[5] instanceof String)?Integer.valueOf((String)(record[5])):(Integer)(record[5]));
    bean.email=(String) record[7];
    bean.time=(String) record[8];
    return bean;
  }

  public String getName() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateNameAccessor
    // in file CompilerCommon.java, at line 178
    return "ptm_expanding";
  }

  public Ptm_expandingOutputs newOutput() {
    // Generated by method org.openprovenance.prov.template.compiler.integration.CompilerIntegrator.generateNewOutputConstructor()
    return new Ptm_expandingOutputs();
  }
}
