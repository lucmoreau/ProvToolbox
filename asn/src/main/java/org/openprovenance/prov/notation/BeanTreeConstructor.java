package org.openprovenance.prov.notation;

import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.BeanConstructor;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.HasLabel;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.URIWrapper;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInformedBy;
import static org.openprovenance.prov.xml.NamespacePrefixMapper.XSI_NS;
import java.util.List;
import java.util.LinkedList;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

/** A class that implements the BeanConstructor interface and relies on a TreeConstructor to construct a data structure for a given bean. */
public class BeanTreeConstructor implements BeanConstructor{
    private TreeConstructor c;
    public BeanTreeConstructor(TreeConstructor c) {
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
            System.out.println("----> convertAttributeValue " + type + " " + a.getFirstChild().getNodeValue());
            return a.getFirstChild().getNodeValue();
        } else {
            System.out.println("----> convertAttributeValue " + type);
            return c.convertTypedLiteral(type,
                                         "\"" + a.getFirstChild().getNodeValue() + "\"");
        }
    }

    public Object convertAttribute(Object name, Object value) {
        return c.convertAttribute(name,value);
    }





    public Object convertTypedLiteral(String datatype, Object value) {
        return c.convertTypedLiteral(datatype,value);
    }

    public List<Object> convertTypeAttributes(List<Object> tAttrs) {
        List attrs=new LinkedList();
        for (Object a: tAttrs) {
            attrs.add(c.convertAttribute("prov:type",a));
        }
        return attrs;
    }

    public Object convertLabelAttribute(Object a) {
        if (a==null) return null;
        return c.convertAttribute("prov:label",a);
    }



    public Object convertEntity(Object id, List<Object> tAttrs, Object lAttr, List<Object> otherAttrs) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        Object lAttr2=convertLabelAttribute(lAttr);
        List attrs=new LinkedList();
        if (lAttr2!=null) attrs.add(lAttr2);
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);

        return c.convertEntity(id,
                               c.convertAttributes(attrs));
    }

    public Object convertActivity(Object id, List<Object> tAttrs, Object lAttr, List<Object> otherAttrs, Object startTime, Object endTime) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        Object lAttr2=convertLabelAttribute(lAttr);
        List attrs=new LinkedList();
        if (lAttr2!=null) attrs.add(lAttr2);
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);

        return c.convertActivity(id,
                                 startTime, 
                                 endTime, 
                                 c.convertAttributes(attrs));
    }


    public Object convertAgent(Object id, List<Object> tAttrs, Object lAttr, List<Object> otherAttrs) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        Object lAttr2=convertLabelAttribute(lAttr);
        List attrs=new LinkedList();
        if (lAttr2!=null) attrs.add(lAttr2);
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);

        return c.convertAgent(id,
                              c.convertAttributes(attrs));
    }


    public Object convertWasAssociatedWith(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object activity, Object agent, Object plan) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        //List otherAttrs2=convertAttributes(otherAttrs);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);
        return c.convertWasAssociatedWith(id,
                                          activity,
                                          agent,
                                          plan,
                                          c.convertAttributes(attrs));
    }
    public Object convertUsed(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object activity, Object entity) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        //List otherAttrs2=convertAttributes(otherAttrs);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);
        return c.convertUsed(id,
                             activity,
                             entity,
                             null,
                             c.convertAttributes(attrs));
    }

    public Object convertWasDerivedFrom(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object effect, Object cause) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        //List otherAttrs2=convertAttributes(otherAttrs);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);
        return c.convertWasDerivedFrom(id,
                                       effect,
                                       cause,
                                       null,//pe
                                       null,//g2
                                       null,//u1
                                       c.convertAttributes(attrs));
    }
    
    public Object convertWasInformedBy(Object id, List<Object> tAttrs, List<Object> otherAttrs) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        //List otherAttrs2=convertAttributes(otherAttrs);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);
        return null;
    }
    public Object convertAlternateOf(Object entity2, Object entity1) {
        return c.convertAlternateOf(entity2,
                                    entity1);
    }

    public Object convertSpecializationOf(Object entity2, Object entity1) {
        return c.convertSpecializationOf(entity2,
                                         entity1);
    }


    public Object convertMentionOf(Object entity2, Object entity1, Object bundle) {
        return c.convertMentionOf(entity2,
				  entity1,
				  bundle);
    }

    public Object convertWasGeneratedBy(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object entity, Object activity) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        //List otherAttrs2=convertAttributes(otherAttrs);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);
        return c.convertWasGeneratedBy(id,
                                       entity,
                                       activity,
                                       null,//time
                                       c.convertAttributes(attrs));
    }

    public Object convertWasInvalidatedBy(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object entity, Object activity) {
        List tAttrs2=convertTypeAttributes(tAttrs);
        //List otherAttrs2=convertAttributes(otherAttrs);
        List attrs=new LinkedList();
        attrs.addAll(tAttrs2);
        attrs.addAll(otherAttrs);
        return c.convertWasInvalidatedBy(id,
                                         entity,
                                         activity,
                                         null,//time
                                         c.convertAttributes(attrs));
    }


    public Object convertBundle(Object namespaces,
                                   List<Object> aRecords,
                                   List<Object> eRecords,
                                   List<Object> agRecords,
                                   List<Object> lnkRecords) {
        List<Object> ll=new LinkedList();
        if (aRecords!=null) ll.addAll(aRecords);
        if (eRecords!=null) ll.addAll(eRecords);
        if (agRecords!=null) ll.addAll(agRecords);
        if (lnkRecords!=null) ll.addAll(lnkRecords);
        return c.convertBundle(namespaces,ll,null);
    }


    public Object convertNamedBundle(Object namespaces,
                                     List<Object> aRecords,
                                     List<Object> eRecords,
                                     List<Object> agRecords,
                                     List<Object> lnkRecords) {
        List<Object> ll=new LinkedList();
        if (aRecords!=null) ll.addAll(aRecords);
        if (eRecords!=null) ll.addAll(eRecords);
        if (agRecords!=null) ll.addAll(agRecords);
        if (lnkRecords!=null) ll.addAll(lnkRecords);
        return c.convertNamedBundle(namespaces,ll,null);
    }


}
