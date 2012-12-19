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
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.HasLabel;
import org.openprovenance.prov.xml.HasLocation;
import org.openprovenance.prov.xml.HasRole;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
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
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class RdfCollector extends RDFHandlerBase {


	enum ProvType
	{

		ENTITY(Ontology.QNAME_PROVO_Entity), AGENT(Ontology.QNAME_PROVO_Agent), ACTIVITY(Ontology.QNAME_PROVO_Activity), INFLUENCE(
				Ontology.QNAME_PROVO_Influence),

		BUNDLE(Ontology.QNAME_PROVO_Bundle, ProvType.ENTITY),

		ORGANIZATION(Ontology.QNAME_PROVO_Organization, ProvType.AGENT),

		PERSON(Ontology.QNAME_PROVO_Person, ProvType.AGENT),

		SOFTWAREAGENT(Ontology.QNAME_PROVO_SoftwareAgent, ProvType.AGENT),

		LOCATION(Ontology.QNAME_PROVO_Location),

		ROLE(Ontology.QNAME_PROVO_Role),

		PLAN(Ontology.QNAME_PROVO_Plan, ProvType.ENTITY),

		COLLECTION(Ontology.QNAME_PROVO_Collection, ProvType.ENTITY),

		EMPTYCOLLECTION(Ontology.QNAME_PROVO_EmptyCollection, ProvType.COLLECTION),

		INSTANTANEOUSEVENT(Ontology.QNAME_PROVO_InstantaneousEvent),

		ENTITYINFLUENCE(Ontology.QNAME_PROVO_EntityInfluence, ProvType.INFLUENCE),

		ACTIVITYINFLUENCE(Ontology.QNAME_PROVO_ActivityInfluence, ProvType.INFLUENCE),

		AGENTINFLUENCE(Ontology.QNAME_PROVO_AgentInfluence, ProvType.INFLUENCE),

		ASSOCIATION(Ontology.QNAME_PROVO_Association, ProvType.AGENTINFLUENCE),

		ATTRIBUTION(Ontology.QNAME_PROVO_Attribution, ProvType.AGENTINFLUENCE),

		COMMUNICATION(Ontology.QNAME_PROVO_Communication, ProvType.AGENTINFLUENCE),

		DELEGATION(Ontology.QNAME_PROVO_Delegation, ProvType.AGENTINFLUENCE),

		DERIVATION(Ontology.QNAME_PROVO_Derivation, ProvType.ENTITYINFLUENCE),

		QUOTATION(Ontology.QNAME_PROVO_Quotation, ProvType.ENTITYINFLUENCE), REVISION(Ontology.QNAME_PROVO_Revision,
				ProvType.ENTITYINFLUENCE), PRIMARYSOURCE(Ontology.QNAME_PROVO_PrimarySource,
				ProvType.ENTITYINFLUENCE),

		END(Ontology.QNAME_PROVO_End, new ProvType[] { ProvType.INSTANTANEOUSEVENT,
				ProvType.ENTITYINFLUENCE }),

		START(Ontology.QNAME_PROVO_Start, new ProvType[] { ProvType.INSTANTANEOUSEVENT,
				ProvType.ENTITYINFLUENCE }),

		GENERATION(Ontology.QNAME_PROVO_Generation, new ProvType[] { ProvType.INSTANTANEOUSEVENT,
				ProvType.ACTIVITYINFLUENCE }),

		INVALIDATION(Ontology.QNAME_PROVO_Invalidation, new ProvType[] {
				ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE }),

		USAGE(Ontology.QNAME_PROVO_Usage, new ProvType[] { ProvType.INSTANTANEOUSEVENT,
				ProvType.ENTITYINFLUENCE });

		private static final Map<QName, ProvType> lookup = new HashMap<QName, ProvType>();
		static
		{
			for (ProvType provStatement : EnumSet.allOf(ProvType.class))
				lookup.put(provStatement.getQName(), provStatement);
		}

		private ProvType[] extendsTypes;
		private QName qname;

		private ProvType(QName qname)
		{
			this.qname = qname;
			this.extendsTypes = new ProvType[] {};
		}

		private ProvType(QName qname, ProvType extendsType)
		{
			this(qname);
			this.extendsTypes = new ProvType[] { extendsType };
		}

		private ProvType(QName qname, ProvType[] extendsTypes)
		{
			this(qname);
			this.extendsTypes = extendsTypes;
		}

		public QName getQName()
		{
			return qname;
		}
		
		public String getURIString() {
			return qname.getNamespaceURI()+qname.getLocalPart();
		}

		public ProvType[] getExtends()
		{
			return this.extendsTypes;
		}

		public static ProvType lookup(QName qname)
		{
			return lookup.get(qname);
		}

		public String toString()
		{
			return qname.toString();
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
		handleNamespace(NamespacePrefixMapper.XSD_PREFIX, NamespacePrefixMapper.XSD_HASH_NS);
	}

	private HashMap<QName, List<Statement>> getCollator(Resource context)
	{
		return this.collators.get(convertResourceToQName(context));
	}

	@Override
	public void handleNamespace(String prefix, String namespace)
	{
		if (prefix.equals(""))
		{
			prefix = "def";
		}
		this.document.getNss().put(prefix, namespace);
		pFactory.setNamespaces(this.document.getNss());
		this.revnss.put(namespace, prefix);
	}

	private boolean isProvURI(QName qname)
	{
		if (!qname.getNamespaceURI().equals(NamespacePrefixMapper.PROV_NS))
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
			QName qname, QName uri)
	{
		ArrayList<Statement> statements = new ArrayList<Statement>();
		for (Statement statement : collators.get(context).get(qname))
		{
			if (convertURIToQName(statement.getPredicate()).equals(uri))
			{
				statements.add(statement);
			}
		}
		return statements;
	}

	protected Statement getSingleStatementForPredicate(QName context,
			QName qname, QName uri)
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
			QName predQ = convertURIToQName(statement.getPredicate());
			if (predQ.equals(Ontology.QNAME_RDF_TYPE))
			{
				Value value = statement.getObject();
				if (!(value instanceof URI))
				{
					continue;
				}
				if (!isProvURI(convertURIToQName((URI)value)))
				{
					continue;
				}
				ProvType provType = ProvType.lookup(convertURIToQName((URI)value));
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

	protected QName convertResourceToQName(Resource resource)
	{
		if (resource instanceof URI)
		{
			return convertURIToQName((URI) resource);
		} else if (resource instanceof BNode)
		{
			return new QName(((BNode) (resource)).getID());
		} else
		{
			return null;
		}
	}

	protected Object valueToObject(Value value)
	{
		if (value instanceof Resource)
		{
			return convertResourceToQName((Resource) value);
		} else if (value instanceof Literal)
		{
			return decodeLiteral((Literal) value);
		} else if (value instanceof URI)
		{
			URI uri = (URI) (value);
			URIWrapper uw = new URIWrapper();
			uw.setValue(java.net.URI.create(uri.toString()));
			return uw;
		} else if (value instanceof BNode)
		{
			return new QName(((BNode) (value)).getID());
		} else
		{
			return null;
		}
	}

	protected Object decodeLiteral(Literal literal)
	{
		String dataType = NamespacePrefixMapper.XSD_HASH_NS + "string";
		if (literal.getLanguage() != null)
		{
			return pFactory.newInternationalizedString(literal.stringValue(),
					literal.getLanguage());
		}

		if (literal.getDatatype() != null)
		{
			if (literal instanceof URI)
			{
				QName qname = (QName) pFactory.newQName(literal.getDatatype()
						.stringValue());
				dataType = qname.getNamespaceURI() + qname.getLocalPart();
			} else
			{
				dataType = literal.getDatatype().stringValue();
			}
		}

		if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "QName"))
		{
			return pFactory.newQName(literal.stringValue());
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "string"))
		{
			return literal.stringValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "dateTime"))
		{
			return literal.calendarValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "int"))
		{
			return literal.intValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "integer"))
		{
			return literal.integerValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "boolean"))
		{
			return literal.booleanValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "double"))
		{
			return literal.doubleValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "float"))
		{
			return literal.floatValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "long"))
		{
			return literal.longValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "short"))
		{
			return literal.shortValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "byte"))
		{
			return literal.byteValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "decimal"))
		{
			return literal.decimalValue();
		} else if (dataType.equals( NamespacePrefixMapper.XSD_HASH_NS + "anyURI"))
		{
			URIWrapper uw = new URIWrapper();
			uw.setValue(java.net.URI.create(literal.stringValue()));
			return uw;
		} else
		{
			// System.out.println("Unhandled::: "+literal.getDatatype());
			return literal.stringValue();
		}
	}

	private String getXsdType(String shorttype)
	{
		String xsdType = "";
		if (revnss.containsKey( NamespacePrefixMapper.XSD_HASH_NS))
		{
			xsdType = revnss.get( NamespacePrefixMapper.XSD_HASH_NS) + ":" + shorttype;
		} else
		{
			xsdType =  NamespacePrefixMapper.XSD_HASH_NS + shorttype;
		}
		return xsdType;
	}

	/* Prov-specific functions */

	protected List<Statement> handleBaseStatements(
			org.openprovenance.prov.xml.Statement element, QName context,
			QName qname, ProvType type)
	{
		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());

			if (element instanceof HasType)
			{
				if (statement.getPredicate().equals(RDF.TYPE))
				{
					Value value = statement.getObject();
					Object obj = valueToObject(statement.getObject());
					if (obj != null)
					{

						Value vobj = statement.getObject();
						Boolean sameAsType = false;
						if (vobj instanceof URI)
						{
							// TODO: Nasty.
							URI uri = (URI) (vobj);

							String uriVal = uri.getNamespace()
									+ uri.getLocalName();
							sameAsType = uriVal.equals(type.getURIString());
						}

						if (!sameAsType)
						{
							if (statement.getObject() instanceof Resource)
							{
								pFactory.addType(
										(HasType) element,
										convertResourceToQName((Resource) statement
												.getObject()));
							} else if (statement.getObject() instanceof Literal)
							{

								pFactory.addType((HasType) element,
										decodeLiteral((Literal) statement
												.getObject()));
							}
						}
					} else
					{
						System.out.println(value);
						System.out.println("Value wasn't a suitable type");
					}
				}
			}

			if (element instanceof HasRole)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_hadRole))
				{
					String role = statement.getObject().stringValue();
					pFactory.addRole((HasRole) element, role);
				}
			}

			if (element instanceof HasLocation)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_atLocation))
				{
					Object obj = valueToObject(statement.getObject());
					if (obj != null)
					{
						((HasLocation) element).getLocation().add(obj);
					}
				}
			}

			if (element instanceof HasLabel)
			{
				if (predQ.equals(Ontology.QNAME_RDFS_LABEL))
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
				Value val = statement.getObject();
				if (!isProvURI(predQ))
				{
					if (!predQ.equals(Ontology.QNAME_RDF_TYPE) && !predQ.equals(Ontology.QNAME_RDFS_LABEL))
					{
						// Retrieve the prefix
						String prefix = this.revnss.get(predQ.getNamespaceURI());
						Attribute attr = null;
						if (val instanceof Literal)
						{
							Literal lit = (Literal) val;

							String shortType = "string";
							if (lit.getDatatype() != null)
							{
								shortType = lit.getDatatype().getLocalName();
							}

							// FIXME: Bug 3 occurs here.
							QName xsdType = pFactory
									.stringToQName(getXsdType(shortType));// Is
																			// it
																			// right?
							attr = pFactory.newAttribute(predQ.getNamespaceURI(),
									predQ.getLocalPart(), prefix,
									decodeLiteral(lit), xsdType);

						} else if (val instanceof Resource)
						{
							URIWrapper uw = new URIWrapper();
							java.net.URI jURI = java.net.URI.create(val
									.stringValue());
							uw.setValue(jURI);

							attr = pFactory
									.newAttribute(
											predQ.getNamespaceURI(),
											predQ.getLocalPart(),
											prefix,
											uw,
											pFactory.stringToQName(getXsdType("anyURI")));
						} else
						{
							System.err.println("Invalid value");
						}

						if (attr != null)
						{
							pFactory.addAttribute((HasExtensibility) element,
									attr);
						}

					}
				}
			}

			if (predQ.equals(Ontology.QNAME_PROVO_wasInfluencedBy))
			{
				QName anyQ = convertResourceToQName((Resource) (statement
						.getObject()));
				WasInfluencedBy wib = pFactory.newWasInfluencedBy((QName) null,
						pFactory.newAnyRef(qname), pFactory.newAnyRef(anyQ));

				store(convertResourceToQName(statement.getContext()), wib);
			}

			if (predQ.equals(Ontology.QNAME_PROVO_influenced))
			{
				QName anyQ = convertResourceToQName((Resource) (statement
						.getObject()));

				WasInfluencedBy wib = pFactory.newWasInfluencedBy((QName) null,
						pFactory.newAnyRef(anyQ), pFactory.newAnyRef(qname));

				store(convertResourceToQName(statement.getContext()), wib);
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
						if (isProvURI(convertURIToQName(statement.getPredicate())))
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

	protected QName convertURIToQName(URI uri)
	{
		// It is not necessary to specify a prefix for a QName. This code was
		// breaking on the jpl trace.
		QName qname;
		if (revnss.containsKey(uri.getNamespace()))
		{
			String prefix = revnss.get(uri.getNamespace());
			qname = new QName(uri.getNamespace(), uri.getLocalName(), prefix);
		} else
		{
			// Ugly!
			String prefix = "ns" + uri.getNamespace().hashCode() + "";//
			handleNamespace(prefix, uri.getNamespace());
			qname = new QName(uri.getNamespace(), uri.getLocalName(), prefix);
		}
		return qname;
	}

	private void createEntity(QName context, QName qname)
	{
		org.openprovenance.prov.xml.Entity entity = pFactory.newEntity(qname);

		List<Statement> statements = collators.get(context).get(qname);
		statements = handleBaseStatements(entity, context, qname,
				ProvType.ENTITY);
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				QName valueQ = convertResourceToQName((Resource) value);
				if (predQ.equals(Ontology.QNAME_PROVO_wasDerivedFrom))
				{
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					store(context, wdf);
				} else if (predQ.equals(Ontology.QNAME_PROVO_hadPrimarySource))
				{
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					pFactory.addPrimarySourceType(wdf);
					store(context, wdf);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasQuotedFrom))
				{
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));
					pFactory.addQuotationType(wdf);
					store(context, wdf);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasRevisionOf))
				{
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));
					pFactory.addRevisionType(wdf);
					store(context, wdf);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasGeneratedBy))
				{
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(
							(QName) null, pFactory.newEntityRef(qname), null,
							pFactory.newActivityRef(valueQ));

					store(context, wgb);
				} else if (predQ.equals(Ontology.QNAME_PROVO_alternateOf))
				{
					AlternateOf ao = pFactory.newAlternateOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					store(context, ao);
				} else if (predQ.equals(Ontology.QNAME_PROVO_specializationOf))
				{
					SpecializationOf so = pFactory.newSpecializationOf(
							pFactory.newEntityRef(qname),
							pFactory.newEntityRef(valueQ));

					store(context, so);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasInvalidatedBy))
				{
					WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newActivityRef(valueQ));

					store(context, wib);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasAttributedTo))
				{
					WasAttributedTo wit = pFactory.newWasAttributedTo(
							(QName) null, pFactory.newEntityRef(qname),
							pFactory.newAgentRef(valueQ));

					store(context, wit);
				} else if (predQ.equals(Ontology.QNAME_PROVO_mentionOf))
				{
					Statement asInBundleStatement = getSingleStatementForPredicate(
							context, qname, Ontology.QNAME_PROVO_asInBundle);
					Object o = (asInBundleStatement == null) ? null
							: asInBundleStatement.getObject();
					QName bundleQ = (o == null) ? null
							: convertURIToQName((URI) o);
					QName entityQ = (value == null) ? null
							: convertURIToQName((URI) value);
					MentionOf nmo = pFactory.newMentionOf(
							(qname == null) ? null : pFactory
									.newEntityRef(qname),
							(entityQ == null) ? null : pFactory
									.newEntityRef(entityQ),
							(bundleQ == null) ? null : pFactory
									.newEntityRef(bundleQ));

					store(context, nmo);
				} else if (predQ.equals(Ontology.QNAME_PROVO_value))
				{
					Object resourceVal = convertResourceToQName((Resource) value);
					entity.setValue(resourceVal);
				}
			} else if (value instanceof Literal)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_value))
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
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_actedOnBehalfOf))
				{
					QName agentQ = convertResourceToQName((Resource) value);
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
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				QName valueQ = convertResourceToQName((Resource) value);
				if (predQ.equals(Ontology.QNAME_PROVO_wasAssociatedWith))
				{
					WasAssociatedWith waw = pFactory.newWasAssociatedWith(
							(QName) null, pFactory.newActivityRef(qname),
							pFactory.newAgentRef(valueQ));

					store(context, waw);

				} else if (predQ.equals(Ontology.QNAME_PROVO_used))
				{
					Used used = pFactory.newUsed((QName) null,
							pFactory.newActivityRef(qname), null,
							pFactory.newEntityRef(valueQ));
					store(context, used);

				} else if (predQ.equals(Ontology.QNAME_PROVO_wasStartedBy))
				{
					WasStartedBy wsb = pFactory.newWasStartedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(valueQ));
					store(context, wsb);

				} else if (predQ.equals(Ontology.QNAME_PROVO_generated))
				{
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(
							(QName) null, pFactory.newEntityRef(valueQ), null,
							pFactory.newActivityRef(qname));
					store(context, wgb);

				} else if (predQ.equals(Ontology.QNAME_PROVO_wasEndedBy))
				{
					WasEndedBy web = pFactory.newWasEndedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(valueQ));
					store(context, web);

				} else if (predQ.equals(Ontology.QNAME_PROVO_wasInformedBy))
				{
					WasInformedBy wib = pFactory.newWasInformedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newActivityRef(valueQ));
					store(context, wib);
				}
			} else if (value instanceof Literal)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_startedAtTime))
				{
					Object literal = decodeLiteral((Literal) value);
					activity.setStartTime((XMLGregorianCalendar) literal);
				} else if (predQ.equals(Ontology.QNAME_PROVO_endedAtTime))
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
			subjectQ = convertURIToQName(uri);
		} else
		{
			System.err.println("Invalid subject resource");
		}

		QName contextQ = convertResourceToQName(statement.getContext());

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
