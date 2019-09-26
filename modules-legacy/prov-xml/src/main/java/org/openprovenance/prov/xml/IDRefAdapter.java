package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IDRefAdapter extends XmlAdapter<IDRef, org.openprovenance.prov.model.QualifiedName> {
    final ProvFactory pf=new ProvFactory();

    @Override
    public IDRef marshal(org.openprovenance.prov.model.QualifiedName qualifiedName) throws Exception {
        if (qualifiedName==null) {
            return null;
        } else {
            return new IDRef(qualifiedName.toQName());
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
