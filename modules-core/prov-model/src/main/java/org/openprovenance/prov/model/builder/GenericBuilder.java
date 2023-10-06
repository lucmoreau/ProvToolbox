package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import java.util.LinkedList;
import java.util.List;

abstract public class GenericBuilder<T extends GenericBuilder<T>> {
    protected final Builder parent;
    protected final ModelConstructor mc;
    protected final ProvFactory pf;
    private final T me;
    private final QualifiedName provType;
    protected QualifiedName id;

    protected List<Attribute> attrs=new LinkedList<>();
    final private QualifiedName provLabel;

    public GenericBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        this.parent=builder;
        this.mc=mc;
        this.pf=pf;
        this.me=(T) this;
        this.provLabel = pf.getName().PROV_LABEL;
        this.provType = pf.getName().PROV_TYPE;


    }
    protected QualifiedName qn(String prefix, String local) {
        return mc.newQualifiedName(parent.namespace.lookupPrefix(prefix),local,prefix);
    }


    public T id(QualifiedName id) {
        this.id =id;
        return me;
    }
    public T id(String prefix, String local) {
        this.id=qn(prefix,local);
        return me;
    }

    public T attr(Attribute attr) {
        this.attrs.add(attr);
        return me;
    }

    public T attr(QualifiedName elementName, Object value, QualifiedName type) {
        return attr(pf.newAttribute(elementName,value,type));
    }
    public T attr(QualifiedName elementName, boolean value) {
        return attr(pf.newAttribute(elementName,""+value,pf.getName().XSD_BOOLEAN));
    }
    public T attr(QualifiedName elementName, int value) {
        return attr(pf.newAttribute(elementName,""+value,pf.getName().XSD_INTEGER));
    }
    public T attr(QualifiedName elementName, double value) {
        return attr(pf.newAttribute(elementName,""+value,pf.getName().XSD_DOUBLE));
    }
    public T attr(QualifiedName elementName, String value) {
        return attr(pf.newAttribute(elementName,value,pf.getName().XSD_STRING));
    }
    public T label(String lab) {
        return attr(pf.newAttribute(provLabel,lab,pf.getName().XSD_STRING));
    }
    public T type(QualifiedName qn) {
        return attr(pf.newAttribute(provType,qn,pf.getName().PROV_QUALIFIED_NAME));
    }
    public T type(String prefix, String local) {
        return attr(pf.newAttribute(provType,pf.newQualifiedName(parent.namespace.lookupPrefix(prefix),local,prefix),pf.getName().PROV_QUALIFIED_NAME));
    }
    public T location(QualifiedName qn) {
        return attr(pf.newAttribute(pf.getName().PROV_LOCATION,qn,pf.getName().PROV_QUALIFIED_NAME));
    }

    public T prefix(String prefix, String ns) {
        parent.prefix(prefix,ns);
        return me;
    }


}