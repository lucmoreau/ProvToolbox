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


import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** A stateless factory for PROV objects. */

public class ProvFactory {

    public static final String packageList = "org.openprovenance.prov.xml";

    static {
	initBuilder();
    }

    public static String printURI(java.net.URI u) {
	return u.toString();
    }

    /** Note, this method now makes it stateful :-( */
    private Hashtable<String, String> namespaces = null;

    private final static ProvFactory oFactory = new ProvFactory();

    static public DocumentBuilder builder;

    public static ProvFactory getFactory() {
	return oFactory;
    }

    static void initBuilder() {
	try {
	    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
		    .newInstance();
	    docBuilderFactory.setNamespaceAware(true);
	    builder = docBuilderFactory.newDocumentBuilder();
	} catch (ParserConfigurationException ex) {
	    throw new RuntimeException(ex);
	}
    }

    protected ObjectFactory of;

    protected DatatypeFactory dataFactory;

    public ProvFactory() {
	of = new ObjectFactory();
	init();
    }

    public ProvFactory(Hashtable<String, String> namespaces) {
	of = new ObjectFactory();
	this.namespaces = namespaces;
	init();
    }

    public ProvFactory(ObjectFactory of) {
	this.of = of;
	init();
    }

    public void addAttribute(HasExtensibility a, Object o) {
	a.getAny().add(o);
    }

    public void addAttribute(HasExtensibility a, String namespace,
			     String prefix, String localName, String value) {

	a.getAny().add(newAttribute(namespace, prefix, localName, value));
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

        public void addLabel(HasExtensibility a, String label) {
        JAXBElement<InternationalizedString> je = of
        	.createLabel(newInternationalizedString(label));
        addAttribute(a, je);
        }

    public void addLabel(HasExtensibility a, String label, String language) {
	JAXBElement<InternationalizedString> je = of
		.createLabel(newInternationalizedString(label, language));
	addAttribute(a, je);
    }

    public void addRole(HasRole a, Object role) {
	if (role != null) {
	    a.getRole().add(role);
	}
    }

    /*
    public DependencyRef newDependencyRef(WasDerivedFrom edge) {
	DependencyRef res = of.createDependencyRef();
	res.setRef(edge.getId());
	return res;
    }

    public DependencyRef newDependencyRef(WasInformedBy edge) {
	DependencyRef res = of.createDependencyRef();
	res.setRef(edge.getId());
	return res;
    }
    */

    public void addType(HasType a, Object type) {

	a.getType().add(type);
    }

    public void addType(HasType a, URI type) {
	URIWrapper u = new URIWrapper();
	u.setValue(type);
	a.getType().add(u);
    }

    public void addTypeOLD(HasExtensibility a, Object type) {

	// TypedLiteral tl=newTypedLiteral(type);
	JAXBElement<Object> je = of.createType(type);
	addAttribute(a, je);

    }

    public void addTypeOLD(HasExtensibility a, URI type) {

	URIWrapper u = new URIWrapper();
	u.setValue(type);
	JAXBElement<Object> je = of.createType(u);
	addAttribute(a, je);
    }

    /**
     * By default, no auto generation of Id. Override this behaviour if
     * required.
     */
    public String autoGenerateId(String prefix) {
	return null;
    }

    /**
     * Conditional autogeneration of Id. By default, no auto generation of Id.
     * Override this behaviour if required.
     */
    public String autoGenerateId(String prefix, String id) {
	return id;
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

    void init() {
	try {
	    dataFactory = DatatypeFactory.newInstance();
	} catch (DatatypeConfigurationException ex) {
	    throw new RuntimeException(ex);
	}
    }

    public ActedOnBehalfOf newActedOnBehalfOf(ActedOnBehalfOf u) {
    	ActedOnBehalfOf u1 = newActedOnBehalfOf(u.getId(), u.getSubordinate(), u.getResponsible(), u.getActivity());
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

    public Activity newActivity(Activity a) {
	Activity res = newActivity(a.getId());
	res.getType().addAll(a.getType()); 
	res.getLabel().addAll(a.getLabel());
	res.getLocation().addAll(a.getLocation());
	res.setStartTime(a.getStartTime());
	res.setEndTime(a.getEndTime());
	return res;
    }

    public Activity newActivity(QName pr) {
	return newActivity(pr, null);
    }

    public Activity newActivity(QName pr, String label) {
	Activity res = of.createActivity();
	res.setId(pr);
	if (label != null)
	    res.getLabel().add(newInternationalizedString(label));
	return res;
    }

    public Activity newActivity(String pr) {
	return newActivity(pr, null);
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
	return newAgent(ag, null);
    }

    public Agent newAgent(QName ag, String label) {
	Agent res = of.createAgent();
	res.setId(ag);
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

    public AnyRef newAnyRef(QName id) {
	AnyRef res = of.createAnyRef();
	res.setRef(id);
	return res;
    }

    public AnyRef newAnyRef(String id) {
	return newAnyRef(stringToQName(id));
    }

    public Element newAttribute(String namespace, String prefix,
				String localName, String value) {
    	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(namespace, ((prefix.equals("")) ? ""
		: (prefix + ":")) + localName);
	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }

    public CollectionMemberOf newCollectionMemberOf(QName id, EntityRef after,
						    List<Entity> entitySet) {
	CollectionMemberOf res = of.createCollectionMemberOf();
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
	DerivedByInsertionFrom res = of.createDerivedByInsertionFrom();
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
	DerivedByRemovalFrom res = of.createDerivedByRemovalFrom();
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
	DictionaryMemberOf res = of.createDictionaryMemberOf();
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
    public Bundle newBundle(String id, Collection<Activity> ps,
			    Collection<Entity> as, Collection<Agent> ags,
			    Collection<Object> lks) {
	return newBundle(stringToQName(id), ps, as, ags, lks);
    }

    public Bundle newBundle(Activity[] ps, Entity[] as, Agent[] ags,
			    Object[] lks) {
	return newBundle(null, ps, as, ags, lks);
    }

*/
    public Document newDocument(Activity[] ps, Entity[] as, Agent[] ags,
			    Object[] lks) {

	return newDocument(((ps == null) ? null : Arrays.asList(ps)),
			 ((as == null) ? null : Arrays.asList(as)),
			 ((ags == null) ? null : Arrays.asList(ags)),
			 ((lks == null) ? null : Arrays.asList(lks)));
    }
    
    public Document newDocument(Collection<Activity> ps, Collection<Entity> as,
			    Collection<Agent> ags, Collection<Object> lks) {
	Document res = of.createDocument();
	res.getEntityOrActivityOrWasGeneratedBy().addAll(ps);
	res.getEntityOrActivityOrWasGeneratedBy().addAll(as);
	res.getEntityOrActivityOrWasGeneratedBy().addAll(ags);
	res.getEntityOrActivityOrWasGeneratedBy().addAll(lks);
	return res;
    }

    
    public Document newDocument(Document graph) {
	Document res=of.createDocument();	
	res.getEntityOrActivityOrWasGeneratedBy().addAll(graph.getEntityOrActivityOrWasGeneratedBy());
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
    public JAXBElement<MentionOf> newElement(MentionOf u) {
	return of.createMentionOf(u);
    }

    public Entity newEntity(Entity e) {
	Entity res = newEntity(e.getId());
	res.getType().addAll(e.getType()); 
        res.getLabel().addAll(e.getLabel());
        res.getLocation().addAll(e.getLocation());
	return res;
    }

    public Entity newEntity(QName id) {
	return newEntity(id, null);
    }

    public Entity newEntity(QName id, String label) {
	Entity res = of.createEntity();
	res.setId(id);
	if (label != null)
	    res.getLabel().add(newInternationalizedString(label));
	return res;
    }

    public Entity newEntity(String id) {
	return newEntity(stringToQName(id), null);
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
	Entry res = of.createEntry();
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

    public HadPrimarySource newHadPrimarySource(QName id, EntityRef derived,
						EntityRef source) {
	HadPrimarySource res = of.createHadPrimarySource();
	res.setId(id);
	res.setDerived(derived);
	res.setSource(source);
	return res;
    }

    public HadPrimarySource newHadPrimarySource(String id, EntityRef derived,
						EntityRef source) {
	HadPrimarySource res = of.createHadPrimarySource();
	res.setId(stringToQName(id));
	res.setDerived(derived);
	res.setSource(source);
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
	return newMentionOf(newEntityRef(infra), newEntityRef(supra),
			    newEntityRef(bundle));
    }

    public MentionOf newMentionOf(EntityRef infra, EntityRef supra,
				  EntityRef bundle) {
	MentionOf res = of.createMentionOf();
	res.setSpecializedEntity(infra);
	res.setBundle(bundle);
	res.setGeneralEntity(supra);
	return res;
    }



    public MentionOf newMentionOf(String infra, String supra, String bundle) {
	MentionOf res = of.createMentionOf();
	res.setSpecializedEntity(newEntityRef(infra));
	res.setBundle(newEntityRef(bundle));
	res.setGeneralEntity(newEntityRef(supra));
	return res;
    }
    
    public MentionOf newMentionOf(MentionOf r) {
	MentionOf res = of.createMentionOf();
	res.setSpecializedEntity(r.getSpecializedEntity());
	res.setBundle(r.getBundle());
	res.setGeneralEntity(r.getGeneralEntity());
	return res;
    }

    

    public NamedBundle newNamedBundle(QName id, Collection<Activity> ps,
				      Collection<Entity> as,
				      Collection<Agent> ags,
				      Collection<Statement> lks) {
	NamedBundle res = of.createNamedBundle();
	if (ps!=null) {
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
	return newUsed(id, null, null, null);
    }

    public Used newUsed(QName id, ActivityRef aid, String role, EntityRef eid) {
	Used res = of.createUsed();
	res.setId(id);
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

    public WasAssociatedWith newWasAssociatedWith(WasAssociatedWith u) {
    	WasAssociatedWith u1 = newWasAssociatedWith(u.getId(), u.getActivity(), u.getAgent());
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

    public WasAttributedTo newWasAttributedTo(WasAttributedTo u) {
    	WasAttributedTo u1 = newWasAttributedTo(u.getId(), u.getEntity(), u.getAgent());
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
					    GenerationRef did1,
					    UsageRef did2) {
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
					    GenerationRef did1,
					    UsageRef did2) {
	return newWasDerivedFrom(stringToQName(id), aid1, aid2, aid, did1, did2);
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

    /*
     * public void addType(HasExtensibility a, String type, String typeOfType) {
     * 
     * OldTypedLiteral tl=newOldTypedLiteral(type,typeOfType);
     * JAXBElement<OldTypedLiteral> je=of.createType(tl); addAttribute(a,je);
     * 
     * }
     */

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

    public WasEndedBy newWasEndedBy(WasEndedBy u) {
	WasEndedBy u1 = newWasEndedBy(u.getId(), u.getActivity(), u.getTrigger());
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
	return newWasGeneratedBy(id, null, null,
				 (org.openprovenance.prov.xml.ActivityRef) null);
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
	return newWasInfluencedBy(id, newAnyRef(influencee),
				  newAnyRef(influencer));
    }

    public WasInfluencedBy newWasInfluencedBy(WasInfluencedBy in) {
	WasInfluencedBy out = newWasInfluencedBy(in.getId(), in.getInfluencee(),
					     in.getInfluencer());
	out.setId(in.getId());
	out.getAny().addAll(in.getAny());
	out.getType().addAll(in.getType()); 
        out.getLabel().addAll(in.getLabel());
	return out;
    }

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

    // public void addType(HasExtensibility a,
    // String type) {
    //
    // addType(a,type,"xsd:anyURI");
    // }

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

    public WasInvalidatedBy newWasInvalidatedBy(WasInvalidatedBy u) {
	WasInvalidatedBy u1 = newWasInvalidatedBy(u.getId(), u.getEntity(), u.getActivity());
	u1.setTime(u.getTime());
	u1.getAny().addAll(u.getAny());
	u1.getType().addAll(u.getType()); 
        u1.getLabel().addAll(u.getLabel());
        u1.getLocation().addAll(u.getLocation());
	return u1;
    }

    public WasQuotedFrom newWasQuotedFrom(QName id, EntityRef quote,
					  EntityRef original) {
	WasQuotedFrom res = of.createWasQuotedFrom();
	res.setId(id);
	res.setQuote(quote);
	res.setOriginal(original);
	return res;
    }


    public WasQuotedFrom newWasQuotedFrom(String id, EntityRef quote,
					  EntityRef original) {
	WasQuotedFrom res = of.createWasQuotedFrom();
	res.setId(stringToQName(id));
	res.setQuote(quote);
	res.setOriginal(original);
	return res;
    }

    public WasRevisionOf newWasRevisionOf(QName id, EntityRef newer,
					  EntityRef older) {
	WasRevisionOf res = of.createWasRevisionOf();
	res.setId(id);
	res.setNewer(newer);
	res.setOlder(older);
	return res;
    }

    public WasRevisionOf newWasRevisionOf(String id, EntityRef newer,
					  EntityRef older) {
	WasRevisionOf res = of.createWasRevisionOf();
	res.setId(stringToQName(id));
	res.setNewer(newer);
	res.setOlder(older);
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

    public WasStartedBy newWasStartedBy(WasStartedBy u) {
	WasStartedBy u1 = newWasStartedBy(u.getId(), u.getActivity(), u.getTrigger());
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
    public void setNamespaces(Hashtable<String, String> nss) {
	namespaces = nss;
    }

    public QName stringToQName(String id) {
	if (id == null)
	    return null;
	int index = id.indexOf(':');
	if (index == -1) {
	    return new QName(namespaces.get("_"), id);
	}
	String prefix = id.substring(0, index);
	String local = id.substring(index + 1, id.length());
	if ("prov".equals(prefix)) {
	    return new QName(NamespacePrefixMapper.PROV_NS, local, prefix);
	} else if ("xsd".equals(prefix)) {
	    return new QName(NamespacePrefixMapper.XSD_NS + "#", // xsd
								 // namespace
								 // for rdf URIs
		    local, prefix);
	} else {
	    return new QName(namespaces.get(prefix), local, prefix);
	}
    }

    public Membership newMembership(EntityRef collection, EntityRef entity) {
        Membership res = of.createMembership();
        res.setCollection(collection);
        if (entity!=null)
            res.getEntity().add(entity);
        return res;
    }

    
}
