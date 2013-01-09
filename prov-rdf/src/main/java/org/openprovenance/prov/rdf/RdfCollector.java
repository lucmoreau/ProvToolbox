package org.openprovenance.prov.rdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.URIWrapper;
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

	enum ProvType
	{

		ENTITY(Ontology.QNAME_PROVO_Entity), AGENT(Ontology.QNAME_PROVO_Agent), ACTIVITY(
				Ontology.QNAME_PROVO_Activity), INFLUENCE(
				Ontology.QNAME_PROVO_Influence),

		BUNDLE(Ontology.QNAME_PROVO_Bundle, ProvType.ENTITY),

		ORGANIZATION(Ontology.QNAME_PROVO_Organization, ProvType.AGENT),

		PERSON(Ontology.QNAME_PROVO_Person, ProvType.AGENT),

		SOFTWAREAGENT(Ontology.QNAME_PROVO_SoftwareAgent, ProvType.AGENT),

		LOCATION(Ontology.QNAME_PROVO_Location),

		ROLE(Ontology.QNAME_PROVO_Role),

		PLAN(Ontology.QNAME_PROVO_Plan, ProvType.ENTITY),

		COLLECTION(Ontology.QNAME_PROVO_Collection, ProvType.ENTITY),

		EMPTYCOLLECTION(Ontology.QNAME_PROVO_EmptyCollection,
				ProvType.COLLECTION),

		INSTANTANEOUSEVENT(Ontology.QNAME_PROVO_InstantaneousEvent),

		ENTITYINFLUENCE(Ontology.QNAME_PROVO_EntityInfluence,
				ProvType.INFLUENCE),

		ACTIVITYINFLUENCE(Ontology.QNAME_PROVO_ActivityInfluence,
				ProvType.INFLUENCE),

		AGENTINFLUENCE(Ontology.QNAME_PROVO_AgentInfluence, ProvType.INFLUENCE),

		ASSOCIATION(Ontology.QNAME_PROVO_Association, ProvType.AGENTINFLUENCE),

		ATTRIBUTION(Ontology.QNAME_PROVO_Attribution, ProvType.AGENTINFLUENCE),

		COMMUNICATION(Ontology.QNAME_PROVO_Communication,
				ProvType.AGENTINFLUENCE),

		DELEGATION(Ontology.QNAME_PROVO_Delegation, ProvType.AGENTINFLUENCE),

		DERIVATION(Ontology.QNAME_PROVO_Derivation, ProvType.ENTITYINFLUENCE),

		QUOTATION(Ontology.QNAME_PROVO_Quotation, ProvType.ENTITYINFLUENCE), REVISION(
				Ontology.QNAME_PROVO_Revision, ProvType.ENTITYINFLUENCE), PRIMARYSOURCE(
				Ontology.QNAME_PROVO_PrimarySource, ProvType.ENTITYINFLUENCE),

		END(Ontology.QNAME_PROVO_End, new ProvType[] {
				ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE }),

		START(Ontology.QNAME_PROVO_Start, new ProvType[] {
				ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE }),

		GENERATION(Ontology.QNAME_PROVO_Generation, new ProvType[] {
				ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE }),

		INVALIDATION(Ontology.QNAME_PROVO_Invalidation, new ProvType[] {
				ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE }),

		USAGE(Ontology.QNAME_PROVO_Usage, new ProvType[] {
				ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });

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

		public String getURIString()
		{
			return qname.getNamespaceURI() + qname.getLocalPart();
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

		public boolean equals(ProvType b)
		{
			return b.getQName().equals(this.getQName());
		}

	}

	protected ProvFactory pFactory;
	protected HashMap<QName, HashMap<QName, List<Statement>>> collators;
	private Hashtable<QName, BundleHolder> bundles;
	protected Document document;
	private Hashtable<String, String> revnss;
	private ValueConverter valueConverter;
	private Ontology ontology;

	public RdfCollector(ProvFactory pFactory)
	{
		this.pFactory = pFactory;
		this.ontology = new Ontology();
		this.collators = new HashMap<QName, HashMap<QName, List<Statement>>>();
		this.revnss = new Hashtable<String, String>();
		this.document = pFactory.newDocument();
		this.valueConverter = new ValueConverter(pFactory);
		this.bundles = new Hashtable<QName, BundleHolder>();
		document.setNss(new Hashtable<String, String>());
		handleNamespace(NamespacePrefixMapper.XSD_PREFIX,
				NamespacePrefixMapper.XSD_HASH_NS);
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

	private ProvType[] filterImplicitTypes(ProvType[] implicitTypes,
			ProvType[] explicitTypes)
	{
		List<ProvType> explicitOptions = new ArrayList<ProvType>(
				Arrays.asList(explicitTypes));
		List<ProvType> implicitOptions = new ArrayList<ProvType>(
				Arrays.asList(implicitTypes));

		// Remove any that are already types that are already explicit
		implicitOptions.removeAll(explicitOptions);

		// Remove any that are extended by types in explicit
		List<ProvType> cloned = new ArrayList<ProvType>(implicitOptions);
		for (ProvType option : explicitOptions)
		{
			for (ProvType extended : option.getExtends())
			{
				cloned.remove(extended);
			}
		}
		implicitOptions = cloned;
		return implicitOptions.toArray(new ProvType[] {});
	}

	protected ProvType[] getImplicitTypes(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<ProvType> implicitOptions = new ArrayList<ProvType>();
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			// Check for a type that we can infer
			if (ontology.domains.containsKey(predQ))
			{
				ProvType provType = ProvType
						.lookup(ontology.domains.get(predQ));
				if (!implicitOptions.contains(provType))
				{
					implicitOptions.add(provType);
				}
			}
		}
		
		// TODO: Handle cases where qname is the object rather than the subject.
		// We can use the range mapping to deduce the type of the object in these
		// cases.

		// Prunes back to the 'top' class (e.g. if we have Person and Agent, we
		// only need Person)
		if (implicitOptions.size() > 1)
		{
			List<ProvType> cloned = new ArrayList<ProvType>(implicitOptions);
			for (ProvType option : implicitOptions)
			{
				for (ProvType extended : option.getExtends())
				{
					cloned.remove(extended);
				}
			}
			implicitOptions = cloned;
		}

		return implicitOptions.toArray(new ProvType[] {});

	}

	protected ProvType[] getExplicitTypes(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<ProvType> explicitOptions = new ArrayList<ProvType>();
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
				if (!isProvURI(convertURIToQName((URI) value)))
				{
					continue;
				}
				ProvType provType = ProvType
						.lookup(convertURIToQName((URI) value));
				explicitOptions.add(provType);
			}
		}

		// Prunes back to the 'top' class (e.g. if we have Person and Agent, we
		// only need Person)
		if (explicitOptions.size() > 1)
		{
			List<ProvType> cloned = new ArrayList<ProvType>(explicitOptions);
			for (ProvType option : explicitOptions)
			{
				for (ProvType extended : option.getExtends())
				{
					cloned.remove(extended);
				}
			}
			explicitOptions = cloned;
		}

		return explicitOptions.toArray(new ProvType[] {});
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

		if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "QName"))
		{
			return pFactory.newQName(literal.stringValue());
		} else if (dataType
				.equals(NamespacePrefixMapper.XSD_HASH_NS + "string"))
		{
			return literal.stringValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
				+ "dateTime"))
		{
			return literal.calendarValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "int"))
		{
			return literal.intValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
				+ "integer"))
		{
			return literal.integerValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
				+ "boolean"))
		{
			return literal.booleanValue();
		} else if (dataType
				.equals(NamespacePrefixMapper.XSD_HASH_NS + "double"))
		{
			return literal.doubleValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "float"))
		{
			return literal.floatValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "long"))
		{
			return literal.longValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "short"))
		{
			return literal.shortValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "byte"))
		{
			return literal.byteValue();
		} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
				+ "decimal"))
		{
			return literal.decimalValue();
		} else if (dataType
				.equals(NamespacePrefixMapper.XSD_HASH_NS + "anyURI"))
		{
			URIWrapper uw = new URIWrapper();
			uw.setValue(java.net.URI.create(literal.stringValue()));
			return uw;
		} else
		{
			return literal.stringValue();
		}
	}

	private String getXsdType(String shorttype)
	{
		String xsdType = "";
		if (revnss.containsKey(NamespacePrefixMapper.XSD_HASH_NS))
		{
			xsdType = revnss.get(NamespacePrefixMapper.XSD_HASH_NS) + ":"
					+ shorttype;
		} else
		{
			xsdType = NamespacePrefixMapper.XSD_HASH_NS + shorttype;
		}
		return xsdType;
	}

	public List<Attribute> collectAttributes(QName context, QName qname,
			ProvType type)
	{
		List<Attribute> attributes = new ArrayList<Attribute>();
		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());

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

						String uriVal = uri.getNamespace() + uri.getLocalName();
						sameAsType = uriVal.equals(type.getURIString());
					}

					if (!sameAsType)
					{

						if (statement.getObject() instanceof Resource)
						{
							attributes
									.add(pFactory
											.newAttribute(
													Attribute.PROV_TYPE_QNAME,
													convertResourceToQName((Resource) statement
															.getObject()),
													this.valueConverter));

						} else if (statement.getObject() instanceof Literal)
						{

							attributes
									.add(pFactory.newAttribute(
											Attribute.PROV_TYPE_QNAME,
											decodeLiteral((Literal) statement
													.getObject()),
											this.valueConverter));
						}
					}
				} else
				{
					System.out.println(value);
					System.out.println("Value wasn't a suitable type");
				}
			}

			if (predQ.equals(Ontology.QNAME_PROVO_hadRole))
			{
				String role = statement.getObject().stringValue();
				attributes.add(pFactory.newAttribute(Attribute.PROV_ROLE_QNAME,
						role, this.valueConverter));
			}

			if (predQ.equals(Ontology.QNAME_PROVO_atLocation))
			{
				Object obj = valueToObject(statement.getObject());
				if (obj != null)
				{
					attributes.add(pFactory.newAttribute(
							Attribute.PROV_LOCATION_QNAME, obj,
							this.valueConverter));
				}
			}
			
			if (predQ.equals(Ontology.QNAME_PROVO_wasInfluencedBy))
			{
				QName anyQ = convertResourceToQName((Resource) (statement
						.getObject()));
				WasInfluencedBy wib = pFactory.newWasInfluencedBy(null, qname,
						anyQ, null);

				store(convertResourceToQName(statement.getContext()), wib);
			}

			if (predQ.equals(Ontology.QNAME_PROVO_influenced))
			{
				QName anyQ = convertResourceToQName((Resource) (statement
						.getObject()));

				WasInfluencedBy wib = pFactory.newWasInfluencedBy(null, anyQ,
						qname, null);

				store(convertResourceToQName(statement.getContext()), wib);
			}

			if (predQ.equals(Ontology.QNAME_RDFS_LABEL))
			{
				Literal lit = (Literal) (statement.getObject());
				if (lit.getLanguage() != null)
				{
					InternationalizedString is = pFactory
							.newInternationalizedString(lit.stringValue(), lit
									.getLanguage().toUpperCase());
					attributes.add(pFactory
							.newAttribute(Attribute.PROV_LABEL_QNAME, is,
									this.valueConverter));
				} else
				{
					attributes.add(pFactory.newAttribute(
							Attribute.PROV_LABEL_QNAME, lit.stringValue(),
							this.valueConverter));
				}
			}

			Value val = statement.getObject();
			if (!isProvURI(predQ))
			{
				if (!predQ.equals(Ontology.QNAME_RDF_TYPE)
						&& !predQ.equals(Ontology.QNAME_RDFS_LABEL))
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

						QName xsdType = pFactory
								.stringToQName(getXsdType(shortType));
						attr = pFactory.newAttribute(predQ.getNamespaceURI(),
								predQ.getLocalPart(), prefix,
								decodeLiteral(lit), xsdType);

					} else if (val instanceof Resource)
					{
						URIWrapper uw = new URIWrapper();
						java.net.URI jURI = java.net.URI.create(val
								.stringValue());
						uw.setValue(jURI);

						attr = pFactory.newAttribute(predQ.getNamespaceURI(),
								predQ.getLocalPart(), prefix, uw,
								pFactory.stringToQName(getXsdType("anyURI")));
					} else
					{
						System.err.println("Invalid value");
					}

					if (attr != null)
					{
						attributes.add(attr);
					}

				}
			}
		}
		return attributes;
	}

	private void handleTypes(ProvType[] types, QName context, QName subject, boolean implicit) {
		for (ProvType type : types)
		{
			switch (type)
			{
			case ACTIVITY:
				createActivity(context, subject, implicit);
				break;
			case AGENT:
			case PERSON:
			case ORGANIZATION:
			case SOFTWAREAGENT:
				createAgent(context, subject, implicit);
				break;
			case COLLECTION:
			case ENTITY:
			case PLAN:
			case BUNDLE:
				createEntity(context, subject, implicit);
				break;
			default:
			}
		}
	}
	
	protected void buildGraph()
	{
		for (QName contextQ : collators.keySet())
		{
			HashMap<QName, List<Statement>> collator = collators.get(contextQ);
			for (QName qname : collator.keySet())
			{
				
				ProvType[] explicitTypes = getExplicitTypes(contextQ, qname);
				ProvType[] implicitTypes = getImplicitTypes(contextQ, qname);
				implicitTypes = filterImplicitTypes(implicitTypes,
						explicitTypes);
				
				handleTypes(explicitTypes, contextQ, qname, false);
				handleTypes(implicitTypes, contextQ, qname, true);
				
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
			Collection<org.openprovenance.prov.xml.Statement> statements = new ArrayList<org.openprovenance.prov.xml.Statement>();
			statements.addAll(bundleHolder.getActivities());
			statements.addAll(bundleHolder.getEntities());
			statements.addAll(bundleHolder.getAgents());
			statements.addAll(bundleHolder.getStatements());
			NamedBundle bundle = pFactory.newNamedBundle(key, null, statements);
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
						if (isProvURI(convertURIToQName(statement
								.getPredicate())))
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

	private void createEntity(QName context, QName qname, boolean implicit)
	{

		List<Statement> statements = collators.get(context).get(qname);
		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.ENTITY);
		List<QName> members = new ArrayList<QName>();
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Resource)
			{
				QName valueQ = convertResourceToQName((Resource) value);
				if (predQ.equals(Ontology.QNAME_PROVO_wasDerivedFrom))
				{
					WasDerivedFrom wdf = pFactory
							.newWasDerivedFrom((QName) null, qname, valueQ,
									null, null, null, null);

					store(context, wdf);
				} else if (predQ.equals(Ontology.QNAME_PROVO_hadPrimarySource))
				{
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(null,
							qname, valueQ, null, null, null, null);

					pFactory.addPrimarySourceType(wdf);
					store(context, wdf);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasQuotedFrom))
				{
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(null,
							qname, valueQ, null, null, null, null);
					pFactory.addQuotationType(wdf);
					store(context, wdf);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasRevisionOf))
				{
					WasDerivedFrom wdf = pFactory.newWasDerivedFrom(null,
							qname, valueQ, null, null, null, null);
					pFactory.addRevisionType(wdf);
					store(context, wdf);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasGeneratedBy))
				{
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(null,
							qname, valueQ, null, null);

					store(context, wgb);
				} else if (predQ.equals(Ontology.QNAME_PROVO_alternateOf))
				{
					AlternateOf ao = pFactory.newAlternateOf(qname, valueQ);

					store(context, ao);
				} else if (predQ.equals(Ontology.QNAME_PROVO_specializationOf))
				{
					SpecializationOf so = pFactory.newSpecializationOf(qname,
							valueQ);

					store(context, so);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasInvalidatedBy))
				{
					WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(null,
							qname, valueQ, null, null);

					store(context, wib);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasAttributedTo))
				{
					WasAttributedTo wit = pFactory.newWasAttributedTo(null,
							qname, valueQ, null);

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
					MentionOf nmo = pFactory.newMentionOf(qname, entityQ,
							bundleQ);

					store(context, nmo);
				} else if (predQ.equals(Ontology.QNAME_PROVO_value))
				{
					Object resourceVal = convertResourceToQName((Resource) value);
					attributes.add(pFactory.newAttribute(
							Attribute.PROV_VALUE_QNAME, resourceVal,
							this.valueConverter));
				} else if (predQ.equals(Ontology.QNAME_PROVO_hadMember))
				{
					members.add(convertResourceToQName((Resource) (statement
                                                .getObject())));
				}
			} else if (value instanceof Literal)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_value))
				{
					Object literal = decodeLiteral((Literal) value);
					attributes.add(pFactory.newAttribute(
							Attribute.PROV_VALUE_QNAME, literal,
							this.valueConverter));
				}
			}
		}

		if(members.size() > 0) {
			HadMember hm = pFactory.newHadMember(qname, members);
			store(context, hm);
		}

		if (!implicit)
		{
			org.openprovenance.prov.xml.Entity entity = pFactory.newEntity(
					qname, attributes);
			store(context, entity);
		}
	}

	private void createAgent(QName context, QName qname, boolean implicit)
	{
		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.AGENT);
		List<Statement> statements = collators.get(context).get(qname);

		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_actedOnBehalfOf))
				{
					QName agentQ = convertResourceToQName((Resource) value);
					ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(null,
							qname, agentQ, null, null);

					store(context, aobo);
				}
			}
		}

		if (!implicit)
		{
			org.openprovenance.prov.xml.Agent agent = pFactory.newAgent(qname,
					attributes);
			store(context, agent);
		}
	}

	private void createActivity(QName context, QName qname, boolean implicit)
	{
		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.ACTIVITY);
		List<Statement> statements = collators.get(context).get(qname);

		XMLGregorianCalendar startTime = null;
		XMLGregorianCalendar endTime = null;

		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();
			if (value instanceof Resource)
			{
				QName valueQ = convertResourceToQName((Resource) value);
				if (predQ.equals(Ontology.QNAME_PROVO_wasAssociatedWith))
				{
					WasAssociatedWith waw = pFactory.newWasAssociatedWith(null,
							qname, valueQ, null, null);

					store(context, waw);

				} else if (predQ.equals(Ontology.QNAME_PROVO_used))
				{
					Used used = pFactory.newUsed(null, qname, valueQ, null,
							null);
					store(context, used);

				} else if (predQ.equals(Ontology.QNAME_PROVO_wasStartedBy))
				{
					WasStartedBy wsb = pFactory.newWasStartedBy(null, qname,
							valueQ, null, null, null);
					store(context, wsb);

				} else if (predQ.equals(Ontology.QNAME_PROVO_generated))
				{
					WasGeneratedBy wgb = pFactory.newWasGeneratedBy(null,
							valueQ, qname, null, null);
					store(context, wgb);

				} else if (predQ.equals(Ontology.QNAME_PROVO_wasEndedBy))
				{
					WasEndedBy web = pFactory.newWasEndedBy(null, qname,
							valueQ, null, null, null);
					store(context, web);

				} else if (predQ.equals(Ontology.QNAME_PROVO_wasInformedBy))
				{
					WasInformedBy wib = pFactory.newWasInformedBy(null, qname,
							valueQ, null);
					store(context, wib);
				}
			} else if (value instanceof Literal)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_startedAtTime))
				{
					Object literal = decodeLiteral((Literal) value);
					startTime = (XMLGregorianCalendar) literal;
				} else if (predQ.equals(Ontology.QNAME_PROVO_endedAtTime))
				{
					Object literal = decodeLiteral((Literal) value);
					endTime = (XMLGregorianCalendar) literal;
				}
			}
		}

		if (!implicit)
		{
			org.openprovenance.prov.xml.Activity activity = pFactory
					.newActivity(qname, startTime, endTime, attributes);
			store(context, activity);
		}
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
