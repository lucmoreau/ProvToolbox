// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.CompilerTypeConverter, method generateTypeConverter,
// in file CompilerTypeConverter.java, at line 31
package org.openprovenance.prov.template.library.ptm_copy.client.integrator;

import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_expandingBuilder;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mexpandingBuilder;

import java.util.HashMap;
import java.util.Map;

public class TypeConverter<T> {
  final Getter<T> getter;

  public TypeConverter(Getter<T> getter) {
    this.getter = getter;
  }

  public Map<String, T> process(Ptm_expandingBuilder builder) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerTypeConverter, method createProcessMethod
    // in file CompilerTypeConverter.java, at line 103
    Map<String, T> m=new HashMap<String, T>();
    m.put("document", getter.getString("document"));
    m.put("provenance", getter.getString("provenance"));
    m.put("template", getter.getString("template"));
    m.put("bindings", getter.getString("bindings"));
    m.put("agent", getter.getObject("agent"));
    m.put("expanding", getter.getObject("expanding"));
    m.put("email", getter.getString("email"));
    m.put("time", getter.getTimestamp("time"));
    return m;
  }

  public Map<String, T> process(Ptm_mexpandingBuilder builder) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerTypeConverter, method createProcessMethod
    // in file CompilerTypeConverter.java, at line 103
    Map<String, T> m=new HashMap<String, T>();
    m.put("template", getter.getString("template"));
    m.put("provenance", getter.getString("provenance"));
    m.put("mtemplate", getter.getString("mtemplate"));
    m.put("bindings", getter.getString("bindings"));
    m.put("agent", getter.getObject("agent"));
    m.put("mexpanding", getter.getObject("mexpanding"));
    m.put("email", getter.getString("email"));
    m.put("time", getter.getTimestamp("time"));
    return m;
  }

  public interface Getter<T> {
    T getString(String col);

    T getObject(String col);

    T getTimestamp(String col);
  }
}