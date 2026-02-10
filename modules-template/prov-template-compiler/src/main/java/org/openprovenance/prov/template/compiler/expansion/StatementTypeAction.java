package org.openprovenance.prov.template.compiler.expansion;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.past.Comment;
import org.openprovenance.prov.template.compiler.past.Statement;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;
import org.openprovenance.prov.template.core.InstantiateUtil;
import org.openprovenance.prov.template.core.exception.MissingAttributeValue;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;
import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_EXT_NS;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constant.getNull;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.core.InstantiateUtil.*;

public class StatementTypeAction implements StatementAction {

    public static String AGENT_URI=PROV_NS+"Agent";
    public static String ENTITY_URI=PROV_NS+"Entity";
    public static String ACTIVITY_URI=PROV_NS+"Activity";
    public static String BUNDLE_URI=PROV_NS+"Bundle";
    public static String WASDERIVEDFROM_URI=PROV_NS+"WasDerivedFrom";
    public static String QUALIFIEDHADMEMBER_URI=PROV_EXT_NS+"HadMember";

    private final Map<String, Collection<String>> knownTypes;
    private final Map<String, Collection<String>> unknownTypes;
    private final List<Statement> statements;
    private final CompilerUtil compilerUtil;
    private final TemplateBindingsSchema bindingsSchema;

    private Set<QualifiedName> allVars;
    private Set<QualifiedName> allAtts;

    private final ProvFactory pFactory;
    private Hashtable<QualifiedName, String> vmap;
    final public QualifiedName PROV_EXT_NS_ID;


    public StatementTypeAction(ProvFactory pFactory, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, Hashtable<QualifiedName, String> vmap, TemplateBindingsSchema bindingsSchema, Map<String, Collection<String>> knownTypes, Map<String, Collection<String>> unknownTypes, List<Statement> statements, CompilerUtil compilerUtil) {
        this.pFactory=pFactory;
        this.allVars=allVars;
        this.allAtts=allAtts;
        this.vmap=vmap;
        this.bindingsSchema=bindingsSchema;
        this.knownTypes=knownTypes;
        this.unknownTypes=unknownTypes;
        this.statements=statements;
        this.compilerUtil=compilerUtil;
        PROV_EXT_NS_ID = pFactory.newQualifiedName(PROV_EXT_NS, "id", "provxt");

    }


    public Map<String, Collection<String>> getKnownTypes() {
        return knownTypes;
    }

    public Map<String, Collection<String>> getUnknownTypes() {
        return unknownTypes;
    }


    public void registerTypes(QualifiedName id, List<Type> types) {
        if (id !=null) {
            types.forEach(type -> {
                Object o=type.getValue();
                if (o instanceof QualifiedName) {
                    QualifiedName qn=(QualifiedName) o;
                    if (InstantiateUtil.isVariable(qn)) {
                        registerUnknownType(id,qn.getUri());
                    } else {
                        registerAType(id,qn.getUri());
                    }
                }
            });
        }
    }
    public void registerTypes(QualifiedName id, String suffix, List<Type> types) {
        if (id !=null) {
            types.forEach(type -> {
                Object o=type.getValue();
                if (o instanceof QualifiedName) {
                    QualifiedName qn=(QualifiedName) o;
                    if (InstantiateUtil.isVariable(qn)) {
                        registerUnknownType(id,suffix,qn.getUri());
                    } else {
                        registerAType(id,suffix,qn.getUri());
                    }
                }
            });
        }
    }
    public void registerTypes2(QualifiedName id, Collection<QualifiedName> types) {
        if ((id !=null) && (types!=null)) {
            types.forEach(qn -> {
                if (InstantiateUtil.isVariable(qn)) {
                    registerUnknownType(id,qn.getUri());
                } else {
                    registerAType(id,qn.getUri());
                }
            });
        }
    }

    public void registerTypes2(QualifiedName id, String suffix, Collection<QualifiedName> types) {
        if ((id !=null) && (types!=null)) {
            types.forEach(qn -> {
                if (InstantiateUtil.isVariable(qn)) {
                    registerUnknownType(id,suffix, qn.getUri());
                } else {
                    registerAType(id, suffix, qn.getUri());
                }
            });
        }
    }

    static final Class<HashSet> collectionClass=HashSet.class;


    public void registerAgent(QualifiedName id) {
        registerAType(id,AGENT_URI);
    }
    public void registerEntity(QualifiedName id) {
        registerAType(id,ENTITY_URI);
    }
    public void registerActivity(QualifiedName id) {
        registerAType(id,ACTIVITY_URI);
    }
    public void registerBundle(QualifiedName id) {
        registerAType(id,BUNDLE_URI);
    }

    private void registerAType(QualifiedName id, String type) {
        if (id !=null) {
            final String uri = id.getUri();
            knownTypes.computeIfAbsent(uri, k -> new HashSet<>());
            knownTypes.get(uri).add(type);
        }
    }
    private void registerAType(QualifiedName id, String suffix, String type) {
        if (id !=null) {
            final String uri = id.getUri() + suffix;
            knownTypes.computeIfAbsent(uri, k -> new HashSet<>());
            knownTypes.get(uri).add(type);
        }
    }
    private void registerUnknownType(QualifiedName id, String type) {
        if (id !=null) {
            final String uri = id.getUri();
            unknownTypes.computeIfAbsent(uri, k -> new HashSet<>());
            unknownTypes.get(uri).add(type);
        }
    }
    private void registerUnknownType(QualifiedName id, String suffix, String type) {
        if (id !=null) {
            final String uri = id.getUri()+suffix;
            unknownTypes.computeIfAbsent(uri, k -> new HashSet<>());
            unknownTypes.get(uri).add(type);
        }
    }


    @Override
    public void doAction(Activity s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getId());
        final Collection<Attribute> attributes = pFactory.getAttributes(s);
        doRegisterTypesForAttributes(s, attributes, ACTIVITY_URI);
        doRegisterIDataForAttributes(s, attributes, s.getType(), ACTIVITY_URI);

    }

    @Override
    public void doAction(Used s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getActivity());
        registerEntity(s.getEntity());
    }


    @Override
    public void doAction(WasGeneratedBy s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getActivity());
        registerEntity(s.getEntity());
    }

    @Override
    public void doAction(WasInvalidatedBy s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getActivity());
        registerEntity(s.getEntity());
    }


    @Override
    public void doAction(WasStartedBy s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getActivity());
        registerEntity(s.getStarter());
        registerEntity(s.getTrigger());
    }

    @Override
    public void doAction(Agent s) {
        registerTypes(s.getId(),s.getType());
        registerAgent(s.getId());
        final Collection<Attribute> attributes = pFactory.getAttributes(s);
        attributes.add(pFactory.newType(pFactory.newQualifiedName(PROV_NS,"Agent", "prov"),pFactory.getName().PROV_QUALIFIED_NAME));
        doRegisterTypesForAttributes(s, attributes, AGENT_URI);
        doRegisterIDataForAttributes(s, attributes, s.getType(), AGENT_URI);
    }

    @Override
    public void doAction(Entity s) {
        registerTypes(s.getId(),s.getType());
        registerEntity(s.getId());
        final Collection<Attribute> attributes = pFactory.getAttributes(s);
        doRegisterTypesForAttributes(s, attributes, ENTITY_URI);
        doRegisterIDataForAttributes(s, attributes, s.getType(), ENTITY_URI);
    }

    @Override
    public void doAction(AlternateOf s) {
        //registerTypes(s.getId(),s.getType());
    }

    @Override
    public void doAction(WasAssociatedWith s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getActivity());
        registerAgent(s.getAgent());
        registerEntity(s.getPlan());
    }

    @Override
    public void doAction(WasAttributedTo s) {
        registerTypes(s.getId(),s.getType());
        registerAgent(s.getAgent());
        registerEntity(s.getEntity());
    }

    @Override
    public void doAction(WasInfluencedBy s) {
        registerTypes(s.getId(),s.getType());
    }

    @Override
    public void doAction(ActedOnBehalfOf s) {
        registerTypes(s.getId(),s.getType());
        registerAgent(s.getResponsible());
        registerAgent(s.getDelegate());
        registerActivity(s.getActivity());
    }



    public Collection<QualifiedName> doCollectElementVariables(org.openprovenance.prov.model.Statement s, String search) {
        return CompilerExpansionBuilder.doCollectElementVariables(pFactory,s,search);
    }


    public static String bnNS="http://openprovenance.org/blank#";
    public static String bnPrefix="bn";

    static int count=0;
    static public QualifiedName gensym() {
        return new org.openprovenance.prov.vanilla.QualifiedName(bnNS, "n" + (count++), bnPrefix);
    }


    @Override
    public void doAction(WasDerivedFrom s) {
        final Collection<QualifiedName> qualifiedNames = doCollectElementVariables(s, InstantiateUtil.ACTIVITY_TYPE_URI);
        if (s.getId()==null) {
            s.setId(gensym());
        }
        statements.add(new Comment("wdf $N", s.getId().getUri()));

        registerAType(s.getId(),WASDERIVEDFROM_URI);
        registerTypes(s.getId(), s.getType());

        registerEntity(s.getUsedEntity());
        registerEntity(s.getGeneratedEntity());
        registerActivity(s.getActivity());


        if (qualifiedNames!=null && !qualifiedNames.isEmpty()) {
            registerTypes2(s.getId(), qualifiedNames);

            dynamicRegisterTypes(s, qualifiedNames, WASDERIVEDFROM_URI);
        }

    }


    static int anotherCounter=0;
    private void doRegisterTypesForAttributes(Identifiable s,Collection<Attribute> attributes, String expressionUri) {

        if (InstantiateUtil.isGensymVariable(s.getId())) return;
        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();

        String tmp_Conv="tmp_Conv"+(anotherCounter++);

        // Build the inner statements for the if-blocks
        List<Statement> innerStatements = new ArrayList<>();

        // tmp_Conv = propertyConverters.get(expressionUri)
        innerStatements.add(DEFINITION(MAP_STRING_BIFUNCTION_OBJECT_STRING_COLLECTION_STRING, VARIABLE(tmp_Conv),
                METHOD_CALL(VARIABLE("propertyConverters"), "get", List.of(CONSTANT(expressionUri)))));

        List<Statement> innerInnerStatements = new ArrayList<>();

        Map<String,String> seen=new HashMap<>();

        attributes.forEach(attr -> {
            String attributeUri = attr.getElementName().getUri();
            final Object value = attr.getValue();
            if (TMPL_NS.equals(attr.getElementName().getNamespaceURI())) return; // don't do anything if it's a tmpl attribute
            if (TMPL_NS.equals(attr.getElementName().getNamespaceURI())) return; // don't do anything if it's a tmpl attribute
            String tmp_Conv2 = seen.get(attributeUri);
            boolean first_encounter;
            if (tmp_Conv2==null) {
                tmp_Conv2 = tmp_Conv + "_" + cleanUpName(attributeUri);
                seen.put(attributeUri, tmp_Conv2);
                first_encounter=true;
            } else {
                first_encounter=false;
            }

            List<Statement> attrStatements = new ArrayList<>();

            if (first_encounter) {
                // BiFunction<Object, String, Collection<String>> tmp_Conv2 = tmp_Conv.get(attributeUri)
                innerInnerStatements.add(DEFINITION(BIFUNCTION_OBJECT_STRING_COLLECTION_STRING, VARIABLE(tmp_Conv2),
                        METHOD_CALL(VARIABLE(tmp_Conv), "get", List.of(CONSTANT(attributeUri)))));
            }

            if (value instanceof QualifiedName) {
                QualifiedName qn=(QualifiedName) value;
                if (InstantiateUtil.isVariable(qn)) {
                    String key=qn.getLocalPart();
                    if (bindingsSchema.getVar().containsKey(key) && (bindingsSchema.getVar().get(key)!=null)) {
                        final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(theVar, key);
                        attrStatements.add(METHOD_CALL(VARIABLE("unknownTypeMap"), "computeIfAbsent",
                                List.of(VARIABLE(s.getId().getLocalPart()), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_SET_GENERICS, List.of()))))));
                        if (atype.equals(QualifiedName.class)) {
                            // if (key!=null) unknownTypeMap.get(id).addAll(tmp_Conv2.apply(key.getUri(), id.getUri()))
                            attrStatements.add(IF(BINARY_OP(VARIABLE(key), "!=", getNull()))
                                    .THEN(METHOD_CALL(METHOD_CALL(VARIABLE("unknownTypeMap"), "get", List.of(VARIABLE(s.getId().getLocalPart()))), "addAll",
                                            List.of(METHOD_CALL(VARIABLE(tmp_Conv2), "apply",
                                                    List.of(METHOD_CALL(VARIABLE(key), "getUri", List.of()),
                                                            METHOD_CALL(VARIABLE(s.getId().getLocalPart()), "getUri", List.of())))))));
                        } else {
                            // if (key!=null) unknownTypeMap.get(id).addAll(tmp_Conv2.apply(key, id.getUri()))
                            attrStatements.add(IF(BINARY_OP(VARIABLE(key), "!=", getNull()))
                                    .THEN(METHOD_CALL(METHOD_CALL(VARIABLE("unknownTypeMap"), "get", List.of(VARIABLE(s.getId().getLocalPart()))), "addAll",
                                            List.of(METHOD_CALL(VARIABLE(tmp_Conv2), "apply",
                                                    List.of(VARIABLE(key),
                                                            METHOD_CALL(VARIABLE(s.getId().getLocalPart()), "getUri", List.of())))))));
                        }
                    }
                } else {
                    // knownTypeMap.computeIfAbsent(id, k -> new HashSet<>())
                    attrStatements.add(METHOD_CALL(VARIABLE("knownTypeMap"), "computeIfAbsent",
                            List.of(VARIABLE(s.getId().getLocalPart()), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_SET_GENERICS, List.of()))))));
                    // knownTypeMap.get(id).addAll(tmp_Conv2.apply(qn.getUri(), id.getUri()))
                    attrStatements.add(METHOD_CALL(METHOD_CALL(VARIABLE("knownTypeMap"), "get", List.of(VARIABLE(s.getId().getLocalPart()))), "addAll",
                            List.of(METHOD_CALL(VARIABLE(tmp_Conv2), "apply",
                                    List.of(CONSTANT(qn.getUri()),
                                            METHOD_CALL(VARIABLE(s.getId().getLocalPart()), "getUri", List.of()))))));
                }
            } else if ((value instanceof String)  || (value instanceof LangString) || (value instanceof Integer)) {
                String aString=String.valueOf(value);
                if (value instanceof LangString) {
                    LangString ls=(LangString) value;
                    final String lang = ls.getLang();
                    if (lang !=null) {
                        aString = ls.getValue() + "~" + lang;
                    } else {
                        aString = ls.getValue();
                    }
                }
                attrStatements.add(METHOD_CALL(METHOD_CALL(VARIABLE("unknownTypeMap"), "get", List.of(VARIABLE(s.getId().getLocalPart()))), "addAll",
                        List.of(METHOD_CALL(VARIABLE(tmp_Conv2), "apply",
                                List.of(CONSTANT(aString),
                                        METHOD_CALL(VARIABLE(s.getId().getLocalPart()), "getUri", List.of()))))));
            } else {
                throw new UnsupportedOperationException("doRegisterTypesForAttributes with attribute value " + value + " for element " +attributeUri);
            }
            // if (tmp_Conv2 != null) { ...attrStatements... }
            innerInnerStatements.add(IF(BINARY_OP(VARIABLE(tmp_Conv2), "!=", getNull()))
                    .THEN(attrStatements.toArray(new Statement[0])));
        });

        // if (tmp_Conv != null) { ...innerInnerStatements... }
        innerStatements.add(IF(BINARY_OP(VARIABLE(tmp_Conv), "!=", getNull()))
                .THEN(innerInnerStatements.toArray(new Statement[0])));

        // if (id != null) { ...innerStatements... }
        statements.add(IF(BINARY_OP(VARIABLE(s.getId().getLocalPart()), "!=", getNull()))
                .THEN(innerStatements.toArray(new Statement[0])));
    }

    static int iDataCounter=0;

    private void doRegisterIDataForAttributes(Identifiable s, Collection<Attribute> attributes, List<Type> types, String expressionUri) {
        if (InstantiateUtil.isGensymVariable(s.getId())) return;
        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();

        String tmp_Conv="itmp_Conv"+(iDataCounter++);

        List<Statement> outerStatements = new ArrayList<>();

        // Map<String, TriFunction<...>> tmp_Conv = null;
        outerStatements.add(DEFINITION(MAP_STRING_TRIFUNCTION, VARIABLE(tmp_Conv), getNull()));

        for (Type type: types) {
            Object o=type.getValue();
            if (o instanceof QualifiedName) {
                QualifiedName qn=(QualifiedName) o;
                if (isVariable(qn)) {
                    // if ((tmp_Conv==null)&&(qn.localPart!=null)) tmp_Conv=idataConverters.get(qn.localPart.getUri())
                    outerStatements.add(IF(BINARY_OP(
                                    BINARY_OP(VARIABLE(tmp_Conv), "==", getNull()),
                                    "&&",
                                    BINARY_OP(VARIABLE(qn.getLocalPart()), "!=", getNull())))
                            .THEN(ASSIGNMENT(VARIABLE(tmp_Conv),
                                    METHOD_CALL(VARIABLE("idataConverters"), "get",
                                            List.of(METHOD_CALL(VARIABLE(qn.getLocalPart()), "getUri", List.of()))))));
                } else {
                    // if (tmp_Conv==null) tmp_Conv=idataConverters.get(qn.getUri())
                    outerStatements.add(IF(BINARY_OP(VARIABLE(tmp_Conv), "==", getNull()))
                            .THEN(ASSIGNMENT(VARIABLE(tmp_Conv),
                                    METHOD_CALL(VARIABLE("idataConverters"), "get",
                                            List.of(CONSTANT(qn.getUri()))))));
                }
            } else if (o instanceof String) {
                String str=(String)o;
                // if (tmp_Conv==null) tmp_Conv=idataConverters.get(str)
                outerStatements.add(IF(BINARY_OP(VARIABLE(tmp_Conv), "==", getNull()))
                        .THEN(ASSIGNMENT(VARIABLE(tmp_Conv),
                                METHOD_CALL(VARIABLE("idataConverters"), "get",
                                        List.of(CONSTANT(str))))));
            } else {
                throw new UnsupportedOperationException("doRegisterIDataForAttributes: Not supported type " + o);
            }
        }

        // if (tmp_Conv==null) tmp_Conv=idataConverters.get(expressionUri)
        outerStatements.add(IF(BINARY_OP(VARIABLE(tmp_Conv), "==", getNull()))
                .THEN(ASSIGNMENT(VARIABLE(tmp_Conv),
                        METHOD_CALL(VARIABLE("idataConverters"), "get",
                                List.of(CONSTANT(expressionUri))))));

        List<Statement> innerStatements = new ArrayList<>();
        Map<String,String> seen=new HashMap<>();

        Collection<Attribute> attributes2= new LinkedList<>(attributes);
        attributes2.add(pFactory.newAttribute(PROV_EXT_NS_ID,s.getId(), pFactory.getName().PROV_QUALIFIED_NAME) );

        attributes2.forEach(attr -> {
            String attributeUri = attr.getElementName().getUri();
            final Object value = attr.getValue();
            if (TMPL_NS.equals(attr.getElementName().getNamespaceURI())) return; // don't do anything if it's a tmpl attribute
            if (TMPL_NS.equals(attr.getElementName().getNamespaceURI())) return; // don't do anything if it's a tmpl attribute
            String tmp_Conv2 = seen.get(attributeUri);
            boolean first_encounter;
            if (tmp_Conv2==null) {
                tmp_Conv2 = tmp_Conv + "_" + cleanUpName(attributeUri);
                seen.put(attributeUri, tmp_Conv2);
                first_encounter=true;
            } else {
                first_encounter=false;
            }

            if (first_encounter) {
                // TriFunction<...> tmp_Conv2 = tmp_Conv.get(attributeUri)
                innerStatements.add(DEFINITION(TRIFUNCTION_OBJECT_STRING_STRING_COLLECTION_PAIRS, VARIABLE(tmp_Conv2),
                        METHOD_CALL(VARIABLE(tmp_Conv), "get", List.of(CONSTANT(attributeUri)))));
            }

            List<Statement> attrStatements = new ArrayList<>();

            if (value instanceof QualifiedName) {
                QualifiedName qn=(QualifiedName) value;
                if (InstantiateUtil.isVariable(qn)) {
                    String key=qn.getLocalPart();
                    if (bindingsSchema.getVar().containsKey(key) && (bindingsSchema.getVar().get(key)!=null)) {
                        final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(theVar, key);
                        // idata.computeIfAbsent(id, k -> new HashMap<>())
                        attrStatements.add(METHOD_CALL(VARIABLE("idata"), "computeIfAbsent",
                                List.of(VARIABLE(s.getId().getLocalPart()), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of()))))));
                        if (atype.equals(QualifiedName.class)) {
                            // if (key!=null) { ... option 1 ... }
                            String pairVariable = "p";
                            String pairVariable2 = "p2";
                            attrStatements.add(new Comment("option 1"));
                            attrStatements.add(IF(BINARY_OP(VARIABLE(key), "!=", getNull()))
                                    .THEN(
                                            // Collection<Pair<String, Collection<String>>> p = tmp_Conv2.apply(key.getUri(), id.getUri(), attributeUri)
                                            DEFINITION(COLLECTION_OF_PAIRS_STRING_COLLECTION_STRING, VARIABLE(pairVariable),
                                                    METHOD_CALL(VARIABLE(tmp_Conv2), "apply",
                                                            List.of(METHOD_CALL(VARIABLE(key), "getUri", List.of()),
                                                                    METHOD_CALL(VARIABLE(s.getId().getLocalPart()), "getUri", List.of()),
                                                                    CONSTANT(attributeUri)))),
                                            // p.forEach(p2 -> { idata.get(id).computeIfAbsent(p2.getLeft(), k -> new HashSet<>()); idata.get(id).get(p2.getLeft()).addAll(p2.getRight()) })
                                            METHOD_CALL(VARIABLE(pairVariable), "forEach", List.of(
                                                    forEachPairLambda(s.getId().getLocalPart())))
                                    ));
                        } else {
                            // if (key!=null) { ... option 2 ... }
                            String pairVariable = "p";
                            attrStatements.add(new Comment("option 2"));
                            attrStatements.add(IF(BINARY_OP(VARIABLE(key), "!=", getNull()))
                                    .THEN(
                                            // Collection<Pair<String, Collection<String>>> p = tmp_Conv2.apply(key, id.getUri(), attributeUri)
                                            DEFINITION(COLLECTION_OF_PAIRS_STRING_COLLECTION_STRING, VARIABLE(pairVariable),
                                                    METHOD_CALL(VARIABLE(tmp_Conv2), "apply",
                                                            List.of(VARIABLE(key),
                                                                    METHOD_CALL(VARIABLE(s.getId().getLocalPart()), "getUri", List.of()),
                                                                    CONSTANT(attributeUri)))),
                                            // p.forEach(p2 -> { ... })
                                            METHOD_CALL(VARIABLE(pairVariable), "forEach", List.of(
                                                    forEachPairLambda(s.getId().getLocalPart())))
                                    ));
                        }
                    }
                } else {
                    // option 3
                    attrStatements.add(new Comment("option 3"));
                    attrStatements.add(METHOD_CALL(VARIABLE("idata"), "computeIfAbsent",
                            List.of(VARIABLE(s.getId().getLocalPart()), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of()))))));
                    String pairVariable="p";
                    // Collection<Pair<String, Collection<String>>> p = tmp_Conv2.apply(attributeUri, id.getUri(), attributeUri)
                    attrStatements.add(DEFINITION(COLLECTION_OF_PAIRS_STRING_COLLECTION_STRING, VARIABLE(pairVariable),
                            METHOD_CALL(VARIABLE(tmp_Conv2), "apply",
                                    List.of(CONSTANT(attributeUri),
                                            METHOD_CALL(VARIABLE(s.getId().getLocalPart()), "getUri", List.of()),
                                            CONSTANT(attributeUri)))));
                    // p.forEach(p2 -> { ... })
                    attrStatements.add(METHOD_CALL(VARIABLE(pairVariable), "forEach", List.of(
                            forEachPairLambda(s.getId().getLocalPart()))));
                }
            } else if ((value instanceof String)  || (value instanceof LangString) || (value instanceof Integer)) {
                String aString=String.valueOf(value);
                if (value instanceof LangString) {
                    LangString ls=(LangString) value;
                    final String lang = ls.getLang();
                    if (lang !=null) {
                        aString = ls.getValue() + "~" + lang;
                    } else {
                        aString = ls.getValue();
                    }
                }
                // option 4
                attrStatements.add(new Comment("option 4"));
                attrStatements.add(METHOD_CALL(VARIABLE("idata"), "computeIfAbsent",
                        List.of(VARIABLE(s.getId().getLocalPart()), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of()))))));
                String pairVariable="p";
                attrStatements.add(DEFINITION(COLLECTION_OF_PAIRS_STRING_COLLECTION_STRING, VARIABLE(pairVariable),
                        METHOD_CALL(VARIABLE(tmp_Conv2), "apply",
                                List.of(CONSTANT(aString),
                                        METHOD_CALL(VARIABLE(s.getId().getLocalPart()), "getUri", List.of()),
                                        CONSTANT(attributeUri)))));
                attrStatements.add(METHOD_CALL(VARIABLE(pairVariable), "forEach", List.of(
                        forEachPairLambda(s.getId().getLocalPart()))));
            } else {
                throw new UnsupportedOperationException("doRegisterIDataForAttributes with attribute value " + value + " for element " +attributeUri);
            }
            // if (tmp_Conv2 != null) { ...attrStatements... }
            innerStatements.add(IF(BINARY_OP(VARIABLE(tmp_Conv2), "!=", getNull()))
                    .THEN(attrStatements.toArray(new Statement[0])));
        });

        // if (tmp_Conv != null) { ...innerStatements... }
        outerStatements.add(IF(BINARY_OP(VARIABLE(tmp_Conv), "!=", getNull()))
                .THEN(innerStatements.toArray(new Statement[0])));

        // if (id != null) { ...outerStatements... }
        statements.add(IF(BINARY_OP(VARIABLE(s.getId().getLocalPart()), "!=", getNull()))
                .THEN(outerStatements.toArray(new Statement[0])));
    }

    private String cleanUpName(String elementName) {
        return Base64.getEncoder().withoutPadding().encodeToString(elementName.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Creates a PAST lambda expression: p2 -> { idata.get(id).computeIfAbsent(p2.getLeft(), k -> new HashSet<>()); idata.get(id).get(p2.getLeft()).addAll(p2.getRight()); }
     */
    private org.openprovenance.prov.template.compiler.past.LambdaExpression forEachPairLambda(String idLocalPart) {
        return LAMBDA(PARAMETER("p2", OBJECT)).BODY(
                METHOD_CALL(METHOD_CALL(VARIABLE("idata"), "get", List.of(VARIABLE(idLocalPart))), "computeIfAbsent",
                        List.of(METHOD_CALL(VARIABLE("p2"), "getLeft", List.of()),
                                LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_SET_GENERICS, List.of()))))),
                METHOD_CALL(METHOD_CALL(METHOD_CALL(VARIABLE("idata"), "get", List.of(VARIABLE(idLocalPart))), "get",
                                List.of(METHOD_CALL(VARIABLE("p2"), "getLeft", List.of()))),
                        "addAll",
                        List.of(METHOD_CALL(VARIABLE("p2"), "getRight", List.of())))
        );
    }

    private void dynamicRegisterTypes(Identifiable s, Collection<QualifiedName> qualifiedNames, String relationURI) {
        if (qualifiedNames==null) return;
        String tmp="_tmp_"+ s.getId().getLocalPart();
        Collection<QualifiedName> activities=doCollectElementVariables((org.openprovenance.prov.model.Statement) s, TMPL_ACTIVITY_URI);
        if (activities==null || activities.isEmpty()) throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in relation " + s);
        final String localPart = s.getId().getLocalPart() + "." ;
        String suffix = activities.stream().findFirst().get().getLocalPart();

        // QualifiedName tmp = pf.newQualifiedName(ns, localPart+suffix.getLocalPart(), prefix)
        statements.add(DEFINITION(PROV_QUALIFIED_NAME, VARIABLE(tmp),
                METHOD_CALL(VARIABLE("pf"), "newQualifiedName",
                        List.of(CONSTANT(s.getId().getNamespaceURI()),
                                CONSTANT(localPart + "\" + " + suffix + ".getLocalPart() + \""),
                                CONSTANT(s.getId().getPrefix())))));

        // knownTypeMap.computeIfAbsent(tmp, k -> new HashSet<>())
        statements.add(METHOD_CALL(VARIABLE("knownTypeMap"), "computeIfAbsent",
                List.of(VARIABLE(tmp), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_SET_GENERICS, List.of()))))));

        // knownTypeMap.get(tmp).add(relationURI)
        statements.add(METHOD_CALL(METHOD_CALL(VARIABLE("knownTypeMap"), "get", List.of(VARIABLE(tmp))), "add",
                List.of(CONSTANT(relationURI))));

        qualifiedNames.forEach(q -> {
            if (InstantiateUtil.isVariable(q)) {
                // unknownTypeMap.computeIfAbsent(tmp, k -> new HashSet<>())
                statements.add(METHOD_CALL(VARIABLE("unknownTypeMap"), "computeIfAbsent",
                        List.of(VARIABLE(tmp), LAMBDA(PARAMETER("k", STRING)).BODY(RETURN(CONSTRUCTOR_CALL(HASH_SET_GENERICS, List.of()))))));
                // unknownTypeMap.get(tmp).add(q.localPart.getUri())
                statements.add(METHOD_CALL(METHOD_CALL(VARIABLE("unknownTypeMap"), "get", List.of(VARIABLE(tmp))), "add",
                        List.of(METHOD_CALL(VARIABLE(q.getLocalPart()), "getUri", List.of()))));
            } else {
                // knownTypeMap.get(tmp).add(q.getUri())
                statements.add(METHOD_CALL(METHOD_CALL(VARIABLE("knownTypeMap"), "get", List.of(VARIABLE(tmp))), "add",
                        List.of(CONSTANT(q.getUri()))));
            }
        });
    }

    @Override
    public void doAction(DictionaryMembership s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(DerivedByRemovalFrom s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(WasEndedBy s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getActivity());
        registerEntity(s.getEnder());
        registerEntity(s.getTrigger());

    }




    @Override
    public void doAction(HadMember s) {
        //registerTypes(s.getId(),s.getType());
        registerEntity(s.getCollection());
        s.getEntity().forEach(this::registerEntity);
    }

    @Override
    public void doAction(MentionOf s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(SpecializationOf s) {
        //registerTypes(s.getId(),s.getType());
        registerEntity(s.getGeneralEntity());
        registerEntity(s.getSpecificEntity());
    }

    @Override
    public void doAction(QualifiedSpecializationOf s) {
        registerTypes(s.getId(),s.getType());
        registerEntity(s.getGeneralEntity());
        registerEntity(s.getSpecificEntity());
    }

    @Override
    public void doAction(QualifiedAlternateOf s) {
        registerTypes(s.getId(),s.getType());
        registerEntity(s.getAlternate1());
        registerEntity(s.getAlternate2());
    }

    @Override
    public void doAction(QualifiedHadMember s) {
        final Collection<QualifiedName> qualifiedNames = doCollectElementVariables(s, InstantiateUtil.ACTIVITY_TYPE_URI);
        if (s.getId()==null) {
            s.setId(gensym());
        }
        statements.add(new Comment("qualified mem $N", s.getId().getUri()));

        registerTypes(s.getId(),s.getType());
        registerEntity(s.getCollection());
        s.getEntity().forEach(this::registerEntity);


        registerAType(s.getId(), QUALIFIEDHADMEMBER_URI);



        if (qualifiedNames!=null && !qualifiedNames.isEmpty()) {
            registerTypes2(s.getId(), qualifiedNames);
            dynamicRegisterTypes(s, qualifiedNames, QUALIFIEDHADMEMBER_URI);
        }

    }

    @Override
    public void doAction(DerivedByInsertionFrom s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(WasInformedBy s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getInformant());
        registerActivity(s.getInformed());
    }

    @Override
    public void doAction(Bundle bun, ProvUtilities provUtilities) {
        registerBundle(bun.getId());
        StatementTypeAction action2=new StatementTypeAction(pFactory, allVars, allAtts, vmap, bindingsSchema, knownTypes, unknownTypes, statements, compilerUtil);

        for (org.openprovenance.prov.model.Statement s: bun.getStatement()) {
            provUtilities.doAction(s, action2);

        }

    }

}
