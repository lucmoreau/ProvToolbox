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
    public static QName QNAME_PROVO_hadGeneration=newProvQName("hadGeneration");
    public static QName QNAME_PROVO_hadUsage=newProvQName("hadUsage");

    
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

    public static QName QNAME_PROVO_Association=newProvQName("Association");
    public static QName QNAME_PROVO_qualifiedAssociation=newProvQName("qualifiedAssociation");
    public static QName QNAME_PROVO_wasAssociatedWith=newProvQName("wasAssociatedWith");

    public static QName QNAME_PROVO_Attribution=newProvQName("Attribution");
    public static QName QNAME_PROVO_qualifiedAttribution=newProvQName("qualifiedAttribution");
    public static QName QNAME_PROVO_wasAttributedTo=newProvQName("wasAttributedTo");

    public static QName QNAME_PROVO_Delegation=newProvQName("Delegation");
    public static QName QNAME_PROVO_qualifiedDelegation=newProvQName("qualifiedDelegation");
    public static QName QNAME_PROVO_actedOnBehalfOf=newProvQName("actedOnBehalfOf");

    public static QName QNAME_PROVO_Derivation=newProvQName("Derivation");
    public static QName QNAME_PROVO_qualifiedDerivation=newProvQName("qualifiedDerivation");
    public static QName QNAME_PROVO_wasDerivedFrom=newProvQName("wasDerivedFrom");

    public static QName QNAME_PROVO_Communication=newProvQName("Communication");
    public static QName QNAME_PROVO_qualifiedCommunication=newProvQName("qualifiedCommunication");
    public static QName QNAME_PROVO_wasInformedBy=newProvQName("wasInformedBy");
    
    public static QName QNAME_PROVO_specializationOf=newProvQName("specializationOf");
    public static QName QNAME_PROVO_alternateOf=newProvQName("alternateOf");
    public static QName QNAME_PROVO_mentionOf=newProvQName("mentionOf");
    public static QName QNAME_PROVO_asInBundle=newProvQName("asInBundle");
    public static QName QNAME_PROVO_hadMember=newProvQName("hadMember");


    public static QName QNAME_RDF_TYPE=newRdfQName("type");
    
    void initInfluenceTables() {
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Influence, QNAME_PROVO_qualifiedInfluence);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Generation, QNAME_PROVO_qualifiedGeneration);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Usage, QNAME_PROVO_qualifiedUsage);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Invalidation, QNAME_PROVO_qualifiedInvalidation);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Start, QNAME_PROVO_qualifiedStart);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_End, QNAME_PROVO_qualifiedEnd);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Association, QNAME_PROVO_qualifiedAssociation);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Attribution, QNAME_PROVO_qualifiedAttribution);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Delegation, QNAME_PROVO_qualifiedDelegation);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Derivation, QNAME_PROVO_qualifiedDerivation);
   	 qualifiedInfluenceTable.put(QNAME_PROVO_Communication, QNAME_PROVO_qualifiedCommunication);
   	
   	 influencerTable.put(QNAME_PROVO_Influence, QNAME_PROVO_influencer);
   	 activityInfluence(QNAME_PROVO_Generation);
   	 entityInfluence(QNAME_PROVO_Usage);
   	 activityInfluence(QNAME_PROVO_Invalidation);
   	 entityInfluence(QNAME_PROVO_Start);
   	 entityInfluence(QNAME_PROVO_End);
   	 agentInfluence(QNAME_PROVO_Association);
   	 agentInfluence(QNAME_PROVO_Attribution);
   	 agentInfluence(QNAME_PROVO_Delegation);
   	 entityInfluence(QNAME_PROVO_Derivation);
   	 activityInfluence(QNAME_PROVO_Communication);
   	 
   	 unqualifiedTable.put(QNAME_PROVO_Influence, QNAME_PROVO_wasInfluencedBy);
   	 unqualifiedTable.put(QNAME_PROVO_Generation, QNAME_PROVO_wasGeneratedBy);
   	 unqualifiedTable.put(QNAME_PROVO_Usage, QNAME_PROVO_used);
   	 unqualifiedTable.put(QNAME_PROVO_Invalidation, QNAME_PROVO_wasInvalidatedBy);
   	 unqualifiedTable.put(QNAME_PROVO_Start, QNAME_PROVO_wasStartedBy);
  	 unqualifiedTable.put(QNAME_PROVO_End, QNAME_PROVO_wasEndedBy);
  	 unqualifiedTable.put(QNAME_PROVO_Association, QNAME_PROVO_wasAssociatedWith);
  	 unqualifiedTable.put(QNAME_PROVO_Attribution, QNAME_PROVO_wasAttributedTo);
  	 unqualifiedTable.put(QNAME_PROVO_Delegation, QNAME_PROVO_actedOnBehalfOf);
  	 unqualifiedTable.put(QNAME_PROVO_Derivation, QNAME_PROVO_wasDerivedFrom);
  	 unqualifiedTable.put(QNAME_PROVO_Communication, QNAME_PROVO_wasInformedBy);
  	 
  	 otherTable.put(QNAME_PROVO_Start, QNAME_PROVO_hadActivity);
  	 otherTable.put(QNAME_PROVO_End, QNAME_PROVO_hadActivity);
  	 otherTable.put(QNAME_PROVO_Derivation, QNAME_PROVO_hadActivity);
  	 otherTable.put(QNAME_PROVO_Association, QNAME_PROVO_hadPlan);
  	 otherTable.put(QNAME_PROVO_Delegation, QNAME_PROVO_hadActivity);
   }

    void activityInfluence(QName name) {
		influencerTable.put(name, QNAME_PROVO_activity);
	}
    void entityInfluence(QName name) {
		influencerTable.put(name, QNAME_PROVO_entity);
	}
    void agentInfluence(QName name) {
		influencerTable.put(name, QNAME_PROVO_agent);
	}
   

    
    final ElmoManager manager;

    private Hashtable<String, String> namespaceTable = new Hashtable<String, String>();

    public Hashtable<String, String> getNamespaceTable() {
        return namespaceTable;
    }

    final GraphBuilder gb;
    public RdfConstructor(ElmoManager manager) {
        this.manager = manager;
        initInfluenceTables();
        this.gb=new GraphBuilder(manager);
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
    	
        @SuppressWarnings("unused")
        QName der = addInfluence(id, entity2, entity1, null, activity, attributes,
                                 QNAME_PROVO_Derivation);

  
  
        if (der!=null) { //FIXME: a scruffy derivation could just have generation and usage, but der==null (no qualified derivation found
        	// since generation and usage are not taken into account.
          if (generation != null) {
             gb.assertStatement(gb.createObjectProperty(der, QNAME_PROVO_hadGeneration, generation));		
          }
         if (usage != null) {
             gb.assertStatement(gb.createObjectProperty(der, QNAME_PROVO_hadUsage, usage));		
          }
        }
   
        return null;
    }

    @Override
    public WasAssociatedWith newWasAssociatedWith(QName id, QName a, QName ag,
                                                  QName plan,
                                                  Collection<Attribute> attributes) {
    	
      	
        @SuppressWarnings("unused")
        QName d = addInfluence(id, a, ag, null, plan, attributes,
                QNAME_PROVO_Association);


        return null;
    }

    @Override
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,
                                              Collection<Attribute> attributes) {
        @SuppressWarnings("unused")
        QName a = addInfluence(id, e, ag, null, null, attributes,
                QNAME_PROVO_Attribution);

        return null;
    }

    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName agent2, QName agent1,
                                              QName a,
                                              Collection<Attribute> attributes) {
    	
        @SuppressWarnings("unused")
        QName d = addInfluence(id, agent2, agent1, null, a, attributes,
                QNAME_PROVO_Delegation);

        return null;
    }

    @Override
    public WasInformedBy newWasInformedBy(QName id, QName activity2, QName activity1,
                                          Collection<Attribute> attributes) {
    	
        @SuppressWarnings("unused")
        QName com = addInfluence(id, activity2, activity1, null, null, attributes, QNAME_PROVO_Communication);

        return null;
    }

    @Override
    public WasInfluencedBy newWasInfluencedBy(QName id, QName qn2, QName qn1,
                                              Collection<Attribute> attributes) {


        @SuppressWarnings("unused")
        QName u = addInfluence(id, qn2, qn1, null, null, attributes, QNAME_PROVO_Influence);
        
        return null;
    }

    @Override
    public AlternateOf newAlternateOf(QName entity2, QName entity1) {

    	if ((entity2!=null) && (entity1!=null))
            gb.assertStatement(gb.createObjectProperty(entity2, QNAME_PROVO_alternateOf, entity1));		
 
    	return null;
    }

    @Override
    public SpecializationOf newSpecializationOf(QName entity2, QName entity1) {
    	
    	
       	if ((entity2!=null) && (entity1!=null))
            gb.assertStatement(gb.createObjectProperty(entity2, QNAME_PROVO_specializationOf, entity1));		

        return null;
    }

    @Override
    public MentionOf newMentionOf(QName entity2, QName entity1, QName b) {


       	if ((entity2!=null) && (entity1!=null))
            gb.assertStatement(gb.createObjectProperty(entity2, QNAME_PROVO_mentionOf, entity1));		
       	if ((entity2!=null) && (b!=null))
            gb.assertStatement(gb.createObjectProperty(entity2, QNAME_PROVO_asInBundle, b));		

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
        gb.setContext();
    }

    @Override
    public void startBundle(QName bundleId, Hashtable<String, String> namespaces) {
        System.out.println("$$$$$$$$$$$$ in startBundle");
        // TODO: bundle name does not seem to be interpreted according to the
        // prefix declared in bundle.
        URIImpl uri = gb.qnameToURI(bundleId);
        contexts.add(uri);
        if (bundleId != null) {
            gb.setContext(uri);
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
	processAttributes(gb.qnameToResource(q), attributes);
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
                literalImpl = new LiteralImpl(qnAsString, gb.qnameToURI(type));

            } else {
                value = attr.getValue().toString();
                literalImpl = new LiteralImpl(value, gb.qnameToURI(type));
            }
            pred = attr.getElementName();

            gb.assertStatement(gb.createDataProperty(r, pred, literalImpl));
        }
    }

   
    public  QName addInfluence(QName infl,
                                     QName subject,
                                      QName object,
                                      XMLGregorianCalendar time, QName other, Collection<Attribute> attributes,
                                      QName qualifiedClass) {
        if ((infl != null)
        		|| (time!=null)
        		|| (other!=null)
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
            gb.assertStatement(gb.createObjectProperty(subject, unqualifiedTable.get(qualifiedClass), object));

        return infl;
    }

    public void asserterOther(QName subject, QName other, QName qualifiedClass) {
        gb.assertStatement(gb.createObjectProperty(subject, otherTable.get(qualifiedClass), other));		
	}

	public void assertAtTime(QName subject, XMLGregorianCalendar time) {
		gb.assertStatement(gb.createDataProperty(subject,QNAME_PROVO_atTime,newLiteral(time)));
		
	}

	private LiteralImpl newLiteral(XMLGregorianCalendar time) {
		return new LiteralImpl(time.toString(), gb.qnameToURI(ValueConverter.QNAME_XSD_HASH_DATETIME));
	}

	public void assertQualifiedInfluence(QName subject, QName infl, QName qualifiedClass) {
	gb.assertStatement(gb.createObjectProperty(subject, 
	                                     qualifiedInfluenceTable.get(qualifiedClass), 
	                                     infl));	
    }

    public void assertInfluencer(QName infl, QName object, QName qualifiedClass) {
	gb.assertStatement(gb.createObjectProperty(infl,
	                                     influencerTable.get(qualifiedClass),
	                                     object));	
    }
    
    
    public QName assertType(QName infl, QName qualifiedClass) {
    	if (infl==null) {
    		infl=gb.newBlankName();
    		//Resource uri=new BNodeImpl(infl.getLocalPart());
//    		gb.assertStatement(gb.createObjectProperty(uri, QNAME_RDF_TYPE, qualifiedClass));
//    	} else {
//    		gb.assertStatement(gb.createObjectProperty(infl, QNAME_RDF_TYPE, qualifiedClass));
    	}
    	gb.assertStatement(gb.createObjectProperty(infl, QNAME_RDF_TYPE, qualifiedClass));
		return infl;
    }

    private void setTime(InstantaneousEvent infl, XMLGregorianCalendar time) {
        if (infl != null) {
            XMLGregorianCalendar t = (XMLGregorianCalendar) time;
            infl.getAtTime().add(t);
        }
    }

  
    public <INFLUENCE, EFFECT> void DELETEaddQualifiedInfluence(EFFECT e2, INFLUENCE g) {
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
