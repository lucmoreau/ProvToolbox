package org.openprovenance.prov.rdf;

import java.net.URISyntaxException;
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
import org.openprovenance.prov.xml.Attribute;
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
import org.openprovenance.prov.xml.URIWrapper;
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
	protected Document document;
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
		pFactory.setNamespaces(this.document.getNss());
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

		return options.toArray(new ProvType[] {});
	}

	protected Object decodeLiteral(Literal literal)
	{
		String dataType = XMLS + "string";
		if (literal.getLanguage() != null)
		{
			return pFactory.newInternationalizedString(literal.stringValue(), literal.getLanguage());
		}

		if (literal.getDatatype() != null)
		{
			dataType = literal.getDatatype().stringValue();
		}

		if (dataType.equals(XMLS + "QName"))
		{
			return pFactory.newQName(literal.stringValue());
		} else if (dataType.equals(XMLS + "string"))
		{
			return literal.stringValue();
		} else if (dataType.equals(XMLS + "dateTime"))
		{
			return literal.calendarValue();
		} else if (dataType.equals(XMLS + "int"))
		{
			return literal.intValue();
		} else if (dataType.equals(XMLS + "integer"))
		{
			return literal.integerValue();
		} else if (dataType.equals(XMLS + "boolean"))
		{
			return literal.booleanValue();
		} else if (dataType.equals(XMLS + "double"))
		{
			return literal.doubleValue();
		} else if (dataType.equals(XMLS + "anyURI"))
		{
			URIWrapper uw = new URIWrapper();
			uw.setValue(java.net.URI.create(literal.stringValue()));
			return uw;
		} else
		{
			return null;
		}
	}

	/* Prov-specific functions */

	protected List<Statement> handleBaseStatements(org.openprovenance.prov.xml.Statement element,
			QName context, QName qname, ProvType type)
	{

		List<Statement> statements = collators.get(context).get(qname);
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
						pFactory.addType((HasType) element, literalValue);
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
				}
			}

			if (element instanceof HasLocation)
			{
				if (predS.equals(PROV + "atLocation"))
				{
					QName anyQ = qNameFromResource((Resource) (statement
							.getObject()));
					((HasLocation) element).getLocation().add(
							pFactory.newAnyRef(anyQ));
				}				
				
				if (predS.equals(PROV + "location"))
				{
					((HasLocation)element).getLocation().add(decodeLiteral((Literal)statement.getObject()));
				}
			}

			if (element instanceof HasLabel)
			{
				if (predS.equals(PROV + "label"))
				{
					Literal lit = (Literal) (statement.getObject());
					if (lit.getLanguage() != null)
					{
						pFactory.addLabel((HasLabel) element,
								lit.stringValue(), lit.getLanguage()
										.toUpperCase());
					} else
					{
						pFactory.addLabel((HasLabel) element, lit.stringValue());
					}
				}
			}

			if (element instanceof HasExtensibility)
			{
				URI uri = (URI) statement.getPredicate();
				Value val = statement.getObject();
				if (!isProvURI(uri))
				{
					if (uri.equals(RDF.TYPE))
					{
						// Add prov:type
						if (element instanceof HasType)
						{
							if (val instanceof URI)
							{
								if (!val.toString().equals(type.getURI()))
								{
									try
									{
										java.net.URI jURI = new java.net.URI(
												val.stringValue());

										pFactory.addType((HasType) element,
												jURI);
									} catch (URISyntaxException use)
									{
										System.err.println("Invalid URI");
									}
								}
							}
							else if(val instanceof Literal) {
								pFactory.addType((HasType)element, decodeLiteral((Literal)val));
							}
						}

					} else
					{
						QName predQ = pFactory.stringToQName(uri.stringValue());
						Attribute attr = null;
						if (val instanceof Literal)
						{
							Literal lit = (Literal) val;
							String xsdType = "http://www.w3.org/2001/XMLSchema#string";
							if (lit.getDatatype() != null)
							{
								xsdType = lit.getDatatype().stringValue();
							}
							attr = new Attribute(predQ, lit.stringValue(),
									xsdType);
						} else if (val instanceof Resource)
						{
							attr = new Attribute(predQ, val.stringValue(),
									"http://www.w3.org/2001/XMLSchema#anyURI");
						} else
						{
							System.err.println("Invalid value");
						}

						if (attr != null)
						{
							pFactory.addAttribute((HasExtensibility)element, attr);
						}

					}
				}
			}

			

			if (predS.equals(PROV + "wasInfluencedBy"))
			{
				QName anyQ = qNameFromResource((Resource) (statement
						.getObject()));
				WasInfluencedBy wib = pFactory.newWasInfluencedBy((QName) null,
						pFactory.newAnyRef(qname), pFactory.newAnyRef(anyQ));

				store(qNameFromResource(statement.getContext()), wib);
			}

			if (predS.equals(PROV + "influenced"))
			{
				QName anyQ = qNameFromResource((Resource) (statement
						.getObject()));

				WasInfluencedBy wib = pFactory.newWasInfluencedBy((QName) null,
						pFactory.newAnyRef(anyQ), pFactory.newAnyRef(qname));

				store(qNameFromResource(statement.getContext()), wib);
			}
		}

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
					case PERSON:
					case ORGANIZATION:
					case SOFTWAREAGENT:
						createAgent(contextQ, qname);
						break;
					case ENTITY:
					case PLAN:
					case BUNDLE:
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

	protected void dumpUnhandled()
	{

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
		statements = handleBaseStatements(entity, context, qname,
				ProvType.ENTITY);

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
				} else if (predS.equals(PROV + "wasGeneratedBy"))
				{
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(
							(QName) null, pFactory.newEntityRef(qname), null,
							pFactory.newActivityRef(valueQ));

					store(context, wgb);
				} else if (predS.equals(PROV + "alternateOf"))
				{
					AlternateOf ao = pFactory.newAlternateOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					store(context, ao);
				} else if (predS.equals(PROV + "specializationOf"))
				{
					SpecializationOf so = pFactory.newSpecializationOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					store(context, so);
				} else if (predS.equals(PROV + "wasInvalidatedBy"))
				{
					WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newActivityRef(valueQ));

					store(context, wib);
				} else if (predS.equals(PROV + "wasAttributedTo"))
				{
					WasAttributedTo wit = pFactory.newWasAttributedTo(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newAgentRef(valueQ));

					store(context, wit);
				} else if (predS.equals(PROV + "mentionOf"))
				{
					Statement asInBundleStatement = getSingleStatementForPredicate(
							context, qname, PROV + "asInBundle");

					QName bundleQ = new QName(asInBundleStatement.getObject()
							.stringValue());

					QName entityQ = new QName(value.stringValue());
					MentionOf nmo = pFactory.newMentionOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(entityQ),
							pFactory.newEntityRef(bundleQ));

					store(context, nmo);
				}
			} else if (value instanceof Literal)
			{
				if (predS.equals(PROV + "generatedAtTime"))
				{
					// TODO: Unable to implement this.
					Object literal = decodeLiteral((Literal) value);
				}
				if (predS.equals(PROV + "invalidatedAtTime"))
				{
					// TODO: Unable to implement this.
					Object literal = decodeLiteral((Literal) value);
				} else if (predS.equals(PROV + "value"))
				{
					Object literal = decodeLiteral((Literal) value);
					entity.setValue(literal);
				}
			}
		}
		store(context, entity);
	}

	private void createAgent(QName context, QName qname)
	{
		org.openprovenance.prov.xml.Agent agent = pFactory.newAgent(qname);

		List<Statement> statements = collators.get(context).get(qname);
		statements = handleBaseStatements(agent, context, qname, ProvType.AGENT);

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
				}
			}
		}
		store(context, agent);
	}

	private void createActivity(QName context, QName qname)
	{
		org.openprovenance.prov.xml.Activity activity = pFactory
				.newActivity(qname);
		List<Statement> statements = collators.get(context).get(qname);

		statements = handleBaseStatements(activity, context, qname,
				ProvType.ACTIVITY);

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

				} else if (predS.equals(PROV + "used"))
				{
					Used used = pFactory.newUsed((QName) null,
							pFactory.newActivityRef(qname), null,
							pFactory.newEntityRef(valueQ));
					store(context, used);

				} else if (predS.equals(PROV + "wasStartedBy"))
				{
					WasStartedBy wsb = pFactory.newWasStartedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(valueQ));
					store(context, wsb);

				} else if (predS.equals(PROV + "generated"))
				{
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(
							(QName) null, pFactory.newEntityRef(valueQ), null,
							pFactory.newActivityRef(qname));
					store(context, wgb);

				} else if (predS.equals(PROV + "wasEndedBy"))
				{
					WasEndedBy web = pFactory.newWasEndedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(valueQ));
					store(context, web);

				} else if (predS.equals(PROV + "wasInformedBy"))
				{
					WasInformedBy wib = pFactory.newWasInformedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newActivityRef(valueQ));
					store(context, wib);
				}
			} else if (value instanceof Literal)
			{
				if (predS.equals(PROV + "startedAtTime"))
				{
					Object literal = decodeLiteral((Literal) value);
					activity.setStartTime((XMLGregorianCalendar) literal);
				} else if (predS.equals(PROV + "endedAtTime"))
				{
					Object literal = decodeLiteral((Literal) value);
					activity.setEndTime((XMLGregorianCalendar) literal);
				}
			}
		}
		store(context, activity);
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