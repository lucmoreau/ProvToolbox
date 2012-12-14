package org.openprovenance.prov.rdf;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Collection;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.ModelConstructor;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.Statement;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.ValueConverter;
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
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 * A Converter to RDF
 */
public class RdfConstructor implements ModelConstructor {
    
    public Hashtable<QName,QName> qualifiedInfluenceTable=new Hashtable<QName, QName>();
    public Hashtable<QName,QName> influencerTable=new Hashtable<QName, QName>();
    public Hashtable<QName,QName> unqualifiedTable=new Hashtable<QName, QName>();
    public Hashtable<QName,QName> otherTable=new Hashtable<QName, QName>();
    

    public static QName newProvQName(String local) {
	return new QName(NamespacePrefixMapper.PROV_NS, local, NamespacePrefixMapper.PROV_PREFIX);
    }

    public static QName newToolboxQName(String local) {
	return new QName(NamespacePrefixMapper.TOOLBOX_NS, local, "_");
    }
    
    static int blankCounter=0;
    
    QName newBlankName() {
    	blankCounter++;
    	return newToolboxQName("blank" + blankCounter);
    }
	boolean isBlankName(QName name) {
		return name.getNamespaceURI().equals(NamespacePrefixMapper.TOOLBOX_NS)
				&& name.getPrefix().equals("_");
	}
	
    
    public static QName newRdfQName(String local) {
	return new QName(NamespacePrefixMapper.RDF_NS, local, NamespacePrefixMapper.RDF_PREFIX);
    }
    

    public static QName QNAME_PROVO_atTime=newProvQName("atTime");
    public static QName QNAME_PROVO_influencer=newProvQName("influencer");
    public static QName QNAME_PROVO_activity=newProvQName("activity");
    public static QName QNAME_PROVO_entity=newProvQName("entity");
    public static QName QNAME_PROVO_agent=newProvQName("agent");
    public static QName QNAME_PROVO_hadActivity=newProvQName("hadActivity");
    public static QName QNAME_PROVO_hadEntity=newProvQName("hadEntity");
    public static QName QNAME_PROVO_hadPlan=newProvQName("hadPlan");
    
    public static QName QNAME_PROVO_Influence=newProvQName("Influence");
    public static QName QNAME_PROVO_qualifiedInfluence=newProvQName("qualifiedInfluence");
    public static QName QNAME_PROVO_wasInfluencedBy=newProvQName("wasInfluencedBy");

    public static QName QNAME_PROVO_Generation=newProvQName("Generation");
    public static QName QNAME_PROVO_qualifiedGeneration=newProvQName("qualifiedGeneration");
    public static QName QNAME_PROVO_wasGeneratedBy=newProvQName("wasGeneratedBy");

    public static QName QNAME_PROVO_Usage=newProvQName("Usage");
    public static QName QNAME_PROVO_qualifiedUsage=newProvQName("qualifiedUsage");
    public static QName QNAME_PROVO_used=newProvQName("used");

    public static QName QNAME_PROVO_Invalidation=newProvQName("Invalidation");
    public static QName QNAME_PROVO_qualifiedInvalidation=newProvQName("qualifiedInvalidation");
    public static QName QNAME_PROVO_wasInvalidatedBy=newProvQName("wasInvalidatedBy");
    
    public static QName QNAME_PROVO_Start=newProvQName("Start");
    public static QName QNAME_PROVO_qualifiedStart=newProvQName("qualifiedStart");
    public static QName QNAME_PROVO_wasStartedBy=newProvQName("wasStartedBy");

    public static QName QNAME_PROVO_End=newProvQName("End");
    public static QName QNAME_PROVO_qualifiedEnd=newProvQName("qualifiedEnd");
    public static QName QNAME_PROVO_wasEndedBy=newProvQName("wasEndedBy");


    public static QName QNAME_RDF_TYPE=newRdfQName("type");
    
    void initInfluenceTables() {
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Influence, QNAME_PROVO_qualifiedInfluence);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Generation, QNAME_PROVO_qualifiedGeneration);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Usage, QNAME_PROVO_qualifiedUsage);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Invalidation, QNAME_PROVO_qualifiedInvalidation);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Start, QNAME_PROVO_qualifiedStart);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_End, QNAME_PROVO_qualifiedEnd);
   	
   	 influencerTable.put(QNAME_PROVO_Influence, QNAME_PROVO_influencer);
   	 activityInfluence(QNAME_PROVO_Generation);
   	 entityInfluence(QNAME_PROVO_Usage);
   	 activityInfluence(QNAME_PROVO_Invalidation);
   	 entityInfluence(QNAME_PROVO_Start);
   	 entityInfluence(QNAME_PROVO_End);
   	 
   	 unqualifiedTable.put(QNAME_PROVO_Influence, QNAME_PROVO_wasInfluencedBy);
   	 unqualifiedTable.put(QNAME_PROVO_Generation, QNAME_PROVO_wasGeneratedBy);
   	 unqualifiedTable.put(QNAME_PROVO_Usage, QNAME_PROVO_used);
   	 unqualifiedTable.put(QNAME_PROVO_Invalidation, QNAME_PROVO_wasInvalidatedBy);
   	 unqualifiedTable.put(QNAME_PROVO_Start, QNAME_PROVO_wasStartedBy);
  	 unqualifiedTable.put(QNAME_PROVO_End, QNAME_PROVO_wasEndedBy);
  	 
  	 otherTable.put(QNAME_PROVO_Start, QNAME_PROVO_hadActivity);
  	 otherTable.put(QNAME_PROVO_End, QNAME_PROVO_hadActivity);
   	 
   }

    void activityInfluence(QName name) {
		influencerTable.put(name, QNAME_PROVO_activity);
	}
    void entityInfluence(QName name) {
		influencerTable.put(name, QNAME_PROVO_entity);
	}
   

    
    final ElmoManager manager;

    private Hashtable<String, String> namespaceTable = new Hashtable<String, String>();

    public Hashtable<String, String> getNamespaceTable() {
        return namespaceTable;
    }

    public RdfConstructor(ElmoManager manager) {
        this.manager = manager;
        initInfluenceTables();
    }

    @Override
    public org.openprovenance.prov.xml.Entity newEntity(QName id,
                                                        Collection<Attribute> attributes) {
        Entity e = (Entity) designateIfNotNull(id, Entity.class);
        processAttributes(e, attributes);
        return null;
    }

    @Override
    public org.openprovenance.prov.xml.Activity newActivity(QName id,
                                                            XMLGregorianCalendar startTime,
                                                            XMLGregorianCalendar endTime,
                                                            Collection<Attribute> attributes) {
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
                                                      Collection<Attribute> attributes) {
        Agent ag = (Agent) designateIfNotNull(id, Agent.class);
        processAttributes(ag, attributes);
        return null;
    }

    @Override
    public Used newUsed(QName id, QName activity, QName entity,
                        XMLGregorianCalendar time, Collection<Attribute> attributes) {
       
 	    @SuppressWarnings("unused")
        QName u = addInfluence(id, activity, entity, time, null, attributes,
                 QNAME_PROVO_Usage);

        return null;
    }

    @Override
    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity,
                                            QName activity,
                                            XMLGregorianCalendar time,
                                            Collection<Attribute> attributes) {


        @SuppressWarnings("unused")
        QName g = addInfluence(id, entity, activity, time, null, attributes,
                QNAME_PROVO_Generation);

        return null;
    }
 

    @Override
    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity,
                                                QName activity,
                                                XMLGregorianCalendar time,
                                                Collection<Attribute> attributes) {

        @SuppressWarnings("unused")
        QName inv = addInfluence(id, entity, activity, time, null, attributes,
                                 QNAME_PROVO_Invalidation);

        return null;
    }

    @Override
    public WasStartedBy newWasStartedBy(QName id, QName activity,
                                        QName trigger, QName starter,
                                        XMLGregorianCalendar time,
                                        Collection<Attribute> attributes) {
    	
        @SuppressWarnings("unused")
        QName s = addInfluence(id, activity, trigger, time, starter, attributes,
                QNAME_PROVO_Start);

        return null;
    }

    @Override
    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger,
                                    QName ender, XMLGregorianCalendar time,
                                    Collection<Attribute> attributes) {
    	
        @SuppressWarnings("unused")
        QName e = addInfluence(id, activity, trigger, time, ender, attributes,
                QNAME_PROVO_End);

        return null;

    }

    @Override
    public WasDerivedFrom newWasDerivedFrom(QName id, QName entity2, QName entity1,
                                            QName activity, QName generation,
                                            QName usage,
                                            Collection<Attribute> attributes) {

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
                                                  Collection<Attribute> attributes) {
        

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
                                              Collection<Attribute> attributes) {

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
                                              Collection<Attribute> attributes) {

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
                                          Collection<Attribute> attributes) {

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
                                              Collection<Attribute> attributes) {


        @SuppressWarnings("unused")
        QName u = addInfluence(id, qn2, qn1, null, null, attributes, QNAME_PROVO_Influence);

//        if ((binaryProp(id, qn2)) && (qn1 != null))
            //assertStatement(createObjectProperty(qn2, QNAME_PROVO_wasInfluencedBy, qn1));
        
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
    public HadMember newHadMember(QName collection, Collection<QName> ll) {
        for (QName entity: ll) {

            org.openprovenance.prov.rdf.Collection c = designateIfNotNull(collection, org.openprovenance.prov.rdf.Collection.class);
            Entity e = designateIfNotNull(entity, Entity.class);

            c.getHadMember().add(e);
        }
        return null;
    }

    @Override
    public Document newDocument(Hashtable<String, String> namespaces,
                                Collection<Statement> statements,
                                Collection<NamedBundle> bundles) {
        //At this stage nothing left to do
        return null;
    }

    @Override
    public NamedBundle newNamedBundle(QName id,
                                      Hashtable<String, String> namespaces,
                                      Collection<Statement> statements) {
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
        URIImpl uri = qnameToURI(bundleId);
        contexts.add(uri);
        if (bundleId != null) {
            ((SesameManager) manager).getConnection().setAddContexts(uri);
        }
    }

 

    public List<Resource> contexts = new LinkedList<Resource>();


    
    public void processAttributes(Object infl, Collection<Attribute> aAttrs) {
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

        processAttributes(r, aAttrs);
    }
    
    public void processAttributes(QName q,
				  Collection<Attribute> attributes) {
	processAttributes(qnameToResource(q), attributes);
    }


    public void processAttributes(org.openrdf.model.Resource r,
				  Collection<Attribute> aAttrs) {
	for (Attribute attr : aAttrs) {

            QName pred = null;

            LiteralImpl literalImpl = null;

            QName type = attr.getXsdType();

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
                literalImpl = new LiteralImpl(qnAsString, qnameToURI(type));

            } else {
                value = attr.getValue().toString();
                literalImpl = new LiteralImpl(value, qnameToURI(type));
            }
            pred = attr.getElementName();

            org.openrdf.model.Statement stmnt = createDataProperty(r, pred, literalImpl);
            assertStatement(stmnt);
        }
    }

    public void assertStatement(org.openrdf.model.Statement stmnt) {
	try {
	    ((org.openrdf.elmo.sesame.SesameManager) manager)
	            .getConnection().add(stmnt);
	} catch (org.openrdf.repository.RepositoryException e) {
	}
    }

    public StatementImpl createDataProperty(org.openrdf.model.Resource r,
                                            QName pred, 
                                            LiteralImpl literalImpl) {
	return new StatementImpl(r,
	                         qnameToURI(pred),
	                         literalImpl);
    }
    public StatementImpl createDataProperty(QName subject,
            QName pred, 
            LiteralImpl literalImpl) {
    	return createDataProperty(qnameToResource(subject), pred, literalImpl);
    }	

    public StatementImpl createObjectProperty(org.openrdf.model.Resource r,
                                              QName pred, 
                                              QName object) {
	return new StatementImpl(r,
	                         qnameToURI(pred),
	                         qnameToURI(object));
    }
    public StatementImpl createObjectProperty(QName subject,
                                              QName pred, 
                                              QName object) {
	return new StatementImpl(qnameToResource(subject),
	                         qnameToURI(pred),
	                         qnameToResource(object));
    }

    
    private URIImpl qnameToURI(QName qname) {
        if (qname.getNamespaceURI().equals(NamespacePrefixMapper.XSD_NS)) {
            return new URIImpl(NamespacePrefixMapper.XSD_HASH_NS
                    + qname.getLocalPart());
        } else {
            return new URIImpl(qname.getNamespaceURI() + qname.getLocalPart());

        }
    }
    private Resource qnameToResource(QName qname) {
        if (qname.getNamespaceURI().equals(NamespacePrefixMapper.XSD_NS)) {
            return new URIImpl(NamespacePrefixMapper.XSD_HASH_NS
                    + qname.getLocalPart());
        } if (isBlankName(qname)) {
        	return new BNodeImpl(qname.getLocalPart());
        } else {
            return new URIImpl(qname.getNamespaceURI() + qname.getLocalPart());

        }
    }


    public <INFLUENCE, TYPE> INFLUENCE addEntityInfluence(QName qname, TYPE e2,
                                                          Entity e1,
                                                          XMLGregorianCalendar time,
                                                          Collection<Attribute> aAttrs,
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


    public  QName addInfluence(QName infl,
                                     QName subject,
                                      QName object,
                                      XMLGregorianCalendar time, QName other, Collection<Attribute> attributes,
                                      QName qualifiedClass) {
        if ((infl != null)
                || ((attributes != null) && !(attributes.isEmpty()))) {
            infl = assertType(infl, qualifiedClass);
            if (object != null) assertInfluencer(infl, object, qualifiedClass);
            if (subject!=null)
              assertQualifiedInfluence(subject, infl, qualifiedClass);
            if (time!=null) assertAtTime(infl,time);
            if (other!=null) asserterOther(infl,other, qualifiedClass);
            processAttributes(infl, attributes);
        }

        if ((binaryProp(infl, subject)) && (object != null))
            assertStatement(createObjectProperty(subject, unqualifiedTable.get(qualifiedClass), object));

        return infl;
    }

    public void asserterOther(QName subject, QName other, QName qualifiedClass) {
        assertStatement(createObjectProperty(subject, otherTable.get(qualifiedClass), other));		
	}

	public void assertAtTime(QName subject, XMLGregorianCalendar time) {
		assertStatement(createDataProperty(subject,QNAME_PROVO_atTime,newLiteral(time)));
		
	}

	private LiteralImpl newLiteral(XMLGregorianCalendar time) {
		return new LiteralImpl(time.toString(), qnameToURI(ValueConverter.QNAME_XSD_HASH_DATETIME));
	}

	public void assertQualifiedInfluence(QName subject, QName infl, QName qualifiedClass) {
	assertStatement(createObjectProperty(subject, 
	                                     qualifiedInfluenceTable.get(qualifiedClass), 
	                                     infl));	
    }

    public void assertInfluencer(QName infl, QName object, QName qualifiedClass) {
	assertStatement(createObjectProperty(infl,
	                                     influencerTable.get(qualifiedClass),
	                                     object));	
    }
	
    
    public QName assertType(QName infl, QName qualifiedClass) {
    	if (infl==null) {
    		infl=newBlankName();
    		//Resource uri=new BNodeImpl(infl.getLocalPart());
//    		assertStatement(createObjectProperty(uri, QNAME_RDF_TYPE, qualifiedClass));
//    	} else {
//    		assertStatement(createObjectProperty(infl, QNAME_RDF_TYPE, qualifiedClass));
    	}
    	assertStatement(createObjectProperty(infl, QNAME_RDF_TYPE, qualifiedClass));
		return infl;
    }

    private void setTime(InstantaneousEvent infl, XMLGregorianCalendar time) {
        if (infl != null) {
            XMLGregorianCalendar t = (XMLGregorianCalendar) time;
            infl.getAtTime().add(t);
        }
    }

    public <INFLUENCE, TYPE> INFLUENCE addActivityInfluence(QName qname, TYPE a2,
                                                               Activity a1,
                                                               XMLGregorianCalendar time,
                                                               Collection<Attribute> aAttrs,
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
                                                         Collection<Attribute> aAttrs,
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
