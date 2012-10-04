package org.openprovenance.prov.rdf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.Element;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.HasLabel;
import org.openprovenance.prov.xml.HasLocation;
import org.openprovenance.prov.xml.HasRole;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.NamedBundle;
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
import org.openprovenance.prov.xml.WasStartedBy;
import org.openrdf.model.BNode;
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

	public static String PROV = "http://www.w3.org/ns/prov#";
	public static String XMLS = "http://www.w3.org/2001/XMLSchema#";

	enum ProvType
	{

		ENTITY("Entity"), AGENT("Agent"), ACTIVITY("Activity"), INFLUENCE(
				"Influence"),

		BUNDLE("Bundle", ProvType.ENTITY),

		ORGANIZATION("Organization", ProvType.AGENT),

		PERSON("Person", ProvType.AGENT),

		SOFTWAREAGENT("SoftwareAgent", ProvType.AGENT),

		LOCATION("Location"),

		ROLE("Role"),

		PLAN("Plan", ProvType.ENTITY),

		COLLECTION("Collection", ProvType.ENTITY),

		EMPTYCOLLECTION("EmptyCollection", ProvType.COLLECTION),

		INSTANTANEOUSEVENT("InstantaneousEvent"),

		ENTITYINFLUENCE("EntityInfluence", ProvType.INFLUENCE),

		ACTIVITYINFLUENCE("ActivityInfluence", ProvType.INFLUENCE),

		AGENTINFLUENCE("AgentInfluence", ProvType.INFLUENCE),

		ASSOCIATION("Association", ProvType.AGENTINFLUENCE),

		ATTRIBUTION("Attribution", ProvType.AGENTINFLUENCE),

		COMMUNICATION("Communication", ProvType.AGENTINFLUENCE),

		DELEGATION("Delegation", ProvType.AGENTINFLUENCE),

		DERIVATION("Derivation", ProvType.ENTITYINFLUENCE),

		END("End", new ProvType[] { ProvType.INSTANTANEOUSEVENT,
				ProvType.ENTITYINFLUENCE }),

		START("Start", new ProvType[] { ProvType.INSTANTANEOUSEVENT,
				ProvType.ENTITYINFLUENCE }),

		GENERATION("Generation", new ProvType[] { ProvType.INSTANTANEOUSEVENT,
				ProvType.ACTIVITYINFLUENCE }),

		INVALIDATION("Invalidation", new ProvType[] {
				ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE }),

		USAGE("Usage", new ProvType[] { ProvType.INSTANTANEOUSEVENT,
				ProvType.ENTITYINFLUENCE });

		private static final Map<String, ProvType> lookup = new HashMap<String, ProvType>();
		static
		{
			for (ProvType provStatement : EnumSet.allOf(ProvType.class))
				lookup.put(provStatement.getURI(), provStatement);
		}

		private String uri;
		private ProvType[] extendsTypes;

		private ProvType(String localName)
		{
			this.uri = PROV + localName;
			this.extendsTypes = new ProvType[] {};
		}

		private ProvType(String localName, ProvType extendsType)
		{
			this(localName);
			this.extendsTypes = new ProvType[] { extendsType };
		}

		private ProvType(String localName, ProvType[] extendsTypes)
		{
			this(localName);
			this.extendsTypes = extendsTypes;
		}

		public String getURI()
		{
			return uri;
		}

		public ProvType[] getExtends()
		{
			return this.extendsTypes;
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

	protected ProvFactory pFactory;
	protected HashMap<QName, HashMap<QName, List<Statement>>> collators;
	private Hashtable<QName, BundleHolder> bundles;
	private Document document;
	private Hashtable<String, String> revnss;

	public RdfCollector(ProvFactory pFactory)
	{
		this.pFactory = pFactory;
		this.collators = new HashMap<QName, HashMap<QName, List<Statement>>>();
		this.revnss = new Hashtable<String, String>();
		this.document = pFactory.newDocument();
		this.bundles = new Hashtable<QName, BundleHolder>();
		document.setNss(new Hashtable<String, String>());
	}

	private HashMap<QName, List<Statement>> getCollator(Resource context)
	{
		return this.collators.get(qNameFromResource(context));
	}

	@Override
	public void handleNamespace(String prefix, String namespace)
	{
		this.document.getNss().put(prefix, namespace);
		this.revnss.put(namespace, prefix);
	}

	protected QName qNameFromResource(Resource resource)
	{
		if (resource instanceof URI)
		{

			return new QName(((URI) resource).getNamespace(),
					((URI) (resource)).getLocalName());
		} else if (resource instanceof BNode)
		{
			return new QName(((BNode) (resource)).getID());
		} else
		{
			return null;
		}
	}

	private boolean isProvURI(URI uri)
	{
		if (!uri.getNamespace().equals(PROV))
		{
			return false;
		}
		return true;
	}

	private BundleHolder getBundleHolder(QName context)
	{
		if (context == null)
			context = new QName("");
		if (!bundles.containsKey(context))
		{
			bundles.put(context, new BundleHolder());
		}
		return bundles.get(context);
	}

	protected void store(QName context,
			org.openprovenance.prov.xml.Element element)
	{
		if (element instanceof org.openprovenance.prov.xml.Activity)
		{
			getBundleHolder(context).addActivity(
					(org.openprovenance.prov.xml.Activity) element);
		} else if (element instanceof org.openprovenance.prov.xml.Entity)
		{

			getBundleHolder(context).addEntity(
					(org.openprovenance.prov.xml.Entity) element);
		} else if (element instanceof org.openprovenance.prov.xml.Agent)
		{

			getBundleHolder(context).addAgent(
					(org.openprovenance.prov.xml.Agent) element);
		}
	}

	protected void store(QName context,
			org.openprovenance.prov.xml.Relation0 relation0)
	{
		getBundleHolder(context).addStatement(
				(org.openprovenance.prov.xml.Statement) relation0);
	}

	/* Utility functions */

	protected List<Statement> getStatementsForPredicate(QName context,
			QName qname, String uri)
	{
		ArrayList<Statement> statements = new ArrayList<Statement>();
		for (Statement statement : collators.get(context).get(qname))
		{
			if (statement.getPredicate().stringValue().equals(uri))
			{
				statements.add(statement);
			}
		}
		return statements;
	}

	protected Statement getSingleStatementForPredicate(QName context,
			QName qname, String uri)
	{
		List<Statement> statements = getStatementsForPredicate(context, qname,
				uri);
		assert (statements.size() <= 1);

		Statement statement = null;
		if (statements.size() == 1)
		{
			statement = statements.get(0);
		}
		return statement;
	}

	protected ProvType[] getResourceTypes(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<ProvType> options = new ArrayList<ProvType>();
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
				options.add(provType);
			}
		}
		if (options.size() > 1)
		{
			List<ProvType> cloned = new ArrayList<ProvType>(options);
			for (ProvType option : options)
			{
				for (ProvType extended : option.getExtends())
				{
					cloned.remove(extended);
				}
			}
			options = cloned;
		}

		return options.toArray(new ProvType[]{});
	}

	protected Object decodeLiteral(Literal literal)
	{
		String dataType = XMLS + "string";
		if (literal.getDatatype() != null)
		{
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

	/* Prov-specific functions */

	private List<Statement> handleBaseStatements(Element element,
			QName context, QName qname)
	{

		List<Statement> statements = collators.get(context).get(qname);
		List<Statement> removedStatements = new ArrayList<Statement>();
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

			if (element instanceof HasLocation)
			{
				if (predS.equals(PROV + "atLocation"))
				{
					QName anyQ = qNameFromResource((Resource) (statement
							.getObject()));
					String role = statement.getObject().stringValue();
					((HasLocation) element).getLocation().add(
							pFactory.newAnyRef(anyQ));
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

			if (predS.equals(PROV + "wasInfluencedBy"))
			{
				QName anyQ = qNameFromResource((Resource) (statement
						.getObject()));
				WasInfluencedBy wib = pFactory.newWasInfluencedBy((QName) null,
						pFactory.newAnyRef(qname), pFactory.newAnyRef(anyQ));

				store(qNameFromResource(statement.getContext()), wib);
				removedStatements.add(statement);
			}

			if (predS.equals(PROV + "influenced"))
			{
				QName anyQ = qNameFromResource((Resource) (statement
						.getObject()));

				WasInfluencedBy wib = pFactory.newWasInfluencedBy((QName) null,
						pFactory.newAnyRef(anyQ), pFactory.newAnyRef(qname));

				store(qNameFromResource(statement.getContext()), wib);
				removedStatements.add(statement);
			}
		}

		statements.removeAll(removedStatements);

		return statements;
	}

	protected void buildGraph()
	{
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
					case ACTIVITY:
						createActivity(contextQ, qname);
						break;
					case AGENT:
						createAgent(contextQ, qname);
						break;
					case ENTITY:
						createEntity(contextQ, qname);
						break;
					default:
					}
				}
			}
		}
	}

	protected void buildBundles()
	{
		// Add 'default' bundle
		if (bundles.containsKey(new QName("")))
		{
			BundleHolder defaultBundle = bundles.get(new QName(""));
			for (org.openprovenance.prov.xml.Activity activity : defaultBundle
					.getActivities())
			{
				document.getEntityOrActivityOrWasGeneratedBy().add(activity);
			}

			for (org.openprovenance.prov.xml.Agent agent : defaultBundle
					.getAgents())
			{
				document.getEntityOrActivityOrWasGeneratedBy().add(agent);
			}

			for (org.openprovenance.prov.xml.Entity entity : defaultBundle
					.getEntities())
			{
				document.getEntityOrActivityOrWasGeneratedBy().add(entity);
			}

			for (org.openprovenance.prov.xml.Statement statement : defaultBundle
					.getStatements())
			{
				document.getEntityOrActivityOrWasGeneratedBy().add(statement);
			}
		}

		for (QName key : bundles.keySet())
		{
			if (key.getLocalPart().equals(""))
			{
				continue;
			}
			BundleHolder bundleHolder = bundles.get(key);
			NamedBundle bundle = pFactory.newNamedBundle(key,
					bundleHolder.getActivities(), bundleHolder.getEntities(),
					bundleHolder.getAgents(), bundleHolder.getStatements());
			bundle.setId(key);
			document.getEntityOrActivityOrWasGeneratedBy().add(bundle);
		}

	}
	
	protected void dumpUnhandled() {

		for (QName contextQ : collators.keySet())
		{
			HashMap<QName, List<Statement>> collator = collators.get(contextQ);
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
	}

	@Override
	public void endRDF()
	{
		buildGraph();
		buildBundles();
	}

	private void createEntity(QName context, QName qname)
	{
		org.openprovenance.prov.xml.Entity entity = pFactory.newEntity(qname);

		List<Statement> statements = collators.get(context).get(qname);
		statements = handleBaseStatements(entity, context, qname);

		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				QName valueQ = qNameFromResource((Resource) value);
				if (predS.equals(PROV + "wasDerivedFrom"))
				{
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					store(context, wdf);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "wasGeneratedBy"))
				{
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(
							(QName) null, pFactory.newEntityRef(qname), null,
							pFactory.newActivityRef(valueQ));

					store(context, wgb);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "alternateOf"))
				{
					AlternateOf ao = pFactory.newAlternateOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					store(context, ao);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "specializationOf"))
				{
					SpecializationOf so = pFactory.newSpecializationOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					store(context, so);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "wasInvalidatedBy"))
				{
					WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newActivityRef(valueQ));

					store(context, wib);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "wasAttributedTo"))
				{
					WasAttributedTo wit = pFactory.newWasAttributedTo(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newAgentRef(valueQ));

					store(context, wit);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "mentionOf"))
				{
					Statement asInBundleStatement = getSingleStatementForPredicate(
							context, qname, PROV + "asInBundle");
					removedStatements.add(asInBundleStatement);

					QName bundleQ = new QName(asInBundleStatement.getObject()
							.stringValue());

					QName entityQ = new QName(value.stringValue());
					MentionOf nmo = pFactory.newMentionOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ),
							pFactory.newEntityRef(bundleQ));

					store(context, nmo);
					removedStatements.add(statement);
				}
			} else if (value instanceof Literal)
			{
				if (predS.equals(PROV + "generatedAtTime"))
				{
					// TODO: Unable to implement this.
					Object literal = decodeLiteral((Literal) value);
					removedStatements.add(statement);
				}
				if (predS.equals(PROV + "invalidatedAtTime"))
				{
					// TODO: Unable to implement this.
					Object literal = decodeLiteral((Literal) value);
					removedStatements.add(statement);
				} else if (predS.equals(PROV + "value"))
				{
					Object literal = decodeLiteral((Literal) value);
					entity.setValue(literal);
					removedStatements.add(statement);
				}
			}
		}
		store(context, entity);
		statements.removeAll(removedStatements);

		// decodeAttributes(entity, statements)
	}

	private void createAgent(QName context, QName qname)
	{
		org.openprovenance.prov.xml.Agent agent = pFactory.newAgent(qname);

		List<Statement> statements = collators.get(context).get(qname);
		statements = handleBaseStatements(agent, context, qname);

		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				if (predS.equals(PROV + "actedOnBehalfOf"))
				{
					QName agentQ = qNameFromResource((Resource) value);
					ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(
							(QName) null, pFactory.newAgentRef(qname),
							pFactory.newAgentRef(agentQ), null);

					store(context, aobo);
					removedStatements.add(statement);
				}
			}
		}
		store(context, agent);
		statements.removeAll(removedStatements);
	}

	private void createActivity(QName context, QName qname)
	{
		org.openprovenance.prov.xml.Activity activity = pFactory
				.newActivity(qname);
		List<Statement> statements = collators.get(context).get(qname);

		statements = handleBaseStatements(activity, context, qname);

		List<Statement> removedStatements = new ArrayList<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				QName valueQ = qNameFromResource((Resource) value);
				if (predS.equals(PROV + "wasAssociatedWith"))
				{
					WasAssociatedWith waw = pFactory.newWasAssociatedWith(
							(QName) null, pFactory.newActivityRef(qname),
							pFactory.newAgentRef(valueQ));

					store(context, waw);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "used"))
				{
					Used used = pFactory.newUsed((QName) null,
							pFactory.newActivityRef(qname), null,
							pFactory.newEntityRef(valueQ));
					store(context, used);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "wasStartedBy"))
				{
					WasStartedBy wsb = pFactory.newWasStartedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(valueQ));
					store(context, wsb);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "generated"))
				{
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(
							(QName) null, pFactory.newEntityRef(valueQ), null,
							pFactory.newActivityRef(qname));
					store(context, wgb);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "wasEndedBy"))
				{
					WasEndedBy web = pFactory.newWasEndedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(valueQ));
					store(context, web);
					removedStatements.add(statement);

				} else if (predS.equals(PROV + "wasInformedBy"))
				{
					WasInformedBy wib = pFactory.newWasInformedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newActivityRef(valueQ));
					store(context, wib);
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
		store(context, activity);

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

		QName contextQ = qNameFromResource(statement.getContext());

		if (!collators.containsKey(contextQ))
		{
			collators.put(contextQ, new HashMap<QName, List<Statement>>());
		}

		HashMap<QName, List<Statement>> currcollator = getCollator(statement
				.getContext());

		if (!currcollator.containsKey(subjectQ))
		{
			currcollator.put(subjectQ, new ArrayList<Statement>());
		}

		currcollator.get(subjectQ).add(statement);
	}

	public Document getDocument()
	{
		return document;
	}
}