package org.openprovenance.prov.template.json;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;

import java.util.Map;

import static org.openprovenance.prov.template.expander.InstantiateUtil.*;

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

    public QualifiedName newQualifiedName(QDescriptor qDescriptor, Bindings bindings) {
        String[] parts = qDescriptor.getParts();
        if (parts.length == 2) {
            if (UUID_PREFIX.equals(parts[0]) && bindings.context.get(UUID_PREFIX)==null) {
                return pf.newQualifiedName(URN_UUID_NS, parts[1], UUID_PREFIX);
            } else {
                return pf.newQualifiedName(bindings.context.get(parts[0]), parts[1], parts[0]);
            }
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


    public Attribute newAttribute(QualifiedName elementName, SingleDescriptor singleDescriptor, Bindings bindings) {
        if (singleDescriptor instanceof QDescriptor) {
            return pf.newAttribute(elementName,
                    newQualifiedName(((QDescriptor) singleDescriptor), bindings),
                    name.PROV_QUALIFIED_NAME);
        } else if (singleDescriptor instanceof VDescriptor) {
            Object val = newValue((VDescriptor) singleDescriptor);
            QualifiedName type = valueType((VDescriptor) singleDescriptor);
            return pf.newAttribute(elementName, val, type);
        } else {
            throw new IllegalArgumentException("Unexpected descriptor type " + singleDescriptor.getClass());
        }
    }

    public boolean isVariable(SingleDescriptor singleDescriptor, Bindings bindings) {
        if (singleDescriptor ==null) return false;
        if (singleDescriptor instanceof QDescriptor) {
            QDescriptor qd = (QDescriptor) singleDescriptor;
            //String[] parts = qd.id.split(":");
            String[] parts = qd.getParts();
            Map<String, String> context = bindings.context;
            return parts.length == 2 && VAR_NS.equals(context.get(parts[0]));
        }
        return false;
    }

    public  void addVariable(Bindings bindings, QualifiedName id, QualifiedName uuid) {
        bindings.addVargen(id.getLocalPart(), newQDescriptor(uuid));
    }

}
