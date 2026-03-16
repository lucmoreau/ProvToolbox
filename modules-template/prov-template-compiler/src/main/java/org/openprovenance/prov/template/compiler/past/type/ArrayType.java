package org.openprovenance.prov.template.compiler.past.type;

public class ArrayType extends TypeName {
    public final TypeName elementType;

    public ArrayType(TypeName elementType) {
        this.elementType = elementType;
        this.typeKind=TypeKind.ARRAY;
    }

    static public ArrayType of(TypeName elementType) {
        if (elementType==null) {
            throw new IllegalArgumentException("Element type cannot be null");
        }
        return new ArrayType(elementType);
    }

    @Override
    public String toString() {
        return "ArrayType{" +
                "elementType=" + elementType +
                '}';
    }
}
