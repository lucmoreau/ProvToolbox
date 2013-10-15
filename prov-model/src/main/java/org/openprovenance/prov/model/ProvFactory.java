package org.openprovenance.prov.model;

import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.util.Date;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.bind.JAXBElement;
import java.util.GregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;


import org.openprovenance.prov.model.Attribute.AttributeKind;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** A stateless factory for PROV objects. */

//TODO: move the QNameExport capability outside the factory, and make it purely stateless, without namespace. 

public abstract class ProvFactory implements ModelConstructor, QNameExport, LiteralConstructor {

    static public DocumentBuilder builder;

    public static final String DEFAULT_NS = "_";

    public static final String packageList = "org.openprovenance.prov.xml:org.openprovenance.prov.xml.validation";

    static {
	initBuilder();
    }

    private static String fileName = "toolbox.properties";
    private static final String toolboxVersion = getPropertiesFromClasspath(fileName).getProperty("toolbox.version");

    public String getVersion() {
        return toolboxVersion;
    }

    private static Properties getPropertiesFromClasspath(String propFileName) {
        Properties props = new Properties();
        InputStream inputStream = ProvFactory.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputStream == null) {
            return null;
        }
        try {
            props.load(inputStream);
        } catch (IOException ee) {
            return null;
        }
        return props;   
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


    protected DatatypeFactory dataFactory;
    /** Note, this method now makes it stateful :-( */
    private Hashtable<String, String> namespaces = null;
    final protected ObjectFactory of;


    public ProvFactory(ObjectFactory of) {
	this.of = of;
	init();
	setNamespaces(new Hashtable<String, String>());
    }

    public ProvFactory(ObjectFactory of, Hashtable<String, String> namespaces) {
	this.of = of;
	this.namespaces = namespaces;
	init();
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
			     QName type) {

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
	a.getType().add(newType(newQName("prov:PrimarySource"),ValueConverter.QNAME_XSD_QNAME));
    }

    public void addQuotationType(HasType a) {
	a.getType().add(newType(newQName("prov:Quotation"),ValueConverter.QNAME_XSD_QNAME));
    }

    public void addRevisionType(HasType a) {
	a.getType().add(newType(newQName("prov:Revision"),ValueConverter.QNAME_XSD_QNAME));
    }

 
    public void addRole(HasRole a, Role role) {
	if (role != null) {
	    a.getRole().add(role);
	}
    }

    public void addType(HasType a, Type type) {

	a.getType().add(type);
    }

    public void addType(HasType a, URI type) {
	URIWrapper u = new URIWrapper();
	u.setValue(type);
	a.getType().add(newType(u,ValueConverter.QNAME_XSD_ANY_URI));
    }

    public void addType(HasType a, Object o, QName type) {
	a.getType().add(newType(o,type));
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

    public List<Type> getType(HasExtensibility e) {
	if (e instanceof HasType)
	    return ((HasType) e).getType();
	List<Type> res = new LinkedList<Type>();
	res.add(newType("pFact: type TODO",ValueConverter.QNAME_XSD_STRING));
	return res;
    }

  

    protected void init() {
	try {
	    dataFactory = DatatypeFactory.newInstance();
	} catch (DatatypeConfigurationException ex) {
	    throw new RuntimeException(ex);
	}
    }

    public ActedOnBehalfOf newActedOnBehalfOf(ActedOnBehalfOf u) {
	ActedOnBehalfOf u1 = newActedOnBehalfOf(u.getId(),
						u.getDelegate(),
						u.getResponsible(),
						u.getActivity());
	u1.getAny().addAll(u.getAny());
	return u1;
    }

    public ActedOnBehalfOf newActedOnBehalfOf(QName id, IDRef delegate,
					      IDRef responsible,
					      IDRef eid2) {
	ActedOnBehalfOf res = of.createActedOnBehalfOf();
	res.setId(id);
	res.setActivity(eid2);
	res.setDelegate(delegate);
	res.setResponsible(responsible);
	return res;
    }

    public ActedOnBehalfOf newActedOnBehalfOf(String id, IDRef delegate,
					      IDRef responsible,
					      IDRef eid2) {
	ActedOnBehalfOf res = of.createActedOnBehalfOf();
	res.setId(stringToQName(id));
	res.setActivity(eid2);
	res.setDelegate(delegate);
	res.setResponsible(responsible);
	return res;
    }
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName ag2, QName ag1, QName a, Collection<Attribute> attributes) {
        IDRef agid2=(ag2==null)? null : newIDRef(ag2);
        IDRef agid1=(ag1==null)? null : newIDRef(ag1);
        IDRef aid=(a==null)? null : newIDRef(a);
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
				Collection<Attribute> attributes) {
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

    public IDRef newIDRef(Activity p) {
	IDRef res = of.createIDRef();
	res.setRef(p.getId());
	return res;
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

    public Agent newAgent(QName id, Collection<Attribute> attributes) {
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

    public IDRef newIDRef(Agent a) {
	IDRef res = of.createIDRef();
	res.setRef(a.getId());
	return res;
    }

    public AlternateOf newAlternateOf(IDRef eid2, IDRef eid1) {
	AlternateOf res = of.createAlternateOf();
	res.setAlternate1(eid2);
	res.setAlternate2(eid1);
	return res;
    }
    

    public AlternateOf newAlternateOf(QName e2, QName e1) {
        IDRef eid2 = (e2==null)? null: newIDRef(e2);
        IDRef eid1 = (e1==null)? null: newIDRef(e1);
        return newAlternateOf(eid2, eid1);
    }
  
    public Attribute newAttribute(QName qname, Object value, ValueConverter vconv) {
  	Attribute res = createAttribute(qname, value, vconv.getXsdType(value));
  	return res;
      }

    public Attribute newAttribute(QName qname, Object value, QName type) {
  	Attribute res = createAttribute(qname, value, type);
  	return res;
      }

    public abstract Attribute createAttribute(QName qname, Object value, QName type);
    public abstract Attribute createAttribute(AttributeKind kind, Object value, QName type);
    
    public Attribute newAttribute(Attribute.AttributeKind kind, Object value, ValueConverter vconv) {
  	Attribute res = createAttribute(kind, value, vconv.getXsdType(value));
  	return res;
      }
    public Attribute newAttribute(Attribute.AttributeKind kind, Object value, QName type) {
  	Attribute res = createAttribute(kind, value, type);
  	return res;
      }



    public Attribute newAttribute(String namespace, String localName,
				  String prefix, Object value, ValueConverter vconv) {
	Attribute res = createAttribute(new QName(namespace, localName, prefix),
				      value, vconv.getXsdType(value));
	return res;
    }

    public Attribute newAttribute(String namespace, String localName,
				  String prefix, Object value, QName type) {
	Attribute res = createAttribute(new QName(namespace, localName, prefix),
				      value, type);
	return res;
    }

    public Location newLocation(Object value, ValueConverter vconv) {
        return newLocation(value,vconv.getXsdType(value));
      }

    public Location newLocation(Object value, QName type) {
        Location res =  of.createLocation();
        res.setType(type);
        res.setValueAsJava(value);
        //FIXME: how can I process a QNAME properly here
        /*
        if (value instanceof QName) {
            QName q=(QName)value;
            if ((q.getPrefix()==null) || (q.getPrefix()=="")) {
                res.getAttributes().put(new QName("http://www.w3.org/2000/xmlns/", "xmlns"), q.getNamespaceURI());
            } else {
                res.getAttributes().put(new QName("http://www.w3.org/2000/xmlns/",  q.getPrefix(), "xmlns"), q.getNamespaceURI());

            }
        }*/
        return res;
      }

    public Role newRole(Object value, ValueConverter vconv) {
        return newRole(value,vconv.getXsdType(value));
      }

    public Role newRole(Object value, QName type) {
	if (value==null) return null;
        Role res =  of.createRole();
        res.setType(type);
        res.setValueAsJava(value);
        return res;
      }

    public Type newType(Object value, ValueConverter vconv) {
        return newType(value,vconv.getXsdType(value));
      }

    public Type newType(Object value, QName type) {
	if (value==null) return null;
        Type res =  of.createType();
        res.setType(type);
        res.setValueAsJava(value);
        return res;
      }
    public Value newValue(Object value, ValueConverter vconv) {
        return newValue(value,vconv.getXsdType(value));
      }

    public Value newValue(Object value, QName type) {
	if (value==null) return null;
        Value res =  of.createValue();
        res.setType(type);
        res.setValueAsJava(value);
        return res;
      }

    public DictionaryMembership newDictionaryMembership(QName id, IDRef dict,
						    List<Entry> entitySet) {
	DictionaryMembership res = of.createDictionaryMembership();
	//res.setId(id);  TODO: no id?
	res.setDictionary(dict);
	if (entitySet != null)
	    res.getKeyEntityPair().addAll(entitySet);
	return res;
    }

    public DictionaryMembership newDictionaryMembership(String id, IDRef after,
						    List<Entry> entitySet) {
	return newDictionaryMembership(stringToQName(id), after, entitySet);
    }

    public DerivedByInsertionFrom newDerivedByInsertionFrom(QName id,
							    IDRef after,
							    IDRef before,
							    List<Entry> keyEntitySet,
							    Collection<Attribute> attributes) {
	DerivedByInsertionFrom res = of.createDerivedByInsertionFrom();
	res.setId(id);
	res.setNewDictionary(after);
	res.setOldDictionary(before);
	if (keyEntitySet != null)
	    res.getKeyEntityPair().addAll(keyEntitySet);
	setAttributes(res, attributes);
	return res;
    }
    
    public DerivedByInsertionFrom newDerivedByInsertionFrom(QName id,
                                                            QName after,
                                                            QName before,
                                                            List<KeyQNamePair> keyEntitySet,
                                                            Collection<Attribute> attributes) {
    	IDRef aa=createIDRef();
    	aa.setRef(after);
    	IDRef ab=createIDRef();
    	ab.setRef(before);
    	List<Entry> entries=new LinkedList<Entry>();
    	if (keyEntitySet!=null) {
    	for (KeyQNamePair p: keyEntitySet) {
    		Entry e=of.createEntry();
    		e.setKey(p.key);
        	IDRef ac=createIDRef();
        	ac.setRef(p.name);
    		e.setEntity(ac);
    		entries.add(e);
    	}
    	}
    	return newDerivedByInsertionFrom(id, aa, ab, entries, attributes);
    }

    abstract public IDRef createIDRef();
    
    public DerivedByInsertionFrom newDerivedByInsertionFrom(String id,
							    IDRef after,
							    IDRef before,
							    List<Entry> keyEntitySet, 
							    Collection<Attribute> attributes) {
	return newDerivedByInsertionFrom(stringToQName(id), after, before,
					 keyEntitySet,
					 attributes);
    }
    
    public DerivedByRemovalFrom newDerivedByRemovalFrom(QName id,
                                                            QName after,
                                                            QName before,
                                                            List<Object> keys,
                                                            Collection<Attribute> attributes) {
    	IDRef aa=createIDRef();
    	aa.setRef(after);
    	IDRef ab=createIDRef();
    	ab.setRef(before);
    	return newDerivedByRemovalFrom(id, aa, ab, keys, attributes);
    }


    public DerivedByRemovalFrom newDerivedByRemovalFrom(QName id,
							IDRef after,
							IDRef before,
							List<Object> keys,
							Collection<Attribute> attributes) {
	DerivedByRemovalFrom res = of.createDerivedByRemovalFrom();
	res.setId(id);
	res.setNewDictionary(after);
	res.setOldDictionary(before);
	if (keys != null)
	    res.getKey().addAll(keys);
	setAttributes(res, attributes);
	return res;
    }

    public DerivedByRemovalFrom newDerivedByRemovalFrom(String id,
							IDRef after,
							IDRef before,
							List<Object> keys,
							Collection<Attribute> attributes) {
	return newDerivedByRemovalFrom(stringToQName(id), after, before, keys, attributes);
    }
    

    public DictionaryMembership newDictionaryMembership(QName id, List<KeyQNamePair> keyEntitySet) {
	DictionaryMembership res = of.createDictionaryMembership();
    	IDRef idr=createIDRef();
    	idr.setRef(id);
	res.setDictionary(idr);
	
	List<Entry> entries=new LinkedList<Entry>();
    	if (keyEntitySet!=null) {
    	    for (KeyQNamePair p: keyEntitySet) {
    		Entry e=of.createEntry();
    		e.setKey(p.key);
    		IDRef ac=createIDRef();
    		ac.setRef(p.name);
    		e.setEntity(ac);
    		entries.add(e);
    	    }
    	}
	res.getKeyEntityPair().addAll(entries);
	return res;
    }



    /*    public DictionaryMembership newDictionaryMembership(QName id, IDRef after,
							List<Entry> keyEntitySet) {
	DictionaryMembership res = of.createDictionaryMembership();
	//res.setId(id);
	res.setEntity(after);
	if (keyEntitySet != null)
	    res.getEntry().addAll(keyEntitySet);
	return res;
    }

    public DictionaryMembership newDictionaryMembership(String id, IDRef after,
							List<Entry> keyEntitySet) {
	return newDictionaryMembership(stringToQName(id), after, keyEntitySet);
    }
    */

    public Document newDocument() {
	Document res = of.createDocument();
	return res;
    }

    /*
     * public Bundle newBundle(String id, Dictionary<Activity> ps,
     * Dictionary<Entity> as, Dictionary<Agent> ags, Dictionary<Object> lks) {
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
	res.getStatementOrBundle().addAll(ps);
	res.getStatementOrBundle().addAll(as);
	res.getStatementOrBundle().addAll(ags);
	res.getStatementOrBundle().addAll(lks);
	return res;
    }
    public Document newDocument(Hashtable<String, String> namespaces,
                                Collection<Statement> statements,
                                Collection<NamedBundle> bundles) {
	Document res = of.createDocument();
	res.setNss(namespaces);
	res.getStatementOrBundle()
	   .addAll(statements);
	res.getStatementOrBundle()
	   .addAll(bundles);
	return res;
    }

    public Document newDocument(Document graph) {
	Document res = of.createDocument();
	res.getStatementOrBundle()
	   .addAll(graph.getStatementOrBundle());
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

    public Element newElement(QName qname, String value, QName type) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(qname.getNamespaceURI(),
	                                 qnameToString(qname));
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", qnameToString(type));
	el.appendChild(doc.createTextNode(value));
	doc.appendChild(el);
	return el;
    }

    public Element newElement(QName qname, String value, QName type,
			      String lang) {
	org.w3c.dom.Document doc = builder.newDocument();
	Element el = doc.createElementNS(qname.getNamespaceURI(),
	                                 qnameToString(qname));				 
	el.setAttributeNS(NamespacePrefixMapper.XSI_NS, "xsi:type", qnameToString(type));
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

    public Entity newEntity(QName id, Collection<Attribute> attributes) {
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

    public IDRef newIDRef(Entity a) {
	IDRef res = of.createIDRef();
	res.setRef(a.getId());
	return res;
    }

    public IDRef newIDRef(QName id) {
	IDRef res = of.createIDRef();
	res.setRef(id);
	return res;
    }

    public IDRef newIDRef(String id) {
	return newIDRef(stringToQName(id));
    }

    public Entry newEntry(Object key, IDRef entity) {
	Entry res = of.createEntry();
	res.setKey(key);
	res.setEntity(entity);
	return res;
    }

    public IDRef newIDRef(WasGeneratedBy edge) {
	IDRef res = of.createIDRef();
	res.setRef(edge.getId());
	return res;
    }

    public HadMember newHadMember(IDRef collection, IDRef... entities) {
	HadMember res = of.createHadMember();
	res.setCollection(collection);
	if (entities != null) {
	    res.getEntity().addAll(Arrays.asList(entities));
	}
	return res;
    }
    public HadMember newHadMember(QName c, Collection<QName> e) {
        IDRef cid=(c==null)? null: newIDRef(c);
        List<IDRef> ll=new LinkedList<IDRef>();
        if (e!=null) {
            for (QName q: e) {
        	IDRef eid=newIDRef(q);
        	ll.add(eid);
            }
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

    public XMLGregorianCalendar newGYear(String year) {
        XMLGregorianCalendar cal=dataFactory.newXMLGregorianCalendar();
        cal.setYear(new Integer(year));
        return cal;
    }

  
    
    public MentionOf newMentionOf(Entity infra, Entity supra, Entity bundle) {
	return newMentionOf((infra == null) ? null : newIDRef(infra),
			    (supra == null) ? null : newIDRef(supra),
			    (bundle == null) ? null : newIDRef(bundle));
    }
    public MentionOf newMentionOf(QName e2, QName e1, QName b) {
        IDRef eid2 = (e2==null)? null: newIDRef(e2);
        IDRef eid1 = (e1==null)? null: newIDRef(e1);
        IDRef bid = (b==null)? null: newIDRef(b);
        return newMentionOf(eid2, eid1, bid);
    }

    
    public MentionOf newMentionOf(IDRef infra, IDRef supra,
				  IDRef bundle) {
	MentionOf res = of.createMentionOf();
	res.setSpecificEntity(infra);
	res.setBundle(bundle);
	res.setGeneralEntity(supra);
	return res;
    }

    
    public MentionOf newMentionOf(MentionOf r) {
	MentionOf res = of.createMentionOf();
	res.setSpecificEntity(r.getSpecificEntity());
	res.setBundle(r.getBundle());
	res.setGeneralEntity(r.getGeneralEntity());
	return res;
    }

    public MentionOf newMentionOf(String infra, String supra, String bundle) {
	MentionOf res = of.createMentionOf();
	if (supra != null)
	    res.setSpecificEntity(newIDRef(infra));
	if (bundle != null)
	    res.setBundle(newIDRef(bundle));
	if (supra != null)
	    res.setGeneralEntity(newIDRef(supra));
	return res;
    }

    public NamedBundle newNamedBundle(QName id, Collection<Activity> ps,
				      Collection<Entity> as,
				      Collection<Agent> ags,
				      Collection<Statement> lks) {
	NamedBundle res = of.createNamedBundle();
	res.setId(id);

	if (ps != null) {
	    res.getStatement().addAll(ps);
	}
	if (as != null) {
	    res.getStatement().addAll(as);
	}
	if (ags != null) {
	    res.getStatement().addAll(ags);
	}
	if (lks != null) {
	    res.getStatement().addAll(lks);
	}
	return res;
    }

    public NamedBundle newNamedBundle(QName id, Collection<Statement> lks) {
	NamedBundle res = of.createNamedBundle();
	res.setId(id);

	if (lks != null) {
	    res.getStatement().addAll(lks);
	}
	return res;
    }

    public NamedBundle newNamedBundle(QName id, Hashtable<String,String> namespaces, Collection<Statement> statements) {
	NamedBundle res = of.createNamedBundle();
	res.setId(id);
	res.setNss(namespaces);
	if (statements != null) {
	    res.getStatement().addAll(statements);
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


    public SpecializationOf newSpecializationOf(IDRef eid2, IDRef eid1) {
	SpecializationOf res = of.createSpecializationOf();
	res.setSpecificEntity(eid2);
	res.setGeneralEntity(eid1);
	return res;
    }

    public SpecializationOf newSpecializationOf(QName e2, QName e1) {
        IDRef eid2 = (e2==null)? null: newIDRef(e2);
        IDRef eid1 = (e1==null)? null: newIDRef(e1);
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


    public IDRef newIDRef(Used edge) {
	IDRef res = of.createIDRef();
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

    public Used newUsed(QName id, IDRef aid, String role, IDRef eid) {
	Used res = newUsed(id);
	res.setActivity(aid);
	addRole(res, newRole(role,ValueConverter.QNAME_XSD_STRING));
	res.setEntity(eid);
	return res;
    }

    public Used newUsed(String id, Activity p, String role, Entity a) {
	IDRef pid = newIDRef(p);
	IDRef aid = newIDRef(a);
	return newUsed(stringToQName(id), pid, role, aid);
    }

    public Used newUsed(String id, Activity p, String role, Entity a,
			String type) {
	Used res = newUsed(id, p, role, a);
	addType(res, newType(type,ValueConverter.QNAME_XSD_STRING));
	return res;
    }

    public Used newUsed(String id, IDRef pid, String role, IDRef aid) {
	Used res = of.createUsed();
	res.setId(stringToQName(id));
	res.setActivity(pid);
	addRole(res, newRole(role,ValueConverter.QNAME_XSD_STRING));
	return res;
    }
    
    public Used newUsed(QName id, QName activity, QName entity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
   	IDRef aid = (activity==null)? null: newIDRef(activity);
        IDRef eid = (entity==null)? null: newIDRef(entity);
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
	return newWasAssociatedWith(id, newIDRef(eid2.getId()),
				    newIDRef(eid1.getId()));
    }

    public WasAssociatedWith newWasAssociatedWith(QName id, IDRef eid2,
						  IDRef eid1) {
	WasAssociatedWith res = of.createWasAssociatedWith();
	res.setId(id);
	res.setActivity(eid2);
	res.setAgent(eid1);
	return res;
    }

    public WasAssociatedWith newWasAssociatedWith(String id, Activity eid2,
						  Agent eid1) {
	return newWasAssociatedWith(id, newIDRef(eid2.getId()),
				    newIDRef(eid1.getId()));
    }

    public WasAssociatedWith newWasAssociatedWith(String id, IDRef eid2,
						  IDRef eid1) {
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
                                                   Collection<Attribute> attributes) {
	IDRef aid=(a==null)? null: newIDRef(a);
	IDRef eid=(plan==null)? null: newIDRef(plan);
	IDRef agid=(ag==null)? null: newIDRef(ag);
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

    public WasAttributedTo newWasAttributedTo(QName id, IDRef eid,
					      IDRef agid) {
	WasAttributedTo res = of.createWasAttributedTo();
	res.setId(id);
	res.setEntity(eid);
	res.setAgent(agid);
	return res;
    }

    public WasAttributedTo newWasAttributedTo(String id, IDRef eid,
					      IDRef agid) {
	return newWasAttributedTo(stringToQName(id), eid, agid);
    }

    
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,  Collection<Attribute> attributes) {
        IDRef eid=(e==null)? null : newIDRef(e);
        IDRef agid=(ag==null)? null : newIDRef(ag);
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

    public WasDerivedFrom newWasDerivedFrom(QName id, IDRef aid1,
					    IDRef aid2) {
	WasDerivedFrom res = of.createWasDerivedFrom();
	res.setId(id);
	res.setUsedEntity(aid2);
	res.setGeneratedEntity(aid1);
	return res;
    }

    public WasDerivedFrom newWasDerivedFrom(QName id, IDRef aid1,
					    IDRef aid2, IDRef aid,
					    IDRef did1, IDRef did2) {
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
	IDRef aid1 = newIDRef(a1);
	IDRef aid2 = newIDRef(a2);
	return newWasDerivedFrom(id, aid1, aid2);
    }

    public WasDerivedFrom newWasDerivedFrom(String id, Entity e2, Entity e1,
					    Activity a, WasGeneratedBy g2,
					    Used u1) {
	IDRef eid1 = newIDRef(e1);
	IDRef eid2 = newIDRef(e2);
	IDRef aid = newIDRef(a);
	IDRef did2 = newIDRef(g2);
	IDRef did1 = newIDRef(u1);
	return newWasDerivedFrom(id, eid2, eid1, aid, did2, did1);
    }

    public WasDerivedFrom newWasDerivedFrom(String id, Entity a1, Entity a2,
					    String type) {
	WasDerivedFrom wdf = newWasDerivedFrom(id, a1, a2);
	addType(wdf, newType(type,ValueConverter.QNAME_XSD_STRING));
	return wdf;
    }


    public WasDerivedFrom newWasDerivedFrom(String id, IDRef aid1,
					    IDRef aid2) {
	return newWasDerivedFrom(stringToQName(id), aid1, aid2);
    }

    public WasDerivedFrom newWasDerivedFrom(String id, IDRef aid1,
					    IDRef aid2, IDRef aid,
					    IDRef did1, IDRef did2) {
	return newWasDerivedFrom(stringToQName(id), aid1, aid2, aid, did1, did2);
    }

    public WasDerivedFrom newWasDerivedFrom(QName id, QName e2, QName e1, QName a, QName g, QName u,  Collection<Attribute> attributes) {
	IDRef eid1 = (e1==null)? null: newIDRef(e1);
	IDRef eid2 = (e2==null)? null: newIDRef(e2);
	IDRef aid = (a==null)? null : newIDRef(a);
	IDRef gid = (g==null)? null: newIDRef(g);
	IDRef uid = (u==null) ? null : newIDRef(u);
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
    
    public WasEndedBy newWasEndedBy(QName id, IDRef aid, IDRef eid) {
	WasEndedBy res = of.createWasEndedBy();
	res.setId(id);
	res.setActivity(aid);
	res.setTrigger(eid);
	return res;
    }

    public WasEndedBy newWasEndedBy(String id, IDRef aid, IDRef eid) {
	return newWasEndedBy(stringToQName(id), aid, eid);
    }
    
    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger, QName ender, XMLGregorianCalendar time, Collection<Attribute> attributes) {
   	IDRef aid = (activity==null)? null: newIDRef(activity);
      	IDRef eid = (trigger==null)? null: newIDRef(trigger);
      	IDRef sid = (ender==null)? null: newIDRef(ender);
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
	IDRef aid = newIDRef(a);
	IDRef pid = newIDRef(p);
	return newWasGeneratedBy(id, aid, role, pid);
    }

    public WasGeneratedBy newWasGeneratedBy(QName id, IDRef aid,
					    String role, IDRef pid) {
	WasGeneratedBy res = of.createWasGeneratedBy();
	res.setId(id);
	res.setActivity(pid);
	res.setEntity(aid);
	addRole(res, newRole(role,ValueConverter.QNAME_XSD_STRING));
	return res;
    }

    public WasGeneratedBy newWasGeneratedBy(String id, Entity a, String role,
					    Activity p) {
	IDRef aid = newIDRef(a);
	IDRef pid = newIDRef(p);
	return newWasGeneratedBy(stringToQName(id), aid, role, pid);
    }

    public WasGeneratedBy newWasGeneratedBy(String id, Entity a, String role,
					    Activity p, String type) {
	WasGeneratedBy wgb = newWasGeneratedBy(id, a, role, p);
	addType(wgb, newType(type,ValueConverter.QNAME_XSD_STRING));
	return wgb;
    }

    public WasGeneratedBy newWasGeneratedBy(String id, IDRef aid,
					    String role, IDRef pid) {
	return newWasGeneratedBy(stringToQName(id), aid, role, pid);
    }
    
    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity, QName activity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
   	IDRef aid = (activity==null)? null: newIDRef(activity);
   	IDRef eid = (entity==null)? null: newIDRef(entity);
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

    public WasInfluencedBy newWasInfluencedBy(QName id, IDRef influencee,
					      IDRef influencer) {
	WasInfluencedBy res = of.createWasInfluencedBy();
	res.setId(id);
	res.setInfluencee(influencee);
	res.setInfluencer(influencer);
	return res;
    }

    public WasInfluencedBy newWasInfluencedBy(String id, IDRef influencee,
					      IDRef influencer) {
	WasInfluencedBy res = of.createWasInfluencedBy();
	res.setId(stringToQName(id));
	res.setInfluencee(influencee);
	res.setInfluencer(influencer);
	return res;
    }

    public WasInfluencedBy newWasInfluencedBy(String id, String influencee,
					      String influencer) {
	return newWasInfluencedBy(id, (influencee == null) ? null
		: newIDRef(influencee), (influencer == null) ? null
		: newIDRef(influencer));
    }
    
    
    public WasInfluencedBy newWasInfluencedBy(QName id, QName a2, QName a1, Collection<Attribute> attributes) {
        IDRef aid2 = (a2==null) ? null: newIDRef(a2);
        IDRef aid1 = (a1==null) ? null: newIDRef(a1);
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


    public WasInformedBy newWasInformedBy(Activity p1, Activity p2) {
	return newWasInformedBy(null, p1, p2);
    }

    public WasInformedBy newWasInformedBy(QName id, Activity p1, Activity p2,
					  String type) {
	WasInformedBy wtb = newWasInformedBy(p1, p2);
	wtb.setId(id);
	addType(wtb, newType(type,ValueConverter.QNAME_XSD_STRING));
	return wtb;
    }

    public WasInformedBy newWasInformedBy(QName id, IDRef pid1,
					  IDRef pid2) {
	WasInformedBy res = of.createWasInformedBy();
	res.setId(id);
	res.setInformed(pid1);
	res.setInformant(pid2);
	return res;
    }

    public WasInformedBy newWasInformedBy(String id, Activity p1, Activity p2) {
	IDRef pid1 = newIDRef(p1);
	IDRef pid2 = newIDRef(p2);
	return newWasInformedBy(id, pid1, pid2);
    }

    public WasInformedBy newWasInformedBy(String id, Activity p1, Activity p2,
					  String type) {
	return newWasInformedBy(stringToQName(id), p1, p2, type);
    }

    public WasInformedBy newWasInformedBy(String id, IDRef pid1,
					  IDRef pid2) {
	return newWasInformedBy(stringToQName(id), pid1, pid2);
    }

    public WasInformedBy newWasInformedBy(QName id, QName a2, QName a1, Collection<Attribute> attributes) {
        IDRef aid2 = (a2==null) ? null: newIDRef(a2);
        IDRef aid1 = (a1==null) ? null: newIDRef(a1);
        WasInformedBy res=newWasInformedBy(id,aid2,aid1);   
        setAttributes(res, attributes);
        return res;
    }
    
    public WasInformedBy newWasInformedBy(WasInformedBy d) {
	WasInformedBy wtb = newWasInformedBy(d.getId(), 
					     d.getInformed(),
					     d.getInformant());
	wtb.setId(d.getId());
	wtb.getAny().addAll(d.getAny());
	wtb.getType().addAll(d.getType());
	wtb.getLabel().addAll(d.getLabel());
	return wtb;
    }

    public WasInvalidatedBy newWasInvalidatedBy(QName id, IDRef eid,
						IDRef aid) {
	WasInvalidatedBy res = of.createWasInvalidatedBy();
	res.setId(id);
	res.setEntity(eid);
	res.setActivity(aid);
	return res;
    }

    public WasInvalidatedBy newWasInvalidatedBy(String id, IDRef eid,
						IDRef aid) {
	return newWasInvalidatedBy(stringToQName(id), eid, aid);
    }
    
    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity, QName activity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
   	IDRef aid = (activity==null) ? null: newIDRef(activity);
   	IDRef eid = (entity==null)? null: newIDRef(entity);
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
    
    public WasStartedBy newWasStartedBy(QName id, IDRef aid, IDRef eid) {
	WasStartedBy res = of.createWasStartedBy();
	res.setId(id);
	res.setActivity(aid);
	res.setTrigger(eid);
	return res;
    }

    public WasStartedBy newWasStartedBy(String id, IDRef aid,
					IDRef eid) {
	return newWasStartedBy(stringToQName(id), aid, eid);
    }
    
    public WasStartedBy newWasStartedBy(QName id, QName activity, QName trigger, QName starter, XMLGregorianCalendar time, Collection<Attribute> attributes) {
   	IDRef aid = (activity==null)? null: newIDRef(activity);
      	IDRef eid = (trigger==null)? null: newIDRef(trigger);
      	IDRef sid = (starter==null)? null: newIDRef(starter);
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


 
    public void setAttributes(HasExtensibility res, Collection<Attribute> attributes) {
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
		    loc.getLocation().add(newLocation(aValue,attr.getXsdType()));
		}
		break;
	    case PROV_ROLE:
		if (rol!=null) {
		    rol.getRole().add(newRole(aValue,attr.getXsdType()));
		}
		break;
	    case PROV_TYPE: 
		if (typ!=null) {
		    typ.getType().add(newType(aValue,attr.getXsdType()));
		}
		break;
	    case PROV_VALUE:
		if (aval!=null) {
		    aval.setValue(newValue(aValue,attr.getXsdType()));
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
    public QName newQName(String qnameAsString) {
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
