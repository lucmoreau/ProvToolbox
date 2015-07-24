package org.openprovenance.prov.sql;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IDRefAdapter extends XmlAdapter<IDRef, org.openprovenance.prov.model.QualifiedName> {
    final ProvFactory pf=new ProvFactory();

    @Override
    public IDRef marshal(org.openprovenance.prov.model.QualifiedName qualifiedName) throws Exception {
        if (qualifiedName==null) {
            return null;
        } else {
            IDRef res=new IDRef();
            res.setRef(qualifiedName.toQName());
            return res;
        }
    }

    @Override
    public QualifiedName unmarshal(IDRef ref) throws Exception {
        if (ref==null) {
            return null;
        } else {
            javax.xml.namespace.QName q=  ref.getRef();
            QualifiedName qq=new QualifiedName(q.getNamespaceURI(), q.getLocalPart(), q.getPrefix());
            return qq;
        }
    }

}
