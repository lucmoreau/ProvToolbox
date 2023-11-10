package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public interface HasRole<T extends GenericBuilder<T>> {

    QualifiedName provRole();
    ProvFactory pf();

    QualifiedName qn(String prefix, String local);

    T attr(QualifiedName elementName, Object value, QualifiedName type);
    public default T role(QualifiedName role) {
        attr(provRole(),role,pf().getName().PROV_QUALIFIED_NAME);
        return (T) this;
    }
    public default T role(String prefix, String local) {
        attr(provRole(), qn(prefix,local),pf().getName().PROV_QUALIFIED_NAME);
        return (T) this;
    }
    public default T role(String role) {
        attr(provRole(),role,pf().getName().XSD_STRING);
        return (T) this;
    }

}
