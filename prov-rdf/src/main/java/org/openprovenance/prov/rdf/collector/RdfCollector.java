package org.openprovenance.prov.rdf.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.KeyQNamePair;
import org.openprovenance.prov.model.URIWrapper;
import org.openprovenance.prov.rdf.Ontology;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.InternationalizedString;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;
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

	protected ProvFactory pFactory;
	protected HashMap<QName, HashMap<QName, List<Statement>>> collators;
	private Hashtable<QName, BundleHolder> bundles;
	protected Document document;
	private Hashtable<String, String> revnss;
	private ValueConverter valueConverter;
	protected static String BNODE_NS = "http://openprovenance.org/provtoolbox/bnode/";
	
	private List<QName> QUAL_PREDS = Arrays.asList(new QName[] {
			Ontology.QNAME_PROVO_qualifiedAssociation,
			Ontology.QNAME_PROVO_qualifiedAttribution,
			Ontology.QNAME_PROVO_qualifiedCommunication,
			Ontology.QNAME_PROVO_qualifiedDelegation,
			Ontology.QNAME_PROVO_qualifiedDerivation,
			Ontology.QNAME_PROVO_qualifiedEnd,
			Ontology.QNAME_PROVO_qualifiedGeneration,
			Ontology.QNAME_PROVO_qualifiedInfluence,
			Ontology.QNAME_PROVO_qualifiedInvalidation,
			Ontology.QNAME_PROVO_qualifiedPrimarySource,
			Ontology.QNAME_PROVO_qualifiedQuotation,
			Ontology.QNAME_PROVO_qualifiedRevision,
			Ontology.QNAME_PROVO_qualifiedStart,
			Ontology.QNAME_PROVO_qualifiedUsage,
			Ontology.QNAME_PROVO_qualifiedInsertion,
			Ontology.QNAME_PROVO_qualifiedRemoval

	});

	private List<QName> DM_TYPES = Arrays.asList(new QName[] {
			Ontology.QNAME_PROVO_Bundle, Ontology.QNAME_PROVO_Collection,
			Ontology.QNAME_PROVO_EmptyCollection,
			Ontology.QNAME_PROVO_Organization, Ontology.QNAME_PROVO_Person,
			Ontology.QNAME_PROVO_Plan, Ontology.QNAME_PROVO_PrimarySource,
			Ontology.QNAME_PROVO_Quotation, Ontology.QNAME_PROVO_Revision,
			Ontology.QNAME_PROVO_SoftwareAgent,
			Ontology.QNAME_PROVDC_Contributor,
			Ontology.QNAME_PROVO_Dictionary,
			Ontology.QNAME_PROVO_EmptyDictionary


			});

	public RdfCollector(ProvFactory pFactory)
	{
		this.pFactory = pFactory;
		this.collators = new HashMap<QName, HashMap<QName, List<Statement>>>();
		this.revnss = new Hashtable<String, String>();
		this.document = pFactory.newDocument();
		this.valueConverter = new ValueConverter(pFactory);
		this.bundles = new Hashtable<QName, BundleHolder>();
		document.setNss(new Hashtable<String, String>());
		handleNamespace(NamespacePrefixMapper.XSD_PREFIX,
				NamespacePrefixMapper.XSD_HASH_NS);
		handleNamespace("bnode", BNODE_NS);
	}

	private HashMap<QName, List<Statement>> getCollator(Resource context)
	{
		return this.collators.get(convertResourceToQName(context));
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
			org.openprovenance.prov.model.Relation0 relation0)
	{
		getBundleHolder(context).addStatement(
				(org.openprovenance.prov.model.Statement) relation0);
	}

	/* Utility functions */

	protected boolean isBNodeReferenced(BNode object)
	{
		Collection<HashMap<QName, List<Statement>>> contexts = collators
				.values();
		for (HashMap<QName, List<Statement>> context : contexts)
		{
			for (QName subject : context.keySet())
			{
				List<Statement> statements = context.get(subject);
				for (Statement statement : statements)
				{
					Value value = statement.getObject();
					QName pred = convertURIToQName(statement.getPredicate());
					if(QUAL_PREDS.contains(pred)) {
						continue;
					}
					
					if (value instanceof BNode)
					{
						if (value.equals(object))
						{
							return true;
						}
					}
					else if(value instanceof QName) {
						QName qname = (QName)value;
						if(qname.getNamespaceURI().equals(BNODE_NS) && qname.getLocalPart().equals(object.getID())) {
							return true;
						} 
					} else if(value instanceof Resource) {
						QName qname = convertResourceToQName((Resource)value);
						if(qname.getNamespaceURI().equals(BNODE_NS) && qname.getLocalPart().equals(object.getID())) {
							return true;
						} 
					}
				}
			}
		}
		
		return false;
	}

	private boolean isProvURI(QName qname)
	{
		if (!qname.getNamespaceURI().equals(NamespacePrefixMapper.PROV_NS))
		{
			return false;
		}
		return true;
	}

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
				if (provType!=null) explicitOptions.add(provType);
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
			return bnodeToQName((BNode) resource);
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
				+ "dateTime")
				|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "time")
				|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "date")
				|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
						+ "gYearMonth")
				|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
						+ "gMonthDay")
				|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "gYear")
				|| dataType
						.equals(NamespacePrefixMapper.XSD_HASH_NS + "gMonth")
				|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "gDay"))
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

	private void handleTypes(ProvType[] types, QName context, QName subject)
	{
		for (ProvType type : types)
		{
			switch (type)
			{
			case ACTIVITY:
				createActivity(context, subject);
				break;
			case AGENT:
			case PERSON:
			case ORGANIZATION:
			case SOFTWAREAGENT:
				createAgent(context, subject);
				break;
			case COLLECTION:
			case DICTIONARY:
			case ENTITY:
			case PLAN:
			case BUNDLE:
				createEntity(context, subject);
				break;
			default:
			}
		}

		handlePredicates(context, subject);
	}

	protected void buildGraph()
	{
		for (QName contextQ : collators.keySet())
		{
			HashMap<QName, List<Statement>> collator = collators.get(contextQ);
			for (QName qname : collator.keySet())
			{
				ProvType[] explicitTypes = getExplicitTypes(contextQ, qname);
				handleTypes(explicitTypes, contextQ, qname);
			}
		}
	}

	protected void buildBundles()
	{
		// Add 'default' bundle
		if (bundles.containsKey(new QName("")))
		{
			BundleHolder defaultBundle = bundles.get(new QName(""));
			for (org.openprovenance.prov.model.Activity activity : defaultBundle
					.getActivities())
			{
				document.getStatementOrBundle().add(activity);
			}

			for (org.openprovenance.prov.model.Agent agent : defaultBundle
					.getAgents())
			{
				document.getStatementOrBundle().add(agent);
			}

			for (org.openprovenance.prov.model.Entity entity : defaultBundle
					.getEntities())
			{
				document.getStatementOrBundle().add(entity);
			}

			for (org.openprovenance.prov.model.Statement statement : defaultBundle
					.getStatements())
			{
				document.getStatementOrBundle().add(statement);
			}
		}

		for (QName key : bundles.keySet())
		{
			if (key.getLocalPart().equals(""))
			{
				continue;
			}
			BundleHolder bundleHolder = bundles.get(key);
			Collection<org.openprovenance.prov.model.Statement> statements = new ArrayList<org.openprovenance.prov.model.Statement>();
			statements.addAll(bundleHolder.getActivities());
			statements.addAll(bundleHolder.getEntities());
			statements.addAll(bundleHolder.getAgents());
			statements.addAll(bundleHolder.getStatements());
			NamedBundle bundle = pFactory.newNamedBundle(key, null, statements);
			document.getStatementOrBundle().add(bundle);
		}

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
			// TODO: Ugly! This generates a prefix when none is given.
			String prefix = "ns" + uri.getNamespace().hashCode() + "";//
			handleNamespace(prefix, uri.getNamespace());
			qname = new QName(uri.getNamespace(), uri.getLocalName(), prefix);
		}
		return qname;
	}

	/*
	 * Handles all attributes (location, role, type, etc). Returns a list of
	 * Attribute objects.
	 */
	public List<Attribute> collectAttributes(QName context, QName qname,
			ProvType type)
	{
		List<Attribute> attributes = new ArrayList<Attribute>();
		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (statement.getPredicate().equals(RDF.TYPE))
			{
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
							QName typeQ = convertResourceToQName((Resource) statement
									.getObject());

							if (isProvURI(typeQ)
									&& DM_TYPES.indexOf(typeQ) == -1)
							{
								// System.out.println("Skipping type: " +
								// typeQ);
							} else
							{
								attributes.add(pFactory.newAttribute(
										org.openprovenance.prov.xml.Attribute.PROV_TYPE_QNAME, typeQ,
										this.valueConverter));
							}

						} else if (statement.getObject() instanceof Literal)
						{

							attributes
									.add(pFactory.newAttribute(
											org.openprovenance.prov.xml.Attribute.PROV_TYPE_QNAME,
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
				attributes.add(pFactory.newAttribute(org.openprovenance.prov.xml.Attribute.PROV_ROLE_QNAME,
						role, this.valueConverter));
			}

			if (predQ.equals(Ontology.QNAME_PROVO_atLocation))
			{
				Object obj = valueToObject(statement.getObject());
				if (obj != null)
				{
					attributes.add(pFactory.newAttribute(
							org.openprovenance.prov.xml.Attribute.PROV_LOCATION_QNAME, obj,
							this.valueConverter));
				}
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
							.newAttribute(org.openprovenance.prov.xml.Attribute.PROV_LABEL_QNAME, is,
									this.valueConverter));
				} else
				{
					attributes.add(pFactory.newAttribute(
							org.openprovenance.prov.xml.Attribute.PROV_LABEL_QNAME, lit.stringValue(),
							this.valueConverter));
				}
			}

			if (value instanceof Resource)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_value))
				{
					Object resourceVal = convertResourceToQName((Resource) value);
					attributes.add(pFactory.newAttribute(
							org.openprovenance.prov.xml.Attribute.PROV_VALUE_QNAME, resourceVal,
							this.valueConverter));
				}
			} else if (value instanceof Literal)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_value))
				{
					Object literal = decodeLiteral((Literal) value);
					attributes.add(pFactory.newAttribute(
							org.openprovenance.prov.xml.Attribute.PROV_VALUE_QNAME, literal,
							this.valueConverter));
				}
			}

			if (!isProvURI(predQ))
			{
				if (!predQ.equals(Ontology.QNAME_RDF_TYPE)
						&& !predQ.equals(Ontology.QNAME_RDFS_LABEL))
				{
					// Retrieve the prefix
					String prefix = this.revnss.get(predQ.getNamespaceURI());
					Attribute attr = null;
					if (value instanceof Literal)
					{
						Literal lit = (Literal) value;

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

					} else if (value instanceof Resource)
					{
						attr = pFactory.newAttribute(predQ.getNamespaceURI(),
								predQ.getLocalPart(), prefix,
								convertResourceToQName((Resource) value),
								this.valueConverter);
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

	/*
	 * Handles PROV-O predicates, creating beans where appropriate.
	 */
	private void handlePredicates(QName context, QName qname)
	{
		List<QName> members = new ArrayList<QName>();
		List<QName> dictionaryMembers = new ArrayList<QName>();

		List<Statement> statements = collators.get(context).get(qname);
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

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
					MentionOf mo = pFactory.newMentionOf(entityQ, qname,
							bundleQ);

					store(context, mo);
				} else if (predQ.equals(Ontology.QNAME_PROVO_wasAssociatedWith))
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
				} else if (predQ.equals(Ontology.QNAME_PROVO_actedOnBehalfOf))
				{
					QName agentQ = convertResourceToQName((Resource) value);
					ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(null,
							qname, agentQ, null, null);

					store(context, aobo);
				} else if (predQ.equals(Ontology.QNAME_PROVO_hadMember))
				{
					members.add(convertResourceToQName((Resource) (statement
							.getObject())));
				} else if (predQ.equals(Ontology.QNAME_PROVO_hadDictionaryMember))
				{
					dictionaryMembers.add(convertResourceToQName((Resource) (statement
							.getObject())));
				}
			}
		}

		if (members.size() > 0)
		{
			HadMember hm = pFactory.newHadMember(qname, members);
			store(context, hm);
		}		
		if (dictionaryMembers.size() > 0)
		{
			List<KeyQNamePair> pairs=
			 createKeyEntityPairs( context, dictionaryMembers);
			
			DictionaryMembership hm = pFactory.newDictionaryMembership(qname, pairs);
			store(context, hm);
		}
	}
	
	protected List<Value> getDataObjects(QName context, QName subject, QName pred)
	{
		List<Statement> statements = collators.get(context).get(subject);
		List<Value> objects = new ArrayList<Value>();
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();
			if (pred.equals(predQ) && (!(value instanceof Resource)))
			{
				objects.add(value);
			}
		}
		return objects;
	}

	protected List<KeyQNamePair> createKeyEntityPairs(QName context, List<QName> pairs) {
		List<KeyQNamePair> result= new LinkedList<KeyQNamePair>();
		for (QName pair: pairs) {
			List<Value> keys = getDataObjects(context, pair,
	                  Ontology.QNAME_PROVO_pairKey);  // key is data property!

			List<QName> entities = getObjects(context, pair,
	                  Ontology.QNAME_PROVO_pairEntity);
			KeyQNamePair p=new KeyQNamePair();
			if (!keys.isEmpty()) p.key=valueToObject(keys.get(0)); // we ignore the others
			if (!entities.isEmpty()) p.name=entities.get(0); // we ignore the others
		    result.add(p);
		}
		return result;
	}

	protected List<QName> getObjects(QName context, QName subject, QName pred)
	{
		List<Statement> statements = collators.get(context).get(subject);
		List<QName> objects = new ArrayList<QName>();
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();
			if (pred.equals(predQ) && value instanceof Resource)
			{
				objects.add(convertResourceToQName((Resource) value));
			}
		}
		return objects;
	}


	private void createEntity(QName context, QName qname)
	{
		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.ENTITY);

		org.openprovenance.prov.model.Entity entity = pFactory.newEntity(qname,
				attributes);
		getBundleHolder(context).store(entity);
	}

	private void createAgent(QName context, QName qname)
	{
		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.AGENT);

		org.openprovenance.prov.model.Agent agent = pFactory.newAgent(qname,
				attributes);
		getBundleHolder(context).store(agent);
	}

	private void createActivity(QName context, QName qname)
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
			if (value instanceof Literal)
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

		org.openprovenance.prov.model.Activity activity = pFactory.newActivity(
				qname, startTime, endTime, attributes);
		getBundleHolder(context).store(activity);
	}

	public Document getDocument()
	{
		return document;
	}

	/**
	 * RDFHandlerBase overrides
	 */

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

	protected QName bnodeToQName(BNode bnode)
	{
		return new QName(BNODE_NS, bnode.getID(), "bnode");
	}

	@Override
	public void handleStatement(Statement statement)
	{
		QName subjectQ = null;

		QName contextQ = convertResourceToQName(statement.getContext());

		Resource subject = statement.getSubject();
		if (subject instanceof BNodeImpl)
		{
			subjectQ = bnodeToQName((BNodeImpl) subject);
		} else if (subject instanceof URI)
		{
			URI uri = (URI) subject;
			subjectQ = convertURIToQName(uri);
		} else
		{
			System.err.println("Invalid subject resource");
		}

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

	@Override
	public void endRDF()
	{
		buildGraph();
		buildBundles();
	}
}
