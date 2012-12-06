package org.openprovenance.prov.rdf;

import java.net.URI;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.notation.TreeConstructor;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvUtilities;
import org.openprovenance.prov.xml.Used;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 * Initial convertor to rdf.
 */
public class RdfConstructor implements TreeConstructor {
	final ProvFactory pFactory;
	final ElmoManager manager;
	final ProvUtilities pUtil;

	private Hashtable<String, String> namespaceTable = new Hashtable<String, String>();

	public Hashtable<String, String> getNamespaceTable()
	{
		return namespaceTable;
	}

	public RdfConstructor(ProvFactory pFactory, ElmoManager manager)
	{
		this.pFactory = pFactory;
		this.pUtil = new ProvUtilities();
		this.manager = manager;
		pFactory.setNamespaces(namespaceTable);
	}

	public Object convertActivity(Object id, Object startTime, Object endTime,
			Object aAttrs)
	{
		QName qname = getQName(id);
		Activity a = (Activity) designateIfNotNull(qname, Activity.class);
		if (startTime != null && startTime instanceof XMLGregorianCalendar)
		{
			a.getStartedAtTime().add((XMLGregorianCalendar) startTime);
		}
		if (endTime != null && endTime instanceof XMLGregorianCalendar)
		{
			a.getEndedAtTime().add((XMLGregorianCalendar) endTime);
		}
		processAttributes(a, (List<?>) aAttrs);
		return a;
	}

	public Object convertEntity(Object id, Object attrs)
	{
		QName qname = getQName(id);
		Entity e = (Entity) designateIfNotNull(qname, Entity.class);
		processAttributes(e, (List<?>) attrs);
		return e;
	}

	public Object convertAgent(Object id, Object attrs)
	{
		QName qname = getQName(id);
		Agent ag = (Agent) designateIfNotNull(qname, Agent.class);
		processAttributes(ag, (List<?>) attrs);
		return ag;
	}

	public Object convertDocument(Object nss, List<Object> records,
			List<Object> bundles)
	{
	    if (nss!=null) {
	        Hashtable<String,String> nss2=(Hashtable<String,String>) nss;
	        getNamespaceTable().putAll(nss2);
	    }
	    //System.out.println("convertDocument -> " + nss);
	    ((SesameManager) manager).getConnection().setAddContexts();
	    return null;
	}

	public Object convertBundle(Object id, Object nss, List<Object> records)
	{
	    //TODO, where are the namespaces stored?
		((SesameManager) manager).getConnection().setAddContexts();
		return null;
	}

	public void startBundle(Object bundleId)
	{
		// TODO: bundle name does not seem to be interpreted according to the
		// prefix declared in bundle.
		// TODO: handle prefix declarations
		QName qname = getQName(bundleId);
		if (qname != null)
		{
			((SesameManager) manager).getConnection()
					.setAddContexts(
							new URIImpl(qname.getNamespaceURI()
									+ qname.getLocalPart()));
		}

	}

	public Object convertAttributes(List<Object> attributes)
	{
		return attributes;
	}

	public Object convertId(String id)
	{
		return id;
	}

	public Object convertAttribute(Object name, Object value)
	{
		return new Object[] { name, value };
	}

	public Object convertStart(String start)
	{
		return null;
	}

	public Object convertEnd(String end)
	{
		return null;
	}

	public Object convertString(String s)
	{
		s = unwrap(s);
		return s;
	}

	public Object convertString(String s, String lang)
	{
		s = unwrap(s);
		return s + "@" + lang;
	}

	public Object convertInt(int s)
	{
		return s;
	}

	private URIImpl uriFromQName(QName qname)
	{
		return new URIImpl(qname.getNamespaceURI() + qname.getLocalPart());
	}

	public void processAttributes(Object infl, List<?> aAttrs)
	{
		if (aAttrs == null)
			return;
		if (infl == null) {
		    throw new NullPointerException(); //should never be here, really
		}

		
		
	//	QName a=null;
	//	org.openrdf.model.Resource r = new URIImpl(a.getNamespaceURI()
	//			+ a.getLocalPart());

		org.openrdf.model.Resource r=((org.openrdf.elmo.sesame.roles.SesameEntity)infl).getSesameResource();
		
		
		for (Object entry : aAttrs)
		{

			QName pred = null;
			QName type = null;

			LiteralImpl literalImpl = null;
			if (entry instanceof Attribute)
			{

				Attribute attr = ((Attribute) entry);
				String typeAsString = attr.getXsdType();
				type = getQName(typeAsString);
				
				String value;
				if (attr.getValue() instanceof InternationalizedString) {
				    InternationalizedString iString=(InternationalizedString)attr.getValue();
				    value=iString.getValue();
				    literalImpl = new LiteralImpl(value, iString.getLang());
				} else if (attr.getValue() instanceof QName) {
				    QName qn=(QName)attr.getValue();
				    String qnAsString;
				    if ((qn.getPrefix()==null) || (qn.getPrefix().equals(""))) {
				        qnAsString=qn.getLocalPart();
				    } else {
				        qnAsString=qn.getPrefix()+ ":" + qn.getLocalPart();
				    }
				    literalImpl = new LiteralImpl(qnAsString, uriFromQName(type));

				} else {
				    value= attr.getValue().toString();
				    literalImpl = new LiteralImpl(value, uriFromQName(type));
				}
				pred = ((Attribute) entry).getElementName();

			} else if (entry instanceof Object[])
			{
				Object[] pair = (Object[]) entry;

				if (pair[1] instanceof Object[])
				{
					Object[] typedLit = (Object[]) pair[1];

					if (typedLit[0] instanceof InternationalizedString)
					{
						// We have a language - so string & language.
						InternationalizedString is = (InternationalizedString) (typedLit[0]);
						literalImpl = new LiteralImpl(is.getValue(),
								is.getLang());
					} else
					{
						// We won't have a language, so typedLit[1] is the type.
						String typeAsString = (String) (typedLit[1]);
						type = getQName(typeAsString);
						String value = unwrap((String) typedLit[0]);
						literalImpl = new LiteralImpl(value, uriFromQName(type));
					}
				} else if (pair[1] instanceof String)
				{
					literalImpl = new LiteralImpl((String) pair[1],
							uriFromQName(getQName("xsd:string")));
				} else
				{
					throw new UnsupportedOperationException();
				}

				pred = getQName(pair[0]);

				// if (pair[0].equals("prov:type"))
				// {
				// if (typeAsString.equals("xsd:QName"))
				// { // TODO: this should
				// // become prov:qualified
				// // name
				// System.out.println("----> " + value);
				// Class<?> cl = reservedClass(value);
				// if (cl != null)
				// designate(a, cl);
				//
				// }
				// }
			}

			org.openrdf.model.Statement stmnt = new StatementImpl(r,
					new URIImpl(pred.getNamespaceURI() + pred.getLocalPart()),
					literalImpl);

			try
			{
				((org.openrdf.elmo.sesame.SesameManager) manager)
						.getConnection().add(stmnt);
			} catch (org.openrdf.repository.RepositoryException e)
			{
			}
		}
	}

	private Class<?> reservedClass(String value)
	{
		if (value.equals("prov:Plan"))
			return Plan.class;
		if (value.equals("prov:SoftwareAgent"))
			return SoftwareAgent.class;
		if (value.equals("prov:Person"))
			return Person.class;
		if (value.equals("prov:Organization"))
			return Organization.class;
		if (value.equals("prov:Bundle"))
			return Bundle.class;
		if (value.equals("prov:Collection"))
			return Collection.class;
		return null;
	}

	public <INFLUENCE, TYPE> INFLUENCE addEntityInfluence(Object id, TYPE e2,
			Entity e1, Object time, Object aAttrs, Object other,
			Class<INFLUENCE> cl)
	{

		INFLUENCE infl = null;

		if ((id != null) || (time != null)
				|| ((aAttrs != null) && !(((List<?>) aAttrs).isEmpty()))
				|| (other != null))
		{
			QName qname = getQName(id);
			infl = designate(qname, cl);  // if qname is null, create an blank node
			EntityInfluence qi = (EntityInfluence) infl;
			if (e1 != null) qi.getEntities().add(e1);
			addQualifiedInfluence(e2, infl);

			if (time != null) {
				setTime((InstantaneousEvent)infl, time);
			}
			processAttributes(qi, (List<?>) aAttrs);
		}
		return infl;
	}

	public <INFLUENCE> INFLUENCE addUnknownInfluence(Object id,
			ActivityOrAgentOrEntity e2, ActivityOrAgentOrEntity e1,
			Object aAttrs, Class<INFLUENCE> cl)
	{

		INFLUENCE infl = null;

		if ((id != null)
				|| ((aAttrs != null) && !(((List<?>) aAttrs).isEmpty())))
		{
			QName qname = getQName(id);
			infl = designate(qname, cl);
			Influence qi = (Influence) infl;
			if (e1!=null) qi.getInfluencers().add(e1);
			addQualifiedInfluence(e2, infl);

			processAttributes(qi, (List<?>) aAttrs);
		}
		return infl;
	}

	private void setTime(InstantaneousEvent infl, Object time) {
		if(infl != null) {
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
	
	public <INFLUENCE, TYPE> INFLUENCE addActivityInfluence(Object id, TYPE e2,
			Activity a1, Object time, Object aAttrs, Class<INFLUENCE> cl)
	{

		INFLUENCE infl = null;

		if ((id != null) || (time != null)
				|| ((aAttrs != null) && !(((List<?>) aAttrs).isEmpty())))
		{
			QName qname = getQName(id);
			infl = designate(qname, cl);
			ActivityInfluence qi = (ActivityInfluence) infl;
			if (a1!=null) qi.getActivities().add(a1);
			addQualifiedInfluence(e2, infl);

			if (time != null) {
				setTime((InstantaneousEvent)infl, time);
			}
			
			processAttributes(qi, (List<?>) aAttrs);
		}
		return infl;
	}

	public <INFLUENCE, TYPE> INFLUENCE addAgentInfluence(Object id, TYPE e2,
			Agent a1, Object time, Object aAttrs, Object other,
			Class<INFLUENCE> cl)
	{

		INFLUENCE infl = null;

		if ((id != null) || (time != null)
				|| ((aAttrs != null) && !(((List<?>) aAttrs).isEmpty()))
				|| (other != null))
		{
			QName qname = getQName(id);
			infl = designate(qname, cl);
			AgentInfluence qi = (AgentInfluence) infl;
			if (a1!=null) qi.getAgents().add(a1);
			addQualifiedInfluence(e2, infl);

			if (time != null) {
				setTime((InstantaneousEvent)infl, time);
			}
			processAttributes(qi, (List<?>) aAttrs);
		}
		return infl;
	}

	public Object convertUsed(Object id, Object id2, Object id1, Object time,
			Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		Entity e1 = designateIfNotNull(qn1,Entity.class);
		Activity a2 = designateIfNotNull(qn2, Activity.class);
		Usage u = addEntityInfluence(id, a2, e1, time, aAttrs, null,
				Usage.class);

		if ((binaryProp(id,a2)) && (e1!=null)) a2.getUsed().add(e1);

		return u;
	}

	public <INFLUENCE, TYPE1> INFLUENCE test(INFLUENCE foo, TYPE1 foo1)
	{
		return foo;
	}

	// not pretty

	public <INFLUENCE, EFFECT> void addQualifiedInfluence(EFFECT e2, INFLUENCE g)
	{
		if ((g != null) && (e2 !=null))
		{
			if (g instanceof Generation)
			{
				((Entity) e2).getQualifiedGeneration().add((Generation) g);
			} else if (g instanceof Invalidation)
			{
				((Entity) e2).getQualifiedInvalidation().add((Invalidation) g);
			} else if (g instanceof Communication)
			{
				((Activity) e2).getQualifiedCommunication().add(
						(Communication) g);
			} else if (g instanceof Usage)
			{
				((Activity) e2).getQualifiedUsage().add((Usage) g);
			} else if (g instanceof Start)
			{
				((Activity) e2).getQualifiedStart().add((Start) g);
			} else if (g instanceof End)
			{
				((Activity) e2).getQualifiedEnd().add((End) g);
			} else if (g instanceof Attribution)
			{
				((Entity) e2).getQualifiedAttribution().add((Attribution) g);
			} else if (g instanceof Association)
			{
				((Activity) e2).getQualifiedAssociation().add((Association) g);
			} else if (g instanceof Delegation)
			{
				((Agent) e2).getQualifiedDelegation().add((Delegation) g);
			} else if (g instanceof Derivation)
			{
				((Entity) e2).getQualifiedDerivation().add((Derivation) g);
			} else if (g instanceof Influence)
			{
				((ActivityOrAgentOrEntity) e2).getQualifiedInfluence().add(
						(Influence) g);
			} else
			{
				throw new UnsupportedOperationException();
			}
		}
	}

	public Object convertWasGeneratedBy(Object id, Object id2, Object id1,
			Object time, Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		Entity e2 =  designateIfNotNull(qn2, Entity.class);
		Activity a1 = designateIfNotNull(qn1, Activity.class);

		Generation g = addActivityInfluence(id, e2, a1, time, aAttrs,
				Generation.class);

		if ((binaryProp(id,e2)) && (a1!=null)) e2.getWasGeneratedBy().add(a1);
		return g;
	}

	public Object convertWasStartedBy(Object id, Object id2, Object id1,
			Object id3, Object time, Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);
		QName qn3 = getQName(id3);

		Entity e1 =  designateIfNotNull(qn1, Entity.class);
		Activity a2 =  designateIfNotNull(qn2, Activity.class);
		Start s = addEntityInfluence(id, a2, e1, time, aAttrs, id3, Start.class);

		if (qn3 != null)
		{
			Activity a3 = designateIfNotNull(qn3, Activity.class);
			s.getHadActivity().add(a3);
		}

		if ((binaryProp(id,a2)) && (e1!=null)) a2.getWasStartedBy().add(e1);

		return s;
	}

	public Object convertWasEndedBy(Object id, Object id2, Object id1,
			Object id3, Object time, Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);
		QName qn3 = getQName(id3);

		Entity e1 = designateIfNotNull(qn1, Entity.class);
		Activity a2 = designateIfNotNull(qn2, Activity.class);
		End s = addEntityInfluence(id, a2, e1, time, aAttrs, id3, End.class);

		if (qn3 != null)
		{
			Activity a3 = designateIfNotNull(qn3,Activity.class);
			s.getHadActivity().add(a3);
		}

		if ((binaryProp(id,a2)) && (e1!=null)) a2.getWasEndedBy().add(e1);

		return s;
	}

	public Object convertWasInvalidatedBy(Object id, Object id2, Object id1,
			Object time, Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		Entity e2 = designateIfNotNull(qn2, Entity.class);
		Activity a1 = designateIfNotNull(qn1, Activity.class);

		Invalidation g = addActivityInfluence(id, e2, a1, time, aAttrs,
				Invalidation.class);

		if ((binaryProp(id,e2))&&(a1!=null)) e2.getWasInvalidatedBy().add(a1);
		return g;
	}

	public Object convertWasInformedBy(Object id, Object id2, Object id1,
			Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		Activity e2 = designateIfNotNull(qn2, Activity.class);
		Activity a1 = designateIfNotNull(qn1, Activity.class);

		Communication g = addActivityInfluence(id, e2, a1, null, aAttrs,
				Communication.class);

		if ((binaryProp(id,e2)) && (a1!=null)) e2.getWasInformedBy().add(a1);
		return g;
	}

	public Object convertWasAttributedTo(Object id, Object id2, Object id1,
			Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		Entity e2 =  designateIfNotNull(qn2, Entity.class);
		Agent a1 =  designateIfNotNull(qn1, Agent.class);

		Attribution g = addAgentInfluence(id, e2, a1, null, aAttrs, null,
				Attribution.class);

		if ((binaryProp(id,e2)) && (a1!=null)) e2.getWasAttributedTo().add(a1);
		return g;
	}

	public Object convertWasDerivedFrom(Object id, Object id2, Object id1,
			Object a, Object gen2, Object use1, Object dAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);
		QName qn3 = getQName(a);

		QName qn4 = getQName(use1);
		QName qn5 = getQName(gen2);

		Entity e2 = designateIfNotNull(qn2, Entity.class);
		Entity e1 = designateIfNotNull(qn1, Entity.class);

		Object other = a;
		if (qn4 != null)
		{
			other = qn4;
		} else
		{
			if (qn5 != null)
				other = qn5;
		}

		Derivation d = addEntityInfluence(id, e2, e1, null, dAttrs, other,
				Derivation.class);

		
		if (d != null)
		{
			if (qn5 != null) 
			{
				Generation g5 = designateIfNotNull(qn5, Generation.class);
				d.getHadGeneration().add(g5);
			}
			if (qn4 != null) 
			{
				Usage u4 = designateIfNotNull(qn4, Usage.class);
				d.getHadUsage().add(u4);
			}
			if (qn3 != null)
			{
				Activity a3 =  designateIfNotNull(qn3,Activity.class);
				d.getHadActivity().add(a3);
			}
		}

		if ((binaryProp(id,e2)) && (e1!=null)) e2.getWasDerivedFrom().add(e1);

		return d;

	}



	public Object convertWasInfluencedBy(Object id, Object id2, Object id1,
			Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		ActivityOrAgentOrEntity e1 = designateIfNotNull(qn1, ActivityOrAgentOrEntity.class);
		ActivityOrAgentOrEntity e2 = designateIfNotNull(qn2, ActivityOrAgentOrEntity.class);
		//(ActivityOrAgentOrEntity) manager
		//		.find(qn1);
		//ActivityOrAgentOrEntity e2 = manager
		//		.designate(qn2, ActivityOrAgentOrEntity.class);

		Influence u = addUnknownInfluence(id, e2, e1, aAttrs, Influence.class);

		if ((binaryProp(id,e2)) && (e1!=null)) e2.getWasInfluencedBy().add(e1);

		return u;

	}

	public Object convertAlternateOf(Object id2, Object id1)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		Entity e2 =  designateIfNotNull(qn2, Entity.class);
		Entity e1 =  designateIfNotNull(qn1, Entity.class);

		e2.getAlternateOf().add(e1);
		return null;
	}

	public Object convertSpecializationOf(Object id2, Object id1)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		Entity e2 = designateIfNotNull(qn2, Entity.class);
		Entity e1 = designateIfNotNull(qn1, Entity.class);

		e2.getSpecializationOf().add(e1);
		return null;
	}

	public Object convertActedOnBehalfOf(Object id, Object id2, Object id1,
			Object a, Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);
		QName qn3 = getQName(a);

		Agent ag2 = designateIfNotNull(qn2, Agent.class);
		Agent ag1 = designateIfNotNull(qn1, Agent.class);

		Delegation g = addAgentInfluence(id, ag2, ag1, null, aAttrs, a,
				Delegation.class);

		if (qn3 != null)
		{
			Activity a3 =  designateIfNotNull(qn3, Activity.class);
			g.getHadActivity().add(a3);
		}

		if ((binaryProp(id,ag2)) && (ag1!=null)) ag2.getActedOnBehalfOf().add(ag1);

		return g;
	}

	public Object convertWasAssociatedWith(Object id, Object id2, Object id1,
			Object pl, Object aAttrs)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);
		QName qn3 = getQName(pl);

		Activity a2 =  designateIfNotNull(qn2, Activity.class);
		Agent ag1 =  designateIfNotNull(qn1, Agent.class);

		Association assoc = addAgentInfluence(id, a2, ag1, null, aAttrs, pl,
				Association.class);

		if ((qn3 != null) && (assoc!=null))
		{
			Plan plan = (Plan) designateIfNotNull(qn3, Plan.class);
			// will declare it as Plan if
			// not alreadydone
			assoc.getHadPlan().add(plan);
		}

		if ((binaryProp(id,a2)) && (ag1!=null)) a2.getWasAssociatedWith().add(ag1);

		return assoc;
	}

	public Object convertExtension(Object name, Object id, Object args,
			Object dAttrs)
	{
		return null;
	}

	public Object convertQualifiedName(String qname)
	{
		return qname;
	}

	public Object convertIRI(String iri)
	{
		iri = unwrap(iri);
		return URI.create(iri);
	}

	public Object convertTypedLiteral(String datatype, Object value)
	{
		return new Object[] { value, datatype };
	}

	public Object convertNamespace(Object pre, Object iri)
	{
		String s_pre = (String) pre;
		String s_iri = (String) iri;
		s_iri = unwrap(s_iri);
		namespaceTable.put(s_pre, s_iri);
		return null;
	}

	public Object convertDefaultNamespace(Object iri)
	{
		String s_iri = (String) iri;
		s_iri = unwrap(s_iri);
		namespaceTable.put("_", s_iri);
		return null;
	}

	public Object convertNamespaces(List<Object> namespaces)
	{
		pFactory.setNamespaces(namespaceTable);
		return namespaceTable;
	}

	public Object convertPrefix(String pre)
	{
		return pre;
	}

	public QName getQName(Object id)
	{
		if (id == null)
		{
			return null;
		}
		String idAsString = (String) id;
		return pFactory.stringToQName(idAsString);
	}

	public String unwrap(String s)
	{
		return s.substring(1, s.length() - 1);
	}

	/* Component 5 */

	public Object convertInsertion(Object id, Object id2, Object id1,
			Object map, Object dAttrs)
	{
		// todo
		throw new UnsupportedOperationException();
	}

	public Object convertEntry(Object o1, Object o2)
	{
		// todo
		throw new UnsupportedOperationException();
	}

	public Object convertKeyEntitySet(List<Object> o)
	{
		// todo
		throw new UnsupportedOperationException();
	}

	public Object convertRemoval(Object id, Object id2, Object id1,
			Object keys, Object dAttrs)
	{
		// todo
		throw new UnsupportedOperationException();
	}

	public Object convertDictionaryMemberOf(Object id, Object id2, Object map,
			Object complete, Object dAttrs)
	{
		// todo
		throw new UnsupportedOperationException();
	}

	public Object convertCollectionMemberOf(Object id, Object id2, Object map,
			Object complete, Object dAttrs)
	{
		// todo
		throw new UnsupportedOperationException();
	}

	public Object convertKeys(List<Object> keys)
	{
		// todo
		throw new UnsupportedOperationException();
	}

	/* Component 6 */

	public Object convertMentionOf(Object id2, Object id1, Object bu)
	{
		QName qn2 = getQName(id2);
		QName qn1 = getQName(id1);

		QName qn3 = getQName(bu);

		Entity e2 =  designateIfNotNull(qn2, Entity.class);
		Entity e1 =  designateIfNotNull(qn1, Entity.class);
		Bundle e3 =  designateIfNotNull(qn3, Bundle.class); // will
		// declare it
		// as plan if
		// not
		// already
		// done
		System.out.println("e2: "+e2);
		System.out.println("e1: "+e1);
		if (e2!=null) {
		    if (e1!=null) e2.getMentionOf().add(e1);
		    if (e3!=null) e2.getAsInBundle().add(e3);
		}

		return null;
	}
	
	
	public <T> T designateIfNotNull(QName qname, Class<T> cl) {
	    if (qname==null) return null;
	    return designate(qname,cl);
	}
	public <T> T designate(QName qname, Class<T> cl) {
	   
	    return manager.designate(qname,cl);
	}
	
	public boolean binaryProp(Object id, Object subject) {
	    return id==null && subject!=null;
	}

	@Override
	public Object convertHadMember(Object collection, Object entity) {
	    QName qnc = getQName(collection);
	    QName qne = getQName(entity);

	    Collection c =  designateIfNotNull(qnc, Collection.class);
	    Entity e =  designateIfNotNull(qne, Entity.class);

	    c.getHadMember().add(e);
	    return null;
	}
	

}
