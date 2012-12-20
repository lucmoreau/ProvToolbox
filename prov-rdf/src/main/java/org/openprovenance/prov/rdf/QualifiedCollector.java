package org.openprovenance.prov.rdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.Identifiable;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Ref;
import org.openprovenance.prov.xml.StatementOrBundle;
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
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;

public class QualifiedCollector extends RdfCollector {

	private Hashtable<QName, org.openprovenance.prov.xml.Influence> influenceMap;
	private Hashtable<String, String> baseProperties;

	public QualifiedCollector(ProvFactory pFactory)
	{
		super(pFactory);
		this.pFactory = pFactory;
		this.influenceMap = new Hashtable<QName, org.openprovenance.prov.xml.Influence>();

		this.baseProperties = new Hashtable<String, String>();
		this.baseProperties.put( NamespacePrefixMapper.PROV_NS + "entity", NamespacePrefixMapper.PROV_NS + "influencer");
		this.baseProperties.put(NamespacePrefixMapper.PROV_NS + "agent", NamespacePrefixMapper.PROV_NS + "influencer");
		this.baseProperties.put(NamespacePrefixMapper.PROV_NS + "activity", NamespacePrefixMapper.PROV_NS + "influencer");

	}

	@Override
	protected void buildGraph()
	{
		super.buildGraph();
		for (QName contextQ : collators.keySet())
		{
			HashMap<QName, List<Statement>> collator = collators.get(contextQ);
			for (QName qname : collator.keySet())
			{
				ProvType[] types = getExplicitTypes(contextQ, qname);
				for (ProvType type : types)
				{
					switch (type)
					{
					case GENERATION:
						createGeneration(contextQ, qname);
						break;
					case USAGE:
						createUsage(contextQ, qname);
						break;
					case ASSOCIATION:
						createAssociation(contextQ, qname);
						break;
					case ATTRIBUTION:
						createAttribution(contextQ, qname);
						break;
					case COMMUNICATION:
						createCommunication(contextQ, qname);
						break;
					case DELEGATION:
						createDelegation(contextQ, qname);
						break;
					case DERIVATION:
						createDerivation(contextQ, qname);
						break;
					case PRIMARYSOURCE:
						createPrimarySource(contextQ, qname);
						break;
					case QUOTATION:
						createQuotation(contextQ, qname);
						break;
					case REVISION:
						createRevision(contextQ, qname);
						break;
					case END:
						createEnd(contextQ, qname);
						break;
//					case ENTITYINFLUENCE:
//						createEntityInfluence(contextQ, qname);
//						break;
					case INVALIDATION:
						createInvalidation(contextQ, qname);
						break;
					case START:
						createStart(contextQ, qname);
						break;
					//case INFLUENCE:
					//	createInfluence(contextQ, qname);
					//	break;
					default:
						break;
					}
				}
			}
		}
	}

	@Override
	public void endRDF()
	{
		super.endRDF();
		this.bindQualifiedProperties();
		this.optimize();
		this.nullBNodes();
	}

	private List<Ref> getSignature(
			org.openprovenance.prov.xml.Influence influence)
	{
		List<Ref> signature = null;
		if (influence instanceof WasGeneratedBy)
		{
			WasGeneratedBy wgb = (WasGeneratedBy) influence;
			if (wgb.getEntity() != null && wgb.getActivity() != null)
			{
				signature = Arrays.asList(new Ref[] { wgb.getEntity(),
						wgb.getActivity() });
			}
		} else if (influence instanceof WasInvalidatedBy)
		{
			WasInvalidatedBy wib = (WasInvalidatedBy) influence;
			if (wib.getActivity() != null && wib.getEntity() != null)
			{
				signature = Arrays.asList(new Ref[] { wib.getActivity(),
						wib.getEntity() });
			}
		} else if (influence instanceof Used)
		{
			Used u = (Used) influence;
			if (u.getActivity() != null && u.getEntity() != null)
			{
				signature = Arrays.asList(new Ref[] { u.getActivity(),
						u.getEntity() });
			}
		} else if (influence instanceof WasDerivedFrom)
		{
			WasDerivedFrom wdf = (WasDerivedFrom) influence;
			if (wdf.getGeneratedEntity() != null && wdf.getUsedEntity() != null)
			{
				signature = Arrays.asList(new Ref[] { wdf.getGeneratedEntity(),
						wdf.getUsedEntity() });
			}
		} else
		{
			// System.out.println("Couldn't get signature for "
			// + influence.getId());
		}
		return signature;
	}

	private void optimize()
	{
		HashMap<List<Ref>, Identifiable> collisions = new HashMap<List<Ref>, Identifiable>();

		List<Identifiable> toRemove = new ArrayList<Identifiable>();
		for (StatementOrBundle sob : document
				.getEntityOrActivityOrWasGeneratedBy())
		{
			if (sob instanceof org.openprovenance.prov.xml.Influence)
			{

				Identifiable hasid = (Identifiable) sob;
				List<Ref> signature = getSignature((org.openprovenance.prov.xml.Influence) hasid);
				if (signature == null)
					continue;
				if (collisions.containsKey(signature))
				{
					Identifiable collision = collisions.get(signature);
					if (hasid.getId() != null)
					{
						// We have a qualified wgb, so remove the collision.
						toRemove.add(collision);
					} else if (collision.getId() != null)
					{
						// We have an unqualified wgb, so remove the wgb if the
						// collision is qualified.
						toRemove.add(hasid);
					}
				} else
				{
					collisions.put(signature, hasid);
				}
			}
		}

		document.getEntityOrActivityOrWasGeneratedBy().removeAll(toRemove);

	}

	private XMLGregorianCalendar getInstantaneousTime(List<Statement> statements) {
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Literal)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_atTime))
				{
					XMLGregorianCalendar time = (XMLGregorianCalendar) super
							.decodeLiteral((Literal) value);
					return time;
				}
			}
		}
		return null;
	}

	private void createRevision(QName contextQ, QName qname)
	{
		WasDerivedFrom wdf = createDerivation(contextQ, qname);
		Object q = pFactory.newQName("prov:Revision");
		if(!wdf.getType().contains(q)) {
			wdf.getType().add(q);
		}
	}

	private void createQuotation(QName contextQ, QName qname)
	{
		WasDerivedFrom wdf = createDerivation(contextQ, qname);
		Object q = pFactory.newQName("prov:Quotation");
		if(!wdf.getType().contains(q)) {
			wdf.getType().add(q);
		}
	}

	private void createPrimarySource(QName contextQ, QName qname)
	{
		WasDerivedFrom wdf = createDerivation(contextQ, qname);
		Object q = pFactory.newQName("prov:PrimarySource");
		if(!wdf.getType().contains(q)) {
			wdf.getType().add(q);
		}
	}

	private WasDerivedFrom createDerivation(QName context, QName qname)
	{
		QName e1 = null;
		QName e2 = null;
		QName activity = null;
		QName generation = null;
		QName usage = null;
		
		List<Statement> statements = collators.get(context).get(qname);

		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				QName valueQ = convertResourceToQName((Resource) value);

				if (predQ.equals(Ontology.QNAME_PROVO_hadActivity))
				{
					activity = valueQ;
				}

				if (predQ.equals(Ontology.QNAME_PROVO_hadGeneration))
				{
					generation = valueQ;
				}

				if (predQ.equals(Ontology.QNAME_PROVO_hadUsage))
				{
					usage = valueQ;
				}

				if (predQ.equals(Ontology.QNAME_PROVO_entity))
				{
					e1 = valueQ;
				}
			}
		}
		List<Attribute> attributes = super.collectAttributes(context, qname, ProvType.DERIVATION);
		WasDerivedFrom wdf = pFactory.newWasDerivedFrom(qname, e2, e1, activity, generation, usage, attributes);
		store(context, wdf);
		this.influenceMap.put(qname, wdf);
		return wdf;
	}

	private void createEnd(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		
		QName activity = null;
		QName trigger = null;
		QName ender = null;
		XMLGregorianCalendar time = getInstantaneousTime(statements);
		
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (predQ.equals(Ontology.QNAME_PROVO_entity))
			{
				trigger = convertResourceToQName((Resource) value);
			}
			if (predQ.equals(Ontology.QNAME_PROVO_hadActivity))
			{
				ender = convertResourceToQName((Resource) value);
			}
		}

		List<Attribute> attributes = collectAttributes(context, qname, ProvType.END);
		WasEndedBy web = pFactory.newWasEndedBy(qname, activity, trigger, ender, time, attributes);
		store(context, web);
		this.influenceMap.put(qname, web);
	}

	private void createStart(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		
		QName activity = null;
		QName trigger = null;
		QName starter = null;
		XMLGregorianCalendar time = getInstantaneousTime(statements);
		
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (predQ.equals(Ontology.QNAME_PROVO_entity))
			{
				trigger = convertResourceToQName((Resource) value);
			}
			if (predQ.equals(Ontology.QNAME_PROVO_hadActivity))
			{
				starter = convertResourceToQName((Resource) value);
			}
		}

		List<Attribute> attributes = collectAttributes(context, qname, ProvType.START);
		WasStartedBy wsb = pFactory.newWasStartedBy(qname, activity, trigger, starter, time, attributes);
		store(context, wsb);
		this.influenceMap.put(qname, wsb);
	}

	private void createInvalidation(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		QName entity = null;
		QName activity = null;
		XMLGregorianCalendar time = getInstantaneousTime(statements);
		
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (predQ.equals(Ontology.QNAME_PROVO_activity))
			{
				activity = convertResourceToQName((Resource) value);
			}
		}

		List<Attribute> attributes = collectAttributes(context, qname, ProvType.INVALIDATION);
		WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(qname, entity, activity, time, attributes);
		store(context, wib);
		this.influenceMap.put(qname, wib);
	}

	private void createDelegation(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		
		QName ag2 = null;
		QName ag1 = null;
		QName a = null;
		
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (predQ.equals(Ontology.QNAME_PROVO_agent))
			{
				ag1 = convertResourceToQName((Resource) value);
			}

			if (predQ.equals(Ontology.QNAME_PROVO_hadActivity))
			{
				a = convertResourceToQName((Resource) value);
			}
		}

		List<Attribute> attributes = super.collectAttributes(context, qname, ProvType.DELEGATION);
		ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(qname, ag2, ag1, a, attributes);
		store(context, aobo);
		this.influenceMap.put(qname, aobo);
	}

	private void createCommunication(QName context, QName qname)
	{
		QName a2 = null;
		QName a1 = null;
		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (predQ.equals(Ontology.QNAME_PROVO_activity))
			{
				a1 = convertResourceToQName((Resource) value);
			}
		}

		List<Attribute> attributes = super.collectAttributes(context, qname, ProvType.COMMUNICATION);
		WasInformedBy wib = pFactory.newWasInformedBy(qname, a2, a1, attributes);
		store(context, wib);
		this.influenceMap.put(qname, wib);
	}

	private void createAttribution(QName context, QName qname)
	{
		QName e = null;
		QName ag = null;
		
		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (predQ.equals(Ontology.QNAME_PROVO_agent))
			{
				ag = convertResourceToQName((Resource) value);
			}
		}

		List<Attribute> attributes = super.collectAttributes(context, qname, ProvType.ATTRIBUTION);
		WasAttributedTo wat = pFactory.newWasAttributedTo(qname, e, ag, attributes);
		store(context, wat);
		this.influenceMap.put(qname, wat);
	}

	private void createAssociation(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);

		QName a = null;
		QName ag = null;
		QName plan = null;
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_hadPlan))
				{
					plan = convertResourceToQName((Resource) value);
				}

				if (predQ.equals(Ontology.QNAME_PROVO_agent))
				{
					ag = convertResourceToQName((Resource) value);
				}
			}
		}

		List<Attribute> attributes = super.collectAttributes(context, qname, ProvType.ASSOCIATION);
		WasAssociatedWith waw = pFactory.newWasAssociatedWith(qname, a, ag, plan, attributes);
		store(context, waw);
		this.influenceMap.put(qname, waw);
	}

	private void createUsage(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		
		QName activity = null;
		QName entity = null;
		XMLGregorianCalendar time = getInstantaneousTime(statements);
		
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_entity))
				{
					entity =  convertResourceToQName((Resource) value);
				}
			}
		}

		List<Attribute> attributes = collectAttributes(context, qname, ProvType.USAGE);
		Used used = pFactory.newUsed(qname, activity, entity, time, attributes);
		store(context, used);
		this.influenceMap.put(qname, used);
	}

	private void createGeneration(QName context, QName qname)
	{
		QName entity = null;
		QName activity = null;
		List<Statement> statements = collators.get(context).get(qname);
		XMLGregorianCalendar time = getInstantaneousTime(statements);
		
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				System.out.println(predQ);
				if (predQ.equals(Ontology.QNAME_PROVO_activity))
				{
					activity = convertResourceToQName((Resource) value);
				}
			}
		}
		
		List<Attribute> attributes = collectAttributes(context, qname, ProvType.GENERATION);
		WasGeneratedBy wgb = pFactory.newWasGeneratedBy(qname, entity, activity, time, attributes);
		store(context, wgb);
		this.influenceMap.put(qname, wgb);
	}

	private void nullBNodes()
	{
		for (QName key : influenceMap.keySet())
		{
			if (key.getNamespaceURI() == "")
			{
				influenceMap.get(key).setId(null);
			}
		}
	}

	public void bindQualifiedProperties()
	{
		// Handle qualified attributes

		for (QName contextQ : collators.keySet())
		{
			HashMap<QName, List<Statement>> collator = collators.get(contextQ);
			for (QName qname : collator.keySet())
			{
				List<Statement> statements = collator.get(qname);
				for (Statement statement : statements)
				{

					QName predQ = convertURIToQName(statement.getPredicate());
					Value value = statement.getObject();
					if (value instanceof Resource)
					{
						QName refQ = convertResourceToQName((Resource) value);

						if (predQ.equals(Ontology.QNAME_PROVO_qualifiedAssociation))
						{
							WasAssociatedWith waw = (WasAssociatedWith) influenceMap
									.get(refQ);
							waw.setActivity(pFactory.newActivityRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedAttribution))
						{
							WasAttributedTo wat = (WasAttributedTo) influenceMap
									.get(refQ);
							wat.setEntity(pFactory.newEntityRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedCommunication))
						{
							WasInformedBy wib = (WasInformedBy) influenceMap
									.get(refQ);
							wib.setEffect(pFactory.newActivityRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedDelegation))
						{
							ActedOnBehalfOf aobo = (ActedOnBehalfOf) influenceMap
									.get(refQ);
							aobo.setSubordinate(pFactory.newAgentRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedDerivation)
								|| predQ.equals(Ontology.QNAME_PROVO_qualifiedPrimarySource)
								|| predQ.equals(Ontology.QNAME_PROVO_qualifiedRevision)
								|| predQ.equals(Ontology.QNAME_PROVO_qualifiedQuotation))
						{
							WasDerivedFrom wdf = (WasDerivedFrom) influenceMap
									.get(refQ);
							wdf.setGeneratedEntity(pFactory.newEntityRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedEnd))
						{

							WasEndedBy web = (WasEndedBy) influenceMap
									.get(refQ);
							web.setActivity(pFactory.newActivityRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedGeneration))
						{
							WasGeneratedBy wgb = (WasGeneratedBy) influenceMap
									.get(refQ);
							wgb.setEntity(pFactory.newEntityRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedInfluence))
						{
							WasInfluencedBy wib = (WasInfluencedBy) influenceMap
									.get(refQ);
							wib.setInfluencee(pFactory.newAnyRef(qname));

						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedInvalidation))
						{

							WasInvalidatedBy wib = (WasInvalidatedBy) influenceMap
									.get(refQ);
							wib.setEntity(pFactory.newEntityRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedStart))
						{
							WasStartedBy wsb = (WasStartedBy) influenceMap
									.get(refQ);
							wsb.setActivity(pFactory.newActivityRef(qname));
						} else if (predQ.equals(Ontology.QNAME_PROVO_qualifiedUsage))
						{
							Used used = (Used) influenceMap.get(refQ);
							used.setActivity(pFactory.newActivityRef(qname));
						}
					}
				}
			}
		}
	}
}
