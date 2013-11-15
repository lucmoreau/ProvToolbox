package org.openprovenance.prov.sql;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IDRefAdapter extends XmlAdapter<IDRef, QName> {
    final ProvFactory pf=new ProvFactory();

    @Override
    public IDRef marshal(QName qname) throws Exception {
        if (qname==null) {
            return null;
        } else {
            System.out.println("marshalling " + qname);
            return (IDRef) pf.newIDRef(qname);
        }
    }

    @Override
    public QName unmarshal(IDRef ref) throws Exception {
        if (ref==null) {
            return null;
        } else {
            System.out.println("unmarshalling " + ref);
            javax.xml.namespace.QName q=  ref.getRef();
            System.out.println("unmarshalling found " + q);
            QName qq=new QName(q.getNamespaceURI(), q.getLocalPart(), q.getPrefix());
            return qq;
        }
    }

}
