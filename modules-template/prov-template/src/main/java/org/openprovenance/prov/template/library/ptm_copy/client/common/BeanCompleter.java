// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerBeanCompleter, method generateBeanCompleter,
// in file CompilerBeanCompleter.java, at line 27
package org.openprovenance.prov.template.library.ptm_copy.client.common;

import java.util.Map;

public class BeanCompleter implements BeanProcessor {
  final Map<String, Object> m;

  final Getter getter;

  public BeanCompleter(Map<String, Object> m) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerBeanCompleter, method generateBeanCompleter
    // in file CompilerBeanCompleter.java, at line 68
    this.m = m;
    // The following code implements this assignment, in a way that jsweet can compile
    // this.getter = this::getMap
    this.getter = new Getter() {
      public <T> T get(Class<T> cl, String col) {
        return getMap(cl, col);
      }
    };
  }

  public BeanCompleter(Getter getter) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerBeanCompleter, method generateBeanCompleter
    // in file CompilerBeanCompleter.java, at line 90
    this.m = null;
    this.getter = (Getter) getter;
  }

  public <T> T getMap(Class<T> cl, String key) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerBeanCompleter, method generateBeanCompleter
    // in file CompilerBeanCompleter.java, at line 47
    return (T) m.get(key);
  }

  public final Ptm_expandingBean process(Ptm_expandingBean bean) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerBeanCompleter, method generateBeanCompleter
    // in file CompilerBeanCompleter.java, at line 107
    bean.document= getter.get(String.class,"document");
    bean.provenance= getter.get(String.class,"provenance");
    bean.expanding= getter.get(Integer.class,"expanding");
    return bean;
  }

  public final Ptm_mexpandingBean process(Ptm_mexpandingBean bean) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerBeanCompleter, method generateBeanCompleter
    // in file CompilerBeanCompleter.java, at line 107
    bean.template= getter.get(String.class,"template");
    bean.provenance= getter.get(String.class,"provenance");
    bean.mexpanding= getter.get(Integer.class,"mexpanding");
    return bean;
  }

  public boolean next() {
    return true;
  }

  public interface Getter {
    <T> T get(Class<T> cl, String col);
  }
}
