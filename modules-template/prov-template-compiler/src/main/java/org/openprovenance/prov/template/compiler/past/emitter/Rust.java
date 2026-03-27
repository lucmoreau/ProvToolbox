package org.openprovenance.prov.template.compiler.past.emitter;

import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Iterator;
import org.openprovenance.prov.template.compiler.past.annotations.*;
import org.openprovenance.prov.template.compiler.past.checker.ClassSignature;
import org.openprovenance.prov.template.compiler.past.checker.MethodSignature;
import org.openprovenance.prov.template.compiler.past.checker.TypeRegistry;
import org.openprovenance.prov.template.compiler.past.emitter.registry.ArgTreatment;
import org.openprovenance.prov.template.compiler.past.emitter.registry.ChainedGetBehavior;
import org.openprovenance.prov.template.compiler.past.emitter.registry.ReceiverTransform;
import org.openprovenance.prov.template.compiler.past.emitter.registry.ResultTransform;
import org.openprovenance.prov.template.compiler.past.emitter.registry.RustMethodRegistry;
import org.openprovenance.prov.template.compiler.past.emitter.registry.RustMethodSpec;
import org.openprovenance.prov.template.compiler.past.emitter.registry.RustTypeCategory;
import org.openprovenance.prov.template.compiler.past.type.ArrayType;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import java.io.File;
import java.io.IOException;
import javax.lang.model.element.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.common.Constants.GENERATED_VAR_PREFIX;
import static org.openprovenance.prov.template.compiler.common.Constants.LOGGER;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.INSTANCEOF;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.emitter.JavaScript.classForPurposeOfLookup;

/**
 * Emitter that generates Rust code from a PAST (Programming Abstract Syntax Tree).
 *
 * <p>PAST classes map to Rust as follows:
 * <ul>
 *   <li>Concrete classes → {@code struct} + {@code impl} block.</li>
 *   <li>Interfaces and abstract classes → {@code trait} (with optional default bodies).</li>
 *   <li>{@code @Override} methods → emitted inside {@code impl TraitName for StructName} blocks.</li>
 * </ul>
 *
 * <h2>Ownership and borrowing</h2>
 * <p>The emitter applies several rules to generate correct Rust ownership semantics:
 * <ul>
 *   <li><b>Option wrapping</b>: PAST fields without an initialiser are emitted as
 *       {@code Option<T>}; fields with an initialiser are plain {@code T}.</li>
 *   <li><b>Trait parameters</b>: struct-type parameters in trait methods are emitted
 *       as {@code &T} (shared borrow); primitive types as {@code Option<T>}; String as
 *       {@code &str}; HashMap as {@code &mut HashMap}.</li>
 *   <li><b>Null arguments</b>: {@code Constant.NULL} passed to a {@code FUNCTIONAL_INTERFACE_CALL}
 *       is emitted as {@code ""} when the corresponding parameter type is {@code String}
 *       (to satisfy {@code &str}), and as {@code None} for all other types.
 *       Parameter types are resolved via {@link #findMethodParameters}.</li>
 *   <li><b>Self-method calls</b>: struct-type arguments to {@code self.method()} calls
 *       receive an automatic {@code &} prefix, looked up via {@link #findSelfMethodParameters}.</li>
 *   <li><b>Super-delegation chains</b>: methods that return via a {@code SUPER_METHOD_CALL}
 *       chain yield a reference ({@code &T}).  The emitter appends {@code .clone()} for
 *       cloneable collections and adjusts the return type to {@code &Collection<...>}
 *       for non-cloneable element types (e.g. {@code AtomicInteger}).</li>
 *   <li><b>Trait default bodies</b>: inside a trait default method body,
 *       primitive-type parameters are already {@code Option<T>}; the emitter suppresses
 *       double-wrapping in {@code Some()} via {@code expressionProducesOption()}.</li>
 * </ul>
 *
 * <h2>Cross-class lookup</h2>
 * <p>The emitter maintains a {@link #classRegistry} (PascalCase name → PAST {@link Class})
 * populated by {@link #discoverClass}.  This registry enables:
 * <ul>
 *   <li>{@link #findMethodParameters} — resolves callee parameter types for
 *       {@code FUNCTIONAL_INTERFACE_CALL} null-arg handling.</li>
 *   <li>{@link #findSelfMethodParameters} — resolves self-method parameter types
 *       for automatic {@code &} injection on struct arguments.</li>
 *   <li>{@link #isLocalFieldAccessProducingOption} — determines whether a local field
 *       access already yields {@code Option<T>} (suppresses extra {@code Some()} wrapping).</li>
 * </ul>
 *
 * <h2>Two-pass generation</h2>
 * <p>For multi-class projects use {@link RustCodeGenerator}:
 * pass 1 calls {@link #discoverTraits}/{@link #discoverClass} on all classes;
 * pass 2 calls {@link #toWritableObject} so cross-class type information is complete.
 */
public class Rust implements Emitter<StringBuilder> {

    /**
     * Shared type/method registry loaded once from {@code rust-registry.json}.
     * Drives all type-category checks ({@link #isMap}, {@link #isList}, etc.) and
     * the {@link #convertCommonType} switch.  Populated at class-load time; safe to
     * share across threads.
     */
    private static final RustMethodRegistry METHOD_REGISTRY;
    public static final String STD_SYNC_ATOMIC_ATOMIC_I_32_ORDERING = "std::sync::atomic::{AtomicI32,Ordering}";

    static {
        RustMethodRegistry r;
        try {
            r = RustMethodRegistry.loadFromClasspath();
        } catch (Exception e) {
            // Emit a warning but allow the class to load so existing unit tests
            // that do not exercise the registry-backed helpers still pass.
            System.err.println("[Rust emitter] WARNING: Failed to load rust-registry.json — " +
                               "type-category helpers will return false. " + e.getMessage());
            r = RustMethodRegistry.builder().build();
        }
        METHOD_REGISTRY = r;
    }

    //private StringBuilder sb;
    private static final String INDENT = "    ";
    private int closureCount = 0;
    private Set<String> imports;
    private List<Method> lateMethods = new ArrayList<>();
    private Class currentClass;
    private String postDecrement = null;
    private Set<String> knownTraits; // Track known trait names (shared across instances)
    private Set<String> statefulTraits; // Track traits with stateful methods (shared across instances)
    /**
     * Registry of PAST Class definitions keyed by PascalCase simple name.
     * Populated in a first pass via discoverClass().  Used to look up field
     * initialiser status across class boundaries (e.g. when a workflow method
     * accesses fields of an output struct that lives in a different class).
     */
    private final Map<String, Class> classRegistry = new HashMap<>();
    private boolean needsValueEnum = false; // Track if Value enum needs to be generated for heterogeneous arrays
    private boolean currentMethodConsumesSelf = false; // True when the current method uses consuming (mut self) receiver
    private boolean currentMethodIsMutable = false;    // True when the current method uses &mut self receiver
    private boolean currentMethodMutableFirstParam = false; // True when first param is mut T (MutableFirstParam annotation)
    private boolean currentEmittingTraitDefaultBody = false; // True when inside a trait's default method body
    private String currentMethodFirstParamName = null;      // Raw name of the first param when MutableFirstParam
    private Set<String> currentMethodRefParamNames = new HashSet<>(); // Raw names of &T ref params in current MutableFirstParam method
    private TypeName currentMethodReturnType = null;         // Declared return type of the current method (for convertWithType in RETURN handler)
    /**
     * "TraitName.methodName" pairs for all methods carrying MutableFirstParam — shared across
     * all emitter instances via RustCodeGenerator.  Scoped by trait name to avoid false matches
     * when two traits happen to share a generic method name (e.g. "process").
     */
    private final Set<String> mutableFirstParamMethodNames;
    private final TypeRegistry typeRegistry; // Type registry from type checking phase (may be null)
    private String currentPackageName;       // Set in toWritableObject; used to look up ClassSignature
    private ClassSignature currentClassSignature; // Looked up at start of emit(); null when no registry
    private String currentClassName;

    // configuration for default constructor
    boolean emitDefaultConstructorParameters =false;

    /**
     * Create a Rust emitter with a shared trait registry
     * @param knownTraits A shared set of known trait names across all code generation
     */
    public Rust(Set<String> knownTraits) {
        this(knownTraits, new HashSet<>(), new HashSet<>(), null);
    }

    /**
     * Create a Rust emitter with shared trait and stateful trait registries
     * @param knownTraits A shared set of known trait names across all code generation
     * @param statefulTraits A shared set of trait names that have stateful methods
     */
    public Rust(Set<String> knownTraits, Set<String> statefulTraits) {
        this(knownTraits, statefulTraits, new HashSet<>(), null);
    }

    /**
     * Create a Rust emitter with shared trait registries and the PAST type registry.
     * @param knownTraits A shared set of known trait names across all code generation
     * @param statefulTraits A shared set of trait names that have stateful methods
     * @param typeRegistry The TypeRegistry produced by type checking (may be null)
     */
    public Rust(Set<String> knownTraits, Set<String> statefulTraits, TypeRegistry typeRegistry) {
        this(knownTraits, statefulTraits, new HashSet<>(), typeRegistry);
    }

    /**
     * Create a Rust emitter with all shared registries and the PAST type registry.
     * @param knownTraits A shared set of known trait names across all code generation
     * @param statefulTraits A shared set of trait names that have stateful methods
     * @param mutableFirstParamMethodNames A shared set of method names that carry MutableFirstParam
     * @param typeRegistry The TypeRegistry produced by type checking (may be null)
     */
    public Rust(Set<String> knownTraits, Set<String> statefulTraits, Set<String> mutableFirstParamMethodNames, TypeRegistry typeRegistry) {
        this.knownTraits = knownTraits;
        this.statefulTraits = statefulTraits;
        this.mutableFirstParamMethodNames = mutableFirstParamMethodNames;
        this.typeRegistry = typeRegistry;
    }

    /**
     * Create a Rust emitter with an empty trait registry (for standalone use)
     */
    public Rust() {
        this(new HashSet<>(), new HashSet<>(), null);
    }

    /**
     * Discover traits from a Class definition without generating code.
     * Call this in a first pass to build up the trait registry.
     */
    public void discoverTraits(Class clazz) {
        if (clazz.isInterface || clazz.modifiers.contains(Modifier.ABSTRACT)) {
            String traitName = toPascalCase(clazz.name);
            knownTraits.add(traitName);

            // Check if this trait has any stateful methods (either auto-detected or explicit MutableReceiver)
            for (Method method : clazz.methods) {
                if (modifiesSelf(method) || consumesSelf(method)) {
                    statefulTraits.add(traitName);
                }
                // Track "TraitName.methodName" for all MutableFirstParam methods — used in
                // OBJECT_METHOD_CALL to inject &mut for the first argument without false-
                // positives from other traits sharing the same generic method name.
                if (hasMutableFirstParam(method)) {
                    mutableFirstParamMethodNames.add(traitName + "." + method.name);
                }
            }
        }
        // Also register any interfaces this class implements
        for (TypeName intfce : clazz.interfaces) {
            registerTraitFromTypeName(intfce);
        }
    }

    /**
     * Register a Class definition so the emitter can look up field initialisers across
     * class boundaries.  Call this in the same first pass as discoverTraits().
     *
     * <p>The principled rule used during emission is: a PAST field with no initialiser is
     * emitted as {@code Option<T>}; a field with an initialiser is emitted as plain {@code T}.
     * Without this registry the emitter cannot check whether a cross-class field access
     * already produces {@code Option<T>}, so it would incorrectly double-wrap with {@code Some()}.
     */
    public void discoverClass(Class clazz) {
        classRegistry.put(toPascalCase(clazz.name), clazz);
    }

    /**
     * Helper to register a trait from a TypeName
     */
    private void registerTraitFromTypeName(TypeName tn) {
        if (tn instanceof ClassName) {
            ClassName cn = (ClassName) tn;
            String traitName = toPascalCase(cn.simpleName);
            knownTraits.add(traitName);
        } else if (tn instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) tn;
            registerTraitFromTypeName(pt.rawType);
        }
    }

    public StringBuilder addHeader(StringBuilder sb, String templateName, String packge, StackTraceElement stackTraceElement) {
        sb.insert(0, "// Generated by ProvToolbox for template configuration '" + templateName + "'\n"
                + "// by class " + stackTraceElement.getClassName() + ", method " + stackTraceElement.getMethodName() +
                ",\n// in file " + stackTraceElement.getFileName() + ", at line " + stackTraceElement.getLineNumber() + "\n\n");
        return sb;
    }

    public WritableObject toWritableObject(Class clazz, String className, String packge, StackTraceElement stackTraceElement) {
        this.currentPackageName = packge;
        this.currentClassName = className;
        StringBuilder theBuffer = emit(clazz);

        addHeader(theBuffer, className, packge, stackTraceElement);
        return new WritableObject() {
            @Override
            public void writeTo(File directory) throws IOException {
                Path destination = Paths.get(directory.getAbsolutePath()).resolve(packge.replace('.', '/'));
                destination.toFile().mkdirs();
                Path path = destination.resolve(toSnakeCase(clazz.name) + ".rs");
                Files.writeString(path, theBuffer.toString());
            }
        };
    }

    @Override
    public StringBuilder emit(Class clazz) {

        StringBuilder sb = emit(clazz, new StringBuilder());

        // Single declareImports call — all emission (struct impl + trait impls) is done,
        // so the imports set is complete and no type will be added twice.
        if (!imports.isEmpty()) {
            declareImports("Importing ", sb);
        }

        insertRustDirectives(sb);

        return sb;
    }

    public StringBuilder emit(Class clazz,StringBuilder sb) {
        this.imports = new HashSet<>();
        this.lateMethods = new ArrayList<>();
        this.closureCount = 0;
        this.currentClass = clazz;
        this.currentClassSignature = (typeRegistry != null && currentPackageName != null)
                ? typeRegistry.lookup(clazz.name, currentPackageName) : null;



        // Check if this is an interface or abstract class (both become Rust traits)
        if (clazz.isInterface || clazz.modifiers.contains(Modifier.ABSTRACT)) {
            return emitTrait(clazz,sb);
        }

        // Suppress Serialize/Deserialize when the class is annotated with @NoSerialization
        boolean noSerialization = clazz.annotation.stream().anyMatch(a -> a instanceof NoSerialization);

        // Add common imports
        if (!noSerialization) {
            imports.add("serde::{Serialize, Deserialize}");
        }

        // Import HashMap if used by any field type, method parameter, or return type
        boolean needsHashMap = clazz.fields.stream().anyMatch(f -> isMap(f.type))
                || clazz.methods.stream().anyMatch(m ->
                        (m.returnType != null && isMap(m.returnType))
                        || (m.parameters != null && m.parameters.stream().anyMatch(p -> isMap(p.type))));
        if (needsHashMap) {
            imports.add("std::collections::HashMap");
        }

        // Collect imports for types used in field declarations, method parameters, and return types
        for (Field field : clazz.fields) {
            addTypeImport(field.type);
        }
        for (Method method : clazz.methods) {
            if (method.parameters != null) {
                for (Parameter param : method.parameters) {
                    addTypeImport(param.type);
                }
            }
            if (method.returnType != null) {
                addTypeImport(method.returnType);
            }
        }

        // Struct documentation
        if (!clazz.comments.isEmpty()) {
            for (Comment comment : clazz.comments) {
                List<String> lines = convert(comment);
                for (String line : lines) {
                    sb.append("/// ").append(line).append("\n");
                }
            }
        }

        // Derive common traits — omit entirely when any field holds a Box<dyn Trait> or
        // Vec<Box<dyn Any>>, because those types do not implement Clone or Debug.
        boolean hasDynField = clazz.fields.stream()
                .filter(f -> !f.modifiers.contains(Modifier.STATIC))
                .anyMatch(f -> isKnownTrait(f.type) || hasObjectTypeArgument(f.type));
        // Classes with a concrete superclass contain an inner_parent field whose type
        // may not implement Clone/Debug (e.g. BeanHistory contains Box<dyn Any> fields).
        boolean hasConcreteSuperclass = hasConcreteSuperclass(clazz);
        if (hasConcreteSuperclass) hasDynField = true; // conservative: suppress derives
        // AtomicI32 (mapped from AtomicInteger) does not implement Clone, so structs that
        // contain it (directly or transitively via a field whose type is a no-clone struct)
        // must not derive Clone (or Serialize/Deserialize which imply Clone).
        boolean hasAtomicField = clazz.fields.stream()
                .filter(f -> !f.modifiers.contains(Modifier.STATIC))
                .anyMatch(f -> containsAtomicType(f.type) || fieldTypeHasAtomicField(f.type));
        if (!hasDynField) {
            if (hasAtomicField) {
                sb.append("#[derive(Debug)]\n");
            } else if (noSerialization) {
                sb.append("#[derive(Debug, Clone)]\n");
            } else {
                sb.append("#[derive(Debug, Clone, Serialize, Deserialize)]\n");
            }
        }

        // Struct declaration with optional generic type parameters
        sb.append("pub struct ").append(toPascalCase(clazz.name)).append(typeParamDecl(clazz));

        sb.append(" {\n");

        // Instance fields
        List<Field> instanceFields = clazz.fields.stream()
                .filter(f -> !f.modifiers.contains(Modifier.STATIC))
                .collect(Collectors.toList());

        for (Field field : instanceFields) {
            if (!field.comments.isEmpty()) {
                for (Comment comment : field.comments) {
                    List<String> lines = convert(comment);
                    for (String line : lines) {
                        sb.append(INDENT).append("/// ").append(line).append("\n");
                    }
                }
            }
            String visibility = field.modifiers.contains(Modifier.PUBLIC) ? "pub " : "";
            // LateEmittedClass: the 'outer' field is a lifetime-annotated borrow, not owned.
            if (currentClass instanceof LateEmittedClass && "outer".equals(field.name)) {
                sb.append(INDENT).append(visibility)
                        .append("outer: &'a ")
                        .append(convertTypeToRust(field.type))
                        .append(",\n");
                continue;
            }
            // Trait types cannot be stored by value in a struct — use Box<dyn Trait>
            String rustType = isKnownTrait(field.type)
                    ? "Box<dyn " + convertTypeToRust(field.type) + ">"
                    : convertTypeToRust(field.type);
            if (field.initialiser == null) {
                rustType = "Option<" + rustType + ">";
                if (!noSerialization) {
                    sb.append(INDENT).append("#[serde(skip_serializing_if = \"Option::is_none\")]\n");
                }
            }
            sb.append(INDENT).append(visibility)
                    .append(sanitizeName(toSnakeCase(field.name)))
                    .append(": ")
                    .append(rustType)
                    .append(",\n");
        }

        // Synthetic inner_parent field for concrete superclass inheritance via composition.
        if (hasConcreteSuperclass) {
            String superTypeStr = convertTypeToRust(clazz.superclass);
            addTypeImport(clazz.superclass);
            sb.append(INDENT).append("inner_parent: Option<").append(superTypeStr).append(">,\n");
        }

        sb.append("}\n\n");

        // Implementation block (inherent methods)
        sb.append("impl").append(typeParamDecl(clazz))
                .append(" ").append(toPascalCase(clazz.name)).append(typeParamArgs(clazz))
                .append(" {\n");

        // Constructor (new method)
        if (clazz.constructors.isEmpty()) {
            emitDefaultConstructor(instanceFields, sb);
        } else {
            for (Constructor constructor : clazz.constructors) {
                Optional<String> name=isOverloadedConstructor(constructor);

                if (name.isPresent()) {
                    String constructorName = name.get();

                    Method rustMethodAsConstructor = METHOD(constructorName)
                            .PARAMETERS(constructor.parameters)
                            .MODIFIERS(constructor.modifiers.toArray(new Modifier[0]))
                            .BODY(constructor.body.toArray(new Statement[0]))
                            //.ANNOTATIONS(constructor.annotation.toArray(new String[0]))
                            .COMMENTS(constructor.comments.toArray(new Comment[0]));
                    for (Field field : clazz.fields) {
                        String fieldName = sanitizeName(field.name);
                        if (!field.modifiers.contains(Modifier.STATIC)) {
                            if (field.initialiser != null) {
                                rustMethodAsConstructor.BODY(
                                        ASSIGNMENT(METHOD_CALL(VARIABLE("this"), fieldName), field.initialiser));

                            }
                        }
                    }
                    rustMethodAsConstructor.COMMENT("Note: this method was originally a PAST constructor, but was renamed to avoid name clashes due to overloading. \n It will not be recognized as a constructor by Python code, so it should be called explicitly by its name.");

                    emitMethod(rustMethodAsConstructor, sb);


                } else {
                    emitConstructor(constructor, instanceFields, sb);
                }
            }
        }

        // Instance methods (excluding those that implement an interface or override abstract superclass
        // methods — those are emitted in their respective trait impl blocks)
        Set<String> superclassAbstractMethods = getSuperclassAbstractMethodNames(clazz.superclass);
        for (Method method : clazz.methods) {
            if (!method.modifiers.contains(Modifier.STATIC)
                    && !isInterfaceImplementation(method)
                    && !superclassAbstractMethods.contains(method.name)) {
                emitMethod(method, sb);
            }
        }

        // Late methods (from closure conversions)
        for (Method method : lateMethods) {
            emitMethod(method, sb);
        }

        sb.append("}\n\n");

        // Static methods and associated functions
        List<Method> staticMethods = clazz.methods.stream()
                .filter(m -> m.modifiers.contains(Modifier.STATIC))
                .collect(Collectors.toList());

        if (!staticMethods.isEmpty() || hasStaticFields(clazz.fields)) {
            sb.append("impl").append(typeParamDecl(clazz))
                    .append(" ").append(toPascalCase(clazz.name)).append(typeParamArgs(clazz))
                    .append(" {\n");

            for (Method method : staticMethods) {
                emitMethod(method, sb);
            }

            // Static fields as associated constants or static items
            emitStaticFields(clazz.fields,sb);

            sb.append("}\n\n");
        }

        // Add Value enum if heterogeneous arrays were used
        if (needsValueEnum) {
            emitValueEnum(sb);
        }

        // Add trait implementations if interfaces are specified.
        // emitTraitImplementations calls emitMethod() which may add further imports via
        // addTypeImport(), so we must not call declareImports() before this point.
        if (!clazz.interfaces.isEmpty() || clazz.superclass != null) {
            emitTraitImplementations(clazz, sb);
        }



        Set<String> savedImports=imports;

        if (!lateEmittedClasses.isEmpty()) {
            List<LateEmittedClass> tmp=lateEmittedClasses.stream().map(c -> c).collect(Collectors.toList());
            lateEmittedClasses.clear();
            sb.append("\n\n");

            for (Class lateClazz : tmp) {
                sb.append("// Late emitted class due to anonymous class\n");
                emit(lateClazz,sb);
                sb.append("\n\n");
            }
        }

        imports.addAll(savedImports);



        return sb;
    }

    /**
     * Emit the Value enum for heterogeneous arrays
     */
    private void emitValueEnum(StringBuilder sb) {
        sb.append("/// Enum for heterogeneous array values\n");
        sb.append("#[derive(Debug, Clone, Serialize, Deserialize)]\n");
        sb.append("pub enum Value {\n");
        sb.append(INDENT).append("String(String),\n");
        sb.append(INDENT).append("Int(i32),\n");
        sb.append(INDENT).append("Float(f32),\n");
        sb.append(INDENT).append("Bool(bool),\n");
        sb.append(INDENT).append("Null,\n");
        sb.append("}\n\n");

        // Generate helper methods for type extraction
        sb.append("impl Value {\n");

        // as_string() -> Option<&String>
        sb.append(INDENT).append("/// Extract String value as reference\n");
        sb.append(INDENT).append("pub fn as_string(&self) -> Option<&String> {\n");
        sb.append(INDENT).append(INDENT).append("match self {\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("Value::String(s) => Some(s),\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("_ => None,\n");
        sb.append(INDENT).append(INDENT).append("}\n");
        sb.append(INDENT).append("}\n\n");

        // as_int() -> Option<i32>
        sb.append(INDENT).append("/// Extract Int value\n");
        sb.append(INDENT).append("pub fn as_int(&self) -> Option<i32> {\n");
        sb.append(INDENT).append(INDENT).append("match self {\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("Value::Int(i) => Some(*i),\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("_ => None,\n");
        sb.append(INDENT).append(INDENT).append("}\n");
        sb.append(INDENT).append("}\n\n");

        // as_float() -> Option<f32>
        sb.append(INDENT).append("/// Extract Float value\n");
        sb.append(INDENT).append("pub fn as_float(&self) -> Option<f32> {\n");
        sb.append(INDENT).append(INDENT).append("match self {\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("Value::Float(f) => Some(*f),\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("_ => None,\n");
        sb.append(INDENT).append(INDENT).append("}\n");
        sb.append(INDENT).append("}\n\n");

        // as_bool() -> Option<bool>
        sb.append(INDENT).append("/// Extract Bool value\n");
        sb.append(INDENT).append("pub fn as_bool(&self) -> Option<bool> {\n");
        sb.append(INDENT).append(INDENT).append("match self {\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("Value::Bool(b) => Some(*b),\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("_ => None,\n");
        sb.append(INDENT).append(INDENT).append("}\n");
        sb.append(INDENT).append("}\n");

        sb.append("}\n\n");
    }


    private Optional<String> isOverloadedConstructor(Constructor constructor) {
        Optional<String> overloaded=Optional.empty();
        if (constructor.annotation != null) {
            Optional<RustAnnotation> annotation=constructor.annotation.stream()
                    .filter(annot -> annot instanceof RustAnnotation)
                    .map(annot -> (RustAnnotation) annot)
                    .filter(annot -> OverloadedMethodRust.NAME.equals(annot.getName()))
                    .findFirst()
                    ;
            if (annotation.isPresent()) {
                overloaded= Optional.of(((OverloadedMethodRust) annotation.get()).getAltName());
                //System.out.println("Constructor with @OverloadedMethod annotation, using alt name: " + constructorName);
            }
        }
        return overloaded;
    }

    /**
     * Emit a trait definition (Rust equivalent of interface)
     */
    private StringBuilder emitTrait(Class clazz, StringBuilder sb) {
        // Note: Trait should already be registered via discoverTraits()
        String traitName = toPascalCase(clazz.name);

        // Trait documentation
        if (!clazz.comments.isEmpty()) {
            for (Comment comment : clazz.comments) {
                List<String> lines = convert(comment);
                for (String line : lines) {
                    sb.append("/// ").append(line).append("\n");
                }
            }
        }

        // Trait declaration
        sb.append("pub trait ").append(traitName);

        // Add generic type parameters to trait
        if (!clazz.typeVariables.isEmpty()) {
            sb.append("<");
            for (int i = 0; i < clazz.typeVariables.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(clazz.typeVariables.get(i).name);
            }
            sb.append(">");
        }

        // Super traits (interfaces this trait extends)
        if (!clazz.interfaces.isEmpty()) {
            sb.append(": ");
            for (int i = 0; i < clazz.interfaces.size(); i++) {
                if (i > 0) sb.append(" + ");
                TypeName intfce = clazz.interfaces.get(i);
                String superTraitName = convertInterfaceToTrait(intfce);
                sb.append(superTraitName);
                addTraitImport(intfce);
            }
        }

        sb.append(" {\n");

        // Collect imports for types used in method signatures
        for (Method method : clazz.methods) {
            if (method.parameters != null) {
                for (Parameter param : method.parameters) {
                    addTypeImport(param.type);
                }
            }
            if (method.returnType != null) {
                addTypeImport(method.returnType);
            }
        }

        // Import HashMap if used in any method parameter or return type
        boolean needsHashMap = clazz.methods.stream().anyMatch(m ->
                (m.returnType != null && isMap(m.returnType))
                || (m.parameters != null && m.parameters.stream().anyMatch(p -> isMap(p.type))));
        if (needsHashMap) {
            imports.add("std::collections::HashMap");
        }

        // Trait methods: abstract methods and non-@Override concrete methods (e.g. helpers).
        // @Override methods (interface implementations) are NOT emitted here — they belong in
        // impl InterfaceName for ConcreteType blocks emitted by emitTraitImplementations().
        for (Method method : clazz.methods) {
            if (isInterfaceImplementation(method)) continue;
            emitTraitMethod(method, sb);
        }

        // Abstract getter methods for instance fields — traits cannot have fields in Rust,
        // so we expose them via &mut getters that trait implementations must provide.
        if (!clazz.isInterface && !clazz.fields.isEmpty()) {
            for (Field field : clazz.fields) {
                if (field.modifiers.contains(Modifier.STATIC)) continue;
                String fieldSnake = sanitizeName(toSnakeCase(field.name));
                String rustType = isKnownTrait(field.type)
                        ? "Box<dyn " + convertTypeToRust(field.type) + ">"
                        : convertTypeToRust(field.type);
                if (field.initialiser == null) rustType = "Option<" + rustType + ">";
                addTypeImport(field.type);
                sb.append(INDENT).append("fn ").append(fieldSnake).append("(&mut self) -> &mut ").append(rustType).append(";\n");
            }
        }

        sb.append("}\n\n");

        // Add imports at the beginning
        if (!imports.isEmpty()) {
            declareImports("Importing (emitTraits) ", sb);
        }

        insertRustDirectives(sb);


        return sb;
    }

    private void insertRustDirectives(StringBuilder sb) {
        sb.insert(0,"#![allow(dead_code, unused_variables, unused_imports, unused_mut)]\n\n");
    }

    private void declareImports(String x, StringBuilder sb) {
        StringBuilder importSection = new StringBuilder();
        for (String imprt : imports) {
           // System.out.println(x + imprt);

            if (LOGGER.equals(imprt)) continue;
            importSection.append("use ").append(imprt).append(";\n");
        }
        if (!importSection.isEmpty()) {
            importSection.append("\n");
            sb.insert(0, importSection.toString());
        }
        imports.clear();
    }

    /**
     * Emit trait method signature (no body)
     */
    private void emitTraitMethod(Method method, StringBuilder sb) {
        // Method documentation
        if (method.comments != null && !method.comments.isEmpty()) {
            for (Comment comment : method.comments) {
                List<String> lines = convert(comment);
                for (String line : lines) {
                    sb.append(INDENT).append("/// ").append(line).append("\n");
                }
            }
        }

        String traitMethodAltName = findAltNameForDeclaration(method);
        String traitMethodName = (traitMethodAltName != null) ? traitMethodAltName : method.name;
        sb.append(INDENT).append("fn ").append(sanitizeName(toSnakeCase(traitMethodName)));

        // MutableFirstParam methods need explicit lifetime '<'a> on the trait signature too.
        boolean traitMFP = hasMutableFirstParam(method);
        // Generic type parameters
        if (traitMFP) {
            if (method.typeVariables != null && !method.typeVariables.isEmpty()) {
                sb.append("<'a, ");
                for (int i = 0; i < method.typeVariables.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(convertTypeToRust(method.typeVariables.get(i)));
                }
                sb.append(">");
            } else {
                sb.append("<'a>");
            }
        } else if (method.typeVariables != null && !method.typeVariables.isEmpty()
                && !hasPhantomTypeVariable(method)) {
            // Phantom type variables (e.g. T in `get<T>(Class<T>, String) -> T`) are suppressed:
            // Class<T> maps to &'static str so T is invisible in Rust, making the trait
            // non-dyn-compatible. We erase T and emit Box<dyn Any> as the return type instead.
            sb.append("<");
            for (int i = 0; i < method.typeVariables.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(convertTypeToRust(method.typeVariables.get(i)));
            }
            sb.append(">");
        }

        sb.append("(");

        // Self parameter for instance methods
        if (!method.modifiers.contains(Modifier.STATIC)) {
            sb.append("&");
            if (modifiesSelf(method) || consumesSelf(method) || implementsStatefulTrait(method)) {
                sb.append("mut ");
            }
            sb.append("self");
            if (method.parameters != null && !method.parameters.isEmpty()) {
                sb.append(", ");
            }
        }

        // Parameters
        if (method.parameters != null) {
            boolean mutableFirst = hasMutableFirstParam(method);
            for (int i = 0; i < method.parameters.size(); i++) {
                if (i > 0) sb.append(", ");
                Parameter param = method.parameters.get(i);
                // MutableFirstParam: first param is &'a mut T (lifetime-annotated mutable borrow).
                // The trait declaration and the impl must agree on the lifetime parameter.
                String rustParamType = (mutableFirst && i == 0)
                        ? "&'a mut " + convertTypeToRustParam(param.type)
                        : convertTypeToRustTraitParam(param.type);
                sb.append(sanitizeName(toSnakeCase(param.name)))
                        .append(": ")
                        .append(rustParamType);
            }
        }

        sb.append(")");

        // Return type
        // MutableFirstParam: return &'a mut T — the same reference passed as first param.
        if (method.returnType != null && !isVoidType(method.returnType)) {
            if (traitMFP) {
                sb.append(" -> &'a mut ").append(convertTypeToRust(method.returnType));
            } else if (hasPhantomTypeVariable(method)) {
                // Phantom T erased: return Box<dyn Any> to keep the trait dyn-compatible.
                sb.append(" -> Box<dyn std::any::Any>");
            } else {
                sb.append(" -> ").append(convertTypeToRust(method.returnType));
            }
        }

        // For abstract class traits: if the method has a body (non-abstract concrete method),
        // emit a default implementation.  Interface traits always get just `;`.
        // @Override methods are NOT emitted here — they belong in impl InterfaceName for ConcreteType blocks.
        boolean hasBody = !method.modifiers.contains(Modifier.ABSTRACT)
                && !isInterfaceImplementation(method)
                && method.body != null && !method.body.isEmpty();
        if (hasBody) {
            sb.append(" {\n");
            currentMethodIsMutable = modifiesSelf(method) || consumesSelf(method) || implementsStatefulTrait(method);
            currentEmittingTraitDefaultBody = true;
            for (int i = 0; i < method.body.size(); i++) {
                Statement stmt = method.body.get(i);
                boolean isLast = (i == method.body.size() - 1);
                emitStatement(stmt, INDENT + INDENT, isLast, sb);
            }
            currentEmittingTraitDefaultBody = false;
            sb.append(INDENT).append("}\n\n");
        } else {
            sb.append(";\n\n");
        }
    }

    /**
     * Returns true if the method implements an interface method (i.e. carries @Override).
     */
    private boolean isInterfaceImplementation(Method method) {
        return method.annotation.stream()
                .anyMatch(a -> a instanceof OverrideAnnotation);
    }

    /**
     * Emit trait implementations for a struct
     */
    private void emitTraitImplementations(Class clazz, StringBuilder sb) {
        for (TypeName intfce : clazz.interfaces) {
            String traitName = convertInterfaceToTrait(intfce);
            //System.out.println("addTraintImport for " + intfce + " "  + traitName);
            addTraitImport(intfce);

            sb.append("impl").append(typeParamDecl(clazz))
                    .append(" ").append(traitName)
                    .append(" for ").append(toPascalCase(clazz.name)).append(typeParamArgs(clazz))
                    .append(" {\n");

            // Emit only methods that implement this trait (annotated with @Override)
            for (Method method : clazz.methods) {
                if (!method.modifiers.contains(Modifier.STATIC) && isInterfaceImplementation(method)) {
                    emitMethod(method, true, sb);
                }
            }

            sb.append("}\n\n");
        }

        // Superclass handling: if the class extends an abstract class (now a Rust trait),
        // emit impl SuperclassTrait for Self containing the overriding methods.
        if (clazz.superclass != null) {
            Set<String> abstractMethodNames = getSuperclassAbstractMethodNames(clazz.superclass);
            if (!abstractMethodNames.isEmpty()) {
                String superTraitName = convertInterfaceToTrait(clazz.superclass);
                addTraitImport(clazz.superclass);
                sb.append("impl").append(typeParamDecl(clazz))
                        .append(" ").append(superTraitName)
                        .append(" for ").append(toPascalCase(clazz.name)).append(typeParamArgs(clazz))
                        .append(" {\n");
                for (Method method : clazz.methods) {
                    if (!method.modifiers.contains(Modifier.STATIC)
                            && abstractMethodNames.contains(method.name)) {
                        emitMethod(method, true, sb);
                    }
                }
                sb.append("}\n\n");
            }

            // Also emit impl InterfaceName for ConcreteClass using method bodies from the superclass.
            // When the class extends an abstract superclass (a Rust trait) that itself implements
            // interfaces, each interface must be explicitly implemented by the concrete class.
            // The method bodies come from the superclass PAST Class object (looked up in classRegistry).
            // NOTE: skip this for classes with a concrete superclass — those use inner_parent
            // composition and cannot re-use the parent's method bodies (which reference parent fields).
            if (!hasConcreteSuperclass(clazz)) {
                ClassName superCn = null;
                if (clazz.superclass instanceof ClassName) {
                    superCn = (ClassName) clazz.superclass;
                } else if (clazz.superclass instanceof ParameterizedType) {
                    TypeName raw = ((ParameterizedType) clazz.superclass).rawType;
                    if (raw instanceof ClassName) superCn = (ClassName) raw;
                }
                if (superCn != null) {
                    Class superclazz = classRegistry.get(toPascalCase(superCn.simpleName));
                    if (superclazz != null && !superclazz.interfaces.isEmpty()) {
                        // Collect imports for all types referenced by the superclass's methods,
                        // since those methods will be emitted into the current file.
                        for (Method superMethod : superclazz.methods) {
                            if (superMethod.parameters != null) {
                                for (Parameter param : superMethod.parameters) {
                                    addTypeImport(param.type);
                                }
                            }
                            if (superMethod.returnType != null) {
                                addTypeImport(superMethod.returnType);
                            }
                        }
                        // Temporarily switch currentClass and currentClassSignature to the superclass
                        // so that implementsStatefulTrait() and findAltNameForDeclaration() resolve
                        // correctly against the superclass's interfaces and overloaded method names.
                        Class savedCurrentClass = currentClass;
                        ClassSignature savedCurrentClassSignature = currentClassSignature;
                        currentClass = superclazz;
                        currentClassSignature = (typeRegistry != null && superCn.packge != null)
                                ? typeRegistry.lookup(superclazz.name, superCn.packge) : null;
                        for (TypeName intfce : superclazz.interfaces) {
                            // Skip if clazz already implements this interface directly
                            // (its own @Override methods handle it in the first loop above).
                            boolean alreadyImplemented = clazz.interfaces.stream()
                                    .anyMatch(i -> convertInterfaceToTrait(i).equals(convertInterfaceToTrait(intfce)));
                            if (alreadyImplemented) continue;
                            String traitName = convertInterfaceToTrait(intfce);
                            addTraitImport(intfce);
                            sb.append("impl").append(typeParamDecl(clazz))
                                    .append(" ").append(traitName)
                                    .append(" for ").append(toPascalCase(clazz.name)).append(typeParamArgs(clazz))
                                    .append(" {\n");
                            for (Method method : superclazz.methods) {
                                if (!method.modifiers.contains(Modifier.STATIC)
                                        && isInterfaceImplementation(method)
                                        && method.body != null && !method.body.isEmpty()) {
                                    // true = in trait impl block (suppress 'pub' on methods)
                                    emitMethod(method, true, sb);
                                }
                            }
                            sb.append("}\n\n");
                        }
                        currentClass = savedCurrentClass;
                        currentClassSignature = savedCurrentClassSignature;
                    }
                }
            }
        }
        // imports and insertRustDirectives are handled by the caller (emit(Class))
    }

    /**
     * Returns the set of abstract method names declared in the superclass (if resolvable).
     * Used to decide which methods of the current class override abstract superclass methods.
     */
    private Set<String> getSuperclassAbstractMethodNames(TypeName superclass) {
        if (superclass == null || typeRegistry == null) return java.util.Collections.emptySet();
        ClassName cn = null;
        if (superclass instanceof ClassName) {
            cn = (ClassName) superclass;
        } else if (superclass instanceof ParameterizedType) {
            TypeName raw = ((ParameterizedType) superclass).rawType;
            if (raw instanceof ClassName) cn = (ClassName) raw;
        }
        if (cn == null) return java.util.Collections.emptySet();
        ClassSignature sig = typeRegistry.lookup(cn.simpleName, cn.packge);
        if (sig == null) return java.util.Collections.emptySet();
        Set<String> names = new java.util.HashSet<>();
        for (MethodSignature ms : sig.methods) {
            if (ms.modifiers.contains(Modifier.ABSTRACT)) {
                names.add(ms.name);
            }
        }
        return names;
    }

    /**
     * Convert an interface TypeName to its Rust trait name
     */
    private String convertInterfaceToTrait(TypeName intfce) {
        if (intfce instanceof ClassName) {
            ClassName cn = (ClassName) intfce;
            return toPascalCase(cn.simpleName);
        } else if (intfce instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) intfce;
            String baseName = convertInterfaceToTrait(pt.rawType);
            StringBuilder result = new StringBuilder(baseName);
            if (pt.typeArguments.length > 0) {
                result.append("<");
                for (int i = 0; i < pt.typeArguments.length; i++) {
                    if (i > 0) result.append(", ");
                    result.append(convertTypeToRust(pt.typeArguments[i]));
                }
                result.append(">");
            }
            return result.toString();
        }
        return intfce.toString();
    }

    /**
     * Returns {@code "<T: Bound1 + Bound2>"} for the class generic-parameter declaration,
     * or {@code ""} if the class has no type variables.
     * Used in struct and impl headers.
     */
    private String typeParamDecl(Class clazz) {
        // LateEmittedClass structs capture the outer class via a &'a reference — include lifetime.
        boolean hasLifetime = clazz instanceof LateEmittedClass;
        if (!hasLifetime && (clazz.typeVariables == null || clazz.typeVariables.isEmpty())) return "";
        StringBuilder s = new StringBuilder("<");
        boolean first = true;
        if (hasLifetime) { s.append("'a"); first = false; }
        if (clazz.typeVariables != null) {
            for (TypeVariable tv : clazz.typeVariables) {
                if (!first) s.append(", ");
                s.append(tv.name);
                if (!tv.bounds.isEmpty()) {
                    s.append(": ");
                    for (int j = 0; j < tv.bounds.size(); j++) {
                        if (j > 0) s.append(" + ");
                        s.append(convertInterfaceToTrait(tv.bounds.get(j)));
                    }
                }
                first = false;
            }
        }
        s.append(">");
        return s.toString();
    }

    /**
     * Returns {@code "<T>"} for use as the concrete type argument after the struct name in impl
     * headers (e.g. {@code impl<T: Foo> Bar<T>}), or {@code ""} if the class has no type
     * variables.
     */
    private String typeParamArgs(Class clazz) {
        boolean hasLifetime = clazz instanceof LateEmittedClass;
        if (!hasLifetime && (clazz.typeVariables == null || clazz.typeVariables.isEmpty())) return "";
        StringBuilder s = new StringBuilder("<");
        boolean first = true;
        if (hasLifetime) { s.append("'a"); first = false; }
        if (clazz.typeVariables != null) {
            for (TypeVariable tv : clazz.typeVariables) {
                if (!first) s.append(", ");
                s.append(tv.name);
                first = false;
            }
        }
        s.append(">");
        return s.toString();
    }

    /**
     * Add import for a trait based on its package
     */
    private void addTraitImport(TypeName intfce) {
        if (intfce instanceof ClassName) {
            ClassName cn = (ClassName) intfce;

            // Always add import for traits in the same crate using crate:: prefix
            // Skip only if it's a built-in Rust trait or from past.* (which are mapped to std types)
            if (cn.packge == null || cn.packge.isEmpty() || !cn.packge.startsWith("past.")) {
                // Build full module path from package, using original name to preserve underscores before digits

                // don't add an import for the current class
                if (cn.simpleName.equals(this.currentClass.name) &&
                        Objects.equals(cn.packge, this.currentPackageName)) {
                    //don't add an import for the outer class
                }  else{
                    String modulePath = buildModulePath(cn.packge, cn.simpleName);
                    imports.add(modulePath);
                }
            }
        } else if (intfce instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) intfce;
            addTraitImport(pt.rawType);
        }
    }

    /**
     * Add import for any type used in method signatures (parameters, return types).
     * Skips primitive types and types from past.* packages (mapped to std types).
     */
    private void addTypeImport(TypeName typeName) {
        if (typeName instanceof ClassName) {
            ClassName cn = (ClassName) typeName;
            // Skip primitive/common types and past.* package types (mapped to Rust builtins)
            if (cn.packge != null && !cn.packge.isEmpty() && !cn.packge.startsWith("past.")) {
                if (convertCommonType(cn.simpleName) == null) {
                    // don't add an import for the current class
                    if (cn.simpleName.equals(this.currentClass.name) &&
                            Objects.equals(cn.packge, this.currentPackageName)) {
                    } else if (isParentClass(cn)) {

                    } else {
                        // Use original name to preserve underscores before digits in file path
                        String modulePath = buildModulePath(cn.packge, cn.simpleName);
                        imports.add(modulePath);
                    }
                }
            }
        } else if (typeName instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) typeName;
            addTypeImport(pt.rawType);
            for (TypeName arg : pt.typeArguments) {
                if (arg instanceof ClassName) {
                    ClassName cn = (ClassName) arg;
                    if (cn.simpleName.equals("AtomicInteger") && Objects.equals(cn.packge, "past.lang")) {
                        imports.add(STD_SYNC_ATOMIC_ATOMIC_I_32_ORDERING);
                    } else {
                        addTypeImport(arg);
                    }

                } else {
                    addTypeImport(arg);
                }
            }
        }
    }

    /**
     * Build a Rust module path from a package name and type name
     * E.g., "org.example.templates.block.client.common" + "TemplateBlockProcessor"
     *    -> "crate::org::example::templates::block::client::common::template_block_processor::TemplateBlockProcessor"
     */
    /**
     * Build a Rust module path from a package name and the original PAST type name.
     * The original name is used to derive both the file name (snake_case) and the Rust type name (PascalCase),
     * preserving underscores before digits (e.g., Inputs_1 -> inputs_1 for file, Inputs1 for type).
     */
    private String buildModulePath(String packageName, String originalName) {
        String fileName = toSnakeCase(originalName);
        String rustTypeName = toPascalCase(originalName);

        if (packageName == null || packageName.isEmpty()) {
            return "crate::" + rustTypeName;
        }

        // Convert package to Rust module path (replace . with ::)
        String modulePath = packageName.replace(".", "::");

        return "crate::" + modulePath + "::" + fileName + "::" + rustTypeName;
    }

    /**
     * Check if a type is a known trait
     */
    private boolean isKnownTrait(TypeName tn) {
        if (tn instanceof ClassName) {
            ClassName cn = (ClassName) tn;
            String name = toPascalCase(cn.simpleName);
            return knownTraits.contains(name);
        } else if (tn instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) tn;
            return isKnownTrait(pt.rawType);
        }
        return false;
    }

    /**
     * Returns true when {@code clazz} has a superclass that is a concrete struct (not a trait).
     * In Rust, concrete superclasses cannot be inherited; instead we use composition via a
     * synthetic {@code inner_parent} field.
     */
    private boolean hasConcreteSuperclass(Class clazz) {
        if (clazz.superclass == null) return false;
        // If the superclass type is a known trait, it maps to a Rust trait (no inner_parent needed).
        return !isKnownTrait(clazz.superclass);
    }

    /**
     * Convert a type for use in a parameter position, using impl for traits.
     * String parameters use &str (borrowed slice) since callers pass string literals
     * and read-only string views are the idiomatic Rust parameter type.
     */
    private String convertTypeToRustParam(TypeName tn) {
        if (tn == null) return "()";

        // If this is a known trait type, use impl for static dispatch.
        // + 'static is required because the value may be stored in Box<dyn Trait + 'static>
        // (the implicit lifetime bound on Box<dyn Trait>). All generated concrete types are
        // fully owned and satisfy 'static, so this restriction is safe.
        if (isKnownTrait(tn)) {
            // Add import for this trait since it's being used
            addTraitImport(tn);

            if (tn instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) tn;
                StringBuilder result = new StringBuilder("impl ");
                result.append(toPascalCase(getSimpleNameFromType(pt.rawType)));
                if (pt.typeArguments.length > 0) {
                    result.append("<");
                    for (int i = 0; i < pt.typeArguments.length; i++) {
                        if (i > 0) result.append(", ");
                        result.append(convertTypeToRust(pt.typeArguments[i]));
                    }
                    result.append(">");
                }
                result.append(" + 'static");
                return result.toString();
            } else if (tn instanceof ClassName) {
                return "impl " + toPascalCase(((ClassName) tn).simpleName) + " + 'static";
            }
        }

        // String parameters use &str: callers pass bare string literals (&'static str)
        // and Rust idiom prefers borrowing over ownership for read-only string params.
        if (tn instanceof ClassName && "String".equals(((ClassName) tn).simpleName)) {
            return "&str";
        }

        // HashMap parameters are passed by mutable reference — callers may insert entries.
        if (isMap(tn)) {
            return "&mut " + convertTypeToRust(tn);
        }

        // Otherwise use the regular conversion
        return convertTypeToRust(tn);
    }

    /**
     * Convert a type for use in trait method parameters, using Option<&str> for String
     * and Option<T> for other types to mirror Java's nullable semantics.
     */
    private String convertTypeToRustTraitParam(TypeName tn) {
        if (tn == null) return "()";

        // If this is a known trait type, use impl for static dispatch (not wrapped in Option)
        if (isKnownTrait(tn)) {
            // Add import for this trait since it's being used
            addTraitImport(tn);

            if (tn instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) tn;
                StringBuilder result = new StringBuilder("impl ");
                result.append(toPascalCase(getSimpleNameFromType(pt.rawType)));
                if (pt.typeArguments.length > 0) {
                    result.append("<");
                    for (int i = 0; i < pt.typeArguments.length; i++) {
                        if (i > 0) result.append(", ");
                        result.append(convertTypeToRust(pt.typeArguments[i]));
                    }
                    result.append(">");
                }
                return result.toString();
            } else if (tn instanceof ClassName) {
                return "impl " + toPascalCase(((ClassName) tn).simpleName);
            }
        }

        // For String types, use &str (same as convertTypeToRustParam):
        // callers pass bare string literals and trait impls delegate to methods expecting &str.
        if (tn instanceof ClassName) {
            ClassName cn = (ClassName) tn;
            if ("String".equals(cn.simpleName)) {
                return "&str";
            }
        }

        // Collection types don't need Option wrapping — they can be empty
        // Vec: pass by immutable reference; HashMap: pass by mutable reference (callers may insert)
        if (isList(tn)) {
            return "&" + convertTypeToRust(tn);
        }
        if (isMap(tn)) {
            return "&mut " + convertTypeToRust(tn);
        }

        // Primitive types are Copy in Rust, wrap in Option to mirror Java's nullable semantics
        if (isPrimitiveType(tn)) {
            return "Option<" + convertTypeToRust(tn) + ">";
        }

        // Non-primitive struct/bean types: pass by reference.
        // Guard against double-referencing: if convertTypeToRust already produces a reference
        // (e.g. Class<T> → &'static str), do not prepend another &.
        String converted = convertTypeToRust(tn);
        return converted.startsWith("&") ? converted : "&" + converted;
    }

    /**
     * Get simple name from a TypeName
     */
    private String getSimpleNameFromType(TypeName tn) {
        if (tn instanceof ClassName) {
            return ((ClassName) tn).simpleName;
        }
        return tn.toString();
    }

    private boolean hasStaticFields(List<Field> fields) {
        return fields.stream().anyMatch(f -> f.modifiers.contains(Modifier.STATIC));
    }


    private String updateMethodNameIfOverloaded(MethodCall mc, String convertedObject, String callMethodName) {
        if ("self".equals(convertedObject) || (mc.object.inferredType instanceof ClassName) || isTypeVariableWithBound(mc.object.inferredType)) {
            int argCount = mc.arguments == null ? 0 : mc.arguments.size();
            if (argCount!=0) {
                List<TypeName> argumentTypes= mc.arguments.stream().map(a -> a.inferredType).collect(Collectors.toList());
                String resolvedAlt;
                if ("self".equals(convertedObject)) {
                    resolvedAlt=findAltNameForCall(mc.methodName, argumentTypes);
                } else {
                    ClassName cl=classForPurposeOfLookup(mc.object.inferredType);
                    resolvedAlt=findAltNameForCall(cl, callMethodName, argumentTypes);
                }
                if (resolvedAlt != null) {
                    callMethodName = sanitizeName(resolvedAlt);
                }
            }
        }
        return callMethodName;
    }


    private boolean isTypeVariableWithBound(TypeName tn) {
        return tn instanceof TypeVariable &&
                !((TypeVariable) tn).bounds.isEmpty() &&
                ((TypeVariable) tn).bounds.get(0) instanceof ClassName;
    }


    private String findAltNameForCall(String methodName, List<TypeName> argTypes) {
        if (currentClassSignature == null || typeRegistry == null) return null;
        for (MethodSignature ms : currentClassSignature.methods) {
            if (!ms.name.equals(methodName)) continue;
            if (!paramTypesMatch(argTypes, ms)) continue;
            for (PastAnnotation ann : ms.getAnnotations()) {
                if (ann instanceof OverloadedMethod) return ((OverloadedMethod) ann).getAltName();
            }
        }
        return null;
    }

    private String findAltNameForCall(ClassName className,String methodName, List<TypeName> argTypes) {
        if (typeRegistry==null) return null;
        ClassSignature sig=typeRegistry.lookup(className.simpleName, className.packge);
        if (sig == null) return null;
        for (MethodSignature ms : sig.methods) {
            if (!ms.name.equals(methodName)) continue;
            if (!paramTypesMatch(argTypes, ms)) continue;
            for (PastAnnotation ann : ms.getAnnotations()) {
                if (ann instanceof OverloadedMethod) return ((OverloadedMethod) ann).getAltName();
            }
        }
        return null;
    }




    private void emitDefaultConstructor(List<Field> fields, StringBuilder sb) {
        sb.append(INDENT).append("/// Creates a new instance\n");
        sb.append(INDENT).append("pub fn new(");

        // Parameters (skip fields with initialisers — they have predetermined values)
        if (emitDefaultConstructorParameters) {
            boolean first = true;
            for (Field field : fields) {
                if (field.initialiser != null) continue;
                if (!first) sb.append(", ");
                first = false;
                sb.append(sanitizeName(toSnakeCase(field.name)))
                        .append(": Option<")
                        .append(convertTypeToRust(field.type))
                        .append(">");
            }
        }
        sb.append(") -> Self {\n");

        // Struct initialization
        sb.append(INDENT).append(INDENT).append("Self {\n");
        for (Field field : fields) {
            String fieldName = sanitizeName(toSnakeCase(field.name));
            sb.append(INDENT).append(INDENT).append(INDENT)
                    .append(fieldName);
            if (field.initialiser != null) {
                sb.append(": ").append(convertWithType(field.initialiser, field.type));
            } else if (!emitDefaultConstructorParameters) {
                sb.append(": None");
            }
            sb.append(",\n");
        }
        sb.append(INDENT).append(INDENT).append("}\n");
        sb.append(INDENT).append("}\n\n");

        // Add to_json method
        sb.append(INDENT).append("/// Converts to JSON string\n");
        sb.append(INDENT).append("pub fn to_json(&self) -> Result<String, serde_json::Error> {\n");
        sb.append(INDENT).append(INDENT).append("serde_json::to_string_pretty(self)\n");
        sb.append(INDENT).append("}\n\n");

        imports.add("serde_json");
    }

    private void emitConstructor(Constructor constructor, List<Field> classFields, StringBuilder sb) {
        sb.append(INDENT).append("/// Constructor\n");
        sb.append(INDENT).append("pub fn new(");

        // Parameters — constructors take ownership, so use convertTypeToRust (owned types).
        // Exception: known-trait types use "impl Trait + 'static" so the concrete impl
        // is heap-allocated into Box<dyn Trait> when stored in the struct.
        if (constructor.parameters != null) {
            for (int i = 0; i < constructor.parameters.size(); i++) {
                if (i > 0) sb.append(", ");
                Parameter param = constructor.parameters.get(i);
                // LateEmittedClass: 'outer' is borrowed, not owned — use &'a OuterType.
                String paramType;
                if (currentClass instanceof LateEmittedClass && "outer".equals(param.name)) {
                    paramType = "&'a " + convertTypeToRust(param.type);
                } else if (isKnownTrait(param.type)) {
                    paramType = convertTypeToRustParam(param.type);   // impl Trait + 'static
                } else {
                    paramType = convertTypeToRust(param.type);        // owned (HashMap, Vec, String…)
                }
                sb.append(sanitizeName(toSnakeCase(param.name)))
                        .append(": ")
                        .append(paramType);
            }
        }
        sb.append(") -> Self {\n");

        // Collect field assignments from constructor body.
        // Each statement is expected to be an Assignment of the form: this.field = expr.
        Map<String, String> fieldValues = new LinkedHashMap<>();
        for (Statement statement : constructor.body) {
            if (statement instanceof Assignment) {
                Assignment a = (Assignment) statement;
                String fieldName = extractConstructorFieldName(a.leftHandExpression);
                if (fieldName != null) {
                    String baseRhs = convert(a.value);

                    // Trait-typed fields are stored as Box<dyn Trait>; wrap the concrete value
                    // so the constructor parameter (impl Trait) is heap-allocated on assignment.
                    Field matchingField = classFields.stream()
                            .filter(f -> sanitizeName(toSnakeCase(f.name)).equals(fieldName))
                            .findFirst().orElse(null);
                    if (matchingField != null && isKnownTrait(matchingField.type)) {
                        baseRhs = "Box::new(" + baseRhs + ")";
                    }

                    // LateEmittedClass: 'outer' is stored as a plain reference, not Option<T>.
                    boolean isOuterRefField = currentClass instanceof LateEmittedClass
                            && "outer".equals(fieldName);
                    boolean needsSomeWrap = !isOuterRefField
                            && isNonSelfFieldAccess(a.leftHandExpression)
                            && !expressionProducesOption(a.value);
                    String rhs = needsSomeWrap ? "Some(" + baseRhs + ")" : baseRhs;
                    fieldValues.put(fieldName, rhs);
                }
            } else if (statement instanceof MethodCall) {
                // SUPER_METHOD_CALL(null, args) — super constructor call.
                // Store as inner_parent initialization when the class has a concrete superclass.
                MethodCall mc = (MethodCall) statement;
                if (mc.operatorKind == MethodCall.MethodCallKind.SUPER_METHOD_CALL
                        && mc.methodName == null
                        && currentClass != null && hasConcreteSuperclass(currentClass)) {
                    // Convert the superclass type to a Rust constructor call prefix.
                    // BeanHistory<BeanLocalEnactor3> → BeanHistory::<BeanLocalEnactor3>
                    addTypeImport(currentClass.superclass);
                    String superTypeRust = convertTypeToRust(currentClass.superclass);
                    // Insert turbofish syntax: BeanHistory<T> -> BeanHistory::<T>
                    String superConstructorPrefix = superTypeRust.replace("<", "::<");
                    String superArgs = mc.arguments == null ? "" :
                            mc.arguments.stream().map(this::convert).collect(Collectors.joining(", "));
                    fieldValues.put("inner_parent", "Some(" + superConstructorPrefix + "::new(" + superArgs + "))");
                }
            }
        }

        // Emit Self { field: value, ... } — assigned fields use computed RHS,
        // unassigned fields with an initialiser use the initialiser, others default to None.
        sb.append(INDENT).append(INDENT).append("Self {\n");
        for (Field field : classFields) {
            if (field.modifiers.contains(Modifier.STATIC)) continue;
            String fieldName = sanitizeName(toSnakeCase(field.name));
            sb.append(INDENT).append(INDENT).append(INDENT).append(fieldName).append(": ");
            if (fieldValues.containsKey(fieldName)) {
                sb.append(fieldValues.get(fieldName));
            } else if (field.initialiser != null) {
                sb.append(convertWithType(field.initialiser, field.type));
            } else {
                sb.append("None");
            }
            sb.append(",\n");
        }
        // Add inner_parent field for concrete superclass composition.
        if (currentClass != null && hasConcreteSuperclass(currentClass)) {
            sb.append(INDENT).append(INDENT).append(INDENT).append("inner_parent: ");
            if (fieldValues.containsKey("inner_parent")) {
                sb.append(fieldValues.get("inner_parent"));
            } else {
                sb.append("None");
            }
            sb.append(",\n");
        }
        sb.append(INDENT).append(INDENT).append("}\n");
        sb.append(INDENT).append("}\n\n");
    }

    /**
     * Extract the Rust field name from a constructor LHS expression of the form {@code this.fieldName}.
     * Returns the sanitised snake_case field name, or {@code null} if the expression is not a direct
     * field assignment on {@code this}.
     */
    private String extractConstructorFieldName(Expression lhs) {
        if (lhs instanceof MethodCall) {
            MethodCall mc = (MethodCall) lhs;
            if (mc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                    && mc.object instanceof Variable) {
                Variable v = (Variable) mc.object;
                // 'this' in a Java constructor body (sanitizeName maps it to 'self' later)
                if ("this".equals(v.name) || "self".equals(v.name)) {
                    return sanitizeName(toSnakeCase(mc.methodName));
                }
            }
        }
        return null;
    }

    private void emitStaticFields(List<Field> fields, StringBuilder sb) {
        for (Field field : fields) {
            if (field.modifiers.contains(Modifier.STATIC)) {
                String fieldName = sanitizeName(toSnakeCase(field.name).toUpperCase());

                if (field.modifiers.contains(Modifier.FINAL) && field.initialiser != null) {
                    // Use const for compile-time constants
                    sb.append(INDENT).append("pub const ").append(fieldName)
                            .append(": ").append(convertTypeToRust(field.type))
                            .append(" = ").append(convert(field.initialiser)).append(";\n");
                } else {
                    // Use lazy_static for runtime initialization
                    sb.append(INDENT).append("// Static field: ").append(fieldName).append("\n");
                    imports.add("lazy_static::lazy_static");
                }
            }
        }
    }
    private void emitMethod(Method method, StringBuilder sb) {
        emitMethod(method, false, sb);
    }

    private void emitMethod(Method method, boolean inTrait, StringBuilder sb) {
        // Method documentation
        if (method.comments != null && !method.comments.isEmpty()) {
            for (Comment comment : method.comments) {
                List<String> lines = convert(comment);
                for (String line : lines) {
                    sb.append(INDENT).append("/// ").append(line).append("\n");
                }
            }
        }

        sb.append(INDENT);

        // Visibility
        if (!inTrait && method.modifiers.contains(Modifier.PUBLIC)) {
            sb.append("pub ");
        }

        // Function signature — use registry alt name for overloaded methods if available
        String methodAltName = findAltNameForDeclaration(method);
        String methodName = (methodAltName != null) ? methodAltName : method.name;
        String sanitizeName = sanitizeName(toSnakeCase(methodName));
        sb.append("fn ").append(sanitizeName);

        // Generic type parameters
        // MutableFirstParam methods need an explicit lifetime '<'a> that ties the first
        // parameter (&'a mut T) to the return type (&'a mut T).
        currentMethodMutableFirstParam = hasMutableFirstParam(method);
        if (currentMethodMutableFirstParam && inTrait) {
            if (method.typeVariables != null && !method.typeVariables.isEmpty()) {
                sb.append("<'a, ");
                for (int i = 0; i < method.typeVariables.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(convertTypeToRust(method.typeVariables.get(i)));
                }
                sb.append(">");
            } else {
                sb.append("<'a>");
            }
        } else if (method.typeVariables != null && !method.typeVariables.isEmpty()
                && !hasPhantomTypeVariable(method)) {
            sb.append("<");
            for (int i = 0; i < method.typeVariables.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(convertTypeToRust(method.typeVariables.get(i)));
            }
            sb.append(">");
        }

        sb.append("(");

        // Self parameter for instance methods
        currentMethodConsumesSelf = consumesSelf(method);
        currentMethodReturnType = method.returnType;
        currentMethodFirstParamName = null;
        currentMethodRefParamNames = new HashSet<>();
        if (!method.modifiers.contains(Modifier.STATIC)) {
            sb.append("&");
            currentMethodIsMutable = currentMethodConsumesSelf || modifiesSelf(method) || implementsStatefulTrait(method);
            if (currentMethodIsMutable) {
                sb.append("mut ");
            }
            sb.append("self");
            if (method.parameters != null && !method.parameters.isEmpty()) {
                sb.append(", ");
            }
        }

        // Parameters
        if (method.parameters != null) {
            for (int i = 0; i < method.parameters.size(); i++) {
                if (i > 0) sb.append(", ");
                Parameter param = method.parameters.get(i);

                // Add 'mut' prefix for trait parameters only if they might be stateful
                // This is determined by checking if the trait has stateful methods
                if (isKnownTrait(param.type) && traitNeedsStatefulAccess(param.type)) {
                    sb.append("mut ");
                }

                // If parameter type is Object, try to infer concrete type from body casts
                TypeName paramType = param.type;
                if (isObjectType(paramType) && method.body != null) {
                    TypeName inferredType = inferConcreteTypeFromCasts(param.name, method.body);
                    if (inferredType != null) {
                        paramType = inferredType;
                    }
                }

                String snakeName = sanitizeName(toSnakeCase(param.name));

                // MutableFirstParam: first param is &mut T (mutable borrow); rest are &T (read-only refs).
                // The caller retains ownership of the bean; the method writes fields into it and
                // returns bean.clone() so the API still looks like a transformer.
                // No lifetime annotation is needed: the return type is owned (not derived from &mut T).
                if (currentMethodMutableFirstParam && inTrait && i == 0) {
                    sb.append(snakeName).append(": &'a mut ").append(convertTypeToRustParam(paramType));
                    currentMethodFirstParamName = param.name; // store raw name for RETURN / call-site lookup
                } else {
                    sb.append(snakeName).append(": ")
                            .append(inTrait ? convertTypeToRustTraitParam(paramType) : convertTypeToRustParam(paramType));
                    // Track which params are &T references so .clone() can be appended when their
                    // fields are read (non-Copy field access through a shared reference requires clone).
                    if (currentMethodMutableFirstParam && inTrait && i > 0) {
                        currentMethodRefParamNames.add(param.name);
                    }
                }
            }
        }

        sb.append(")");

        // Return type
        // MutableFirstParam: return &'a mut T — the same mutable reference that was passed in,
        // so the caller can chain calls without cloning.
        if (method.returnType != null && !isVoidType(method.returnType)) {
            if (currentMethodMutableFirstParam && inTrait) {
                sb.append(" -> &'a mut ").append(convertTypeToRust(method.returnType));
            } else {
                // Inspect the last RETURN statement to detect self-Option-field getters.
                // Option<Box<dyn T>>  → return &dyn T  (Box<dyn T> is not Clone)
                // Option<Vec<…>>      → return &ReturnType  (Vec<Box<dyn Any>> is not Clone)
                // These references are borrowed from &self so no Clone is needed.
                boolean returnsTraitFieldRef = false;
                boolean returnsOptionFieldRef = false;
                if (!currentMethodConsumesSelf && method.body != null && !method.body.isEmpty()) {
                    Statement lastStmt = method.body.get(method.body.size() - 1);
                    if (lastStmt instanceof Return) {
                        Expression retExpr = ((Return) lastStmt).expression;
                        if (isSelfOptionFieldAccess(retExpr)) {
                            MethodCall mc = (MethodCall) retExpr;
                            TypeName fieldType = getFieldType(mc.methodName);
                            if (fieldType != null && isKnownTrait(fieldType)) {
                                returnsTraitFieldRef = true;
                            } else {
                                returnsOptionFieldRef = true;
                            }
                        } else if (retExpr instanceof MethodCall) {
                            // Delegation pattern: this.optionField.method() — the called method
                            // returns a reference into the inner struct; use &ReturnType.
                            MethodCall retMc = (MethodCall) retExpr;
                            if (retMc.operatorKind == MethodCall.MethodCallKind.OBJECT_METHOD_CALL
                                    && isSelfOptionFieldAccess(retMc.object)
                                    && !currentMethodIsMutable) {
                                returnsOptionFieldRef = true;
                            }
                            // Super-delegation chain (inner_parent.get_delegator().getXxx()) that returns
                            // a non-cloneable collection (e.g. HashMap<String, AtomicI32>).
                            // Emit &ReturnType so we return a reference instead of trying to clone.
                            else if (containsSuperMethodCall(retExpr)
                                    && method.returnType != null
                                    && (isMap(method.returnType) || isList(method.returnType))
                                    && containsNonCloneableElement(method.returnType)) {
                                returnsOptionFieldRef = true;
                            }
                        }
                    }
                }
                if (returnsTraitFieldRef) {
                    sb.append(" -> &dyn ").append(convertTypeToRust(method.returnType));
                } else if (returnsOptionFieldRef) {
                    sb.append(" -> &").append(convertTypeToRust(method.returnType));
                } else if (hasPhantomTypeVariable(method)) {
                    // Phantom T (e.g. from Class<T> → &'static str): erase T and use Box<dyn Any>
                    // so the impl matches the dyn-compatible trait declaration.
                    sb.append(" -> Box<dyn std::any::Any>");
                } else {
                    sb.append(" -> ").append(convertTypeToRust(method.returnType));
                }
            }
        }

        sb.append(" {\n");

        if (method.modifiers.contains(Modifier.ABSTRACT)) {
            //        panic!("new_identifier is unimplemented (field={field}, counter={counter})")
            sb.append(INDENT + INDENT).append("panic! (\"method ").append(sanitizeName).append(" (").append(methodName).append(") is declared abstract in PAST and unimplemented\")\n");
        } else {
            // Method body

            if (method.body != null && !method.body.isEmpty()) {
                for (int i = 0; i < method.body.size(); i++) {
                    Statement statement = method.body.get(i);
                    boolean isLastStatement = (i == method.body.size() - 1);
                    emitStatement(statement, INDENT + INDENT, isLastStatement, sb);
                }
            }
        }

        sb.append(INDENT).append("}\n\n");
    }

    /**
     * Check if a trait needs stateful access (requires mut parameter)
     */
    private boolean traitNeedsStatefulAccess(TypeName tn) {
        if (tn instanceof ClassName) {
            ClassName cn = (ClassName) tn;
            String name = toPascalCase(cn.simpleName);
            return statefulTraits.contains(name);
        } else if (tn instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) tn;
            return traitNeedsStatefulAccess(pt.rawType);
        }
        return false;
    }

    // ---- OverloadedMethod alt-name resolution (mirrors Python.java) -------------------------

    /** Look up the alt name for a method declaration from the TypeRegistry. */
    private String findAltNameForDeclaration(Method method) {
        if (currentClassSignature == null || typeRegistry == null) return null;
        int paramCount = method.parameters == null ? 0 : method.parameters.size();
        for (MethodSignature ms : currentClassSignature.methods) {
            if (!ms.name.equals(method.name)) continue;
            if (ms.parameterTypes.size() != paramCount) continue;
            for (PastAnnotation ann : ms.getAnnotations()) {
                if (ann instanceof OverloadedMethod) {
                    if (paramCount == 0 || paramTypesMatch(method, ms)) {
                        return ((OverloadedMethod) ann).getAltName();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Look up the alt name for a self-method call site from the TypeRegistry.
     * Returns non-null only when there is exactly one overloaded candidate with the given arg count.
     */
    private String findAltNameForCall(String methodName, int argCount) {
        if (currentClassSignature == null || typeRegistry == null) return null;
        List<MethodSignature> candidates = new ArrayList<>();
        for (MethodSignature ms : currentClassSignature.methods) {
            if (!ms.name.equals(methodName)) continue;
            if (ms.parameterTypes.size() != argCount) continue;
            for (PastAnnotation ann : ms.getAnnotations()) {
                if (ann instanceof OverloadedMethod) {
                    candidates.add(ms);
                    break;
                }
            }
        }
        if (candidates.size() == 1) {
            for (PastAnnotation ann : candidates.get(0).getAnnotations()) {
                if (ann instanceof OverloadedMethod) return ((OverloadedMethod) ann).getAltName();
            }
        }
        return null;
    }

    private boolean paramTypesMatch(Method method, MethodSignature ms) {
        if (method.parameters.size() != ms.parameterTypes.size()) return false;
        List<TypeName> argTypes = new ArrayList<>();
        for (Parameter p : method.parameters) argTypes.add(p.type);
        return paramTypesMatch(argTypes, ms);
    }

    private boolean paramTypesMatch(List<TypeName> argTypes, MethodSignature ms) {
        if (argTypes.size() != ms.parameterTypes.size()) return false;
        for (int i = 0; i < argTypes.size(); i++) {
            String argTypeName = altNameTypeSimpleName(argTypes.get(i)).toLowerCase();
            String regTypeName = altNameTypeSimpleName(ms.parameterTypes.get(i)).toLowerCase();
            if (!argTypeName.equals(regTypeName)) return false;
        }
        return true;
    }

    private static String altNameTypeSimpleName(TypeName type) {
        if (type instanceof ClassName) return ((ClassName) type).simpleName;
        if (type instanceof ParameterizedType) return ((ParameterizedType) type).getRawType().simpleName;
        if (type instanceof ArrayType) return altNameTypeSimpleName(((ArrayType) type).elementType) + "Array";
        if (type instanceof TypeVariable) return ((TypeVariable) type).name;
        return type.toString();
    }

    // ---- end OverloadedMethod helpers -------------------------------------------------------

    private boolean modifiesSelf(Method method) {
        // Check for StatefulProcessor annotation
        // If present, method needs &mut self
        for (PastAnnotation annot : method.annotation) {
            if (annot instanceof RustAnnotation) {
                if (annot.getName().equals(StatefulProcessor.NAME)) {
                    return true;
                }
            }
        }
        // Check if the method body contains calls that mutate fields (e.g., forEach on a field, add to a field)
        if (method.body != null) {
            for (Statement stmt : method.body) {
                if (bodyContainsMutatingCall(stmt)) {
                    return true;
                }
            }
        }
        return false; // Default: stateless processors (&self)
    }

    /**
     * Returns true if the method should use a consuming (mut self) receiver.
     * Triggered by the MutableReceiver annotation.
     */
    private boolean consumesSelf(Method method) {
        for (PastAnnotation annot : method.annotation) {
            if (annot instanceof RustAnnotation) {
                if (annot.getName().equals(MutableReceiver.NAME)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true when the return expression is the first (bean) parameter of a
     * MutableFirstParam method.  Because that parameter is {@code &mut T}, returning
     * the variable as {@code T} requires appending {@code .clone()}.
     */
    private boolean isMutableFirstParamReturn(Expression expr) {
        if (currentMethodFirstParamName == null) return false;
        if (!(expr instanceof Variable)) return false;
        return currentMethodFirstParamName.equals(((Variable) expr).name);
    }

    /**
     * Returns true when a method-call on {@code this}/{@code self} resolves to a
     * MutableFirstParam method in the current class.  Used by the OPERATOR_VARIABLE
     * emitter to add {@code &mut} to argument 0 and {@code &} to arguments 1+.
     */
    private boolean calledMethodHasMutableFirstParam(MethodCall mc) {
        if (currentClass == null) return false;
        if (!(mc.object instanceof Variable)) return false;
        if (!"this".equals(((Variable) mc.object).name)) return false;
        int argCount = mc.arguments == null ? 0 : mc.arguments.size();
        for (Method m : currentClass.methods) {
            if (m.name.equals(mc.methodName)
                    && m.parameters != null && m.parameters.size() == argCount
                    && hasMutableFirstParam(m)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true when {@code method} is an {@code @Override} implementation of a trait that is
     * in {@code statefulTraits} (i.e. the trait declares {@code &mut self} methods).  Used to
     * force {@code &mut self} on implementing methods even when the body does not auto-detect
     * mutation — required when the trait has been marked stateful via {@code MutableReceiver}.
     */
    private boolean implementsStatefulTrait(Method method) {
        if (!isInterfaceImplementation(method)) return false;
        if (currentClass == null) return false;
        for (TypeName intf : currentClass.interfaces) {
            if (intf instanceof ClassName) {
                String traitName = toPascalCase(((ClassName) intf).simpleName);
                if (statefulTraits.contains(traitName)) return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the method's first non-self parameter should be taken by owned
     * mutable value ({@code &mut T}) rather than shared reference ({@code &T}).
     * Triggered by the MutableFirstParam annotation.
     * <p>
     * This is used for the BeanMerger pattern: the method receives a bean, writes
     * fields from an input/output into it, and returns the updated owned bean.
     * No lifetime annotation is needed because both the parameter and return type
     * are owned values.
     */
    private boolean hasMutableFirstParam(Method method) {
        for (PastAnnotation annot : method.annotation) {
            if (annot instanceof RustAnnotation) {
                if (annot.getName().equals(MutableFirstParam.NAME)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean bodyContainsMutatingCall(Statement stmt) {
        // Expression statements are Expression objects with statementKind == EXPRESSION_STATEMENT
        if (stmt.statementKind == Statement.StatementKind.EXPRESSION_STATEMENT && stmt instanceof Expression) {
            return expressionContainsMutatingCall((Expression) stmt);
        }
        // Recurse into IF bodies: add() calls on self-fields are typically inside
        // if (this.inputs != null) { ... } guards, not at the top level of the method.
        if (stmt instanceof IfStatement) {
            IfStatement ifs = (IfStatement) stmt;
            for (Statement s : ifs.thenBlock) {
                if (bodyContainsMutatingCall(s)) return true;
            }
            for (Statement s : ifs.elseBlock) {
                if (bodyContainsMutatingCall(s)) return true;
            }
        }
        return false;
    }

    private boolean expressionContainsMutatingCall(Expression expr) {
        if (expr instanceof MethodCall) {
            MethodCall mc = (MethodCall) expr;
            // OPERATOR_VARIABLE: mutating call directly on a field variable
            if (mc.operatorKind == MethodCall.MethodCallKind.OPERATOR_VARIABLE && mc.object instanceof Variable) {
                Variable v = (Variable) mc.object;
                if (v.field == Variable.VariableKind.FIELD_VARIABLE) {
                    RustMethodSpec spec = METHOD_REGISTRY.resolveMethodGlobal(mc.methodName);
                    if (spec != null && spec.mutatesReceiver) {
                        return true;
                    }
                }
            }
            // OBJECT_METHOD_CALL: this.<field>.mutatingMethod(...) — the object is an OBJECT_ACCESSOR
            // on this/self. Workflow generators use this form for e.g. this.inputs.add(…).
            if (mc.operatorKind == MethodCall.MethodCallKind.OBJECT_METHOD_CALL
                    && mc.object instanceof MethodCall) {
                RustMethodSpec spec = METHOD_REGISTRY.resolveMethodGlobal(mc.methodName);
                if (spec != null && spec.mutatesReceiver) {
                    MethodCall accessor = (MethodCall) mc.object;
                    if (accessor.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                            && accessor.object instanceof Variable) {
                        Variable v = (Variable) accessor.object;
                        if ("this".equals(v.name) || "self".equals(v.name)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isVoidType(TypeName returnType) {
        if (returnType instanceof ClassName) {
            ClassName cn = (ClassName) returnType;
            return cn.packge != null && cn.packge.equals("past.lang") && cn.simpleName.equals("Void");
        }
        return false;
    }

    private void emitStatement(Statement statement, String indent, StringBuilder sb) {
        emitStatement(statement, indent, false, sb);
    }

    private void emitStatement(Statement statement, String indent, boolean isLastStatement, StringBuilder sb) {
        switch (statement.statementKind) {
            case ASSIGNMENT -> {
                Assignment assignment = (Assignment) statement;

                if (assignment.leftHandExpression instanceof Variable
                        && ((Variable) assignment.leftHandExpression).name.equals("self")) {
                    return;
                }

                sb.append(indent);

                // Check if LHS is a field access on a non-self variable (e.g., b.field in a forEach closure).
                // Such fields are Option<T> in Rust, so the RHS needs to be wrapped in Some(...)
                // Exception: if the RHS already produces Option<T> (e.g. inner HashMap get + .copied()),
                // wrapping in Some() would produce Option<Option<T>> — suppress in that case.
                boolean needsSomeWrap = isNonSelfFieldAccess(assignment.leftHandExpression)
                        && !expressionProducesOption(assignment.value);
                String rhs = convert(assignment.value);
                if (needsSomeWrap) {
                    // If the RHS has Java type String, its Rust representation is &str (for variables /
                    // field accesses / string literals).  Wrapping &str in Some() would yield Option<&str>
                    // which mismatches the Option<String> field.  Append .to_string() to produce an owned
                    // String before wrapping.  Method calls that return String already yield an owned
                    // String and do not need .to_string().
                    boolean rhsIsMethodCallResult = (assignment.value instanceof MethodCall)
                            && (((MethodCall) assignment.value).operatorKind == MethodCall.MethodCallKind.OBJECT_METHOD_CALL
                                    || ((MethodCall) assignment.value).operatorKind == MethodCall.MethodCallKind.STATIC_METHOD_CALL
                                    || ((MethodCall) assignment.value).operatorKind == MethodCall.MethodCallKind.CONSTRUCTOR_CALL);
                    // BinaryOp + on strings is emitted as format!() which already returns an
                    // owned String — no .to_string() needed (and adding it would be redundant).
                    boolean rhsIsStringConcat = (assignment.value instanceof BinaryOp)
                            && "+".equals(((BinaryOp) assignment.value).op);
                    if (!rhsIsMethodCallResult && !rhsIsStringConcat) {
                        if (assignment.value.inferredType != null
                                && isStringType(assignment.value.inferredType)) {
                            // Known String type — produce an owned String via .to_string().
                            rhs = rhs + ".to_string()";
                        } else if (assignment.value.inferredType == null && isSelfFieldRef(assignment.value)) {
                            // Self-field with no inferred type (e.g. this.agent1Time via &self).
                            // .clone() avoids moving a non-Copy field out of &self, and is safe
                            // for all field types (String, struct, etc.).
                            rhs = rhs + ".clone()";
                        }
                    }
                    rhs = "Some(" + rhs + ")";
                }

                sb.append(sanitizeName(toSnakeCase(convertLH(assignment.leftHandExpression))))
                        .append(" = ")
                        .append(rhs)
                        .append(";\n");

                if (postDecrement != null) {
                    sb.append(indent)
                            .append(postDecrement)
                            .append(" -= 1;\n");
                    postDecrement = null;
                }
            }

            case DEFINITION -> {
                Definition definition = (Definition) statement;

                if (definition.leftHandExpression instanceof Variable
                        && ((Variable) definition.leftHandExpression).name.equals("self")) {
                    return;
                }

                // Evaluate value first so side effects (like postDecrement) are visible.
                // Use convertWithType so string literals get .to_string() when the variable is typed String.
                String valueStr = convertWithType(definition.value, definition.type);

                sb.append(indent);

                // Add let for new variables
                if (definition.leftHandExpression instanceof Variable) {
                    // In Java, final arrays/collections can still have their contents mutated.
                    // In Rust, we need `let mut` if the variable will be mutated (e.g. via index assignment).
                    // Detect this by checking if postDecrement was set during value conversion, or if the
                    // type is an array/collection that may be mutated.
                    boolean needsMut = !definition.modifiers.contains(Modifier.FINAL)
                            || postDecrement != null
                            || isArrayType(definition.type);
                    if (needsMut) {
                        sb.append("let mut ");
                    } else {
                        sb.append("let ");
                    }
                }

                sb.append(sanitizeName(toSnakeCase(convertLH(definition.leftHandExpression))))
                        .append(" = ")
                        .append(valueStr)
                        .append(";\n");

                if (postDecrement != null) {
                    sb.append(indent)
                            .append(postDecrement)
                            .append(" -= 1;\n");
                    postDecrement = null;
                }
            }

            case RETURN -> {
                Return ret = (Return) statement;
                // Use convertWithType so that a string constant returned from a String method
                // gets .to_string() appended (avoids E0308 &str vs String).
                String retExpr = convertWithType(ret.expression, currentMethodReturnType);
                // When the return expression goes through a SUPER_METHOD_CALL delegation chain
                // (e.g. self.inner_parent.as_ref().unwrap().get_delegator().get_counter_map()),
                // the chain passes through as_ref() and the final call returns &Map/&List.
                // We need to .clone() to produce an owned value — unless the map contains a
                // non-Clone element type like AtomicI32, in which case we return &Map directly
                // (the method signature has been adjusted to &HashMap<...> in emitMethod).
                if (currentMethodReturnType != null
                        && (isMap(currentMethodReturnType) || isList(currentMethodReturnType))
                        && containsSuperMethodCall(ret.expression)
                        && !containsNonCloneableElement(currentMethodReturnType)) {
                    retExpr += ".clone()";
                }
                if (currentMethodConsumesSelf) {
                    // Returning self in a &mut self method: self is &mut T, return type is T → clone.
                    if (isSelfVariableReturn(ret.expression)) {
                        retExpr += ".clone()";
                    }
                    // Returning self.optionField in a &mut self method: Option<T>, return type is T → clone + unwrap.
                    else if (isSelfOptionFieldAccess(ret.expression)) {
                        retExpr += ".clone().unwrap()";
                    }
                } else if (isSelfOptionFieldAccess(ret.expression)) {
                    // Non-MutableReceiver getter returning a self Option field — borrow instead of clone.
                    // Option<Box<dyn T>>  → .as_ref().unwrap().as_ref()  gives &dyn T
                    // Option<Vec<T>>      → .as_ref().unwrap()            gives &Vec<T>
                    MethodCall mc = (MethodCall) ret.expression;
                    TypeName fieldType = getFieldType(mc.methodName);
                    if (fieldType != null && isKnownTrait(fieldType)) {
                        retExpr += ".as_ref().unwrap().as_ref()";
                    } else {
                        retExpr += ".as_ref().unwrap()";
                    }
                }
                // MutableFirstParam: return &'a mut T directly — no clone needed.
                // (The returned reference carries lifetime 'a, same as the first parameter.)
                if (isLastStatement) {
                    // Rust implicit return - no semicolon
                    sb.append(indent)
                            .append(retExpr)
                            .append("\n");
                } else {
                    sb.append(indent)
                            .append("return ")
                            .append(retExpr)
                            .append(";\n");
                }
            }

            case COMMENT -> {
                Comment cs = (Comment) statement;
                List<String> lines = convert(cs);
                for (String line : lines) {
                    sb.append(indent)
                            .append("// ")
                            .append(line)
                            .append("\n");
                }
            }

            case EXPRESSION_STATEMENT -> {
                Expression es = (Expression) statement;
                sb.append(indent)
                        .append(convert(es))
                        .append(";\n");
            }

            case IF_STATEMENT -> {
                IfStatement ifs = (IfStatement) statement;
                sb.append(indent)
                        .append("if ")
                        .append(convert(ifs.condition))
                        .append(" {\n");
                if (ifs.thenBlock.isEmpty()) {
                    sb.append(indent).append(INDENT).append("// empty\n");
                } else {
                    for (Statement thenStmt : ifs.thenBlock) {
                        emitStatement(thenStmt, indent + INDENT, sb);
                    }
                }
                if (!ifs.elseBlock.isEmpty()) {
                    sb.append(indent)
                            .append("} else {\n");
                    for (Statement elseStmt : ifs.elseBlock) {
                        emitStatement(elseStmt, indent + INDENT, sb);
                    }
                }
                sb.append(indent).append("}\n");
            }

            case FOR_LOOP -> {
                ForLoop forLoop = (ForLoop) statement;
                Statement initialization = forLoop.initialization;
                Expression condition = forLoop.condition;
                Statement update = forLoop.update;

                // Rust doesn't have C-style for loops, convert to while
                emitStatement(initialization, indent, sb);
                sb.append(indent)
                        .append("while ")
                        .append(convert(condition))
                        .append(" {\n");
                for (Statement bodyStmt : forLoop.body) {
                    emitStatement(bodyStmt, indent + INDENT, sb);
                }
                emitStatement(update, indent + INDENT, sb);
                sb.append(indent).append("}\n");
            }

            case ITERATOR -> {
                Iterator iterator = (Iterator) statement;
                // When iterating over a field of a borrowed local struct (e.g. bean.elements where
                // bean is &T), iterating by value would move the Vec out of the borrow.
                // Use .iter().cloned() to produce owned items without moving: all generated
                // structs derive Clone, so this is safe and preserves by-value method signatures.
                String collectionExpr = convert(iterator.collection);
                if (isLocalFieldAccess(iterator.collection)) {
                    collectionExpr = collectionExpr + ".iter().cloned()";
                }
                sb.append(indent)
                        .append("for ")
                        .append(sanitizeName(toSnakeCase(iterator.parameter.name)))
                        .append(" in ")
                        .append(collectionExpr)
                        .append(" {\n");
                for (Statement bodyStmt : iterator.body) {
                    emitStatement(bodyStmt, indent + INDENT, sb);
                }
                sb.append(indent).append("}\n");
            }

            case DO_LOOP -> {
                DoLoop doLoop = (DoLoop) statement;
                sb.append(indent)
                        .append("loop {\n");
                for (Statement bodyStmt : doLoop.body) {
                    emitStatement(bodyStmt, indent + INDENT, sb);
                }
                sb.append(indent+INDENT).append("if !").append(convert(doLoop.condition)).append(" {\n");
                sb.append(indent+INDENT+INDENT).append("break;\n");
                sb.append(indent+INDENT).append("}\n");
                sb.append(indent).append("}\n");
            }

            case THROW -> {
                ThrowStatement throwStmt = (ThrowStatement) statement;
                sb.append(indent)
                        .append("panic! (\"")
                        .append(convert(throwStmt.expression).replace("\"", "\\\""))
                        .append("\");\n");

                ClassName cn=(ClassName) throwStmt.expression.inferredType;
                String modulePath = buildModulePath(cn.packge, cn.simpleName);
                imports.remove(modulePath); //
            }

            default -> {
                throw new IllegalArgumentException("Unsupported statement type " + statement);
            }
        }
    }

    private String convertStatementForClosure(Statement statement) {
        StringBuilder result = new StringBuilder();
        switch (statement.statementKind) {
            case RETURN -> {
                Return ret = (Return) statement;
                result.append(convert(ret.expression));
            }
            case EXPRESSION_STATEMENT -> {
                Expression es = (Expression) statement;
                result.append(convert(es));
            }
            default -> {
                throw new IllegalArgumentException("Unsupported statement in closure: " + statement);
            }
        }
        return result.toString();
    }

    /**
     * Convert an argument expression, handling Option<T> fields for nullable semantics.
     * - Option<String> fields use .as_deref().unwrap_or_default() to yield &str
     * - Other Option<T> fields (i32, etc.) are passed by value (Copy types)
     * - Non-optional String fields (with initialiser) use & to borrow as &str
     */
    private String convertArgument(Expression expression) {
        if (expression instanceof Variable) {
            Variable ve = (Variable) expression;
            if (ve.field == Variable.VariableKind.FIELD_VARIABLE) {
                Field field = getField(ve.name);
                if (field != null) {
                    TypeName fieldType = field.type;
                    if (field.initialiser == null) {
                        // This is an Option<T> field
                        if (isStringType(fieldType)) {
                            // Option<String> → &str: processor trait methods take &str (not Option<&str>)
                            return "self." + sanitizeName(toSnakeCase(ve.name)) + ".as_deref().unwrap_or_default()";
                        } else {
                            // Option<i32> etc. — pass by value (Copy)
                            return "self." + sanitizeName(toSnakeCase(ve.name));
                        }
                    } else {
                        // Non-optional field (has initialiser)
                        if (isStringType(fieldType)) {
                            return "&self." + sanitizeName(toSnakeCase(ve.name));
                        } else if (isList(fieldType) || isMap(fieldType)) {
                            // Vec/HashMap — pass by reference since they don't implement Copy
                            return "&self." + sanitizeName(toSnakeCase(ve.name));
                        }
                    }
                }
            }
        }
        // For other expressions (including Constant.NULL → "None"), use normal conversion
        return convert(expression);
    }

    /**
     * Returns true when a push/add call targets a self-owned list field
     * (i.e. the PAST receiver is an OBJECT_ACCESSOR on this/self for a field whose
     * Java type is a List).  Such fields are emitted as Option&lt;Vec&lt;Box&lt;dyn Any&gt;&gt;&gt;;
     * the push must therefore go through .as_mut().unwrap() and the argument must
     * be wrapped in Box::new(…).
     */
    private boolean isSelfOptionListField(MethodCall mc) {
        if (mc.object instanceof MethodCall) {
            MethodCall accessor = (MethodCall) mc.object;
            if (accessor.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                    && accessor.object instanceof Variable) {
                Variable v = (Variable) accessor.object;
                if ("this".equals(v.name) || "self".equals(v.name)) {
                    TypeName fieldType = getFieldType(accessor.methodName);
                    return fieldType != null && isList(fieldType);
                }
            }
        }
        return false;
    }

    /**
     * Get the Field object by name from the current class
     */
    private Field getField(String fieldName) {
        if (currentClass == null) return null;
        for (Field field : currentClass.fields) {
            if (field.name.equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Get the type of a field by name from the current class
     */
    private TypeName getFieldType(String fieldName) {
        Field field = getField(fieldName);
        return field != null ? field.type : null;
    }

    /**
     * Look up the parameter list of a method named {@code methodName} in the current class and
     * its superclass chain (via {@link #classRegistry}).  Used by the OPERATOR_VARIABLE handler
     * to determine whether struct-type arguments to self method calls need {@code &} borrowing.
     *
     * @param methodName the PAST-level method name (e.g. "process")
     * @param argCount   the number of arguments in the call (used to disambiguate overloads)
     * @return the parameter list of the first matching method, or {@code null} if not found
     */
    private List<Parameter> findSelfMethodParameters(String methodName, int argCount) {
        Class clazz = currentClass;
        while (clazz != null) {
            for (Method m : clazz.methods) {
                if (methodName.equals(m.name)) {
                    int paramCount = (m.parameters != null) ? m.parameters.size() : 0;
                    if (paramCount == argCount) return m.parameters;
                }
            }
            // Climb the superclass chain
            if (clazz.superclass instanceof ClassName) {
                String superName = ((ClassName) clazz.superclass).simpleName;
                clazz = classRegistry.get(toPascalCase(superName));
            } else {
                break;
            }
        }
        return null;
    }

    /**
     * Returns true if the collection type contains a non-Clone element type (e.g. AtomicInteger).
     * Used to decide whether to return {@code &Map<K,V>} (reference) or clone an owned map.
     */
    private boolean containsNonCloneableElement(TypeName tn) {
        if (tn instanceof ParameterizedType) {
            for (TypeName arg : ((ParameterizedType) tn).typeArguments) {
                if (arg instanceof ClassName) {
                    String name = ((ClassName) arg).simpleName;
                    if ("AtomicInteger".equals(name) || "AtomicI32".equals(name)) return true;
                }
                if (containsNonCloneableElement(arg)) return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the expression tree contains a SUPER_METHOD_CALL node.
     * Used to detect delegation chains that pass through get_delegator() and therefore
     * produce reference values (&T) instead of owned values (T).
     */
    private boolean containsSuperMethodCall(Expression expr) {
        if (expr instanceof MethodCall) {
            MethodCall mc = (MethodCall) expr;
            if (mc.operatorKind == MethodCall.MethodCallKind.SUPER_METHOD_CALL) return true;
            if (mc.object != null && containsSuperMethodCall(mc.object)) return true;
        }
        return false;
    }

    /**
     * Look up the PAST parameter list for a method on the class identified by {@code receiverType}.
     * Used by the FUNCTIONAL_INTERFACE_CALL handler to resolve which null arguments should become
     * {@code ""} (for {@code &str} params) vs {@code None} (for {@code Option<T>} params).
     *
     * @param receiverType the inferred type of the call receiver (may be {@code ParameterizedType},
     *                     {@code ClassName}, or {@code TypeVariable})
     * @param methodName   the Java-style method name (e.g. "process")
     * @return the parameter list of the first matching method, or {@code null} if not found
     */
    private List<Parameter> findMethodParameters(TypeName receiverType, String methodName) {
        if (receiverType == null) return null;
        String className = null;
        if (receiverType instanceof ClassName) {
            className = ((ClassName) receiverType).simpleName;
        } else if (receiverType instanceof ParameterizedType) {
            TypeName rawType = ((ParameterizedType) receiverType).getRawType();
            if (rawType instanceof ClassName) {
                className = ((ClassName) rawType).simpleName;
            }
        } else if (receiverType instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) receiverType;
            if (!tv.bounds.isEmpty() && tv.bounds.get(0) instanceof ClassName) {
                className = ((ClassName) tv.bounds.get(0)).simpleName;
            }
        }
        if (className == null) return null;
        Class clazz = classRegistry.get(toPascalCase(className));
        if (clazz == null) return null;
        for (Method m : clazz.methods) {
            if (methodName.equals(m.name)) {
                return m.parameters;
            }
        }
        return null;
    }

    /**
     * Returns true if the named field of the current class is emitted as Option<T>
     * (i.e. it has no initialiser in the PAST).
     */
    private boolean isOptionField(String fieldName) {
        Field field = getField(fieldName);
        return field != null && field.initialiser == null;
    }

    /**
     * Returns true if the expression is a bare self/this variable (i.e. the method returns self).
     * Used to detect RETURN(VARIABLE("this")) in a MutableReceiver method.
     */
    private boolean isSelfVariableReturn(Expression expr) {
        if (!(expr instanceof Variable)) return false;
        Variable v = (Variable) expr;
        return "this".equals(v.name) || "self".equals(v.name);
    }

    /**
     * Returns true if the expression is a direct OBJECT_ACCESSOR of an Option field on self/this.
     * Used to decide whether to append .clone().unwrap() on a return statement in a MutableReceiver method.
     */
    private boolean isSelfOptionFieldAccess(Expression expr) {
        if (!(expr instanceof MethodCall)) return false;
        MethodCall mc = (MethodCall) expr;
        if (mc.operatorKind != MethodCall.MethodCallKind.OBJECT_ACCESSOR) return false;
        if (!(mc.object instanceof Variable)) return false;
        Variable v = (Variable) mc.object;
        return ("this".equals(v.name) || "self".equals(v.name)) && isOptionField(mc.methodName);
    }

    /**
     * Returns true when {@code fieldName} is an instance field declared on the current
     * (abstract) class.  Used to translate direct field access to getter calls inside
     * trait default method bodies, where Rust does not allow field access.
     */
    private boolean isFieldOfCurrentAbstractClass(String fieldName) {
        if (currentClass == null) return false;
        String snakeName = sanitizeName(toSnakeCase(fieldName));
        return currentClass.fields.stream()
                .filter(f -> !f.modifiers.contains(Modifier.STATIC))
                .anyMatch(f -> sanitizeName(toSnakeCase(f.name)).equals(snakeName));
    }

    /**
     * True when {@code mc} is a Vec index access disguised as {@code get}: i.e.
     * {@code someExpr.__elements.get(i)}.  These are converted to {@code [i as usize].clone()}
     * which returns {@code T} directly — no further {@code .unwrap()} is needed.
     */
    private boolean isVecIndexAccessGet(MethodCall mc) {
        return "get".equals(mc.methodName)
                && mc.arguments != null && mc.arguments.size() == 1
                && mc.object instanceof MethodCall
                && ((MethodCall) mc.object).operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                && "__elements".equals(((MethodCall) mc.object).methodName);
    }

    private String convert(Expression expression) {
        switch (expression.expressionKind) {
            case VARIABLE: {
                Variable ve = (Variable) expression;
                return switch (ve.field) {
                    case STATIC_FIELD_VARIABLE -> currentClass.name + "::" + sanitizeName(toSnakeCase(ve.name).toUpperCase());
                    case FIELD_VARIABLE -> "self." + sanitizeName(toSnakeCase(ve.name));
                    case LOCAL_VARIABLE -> sanitizeName(toSnakeCase(ve.name));
                };
            }

            case CONSTANT: {
                Constant constant = (Constant) expression;
                return convertConstant(constant);
            }

            case METHOD_CALL: {
                MethodCall mc = (MethodCall) expression;
                return convertMethodCall(mc);
            }

            case ARRAY_INITIALISER: {
                ArrayInitialiser ai = (ArrayInitialiser) expression;

                // Check for HeterogeneousArray annotation
                boolean isHeterogeneous = ai.annotation.stream()
                        .anyMatch(annot -> annot instanceof HeterogeneousArray);

                if (isHeterogeneous) {
                    // Generate Vec<Value> with wrapped elements
                    needsValueEnum = true;  // Flag to generate Value enum
                    return "vec![" + ai.values.stream()
                            .map(this::wrapInValueEnum)
                            .collect(Collectors.joining(", ")) + "]";
                } else {
                    // Normal homogeneous array
                    return "vec![" + ai.values.stream()
                            .map(this::convert)
                            .collect(Collectors.joining(", ")) + "]";
                }
            }

            case CAST: {
                CastExpression c = (CastExpression) expression;
                // In Rust, 'as' only works for primitive type casts
                // For reference/struct types, skip the cast (use proper parameter typing instead)
                if (isPrimitiveType(c.targetType)) {
                    return convert(c.expression) + " as " + convertTypeToRust(c.targetType);
                }
                // Non-primitive cast: just return the expression (the parameter type should be correct)
                return convert(c.expression);
            }

            case LAMBDA_EXPRESSION: {
                LambdaExpression le = (LambdaExpression) expression;

                // Simple closure
                if (le.body.size() == 1 && isSimpleClosureBody(le.body.get(0))) {
                    StringBuilder result = new StringBuilder();
                    result.append("|");
                    if (!le.parameters.isEmpty()) {
                        result.append(le.parameters.stream()
                                .map(p -> sanitizeName(toSnakeCase(p.name)))
                                .collect(Collectors.joining(", ")));
                    }
                    result.append("| ");
                    result.append(convertStatementForClosure(le.body.get(0)));
                    return result.toString();
                }

                // Complex closure - extract to method
                String fnName = "closure_" + (closureCount++);
                Method method = new Method(fnName);
                method.parameters.addAll(le.parameters);
                method.body.addAll(le.body);
                lateEmitMethod(method);

                return "Self::" + sanitizeName(toSnakeCase(fnName));
            }

            case POST_INCREMENT: {
                PostIncrement pi = (PostIncrement) expression;
                Expression expr = pi.expression;
                String exprStr = convert(expr);
                if (pi.increment < 0) {
                    postDecrement = exprStr;
                    return exprStr;
                } else {
                    // Rust doesn't have post-increment, use statement form
                    return exprStr + " + 1";
                }
            }

            case ARRAY_ACCESSOR: {
                ArrayAccessor aa = (ArrayAccessor) expression;

                // Check for HeterogeneousArray annotation with expected type
                String expectedType = getExpectedTypeFromAnnotation(aa.annotation);

                if (expectedType != null) {
                    // Generate accessor with unwrapping method call
                    needsValueEnum = true;  // Ensure Value enum is generated
                    String helperMethod = "as_" + expectedType.toLowerCase();
                    return convert(aa.arrayExpression) + "[" + convert(aa.indexExpression) + "]." + helperMethod + "()";
                } else {
                    // Normal accessor (returns Value or normal element)
                    return convert(aa.arrayExpression) + "[" + convert(aa.indexExpression) + "]";
                }
            }

            case BINARY_OP: {
                BinaryOp bo = (BinaryOp) expression;
                if (bo.op.equals(INSTANCEOF)) {
                    // Rust uses 'is_instance_of' from a trait or type checking
                    // For now, generate a type check pattern
                    return "/* instanceof check */ true";
                }
                // null/None comparisons: use inferredType on the right operand to confirm it
                // is a null constant, then emit idiomatic Option methods instead of != None /
                // == None.  Option<T> does not implement PartialEq when T doesn't (e.g.
                // Option<Vec<Box<dyn Any>>>), so != None won't compile.
                if (bo.right instanceof Constant
                        && ((Constant) bo.right).constantType == Constant.ConstantType.NULL) {
                    if ("!=".equals(bo.op)) return convert(bo.left) + ".is_some()";
                    if ("==".equals(bo.op)) return convert(bo.left) + ".is_none()";
                }
                // String concatenation via +: &str + &str / &str + String are not valid Rust.
                // Use the inferredType on the left operand (populated by PAST TypeInferrer) to
                // detect string concatenation, then emit a format!() macro instead.
                if ("+".equals(bo.op)) {
                    boolean leftIsString = bo.left.inferredType != null && isStringType(bo.left.inferredType);
                    boolean rightIsStringLiteral = bo.right instanceof Constant
                            && ((Constant) bo.right).constantType == Constant.ConstantType.STRING;
                    if (leftIsString || rightIsStringLiteral) {
                        if (rightIsStringLiteral) {
                            // Embed literal text directly in the format string, escaping { and }
                            String literal = ((Constant) bo.right).value.toString()
                                    .replace("{", "{{").replace("}", "}}");
                            // "format!(\"{}" starts the Rust format string, literal is the
                            // suffix, "\", " closes it and adds the separator.
                            return "format!(\"{}" + literal + "\", " + convert(bo.left) + ")";
                        }
                        return "format!(\"{}{}\", " + convert(bo.left) + ", " + convert(bo.right) + ")";
                    }
                }
                return convert(bo.left) + " " + bo.op + " " + convert(bo.right);
            }

            case IF_EXPRESSION: {
                IfExpression ie = (IfExpression) expression;
                return "if " + convert(ie.condition) + " { " + convert(ie.thenExpression) + " } else { " + convert(ie.elseExpression) + " }";
            }

            case ARRAY_ALLOCATOR: {
                ArrayAllocator aa = (ArrayAllocator) expression;
                return "vec![Default::default(); " + convert(aa.size) + "]";
            }

            default:
                throw new IllegalArgumentException("Unsupported expression type " + expression);
        }
    }

    /**
     * Extract expected type from HeterogeneousArray annotation
     * @param annotations List of annotations to check
     * @return Expected type name (e.g., "String", "Int"), or null if not specified
     */
    private String getExpectedTypeFromAnnotation(List<PastAnnotation> annotations) {
        for (PastAnnotation annot : annotations) {
            if (annot instanceof HeterogeneousArray) {
                HeterogeneousArray ha =
                    (HeterogeneousArray) annot;
                return ha.getExpectedType();
            }
        }
        return null;
    }

    /**
     * Wrap an expression in a Value enum variant for heterogeneous arrays
     */
    private String wrapInValueEnum(Expression expr) {
        // Handle constants by type
        if (expr instanceof Constant) {
            Constant c = (Constant) expr;
            return switch (c.constantType) {
                case STRING -> "Value::String(" + convertConstant(c) + ".to_string())";
                case INTEGER, LONG -> "Value::Int(" + convertConstant(c) + ")";
                case FLOAT, DOUBLE -> "Value::Float(" + convertConstant(c) + ")";
                case BOOLEAN -> "Value::Bool(" + convertConstant(c) + ")";
                case NULL -> "Value::Null";
                default -> throw new IllegalArgumentException("Unsupported constant type for heterogeneous array: " + c.constantType);
            };
        }

        // Handle variables - need to determine type at runtime or use generic conversion
        if (expr instanceof Variable) {
            Variable ve = (Variable) expr;
            String varExpr = convert(expr);

            // Try to determine type from field if it's a field variable
            if (ve.field == Variable.VariableKind.FIELD_VARIABLE) {
                TypeName fieldType = getFieldType(ve.name);
                if (fieldType != null) {
                    if (isStringType(fieldType)) {
                        return "Value::String(" + varExpr + ".to_string())";
                    }
                    // Could add more type checks here for i32, bool, etc.
                }
            }

            // Default: assume it's already a Value or needs explicit conversion
            return varExpr;
        }

        // For other expressions, convert normally
        // (they might already be Value types or need explicit wrapping)
        return convert(expr);
    }

    private boolean isSimpleClosureBody(Statement statement) {
        return statement.statementKind == Statement.StatementKind.RETURN ||
                statement.statementKind == Statement.StatementKind.EXPRESSION_STATEMENT;
    }

    private void lateEmitMethod(Method method) {
        this.lateMethods.add(method);
    }

    private String convertConstant(Constant c) {
        return switch (c.constantType) {
            // Emit bare &str literal by default; callers that need an owned String use convertConstant(c, targetType)
            case STRING -> "\"" + c.value.toString().replace("\"", "\\\"") + "\"";
            case INTEGER -> c.value.toString();
            case LONG -> c.value.toString();
            case FLOAT -> c.value.toString();
            case DOUBLE -> c.value.toString();
            case BOOLEAN -> c.value.toString();
            case NULL -> "None";
            case BOOL -> c.value.toString();
        };
    }

    private String convertConstant(Constant c, TypeName targetType) {
        String base = convertConstant(c);
        // Add .to_string() only when the storage target is an owned String
        if (c.constantType == Constant.ConstantType.STRING && isStringType(targetType)) {
            return base + ".to_string()";
        }
        return base;
    }

    /** @deprecated use {@code METHOD_REGISTRY.isCategory(tn, STRING)} directly. */
    @Deprecated
    private boolean isStringType(TypeName tn) {
        return METHOD_REGISTRY.isCategory(tn, RustTypeCategory.STRING);
    }

    private String convertWithType(Expression expression, TypeName targetType) {
        if (expression instanceof Constant) {
            return convertConstant((Constant) expression, targetType);
        }
        return convert(expression);
    }

    private String convertMethodCall(MethodCall mc) {
        StringBuilder result = new StringBuilder();
        switch (mc.operatorKind) {
            case CONSTRUCTOR_CALL -> {
                if (mc.clazz!=null) {
                    return emitAnonymous(mc.clazz,result) ;
                }
                assert mc.className != null;
                if (isMap(mc.className)) {
                    imports.add("std::collections::HashMap");
                    return "HashMap::new()";
                }
                if (isList(mc.className)) {
                    return "Vec::new()";
                }
                String className = importAndGetSimpleName(mc.className);
                result.append(className).append("::new(");
                if (mc.arguments != null) {
                    result.append(mc.arguments.stream()
                            .map(this::convert)
                            .collect(Collectors.joining(", ")));
                }
                result.append(")");
                return result.toString();
            }

            case OBJECT_METHOD_CALL -> {
                assert mc.object != null;

                // Convert Java's forEach(lambda) to Rust's iter_mut().for_each(|params| { body })
                if ("forEach".equals(mc.methodName) && mc.arguments != null && mc.arguments.size() == 1
                        && mc.arguments.get(0) instanceof LambdaExpression) {
                    LambdaExpression le = (LambdaExpression) mc.arguments.get(0);
                    result.append(convert(mc.object)).append(".iter_mut().for_each(|");
                    result.append(le.parameters.stream()
                            .map(p -> sanitizeName(toSnakeCase(p.name)))
                            .collect(Collectors.joining(", ")));
                    result.append("| {\n");

                    // Emit closure body into a temporary buffer to avoid interleaving with sb
                    StringBuilder newsb = new StringBuilder();
                    String closureIndent = INDENT + INDENT + INDENT;
                    for (Statement stmt : le.body) {
                        emitStatement(stmt, closureIndent, newsb);
                    }
                    result.append(newsb);

                    result.append(INDENT).append(INDENT).append("})");
                    return result.toString();
                }

                String convertedObject = convertTraitReceiver(mc);
                String callMethodName = updateMethodNameIfOverloaded(mc, convertedObject, sanitizeName(mc.methodName));

                // Handle Option unwrapping for chained HashMap method calls.
                // HashMap::get() returns Option<&V>; calling methods on Option directly won't compile.
                // When the object is itself a .get() call, we must .unwrap() the Option first.
                // Registry ChainedGetBehavior drives whether to also switch to .get_mut().
                if (mc.object instanceof MethodCall) {
                    MethodCall innerMc = (MethodCall) mc.object;
                    if ("get".equals(innerMc.methodName)) {
                        // Use the original Java method name to look up registry metadata
                        RustMethodSpec chainSpec = METHOD_REGISTRY.resolveMethodGlobal(mc.methodName);
                        ChainedGetBehavior cgb = (chainSpec != null)
                                ? chainSpec.chainedGetBehavior : ChainedGetBehavior.NONE;

                        if (cgb == ChainedGetBehavior.SWITCH_TO_GET_MUT) {
                            // Need a mutable reference into the map — replace .get( with .get_mut(
                            int idx = convertedObject.lastIndexOf(".get(");
                            if (idx >= 0) {
                                convertedObject = convertedObject.substring(0, idx)
                                        + ".get_mut("
                                        + convertedObject.substring(idx + 5);
                            }
                        }
                        // Unwrap the Option in all cases — we are in the else-branch
                        // where the key is known to exist.
                        convertedObject += ".unwrap()";

                        result.append(convertedObject).append(".").append(toSnakeCase(callMethodName)).append("(");
                        if (mc.arguments != null) {
                            for (int i = 0; i < mc.arguments.size(); i++) {
                                if (i > 0) result.append(", ");
                                Expression arg = mc.arguments.get(i);
                                ArgTreatment treatment = (chainSpec != null)
                                        ? chainSpec.argTreatment(i) : ArgTreatment.PASS_BY_VALUE;
                                result.append(applyArgTreatment(treatment, arg));
                            }
                        }
                        // AtomicI32::fetch_add / fetch_sub require (delta, order) args even
                        // when reached through the chained-get path (which returns early).
                        if (("fetch_add".equals(callMethodName) || "fetch_sub".equals(callMethodName))
                                && (mc.arguments == null || mc.arguments.isEmpty())) {
                            imports.add(STD_SYNC_ATOMIC_ATOMIC_I_32_ORDERING);
                            result.append("1, Ordering::SeqCst");
                        }
                        result.append(")");
                        // Apply result transform: COPIED appends .copied() after HashMap::get()
                        if (chainSpec != null && chainSpec.resultTransform == ResultTransform.COPIED) {
                            result.append(".copied()");
                        }
                        return result.toString();
                    }
                }

                // Vec index access: someExpr.elements.get(count) → someExpr.elements[count as usize].clone()
                // Vec::get(usize) returns Option<&T> which is incompatible with the Option<T> LHS field.
                // Using index notation returns T directly, and .clone() gives an owned value.
                if ("get".equals(mc.methodName) && mc.arguments != null && mc.arguments.size() == 1
                        && mc.object instanceof MethodCall) {
                    MethodCall objectMc = (MethodCall) mc.object;
                    if (objectMc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                            && "__elements".equals(objectMc.methodName)) {
                        result.append(convertedObject).append("[").append(convert(mc.arguments.get(0))).append(" as usize].clone()");
                        return result.toString();
                    }
                }

                // Resolve method spec by original Java name for receiver/arg metadata.
                RustMethodSpec pushSpec = METHOD_REGISTRY.resolveMethodGlobal(mc.methodName);
                // self.inputs / self.outputs are Option<Vec<Box<dyn Any>>>.
                // ReceiverTransform.AS_MUT_UNWRAP signals that .as_mut().unwrap() is required
                // to reach the inner Vec, and each element must be Box::new(…) wrapped.
                boolean isOptionListField = isSelfOptionListField(mc);
                ReceiverTransform rt = (pushSpec != null) ? pushSpec.receiverTransform : ReceiverTransform.NONE;
                if (rt == ReceiverTransform.AS_MUT_UNWRAP && isOptionListField) {
                    convertedObject += ".as_mut().unwrap()";
                }
                result.append(convertedObject).append(".").append(toSnakeCase(callMethodName)).append("(");
                boolean traitCall = isTraitReceiver(mc);
                // MutableFirstParam call through a trait field: first arg needs &mut, rest &T.
                // Detected via the scoped "TraitName.methodName" set — avoids false positives when
                // two traits share the same generic method name (e.g. "process").
                TypeName receiverTraitType = traitCall ? traitReceiverType(mc) : null;
                boolean traitMFP = receiverTraitType != null
                        && mutableFirstParamMethodNames.contains(
                                toPascalCase(getSimpleNameFromType(receiverTraitType)) + "." + mc.methodName);
                if (mc.arguments != null) {
                    for (int i = 0; i < mc.arguments.size(); i++) {
                        if (i > 0) result.append(", ");
                        Expression arg = mc.arguments.get(i);
                        String argStr;
                        if (traitMFP && i == 0) {
                            // First arg of MutableFirstParam trait method: pass as &mut T
                            argStr = "&mut " + convert(arg);
                        } else if (traitCall) {
                            argStr = convertTraitCallArg(arg);
                        } else {
                            ArgTreatment treatment = (pushSpec != null)
                                    ? pushSpec.argTreatment(i) : ArgTreatment.PASS_BY_VALUE;
                            argStr = applyArgTreatment(treatment, arg);
                        }
                        // Wrap in Box::new(…) when pushing into an Option<Vec<Box<...>>> field.
                        if (rt == ReceiverTransform.AS_MUT_UNWRAP && isOptionListField) {
                            argStr = "Box::new(" + argStr + ")";
                        }
                        result.append(argStr);
                    }
                }
                // AtomicI32::fetch_add / fetch_sub require (delta: i32, order: Ordering).
                // Java's getAndIncrement/getAndDecrement take no args, so we inject the
                // standard delta=1 and SeqCst ordering when the PAST call has no arguments.
                if (("fetch_add".equals(callMethodName) || "fetch_sub".equals(callMethodName))
                        && (mc.arguments == null || mc.arguments.isEmpty())) {
                    imports.add(STD_SYNC_ATOMIC_ATOMIC_I_32_ORDERING);
                    result.append("1, Ordering::SeqCst");
                }
                result.append(")");
                return result.toString();
            }

            case OBJECT_ACCESSOR -> {
                if (mc.className != null) {
                    String className = getSimpleName(convert(mc.className));
                    // Java's .class literal → Rust string literal with the class name
                    if ("class".equals(mc.methodName)) {
                        result.append("\"").append(className).append("\"");
                        return result.toString();
                    }
                    result.append(className).append("::").append(sanitizeName(toSnakeCase(mc.methodName).toUpperCase()));
                } else if (mc.object instanceof Variable) {
                    // In a trait default method, fields cannot be accessed directly.
                    // Replace self.fieldName with self.fieldName() (abstract getter call).
                    Variable objVar2 = (Variable) mc.object;
                    if (currentEmittingTraitDefaultBody
                            && ("this".equals(objVar2.name) || "self".equals(objVar2.name))
                            && currentClass != null
                            && (currentClass.isInterface || currentClass.modifiers.contains(Modifier.ABSTRACT))
                            && isFieldOfCurrentAbstractClass(mc.methodName)) {
                        result.append("self.").append(sanitizeName(toSnakeCase(mc.methodName))).append("()");
                        return result.toString();
                    }
                    result.append(convert(mc.object)).append(".").append(sanitizeName(toSnakeCase(mc.methodName)));
                    // In MutableFirstParam methods, params 1..n are &T references.
                    // Reading a field through &T requires .clone() for non-Copy types.
                    // We always emit .clone() here — it is a no-op for Copy types and
                    // required (E0507) for non-Copy types such as Option<String>.
                    if (currentMethodMutableFirstParam
                            && currentMethodRefParamNames.contains(((Variable) mc.object).name)) {
                        result.append(".clone()");
                    }
                    // Primitive self-fields are stored as Option<T> in the struct.
                    // When read in a value context (arithmetic, if-condition, function arg),
                    // they must be unwrapped.  Non-primitive fields (HashMap, Vec, trait objects)
                    // are handled by their respective call-site converters (convertTraitReceiver,
                    // AS_MUT_UNWRAP, etc.) and must NOT get an extra .unwrap() here.
                    Variable objVar = (Variable) mc.object;
                    if (("this".equals(objVar.name) || "self".equals(objVar.name))
                            && isOptionField(mc.methodName)) {
                        TypeName ft = getFieldType(mc.methodName);
                        if (ft != null && isPrimitiveType(ft)) {
                            result.append(".unwrap()");
                        }
                    }
                } else if (mc.object instanceof MethodCall) {
                    MethodCall mc2 = (MethodCall) mc.object;
                    String inner = convert(mc2);
                    // get() returns Option<&T> — must .unwrap() before accessing a field.
                    // Exception: Vec index access (elements.get(i)) is converted to [i].clone()
                    // which already returns T — no further .unwrap() is needed.
                    if ("get".equals(mc2.methodName) && !isVecIndexAccessGet(mc2)) {
                        inner += ".unwrap()";
                    }
                    // Self Option field access: self.optionField.subField — unwrap to reach the inner T.
                    else if (isSelfOptionFieldAccess(mc2)) {
                        inner += ".as_ref().unwrap()";
                    }
                    result.append(inner).append(".").append(sanitizeName(toSnakeCase(mc.methodName)));
                } else {
                    throw new IllegalArgumentException("Unsupported object type in accessor: " + mc.object);
                }
                return result.toString();
            }

            case FUNCTIONAL_INTERFACE_CALL -> {
                // In Rust (like Java), traits have named methods that must be called
                // Unlike Python where functional interfaces can be called directly
                assert mc.object != null;
                result.append(convertTraitReceiver(mc)).append(".").append(sanitizeName(toSnakeCase(mc.methodName))).append("(");
                if (mc.arguments != null) {
                    // Look up the method's parameter types so that Constant.NULL arguments
                    // can be emitted as "" (empty string slice) for &str parameters instead of None.
                    List<Parameter> methodParams = findMethodParameters(mc.object.inferredType, mc.methodName);
                    List<String> convertedArgs = new ArrayList<>();
                    for (int i = 0; i < mc.arguments.size(); i++) {
                        Expression arg = mc.arguments.get(i);
                        if (arg instanceof Constant && ((Constant) arg).constantType == Constant.ConstantType.NULL
                                && methodParams != null && i < methodParams.size()
                                && isStringType(methodParams.get(i).type)) {
                            convertedArgs.add("\"\"");
                        } else {
                            convertedArgs.add(convertTraitCallArg(arg));
                        }
                    }
                    result.append(String.join(", ", convertedArgs));
                }
                result.append(")");
                return result.toString();
            }

            case OPERATOR_VARIABLE -> {
                assert mc.object != null;

                // Convert Java's forEach(lambda) to Rust's iter_mut().for_each(|params| { body })
                if ("forEach".equals(mc.methodName) && mc.arguments != null && mc.arguments.size() == 1
                        && mc.arguments.get(0) instanceof LambdaExpression) {
                    LambdaExpression le = (LambdaExpression) mc.arguments.get(0);
                    result.append(convert(mc.object)).append(".iter_mut().for_each(|");
                    result.append(le.parameters.stream()
                            .map(p -> sanitizeName(toSnakeCase(p.name)))
                            .collect(Collectors.joining(", ")));
                    result.append("| {\n");
                    // Emit closure body into a temporary buffer to avoid interleaving with sb
                    StringBuilder newSb = new StringBuilder();
                    String closureIndent = INDENT + INDENT + INDENT;
                    for (Statement stmt : le.body) {
                        emitStatement(stmt, closureIndent, newSb);
                    }
                    result.append(newSb);
                    result.append(INDENT).append(INDENT).append("})");
                    return result.toString();
                }

                String convertedObject = convert(mc.object);
                String callMethodName = updateMethodNameIfOverloaded(mc, convertedObject, sanitizeName(mc.methodName));

                // Resolve spec by original Java method name for per-argument treatment.
                RustMethodSpec opVarSpec = METHOD_REGISTRY.resolveMethodGlobal(mc.methodName);

                // For MutableFirstParam methods called on self: arg 0 is &mut (mutable borrow of the
                // bean being updated); args 1+ are & (read-only borrows of input/output beans).
                boolean calleeMFP = calledMethodHasMutableFirstParam(mc);

                // For self-method calls that are trait methods, look up parameter types so that
                // struct/bean arguments are passed as &arg (matching &T trait param declarations).
                int selfArgCount = (mc.arguments != null) ? mc.arguments.size() : 0;
                List<Parameter> traitParams = "self".equals(convertedObject)
                        ? findSelfMethodParameters(mc.methodName, selfArgCount) : null;

                result.append(convertedObject).append(".").append(toSnakeCase(callMethodName)).append("(");
                if (mc.arguments != null) {
                    for (int i = 0; i < mc.arguments.size(); i++) {
                        if (i > 0) result.append(", ");
                        Expression arg = mc.arguments.get(i);
                        if (calleeMFP && i == 0) {
                            result.append("&mut ").append(convert(arg));
                        } else if (calleeMFP && i > 0) {
                            result.append("&").append(convert(arg));
                        } else if (traitParams != null && i < traitParams.size()) {
                            TypeName paramType = traitParams.get(i).type;
                            // Trait methods declare struct/bean params as &T via convertTypeToRustTraitParam.
                            // Pass the argument as &arg to match the expected &T signature.
                            boolean isStructParam = !isPrimitiveType(paramType) && !isStringType(paramType)
                                    && !isList(paramType) && !isMap(paramType) && !isKnownTrait(paramType);
                            if (isStructParam) {
                                result.append("&").append(convert(arg));
                            } else {
                                ArgTreatment treatment = (opVarSpec != null)
                                        ? opVarSpec.argTreatment(i) : ArgTreatment.PASS_BY_VALUE;
                                result.append(applyArgTreatment(treatment, arg));
                            }
                        } else {
                            ArgTreatment treatment = (opVarSpec != null)
                                    ? opVarSpec.argTreatment(i) : ArgTreatment.PASS_BY_VALUE;
                            result.append(applyArgTreatment(treatment, arg));
                        }
                    }
                }
                result.append(")");
                return result.toString();
            }

            case STATIC_METHOD_CALL -> {
                assert mc.className != null;
                String className = getSimpleName(convert(mc.className));
                // Java's String.valueOf(x) → x.to_string() in Rust (no String::value_of exists)
                if ("String".equals(className) && "valueOf".equals(mc.methodName)
                        && mc.arguments != null && mc.arguments.size() == 1) {
                    result.append(convert(mc.arguments.get(0))).append(".to_string()");
                    return result.toString();
                }
                result.append(className).append("::").append(sanitizeName(toSnakeCase(mc.methodName))).append("(");
                if (mc.arguments != null) {
                    result.append(mc.arguments.stream()
                            .map(this::convert)
                            .collect(Collectors.joining(", ")));
                }
                result.append(")");
                return result.toString();
            }

            case NO_OPERATOR -> {
                String str = sanitizeName(toSnakeCase(mc.methodName));
                if (str.equals("get_map")) {
                    str="self.outer.get_map"; // this a hack to deal with anonymous class;
                    // Ideally, we should add an annotation to drive the behaviour of the emitter instead of hardcoding this logic here
                    // Same behaviour as Python emitter
                }
                result.append(str).append("(");
                if (mc.arguments != null) {
                    result.append(mc.arguments.stream()
                            .map(this::convert)
                            .collect(Collectors.joining(", ")));
                }
                result.append(")");
                return result.toString();
            }

            case SUPER_METHOD_CALL -> {
                // In Rust there is no super() syntax. Delegate via inner_parent composition.
                String superArgs = mc.arguments == null ? "" :
                        mc.arguments.stream().map(this::convert).collect(Collectors.joining(", "));
                if (mc.methodName == null) {
                    // super constructor call — handled in emitConstructor via inner_parent init;
                    // if we reach here outside a constructor, emit a commented-out placeholder.
                    result.append("/* super(").append(superArgs).append(") */");
                } else {
                    // super.method(args) — delegate to inner_parent
                    String mutableAccess = currentMethodIsMutable ? "as_mut()" : "as_ref()";
                    result.append("self.inner_parent.").append(mutableAccess).append(".unwrap().")
                          .append(sanitizeName(toSnakeCase(mc.methodName))).append("(").append(superArgs).append(")");
                }
                return result.toString();
            }
        }
        throw new IllegalArgumentException("Unsupported method call type " + mc);
    }

    private String emitAnonymousOLD(Class clazz, StringBuilder result) {
        result.append("new ");
        emit(clazz, result);
        return result.toString();
    }

    int anonymousClassCount=0;

    private String emitAnonymous(Class clazz, StringBuilder result) {
        // create an internal class, and create an instance
        String intf=clazz.interfaces.isEmpty() ? "Nointerface" : ((ClassName)clazz.interfaces.get(0)).simpleName;
        String className="Anonymous" + intf + (anonymousClassCount++);
        ClassName outer=new ClassName(currentClassName, currentPackageName);
        lateEmittedClasses.add(new LateEmittedClass(className, clazz, outer));
        return className + "::new(Self)";
    }

    List<LateEmittedClass> lateEmittedClasses=new ArrayList<>();

    static class LateEmittedClass extends Class {
        private final ClassName outer;
        Class clazz;

        public LateEmittedClass(String className, Class clazz, ClassName outer) {
            super(className);
            this.clazz = clazz;
            this.comments.addAll(clazz.comments);
            this.outer=outer;

            constructors.add(
                    new Constructor()
                            .MODIFIERS(Modifier.PUBLIC)
                            .PARAMETER(outer, "outer")
                            .BODY(ASSIGNMENT( METHOD_CALL(VARIABLE("this"),"outer"), VARIABLE( "outer"))));

            // All methods from the anonymous class implement the declared interface — add
            // @Override so the Rust emitter routes them to the correct
            // `impl TraitName for StructName` block rather than the inherent impl block.
            clazz.methods.forEach(m -> {
                if (m.annotation.stream().noneMatch(a -> a instanceof OverrideAnnotation)) {
                    m.annotation.add(new OverrideAnnotation());
                }
            });
            methods.addAll(clazz.methods);
            interfaces.addAll(clazz.interfaces);
            typeVariables.addAll(clazz.typeVariables);
            modifiers.addAll(clazz.modifiers);
            annotation.add(new NoSerialization());
            fields.add(FIELD("outer", outer));
        }
    }

    private String convertLH(Expression leftHandExpression) {
        if (leftHandExpression instanceof Variable) {
            Variable ve = (Variable) leftHandExpression;
            return ve.name;
        } else if (leftHandExpression instanceof MethodCall) {
            MethodCall mc = (MethodCall) leftHandExpression;
            // Detect self.optionField.subField pattern: OBJECT_ACCESSOR(OBJECT_ACCESSOR(Variable("this"), outerField), innerField)
            // The outer field is Option<T> in Rust, so we must insert .as_mut().unwrap() to get &mut T.
            if (mc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                    && mc.object instanceof MethodCall) {
                MethodCall innerMc = (MethodCall) mc.object;
                if (innerMc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                        && innerMc.object instanceof Variable) {
                    Variable v = (Variable) innerMc.object;
                    if (("this".equals(v.name) || "self".equals(v.name)) && isOptionField(innerMc.methodName)) {
                        String inner = convert(innerMc);
                        return inner + ".as_mut().unwrap()." + sanitizeName(toSnakeCase(mc.methodName));
                    }
                }
            }
            // Simple this.field = … assignment: return "self.field_name" directly.
            // Must NOT fall through to convertMethodCall, which adds .unwrap() for primitive
            // Option fields — that would produce unassignable `self.x.unwrap() = …`.
            if (mc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                    && mc.object instanceof Variable) {
                Variable v = (Variable) mc.object;
                if ("this".equals(v.name) || "self".equals(v.name)) {
                    return "self." + sanitizeName(toSnakeCase(mc.methodName));
                }
            }
            return convertMethodCall(mc);
        } else if (leftHandExpression instanceof ArrayAccessor) {
            ArrayAccessor aa = (ArrayAccessor) leftHandExpression;
            return convert(aa.arrayExpression) + "[" + convert(aa.indexExpression) + "]";
        }
        throw new IllegalArgumentException("Unsupported left-hand expression type");
    }

    /**
     * Check if an expression is a field access on a non-self variable (e.g., b.field_name).
     * Such fields on bean elements are Option&lt;T&gt; in Rust and need Some(...) wrapping on assignment.
     */
    private boolean isNonSelfFieldAccess(Expression expr) {
        if (expr instanceof MethodCall) {
            MethodCall mc = (MethodCall) expr;
            if (mc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR && mc.object instanceof Variable) {
                Variable v = (Variable) mc.object;
                // Field access on a non-self, non-field variable (i.e., a local/closure parameter like "b")
                return v.field == Variable.VariableKind.LOCAL_VARIABLE && !"self".equals(v.name);
            }
        }
        return false;
    }

    private String importAndGetSimpleName(TypeName typeName) {

        if (typeName instanceof ClassName) {
            ClassName cn = (ClassName) typeName;
            if (Objects.equals(cn.packge,"past.lang") && cn.simpleName.equals("AtomicInteger")) {
                imports.add(STD_SYNC_ATOMIC_ATOMIC_I_32_ORDERING);
                return "AtomicI32";
            }
            String modulePath = buildModulePath(cn.packge, cn.simpleName);
            // don't add an import for the current class
            if (cn.simpleName.equals(this.currentClass.name) &&
                    Objects.equals(cn.packge, this.currentPackageName)) {
            } else if (isParentClass(cn)) {
                // The class we are currently emitting as a late-emitted class references its own outer class.
                // This can happen when an anonymous class implements an interface and calls another method on the same interface.
                // In this case, we must not import the outer class
            } else {
                imports.add(modulePath);
            }
        }
        return getSimpleName(convert(typeName));
    }

    private boolean isParentClass(ClassName cn) {
        if (this.currentClass instanceof LateEmittedClass) {
            LateEmittedClass lec = (LateEmittedClass) this.currentClass;
            if (lec.outer !=null) {
                ClassName outerCn = lec.outer;
                return (outerCn.simpleName.equals(cn.simpleName) &&
                        Objects.equals(outerCn.packge, cn.packge));


            }
        }
        return false;
    }


    /**
     * True when expr is a field access on a non-self local variable (always Option&lt;T&gt; in generated structs).
     * This is the same predicate as isNonSelfFieldAccess but named for clarity when used in HashMap key contexts.
     */
    /**
     * Convert the receiver of a method call, appending {@code .as_ref().unwrap()} when the
     * receiver is a self-field of a known trait type (stored as {@code Option<Box<dyn Trait>>}).
     *
     * <p>Two PAST representations are handled:
     * <ul>
     *   <li>{@code Variable(name, FIELD_VARIABLE)} — direct field variable (most common in
     *       workflow generators using {@code FUNCTIONAL_INTERFACE_CALL}).</li>
     *   <li>{@code MethodCall(OBJECT_ACCESSOR, Variable("this"/"self"))} — chained accessor
     *       form used in some PAST constructions.</li>
     * </ul>
     */
    /**
     * Convert an argument passed to a {@code FUNCTIONAL_INTERFACE_CALL} (trait method call).
     *
     * <p>Trait methods declare struct/bean parameters as {@code &T} (see
     * {@code convertTypeToRustTraitParam}).  When the caller holds an owned local variable of
     * that type it must pass {@code &var} to satisfy the borrow.  Primitive ({@code Copy}) types
     * and self-field accesses are left unchanged.
     */
    private String convertTraitCallArg(Expression expression) {
        if (expression instanceof Variable) {
            Variable ve = (Variable) expression;
            if (ve.field == Variable.VariableKind.LOCAL_VARIABLE) {
                TypeName ty = ve.inferredType;
                // If inferredType is available, only borrow non-primitive, non-string, non-collection types.
                // If inferredType is null (type inference not run), assume struct and borrow.
                boolean isStruct = (ty == null)
                        || (!isPrimitiveType(ty) && !isStringType(ty) && !isList(ty) && !isMap(ty));
                if (isStruct) {
                    return "&" + sanitizeName(toSnakeCase(ve.name));
                }
            }
        }
        return convertArgument(expression);
    }

    /** Returns the type of the receiver if it is a known-trait self-field, otherwise null. */
    private TypeName traitReceiverType(MethodCall mc) {
        if (mc.object instanceof Variable) {
            Variable objVar = (Variable) mc.object;
            if (objVar.field == Variable.VariableKind.FIELD_VARIABLE) {
                TypeName t = mc.object.inferredType != null
                        ? mc.object.inferredType
                        : getFieldType(objVar.name);
                return (t != null && isKnownTrait(t)) ? t : null;
            }
        } else if (mc.object instanceof MethodCall) {
            MethodCall objMc = (MethodCall) mc.object;
            if (objMc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                    && objMc.object instanceof Variable) {
                String varName = ((Variable) objMc.object).name;
                if ("self".equals(varName) || "this".equals(varName)) {
                    // Mirror the FIELD_VARIABLE branch: prefer inferredType, fall back to
                    // getFieldType(accessor name) so that e.g. this.merger.process(…) is
                    // resolved even when no inferredType was set on the accessor expression.
                    TypeName t = mc.object.inferredType != null
                            ? mc.object.inferredType
                            : getFieldType(objMc.methodName);
                    return (t != null && isKnownTrait(t)) ? t : null;
                }
            }
        }
        return null;
    }

    /** True when the method call receiver is a known-trait self-field (Option&lt;Box&lt;dyn Trait&gt;&gt;). */
    private boolean isTraitReceiver(MethodCall mc) {
        return traitReceiverType(mc) != null;
    }

    private String convertTraitReceiver(MethodCall mc) {
        String convertedObject = convert(mc.object);
        TypeName trt = traitReceiverType(mc);
        if (trt != null) {
            // Use as_mut() when the trait is stateful (declares &mut self methods) so the
            // caller can invoke mutable methods through Box<dyn Trait>.  Use as_ref() otherwise.
            String traitName = toPascalCase(getSimpleNameFromType(trt));
            if (statefulTraits.contains(traitName)) {
                convertedObject += ".as_mut().unwrap()";
            } else {
                convertedObject += ".as_ref().unwrap()";
            }
        } else if (isSelfOptionFieldAccess(mc.object) && !isSelfOptionListField(mc)) {
            // Method called on an Option<T> self-field — must unwrap to reach the inner T.
            // Exception: Option<Vec> fields are handled downstream by the AS_MUT_UNWRAP transform.
            // Use as_mut() only when the enclosing method takes &mut self; otherwise as_ref().
            convertedObject += currentMethodIsMutable ? ".as_mut().unwrap()" : ".as_ref().unwrap()";
        }
        return convertedObject;
    }

    private boolean isLocalFieldAccess(Expression expr) {
        if (expr instanceof MethodCall) {
            MethodCall mc = (MethodCall) expr;
            if (mc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR && mc.object instanceof Variable) {
                Variable v = (Variable) mc.object;
                // "this" and "self" are self-references, not regular local variables.
                // Treating "this" as a local variable would cause isLocalFieldAccessProducingOption
                // to apply the conservative Option fallback to self-fields, suppressing Some() wraps.
                return v.field == Variable.VariableKind.LOCAL_VARIABLE
                        && !"self".equals(v.name)
                        && !"this".equals(v.name);
            }
        }
        return false;
    }

    /**
     * Convert a HashMap key argument: borrows the argument so it can be passed to
     * {@code HashMap::get} / {@code HashMap::contains_key} which expect {@code &K}.
     *
     * <p>String constants are already {@code &str} in Rust, so no extra {@code &} is added —
     * adding one would produce {@code &&str} which is unnecessary (though Deref-coercible).</p>
     */
    private String convertHashMapKeyArg(Expression arg) {
        // String constants are &str — passing them directly satisfies HashMap<String,_>::get(&str).
        if (arg instanceof Constant && ((Constant) arg).constantType == Constant.ConstantType.STRING) {
            return convertOptionArg(arg);
        }
        // Integer/long constants are Vec indices — no borrow needed (Vec::get takes usize, not &usize).
        if (arg instanceof Constant) {
            Constant.ConstantType ct = ((Constant) arg).constantType;
            if (ct == Constant.ConstantType.INTEGER || ct == Constant.ConstantType.LONG) {
                return convert(arg);
            }
        }
        // String-typed local variable parameters are already &str — adding & would yield &&str,
        // which does not satisfy Borrow<str> for HashMap<String, V> lookups.
        // Non-string local variables (e.g. i32 keys) still need & to borrow them.
        if (arg instanceof Variable && ((Variable) arg).field == Variable.VariableKind.LOCAL_VARIABLE) {
            Variable ve = (Variable) arg;
            if (ve.inferredType != null && isStringType(ve.inferredType)) {
                return convertOptionArg(arg);
            }
        }
        return "&" + convertOptionArg(arg);
    }

    /**
     * True when {@code expr} is a direct self-field reference that yields an owned (non-borrowed)
     * value in Java but a borrowed value in Rust when accessed through {@code &self}.
     * Covers both the {@code FIELD_VARIABLE} form and the {@code this.field} OBJECT_ACCESSOR form.
     */
    private boolean isSelfFieldRef(Expression expr) {
        if (expr instanceof Variable) {
            return ((Variable) expr).field == Variable.VariableKind.FIELD_VARIABLE;
        }
        if (expr instanceof MethodCall) {
            MethodCall mc = (MethodCall) expr;
            if (mc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                    && mc.object instanceof Variable) {
                String name = ((Variable) mc.object).name;
                return "this".equals(name) || "self".equals(name);
            }
        }
        return false;
    }

    /**
     * Apply the given {@link ArgTreatment} to a call-site argument, returning the Rust source
     * string for that argument position.
     *
     * <p>Dispatches to the appropriate existing converter helper:</p>
     * <ul>
     *   <li>{@link ArgTreatment#PASS_BY_VALUE} — {@link #convertCallArg}</li>
     *   <li>{@link ArgTreatment#PASS_BY_REF}   — {@code &amp;} + {@link #convertCallArg}</li>
     *   <li>{@link ArgTreatment#KEY_BORROW}    — {@link #convertHashMapKeyArg} ({@code &amp;} + unwrap)</li>
     *   <li>{@link ArgTreatment#VALUE_UNWRAP}  — {@link #convertOptionArg} (unwrap Option)</li>
     *   <li>{@link ArgTreatment#OWNED_STRING}  — {@link #convertOwnedStringArg} ({@code .to_string()})</li>
     *   <li>{@link ArgTreatment#CLONE}         — {@link #convertCallArg} + {@code .clone()} unless Copy</li>
     *   <li>{@link ArgTreatment#BOX_CLONE}     — {@code Box::new(} … {@code .clone())} unless Copy</li>
     * </ul>
     */
    private String applyArgTreatment(ArgTreatment treatment, Expression arg) {
        return switch (treatment) {
            case PASS_BY_VALUE -> convertCallArg(arg);
            case PASS_BY_REF   -> "&" + convertCallArg(arg);
            case KEY_BORROW    -> convertHashMapKeyArg(arg);
            case VALUE_UNWRAP  -> convertOptionArg(arg);
            case OWNED_STRING  -> convertOwnedStringArg(arg);
            case CLONE -> {
                String base = convertCallArg(arg);
                if (arg.inferredType != null && METHOD_REGISTRY.isCopy(arg.inferredType)) yield base;
                yield base + ".clone()";
            }
            case BOX_CLONE -> {
                String base = convertCallArg(arg);
                if (arg.inferredType != null && METHOD_REGISTRY.isCopy(arg.inferredType))
                    yield "Box::new(" + base + ")";
                yield "Box::new(" + base + ".clone())";
            }
        };
    }

    /**
     * Convert a string constant argument to an owned {@code String} via {@code .to_string()}.
     * Used for HashMap {@code insert(key, value)} where the key must be {@code String}, not {@code &str}.
     *
     * <p>For non-String Option fields (e.g. {@code bean.transformed_file: Option<QualifiedName>}),
     * appends {@code .unwrap()} so the insert receives an owned value rather than an Option.</p>
     */
    private String convertOwnedStringArg(Expression arg) {
        if (arg instanceof Constant) {
            Constant c = (Constant) arg;
            if (c.constantType == Constant.ConstantType.STRING) {
                return "\"" + c.value.toString().replace("\"", "\\\"") + "\".to_string()";
            }
        }
        // Non-string Option field access — unwrap to get the owned key value.
        if (isLocalFieldAccessProducingOption(arg)) {
            return convert(arg) + ".unwrap()";
        }
        // Plain &str variable (e.g. a method parameter) needs .to_string() to produce an
        // owned String for HashMap key insertion.
        if (arg instanceof Variable) {
            return convert(arg) + ".to_string()";
        }
        return convert(arg);
    }

    /**
     * Convert a call-site argument, adding {@code &mut} when passing a local {@code HashMap} variable.
     * All HashMap parameters are declared {@code &mut} by the emitter; callers must pass a mutable borrow.
     */
    private String convertCallArg(Expression arg) {
        if (arg instanceof Variable) {
            Variable v = (Variable) arg;
            if (v.field == Variable.VariableKind.LOCAL_VARIABLE
                    && arg.inferredType != null && isMap(arg.inferredType)) {
                return "&mut " + sanitizeName(toSnakeCase(v.name));
            }
        }
        // Self Option fields (self.bean__x : Option<T>) need .clone().unwrap() to produce T
        // when passed by value to a method that expects T (e.g. add_elements(T)).
        if (isSelfOptionFieldAccess(arg)) {
            return convert(arg) + ".clone().unwrap()";
        }
        return convert(arg);
    }

    /**
     * Convert an argument that must be plain {@code T} (not {@code Option&lt;T&gt;}).
     * Uses the principled rule: a field access on a local variable is {@code Option<T>}
     * if and only if the corresponding PAST field has no initialiser (looked up via
     * {@link #isLocalFieldAccessProducingOption}).  Appends {@code .unwrap()} only in
     * that case.  Initialized fields are plain {@code T} and need no unwrap.
     */
    private String convertOptionArg(Expression arg) {
        if (isLocalFieldAccessProducingOption(arg)) {
            return convert(arg) + ".unwrap()";
        }
        return convert(arg);
    }

    /**
     * True when expr already evaluates to {@code Option&lt;T&gt;} — used to suppress {@code Some(...)} wrapping
     * on the LHS of an assignment.
     *
     * <p>The primary case is an inner HashMap get chain:
     * {@code map.get(outerKey).get(innerKey)} where the emitter adds {@code .copied()} → {@code Option&lt;T&gt;}.
     */
    /**
     * Returns true when a local (non-self) field access resolves to an {@code Option<T>} in
     * the generated Rust.  The determination uses the principled rule:
     * <ul>
     *   <li>A PAST field <em>with</em> an initialiser is emitted as plain {@code T}.</li>
     *   <li>A PAST field <em>without</em> an initialiser is emitted as {@code Option<T>}.</li>
     * </ul>
     * The field definition is looked up in {@link #classRegistry} (populated by
     * {@link #discoverClass}).  If the class is not yet registered, the method returns
     * {@code true} as a conservative fallback (all fields in the generated workflow pattern
     * are uninitialised and therefore {@code Option<T>}).
     */
    private boolean isLocalFieldAccessProducingOption(Expression expr) {
        if (!isLocalFieldAccess(expr)) return false;
        MethodCall mc = (MethodCall) expr;
        Variable objVar = (Variable) mc.object;
        TypeName objType = objVar.inferredType;
        if (objType instanceof ClassName) {
            String simpleClassName = ((ClassName) objType).simpleName;
            Class fieldClass = classRegistry.get(toPascalCase(simpleClassName));
            if (fieldClass != null) {
                String fieldName = mc.methodName; // Java camelCase, same as PAST field.name
                for (Field f : fieldClass.fields) {
                    if (f.name.equals(fieldName)) {
                        return f.initialiser == null; // no initialiser → Option<T>
                    }
                }
                // Field not found in class — not expected; fall through to conservative true
            }
        }
        // Conservative: if the class is not registered or the type is unknown,
        // assume the field is Option<T> (correct for all current workflow output structs).
        return true;
    }

    private boolean expressionProducesOption(Expression expr) {
        // Chained inner-HashMap get: map.get(outerKey).get(innerKey) — emitter appends .copied() → Option<T>
        if (expr instanceof MethodCall) {
            MethodCall mc = (MethodCall) expr;
            if ("get".equals(mc.methodName) && mc.object instanceof MethodCall) {
                return "get".equals(((MethodCall) mc.object).methodName);
            }
        }
        // Field access on a struct element obtained via .get() — e.g. elements.get(0).container1.
        // By convention, all uninitialized struct fields in the generated workflow pattern are Option<T>.
        if (expr instanceof MethodCall) {
            MethodCall mc = (MethodCall) expr;
            if (mc.operatorKind == MethodCall.MethodCallKind.OBJECT_ACCESSOR
                    && mc.object instanceof MethodCall
                    && "get".equals(((MethodCall) mc.object).methodName)) {
                return true;
            }
        }
        // A field access on a local (non-self) variable e.g. transforming_outputs.transformed_file.
        // The field is Option<T> when it has no initialiser (principled rule — see isLocalFieldAccessProducingOption).
        if (isLocalFieldAccessProducingOption(expr)) return true;
        // Inside a trait default body, method parameters of primitive type (e.g. INTEGER → i32) are
        // emitted as Option<T> via convertTypeToRustTraitParam.  Wrapping them again in Some() would
        // produce Option<Option<T>> which doesn't match the Option<T> struct field.
        if (currentEmittingTraitDefaultBody && expr instanceof Variable) {
            Variable ve = (Variable) expr;
            if (ve.field == Variable.VariableKind.LOCAL_VARIABLE
                    && ve.inferredType != null && isPrimitiveType(ve.inferredType)) {
                return true;
            }
        }
        return false;
    }

    private String convertTypeToRust(TypeName tn) {
        if (tn == null) return "()";

        switch (tn.typeKind) {
            case CLASS: {
                ClassName cn = (ClassName) tn;

                // Check for common types regardless of package
                String rustType = convertCommonType(cn.simpleName);
                if (rustType != null) {
                    return rustType;
                }

                // Package-specific handling
                if (cn.packge != null && cn.packge.equals("past.util")) {
                    return switch (cn.simpleName) {
                        case "List", "ArrayList", "LinkedList" -> "Vec";
                        case "Map", "HashMap" -> "HashMap";
                        default -> toPascalCase(cn.simpleName);
                    };
                }

                // Handle Class type (java.lang.Class -> &'static str in Rust)
                //if (cn.packge != null && cn.packge.equals("past.lang") && "Class".equals(cn.simpleName)) {
                //    return "&'static str";
               // }
                if (cn.packge != null && cn.packge.equals("past.lang")) {
                    return switch (cn.simpleName) {
                        case "AtomicInteger" -> "AtomicI32";
                        case "Class" -> "&'static str";
                        // "bool" is already a Rust keyword; toPascalCase would wrongly give "Bool"
                        case "bool" -> "bool";
                        default -> toPascalCase(cn.simpleName);
                    };
                }


                return toPascalCase(cn.simpleName);
            }
            case ARRAY: {
                ArrayType at = (ArrayType) tn;
                return "Vec<" + convertTypeToRust(at.elementType) + ">";
            }
            case PARAMETERIZED: {
                ParameterizedType pt = (ParameterizedType) tn;

                // Check if this is Class<?> — in Rust, there's no Class type; use &'static str
                if (isClassType(pt.rawType)) {
                    return "&'static str";
                }

                // Check if this is a Function type
                if (isFunctionType(pt.rawType)) {
                    // Convert Function<A, B> to impl Fn(A) -> B
                    if (pt.typeArguments.length == 2) {
                        return "impl Fn(" + convertTypeToRust(pt.typeArguments[0]) + ") -> " + convertTypeToRust(pt.typeArguments[1]);
                    }
                    // Convert BiFunction<A, B, C> to impl Fn(A,B) -> C
                    if (pt.typeArguments.length == 3) {
                        return "impl Fn(" + convertTypeToRust(pt.typeArguments[0]) + "," + convertTypeToRust(pt.typeArguments[1]) + ") -> " + convertTypeToRust(pt.typeArguments[2]);
                    }
                }

                StringBuilder result = new StringBuilder();
                result.append(convertTypeToRust(pt.rawType));
                if (pt.typeArguments.length > 0) {
                    result.append("<");
                    for (int i = 0; i < pt.typeArguments.length; i++) {
                        if (i > 0) result.append(", ");
                        result.append(convertTypeToRust(pt.typeArguments[i]));
                    }
                    result.append(">");
                }
                return result.toString();
            }
            case VARIABLE: {
                TypeVariable tv =
                        (TypeVariable) tn;
                return tv.name;
            }
            default:
                return tn.toString();
        }
    }

    private String convert(TypeName tn) {
        switch (tn.typeKind) {
            case CLASS: {
                ClassName cn = (ClassName) tn;
                if (cn.packge != null && !cn.packge.isEmpty()) {
                    return cn.packge + "." + cn.simpleName;
                } else {
                    return cn.simpleName;
                }
            }
            case VARIABLE:
            case ARRAY:
            case PARAMETERIZED:
            default:
                return tn.toString();
        }
    }

    private String getSimpleName(String fullName) {
        String simpleName;
        if (fullName.contains(".")) {
            simpleName = fullName.substring(fullName.lastIndexOf('.') + 1);
        } else {
            simpleName = fullName;
        }
        return toPascalCase(simpleName);
    }

    private List<String> convert(Comment c) {
        return List.of(String.format(c.format.replace("$L", "%s").replace("$N", "%s"), c.objects).split("\n"));
    }

    // ---- Type-category helpers (Phase 2: delegate to METHOD_REGISTRY) -----------------------
    // These thin wrappers keep all existing call sites unchanged while routing the actual
    // logic through the registry.  Phase 5 will inline the registry calls and delete these.

    /** @deprecated use {@code METHOD_REGISTRY.isCategory(tn, MAP)} directly. */
    @Deprecated
    private boolean isMap(TypeName typeName) {
        return METHOD_REGISTRY.isCategory(typeName, RustTypeCategory.MAP);
    }

    /** @deprecated use {@code METHOD_REGISTRY.isCategory(tn, LIST)} directly. */
    @Deprecated
    private boolean isList(TypeName typeName) {
        return METHOD_REGISTRY.isCategory(typeName, RustTypeCategory.LIST);
    }

    /** @deprecated use {@code METHOD_REGISTRY.isCategory(tn, FUNCTION)} directly. */
    @Deprecated
    private boolean isFunctionType(TypeName typeName) {
        return METHOD_REGISTRY.isCategory(typeName, RustTypeCategory.FUNCTION);
    }

    /** @deprecated use {@code METHOD_REGISTRY.isCategory(tn, CLASS_REF)} directly. */
    @Deprecated
    private boolean isClassType(TypeName typeName) {
        return METHOD_REGISTRY.isCategory(typeName, RustTypeCategory.CLASS_REF);
    }

    /**
     * Returns true when the method has a "phantom" type variable in Rust: the variable
     * appears as the return type but every parameter that references it does so only through
     * {@code Class<T>} (which maps to {@code &'static str}, erasing {@code T}).
     * Such methods are not dyn-compatible as {@code fn get<T>(...) -> T}; they must be
     * emitted without the generic and with {@code Box<dyn std::any::Any>} as return type.
     */
    private boolean hasPhantomTypeVariable(Method method) {
        if (method.typeVariables == null || method.typeVariables.isEmpty()) return false;
        if (!(method.returnType instanceof TypeVariable)) return false;
        if (method.parameters == null || method.parameters.isEmpty()) return true;
        String tvName = ((TypeVariable) method.returnType).name;
        for (Parameter p : method.parameters) {
            if (isClassType(p.type)) continue;           // Class<T> → &'static str: T is erased
            if (typeContainsVariable(p.type, tvName)) return false; // T appears concretely
        }
        return true;
    }

    /** Returns true if {@code tn} references the type variable named {@code varName}. */
    private boolean typeContainsVariable(TypeName tn, String varName) {
        if (tn instanceof TypeVariable) return ((TypeVariable) tn).name.equals(varName);
        if (tn instanceof ParameterizedType) {
            for (TypeName arg : ((ParameterizedType) tn).typeArguments) {
                if (typeContainsVariable(arg, varName)) return true;
            }
        }
        return false;
    }

    private boolean isObjectType(TypeName typeName) {
        if (typeName.typeKind != TypeName.TypeKind.CLASS) {
            return false;
        }
        ClassName cn = (ClassName) typeName;
        return "Object".equals(cn.simpleName);
    }

    /**
     * Returns true if {@code tn} is, or contains as a type argument, the Java {@code Object}
     * type — which maps to {@code Box<dyn std::any::Any>} in Rust and therefore cannot
     * implement {@code Clone} or {@code Debug} automatically.
     */
    private boolean hasObjectTypeArgument(TypeName tn) {
        if (isObjectType(tn)) return true;
        if (tn instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) tn;
            for (TypeName arg : pt.typeArguments) {
                if (hasObjectTypeArgument(arg)) return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the field type {@code tn} is a class (by simple name) that is known
     * to the type registry and itself has fields containing {@code AtomicInteger}.
     * Used to propagate no-Clone transitively: {@code BeanLocalEnactor3} holds
     * {@code IdentifierRegistry} which holds {@code AtomicI32}, so neither can derive Clone.
     */
    private boolean fieldTypeHasAtomicField(TypeName tn) {
        if (typeRegistry == null) return false;
        ClassName cn = null;
        if (tn instanceof ClassName) cn = (ClassName) tn;
        else if (tn instanceof ParameterizedType) {
            TypeName raw = ((ParameterizedType) tn).rawType;
            if (raw instanceof ClassName) cn = (ClassName) raw;
        }
        if (cn == null) return false;
        Class pastClass = typeRegistry.getPastClass(cn.simpleName, cn.packge);
        if (pastClass == null) return false;
        return pastClass.fields.stream()
                .filter(f -> !f.modifiers.contains(Modifier.STATIC))
                .anyMatch(f -> containsAtomicType(f.type));
    }

    /**
     * Returns true if {@code tn} is, or transitively contains, {@code AtomicInteger}
     * (which maps to {@code AtomicI32} in Rust).  {@code AtomicI32} does not implement
     * {@code Clone}, so structs with such fields must omit the {@code Clone} derive.
     */
    private boolean containsAtomicType(TypeName tn) {
        if (tn instanceof ClassName) {
            ClassName cn = (ClassName) tn;
            return "AtomicInteger".equals(cn.simpleName);
        }
        if (tn instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) tn;
            if (containsAtomicType(pt.rawType)) return true;
            for (TypeName arg : pt.typeArguments) {
                if (containsAtomicType(arg)) return true;
            }
        }
        return false;
    }

    /**
     * Scan method body for a CastExpression targeting the given parameter name,
     * and return the cast target type. This allows inferring the concrete type
     * for Object-typed parameters in Rust (avoiding Box&lt;dyn Any&gt;).
     */
    private TypeName inferConcreteTypeFromCasts(String paramName, List<Statement> body) {
        for (Statement stmt : body) {
            TypeName result = findCastTargetInStatement(paramName, stmt);
            if (result != null) return result;
        }
        return null;
    }

    private TypeName findCastTargetInStatement(String paramName, Statement stmt) {
        if (stmt.statementKind == Statement.StatementKind.EXPRESSION_STATEMENT && stmt instanceof Expression) {
            return findCastTargetInExpression(paramName, (Expression) stmt);
        }
        return null;
    }

    private TypeName findCastTargetInExpression(String paramName, Expression expr) {
        if (expr instanceof CastExpression) {
            CastExpression c = (CastExpression) expr;
            if (c.expression instanceof Variable) {
                Variable v = (Variable) c.expression;
                if (paramName.equals(v.name)) {
                    return c.targetType;
                }
            }
        }
        if (expr instanceof MethodCall) {
            MethodCall mc = (MethodCall) expr;
            if (mc.arguments != null) {
                for (Expression arg : mc.arguments) {
                    TypeName result = findCastTargetInExpression(paramName, arg);
                    if (result != null) return result;
                }
            }
        }
        return null;
    }

    private boolean isArrayType(TypeName typeName) {
        if (typeName == null) return false;
        if (typeName.typeKind == TypeName.TypeKind.ARRAY) return true;
        // Also detect ClassName "int[]" which is used for PAST int arrays
        if (typeName instanceof ClassName) {
            ClassName cn = (ClassName) typeName;
            return cn.simpleName != null && cn.simpleName.endsWith("[]");
        }
        return false;
    }

    /** @deprecated use {@code METHOD_REGISTRY.isCategory(tn, PRIMITIVE)} directly. */
    @Deprecated
    private boolean isPrimitiveType(TypeName typeName) {
        return METHOD_REGISTRY.isCategory(typeName, RustTypeCategory.PRIMITIVE);
    }

    /**
     * Map a Java simple type name to its Rust equivalent.
     * Delegates to the registry; returns {@code null} for unknown types.
     *
     * @deprecated use {@code METHOD_REGISTRY.rustName(simpleName)} directly.
     */
    @Deprecated
    private String convertCommonType(String typeName) {
        return METHOD_REGISTRY.rustName(typeName);
    }

    Map<String, String> methodNameConversion = new HashMap<>() {{
        put("add", "push");
        put("size", "len");
        put("isEmpty", "is_empty");
        put("toString", "to_string");
        // HashMap method translations
        put("containsKey", "contains_key");
        put("put", "insert");
        put("getAndIncrement", "fetch_add");
        put("getAndDecrement", "fetch_sub");
    }};

    private String sanitizeName(String name) {
        if (name == null) return "unknown";
        // Registry-driven method name translation (replaces methodNameConversion, Phase 3).
        RustMethodSpec spec = METHOD_REGISTRY.resolveMethodGlobal(name);
        if (spec != null) return spec.rustName;
        // Legacy fallback — to be removed in Phase 5 once all entries are in the registry.
        if (methodNameConversion.containsKey(name)) {
            return methodNameConversion.get(name);
        }
        if (name.startsWith(GENERATED_VAR_PREFIX)) {
            return name.substring(2);
        }
        if (name.equals("this")) {
            return "self";
        }

        // Handle Rust reserved keywords
        if (isRustKeyword(name)) {
            return "r#" + name;
        }
        return name;
    }

    private boolean isRustKeyword(String name) {
        return List.of("as", "break", "const", "continue", "crate", "else", "enum", "extern",
                        "false", "fn", "for", "if", "impl", "in", "let", "loop", "match", "mod",
                        "move", "mut", "pub", "ref", "return", "self", "Self", "static", "struct",
                        "super", "trait", "true", "type", "unsafe", "use", "where", "while",
                        "async", "await", "dyn", "abstract", "become", "box", "do", "final",
                        "macro", "override", "priv", "typeof", "unsized", "virtual", "yield")
                .contains(name);
    }

    private String toSnakeCase(String name) {
        if (name == null || name.isEmpty()) return name;

        // Convert camelCase to snake_case
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(name.charAt(0)));

        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_');
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private String toPascalCase(String name) {
        if (name == null || name.isEmpty()) return name;

        // Convert snake_case or camelCase to PascalCase
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }
}
