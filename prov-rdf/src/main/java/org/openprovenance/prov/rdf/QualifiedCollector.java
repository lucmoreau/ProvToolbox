package org.openprovenance.prov.rdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.HasLocation;
import org.openprovenance.prov.xml.HasRole;
import org.openprovenance.prov.xml.HasTime;
import org.openprovenance.prov.xml.ProvFactory;
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
					System.out.println("Handle type: " + type);
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
						System.out.println("Unhandled class: "+type);
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
		dumpUnhandled();
	}

	private void handleInfluence(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements)
	{

		List<Statement> removedStatements = new ArrayList<Statement>();

		WasInfluencedBy wib = new WasInfluencedBy();
		wib.setInfluencee(pFactory.newAnyRef(target.getId()));

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				QName valueQ = qNameFromResource((Resource) value);
				if (this.baseProperties.containsKey(predS)
						&& this.baseProperties.get(predS).equals(
								PROV + "influencer"))
				{
					wib.setInfluencer(pFactory.newAnyRef(valueQ));
					removedStatements.add(statement);
				}
			}
		}

		statements.removeAll(removedStatements);
		store(context, wib);
	}

	private void handleAgentInfluence(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements)
	{
		handleInfluence(context, target, statements);
	}

	private void handleActivityInfluence(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements)
	{
		handleInfluence(context, target, statements);
	}

	private void handleEntityInfluence(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements)
	{
		handleInfluence(context, target, statements);
	}

	private void handleInstantaneousEvent(QName context,
			org.openprovenance.prov.xml.Influence target,
			List<Statement> statements)
	{
		List<Statement> removedStatements = new ArrayList<Statement>();

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
					removedStatements.add(statement);
				}
			}

			if (value instanceof Resource)
			{
				QName valueQ = qNameFromResource((Resource) value);

				if (predS.equals(PROV + "hadRole") && target instanceof HasRole)
				{
					// TODO: Not really clarified as to what 'Role' is here.
					((HasRole) (target)).getRole().add(
							pFactory.newAnyRef(valueQ));
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "atLocation")
						&& target instanceof HasLocation)
				{
					((HasLocation) target).getLocation().add(
							pFactory.newAnyRef(valueQ));
					removedStatements.add(statement);
				}
			}
		}
		statements.removeAll(removedStatements);
	}

	private void createEntityInfluence(QName context, QName qname)
	{
		WasInfluencedBy wib = new WasInfluencedBy();
		wib.setId(qname);
		List<Statement> statements = collators.get(context).get(qname);
		handleEntityInfluence(context, wib, statements);
	}

	private void createDerivation(QName context, QName qname)
	{
		WasDerivedFrom wdf = new WasDerivedFrom();
		wdf.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();

		for (Statement statement : statements)
		{

			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				QName valueQ = qNameFromResource((Resource) value);

				if (predS.equals(PROV + "hadActivity"))
				{
					wdf.setActivity(pFactory.newActivityRef(valueQ));
					removedStatements.add(statement);
				}

				if (predS.equals(PROV + "hadGeneration"))
				{
					wdf.setGeneration(pFactory.newGenerationRef(valueQ));
					removedStatements.add(statement);
				}

				if (predS.equals(PROV + "hadUsage"))
				{
					wdf.setUsage(pFactory.newUsageRef(valueQ));
					removedStatements.add(statement);
				}

				if (predS.equals(PROV + "entity"))
				{
					QName entityQ = qNameFromResource((Resource) value);
					wdf.setUsedEntity(pFactory.newEntityRef(entityQ));
				}
			}
		}
		statements.removeAll(removedStatements);

		handleEntityInfluence(context, wdf, statements);

		store(context, wdf);
		this.influenceMap.put(qname, wdf);
	}

	private void createInfluence(QName context, QName qname)
	{
		System.out.println("Create an influence");
		WasInfluencedBy wib = new WasInfluencedBy();
		wib.setId(qname);
		System.out.println("ID: " + qname);
		List<Statement> statements = collators.get(context).get(qname);
		handleInfluence(context, wib, statements);
		this.influenceMap.put(qname, wib);

	}

	private void createEnd(QName context, QName qname)
	{
		WasEndedBy web = new WasEndedBy();
		web.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "entity"))
			{
				QName entityQ = qNameFromResource((Resource) value);
				web.setTrigger(pFactory.newEntityRef(entityQ));
			}

		}
		statements.removeAll(removedStatements);

		handleEntityInfluence(context, web, statements);
		handleInstantaneousEvent(context, web, statements);

		store(context, web);
		this.influenceMap.put(qname, web);
	}

	private void createStart(QName context, QName qname)
	{
		WasStartedBy wsb = new WasStartedBy();
		wsb.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "entity"))
			{
				QName entityQ = qNameFromResource((Resource) value);
				wsb.setTrigger(pFactory.newEntityRef(entityQ));
			}

		}
		statements.removeAll(removedStatements);

		handleEntityInfluence(context, wsb, statements);
		handleInstantaneousEvent(context, wsb, statements);

		store(context, wsb);
		this.influenceMap.put(qname, wsb);
	}

	private void createInvalidation(QName context, QName qname)
	{
		WasInvalidatedBy wib = new WasInvalidatedBy();
		wib.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "activity"))
			{
				QName activityQ = qNameFromResource((Resource) value);
				wib.setActivity(pFactory.newActivityRef(activityQ));
			}

		}
		statements.removeAll(removedStatements);

		handleActivityInfluence(context, wib, statements);
		handleInstantaneousEvent(context, wib, statements);

		store(context, wib);
		this.influenceMap.put(qname, wib);
	}

	private void createDelegation(QName context, QName qname)
	{
		ActedOnBehalfOf aobo = new ActedOnBehalfOf();
		aobo.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "agent"))
			{
				QName agentQ = qNameFromResource((Resource) value);
				aobo.setResponsible(pFactory.newAgentRef(agentQ));
			}

			if (predS.equals(PROV + "hadActivity"))
			{
				QName activityQ = qNameFromResource((Resource) value);
				aobo.setActivity(pFactory.newActivityRef(activityQ));
				removedStatements.add(statement);
			}
		}
		statements.removeAll(removedStatements);

		handleAgentInfluence(context, aobo, statements);

		store(context, aobo);
		this.influenceMap.put(qname, aobo);
	}

	private void createCommunication(QName context, QName qname)
	{
		WasInformedBy wib = new WasInformedBy();
		wib.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "activity"))
			{
				QName activityQ = qNameFromResource((Resource) value);
				wib.setCause(pFactory.newActivityRef(activityQ));
			}
		}
		statements.removeAll(removedStatements);

		handleActivityInfluence(context, wib, statements);

		store(context, wib);
		this.influenceMap.put(qname, wib);
	}

	private void createAttribution(QName context, QName qname)
	{
		WasAttributedTo wat = new WasAttributedTo();
		wat.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (predS.equals(PROV + "agent"))
			{
				QName agentQ = qNameFromResource((Resource) value);
				wat.setAgent(pFactory.newAgentRef(agentQ));
			}
		}
		statements.removeAll(removedStatements);

		handleAgentInfluence(context, wat, statements);

		store(context, wat);
		this.influenceMap.put(qname, wat);
	}

	private void createAssociation(QName context, QName qname)
	{
		WasAssociatedWith waw = new WasAssociatedWith();
		waw.setId(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "hadPlan"))
				{
					QName entityQ = qNameFromResource((Resource) value);
					waw.setPlan(pFactory.newEntityRef(entityQ));
				}

				if (predS.equals(PROV + "agent"))
				{
					QName agentQ = qNameFromResource((Resource) value);
					waw.setAgent(pFactory.newAgentRef(agentQ));
				}
			}
		}
		statements.removeAll(removedStatements);

		handleAgentInfluence(context, waw, statements);

		store(context, waw);
		this.influenceMap.put(qname, waw);
	}

	private void createUsage(QName context, QName qname)
	{
		Used used = new Used();
		used.setId(qname);
		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "entity"))
				{
					QName entityQ = qNameFromResource((Resource) value);
					used.setEntity(pFactory.newEntityRef(entityQ));
				}
			}
		}
		statements.removeAll(removedStatements);

		handleEntityInfluence(context, used, statements);
		handleInstantaneousEvent(context, used, statements);

		store(context, used);
		this.influenceMap.put(qname, used);
	}

	private void createGeneration(QName context, QName qname)
	{

		WasGeneratedBy wgb = pFactory.newWasGeneratedBy(qname);

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "activity"))
				{
					QName activityQ = qNameFromResource((Resource) value);
					wgb.setActivity(pFactory.newActivityRef(activityQ));
				}
			}
		}
		statements.removeAll(removedStatements);
		handleActivityInfluence(context, wgb, statements);
		handleInstantaneousEvent(context, wgb, statements);

		store(context, wgb);
		this.influenceMap.put(qname, wgb);
	}

	public void bindQualifiedProperties()
	{
		// Handle qualified attributes

		for (QName contextQ : collators.keySet())
		{
			HashMap<QName, List<Statement>> collator = collators.get(contextQ);
			for (QName qname : collator.keySet())
			{
				List<Statement> removedStatements = new ArrayList<Statement>();
				List<Statement> statements = collator.get(qname);
				for (Statement statement : statements)
				{

					String predS = statement.getPredicate().stringValue();
					Value value = statement.getObject();
					if (value instanceof Resource)
					{
						QName refQ = qNameFromResource((Resource) value);

						if (predS.equals(RdfCollector.PROV
								+ "qualifiedAssociation"))
						{
							WasAssociatedWith waw = (WasAssociatedWith) influenceMap
									.get(refQ);
							waw.setActivity(pFactory.newActivityRef(qname));
							removedStatements.add(statement);
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedAttribution"))
						{
							WasAttributedTo wat = (WasAttributedTo) influenceMap
									.get(refQ);
							wat.setEntity(pFactory.newEntityRef(qname));
							removedStatements.add(statement);
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedCommunication"))
						{
							WasInformedBy wib = (WasInformedBy) influenceMap
									.get(refQ);
							wib.setEffect(pFactory.newActivityRef(qname));
							removedStatements.add(statement);
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedDelegation"))
						{
							ActedOnBehalfOf aobo = (ActedOnBehalfOf) influenceMap
									.get(refQ);
							aobo.setSubordinate(pFactory.newAgentRef(qname));
							removedStatements.add(statement);
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedDerivation"))
						{
							WasDerivedFrom wdf = (WasDerivedFrom) influenceMap
									.get(refQ);
							wdf.setGeneratedEntity(pFactory.newEntityRef(qname));
							removedStatements.add(statement);

						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedEnd"))
						{

							WasEndedBy web = (WasEndedBy) influenceMap
									.get(refQ);
							web.setActivity(pFactory.newActivityRef(qname));
							removedStatements.add(statement);
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedGeneration"))
						{
							WasGeneratedBy wgb = (WasGeneratedBy) influenceMap
									.get(refQ);
							wgb.setEntity(pFactory.newEntityRef(qname));
							removedStatements.add(statement);
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedInfluence"))
						{
							WasInfluencedBy wib = (WasInfluencedBy) influenceMap
									.get(refQ);
							System.out.println(influenceMap.keySet());
							wib.setInfluencee(pFactory.newAnyRef(qname));
							removedStatements.add(statement);

						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedInvalidation"))
						{

							WasInvalidatedBy wib = (WasInvalidatedBy) influenceMap
									.get(refQ);
							wib.setEntity(pFactory.newEntityRef(qname));
							removedStatements.add(statement);
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedStart"))
						{
							WasStartedBy wsb = (WasStartedBy) influenceMap
									.get(refQ);
							wsb.setActivity(pFactory.newActivityRef(qname));
							removedStatements.add(statement);
						} else if (predS.equals(RdfCollector.PROV
								+ "qualifiedUsage"))
						{
							Used used = (Used) influenceMap.get(refQ);
							used.setActivity(pFactory.newActivityRef(qname));
							removedStatements.add(statement);
						}
					}
				}
				statements.removeAll(removedStatements);
			}
		}
	}
}
