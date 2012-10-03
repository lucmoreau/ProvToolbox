package org.openprovenance.prov.rdf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.Element;
import org.openprovenance.prov.xml.HadPrimarySource;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.HasLabel;
import org.openprovenance.prov.xml.HasRole;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasAttributedTo;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInfluencedBy;
import org.openprovenance.prov.xml.WasInformedBy;
import org.openprovenance.prov.xml.WasInvalidatedBy;
import org.openprovenance.prov.xml.WasQuotedFrom;
import org.openprovenance.prov.xml.WasRevisionOf;
import org.openprovenance.prov.xml.WasStartedBy;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class RdfCollector extends RDFHandlerBase {

	private static String PROV = "http://www.w3.org/ns/prov#";
	private static String XMLS = "http://www.w3.org/2001/XMLSchema#";

	enum ProvType
	{

		ENTITY("Entity"), AGENT("Agent"), ACTIVITY("Activity"), GENERATION(
				"Generation"), ASSOCIATION("Association"), DERIVATION(
				"Derivation"), COMMUNICATION("Communication"), INFLUENCE(
				"Influence"), USAGE("Usage"), END("End"), START("Start"), DELEGATION(
				"Delegation"), INVALIDATION("Invalidation"), ATTRIBUTION(
				"Attribution"), BUNDLE("Bundle", ProvType.ENTITY), ORGANIZATION(
				"Organization", ProvType.AGENT), PERSON("Person",
				ProvType.AGENT), SOFTWAREAGENT("SoftwareAgent", ProvType.AGENT), LOCATION(
				"Location"), ROLE("Role"), PLAN("Plan", ProvType.ENTITY);

		private static final Map<String, ProvType> lookup = new HashMap<String, ProvType>();
		static
		{
			for (ProvType provStatement : EnumSet.allOf(ProvType.class))
				lookup.put(provStatement.getURI(), provStatement);
		}

		private String uri;
		private ProvType replacement;

		private ProvType(String localName)
		{
			this.uri = PROV + localName;
			this.replacement = null;
		}

		private ProvType(String localName, ProvType replaceWith)
		{
			this(localName);
			this.replacement = replaceWith;
		}

		public String getURI()
		{
			return uri;
		}

		public ProvType getReplacement()
		{
			return this.replacement;
		}

		public boolean hasReplacement()
		{
			return (this.replacement != null);
		}

		public static ProvType lookup(String uri)
		{
			return lookup.get(uri);
		}

		public String toString()
		{
			return uri;
		}

	}

	private ProvFactory pFactory;
	private HashMap<QName, Set<Statement>> collator;
	private Document document;
	private Hashtable<String, String> revnss;

	public RdfCollector(ProvFactory pFactory)
	{
		this.pFactory = pFactory;
		this.collator = new HashMap<QName, Set<Statement>>();
		this.revnss = new Hashtable<String, String>();
		this.document = pFactory.newDocument();
		document.setNss(new Hashtable<String, String>());
	}

	@Override
	public void handleNamespace(String prefix, String namespace)
	{
		System.out.println("Namespace: " + prefix + " " + namespace);
		this.document.getNss().put(prefix, namespace);
		this.revnss.put(namespace, prefix);
	}

	private boolean isProvURI(URI uri)
	{
		if (!uri.getNamespace().equals(PROV))
		{
			return false;
		}
		return true;
	}

	private ProvType getResourceType(QName qname)
	{
		Set<Statement> statements = collator.get(qname);
		ProvType currentType = null;
		Set<Statement> removedStatements = new HashSet<Statement>();
		for (Statement statement : statements)
		{
			if (statement.getPredicate().equals(RDF.TYPE))
			{
				Value value = statement.getObject();
				if (value instanceof URI && !isProvURI((URI) value))
				{
					continue;
				}
				ProvType provType = ProvType.lookup(value.stringValue());

				if (provType.hasReplacement())
				{
					currentType = provType.getReplacement();
				} else
				{
					currentType = provType;
				}

				// TODO: We need to make sure that Organization, etc, can still
				// come back out in an export.

				removedStatements.add(statement);
			}
		}
		statements.removeAll(removedStatements);
		return currentType;
	}

	private Object decodeLiteral(Literal literal)
	{
		String dataType = XMLS+"string";
		if(literal.getDatatype() != null) {
			dataType = literal.getDatatype().stringValue();
		}
		
		if (dataType.equals(XMLS + "QName"))
		{
			return new QName(literal.stringValue());
		} else if (dataType.equals(XMLS + "string"))
		{
			return literal.stringValue();
		} else if (dataType.equals(XMLS + "dateTime"))
		{
			return literal.calendarValue();
		} else
		{
			return null;
		}
	}

	private Set<Statement> handleBaseStatements(Element element, QName qname)
	{

		Set<Statement> statements = collator.get(qname);
		Set<Statement> removedStatements = new HashSet<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();

			if (element instanceof HasType)
			{
				if (statement.getPredicate().stringValue()
						.equals(PROV + "type"))
				{
					Value value = statement.getObject();
					if (value instanceof Literal)
					{
						Object literalValue = decodeLiteral((Literal) (value));
						if (literalValue == null)
						{
							System.out
									.println("Null :( " + value.stringValue());
						}
						pFactory.addType((HasType) element,
								decodeLiteral((Literal) value));
						removedStatements.add(statement);
					} else
					{
						System.out.println(value);
						System.out.println("Value wasn't a suitable type");
					}
				}
			}

			if (element instanceof HasRole)
			{
				if (predS.equals(PROV + "role"))
				{
					String role = statement.getObject().stringValue();
					pFactory.addRole((HasRole) element, role);
					removedStatements.add(statement);
				}
			}

			if (element instanceof HasLabel)
			{
				if (predS.equals(RDFS.LABEL.toString()))
				{
					String label = statement.getObject().stringValue();
					pFactory.addLabel((HasExtensibility) element, label);
					removedStatements.add(statement);
				}
			}

			/*
			 * TODO: Waiting on a HasLocation interface.
			 */
			// if (element instanceof HasLocation)
			// {
			// if (predS.equals(PROV + "atLocation"))
			// {
			// Value value = statement.getObject();
			// URI predicateURI = statement.getPredicate();
			//
			// String prefix = null;
			// if(revnss.containsKey(predicateURI.getNamespace())) {
			// prefix = revnss.get(predicateURI.getNamespace());
			// }
			//
			// pFactory.addAttribute((HasExtensibility)element,
			// statement.getPredicate()
			// .getNamespace(), prefix, statement.getPredicate()
			// .getLocalName(), value.stringValue());
			//
			// removedStatements.add(statement);
			// }
			// }

			if (predS.equals(PROV + "wasInfluencedBy"))
			{
				Value value = statement.getObject();
				QName anyQ = new QName(value.stringValue());
				WasInfluencedBy wib = pFactory.newWasInfluencedBy((QName) null,
						pFactory.newAnyRef(qname), pFactory.newAnyRef(anyQ));

				this.document.getEntityOrActivityOrWasGeneratedBy().add(wib);
				removedStatements.add(statement);
			}
		}

		statements.removeAll(removedStatements);

		return statements;
	}

	@Override
	public void endRDF()
	{
		for (QName qname : collator.keySet())
		{
			System.out.println("Get resource type " + qname);
			ProvType type = getResourceType(qname);

			switch (type)
			{
			case ACTIVITY:
				createActivity(qname);
				break;
			case AGENT:
				createAgent(qname);
				break;
			case ENTITY:
				createEntity(qname);
				break;
			default:
				System.out.println("Unhandled type: " + type);
			}
		}

		for (QName qname : collator.keySet())
		{
			if (collator.get(qname).size() > 0)
			{
				System.out.println("Unhandled statements in " + qname);
				for (Statement statement : collator.get(qname))
				{
					if (isProvURI(statement.getPredicate()))
					{
						System.out.println(statement);
					}
				}
			}
		}
	}

	private List<Statement> getStatementsForPredicate(QName qname, String uri)
	{
		ArrayList<Statement> statements = new ArrayList<Statement>();
		for (Statement statement : collator.get(qname))
		{
			if (statement.getPredicate().stringValue().equals(uri))
			{
				statements.add(statement);
			}
		}
		return statements;
	}

	private Statement getSingleStatementForPredicate(QName qname, String uri)
	{
		List<Statement> statements = getStatementsForPredicate(qname, uri);
		assert (statements.size() <= 1);

		Statement statement = null;
		if (statements.size() == 1)
		{
			statement = statements.get(0);
		}
		return statement;
	}

	private void createEntity(QName qname)
	{
		org.openprovenance.prov.xml.Entity entity = pFactory.newEntity(qname);
		this.document.getEntityOrActivityOrWasGeneratedBy().add(entity);

		Set<Statement> statements = collator.get(qname);
		statements = handleBaseStatements(entity, qname);

		Set<Statement> removedStatements = new HashSet<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "wasDerivedFrom"))
				{
					QName entityQ = new QName(value.stringValue());
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ));

					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wdf);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "wasGeneratedBy"))
				{
					QName activityQ = new QName(value.stringValue());
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(
							(QName) null, pFactory.newEntityRef(qname), null,
							pFactory.newActivityRef(activityQ));

					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wgb);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "alternateOf"))
				{
					QName entityQ = new QName(value.stringValue());
					AlternateOf ao = pFactory.newAlternateOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ));

					this.document.getEntityOrActivityOrWasGeneratedBy().add(ao);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "specializationOf"))
				{
					QName entityQ = new QName(value.stringValue());
					SpecializationOf so = pFactory.newSpecializationOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ));

					this.document.getEntityOrActivityOrWasGeneratedBy().add(so);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "wasInvalidatedBy"))
				{
					QName activityQ = new QName(value.stringValue());
					WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newActivityRef(activityQ));

					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wib);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "wasAttributedTo"))
				{
					QName agentQ = new QName(value.stringValue());
					WasAttributedTo wit = pFactory.newWasAttributedTo(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newAgentRef(agentQ));

					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wit);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "mentionOf"))
				{
					Statement asInBundleStatement = getSingleStatementForPredicate(
							qname, PROV + "asInBundle");
					removedStatements.add(asInBundleStatement);

					QName bundleQ = new QName(asInBundleStatement.getObject()
							.stringValue());

					QName entityQ = new QName(value.stringValue());
					MentionOf nmo = pFactory.newMentionOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ),
							pFactory.newEntityRef(bundleQ));

					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(nmo);
					removedStatements.add(statement);
				} // The following might be deprecated?
				else if (predS.equals(PROV + "hadPrimarySource"))
				{
					QName entityQ = new QName(value.stringValue());
					HadPrimarySource hps = pFactory.newHadPrimarySource(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ));
					// TODO: Can't add to document yet.
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "wasRevisionOf"))
				{
					QName entityQ = new QName(value.stringValue());
					WasRevisionOf wro = pFactory.newWasRevisionOf((QName) null,
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wro);
					removedStatements.add(statement);
				}else if (predS.equals(PROV + "wasQuotedFrom"))
				{
					QName entityQ = new QName(value.stringValue());
					WasQuotedFrom wqf = pFactory.newWasQuotedFrom((QName) null,
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ));
					// TODO: Can't add to document yet.
					removedStatements.add(statement);
				}
			} else if (value instanceof Literal)
			{
				if (predS.equals(PROV + "generatedAtTime"))
				{
					// TODO: Unable to implement this.
					Object literal = decodeLiteral((Literal) value);
					removedStatements.add(statement);
				} if (predS.equals(PROV + "invalidatedAtTime"))
				{
					// TODO: Unable to implement this.
					Object literal = decodeLiteral((Literal) value);
					removedStatements.add(statement);
				}
				else if (predS.equals(PROV + "value"))
				{
					// TODO: Unable to implement this.
					Object literal = decodeLiteral((Literal) value);
					entity.setValue(literal);
					removedStatements.add(statement);
				}
			}
		}
		statements.removeAll(removedStatements);

		// decodeAttributes(entity, statements)
	}

	private void createAgent(QName qname)
	{
		org.openprovenance.prov.xml.Agent agent = pFactory.newAgent(qname);
		this.document.getEntityOrActivityOrWasGeneratedBy().add(agent);

		Set<Statement> statements = collator.get(qname);
		statements = handleBaseStatements(agent, qname);

		Set<Statement> removedStatements = new HashSet<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "actedOnBehalfOf"))
				{
					QName agentQ = new QName(value.stringValue());
					ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(
							(QName) null, pFactory.newAgentRef(qname),
							pFactory.newAgentRef(agentQ), null);

					this.document.getEntityOrActivityOrWasGeneratedBy().add(
							aobo);
					removedStatements.add(statement);
				}
			}
		}
		statements.removeAll(removedStatements);
	}

	private void createActivity(QName qname)
	{
		org.openprovenance.prov.xml.Activity activity = pFactory
				.newActivity(qname);
		this.document.getEntityOrActivityOrWasGeneratedBy().add(activity);
		Set<Statement> statements = collator.get(qname);
		statements = handleBaseStatements(activity, qname);

		Set<Statement> removedStatements = new HashSet<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "wasAssociatedWith"))
				{	
					QName agentQ = new QName(value.stringValue());
					WasAssociatedWith waw = pFactory.newWasAssociatedWith(
							(QName) null, pFactory.newActivityRef(qname),
							pFactory.newAgentRef(agentQ));
					
					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(waw);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "used"))
				{
					QName entityQ = new QName(value.stringValue());
					Used used = pFactory.newUsed((QName) null,
							pFactory.newActivityRef(qname), null,
							pFactory.newEntityRef(entityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy().add(
							used);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "wasStartedBy"))
				{
					QName entityQ = new QName(value.stringValue());
					WasStartedBy wsb = pFactory.newWasStartedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(entityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wsb);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "generated"))
				{
					// TODO: Not in ProvFactory.
					QName entityQ = new QName(value.stringValue());
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "wasEndedBy"))
				{
					QName entityQ = new QName(value.stringValue());
					WasEndedBy web = pFactory.newWasEndedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(entityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(web);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "wasInformedBy"))
				{
					QName activityQ = new QName(value.stringValue());
					WasInformedBy wib = pFactory.newWasInformedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newActivityRef(activityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wib);
					removedStatements.add(statement);
				}
			} else if (value instanceof Literal)
			{
				if (predS.equals(PROV + "startedAtTime"))
				{
					Object literal = decodeLiteral((Literal) value);
					activity.setStartTime((XMLGregorianCalendar) literal);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "endedAtTime"))
				{
					Object literal = decodeLiteral((Literal) value);
					activity.setEndTime((XMLGregorianCalendar) literal);
					removedStatements.add(statement);
				}
			}
		}
		statements.removeAll(removedStatements);
	}

	@Override
	public void handleStatement(Statement statement)
	{
		QName subjectQ = null;

		Resource subject = statement.getSubject();
		if (subject instanceof BNodeImpl)
		{
			BNodeImpl bnode = (BNodeImpl) subject;
			subjectQ = new QName(bnode.getID());
		} else if (subject instanceof URI)
		{
			URI uri = (URI) subject;
			subjectQ = new QName(uri.getNamespace(), uri.getLocalName());
		} else
		{
			System.err.println("Invalid subject resource");
		}

		if (!collator.containsKey(subjectQ))
		{
			collator.put(subjectQ, new HashSet<Statement>());
		}

		collator.get(subjectQ).add(statement);
	}

	public Document getDocument()
	{
		return document;
	}
}