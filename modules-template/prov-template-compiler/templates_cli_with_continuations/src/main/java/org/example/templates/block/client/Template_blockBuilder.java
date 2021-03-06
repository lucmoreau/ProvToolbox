// Generated Automatically by ProvToolbox (org.openprovenance.prov.template.compiler.CompilerClient) for template template_block
package org.example.templates.block.client;

import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuffer;
import java.util.HashMap;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;
import org.openprovenance.prov.client.Builder;
import org.openprovenance.prov.client.SQL;

public class Template_blockBuilder implements Builder, SQL {
  public static final HashMap<Integer, int[]> __successors = __getSuccessors();

  public static final int[] __nodes = __getNodes();

  private static String _sqlInsert1 = "INSERT INTO  template_block (operation, operation_type, agent, consumed1, consumed_value1, consumed2, consumed_value2, produced, produced_type, produced_value)";

  /**
   * logTemplate_block client side logging method
   * @param __operation class java.lang.String
   * @param __operation_type class java.lang.String
   * @param __agent class java.lang.String
   * @param __consumed1 class java.lang.String
   * @param __consumed_value1 class java.lang.String
   * @param __consumed2 class java.lang.String
   * @param __consumed_value2 class java.lang.Integer
   * @param __produced class java.lang.String
   * @param __produced_type class java.lang.String
   * @param __produced_value class java.lang.Integer
   * @return java.lang.String
   */

    public Template_blockContinuation<String> csvConverter () {
	Template_blockBuilder self=this;
	return (String __operation, String __operation_type, String __agent,
      String __consumed1, String __consumed_value1, String __consumed2, Integer __consumed_value2,
      String __produced, String __produced_type, Integer __produced_value) -> {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerClient.generateClientMethod_aux()
    StringBuffer sb=new StringBuffer();
    self.logTemplate_block(sb, __operation, __operation_type, __agent, __consumed1, __consumed_value1, __consumed2, __consumed_value2, __produced, __produced_type, __produced_value);
    return sb.toString();
	};
    };

    

  public void logTemplate_block_impure(StringBuffer sb, String __operation, String __operation_type,
      String __agent, String __consumed1, String __consumed_value1, String __consumed2,
      Integer __consumed_value2, String __produced, String __produced_type,
      Integer __produced_value) {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerClient.generateClientMethod2()
    sb.append("[\"template_block\",");
    if (__operation==null) {
      sb.append(__operation);
    } else {
      sb.append("\"");
      sb.append(__operation);
      sb.append("\"");
    }
    sb.append(",");
    if (__operation_type==null) {
      sb.append(__operation_type);
    } else {
      sb.append("\"");
      sb.append(__operation_type);
      sb.append("\"");
    }
    sb.append(",");
    if (__agent==null) {
      sb.append(__agent);
    } else {
      sb.append("\"");
      sb.append(__agent);
      sb.append("\"");
    }
    sb.append(",");
    if (__consumed1==null) {
      sb.append(__consumed1);
    } else {
      sb.append("\"");
      sb.append(__consumed1);
      sb.append("\"");
    }
    sb.append(",");
    if (__consumed_value1==null) {
      sb.append(__consumed_value1);
    } else {
      sb.append("\"");
      sb.append(StringEscapeUtils.escapeJavaScript(__consumed_value1));
      sb.append("\"");
    }
    sb.append(",");
    if (__consumed2==null) {
      sb.append(__consumed2);
    } else {
      sb.append("\"");
      sb.append(__consumed2);
      sb.append("\"");
    }
    sb.append(",");
    if (__consumed_value2==null) {
    } else {
      sb.append("");
      sb.append(__consumed_value2);
    }
    sb.append(",");
    if (__produced==null) {
      sb.append(__produced);
    } else {
      sb.append("\"");
      sb.append(__produced);
      sb.append("\"");
    }
    sb.append(",");
    if (__produced_type==null) {
      sb.append(__produced_type);
    } else {
      sb.append("\"");
      sb.append(__produced_type);
      sb.append("\"");
    }
    sb.append(",");
    if (__produced_value==null) {
    } else {
      sb.append("");
      sb.append(__produced_value);
    }
    sb.append("]");
  }

  public void logTemplate_block(StringBuffer sb, String __operation, String __operation_type,
      String __agent, String __consumed1, String __consumed_value1, String __consumed2,
      Integer __consumed_value2, String __produced, String __produced_type,
      Integer __produced_value) {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerClient.generateClientMethod2()
    sb.append("\"template_block\",");
    if (__operation==null) {
    } else {
      sb.append("\"");
      sb.append(__operation);
      sb.append("\"");
    }
    sb.append(",");
    if (__operation_type==null) {
    } else {
      sb.append("\"");
      sb.append(__operation_type);
      sb.append("\"");
    }
    sb.append(",");
    if (__agent==null) {
    } else {
      sb.append("\"");
      sb.append(__agent);
      sb.append("\"");
    }
    sb.append(",");
    if (__consumed1==null) {
    } else {
      sb.append("\"");
      sb.append(__consumed1);
      sb.append("\"");
    }
    sb.append(",");
    if (__consumed_value1==null) {
    } else {
      sb.append("\"");
      sb.append(StringEscapeUtils.escapeJavaScript(__consumed_value1));
      sb.append("\"");
    }
    sb.append(",");
    if (__consumed2==null) {
    } else {
      sb.append("\"");
      sb.append(__consumed2);
      sb.append("\"");
    }
    sb.append(",");
    if (__consumed_value2==null) {
    } else {
      sb.append("");
      sb.append(__consumed_value2);
    }
    sb.append(",");
    if (__produced==null) {
    } else {
      sb.append("\"");
      sb.append(__produced);
      sb.append("\"");
    }
    sb.append(",");
    if (__produced_type==null) {
    } else {
      sb.append("\"");
      sb.append(__produced_type);
      sb.append("\"");
    }
    sb.append(",");
    if (__produced_value==null) {
    } else {
      sb.append("");
      sb.append(__produced_value);
    }
    sb.append("");
  }

  public static int[] __getNodes() {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerClient.generateClientMethod3static()
    return new int[] {1, 2, 3, 4, 6, 8, 9};
  }

  public int[] getNodes() {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerClient.generateClientMethod3()
    return __nodes;
  }

  public static HashMap<Integer, int[]> __getSuccessors() {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerClient.generateClientMethod4static()
    HashMap<Integer, int[]> table = new HashMap<Integer, int[]>();
    table.put(1,new int[] { });
    table.put(2,new int[] { });
    table.put(3,new int[] { });
    table.put(4,new int[] { 8});
    table.put(6,new int[] { 8});
    table.put(8,new int[] { });
    table.put(9,new int[] { });
    return table;
  }

  public HashMap<Integer, int[]> getSuccessors() {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerClient.generateClientMethod4()
    return __successors;
  }

  @Override
  public String getName() {
    return "template_block";
  }

    public static Template_blockContinuation<Template_blockBean> beanConverter() {
	return (String __operation, String __operation_type,
      String __agent, String __consumed1, String __consumed_value1, String __consumed2,
      Integer __consumed_value2, String __produced, String __produced_type,
      Integer __produced_value) -> {
    Template_blockBean bean=new Template_blockBean();
    bean.operation=__operation;
    bean.operation_type=__operation_type;
    bean.agent=__agent;
    bean.consumed1=__consumed1;
    bean.consumed_value1=__consumed_value1;
    bean.consumed2=__consumed2;
    bean.consumed_value2=__consumed_value2;
    bean.produced=__produced;
    bean.produced_type=__produced_type;
    bean.produced_value=__produced_value;
    return bean;
	};
    }

  public Template_blockBean toBean(Object[] record) {
    Template_blockBean bean=new Template_blockBean();
    bean.operation=(String) record[1];
    bean.operation_type=(String) record[2];
    bean.agent=(String) record[3];
    bean.consumed1=(String) record[4];
    bean.consumed_value1=(String) record[5];
    bean.consumed2=(String) record[6];
    bean.consumed_value2=(record[7]==null)?0:Integer.valueOf((String)(record[7]));
    bean.produced=(String) record[8];
    bean.produced_type=(String) record[9];
    bean.produced_value=(record[10]==null)?0:Integer.valueOf((String)(record[10]));
    return bean;
  }

  public Template_blockBean newBean() {
    Template_blockBean bean=new Template_blockBean();
    return bean;
  }

  public static Template_blockBean examplar() {
    Template_blockBean bean=new Template_blockBean();
    bean.agent="example_agent";
    bean.produced="example_produced";
    bean.consumed1="example_consumed1";
    bean.operation="example_operation";
    bean.consumed2="example_consumed2";
    bean.operation_type="test_operation_type";
    bean.consumed_value1="test_consumed_value1";
    bean.produced_value=Integer.valueOf("12345");
    bean.consumed_value2=Integer.valueOf("12345");
    bean.produced_type="test_produced_type";
    return bean;
  }

  public String getSQLInsert() {
    return _sqlInsert1;
  }

  public String getSQLInsertStatement() {
    return _sqlInsert1+" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
  }

  /**
   * sqlTuple client side logging method
   * @param __operation class java.lang.String
   * @param __operation_type class java.lang.String
   * @param __agent class java.lang.String
   * @param __consumed1 class java.lang.String
   * @param __consumed_value1 class java.lang.String
   * @param __consumed2 class java.lang.String
   * @param __consumed_value2 class java.lang.Integer
   * @param __produced class java.lang.String
   * @param __produced_type class java.lang.String
   * @param __produced_value class java.lang.Integer
   * @return java.lang.String
   */
  public  Template_blockContinuation<String> sqlConverter () {
	Template_blockBuilder self=this;
	return (String __operation, String __operation_type, String __agent,
      String __consumed1, String __consumed_value1, String __consumed2, Integer __consumed_value2,
      String __produced, String __produced_type, Integer __produced_value) -> {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerClient.generateClientMethod_aux()
    StringBuffer sb=new StringBuffer();
    self.sqlTuple(sb, __operation, __operation_type, __agent, __consumed1, __consumed_value1, __consumed2, __consumed_value2, __produced, __produced_type, __produced_value);
    return sb.toString();
	};
	    
  }

  public void sqlTuple(StringBuffer sb, String __operation, String __operation_type, String __agent,
      String __consumed1, String __consumed_value1, String __consumed2, Integer __consumed_value2,
      String __produced, String __produced_type, Integer __produced_value) {
    // Generated by method org.openprovenance.prov.template.compiler.CompilerSQL.generateClientSQLMethod2()
    sb.append("(");
    if (__operation==null) {
    } else {
      sb.append("'");
      sb.append(__operation);
      sb.append("'");
    }
    sb.append(",");
    if (__operation_type==null) {
    } else {
      sb.append("'");
      sb.append(__operation_type);
      sb.append("'");
    }
    sb.append(",");
    if (__agent==null) {
    } else {
      sb.append("'");
      sb.append(__agent);
      sb.append("'");
    }
    sb.append(",");
    if (__consumed1==null) {
    } else {
      sb.append("'");
      sb.append(__consumed1);
      sb.append("'");
    }
    sb.append(",");
    if (__consumed_value1==null) {
    } else {
      sb.append("'");
      sb.append(StringEscapeUtils.escapeJavaScript(__consumed_value1));
      sb.append("'");
    }
    sb.append(",");
    if (__consumed2==null) {
    } else {
      sb.append("'");
      sb.append(__consumed2);
      sb.append("'");
    }
    sb.append(",");
    if (__consumed_value2==null) {
    } else {
      sb.append("");
      sb.append(__consumed_value2);
    }
    sb.append(",");
    if (__produced==null) {
    } else {
      sb.append("'");
      sb.append(__produced);
      sb.append("'");
    }
    sb.append(",");
    if (__produced_type==null) {
    } else {
      sb.append("'");
      sb.append(__produced_type);
      sb.append("'");
    }
    sb.append(",");
    if (__produced_value==null) {
    } else {
      sb.append("");
      sb.append(__produced_value);
    }
    sb.append("");
  }
}
