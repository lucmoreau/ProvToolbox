package org.openprovenance.prov.template.compiler.past.type;


import java.util.Arrays;

public class ParameterizedType extends TypeName {
    public final ClassName rawType;
    public final TypeName[] typeArguments;

    public ParameterizedType(ClassName rawType, TypeName... typeArguments) {
        this.rawType = rawType;
        this.typeArguments = typeArguments;
        this.typeKind=TypeKind.PARAMETERIZED;
    }

    static public ParameterizedType get(ClassName rawType, TypeName... typeArguments) {
        if (rawType == null || typeArguments == null ) {
            throw new IllegalArgumentException("Null argument(s) in ParameterizedType.get");
        }
        return new ParameterizedType(rawType, typeArguments);
    }

    ;


    public ClassName getRawType() {
        return rawType;
    }

    public TypeName[] getTypeArguments() {
        return typeArguments;
    }

    @Override
    public String toString() {
        return "ParameterizedType{" +
                "rawType=" + rawType +
                ", typeArguments=" + Arrays.toString(typeArguments) +
                '}';
    }
}


