package org.openprovenance.prov.sql;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

public class QualifiedNameAdapter extends XmlAdapter<QName, org.openprovenance.prov.model.QualifiedName> {
    final ProvFactory pf=new ProvFactory();

    @Override
    public QName marshal(org.openprovenance.prov.model.QualifiedName qualifiedName) throws Exception {
        if (qualifiedName==null) {
            return null;
        } else {
            return qualifiedName.toQName();
        }
    }

    @Override
    public QualifiedName unmarshal(QName q) throws Exception {
        if (q==null) {
            return null;
        } else {
        	QualifiedName qq=new QualifiedName(q);
            return qq;
        }
    }

}
