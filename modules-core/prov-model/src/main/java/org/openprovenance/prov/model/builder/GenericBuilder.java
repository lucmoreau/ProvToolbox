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
    protected final QualifiedName provType;
    protected final QualifiedName provRole;
    protected QualifiedName id;

    protected List<Attribute> attrs=new LinkedList<>();
    final private QualifiedName provLabel;
    protected final QualifiedName provValue;
    final private QualifiedName provLocation;

    public GenericBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        this.parent=builder;
        this.mc=mc;
        this.pf=pf;
        this.me=(T) this;
        this.provLocation = pf.getName().PROV_LOCATION;
        this.provLabel = pf.getName().PROV_LABEL;
        this.provType = pf.getName().PROV_TYPE;
        this.provValue = pf.getName().PROV_VALUE;
        this.provRole = pf.getName().PROV_ROLE;
    }
    final public QualifiedName qn(String prefix, String local) {
        return parent.qn(prefix,local);
    }
    final public QualifiedName qn(Prefix prefix, String local) {
        return parent.qn(prefix,local);
    }

    public ProvFactory pf() {
        return pf;
    }

    public QualifiedName provRole() {
        return provRole;
    }

    public T id(QualifiedName id) {
        this.id=id;
        return me;
    }
    public T id(String prefix, String local) {
        this.id= qn(prefix,local);
        return me;
    }
    public T id(Prefix prefix, String local) {
        this.id= qn(prefix.get(),local);
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
    public T type(Prefix prefix, String local) {
        return attr(pf.newAttribute(provType,pf.newQualifiedName(parent.namespace.lookupPrefix(prefix.get()),local,prefix.get()),pf.getName().PROV_QUALIFIED_NAME));
    }
    public T location(QualifiedName qn) {
        return attr(pf.newAttribute(provLocation,qn,pf.getName().PROV_QUALIFIED_NAME));
    }

    public T prefix(String prefix, String ns) {
        parent.prefix(prefix,ns);
        return me;
    }


}