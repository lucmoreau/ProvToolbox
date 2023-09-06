package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec.Builder;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.expander.ExpandUtil;
import org.openprovenance.prov.template.expander.exception.MissingAttributeValue;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;
import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_EXT_NS;
import static org.openprovenance.prov.template.compiler.expansion.CompilerTypeManagement.*;
import static org.openprovenance.prov.template.expander.ExpandUtil.*;

public class StatementTypeAction implements StatementAction {

    public static String AGENT_URI=PROV_NS+"Agent";
    public static String ENTITY_URI=PROV_NS+"Entity";
    public static String ACTIVITY_URI=PROV_NS+"Activity";
    public static String BUNDLE_URI=PROV_NS+"Bundle";
    public static String WASDERIVEDFROM_URI=PROV_NS+"WasDerivedFrom";
    public static String QUALIFIEDHADMEMBER_URI=PROV_EXT_NS+"HadMember";

    private final JsonNode bindings_schema;
    private final Map<String, Collection<String>> knownTypes;
    private final Map<String, Collection<String>> unknownTypes;
    private final Builder mbuilder;
    private final CompilerUtil compilerUtil;

    private Set<QualifiedName> allVars;
    private Set<QualifiedName> allAtts;

    private final ProvFactory pFactory;
    private Hashtable<QualifiedName, String> vmap;
    final public QualifiedName PROV_EXT_NS_ID;


    public StatementTypeAction(ProvFactory pFactory, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, Hashtable<QualifiedName, String> vmap, Builder builder, String target, JsonNode bindings_schema, Map<String, Collection<String>> knownTypes, Map<String, Collection<String>> unknownTypes, Builder mbuilder, CompilerUtil compilerUtil) {
        this.pFactory=pFactory;
        this.allVars=allVars;
        this.allAtts=allAtts;
        //this.builder=builder;
        //this.target=target;
        this.vmap=vmap;
        this.bindings_schema=bindings_schema;
        this.knownTypes=knownTypes;
        this.unknownTypes=unknownTypes;
        this.mbuilder=mbuilder;
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
                    if (ExpandUtil.isVariable(qn)) {
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
                    if (ExpandUtil.isVariable(qn)) {
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
                if (ExpandUtil.isVariable(qn)) {
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
                if (ExpandUtil.isVariable(qn)) {
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



    public Collection<QualifiedName> doCollectElementVariables(Statement s, String search) {
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
        final Collection<QualifiedName> qualifiedNames = doCollectElementVariables(s, ExpandUtil.ACTIVITY_TYPE_URI);
        if (s.getId()==null) {
            s.setId(gensym());
        }
        compilerUtil.specWithComment(mbuilder);
        mbuilder.addComment("wdf $N", s.getId().getUri());

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


        if (ExpandUtil.isGensymVariable(s.getId())) return;
        JsonNode the_var = bindings_schema.get("var");

        mbuilder.beginControlFlow("if ($N!=null) ", s.getId().getLocalPart());

        String tmp_Conv="tmp_Conv"+(anotherCounter++);
        mbuilder.addStatement("$T $N=propertyConverters.get($S)", Map_S_to_Function, tmp_Conv, expressionUri);

        mbuilder.beginControlFlow("if ($N!=null) ", tmp_Conv);
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
            if (first_encounter) mbuilder.addStatement("$T $N=$N.get($S)", Function_O_Col_S, tmp_Conv2, tmp_Conv, attributeUri);
            mbuilder.beginControlFlow("if ($N!=null) ", tmp_Conv2);

            if (value instanceof QualifiedName) {
                QualifiedName qn=(QualifiedName) value;
                if (ExpandUtil.isVariable(qn)) {
                    String key=qn.getLocalPart();
                    final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
                    mbuilder.addStatement("unknownTypeMap.computeIfAbsent($N, k -> new HashSet<>())",s.getId().getLocalPart());
                    if (atype.equals(QualifiedName.class)) {
                        mbuilder.addStatement("if ($N!=null) unknownTypeMap.get($N).addAll($N.apply($N.getUri(), $N.getUri()))", key, s.getId().getLocalPart(), tmp_Conv2, key, s.getId().getLocalPart());
                    } else {
                        mbuilder.addStatement("if ($N!=null) unknownTypeMap.get($N).addAll($N.apply($N, $N.getUri()))", key, s.getId().getLocalPart(), tmp_Conv2, key, s.getId().getLocalPart());
                    }
                } else {
                    mbuilder.addStatement("knownTypeMap.computeIfAbsent($N, k -> new HashSet<>())",s.getId().getLocalPart());
                    mbuilder.addStatement("knownTypeMap.get($N).addAll($N.apply($S,$N.getUri()))", s.getId().getLocalPart(), tmp_Conv2, qn.getUri(), s.getId().getLocalPart());
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
                mbuilder.addStatement("unknownTypeMap.get($N).addAll($N.apply($S,$N.getUri()))", s.getId().getLocalPart(), tmp_Conv2, aString, s.getId().getLocalPart());
            } else {
                throw new UnsupportedOperationException("doRegisterTypesForAttributes with attribute value " + value + " for element " +attributeUri);
            }
            mbuilder.endControlFlow();
        });

        mbuilder.endControlFlow();
        mbuilder.endControlFlow();
    }

    static int iDataCounter=0;

    private void doRegisterIDataForAttributes(Identifiable s, Collection<Attribute> attributes, List<Type> types, String expressionUri) {
        if (ExpandUtil.isGensymVariable(s.getId())) return;
        JsonNode the_var = bindings_schema.get("var");

        mbuilder.beginControlFlow("if ($N!=null) ", s.getId().getLocalPart());

        String tmp_Conv="itmp_Conv"+(iDataCounter++);
        mbuilder.addStatement("$T $N=null;", Map_S_to_TriFunction, tmp_Conv);

        for (Type type: types) {
// LUC use each tyep here
            Object o=type.getValue();
            if (o instanceof QualifiedName) {
                QualifiedName qn=(QualifiedName) o;
                if (isVariable(qn)) {
                    mbuilder.addStatement("if (($N==null)&&($N!=null)) $N=idataConverters.get($N.getUri())", tmp_Conv, qn.getLocalPart(), tmp_Conv, qn.getLocalPart());
                } else {
                    mbuilder.addStatement("if ($N==null) $N=idataConverters.get($S)", tmp_Conv, tmp_Conv, qn.getUri());
                }
            } else if (o instanceof String) {
                String str=(String)o;
                mbuilder.addStatement("if ($N==null) $N=idataConverters.get($S)", tmp_Conv, tmp_Conv, str);

            } else {
                throw new UnsupportedOperationException("doRegisterIDataForAttributes: Not supported type " + o);
            }
        }

        mbuilder.addStatement("if ($N==null) $N=idataConverters.get($S)", tmp_Conv, tmp_Conv, expressionUri);

        mbuilder.beginControlFlow("if ($N!=null) ", tmp_Conv);
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
            if (first_encounter) mbuilder.addStatement("$T $N=$N.get($S)", TriFunction_O_Col_S, tmp_Conv2, tmp_Conv, attributeUri);
            mbuilder.beginControlFlow("if ($N!=null) ", tmp_Conv2);

            if (value instanceof QualifiedName) {
                QualifiedName qn=(QualifiedName) value;
                if (ExpandUtil.isVariable(qn)) {
                    String key=qn.getLocalPart();
                    final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
                    mbuilder.addStatement("idata.computeIfAbsent($N, k -> new $T<>())",s.getId().getLocalPart(), HashMap.class);
                    if (atype.equals(QualifiedName.class)) {
                       //mbuilder.addStatement("if ($N!=null) idata.get($N).addAll($N.apply($N.getUri(), $N.getUri()))", key, s.getId().getLocalPart(), tmp_Conv2, key, s.getId().getLocalPart());
                        mbuilder.beginControlFlow("if /* option 1 */ ($N!=null)",key);
                        String pairVariable="p";
                        String pairVariable2="p2";
                        mbuilder.addStatement("$T $N=$N.apply($N.getUri(), $N.getUri(), $S))", CollectionOfPairsOfStringAndString, pairVariable, tmp_Conv2, key, s.getId().getLocalPart(), attributeUri);
                        CodeBlock.Builder subBuilder = CodeBlock.builder();
                        subBuilder.addStatement("idata.get($N).computeIfAbsent($N.getLeft(), k -> new $T<>())",s.getId().getLocalPart(), pairVariable2, collectionClass);
                        subBuilder.addStatement("idata.get($N).get($N.getLeft()).addAll($N.getRight())", s.getId().getLocalPart(), pairVariable2, pairVariable2);
                        mbuilder.beginControlFlow("$N.forEach(p2 -> ", pairVariable);
                        mbuilder.addCode(subBuilder.build());
                        mbuilder.endControlFlow(")");
                        mbuilder.endControlFlow();
                    } else {
                        //mbuilder.addStatement("if ($N!=null) idata.get($N).addAll($N.apply($N, $N.getUri()))", key, s.getId().getLocalPart(), tmp_Conv2, key, s.getId().getLocalPart());
                        mbuilder.beginControlFlow("if /* option 2 */ ($N!=null)",key);
                        String pairVariable="p";
                        String pairVariable2="p2";
                        mbuilder.addStatement("$T $N=$N.apply($N,  $N.getUri(), $S)", CollectionOfPairsOfStringAndString, pairVariable, tmp_Conv2, key,  s.getId().getLocalPart(), attributeUri);
                        CodeBlock.Builder subBuilder = CodeBlock.builder();
                        subBuilder.addStatement("idata.get($N).computeIfAbsent($N.getLeft(), k -> new $T<>())",s.getId().getLocalPart(), pairVariable2, collectionClass);
                        subBuilder.addStatement("idata.get($N).get($N.getLeft()).addAll($N.getRight())", s.getId().getLocalPart(),  pairVariable2, pairVariable2);
                        mbuilder.beginControlFlow("$N.forEach(p2 -> ", pairVariable);
                        mbuilder.addCode(subBuilder.build());
                        mbuilder.endControlFlow(")");
                        mbuilder.endControlFlow();
                    }
                } else {
                    mbuilder.addStatement("// option 3");
                    mbuilder.addStatement("idata.computeIfAbsent($N, k -> new $T<>())",s.getId().getLocalPart(), HashMap.class);
                    String pairVariable="p";
                    String pairVariable2="p2";
                    mbuilder.addStatement("$T $N=$N.apply($S,  $N.getUri(), $S)", CollectionOfPairsOfStringAndString, pairVariable, tmp_Conv2, attributeUri,  s.getId().getLocalPart(), attributeUri);
                    CodeBlock.Builder subBuilder = CodeBlock.builder();
                    subBuilder.addStatement("idata.get($N).computeIfAbsent($N.getLeft(), k -> new $T<>())",s.getId().getLocalPart(), pairVariable2, collectionClass);
                    subBuilder.addStatement("idata.get($N).get($N.getLeft()).addAll($N.getRight())", s.getId().getLocalPart(), pairVariable2, pairVariable2);
                    mbuilder.beginControlFlow("$N.forEach(p2 -> ", pairVariable);
                    mbuilder.addCode(subBuilder.build());
                    mbuilder.endControlFlow(")");
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
                mbuilder.addStatement("// option 4");
                mbuilder.addStatement("idata.computeIfAbsent($N, k -> new $T<>())",s.getId().getLocalPart(), HashMap.class);
                String pairVariable="p";
                String pairVariable2="p2";
                mbuilder.addStatement("$T $N=$N.apply($S, $N.getUri(), $S)", CollectionOfPairsOfStringAndString, pairVariable, tmp_Conv2, aString, s.getId().getLocalPart(),attributeUri);
                CodeBlock.Builder subBuilder = CodeBlock.builder();
                subBuilder.addStatement("idata.get($N).computeIfAbsent($N.getLeft(), k -> new $T<>())",s.getId().getLocalPart(), pairVariable2, collectionClass);
                subBuilder.addStatement("idata.get($N).get($N.getLeft()).addAll($N.getRight())", s.getId().getLocalPart(),  pairVariable2, pairVariable2);
                mbuilder.beginControlFlow("$N.forEach(p2 -> ", pairVariable);
                mbuilder.addCode(subBuilder.build());
                mbuilder.endControlFlow(")");
            } else {
                throw new UnsupportedOperationException("doRegisterIDataForAttributes with attribute value " + value + " for element " +attributeUri);
            }
            mbuilder.endControlFlow();
        });

        mbuilder.endControlFlow();
        mbuilder.endControlFlow();
    }

    private String cleanUpName(String elementName) {
        return Base64.getEncoder().withoutPadding().encodeToString(elementName.getBytes(StandardCharsets.UTF_8));
    }

    private void dynamicRegisterTypes(Identifiable s, Collection<QualifiedName> qualifiedNames, String relationURI) {
        if (qualifiedNames==null) return;
        String tmp="_tmp_"+ s.getId().getLocalPart();
        Collection<QualifiedName> activities=doCollectElementVariables((Statement) s, TMPL_ACTIVITY_URI);
        if (activities==null || activities.isEmpty()) throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in relation " + s);
        final String localPart = s.getId().getLocalPart() + "." ;
        String suffix = activities.stream().findFirst().get().getLocalPart();
        mbuilder.addStatement("$T $N=pf.newQualifiedName($S,$S+$N.getLocalPart(),$S)", QualifiedName.class, tmp, s.getId().getNamespaceURI(), localPart, suffix, s.getId().getPrefix());
        mbuilder.addStatement("knownTypeMap.computeIfAbsent($N, k -> new $T<>())", tmp, HashSet.class);
        mbuilder.addStatement("knownTypeMap.get($N).add($S)", tmp, relationURI);

        qualifiedNames.forEach(q -> {
            if (ExpandUtil.isVariable(q)) {
                mbuilder.addStatement("unknownTypeMap.computeIfAbsent($N, k -> new $T<>())", tmp, HashSet.class);
                mbuilder.addStatement("unknownTypeMap.get($N).add($N.getUri())", tmp, q.getLocalPart());
            } else {
                mbuilder.addStatement("knownTypeMap.get($N).add($S)", tmp, q.getUri());
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
        final Collection<QualifiedName> qualifiedNames = doCollectElementVariables(s, ExpandUtil.ACTIVITY_TYPE_URI);
        if (s.getId()==null) {
            s.setId(gensym());
        }
        compilerUtil.specWithComment(mbuilder);
        mbuilder.addComment("qualified mem $N", s.getId().getUri());

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
        StatementTypeAction action2=new StatementTypeAction(pFactory, allVars, allAtts, vmap, null, null, bindings_schema, knownTypes, unknownTypes, mbuilder, compilerUtil);
        
        for (Statement s: bun.getStatement()) {
            provUtilities.doAction(s, action2);

        }

    }

}
