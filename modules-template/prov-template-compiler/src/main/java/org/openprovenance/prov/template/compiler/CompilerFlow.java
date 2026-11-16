package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Comment;
import org.openprovenance.prov.template.compiler.past.Constant;
import org.openprovenance.prov.template.compiler.past.Expression;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.Statement;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.ArrayInitialiser.ARRAY_INITIALISER;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.ThrowStatement.THROW;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

/**
 * T36 part E (design §13): the flow of a {@code flow} entry, as PAST, so the existing emitters write it in Java, Python and
 * JavaScript (not Rust: E6). One class with the methods {@code ask}, {@code record}, {@code answer}, {@code refused} and
 * {@code failed}, one assignment per origin each preceded by a comment naming its kind, and one interface of rules, a method per
 * supplied value, which the application implements. Everything it says it reads from {@link FlowModel}, the list the documentation
 * and the Turtle read too.
 */
public class CompilerFlow {

    static final ClassName JSON_TEXT = CompilerSubtype.JSON_TEXT;
    static final String ASKED = "asked", REQUEST = "request", RULES = "rules", RECORDED = "recorded", MADE = "made";
    static final String MADE_SUFFIX = "Made";

    private final CompilerUtil compilerUtil;
    private final CompilerSubtype compilerSubtype;
    private final List<Class> generated = new ArrayList<>();
    private final List<FlowModel> models = new ArrayList<>();

    public CompilerFlow(ProvFactory pFactory, CompilerSubtype compilerSubtype) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.compilerSubtype = compilerSubtype;
    }

    /** The PAST classes generated so far (the flows and their rules), for tests of the emitters. */
    public List<Class> getGeneratedClasses() {
        return generated;
    }

    /** The flows read and checked so far. */
    public List<FlowModel> getModels() {
        return models;
    }

    /**
     * Reads and checks every flow of the catalogue, and keeps what it read for {@link #generateFlows}; throws {@link FlowException} for the first
     * that is ill-declared. Called with the subtypes' check, before anything is generated.
     */
    public List<FlowModel> check(TemplatesProjectConfiguration configs, String cbindingsBaseDir) {
        models.clear();
        Set<String> names = new HashSet<>();
        for (FlowTemplateCompilerConfig f : FlowModel.flows(configs)) {
            FlowModel m = FlowModel.resolve(configs, f, compilerUtil, compilerSubtype, cbindingsBaseDir);
            if (!names.add(m.className())) throw new FlowException("flow " + f.fullyQualifiedName + ": its class name " + m.className() + " is used by another flow");
            models.add(m);
        }
        return models;
    }

    /** The files of every flow checked: the class and, when it has rules, their interface; empty for a catalogue without a flow. */
    public List<SpecificationFile> generateFlows(TemplatesProjectConfiguration configs, Locations locations) {
        List<SpecificationFile> res = new ArrayList<>();
        for (FlowModel m : models) {
            if (!m.rules.isEmpty()) res.add(generateRules(configs, locations, m));
            res.add(generateFlow(configs, locations, m));
        }
        return res;
    }

    // ---- types ---------------------------------------------------------------------------------------------------------------

    private ClassName userBean(Locations l, FlowModel m) {
        return compilerSubtype.typedBeanType(l, m.userTemplate, m.from);
    }

    private ClassName request(Locations l, FlowModel m) {
        return compilerSubtype.messageType(l, m.userTemplate, m.from, "request");
    }

    private ClassName reply(Locations l, FlowModel m) {
        return compilerSubtype.messageType(l, m.userTemplate, m.from, "reply");
    }

    /** The typed bean of a backend subtype of a template. */
    private ClassName backendBean(Locations l, SimpleTemplateCompilerConfig template, SubtypeTemplateCompilerConfig st) {
        return compilerSubtype.typedBeanType(l, template, st);
    }

    /** The outputs bean of a template: what the store made of a record of it. */
    private ClassName outputsBean(Locations l, SimpleTemplateCompilerConfig template) {
        return get(compilerUtil.beanNameClass(template.name, BeanDirection.OUTPUTS), l.getBeansPackage(template.fullyQualifiedName, BeanDirection.OUTPUTS));
    }

    /** The inputs of a stood thing's previous record: the typed bean of its subtype, or the general input bean of its template (T41, G2). */
    private ClassName stoodInputs(Locations l, FlowModel m, FlowModel.Stood s) {
        return s.record != null ? backendBean(l, s.template, s.record) : compilerSubtype.generalBeanType(l, s.template);
    }

    private ClassName flowClass(FlowModel m) {
        return get(m.className(), m.flow.package_);
    }

    private ClassName rulesClass(FlowModel m) {
        return get(m.className() + "Rules", m.flow.package_);
    }

    // ---- the rules -----------------------------------------------------------------------------------------------------------

    private SpecificationFile generateRules(TemplatesProjectConfiguration configs, Locations locations, FlowModel m) {
        StackTraceElement where = compilerUtil.thisMethodAndLine();
        ClassName rules = rulesClass(m);
        Class intfce = compilerUtil.getPastFactory().INTERFACE(rules.simpleName).MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT);
        intfce.COMMENT("The rules of the flow $N: one method for each value the application computes, with the beans in scope in the step that uses it.\n", m.flow.fullyQualifiedName);
        intfce.COMMENT("A rule is the application's: the flow copies nothing for these values.\n");
        for (FlowModel.Rule r : m.rules.values()) {
            Method method = METHOD(r.name).MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT).RETURNS(r.returns)
                    .COMMENT("The rule for $N, in the step $N.\n", r.target, r.step);
            for (Map.Entry<String, TypeName> p : ruleParameters(locations, m, r.step).entrySet()) method.PARAMETER(p.getValue(), p.getKey());
            intfce.METHOD(method);
        }
        generated.add(intfce);
        return files(intfce, rules.packge, configs, locations, where);
    }

    /** The beans in scope in a step, in order: what a rule of that step receives. */
    private Map<String, TypeName> ruleParameters(Locations l, FlowModel m, String step) {
        Map<String, TypeName> res = new LinkedHashMap<>();
        res.put(ASKED, userBean(l, m));
        if (step.startsWith("record")) {
            FlowModel.Rec rec = null;
            for (FlowModel.Rec r : m.records) if (r.stepKey().equals(step)) rec = r;
            res.put(RECORDED, backendBean(l, rec.template, rec.to));
            for (FlowModel.Stood s : rec.stood.values()) {
                res.put(s.name, stoodInputs(l, m, s));
                res.put(s.name + MADE_SUFFIX, outputsBean(l, s.template));
            }
            return res;
        }
        switch (step) {
            case "ask":
                for (FlowModel.Stood s : m.stood.values()) if (s.addressed != null) res.put(s.name + MADE_SUFFIX, outputsBean(l, s.template));
                break;
            case "answer":
                for (FlowModel.Rec r : m.records) {
                    res.put(r.recordedVariable(), backendBean(l, r.template, r.to));
                    res.put(r.madeVariable(), outputsBean(l, r.template));
                }
                break;
            default:   // refused, failed: nothing was recorded
                break;
        }
        return res;
    }

    // ---- the flow ------------------------------------------------------------------------------------------------------------

    private SpecificationFile generateFlow(TemplatesProjectConfiguration configs, Locations locations, FlowModel m) {
        StackTraceElement where = compilerUtil.thisMethodAndLine();
        ClassName self = flowClass(m);
        ClassName rules = rulesClass(m);
        Class pastClass = compilerUtil.getPastFactory().CLASS(self.simpleName).MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .COMMENT("The flow $N: the pattern that connects the beans of a request to the beans of the backend's record, from $N to $N.\n", m.flow.fullyQualifiedName, m.from.fullyQualifiedName,
                        m.records.stream().map(r -> r.to.fullyQualifiedName).collect(java.util.stream.Collectors.joining(", ")))
                .COMMENT("The request becomes the user bean ($N), the user bean and what stood become the backend's bean ($N), and what the store made becomes the reply ($N).\n", "ask",
                        m.records.stream().map(FlowModel.Rec::methodName).collect(java.util.stream.Collectors.joining(", ")), "answer")
                .COMMENT("Every input of those beans has one stated origin, said beside it: AS ASKED, CHAINED, CARRIED OVER, CONSTANT, SUPPLIED or AS MADE; the values the application computes are the rules of $N.\n", rules.simpleName)
                .COMMENT("Generated from the catalogue entry: the application writes the rules and nothing else.\n");

        pastClass.METHOD(ask(locations, m));
        for (FlowModel.Rec rec : m.records) pastClass.METHOD(record(locations, m, rec));
        if (m.steps.containsKey("answer")) {
            for (String block : List.of("answer", "refused", "failed")) if (m.steps.containsKey(block)) pastClass.METHOD(replyMethod(locations, m, block));
        }
        generated.add(pastClass);
        return files(pastClass, self.packge, configs, locations, where);
    }

    private SpecificationFile files(Class pastClass, String packge, TemplatesProjectConfiguration configs, Locations locations, StackTraceElement where) {
        String directory = locations.convertToDirectory(packge);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, packge, locations, where);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, configs, directory, where, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, packge, locations, where);
        // Rust (E6): none. The flow calls the runtime class, for which the Rust emitter has no mapping (R4)
        return new SpecificationFile(javaGenerator, pythonGenerator, jsGenerator, emptyGenerator);
    }

    private static Expression field(String variable, String name) {
        return METHOD_CALL(VARIABLE(variable), name);
    }

    private static Statement comment(FlowModel.Item i) {
        String label = i.kind.label;
        StringBuilder pad = new StringBuilder(label);
        while (pad.length() < 13) pad.append(' ');
        return new Comment((pad + " " + i.target + ": " + i.describe()).replace("$", ""));
    }

    private static Statement refusal(String message) {
        return THROW(CONSTRUCTOR_CALL(ILLEGAL_ARGUMENT_EXCEPTION, List.of(CONSTANT("T36: " + message))));
    }

    /** The constant of a field, of the PAST type the field has. */
    private static Expression constant(FlowModel.Field f, Object value) {
        switch (f.valueType) {
            case "xsd:int": return CONSTANT(((Number) value).intValue());
            case "xsd:long": return CONSTANT(((Number) value).longValue());
            case "xsd:float": return CONSTANT(((Number) value).floatValue());
            case "xsd:double": return CONSTANT(((Number) value).doubleValue());
            case "xsd:boolean": return CONSTANT((Boolean) value);
            default: return CONSTANT((String) value);
        }
    }

    private static Expression text(Expression e, String as) {
        return as == null ? e : METHOD_CALL(JSON_TEXT, "text", List.of(e));
    }

    /** The call of a rule, with the beans the step has in scope. */
    private Expression ruleCall(Locations l, FlowModel m, FlowModel.Item i) {
        List<Expression> args = new ArrayList<>();
        for (String p : ruleParameters(l, m, i.step).keySet()) args.add(VARIABLE(p));
        return METHOD_CALL(VARIABLE(RULES), i.rule, args);
    }

    // ---- ask -----------------------------------------------------------------------------------------------------------------

    private Method ask(Locations l, FlowModel m) {
        List<FlowModel.Item> items = m.steps.get("ask");
        boolean rules = items.stream().anyMatch(i -> i.kind == FlowModel.Kind.SUPPLIED);
        ClassName user = userBean(l, m);
        Method method = METHOD("ask").MODIFIERS(Modifier.PUBLIC, Modifier.STATIC).RETURNS(user)
                .COMMENT("1. The request becomes the user bean: what the person asked is copied, the rest is given here.\n")
                .PARAMETER(request(l, m), REQUEST);
        if (rules) method.PARAMETER(rulesClass(m), RULES);
        for (FlowModel.Stood s : m.stood.values()) if (s.addressed != null) method.PARAMETER(outputsBean(l, s.template), s.name + MADE_SUFFIX);
        method.commentFileLocation();

        List<String> asked = new ArrayList<>();
        for (FlowModel.Item i : items) if (i.kind == FlowModel.Kind.AS_ASKED) asked.add(i.target);
        method.BODY(new Comment(("AS ASKED      " + String.join(", ", asked) + ": the request's values").replace("$", "")));
        method.BODY(DEFINITION(user, VARIABLE(ASKED), METHOD_CALL(VARIABLE(REQUEST), "into", List.of(CONSTRUCTOR_CALL(user, List.of())))));
        for (FlowModel.Kind kind : List.of(FlowModel.Kind.CHAINED, FlowModel.Kind.CONSTANT, FlowModel.Kind.ABSENT, FlowModel.Kind.SUPPLIED)) {
            for (FlowModel.Item i : items) {
                if (i.kind != kind) continue;
                method.BODY(comment(i));
                Expression target = field(ASKED, i.target);
                switch (kind) {
                    case CHAINED: {
                        String made = i.thing + MADE_SUFFIX;
                        method.BODY(IF(BINARY_OP(VARIABLE(made), "!=", Constant.getNull()),
                                List.of(ASSIGNMENT(target, field(made, i.madeBy))), List.of()));
                        break;
                    }
                    case CONSTANT:
                        method.BODY(ASSIGNMENT(target, constant(i.field, i.constant)));
                        break;
                    case ABSENT:
                        break;   // said, and nothing assigned
                    default:
                        method.BODY(ASSIGNMENT(target, ruleCall(l, m, i)));
                }
            }
        }
        method.BODY(RETURN(VARIABLE(ASKED)));
        return method;
    }

    // ---- record --------------------------------------------------------------------------------------------------------------

    private Method record(Locations l, FlowModel m, FlowModel.Rec rec) {
        List<FlowModel.Item> items = m.steps.get(rec.stepKey());
        boolean rules = items.stream().anyMatch(i -> i.kind == FlowModel.Kind.SUPPLIED);
        ClassName backend = backendBean(l, rec.template, rec.to);
        Method method = METHOD(rec.methodName()).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC).RETURNS(backend)
                .COMMENT("2. The user bean and what stood become the backend's bean. Each stood thing is the inputs and the outputs of the record that made it;\n")
                .COMMENT("a previous record that does not match the request is refused before anything is built.\n")
                .PARAMETER(userBean(l, m), ASKED);
        for (FlowModel.Stood s : rec.stood.values()) {
            method.PARAMETER(stoodInputs(l, m, s), s.name);
            method.PARAMETER(outputsBean(l, s.template), s.name + MADE_SUFFIX);
        }
        if (rules) method.PARAMETER(rulesClass(m), RULES);
        method.commentFileLocation();

        // the guards
        String flow = m.flow.fullyQualifiedName;
        for (FlowModel.Stood s : rec.stood.values()) {
            if (s.identityUser != null) {
                FlowModel.Field u = m.userFields.get(s.identityUser), b = s.fields.get(s.identityBackend);
                Expression user = field(ASKED, s.identityUser);
                if (!String.valueOf(u.past).equals(String.valueOf(b.past))) user = METHOD_CALL(JSON_TEXT, "text", List.of(user));
                method.BODY(new Comment(("GUARD         the previous record of the " + s.name + " is of the " + s.identityUser + " the request names").replace("$", "")));
                method.BODY(IF(BINARY_OP(METHOD_CALL(JSON_TEXT, "same", List.of(field(s.name, s.identityBackend), user)), "==", CONSTANT(false)),
                        List.of(refusal("flow " + flow + ": the previous record of the " + s.name + " is of another " + s.identityBackend + " than the request's " + s.identityUser)), List.of()));
            }
            if (s.addressed != null) {
                method.BODY(new Comment(("GUARD         the " + s.name + " the request addresses is the one the previous record made").replace("$", "")));
                method.BODY(IF(BINARY_OP(field(ASKED, s.addressed), "==", Constant.getNull()),
                        List.of(refusal("flow " + flow + ": the request addresses no " + s.name)), List.of()));
                method.BODY(IF(BINARY_OP(METHOD_CALL(JSON_TEXT, "same", List.of(field(ASKED, s.addressed), field(s.name + MADE_SUFFIX, s.madeBy))), "==", CONSTANT(false)),
                        List.of(refusal("flow " + flow + ": the request addresses another " + s.name + " than the one the previous record made")), List.of()));
            }
        }

        method.BODY(DEFINITION(backend, VARIABLE(RECORDED), CONSTRUCTOR_CALL(backend, List.of())));
        for (FlowModel.Item i : items) {
            method.BODY(comment(i));
            Expression target = field(RECORDED, i.target);
            switch (i.kind) {
                case AS_ASKED:
                    method.BODY(ASSIGNMENT(target, text(field(ASKED, i.source), i.as)));
                    break;
                case CHAINED:
                    method.BODY(ASSIGNMENT(target, field(i.thing + MADE_SUFFIX, i.madeBy)));
                    break;
                case CARRIED:
                    if (i.field.isOtherKeys()) {
                        // a copy of the map, made when the previous record has one
                        method.BODY(IF(BINARY_OP(field(i.thing, i.source), "!=", Constant.getNull()),
                                List.of(ASSIGNMENT(target, METHOD_CALL(JSON_TEXT, "others", List.of(field(i.thing, i.source), ARRAY_INITIALISER(STRING, List.of()))))), List.of()));
                    } else {
                        method.BODY(ASSIGNMENT(target, field(i.thing, i.source)));
                    }
                    break;
                case CONSTANT:
                    method.BODY(ASSIGNMENT(target, constant(i.field, i.constant)));
                    break;
                case SUPPLIED:
                    method.BODY(ASSIGNMENT(target, ruleCall(l, m, i)));
                    break;
                default:   // absent: said, and nothing assigned
                    break;
            }
        }
        method.BODY(RETURN(VARIABLE(RECORDED)));
        return method;
    }

    // ---- the reply -----------------------------------------------------------------------------------------------------------

    private Method replyMethod(Locations l, FlowModel m, String block) {
        List<FlowModel.Item> items = m.steps.get(block);
        boolean answer = "answer".equals(block);
        boolean rules = items.stream().anyMatch(i -> i.kind == FlowModel.Kind.SUPPLIED && !i.argument);
        Method method = METHOD(block).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC).RETURNS(reply(l, m))
                .COMMENT(answer ? "3. What the store made becomes the reply.\n"
                        : "refused".equals(block) ? "3'. A request the service refused: nothing was recorded, the reply says why.\n"
                        : "3''. A request that failed in the service for a reason that is not a refusal: nothing was recorded.\n")
                .PARAMETER(userBean(l, m), ASKED);
        if (answer) {
            for (FlowModel.Rec r : m.records) {
                method.PARAMETER(backendBean(l, r.template, r.to), r.recordedVariable());
                method.PARAMETER(outputsBean(l, r.template), r.madeVariable());
            }
        }
        if (rules) method.PARAMETER(rulesClass(m), RULES);
        for (FlowModel.Item i : items) if (i.argument) method.PARAMETER(i.field.past, i.target);
        method.commentFileLocation();

        for (FlowModel.Item i : items) {
            method.BODY(comment(i));
            Expression target = field(ASKED, i.target);
            switch (i.kind) {
                case CONSTANT:
                    method.BODY(ASSIGNMENT(target, constant(i.field, i.constant)));
                    break;
                case SUPPLIED:
                    method.BODY(ASSIGNMENT(target, i.argument ? VARIABLE(i.target) : ruleCall(l, m, i)));
                    break;
                case AS_MADE:
                    if (i.whenMade != null) {
                        method.BODY(IF(BINARY_OP(field(i.rec.madeVariable(), i.whenMade), "==", Constant.getNull()),
                                List.of(ASSIGNMENT(target, constant(i.field, i.otherwise))), List.of(ASSIGNMENT(target, constant(i.field, i.is)))));
                    } else if ("made".equals(i.from)) {
                        method.BODY(ASSIGNMENT(target, field(i.rec.madeVariable(), i.source)));
                    } else {
                        method.BODY(ASSIGNMENT(target, text(field(i.rec.recordedVariable(), i.source), i.as)));
                    }
                    break;
                default:   // absent
                    break;
            }
        }
        method.BODY(RETURN(METHOD_CALL(reply(l, m), "of", VARIABLE(ASKED))));
        return method;
    }
}
