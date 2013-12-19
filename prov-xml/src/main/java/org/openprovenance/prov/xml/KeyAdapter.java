package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.QualifiedName;
import org.w3c.dom.Element;

public class KeyAdapter extends XmlAdapter<Element, TypedValue> {

    
    final org.openprovenance.prov.model.ProvFactory pFactory;
    final ValueConverter vconv;
    final private DOMProcessing domProcessor;
    final QualifiedName qname_PROV_KEY;

    public KeyAdapter() {
	pFactory= new ProvFactory();
	domProcessor=new DOMProcessing(pFactory);
	qname_PROV_KEY = pFactory.getName().PROV_KEY;
	vconv=new ValueConverter(pFactory);
    }


    public KeyAdapter(org.openprovenance.prov.model.ProvFactory pFactory) {
	this.pFactory=pFactory;
	domProcessor=new DOMProcessing(pFactory);
	qname_PROV_KEY = pFactory.getName().PROV_KEY;

	vconv=new ValueConverter(pFactory);
    }


    @Override
    public Element marshal(TypedValue value) throws Exception {
         //System.out.println("==> KeyAdapter marshalling for " + value);
	return DOMProcessing.marshalTypedValue(value,qname_PROV_KEY);
    }

    @Override
    public TypedValue unmarshal(Element el) throws Exception {
         //System.out.println("==> KeyAdapter unmarshalling for " + el);
         //TODO: make sure I construct a typedvalue. Update newAttribute in xml.ProvFactory.
         return (TypedValue) domProcessor.unmarshallAttribute(el,pFactory,vconv);
    }

}
