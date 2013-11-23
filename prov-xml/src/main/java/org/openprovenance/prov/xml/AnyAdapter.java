package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.openprovenance.prov.model.DOMProcessing;

/** Adapter convert an Attribute to DOM and vice-versa.
 * @author lavm
 * @see javax.xml.bind.annotation.adapters.XmlAdapter
 * 
 */
public class AnyAdapter extends
        XmlAdapter<Object, org.openprovenance.prov.model.Attribute> {

    final org.openprovenance.prov.model.ProvFactory pFactory;
    
    public AnyAdapter() {
	pFactory= new ProvFactory();
	vconv=new ValueConverter(pFactory,null);
    }

    public AnyAdapter(org.openprovenance.prov.model.ProvFactory pFactory) {
	this.pFactory= pFactory;
	vconv=new ValueConverter(pFactory,null);
    }

    final ValueConverter vconv;
    


    /** Unmarshals an Object (expect to be a DOM Element) into an Attribute.
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    public org.openprovenance.prov.model.Attribute unmarshal(Object value) {
        // System.out.println("AnyAdapter unmarshalling for " + value);
        if (value instanceof org.w3c.dom.Element) {
            org.w3c.dom.Element el = (org.w3c.dom.Element) value;
            return DOMProcessing.unmarshallAttribute(el,pFactory,vconv);
        }
        /*
        if (value instanceof JAXBElement) {
            JAXBElement<?> je = (JAXBElement<?>) value;
            return pFactory.newAttribute(je.getName(), je.getValue(), vconv);
        }
        */
        return null;
    }

    
    /** Marshals an Attribute to a DOM Element.
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    public Object marshal(org.openprovenance.prov.model.Attribute attribute) {
        return DOMProcessing.marshalAttribute(attribute);
    }
   
}
