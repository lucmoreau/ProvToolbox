package org.openprovenance.prov.asn;
import org.openprovenance.prov.xml.BeanConstructor;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Agent;
import static org.openprovenance.prov.xml.NamespacePrefixMapper.XSI_NS;
import java.util.List;
import java.util.LinkedList;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

public class BeanASNConstructor implements BeanConstructor{
    private ASNConstructor c;
    public BeanASNConstructor(ASNConstructor c) {
        this.c=c;
    }

    public Object convert(QName q) {
        if (q==null) return null;
        if (q.getPrefix()!=null) {
            return q.getPrefix()+ ":" + q.getLocalPart();
        } else {
            return q.getLocalPart();
        }
    }

    public Object convertAttributeValue(Element a) {
        String type=a.getAttributeNS(XSI_NS,"type");
        if ((type==null) || ("".equals(type))) {
            return "\"" + a.getFirstChild().getNodeValue() + "\"";
        } else {
            return c.convertTypedLiteral(type,
                                         "\"" + a.getFirstChild().getNodeValue() + "\"");
        }
    }

    public Object convertAttribute(Element a) {
        return c.convertAttribute(a.getNodeName(),
                                  convertAttributeValue(a));
    }

    public Object convertAttribute(Object a) {
        if (a instanceof JAXBElement) {
            JAXBElement je=(JAXBElement) a;
            QName q=je.getName();
            return c.convertAttribute(convert(q),
                                      je.getValue());
        } if (a instanceof Element) {
            return convertAttribute((Element) a);
        } else {
            System.out.println(""+a.getClass());
            return c.convertAttribute(a.toString(), a.toString());
        }
    }

    public Object convert(Object id, Entity e) {
        List attrs=new LinkedList();
        for (Object a: e.getAny()) {
            attrs.add(convertAttribute(a));
        }
        return c.convertEntity(id,
                               c.convertAttributes(attrs));
    }

    public Object convert(Object id, Activity e) {
        List attrs=new LinkedList();
        for (Object a: e.getAny()) {
            attrs.add(convertAttribute(a));
        }
        return c.convertActivity(id,
                                 null, //start
                                 null, //end
                                 c.convertAttributes(attrs));
    }

    public Object convert(Object id, Agent e) {
        List attrs=new LinkedList();
        for (Object a: e.getAny()) {
            attrs.add(convertAttribute(a));
        }
        return c.convertAgent(id,
                              c.convertAttributes(attrs));
    }

    public Object convertContainer(Object namespaces, List<Object> records) {
        return c.convertContainer(namespaces,records);
    }

}
