package org.openprovenance.prov.core.json.serialization;

import org.openprovenance.apache.commons.lang.ArrayUtils;
import org.openprovenance.prov.core.vanilla.QualifiedSpecializationOf;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;

import java.util.LinkedList;
import java.util.List;

public class SortedDocument extends SortedBundle {

    List<Bundle> bundle=new LinkedList<>();

    private SortedDocument() {}

    public SortedDocument (org.openprovenance.prov.model.Document doc)  {
        this.namespace=doc.getNamespace();
        for (StatementOrBundle s: doc.getStatementOrBundle()) {
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
                    bundle.add((Bundle)s);
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


    public List<Bundle> getBundle() {
        return bundle;
    }



    public Document toDocument(ProvFactory provFactory) {
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
        return provFactory.newDocument(namespace,ss, bundle);
    }
}
