// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerTemplateInvoker, method generateTemplateInvoker,
// in file CompilerTemplateInvoker.java, at line 27
package org.openprovenance.prov.template.library.ptm.client.integrator;

import java.util.Map;
import java.util.function.BiFunction;

public abstract class TemplateInvoker implements InputOutputProcessor {
  public Ptm_expandingOutputs process(Ptm_expandingInputs bean) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerTemplateInvoker, method generateTemplateInvoker
    // in file CompilerTemplateInvoker.java, at line 42
    return generic_post_and_return(Ptm_expandingOutputs.class, bean, (m, o) -> new BeanCompleter2(m).process(o));
  }

  public Ptm_mexpandingOutputs process(Ptm_mexpandingInputs bean) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerTemplateInvoker, method generateTemplateInvoker
    // in file CompilerTemplateInvoker.java, at line 42
    return generic_post_and_return(Ptm_mexpandingOutputs.class, bean, (m, o) -> new BeanCompleter2(m).process(o));
  }

  public abstract <IN, OUT> OUT generic_post_and_return(Class<OUT> clazz, IN inputs,
      BiFunction<Map<String, Object>, OUT, OUT> completer);
}
