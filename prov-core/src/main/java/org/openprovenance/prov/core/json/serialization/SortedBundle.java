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
    List<Activity> activity=new LinkedList();
    List<Agent> agent=new LinkedList();
    List<Used> used=new LinkedList();
    List<WasGeneratedBy> wgb=new LinkedList();
    List<WasInvalidatedBy> wib=new LinkedList();

    List<WasAssociatedWith> wasAssociatedWith=new LinkedList<>();
    List<WasAttributedTo> wasAttributedTo=new LinkedList<>();
    List<ActedOnBehalfOf> actedOnBehalfOf=new LinkedList<>();
    List<WasStartedBy> wasStartedBy=new LinkedList<>();
    List<WasEndedBy> wasEndedBy=new LinkedList<>();
    List<WasInformedBy> wasInformedBy=new LinkedList<>();
    List<WasInfluencedBy> wasInfluencedBy=new LinkedList<>();
    List<AlternateOf> alternateOf=new LinkedList<>();
    List<SpecializationOf> specializationOf=new LinkedList<>();
    List<HadMember> hadMember=new LinkedList<>();
    List<WasDerivedFrom> wasDerivedFrom=new LinkedList<>();
    List<QualifiedSpecializationOf> qualifiedSpecializationOf=new LinkedList<>();
    List<QualifiedAlternateOf> qualifiedAlternateOf=new LinkedList<>();
    List<QualifiedHadMember> qualifiedHadMember=new LinkedList<>();

    private QualifiedName id;

    protected SortedBundle() {}

    public SortedBundle(Bundle bun)  {
        this.namespace=bun.getNamespace();
        this.id=bun.getId();
        for (Statement s: bun.getStatement()) {
            switch (s.getKind()) {
                case PROV_ENTITY:
                    entity.put(((Entity) s).getId(), (Entity) s);
                    break;
                case PROV_ACTIVITY:
                    activity.add((Activity) s);
                    break;
                case PROV_AGENT:
                    agent.add((Agent) s);
                    break;
                case PROV_USAGE:
                    used.add((Used) s);
                    break;
                case PROV_GENERATION:
                    wgb.add((WasGeneratedBy)s);
                    break;
                case PROV_INVALIDATION:
                    wib.add((WasInvalidatedBy) s);
                    break;
                case PROV_START:
                    wasStartedBy.add((WasStartedBy) s);
                    break;
                case PROV_END:
                    wasEndedBy.add((WasEndedBy)s);
                    break;
                case PROV_COMMUNICATION:
                    wasInformedBy.add((WasInformedBy)s);
                    break;
                case PROV_DERIVATION:
                    wasDerivedFrom.add((WasDerivedFrom)s);
                    break;
                case PROV_ASSOCIATION:
                    wasAssociatedWith.add((WasAssociatedWith)s);
                    break;
                case PROV_ATTRIBUTION:
                    wasAttributedTo.add((WasAttributedTo)s);
                    break;
                case PROV_DELEGATION:
                    actedOnBehalfOf.add((ActedOnBehalfOf)s);
                    break;
                case PROV_INFLUENCE:
                    wasInfluencedBy.add((WasInfluencedBy)s);
                    break;
                case PROV_ALTERNATE:
                    if (s instanceof QualifiedRelation) {
                        qualifiedAlternateOf.add((QualifiedAlternateOf) s);
                    } else {
                        alternateOf.add((AlternateOf) s);
                    }
                    break;
                case PROV_SPECIALIZATION:
                    if (s instanceof QualifiedRelation) {
                        qualifiedSpecializationOf.add((QualifiedSpecializationOf) s);
                    } else {
                        specializationOf.add((SpecializationOf) s);
                    }
                case PROV_MENTION:
                    break;
                case PROV_MEMBERSHIP:
                    if (s instanceof QualifiedRelation) {
                        qualifiedHadMember.add((QualifiedHadMember) s);
                    } else {
                        hadMember.add((HadMember) s);
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

    public List<Activity> getActivity() {
        return activity;
    }

    public Map<QualifiedName, Entity> getEntity() {
        return entity;
    }

    public List<Agent> getAgent() {
        return agent;
    }

    public List<Used> getUsed() {
        return used;
    }

    public List<WasGeneratedBy> getWasGeneratedBy() {
        return wgb;
    }

    public List<WasInvalidatedBy> getWasInvalidatedBy() {
        return wib;
    }


    public List<WasAssociatedWith> getWasAssociatedWith() {
        return wasAssociatedWith;
    }

    public List<WasAttributedTo> getWasAttributedTo() {
        return wasAttributedTo;
    }

    public List<ActedOnBehalfOf> getActedOnBehalfOf() {
        return actedOnBehalfOf;
    }

    public List<WasStartedBy> getWasStartedBy() {
        return wasStartedBy;
    }

    public List<WasEndedBy> getWasEndedBy() {
        return wasEndedBy;
    }

    public List<WasInformedBy> getWasInformedBy() {
        return wasInformedBy;
    }

    public List<WasInfluencedBy> getWasInfluencedBy() {
        return wasInfluencedBy;
    }

    public List<AlternateOf> getAlternateOf() {
        return alternateOf;
    }

    public List<SpecializationOf> getSpecializationOf() {
        return specializationOf;
    }

    public List<QualifiedSpecializationOf> getQualifiedSpecializationOf() {
        return qualifiedSpecializationOf;
    }

    public List<HadMember> getHadMember() {
        return hadMember;
    }

    public List<WasDerivedFrom> getWasDerivedFrom() {
        return wasDerivedFrom;
    }

    public List<QualifiedAlternateOf> getQualifiedAlternateOf() {
        return qualifiedAlternateOf;
    }


    public List<QualifiedHadMember> getQualifiedHadMember() {
        return qualifiedHadMember;
    }




    public Bundle toBundle(ProvFactory provFactory) {
        List<Statement> ss=new LinkedList<>();
        ss.addAll(getEntity().values());
        ss.addAll(getActivity());
        ss.addAll(getAgent());
        ss.addAll(getUsed());
        ss.addAll(getWasGeneratedBy());
        ss.addAll(getWasInvalidatedBy());
        ss.addAll(getWasAssociatedWith());
        ss.addAll(getWasAttributedTo());
        ss.addAll(getActedOnBehalfOf());
        ss.addAll(getWasStartedBy());
        ss.addAll(getWasEndedBy());
        ss.addAll(getWasInformedBy());
        ss.addAll(getWasInfluencedBy());
        ss.addAll(getAlternateOf());
        ss.addAll(getSpecializationOf());
        ss.addAll(getHadMember());
        ss.addAll(getWasDerivedFrom());
        ss.addAll(getQualifiedSpecializationOf());
        ss.addAll(getQualifiedAlternateOf());
        ss.addAll(getQualifiedHadMember());
        return provFactory.newNamedBundle(id,namespace,ss);
    }
}
