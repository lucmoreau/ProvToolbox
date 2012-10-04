package org.openprovenance.prov.rdf;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.HasLocation;
import org.openprovenance.prov.xml.HasRole;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInfluencedBy;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
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
		for (QName qname : collator.keySet())
		{
			ProvType type = getResourceType(qname);

			switch (type)
			{
			case GENERATION:
				createGeneration(qname);
				break;
			case USAGE:
				createUsage(qname);
				break;
			case ASSOCIATION:
				createAssociation(qname);
				break;
			case DERIVATION:
				createDerivation(qname);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void endRDF()
	{
		super.endRDF();
		this.bindQualifiedProperties();
	}
	
	private void handleInfluence(Resource context,
			org.openprovenance.prov.xml.Influence target,
			Set<Statement> statements)
	{

		Set<Statement> removedStatements = new HashSet<Statement>();

		WasInfluencedBy wib = new WasInfluencedBy();
		wib.setInfluencee(pFactory.newAnyRef(target.getId()));

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				QName valueQ = qNameFromResource((Resource)value);
				if (this.baseProperties.containsKey(predS)
						&& this.baseProperties.get(predS).equals(
								PROV + "influencer"))
				{
					wib.setInfluencer(pFactory.newAnyRef(valueQ));
					removedStatements.add(statement);
				}

				if (predS.equals(PROV + "hadRole") && target instanceof HasRole)
				{
					// TODO: Not really clarified as to what 'Role' is here.
					((HasRole) (target)).getRole()
							.add(pFactory.newAnyRef(valueQ));
					removedStatements.add(statement);
				}
				
				// TODO: Activity not handled yet (not in WasInfluencedBy).
			}
		}

		statements.removeAll(removedStatements);
		store(context, wib);
	}	
	
	private void handleAgentInfluence(Resource context,
			org.openprovenance.prov.xml.Influence target,
			Set<Statement> statements)
	{		
		handleInfluence(context, target, statements);
	}

	private void handleActivityInfluence(Resource context,
			org.openprovenance.prov.xml.Influence target,
			Set<Statement> statements)
	{		
		handleInfluence(context, target, statements);
	}
	
	private void handleEntityInfluence(Resource context,
			org.openprovenance.prov.xml.Influence target,
			Set<Statement> statements)
	{		
		handleInfluence(context, target, statements);
	}

	private void handleInstantaneousEvent(Resource context,
			org.openprovenance.prov.xml.Influence target,
			Set<Statement> statements)
	{
		Set<Statement> removedStatements = new HashSet<Statement>();

		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Literal)
			{
				if (predS.equals(PROV + "atTime"))
				{
					XMLGregorianCalendar time = (XMLGregorianCalendar) super
							.decodeLiteral((Literal) value);

					// TODO: Until we have a HasTime interface.
					if (target instanceof org.openprovenance.prov.xml.WasGeneratedBy)
					{
						((org.openprovenance.prov.xml.WasGeneratedBy) target)
								.setTime(time);
					}
					if (target instanceof org.openprovenance.prov.xml.Used)
					{
						((org.openprovenance.prov.xml.Used) target)
								.setTime(time);
					}
					if (target instanceof org.openprovenance.prov.xml.WasInvalidatedBy)
					{
						((org.openprovenance.prov.xml.WasInvalidatedBy) target)
								.setTime(time);
					}
					if (target instanceof org.openprovenance.prov.xml.WasStartedBy)
					{
						((org.openprovenance.prov.xml.WasStartedBy) target)
								.setTime(time);
					}
					if (target instanceof org.openprovenance.prov.xml.WasEndedBy)
					{
						((org.openprovenance.prov.xml.WasEndedBy) target)
								.setTime(time);
					}

					removedStatements.add(statement);
				}
			}

			if (value instanceof Resource)
			{
				QName valueQ = qNameFromResource((Resource)value);
				
				if (predS.equals(PROV + "hadRole") && target instanceof HasRole)
				{
					// TODO: Not really clarified as to what 'Role' is here.
					((HasRole) (target)).getRole()
							.add(pFactory.newAnyRef(valueQ));
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
	
	private void createDerivation(QName qname) {
		WasDerivedFrom wdf = new WasDerivedFrom();
		wdf.setId(qname);
		Resource context = null;

		Set<Statement> statements = collator.get(qname);
		Set<Statement> removedStatements = new HashSet<Statement>();

		for (Statement statement : statements)
		{
			if (context == null)
			{
				context = statement.getContext();
			}

			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				QName valueQ = qNameFromResource((Resource)value);
								
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
					QName entityQ = qNameFromResource((Resource)value);
					wdf.setUsedEntity(pFactory.newEntityRef(entityQ));
				}
			}
		}
		statements.removeAll(removedStatements);

		handleEntityInfluence(context, wdf, statements);

		store(context, wdf);
		this.influenceMap.put(qname, wdf);
	}
	
	private void createAssociation(QName qname) {
		WasAssociatedWith waw = new WasAssociatedWith();
		waw.setId(qname);
		Resource context = null;

		Set<Statement> statements = collator.get(qname);
		Set<Statement> removedStatements = new HashSet<Statement>();

		for (Statement statement : statements)
		{
			if (context == null)
			{
				context = statement.getContext();
			}

			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "hadPlan"))
				{
					QName entityQ = qNameFromResource((Resource)value);
					waw.setPlan(pFactory.newEntityRef(entityQ));
				}
				
				if (predS.equals(PROV + "agent"))
				{
					QName agentQ = qNameFromResource((Resource)value);
					waw.setAgent(pFactory.newAgentRef(agentQ));
				}
			}
		}
		statements.removeAll(removedStatements);

		handleAgentInfluence(context, waw, statements);

		store(context, waw);
		this.influenceMap.put(qname, waw);
	}

	private void createUsage(QName qname)
	{
		Used used = new Used();
		used.setId(qname);
		Resource context = null;

		Set<Statement> statements = collator.get(qname);
		Set<Statement> removedStatements = new HashSet<Statement>();

		for (Statement statement : statements)
		{
			if (context == null)
			{
				context = statement.getContext();
			}

			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "entity"))
				{
					QName entityQ = qNameFromResource((Resource)value);
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

	private void createGeneration(QName qname)
	{

		WasGeneratedBy wgb = pFactory.newWasGeneratedBy(qname);

		Resource context = null;
		Set<Statement> statements = collator.get(qname);
		Set<Statement> removedStatements = new HashSet<Statement>();

		for (Statement statement : statements)
		{
			if (context == null)
			{
				context = statement.getContext();
			}

			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "activity"))
				{
					QName activityQ = qNameFromResource((Resource)value);
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
		for (QName qname : collator.keySet())
		{
			for (Statement statement : collator.get(qname))
			{

				String predS = statement.getPredicate().stringValue();
				Value value = statement.getObject();
				System.out.println(predS);
				System.out.println(value);
				if(value instanceof BNode) {
					System.out.println("WAHBNODE");
				}
				if (value instanceof Resource)
				{
					QName refQ = qNameFromResource((Resource)value);

					if (predS
							.equals(RdfCollector.PROV + "qualifiedAssociation"))
					{
						System.out.println("Bind assoc");
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedAttribution"))
					{

						System.out.println("Bind attr");
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedCommunication"))
					{

						System.out.println("Bind com");
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedDelegation"))
					{

						System.out.println("Bind del");
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedDerivation"))
					{
						WasDerivedFrom wdf = (WasDerivedFrom) influenceMap.get(refQ);
						wdf.setGeneratedEntity(pFactory.newEntityRef(qname));
						
					} else if (predS.equals(RdfCollector.PROV + "qualifiedEnd"))
					{

						System.out.println("Bind end");
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedGeneration"))
					{
						WasGeneratedBy wgb = (WasGeneratedBy) influenceMap.get(refQ);
						wgb.setEntity(pFactory.newEntityRef(qname));
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedInfluence"))
					{

						System.out.println("Bind inf");
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedInvalidation"))
					{

						System.out.println("Bind inv");
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedRevision"))
					{

						System.out.println("Bind rev");
					} else if (predS.equals(RdfCollector.PROV
							+ "qualifiedStart"))
					{

						System.out.println("Bind start");
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
