package org.openprovenance.prov.core.jsonld;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collection;
import java.util.List;

public class HadMember extends org.openprovenance.prov.core.HadMember implements JLD_HadMember {
    public HadMember(QualifiedName collection, List<QualifiedName> entity) {
        super(collection, entity);
    }
    private HadMember() {}

}
