package org.openprovenance.prov.rdf;

import java.util.List;
import java.util.Collection;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Entry;
import org.openprovenance.prov.model.Key;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.TypedValue;
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
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;

/**
 * A Converter to RDF
 */
public class RdfConstructor<RESOURCE, LITERAL, STATEMENT> implements
	ModelConstructor {

    private Namespace namespace ;

    public Namespace getNamespace() {
	return namespace;
    }
    
    public void setNamespace(Namespace ns) {
	namespace=ns;
    }

    final GraphBuilder<RESOURCE, LITERAL, STATEMENT> gb;
    final Ontology onto;
    final ProvFactory pFactory;
    final Name name;


    public RdfConstructor(GraphBuilder<RESOURCE, LITERAL, STATEMENT> gb, ProvFactory pFactory) {
	this.onto = new Ontology(pFactory);
	this.gb = gb;
	this.pFactory=pFactory;
	this.name=pFactory.getName();
    }

    @Override
    public Entity newEntity(QualifiedName id,
							Collection<Attribute> attributes) {
	assertType(id, onto.QNAME_PROVO_Entity);
	processAttributes(id, attributes);
	return null;
    }

    @Override
    public Activity newActivity(QualifiedName id,
							    XMLGregorianCalendar startTime,
							    XMLGregorianCalendar endTime,
							    Collection<Attribute> attributes) {
	assertType(id, onto.QNAME_PROVO_Activity);
	if (startTime != null) {
	    gb.assertStatement(gb.createDataProperty(id,
						     onto.QNAME_PROVO_startedAtTime,
						     newLiteral(startTime)));
	}
	if (endTime != null) {
	    gb.assertStatement(gb.createDataProperty(id,
						     onto.QNAME_PROVO_endedAtTime,
						     newLiteral(endTime)));
	}
	processAttributes(id, attributes);
	return null;
    }

    @Override
    public Agent newAgent(QualifiedName id,
						      Collection<Attribute> attributes) {
	assertType(id, onto.QNAME_PROVO_Agent);
	processAttributes(id, attributes);
	return null;
    }

    @Override
    public Used newUsed(QualifiedName id, 
                        QualifiedName activity, 
                        QualifiedName entity,
			XMLGregorianCalendar time,
			Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName u = addInfluence(id, activity, entity, time, (QualifiedName)null, false,
			       attributes, onto.QNAME_PROVO_Usage);

	return null;
    }

    @Override
    public WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity,
					    QualifiedName activity,
					    XMLGregorianCalendar time,
					    Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName g = addInfluence(id, entity, activity, time, null, false,
			       attributes, onto.QNAME_PROVO_Generation);

	return null;
    }

    @Override
    public WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, QualifiedName entity,
						QualifiedName activity,
						XMLGregorianCalendar time,
						Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName inv = addInfluence(id, entity, activity, time, null, false,
				 attributes, onto.QNAME_PROVO_Invalidation);

	return null;
    }

    @Override
    public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity,
					QualifiedName trigger, QualifiedName starter,
					XMLGregorianCalendar time,
					Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName s = addInfluence(id, activity, trigger, time, starter, false,
			       attributes, onto.QNAME_PROVO_Start);

	return null;
    }

    @Override
    public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger,
				    QualifiedName ender, XMLGregorianCalendar time,
				    Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName e = addInfluence(id, activity, trigger, time, ender, false,
			       attributes, onto.QNAME_PROVO_End);

	return null;

    }

    @Override
    public WasDerivedFrom newWasDerivedFrom(QualifiedName id,
					    QualifiedName entity2,
					    QualifiedName entity1,
					    QualifiedName activity,
					    QualifiedName generation,
					    QualifiedName usage,
					    Collection<org.openprovenance.prov.model.Attribute> attributes) {

	int knownSubtypes = 0;
	QualifiedName der = id;
	if (ProvUtilities.hasType(onto.QNAME_PROVO_Revision,
					     attributes)) {
	    knownSubtypes++;
	    der = addInfluence(der, entity2, entity1, null, activity, false,
			       attributes, onto.QNAME_PROVO_Revision);

	}
	if (ProvUtilities.hasType(onto.QNAME_PROVO_Quotation,
						       attributes)) {
	    knownSubtypes++;
	    der = addInfluence(der, entity2, entity1, null, activity, false,
			       attributes, onto.QNAME_PROVO_Quotation);

	}
	if (ProvUtilities.hasType(onto.QNAME_PROVO_PrimarySource,
						       attributes)) {
	    knownSubtypes++;
	    der = addInfluence(der, entity2, entity1, null, activity, false,
			       attributes, onto.QNAME_PROVO_PrimarySource);

	}

	if (knownSubtypes == 0) {
	    der = addInfluence(der, entity2, entity1, null, activity, false,
			       attributes, onto.QNAME_PROVO_Derivation);
	}

	if (der != null) { // FIXME: a scruffy derivation could just have
			   // generation and usage, but der==null (no qualified
			   // derivation found
	    // since generation and usage are not taken into account.
	    if (generation != null) {
		gb.assertStatement(gb.createObjectProperty(der,
							   onto.QNAME_PROVO_hadGeneration,
							   generation));
	    }
	    if (usage != null) {
		gb.assertStatement(gb.createObjectProperty(der,
							   onto.QNAME_PROVO_hadUsage,
							   usage));
	    }
	}

	return null;
    }

    @Override
    public WasAssociatedWith newWasAssociatedWith(QualifiedName id,
						  QualifiedName a,
						  QualifiedName ag,
						  QualifiedName plan,
						  Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName d = addInfluence(id, a, ag, null, plan, false, attributes,
			       onto.QNAME_PROVO_Association);

	return null;
    }

    @Override
    public WasAttributedTo newWasAttributedTo(QualifiedName id, QualifiedName e, QualifiedName ag,
					      Collection<Attribute> attributes) {
	@SuppressWarnings("unused")
	QualifiedName a = addInfluence(id, e, ag, null, null, false, attributes,
			       onto.QNAME_PROVO_Attribution);

	return null;
    }

    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, QualifiedName agent2,
					      QualifiedName agent1, QualifiedName a,
					      Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName d = addInfluence(id, agent2, agent1, null, a, false, attributes,
			       onto.QNAME_PROVO_Delegation);

	return null;
    }

    @Override
    public WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName activity2,
					  QualifiedName activity1,
					  Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName com = addInfluence(id, activity2, activity1, null, null, false,
				 attributes, onto.QNAME_PROVO_Communication);

	return null;
    }

    @Override
    public WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName qn2, QualifiedName qn1,
					      Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QualifiedName u = addInfluence(id, qn2, qn1, null, null, false, attributes,
			       onto.QNAME_PROVO_Influence);

	return null;
    }

    @Override
    public AlternateOf newAlternateOf(QualifiedName entity2, QualifiedName entity1) {

	if ((entity2 != null) && (entity1 != null))
	    gb.assertStatement(gb.createObjectProperty(entity2,
						       onto.QNAME_PROVO_alternateOf,
						       entity1));

	return null;
    }

    @Override
    public SpecializationOf newSpecializationOf(QualifiedName entity2, QualifiedName entity1) {

	if ((entity2 != null) && (entity1 != null))
	    gb.assertStatement(gb.createObjectProperty(entity2,
						       onto.QNAME_PROVO_specializationOf,
						       entity1));

	return null;
    }

    @Override
    public MentionOf newMentionOf(QualifiedName entity2, QualifiedName entity1, QualifiedName b) {

	if ((entity2 != null) && (entity1 != null))
	    gb.assertStatement(gb.createObjectProperty(entity2,
						       onto.QNAME_PROVO_mentionOf,
						       entity1));
	if ((entity2 != null) && (b != null))
	    gb.assertStatement(gb.createObjectProperty(entity2,
						       onto.QNAME_PROVO_asInBundle,
						       b));

	return null;
    }

    @Override
    public HadMember newHadMember(QualifiedName collection, Collection<QualifiedName> ll) {
	for (QualifiedName entity : ll) {
	    gb.assertStatement(gb.createObjectProperty(collection,
						       onto.QNAME_PROVO_hadMember,
						       entity));
	}
	return null;
    }

    @Override
    public Document newDocument(Namespace namespaces,
				Collection<Statement> statements,
				Collection<NamedBundle> bundles) {
	// At this stage nothing left to do
	return null;
    }

    @Override
    public NamedBundle newNamedBundle(QualifiedName id,
				      Namespace namespaces,
				      Collection<Statement> statements) {
	// At this stage nothing left to do
	return null;
    }

    @Override
    public void startDocument(Namespace namespaces) {
	if (namespaces != null) {
	    //namespace=namespaces; // namespace is expected to have been set outside RdfConstructor
	}
	gb.setContext();
    }

    @Override
    public void startBundle(QualifiedName bundleId, Namespace namespaces) {
	//System.out.println("$$$$$$$$$$$$ in startBundle");
	// TODO: bundle name does not seem to be interpreted according to the
	// prefix declared in bundle.
	if (bundleId != null) {
	    gb.setContext(gb.qualifiedNameToURI(bundleId));
	}
    }

    public void processAttributes(QualifiedName q, Collection<Attribute> attributes) {
	RESOURCE r = gb.qualifiedNameToResource(q);

	if (attributes != null)
	    for (Attribute attr : attributes) {

		LITERAL lit = null;

		QualifiedName type = attr.getType();
		QualifiedName pred = onto.convertToRdf(attr.getElementName()); // FIXME:
								       // convert
								       // to
								       // XSD_HASH

		String value;
		/*
		if (!(type.equals(ValueConverter.QNAME_XSD_QNAME))
			&& onto.asObjectProperty.contains(pred)) {
		    System.out.println(" TODO $$$$$$$$$$$$$$$$$$$$$$ " + pred + " is object property, but range is " + type);
		    // TODO
		}
		*/

		if (attr.getValue() instanceof LangString) {
		    LangString iString = (LangString) attr.getValue();
		    value = iString.getValue();
		    lit = gb.newLiteral(value, iString.getLang());
		    gb.assertStatement(gb.createDataProperty(r, pred, lit));
		} /* else if (attr.getValue() instanceof QName) {
		    QName qn = (QName) attr.getValue();
		    String qnAsString;
		    if ((qn.getPrefix() == null) || (qn.getPrefix().equals(""))) {
			qnAsString = qn.getLocalPart();
		    } else {
			qnAsString = qn.getPrefix() + ":" + qn.getLocalPart();
		    }
		    if (false) { // That's here the code to generate resource or
				 // literal.
			lit = gb.newLiteral(qnAsString, type);
			gb.assertStatement(gb.createDataProperty(r, pred, lit));
		    } else {
			gb.assertStatement(gb.createObjectProperty(r, pred, pFactory.newQualifiedName(qn)));
		    }

		} */ else if (attr.getValue() instanceof QualifiedName) {
		    QualifiedName qn = (QualifiedName) attr.getValue();
		    String qnAsString;
		    if ((qn.getPrefix() == null) || (qn.getPrefix().equals(""))) {
			qnAsString = qn.getLocalPart();
		    } else {
			qnAsString = qn.getPrefix() + ":" + qn.getLocalPart();
		    }
		    if (false) { // That's here the code to generate resource or
				 // literal.
			lit = gb.newLiteral(qnAsString, type);
			gb.assertStatement(gb.createDataProperty(r, pred, lit));
		    } else {
			gb.assertStatement(gb.createObjectProperty(r, pred, qn));
		    }

		} else {
		    value = attr.getValue().toString();
		    lit = gb.newLiteral(value, type);
		    gb.assertStatement(gb.createDataProperty(r, pred, lit));
		}

	    }
    }


    public QualifiedName addInfluence(QualifiedName infl, QualifiedName subject, QualifiedName object,
        			      XMLGregorianCalendar time, QualifiedName other,
        			      boolean someOther,
        			      Collection<Attribute> attributes,
        			      QualifiedName qualifiedClass) {
	if ((infl != null) || (time != null) || (other != null) || someOther
		|| ((attributes != null) && !(attributes.isEmpty()))) {
	    infl = assertType(infl, qualifiedClass);
	    if (object != null)
		assertInfluencer(infl, object, qualifiedClass);
	    if (subject != null) // scruffy provenance: subject may not be
				 // defined
		assertQualifiedInfluence(subject, infl, qualifiedClass);
	    if (time != null)
		assertAtTime(infl, time);
	    if (other != null)
		asserterOther(infl, other, qualifiedClass);
	    processAttributes(infl, attributes);
	}

	if ((binaryProp(infl, subject)) && (object != null))
	    gb.assertStatement(gb.createObjectProperty(subject,
						       onto.unqualifiedTable.get(qualifiedClass),
						       object));

	return infl;
    }

    public QualifiedName addInfluenceDELETEME(QualifiedName infl, QualifiedName subject, QualifiedName object,
			      XMLGregorianCalendar time, QualifiedName other,
			      boolean someOther,
			      Collection<Attribute> attributes,
			      QualifiedName qualifiedClass) {
	if ((infl != null) || (time != null) || (other != null) || someOther
		|| ((attributes != null) && !(attributes.isEmpty()))) {
	    infl = assertType(infl, qualifiedClass);
	    if (object != null)
		assertInfluencer(infl, object, qualifiedClass);
	    if (subject != null) // scruffy provenance: subject may not be
				 // defined
		assertQualifiedInfluence(subject, infl, qualifiedClass);
	    if (time != null)
		assertAtTime(infl, time);
	    if (other != null)
		asserterOther(infl, other, qualifiedClass);
	    processAttributes(infl, attributes);
	}

	if ((binaryProp(infl, subject)) && (object != null))
	    gb.assertStatement(gb.createObjectProperty(subject,
						       onto.unqualifiedTable.get(qualifiedClass),
						       object));

	return infl;
    }

    public void asserterOther(QualifiedName subject, QualifiedName other, QualifiedName qualifiedClass) {
	gb.assertStatement(gb.createObjectProperty(subject,
						   onto.otherTable.get(qualifiedClass),
						   other));
    }

    public void assertAtTime(QualifiedName subject, XMLGregorianCalendar time) {
	gb.assertStatement(gb.createDataProperty(subject,
						 onto.QNAME_PROVO_atTime,
						 newLiteral(time)));

    }

    private LITERAL newLiteral(XMLGregorianCalendar time) {
	return gb.newLiteral(time.toString(),
			     name.QNAME_XSD_HASH_DATETIME);
    }

    public void assertQualifiedInfluence(QualifiedName subject, 
                                         QualifiedName infl,
					 QualifiedName qualifiedClass) {
	gb.assertStatement(gb.createObjectProperty(subject,
						   onto.qualifiedInfluenceTable.get(qualifiedClass),
						   infl));
    }
    
    public void assertInfluencer(QualifiedName infl, QualifiedName object, QualifiedName qualifiedClass) {
 	gb.assertStatement(gb.createObjectProperty(infl,
 						   onto.influencerTable.get(qualifiedClass),
 						   object));
     }

    public QualifiedName assertType(QualifiedName infl, QualifiedName qualifiedClass) {
	if (infl == null) {
	    infl = gb.newBlankName();
	}
	gb.assertStatement(gb.createObjectProperty(infl,
						   onto.QNAME_RDF_TYPE,
						   qualifiedClass));
	return infl;
    }

    public Object convertExtension(Object name, Object id, Object args,
				   Object dAttrs) {
	return null;
    }

    /* Component 5 */

    public Object convertInsertion(Object id, Object id2, Object id1,
				   Object map, Object dAttrs) {
	// todo
	throw new UnsupportedOperationException();
    }

    public Object convertEntry(Object o1, Object o2) {
	// todo
	throw new UnsupportedOperationException();
    }

    public Object convertKeyEntitySet(List<Object> o) {
	// todo
	throw new UnsupportedOperationException();
    }

    public Object convertRemoval(Object id, Object id2, Object id1,
				 Object keys, Object dAttrs) {
	// todo
	throw new UnsupportedOperationException();
    }

    public Object convertDictionaryMemberOf(Object id, Object id2, Object map,
					    Object complete, Object dAttrs) {
	// todo
	throw new UnsupportedOperationException();
    }

    public Object convertCollectionMemberOf(Object id, Object id2, Object map,
					    Object complete, Object dAttrs) {
	// todo
	throw new UnsupportedOperationException();
    }

    public Object convertKeys(List<Object> keys) {
	// todo
	throw new UnsupportedOperationException();
    }

    public boolean binaryProp(Object id, Object subject) {
	return id == null && subject != null;
    }

    @Override
    public DerivedByInsertionFrom newDerivedByInsertionFrom(QualifiedName id,
							    QualifiedName after,
							    QualifiedName before,
							    List<Entry> keyEntitySet,
							    Collection<Attribute> attributes) {
	QualifiedName der = addInfluence(id, after, before, null, null, true,
				 attributes, onto.QNAME_PROVO_Insertion);
	for (Entry p : keyEntitySet) {
	    QualifiedName thePair = gb.newBlankName();
	    gb.assertStatement(gb.createObjectProperty(der,
						       onto.QNAME_PROVO_insertedKeyEntityPair,
						       thePair));


	    LITERAL lit = valueToLiteral(p.getKey());

	    gb.assertStatement(gb.createDataProperty(thePair,
						     onto.QNAME_PROVO_pairKey,
						     lit));
	    gb.assertStatement(gb.createObjectProperty(thePair,
						       onto.QNAME_PROVO_pairEntity,
						       p.getEntity()));

	}

	return null;
    }
    private LITERAL valueToLiteral(TypedValue val) {
 	LITERAL lit = null;
 	String value;
 	if (val.getValue() instanceof QualifiedName) {
 	    value = Namespace.qualifiedNameToStringWithNamespace((QualifiedName) val.getValue());
 	 	lit = gb.newLiteral(value, val.getType());

 	} else if (val.getValue() instanceof LangString) {
 	    LangString iString=(LangString) val.getValue();
	    lit = gb.newLiteral(iString.getValue(), iString.getLang());

 	} else {
 	    value = val.getValue().toString();
 	    lit = gb.newLiteral(value, val.getType());

 	}
 	return lit;
     }


    @Override
    public DerivedByRemovalFrom newDerivedByRemovalFrom(QualifiedName id,
							QualifiedName after,
							QualifiedName before,
							List<Key> keys,
							Collection<Attribute> attributes) {
	QualifiedName der = addInfluence(id, after, before, null, null, true,
				 attributes, onto.QNAME_PROVO_Removal);
	for (Key k : keys) {

	    LITERAL lit = valueToLiteral(k);

	    gb.assertStatement(gb.createDataProperty(der,
						     onto.QNAME_PROVO_removedKey,
						     lit));

	}

	return null;

    }

    @Override
    public DictionaryMembership newDictionaryMembership(QualifiedName dict,
							List<Entry> keyEntitySet) {
	for (Entry p : keyEntitySet) {
	    QualifiedName thePair = gb.newBlankName();
	    gb.assertStatement(gb.createObjectProperty(dict,
						       onto.QNAME_PROVO_hadDictionaryMember,
						       thePair));
	    LITERAL lit = valueToLiteral(p.getKey());

	    gb.assertStatement(gb.createDataProperty(thePair,
						     onto.QNAME_PROVO_pairKey,
						     lit));
	    gb.assertStatement(gb.createObjectProperty(thePair,
						       onto.QNAME_PROVO_pairEntity,
						       p.getEntity()));

	}
	return null;

    }

    @Override
    public QualifiedName newQualifiedName(String namespace, String local,
					  String prefix) {
	// TODO Auto-generated method stub
	return null;
    }

}
