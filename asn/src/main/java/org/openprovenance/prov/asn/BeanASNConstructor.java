package org.openprovenance.prov.asn;
import org.openprovenance.prov.xml.BeanConstructor;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasControlledBy;
import org.openprovenance.prov.xml.HasAnnotation;
import org.openprovenance.prov.xml.WasInformedBy;
import org.openprovenance.prov.xml.WasComplementOf;
import org.openprovenance.prov.xml.HadPlan;
import org.openprovenance.prov.xml.WasGeneratedBy;

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

    public List<Object> convertAttributes(HasExtensibility e) {
        List attrs=new LinkedList();
        for (Object a: e.getAny()) {
            attrs.add(convertAttribute(a));
        }
        return attrs;
    }

    public Object convert(Object id, Entity e) {
        List attrs=convertAttributes(e);
        return c.convertEntity(id,
                               c.convertAttributes(attrs));
    }

    public Object convert(Object id, Activity e) {
        List attrs=convertAttributes(e);
        return c.convertActivity(id,
                                 null, //start
                                 null, //end
                                 c.convertAttributes(attrs));
    }


    public Object convert(Object id, Agent e) {
        List attrs=convertAttributes(e);
        return c.convertAgent(id,
                              c.convertAttributes(attrs));
    }


    public Object convert(Object id, WasAssociatedWith o) {
        List attrs=convertAttributes(o);
        return c.convertWasAssociatedWith(id,
                                          o.getActivity().getRef(),
                                          o.getAgent().getRef(),
                                          c.convertAttributes(attrs));
    }
    public Object convert(Object id, Used o) {
        List attrs=convertAttributes(o);
        return c.convertUsed(id,
                             convert(o.getActivity().getRef()),
                             convert(o.getEntity().getRef()),
                             null,
                             c.convertAttributes(attrs));
    }

    public Object convert(Object id, WasDerivedFrom o) {
        List attrs=convertAttributes(o);
        return c.convertWasDerivedFrom(//id,
                                       convert(o.getEffect().getRef()),
                                       convert(o.getCause().getRef()),
                                       null,//pe
                                       null,//g2
                                       null,//u1
                                       c.convertAttributes(attrs));
    }
    public Object convert(Object id, WasControlledBy o) {
        List attrs=convertAttributes(o);
        return null;
    }
    public Object convert(Object id, HasAnnotation o) {
        List attrs=convertAttributes(o);
        return null;
    }
    public Object convert(Object id, WasInformedBy o) {
        List attrs=convertAttributes(o);
        return null;
    }
    public Object convert(Object id, WasComplementOf o) {
        List attrs=convertAttributes(o);
        return c.convertWasComplementOf(id,
                                        convert(o.getEntity2().getRef()),
                                        convert(o.getEntity1().getRef()),
                                        attrs);
    }
    public Object convert(Object id, HadPlan o) {
        List attrs=convertAttributes(o);
        return c.convertHadPlan(id,
                                convert(o.getActivity().getRef()),
                                convert(o.getEntity().getRef()),
                                attrs);
    }
    public Object convert(Object id, WasGeneratedBy o) {
        List attrs=convertAttributes(o);
        return c.convertWasGeneratedBy(id,
                                       convert(o.getEntity().getRef()),
                                       convert(o.getActivity().getRef()),
                                       null,//time
                                       c.convertAttributes(attrs));
    }


    public Object convertContainer(Object namespaces,
                                   List<Object> aRecords,
                                   List<Object> eRecords,
                                   List<Object> agRecords,
                                   List<Object> lnkRecords) {
        List<Object> ll=new LinkedList();
        if (aRecords!=null) ll.addAll(aRecords);
        if (eRecords!=null) ll.addAll(eRecords);
        if (agRecords!=null) ll.addAll(agRecords);
        if (lnkRecords!=null) ll.addAll(lnkRecords);
        return c.convertContainer(namespaces,ll);
    }

}
