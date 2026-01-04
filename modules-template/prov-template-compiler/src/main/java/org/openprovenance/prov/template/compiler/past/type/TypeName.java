package org.openprovenance.prov.template.compiler.past.type;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"typeKind"})

abstract public class TypeName {
    public enum TypeKind {CLASS, VARIABLE, ARRAY, PARAMETERIZED}
    public TypeKind typeKind;

}
