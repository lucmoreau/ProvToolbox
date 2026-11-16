package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Constant;
import org.openprovenance.prov.template.compiler.past.Expression;
import org.openprovenance.prov.template.compiler.past.Field;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.Statement;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.descriptors.*;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.ArrayInitialiser.ARRAY_INITIALISER;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.ThrowStatement.THROW;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

/**
 * T36 stage 3: the typed bean of each {@code subtype} of the catalogue, its two conversions, and the catalogue's dispatcher
 * (R9), all as PAST, so the existing emitters write them in each language. Nothing here is text of one language: the JSON
 * text of a carrier is reached through calls to the runtime class {@code JsonText} (in prov-model, a candy in jsweet, a port
 * in Python), as {@code @escape} reaches {@code escapeCsv}.
 * <p>
 * A subtype adds nothing to the server side (design §5.1): these classes are client beans, in the package of the refined
 * template's input bean, and convert from and to that bean.
 */
public class CompilerSubtype {

    static final ClassName JSON_TEXT = get(Constants.JSON_TEXT_RUNTIME_CLASS, Constants.JSON_TEXT_RUNTIME_PACKAGE);
    static final String GENERAL = Constants.GENERATED_VAR_PREFIX + "general";
    static final String TYPED = Constants.GENERATED_VAR_PREFIX + "typed";
    static final String REFINES = "refines";

    private final CompilerUtil compilerUtil;
    private boolean found = false;
    private final List<Class> generated = new ArrayList<>();

    /** The PAST classes generated so far (typed beans and the dispatcher, whole: Rust gets the typed beans without their methods), for tests of the emitters. */
    public List<Class> getGeneratedClasses() {
        return generated;
    }

    public CompilerSubtype(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
    }

    /** Whether the catalogue has a subtype: its client project needs the JSON text runtime (prov-model). */
    public boolean foundSubtype() {
        return found;
    }

    /** The subtypes of the catalogue, in its order. */
    static List<SubtypeTemplateCompilerConfig> subtypes(TemplatesProjectConfiguration configs) {
        List<SubtypeTemplateCompilerConfig> res = new ArrayList<>();
        if (configs.templates == null) return res;
        for (TemplateCompilerConfig c : configs.templates) if (c instanceof SubtypeTemplateCompilerConfig) res.add((SubtypeTemplateCompilerConfig) c);
        return res;
    }

    static SimpleTemplateCompilerConfig refined(TemplatesProjectConfiguration configs, SubtypeTemplateCompilerConfig st) {
        for (TemplateCompilerConfig c : configs.templates) {
            if (c instanceof SimpleTemplateCompilerConfig && Objects.equals(c.fullyQualifiedName, st.refines)) return (SimpleTemplateCompilerConfig) c;
        }
        throw new IllegalStateException("T36: subtype " + st.fullyQualifiedName + " refines " + st.refines + ", no simple template (the checker should have refused it)");
    }

    // ---- the typed beans -----------------------------------------------------------------------------------------------

    /** One file per subtype; empty for a catalogue without subtype. */
    public List<SpecificationFile> generateTypedBeans(TemplatesProjectConfiguration configs, Locations locations) {
        List<SpecificationFile> res = new ArrayList<>();
        for (SubtypeTemplateCompilerConfig st : subtypes(configs)) {
            res.add(generateTypedBean(configs, locations, st));
        }
        return res;
    }

    /** The class of a subtype's typed bean, named as the beans are, in the subtype's own package (Luc, 2026-11-16: "Honour the declared package"). */
    ClassName typedBeanType(Locations locations, SimpleTemplateCompilerConfig simple, SubtypeTemplateCompilerConfig st) {
        return get(compilerUtil.beanNameClass(st.name, BeanDirection.INPUTS), locations.getBeansPackage(st.fullyQualifiedName, BeanDirection.INPUTS));
    }

    ClassName generalBeanType(Locations locations, SimpleTemplateCompilerConfig simple) {
        return get(compilerUtil.beanNameClass(simple.name, BeanDirection.INPUTS), locations.getBeansPackage(simple.fullyQualifiedName, BeanDirection.INPUTS));
    }

    /** The in-JSON values this subtype carries, grouped by carrier, in the order the template declares them. */
    static Map<String, List<InJsonValue>> carried(TemplateBindingsSchema schema, SubtypeTemplateCompilerConfig st) {
        Map<String, List<InJsonValue>> res = new LinkedHashMap<>();
        for (InJsonValue v : schema.getInJson().values()) {
            if (st.carries.containsKey(v.getName())) res.computeIfAbsent(v.getCarrier(), k -> new ArrayList<>()).add(v);
        }
        return res;
    }

    /** The input variables of the refined template that a typed bean keeps as fields of their own: not fixed, not a carrier it carries values in. */
    static List<String> ordinaryInputs(TemplateBindingsSchema schema, SubtypeTemplateCompilerConfig st) {
        Map<String, List<InJsonValue>> carried = carried(schema, st);
        List<String> ordinary = new ArrayList<>();
        for (String key : descriptorUtils.fieldNames(schema)) {
            if (!descriptorUtils.isInput(key, schema)) continue;
            if (st.fixed.containsKey(key) || carried.containsKey(key)) continue;
            ordinary.add(key);
        }
        return ordinary;
    }

    static final String OTHER_SUFFIX = "Other";

    static String otherKeysField(String carrier) {
        return carrier + OTHER_SUFFIX;
    }

    public SpecificationFile generateTypedBean(TemplatesProjectConfiguration configs, Locations locations, SubtypeTemplateCompilerConfig st) {
        found = true;
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();
        SimpleTemplateCompilerConfig simple = refined(configs, st);
        TemplateBindingsSchema schema = compilerUtil.getBindingsSchema(simple);
        Map<String, List<Descriptor>> var = schema.getVar();
        Map<String, List<InJsonValue>> carried = carried(schema, st);

        ClassName typed = typedBeanType(locations, simple, st);
        ClassName general = generalBeanType(locations, simple);
        String where = "subtype " + st.fullyQualifiedName;

        // the general bean's input fields this typed bean keeps as they are
        List<String> ordinary = ordinaryInputs(schema, st);

        Class pastClass = compilerUtil.getPastFactory()
                .CLASS(typed.simpleName)
                .MODIFIERS(Modifier.PUBLIC)
                .COMMENT("The typed bean of the subtype $N of the template $N: a record of the template, with ", st.fullyQualifiedName, st.refines)
                .COMMENT("some inputs fixed and some values kept inside a JSON text variable given a field each.\n")
                .COMMENT("It adds nothing to the server side: $N converts it to the template's input bean, and $N back.\n", "toGeneral", "fromGeneral");

        pastClass.FIELDS(FIELD(Constants.IS_A, STRING).COMMENT("The subtype name").MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .INITIALIZER(new Constant(st.fullyQualifiedName)));
        pastClass.FIELDS(FIELD(REFINES, STRING).COMMENT("The template this is a subtype of").MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .INITIALIZER(new Constant(st.refines)));

        for (String key : ordinary) {
            Descriptor d = var.get(key).get(0);
            String doc = descriptorUtils.getFromDescriptor(d, AttributeDescriptor::getDocumentation, NameDescriptor::getDocumentation);
            pastClass.FIELDS(FIELD(key, compilerUtil.getPastTypeForDeclaredType(var, key)).MODIFIERS(Modifier.PUBLIC)
                    .COMMENT("$N: $L\n", key, doc == null ? CompilerBeanGenerator.JAVADOC_NO_DOCUMENTATION : doc));
        }
        for (Map.Entry<String, List<InJsonValue>> e : carried.entrySet()) {
            for (InJsonValue v : e.getValue()) {
                pastClass.FIELDS(FIELD(v.getName(), compilerUtil.getPastClassForType(v.getType())).MODIFIERS(Modifier.PUBLIC)
                        .COMMENT("$N: $L (in $N as \"$N\", $L)\n", v.getName(), v.getDocumentation() == null ? CompilerBeanGenerator.JAVADOC_NO_DOCUMENTATION : v.getDocumentation(),
                                e.getKey(), v.getKey(), SubtypeTemplateCompilerConfig.REQUIRED.equals(st.carries.get(v.getName())) ? "required" : "optional"));
            }
            pastClass.FIELDS(FIELD(otherKeysField(e.getKey()), MAP_STRING_OBJECT).MODIFIERS(Modifier.PUBLIC)
                    .COMMENT("The other keys of $N: its members that no value of this subtype declares (the carrier is open).\n", e.getKey())
                    .INITIALIZER(METHOD_CALL(JSON_TEXT, "newObject", List.of())));
        }

        pastClass.METHOD(toGeneral(st, general, schema, ordinary, carried, where));
        pastClass.METHOD(fromGeneral(st, typed, general, schema, ordinary, carried, where));
        generated.add(pastClass);

        String packge = typed.packge;
        // Rust: the typed fields and no conversions (R4): the registry maps no static call to the runtime class
        Class rustClass = withoutMethods(pastClass);
        String directory = locations.convertToDirectory(packge);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, packge, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, configs, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, packge, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(rustClass, packge, locations, stackTraceElement);
        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);
    }

    /**
     * The class as Rust gets it (R4): the typed fields and the subtype's name, and no conversion. Neither is the constant
     * {@code refines} (Rust has no {@code String} constant) nor the maps of other keys (their {@code Object} values would
     * take the derives of the struct away), which only the conversions use.
     */
    private Class withoutMethods(Class c) {
        return withoutMethods(c, "The conversions $N and $N, the constant $N and the maps of other keys are not in Rust: its registry maps no static call to the JSON text runtime (T36, R4).\n",
                "toGeneral", "fromGeneral", REFINES);
    }

    /** The class with its fields and no method, and the given comment saying why. */
    private Class withoutMethods(Class c, String why, Object... arguments) {
        Class r = compilerUtil.getPastFactory().CLASS(c.name).MODIFIERS(c.modifiers.toArray(new Modifier[0]));
        r.comments.addAll(c.comments);
        r.COMMENT(why, arguments);
        for (Field f : c.fields) {
            if (f.modifiers.contains(Modifier.STATIC) || f.name.endsWith(OTHER_SUFFIX)) continue;
            Field g = FIELD(f.name, f.type).MODIFIERS(f.modifiers.toArray(new Modifier[0]));
            g.comments.addAll(f.comments);
            if (f.initialiser != null) g.INITIALIZER(f.initialiser);
            r.FIELDS(g);
        }
        return r;
    }

    // ---- toGeneral -----------------------------------------------------------------------------------------------------

    private Method toGeneral(SubtypeTemplateCompilerConfig st, ClassName general, TemplateBindingsSchema schema, List<String> ordinary,
                             Map<String, List<InJsonValue>> carried, String where) {
        Method m = METHOD("toGeneral").MODIFIERS(Modifier.PUBLIC).RETURNS(general)
                .COMMENT("Converts to the input bean of $N, writing each carrier's JSON text.\n", st.refines)
                .commentFileLocation();
        m.BODY(DEFINITION(general, VARIABLE(GENERAL), CONSTRUCTOR_CALL(general, List.of())));

        for (String key : ordinary) {
            m.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(GENERAL), key), VARIABLE(key, org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE)));
        }
        for (Map.Entry<String, Object> f : st.fixed.entrySet()) {
            m.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(GENERAL), f.getKey()), constant(schema, f.getKey(), f.getValue())));
        }
        for (List<InJsonValue> values : carried.values()) {
            for (InJsonValue v : values) {
                if (SubtypeTemplateCompilerConfig.REQUIRED.equals(st.carries.get(v.getName()))) m.BODY(requiredCheck(VARIABLE(v.getName(), org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE), v, where));
            }
        }
        for (Map.Entry<String, List<InJsonValue>> e : carried.entrySet()) {
            String carrier = e.getKey();
            String map = Constants.GENERATED_VAR_PREFIX + "map_" + carrier;
            m.BODY(DEFINITION(MAP_STRING_OBJECT, VARIABLE(map), METHOD_CALL(JSON_TEXT, "newObject", List.of())));
            for (InJsonValue v : e.getValue()) {
                Expression value = VARIABLE(v.getName(), org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE);
                if ("json".equals(v.getType())) value = METHOD_CALL(JSON_TEXT, "parse", List.of(value));
                m.BODY(IF(BINARY_OP(VARIABLE(v.getName(), org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE), "!=", Constant.getNull()),
                        List.of(METHOD_CALL(VARIABLE(map), "put", List.of(CONSTANT(v.getKey()), value))), List.of()));
            }
            m.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(GENERAL), carrier),
                    METHOD_CALL(JSON_TEXT, "write", List.of(
                            METHOD_CALL(JSON_TEXT, "merge", List.of(VARIABLE(map),
                                    VARIABLE(otherKeysField(carrier), org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE),
                                    CONSTANT(where + ", carrier " + carrier)))))));
        }
        m.BODY(RETURN(VARIABLE(GENERAL)));
        return m;
    }

    // ---- fromGeneral -----------------------------------------------------------------------------------------------------

    private Method fromGeneral(SubtypeTemplateCompilerConfig st, ClassName typed, ClassName general, TemplateBindingsSchema schema, List<String> ordinary,
                               Map<String, List<InJsonValue>> carried, String where) {
        Method m = METHOD("fromGeneral").MODIFIERS(Modifier.PUBLIC, Modifier.STATIC).RETURNS(typed).PARAMETER(general, GENERAL)
                .COMMENT("Converts an input bean of $N to this typed bean; refused when a fixed value of the subtype is not the bean's.\n", st.refines)
                .commentFileLocation();

        for (Map.Entry<String, Object> f : st.fixed.entrySet()) {
            m.BODY(IF(BINARY_OP(METHOD_CALL(JSON_TEXT, "same", List.of(METHOD_CALL(VARIABLE(GENERAL), f.getKey()), constant(schema, f.getKey(), f.getValue()))),
                            "==", CONSTANT(false)),
                    List.of(refusal(where + ": the bean's " + f.getKey() + " is not the subtype's " + f.getValue())), List.of()));
        }
        m.BODY(DEFINITION(typed, VARIABLE(TYPED), CONSTRUCTOR_CALL(typed, List.of())));
        for (String key : ordinary) {
            m.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(TYPED), key), METHOD_CALL(VARIABLE(GENERAL), key)));
        }
        for (Map.Entry<String, List<InJsonValue>> e : carried.entrySet()) {
            String carrier = e.getKey();
            String map = Constants.GENERATED_VAR_PREFIX + "map_" + carrier;
            m.BODY(DEFINITION(MAP_STRING_OBJECT, VARIABLE(map), METHOD_CALL(JSON_TEXT, "read", List.of(METHOD_CALL(VARIABLE(GENERAL), carrier)))));
            List<Expression> declaredKeys = new ArrayList<>();
            for (InJsonValue v : e.getValue()) {
                m.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(TYPED), v.getName()), METHOD_CALL(JSON_TEXT, getter(v.getType()), List.of(VARIABLE(map), CONSTANT(v.getKey())))));
                declaredKeys.add(CONSTANT(v.getKey()));
            }
            m.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(TYPED), otherKeysField(carrier)),
                    METHOD_CALL(JSON_TEXT, "others", List.of(VARIABLE(map), ARRAY_INITIALISER(STRING, declaredKeys)))));
        }
        for (List<InJsonValue> values : carried.values()) {
            for (InJsonValue v : values) {
                if (SubtypeTemplateCompilerConfig.REQUIRED.equals(st.carries.get(v.getName()))) m.BODY(requiredCheck(METHOD_CALL(VARIABLE(TYPED), v.getName()), v, where));
            }
        }
        m.BODY(RETURN(VARIABLE(TYPED)));
        return m;
    }

    // ---- pieces ------------------------------------------------------------------------------------------------------------

    private Statement requiredCheck(Expression value, InJsonValue v, String where) {
        return IF(BINARY_OP(value, "==", Constant.getNull()),
                List.of(refusal(where + ": the required value " + v.getName() + " is absent")), List.of());
    }

    private Statement refusal(String message) {
        return THROW(CONSTRUCTOR_CALL(ILLEGAL_ARGUMENT_EXCEPTION, List.of(CONSTANT("T36: " + message))));
    }

    static String getter(String type) {
        switch (type) {
            case "xsd:int": return "getInteger";
            case "xsd:long": return "getLong";
            case "xsd:double": return "getDouble";
            case "xsd:float": return "getFloat";
            case "xsd:boolean": return "getBoolean";
            case "json": return "getJson";
            default: return "getString";   // xsd:string, xsd:date, xsd:dateTime
        }
    }

    /** A fixed constant, of the PAST type of the variable's declared type. */
    Expression constant(TemplateBindingsSchema schema, String variable, Object value) {
        String type = descriptorUtils.getFromDescriptor(schema.getVar().get(variable).get(0), AttributeDescriptor::getType, nd -> null);
        if (type == null) return CONSTANT((String) value);   // a node: its local part, as text
        switch (type) {
            case "xsd:int": return CONSTANT(((Number) value).intValue());
            case "xsd:long": return CONSTANT(((Number) value).longValue());
            case "xsd:float": return CONSTANT(((Number) value).floatValue());
            case "xsd:double": return CONSTANT(((Number) value).doubleValue());
            case "xsd:boolean": return CONSTANT((Boolean) value);
            default: return CONSTANT((String) value);
        }
    }

    // ---- the request and the reply (part D, §12) -------------------------------------------------------------------------

    public static final String REQUEST_SUFFIX = "Request";
    public static final String REPLY_SUFFIX = "Reply";

    /** A value of a typed bean, with the performative it was declared with ({@code request}, {@code reply} or null: the service's own). */
    static final class Value {
        final String name;
        final TypeName type;
        final String documentation;
        final String performative;
        /** For an in-JSON value, its carrier and key; null for an ordinary value. */
        final String carrier;
        final String key;

        Value(String name, TypeName type, String documentation, String performative, String carrier, String key) {
            this.name = name;
            this.type = type;
            this.documentation = documentation;
            this.performative = performative;
            this.carrier = carrier;
            this.key = key;
        }
    }

    /**
     * The values of a subtype's typed bean, in the order of its fields: the ordinary inputs it keeps, then the in-JSON values
     * it carries, with the performative of each (design §12.4: a value is the subtype's when it is an ordinary input the
     * subtype does not fix, or an in-JSON value it carries).
     */
    List<Value> values(TemplateBindingsSchema schema, SubtypeTemplateCompilerConfig st) {
        Map<String, List<Descriptor>> var = schema.getVar();
        List<Value> res = new ArrayList<>();
        for (String key : ordinaryInputs(schema, st)) {
            Descriptor d = var.get(key).get(0);
            String doc = descriptorUtils.getFromDescriptor(d, AttributeDescriptor::getDocumentation, NameDescriptor::getDocumentation);
            String performative = descriptorUtils.getFromDescriptor(d, AttributeDescriptor::getPerformative, NameDescriptor::getPerformative);
            res.add(new Value(key, compilerUtil.getPastTypeForDeclaredType(var, key), doc, performative, null, null));
        }
        for (Map.Entry<String, List<InJsonValue>> e : carried(schema, st).entrySet()) {
            for (InJsonValue v : e.getValue()) {
                res.add(new Value(v.getName(), compilerUtil.getPastClassForType(v.getType()), v.getDocumentation(), v.getPerformative(), e.getKey(), v.getKey()));
            }
        }
        return res;
    }

    /**
     * The names of a subtype's values by whose content they are: {@code request}, {@code reply}, and {@code service} for the rest
     * (no performative), each in the order of the typed bean's fields. Groups with no value are not in the map.
     */
    static Map<String, List<String>> messageGroups(TemplateBindingsSchema schema, SubtypeTemplateCompilerConfig st) {
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (String g : List.of(PerformativeChecker.REQUEST, PerformativeChecker.REPLY, "service")) groups.put(g, new ArrayList<>());
        for (String key : ordinaryInputs(schema, st)) {
            String p = descriptorUtils.getFromDescriptor(schema.getVar().get(key).get(0), AttributeDescriptor::getPerformative, NameDescriptor::getPerformative);
            groups.get(p == null ? "service" : p).add(key);
        }
        for (List<InJsonValue> values : carried(schema, st).values()) {
            for (InJsonValue v : values) groups.get(v.getPerformative() == null ? "service" : v.getPerformative()).add(v.getName());
        }
        groups.values().removeIf(List::isEmpty);
        return groups;
    }

    /**
     * The carriers whose other-keys map the request class keeps (R13, ruled): those the subtype carries values in, of which
     * every value the <em>template</em> declares is a request value. A carrier that also holds a reply value, or a value of
     * the service's own that this subtype does not carry, gets none: a key under such a value's name would let the caller
     * set it.
     */
    static List<String> requestCarriers(TemplateBindingsSchema schema, SubtypeTemplateCompilerConfig st) {
        List<String> res = new ArrayList<>();
        for (String carrier : carried(schema, st).keySet()) {
            boolean allRequest = true;
            for (InJsonValue v : schema.getInJsonOf(carrier)) {
                if (!PerformativeChecker.REQUEST.equals(v.getPerformative())) allRequest = false;
            }
            if (allRequest) res.add(carrier);
        }
        return res;
    }

    /** The request and reply classes of every subtype that has a request or a reply value; empty when no value has a performative. */
    public List<SpecificationFile> generateRequestReplyClasses(TemplatesProjectConfiguration configs, Locations locations) {
        List<SpecificationFile> res = new ArrayList<>();
        for (SubtypeTemplateCompilerConfig st : subtypes(configs)) {
            SimpleTemplateCompilerConfig simple = refined(configs, st);
            TemplateBindingsSchema schema = compilerUtil.getBindingsSchema(simple);
            List<Value> values = values(schema, st);
            for (String performative : List.of(PerformativeChecker.REQUEST, PerformativeChecker.REPLY)) {
                List<Value> mine = new ArrayList<>();
                for (Value v : values) if (performative.equals(v.performative)) mine.add(v);
                List<String> others = PerformativeChecker.REQUEST.equals(performative) ? requestCarriers(schema, st) : List.of();
                if (mine.isEmpty()) continue;
                res.add(generateRequestOrReply(configs, locations, st, simple, performative, mine, others));
            }
        }
        return res;
    }

    /** {@code <Name>Request} or {@code <Name>Reply}, in the package of the subtype's typed bean. */
    ClassName messageType(Locations locations, SimpleTemplateCompilerConfig simple, SubtypeTemplateCompilerConfig st, String performative) {
        String suffix = PerformativeChecker.REQUEST.equals(performative) ? REQUEST_SUFFIX : REPLY_SUFFIX;
        return get(compilerUtil.capitalize(st.name) + suffix, locations.getBeansPackage(st.fullyQualifiedName, BeanDirection.INPUTS));
    }

    private SpecificationFile generateRequestOrReply(TemplatesProjectConfiguration configs, Locations locations, SubtypeTemplateCompilerConfig st,
                                                     SimpleTemplateCompilerConfig simple, String performative, List<Value> values, List<String> otherCarriers) {
        found = true;
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();
        ClassName typed = typedBeanType(locations, simple, st);
        ClassName self = messageType(locations, simple, st, performative);
        boolean request = PerformativeChecker.REQUEST.equals(performative);

        Class pastClass = compilerUtil.getPastFactory().CLASS(self.simpleName).MODIFIERS(Modifier.PUBLIC)
                .COMMENT("The $N of the subtype $N: only the values that are content of the $N (declared with @performative \"$N\").\n",
                        request ? "request" : "reply", st.fullyQualifiedName, request ? "request" : "reply", performative)
                .COMMENT("It is copied into the typed bean $N with $N, and built from one with $N; it has no field for any other value.\n",
                        typed.simpleName, "into", "of");
        for (Value v : values) {
            pastClass.FIELDS(FIELD(v.name, v.type).MODIFIERS(Modifier.PUBLIC)
                    .COMMENT("$N: $L\n", v.name, v.documentation == null ? CompilerBeanGenerator.JAVADOC_NO_DOCUMENTATION : v.documentation));
        }
        for (String carrier : otherCarriers) {
            pastClass.FIELDS(FIELD(otherKeysField(carrier), MAP_STRING_OBJECT).MODIFIERS(Modifier.PUBLIC)
                    .COMMENT("The other keys of $N: its members that no value of the subtype declares. Every value the template declares in $N is of the request, so the caller may add keys (the carrier is open).\n", carrier, carrier));
        }

        Method into = METHOD("into").MODIFIERS(Modifier.PUBLIC).RETURNS(typed).PARAMETER(typed, TYPED)
                .COMMENT("Copies these fields into the typed bean and returns it. No other field of the bean is touched.\n")
                .commentFileLocation();
        for (Value v : values) {
            into.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(TYPED), v.name), VARIABLE(v.name, org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE)));
        }
        for (String carrier : otherCarriers) {
            Expression field = VARIABLE(otherKeysField(carrier), org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE);
            // an absent map leaves the typed bean's own (empty) one
            into.BODY(IF(BINARY_OP(field, "!=", Constant.getNull()),
                    List.of(ASSIGNMENT(METHOD_CALL(VARIABLE(TYPED), otherKeysField(carrier)), field)), List.of()));
        }
        into.BODY(RETURN(VARIABLE(TYPED)));

        String local = Constants.GENERATED_VAR_PREFIX + (request ? "request" : "reply");
        Method of = METHOD("of").MODIFIERS(Modifier.PUBLIC, Modifier.STATIC).RETURNS(self).PARAMETER(typed, TYPED)
                .COMMENT("The $N of a typed bean: the values of its fields that are content of the $N.\n", request ? "request" : "reply", request ? "request" : "reply")
                .commentFileLocation();
        of.BODY(DEFINITION(self, VARIABLE(local), CONSTRUCTOR_CALL(self, List.of())));
        for (Value v : values) {
            of.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(local), v.name), METHOD_CALL(VARIABLE(TYPED), v.name)));
        }
        for (String carrier : otherCarriers) {
            of.BODY(ASSIGNMENT(METHOD_CALL(VARIABLE(local), otherKeysField(carrier)), METHOD_CALL(VARIABLE(TYPED), otherKeysField(carrier))));
        }
        of.BODY(RETURN(VARIABLE(local)));
        pastClass.METHOD(into);
        pastClass.METHOD(of);
        generated.add(pastClass);

        String packge = self.packge;
        String directory = locations.convertToDirectory(packge);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, packge, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, configs, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, packge, locations, stackTraceElement);
        // Rust: the fields only. The Rust emitter writes the assignment of one bean's field to another's as Some(x.to_string()) or Some(x),
        // which does not compile when the field is an Option, as every field of a bean is (checked with cargo on the generated file), and it is not extended here.
        Class rustClass = withoutMethods(pastClass, "The methods $N and $N are not in Rust: its emitter does not write the copy of one bean's field into another's correctly (T36 part D). The map of other keys is not either.\n", "into", "of");
        Supplier<Boolean> rustGenerator = () -> generateRust(rustClass, packge, locations, stackTraceElement);
        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, rustGenerator);
    }

    // ---- the dispatcher (R9) ---------------------------------------------------------------------------------------------

    /**
     * One class for the catalogue, {@code Subtypes}, with for each general template that has subtypes two static methods:
     * {@code which<Template>(general)}, the fully qualified name of the subtype whose fixed values the bean holds (null when
     * none), and {@code fromGeneral<Template>(general)}, its typed bean (an {@code Object}: typed beans share no type). The
     * subtype with the most fixed values is tried first. Null for a catalogue with no subtype.
     */
    public SpecificationFile generateDispatcher(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        List<SubtypeTemplateCompilerConfig> all = subtypes(configs);
        if (all.isEmpty()) return null;
        found = true;
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        Class pastClass = compilerUtil.getPastFactory().CLASS(fileName).MODIFIERS(Modifier.PUBLIC)
                .COMMENT("T36 (R9): from the input bean of a template to the typed bean of its subtype, chosen by the values the bean fixes.\n");

        Map<String, List<SubtypeTemplateCompilerConfig>> byTemplate = new LinkedHashMap<>();
        for (SubtypeTemplateCompilerConfig st : all) byTemplate.computeIfAbsent(st.refines, k -> new ArrayList<>()).add(st);

        for (Map.Entry<String, List<SubtypeTemplateCompilerConfig>> e : byTemplate.entrySet()) {
            SimpleTemplateCompilerConfig simple = refined(configs, e.getValue().get(0));
            TemplateBindingsSchema schema = compilerUtil.getBindingsSchema(simple);
            ClassName general = generalBeanType(locations, simple);
            List<SubtypeTemplateCompilerConfig> ordered = new ArrayList<>(e.getValue());
            ordered.sort(Comparator.comparingInt((SubtypeTemplateCompilerConfig s) -> -s.fixed.size()));   // stable: catalogue order on a tie
            String suffix = compilerUtil.capitalize(simple.name);

            Method which = METHOD("which" + suffix).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC).RETURNS(STRING).PARAMETER(general, GENERAL)
                    .COMMENT("The subtype of $N whose fixed values this bean holds, or null.\n", simple.fullyQualifiedName).commentFileLocation();
            Method from = METHOD("fromGeneral" + suffix).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC).RETURNS(OBJECT).PARAMETER(general, GENERAL)
                    .COMMENT("The typed bean of the subtype of $N whose fixed values this bean holds; refused when there is none.\n", simple.fullyQualifiedName).commentFileLocation();
            boolean unconditional = false;
            for (SubtypeTemplateCompilerConfig st : ordered) {
                ClassName typed = typedBeanType(locations, simple, st);
                Expression cond = null;
                for (Map.Entry<String, Object> f : st.fixed.entrySet()) {
                    Expression one = METHOD_CALL(JSON_TEXT, "same", List.of(METHOD_CALL(VARIABLE(GENERAL), f.getKey()), constant(schema, f.getKey(), f.getValue())));
                    cond = (cond == null) ? one : BINARY_OP(cond, "&&", one);
                }
                if (cond == null) {   // fixes nothing: it matches whatever the earlier ones did not, and ends the chain
                    which.BODY(RETURN(CONSTANT(st.fullyQualifiedName)));
                    from.BODY(RETURN(METHOD_CALL(typed, "fromGeneral", List.of(VARIABLE(GENERAL)))));
                    unconditional = true;
                    break;
                }
                which.BODY(IF(cond, List.of(RETURN(CONSTANT(st.fullyQualifiedName))), List.of()));
                from.BODY(IF(cond, List.of(RETURN(METHOD_CALL(typed, "fromGeneral", List.of(VARIABLE(GENERAL))))), List.of()));
            }
            if (!unconditional) {
                which.BODY(RETURN(Constant.getNull()));
                from.BODY(refusal("no subtype of " + simple.fullyQualifiedName + " fixes the values of this bean"));
            }
            pastClass.METHOD(which);
            pastClass.METHOD(from);
        }

        generated.add(pastClass);
        String packge = locations.getFilePackage(configs.name, fileName);
        String directory = locations.convertToDirectory(packge);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, packge, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, configs, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, packge, locations, stackTraceElement);
        // Rust (R4): every method calls a conversion, and the conversions are not in Rust
        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, emptyGenerator);
    }
}
