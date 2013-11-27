package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IDRefAdapter extends XmlAdapter<IDRef, org.openprovenance.prov.model.QualifiedName> {
    final ProvFactory pf=new ProvFactory();

    @Override
    public IDRef marshal(org.openprovenance.prov.model.QualifiedName qname) throws Exception {
        if (qname==null) {
            return null;
        } else {
            //System.out.println("marshalling " + qname);
            return new IDRef(qname.toQName());
        }
    }

    @Override
    public QualifiedName unmarshal(IDRef ref) throws Exception {
        if (ref==null) {
            return null;
        } else {
            //System.out.println("unmarshalling " + ref);
            javax.xml.namespace.QName q=  ref.getRef();
            //System.out.println("unmarshalling found " + q);
            QualifiedName qq=new QualifiedName(q);
            return qq;
        }
    }

}
