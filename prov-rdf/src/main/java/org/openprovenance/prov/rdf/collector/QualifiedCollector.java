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
			for (QualifiedName qname : collator.keySet())
			{
				Types.ProvType[] types = getExplicitTypes(contextQ, qname);
				for (Types.ProvType type : types)
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
								onto.QNAME_PROVO_qualifiedDerivation);
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
			QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
			Value value = statement.getObject();

			if (value instanceof Literal)
			{
				if (predQ.equals(onto.QNAME_PROVO_atTime))
				{
					times.add((XMLGregorianCalendar) super
							.decodeLiteral((Literal) value));
				}
			}
		}
		return times;
	}

	private void createRevision(QualifiedName contextQ, QualifiedName qname)
	{
		List<WasDerivedFrom> wdfs = createDerivation(contextQ, qname,
				onto.QNAME_PROVO_qualifiedRevision);
		//QualifiedName q = pFactory.newQName("prov:Revision");
		QualifiedName q = onto.QNAME_PROVO_Revision;
		Type type=pFactory.newType(q, name.XSD_QNAME);
		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private void createQuotation(QualifiedName contextQ, QualifiedName qname)
	{
		List<WasDerivedFrom> wdfs = createDerivation(contextQ, qname,
				onto.QNAME_PROVO_qualifiedQuotation);
		//Object q = pFactory.newQName("prov:Quotation");
		QualifiedName q = onto.QNAME_PROVO_Quotation;
		Type type=pFactory.newType(q, name.XSD_QNAME);
		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private void createPrimarySource(QualifiedName context, QualifiedName qname)
	{
		List<WasDerivedFrom> wdfs = createDerivation(context, qname,
				onto.QNAME_PROVO_qualifiedPrimarySource);
		//Object q = pFactory.newQName("prov:PrimarySource");
		QualifiedName q = onto.QNAME_PROVO_PrimarySource;
		Type type=pFactory.newType(q, name.XSD_QNAME);

		for (WasDerivedFrom wdf : wdfs)
		{
			if (!wdf.getType().contains(type))
			{
				wdf.getType().add(type);
			}
		}
	}

	private List<WasDerivedFrom> createDerivation(QualifiedName context, QualifiedName qname,
			QualifiedName pred)
	{

		List<QualifiedName> entities = getObjects(context, qname,
				onto.QNAME_PROVO_entity);
		List<QualifiedName> activities = getObjects(context, qname,
				onto.QNAME_PROVO_hadActivity);
		List<QualifiedName> generations = getObjects(context, qname,
				onto.QNAME_PROVO_hadGeneration);
		List<QualifiedName> usages = getObjects(context, qname,
				onto.QNAME_PROVO_hadUsage);

		List<QualifiedName> generated = getSubjects(context, pred, qname);

		List<Attribute> attributes = collectAttributes(context, 
		                                               qname,
		                                               Types.ProvType.DERIVATION);

		qname = getQualQName(qname);

		List<WasDerivedFrom> wdfs = new ArrayList<WasDerivedFrom>();
		List<List<?>> perms = permute(generated, entities, activities,
				generations, usages);
		for (List<?> perm : perms)
		{
			WasDerivedFrom wdf = pFactory.newWasDerivedFrom(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(QualifiedName) perm.get(2), (QualifiedName) perm.get(3),
					(QualifiedName) perm.get(4), attributes);
			store(context, wdf);
			wdfs.add(wdf);
		}
		return wdfs;
	}

	private List<WasInfluencedBy> createInfluence(QualifiedName context, QualifiedName qname)
	{
		HashSet<QualifiedName> all_influencers = new HashSet<QualifiedName>();
		List<QualifiedName> influencers = getObjects(context, qname,
				onto.QNAME_PROVO_influencer);
		List<QualifiedName> agents = getObjects(context, qname,
				onto.QNAME_PROVO_agent);
		List<QualifiedName> entities = getObjects(context, qname,
				onto.QNAME_PROVO_entity);
		List<QualifiedName> activities = getObjects(context, qname,
				onto.QNAME_PROVO_activity);
		all_influencers.addAll(influencers);
		all_influencers.addAll(agents);
		all_influencers.addAll(entities);
		all_influencers.addAll(activities);
		List<QualifiedName> influencees = getSubjects(context,
				onto.QNAME_PROVO_qualifiedInfluence, qname);

		List<Attribute> attributes = collectAttributes(context, 
		                                               qname,
		                                               Types.ProvType.INFLUENCE);

		qname = getQualQName(qname);

		List<WasInfluencedBy> wibs = new ArrayList<WasInfluencedBy>();
		List<List<?>> perms = permute(influencees, new ArrayList<QualifiedName>(
				all_influencers));
		for (List<?> perm : perms)
		{
			WasInfluencedBy wib = pFactory.newWasInfluencedBy(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1), attributes);
			store(context, wib);
			wibs.add(wib);
		}
		return wibs;
	}

	private void createEnd(QualifiedName context, QualifiedName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QualifiedName> triggers = getObjects(context, qname,
				onto.QNAME_PROVO_entity);
		List<QualifiedName> enders = getObjects(context, qname,
				onto.QNAME_PROVO_hadActivity);
		List<QualifiedName> activities = getSubjects(context,
				onto.QNAME_PROVO_qualifiedEnd, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.END);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(activities, triggers, enders, times);
		for (List<?> perm : perms)
		{
			WasEndedBy web = pFactory.newWasEndedBy(qname, (QualifiedName) perm.get(0),
					(QualifiedName) perm.get(1), (QualifiedName) perm.get(2),
					(XMLGregorianCalendar) perm.get(3), attributes);
			store(context, web);
		}
	}

	private void createInsertion(QualifiedName context, QualifiedName qname)
	{
		List<QualifiedName> objectDictionaries = getObjects(context, qname,
				onto.QNAME_PROVO_dictionary);
		List<QualifiedName> pairs = getObjects(context, qname,
				onto.QNAME_PROVO_insertedKeyEntityPair);
		List<QualifiedName> subjectDictionaries = getSubjects(context,
				onto.QNAME_PROVO_qualifiedInsertion, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.INSERTION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(subjectDictionaries, objectDictionaries);
		for (List<?> perm : perms)
		{
			DerivedByInsertionFrom dbif = pFactory.newDerivedByInsertionFrom(qname, 
					(QualifiedName) perm.get(0),
					(QualifiedName) perm.get(1),
					createKeyEntityPairs(context, pairs), 
					attributes);
					
			store(context, dbif);
		}
	}

	
	private void createRemoval(QualifiedName context, QualifiedName qname)
	{
		List<QualifiedName> objectDictionaries = getObjects(context, qname,
				onto.QNAME_PROVO_dictionary);
		List<Value> keys = getDataObjects(context, 
		                                  qname,
		                                  onto.QNAME_PROVO_removedKey);
		List<QualifiedName> subjectDictionaries = getSubjects(context,
				onto.QNAME_PROVO_qualifiedRemoval, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.REMOVAL);

		qname = getQualQName(qname);
		
		List<Key> theKeys=new LinkedList<Key>();
		for (Value key: keys) {
		    theKeys.add(valueToKey(key));
		}

		List<List<?>> perms = permute(subjectDictionaries, objectDictionaries);
		for (List<?> perm : perms) {
		    DerivedByRemovalFrom dbif = pFactory.newDerivedByRemovalFrom(qname, 
		                                                                 (QualifiedName) perm.get(0),
					(QualifiedName) perm.get(1),
					theKeys,
					attributes);
					
			store(context, dbif);
		}
	}


	private void createStart(QualifiedName context, QualifiedName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QualifiedName> triggers = getObjects(context, qname,
				onto.QNAME_PROVO_entity);
		List<QualifiedName> starters = getObjects(context, qname,
				onto.QNAME_PROVO_hadActivity);
		List<QualifiedName> activities = getSubjects(context,
				onto.QNAME_PROVO_qualifiedStart, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.START);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(activities, triggers, starters, times);
		for (List<?> perm : perms)
		{
			WasStartedBy wsb = pFactory.newWasStartedBy(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(QualifiedName) perm.get(2), (XMLGregorianCalendar) perm.get(3),
					attributes);
			store(context, wsb);
		}
	}

	private void createInvalidation(QualifiedName context, QualifiedName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);

		List<QualifiedName> activities = getObjects(context, qname,
				onto.QNAME_PROVO_activity);
		List<QualifiedName> entities = getSubjects(context,
				onto.QNAME_PROVO_qualifiedInvalidation, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.INVALIDATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(entities, activities, times);
		for (List<?> perm : perms)
		{
			WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(XMLGregorianCalendar) perm.get(2), attributes);
			store(context, wib);
		}
	}

	private void createDelegation(QualifiedName context, QualifiedName qname)
	{
		List<QualifiedName> activities = getObjects(context, qname,
				onto.QNAME_PROVO_hadActivity);
		List<QualifiedName> agents = getObjects(context, qname,
				onto.QNAME_PROVO_agent);
		List<QualifiedName> subordinates = getSubjects(context,
				onto.QNAME_PROVO_qualifiedDelegation, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.DELEGATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(subordinates, agents, activities);
		for (List<?> perm : perms)
		{
			ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(QualifiedName) perm.get(2), attributes);
			store(context, aobo);
		}

	}

	private void createCommunication(QualifiedName context, QualifiedName qname)
	{
		List<QualifiedName> activities = getObjects(context, qname,
				onto.QNAME_PROVO_activity);
		List<QualifiedName> effects = getSubjects(context,
				onto.QNAME_PROVO_qualifiedCommunication, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.COMMUNICATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(effects, activities);
		for (List<?> perm : perms)
		{
			WasInformedBy wib = pFactory.newWasInformedBy(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1), attributes);
			store(context, wib);
		}
	}

	private void createAttribution(QualifiedName context, QualifiedName qname)
	{
		List<QualifiedName> agents = getObjects(context, qname,
				onto.QNAME_PROVO_agent);
		List<QualifiedName> entities = getSubjects(context,
				onto.QNAME_PROVO_qualifiedAttribution, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.ATTRIBUTION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(entities, agents);
		for (List<?> perm : perms)
		{
			WasAttributedTo wat = pFactory.newWasAttributedTo(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1), attributes);
			store(context, wat);
		}
	}

	private void createAssociation(QualifiedName context, QualifiedName qname)
	{
		List<QualifiedName> plans = getObjects(context, qname,
				onto.QNAME_PROVO_hadPlan);
		List<QualifiedName> agents = getObjects(context, qname,
				onto.QNAME_PROVO_agent);
		List<QualifiedName> activities = getSubjects(context,
				onto.QNAME_PROVO_qualifiedAssociation, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.ASSOCIATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(activities, agents, plans);
		for (List<?> perm : perms)
		{
			WasAssociatedWith waw = pFactory.newWasAssociatedWith(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(QualifiedName) perm.get(2), attributes);
			store(context, waw);
		}
	}

	private void createUsage(QualifiedName context, QualifiedName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QualifiedName> entities = getObjects(context, qname,
				onto.QNAME_PROVO_entity);
		List<QualifiedName> activities = getSubjects(context,
				onto.QNAME_PROVO_qualifiedUsage, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.USAGE);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(activities, entities, times);
		for (List<?> perm : perms)
		{
			Used used = pFactory.newUsed(qname, 
			                             (QualifiedName) perm.get(0),
			                             (QualifiedName) perm.get(1), 
			                             (XMLGregorianCalendar) perm.get(2),
					attributes);
			store(context, used);
		}

	}
	
	 

	private QualifiedName getQualQName(QualifiedName qname)
	{
		if (qname.getNamespaceURI() == "" || qname.getNamespaceURI().equals(BNODE_NS))
		{
			BNode bnode = new BNodeImpl(qname.getLocalPart());
			if (!isBNodeReferenced(bnode))
			{
				return null;
			}
			return pFactory.newQualifiedName(BNODE_NS, qname.getLocalPart(), "bnode");
		}
		return qname;
	}

	private void createGeneration(QualifiedName context, QualifiedName qname)
	{
		List<Statement> statements = collators.get(context).get(qname);
		List<XMLGregorianCalendar> times = getInstantaneousTimes(statements);
		List<QualifiedName> activities = getObjects(context, qname,
				onto.QNAME_PROVO_activity);
		List<QualifiedName> entities = getSubjects(context,
				onto.QNAME_PROVO_qualifiedGeneration, qname);

		List<Attribute> attributes = collectAttributes(context, qname,
				Types.ProvType.GENERATION);

		qname = getQualQName(qname);

		List<List<?>> perms = permute(entities, activities, times);
		for (List<?> perm : perms)
		{
			WasGeneratedBy wgb = pFactory.newWasGeneratedBy(qname,
					(QualifiedName) perm.get(0), (QualifiedName) perm.get(1),
					(XMLGregorianCalendar) perm.get(2), attributes);
			store(context, wgb);
		}

	}
}
