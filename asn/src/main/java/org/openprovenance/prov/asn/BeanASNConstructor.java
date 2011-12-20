package org.openprovenance.prov.asn;

import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.BeanConstructor;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.HadPlan;
import org.openprovenance.prov.xml.HasAnnotation;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.HasLabel;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.TypedLiteral;
import org.openprovenance.prov.xml.URIWrapper;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasComplementOf;
import org.openprovenance.prov.xml.WasControlledBy;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInformedBy;
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

    public String quoteWrap(Object b) {
        return "\"" + b + "\"";
    }

    public Object convertTypedLiteral(Object a) {
        if (a instanceof QName) {
            QName q=(QName) a;
            return c.convertTypedLiteral("xsd:QName",quoteWrap(convert(q)));
        } if (a instanceof URIWrapper) {
            URIWrapper u=(URIWrapper) a;
            return c.convertTypedLiteral("xsd:anyURI",quoteWrap(u));
        } if (a instanceof Boolean) {
            Boolean b=(Boolean) a;
            return c.convertTypedLiteral("xsd:boolean",quoteWrap(b));
        } if (a instanceof String) {
            String b=(String) a;
            return c.convertTypedLiteral("xsd:string",quoteWrap(b));
        } if (a instanceof Double) {
            Double b=(Double) a;
            return c.convertTypedLiteral("xsd:double",quoteWrap(b));
        } if (a instanceof Integer) {
            Integer b=(Integer) a;
            return c.convertTypedLiteral("xsd:int",quoteWrap(b));
        } else {
            return "$" + a + "$(" + a.getClass() + ")";
        }
    }

    public Object convertAttribute(Object a) {
        if (a instanceof JAXBElement) {
            JAXBElement je=(JAXBElement) a;
            QName q=je.getName();
            Object o=je.getValue();
            String value;
            if (o instanceof TypedLiteral) {
                TypedLiteral tl=(TypedLiteral) o;
                value=tl.getValue().toString();
            } else {
                value=o.toString();
            }
            return c.convertAttribute(convert(q),
                                      value);
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

    public List<Object> convertTypeAttributes(HasType e) {
        List attrs=new LinkedList();
        for (Object a: e.getType()) {
            attrs.add(c.convertAttribute("prov:type",
                                         convertTypedLiteral(a)));
        }
        return attrs;
    }

    public Object convertLabelAttribute(HasLabel e) {
        Object a=e.getLabel();
        if (a==null) return null;
        return c.convertAttribute("prov:label",
                                  convertTypedLiteral(a));
    }

    public Object convert(Object id, Entity e) {
        List tAttrs=convertTypeAttributes(e);
        List otherAttrs=convertAttributes(e);
        Object lAttr=convertLabelAttribute(e);
        List attrs=new LinkedList();
        if (lAttr!=null) attrs.add(lAttr);
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return c.convertEntity(id,
                               c.convertAttributes(attrs));
    }

    public Object convert(Object id, Activity e) {
        List tAttrs=convertTypeAttributes(e);
        List otherAttrs=convertAttributes(e);
        Object lAttr=convertLabelAttribute(e);
        List attrs=new LinkedList();
        if (lAttr!=null) attrs.add(lAttr);
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return c.convertActivity(id,
                                 e.getStartTime(), 
                                 e.getEndTime(), 
                                 c.convertAttributes(attrs));
    }


    public Object convert(Object id, Agent e) {
        List tAttrs=convertTypeAttributes(e);
        List otherAttrs=convertAttributes(e);
        Object lAttr=convertLabelAttribute(e);
        List attrs=new LinkedList();
        if (lAttr!=null) attrs.add(lAttr);
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return c.convertAgent(id,
                              c.convertAttributes(attrs));
    }


    public Object convert(Object id, WasAssociatedWith o) {
        List tAttrs=convertTypeAttributes(o);
        List otherAttrs=convertAttributes(o);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return c.convertWasAssociatedWith(id,
                                          o.getActivity().getRef(),
                                          o.getAgent().getRef(),
                                          c.convertAttributes(attrs));
    }
    public Object convert(Object id, Used o) {
        List tAttrs=convertTypeAttributes(o);
        List otherAttrs=convertAttributes(o);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return c.convertUsed(id,
                             convert(o.getActivity().getRef()),
                             convert(o.getEntity().getRef()),
                             null,
                             c.convertAttributes(attrs));
    }

    public Object convert(Object id, WasDerivedFrom o) {
        List tAttrs=convertTypeAttributes(o);
        List otherAttrs=convertAttributes(o);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return c.convertWasDerivedFrom(//id,
                                       convert(o.getEffect().getRef()),
                                       convert(o.getCause().getRef()),
                                       null,//pe
                                       null,//g2
                                       null,//u1
                                       c.convertAttributes(attrs));
    }
    public Object convert(Object id, WasControlledBy o) {
        List tAttrs=convertTypeAttributes(o);
        List otherAttrs=convertAttributes(o);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return null;
    }
    public Object convert(Object id, HasAnnotation o) {
        List otherAttrs=convertAttributes(o);
        return null;
    }
    public Object convert(Object id, WasInformedBy o) {
        List tAttrs=convertTypeAttributes(o);
        List otherAttrs=convertAttributes(o);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return null;
    }
    public Object convert(Object id, WasComplementOf o) {
        List tAttrs=convertTypeAttributes(o);
        List otherAttrs=convertAttributes(o);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return c.convertWasComplementOf(id,
                                        convert(o.getEntity2().getRef()),
                                        convert(o.getEntity1().getRef()),
                                        attrs);
    }
    public Object convert(Object id, HadPlan o) {
        List tAttrs=convertTypeAttributes(o);
        List otherAttrs=convertAttributes(o);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
        return c.convertHadPlan(id,
                                convert(o.getActivity().getRef()),
                                convert(o.getEntity().getRef()),
                                attrs);
    }
    public Object convert(Object id, WasGeneratedBy o) {
        List tAttrs=convertTypeAttributes(o);
        List otherAttrs=convertAttributes(o);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs);
        attrs.addAll(otherAttrs);
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
