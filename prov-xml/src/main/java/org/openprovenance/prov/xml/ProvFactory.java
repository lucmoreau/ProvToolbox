package org.openprovenance.prov.xml;

import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.util.Date;
import java.net.URI;
import javax.xml.bind.JAXBElement;
import java.util.GregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;

import org.openprovenance.prov.xml.collection.CollectionMemberOf;
import org.openprovenance.prov.xml.collection.DerivedByInsertionFrom;
import org.openprovenance.prov.xml.collection.DerivedByRemovalFrom;
import org.openprovenance.prov.xml.collection.DictionaryMemberOf;
import org.openprovenance.prov.xml.collection.Entry;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** A stateless factory for PROV objects. */

public class ProvFactory implements ModelConstructor {

    static public DocumentBuilder builder;

    public static final String DEFAULT_NS = "_";

    private final static ProvFactory oFactory = new ProvFactory();

    public static final String packageList = "org.openprovenance.prov.xml:org.openprovenance.prov.xml.collection:org.openprovenance.prov.xml.validation";

    static {
	initBuilder();
    }

    public static ProvFactory getFactory() {
	return oFactory;
    }

    static void initBuilder() {
	try {
	    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	    docBuilderFactory.setNamespaceAware(true);
	    builder = docBuilderFactory.newDocumentBuilder();
	} catch (ParserConfigurationException ex) {
	    throw new RuntimeException(ex);
	}
    }

    public static String printURI(java.net.URI u) {
	return u.toString();
    }

    final protected org.openprovenance.prov.xml.collection.ObjectFactory cof;

    protected DatatypeFactory dataFactory;
    /** Note, this method now makes it stateful :-( */
    private Hashtable<String, String> namespaces = null;
    protected ObjectFactory of;

    final protected org.openprovenance.prov.xml.validation.ObjectFactory vof;

    public ProvFactory() {
	of = new ObjectFactory();
	vof = new org.openprovenance.prov.xml.validation.ObjectFactory();
	cof = new org.openprovenance.prov.xml.collection.ObjectFactory();
	init();
	setNamespaces(new Hashtable<String, String>());
    }

    public ProvFactory(Hashtable<String, String> namespaces) {
	of = new ObjectFactory();
	vof = new org.openprovenance.prov.xml.validation.ObjectFactory();
	cof = new org.openprovenance.prov.xml.collection.ObjectFactory();
	this.namespaces = namespaces;
	init();
    }

    public ProvFactory(ObjectFactory of) {
	this.of = of;
	vof = new org.openprovenance.prov.xml.validation.ObjectFactory();
	cof = new org.openprovenance.prov.xml.collection.ObjectFactory();
	init();
	setNamespaces(new Hashtable<String, String>());
    }

    public void addAttribute(HasExtensibility a, Attribute o) {
	a.getAny().add(o);
    }

    public void addAttribute(HasExtensibility a, String namespace,
			     String localName, String prefix, Object value, ValueConverter vconv) {

	a.getAny().add(newAttribute(namespace, localName, prefix, value, vconv));
    }

    public void addAttribute(HasExtensibility a, String namespace,
			     String localName, String prefix, Object value,
			     String type) {

	a.getAny().add(newAttribute(namespace, localName, prefix, value, type));
    }

    public ActedOnBehalfOf addAttributes(ActedOnBehalfOf from,
					 ActedOnBehalfOf to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public Activity addAttributes(Activity from, Activity to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public Agent addAttributes(Agent from, Agent to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	// to.getLocation().addAll(from.getLocation());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public Entity addAttributes(Entity from, Entity to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public Used addAttributes(Used from, Used to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasAssociatedWith addAttributes(WasAssociatedWith from,
					   WasAssociatedWith to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getRole().addAll(from.getRole());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasAttributedTo addAttributes(WasAttributedTo from,
					 WasAttributedTo to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasDerivedFrom addAttributes(WasDerivedFrom from, WasDerivedFrom to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasEndedBy addAttributes(WasEndedBy from, WasEndedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasGeneratedBy addAttributes(WasGeneratedBy from, WasGeneratedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasInfluencedBy addAttributes(WasInfluencedBy from,
					 WasInfluencedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasInformedBy addAttributes(WasInformedBy from, WasInformedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasInvalidatedBy addAttributes(WasInvalidatedBy from,
					  WasInvalidatedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public WasStartedBy addAttributes(WasStartedBy from, WasStartedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getAny().addAll(from.getAny());
	return to;
    }

    public void addLabel(HasLabel a, String label) {
	a.getLabel().add(newInternationalizedString(label));
    }

    public void addLabel(HasLabel a, String label, String language) {
	a.getLabel().add(newInternationalizedString(label, language));
    }

    public void addPrimarySourceType(HasType a) {
	a.getType().add(newQName("prov:PrimarySource"));
    }

    public void addQuotationType(HasType a) {
	a.getType().add(newQName("prov:Quotation"));
    }

    public void addRevisionType(HasType a) {
	a.getType().add(newQName("prov:Revision"));
    }

 
    public void addRole(HasRole a, Object role) {
	if (role != null) {
	    a.getRole().add(role);
	}
    }

    public void addType(HasType a, Object type) {

	a.getType().add(type);
    }

    public void addType(HasType a, URI type) {
	URIWrapper u = new URIWrapper();
	u.setValue(type);
	a.getType().add(u);
    }

 
 
    public org.openprovenance.prov.xml.collection.ObjectFactory getCollectionObjectFactory() {
	return cof;
    }

    /* Return the first label, it it exists */
    public String getLabel(HasExtensibility e) {

	List<InternationalizedString> labels = ((HasLabel) e).getLabel();
	if ((labels == null) || (labels.isEmpty()))
	    return null;
	if (e instanceof HasLabel)
	    return labels.get(0).getValue();
	return "pFact: label TODO";
    }

    public String getNamespace(String prefix) {
	if ((prefix == null) || ("".equals(prefix)))
	    return namespaces.get(DEFAULT_NS);
	if (prefix.equals(NamespacePrefixMapper.PROV_PREFIX))
	    return NamespacePrefixMapper.PROV_NS;
	if (prefix.equals(NamespacePrefixMapper.XSD_PREFIX))
	    return NamespacePrefixMapper.XSD_NS;
	return namespaces.get(prefix);
    }

    public Hashtable<String, String> getNss() {
	return namespaces;
    }

    public ObjectFactory getObjectFactory() {
	return of;
    }

    public String getPackageList() {
	return packageList;
    }

    public String getRole(HasExtensibility e) {
	return "pFact: role TODO";
    }

    public List<Object> getType(HasExtensibility e) {
	if (e instanceof HasType)
	    return ((HasType) e).getType();
	List<Object> res = new LinkedList<Object>();
	res.add("pFact: type TODO");
	return res;
    }

    public org.openprovenance.prov.xml.validation.ObjectFactory getValidationObjectFactory() {
	return vof;
    }

    void init() {
	try {
	    dataFactory = DatatypeFactory.newInstance();
	} catch (DatatypeConfigurationException ex) {
	    throw new RuntimeException(ex);
	}
    }

    public ActedOnBehalfOf newActedOnBehalfOf(ActedOnBehalfOf u) {
	ActedOnBehalfOf u1 = newActedOnBehalfOf(u.getId(), u.getSubordinate(),
						u.getResponsible(),
						u.getActivity());
	u1.getAny().addAll(u.getAny());
	return u1;
    }

    public ActedOnBehalfOf newActedOnBehalfOf(QName id, AgentRef subordinate,
					      AgentRef responsible,
					      ActivityRef eid2) {
	ActedOnBehalfOf res = of.createActedOnBehalfOf();
	res.setId(id);
	res.setActivity(eid2);
	res.setSubordinate(subordinate);
	res.setResponsible(responsible);
	return res;
    }

    public ActedOnBehalfOf newActedOnBehalfOf(String id, AgentRef subordinate,
					      AgentRef responsible,
					      ActivityRef eid2) {
	ActedOnBehalfOf res = of.createActedOnBehalfOf();
	res.setId(stringToQName(id));
	res.setActivity(eid2);
	res.setSubordinate(subordinate);
	res.setResponsible(responsible);
	return res;
    }
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName ag2, QName ag1, QName a, List<Attribute> attributes) {
        AgentRef agid2=(ag2==null)? null : newAgentRef(ag2);
        AgentRef agid1=(ag1==null)? null : newAgentRef(ag1);
        ActivityRef aid=(a==null)? null : newActivityRef(a);
        ActedOnBehalfOf res=newActedOnBehalfOf(id, agid2, agid1, aid);
        setAttributes(res, attributes);
        return res;
    }

    
    
    public Activity newActivity(Activity a) {
	Activity res = newActivity(a.getId());
	res.getType().addAll(a.getType());
	res.getLabel().addAll(a.getLabel());
	res.getLocation().addAll(a.getLocation());
	res.setStartTime(a.getStartTime());
	res.setEndTime(a.getEndTime());
	return res;
    }

    public Activity newActivity(QName a) {
	Activity res = of.createActivity();
	res.setId(a);
	return res;
    }

    public Activity newActivity(QName q, String label) {
	Activity res = newActivity(q);
	if (label != null)
	    res.getLabel().add(newInternationalizedString(label));
	return res;
    }

    public Activity newActivity(QName id, 
                                XMLGregorianCalendar startTime,
				XMLGregorianCalendar endTime,
				List<Attribute> attributes) {
	Activity res = newActivity(id);
	res.setStartTime(startTime);
	res.setEndTime(endTime);
	setAttributes(res, attributes);
	return res;

    }

    public Activity newActivity(String pr) {
	return newActivity(stringToQName(pr));
    }

    public Activity newActivity(String pr, String label) {
	return newActivity(stringToQName(pr), label);
    }

    public ActivityRef newActivityRef(Activity p) {
	ActivityRef res = of.createActivityRef();
	res.setRef(p.getId());
	return res;
    }

    public ActivityRef newActivityRef(QName id) {
	ActivityRef res = of.createActivityRef();
	res.setRef(id);
	return res;
    }

    public ActivityRef newActivityRef(String id) {
	return newActivityRef(stringToQName(id));
    }

    public Agent newAgent(Agent a) {
	Agent res = newAgent(a.getId());
	res.getType().addAll(a.getType());
	res.getLabel().addAll(a.getLabel());
	return res;
    }

    public Agent newAgent(QName ag) {
	Agent res = of.createAgent();
	res.setId(ag);
	return res;
    }

    public Agent newAgent(QName id, List<Attribute> attributes) {
	Agent res = newAgent(id);
	setAttributes(res, attributes);
	return res;
    }

    public Agent newAgent(QName ag, String label) {
	Agent res = newAgent(ag);
	if (label != null)
	    res.getLabel().add(newInternationalizedString(label));
	return res;
    }

    public Agent newAgent(String ag) {
	return newAgent(ag, null);
    }

    public Agent newAgent(String ag, String label) {
	return newAgent(stringToQName(ag), label);
    }

    public AgentRef newAgentRef(Agent a) {
	AgentRef res = of.createAgentRef();
	res.setRef(a.getId());
	return res;
    }

    public AgentRef newAgentRef(QName id) {
	AgentRef res = of.createAgentRef();
	res.setRef(id);
	return res;
    }

    public AgentRef newAgentRef(String id) {
	return newAgentRef(stringToQName(id));
    }

    public AlternateOf newAlternateOf(EntityRef eid2, EntityRef eid1) {
	AlternateOf res = of.createAlternateOf();
	res.setEntity2(eid2);
	res.setEntity1(eid1);
	return res;
    }
    

    public AlternateOf newAlternateOf(QName e2, QName e1) {
        EntityRef eid2 = (e2==null)? null: newEntityRef(e2);
        EntityRef eid1 = (e1==null)? null: newEntityRef(e1);
        return newAlternateOf(eid2, eid1);
    }
  

    public AnyRef newAnyRef(QName id) {
	AnyRef res = of.createAnyRef();
	res.setRef(id);
	return res;
    }

    public AnyRef newAnyRef(String id) {
	return newAnyRef(stringToQName(id));
    }

    public Attribute newAttribute(QName qname, Object value, ValueConverter vconv) {
  	Attribute res = new Attribute(qname, value, vconv.getXsdType(value));
  	return res;
      }

    public Attribute newAttribute(Attribute.AttributeKind kind, Object value, ValueConverter vconv) {
  	Attribute res = new Attribute(kind, value, vconv.getXsdType(value));
  	return res;
      }

    public Attribute newAttribute(String namespace, String localName,
				  String prefix, Object value, ValueConverter vconv) {
	Attribute res = new Attribute(new QName(namespace, localName, prefix),
				      value, vconv.getXsdType(value));
	return res;
    }

    public Attribute newAttribute(String namespace, String localName,
				  String prefix, Object value, String type) {
	Attribute res = new Attribute(new QName(namespace, localName, prefix),
				      value, type);
	return res;
    }

    public CollectionMemberOf newCollectionMemberOf(QName id, EntityRef after,
						    List<Entity> entitySet) {
	CollectionMemberOf res = cof.createCollectionMemberOf();
	res.setId(id);
	res.setEntity(after);
	if (entitySet != null)
	    res.getMember().addAll(entitySet);
	return res;
    }

    public CollectionMemberOf newCollectionMemberOf(String id, EntityRef after,
						    List<Entity> entitySet) {
	return newCollectionMemberOf(stringToQName(id), after, entitySet);
    }

    public DerivedByInsertionFrom newDerivedByInsertionFrom(QName id,
							    EntityRef after,
							    EntityRef before,
							    List<Entry> keyEntitySet) {
	DerivedByInsertionFrom res = cof.createDerivedByInsertionFrom();
	res.setId(id);
	res.setAfter(after);
	res.setBefore(before);
	if (keyEntitySet != null)
	    res.getEntry().addAll(keyEntitySet);
	return res;
    }

    public DerivedByInsertionFrom newDerivedByInsertionFrom(String id,
							    EntityRef after,
							    EntityRef before,
							    List<Entry> keyEntitySet) {
	return newDerivedByInsertionFrom(stringToQName(id), after, before,
					 keyEntitySet);
    }

    public DerivedByRemovalFrom newDerivedByRemovalFrom(QName id,
							EntityRef after,
							EntityRef before,
							List<Object> keys) {
	DerivedByRemovalFrom res = cof.createDerivedByRemovalFrom();
	res.setId(id);
	res.setAfter(after);
	res.setBefore(before);
	if (keys != null)
	    res.getKey().addAll(keys);
	return res;
    }

    public DerivedByRemovalFrom newDerivedByRemovalFrom(String id,
							EntityRef after,
							EntityRef before,
							List<Object> keys) {
	return newDerivedByRemovalFrom(stringToQName(id), after, before, keys);
    }

    public DictionaryMemberOf newDictionaryMemberOf(QName id, EntityRef after,
						    List<Entry> keyEntitySet) {
	DictionaryMemberOf res = cof.createDictionaryMemberOf();
	res.setId(id);
	res.setEntity(after);
	if (keyEntitySet != null)
	    res.getEntry().addAll(keyEntitySet);
	return res;
    }

    public DictionaryMemberOf newDictionaryMemberOf(String id, EntityRef after,
						    List<Entry> keyEntitySet) {
	return newDictionaryMemberOf(stringToQName(id), after, keyEntitySet);
    }

    public Document newDocument() {
	Document res = of.createDocument();
	return res;
    }

    /*
     * public Bundle newBundle(String id, Collection<Activity> ps,
     * Collection<Entity> as, Collection<Agent> ags, Collection<Object> lks) {
     * return newBundle(stringToQName(id), ps, as, ags, lks); }
     * 
     * public Bundle newBundle(Activity[] ps, Entity[] as, Agent[] ags, Object[]
     * lks) { return newBundle(null, ps, as, ags, lks); }
     */
    public Document newDocument(Activity[] ps, Entity[] as, Agent[] ags,
				Statement[] lks) {

	return newDocument(((ps == null) ? null : Arrays.asList(ps)),
			   ((as == null) ? null : Arrays.asList(as)),
			   ((ags == null) ? null : Arrays.asList(ags)),
			   ((lks == null) ? null : Arrays.asList(lks)));
    }

    public Document newDocument(Collection<Activity> ps, Collection<Entity> as,
				Collection<Agent> ags, Collection<Statement> lks) {
	Document res = of.createDocument();
	res.getEntityOrActivityOrWasGeneratedBy().addAll(ps);
	res.getEntityOrActivityOrWasGeneratedBy().addAll(as);
	res.getEntityOrActivityOrWasGeneratedBy().addAll(ags);
	res.getEntityOrActivityOrWasGeneratedBy().addAll(lks);
	return res;
    }
    public Document newDocument(Hashtable<String, String> namespaces,
                                Collection<Statement> statements,
                                Collection<NamedBundle> bundles) {
	Document res = of.createDocument();
	res.setNss(namespaces);
	res.getEntityOrActivityOrWasGeneratedBy()
	   .addAll(statements);
	res.getEntityOrActivityOrWasGeneratedBy()
	   .addAll(bundles);
	return res;
    }

    public Document newDocument(Document graph) {
	Document res = of.createDocument();
	res.getEntityOrActivityOrWasGeneratedBy()
	   .addAll(graph.getEntityOrActivityOrWasGeneratedBy());
	return res;
    }

    public JAXBElement<ActedOnBehalfOf> newElement(ActedOnBehalfOf u) {
	return of.createActedOnBehalfOf(u);
    }

    public JAXBElement<Activity> newElement(Activity u) {
	return of.createActivity(u);
    }

    public JAXBElement<Agent> newElement(Agent u) {
	return of.createAgent(u);
    }

    public JAXBElement<Entity> newElement(Entity u) {
	return of.createEntity(u);
    }

    public JAXBElement<MentionOf> newElement(MentionOf u) {
	return of.createMentionOf(u);
    }

    public Element newElement(QName qname, QName value) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(qname.getNamespaceURI(),
					 qnameToString(qname));
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", "xsd:QName");
	// add xmlns for prefix?
	el.appendChild(doc.createTextNode(qnameToString(value)));
	doc.appendChild(el);
	return el;
    }

    public Element newElement(QName qname, String value, String type) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(qname.getNamespaceURI(),
					 ((qname.getPrefix().equals("")) ? ""
						 : (qname.getPrefix() + ":"))
						 + qname.getLocalPart());
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", type);
	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }

    public Element newElement(QName qname, String value, String type,
			      String lang) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(qname.getNamespaceURI(),
					 ((qname.getPrefix().equals("")) ? ""
						 : (qname.getPrefix() + ":"))
						 + qname.getLocalPart());
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", type);
	el.setAttributeNS(NamespacePrefixMapper.XML_NS, "xml:lang", lang);
	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }

    public JAXBElement<Used> newElement(Used u) {
	return of.createUsed(u);
    }

    public JAXBElement<WasAssociatedWith> newElement(WasAssociatedWith u) {
	return of.createWasAssociatedWith(u);
    }

    public JAXBElement<WasAttributedTo> newElement(WasAttributedTo u) {
	return of.createWasAttributedTo(u);
    }

    public JAXBElement<WasDerivedFrom> newElement(WasDerivedFrom u) {
	return of.createWasDerivedFrom(u);
    }

    public JAXBElement<WasEndedBy> newElement(WasEndedBy u) {
	return of.createWasEndedBy(u);
    }

    public JAXBElement<WasGeneratedBy> newElement(WasGeneratedBy u) {
	return of.createWasGeneratedBy(u);
    }

    public JAXBElement<WasInfluencedBy> newElement(WasInfluencedBy u) {
	return of.createWasInfluencedBy(u);
    }

    public JAXBElement<WasInformedBy> newElement(WasInformedBy u) {
	return of.createWasInformedBy(u);
    }

    public JAXBElement<WasInvalidatedBy> newElement(WasInvalidatedBy u) {
	return of.createWasInvalidatedBy(u);
    }

    public JAXBElement<WasStartedBy> newElement(WasStartedBy u) {
	return of.createWasStartedBy(u);
    }

    public Entity newEntity(Entity e) {
	Entity res = newEntity(e.getId());
	res.getType().addAll(e.getType());
	res.getLabel().addAll(e.getLabel());
	res.getLocation().addAll(e.getLocation());
	return res;
    }

    public Entity newEntity(QName id) {
	Entity res = of.createEntity();
	res.setId(id);
	return res;
    }

    public Entity newEntity(QName id, List<Attribute> attributes) {
	Entity res = newEntity(id);
	setAttributes(res, attributes);
	return res;
    }

    public Entity newEntity(QName id, String label) {
	Entity res = newEntity(id);
	if (label != null)
	    res.getLabel().add(newInternationalizedString(label));
	return res;
    }

    public Entity newEntity(String id) {
	return newEntity(stringToQName(id));
    }

    public Entity newEntity(String id, String label) {
	return newEntity(stringToQName(id), label);
    }

    public EntityRef newEntityRef(Entity a) {
	EntityRef res = of.createEntityRef();
	res.setRef(a.getId());
	return res;
    }

    public EntityRef newEntityRef(QName id) {
	EntityRef res = of.createEntityRef();
	res.setRef(id);
	return res;
    }

    public EntityRef newEntityRef(String id) {
	return newEntityRef(stringToQName(id));
    }

    public Entry newEntry(Object key, EntityRef entity) {
	Entry res = cof.createEntry();
	res.setKey(key);
	res.setEntity(entity);
	return res;
    }

    public GenerationRef newGenerationRef(QName id) {
	GenerationRef res = of.createGenerationRef();
	res.setRef(id);
	return res;
    }

    public GenerationRef newGenerationRef(String id) {
	GenerationRef res = of.createGenerationRef();
	res.setRef(stringToQName(id));
	return res;
    }

    public GenerationRef newGenerationRef(WasGeneratedBy edge) {
	GenerationRef res = of.createGenerationRef();
	res.setRef(edge.getId());
	return res;
    }

    public HadMember newHadMember(EntityRef collection, EntityRef... entities) {
	HadMember res = of.createHadMember();
	res.setCollection(collection);
	if (entities != null) {
	    res.getEntity().addAll(Arrays.asList(entities));
	}
	return res;
    }
    public HadMember newHadMember(QName c, List<QName> e) {
        EntityRef cid=(c==null)? null: newEntityRef(c);
        List<EntityRef> ll=new LinkedList<EntityRef>();
        for (QName q: e) {
            EntityRef eid=(e==null)? null: newEntityRef(q);
            ll.add(eid);
        }
        HadMember res = of.createHadMember();
        res.setCollection(cid);
        res.getEntity().addAll(ll);
        return res;
    }

    

    public InternationalizedString newInternationalizedString(String s) {
	InternationalizedString res = of.createInternationalizedString();
	res.setValue(s);
	return res;
    }

    public InternationalizedString newInternationalizedString(String s,
							      String lang) {
	InternationalizedString res = of.createInternationalizedString();
	res.setValue(s);
	res.setLang(lang);
	return res;
    }

    public XMLGregorianCalendar newISOTime(String time) {
	return newTime(javax.xml.bind.DatatypeConverter.parseDateTime(time)
						       .getTime());
    }

    public MentionOf newMentionOf(Entity infra, Entity supra, Entity bundle) {
	return newMentionOf((infra == null) ? null : newEntityRef(infra),
			    (supra == null) ? null : newEntityRef(supra),
			    (bundle == null) ? null : newEntityRef(bundle));
    }
    public MentionOf newMentionOf(QName e2, QName e1, QName b) {
        EntityRef eid2 = (e2==null)? null: newEntityRef(e2);
        EntityRef eid1 = (e1==null)? null: newEntityRef(e1);
        EntityRef bid = (b==null)? null: newEntityRef(b);
        return newMentionOf(eid2, eid1, bid);
    }

    
    public MentionOf newMentionOf(EntityRef infra, EntityRef supra,
				  EntityRef bundle) {
	MentionOf res = of.createMentionOf();
	res.setSpecializedEntity(infra);
	res.setBundle(bundle);
	res.setGeneralEntity(supra);
	return res;
    }

    public MentionOf newMentionOf(MentionOf r) {
	MentionOf res = of.createMentionOf();
	res.setSpecializedEntity(r.getSpecializedEntity());
	res.setBundle(r.getBundle());
	res.setGeneralEntity(r.getGeneralEntity());
	return res;
    }

    public MentionOf newMentionOf(String infra, String supra, String bundle) {
	MentionOf res = of.createMentionOf();
	if (supra != null)
	    res.setSpecializedEntity(newEntityRef(infra));
	if (bundle != null)
	    res.setBundle(newEntityRef(bundle));
	if (supra != null)
	    res.setGeneralEntity(newEntityRef(supra));
	return res;
    }

    public NamedBundle newNamedBundle(QName id, Collection<Activity> ps,
				      Collection<Entity> as,
				      Collection<Agent> ags,
				      Collection<Statement> lks) {
	NamedBundle res = of.createNamedBundle();
	res.setId(id);

	if (ps != null) {
	    res.getEntityOrActivityOrWasGeneratedBy().addAll(ps);
	}
	if (as != null) {
	    res.getEntityOrActivityOrWasGeneratedBy().addAll(as);
	}
	if (ags != null) {
	    res.getEntityOrActivityOrWasGeneratedBy().addAll(ags);
	}
	if (lks != null) {
	    res.getEntityOrActivityOrWasGeneratedBy().addAll(lks);
	}
	return res;
    }

    public NamedBundle newNamedBundle(QName id, Collection<Statement> lks) {
	NamedBundle res = of.createNamedBundle();
	res.setId(id);

	if (lks != null) {
	    res.getEntityOrActivityOrWasGeneratedBy().addAll(lks);
	}
	return res;
    }

    public NamedBundle newNamedBundle(QName id, Hashtable<String,String> namespaces, Collection<Statement> statements) {
	NamedBundle res = of.createNamedBundle();
	res.setId(id);
	res.setNss(namespaces);
	if (statements != null) {
	    res.getEntityOrActivityOrWasGeneratedBy().addAll(statements);
	}
	return res;
    }

    public NamedBundle newNamedBundle(String id, Activity[] ps, Entity[] es,
				      Agent[] ags, Statement[] lks) {

	return newNamedBundle(id, ((ps == null) ? null : Arrays.asList(ps)),
			      ((es == null) ? null : Arrays.asList(es)),
			      ((ags == null) ? null : Arrays.asList(ags)),
			      ((lks == null) ? null : Arrays.asList(lks)));
    }

    public NamedBundle newNamedBundle(String id, Collection<Activity> ps,
				      Collection<Entity> as,
				      Collection<Agent> ags,
				      Collection<Statement> lks) {
	return newNamedBundle(stringToQName(id), ps, as, ags, lks);
    }


    public SpecializationOf newSpecializationOf(EntityRef eid2, EntityRef eid1) {
	SpecializationOf res = of.createSpecializationOf();
	res.setSpecializedEntity(eid2);
	res.setGeneralEntity(eid1);
	return res;
    }

    public SpecializationOf newSpecializationOf(QName e2, QName e1) {
        EntityRef eid2 = (e2==null)? null: newEntityRef(e2);
        EntityRef eid1 = (e1==null)? null: newEntityRef(e1);
        return newSpecializationOf(eid2, eid1);
    }
  
    

    

    public XMLGregorianCalendar newTime(Date date) {
	GregorianCalendar gc = new GregorianCalendar();
	gc.setTime(date);
	return newXMLGregorianCalendar(gc);
    }

    public XMLGregorianCalendar newTimeNow() {
	return newTime(new Date());
    }

    public UsageRef newUsageRef(QName id) {
	UsageRef res = of.createUsageRef();
	res.setRef(id);
	return res;
    }

    public UsageRef newUsageRef(String id) {
	UsageRef res = of.createUsageRef();
	res.setRef(stringToQName(id));
	return res;
    }

    public UsageRef newUsageRef(Used edge) {
	UsageRef res = of.createUsageRef();
	res.setRef(edge.getId());
	return res;
    }

    public Used newUsed(Activity p, String role, Entity a) {
	Used res = newUsed(null, p, role, a);
	return res;
    }

    public Used newUsed(QName id) {
	Used res = of.createUsed();
	res.setId(id);
	return res;
    }

    public Used newUsed(QName id, ActivityRef aid, String role, EntityRef eid) {
	Used res = newUsed(id);
	res.setActivity(aid);
	addRole(res, role);
	res.setEntity(eid);
	return res;
    }

    public Used newUsed(String id, Activity p, String role, Entity a) {
	ActivityRef pid = newActivityRef(p);
	EntityRef aid = newEntityRef(a);
	return newUsed(stringToQName(id), pid, role, aid);
    }

    public Used newUsed(String id, Activity p, String role, Entity a,
			String type) {
	Used res = newUsed(id, p, role, a);
	addType(res, type);
	return res;
    }

    public Used newUsed(String id, ActivityRef pid, String role, EntityRef aid) {
	Used res = of.createUsed();
	res.setId(stringToQName(id));
	res.setActivity(pid);
	addRole(res, role);
	res.setEntity(aid);
	return res;
    }
    
    public Used newUsed(QName id, QName activity, QName entity, XMLGregorianCalendar time, List<Attribute> attributes) {
   	ActivityRef aid = (activity==null)? null: newActivityRef(activity);
        EntityRef eid = (entity==null)? null: newEntityRef(entity);
   	Used res=newUsed(id,aid,null,eid);	
   	res.setTime(time);
	setAttributes(res, attributes);
   	return res;
       }

    
    

    public Used newUsed(Used u) {
	Used u1 = newUsed(u.getId(), u.getActivity(), null, u.getEntity());
	u1.getAny().addAll(u.getAny());
	u1.setTime(u.getTime());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	u1.getLocation().addAll(u.getLocation());
	return u1;
    }

    public WasAssociatedWith newWasAssociatedWith(QName id, Activity eid2,
						  Agent eid1) {
	return newWasAssociatedWith(id, newActivityRef(eid2.getId()),
				    newAgentRef(eid1.getId()));
    }

    public WasAssociatedWith newWasAssociatedWith(QName id, ActivityRef eid2,
						  AgentRef eid1) {
	WasAssociatedWith res = of.createWasAssociatedWith();
	res.setId(id);
	res.setActivity(eid2);
	res.setAgent(eid1);
	return res;
    }

    public WasAssociatedWith newWasAssociatedWith(String id, Activity eid2,
						  Agent eid1) {
	return newWasAssociatedWith(id, newActivityRef(eid2.getId()),
				    newAgentRef(eid1.getId()));
    }

    public WasAssociatedWith newWasAssociatedWith(String id, ActivityRef eid2,
						  AgentRef eid1) {
	WasAssociatedWith res = of.createWasAssociatedWith();
	res.setId(stringToQName(id));
	res.setActivity(eid2);
	res.setAgent(eid1);
	return res;
    }
    
    public WasAssociatedWith  newWasAssociatedWith(QName id, 
                                                   QName a, 
                                                   QName ag, 
                                                   QName plan, 
                                                   List<Attribute> attributes) {
	ActivityRef aid=(a==null)? null: newActivityRef(a);
	EntityRef eid=(plan==null)? null: newEntityRef(plan);
	AgentRef agid=(ag==null)? null: newAgentRef(ag);
	WasAssociatedWith res= newWasAssociatedWith(id,aid,agid);
	res.setPlan(eid);
	setAttributes(res, attributes);
	return res;
    }


    public WasAssociatedWith newWasAssociatedWith(WasAssociatedWith u) {
	WasAssociatedWith u1 = newWasAssociatedWith(u.getId(), u.getActivity(),
						    u.getAgent());
	u1.getAny().addAll(u.getAny());
	u1.setPlan(u.getPlan());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	return u1;
    }

    public WasAttributedTo newWasAttributedTo(QName id, EntityRef eid,
					      AgentRef agid) {
	WasAttributedTo res = of.createWasAttributedTo();
	res.setId(id);
	res.setEntity(eid);
	res.setAgent(agid);
	return res;
    }

    public WasAttributedTo newWasAttributedTo(String id, EntityRef eid,
					      AgentRef agid) {
	return newWasAttributedTo(stringToQName(id), eid, agid);
    }

    
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,  List<Attribute> attributes) {
        EntityRef eid=(e==null)? null : newEntityRef(e);
        AgentRef agid=(ag==null)? null : newAgentRef(ag);
        WasAttributedTo res=newWasAttributedTo(id, eid, agid);
        setAttributes(res, attributes);
        return res;
    }

    public WasAttributedTo newWasAttributedTo(WasAttributedTo u) {
	WasAttributedTo u1 = newWasAttributedTo(u.getId(), u.getEntity(),
						u.getAgent());
	u1.getAny().addAll(u.getAny());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	return u1;
    }

    public WasDerivedFrom newWasDerivedFrom(Entity a1, Entity a2) {
	return newWasDerivedFrom(null, a1, a2);
    }

    public WasDerivedFrom newWasDerivedFrom(Entity a1, Entity a2, Activity a,
					    WasGeneratedBy g2, Used u1) {
	return newWasDerivedFrom(null, a1, a2, a, g2, u1);
    }

    public WasDerivedFrom newWasDerivedFrom(QName id, EntityRef aid1,
					    EntityRef aid2) {
	WasDerivedFrom res = of.createWasDerivedFrom();
	res.setId(id);
	res.setUsedEntity(aid2);
	res.setGeneratedEntity(aid1);
	return res;
    }

    public WasDerivedFrom newWasDerivedFrom(QName id, EntityRef aid1,
					    EntityRef aid2, ActivityRef aid,
					    GenerationRef did1, UsageRef did2) {
	WasDerivedFrom res = of.createWasDerivedFrom();
	res.setId(id);
	res.setUsedEntity(aid2);
	res.setGeneratedEntity(aid1);
	res.setActivity(aid);
	res.setGeneration(did1);
	res.setUsage(did2);
	return res;
    }

    public WasDerivedFrom newWasDerivedFrom(String id, Entity a1, Entity a2) {
	EntityRef aid1 = newEntityRef(a1);
	EntityRef aid2 = newEntityRef(a2);
	return newWasDerivedFrom(id, aid1, aid2);
    }

    public WasDerivedFrom newWasDerivedFrom(String id, Entity e2, Entity e1,
					    Activity a, WasGeneratedBy g2,
					    Used u1) {
	EntityRef eid1 = newEntityRef(e1);
	EntityRef eid2 = newEntityRef(e2);
	ActivityRef aid = newActivityRef(a);
	GenerationRef did2 = newGenerationRef(g2);
	UsageRef did1 = newUsageRef(u1);
	return newWasDerivedFrom(id, eid2, eid1, aid, did2, did1);
    }

    public WasDerivedFrom newWasDerivedFrom(String id, Entity a1, Entity a2,
					    String type) {
	WasDerivedFrom wdf = newWasDerivedFrom(id, a1, a2);
	addType(wdf, type);
	return wdf;
    }


    public WasDerivedFrom newWasDerivedFrom(String id, EntityRef aid1,
					    EntityRef aid2) {
	return newWasDerivedFrom(stringToQName(id), aid1, aid2);
    }

    public WasDerivedFrom newWasDerivedFrom(String id, EntityRef aid1,
					    EntityRef aid2, ActivityRef aid,
					    GenerationRef did1, UsageRef did2) {
	return newWasDerivedFrom(stringToQName(id), aid1, aid2, aid, did1, did2);
    }

    public WasDerivedFrom newWasDerivedFrom(QName id, QName e2, QName e1, QName a, QName g, QName u,  List<Attribute> attributes) {
	EntityRef eid1 = (e1==null)? null: newEntityRef(e1);
	EntityRef eid2 = (e2==null)? null: newEntityRef(e2);
	ActivityRef aid = (a==null)? null : newActivityRef(a);
	GenerationRef gid = (g==null)? null: newGenerationRef(g);
	UsageRef uid = (u==null) ? null : newUsageRef(u);
	WasDerivedFrom res=newWasDerivedFrom(id, eid2, eid1, aid, gid, uid);
	setAttributes(res, attributes);
	return res;
    }

    public WasDerivedFrom newWasDerivedFrom(WasDerivedFrom d) {
	WasDerivedFrom wdf = newWasDerivedFrom(d.getId(),
					       d.getGeneratedEntity(),
					       d.getUsedEntity());
	wdf.setGeneration(d.getGeneration());
	wdf.setUsage(d.getUsage());
	wdf.getAny().addAll(d.getAny());
	wdf.getType().addAll(d.getType());
	wdf.getLabel().addAll(d.getLabel());
	return wdf;
    }
    
    public WasEndedBy newWasEndedBy(QName id) {
	WasEndedBy res = of.createWasEndedBy();
	res.setId(id);
	return res;
    }
    
    public WasEndedBy newWasEndedBy(QName id, ActivityRef aid, EntityRef eid) {
	WasEndedBy res = of.createWasEndedBy();
	res.setId(id);
	res.setActivity(aid);
	res.setTrigger(eid);
	return res;
    }

    public WasEndedBy newWasEndedBy(String id, ActivityRef aid, EntityRef eid) {
	return newWasEndedBy(stringToQName(id), aid, eid);
    }
    
    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger, QName ender, XMLGregorianCalendar time, List<Attribute> attributes) {
   	ActivityRef aid = (activity==null)? null: newActivityRef(activity);
      	EntityRef eid = (trigger==null)? null: newEntityRef(trigger);
      	ActivityRef sid = (ender==null)? null: newActivityRef(ender);
      	WasEndedBy res=newWasEndedBy(id,aid,eid);	
      	res.setTime(time);
      	res.setEnder(sid);
	setAttributes(res, attributes);
      	return res;
    }

    public WasEndedBy newWasEndedBy(WasEndedBy u) {
	WasEndedBy u1 = newWasEndedBy(u.getId(), u.getActivity(),
				      u.getTrigger());
	u1.setEnder(u.getEnder());
	u1.setTime(u.getTime());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	u1.getLocation().addAll(u.getLocation());
	u1.getAny().addAll(u.getAny());
	return u1;
    }

    public WasGeneratedBy newWasGeneratedBy(Entity a, String role, Activity p) {
	return newWasGeneratedBy((QName) null, a, role, p);
    }

    public WasGeneratedBy newWasGeneratedBy(QName id) {
	WasGeneratedBy res = of.createWasGeneratedBy();
	res.setId(id);
	return res;
	
    }

    public WasGeneratedBy newWasGeneratedBy(QName id, Entity a, String role,
					    Activity p) {
	EntityRef aid = newEntityRef(a);
	ActivityRef pid = newActivityRef(p);
	return newWasGeneratedBy(id, aid, role, pid);
    }

    public WasGeneratedBy newWasGeneratedBy(QName id, EntityRef aid,
					    String role, ActivityRef pid) {
	WasGeneratedBy res = of.createWasGeneratedBy();
	res.setId(id);
	res.setActivity(pid);
	res.setEntity(aid);
	addRole(res, role);
	return res;
    }

    public WasGeneratedBy newWasGeneratedBy(String id, Entity a, String role,
					    Activity p) {
	EntityRef aid = newEntityRef(a);
	ActivityRef pid = newActivityRef(p);
	return newWasGeneratedBy(stringToQName(id), aid, role, pid);
    }

    public WasGeneratedBy newWasGeneratedBy(String id, Entity a, String role,
					    Activity p, String type) {
	WasGeneratedBy wgb = newWasGeneratedBy(id, a, role, p);
	addType(wgb, type);
	return wgb;
    }

    public WasGeneratedBy newWasGeneratedBy(String id, EntityRef aid,
					    String role, ActivityRef pid) {
	return newWasGeneratedBy(stringToQName(id), aid, role, pid);
    }
    
    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity, QName activity, XMLGregorianCalendar time, List<Attribute> attributes) {
   	ActivityRef aid = (activity==null)? null: newActivityRef(activity);
   	EntityRef eid = (entity==null)? null: newEntityRef(entity);
   	WasGeneratedBy res=newWasGeneratedBy(id,eid,null,aid);	
   	res.setTime(time);
	setAttributes(res, attributes);
   	return res;
       }

    public WasGeneratedBy newWasGeneratedBy(WasGeneratedBy g) {
	WasGeneratedBy wgb = newWasGeneratedBy(g.getId(), g.getEntity(), null,
					       g.getActivity());
	wgb.setId(g.getId());
	wgb.setTime(g.getTime());
	wgb.getAny().addAll(g.getAny());
	wgb.getType().addAll(g.getType());
	wgb.getLabel().addAll(g.getLabel());
	wgb.getLocation().addAll(g.getLocation());
	return wgb;
    }

    public WasInfluencedBy newWasInfluencedBy(QName id, AnyRef influencee,
					      AnyRef influencer) {
	WasInfluencedBy res = of.createWasInfluencedBy();
	res.setId(id);
	res.setInfluencee(influencee);
	res.setInfluencer(influencer);
	return res;
    }

    public WasInfluencedBy newWasInfluencedBy(String id, AnyRef influencee,
					      AnyRef influencer) {
	WasInfluencedBy res = of.createWasInfluencedBy();
	res.setId(stringToQName(id));
	res.setInfluencee(influencee);
	res.setInfluencer(influencer);
	return res;
    }

    public WasInfluencedBy newWasInfluencedBy(String id, String influencee,
					      String influencer) {
	return newWasInfluencedBy(id, (influencee == null) ? null
		: newAnyRef(influencee), (influencer == null) ? null
		: newAnyRef(influencer));
    }
    
    
    public WasInfluencedBy newWasInfluencedBy(QName id, QName a2, QName a1, List<Attribute> attributes) {
        AnyRef aid2 = (a2==null) ? null: newAnyRef(a2);
        AnyRef aid1 = (a1==null) ? null: newAnyRef(a1);
        WasInfluencedBy res=newWasInfluencedBy(id,aid2,aid1);   
        setAttributes(res, attributes);
        return res;
    }

    public WasInfluencedBy newWasInfluencedBy(WasInfluencedBy in) {
	WasInfluencedBy out = newWasInfluencedBy(in.getId(),
						 in.getInfluencee(),
						 in.getInfluencer());
	out.setId(in.getId());
	out.getAny().addAll(in.getAny());
	out.getType().addAll(in.getType());
	out.getLabel().addAll(in.getLabel());
	return out;
    }

    // public void addType(HasExtensibility a,
    // String type) {
    //
    // addType(a,type,"xsd:anyURI");
    // }

    public WasInformedBy newWasInformedBy(Activity p1, Activity p2) {
	return newWasInformedBy(null, p1, p2);
    }

    public WasInformedBy newWasInformedBy(QName id, Activity p1, Activity p2,
					  String type) {
	WasInformedBy wtb = newWasInformedBy(p1, p2);
	wtb.setId(id);
	addType(wtb, type);
	return wtb;
    }

    public WasInformedBy newWasInformedBy(QName id, ActivityRef pid1,
					  ActivityRef pid2) {
	WasInformedBy res = of.createWasInformedBy();
	res.setId(id);
	res.setEffect(pid1);
	res.setCause(pid2);
	return res;
    }

    public WasInformedBy newWasInformedBy(String id, Activity p1, Activity p2) {
	ActivityRef pid1 = newActivityRef(p1);
	ActivityRef pid2 = newActivityRef(p2);
	return newWasInformedBy(id, pid1, pid2);
    }

    public WasInformedBy newWasInformedBy(String id, Activity p1, Activity p2,
					  String type) {
	return newWasInformedBy(stringToQName(id), p1, p2, type);
    }

    public WasInformedBy newWasInformedBy(String id, ActivityRef pid1,
					  ActivityRef pid2) {
	return newWasInformedBy(stringToQName(id), pid1, pid2);
    }

    public WasInformedBy newWasInformedBy(QName id, QName a2, QName a1, List<Attribute> attributes) {
        ActivityRef aid2 = (a2==null) ? null: newActivityRef(a2);
        ActivityRef aid1 = (a1==null) ? null: newActivityRef(a1);
        WasInformedBy res=newWasInformedBy(id,aid2,aid1);   
        setAttributes(res, attributes);
        return res;
    }
    
    public WasInformedBy newWasInformedBy(WasInformedBy d) {
	WasInformedBy wtb = newWasInformedBy(d.getId(), d.getEffect(),
					     d.getCause());
	wtb.setId(d.getId());
	wtb.getAny().addAll(d.getAny());
	wtb.getType().addAll(d.getType());
	wtb.getLabel().addAll(d.getLabel());
	return wtb;
    }

    public WasInvalidatedBy newWasInvalidatedBy(QName id, EntityRef eid,
						ActivityRef aid) {
	WasInvalidatedBy res = of.createWasInvalidatedBy();
	res.setId(id);
	res.setEntity(eid);
	res.setActivity(aid);
	return res;
    }

    public WasInvalidatedBy newWasInvalidatedBy(String id, EntityRef eid,
						ActivityRef aid) {
	return newWasInvalidatedBy(stringToQName(id), eid, aid);
    }
    
    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity, QName activity, XMLGregorianCalendar time, List<Attribute> attributes) {
   	ActivityRef aid = (activity==null) ? null: newActivityRef(activity);
   	EntityRef eid = (entity==null)? null: newEntityRef(entity);
   	WasInvalidatedBy res=newWasInvalidatedBy(id,eid,aid);	
   	res.setTime(time);
	setAttributes(res, attributes);
   	return res;
       }


    public WasInvalidatedBy newWasInvalidatedBy(WasInvalidatedBy u) {
	WasInvalidatedBy u1 = newWasInvalidatedBy(u.getId(), u.getEntity(),
						  u.getActivity());
	u1.setTime(u.getTime());
	u1.getAny().addAll(u.getAny());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	u1.getLocation().addAll(u.getLocation());
	return u1;
    }
    public WasStartedBy newWasStartedBy(QName id) {
   	WasStartedBy res = of.createWasStartedBy();
   	res.setId(id);
   	return res;
    }
    
    public WasStartedBy newWasStartedBy(QName id, ActivityRef aid, EntityRef eid) {
	WasStartedBy res = of.createWasStartedBy();
	res.setId(id);
	res.setActivity(aid);
	res.setTrigger(eid);
	return res;
    }

    public WasStartedBy newWasStartedBy(String id, ActivityRef aid,
					EntityRef eid) {
	return newWasStartedBy(stringToQName(id), aid, eid);
    }
    
    public WasStartedBy newWasStartedBy(QName id, QName activity, QName trigger, QName starter, XMLGregorianCalendar time, List<Attribute> attributes) {
   	ActivityRef aid = (activity==null)? null: newActivityRef(activity);
      	EntityRef eid = (trigger==null)? null: newEntityRef(trigger);
      	ActivityRef sid = (starter==null)? null: newActivityRef(starter);
      	WasStartedBy res=newWasStartedBy(id,aid,eid);	
      	res.setTime(time);
      	res.setStarter(sid);
	setAttributes(res, attributes);
      	return res;
       }


    public WasStartedBy newWasStartedBy(WasStartedBy u) {
	WasStartedBy u1 = newWasStartedBy(u.getId(), u.getActivity(),
					  u.getTrigger());
	u1.setStarter(u.getStarter());
	u1.setTime(u.getTime());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	u1.getLocation().addAll(u.getLocation());
	u1.getAny().addAll(u.getAny());
	return u1;
    }

    public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar gc) {
	return dataFactory.newXMLGregorianCalendar(gc);
    }

    public Element OLDnewAttribute(String namespace, String prefix,
				   String localName, String value) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(namespace, ((prefix.equals("")) ? ""
		: (prefix + ":")) + localName);
	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }

 
    public void setAttributes(HasExtensibility res, List<Attribute> attributes) {
	if (attributes==null) return;
	HasType typ=(res instanceof HasType)? (HasType)res : null;
	HasLocation loc=(res instanceof HasLocation)? (HasLocation)res : null;
	HasLabel lab=(res instanceof HasLabel)? (HasLabel)res : null;
	HasValue aval=(res instanceof HasValue)? (HasValue)res : null;
	HasRole rol=(res instanceof HasRole)? (HasRole)res : null;

	for (Attribute attr: attributes) {
	    Object aValue=attr.getValue();
	    switch (attr.getKind()) {
	    case PROV_LABEL:
		if (lab!=null) {
		    if (aValue instanceof InternationalizedString) {
			lab.getLabel().add((InternationalizedString) aValue);		
		    } else {
			lab.getLabel().add(newInternationalizedString(aValue.toString()));
		    }
		}
		break;
	    case PROV_LOCATION:
		if (loc!=null) {
		    loc.getLocation().add(aValue);
		}
		break;
	    case PROV_ROLE:
		if (rol!=null) {
		    rol.getRole().add(aValue);
		}
		break;
	    case PROV_TYPE: 
		if (typ!=null) {
		    typ.getType().add(aValue);
		}
		break;
	    case PROV_VALUE:
		if (aval!=null) {
		    aval.setValue(aValue);
		}
		break;
	    case OTHER:
		res.getAny().add(attr);
		break;
	    
	    default:
		break;
	    
	    }
	    
	}
    }

    public void setNamespaces(Hashtable<String, String> nss) {
	namespaces = nss;
    }

    public void resetNamespaces() {
	namespaces = new Hashtable<String, String>();
    }

    // What's the difference with stringToQName?
    public Object newQName(String qnameAsString) {
	int index = qnameAsString.indexOf(':');
	String prefix;
	String local;

	if (index == -1) {
	    prefix = "";
	    local = qnameAsString;
	} else {
	    prefix = qnameAsString.substring(0, index);
	    local = qnameAsString.substring(index + 1, qnameAsString.length());
	}
	return new QName(getNamespace(prefix), local, prefix);
    }

    public QName stringToQName(String id) {
	if (id == null)
	    return null;
	int index = id.indexOf(':');
	if (index == -1) {
	    return new QName(namespaces.get(DEFAULT_NS), id);
	}
	String prefix = id.substring(0, index);
	String local = id.substring(index + 1, id.length());
	if ("prov".equals(prefix)) {
	    return new QName(NamespacePrefixMapper.PROV_NS, local, prefix);
	} else if ("xsd".equals(prefix)) {
	    return new QName(NamespacePrefixMapper.XSD_NS, // + "#", // RDF ns ends
								 // in #, not
								 // XML ns.
			     local, prefix);
	} else {
	    return new QName(namespaces.get(prefix), local, prefix);
	}
    }
    
    /* Uses the xsd:type to java:type mapping of JAXB */
    
    ///TODO: should use the prefix definition of nss, as opposed to the one in qname

    public String qnameToString(QName qname) {
	return ((qname.getPrefix().equals("")) ? "" : (qname.getPrefix() + ":"))
		+ qname.getLocalPart();
    }

    @Override
    public void startDocument(Hashtable<String, String> hashtable) {
        
    }

    @Override
    public void startBundle(QName bundleId, Hashtable<String, String> namespaces) {
      
    }


}
