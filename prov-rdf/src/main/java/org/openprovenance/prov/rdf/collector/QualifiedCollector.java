package org.openprovenance.prov.rdf.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.rdf.Ontology;
import org.openprovenance.prov.rdf.collector.Types;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Key;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.Used;
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
    

	public QualifiedCollector(ProvFactory pFactory, Ontology onto)
	{
		super(pFactory,onto);
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

	

	private List<QualifiedName> getSubjects(QualifiedName context, QualifiedName pred, QualifiedName object)
	{
		HashMap<QualifiedName, List<Statement>> resources = collators.get(context);
		List<QualifiedName> subjects = new ArrayList<QualifiedName>();
		for (QualifiedName subject : resources.keySet())
		{
			List<Statement> statements = resources.get(subject);
			for (Statement statement : statements)
			{
				QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
				Value value = statement.getObject();
				if (pred.equals(predQ) && value instanceof Resource)
				{
					QualifiedName valQ = convertResourceToQualifiedName((Resource) value);
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
		for (QualifiedName contextQ : collators.keySet())
		{
			HashMap<QualifiedName, List<Statement>> collator = collators.get(contextQ);
			for (QualifiedName qualifiedName : collator.keySet())
			{
				Types.ProvType[] types = getExplicitTypes(contextQ, qualifiedName);
				for (Types.ProvType type : types)
				{
					switch (type)
					{
					case GENERATION:
						createGeneration(contextQ, qualifiedName);
						break;
					case USAGE:
						createUsage(contextQ, qualifiedName);
						break;
					case ASSOCIATION:
						createAssociation(contextQ, qualifiedName);
						break;
					case ATTRIBUTION:
						createAttribution(contextQ, qualifiedName);
						break;
					case COMMUNICATION:
						createCommunication(contextQ, qualifiedName);
						break;
					case DELEGATION:
						createDelegation(contextQ, qualifiedName);
						break;
					case DERIVATION:
						createDerivation(contextQ, qualifiedName,
								onto.QualifiedName_PROVO_qualifiedDerivation);
						break;
					case PRIMARYSOURCE:
						createPrimarySource(contextQ, qualifiedName);
						break;
					case QUOTATION:
						createQuotation(contextQ, qualifiedName);
						break;
					case REVISION:
						createRevision(contextQ, qualifiedName);
						break;
					case END:
						createEnd(contextQ, qualifiedName);
						break;
					case INVALIDATION:
						createInvalidation(contextQ, qualifiedName);
						break;
					case START:
						createStart(contextQ, qualifiedName);
						break;
					case INFLUENCE:
						createInfluence(contextQ, qualifiedName);
						break;
					case INSERTION:
						createInsertion(contextQ, qualifiedName);
						break;
					case REMOVAL:
						createRemoval(contextQ,qualifiedName);
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
			QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Literal)
			{
				if (predQ.equals(onto.QualifiedName_PROVO_atTime))
				{
					times.add((XMLGregorianCalendar) super
							.decodeLiteral((Literal) value));
				}
			}
		}
		return times;
	}

	private void createRevision(QualifiedName contextQ, QualifiedName qualifiedName)
	{
		List<WasDerivedFrom> wdfs = createDerivation(contextQ, qualifiedName,
				onto.QualifiedName_PROVO_qualifiedRevision);
		QualifiedName q = onto.QualifiedName_PROVO_Revision;
		Type type=pFactory.newType(q, name.PROV_QUALIFIED_NAME);
		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private void createQuotation(QualifiedName contextQ, QualifiedName qualifiedName)
	{
		List<WasDerivedFrom> wdfs = createDerivation(contextQ, qualifiedName,
				onto.QualifiedName_PROVO_qualifiedQuotation);
		QualifiedName q = onto.QualifiedName_PROVO_Quotation;
		Type type=pFactory.newType(q, name.PROV_QUALIFIED_NAME);
		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private void createPrimarySource(QualifiedName context, QualifiedName qualifiedName)
	{
		List<WasDerivedFrom> wdfs = createDerivation(context, qualifiedName,
				onto.QualifiedName_PROVO_qualifiedPrimarySource);
		QualifiedName q = onto.QualifiedName_PROVO_PrimarySource;
		Type type=pFactory.newType(q, name.PROV_QUALIFIED_NAME);

		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private List<WasDerivedFrom> createDerivation(QualifiedName context, QualifiedName qualifiedName,
			QualifiedName pred)
	{

		List<QualifiedName> entities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_entity);
		List<QualifiedName> activities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_hadActivity);
		List<QualifiedName> generations = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_hadGeneration);
		List<QualifiedName> usages = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_hadUsage);

		List<QualifiedName> generated = getSubjects(context, pred, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, 
		                                               qualifiedName,
		                                               Types.ProvType.DERIVATION);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<WasDerivedFrom> wdfs = new ArrayList<WasDerivedFrom>();
		List<List<?>> perms = permute(generated, entities, activities,
				generations, usages);
		for (List<?> perm : perms)
		{
			WasDerivedFrom wdf = pFactory.newWasDerivedFrom(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(QualifiedName) perm.get(2), (QualifiedName) perm.get(3),
					(QualifiedName) perm.get(4), attributes);
			store(context, wdf);
			wdfs.add(wdf);
		}
		return wdfs;
	}

	private List<WasInfluencedBy> createInfluence(QualifiedName context, QualifiedName qualifiedName)
	{
		HashSet<QualifiedName> all_influencers = new HashSet<QualifiedName>();
		List<QualifiedName> influencers = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_influencer);
		List<QualifiedName> agents = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_agent);
		List<QualifiedName> entities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_entity);
		List<QualifiedName> activities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_activity);
		all_influencers.addAll(influencers);
		all_influencers.addAll(agents);
		all_influencers.addAll(entities);
		all_influencers.addAll(activities);
		List<QualifiedName> influencees = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedInfluence, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, 
		                                               qualifiedName,
		                                               Types.ProvType.INFLUENCE);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<WasInfluencedBy> wibs = new ArrayList<WasInfluencedBy>();
		List<List<?>> perms = permute(influencees, new ArrayList<QualifiedName>(
				all_influencers));
		for (List<?> perm : perms)
		{
			WasInfluencedBy wib = pFactory.newWasInfluencedBy(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1), attributes);
			store(context, wib);
			wibs.add(wib);
		}
		return wibs;
	}

	private void createEnd(QualifiedName context, QualifiedName qualifiedName)
	{
		List<Statement> statements = collators.get(context).get(qualifiedName);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QualifiedName> triggers = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_entity);
		List<QualifiedName> enders = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_hadActivity);
		List<QualifiedName> activities = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedEnd, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.END);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(activities, triggers, enders, times);
		for (List<?> perm : perms)
		{
			WasEndedBy web = pFactory.newWasEndedBy(qualifiedName, (QualifiedName) perm.get(0),
					(QualifiedName) perm.get(1), (QualifiedName) perm.get(2),
					(XMLGregorianCalendar) perm.get(3), attributes);
			store(context, web);
		}
	}

	private void createInsertion(QualifiedName context, QualifiedName qualifiedName)
	{
		List<QualifiedName> objectDictionaries = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_dictionary);
		List<QualifiedName> pairs = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_insertedKeyEntityPair);
		List<QualifiedName> subjectDictionaries = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedInsertion, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.INSERTION);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(subjectDictionaries, objectDictionaries);
		for (List<?> perm : perms)
		{
			DerivedByInsertionFrom dbif = pFactory.newDerivedByInsertionFrom(qualifiedName, 
					(QualifiedName) perm.get(0),
					(QualifiedName) perm.get(1),
					createKeyEntityPairs(context, pairs), 
					attributes);
					
			store(context, dbif);
		}
	}

	
	private void createRemoval(QualifiedName context, QualifiedName qualifiedName)
	{
		List<QualifiedName> objectDictionaries = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_dictionary);
		List<Value> keys = getDataObjects(context, 
		                                  qualifiedName,
		                                  onto.QualifiedName_PROVO_removedKey);
		List<QualifiedName> subjectDictionaries = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedRemoval, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.REMOVAL);

		qualifiedName = getQualqualifiedName(qualifiedName);
		
		List<Key> theKeys=new LinkedList<Key>();
		for (Value key: keys) {
		    theKeys.add(valueToKey(key));
		}

		List<List<?>> perms = permute(subjectDictionaries, objectDictionaries);
		for (List<?> perm : perms) {
		    DerivedByRemovalFrom dbif = pFactory.newDerivedByRemovalFrom(qualifiedName, 
		                                                                 (QualifiedName) perm.get(0),
					(QualifiedName) perm.get(1),
					theKeys,
					attributes);
					
			store(context, dbif);
		}
	}


	private void createStart(QualifiedName context, QualifiedName qualifiedName)
	{
		List<Statement> statements = collators.get(context).get(qualifiedName);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QualifiedName> triggers = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_entity);
		List<QualifiedName> starters = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_hadActivity);
		List<QualifiedName> activities = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedStart, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.START);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(activities, triggers, starters, times);
		for (List<?> perm : perms)
		{
			WasStartedBy wsb = pFactory.newWasStartedBy(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(QualifiedName) perm.get(2), (XMLGregorianCalendar) perm.get(3),
					attributes);
			store(context, wsb);
		}
	}

	private void createInvalidation(QualifiedName context, QualifiedName qualifiedName)
	{
		List<Statement> statements = collators.get(context).get(qualifiedName);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);

		List<QualifiedName> activities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_activity);
		List<QualifiedName> entities = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedInvalidation, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.INVALIDATION);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(entities, activities, times);
		for (List<?> perm : perms)
		{
			WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(XMLGregorianCalendar) perm.get(2), attributes);
			store(context, wib);
		}
	}

	private void createDelegation(QualifiedName context, QualifiedName qualifiedName)
	{
		List<QualifiedName> activities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_hadActivity);
		List<QualifiedName> agents = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_agent);
		List<QualifiedName> subordinates = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedDelegation, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.DELEGATION);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(subordinates, agents, activities);
		for (List<?> perm : perms)
		{
			ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(QualifiedName) perm.get(2), attributes);
			store(context, aobo);
		}

	}

	private void createCommunication(QualifiedName context, QualifiedName qualifiedName)
	{
		List<QualifiedName> activities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_activity);
		List<QualifiedName> effects = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedCommunication, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.COMMUNICATION);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(effects, activities);
		for (List<?> perm : perms)
		{
			WasInformedBy wib = pFactory.newWasInformedBy(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1), attributes);
			store(context, wib);
		}
	}

	private void createAttribution(QualifiedName context, QualifiedName qualifiedName)
	{
		List<QualifiedName> agents = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_agent);
		List<QualifiedName> entities = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedAttribution, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.ATTRIBUTION);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(entities, agents);
		for (List<?> perm : perms)
		{
			WasAttributedTo wat = pFactory.newWasAttributedTo(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1), attributes);
			store(context, wat);
		}
	}

	private void createAssociation(QualifiedName context, QualifiedName qualifiedName)
	{
		List<QualifiedName> plans = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_hadPlan);
		List<QualifiedName> agents = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_agent);
		List<QualifiedName> activities = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedAssociation, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.ASSOCIATION);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(activities, agents, plans);
		for (List<?> perm : perms)
		{
			WasAssociatedWith waw = pFactory.newWasAssociatedWith(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(QualifiedName) perm.get(2), attributes);
			store(context, waw);
		}
	}

	private void createUsage(QualifiedName context, QualifiedName qualifiedName)
	{
		List<Statement> statements = collators.get(context).get(qualifiedName);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QualifiedName> entities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_entity);
		List<QualifiedName> activities = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedUsage, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.USAGE);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(activities, entities, times);
		for (List<?> perm : perms)
		{
			Used used = pFactory.newUsed(qualifiedName, 
			                             (QualifiedName) perm.get(0),
			                             (QualifiedName) perm.get(1), 
			                             (XMLGregorianCalendar) perm.get(2),
					attributes);
			store(context, used);
		}

	}
	
	 

	private QualifiedName getQualqualifiedName(QualifiedName qualifiedName)
	{
		if (qualifiedName.getNamespaceURI() == "" || qualifiedName.getNamespaceURI().equals(BNODE_NS))
		{
			BNode bnode = new BNodeImpl(qualifiedName.getLocalPart());
			if (!isBNodeReferenced(bnode))
			{
				return null;
			}
			return pFactory.newQualifiedName(BNODE_NS, qualifiedName.getLocalPart(), "bnode");
		}
		return qualifiedName;
	}

	private void createGeneration(QualifiedName context, QualifiedName qualifiedName)
	{
		List<Statement> statements = collators.get(context).get(qualifiedName);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QualifiedName> activities = getObjects(context, qualifiedName,
				onto.QualifiedName_PROVO_activity);
		List<QualifiedName> entities = getSubjects(context,
				onto.QualifiedName_PROVO_qualifiedGeneration, qualifiedName);

		List<Attribute> attributes = collectAttributes(context, qualifiedName,
				Types.ProvType.GENERATION);

		qualifiedName = getQualqualifiedName(qualifiedName);

		List<List<?>> perms = permute(entities, activities, times);
		for (List<?> perm : perms)
		{
			WasGeneratedBy wgb = pFactory.newWasGeneratedBy(qualifiedName,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(XMLGregorianCalendar) perm.get(2), attributes);
			store(context, wgb);
		}

	}
}
