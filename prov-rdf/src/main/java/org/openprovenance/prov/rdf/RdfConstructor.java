package org.openprovenance.prov.rdf;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Collection;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.ModelConstructor;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.Statement;
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
import org.openrdf.elmo.ElmoManager;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;

/**
 * A Converter to RDF
 */
public class RdfConstructor implements ModelConstructor {

    private Hashtable<String, String> namespaceTable = new Hashtable<String, String>();

    public Hashtable<String, String> getNamespaceTable() {
	return namespaceTable;
    }

    final GraphBuilder gb;
    final Ontology onto;

    public RdfConstructor(ElmoManager manager) { // manager should be passed to
						 // graph builder and not this
						 // constructor
	this.onto = new Ontology();
	this.gb = new GraphBuilder(manager);
    }

    @Override
    public org.openprovenance.prov.xml.Entity newEntity(QName id,
							Collection<Attribute> attributes) {
	assertType(id, Ontology.QNAME_PROVO_Entity);
	processAttributes(id, attributes);
	return null;
    }

    @Override
    public org.openprovenance.prov.xml.Activity newActivity(QName id,
							    XMLGregorianCalendar startTime,
							    XMLGregorianCalendar endTime,
							    Collection<Attribute> attributes) {
	assertType(id, Ontology.QNAME_PROVO_Activity);
	if (startTime != null) {
	    gb.assertStatement(gb.createDataProperty(id,
						     Ontology.QNAME_PROVO_startedAtTime,
						     newLiteral(startTime)));
	}
	if (endTime != null) {
	    gb.assertStatement(gb.createDataProperty(id,
						     Ontology.QNAME_PROVO_endedAtTime,
						     newLiteral(endTime)));
	}
	processAttributes(id, attributes);
	return null;
    }

    @Override
    public org.openprovenance.prov.xml.Agent newAgent(QName id,
						      Collection<Attribute> attributes) {
	assertType(id, Ontology.QNAME_PROVO_Agent);
	processAttributes(id, attributes);
	return null;
    }

    @Override
    public Used newUsed(QName id, QName activity, QName entity,
			XMLGregorianCalendar time,
			Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName u = addInfluence(id, activity, entity, time, null, attributes,
			       Ontology.QNAME_PROVO_Usage);

	return null;
    }

    @Override
    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity,
					    QName activity,
					    XMLGregorianCalendar time,
					    Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName g = addInfluence(id, entity, activity, time, null, attributes,
			       Ontology.QNAME_PROVO_Generation);

	return null;
    }

    @Override
    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity,
						QName activity,
						XMLGregorianCalendar time,
						Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName inv = addInfluence(id, entity, activity, time, null, attributes,
				 Ontology.QNAME_PROVO_Invalidation);

	return null;
    }

    @Override
    public WasStartedBy newWasStartedBy(QName id, QName activity,
					QName trigger, QName starter,
					XMLGregorianCalendar time,
					Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName s = addInfluence(id, activity, trigger, time, starter,
			       attributes, Ontology.QNAME_PROVO_Start);

	return null;
    }

    @Override
    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger,
				    QName ender, XMLGregorianCalendar time,
				    Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName e = addInfluence(id, activity, trigger, time, ender, attributes,
			       Ontology.QNAME_PROVO_End);

	return null;

    }

    @Override
    public WasDerivedFrom newWasDerivedFrom(QName id, QName entity2,
					    QName entity1, QName activity,
					    QName generation, QName usage,
					    Collection<Attribute> attributes) {

	int knownSubtypes = 0;
	QName der = id;
	if (Attribute.hasType(Ontology.QNAME_PROVO_Revision, attributes)) {
	    knownSubtypes++;
	    der = addInfluence(der, entity2, entity1, null, activity,
			       attributes, Ontology.QNAME_PROVO_Revision);

	}
	if (Attribute.hasType(Ontology.QNAME_PROVO_Quotation, attributes)) {
	    knownSubtypes++;
	    der = addInfluence(der, entity2, entity1, null, activity,
			       attributes, Ontology.QNAME_PROVO_Quotation);

	}
	if (Attribute.hasType(Ontology.QNAME_PROVO_PrimarySource, attributes)) {
	    knownSubtypes++;
	    der = addInfluence(der, entity2, entity1, null, activity,
			       attributes, Ontology.QNAME_PROVO_PrimarySource);

	}

	if (knownSubtypes == 0) {
	    der = addInfluence(der, entity2, entity1, null, activity,
			       attributes, Ontology.QNAME_PROVO_Derivation);
	}

	if (der != null) { // FIXME: a scruffy derivation could just have
			   // generation and usage, but der==null (no qualified
			   // derivation found
	    // since generation and usage are not taken into account.
	    if (generation != null) {
		gb.assertStatement(gb.createObjectProperty(der,
							   Ontology.QNAME_PROVO_hadGeneration,
							   generation));
	    }
	    if (usage != null) {
		gb.assertStatement(gb.createObjectProperty(der,
							   Ontology.QNAME_PROVO_hadUsage,
							   usage));
	    }
	}

	return null;
    }

    @Override
    public WasAssociatedWith newWasAssociatedWith(QName id,
						  QName a,
						  QName ag,
						  QName plan,
						  Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName d = addInfluence(id, a, ag, null, plan, attributes,
			       Ontology.QNAME_PROVO_Association);

	return null;
    }

    @Override
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,
					      Collection<Attribute> attributes) {
	@SuppressWarnings("unused")
	QName a = addInfluence(id, e, ag, null, null, attributes,
			       Ontology.QNAME_PROVO_Attribution);

	return null;
    }

    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName agent2,
					      QName agent1, QName a,
					      Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName d = addInfluence(id, agent2, agent1, null, a, attributes,
			       Ontology.QNAME_PROVO_Delegation);

	return null;
    }

    @Override
    public WasInformedBy newWasInformedBy(QName id, QName activity2,
					  QName activity1,
					  Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName com = addInfluence(id, activity2, activity1, null, null,
				 attributes, Ontology.QNAME_PROVO_Communication);

	return null;
    }

    @Override
    public WasInfluencedBy newWasInfluencedBy(QName id, QName qn2, QName qn1,
					      Collection<Attribute> attributes) {

	@SuppressWarnings("unused")
	QName u = addInfluence(id, qn2, qn1, null, null, attributes,
			       Ontology.QNAME_PROVO_Influence);

	return null;
    }

    @Override
    public AlternateOf newAlternateOf(QName entity2, QName entity1) {

	if ((entity2 != null) && (entity1 != null))
	    gb.assertStatement(gb.createObjectProperty(entity2,
						       Ontology.QNAME_PROVO_alternateOf,
						       entity1));

	return null;
    }

    @Override
    public SpecializationOf newSpecializationOf(QName entity2, QName entity1) {

	if ((entity2 != null) && (entity1 != null))
	    gb.assertStatement(gb.createObjectProperty(entity2,
						       Ontology.QNAME_PROVO_specializationOf,
						       entity1));

	return null;
    }

    @Override
    public MentionOf newMentionOf(QName entity2, QName entity1, QName b) {

	if ((entity2 != null) && (entity1 != null))
	    gb.assertStatement(gb.createObjectProperty(entity2,
						       Ontology.QNAME_PROVO_mentionOf,
						       entity1));
	if ((entity2 != null) && (b != null))
	    gb.assertStatement(gb.createObjectProperty(entity2,
						       Ontology.QNAME_PROVO_asInBundle,
						       b));

	return null;
    }

    @Override
    public HadMember newHadMember(QName collection, Collection<QName> ll) {
	for (QName entity : ll) {
	    gb.assertStatement(gb.createObjectProperty(collection,
						       Ontology.QNAME_PROVO_hadMember,
						       entity));
	}
	return null;
    }

    @Override
    public Document newDocument(Hashtable<String, String> namespaces,
				Collection<Statement> statements,
				Collection<NamedBundle> bundles) {
	// At this stage nothing left to do
	return null;
    }

    @Override
    public NamedBundle newNamedBundle(QName id,
				      Hashtable<String, String> namespaces,
				      Collection<Statement> statements) {
	// At this stage nothing left to do
	return null;
    }

    @Override
    public void startDocument(Hashtable<String, String> namespaces) {
	if (namespaces != null) {
	    getNamespaceTable().putAll(namespaces);
	}
	gb.setContext();
    }

    @Override
    public void startBundle(QName bundleId, Hashtable<String, String> namespaces) {
	System.out.println("$$$$$$$$$$$$ in startBundle");
	// TODO: bundle name does not seem to be interpreted according to the
	// prefix declared in bundle.
	URIImpl uri = gb.qnameToURI(bundleId);
	contexts.add(uri);
	if (bundleId != null) {
	    gb.setContext(uri);
	}
    }

    public List<Resource> contexts = new LinkedList<Resource>();

    public void processAttributes(QName q, Collection<Attribute> attributes) {
	org.openrdf.model.Resource r = gb.qnameToResource(q);

	for (Attribute attr : attributes) {

	    LiteralImpl literalImpl = null;

	    QName type = attr.getXsdType();
	    QName pred = onto.convertToRdf(attr.getElementName()); // FIXME: convert to XSD_HASH

	    String value;
	    if (attr.getValue() instanceof InternationalizedString) {
		InternationalizedString iString = (InternationalizedString) attr.getValue();
		value = iString.getValue();
		literalImpl = new LiteralImpl(value, iString.getLang());
		gb.assertStatement(gb.createDataProperty(r, pred, literalImpl));
	    } else if (attr.getValue() instanceof QName) {
		QName qn = (QName) attr.getValue();
		String qnAsString;
		if ((qn.getPrefix() == null) || (qn.getPrefix().equals(""))) {
		    qnAsString = qn.getLocalPart();
		} else {
		    qnAsString = qn.getPrefix() + ":" + qn.getLocalPart();
		}
		if (false) { // That's here the code to generate resource or
			    // literal.
		    literalImpl = new LiteralImpl(qnAsString,
						  gb.qnameToURI(type));
		    gb.assertStatement(gb.createDataProperty(r, pred,
							     literalImpl));
		} else {
		    gb.assertStatement(gb.createObjectProperty(r, pred, qn));
		}

	    } else {
		value = attr.getValue().toString();
		literalImpl = new LiteralImpl(value, gb.qnameToURI(type));
		gb.assertStatement(gb.createDataProperty(r, pred, literalImpl));
	    }

	}
    }

    public QName addInfluence(QName infl, QName subject, QName object,
			      XMLGregorianCalendar time, QName other,
			      Collection<Attribute> attributes,
			      QName qualifiedClass) {
	if ((infl != null) || (time != null) || (other != null)
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

    public void asserterOther(QName subject, QName other, QName qualifiedClass) {
	gb.assertStatement(gb.createObjectProperty(subject,
						   onto.otherTable.get(qualifiedClass),
						   other));
    }

    public void assertAtTime(QName subject, XMLGregorianCalendar time) {
	gb.assertStatement(gb.createDataProperty(subject,
						 Ontology.QNAME_PROVO_atTime,
						 newLiteral(time)));

    }

    private LiteralImpl newLiteral(XMLGregorianCalendar time) {
	return new LiteralImpl(
			       time.toString(),
			       gb.qnameToURI(ValueConverter.QNAME_XSD_HASH_DATETIME));
    }

    public void assertQualifiedInfluence(QName subject, QName infl,
					 QName qualifiedClass) {
	gb.assertStatement(gb.createObjectProperty(subject,
						   onto.qualifiedInfluenceTable.get(qualifiedClass),
						   infl));
    }

    public void assertInfluencer(QName infl, QName object, QName qualifiedClass) {
	gb.assertStatement(gb.createObjectProperty(infl,
						   onto.influencerTable.get(qualifiedClass),
						   object));
    }

    public QName assertType(QName infl, QName qualifiedClass) {
	if (infl == null) {
	    infl = gb.newBlankName();
	}
	gb.assertStatement(gb.createObjectProperty(infl,
						   Ontology.QNAME_RDF_TYPE,
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

}
