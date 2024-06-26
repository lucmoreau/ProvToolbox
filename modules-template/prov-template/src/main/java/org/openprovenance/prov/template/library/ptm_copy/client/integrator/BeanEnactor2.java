// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerBeanEnactor2, method generateBeanEnactor2,
// in file CompilerBeanEnactor2.java, at line 25
package org.openprovenance.prov.template.library.ptm_copy.client.integrator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class BeanEnactor2<RESULT> implements InputOutputProcessor {
  private final InputProcessor checker;

  private final EnactorImplementation<RESULT> realiser;

  public BeanEnactor2(EnactorImplementation<RESULT> realiser, InputProcessor checker) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerBeanEnactor2, method generateBeanEnactor2
    // in file CompilerBeanEnactor2.java, at line 84
    this.realiser = realiser;
    this.checker = checker;
  }

  public Ptm_expandingOutputs process(Ptm_expandingInputs bean) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerBeanEnactor2, method generateBeanEnactor2
    // in file CompilerBeanEnactor2.java, at line 107
    return realiser.generic_enact(new Ptm_expandingOutputs(),bean,
                        b -> checker.process(b),
                        (sb,b) -> new QueryInvoker2(sb).process(b),
                        (rs,b) -> realiser.beanCompleterFactory(rs).process(b));
  }

  public Ptm_mexpandingOutputs process(Ptm_mexpandingInputs bean) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerBeanEnactor2, method generateBeanEnactor2
    // in file CompilerBeanEnactor2.java, at line 107
    return realiser.generic_enact(new Ptm_mexpandingOutputs(),bean,
                        b -> checker.process(b),
                        (sb,b) -> new QueryInvoker2(sb).process(b),
                        (rs,b) -> realiser.beanCompleterFactory(rs).process(b));
  }

  public interface EnactorImplementation<RESULT> {
    <IN, OUT> OUT generic_enact(OUT output, IN bean, Consumer<IN> checker,
        BiConsumer<StringBuilder, IN> queryInvoker, BiConsumer<RESULT, OUT> completeBean);

    BeanCompleter2 beanCompleterFactory(RESULT rs);
  }
}
