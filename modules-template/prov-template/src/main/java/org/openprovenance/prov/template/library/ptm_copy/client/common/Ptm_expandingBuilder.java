// Generated automatically by ProvToolbox for template configuration 'template_library'
// by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonLib_aux,
// in file CompilerCommon.java, at line 62
package org.openprovenance.prov.template.library.ptm_copy.client.common;

import org.openprovenance.prov.client_copy.Builder;
import org.openprovenance.prov.client_copy.ProcessorArgsInterface;
import org.openprovenance.prov.client_copy.SQL;
import org.openprovenance.prov.template.library.ptm_copy.client.integrator.Ptm_expandingIntegratorBuilder;

import java.util.HashMap;
import java.util.Map;

public class Ptm_expandingBuilder implements Builder, SQL {
  public static String[] propertyOrder = new String[] { "isA" ,  "document",  "provenance",  "template",  "bindings",  "agent",  "expanding",  "email",  "time"};

  private static String _sqlInsert1 = "INSERT INTO  ptm_expanding (document, provenance, template, bindings, agent, expanding, email, time)";

  public static final Ptm_expandingIntegratorBuilder __integrator = new Ptm_expandingIntegratorBuilder();

  public static String[] outputs = new String[] {  "document",  "provenance",  "expanding"};

  public static String[] inputs = new String[] {  "template",  "bindings",  "agent",  "email",  "time"};

  public static String[] compulsoryInputs = new String[] {  "template",  "bindings"};

  public static final Map<Integer, int[]> __successors = __getSuccessors();

  public static final Map<Integer, int[]> __successors2 = __getTypedSuccessors();

  public static final int[] __nodes = __getNodes();

  public final Ptm_expandingProcessor<Ptm_expandingBean> aArgs2BeanConverter =  (String __document, String __provenance, String __template, String __bindings, Integer __agent, Integer __expanding, String __email, String __time) -> { return toBean( __document,  __provenance,  __template,  __bindings,  __agent,  __expanding,  __email,  __time); };

  /**
   * Generated by method org.openprovenance.prov.template.compiler.common.CompilerCommon.generateField4aBeanConverter2()
   */
  public final ProcessorArgsInterface<Ptm_expandingBean> aRecord2BeanConverter =  (Object [] record) -> { return toBean(record); };

  /**
   * Generated by method org.openprovenance.prov.template.compiler.common.CompilerCommon.generateField4aSQLConverter2()
   */
  public final Ptm_expandingProcessor<String> aBean2SqlConverter = bean2sql();

  public final Ptm_expandingProcessor<String> aArgs2CsVConverter = args2csv();

  public final ProcessorArgsInterface<String> aRecord2SqlConverter =  (Object [] record) -> { return toBean(record).process(aBean2SqlConverter); };

  /**
   * Generated by method org.openprovenance.prov.template.compiler.common.CompilerCommon.generateField4aRecord2CsvConverter()
   */
  public final ProcessorArgsInterface<String> aRecord2CsvConverter =  (Object [] record) -> { return toBean(record).process(aArgs2CsVConverter); };

  public String getName() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateNameAccessor
    // in file CompilerCommon.java, at line 178
    return "ptm_expanding";
  }

  public String[] getPropertyOrder() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generatePropertyOrderMethod
    // in file CompilerCommon.java, at line 843
    return propertyOrder;
  }

  public int[] getNodes() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethodGetNodes
    // in file CompilerCommon.java, at line 819
    return __nodes;
  }

  public Map<Integer, int[]> getSuccessors() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethodGetSuccessors
    // in file CompilerCommon.java, at line 888
    return __successors;
  }

  public Map<Integer, int[]> getTypedSuccessors() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethodGetTypedSuccessors
    // in file CompilerCommon.java, at line 901
    return __successors2;
  }

  public ProcessorArgsInterface<String> record2csv(Object[] record) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateRecordCsvProcessorMethod
    // in file CompilerCommon.java, at line 876
    return aRecord2CsvConverter;
  }

  public String getSQLInsert() {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerSQL, method generateSQLInsert
    // in file CompilerSQL.java, at line 208
    return _sqlInsert1;
  }

  public String getSQLInsertStatement() {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerSQL, method generateSQLInsertStatement
    // in file CompilerSQL.java, at line 222
    return _sqlInsert1+" VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
  }

  public final Ptm_expandingIntegratorBuilder getIntegrator() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonLib_aux
    // in file CompilerCommon.java, at line 88
    return __integrator;
  }

  /**
   * logPtm_expanding client side logging method
   * @return Ptm_expandingProcessor
   */
  public Ptm_expandingProcessor<String> args2csv() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonCSVConverterMethod_aux
    // in file CompilerCommon.java, at line 197
    Ptm_expandingBuilder self=this;
    return (String __document, String __provenance, String __template, String __bindings, Integer __agent, Integer __expanding, String __email, String __time) -> { StringBuffer sb=new StringBuffer();self.logPtm_expanding(sb, __document,  __provenance,  __template,  __bindings,  __agent,  __expanding,  __email,  __time); return sb.toString(); };
  }

  /**
   * logPtm_expanding client side logging method
   * @return Ptm_expandingProcessor
   */
  public Ptm_expandingProcessor<String> bean2sql() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonSQLConverterMethod_aux
    // in file CompilerCommon.java, at line 425
    Ptm_expandingBuilder self=this;
    return (String __document, String __provenance, String __template, String __bindings, Integer __agent, Integer __expanding, String __email, String __time) -> { StringBuffer sb=new StringBuffer(); self.sqlTuple(sb, __document,  __provenance,  __template,  __bindings,  __agent,  __expanding,  __email,  __time); return sb.toString(); };
  }

  /**
   * Returns a converter from arguments to record
   * @return Ptm_expandingProcessor
   */
  public Ptm_expandingProcessor<Object[]> aArgs2RecordConverter() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateArgsToRecordMethod
    // in file CompilerCommon.java, at line 478
    return (String __document, String __provenance, String __template, String __bindings, Integer __agent, Integer __expanding, String __email, String __time) -> {  return new Object [] { getName(),  __document,  __provenance,  __template,  __bindings,  __agent,  __expanding,  __email,  __time}; };
  }

  /**
   * Returns a converter from Processor taking arguments to Processor taking record
   * @param __processor a transformer for this template
   * @param <T> type variable for the result of processor
   * @return Ptm_expandingProcessor&lt;T&gt;
   */
  public <T> Ptm_expandingProcessor<T> processorConverter(
      final ProcessorArgsInterface<T> __processor) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateProcessorConverter
    // in file CompilerCommon.java, at line 520
    return (String __document, String __provenance, String __template, String __bindings, Integer __agent, Integer __expanding, String __email, String __time) -> {  return __processor.process(new Object [] { getName(),  __document,  __provenance,  __template,  __bindings,  __agent,  __expanding,  __email,  __time}); };
  }

  /**
   * Returns a converter from Processor taking arguments to Processor taking record
   * @param processor a transformer for this template
   * @param <T> type variable for the result of processor
   * @return ProcessorArgsInterface&lt;T&gt;
   */
  public <T> ProcessorArgsInterface<T> processorConverter(
      final Ptm_expandingProcessor<T> processor) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateProcessorConverter2
    // in file CompilerCommon.java, at line 588
    return (Object [] record) -> {  return processor.process( (String) record[1],  (String) record[2],  (String) record[3],  (String) record[4],  (record[5]==null)?null:((record[5] instanceof String)?Integer.valueOf((String)(record[5])):(Integer)(record[5])),  (record[6]==null)?null:((record[6] instanceof String)?Integer.valueOf((String)(record[6])):(Integer)(record[6])),  (String) record[7],  (String) record[8]); };
  }

  /**
   * Apply method
   * @param processor a transformer for this template
   * @param record as an array of Objects
   * @param <T> type variable for the result of processor
   * @return an object of type T
   */
  public <T> T apply(Ptm_expandingProcessor<T> processor, Object[] record) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateApplyMethod
    // in file CompilerCommon.java, at line 651
    return toBean(record).process(processor);
  }

  public void logPtm_expanding_impure(StringBuffer sb, String __document, String __provenance,
      String __template, String __bindings, Integer __agent, Integer __expanding, String __email,
      String __time) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethod2
    // in file CompilerCommon.java, at line 708
    sb.append("[ptm_expanding,");
    if (__document==null) {
      sb.append(__document);
    } else {
      sb.append(__document);
    }
    sb.append(",");
    if (__provenance==null) {
      sb.append(__provenance);
    } else {
      sb.append(__provenance);
    }
    sb.append(",");
    if (__template==null) {
      sb.append(__template);
    } else {
      sb.append(__template);
    }
    sb.append(",");
    if (__bindings==null) {
      sb.append(__bindings);
    } else {
      sb.append(__bindings);
    }
    sb.append(",");
    if (__agent==null) {
    } else {
      sb.append("");
      sb.append(__agent);
    }
    sb.append(",");
    if (__expanding==null) {
    } else {
      sb.append("");
      sb.append(__expanding);
    }
    sb.append(",");
    if (__email==null) {
      sb.append(__email);
    } else {
      sb.append(__email);
    }
    sb.append(",");
    if (__time==null) {
      sb.append(__time);
    } else {
      sb.append(__time);
    }
    sb.append("]");
  }

  public void logPtm_expanding(StringBuffer sb, String __document, String __provenance,
      String __template, String __bindings, Integer __agent, Integer __expanding, String __email,
      String __time) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethod2
    // in file CompilerCommon.java, at line 708
    sb.append("ptm_expanding,");
    if (__document==null) {
    } else {
      sb.append(__document);
    }
    sb.append(",");
    if (__provenance==null) {
    } else {
      sb.append(__provenance);
    }
    sb.append(",");
    if (__template==null) {
    } else {
      sb.append(__template);
    }
    sb.append(",");
    if (__bindings==null) {
    } else {
      sb.append(__bindings);
    }
    sb.append(",");
    if (__agent==null) {
    } else {
      sb.append("");
      sb.append(__agent);
    }
    sb.append(",");
    if (__expanding==null) {
    } else {
      sb.append("");
      sb.append(__expanding);
    }
    sb.append(",");
    if (__email==null) {
    } else {
      sb.append(__email);
    }
    sb.append(",");
    if (__time==null) {
    } else {
      sb.append(__time);
    }
    sb.append("");
  }

  public static int[] __getNodes() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethod3static
    // in file CompilerCommon.java, at line 782
    return new int[] {1, 2, 3, 4, 5, 6};
  }

  public static Map<Integer, int[]> __getSuccessors() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethod4static
    // in file CompilerCommon.java, at line 916
    Map<Integer, int[]> table = new HashMap<Integer, int[]>();
    table.put(1,new int[] { });
    table.put(2,new int[] { });
    table.put(3,new int[] { 1});
    table.put(4,new int[] { 2});
    table.put(5,new int[] { });
    table.put(6,new int[] { });
    return table;
  }

  public static Map<Integer, int[]> __getTypedSuccessors() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethod5static
    // in file CompilerCommon.java, at line 1043
    Map<Integer, int[]> table = new HashMap<Integer, int[]>();
    table.put(1,new int[] { });
    table.put(2,new int[] { });
    table.put(3,new int[] { 1, 9 /* PROV_DERIVATION */});
    table.put(4,new int[] { 2, 9 /* PROV_DERIVATION */});
    table.put(5,new int[] { 1, 11 /* PROV_ATTRIBUTION */, 2, 11 /* PROV_ATTRIBUTION */});
    table.put(6,new int[] { });
    return table;
  }

  public static String[] __getAllTypes() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCommonMethod6static
    // in file CompilerCommon.java, at line 1174
    String [] table = new String[10];
    table[0]="http://www.w3.org/ns/prov#Entity";
    table[1]="http://www.w3.org/ns/prov#Activity";
    table[2]="http://www.w3.org/ns/prov#Agent";
    table[3]="http://openprovenance.org/ns/ptm#Bindings";
    table[4]="http://openprovenance.org/ns/ptm#CompactBindings";
    table[5]="http://openprovenance.org/ns/ptm#ExpandingTemplate";
    table[6]="http://openprovenance.org/ns/ptm#ProvDocument";
    table[7]="http://openprovenance.org/ns/ptm#ProvTemplate";
    table[8]="http://schema.org/Person";
    table[9]="http://www.w3.org/ns/prov#Person";
    return table;
  }

  public String[] getOutputs() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateOutputsMethod
    // in file CompilerCommon.java, at line 851
    return outputs;
  }

  public String[] getCompulsoryInputs() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateCompulsoryInputsMethod
    // in file CompilerCommon.java, at line 859
    return compulsoryInputs;
  }

  public String[] getInputs() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateInputsMethod
    // in file CompilerCommon.java, at line 867
    return inputs;
  }

  /**
   * Converter to bean of type Ptm_expandingBean for template ptm_expanding.
   * @param record an array of objects
   * @return a bean
   */
  public Ptm_expandingBean toBean(Object[] record) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateFactoryMethodToBeanWithArray
    // in file CompilerCommon.java, at line 1326
    Ptm_expandingBean bean=new Ptm_expandingBean();
    bean.document=(String) record[1];
    bean.provenance=(String) record[2];
    bean.template=(String) record[3];
    bean.bindings=(String) record[4];
    bean.agent=(record[5]==null)?null:((record[5] instanceof String)?Integer.valueOf((String)(record[5])):(Integer)(record[5]));
    bean.expanding=(record[6]==null)?null:((record[6] instanceof String)?Integer.valueOf((String)(record[6])):(Integer)(record[6]));
    bean.email=(String) record[7];
    bean.time=(String) record[8];
    return bean;
  }

  public static Ptm_expandingBean toBean(String __document, String __provenance, String __template,
      String __bindings, Integer __agent, Integer __expanding, String __email, String __time) {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateFactoryMethodWithBean
    // in file CompilerCommon.java, at line 1230
    Ptm_expandingBean bean=new Ptm_expandingBean();
    bean.document=__document;
    bean.provenance=__provenance;
    bean.template=__template;
    bean.bindings=__bindings;
    bean.agent=__agent;
    bean.expanding=__expanding;
    bean.email=__email;
    bean.time=__time;
    return bean;
  }

  public Ptm_expandingBean newBean() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateNewBean
    // in file CompilerCommon.java, at line 1374
    Ptm_expandingBean bean=new Ptm_expandingBean();
    return bean;
  }

  public static Ptm_expandingBean examplar() {
    // Generated by class org.openprovenance.prov.template.compiler.common.CompilerCommon, method generateExamplarBean
    // in file CompilerCommon.java, at line 1395
    Ptm_expandingBean bean=new Ptm_expandingBean();
    bean.document="file/doc123-456.provn";
    bean.bindings="file/bindings456.json";
    bean.agent=Integer.valueOf("12345");
    bean.expanding=Integer.valueOf("12345");
    bean.template="file/template123.provn";
    bean.provenance="file/prov-doc123-456.prov-csv";
    bean.email="luc.moreau@kcl.ac.uk";
    bean.time="2023-10-27T15:51:25.826+01:00";
    return bean;
  }

  public void sqlTuple(StringBuffer sb, String __document, String __provenance, String __template,
      String __bindings, Integer __agent, Integer __expanding, String __email, String __time) {
    // Generated by class org.openprovenance.prov.template.compiler.CompilerSQL, method generateCommonSQLMethod2
    // in file CompilerSQL.java, at line 317
    sb.append("(");
    if (__document==null) {
      sb.append("''");
    } else {
      sb.append("'");
      sb.append(__document);
      sb.append("'");
    }
    sb.append(",");
    if (__provenance==null) {
      sb.append("''");
    } else {
      sb.append("'");
      sb.append(__provenance);
      sb.append("'");
    }
    sb.append(",");
    if (__template==null) {
      sb.append("''");
    } else {
      sb.append("'");
      sb.append(__template);
      sb.append("'");
    }
    sb.append(",");
    if (__bindings==null) {
      sb.append("''");
    } else {
      sb.append("'");
      sb.append(__bindings);
      sb.append("'");
    }
    sb.append(",");
    if (__agent==null) {
      sb.append("''");
    } else {
      sb.append("");
      sb.append(__agent);
    }
    sb.append(",");
    if (__expanding==null) {
      sb.append("''");
    } else {
      sb.append("");
      sb.append(__expanding);
    }
    sb.append(",");
    if (__email==null) {
      sb.append("''");
    } else {
      sb.append("'");
      sb.append(__email);
      sb.append("'");
    }
    sb.append(",");
    if (__time==null) {
      sb.append("''");
    } else {
      sb.append("'");
      sb.append(__time);
      sb.append("'");
    }
    sb.append(")");
  }
}
