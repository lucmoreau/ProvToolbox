package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.w3c.dom.Element;

public class KeyAdapter extends XmlAdapter<Element, TypedValue> {

    final org.openprovenance.prov.model.ProvFactory pFactory;
    final ValueConverter vconv;

    public KeyAdapter() {
	pFactory= new ProvFactory();
	vconv=new ValueConverter(pFactory);
    }



    @Override
    public Element marshal(TypedValue value) throws Exception {
         System.out.println("==> KeyAdapter marshalling for " + value);
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public TypedValue unmarshal(Element value) throws Exception {
         System.out.println("==> KeyAdapter unmarshalling for " + value);
	// TODO Auto-generated method stub
	return null;
    }

}
