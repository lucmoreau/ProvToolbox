package org.openprovenance.prov.template.json;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;

public class DescriptorUtils {
    private final ProvFactory pf;
    private final Name name;

    public DescriptorUtils(ProvFactory pf) {
        this.pf = pf;
        this.name=new Name(pf);
    }


    public QDescriptor newQDescriptor(QualifiedName qualifiedName) {
        QDescriptor qDescriptor = new QDescriptor();
        qDescriptor.id= qualifiedName.getPrefix()+":"+ qualifiedName.getLocalPart();
        qDescriptor.namespace= qualifiedName.getNamespaceURI();
        return qDescriptor;
    }

    public QualifiedName newQualifiedName(QDescriptor qDescriptor) {
        String[] parts = qDescriptor.id.split(":");
        if (parts.length == 2) {
            return pf.newQualifiedName(qDescriptor.namespace, parts[1], parts[0]);
        } else {
            return pf.newQualifiedName(qDescriptor.namespace, qDescriptor.id, "");
        }
    }

    public Object newValue(VDescriptor vDescriptor) {
        if (vDescriptor == null) {
            return null;
        }
        if (vDescriptor.type == null) {
            return vDescriptor.value;
        }
        return switch (vDescriptor.type) {
            case "xsd:string" -> vDescriptor.value;
            case "xsd:int" -> Integer.parseInt(vDescriptor.value);
            case "xsd:boolean" -> Boolean.parseBoolean(vDescriptor.value);
            default -> throw new IllegalArgumentException("Unsupported VDescriptor type " + vDescriptor.type);
        };
    }

    public QualifiedName valueType(VDescriptor vDescriptor) {
        if (vDescriptor == null) {
            return null;
        }
        if (vDescriptor.type == null) {
            return name.XSD_STRING;
        }
        return switch (vDescriptor.type) {
            case "xsd:string" -> name.XSD_STRING;
            case "xsd:int" -> name.XSD_INT;
            case "xsd:boolean" -> name.XSD_BOOLEAN;
            default -> throw new IllegalArgumentException("Unsupported VDescriptor type " + vDescriptor.type);
        };
    }


    public Attribute newAttribute(QualifiedName elementName, SingleDescriptor singleDescriptor) {
        if (singleDescriptor instanceof QDescriptor) {
            return pf.newAttribute(elementName,
                    newQualifiedName(((QDescriptor) singleDescriptor)),
                    pf.getName().PROV_QUALIFIED_NAME);
        } else if (singleDescriptor instanceof VDescriptor) {
            Object val = newValue((VDescriptor) singleDescriptor);
            QualifiedName type = valueType((VDescriptor) singleDescriptor);
            return pf.newAttribute(elementName,
                    val,
                    type);
        } else {
            throw new IllegalArgumentException("Unexpected descriptor type " + singleDescriptor.getClass());
        }
    }

    public  void addVariable(Bindings bindings, QualifiedName id, QualifiedName uuid) {
        bindings.addVargen(id.getLocalPart(), newQDescriptor(uuid));
    }

}
