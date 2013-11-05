package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.w3c.dom.Element;

public class KeyAdapter extends XmlAdapter<Element, TypedValue> {

    private static final QName QNAME_PROV_KEY = new QName(NamespacePrefixMapper.PROV_NS, "key", "prov");
    
    final org.openprovenance.prov.model.ProvFactory pFactory;
    final ValueConverter vconv;

    public KeyAdapter() {
	pFactory= new ProvFactory();
	vconv=new ValueConverter(pFactory);
    }



    @Override
    public Element marshal(TypedValue value) throws Exception {
         System.out.println("==> KeyAdapter marshalling for " + value);
         return DOMProcessing.marshalTypedValue(value,QNAME_PROV_KEY);
    }

    @Override
    public TypedValue unmarshal(Element value) throws Exception {
         System.out.println("==> KeyAdapter unmarshalling for " + value);
	// TODO Auto-generated method stub
	return null;
    }

}
