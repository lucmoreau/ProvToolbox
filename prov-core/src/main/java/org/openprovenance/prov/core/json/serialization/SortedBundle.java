package org.openprovenance.prov.core.json.serialization;

import org.openprovenance.prov.core.vanilla.QualifiedSpecializationOf;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SortedBundle {
    Map<QualifiedName,Entity> entity=new HashMap<>();
    Namespace namespace=new Namespace();
    Map<QualifiedName,Activity> activity= new HashMap<QualifiedName, Activity>();
    Map<QualifiedName,Agent> agent= new HashMap<QualifiedName, Agent>();
    Map<QualifiedName,Used> used= new HashMap<QualifiedName, Used>();
    Map<QualifiedName,WasGeneratedBy> wgb= new HashMap<QualifiedName, WasGeneratedBy>();
    Map<QualifiedName,WasInvalidatedBy> wib= new HashMap<QualifiedName, WasInvalidatedBy>();

    Map<QualifiedName,WasAssociatedWith> wasAssociatedWith=new HashMap<>();
    Map<QualifiedName,WasAttributedTo> wasAttributedTo=new HashMap<>();
    Map<QualifiedName,ActedOnBehalfOf> actedOnBehalfOf=new HashMap<>();
    Map<QualifiedName,WasStartedBy> wasStartedBy=new HashMap<>();
    Map<QualifiedName,WasEndedBy> wasEndedBy=new HashMap<>();
    Map<QualifiedName,WasInformedBy> wasInformedBy=new HashMap<>();
    Map<QualifiedName,WasInfluencedBy> wasInfluencedBy=new HashMap<>();
    Map<QualifiedName,AlternateOf> alternateOf=new HashMap<>();
    Map<QualifiedName,SpecializationOf> specializationOf=new HashMap<>();
    Map<QualifiedName,HadMember> hadMember=new HashMap<>();
    Map<QualifiedName,WasDerivedFrom> wasDerivedFrom=new HashMap<>();
    Map<QualifiedName,QualifiedSpecializationOf> qualifiedSpecializationOf=new HashMap<>();
    Map<QualifiedName,QualifiedAlternateOf> qualifiedAlternateOf=new HashMap<>();
    Map<QualifiedName,QualifiedHadMember> qualifiedHadMember=new HashMap<>();
    public static String bnNS="https://openprovenance.org/blank#";
    public static int count=0;

    <T extends Statement> void put(Map<QualifiedName,T> map, StatementOrBundle s) {
         if (s instanceof Identifiable) {
             Identifiable iS = (Identifiable) s;
             if (iS.getId()==null) {
                 map.put(gensym(), (T) s);
             } else {
                 map.put(iS.getId(), (T) s);
             }
         } else {
             map.put(gensym(), (T) s);
         }
    }

    public QualifiedName gensym() {
         return new org.openprovenance.prov.core.vanilla.QualifiedName(bnNS, "n" + (count++), "bn");
    }

    private QualifiedName id;

    protected SortedBundle() {}

    public SortedBundle(Bundle bun)  {
        this.namespace=bun.getNamespace();
        this.id=bun.getId();
        for (Statement s: bun.getStatement()) {
            switch (s.getKind()) {
                case PROV_ENTITY:
                    //entity.put(((Entity) s).getId(), (Entity) s);
                    put(entity,s);
                    break;
                case PROV_ACTIVITY:
                    put(activity,s);
                    break;
                case PROV_AGENT:
                    put(agent,s);
                    break;
                case PROV_USAGE:
                    put(used,s);
                    break;
                case PROV_GENERATION:
                    put(wgb,s);
                    break;
                case PROV_INVALIDATION:
                    put(wib,s);
                    break;
                case PROV_START:
                    put(wasStartedBy,s);
                    break;
                case PROV_END:
                    put(wasEndedBy,s);
                    break;
                case PROV_COMMUNICATION:
                    put(wasInformedBy,s);
                    break;
                case PROV_DERIVATION:
                    put(wasDerivedFrom,s);
                    break;
                case PROV_ASSOCIATION:
                    put(wasAssociatedWith,s);
                    break;
                case PROV_ATTRIBUTION:
                    put(wasAttributedTo,s);
                    break;
                case PROV_DELEGATION:
                    put(actedOnBehalfOf,s);
                    break;
                case PROV_INFLUENCE:
                    put(wasInfluencedBy,s);
                    break;
                case PROV_ALTERNATE:
                    if (s instanceof QualifiedRelation) {
                        put(qualifiedAlternateOf,s);
                    } else {
                        put(alternateOf,s);
                    }
                    break;
                case PROV_SPECIALIZATION:
                    if (s instanceof QualifiedRelation) {
                        put(qualifiedSpecializationOf,s);
                    } else {
                        put(specializationOf,s);
                    }
                case PROV_MENTION:
                    break;
                case PROV_MEMBERSHIP:
                    if (s instanceof QualifiedRelation) {
                        put(qualifiedHadMember,s);
                    } else {
                        put(hadMember,s);
                    }
                    break;
                case PROV_BUNDLE:
                    break;
                case PROV_DICTIONARY_INSERTION:
                    break;
                case PROV_DICTIONARY_REMOVAL:
                    break;
                case PROV_DICTIONARY_MEMBERSHIP:
                    break;
            }
        }
    }

    public QualifiedName getId() {return id;}

    public void setId(QualifiedName id) {
        this.id=id;
    }

    public Map<String, String> getPrefix() {
        return namespace.getPrefixes();
    }

    public String getDefaultNamespace() {
        return namespace.getDefaultNamespace();
    }

    public void setPrefix( Map<String, String> ns) {
        for (Map.Entry<String, String> entry : ns.entrySet()) {
            this.namespace.register(entry.getKey(), entry.getValue());
        }
    }

    public void setDefaultNamespace(String name) {
        this.namespace.setDefaultNamespace(name);
    }

    public Map<QualifiedName, Activity> getActivity() {
        return activity;
    }

    public Map<QualifiedName, Entity> getEntity() {
        return entity;
    }

    public Map<QualifiedName, Agent> getAgent() {
        return agent;
    }

    public Map<QualifiedName, Used> getUsed() {
        return used;
    }

    public Map<QualifiedName, WasGeneratedBy> getWasGeneratedBy() {
        return wgb;
    }

    public Map<QualifiedName, WasInvalidatedBy> getWasInvalidatedBy() {
        return wib;
    }


    public Map<QualifiedName,WasAssociatedWith> getWasAssociatedWith() {
        return wasAssociatedWith;
    }

    public Map<QualifiedName,WasAttributedTo> getWasAttributedTo() {
        return wasAttributedTo;
    }

    public Map<QualifiedName,ActedOnBehalfOf> getActedOnBehalfOf() {
        return actedOnBehalfOf;
    }

    public Map<QualifiedName,WasStartedBy> getWasStartedBy() {
        return wasStartedBy;
    }

    public Map<QualifiedName,WasEndedBy> getWasEndedBy() {
        return wasEndedBy;
    }

    public Map<QualifiedName,WasInformedBy> getWasInformedBy() {
        return wasInformedBy;
    }

    public Map<QualifiedName,WasInfluencedBy> getWasInfluencedBy() {
        return wasInfluencedBy;
    }

    public Map<QualifiedName,AlternateOf> getAlternateOf() {
        return alternateOf;
    }

    public Map<QualifiedName,SpecializationOf> getSpecializationOf() {
        return specializationOf;
    }

    public Map<QualifiedName,QualifiedSpecializationOf> getQualifiedSpecializationOf() {
        return qualifiedSpecializationOf;
    }

    public Map<QualifiedName,HadMember> getHadMember() {
        return hadMember;
    }

    public Map<QualifiedName,WasDerivedFrom> getWasDerivedFrom() {
        return wasDerivedFrom;
    }

    public Map<QualifiedName,QualifiedAlternateOf> getQualifiedAlternateOf() {
        return qualifiedAlternateOf;
    }


    public Map<QualifiedName,QualifiedHadMember> getQualifiedHadMember() {
        return qualifiedHadMember;
    }


    public <S extends Identifiable> Map<QualifiedName, S> reassignId(Map<QualifiedName, S> map) {
        for (Map.Entry<QualifiedName, S> entry: map.entrySet()) {
            QualifiedName name=entry.getKey();
            if (!(name.getNamespaceURI().equals(bnNS)) ) {
                entry.getValue().setId(name);
            }
        }
        return map;
    }



    public Bundle toBundle(ProvFactory provFactory) {
        List<Statement> ss=new LinkedList<>();
        ss.addAll(reassignId(getEntity()).values());
        ss.addAll(reassignId(getActivity()).values());
        ss.addAll(reassignId(getAgent()).values());
        ss.addAll(reassignId(getUsed()).values());
        ss.addAll(reassignId(getWasGeneratedBy()).values());
        ss.addAll(reassignId(getWasInvalidatedBy()).values());
        ss.addAll(reassignId(getWasAssociatedWith()).values());
        ss.addAll(reassignId(getWasAttributedTo()).values());
        ss.addAll(reassignId(getActedOnBehalfOf()).values());
        ss.addAll(reassignId(getWasStartedBy()).values());
        ss.addAll(reassignId(getWasEndedBy()).values());
        ss.addAll(reassignId(getWasInformedBy()).values());
        ss.addAll(reassignId(getWasInfluencedBy()).values());
        ss.addAll(getAlternateOf().values());
        ss.addAll(getSpecializationOf().values());
        ss.addAll(getHadMember().values());
        ss.addAll(reassignId(getWasDerivedFrom()).values());
        ss.addAll(reassignId(getQualifiedSpecializationOf()).values());
        ss.addAll(reassignId(getQualifiedAlternateOf()).values());
        ss.addAll(reassignId(getQualifiedHadMember()).values());
        return provFactory.newNamedBundle(id,namespace,ss);
    }
}
