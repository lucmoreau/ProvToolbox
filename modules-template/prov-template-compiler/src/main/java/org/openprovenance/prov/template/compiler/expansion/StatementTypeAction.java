package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.MethodSpec.Builder;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.template.expander.ExpandUtil;

import java.util.*;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;

public class StatementTypeAction implements StatementAction {

    public static String AGENT_URI=PROV_NS+"Agent";
    public static String ENTITY_URI=PROV_NS+"Entity";
    public static String ACTIVITY_URI=PROV_NS+"Activity";
    public static String BUNDLE_URI=PROV_NS+"Bundle";

    private final JsonNode bindings_schema;
    private final Map<String, Collection<String>> knownTypes;
    private final Map<String, Collection<String>> unknownTypes;
    private Set<QualifiedName> allVars;
    private Set<QualifiedName> allAtts;

    private ProvFactory pFactory;
    private Hashtable<QualifiedName, String> vmap;


    public StatementTypeAction(ProvFactory pFactory, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, Hashtable<QualifiedName, String> vmap, Builder builder, String target, JsonNode bindings_schema, Map<String, Collection<String>> knownTypes, Map<String, Collection<String>> unknownTypes) {
        this.pFactory=pFactory;
        this.allVars=allVars;
        this.allAtts=allAtts;
        //this.builder=builder;
        //this.target=target;
        this.vmap=vmap;
        this.bindings_schema=bindings_schema;
        this.knownTypes=knownTypes;
        this.unknownTypes=unknownTypes;
    }


    public Map<String, Collection<String>> getKnownTypes() {
        return knownTypes;
    }

    public Map<String, Collection<String>> getUnknownTypes() {
        return unknownTypes;
    }


    public void registerTypes(QualifiedName id, List<Type> types) {
        if (id !=null) {
            final String uri = id.getUri();
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
            System.out.println("registering " + id + " type " + type);
            final String uri = id.getUri();
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


    @Override
    public void doAction(Activity s) {
        registerTypes(s.getId(),s.getType());
        registerActivity(s.getId());
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

    @Override
    public void doAction(WasDerivedFrom s) {
        registerTypes(s.getId(),s.getType());
        registerEntity(s.getUsedEntity());
        registerEntity(s.getGeneratedEntity());
        registerActivity(s.getActivity());
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
        StatementTypeAction action2=new StatementTypeAction(pFactory, allVars, allAtts, vmap, null, null, bindings_schema, knownTypes, unknownTypes);
        
        for (Statement s: bun.getStatement()) {
            provUtilities.doAction(s, action2);

        }

    }

}
