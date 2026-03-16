package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

public class ArrayAllocator extends Expression {


    public final TypeName elementType;
    public final Expression size;

    public ArrayAllocator(TypeName elementType, Expression size) {
        this.elementType = elementType;
        this.size =size;
        this.expressionKind= ExpressionKind.ARRAY_ALLOCATOR;
    }

    public static ArrayAllocator ARRAY_ALLOCATOR(TypeName elementType, Expression expression) {
        return new ArrayAllocator(elementType, expression);
    }

    @Override
    public String toString() {
        return "ArrayAllocator{" +
                "elementType=" + elementType +
                ", value=" + size +
                ", expressionKind=" + expressionKind +
                ", statementKind=" + statementKind +
                '}';
    }
}
