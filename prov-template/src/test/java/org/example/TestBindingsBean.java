// Generated Automatically by ProvToolbox for template "test"
package org.example;

import java.lang.String;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.Bindings;
import org.openprovenance.prov.template.BindingsBean;

public class TestBindingsBean implements BindingsBean {
  private final Bindings bindings;

  private final ProvFactory pf;

  public TestBindingsBean(ProvFactory pf) {
    this.pf = pf;
    this.bindings = new Bindings(pf);
  }

  public void addB(QualifiedName arg) {
    bindings.addVariable("b",arg);
  }

  public void addA(QualifiedName arg) {
    bindings.addVariable("a",arg);
  }

  public void addC(QualifiedName arg) {
    bindings.addAttribute("c",arg);
  }

  public void addC(String arg) {
    bindings.addAttribute("c",arg);
  }

  public Bindings getBindings() {
    return bindings;
  }
}
