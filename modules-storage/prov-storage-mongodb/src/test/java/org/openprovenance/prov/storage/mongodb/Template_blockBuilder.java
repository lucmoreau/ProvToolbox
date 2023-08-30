// Generated Automatically by ProvToolbox for template "template_block"
package org.openprovenance.prov.storage.mongodb;
// Generated Automatically by ProvToolbox (org.openprovenance.prov.template.compiler.CompilerBuilder) for template template_block

import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.text.StringSubstitutor;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.template.expander.ExpandAction;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.interfaces.ProxyClientAccessorInterface;

public class Template_blockBuilder extends FileBuilder implements ProxyClientAccessorInterface {
  private final ProvFactory pf;

  private final ValueConverter vc;

  public final QualifiedName _Q_prov_value;

  public final QualifiedName _Q_prov_type;

  public final QualifiedName _Q_prov_QUALIFIED_NAME;

  public final QualifiedName _Q_prov_label;

  public Template_blockBuilder(ProvFactory pf) {
    this.pf = pf;
    this._Q_prov_type = pf.newQualifiedName("http://www.w3.org/ns/prov#","type","prov");
    this._Q_prov_QUALIFIED_NAME = pf.newQualifiedName("http://www.w3.org/ns/prov#","QUALIFIED_NAME","prov");
    this._Q_prov_label = pf.newQualifiedName("http://www.w3.org/ns/prov#","label","prov");
    this._Q_prov_value = pf.newQualifiedName("http://www.w3.org/ns/prov#","value","prov");
    this.vc = new ValueConverter(pf);
    register(this);
  }

  public Document generator(QualifiedName agent, QualifiedName produced, QualifiedName b,
                            QualifiedName consumed1, QualifiedName operation, QualifiedName consumed2,
                            Object operation_type, Object consumed_value1, Object produced_value, Object consumed_value2,
                            Object produced_type) {
    QualifiedName nullqn = null;
    Collection<Attribute> attrs=null;
    Document __C_document = pf.newDocument();
    Namespace namespace = pf.newNamespace();

    if (b==null) {
      b=ExpandAction.getUUIDQualifiedName2(pf);
      namespace.register(b.getPrefix(),b.getNamespaceURI());
    }
    Bundle b_ = pf.newNamedBundle(b, namespace,null);
    __C_document.getStatementOrBundle().add(b_);
    attrs= new LinkedList<>();
    if (operation_type!=null) attrs.add(pf.newAttribute(_Q_prov_type,operation_type,vc.getXsdType(operation_type)));
    if (operation!=null) b_.getStatement().add(pf.newActivity(operation,null,null, attrs));
    if (agent!=null) b_.getStatement().add(pf.newAgent(agent));
    b_.getStatement().add(pf.newWasAssociatedWith(nullqn,operation,agent,nullqn));
    attrs= new LinkedList<>();
    if (consumed_value1!=null) attrs.add(pf.newAttribute(_Q_prov_value,consumed_value1,vc.getXsdType(consumed_value1)));
    if (consumed1!=null) b_.getStatement().add(pf.newEntity(consumed1, attrs));
    attrs= new LinkedList<>();
    if (consumed_value2!=null) attrs.add(pf.newAttribute(_Q_prov_value,consumed_value2,vc.getXsdType(consumed_value2)));
    if (consumed2!=null) b_.getStatement().add(pf.newEntity(consumed2, attrs));
    if ((operation!=null) &&  (consumed1!=null)) b_.getStatement().add(pf.newUsed(nullqn,operation,consumed1,null));
    if ((operation!=null) &&  (consumed2!=null)) b_.getStatement().add(pf.newUsed(nullqn,operation,consumed2,null));
    attrs= new LinkedList<>();
    if (produced_type!=null) attrs.add(pf.newAttribute(_Q_prov_type,produced_type,vc.getXsdType(produced_type)));
    if (produced_value!=null) attrs.add(pf.newAttribute(_Q_prov_value,produced_value,vc.getXsdType(produced_value)));
    if (produced!=null) b_.getStatement().add(pf.newEntity(produced, attrs));
    if (produced!=null) b_.getStatement().add(pf.newWasGeneratedBy(nullqn,produced,operation));
    if ((produced!=null) &&  (consumed1!=null)) b_.getStatement().add(pf.newWasDerivedFrom(nullqn,produced,consumed1));
    if ((produced!=null) &&  (consumed2!=null)) b_.getStatement().add(pf.newWasDerivedFrom(nullqn,produced,consumed2));
    new ProvUtilities().updateNamespaces(__C_document);
    System.out.println("names " + __C_document.getNamespace());
    return __C_document;
  }

  @Override
  public String getName() {
    return "template_block";
  }

  // Luc Hand edited
  public Object getClientBuilder() {
    return new String[0];
  }

  public static void main(String[] args) {
    ProvFactory pf=org.openprovenance.prov.interop.InteropFramework.getDefaultFactory();
    Template_blockBuilder me=new Template_blockBuilder(pf);
    QualifiedName __var_agent=pf.newQualifiedName("http://example.org/","agent","ex");
    QualifiedName __var_produced=pf.newQualifiedName("http://example.org/","produced","ex");
    QualifiedName __var_b=pf.newQualifiedName("http://example.org/","b","ex");
    QualifiedName __var_consumed1=pf.newQualifiedName("http://example.org/","consumed1","ex");
    QualifiedName __var_operation=pf.newQualifiedName("http://example.org/","operation","ex");
    QualifiedName __var_consumed2=pf.newQualifiedName("http://example.org/","consumed2","ex");
    String __att_operation_type="test_operation_type";
    String __att_consumed_value1="test_consumed_value1";
    String __att_produced_value="12345";
    String __att_consumed_value2="12345";
    String __att_produced_type="test_produced_type";
    Document document=me.generator(__var_agent, __var_produced, __var_b, __var_consumed1, __var_operation, __var_consumed2, __att_operation_type, __att_consumed_value1, __att_produced_value, __att_consumed_value2, __att_produced_type);
    new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.Formats.ProvFormat.PROVN,document);
    document=me.make("v0", "v1", "v2", "v3", "v4", "v5", 6, "v7", "v8", 9);
    new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.Formats.ProvFormat.PROVN,document);
  }

  public Document make(String operation, String operation_type, String agent, String consumed1,
                       String consumed_value1, String consumed2, Integer consumed_value2, String produced,
                       String produced_type, Integer produced_value) {
    Document __C_document = null;
    Namespace __C_ns = new Namespace();
    StringSubstitutor subst= new StringSubstitutor(getVariableMap());
    __C_ns.register("ex",subst.replace("http://example.org/#"));
    QualifiedName __var_agent=(agent==null)?null:__C_ns.stringToQualifiedName("ex:agent/" + agent + "",pf);
    QualifiedName __var_produced=(produced==null)?null:__C_ns.stringToQualifiedName("ex:output/" + produced + "",pf);
    QualifiedName __var_b=null;
    QualifiedName __var_consumed1=(consumed1==null)?null:__C_ns.stringToQualifiedName("ex:" + consumed1 + "",pf);
    QualifiedName __var_operation=(operation==null)?null:__C_ns.stringToQualifiedName("ex:" + operation + "",pf);
    QualifiedName __var_consumed2=(consumed2==null)?null:__C_ns.stringToQualifiedName("ex:" + consumed2 + "",pf);
    QualifiedName __att_operation_type=(operation_type==null)?null:__C_ns.stringToQualifiedName("ex:type/" + operation_type + "",pf);
    QualifiedName __att_produced_type=(produced_type==null)?null:__C_ns.stringToQualifiedName("ex:Result" + produced_type + "",pf);
    __C_document = generator(__var_agent, __var_produced, __var_b, __var_consumed1, __var_operation, __var_consumed2, __att_operation_type, consumed_value1, produced_value, consumed_value2, __att_produced_type);
    return __C_document;
  }

  public Document make(Object[] record) {
    String operation=(String) record[1];
    String operation_type=(String) record[2];
    String agent=(String) record[3];
    String consumed1=(String) record[4];
    String consumed_value1=(String) record[5];
    String consumed2=(String) record[6];
    Integer consumed_value2=toInt(record[7]);
    String produced=(String) record[8];
    String produced_type=(String) record[9];
    Integer produced_value=toInt(record[10]);
    return make(operation, operation_type, agent, consumed1, consumed_value1, consumed2, consumed_value2, produced, produced_type, produced_value);
  }
}
