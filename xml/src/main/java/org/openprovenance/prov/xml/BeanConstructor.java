package org.openprovenance.prov.xml;
import java.util.List;
import javax.xml.namespace.QName;

public interface BeanConstructor {
    public Object convert(QName q);
    public Object convert(Object id, Entity e);
    public Object convert(Object id, Agent ag);
    public Object convert(Object id, Activity a);
    public Object convertContainer(Object namespaces, List<Object> records);
}
