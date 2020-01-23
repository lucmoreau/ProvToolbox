// Generated Automatically by ProvToolbox for template "template_block"
package org.openprovenance.prov.storage.mongodb;

import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Collection;
import java.util.LinkedList;

import org.openprovenance.prov.interop.InteropFramework;
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

public class Template_blockBuilder extends FileBuilder {
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
    Document document = pf.newDocument();
    if (b==null) b=ExpandAction.getUUIDQualifiedName2(pf);
    Bundle b_ = pf.newNamedBundle(b,pf.newNamespace(),null);
    document.getStatementOrBundle().add(b_);
    attrs=new LinkedList<Attribute>();
    if (operation_type!=null) attrs.add(pf.newAttribute(_Q_prov_type,operation_type,vc.getXsdType(operation_type)));
    if (operation!=null) b_.getStatement().add(pf.newActivity(operation,null,null, attrs));
    if (agent!=null) b_.getStatement().add(pf.newAgent(agent));
    b_.getStatement().add(pf.newWasAssociatedWith(nullqn,operation,agent,nullqn));
    attrs=new LinkedList<Attribute>();
    if (consumed_value1!=null) attrs.add(pf.newAttribute(_Q_prov_value,consumed_value1,vc.getXsdType(consumed_value1)));
    if (consumed1!=null) b_.getStatement().add(pf.newEntity(consumed1, attrs));
    attrs=new LinkedList<Attribute>();
    if (consumed_value2!=null) attrs.add(pf.newAttribute(_Q_prov_value,consumed_value2,vc.getXsdType(consumed_value2)));
    if (consumed2!=null) b_.getStatement().add(pf.newEntity(consumed2, attrs));
    if ((operation!=null) &&  (consumed1!=null)) b_.getStatement().add(pf.newUsed(nullqn,operation,consumed1,null));
    if ((operation!=null) &&  (consumed2!=null)) b_.getStatement().add(pf.newUsed(nullqn,operation,consumed2,null));
    attrs=new LinkedList<Attribute>();
    if (produced_type!=null) attrs.add(pf.newAttribute(_Q_prov_type,produced_type,vc.getXsdType(produced_type)));
    if (produced_value!=null) attrs.add(pf.newAttribute(_Q_prov_value,produced_value,vc.getXsdType(produced_value)));
    if (produced!=null) b_.getStatement().add(pf.newEntity(produced, attrs));
    if (produced!=null) b_.getStatement().add(pf.newWasGeneratedBy(nullqn,produced,operation));
    if ((produced!=null) &&  (consumed1!=null)) b_.getStatement().add(pf.newWasDerivedFrom(nullqn,produced,consumed1));
    if ((produced!=null) &&  (consumed2!=null)) b_.getStatement().add(pf.newWasDerivedFrom(nullqn,produced,consumed2));
    new ProvUtilities().updateNamespaces(document);
    return document;
  }

  @Override
  public String getName() {
    return "template_block";
  }

  public static void main(String[] args) {
    ProvFactory pf= InteropFramework.getDefaultFactory();
    Template_blockBuilder me=new Template_blockBuilder(pf);
    QualifiedName agent=pf.newQualifiedName("http://example.org/","agent","ex");
    QualifiedName produced=pf.newQualifiedName("http://example.org/","produced","ex");
    QualifiedName b=pf.newQualifiedName("http://example.org/","b","ex");
    QualifiedName consumed1=pf.newQualifiedName("http://example.org/","consumed1","ex");
    QualifiedName operation=pf.newQualifiedName("http://example.org/","operation","ex");
    QualifiedName consumed2=pf.newQualifiedName("http://example.org/","consumed2","ex");
    String operation_type="test_operation_type";
    String consumed_value1="12345";
    String produced_value="12345";
    String consumed_value2="12345";
    String produced_type="test_produced_type";
    Document document=me.generator(agent, produced, b, consumed1, operation, consumed2, operation_type, consumed_value1, produced_value, consumed_value2, produced_type);
    new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.Formats.ProvFormat.PROVN,document);
    document=me.make("v0", "v1", "v2", "v3", 4, "v5", 6, "v7", "v8", 9);
    new org.openprovenance.prov.interop.InteropFramework().writeDocument(System.out,org.openprovenance.prov.interop.Formats.ProvFormat.PROVN,document);
  }

  public Document make(String operation, String operation_type, String agent, String consumed1,
      Integer consumed_value1, String consumed2, Integer consumed_value2, String produced,
      String produced_type, Integer produced_value) {
    Document document = null;
    Namespace ns = new Namespace();
    ns.register("ex","http://example.org/#");
    QualifiedName __agent=(agent==null)?null:ns.stringToQualifiedName("ex:agent/" + agent + "",pf);
    QualifiedName __produced=(produced==null)?null:ns.stringToQualifiedName("ex:output/" + produced + "",pf);
    QualifiedName __b=null;
    QualifiedName __consumed1=(consumed1==null)?null:ns.stringToQualifiedName("ex:" + consumed1 + "",pf);
    QualifiedName __operation=(operation==null)?null:ns.stringToQualifiedName("ex:" + operation + "",pf);
    QualifiedName __consumed2=(consumed2==null)?null:ns.stringToQualifiedName("ex:" + consumed2 + "",pf);
    QualifiedName __operation_type=(operation_type==null)?null:ns.stringToQualifiedName("ex:type/" + operation_type + "",pf);
    QualifiedName __produced_type=(produced_type==null)?null:ns.stringToQualifiedName("ex:Result" + produced_type + "",pf);
    document = generator(__agent, __produced, __b, __consumed1, __operation, __consumed2, __operation_type, consumed_value1, produced_value, consumed_value2, __produced_type);
    return document;
  }

  public Document make(Object[] record) {
    String operation=(String) record[1];
    String operation_type=(String) record[2];
    String agent=(String) record[3];
    String consumed1=(String) record[4];
    Integer consumed_value1=toInt(record[5]);
    String consumed2=(String) record[6];
    Integer consumed_value2=toInt(record[7]);
    String produced=(String) record[8];
    String produced_type=(String) record[9];
    Integer produced_value=toInt(record[10]);
    return make(operation, operation_type, agent, consumed1, consumed_value1, consumed2, consumed_value2, produced, produced_type, produced_value);
  }
}
