package org.openprovenance.prov.template.compiler.past.type;

public class ClassName extends TypeName {
    static public final ClassName _int =ClassName.get("int", "past.lang");
    static public final ClassName intArray =ClassName.get("int[]", "past.lang");
    static public final ClassName OBJECT=ClassName.get("Object", "past.lang");
    static public final ClassName STRING=ClassName.get("String", "past.lang");
    public static final ClassName INTEGER = ClassName.get("Integer", "past.lang");
    public static final ClassName MAP = ClassName.get("Map", "past.util");
    public static final ClassName HASHMAP = ClassName.get("HashMap", "past.util");
    public static final ClassName LIST = ClassName.get("List", "past.util");
    public static final ClassName LINKED_LIST = ClassName.get( "LinkedList", "past.util");
    public static final ClassName CLASS = ClassName.get( "Class", "past.lang");
    public static final ClassName VOID = ClassName.get( "Void", "past.lang");
    public static final ArrayType STRING_ARRAY=ArrayType.of(STRING);
    public static final ArrayType OBJECT_ARRAY=ArrayType.of(OBJECT);
    public static final ClassName STRING_BUILDER = ClassName.get( "StringBuilder", "past.util");
    public static final ClassName FUNCTION = ClassName.get( "Function", "past.lang");
    public static final ClassName UNSUPPORTED_OPERATION_EXCEPTION=ClassName.get("UnsupportedOperationException", "past.exception");
    public static final ParameterizedType MAP_INTEGER_INTARRAY= ParameterizedType.get(MAP, INTEGER, intArray);
    public static final ParameterizedType HASH_MAP_INTEGER_INTARRAY= ParameterizedType.get(HASHMAP, INTEGER, intArray);

    final public String simpleName;
    final public String packge;

    public ClassName(com.squareup.javapoet.ClassName className) {
        this.typeKind=TypeKind.CLASS;
        this.packge=className.packageName();
        this.simpleName=className.simpleName();
    }

    public ClassName( String simpleName, String packge) {
        this.typeKind=TypeKind.CLASS;
        this.packge=packge;
        this.simpleName=simpleName;
    }

    public static ClassName get(String simpleName , String packge) {
        if (packge==null || simpleName==null) {
            throw new IllegalArgumentException("Null argument(s) in ClassName.get");
        }
        return new ClassName(simpleName, packge);
    }

    @Override
    public String toString() {
        return "ClassName{" +
                "packge='" + packge + '\'' +
                ", simpleName='" + simpleName + '\'' +
                '}';
    }
}
