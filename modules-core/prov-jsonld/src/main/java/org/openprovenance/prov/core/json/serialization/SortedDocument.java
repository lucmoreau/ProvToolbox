package org.openprovenance.prov.core.json.serialization;


import org.openprovenance.prov.model.*;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SortedDocument extends SortedBundle {

    Map<QualifiedName,Bundle> theBundles = new HashMap<>();

    private SortedDocument() {}

    public SortedDocument (org.openprovenance.prov.model.Document doc)  {
        this.namespace=new Namespace(doc.getNamespace());
        // prov-json assumes default namespace to be listed with the "reserved" prefix "default"
        if (namespace.getDefaultNamespace()!=null) this.namespace.register("default", namespace.getDefaultNamespace());
        for (StatementOrBundle s: doc.getStatementOrBundle()) {
            switch (s.getKind()) {
                case PROV_ENTITY:
                    entity.put(((Entity) s).getId(), (Entity) s);
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
                    put(theBundles,(Bundle)s);
                    break;
                case PROV_DICTIONARY_INSERTION:
                    break;
                case PROV_DICTIONARY_REMOVAL:
                    break;
                case PROV_DICTIONARY_MEMBERSHIP:
                    break;
            }
        }

        if (count>0)
        namespace.register(SortedBundle.bnPrefix,bnNS);

    }


    public Map<QualifiedName, Bundle> getBundle() {
        return theBundles;
    }




    public Document toDocument(ProvFactory provFactory) {


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




        String defaultJsonStyle=namespace.getPrefixes().get("default");
        if (defaultJsonStyle!=null) {
            namespace.unregister("default", defaultJsonStyle);
        }
        namespace.setDefaultNamespace(defaultJsonStyle);
        // return provFactory.newDocument(namespace,ss, reassignId(theBundles).values());
        // NO reassignement here, as it was done in the SortedBundle
        return provFactory.newDocument(namespace,ss,theBundles.values());
    }
}
