package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class KeyAdapter extends XmlAdapter<Object, TypedValue> {

    @Override
    public Object marshal(TypedValue value) throws Exception {
         System.out.println("==> KeyAdapter marshalling for " + value);
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public TypedValue unmarshal(Object value) throws Exception {
         System.out.println("==> KeyAdapter unmarshalling for " + value);
	// TODO Auto-generated method stub
	return null;
    }

}
