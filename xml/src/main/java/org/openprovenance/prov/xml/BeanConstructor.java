package org.openprovenance.prov.xml;
import java.util.List;
import javax.xml.namespace.QName;

public interface BeanConstructor {
    public Object convert(QName q);
    public Object convert(Object id, Entity e);
    public Object convert(Object id, Agent ag);
    public Object convert(Object id, Activity a);
    public Object convertContainer(Object namespaces,
                                   List<Object> aRecords,
                                   List<Object> eRecords,
                                   List<Object> agRecords,
                                   List<Object> lnkRecords);

    public Object convert(Object id, WasAssociatedWith o);
    public Object convert(Object id, Used o);
    public Object convert(Object id, WasDerivedFrom o);
    public Object convert(Object id, WasControlledBy o);
    public Object convert(Object id, HasAnnotation o);
    public Object convert(Object id, WasInformedBy o);
    public Object convert(Object id, WasComplementOf o);
    public Object convert(Object id, HadPlan o);
    public Object convert(Object id, WasGeneratedBy o);

}
