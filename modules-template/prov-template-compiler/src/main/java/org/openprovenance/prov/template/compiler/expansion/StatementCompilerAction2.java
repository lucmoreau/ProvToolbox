package org.openprovenance.prov.template.compiler.expansion;

import java.util.*;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Statement;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;
import org.openprovenance.prov.template.core.InstantiateUtil;

import static org.openprovenance.prov.template.compiler.expansion.StatementTypeAction.bnNS;
import static org.openprovenance.prov.template.compiler.expansion.StatementTypeAction.gensym;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.EQ;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constant.getNull;
import static org.openprovenance.prov.template.compiler.past.IfExpression.IF_;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

/**
 * Variant of {@link StatementCompilerAction} that generates PAST statements
 * (provenance abstract syntax tree) instead of JavaPoet code.
 *
 * <p>Each {@code doAction} method appends PAST {@link Statement} objects to
 * the supplied list, rather than calling {@code builder.addStatement(...)}.
 */
public class StatementCompilerAction2 implements StatementAction {

    private final TemplateBindingsSchema bindingsSchema;
    private final Collection<QualifiedName> allVars;
    private final Collection<QualifiedName> allAtts;
    private final List<Statement> statements;
    private final String target;
    private final ProvFactory pFactory;
    private final Hashtable<QualifiedName, String> vmap;

    public StatementCompilerAction2(ProvFactory pFactory, Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, Hashtable<QualifiedName, String> vmap, List<Statement> statements, String target, TemplateBindingsSchema bindingsSchema) {
        this.pFactory = pFactory;
        this.allVars = allVars;
        this.allAtts = allAtts;
        this.statements = statements;
        this.target = target;
        this.vmap = vmap;
        this.bindingsSchema = bindingsSchema;
    }

    public String local(QualifiedName id) {
        return (id == null) ? "nullqn" : id.getLocalPart();
    }

    public List<String> local(List<QualifiedName> ids) {
        List<String> res = new LinkedList<>();
        for (QualifiedName id : ids) {
            res.add(local(id));
        }
        return res;
    }

    // --- helper: build "target.add(expr)" as a PAST MethodCall statement ---
    private Expression targetAdd(Expression expr) {
        // target is a string like "__C_document.getStatementOrBundle()" or "id_.getStatement()"
        // We model this as a method call chain.
        // Parse the target string to identify the variable and method parts.
        // e.g. "__C_document.getStatementOrBundle()" => variable "__C_document", method "getStatementOrBundle"
        // then call .add(expr) on the result.
        int dotIndex = target.indexOf('.');
        String varName = target.substring(0, dotIndex);
        String methodPart = target.substring(dotIndex + 1);
        // strip trailing "()" if present
        if (methodPart.endsWith("()")) {
            methodPart = methodPart.substring(0, methodPart.length() - 2);
        }
        return new MethodCall(METHOD_CALL(VARIABLE(varName), methodPart, List.of()), "add", List.of(expr));
    }

    // --- helper: build pf.newXXX(...) ---
    private Expression pfCall(String methodName, List<Expression> args) {
        return METHOD_CALL(VARIABLE("pf"), methodName, args);
    }

    // --- helper: build pf.newISOTime(expr.toString()) ---
    private Expression newISOTime(String varName) {
        return pfCall("newISOTime", List.of(
                METHOD_CALL(VARIABLE(varName), "toString", List.of())));
    }

    // --- helper: ternary ($var==null)?null:pf.newISOTime($var.toString()) ---
    private Expression ternaryISOTime(String varName) {
        return IF_(BINARY_OP(VARIABLE(varName), EQ, getNull()))
                .THEN(getNull())
                .ELSE(newISOTime(varName));
    }

    // --- helper: build "if (cond) target.add(pf.newXXX(...))" ---
    private Statement ifThenTargetAdd(Expression condition, Expression pfCallExpr) {
        return IF(condition).THEN(targetAdd(pfCallExpr));
    }

    // --- helper: $N!=null ---
    private Expression notNull(String varName) {
        return BINARY_OP(VARIABLE(varName), "!=", getNull());
    }

    // --- helper: ($N!=null) && ($N!=null) ---
    private Expression andNotNull(String var1, String var2) {
        return BINARY_OP(notNull(var1), "&&", notNull(var2));
    }

    // --- helper to convert a list of variable name strings to VARIABLE expressions ---
    private List<Expression> vars(String... names) {
        List<Expression> result = new ArrayList<>();
        for (String name : names) {
            result.add(VARIABLE(name));
        }
        return result;
    }

    @Override
    public void doAction(Activity s) {
        final String var = s.getId().getLocalPart();
        final String start = doCollectElementVariable(s, InstantiateUtil.STARTTIME_URI);
        final String end = doCollectElementVariable(s, InstantiateUtil.ENDTIME_URI);

        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);
        Expression attrsArg = hasAttrs ? VARIABLE("attrs") : getNull();

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(var));

        if (start == null) {
            args.add(getNull());
        } else {
            args.add(ternaryISOTime(start));
        }

        if (end == null) {
            args.add(getNull());
        } else {
            args.add(ternaryISOTime(end));
        }

        args.add(attrsArg);

        Expression condition;
        if (start != null) {
            condition = notNull(var);
        } else if (end != null) {
            condition = notNull(var);
        } else {
            condition = notNull(var);
        }

        statements.add(ifThenTargetAdd(condition, pfCall("newActivity", args)));
    }

    @Override
    public void doAction(Used s) {
        final String activity = local(s.getActivity());
        final String entity = local(s.getEntity());

        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getId())));
        args.add(VARIABLE(activity));
        args.add(VARIABLE(entity));
        args.add(getNull()); // time
        if (hasAttrs) args.add(VARIABLE("attrs"));

        statements.add(ifThenTargetAdd(
                andNotNull(activity, entity),
                pfCall("newUsed", args)));
    }

    @Override
    public void doAction(WasGeneratedBy s) {
        final String entity = local(s.getEntity());

        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getId())));
        args.add(VARIABLE(entity));
        args.add(VARIABLE(local(s.getActivity())));
        args.add(getNull()); // time
        if (hasAttrs) args.add(VARIABLE("attrs"));

        statements.add(ifThenTargetAdd(
                notNull(entity),
                pfCall("newWasGeneratedBy", args)));
    }

    @Override
    public void doAction(WasInvalidatedBy s) {
        final String entity = local(s.getEntity());
        String ifVarValue = hasIfVarValue(s);

        Expression attrs = doAttributesActionPast(s);
        Expression attrsArg = (attrs != null) ? VARIABLE("attrs") : getNull();

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getId())));
        args.add(VARIABLE(entity));
        args.add(VARIABLE(local(s.getActivity())));
        args.add(getNull()); // time
        args.add(attrsArg);

        Expression condition;
        if (ifVarValue == null) {
            condition = notNull(entity);
        } else {
            condition = new BinaryOp(
                    METHOD_CALL("toBoolean", List.of(VARIABLE(ifVarValue))),
                    "&&",
                    notNull(entity));
        }

        statements.add(ifThenTargetAdd(condition, pfCall("newWasInvalidatedBy", args)));
    }

    @Override
    public void doAction(WasStartedBy s) {
        // TODO
    }

    @Override
    public void doAction(Agent s) {
        final String var = s.getId().getLocalPart();

        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(var));
        if (hasAttrs) args.add(VARIABLE("attrs"));

        statements.add(ifThenTargetAdd(notNull(var), pfCall("newAgent", args)));
    }

    @Override
    public void doAction(AlternateOf s) {
        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getAlternate2())));
        args.add(VARIABLE(local(s.getAlternate1())));
        statements.add(targetAdd(pfCall("newAlternateOf", args)));
    }

    @Override
    public void doAction(WasAssociatedWith s) {
        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getId())));
        args.add(VARIABLE(local(s.getActivity())));
        args.add(VARIABLE(local(s.getAgent())));
        args.add(VARIABLE(local(s.getPlan())));
        if (hasAttrs) args.add(VARIABLE("attrs"));

        statements.add(targetAdd(pfCall("newWasAssociatedWith", args)));
    }

    @Override
    public void doAction(WasAttributedTo s) {
        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getId())));
        args.add(VARIABLE(local(s.getEntity())));
        args.add(VARIABLE(local(s.getAgent())));
        statements.add(targetAdd(pfCall("newWasAttributedTo", args)));
    }

    @Override
    public void doAction(WasInfluencedBy s) {
        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getId())));
        args.add(VARIABLE(local(s.getInfluencee())));
        args.add(VARIABLE(local(s.getInfluencer())));
        statements.add(targetAdd(pfCall("newWasInfluencedBy", args)));
    }

    @Override
    public void doAction(ActedOnBehalfOf s) {
        final String responsible = local(s.getResponsible());
        final String delegate = local(s.getDelegate());

        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getId())));
        args.add(VARIABLE(delegate));
        args.add(VARIABLE(responsible));
        if (s.getActivity() == null) {
            args.add(getNull());
        } else {
            args.add(VARIABLE(local(s.getActivity())));
        }
        if (hasAttrs) args.add(VARIABLE("attrs"));

        statements.add(ifThenTargetAdd(
                andNotNull(delegate, responsible),
                pfCall("newActedOnBehalfOf", args)));
    }

    public String localNotBlank(QualifiedName id) {
        return ((id == null) || (id.getNamespaceURI().equals(bnNS))) ? "nullqn" : id.getLocalPart();
    }

    public boolean nullOrBlank(QualifiedName id) {
        return (id == null) || (id.getNamespaceURI().equals(bnNS));
    }

    @Override
    public void doAction(WasDerivedFrom s) {
        if (s.getId() == null) {
            s.setId(gensym());
        }
        final String generated = local(s.getGeneratedEntity());
        final String used = local(s.getUsedEntity());
        final String act = local(s.getActivity());
        final String generation = local(s.getGeneration());
        final String usage = local(s.getUsage());

        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(localNotBlank(s.getId())));
        args.add(VARIABLE(generated));
        args.add(VARIABLE(used));

        if (s.getActivity() == null) {
            if (!hasAttrs) {
                // simple 3-arg form: pf.newWasDerivedFrom(id, gen, used)
                statements.add(ifThenTargetAdd(
                        andNotNull(generated, used),
                        pfCall("newWasDerivedFrom", args)));
            } else {
                // with attrs but no activity
                args.add(VARIABLE("nullqn")); // activity
                args.add(VARIABLE("nullqn")); // generation
                args.add(VARIABLE("nullqn")); // usage
                args.add(VARIABLE("attrs"));
                statements.add(ifThenTargetAdd(
                        andNotNull(generated, used),
                        pfCall("newWasDerivedFrom", args)));
            }
        } else {
            args.add(VARIABLE(act));
            args.add(VARIABLE(generation));
            args.add(VARIABLE(usage));
            if (hasAttrs) {
                args.add(VARIABLE("attrs"));
            } else {
                args.add(getNull());
            }
            statements.add(ifThenTargetAdd(
                    andNotNull(generated, used),
                    pfCall("newWasDerivedFrom", args)));
        }
    }

    @Override
    public void doAction(DictionaryMembership s) {
        // TODO
    }

    @Override
    public void doAction(DerivedByRemovalFrom s) {
        // TODO
    }

    @Override
    public void doAction(WasEndedBy s) {
        // TODO
    }

    @Override
    public void doAction(Entity s) {
        final String var = s.getId().getLocalPart();

        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(var));
        if (hasAttrs) args.add(VARIABLE("attrs"));

        statements.add(ifThenTargetAdd(notNull(var), pfCall("newEntity", args)));
    }

    @Override
    public void doAction(HadMember s) {
        final String element = local(s.getEntity().get(0));
        final String set = local(s.getCollection());

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(set));
        args.add(VARIABLE(element));

        statements.add(ifThenTargetAdd(
                andNotNull(element, set),
                pfCall("newHadMember", args)));
    }

    @Override
    public void doAction(MentionOf s) {
        // TODO
    }

    @Override
    public void doAction(SpecializationOf s) {
        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(local(s.getSpecificEntity())));
        args.add(VARIABLE(local(s.getGeneralEntity())));
        statements.add(targetAdd(pfCall("newSpecializationOf", args)));
    }

    @Override
    public void doAction(QualifiedSpecializationOf s) {
        final String specific = local(s.getSpecificEntity());
        final String general = local(s.getGeneralEntity());

        Expression attrs = doAttributesActionPast(s);
        boolean hasAttrs = (attrs != null);

        List<Expression> args = new ArrayList<>();
        args.add(VARIABLE(localNotBlank(s.getId())));
        args.add(VARIABLE(specific));
        args.add(VARIABLE(general));
        if (hasAttrs) {
            args.add(VARIABLE("attrs"));
        } else {
            args.add(getNull());
        }

        statements.add(ifThenTargetAdd(
                andNotNull(specific, general),
                pfCall("newQualifiedSpecializationOf", args)));
    }

    @Override
    public void doAction(QualifiedAlternateOf s) {
        // TODO
    }

    @Override
    public void doAction(QualifiedHadMember s) {
        if (s.getId() == null) {
            s.setId(gensym());
        }

        String ifVarValue = hasIfVarValue(s);
        Collection<Attribute> checkAttrs = doCheckAttributesAction(s);
        boolean emptyAttrs = checkAttrs.isEmpty();
        boolean isBlankId = nullOrBlank(s.getId());
        boolean singleEntity = s.getEntity().size() == 1;

        Expression attrs = doAttributesActionPast(s);
        Expression attrsArg = (attrs != null) ? VARIABLE("attrs") : getNull();

        Expression condition;
        if (ifVarValue == null) {
            condition = notNull(local(s.getCollection()));
        } else {
            condition = new BinaryOp(
                    METHOD_CALL("toBoolean", List.of(VARIABLE(ifVarValue))),
                    "&&",
                    notNull(local(s.getCollection())));
        }

        if (emptyAttrs && isBlankId && singleEntity) {
            // simple form: pf.newHadMember(collection, entity)
            List<Expression> args = new ArrayList<>();
            args.add(VARIABLE(local(s.getCollection())));
            args.add(VARIABLE(local(s.getEntity().get(0))));
            statements.add(ifThenTargetAdd(condition, pfCall("newHadMember", args)));
        } else {
            // qualified form: pf.newQualifiedHadMember(id, collection, List.of(entity), attrs)
            List<Expression> args = new ArrayList<>();
            args.add(VARIABLE(localNotBlank(s.getId())));
            args.add(VARIABLE(local(s.getCollection())));
            // List.of(entity)
            args.add(METHOD_CALL(
                    ClassName.get("List", "past.util"),
                    "of",
                    VARIABLE(local(s.getEntity().get(0)))));
            args.add(attrsArg);
            statements.add(ifThenTargetAdd(condition, pfCall("newQualifiedHadMember", args)));
        }
    }

    @Override
    public void doAction(DerivedByInsertionFrom s) {
        // TODO
    }

    @Override
    public void doAction(WasInformedBy s) {
        // TODO
    }

    @Override
    public void doAction(Bundle bun, ProvUtilities provUtilities) {
        final String id = bun.getId().getLocalPart();
        final String id_ = id + "_";

        // Bundle id_ = pf.newNamedBundle(id, pf.newNamespace(), null)
        statements.add(Assignment.ASSIGNMENT(
                PROV_BUNDLE,
                VARIABLE(id_),
                pfCall("newNamedBundle", List.of(
                        VARIABLE(id),
                        METHOD_CALL(VARIABLE("pf"), "newNamespace", List.of()),
                        getNull()))));

        // target.add(id_)
        statements.add(targetAdd(VARIABLE(id_)));

        // Recurse into the bundle with a new target
        String target2 = id_ + ".getStatement()";
        StatementCompilerAction2 action2 = new StatementCompilerAction2(pFactory, allVars, allAtts, vmap, statements, target2, bindingsSchema);

        for (org.openprovenance.prov.model.Statement s : bun.getStatement()) {
            provUtilities.doAction(s, action2);
        }
    }

    // ========================================================================
    // Attribute handling — mirrors StatementCompilerAction but produces PAST
    // ========================================================================

    public String hasIfVarValue(org.openprovenance.prov.model.Statement s) {
        Collection<Attribute> attributes = pFactory.getAttributes(s);
        String ifVarValue = null;
        for (Attribute attribute : attributes) {
            QualifiedName element = attribute.getElementName();
            Object value = attribute.getValue();
            if (reservedIfVar(element)) {
                if (value instanceof QualifiedName) {
                    QualifiedName vq = (QualifiedName) value;
                    if (InstantiateUtil.isVariable(vq)) {
                        ifVarValue = vq.getLocalPart();
                    }
                }
            }
        }
        return ifVarValue;
    }

    /**
     * Generates PAST statements for attribute construction, equivalent to
     * {@link StatementCompilerAction#doAttributesAction}.
     * Returns the collection of attributes (for isEmpty check), or null if empty.
     * Adds assignment and attribute-add statements to the statements list.
     */
    public Expression doAttributesActionPast(org.openprovenance.prov.model.Statement s) {
        Collection<Attribute> attributes = pFactory.getAttributes(s);
        if (attributes.isEmpty()) {
            return null;
        }

        // attrs = new LinkedList<Attribute>()
        statements.add(Assignment.ASSIGNMENT(
                null,
                VARIABLE("attrs"),
                CONSTRUCTOR_CALL(
                        ParameterizedType.get(LINKED_LIST),
                        List.of())));

        for (Attribute attribute : attributes) {
            QualifiedName element = attribute.getElementName();
            QualifiedName typeq = attribute.getType();
            Object value = attribute.getValue();
            if (value instanceof QualifiedName) {
                QualifiedName vq = (QualifiedName) value;
                if (reservedElement(element)) {
                    doReservedAttributeActionPast(element, vq, typeq);
                } else {
                    if (InstantiateUtil.isVariable(vq)) {
                        String localPart = vq.getLocalPart();
                        if (vmap.get(element) != null) {
                            // if (localPart!=null) attrs.add(pf.newAttribute(vmap[element], localPart, vc.getXsdType(localPart)))
                            statements.add(IF(notNull(localPart))
                                            .THEN(METHOD_CALL(VARIABLE("attrs"), "add",
                                                    pfCall("newAttribute", List.of(
                                                            VARIABLE(vmap.get(element)),
                                                            VARIABLE(localPart),
                                                            METHOD_CALL(VARIABLE("vc"), "getXsdType", VARIABLE(localPart)))))));
                        } else {
                            // Warning case
                            statements.add(new Comment("Warning: " + element.getPrefix() + ":" + element.getLocalPart() + " not declared"));
                            statements.add(IF(BINARY_OP(notNull(element.getLocalPart()),
                                    "&&",
                                    notNull(localPart)))
                                    .THEN(METHOD_CALL(VARIABLE("attrs"), "add",
                                            pfCall("newAttribute", List.of(
                                                    CAST(PROV_QUALIFIED_NAME, VARIABLE(element.getLocalPart())),
                                                    VARIABLE(localPart),
                                                    METHOD_CALL(VARIABLE("vc"), "getXsdType", VARIABLE(localPart)))))));
                        }
                    } else {
                        // attrs.add(pf.newAttribute(vmap[element], vmap[vq], vmap[typeq]))
                        statements.add(
                                METHOD_CALL(VARIABLE("attrs"), "add",
                                        pfCall("newAttribute", List.of(
                                                VARIABLE(vmap.get(element)),
                                                VARIABLE(vmap.get(vq)),
                                                VARIABLE(vmap.get(typeq))))));
                    }
                }
            } else {
                if (value instanceof LangString) {
                    statements.add(
                            METHOD_CALL(VARIABLE("attrs"), "add",
                                    pfCall("newAttribute", List.of(
                                            VARIABLE(vmap.get(element)),
                                            CONSTANT(((LangString) value).getValue()),
                                            VARIABLE(vmap.get(typeq))))));
                } else {
                    statements.add(
                            METHOD_CALL(VARIABLE("attrs"), "add",
                                    pfCall("newAttribute", List.of(
                                            VARIABLE(vmap.get(element)),
                                            CONSTANT(value.toString()),
                                            VARIABLE(vmap.get(typeq))))));
                }
            }
        }
        return VARIABLE("attrs");
    }

    public void doReservedAttributeActionPast(QualifiedName element, QualifiedName vq, QualifiedName typeq) {
        final String elementUri = element.getUri();
        if (InstantiateUtil.LABEL_URI.equals(elementUri)) {
            // if (vq.localPart!=null) attrs.add(pf.newAttribute(PROV_LABEL, vq.localPart, vc.getXsdType(vq.localPart)))
            statements.add(
                    IF(notNull(vq.getLocalPart()))
                            .THEN(METHOD_CALL(VARIABLE("attrs"), "add",
                                    pfCall("newAttribute", List.of(
                                            VARIABLE(vmap.get(pFactory.getName().PROV_LABEL)),
                                            VARIABLE(vq.getLocalPart()),
                                            METHOD_CALL(VARIABLE("vc"), "getXsdType", VARIABLE(vq.getLocalPart())))))));
        } else if (InstantiateUtil.TIME_URI.equals(elementUri)) {
            // don't include it!
        } else if (InstantiateUtil.STARTTIME_URI.equals(elementUri)) {
            // don't include it!
        } else if (InstantiateUtil.ENDTIME_URI.equals(elementUri)) {
            // don't include it!
        } else if (InstantiateUtil.IFVAR_URI.equals(elementUri)) {
            // don't include it!
        } else if (InstantiateUtil.ACTIVITY_TYPE_URI.equals(elementUri)) {
            // don't include it!
        } else if (InstantiateUtil.TMPL_ACTIVITY_URI.equals(elementUri)) {
            // don't include it!
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* Same as doAttributesAction, except that it does not generate code. */
    public Collection<Attribute> doCheckAttributesAction(org.openprovenance.prov.model.Statement s) {
        Collection<Attribute> attributes = pFactory.getAttributes(s);
        Collection<Attribute> result = new LinkedList<>();
        if (!(attributes.isEmpty())) {
            for (Attribute attribute : attributes) {
                QualifiedName element = attribute.getElementName();
                Object value = attribute.getValue();
                if (value instanceof QualifiedName) {
                    QualifiedName vq = (QualifiedName) value;
                    if (InstantiateUtil.isVariable(vq)) {
                        if (!reservedElement(element)) {
                            result.add(attribute);
                        }
                    } else {
                        result.add(attribute);
                    }
                } else {
                    result.add(attribute);
                }
            }
        }
        return result;
    }

    public boolean reservedElement(QualifiedName element) {
        final String elementName = element.getUri();
        return (InstantiateUtil.LABEL_URI.equals(elementName))
                || (InstantiateUtil.TIME_URI.equals(elementName))
                || (InstantiateUtil.STARTTIME_URI.equals(elementName))
                || (InstantiateUtil.ENDTIME_URI.equals(elementName))
                || (InstantiateUtil.ACTIVITY_TYPE_URI.equals(elementName))
                || (InstantiateUtil.TMPL_ACTIVITY_URI.equals(elementName))
                || (InstantiateUtil.IFVAR_URI.equals(elementName));
    }

    public boolean reservedIfVar(QualifiedName element) {
        final String elementName = element.getUri();
        return (InstantiateUtil.IFVAR_URI.equals(elementName));
    }

    public String doCollectElementVariable(org.openprovenance.prov.model.Statement s, String search) {
        Collection<Attribute> attributes = pFactory.getAttributes(s);
        if (!(attributes.isEmpty())) {
            for (Attribute attribute : attributes) {
                QualifiedName element = attribute.getElementName();
                Object value = attribute.getValue();
                if (value instanceof QualifiedName) {
                    QualifiedName vq = (QualifiedName) value;
                    if (InstantiateUtil.isVariable(vq)) {
                        if (search.equals(element.getUri())) {
                            return vq.getLocalPart();
                        }
                    }
                }
            }
        }
        return null;
    }
}
