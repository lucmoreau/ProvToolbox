package org.openprovenance.prov.rdf;

import java.net.URI;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.BeanConstructor;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvUtilities;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.Statement;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasAttributedTo;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInfluencedBy;
import org.openprovenance.prov.xml.WasInformedBy;
import org.openprovenance.prov.xml.WasInvalidatedBy;
import org.openprovenance.prov.xml.WasStartedBy;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 * A Converter to RDF
 */
public class RdfConstructor implements BeanConstructor {
    final ProvFactory pFactory;
    final ElmoManager manager;
    final ProvUtilities pUtil;

    private Hashtable<String, String> namespaceTable = new Hashtable<String, String>();

    public Hashtable<String, String> getNamespaceTable() {
        return namespaceTable;
    }

    public RdfConstructor(ProvFactory pFactory, ElmoManager manager) {
        this.pFactory = pFactory;
        this.pUtil = new ProvUtilities();
        this.manager = manager;
        pFactory.setNamespaces(namespaceTable);
    }

    @Override
    public org.openprovenance.prov.xml.Entity newEntity(QName id,
                                                        List<Attribute> attributes) {
        Entity e = (Entity) designateIfNotNull(id, Entity.class);
        processAttributes(e, attributes);
        return null;
    }

    @Override
    public org.openprovenance.prov.xml.Activity newActivity(QName id,
                                                            XMLGregorianCalendar startTime,
                                                            XMLGregorianCalendar endTime,
                                                            List<Attribute> attributes) {
        Activity a = (Activity) designateIfNotNull(id, Activity.class);
        if (startTime != null) {
            a.getStartedAtTime().add(startTime);
        }
        if (endTime != null) {
            a.getEndedAtTime().add(endTime);
        }
        processAttributes(a, attributes);
        return null;
    }

    @Override
    public org.openprovenance.prov.xml.Agent newAgent(QName id,
                                                      List<Attribute> attributes) {
        Agent ag = (Agent) designateIfNotNull(id, Agent.class);
        processAttributes(ag, attributes);
        return null;
    }

    @Override
    public Used newUsed(QName id, QName activity, QName entity,
                        XMLGregorianCalendar time, List<Attribute> attributes) {
       
        Entity e1 = designateIfNotNull(entity, Entity.class);
        Activity a2 = designateIfNotNull(activity, Activity.class);
        @SuppressWarnings("unused")
        Usage u = addEntityInfluence(id, a2, e1, time, attributes, null,
                                     Usage.class);

        if ((binaryProp(id, a2)) && (e1 != null))
            a2.getUsed().add(e1);
        return null;
    }

    @Override
    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity,
                                            QName activity,
                                            XMLGregorianCalendar time,
                                            List<Attribute> attributes) {

        Entity e2 = designateIfNotNull(entity, Entity.class);
        Activity a1 = designateIfNotNull(activity, Activity.class);

        @SuppressWarnings("unused")
        Generation g = addActivityInfluence(id, e2, a1, time, attributes,
                                            Generation.class);

        if ((binaryProp(id, e2)) && (a1 != null))
            e2.getWasGeneratedBy().add(a1);
        return null;
    }

    @Override
    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity,
                                                QName activity,
                                                XMLGregorianCalendar time,
                                                List<Attribute> attributes) {
        Entity e2 = designateIfNotNull(entity, Entity.class);
        Activity a1 = designateIfNotNull(activity, Activity.class);

        @SuppressWarnings("unused")
        Invalidation g = addActivityInfluence(id, e2, a1, time, attributes,
                                            Invalidation.class);

        if ((binaryProp(id, e2)) && (a1 != null))
            e2.getWasInvalidatedBy().add(a1);
        return null;
    }

    @Override
    public WasStartedBy newWasStartedBy(QName id, QName activity,
                                        QName trigger, QName starter,
                                        XMLGregorianCalendar time,
                                        List<Attribute> attributes) {

        Entity e1 = designateIfNotNull(trigger, Entity.class);
        Activity a2 = designateIfNotNull(activity, Activity.class);
        Start s = addEntityInfluence(id, a2, e1, time, attributes, starter, Start.class);
        
        if (starter != null) {
            Activity a3 = designateIfNotNull(starter, Activity.class);
            s.getHadActivity().add(a3);
        }
        
        if ((binaryProp(id, a2)) && (e1 != null))
            a2.getWasStartedBy().add(e1);
        

        return null;
    }

    @Override
    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger,
                                    QName ender, XMLGregorianCalendar time,
                                    List<Attribute> attributes) {

        Entity e1 = designateIfNotNull(trigger, Entity.class);
        Activity a2 = designateIfNotNull(activity, Activity.class);
        End s = addEntityInfluence(id, a2, e1, time, attributes, ender, End.class);
        
        if (ender != null) {
            Activity a3 = designateIfNotNull(ender, Activity.class);
            s.getHadActivity().add(a3);
        }
        
        if ((binaryProp(id, a2)) && (e1 != null))
            a2.getWasEndedBy().add(e1);
        


        return null;
    }

    @Override
    public WasDerivedFrom newWasDerivedFrom(QName id, QName entity2, QName entity1,
                                            QName activity, QName generation,
                                            QName usage,
                                            List<Attribute> attributes) {

        Entity e2 = designateIfNotNull(entity2, Entity.class);
        Entity e1 = designateIfNotNull(entity1, Entity.class);

        QName other = activity;
        if (usage != null) {
            other = usage;
        } else {
            if (generation != null)
                other = generation;
        }

        Derivation d = addEntityInfluence(id, e2, e1, null, attributes, other,
                                          Derivation.class);

        if (d != null) {
            if (generation != null) {
                Generation g5 = designateIfNotNull(generation, Generation.class);
                d.getHadGeneration().add(g5);
            }
            if (usage != null) {
                Usage u4 = designateIfNotNull(usage, Usage.class);
                d.getHadUsage().add(u4);
            }
            if (activity != null) {
                Activity a3 = designateIfNotNull(activity, Activity.class);
                d.getHadActivity().add(a3);
            }
        }

        if ((binaryProp(id, e2)) && (e1 != null))
            e2.getWasDerivedFrom().add(e1);

   
        return null;
    }

    @Override
    public WasAssociatedWith newWasAssociatedWith(QName id, QName a, QName ag,
                                                  QName plan,
                                                  List<Attribute> attributes) {
        

        Activity a2 = designateIfNotNull(a, Activity.class);
        Agent ag1 = designateIfNotNull(ag, Agent.class);

        Association assoc = addAgentInfluence(id, a2, ag1, null, attributes, plan,
                                              Association.class);

        if ((plan != null) && (assoc != null)) {
            Plan pl = (Plan) designateIfNotNull(plan, Plan.class);
            // will declare it as Plan if
            // not alreadydone
            assoc.getHadPlan().add(pl);
        }

        if ((binaryProp(id, a2)) && (ag1 != null))
            a2.getWasAssociatedWith().add(ag1);

        return null;
    }

    @Override
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,
                                              List<Attribute> attributes) {

        Entity e2 = designateIfNotNull(e, Entity.class);
        Agent a1 = designateIfNotNull(ag, Agent.class);

        @SuppressWarnings("unused")
        Attribution g = addAgentInfluence(id, e2, a1, null, attributes, null,
                                          Attribution.class);

        if ((binaryProp(id, e2)) && (a1 != null))
            e2.getWasAttributedTo().add(a1);
        return null;
    }

    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName agent2, QName agent1,
                                              QName a,
                                              List<Attribute> attributes) {

        Agent ag2 = designateIfNotNull(agent2, Agent.class);
        Agent ag1 = designateIfNotNull(agent1, Agent.class);

        Delegation g = addAgentInfluence(id, ag2, ag1, null, attributes, a,
                                         Delegation.class);

        if (a != null) {
            Activity a3 = designateIfNotNull(a, Activity.class);
            g.getHadActivity().add(a3);
        }

        if ((binaryProp(id, ag2)) && (ag1 != null))
            ag2.getActedOnBehalfOf().add(ag1);
        return null;
    }

    @Override
    public WasInformedBy newWasInformedBy(QName id, QName activity2, QName activity1,
                                          List<Attribute> attributes) {

        Activity a2 = designateIfNotNull(activity2, Activity.class);
        Activity a1 = designateIfNotNull(activity1, Activity.class);

        @SuppressWarnings("unused")
        Communication g = addActivityInfluence(id, a2, a1, null, attributes,
                                               Communication.class);

        if ((binaryProp(id, a2)) && (a1 != null))
            a2.getWasInformedBy().add(a1);

        return null;
    }

    @Override
    public WasInfluencedBy newWasInfluencedBy(QName id, QName qn2, QName qn1,
                                              List<Attribute> attributes) {


        ActivityOrAgentOrEntity e1 = designateIfNotNull(qn1,
                                                        ActivityOrAgentOrEntity.class);
        ActivityOrAgentOrEntity e2 = designateIfNotNull(qn2,
                                                        ActivityOrAgentOrEntity.class);
        // (ActivityOrAgentOrEntity) manager
        // .find(qn1);
        // ActivityOrAgentOrEntity e2 = manager
        // .designate(qn2, ActivityOrAgentOrEntity.class);

        @SuppressWarnings("unused")
        Influence u = addUnknownInfluence(id, e2, e1, attributes, Influence.class);

        if ((binaryProp(id, e2)) && (e1 != null))
            e2.getWasInfluencedBy().add(e1);

        return null;
    }

    @Override
    public AlternateOf newAlternateOf(QName entity2, QName entity1) {

        Entity e2 = designateIfNotNull(entity2, Entity.class);
        Entity e1 = designateIfNotNull(entity1, Entity.class);

        e2.getAlternateOf().add(e1);
        return null;
    }

    @Override
    public SpecializationOf newSpecializationOf(QName entity2, QName entity1) {
        Entity e2 = designateIfNotNull(entity2, Entity.class);
        Entity e1 = designateIfNotNull(entity1, Entity.class);

        e2.getSpecializationOf().add(e1);
        return null;
    }

    @Override
    public MentionOf newMentionOf(QName entity2, QName entity1, QName b) {


        Entity e2 = designateIfNotNull(entity2, Entity.class);
        Entity e1 = designateIfNotNull(entity1, Entity.class);
        Bundle e3 = designateIfNotNull(b, Bundle.class); // will
       
        if (e2 != null) {
            if (e1 != null)
                e2.getMentionOf().add(e1);
            if (e3 != null)
                e2.getAsInBundle().add(e3);
        }

        return null;
    }

    @Override
    public HadMember newHadMember(QName collection, List<QName> ll) {
        for (QName entity: ll) {

            Collection c = designateIfNotNull(collection, Collection.class);
            Entity e = designateIfNotNull(entity, Entity.class);

            c.getHadMember().add(e);
        }
        return null;
    }

    @Override
    public Document newDocument(Hashtable<String, String> namespaces,
                                java.util.Collection<Statement> statements,
                                java.util.Collection<NamedBundle> bundles) {
        //At this stage nothing left to do
        return null;
    }

    @Override
    public NamedBundle newNamedBundle(QName id,
                                      Hashtable<String, String> namespaces,
                                      java.util.Collection<Statement> statements) {
        //At this stage nothing left to do
        return null;
    }

    @Override
    public void startDocument(Hashtable<String, String> namespaces) {
        if (namespaces != null) {
            getNamespaceTable().putAll(namespaces);
        }
        ((SesameManager) manager).getConnection().setAddContexts();
    }

    @Override
    public void startBundle(QName bundleId, Hashtable<String, String> namespaces) {
        System.out.println("$$$$$$$$$$$$ in startBundle");
        // TODO: bundle name does not seem to be interpreted according to the
        // prefix declared in bundle.
        // TODO: handle prefix declarations
        URIImpl uri = new URIImpl(bundleId.getNamespaceURI()
                                  + bundleId.getLocalPart());
        contexts.add(uri);
        if (bundleId != null) {
            ((SesameManager) manager).getConnection().setAddContexts(uri);
        }
    }

 

    public List<Resource> contexts = new LinkedList<Resource>();


    private URIImpl uriFromQName(QName qname) {
        if (qname.getNamespaceURI().equals(NamespacePrefixMapper.XSD_NS)) {
            return new URIImpl(NamespacePrefixMapper.XSD_HASH_NS
                    + qname.getLocalPart());
        } else {
            return new URIImpl(qname.getNamespaceURI() + qname.getLocalPart());

        }
    }

    public void processAttributes(Object infl, List<Attribute> aAttrs) {
        if (aAttrs == null)
            return;
        if (infl == null) {
            throw new NullPointerException(); // should never be here, really
        }

        // QName a=null;
        // org.openrdf.model.Resource r = new URIImpl(a.getNamespaceURI()
        // + a.getLocalPart());

        org.openrdf.model.Resource r = ((org.openrdf.elmo.sesame.roles.SesameEntity) infl)
                .getSesameResource();

        for (Attribute attr : aAttrs) {

            QName pred = null;
            QName type = null;

            LiteralImpl literalImpl = null;

            String typeAsString = attr.getXsdType();
            type = getQName(typeAsString);

            String value;
            if (attr.getValue() instanceof InternationalizedString) {
                InternationalizedString iString = (InternationalizedString) attr
                        .getValue();
                value = iString.getValue();
                literalImpl = new LiteralImpl(value, iString.getLang());
            } else if (attr.getValue() instanceof QName) {
                QName qn = (QName) attr.getValue();
                String qnAsString;
                if ((qn.getPrefix() == null) || (qn.getPrefix().equals(""))) {
                    qnAsString = qn.getLocalPart();
                } else {
                    qnAsString = qn.getPrefix() + ":" + qn.getLocalPart();
                }
                literalImpl = new LiteralImpl(qnAsString, uriFromQName(type));

            } else {
                value = attr.getValue().toString();
                literalImpl = new LiteralImpl(value, uriFromQName(type));
            }
            pred = attr.getElementName();

            org.openrdf.model.Statement stmnt = new StatementImpl(r,
                    new URIImpl(pred.getNamespaceURI() + pred.getLocalPart()),
                    literalImpl);

            try {
                ((org.openrdf.elmo.sesame.SesameManager) manager)
                        .getConnection().add(stmnt);
            } catch (org.openrdf.repository.RepositoryException e) {
            }
        }
    }

    public <INFLUENCE, TYPE> INFLUENCE addEntityInfluence(QName qname, TYPE e2,
                                                          Entity e1,
                                                          XMLGregorianCalendar time,
                                                          List<Attribute> aAttrs,
                                                          Object other,
                                                          Class<INFLUENCE> cl) {

        INFLUENCE infl = null;

        if ((qname != null) || (time != null)
                || ((aAttrs != null) && !(((List<?>) aAttrs).isEmpty()))
                || (other != null)) {
            infl = designate(qname, cl); // if qname is null, create an blank
                                         // node
            EntityInfluence qi = (EntityInfluence) infl;
            if (e1 != null)
                qi.getEntities().add(e1);
            addQualifiedInfluence(e2, infl);

            if (time != null) {
                setTime((InstantaneousEvent) infl, time);
            }
            processAttributes(qi,  aAttrs);
        }
        return infl;
    }


    public <INFLUENCE> INFLUENCE addUnknownInfluence(QName id,
                                                     ActivityOrAgentOrEntity e2,
                                                     ActivityOrAgentOrEntity e1,
                                                     List<Attribute> aAttrs,
                                                     Class<INFLUENCE> cl) {

        INFLUENCE infl = null;

        if ((id != null)
                || ((aAttrs != null) && !(((List<?>) aAttrs).isEmpty()))) {
            QName qname = getQName(id);
            infl = designate(qname, cl);
            Influence qi = (Influence) infl;
            if (e1 != null)
                qi.getInfluencers().add(e1);
            addQualifiedInfluence(e2, infl);

            processAttributes(qi, aAttrs);
        }
        return infl;
    }

    private void setTime(InstantaneousEvent infl, Object time) {
        if (infl != null) {
            if (time instanceof XMLGregorianCalendar) {
                XMLGregorianCalendar t = (XMLGregorianCalendar) time;
                infl.getAtTime().add(t);
            } else {
                String s = (String) time;
                XMLGregorianCalendar t = pFactory.newISOTime(s);
                infl.getAtTime().add(t);
            }
        }
    }

    public <INFLUENCE, TYPE> INFLUENCE addActivityInfluence(QName qname, TYPE a2,
                                                               Activity a1,
                                                               XMLGregorianCalendar time,
                                                               List<Attribute> aAttrs,
                                                               Class<INFLUENCE> cl) {

           INFLUENCE infl = null;

           if ((qname != null) || (time != null)
                   || ((aAttrs != null) && !(aAttrs.isEmpty()))) {
               infl = designate(qname, cl);
               ActivityInfluence qi = (ActivityInfluence) infl;
               if (a1 != null)
                   qi.getActivities().add(a1);
               addQualifiedInfluence(a2, infl);

               if (time != null) {
                   setTime((InstantaneousEvent) infl, time);
               }

               processAttributes(qi,  aAttrs);
           }
           return infl;
       }
    
    

    public <INFLUENCE, TYPE> INFLUENCE addAgentInfluence(QName qname, TYPE e2,
                                                         Agent a1, XMLGregorianCalendar time,
                                                         List<Attribute> aAttrs,
                                                         Object other,
                                                         Class<INFLUENCE> cl) {

        INFLUENCE infl = null;

        if ((qname != null) || (time != null)
                || ((aAttrs != null) && !(((List<?>) aAttrs).isEmpty()))
                || (other != null)) {
            infl = designate(qname, cl);
            AgentInfluence qi = (AgentInfluence) infl;
            if (a1 != null)
                qi.getAgents().add(a1);
            addQualifiedInfluence(e2, infl);

            if (time != null) {
                setTime((InstantaneousEvent) infl, time);
            }
            processAttributes(qi,  aAttrs);
        }
        return infl;
    }
    
   
    // not pretty

    public <INFLUENCE, EFFECT> void addQualifiedInfluence(EFFECT e2, INFLUENCE g) {
        if ((g != null) && (e2 != null)) {
            if (g instanceof Generation) {
                ((Entity) e2).getQualifiedGeneration().add((Generation) g);
            } else if (g instanceof Invalidation) {
                ((Entity) e2).getQualifiedInvalidation().add((Invalidation) g);
            } else if (g instanceof Communication) {
                ((Activity) e2).getQualifiedCommunication()
                        .add((Communication) g);
            } else if (g instanceof Usage) {
                ((Activity) e2).getQualifiedUsage().add((Usage) g);
            } else if (g instanceof Start) {
                ((Activity) e2).getQualifiedStart().add((Start) g);
            } else if (g instanceof End) {
                ((Activity) e2).getQualifiedEnd().add((End) g);
            } else if (g instanceof Attribution) {
                ((Entity) e2).getQualifiedAttribution().add((Attribution) g);
            } else if (g instanceof Association) {
                ((Activity) e2).getQualifiedAssociation().add((Association) g);
            } else if (g instanceof Delegation) {
                ((Agent) e2).getQualifiedDelegation().add((Delegation) g);
            } else if (g instanceof Derivation) {
                ((Entity) e2).getQualifiedDerivation().add((Derivation) g);
            } else if (g instanceof Influence) {
                ((ActivityOrAgentOrEntity) e2).getQualifiedInfluence()
                        .add((Influence) g);
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }


   

    public Object convertExtension(Object name, Object id, Object args,
                                   Object dAttrs) {
        return null;
    }

  

    public QName getQName(Object id) {
        if (id == null) {
            return null;
        }
        String idAsString = (String) id;
        return pFactory.stringToQName(idAsString);
    }

    /* Component 5 */

    public Object convertInsertion(Object id, Object id2, Object id1,
                                   Object map, Object dAttrs) {
        // todo
        throw new UnsupportedOperationException();
    }

    public Object convertEntry(Object o1, Object o2) {
        // todo
        throw new UnsupportedOperationException();
    }

    public Object convertKeyEntitySet(List<Object> o) {
        // todo
        throw new UnsupportedOperationException();
    }

    public Object convertRemoval(Object id, Object id2, Object id1,
                                 Object keys, Object dAttrs) {
        // todo
        throw new UnsupportedOperationException();
    }

    public Object convertDictionaryMemberOf(Object id, Object id2, Object map,
                                            Object complete, Object dAttrs) {
        // todo
        throw new UnsupportedOperationException();
    }

    public Object convertCollectionMemberOf(Object id, Object id2, Object map,
                                            Object complete, Object dAttrs) {
        // todo
        throw new UnsupportedOperationException();
    }

    public Object convertKeys(List<Object> keys) {
        // todo
        throw new UnsupportedOperationException();
    }

  

    public <T> T designateIfNotNull(QName qname, Class<T> cl) {
        if (qname == null)
            return null;
        return designate(qname, cl);
    }

    public <T> T designate(QName qname, Class<T> cl) {

        return manager.designate(qname, cl);
    }

    public boolean binaryProp(Object id, Object subject) {
        return id == null && subject != null;
    }

  
}
