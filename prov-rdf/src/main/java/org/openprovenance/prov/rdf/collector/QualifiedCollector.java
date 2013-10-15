package org.openprovenance.prov.rdf.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.rdf.Ontology;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Type;
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
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;

public class QualifiedCollector extends RdfCollector {

	public QualifiedCollector(ProvFactory pFactory)
	{
		super(pFactory);
		this.pFactory = pFactory;
	}

	/*
	 * Given two lists, this creates a list of permutations of those lists. e.g.
	 * Given [1,2], [[3,4]], returns [[1,3],[1,4],[2,3],[2,4]]
	 */
	private ArrayList<List<?>> permute(List<?>... lists)
	{

		ArrayList<List<?>> output = new ArrayList<List<?>>();
		if (lists.length == 0)
		{
			output.add(new ArrayList<Object>());
			return output;
		}

		for (List<?> list : lists)
		{
			// We require that the result has as many elements as the number
			// of lists passed - so if a list is empty, add in a single null.
			if (list.size() == 0)
				list.add(null);
		}

		List<?> first = lists[0];
		List<?>[] rest = Arrays.copyOfRange(lists, 1, lists.length);

		for (Object x : first)
		{
			ArrayList<List<?>> permuted = permute(rest);
			for (List<?> tocombine : permuted)
			{
				ArrayList<Object> l = new ArrayList<Object>();
				l.add(x);
				l.addAll(tocombine);
				output.add(l);
			}
		}

		return output;
	}

	

	private List<QName> getSubjects(QName context, QName pred, QName object)
	{
		HashMap<QName, List<Statement>> resources = collators.get(context);
		List<QName> subjects = new ArrayList<QName>();
		for (QName subject : resources.keySet())
		{
			List<Statement> statements = resources.get(subject);
			for (Statement statement : statements)
			{
				QName predQ = convertURIToQName(statement.getPredicate());
				Value value = statement.getObject();
				if (pred.equals(predQ) && value instanceof Resource)
				{
					QName valQ = convertResourceToQName((Resource) value);
					if (valQ.equals(object))
					{
						subjects.add(subject);
					}
				}
			}
		}
		return subjects;
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
				ProvType[] types = getExplicitTypes(contextQ, qname);
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
						createDerivation(contextQ, qname,
								Ontology.QNAME_PROVO_qualifiedDerivation);
						break;
					case PRIMARYSOURCE:
						createPrimarySource(contextQ, qname);
						break;
					case QUOTATION:
						createQuotation(contextQ, qname);
						break;
					case REVISION:
						createRevision(contextQ, qname);
						break;
					case END:
						createEnd(contextQ, qname);
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
					case INSERTION:
						createInsertion(contextQ, qname);
						break;
					case REMOVAL:
						createRemoval(contextQ,qname);
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
		nullifyBNodes();
	}

	private void nullifyBNodes()
	{
		for (StatementOrBundle sob : document
				.getStatementOrBundle())
		{
			if (sob instanceof Identifiable)
			{
				Identifiable idsob = (Identifiable) sob;

				if (idsob.getId() != null
						&& idsob.getId().getNamespaceURI().equals(BNODE_NS))
				{
					if (!isBNodeReferenced(new BNodeImpl(idsob.getId()
							.getLocalPart())))
					{
						idsob.setId(null);
					}
				}
			}
		}
	}

	private List<XMLGregorianCalendar> getInstantaneousTimes(
			List<Statement> statements)
	{
		List<XMLGregorianCalendar> times = new ArrayList<XMLGregorianCalendar>();
		for (Statement statement : statements)
		{
			QName predQ = convertURIToQName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Literal)
			{
				if (predQ.equals(Ontology.QNAME_PROVO_atTime))
				{
					times.add((XMLGregorianCalendar) super
							.decodeLiteral((Literal) value));
				}
			}
		}
		return times;
	}

	private void createRevision(QName contextQ, QName qname)
	{
		List<WasDerivedFrom> wdfs = createDerivation(contextQ, qname,
				Ontology.QNAME_PROVO_qualifiedRevision);
		//QName q = pFactory.newQName("prov:Revision");
		QName q = Ontology.QNAME_PROVO_Revision;
		Type type=pFactory.newType(q, ValueConverter.QNAME_XSD_QNAME);
		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private void createQuotation(QName contextQ, QName qname)
	{
		List<WasDerivedFrom> wdfs = createDerivation(contextQ, qname,
				Ontology.QNAME_PROVO_qualifiedQuotation);
		//Object q = pFactory.newQName("prov:Quotation");
		QName q = Ontology.QNAME_PROVO_Quotation;
		Type type=pFactory.newType(q, ValueConverter.QNAME_XSD_QNAME);
		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private void createPrimarySource(QName context, QName qname)
	{
		List<WasDerivedFrom> wdfs = createDerivation(context, qname,
				Ontology.QNAME_PROVO_qualifiedPrimarySource);
		//Object q = pFactory.newQName("prov:PrimarySource");
		QName q = Ontology.QNAME_PROVO_PrimarySource;
		Type type=pFactory.newType(q, ValueConverter.QNAME_XSD_QNAME);

		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private List<WasDerivedFrom> createDerivation(QName context, QName qname,
			QName pred)
	{

		List<QName> entities = getObjects(context, qname,
				Ontology.QNAME_PROVO_entity);
		List<QName> activities = getObjects(context, qname,
				Ontology.QNAME_PROVO_hadActivity);
		List<QName> generations = getObjects(context, qname,
				Ontology.QNAME_PROVO_hadGeneration);
		List<QName> usages = getObjects(context, qname,
				Ontology.QNAME_PROVO_hadUsage);

		List<QName> generated = getSubjects(context, pred, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.DERIVATION);

		qname = getQualQName(qname);

		List<WasDerivedFrom> wdfs = new ArrayList<WasDerivedFrom>();
		List<List<?>> perms = permute(generated, entities, activities,
				generations, usages);
		for (List<?> perm : perms)
		{
			WasDerivedFrom wdf = pFactory.newWasDerivedFrom(qname,
					(QName) perm.get(0), (QName) perm.get(1),
					(QName) perm.get(2), (QName) perm.get(3),
					(QName) perm.get(4), attributes);
			store(context, wdf);
			wdfs.add(wdf);
		}
		return wdfs;
	}

	private List<WasInfluencedBy> createInfluence(QName context, QName qname)
	{
		HashSet<QName> all_influencers = new HashSet<QName>();
		List<QName> influencers = getObjects(context, qname,
				Ontology.QNAME_PROVO_influencer);
		List<QName> agents = getObjects(context, qname,
				Ontology.QNAME_PROVO_agent);
		List<QName> entities = getObjects(context, qname,
				Ontology.QNAME_PROVO_entity);
		List<QName> activities = getObjects(context, qname,
				Ontology.QNAME_PROVO_activity);
		all_influencers.addAll(influencers);
		all_influencers.addAll(agents);
		all_influencers.addAll(entities);
		all_influencers.addAll(activities);
		List<QName> influencees = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedInfluence, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.INFLUENCE);

		qname = getQualQName(qname);

		List<WasInfluencedBy> wibs = new ArrayList<WasInfluencedBy>();
		List<List<?>> perms = permute(influencees, new ArrayList<QName>(
				all_influencers));
		for (List<?> perm : perms)
		{
			WasInfluencedBy wib = pFactory.newWasInfluencedBy(qname,
					(QName) perm.get(0), (QName) perm.get(1), attributes);
			store(context, wib);
			wibs.add(wib);
		}
		return wibs;
	}

	private void createEnd(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QName> triggers = getObjects(context, qname,
				Ontology.QNAME_PROVO_entity);
		List<QName> enders = getObjects(context, qname,
				Ontology.QNAME_PROVO_hadActivity);
		List<QName> activities = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedEnd, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.END);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(activities, triggers, enders, times);
		for (List<?> perm : perms)
		{
			WasEndedBy web = pFactory.newWasEndedBy(qname, (QName) perm.get(0),
					(QName) perm.get(1), (QName) perm.get(2),
					(XMLGregorianCalendar) perm.get(3), attributes);
			store(context, web);
		}
	}

	private void createInsertion(QName context, QName qname)
	{
		List<QName> objectDictionaries = getObjects(context, qname,
				Ontology.QNAME_PROVO_dictionary);
		List<QName> pairs = getObjects(context, qname,
				Ontology.QNAME_PROVO_insertedKeyEntityPair);
		List<QName> subjectDictionaries = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedInsertion, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.INSERTION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(subjectDictionaries, objectDictionaries);
		for (List<?> perm : perms)
		{
			DerivedByInsertionFrom dbif = pFactory.newDerivedByInsertionFrom(qname, 
					(QName) perm.get(0),
					(QName) perm.get(1),
					createKeyEntityPairs(context, pairs), 
					attributes);
					
			store(context, dbif);
		}
	}

	
	private void createRemoval(QName context, QName qname)
	{
		List<QName> objectDictionaries = getObjects(context, qname,
				Ontology.QNAME_PROVO_dictionary);
		List<Value> keys = getDataObjects(context, qname,
				Ontology.QNAME_PROVO_removedKey);
		List<QName> subjectDictionaries = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedRemoval, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.REMOVAL);

		qname = getQualQName(qname);
		
		List<Object> theKeys=new LinkedList<Object>();
		for (Value key: keys) {
			theKeys.add(valueToObject(key));
		}

		List<List<?>> perms = permute(subjectDictionaries, objectDictionaries);
		for (List<?> perm : perms)
		{
			DerivedByRemovalFrom dbif = pFactory.newDerivedByRemovalFrom(qname, 
					(QName) perm.get(0),
					(QName) perm.get(1),
	                theKeys,
					attributes);
					
			store(context, dbif);
		}
	}


	private void createStart(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QName> triggers = getObjects(context, qname,
				Ontology.QNAME_PROVO_entity);
		List<QName> starters = getObjects(context, qname,
				Ontology.QNAME_PROVO_hadActivity);
		List<QName> activities = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedStart, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.START);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(activities, triggers, starters, times);
		for (List<?> perm : perms)
		{
			WasStartedBy wsb = pFactory.newWasStartedBy(qname,
					(QName) perm.get(0), (QName) perm.get(1),
					(QName) perm.get(2), (XMLGregorianCalendar) perm.get(3),
					attributes);
			store(context, wsb);
		}
	}

	private void createInvalidation(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);

		List<QName> activities = getObjects(context, qname,
				Ontology.QNAME_PROVO_activity);
		List<QName> entities = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedInvalidation, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.INVALIDATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(entities, activities, times);
		for (List<?> perm : perms)
		{
			WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(qname,
					(QName) perm.get(0), (QName) perm.get(1),
					(XMLGregorianCalendar) perm.get(2), attributes);
			store(context, wib);
		}
	}

	private void createDelegation(QName context, QName qname)
	{
		List<QName> activities = getObjects(context, qname,
				Ontology.QNAME_PROVO_hadActivity);
		List<QName> agents = getObjects(context, qname,
				Ontology.QNAME_PROVO_agent);
		List<QName> subordinates = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedDelegation, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.DELEGATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(subordinates, agents, activities);
		for (List<?> perm : perms)
		{
			ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(qname,
					(QName) perm.get(0), (QName) perm.get(1),
					(QName) perm.get(2), attributes);
			store(context, aobo);
		}

	}

	private void createCommunication(QName context, QName qname)
	{
		List<QName> activities = getObjects(context, qname,
				Ontology.QNAME_PROVO_activity);
		List<QName> effects = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedCommunication, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.COMMUNICATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(effects, activities);
		for (List<?> perm : perms)
		{
			WasInformedBy wib = pFactory.newWasInformedBy(qname,
					(QName) perm.get(0), (QName) perm.get(1), attributes);
			store(context, wib);
		}
	}

	private void createAttribution(QName context, QName qname)
	{
		List<QName> agents = getObjects(context, qname,
				Ontology.QNAME_PROVO_agent);
		List<QName> entities = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedAttribution, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.ATTRIBUTION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(entities, agents);
		for (List<?> perm : perms)
		{
			WasAttributedTo wat = pFactory.newWasAttributedTo(qname,
					(QName) perm.get(0), (QName) perm.get(1), attributes);
			store(context, wat);
		}
	}

	private void createAssociation(QName context, QName qname)
	{
		List<QName> plans = getObjects(context, qname,
				Ontology.QNAME_PROVO_hadPlan);
		List<QName> agents = getObjects(context, qname,
				Ontology.QNAME_PROVO_agent);
		List<QName> activities = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedAssociation, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.ASSOCIATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(activities, agents, plans);
		for (List<?> perm : perms)
		{
			WasAssociatedWith waw = pFactory.newWasAssociatedWith(qname,
					(QName) perm.get(0), (QName) perm.get(1),
					(QName) perm.get(2), attributes);
			store(context, waw);
		}
	}

	private void createUsage(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QName> entities = getObjects(context, qname,
				Ontology.QNAME_PROVO_entity);
		List<QName> activities = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedUsage, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.USAGE);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(activities, entities, times);
		for (List<?> perm : perms)
		{
			Used used = pFactory.newUsed(qname, (QName) perm.get(0),
					(QName) perm.get(1), (XMLGregorianCalendar) perm.get(2),
					attributes);
			store(context, used);
		}

	}

	private QName getQualQName(QName qname)
	{
		if (qname.getNamespaceURI() == "" || qname.getNamespaceURI().equals(BNODE_NS))
		{
			BNode bnode = new BNodeImpl(qname.getLocalPart());
			if (!isBNodeReferenced(bnode))
			{
				return null;
			}
			return new QName(BNODE_NS, qname.getLocalPart(), "bnode");
		}
		return qname;
	}

	private void createGeneration(QName context, QName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QName> activities = getObjects(context, qname,
				Ontology.QNAME_PROVO_activity);
		List<QName> entities = getSubjects(context,
				Ontology.QNAME_PROVO_qualifiedGeneration, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				ProvType.GENERATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(entities, activities, times);
		for (List<?> perm : perms)
		{
			WasGeneratedBy wgb = pFactory.newWasGeneratedBy(qname,
					(QName) perm.get(0), (QName) perm.get(1),
					(XMLGregorianCalendar) perm.get(2), attributes);
			store(context, wgb);
		}

	}
}
