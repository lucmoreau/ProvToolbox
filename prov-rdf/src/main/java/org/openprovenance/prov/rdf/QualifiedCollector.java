package org.openprovenance.prov.rdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.Element;
import org.openprovenance.prov.xml.HasLocation;
import org.openprovenance.prov.xml.HasRole;
import org.openprovenance.prov.xml.HasTime;
import org.openprovenance.prov.xml.Identifiable;
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
		this.baseProperties.put(PROV + "entity", PROV + "influencer");
		this.baseProperties.put(PROV + "agent", PROV + "influencer");
		this.baseProperties.put(PROV + "activity", PROV + "influencer");

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
				ProvType[] types = getResourceTypes(contextQ, qname);
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
					case END:
						createEnd(contextQ, qname);
						break;
					case ENTITYINFLUENCE:
						createEntityInfluence(contextQ, qname);
						break;
					case INVALIDATION:
						createInvalidation(contextQ, qname);
						break;
					case START:
						createStart(contextQ, qname);
						break;
					case INFLUENCE:
						createInfluence(contextQ, qname);
						break;
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

	private void handleInfluence(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements, ProvType type)
	{
		super.handleBaseStatements(target, context, target.getId(), type);
		/*
		 * WasInfluencedBy wib = new WasInfluencedBy();
		 * wib.setInfluencee(pFactory.newAnyRef(target.getId()));
		 * 
		 * for (Statement statement : statements) { String predS =
		 * statement.getPredicate().stringValue(); Value value =
		 * statement.getObject();
		 * 
		 * if (value instanceof Resource) { QName valueQ =
		 * qNameFromResource((Resource) value); if
		 * (this.baseProperties.containsKey(predS) &&
		 * this.baseProperties.get(predS).equals( PROV + "influencer")) {
		 * wib.setInfluencer(pFactory.newAnyRef(valueQ)); } } }
		 * 
		 * store(context, wib);
		 */
	}

	private void handleAgentInfluence(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements, ProvType type)
	{
		handleInfluence(context, target, statements, type);
	}

	private void handleActivityInfluence(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements, ProvType type)
	{
		handleInfluence(context, target, statements, type);
	}

	private void handleEntityInfluence(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements, ProvType type)
	{
		handleInfluence(context, target, statements, type);
	}

	private void handleInstantaneousEvent(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements, ProvType type)
	{
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Literal)
			{
				if (predS.equals(PROV + "atTime") && target instanceof HasTime)
				{
					XMLGregorianCalendar time = (XMLGregorianCalendar) super
							.decodeLiteral((Literal) value);
					((HasTime) target).setTime(time);
				}
			}

			if (value instanceof Resource)
			{
				QName valueQ = convertResourceToQName((Resource) value);

				if (predS.equals(PROV + "hadRole") && target instanceof HasRole)
				{
					// TODO: Not really clarified as to what 'Role' is here.
					((HasRole) (target)).getRole().add(
							pFactory.newAnyRef(valueQ));
				} 
			}
		}
	}

	private void createEntityInfluence(QName context, QName qname)
	{
		WasInfluencedBy wib = new WasInfluencedBy();
		wib.setId(qname);
		List<Statement> statements = collators.get(context).get(qname);
		handleEntityInfluence(context, wib, statements,
				ProvType.ENTITYINFLUENCE);
	}

	private void createDerivation(QName context, QName qname)
	{
		WasDerivedFrom wdf = new WasDerivedFrom();
		wdf.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				QName valueQ = convertResourceToQName((Resource) value);

				if (predS.equals(PROV + "hadActivity"))
				{
					wdf.setActivity(pFactory.newActivityRef(valueQ));
				}

				if (predS.equals(PROV + "hadGeneration"))
				{
					wdf.setGeneration(pFactory.newGenerationRef(valueQ));
				}

				if (predS.equals(PROV + "hadUsage"))
				{
					wdf.setUsage(pFactory.newUsageRef(valueQ));
				}

				if (predS.equals(PROV + "entity"))
				{
					QName entityQ = convertResourceToQName((Resource) value);
					wdf.setUsedEntity(pFactory.newEntityRef(entityQ));
				}
			}
		}
		handleEntityInfluence(context, wdf, statements, ProvType.DERIVATION);
		store(context, wdf);
		this.influenceMap.put(qname, wdf);
	}

	private void createInfluence(QName context, QName qname)
	{
		WasInfluencedBy wib = new WasInfluencedBy();
		wib.setId(qname);
		List<Statement> statements = collators.get(context).get(qname);
		handleInfluence(context, wib, statements, ProvType.INFLUENCE);
		this.influenceMap.put(qname, wib);

	}

	private void createEnd(QName context, QName qname)
	{
		WasEndedBy web = new WasEndedBy();
		web.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "entity"))
			{
				QName entityQ = convertResourceToQName((Resource) value);
				web.setTrigger(pFactory.newEntityRef(entityQ));
			}
			if (predS.equals(PROV + "hadActivity"))
			{
				QName activityQ = convertResourceToQName((Resource) value);
				web.setEnder(pFactory.newActivityRef(activityQ));
			}
		}

		handleEntityInfluence(context, web, statements, ProvType.END);
		handleInstantaneousEvent(context, web, statements, ProvType.END);

		store(context, web);
		this.influenceMap.put(qname, web);
	}

	private void createStart(QName context, QName qname)
	{
		WasStartedBy wsb = new WasStartedBy();
		wsb.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "entity"))
			{
				QName entityQ = convertResourceToQName((Resource) value);
				wsb.setTrigger(pFactory.newEntityRef(entityQ));
			}
			if (predS.equals(PROV + "hadActivity"))
			{
				QName activityQ = convertResourceToQName((Resource) value);
				wsb.setStarter(pFactory.newActivityRef(activityQ));
			}
		}

		handleEntityInfluence(context, wsb, statements, ProvType.START);
		handleInstantaneousEvent(context, wsb, statements, ProvType.START);

		store(context, wsb);
		this.influenceMap.put(qname, wsb);
	}

	private void createInvalidation(QName context, QName qname)
	{
		WasInvalidatedBy wib = new WasInvalidatedBy();
		wib.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "activity"))
			{
				QName activityQ = convertResourceToQName((Resource) value);
				wib.setActivity(pFactory.newActivityRef(activityQ));
			}

		}

		handleActivityInfluence(context, wib, statements, ProvType.INVALIDATION);
		handleInstantaneousEvent(context, wib, statements,
				ProvType.INVALIDATION);

		store(context, wib);
		this.influenceMap.put(qname, wib);
	}

	private void createDelegation(QName context, QName qname)
	{
		ActedOnBehalfOf aobo = new ActedOnBehalfOf();
		aobo.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "agent"))
			{
				QName agentQ = convertResourceToQName((Resource) value);
				aobo.setResponsible(pFactory.newAgentRef(agentQ));
			}

			if (predS.equals(PROV + "hadActivity"))
			{
				QName activityQ = convertResourceToQName((Resource) value);
				aobo.setActivity(pFactory.newActivityRef(activityQ));
			}
		}

		handleAgentInfluence(context, aobo, statements, ProvType.DELEGATION);

		store(context, aobo);
		this.influenceMap.put(qname, aobo);
	}

	private void createCommunication(QName context, QName qname)
	{
		WasInformedBy wib = new WasInformedBy();
		wib.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "activity"))
			{
				QName activityQ = convertResourceToQName((Resource) value);
				wib.setCause(pFactory.newActivityRef(activityQ));
			}
		}

		handleActivityInfluence(context, wib, statements,
				ProvType.COMMUNICATION);

		store(context, wib);
		this.influenceMap.put(qname, wib);
	}

	private void createAttribution(QName context, QName qname)
	{
		WasAttributedTo wat = new WasAttributedTo();
		wat.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "agent"))
			{
				QName agentQ = convertResourceToQName((Resource) value);
				wat.setAgent(pFactory.newAgentRef(agentQ));
			}
		}

		handleAgentInfluence(context, wat, statements, ProvType.ATTRIBUTION);

		store(context, wat);
		this.influenceMap.put(qname, wat);
	}

	private void createAssociation(QName context, QName qname)
	{
		WasAssociatedWith waw = new WasAssociatedWith();
		waw.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "hadPlan"))
				{
					QName entityQ = convertResourceToQName((Resource) value);
					waw.setPlan(pFactory.newEntityRef(entityQ));
				}

				if (predS.equals(PROV + "agent"))
				{
					QName agentQ = convertResourceToQName((Resource) value);
					waw.setAgent(pFactory.newAgentRef(agentQ));
				}
			}
		}

		handleAgentInfluence(context, waw, statements, ProvType.ASSOCIATION);

		store(context, waw);
		this.influenceMap.put(qname, waw);
	}

	private void createUsage(QName context, QName qname)
	{
		Used used = new Used();
		used.setId(qname);
		List<Statement> statements = collators.get(context).get(qname);

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "entity"))
				{
					QName entityQ = convertResourceToQName((Resource) value);
					used.setEntity(pFactory.newEntityRef(entityQ));
				}
			}
		}

		handleEntityInfluence(context, used, statements, ProvType.USAGE);
		handleInstantaneousEvent(context, used, statements, ProvType.USAGE);

		store(context, used);
		this.influenceMap.put(qname, used);
	}

	private void createGeneration(QName context, QName qname)
	{

		WasGeneratedBy wgb = pFactory.newWasGeneratedBy(qname);

		List<Statement> statements = collators.get(context).get(qname);

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "activity"))
				{
					QName activityQ = convertResourceToQName((Resource) value);
					wgb.setActivity(pFactory.newActivityRef(activityQ));
				}
			}
		}
		handleActivityInfluence(context, wgb, statements, ProvType.GENERATION);
		handleInstantaneousEvent(context, wgb, statements, ProvType.GENERATION);

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

					String predS = statement.getPredicate().stringValue();
					Value value = statement.getObject();
					if (value instanceof Resource)
					{
						QName refQ = convertResourceToQName((Resource) value);

						if (predS.equals(RdfCollector.PROV
								+ "qualifiedAssociation"))
						{
							WasAssociatedWith waw = (WasAssociatedWith) influenceMap
									.get(refQ);
							waw.setActivity(pFactory.newActivityRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedAttribution"))
						{
							WasAttributedTo wat = (WasAttributedTo) influenceMap
									.get(refQ);
							wat.setEntity(pFactory.newEntityRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedCommunication"))
						{
							WasInformedBy wib = (WasInformedBy) influenceMap
									.get(refQ);
							wib.setEffect(pFactory.newActivityRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedDelegation"))
						{
							ActedOnBehalfOf aobo = (ActedOnBehalfOf) influenceMap
									.get(refQ);
							aobo.setSubordinate(pFactory.newAgentRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedDerivation"))
						{
							WasDerivedFrom wdf = (WasDerivedFrom) influenceMap
									.get(refQ);
							wdf.setGeneratedEntity(pFactory.newEntityRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedEnd"))
						{

							WasEndedBy web = (WasEndedBy) influenceMap
									.get(refQ);
							web.setActivity(pFactory.newActivityRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedGeneration"))
						{
							WasGeneratedBy wgb = (WasGeneratedBy) influenceMap
									.get(refQ);
							wgb.setEntity(pFactory.newEntityRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedInfluence"))
						{
							WasInfluencedBy wib = (WasInfluencedBy) influenceMap
									.get(refQ);
							wib.setInfluencee(pFactory.newAnyRef(qname));

						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedInvalidation"))
						{

							WasInvalidatedBy wib = (WasInvalidatedBy) influenceMap
									.get(refQ);
							wib.setEntity(pFactory.newEntityRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedStart"))
						{
							WasStartedBy wsb = (WasStartedBy) influenceMap
									.get(refQ);
							wsb.setActivity(pFactory.newActivityRef(qname));
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedUsage"))
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
