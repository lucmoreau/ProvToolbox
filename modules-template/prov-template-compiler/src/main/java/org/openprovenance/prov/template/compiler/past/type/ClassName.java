package org.openprovenance.prov.template.compiler.past.type;

import static org.openprovenance.prov.template.compiler.common.Constants.CLIENT_PACKAGE;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class ClassName extends TypeName {
    static public final ClassName _int =ClassName.get("int", "past.lang");
    public static final TypeName _bool =ClassName.get("bool", "past.lang");


    static public final ClassName intArray =ClassName.get("int[]", "past.lang");
    static public final ClassName OBJECT=ClassName.get("Object", "past.lang");
    static public final ClassName STRING=ClassName.get("String", "past.lang");
    static public final ClassName BOOLEAN=ClassName.get("Boolean", "past.lang");
    public static final ClassName INTEGER = ClassName.get("Integer", "past.lang");
    public static final ClassName MAP = ClassName.get("Map", "past.util");
    public static final ClassName HASHMAP = ClassName.get("HashMap", "past.util");
    public static final ClassName LIST = ClassName.get("List", "past.util");
    public static final ClassName SET = ClassName.get("Set", "past.util");
    public static final ClassName LINKED_LIST = ClassName.get( "LinkedList", "past.util");
    public static final ClassName CLASS = ClassName.get( "Class", "past.lang");
    public static final ClassName VOID = ClassName.get( "Void", "past.lang");
    public static final ArrayType STRING_ARRAY=ArrayType.of(STRING);
    public static final ArrayType OBJECT_ARRAY=ArrayType.of(OBJECT);
    public static final ArrayType INTEGER_ARRAY=ArrayType.of(INTEGER);
    public static final ArrayType OBJECT_ARRAY_ARRAY=ArrayType.of(OBJECT_ARRAY);
    public static final ClassName STRING_BUILDER = ClassName.get( "StringBuilder", "past.util");
    public static final ClassName SUPPLIER = ClassName.get( "Supplier", "past.lang");
    public static final ClassName CONSUMER = ClassName.get( "Consumer", "past.lang");
    public static final ClassName BICONSUMER = ClassName.get( "BiConsumer", "past.lang");
    public static final ClassName FUNCTION = ClassName.get( "Function", "past.lang");
    public static final ClassName BIFUNCTION = ClassName.get( "BiFunction", "past.lang");
    public static final ClassName UNSUPPORTED_OPERATION_EXCEPTION=ClassName.get("UnsupportedOperationException", "past.exception");
    public static final ClassName ILLEGAL_STATE_EXCEPTION=ClassName.get("IllegalStateException", "past.exception");
    public static final ClassName ILLEGAL_ARGUMENT_EXCEPTION=ClassName.get("IllegalArgumentException", "past.exception");
    public static final ClassName PAST_EXCEPTION=ClassName.get("Exception", "past.exception");

    public static final ClassName PROV_FILE_BUILDER = ClassName.get("FileBuilder", "org.openprovenance.prov.template.log2prov");
    public static final ClassName PROV_FACTORY = ClassName.get("ProvFactory", "org.openprovenance.prov.model");
    public static final ClassName PROV_QUALIFIED_NAME = ClassName.get("QualifiedName", "org.openprovenance.prov.model");
    public static final ClassName PROV_PROVENANCE_KERNELS = ClassName.get("ProvenanceKernels", "org.openprovenance.prov.template.types");
    public static final ClassName PROV_RUNNER = ClassName.get("Runner", "org.openprovenance.prov.template.types");


    public static final ParameterizedType SET_STRING = ParameterizedType.get(SET, STRING);
    public static final ParameterizedType MAP_STRING_T= ParameterizedType.get(MAP, STRING, T());
    public static final ParameterizedType MAP_STRING_STRING= ParameterizedType.get(MAP, STRING, STRING);
    public static final ParameterizedType MAP_INTEGER_INTARRAY= ParameterizedType.get(MAP, INTEGER, intArray);
    public static final ParameterizedType HASH_MAP_INTEGER_INTARRAY= ParameterizedType.get(HASHMAP, INTEGER, intArray);
    public static final ParameterizedType MAP_STRING_INTARRAY = ParameterizedType.get(MAP, STRING, intArray);
    public static final ParameterizedType HASH_MAP_STRING_INTARRAY = ParameterizedType.get(HASHMAP, STRING, intArray);
    public static final ParameterizedType MAP_STRING_MAP_STRING_INTARRAY = ParameterizedType.get(MAP, STRING, MAP_STRING_INTARRAY);
    public static final ParameterizedType HASH_MAP_STRING_MAP_STRING_INTARRAY = ParameterizedType.get(HASHMAP, STRING, MAP_STRING_INTARRAY);
    public static final TypeName LIST_OF_OBJECT_ARRAYS = ParameterizedType.get(LIST,OBJECT_ARRAY);
    public static final TypeName LIST_OF_OBJECT_ARRAYS_ARRAYS = ParameterizedType.get(LIST,OBJECT_ARRAY_ARRAY);
    public static final ParameterizedType HASH_MAP_STRING_T= ParameterizedType.get(HASHMAP, STRING, T());
    public static final ClassName BUILDER_INTERFACE=ClassName.get("Builder", CLIENT_PACKAGE);
    public static final ParameterizedType MAP_STRING_BUILDER=ParameterizedType.get(MAP, STRING, BUILDER_INTERFACE);
    public static final ParameterizedType MAP_STRING_FILEBUILDER= ParameterizedType.get(MAP, STRING, PROV_FILE_BUILDER);

    public static final TypeVariable TYPE_RESULT = TypeVariable.get("RESULT");
    public static final TypeVariable TYPE_OUTPUT = TypeVariable.get("OUTPUT");
    public static final TypeVariable TYPE_OUT = TypeVariable.get("OUT");
    public static final TypeVariable TYPE_IN = TypeVariable.get("IN");
    public static final TypeName  CONSUMER_OF_IN= ParameterizedType.get(CONSUMER, TYPE_IN);
    public static final TypeName  CONSUMER_OF_T= ParameterizedType.get(CONSUMER, T());
    public static final TypeName  SUPPLIER_OF_STRING= ParameterizedType.get(SUPPLIER, STRING);
    public static final TypeName MAP_STRING_MAP_INTEGER_INTEGER=ParameterizedType.get(MAP,STRING,ParameterizedType.get(MAP,INTEGER,INTEGER));
    public static final ArrayType BUILDERS_ARRAY=ArrayType.of(BUILDER_INTERFACE);
    public static final ParameterizedType MAP_STRING_OBJECT= ParameterizedType.get(MAP, STRING, OBJECT);
    public static final ParameterizedType CLASS_T=ParameterizedType.get(CLASS, T());
    public static final ParameterizedType CLASS_OUT=ParameterizedType.get(CLASS, TYPE_OUT);
    public static final ParameterizedType FUNCTION_BUILDER_T=ParameterizedType.get(FUNCTION,  BUILDER_INTERFACE, T());
    public static final ParameterizedType FUNCTION_OBJARRAY_TO_ANY=FUNCTION_OBJARRAY_TO_TYPE(TypeVariable.get("?"));
    public static final ParameterizedType FUNCTION_OBJARRAY_TO_STRING=FUNCTION_OBJARRAY_TO_TYPE(STRING);
    public static final ParameterizedType FUNCTION_OBJARRAY_TO_OBJ_ARRAY = FUNCTION_OBJARRAY_TO_TYPE(OBJECT_ARRAY);
    public static final ParameterizedType BIFUNCTION_MAP_STRING_MAP_STRING_INTARRAY_STRINGARRAY_TO_T= ParameterizedType.get(BIFUNCTION, MAP_STRING_MAP_STRING_INTARRAY, STRING_ARRAY, T());
    public static final ParameterizedType LIST_MAP_STRING_OBJECT=ParameterizedType.get(LIST, MAP_STRING_OBJECT);
    public static final ParameterizedType BIFUNCTION_INTEGER_STRING_OBJECT = ParameterizedType.get(BIFUNCTION, INTEGER, STRING, OBJECT);
    public static final ParameterizedType BICONSUMER_STRINGBUILDER_TYPEIN=ParameterizedType.get(BICONSUMER, STRING_BUILDER, TYPE_IN);
    public static final ParameterizedType BICONSUMER_STRINGBUILDER_T=ParameterizedType.get(BICONSUMER, STRING_BUILDER, T());
    public static final ParameterizedType BICONSUMER_RESULT_TYPEOUT =ParameterizedType.get(BICONSUMER, TYPE_RESULT, TYPE_OUT);
    public static final ParameterizedType BICONSUMER_RESULT_T =ParameterizedType.get(BICONSUMER, TYPE_RESULT, T());

    public static final ParameterizedType MAP_QUALIFIEDNAME_STRING_SET = ParameterizedType.get(MAP, PROV_QUALIFIED_NAME, SET_STRING);
    public static final ParameterizedType MAP_STRING_STRING_SET = ParameterizedType.get(MAP, STRING, SET_STRING);
    public static final ParameterizedType MAP_STRING_STRING_ARRAY = ParameterizedType.get(MAP, STRING, STRING_ARRAY);

    public static final ParameterizedType HASH_MAP_GENERICS= ParameterizedType.get(HASHMAP);
    public static final ParameterizedType LINKED_LIST_GENERICS= ParameterizedType.get(LINKED_LIST);
    public static final ParameterizedType BIFUNCTION_MAP_OUT_OUT = ParameterizedType.get(BIFUNCTION, MAP_STRING_OBJECT, TYPE_OUT, TYPE_OUT);

    public static ParameterizedType FUNCTION_OBJARRAY_TO_TYPE(TypeName returnType) {
        return ParameterizedType.get(FUNCTION, OBJECT_ARRAY, returnType);
    }
    public static ParameterizedType FUNCTION_LIST_OBJARRAY_TO_TYPE(TypeName returnType) {
        return ParameterizedType.get(FUNCTION, LIST_OF_OBJECT_ARRAYS, returnType);
    }
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
