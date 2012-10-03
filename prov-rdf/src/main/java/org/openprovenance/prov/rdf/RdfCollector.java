package org.openprovenance.prov.rdf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasInformedBy;
import org.openprovenance.prov.xml.WasStartedBy;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.vocabulary.RDF;
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
				"Attribution"), BUNDLE("Bundle");

		private static final Map<String, ProvType> lookup = new HashMap<String, ProvType>();
		static
		{
			for (ProvType provStatement : EnumSet.allOf(ProvType.class))
				lookup.put(provStatement.getURI(), provStatement);
		}

		private String uri;

		private ProvType(String localName)
		{
			this.uri = PROV + localName;
		}

		public String getURI()
		{
			return uri;
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

	public RdfCollector(ProvFactory pFactory)
	{
		this.pFactory = pFactory;
		this.collator = new HashMap<QName, Set<Statement>>();
		this.document = pFactory.newDocument();
		document.setNss(new Hashtable<String, String>());
	}

	@Override
	public void handleNamespace(String prefix, String namespace)
	{
		System.out.println("Namespace: " + prefix + " " + namespace);
		document.getNss().put(prefix, namespace);
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
				ProvType provType = ProvType.lookup(statement.getObject()
						.stringValue());
				if (currentType == null
						|| (currentType == ProvType.ENTITY && provType == ProvType.BUNDLE))
				{
					currentType = provType;
				}
				removedStatements.add(statement);
			}
		}
		statements.removeAll(removedStatements);
		return currentType;
	}

	private Object decodeLiteral(Literal literal)
	{
		String dataType = literal.getDatatype().stringValue();
		if (dataType.equals(XMLS + "QName"))
		{
			return new QName(literal.stringValue());
		} else if (dataType.equals(XMLS + "string"))
		{
			return literal.stringValue();
		} else
		{
			return null;
		}
	}

	private Set<Statement> decodeTypes(HasType hasType,
			Set<Statement> statements)
	{

		Set<Statement> removedStatements = new HashSet<Statement>();
		for (Statement statement : statements)
		{
			if (statement.getPredicate().stringValue().equals(PROV + "type"))
			{
				Value value = statement.getObject();
				if (value instanceof Literal)
				{
					Object literalValue = decodeLiteral((Literal) (value));
					if (literalValue == null)
					{
						System.out.println("Null :( " + value.stringValue());
					}
					pFactory.addType(hasType, decodeLiteral((Literal) value));
					removedStatements.add(statement);
				} else
				{
					System.out.println(value);
					System.out.println("Value wasn't a suitable type");
				}
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
					System.out.println(statement);
				}
			}
		}
	}

	private void createEntity(QName qname)
	{
		System.out.println("Create entity: " + qname);
		org.openprovenance.prov.xml.Entity entity = pFactory.newEntity(qname);
		this.document.getEntityOrActivityOrWasGeneratedBy().add(entity);

		Set<Statement> statements = collator.get(qname);
		statements = decodeTypes(entity, statements);

		for (Statement statement : statements)
		{

		}

		// decodeAttributes(entity, statements)
	}

	private void createAgent(QName qname)
	{
		System.out.println("Create agent: " + qname);
		org.openprovenance.prov.xml.Agent agent = pFactory.newAgent(qname);
		this.document.getEntityOrActivityOrWasGeneratedBy().add(agent);

		Set<Statement> statements = collator.get(qname);
		statements = decodeTypes(agent, statements);

		for (Statement statement : statements)
		{
		}
	}

	private List<Value> getValuesForPredicate(QName qname, String uri)
	{
		ArrayList<Value> values = new ArrayList<Value>();
		for (Statement statement : collator.get(qname))
		{
			if (statement.getPredicate().stringValue().equals(uri))
			{
				values.add(statement.getObject());
			}
		}
		return values;
	}

	private void createActivity(QName qname)
	{
		System.out.println("Create activity: " + qname);
		org.openprovenance.prov.xml.Activity activity = pFactory
				.newActivity(qname);
		this.document.getEntityOrActivityOrWasGeneratedBy().add(activity);
		Set<Statement> statements = collator.get(qname);
		statements = decodeTypes(activity, statements);

		Set<Statement> removedStatements = new HashSet<Statement>();
		for (Statement statement : statements)
		{
			String predS = statement.getPredicate().stringValue();
			Value value = statement.getObject();
			if (predS.equals(PROV + "wasAssociatedWith"))
			{
				if (value instanceof Resource)
				{
					QName agentQ = new QName(value.stringValue());
					WasAssociatedWith waw = pFactory.newWasAssociatedWith(
							(QName) null, pFactory.newActivityRef(qname),
							pFactory.newAgentRef(agentQ));

					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(waw);
					removedStatements.add(statement);
				}
			} else if (predS.equals(PROV + "used"))
			{
				if (value instanceof Resource)
				{
					List<Value> roles = getValuesForPredicate(qname, PROV
							+ "role");
					assert (roles.size() <= 1);

					String role = null;
					// Role is a string
					if (roles.size() == 1)
					{
						role = roles.get(0).stringValue();
					}
					QName entityQ = new QName(value.stringValue());
					Used used = pFactory.newUsed((QName) null,
							pFactory.newActivityRef(qname), role,
							pFactory.newEntityRef(entityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy().add(
							used);
					removedStatements.add(statement);
				}
			} else if (predS.equals(PROV + "wasStartedBy"))
			{
				if (value instanceof Resource)
				{
					QName entityQ = new QName(value.stringValue());
					WasStartedBy wsb = pFactory.newWasStartedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(entityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wsb);
					removedStatements.add(statement);
				}
			} else if (predS.equals(PROV + "wasEndedBy"))
			{
				if (value instanceof Resource)
				{
					QName entityQ = new QName(value.stringValue());
					WasEndedBy web = pFactory.newWasEndedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newEntityRef(entityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(web);
					removedStatements.add(statement);
				}
			} else if (predS.equals(PROV + "wasInformedBy"))
			{
				if (value instanceof Resource)
				{
					QName activityQ = new QName(value.stringValue());
					WasInformedBy wib = pFactory.newWasInformedBy((QName) null,
							pFactory.newActivityRef(qname),
							pFactory.newActivityRef(activityQ));
					this.document.getEntityOrActivityOrWasGeneratedBy()
							.add(wib);
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