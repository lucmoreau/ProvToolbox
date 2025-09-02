package org.openprovenance.prov.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

import javax.xml.datatype.XMLGregorianCalendar;


/** This class provides a set of indexes over information contained in
 * an Document, facilitating its navigation.  Its constructor takes an
 * Document builds an index for it.  Of course, for the index to be
 * maintained, one cannot access, say the list of edges, and mutate
 * it. Instead, one has to use the add methods provided.
 *<p>
 * Note that code is not thread-safe.

 TODO: index annotation, index edges

 */

public class IndexedDocument implements StatementAction {



    ProvUtilities u=new ProvUtilities();
    final ProvFactory pFactory;


    private final HashMap<QualifiedName,Entity>   entityMap= new HashMap<>();
    private final HashMap<QualifiedName,Activity> activityMap= new HashMap<>();
    private final HashMap<QualifiedName,Agent>    agentMap= new HashMap<>();

    /* Collection of Used edges that have a given process as an
     * effect. */
    private final HashMap<QualifiedName,Collection<Used>> activityUsedMap=new HashMap<>();

    /* Collection of Used edges that have a given entity as a
     * cause. */
    private final HashMap<QualifiedName,Collection<Used>> entityUsedMap= new HashMap<>();
    private final Collection<Used> anonUsed= new LinkedList<>();
    private final HashMap<QualifiedName,Collection<Used>> namedUsedMap= new HashMap<>();


    /* Collection of WasGeneratedBy edges that have a given activity as a
     * cause. */
    private final HashMap<QualifiedName,Collection<WasGeneratedBy>> activityWasGeneratedByMap= new HashMap<>();

    /* Collection of WasGeneratedBy edges that have a given entity as an
     * effect. */
    private final HashMap<QualifiedName,Collection<WasGeneratedBy>> entityWasGeneratedByMap= new HashMap<>();
    private final Collection<WasGeneratedBy> anonWasGeneratedBy= new LinkedList<>();
    private final HashMap<QualifiedName,Collection<WasGeneratedBy>> namedWasGeneratedByMap= new HashMap<>();


    /* Collection of WasDerivedFrom edges that have a given entity as a cause. */
    private final HashMap<QualifiedName,Collection<WasDerivedFrom>> entityCauseWasDerivedFromMap= new HashMap<>();

    /* Collection of WasDerivedFrom edges that have a given entity as an
     * effect. */
    private final HashMap<QualifiedName,Collection<WasDerivedFrom>> entityEffectWasDerivedFromMap= new HashMap<>();
    private final Collection<WasDerivedFrom> anonWasDerivedFrom= new LinkedList<>();
    private final HashMap<QualifiedName,Collection<WasDerivedFrom>> namedWasDerivedFromMap= new HashMap<>();


    /* Collection of WasAssociatedWith edges that have a given activity as an
     * effect. */
    private final HashMap<QualifiedName,Collection<WasAssociatedWith>> activityWasAssociatedWithMap= new HashMap<>();

    /* Collection of WasAssociatedWith edges that have a given agent as a
     * cause. */
    private final HashMap<QualifiedName,Collection<WasAssociatedWith>> agentWasAssociatedWithMap= new HashMap<>();
    private final Collection<WasAssociatedWith> anonWasAssociatedWith= new LinkedList<>();
    private final HashMap<QualifiedName,Collection<WasAssociatedWith>> namedWasAssociatedWithMap= new HashMap<>();


    /* Collection of WasAttributedTo edges that have a given entity as an
     * effect. */
    private final HashMap<QualifiedName,Collection<WasAttributedTo>> entityWasAttributedToMap= new HashMap<>();

    /* Collection of WasAttributedTo edges that have a given agent as a
     * cause. */
    private final HashMap<QualifiedName,Collection<WasAttributedTo>> agentWasAttributedToMap= new HashMap<>();
    private final Collection<WasAttributedTo> anonWasAttributedTo= new LinkedList<>();
    private final HashMap<QualifiedName,Collection<WasAttributedTo>> namedWasAttributedToMap= new HashMap<>();


    /* Collection of WasInformedBy edges that have a given activity as a cause. */
    private final HashMap<QualifiedName,Collection<WasInformedBy>> activityCauseWasInformedByMap= new HashMap<>();

    /* Collection of WasInformedBy edges that have a given activity as an
     * effect. */
    private final HashMap<QualifiedName,Collection<WasInformedBy>> activityEffectWasInformedByMap= new HashMap<>();
    private final Collection<WasInformedBy> anonWasInformedBy= new LinkedList<>();
    private final HashMap<QualifiedName,Collection<WasInformedBy>> namedWasInformedByMap= new HashMap<>();

    private Namespace nss;
    private final boolean flatten;

    private final Collection<ActedOnBehalfOf> anonActedOnBehalfOf= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<ActedOnBehalfOf>> namedActedOnBehalfOfMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<ActedOnBehalfOf>> responsibleActedOnBehalfOfMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<ActedOnBehalfOf>> delegateActedOnBehalfOfMap= new HashMap<>();


    private final HashMap<QualifiedName, Collection<WasInvalidatedBy>> namedWasInvalidatedByMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<WasInvalidatedBy>> entityWasInvalidatedByMap= new HashMap<>();
    private final Collection<WasInvalidatedBy> anonWasInvalidatedBy= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<WasInvalidatedBy>> activityWasInvalidatedByMap= new HashMap<>();

    private final HashMap<QualifiedName, Collection<SpecializationOf>> namedSpecializationOfMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<SpecializationOf>> specificEntitySpecializationOfMap= new HashMap<>();
    private final Collection<SpecializationOf> anonSpecializationOf= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<SpecializationOf>> genericEntitySpecializationOfMap= new HashMap<>();


    private final HashMap<QualifiedName, Collection<QualifiedSpecializationOf>> namedQualifiedSpecializationOfMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<QualifiedSpecializationOf>> specificEntityQualifiedSpecializationOfMap= new HashMap<>();
    private final Collection<QualifiedSpecializationOf> anonQualifiedSpecializationOf= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<QualifiedSpecializationOf>> genericEntityQualifiedSpecializationOfMap= new HashMap<>();

    private final Collection<AlternateOf> anonAlternateOf= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<AlternateOf>> namedAlternateOfMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<AlternateOf>> entityCauseAlternateOfMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<AlternateOf>> entityEffectAlternateOfMap= new HashMap<>();

    private final HashMap<QualifiedName, Collection<WasInfluencedBy>> influenceeWasInfluencedByMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<WasInfluencedBy>> influencerWasInfluencedByMap= new HashMap<>();
    private final Collection<WasInfluencedBy> anonWasInfluencedBy= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<WasInfluencedBy>> namedWasInfluencedByMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<WasStartedBy>> activityWasStartedByMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<WasStartedBy>> entityWasStartedByMap= new HashMap<>();
    private final Collection<WasStartedBy> anonWasStartedBy= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<WasStartedBy>> namedWasStartedByMap= new HashMap<>();
    private final Collection<WasEndedBy> anonWasEndedBy= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<WasEndedBy>> activityWasEndedByMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<WasEndedBy>> namedWasEndedByMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<WasEndedBy>> entityWasEndedByMap= new HashMap<>();
    private final Collection<HadMember> anonHadMember= new LinkedList<>();
    private final HashMap<QualifiedName, Collection<HadMember>> collHadMemberMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<HadMember>> namedHadMemberMap= new HashMap<>();
    private final HashMap<QualifiedName, Collection<HadMember>> entityHadMemberMap= new HashMap<>();


    /** Return all anonymous used edges for this graph.
     * @return a collection of {@link Used} edges
     * */
    public Collection<Used> getUsed() {
        return anonUsed;
    }

    /**
     * Return all named used edges for this graph.
     *
     * @return a collection of {@link Used} edges
     */
    public Map<QualifiedName, Collection<Used>> getNamedUsed() {
        return namedUsedMap;
    }
    /** Return all used edges with activity p as an effect.
     * @param p an activity
     * @return a collection of {@link Used} edges
     *  */
    public Collection<Used> getUsed(Activity p) {
        return activityUsedMap.get(p.getId());
    }

    /** Return all used edges with entity a as a cause.
     *  @param p an entity
     *  @return a collection of {@link Used} edges
     *  */
    public Collection<Used> getUsed(Entity p) {
        return entityUsedMap.get(p.getId());
    }

    /** Return all anonymous WasGeneratedBy edges for this graph.
     *
     *  @return a collection of {@link WasGeneratedBy} edges
     *  */
    public Collection<WasGeneratedBy> getWasGeneratedBy() {
        return anonWasGeneratedBy;
    }

    /**
     * Return all named WasGeneratedBy edges for this graph.
     *
     * @return a map of {@link WasGeneratedBy} edges
     */
    public Map<QualifiedName, Collection<WasGeneratedBy>> getNamedWasGeneratedBy() {
        return namedWasGeneratedByMap;
    }

    /** Return all WasGeneratedBy edges with activity p as an effect.
     * @param p an activty
     * @return a collection of {@link WasGeneratedBy} edges
     * */
    public Collection<WasGeneratedBy> getWasGeneratedBy(Activity p) {
        return activityWasGeneratedByMap.get(p.getId());
    }

    /** Return all WasGeneratedBy edges with entity a as a cause.
     * @param p an entity
     * @return a collection of {@link WasGeneratedBy} edges
     * */
    public Collection<WasGeneratedBy> getWasGeneratedBy(Entity p) {
        return entityWasGeneratedByMap.get(p.getId());
    }

    /** Return all anonymous WasDerivedFrom edges for this graph.
     * @return a collection of {@link WasDerivedFrom} edges
     * */
    public Collection<WasDerivedFrom> getWasDerivedFrom() {
        return anonWasDerivedFrom;
    }

    /**
     * Return all named WasDerivedFrom edges for this graph.
     *
     * @return a collection of {@link WasDerivedFrom} edges
     */
    public Map<QualifiedName, Collection<WasDerivedFrom>> getNamedWasDerivedFrom() {
        return namedWasDerivedFromMap;
    }

    /**
     * Return all anonymous WasAttributedTo edges for this graph.
     *
     * @return a collection of {@link WasDerivedFrom} edges
     */
    public Collection<WasAttributedTo> getWasAttributedTo() {
        return anonWasAttributedTo;
    }

    /**
     * Return all named WasAttributedTo edges for this graph.
     *
     * @return a collection of {@link WasAttributedTo} edges
     */
    public Map<QualifiedName, Collection<WasAttributedTo>> getNamedWasAttributedTo() {
        return namedWasAttributedToMap;
    }

    /**
     * Return all anonymous ActedOnBehalfOf edges for this graph.
     *
     * @return a collection of {@link ActedOnBehalfOf} edges
     */
    public Collection<ActedOnBehalfOf> getActedOnBehalfOf() {
        return anonActedOnBehalfOf;
    }

    /**
     * Return all named ActedOnBehalfOf edges for this graph.
     *
     * @return a map of {@link ActedOnBehalfOf} edges
     */
    public Map<QualifiedName, Collection<ActedOnBehalfOf>> getNamedActedOnBehalfOf() {
        return namedActedOnBehalfOfMap;
    }

    /**
     * Return all anonymous SpecializationOf edges for this graph.
     *
     * @return a collection of {@link SpecializationOf} edges
     */
    public Collection<SpecializationOf> getSpecializationOf() {
        return anonSpecializationOf;
    }

    /**
     * Return all named SpecializationOf edges for this graph.
     *
     * @return a map of {@link SpecializationOf} edges
     */
    public Map<QualifiedName, Collection<SpecializationOf>> getNamedSpecializationOf() {
        return namedSpecializationOfMap;
    }

    /**
     * Return all anonymous HadMember edges for this graph.
     *
     * @return a collection of {@link HadMember} edges
     */
    public Collection<HadMember> getHadMember() {
        return anonHadMember;
    }

    /**
     * Return all named HadMember edges for this graph.
     *
     * @return a map of {@link HadMember} edges
     */
    public Map<QualifiedName, Collection<HadMember>> getNamedHadMember() {
        return namedHadMemberMap;
    }



    /**
     * Return all anonymous AlternateOf edges for this graph.
     *
     * @return a collection of {@link AlternateOf} edges
     */
    public Collection<AlternateOf> getAlternateOf() {
        return anonAlternateOf;
    }

    /**
     * Return all named AlternateOf edges for this graph.
     *
     * @return a map of {@link AlternateOf} edges
     */
    public Map<QualifiedName, Collection<AlternateOf>> getNamedAlternateOf() {
        return namedAlternateOfMap;
    }

    /** Return all WasDerivedFrom edges with entity a as a cause.
     *  @param a an entity
     * @return a collection of {@link WasDerivedFrom} edges*/
    public Collection<WasDerivedFrom> getWasDerivedFromWithCause(Entity a) {
        return entityCauseWasDerivedFromMap.get(a.getId());
    }

    /** Return all WasDerivedFrom edges with entity a as an effect.
     *  @param a an entity
     *  @return a collection of {@link WasDerivedFrom} edges */
    public Collection<WasDerivedFrom> getWasDerivedFromWithEffect(Entity a) {
        return entityEffectWasDerivedFromMap.get(a.getId());
    }

    /** Return all WasInformedBy edges for this graph.
     *  @return a collection of {@link WasInformedBy} edges*/
    public Collection<WasInformedBy> getWasInformedBy() {
        return anonWasInformedBy;
    }

    /** Return all WasInformedBy edges with activity p as a cause.
     * @param a an activity
     * @return a collection of {@link WasInformedBy} edges
     * */
    public Collection<WasInformedBy> getWasInformedByWithCause(Activity a) {
        return activityCauseWasInformedByMap.get(a.getId());
    }

    /** Return all WasInformedBy edges with activity a as an effect.
     *  @param a an activity
     * @return a collection of {@link WasInformedBy} edges*/
    public Collection<WasInformedBy> getWasInformedByWithEffect(Activity a) {
        return activityEffectWasInformedByMap.get(a.getId());
    }

    /** Return all anonymous WasAssociatedWith edges for this graph.
     * @return  a collection of  {@link WasAssociatedWith} edges */
    public Collection<WasAssociatedWith> getWasAssociatedWith() {
        return anonWasAssociatedWith;
    }

    public Map<QualifiedName, Collection<WasAssociatedWith>> getNamedWasAssociatedWith() {
        return namedWasAssociatedWithMap;
    }

    /** Return all WasAssociatedWith edges with activity p as an effect.
     * @param p an activity
     * @return  a collection of  {@link WasAssociatedWith} edges
     * */
    public Collection<WasAssociatedWith> getWasAssociatedWith(Activity p) {
        return activityWasAssociatedWithMap.get(p.getId());
    }

    /** Return all WasAssociatedWith edges with entity a as a cause.
     * @param a an agent
     * @return  a collection of  {@link WasAssociatedWith} edges*/
    public Collection<WasAssociatedWith> getWasAssociatedWith(Agent a) {
        return agentWasAssociatedWithMap.get(a.getId());

    }




    public Entity add(Entity entity) {
        return add(entity.getId(),entity);
    }
    public Entity add(QualifiedName name, Entity entity) {
        Entity existing=entityMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,entity);
            return existing;
        } else {
            entityMap.put(name,entity);
            return entity;
        }
    }



    <T extends Element> void  mergeAttributes(T existing, T newElement) {
        Set<LangString> set=new HashSet<>(newElement.getLabel());
        set.removeAll(existing.getLabel());
        existing.getLabel().addAll(set);

        Set<Location> set2=new HashSet<>(newElement.getLocation());
        set2.removeAll(existing.getLocation());
        existing.getLocation().addAll(set2);

        Set<Type> set3=new HashSet<>(newElement.getType());
        set3.removeAll(existing.getType());
        existing.getType().addAll(set3);

        Set<Other> set4=new HashSet<>(newElement.getOther());
        set4.removeAll(existing.getOther());
        existing.getOther().addAll(set4);

        if (existing instanceof Activity) {
            Activity existing2=(Activity) existing;
            Activity newElement2=(Activity) newElement;
            if (existing2.getStartTime()==null) {
                XMLGregorianCalendar startTime = newElement2.getStartTime();
                if (startTime !=null) {
                    existing2.setStartTime(startTime);
                }
            }
            // if existing2 has a start time, we do not override it
            if (existing2.getEndTime()==null) {
                XMLGregorianCalendar endTime = newElement2.getEndTime();
                if (endTime !=null) {
                    existing2.setEndTime(endTime);
                }
            }
            // if existing2 has an end time, we do not override it
        }

        if (existing instanceof Entity) {
            Entity existing2=(Entity) existing;
            Entity newElement2=(Entity) newElement;
            if (existing2.getValue()==null) {
                Value value = newElement2.getValue();
                if (value !=null) {
                    existing2.setValue(value);
                }
            }
            // if existing2 has a value, we do not override it
        }
    }

    void mergeAttributes(Influence existing, Influence newElement) {
        Set<LangString> set=new HashSet<>(newElement.getLabel());
        set.removeAll(existing.getLabel());
        existing.getLabel().addAll(set);

        if (existing instanceof HasLocation) {
            HasLocation existing2=(HasLocation) existing;
            Set<Location> set2=new HashSet<>(((HasLocation)newElement).getLocation());
            set2.removeAll(existing2.getLocation());
            existing2.getLocation().addAll(set2);
        }

        Set<Type> set3=new HashSet<Type>(newElement.getType());
        set3.removeAll(existing.getType());
        existing.getType().addAll(set3);

        Set<Other> set4=new HashSet<Other>(newElement.getOther());
        set4.removeAll(existing.getOther());
        existing.getOther().addAll(set4);
    }
    <T extends Statement> void mergeAttributes(T existing, T newElement) {
        if (existing instanceof Element) {
            mergeAttributes((Element) existing, (Element) newElement);
            return;
        }
        if (existing instanceof Influence) {
            mergeAttributes((Influence) existing, (Influence) newElement);
            return;
        }
        throw new UnsupportedOperationException();
    }
    boolean sameEdge(Statement existing, Statement newElement, int count) {
        boolean ok=true;
        for (int i=1; i<=count; i++) {
            Object qn1 = u.getter(existing,i);
            Object qn2 = u.getter(newElement,i);
            if (qn1==null) {
                if (qn2==null) {

                } else {
                    ok=false;
                    break;
                }
            } else {
                if (qn2==null) {
                    ok=false;
                    break;
                } else {
                    if (!qn1.equals(qn2)) {
                        ok=false;
                        break;
                    }
                }
            }
        }
        return ok;
    }

    public Agent add(Agent agent) {
        return add(agent.getId(),agent);
    }
    public Agent add(QualifiedName name, Agent agent) {
        Agent existing=agentMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,agent);
            return existing;
        } else {
            agentMap.put(name,agent);
            return agent;
        }
    }

    public Activity add(Activity activity) {
        return add(activity.getId(),activity);
    }

    public Activity add(QualifiedName name, Activity activity) {
        Activity existing=activityMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,activity);
            return existing;
        } else {
            activityMap.put(name,activity);
            return activity;
        }
    }

    public Activity getActivity(QualifiedName name) {
        return activityMap.get(name);
    }
    public Entity getEntity(QualifiedName name) {
        return entityMap.get(name);
    }
    public Agent getAgent(QualifiedName name) {
        return agentMap.get(name);
    }
    public IndexedDocument(ProvFactory pFactory, Document doc) {
        this(pFactory,doc,true);
    }

    public IndexedDocument(ProvFactory pFactory, Document doc, boolean flatten) {
        this.pFactory=pFactory;
        this.flatten=flatten;

        if (doc!=null) {
            this.nss=doc.getNamespace();
            u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);
        }

    }



    public WasInformedBy add(WasInformedBy wib) {
        return add(wib, 2, anonWasInformedBy, namedWasInformedByMap, activityEffectWasInformedByMap, activityCauseWasInformedByMap);
    }
    public Used add(Used used) {
        return add(used, 3, anonUsed, namedUsedMap, activityUsedMap, entityUsedMap);
    }
    public WasGeneratedBy add(WasGeneratedBy wgb) {
        return add(wgb, 3, anonWasGeneratedBy, namedWasGeneratedByMap, entityWasGeneratedByMap, activityWasGeneratedByMap);
    }
    public WasDerivedFrom add(WasDerivedFrom wdf) {
        return add(wdf, 5, anonWasDerivedFrom, namedWasDerivedFromMap, entityEffectWasDerivedFromMap, entityCauseWasDerivedFromMap);
    }
    public WasAssociatedWith add(WasAssociatedWith waw) {
        return add(waw, 3, anonWasAssociatedWith, namedWasAssociatedWithMap, activityWasAssociatedWithMap, agentWasAssociatedWithMap);
    }
    public WasAttributedTo add(WasAttributedTo wat) {
        return add(wat, 2, anonWasAttributedTo, namedWasAttributedToMap, entityWasAttributedToMap, agentWasAttributedToMap);
    }
    public ActedOnBehalfOf add(ActedOnBehalfOf act) {
        return add(act, 3, anonActedOnBehalfOf, namedActedOnBehalfOfMap, delegateActedOnBehalfOfMap, responsibleActedOnBehalfOfMap);
    }
    public WasInvalidatedBy add(WasInvalidatedBy wib) {
        return add(wib, 3, anonWasInvalidatedBy, namedWasInvalidatedByMap, entityWasInvalidatedByMap, activityWasInvalidatedByMap);
    }
    public SpecializationOf add(SpecializationOf spec) {
        return add(spec, 2, anonSpecializationOf, namedSpecializationOfMap, specificEntitySpecializationOfMap, genericEntitySpecializationOfMap);
    }
    public QualifiedSpecializationOf add(QualifiedSpecializationOf spec) {
        return add(spec, 2, anonQualifiedSpecializationOf, namedQualifiedSpecializationOfMap, specificEntityQualifiedSpecializationOfMap, genericEntityQualifiedSpecializationOfMap);
    }
    public AlternateOf add(AlternateOf alt) {
        return add(alt, 2, anonAlternateOf, namedAlternateOfMap, entityEffectAlternateOfMap,entityCauseAlternateOfMap);
    }
    public WasInfluencedBy add(WasInfluencedBy winf) {
        return add(winf, 2, anonWasInfluencedBy, namedWasInfluencedByMap, influenceeWasInfluencedByMap, influencerWasInfluencedByMap);
    }
    public WasStartedBy add(WasStartedBy wsb) {
        return add(wsb, 4, anonWasStartedBy, namedWasStartedByMap, activityWasStartedByMap, entityWasStartedByMap);
    }
    public WasEndedBy add(WasEndedBy web) {
        return add(web, 4, anonWasEndedBy, namedWasEndedByMap, activityWasEndedByMap, entityWasEndedByMap);
    }
    public HadMember add(HadMember hm) {
        return add(hm, 2, anonHadMember, namedHadMemberMap, collHadMemberMap, entityHadMemberMap);
    }


    /** Add an  edge to the graph. Update namedRelationMap, effectRelationMap and causeRelationMap, accordingly.
     Edges with different attributes are considered distinct.
        @param <T> the type of the edge to be added
        @param statement the edge to be added
        @param num an integer
        @param anonRelationCollection the collection of anonymous edges
        @param namedRelationMap the collection of named edges
        @param effectRelationMap the collection of edges indexed by their effect
        @param causeRelationMap the collection of edges indexed by their cause
        @return the edge that was added
     */

    public <T extends Relation> T add(T statement,
                                      int num,
                                      Collection<T> anonRelationCollection,
                                      HashMap<QualifiedName, Collection<T>> namedRelationMap,
                                      HashMap<QualifiedName, Collection<T>> effectRelationMap,
                                      HashMap<QualifiedName, Collection<T>> causeRelationMap) {
        QualifiedName aid2 = u.getEffect(statement); //wib.getInformed();
        QualifiedName aid1 = u.getCause(statement); //wib.getInformant();

        statement = pFactory.newStatement(statement); // clone

        QualifiedName id;
        if (statement instanceof Identifiable) {
            id=((Identifiable)statement).getId();
        } else {
            id=null;
        }

        {

            boolean found = false;
            Collection<T> relationCollection = effectRelationMap.get(aid2);
            if (relationCollection == null) {
                relationCollection = new LinkedList<T>();
                relationCollection.add(statement);
                effectRelationMap.put(aid2, relationCollection);
            } else {
                for (T u : relationCollection) {
                    if (u.equals(statement)) {
                        found = true;
                        statement = u;
                        break;
                    }
                }
                if (!found) {
                    relationCollection.add(statement);
                }
            }


            boolean found2 = false;
            Collection<T> relationCollection2 = causeRelationMap.get(aid1);
            if (relationCollection2 == null) {
                relationCollection2 = new LinkedList<T>();
                relationCollection2.add(statement);
                causeRelationMap.put(aid1, relationCollection2);
            } else {
                for (T u : relationCollection2) {
                    if (u.equals(statement)) {
                        found2 = true;
                        statement = u;
                        break;
                    }
                }
                if (!found2) {
                    // if we had not found it in the first table, then we
                    // have to add it here too
                    relationCollection2.add(statement);
                }
            }

            if ((id==null) && (!found || !found2)) {
                anonRelationCollection.add(statement);
            }

        }
        if (id!=null){
            Collection<T> relationCollection=namedRelationMap.get(id);
            if (relationCollection==null) {
                relationCollection=new LinkedList<T>();
                relationCollection.add(statement);
                namedRelationMap.put(id, relationCollection);
            } else {
                boolean found=false;
                for (T u1: relationCollection) {
                    if (sameEdge(u1,statement,num)) {
                        found=true;
                        mergeAttributes(u1, statement);
                        break;
                    }
                }
                if (!found) {
                    relationCollection.add(statement);
                }
            }
        }
        return statement;
    }


    @Override
    public void doAction(Activity s) {
        add(s);

    }
    @Override
    public void doAction(Used s) {
        add(s);
    }
    @Override
    public void doAction(WasStartedBy s) {
        add(s);
    }
    @Override
    public void doAction(Agent s) {
        add(s);
    }
    @Override
    public void doAction(AlternateOf s) {
        add(s);
    }
    @Override
    public void doAction(WasAssociatedWith s) {
        add(s);
    }
    @Override
    public void doAction(WasAttributedTo s) {
        add(s);
    }
    @Override
    public void doAction(WasInfluencedBy s) {
        add(s);
    }
    @Override
    public void doAction(ActedOnBehalfOf s) {
        add(s);
    }
    @Override
    public void doAction(WasDerivedFrom s) {
        add(s);
    }
    @Override
    public void doAction(WasEndedBy s) {
        add(s);
    }
    @Override
    public void doAction(Entity s) {
        add(s);
    }
    @Override
    public void doAction(WasGeneratedBy s) {
        add(s);
    }
    @Override
    public void doAction(WasInvalidatedBy s) {
        add(s);
    }
    @Override
    public void doAction(HadMember s) {
        add(s);
    }
    @Override
    public void doAction(MentionOf s) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void doAction(SpecializationOf s) {
        add(s);
    }
    @Override
    public void doAction(QualifiedSpecializationOf s) {
        add(s);
    }
    public void doAction(QualifiedAlternateOf s) {
        add(s);
    }
    public void doAction(QualifiedHadMember s) {
        add(s);
    }
    @Override
    public void doAction(WasInformedBy s) {
        add(s);
    }
    @Override
    public void doAction(DerivedByInsertionFrom s) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void doAction(DictionaryMembership s) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void doAction(DerivedByRemovalFrom s) {
        throw new UnsupportedOperationException();
    }

    HashMap<QualifiedName,IndexedDocument> bundleMap= new HashMap<>();

    @Override
    public void doAction(Bundle bun, ProvUtilities provUtilities) {
        if (flatten) {
            provUtilities.forAllStatement(bun.getStatement(), this);
        } else {
            IndexedDocument iDoc=bundleMap.get(bun.getId());
            if (iDoc==null) {
                iDoc=new IndexedDocument(pFactory, null,flatten);
                bundleMap.put(bun.getId(),iDoc);
            }
            u.forAllStatement(bun.getStatement(), iDoc);
        }
    }

    public Document toDocument() {
        Document res=pFactory.newDocument();
        List<StatementOrBundle> statementOrBundle = res.getStatementOrBundle();

        toContainer(statementOrBundle);

        if (!flatten) {
            for (QualifiedName bunId: bundleMap.keySet()) {
                IndexedDocument idoc=bundleMap.get(bunId);
                Bundle bun=pFactory.newNamedBundle(bunId, null);
                List<StatementOrBundle> ll=new LinkedList<StatementOrBundle>();
                idoc.toContainer(ll);
                for (StatementOrBundle s: ll) {
                    bun.getStatement().add((Statement) s);
                }
                bun.setNamespace(Namespace.gatherNamespaces(bun));

                statementOrBundle.add(bun);
            }
        }
        res.setNamespace(Namespace.gatherNamespaces(res));
        return res;
    }
    private void toContainer(List<StatementOrBundle> statementOrBundle) {
        statementOrBundle.addAll(entityMap.values());
        statementOrBundle.addAll(activityMap.values());
        statementOrBundle.addAll(agentMap.values());

        statementOrBundle.addAll(anonUsed);
        for (Collection<Used> c: namedUsedMap.values()) {
            statementOrBundle.addAll(c);
        }
        statementOrBundle.addAll(anonWasGeneratedBy);
        for (Collection<WasGeneratedBy> c: namedWasGeneratedByMap.values()) {
            statementOrBundle.addAll(c);
        }
        statementOrBundle.addAll(anonWasDerivedFrom);
        for (Collection<WasDerivedFrom> c: namedWasDerivedFromMap.values()) {
            statementOrBundle.addAll(c);
        }
        statementOrBundle.addAll(anonWasAssociatedWith);
        for (Collection<WasAssociatedWith> c: namedWasAssociatedWithMap.values()) {
            statementOrBundle.addAll(c);
        }
        statementOrBundle.addAll(anonWasAttributedTo);
        for (Collection<WasAttributedTo> c: namedWasAttributedToMap.values()) {
            statementOrBundle.addAll(c);
        }
        statementOrBundle.addAll(anonWasInformedBy);
        for (Collection<WasInformedBy> c: namedWasInformedByMap.values()) {
            statementOrBundle.addAll(c);
        }

        statementOrBundle.addAll(anonSpecializationOf);
        statementOrBundle.addAll(anonQualifiedSpecializationOf);
        for (Collection<QualifiedSpecializationOf> c: namedQualifiedSpecializationOfMap.values()) {
            statementOrBundle.addAll(c);
        }
        statementOrBundle.addAll(anonAlternateOf);
        statementOrBundle.addAll(anonHadMember);

        statementOrBundle.addAll(anonWasInvalidatedBy);
        for (Collection<WasInvalidatedBy> c: namedWasInvalidatedByMap.values()) {
            statementOrBundle.addAll(c);
        }

        statementOrBundle.addAll(anonWasStartedBy);
        for (Collection<WasStartedBy> c: namedWasStartedByMap.values()) {
            statementOrBundle.addAll(c);
        }

        statementOrBundle.addAll(anonWasEndedBy);
        for (Collection<WasEndedBy> c: namedWasEndedByMap.values()) {
            statementOrBundle.addAll(c);
        }

        statementOrBundle.addAll(anonActedOnBehalfOf);
        for (Collection<ActedOnBehalfOf> c: namedActedOnBehalfOfMap.values()) {
            statementOrBundle.addAll(c);
        }

        statementOrBundle.addAll(anonWasInfluencedBy);
        for (Collection<WasInfluencedBy> c: namedWasInfluencedByMap.values()) {
            statementOrBundle.addAll(c);
        }


    }

    /** This function allows a document to be merged with this IndexedDocument. If flatten is true, bundles include in the document will be flattend into this one.
     *
     *
     * @param doc the document to be merged into this
     * @return this indexed document
     */
    public IndexedDocument merge(Document doc) {
        u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);
        return this;
    }

    public Set<QualifiedName> traverseDerivations(QualifiedName from) {
        Stack<QualifiedName> s=new Stack<>();
        s.push(from);
        return traverseDerivations1(new HashSet<>(),new HashSet<>(),s);
    }


    public Set<QualifiedName> traverseDerivations1(Set<QualifiedName> last, Set<QualifiedName> seen, Stack<QualifiedName> todo) {

        QualifiedName current=null;

        while (!(todo.isEmpty())) {

            current=todo.pop();
            if (seen.contains(current)) {
                // move on to next
            } else {
                seen.add(current);
                Collection<WasDerivedFrom> successors=entityCauseWasDerivedFromMap.get(current);
                if (successors==null || successors.isEmpty()) {
                    //move on next
                } else {
                    for (WasDerivedFrom wdf: successors) {
                        QualifiedName qn=wdf.getGeneratedEntity();
                        last.add(qn);
                        // This makes not sense to have this:  todo.push(qn);
                    }
                }
            }

        }

        return last;
    }

    public Set<Pair<QualifiedName,WasDerivedFrom>> traverseDerivationsWithRelations(QualifiedName from) {
        Stack<QualifiedName> s=new Stack<>();
        s.push(from);
        return traverseDerivations2(new HashSet<>(),new HashSet<>(),s);
    }

    public Set<Pair<QualifiedName,WasDerivedFrom>> traverseDerivations2(Set<Pair<QualifiedName,WasDerivedFrom>> last, Set<QualifiedName> seen, Stack<QualifiedName> todo) {

        QualifiedName current=null;

        while (!(todo.isEmpty())) {

            current=todo.pop();
            if (seen.contains(current)) {
                // move on to next
            } else {
                seen.add(current);
                Collection<WasDerivedFrom> successors=entityCauseWasDerivedFromMap.get(current);
                if (successors==null || successors.isEmpty()) {
                    //move on next
                } else {
                    for (WasDerivedFrom wdf: successors) {
                        QualifiedName qn=wdf.getGeneratedEntity();
                        last.add(Pair.of(qn,wdf));
                        // This makes not sense to have this: todo.push(qn);
                    }
                }
            }

        }

        return last;
    }


    public Set<Pair<QualifiedName,WasAttributedTo>> traverseAttributionsWithRelations(QualifiedName from) {
        Stack<QualifiedName> s=new Stack<>();
        s.push(from);
        return traverseAttributions2(new HashSet<>(),new HashSet<>(),s);
    }

    public Set<Pair<QualifiedName,WasAttributedTo>> traverseAttributions2(Set<Pair<QualifiedName,WasAttributedTo>> last, Set<QualifiedName> seen, Stack<QualifiedName> todo) {

        QualifiedName current=null;

        while (!(todo.isEmpty())) {

            current=todo.pop();
            if (seen.contains(current)) {
                // move on to next
            } else {
                seen.add(current);
                Collection<WasAttributedTo> successors=agentWasAttributedToMap.get(current);
                if (successors==null || successors.isEmpty()) {
                    //move on next
                } else {
                    for (WasAttributedTo wat: successors) {
                        QualifiedName qn=wat.getEntity();
                        last.add(Pair.of(qn,wat));
                        // This makes not sense to have this: todo.push(qn);  // this for the case of agent being attributed to another agent? does this exist?
                    }
                }
            }

        }

        return last;
    }


    public Set<Pair<QualifiedName, HadMember>> traverseMembershipsWithRelations(QualifiedName from) {
        Stack<QualifiedName> s=new Stack<>();
        s.push(from);
        return traverseMembership2(new HashSet<>(),new HashSet<>(),s);
    }

    public Set<Pair<QualifiedName,HadMember>> traverseMembership2(Set<Pair<QualifiedName,HadMember>> last, Set<QualifiedName> seen, Stack<QualifiedName> todo) {

        QualifiedName current=null;

        while (!(todo.isEmpty())) {

            current=todo.pop();
            if (seen.contains(current)) {
                // move on to next
            } else {
                seen.add(current);
                Collection<HadMember> successors=entityHadMemberMap.get(current);//
                if (successors==null || successors.isEmpty()) {
                    //move on next
                } else {
                    for (HadMember mem: successors) {
                        QualifiedName qn=mem.getCollection();
                        last.add(Pair.of(qn,mem));
                        // This makes not sense to have this: todo.push(qn);
                    }
                }
            }

        }

        return last;
    }


    public Set<Pair<QualifiedName, HadMember>> traverseReverseMembershipsWithRelations(QualifiedName from) {
        Stack<QualifiedName> todo=new Stack<>();
        todo.push(from);
        return traverseReverseMembership2(new HashSet<>(),new HashSet<>(),todo);
    }

    public Set<Pair<QualifiedName,HadMember>> traverseReverseMembership2(Set<Pair<QualifiedName,HadMember>> last, Set<QualifiedName> seen, Stack<QualifiedName> todo) {

        QualifiedName current=null;

        while (!(todo.isEmpty())) {

            current=todo.pop();
            if (seen.contains(current)) {
                // move on to next
            } else {
                seen.add(current);
                Collection<HadMember> successors=collHadMemberMap.get(current);
                if (successors==null || successors.isEmpty()) {
                    //move on next
                } else {
                    for (HadMember mem: successors) {
                        if (!mem.getCollection().equals(current)) throw new  org.openprovenance.prov.model.exception.InvalidIndexException("HadMember not indexed properly " + current + ": " + mem);
                        List<QualifiedName> qns=mem.getEntity();
                        for (QualifiedName qn: qns) {
                            last.add(Pair.of(qn, mem));
                            // This makes not sense to have this: todo.push(qn);
                        }
                    }
                }
            }

        }

        return last;
    }




    public Set<Pair<QualifiedName,SpecializationOf>> traverseSpecializationsWithRelations(QualifiedName from) {
        Stack<QualifiedName> s=new Stack<>();
        s.push(from);
        return traverseSpecializations2(new HashSet<>(),new HashSet<>(),s);
    }

    public Set<Pair<QualifiedName,SpecializationOf>> traverseSpecializations2(Set<Pair<QualifiedName,SpecializationOf>> last, Set<QualifiedName> seen, Stack<QualifiedName> todo) {

        QualifiedName current=null;

        while (!(todo.isEmpty())) {

            current=todo.pop();
            if (seen.contains(current)) {
                // move on to next
            } else {
                seen.add(current);
                Collection<SpecializationOf> successors=genericEntitySpecializationOfMap.get(current);
                if (successors==null || successors.isEmpty()) {
                    //move on next
                } else {
                    for (SpecializationOf spe: successors) {
                        QualifiedName qn=spe.getSpecificEntity();
                        last.add(Pair.of(qn,spe));
                        // This makes not sense to have this: todo.push(qn);
                    }
                }
            }

        }

        return last;
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    public Collection<Activity> getActivities() {
        return activityMap.values();
    }

    public Collection<Agent> getAgents() {
        return agentMap.values();
    }

    public void checkActivityUsedMap() {
        for (QualifiedName qn: activityUsedMap.keySet()) {
            Collection<Used> c=activityUsedMap.get(qn);
            for (Used u: c) {
                if (!qn.equals(u.getActivity())) {
                    throw new UnsupportedOperationException("ActivityUsedMap not indexed properly " + qn + ": " + u);
                }
            }
        }
    }

    public void checkEntityWasGeneratedByMap() {
        for (QualifiedName qn: entityWasGeneratedByMap.keySet()) {
            Collection<WasGeneratedBy> c=entityWasGeneratedByMap.get(qn);
            for (WasGeneratedBy u: c) {
                if (!qn.equals(u.getEntity())) {
                    throw new UnsupportedOperationException("EntityWasGeneratedByMap not indexed properly " + qn + ": " + u);
                }
            }
        }
    }

    public void checkEntityCauseWasDerivedFromMap() {
        for (QualifiedName qn: entityCauseWasDerivedFromMap.keySet()) {
            Collection<WasDerivedFrom> c=entityCauseWasDerivedFromMap.get(qn);
            for (WasDerivedFrom u: c) {
                if (!qn.equals(u.getUsedEntity())) {
                    throw new UnsupportedOperationException("EntityCauseWasDerivedFromMap not indexed properly " + qn + ": " + u);
                }
            }
        }
    }

    public Collection<WasEndedBy> getWasEndedBy() {
        return anonWasEndedBy;
    }
    public Collection<WasInvalidatedBy> getWasInvalidatedBy() {
        return anonWasInvalidatedBy;
    }

    public Collection<WasStartedBy> getWasStartedBy() {
        return anonWasStartedBy;
    }
    public Collection<WasInfluencedBy> getWasInfluencedBy() {
        return anonWasInfluencedBy;
    }

    public Collection<QualifiedName> nonRootEntities() {
        Collection<QualifiedName> effectEntities=new HashSet<>();
        effectEntities.addAll(entityEffectWasDerivedFromMap.keySet());
        effectEntities.addAll(entityWasAttributedToMap.keySet());
        effectEntities.addAll(entityWasGeneratedByMap.keySet());
        effectEntities.addAll(entityWasInvalidatedByMap.keySet());
        effectEntities.addAll(entityHadMemberMap.keySet());
        return effectEntities;
    }

}


