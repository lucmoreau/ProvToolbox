// Generated Automatically by ProvToolbox for template "test"
package org.example;

import java.lang.String;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.expander.deprecated.OldBindings;
import org.openprovenance.prov.template.expander.BindingsBeanInterface;

public class TestBindingsBeanInterface implements BindingsBeanInterface {
  private final OldBindings bindings;

  private final ProvFactory pf;

  public TestBindingsBeanInterface(ProvFactory pf) {
    this.pf = pf;
    this.bindings = new OldBindings(pf);
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

  public OldBindings getBindings() {
    return bindings;
  }

  public String getTemplate() {
    return "my/template";
  }
}
