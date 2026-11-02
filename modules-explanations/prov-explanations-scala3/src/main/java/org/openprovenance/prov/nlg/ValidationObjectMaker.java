package org.openprovenance.prov.nlg;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.validation.ObjectMaker;
import org.openprovenance.prov.validation.Unknown;
import org.openprovenance.prov.validation.VarQName;

public class ValidationObjectMaker implements ObjectMaker {

    @Override
    public VarQName makeVarQName(String namespaceURI, String localPart, String prefix) {
        return new MyVarQName(namespaceURI,localPart,prefix);
    }

    @Override
    public VarQName makeVarQName(QualifiedName x) {
        return new MyVarQName(x);
    }

    @Override
    public Unknown makeUnknown(String this_VAL_URI, String valPrefix) {
        return new MyUnknown(this_VAL_URI,valPrefix);
    }

    static class MyVarQName extends org.openprovenance.prov.scala.mutable.QualifiedName implements  VarQName {
        MyVarQName(String namespaceURI, String localPart, String prefix) {
            super(namespaceURI,localPart,prefix);
        }
        MyVarQName(QualifiedName qn) {
            super(qn.getNamespaceURI(),qn.getLocalPart(),qn.getPrefix());
        }
    }

    static class MyUnknown extends org.openprovenance.prov.scala.mutable.QualifiedName implements  Unknown {
        MyUnknown(String namespaceURI, String prefix) {
            super(namespaceURI,"UNKOWN",prefix);
        }
    }
}
