package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.MethodSpec.Builder;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.expander.ExpandUtil;
import org.openprovenance.prov.template.expander.MissingAttributeValue;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;
import static org.openprovenance.prov.template.compiler.expansion.CompilerTypeManagement.Function_O_Col_S;
import static org.openprovenance.prov.template.compiler.expansion.CompilerTypeManagement.Map_S_to_Function;
import static org.openprovenance.prov.template.expander.ExpandUtil.TMPL_ACTIVITY_URI;
import static org.openprovenance.prov.template.expander.ExpandUtil.TMPL_NS;

public class StatementTypeAction implements StatementAction {

    public static String AGENT_URI=PROV_NS+"Agent";
    public static String ENTITY_URI=PROV_NS+"Entity";
    public static String ACTIVITY_URI=PROV_NS+"Activity";
    public static String BUNDLE_URI=PROV_NS+"Bundle";
    public static String WASDERIVEDFROM_URI=PROV_NS+"WasDerivedFrom";

    private final JsonNode bindings_schema;
    private final Map<String, Collection<String>> knownTypes;
    private final Map<String, Collection<String>> unknownTypes;
    private final Builder mbuilder;
    private final CompilerUtil compilerUtil;

    private Set<QualifiedName> allVars;
    private Set<QualifiedName> allAtts;

    private ProvFactory pFactory;
    private Hashtable<QualifiedName, String> vmap;


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
        doRegisterTypesForAttributes(s, pFactory.getAttributes(s), ACTIVITY_URI);
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
        doRegisterTypesForAttributes(s, pFactory.getAttributes(s), AGENT_URI);

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

        mbuilder.addComment("wdf $N", s.getId().getUri());

        registerAType(s.getId(),WASDERIVEDFROM_URI);
        registerTypes(s.getId(), s.getType());

        registerEntity(s.getUsedEntity());
        registerEntity(s.getGeneratedEntity());
        registerActivity(s.getActivity());


        if (qualifiedNames!=null && !qualifiedNames.isEmpty()) {
            registerTypes2(s.getId(), qualifiedNames);

            dynamicRegisterTypes(s, qualifiedNames);
        }

    }


    static int anotherCounter=0;
    private void doRegisterTypesForAttributes(Identifiable s,Collection<Attribute> attributes, String expressionUri) {
        if (ExpandUtil.isGensymVariable(s.getId())) return;
        JsonNode the_var = bindings_schema.get("var");

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
            boolean first_encounter=true;
            if (tmp_Conv2==null) {
                tmp_Conv2 = tmp_Conv + "_" + cleanUpName(attributeUri);
                seen.put(attributeUri, tmp_Conv2);
            } else {
                first_encounter=false;
            }
            if (first_encounter) mbuilder.addStatement("$T $N=$N.get($S)", Function_O_Col_S, tmp_Conv2, tmp_Conv, attributeUri);
            mbuilder.beginControlFlow("if ($N!=null) ", tmp_Conv2);
            mbuilder.addStatement("unknownTypeMap.computeIfAbsent($N, k -> new HashSet<>())",s.getId().getLocalPart());
            if (value instanceof QualifiedName) {
                QualifiedName qn=(QualifiedName) value;
                if (ExpandUtil.isVariable(qn)) {
                    String key=qn.getLocalPart();
                    final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(the_var, key);
                    if (atype.equals(QualifiedName.class)) {
                        mbuilder.addStatement("if ($N!=null) unknownTypeMap.get($N).addAll($N.apply($N.getUri()))", key, s.getId().getLocalPart(), tmp_Conv2, key);
                    } else {
                        mbuilder.addStatement("if ($N!=null) unknownTypeMap.get($N).addAll($N.apply($N))", key, s.getId().getLocalPart(), tmp_Conv2, key);
                    }
                } else {
                    mbuilder.addStatement("knownTypeMap.get($N).addAll($N.apply($S))", s.getId().getLocalPart(), tmp_Conv2, qn.getUri());
                }
            } else if ((value instanceof String)  || (value instanceof LangString) || (value instanceof Integer)) {
                String aString=String.valueOf(value);
                mbuilder.addStatement("unknownTypeMap.get($N).addAll($N.apply($S))", s.getId().getLocalPart(), tmp_Conv2, aString);
            } else {
                throw new UnsupportedOperationException("doRegisterTypesForAttributes with attribute value " + value + " for element " +attributeUri);
            }
            mbuilder.endControlFlow();
        });

        mbuilder.endControlFlow();
    }

    private String cleanUpName(String elementName) {
        return Base64.getEncoder().withoutPadding().encodeToString(elementName.getBytes(StandardCharsets.UTF_8));
    }

    private void dynamicRegisterTypes(Identifiable s, Collection<QualifiedName> qualifiedNames) {
        if (qualifiedNames==null) return;
        String tmp="_tmp_"+ s.getId().getLocalPart();
        Collection<QualifiedName> activities=doCollectElementVariables((Statement) s, TMPL_ACTIVITY_URI);
        if (activities==null || activities.isEmpty()) throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in relation " + s);
        final String localPart = s.getId().getLocalPart() + "." ;
        String suffix = activities.stream().findFirst().get().getLocalPart();
        mbuilder.addStatement("$T $N=pf.newQualifiedName($S,$S+$N.getLocalPart(),$S)", QualifiedName.class, tmp, s.getId().getNamespaceURI(), localPart, suffix, s.getId().getPrefix());
        mbuilder.addStatement("knownTypeMap.computeIfAbsent($N, k -> new $T<>())", tmp, HashSet.class);
        mbuilder.addStatement("knownTypeMap.get($N).add($S)", tmp, WASDERIVEDFROM_URI);

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
    public void doAction(Entity s) {
        registerTypes(s.getId(),s.getType());
        registerEntity(s.getId());
        doRegisterTypesForAttributes(s, pFactory.getAttributes(s), ENTITY_URI);

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
        registerTypes(s.getId(),s.getType());
        registerEntity(s.getCollection());
        s.getEntity().forEach(this::registerEntity);
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
