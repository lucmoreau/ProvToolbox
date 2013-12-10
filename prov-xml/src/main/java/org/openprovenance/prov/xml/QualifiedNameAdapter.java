package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

public class QualifiedNameAdapter extends XmlAdapter<QName, org.openprovenance.prov.model.QualifiedName> {
    final ProvFactory pf=new ProvFactory();

    @Override
    public QName marshal(org.openprovenance.prov.model.QualifiedName qname) throws Exception {
        if (qname==null) {
            return null;
        } else {
            //System.out.println("marshalling " + qname);
            return qname.toQName();
        }
    }

    @Override
    public QualifiedName unmarshal(QName q) throws Exception {
        if (q==null) {
            return null;
        } else {
            //System.out.println("unmarshalling " + ref);
            //System.out.println("unmarshalling found " + q);
            QualifiedName qq=new QualifiedName(q);
            return qq;
        }
    }

}
