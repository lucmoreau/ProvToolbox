package org.openprovenance.prov.rules;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.rules.counters.ActivityCounter;
import org.openprovenance.prov.rules.counters.EntityActivityDerivationCounter;
import org.openprovenance.prov.rules.counters.EntityCounter;
import org.openprovenance.prov.vanilla.ProvUtilities;

import java.util.*;
import java.util.stream.Collectors;

public class Rules {

    public static final String PATTERN_METRICS = "patternMetrics";
    public static final String SIMPLE_METRICS = "simpleMetrics";
    public static final String COUNT_DERIVATIONS_AND_GENERATIONS_AND_USAGES = "countDerivationsAndGenerationsAndUsages";
    ProvUtilities u = new ProvUtilities();

    /** Within a Document,  the method returns threes counts:
     * the first one counts the number of entities,
     * the second one counts the number of those entities generated by an activity,
     * the third one indicates the number of those entities that are derived from another entity. */


    public EntityCounter countDerivationsAndGenerations(IndexedDocument indexedDocument) {
        Collection<Entity> entities=indexedDocument.getEntities();
        EntityCounter res=new EntityCounter();
        for (Entity entity:entities) {
            res.countEntities++;
            Collection<WasGeneratedBy> wasGeneratedBy = indexedDocument.getWasGeneratedBy(entity);
            Collection<WasDerivedFrom> wasDerivedFrom =indexedDocument.getWasDerivedFromWithEffect(entity);
            if (wasGeneratedBy !=null && !wasGeneratedBy.isEmpty()) {
                res.countGeneratedBy= res.countGeneratedBy + wasGeneratedBy.size();
                if (wasDerivedFrom!=null && !wasDerivedFrom.isEmpty()) {
                    res.countDerivedFrom= res.countDerivedFrom + wasDerivedFrom.size();
                }
            }
        }
        return res;
    }

    /** Within a Document,  the method returns threes counts:
     * the first one counts the number of activities,
     * the second one counts the number of those activities without an association with an agent. */

    public ActivityCounter countActivitiesWithoutAgent(IndexedDocument indexedDocument) {
        Collection<Activity> activities = indexedDocument.getActivities();
        ActivityCounter res = new ActivityCounter();
        for (Activity activity : activities) {
            res.countActivities++;
            Collection<WasAssociatedWith> wasAssociatedWith = indexedDocument.getWasAssociatedWith(activity);
            if (wasAssociatedWith == null || wasAssociatedWith.isEmpty()) {
                res.countWithoutAgent++;
            }
        }
        return res;
    }

    /** Within a Document,  the method returns the following counts:
     * the first one counts the number of entities e2,
     * the second one counts the number of those entities e2 generated by an activity (a),
     * the third one indicates the number of those entities e2 that are derived from another entity e1,
     * the fourth one indicates the number of activity a using e1,
     * the fifth one indicates the number of those derivations referring to a,
     * the sixth one indicates the number of those derivations referring to the generation of e2,
     * the seventh one indicates the number of those derivations referring to the usage of e1. */

    public EntityActivityDerivationCounter countDerivationsAndGenerationsAndUsages(IndexedDocument indexedDocument) {
        Collection<List<Statement>> triangle=new LinkedList<>();
        Collection<Entity> entities=indexedDocument.getEntities();
        Collection<Activity> activities=indexedDocument.getActivities();
        for (Entity e2:entities) {
            Collection<WasGeneratedBy> wgbCollection = indexedDocument.getWasGeneratedBy(e2);
            if (wgbCollection != null) {
                for (WasGeneratedBy wgb : wgbCollection) {
                    QualifiedName qnA = wgb.getActivity();
                    Activity activity = (qnA == null) ? null : indexedDocument.getActivity(qnA);
                    if (activity != null) {
                        Collection<Used> usedCollection = indexedDocument.getUsed(activity);
                        if (usedCollection != null) {
                            for (Used usd : usedCollection) {
                                QualifiedName qnE1 = usd.getEntity();
                                Entity e1 = (qnE1 == null) ? null : indexedDocument.getEntity(qnE1);
                                if (e1 != null) {
                                    Collection<WasDerivedFrom> wdfCollection = indexedDocument.getWasDerivedFromWithCause(e1);//getWasDerivedFromWithEffect(e1);
                                    if (wdfCollection != null) {
                                        for (WasDerivedFrom wdf : wdfCollection) {
                                            if (e2.getId().equals(wdf.getGeneratedEntity())) {
                                                triangle.add(List.of(activity, e1, e2, wdf, wgb, usd));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        indexedDocument.checkActivityUsedMap();
        indexedDocument.checkEntityWasGeneratedByMap();
        indexedDocument.checkEntityCauseWasDerivedFromMap();

        //System.out.println(indexedDocument.entityCauseWasDerivedFromMap);
        System.out.println("=====");

        EntityActivityDerivationCounter res=new EntityActivityDerivationCounter();
        res.triangle=triangle.size();
        res.entities=entities.stream().map(Identifiable::getId).collect(Collectors.toSet()).size();

        Set<QualifiedName> allEntityIds=entities.stream().map(Identifiable::getId).collect(Collectors.toSet());
        indexedDocument.nonRootEntities().forEach(allEntityIds::remove);
        res.rootEntities=allEntityIds.size();
        res.activities=activities.stream().map(Identifiable::getId).collect(Collectors.toSet()).size();
        res.entitiesWithTriangle = triangle.stream().map(l -> ((Entity)l.get(2)).getId()).collect(Collectors.toSet()).size();
        res.activitiesWithTriangle = triangle.stream().map(l -> ((Activity)l.get(0)).getId()).collect(Collectors.toSet()).size();
        for (List<Statement> l:triangle) {
            Activity a=(Activity)l.get(0);
            Entity e1=(Entity)l.get(1);
            Entity e2=(Entity)l.get(2);
            WasDerivedFrom wdf=(WasDerivedFrom)l.get(3);
            WasGeneratedBy wgb=(WasGeneratedBy)l.get(4);
            Used usd=(Used)l.get(5);
            int count=0;
            if (Objects.equals(wdf.getActivity(),a.getId())) {
                res.wdf_with_activity++;
                count++;
            }
            if (wdf.getGeneration()!=null && Objects.equals(wdf.getGeneration(),wgb.getId())) {
                res.wdf_with_wgb++;
                count++;
            }
            if (wdf.getUsage()!=null && Objects.equals(wdf.getUsage(),usd.getId())) {
                res.wdf_with_usd++;
                count++;
            }
            switch (count) {
                case 0:
                    res.count0++;
                    break;
                case 1:
                    res.count1++;
                    break;
                case 2:
                    res.count2++;
                    break;
                case 3:
                    res.count3++;
                    break;
            }
        }
        return res;
    }


    public Object computeMetrics(Document document, ProvFactory pFactory) {
        IndexedDocument indexedDocument = new IndexedDocument(pFactory, document, true);
        Map<String, Object> res = new HashMap<>();

        SimpleMetrics res1 = new SimpleMetrics();
        res1.countEntities = indexedDocument.getEntities().size();
        res1.countActivities = indexedDocument.getActivities().size();
        res1.countAgents = indexedDocument.getAgents().size();
        res1.countWasGeneratedBy = indexedDocument.getWasGeneratedBy().size()+indexedDocument.getNamedWasGeneratedBy().keySet().size();
        res1.countUsed = indexedDocument.getUsed().size()+indexedDocument.getNamedUsed().keySet().size();
        res1.countWasAssociatedWith = indexedDocument.getWasAssociatedWith().size()+indexedDocument.getNamedWasAssociatedWith().keySet().size();
        res1.countWasAttributedTo = indexedDocument.getWasAttributedTo().size()+indexedDocument.getNamedWasAttributedTo().keySet().size();
        res1.countWasDerivedFrom = indexedDocument.getWasDerivedFrom().size()+indexedDocument.getNamedWasDerivedFrom().keySet().size();
        res1.countWasEndedBy = indexedDocument.getWasEndedBy().size();//+indexedDocument.getNamedWasEndedBy().keySet().size();
        res1.countWasInformedBy = indexedDocument.getWasInformedBy().size();
        res1.countWasInvalidatedBy = indexedDocument.getWasInvalidatedBy().size();//+indexedDocument.getNamedWasInvalidatedBy().keySet().size();
        res1.countWasStartedBy = indexedDocument.getWasStartedBy().size();
        res1.countWasInfluencedBy = indexedDocument.getWasInfluencedBy().size();
        res1.countSpecializationOf = indexedDocument.getSpecializationOf().size()+indexedDocument.getNamedSpecializationOf().keySet().size();
        res1.countAlternateOf = indexedDocument.getAlternateOf().size()+indexedDocument.getNamedAlternateOf().keySet().size();
        res1.countHadMember = indexedDocument.getHadMember().size()+indexedDocument.getNamedHadMember().keySet().size();
        res.put(SIMPLE_METRICS, res1);


        Map<String, Object> res2 = new HashMap<>();
        res2.put("countDerivationsAndGenerations", countDerivationsAndGenerations(indexedDocument));
        res2.put("countActivitiesWithoutAgent", countActivitiesWithoutAgent(indexedDocument));
        res2.put(COUNT_DERIVATIONS_AND_GENERATIONS_AND_USAGES, countDerivationsAndGenerationsAndUsages(indexedDocument));
        res.put(PATTERN_METRICS, res2);


        return res;
    }



}
