package org.openprovenance.prov.template.compiler.past.type;

import org.openprovenance.prov.template.compiler.past.Class;

import static org.openprovenance.prov.template.compiler.common.Constants.CLIENT_PACKAGE;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class ClassName extends TypeName {
    static public final ClassName _int =ClassName.get("int", "past.lang");
    public static final TypeName _bool =ClassName.get("bool", "past.lang");


    static public final ArrayType intArray =ArrayType.of(_int);
    static public final ClassName OBJECT=ClassName.get("Object", "past.lang");
    static public final ClassName STRING=ClassName.get("String", "past.lang");
    static public final ClassName LONG=ClassName.get("Long", "past.lang");
    static public final ClassName FLOAT=ClassName.get("Float", "past.lang");
    static public final ClassName DOUBLE=ClassName.get("Double", "past.lang");
    static public final ClassName BOOLEAN=ClassName.get("Boolean", "past.lang");
    public static final ClassName INTEGER = ClassName.get("Integer", "past.lang");
    public static final ClassName MAP = ClassName.get("Map", "past.util");
    public static final ClassName OPTIONAL = ClassName.get("Optional", "past.lang");

    public static final ClassName HASHMAP = ClassName.get("HashMap", "past.util");
    public static final ClassName LIST = ClassName.get("List", "past.util");
    public static final ClassName SET = ClassName.get("Set", "past.util");
    public static final ClassName LINKED_LIST = ClassName.get( "LinkedList", "past.util");
    public static final ClassName ARRAY_LIST = ClassName.get( "ArrayList", "past.util");
    public static final ClassName HASH_SET = ClassName.get( "HashSet", "past.util");
    public static final ClassName COLLECTION = ClassName.get( "Collection", "past.util");
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
    public static final ClassName SYSTEM = ClassName.get("System", "past.lang");
    public static final ClassName PRINT_STREAM = ClassName.get("PrintStream", "past.io");
    public static final ClassName UNSUPPORTED_OPERATION_EXCEPTION=ClassName.get("UnsupportedOperationException", "past.exception");
    public static final ClassName ILLEGAL_STATE_EXCEPTION=ClassName.get("IllegalStateException", "past.exception");
    public static final ClassName ILLEGAL_ARGUMENT_EXCEPTION=ClassName.get("IllegalArgumentException", "past.exception");
    public static final ClassName PAST_EXCEPTION=ClassName.get("Exception", "past.exception");
    public static final ClassName RUNTIME_EXCEPTION=ClassName.get("RuntimeException", "past.exception");
    public static final ClassName UNCHECKED_EXCEPTION=ClassName.get("UncheckedException", "org.openprovenance.prov.model.exception");


    // sql related
    public static final ClassName SQL_EXCEPTION=ClassName.get("SQLException", "java.sql");
    public static final ClassName RESULT_SET = ClassName.get("ResultSet", "java.sql");
    public static final ClassName RESULT_SET_META_DATA = ClassName.get("ResultSetMetaData", "java.sql");
    public static final ClassName POSTGRES_PGOBJECT=ClassName.get("PGobject", "org.postgresql.util");

    public static final ClassName PROV_FILE_BUILDER = ClassName.get("FileBuilder", "org.openprovenance.prov.template.log2prov");
    public static final ClassName PROV_FACTORY = ClassName.get("ProvFactory", "org.openprovenance.prov.model");
    public static final ClassName PROV_VANILLA_FACTORY = ClassName.get("ProvFactory", "org.openprovenance.prov.vanilla");

    public static final ClassName PROV_QUALIFIED_NAME = ClassName.get("QualifiedName", "org.openprovenance.prov.model");
    public static final ClassName PROV_BUNDLE = ClassName.get("Bundle", "org.openprovenance.prov.model");
    public static final ClassName PROV_STATEMENT = ClassName.get("Statement", "org.openprovenance.prov.model");
    public static final ClassName PROV_DOCUMENT = ClassName.get("Document", "org.openprovenance.prov.model");
    public static final ClassName PROV_UTILITIES = ClassName.get("ProvUtilities", "org.openprovenance.prov.model");
    public static final ClassName PROV_PROVENANCE_KERNELS = ClassName.get("ProvenanceKernels", "org.openprovenance.prov.template.types");
    public static final ClassName PROV_RUNNER = ClassName.get("Runner", "org.openprovenance.prov.template.log2prov");
    public static final ClassName PROV_VALUE_CONVERTER = ClassName.get("ValueConverter", "org.openprovenance.prov.model");
    public static final ClassName PROV_PROXY_CLIENT_ACCESSOR = ClassName.get("ProxyClientAccessorInterface", "org.openprovenance.prov.template.log2prov.interfaces");
    public static final ClassName PROV_INSTANTIATE_ACTION = ClassName.get("InstantiateAction", "org.openprovenance.prov.template.core");
    public static final ClassName PROV_NAMESPACE = ClassName.get("Namespace", "org.openprovenance.prov.model");
    public static final ClassName PROV_FRAMEWORK = ClassName.get("Framework", "org.openprovenance.prov.model.interop");
    public static final ClassName PROV_FORMATS = ClassName.get("Formats", "org.openprovenance.prov.model.interop");

    public static final ClassName PROV_ATTRIBUTE = ClassName.get("Attribute", "org.openprovenance.prov.model");
    public static final TypeName PROV_LINKED_LIST_OF_ATTRIBUTES = ParameterizedType.get(LINKED_LIST, PROV_ATTRIBUTE);
    public static final TypeName PROV_COLLECTION_OF_ATTRIBUTES = ParameterizedType.get(COLLECTION, PROV_ATTRIBUTE);

    public static final ClassName TRIFUNCTION = ClassName.get("TriFunction", "org.openprovenance.prov.template.log2prov.interfaces");
    public static final ClassName PAIR = ClassName.get("Pair", "org.apache.commons.lang3.tuple");
    public static final ClassName APACHE_STRING_SUBSTITUTOR= ClassName.get("StringSubstitutor", "org.apache.commons.text");


    public static final ClassName ATOMIC_INTEGER = ClassName.get("AtomicInteger", "past.lang");
    public static final ParameterizedType MAP_STRING_ATOMIC_INTEGER = ParameterizedType.get(MAP,STRING, ATOMIC_INTEGER);
    public static final ParameterizedType HASHMAP_STRING_ATOMIC_INTEGER = ParameterizedType.get(HASHMAP,STRING, ATOMIC_INTEGER);
    public static final ParameterizedType SET_STRING = ParameterizedType.get(SET, STRING);
    public static final ParameterizedType MAP_STRING_T= ParameterizedType.get(MAP, STRING, T());
    public static final ParameterizedType MAP_STRING_STRING= ParameterizedType.get(MAP, STRING, STRING);
    public static final ParameterizedType MAP_INTEGER_INTARRAY= ParameterizedType.get(MAP, INTEGER, intArray);
    public static final ParameterizedType HASH_MAP_INTEGER_INTARRAY= ParameterizedType.get(HASHMAP, INTEGER, intArray);
    public static final ParameterizedType MAP_STRING_INTARRAY = ParameterizedType.get(MAP, STRING, intArray);
    public static final ParameterizedType HASH_MAP_STRING_INTARRAY = ParameterizedType.get(HASHMAP, STRING, intArray);
    public static final ParameterizedType MAP_STRING_MAP_STRING_INTARRAY = ParameterizedType.get(MAP, STRING, MAP_STRING_INTARRAY);
    public static final ParameterizedType HASH_MAP_STRING_MAP_STRING_INTARRAY = ParameterizedType.get(HASHMAP, STRING, MAP_STRING_INTARRAY);
    public static final TypeName LIST_OF_INTEGER = ParameterizedType.get(LIST,INTEGER);
    public static final TypeName LIST_OF_OBJECTS = ParameterizedType.get(LIST,OBJECT);
    public static final TypeName LINKED_LIST_OF_INTEGER = ParameterizedType.get(LINKED_LIST,INTEGER);
    public static final TypeName LINKED_LIST_OF_OBJECTS = ParameterizedType.get(LINKED_LIST,OBJECT);
    public static final TypeName LIST_OF_OBJECT_ARRAYS = ParameterizedType.get(LIST,OBJECT_ARRAY);
    public static final TypeName LIST_OF_OBJECT_ARRAYS_ARRAYS = ParameterizedType.get(LIST,OBJECT_ARRAY_ARRAY);
    public static final ParameterizedType HASH_MAP_STRING_T= ParameterizedType.get(HASHMAP, STRING, T());
    public static final ClassName BUILDER_INTERFACE=ClassName.get("Builder", CLIENT_PACKAGE);
    public static final ParameterizedType MAP_STRING_BUILDER=ParameterizedType.get(MAP, STRING, BUILDER_INTERFACE);
    public static final ParameterizedType MAP_STRING_FILEBUILDER= ParameterizedType.get(MAP, STRING, PROV_FILE_BUILDER);
    public static final ParameterizedType MAP_STRING_LIST_INTEGER = ParameterizedType.get(MAP,STRING, LIST_OF_INTEGER);
    public static final ParameterizedType HASH_STRING_LIST_INTEGER = ParameterizedType.get(HASHMAP,STRING, LIST_OF_INTEGER);

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
    public static final ParameterizedType FUNCTION_LIST_OBJARRAY_TO_ANY=FUNCTION_LIST_OBJARRAY_TO_TYPE(TypeVariable.get("?"));
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
    public static final ParameterizedType COLLECTION_OF_STRING = ParameterizedType.get(COLLECTION, STRING);
    public static final ParameterizedType MAP_STRING_COLLECTION_OF_STRING = ParameterizedType.get(MAP, STRING, COLLECTION_OF_STRING);
    public static final ParameterizedType MAP_QUALIFIEDNAME_MAP_STRING_COLLECTION_OF_STRING = ParameterizedType.get(MAP, PROV_QUALIFIED_NAME, MAP_STRING_COLLECTION_OF_STRING);
    // BiFunction<Object, String, Collection<String>>
    public static final ParameterizedType BIFUNCTION_OBJECT_STRING_COLLECTION_STRING = ParameterizedType.get(BIFUNCTION, OBJECT, STRING, COLLECTION_OF_STRING);
    // Map<String, BiFunction<Object, String, Collection<String>>>
    public static final ParameterizedType MAP_STRING_BIFUNCTION_OBJECT_STRING_COLLECTION_STRING = ParameterizedType.get(MAP, STRING, BIFUNCTION_OBJECT_STRING_COLLECTION_STRING);
    // Map<String, Map<String, BiFunction<Object, String, Collection<String>>>>
    public static final ParameterizedType MAP_STRING_MAP_STRING_BIFUNCTION = ParameterizedType.get(MAP, STRING, MAP_STRING_BIFUNCTION_OBJECT_STRING_COLLECTION_STRING);
    // Collection<Pair<String, Collection<String>>>
    public static final ParameterizedType PAIR_STRING_COLLECTION_STRING = ParameterizedType.get(PAIR, STRING, COLLECTION_OF_STRING);
    public static final ParameterizedType COLLECTION_OF_PAIRS_STRING_COLLECTION_STRING = ParameterizedType.get(COLLECTION, PAIR_STRING_COLLECTION_STRING);
    // TriFunction<Object, String, String, Collection<Pair<String, Collection<String>>>>
    public static final ParameterizedType TRIFUNCTION_OBJECT_STRING_STRING_COLLECTION_PAIRS = ParameterizedType.get(TRIFUNCTION, OBJECT, STRING, STRING, COLLECTION_OF_PAIRS_STRING_COLLECTION_STRING);
    // Map<String, TriFunction<...>>
    public static final ParameterizedType MAP_STRING_TRIFUNCTION = ParameterizedType.get(MAP, STRING, TRIFUNCTION_OBJECT_STRING_STRING_COLLECTION_PAIRS);
    // Map<String, Map<String, TriFunction<...>>>
    public static final ParameterizedType MAP_STRING_MAP_STRING_TRIFUNCTION = ParameterizedType.get(MAP, STRING, MAP_STRING_TRIFUNCTION);
    public static final ParameterizedType MAP_STRING_STRING_SET = ParameterizedType.get(MAP, STRING, SET_STRING);
    public static final ParameterizedType MAP_STRING_MAP_STRING_SET_STRING = ParameterizedType.get(MAP, STRING, MAP_STRING_STRING_SET);
    public static final ParameterizedType MAP_STRING_STRING_ARRAY = ParameterizedType.get(MAP, STRING, STRING_ARRAY);
    public static final ParameterizedType LIST_OF_STRING = ParameterizedType.get(LIST, STRING);
    public static final ParameterizedType MAP_STRING_LIST_STRING = ParameterizedType.get(MAP, STRING, LIST_OF_STRING);
    public static final ParameterizedType FUNCTION_STRING_RESULTSET = ParameterizedType.get(FUNCTION, STRING, RESULT_SET);
    public static final ClassName CATALOGUE_DISPATCHER_INTERFACE = ClassName.get("CatalogueDispatcherInterface", "org.openprovenance.prov.model.interop");
    public static final ClassName COLLECTORS = ClassName.get("Collectors", "past.util.stream");
    public static final ClassName ARRAYS = ClassName.get("Arrays", "past.util");

    public static final ParameterizedType HASH_MAP_GENERICS= ParameterizedType.get(HASHMAP);
    public static final ParameterizedType LINKED_LIST_GENERICS= ParameterizedType.get(LINKED_LIST);
    public static final ParameterizedType ARRAY_LIST_GENERICS= ParameterizedType.get(ARRAY_LIST);
    public static final ParameterizedType HASH_SET_GENERICS= ParameterizedType.get(HASH_SET);
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
