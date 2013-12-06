package org.openprovenance.prov.model;

import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.bind.JAXBElement;
import java.util.GregorianCalendar;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;




/** A stateless factory for PROV objects. */


public abstract class ProvFactory implements LiteralConstructor, ModelConstructor {

    public static final String DEFAULT_NS = "_";
 
    public static final String packageList = "org.openprovenance.prov.xml:org.openprovenance.prov.xml.validation";

    private static String fileName = "toolbox.properties";
    
    private static final String toolboxVersion = getPropertiesFromClasspath(fileName).getProperty("toolbox.version");

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

    public static String printURI(java.net.URI u) {
	return u.toString();
    }

  
    protected DatatypeFactory dataFactory;



    final protected ObjectFactory of;
    public ProvFactory(ObjectFactory of) {
	this.of = of;
	init();
    }



    public void addAttribute(HasOther a, Other o) {
	a.getOther().add(o);
    }



    public ActedOnBehalfOf addAttributes(ActedOnBehalfOf from,
					 ActedOnBehalfOf to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getOther().addAll(from.getOther());
	return to;
    }

   
    public Activity addAttributes(Activity from, Activity to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public Agent addAttributes(Agent from, Agent to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	// to.getLocation().addAll(from.getLocation());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public Entity addAttributes(Entity from, Entity to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public Used addAttributes(Used from, Used to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasAssociatedWith addAttributes(WasAssociatedWith from,
					   WasAssociatedWith to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getRole().addAll(from.getRole());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasAttributedTo addAttributes(WasAttributedTo from,
					 WasAttributedTo to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasDerivedFrom addAttributes(WasDerivedFrom from, WasDerivedFrom to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasEndedBy addAttributes(WasEndedBy from, WasEndedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasGeneratedBy addAttributes(WasGeneratedBy from, WasGeneratedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasInfluencedBy addAttributes(WasInfluencedBy from,
					 WasInfluencedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasInformedBy addAttributes(WasInformedBy from, WasInformedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasInvalidatedBy addAttributes(WasInvalidatedBy from,
					  WasInvalidatedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getOther().addAll(from.getOther());
	return to;
    }

    public WasStartedBy addAttributes(WasStartedBy from, WasStartedBy to) {
	to.getLabel().addAll(from.getLabel());
	to.getType().addAll(from.getType());
	to.getLocation().addAll(from.getLocation());
	to.getRole().addAll(from.getRole());
	to.getOther().addAll(from.getOther());	
	return to;
    }

    public void addLabel(HasLabel a, String label) {
	a.getLabel().add(newInternationalizedString(label));
    }

    public void addLabel(HasLabel a, String label, String language) {
	a.getLabel().add(newInternationalizedString(label, language));
    }

    public void addPrimarySourceType(HasType a) {
	a.getType().add(newType(getName().QNAME_PROV_PRIMARY_SOURCE,
	                        getName().QNAME_XSD_QNAME));
    }

    public void addQuotationType(HasType a) {
	a.getType().add(newType(getName().QNAME_PROV_QUOTATION,getName().QNAME_XSD_QNAME));
    }

    public void addRevisionType(HasType a) {
	a.getType().add(newType(getName().QNAME_PROV_REVISION,getName().QNAME_XSD_QNAME));
    }

    public void addBundleType(HasType a) {
	a.getType().add(newType(getName().QNAME_PROV_BUNDLE,getName().QNAME_XSD_QNAME));
    }

    public void addRole(HasRole a, Role role) {
	if (role != null) {
	    a.getRole().add(role);
	}
    }

 
    public void addType(HasType a, Object o, QualifiedName type) {
	a.getType().add(newType(o,type));
    }

    public void addType(HasType a, Type type) {

	a.getType().add(type);
    }

    public void addType(HasType a, URI type) {
	URIWrapper u = new URIWrapper();
	u.setValue(type);
	a.getType().add(newType(u,getName().QNAME_XSD_ANY_URI));
    }

    public byte [] base64Decoding(String s) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(s);
    }

 
 


    public String base64Encoding(byte [] b) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(b);
    }


    /* Return the first label, it it exists */
    public String getLabel(HasOther e) {

	List<InternationalizedString> labels = ((HasLabel) e).getLabel();
	if ((labels == null) || (labels.isEmpty()))
	    return null;
	if (e instanceof HasLabel)
	    return labels.get(0).getValue();
	return "pFact: label TODO";
    }

    private Name name=null;
    public Name getName() {
	if (name==null) {
	    name=new Name(this);
	}
	return name;
    }

    public ObjectFactory getObjectFactory() {
	return of;
    }

    public String getPackageList() {
	return packageList;
    }

  

    public String getRole(HasOther e) {
	return "pFact: role TODO";
    }

    public List<Type> getType(HasOther e) {
	if (e instanceof HasType)
	    return ((HasType) e).getType();
	List<Type> res = new LinkedList<Type>();
	res.add(newType("pFact: type TODO",getName().QNAME_XSD_STRING));
	return res;
    }

    public String getVersion() {
        return toolboxVersion;
    }

    public byte [] hexDecoding(String s) {
        try {
            return org.apache.commons.codec.binary.Hex.decodeHex(s.toCharArray());
        } catch  (Exception e) {
            return s.getBytes(); // fall back, but obviously, this is not converted
        }
     
    }
    public String hexEncoding(byte [] b) {
        return org.apache.commons.codec.binary.Hex.encodeHexString(b);
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
						u.getActivity(),
						null);
	u1.getOther().addAll(u.getOther());
	return u1;
    }

    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, 
                                              QualifiedName delegate,
					      QualifiedName responsible,
					      QualifiedName activity) {
	ActedOnBehalfOf res = of.createActedOnBehalfOf();
	res.setId(id);
	res.setActivity(activity);
	res.setDelegate(delegate);
	res.setResponsible(responsible);
	return res;
    }    
    
    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, 
                                              QualifiedName delegate,
     					      QualifiedName responsible,
     					      QualifiedName activity, 
     					      Collection<Attribute> attributes) {
     	ActedOnBehalfOf res = of.createActedOnBehalfOf();
     	res.setId(id);
     	res.setActivity(activity);
     	res.setDelegate(delegate);
     	res.setResponsible(responsible);
	setAttributes(res, attributes);
     	return res;
    }

    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, QualifiedName ag2, QualifiedName ag1) {
        ActedOnBehalfOf res=newActedOnBehalfOf(id, ag2, ag1, null,null);
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

    public Activity newActivity(QualifiedName a) {
	Activity res = of.createActivity();
	res.setId(a);
	return res;
    }

    public Activity newActivity(QualifiedName q, String label) {
	Activity res = newActivity(q);
	if (label != null)
	    res.getLabel().add(newInternationalizedString(label));
	return res;
    }

    public Activity newActivity(QualifiedName id, 
                                XMLGregorianCalendar startTime,
				XMLGregorianCalendar endTime,
				Collection<Attribute> attributes) {
	Activity res = newActivity(id);
	res.setStartTime(startTime);
	res.setEndTime(endTime);
	setAttributes(res, attributes);
	return res;

    }

    public Agent newAgent(Agent a) {
	Agent res = newAgent(a.getId());
	res.getType().addAll(a.getType());
	res.getLabel().addAll(a.getLabel());
	return res;
    }

    public Agent newAgent(QualifiedName ag) {
	Agent res = of.createAgent();
	res.setId(ag);
	return res;
    }

    public Agent newAgent(QualifiedName id, Collection<Attribute> attributes) {
	Agent res = newAgent(id);
	setAttributes(res, attributes);
	return res;
    }

    public Agent newAgent(QualifiedName ag, String label) {
	Agent res = newAgent(ag);
	if (label != null)
	    res.getLabel().add(newInternationalizedString(label));
	return res;
    }
    

    public AlternateOf newAlternateOf(QualifiedName eid2, QualifiedName eid1) {
	AlternateOf res = of.createAlternateOf();
	res.setAlternate1(eid2);
	res.setAlternate2(eid1);
	return res;
    }



   abstract public org.openprovenance.prov.model.Attribute newAttribute(QualifiedName elementName, Object value, QualifiedName type) ;
   
    abstract public org.openprovenance.prov.model.Attribute newAttribute(Attribute.AttributeKind kind, Object value, QualifiedName type);
 
    public Attribute newAttribute(String namespace, String localName,
				  String prefix, Object value, QualifiedName type) {
	Attribute res;
	res = newAttribute(newQualifiedName(namespace, localName, prefix),
	                   value, type);	
	return res;
    }

    public DerivedByInsertionFrom newDerivedByInsertionFrom(QualifiedName id,
							    QualifiedName after,
							    QualifiedName before,
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

   

    public DerivedByRemovalFrom newDerivedByRemovalFrom(QualifiedName id,
							QualifiedName after,
							QualifiedName before,
							List<Key> keys,
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

  
    public DictionaryMembership newDictionaryMembership(QualifiedName dict,
                                                        List<Entry> entitySet) {
	DictionaryMembership res = of.createDictionaryMembership();
	res.setDictionary(dict);
	if (entitySet != null)
	    res.getKeyEntityPair().addAll(entitySet);
	return res;
    }

    public Document newDocument() {
	Document res = of.createDocument();
	return res;
    }

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
    
    public Document newDocument(Document graph) {
	Document res = of.createDocument();
	res.getStatementOrBundle()
	   .addAll(graph.getStatementOrBundle());
	return res;
    }


    public Document newDocument(Namespace namespace,
                                Collection<Statement> statements,
                                Collection<NamedBundle> bundles) {
	Document res = of.createDocument();
	res.setNamespace(namespace);
	res.getStatementOrBundle()
	   .addAll(statements);
	res.getStatementOrBundle()
	   .addAll(bundles);
	return res;
    }

    public Duration newDuration(int durationInMilliSeconds) {
        Duration dur=dataFactory.newDuration(durationInMilliSeconds);
        return dur;
    }
    

    public Duration newDuration(String lexicalRepresentation) {
        Duration dur=dataFactory.newDuration(lexicalRepresentation);
        return dur;
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

    public Entity newEntity(QualifiedName id) {
	Entity res = of.createEntity();
	res.setId(id);
	return res;
    }

    public Entity newEntity(QualifiedName id, Collection<Attribute> attributes) {
	Entity res = newEntity(id);
	setAttributes(res, attributes);
	return res;
    }

    public Entity newEntity(QualifiedName id, String label) {
	Entity res = newEntity(id);
	if (label != null)
	    res.getLabel().add(newInternationalizedString(label));
	return res;
    }

    public Entry newEntry(Key key, QualifiedName entity) {
	Entry res = of.createEntry();
	res.setKey(key);
	res.setEntity(entity);
	return res;
    }

    public XMLGregorianCalendar newGDay(int day) {
        XMLGregorianCalendar cal=dataFactory.newXMLGregorianCalendar();
        cal.setDay(day);
        return cal;
    }

    public XMLGregorianCalendar newGMonth(int month) {
        XMLGregorianCalendar cal=dataFactory.newXMLGregorianCalendar();
        cal.setMonth(month);
        return cal;
    }

    public XMLGregorianCalendar newGMonthDay(int month, int day) {
        XMLGregorianCalendar cal=dataFactory.newXMLGregorianCalendar();
        cal.setMonth(month);
        cal.setDay(day);
        return cal;
    }

    public XMLGregorianCalendar newGYear(int year) {
        XMLGregorianCalendar cal=dataFactory.newXMLGregorianCalendar();
        cal.setYear(year);
        return cal;
    }


    public HadMember newHadMember(QualifiedName collection, QualifiedName... entities) {
        HadMember res = of.createHadMember();
        res.setCollection(collection);
        if (entities != null) {
            res.getEntity().addAll(Arrays.asList(entities));
        }
        return res;
    }


    public HadMember newHadMember(QualifiedName c, Collection<QualifiedName> e) {
        List<QualifiedName> ll=new LinkedList<QualifiedName>();
        if (e!=null) {
            for (QualifiedName q: e) {
        	ll.add(q);
            }
        }
        HadMember res = of.createHadMember();
        res.setCollection(c);
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

    public Key newKey(Object o, QualifiedName type) {
    	Key res=of.createKey();
    	res.setType(type);
    	res.setValueAsObject(o);
    	return res;
       }


    public Location newLocation(Object value, QualifiedName type) {
            Location res =  of.createLocation();
            res.setType(type);
            res.setValueAsObject(value);
            return res;
          }

  
   
    public MentionOf newMentionOf(QualifiedName infra, 
                                  QualifiedName supra,
				  QualifiedName bundle) {
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
   
    
    public NamedBundle newNamedBundle(QualifiedName id, 
                                      Collection<Activity> ps,
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

  
    public NamedBundle newNamedBundle(QualifiedName id, Collection<Statement> lks) {
	NamedBundle res = of.createNamedBundle();
	res.setId(id);

	if (lks != null) {
	    res.getStatement().addAll(lks);
	}
	return res;
    }
    public NamedBundle newNamedBundle(QualifiedName id, Namespace namespace, Collection<Statement> statements) {
	NamedBundle res = of.createNamedBundle();
	res.setId(id);
	res.setNamespace(namespace);
	if (statements != null) {
	    res.getStatement().addAll(statements);
	}
	return res;
    }

    

    public Other newOther(QualifiedName elementName, Object value, QualifiedName type) {
	if (value==null) return null;
        Other res =  of.createOther();
        res.setType(type);
        res.setValueAsObject(value);
        res.setElementName(elementName);
        return res;
      }

    public Other newOther(String namespace, String local, String prefix,  Object value, QualifiedName type) {
	QualifiedName elementName=newQualifiedName(namespace,local,prefix);
        return newOther(elementName,value,type);
    }
    
    
    abstract public QualifiedName newQualifiedName(String namespace, String local, String prefix);
    
    /* A convenience function. */
    public QualifiedName newQualifiedName(javax.xml.namespace.QName qname) {
	return newQualifiedName(qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
    }
    
    
    public Role newRole(Object value, QualifiedName type) {
	if (value==null) return null;
        Role res =  of.createRole();
        res.setType(type);
        res.setValueAsObject(value);
        return res;
      }

    public SpecializationOf newSpecializationOf(QualifiedName eid2, QualifiedName eid1) {
	SpecializationOf res = of.createSpecializationOf();
	res.setSpecificEntity(eid2);
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
  
    public Type newType(Object value, QualifiedName type) {
	if (value==null) return null;
        Type res =  of.createType();
        res.setType(type);
        res.setValueAsObject(value);
        return res;
    }

    public Type newType(Object value, ValueConverter vconv) {
	return newType(value, vconv.getXsdType(value));
    }


    public Used newUsed(QualifiedName id) {
	Used res = of.createUsed();
	res.setId(id);
	return res;
    }

    public Used newUsed(QualifiedName id, QualifiedName aid, String role, QualifiedName eid) {
	Used res = newUsed(id);
	res.setActivity(aid);
	if (role!=null)
	addRole(res, newRole(role,getName().QNAME_XSD_STRING));
	res.setEntity(eid);
	return res;
    }
    
    /** A factory method to create an instance of a usage {@link Used}
     * @param id an optional identifier for a usage
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
     * @return an instance of {@link Used}
     */    

    public Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity) {
 	Used res = newUsed(id);
	res.setActivity(activity);
	res.setEntity(entity);
 	return res;
     }

    public Used newUsed(QualifiedName activity, QualifiedName entity) {
 	Used res = newUsed((QualifiedName)null);
	res.setActivity(activity);
	res.setEntity(entity);
 	return res;
     }

     /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newUsed(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity,
			XMLGregorianCalendar time,
			Collection<Attribute> attributes) {
	Used res = newUsed(id, activity, null, entity);
	res.setTime(time);
	setAttributes(res, attributes);
	return res;
    }


    

    public Used newUsed(Used u) {
	Used u1 = newUsed(u.getId(), u.getActivity(), u.getEntity(),null,null);
	u1.getOther().addAll(u.getOther());
	u1.setTime(u.getTime());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	u1.getLocation().addAll(u.getLocation());
	return u1;
    }

    
    

    public Value newValue(Object value, QualifiedName type) {
	if (value==null) return null;
        Value res =  of.createValue();
        res.setType(type);
        res.setValueAsObject(value);
        return res;
      }


    public WasAssociatedWith newWasAssociatedWith(QualifiedName id) {
	return newWasAssociatedWith(id, (QualifiedName)null,(QualifiedName)null);
    }
    public WasAssociatedWith newWasAssociatedWith(QualifiedName id,
                                                  Activity eid2,
						  Agent eid1) {
	return newWasAssociatedWith(id, eid2.getId(), eid1.getId());
    }

    public WasAssociatedWith newWasAssociatedWith(QualifiedName id,
                                                  QualifiedName eid2,
						  QualifiedName eid1) {
	WasAssociatedWith res = of.createWasAssociatedWith();
	res.setId(id);
	res.setActivity(eid2);
	res.setAgent(eid1);
	return res;
    }

    
    public WasAssociatedWith  newWasAssociatedWith(QualifiedName id, 
                                                   QualifiedName a, 
                                                   QualifiedName ag, 
                                                   QualifiedName plan, 
                                                   Collection<Attribute> attributes) {
	WasAssociatedWith res= newWasAssociatedWith(id,a,ag);
	res.setPlan(plan);
	setAttributes(res, attributes);
	return res;
    }
    
    public WasAssociatedWith newWasAssociatedWith(WasAssociatedWith u) {
	WasAssociatedWith u1 = newWasAssociatedWith(u.getId(), u.getActivity(),
						    u.getAgent());
	u1.getOther().addAll(u.getOther());
	u1.setPlan(u.getPlan());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	return u1;
    }

    public WasAttributedTo newWasAttributedTo(QualifiedName id, 
                                              QualifiedName eid,
					      QualifiedName agid) {
	WasAttributedTo res = of.createWasAttributedTo();
	res.setId(id);
	res.setEntity(eid);
	res.setAgent(agid);
	return res;
    }


    public WasAttributedTo newWasAttributedTo(QualifiedName id, 
                                              QualifiedName eid,
					      QualifiedName agid,
					      Collection<Attribute> attributes) {
	WasAttributedTo res = of.createWasAttributedTo();
	res.setId(id);
	res.setEntity(eid);
	res.setAgent(agid);
        setAttributes(res, attributes);
	return res;
    }

    

    public WasAttributedTo newWasAttributedTo(WasAttributedTo u) {
	WasAttributedTo u1 = newWasAttributedTo(u.getId(), u.getEntity(),
						u.getAgent());
	u1.getOther().addAll(u.getOther());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	return u1;
    }
    
    /** A factory method to create an instance of a derivation {@link WasDerivedFrom}
     * @param id an optional identifier for a derivation
     * @param e2 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation 
     * @param e1 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
     * @return an instance of {@link WasDerivedFrom}
     */
    public WasDerivedFrom newWasDerivedFrom(QualifiedName id, 
                                            QualifiedName e2,
					    QualifiedName e1) {
	WasDerivedFrom res = of.createWasDerivedFrom();
	res.setId(id);
	res.setUsedEntity(e1);
	res.setGeneratedEntity(e2);
	return res;
    }    
    public WasDerivedFrom newWasDerivedFrom(QualifiedName aid1,
					    QualifiedName aid2) {
	WasDerivedFrom res = of.createWasDerivedFrom();
	res.setUsedEntity(aid2);
	res.setGeneratedEntity(aid1);
	return res;
    }    
   

    public WasDerivedFrom newWasDerivedFrom(QualifiedName id, 
                                            QualifiedName aid1,
					    QualifiedName aid2, 
					    QualifiedName aid,
					    QualifiedName did1, 
					    QualifiedName did2,
					    Collection<Attribute> attributes) {
	WasDerivedFrom res = of.createWasDerivedFrom();
	res.setId(id);
	res.setUsedEntity(aid2);
	res.setGeneratedEntity(aid1);
	res.setActivity(aid);
	res.setGeneration(did1);
	res.setUsage(did2);
        setAttributes(res, attributes);
	return res;
    }


    public WasDerivedFrom newWasDerivedFrom(WasDerivedFrom d) {
	WasDerivedFrom wdf = newWasDerivedFrom(d.getId(),
					       d.getGeneratedEntity(),
					       d.getUsedEntity());
	wdf.setGeneration(d.getGeneration());
	wdf.setUsage(d.getUsage());
	wdf.getOther().addAll(d.getOther());
	wdf.getType().addAll(d.getType());
	wdf.getLabel().addAll(d.getLabel());
	return wdf;
    }
    
    public WasEndedBy newWasEndedBy(QualifiedName id) {
	WasEndedBy res = of.createWasEndedBy();
	res.setId(id);
	return res;
    }

    public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName aid, QualifiedName eid) {
	WasEndedBy res = of.createWasEndedBy();
	res.setId(id);
	res.setActivity(aid);
	res.setTrigger(eid);
	return res;
    }
    
    
    /** A factory method to create an instance of an end {@link WasEndedBy}
     * @param id
     * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
     * @param ender an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
     * @param time the optional <a href="http://www.w3.org/TR/prov-dm/#end.time">time</a>  at which the activity was ended
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this activity end
     * @return an instance of {@link WasStartedBy}
     */
    public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName ender) {
      	WasEndedBy res=newWasEndedBy(id,activity,trigger);	
      	res.setEnder(ender);
      	return res;
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasEndedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName ender, XMLGregorianCalendar time, Collection<Attribute> attributes) {
      	WasEndedBy res=newWasEndedBy(id,activity,trigger);	
      	res.setTime(time);
      	res.setEnder(ender);
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
	u1.getOther().addAll(u.getOther());
	return u1;
    }

    public WasGeneratedBy newWasGeneratedBy(Entity a, String role, Activity p) {
	return newWasGeneratedBy((QualifiedName) null, a, role, p);
    }

    public WasGeneratedBy newWasGeneratedBy(QualifiedName id) {
	WasGeneratedBy res = of.createWasGeneratedBy();
	res.setId(id);
	return res;
	
    }

    public WasGeneratedBy newWasGeneratedBy(QualifiedName id, 
                                            Entity a, 
                                            String role,
					    Activity p) {

	WasGeneratedBy res=newWasGeneratedBy(id, a.getId(), p.getId());
	if (role!=null) addRole(res, newRole(role,getName().QNAME_XSD_STRING));
	return res;
    }

    public WasGeneratedBy newWasGeneratedBy(QualifiedName id, 
                                            QualifiedName aid,
					    String role, 
					    QualifiedName pid) {
	WasGeneratedBy res = of.createWasGeneratedBy();
	res.setId(id);
	res.setActivity(pid);
	res.setEntity(aid);
	if (role!=null) addRole(res, newRole(role,getName().QNAME_XSD_STRING));
	return res;
    }

    /** A factory method to create an instance of a generation {@link WasGeneratedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
     * @return an instance of {@link WasGeneratedBy}
     */    

    public WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity, QualifiedName activity) {
   	WasGeneratedBy res=newWasGeneratedBy(id,entity,null,activity);	
   	return res;
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasGeneratedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
   	WasGeneratedBy res=newWasGeneratedBy(id,entity,null,activity);	
   	res.setTime(time);
	setAttributes(res, attributes);
   	return res;
    }



    public WasGeneratedBy newWasGeneratedBy(WasGeneratedBy g) {
	WasGeneratedBy wgb = newWasGeneratedBy(g.getId(), g.getEntity(), null,
					       g.getActivity());
	wgb.setId(g.getId());
	wgb.setTime(g.getTime());
	wgb.getOther().addAll(g.getOther());
	wgb.getType().addAll(g.getType());
	wgb.getLabel().addAll(g.getLabel());
	wgb.getLocation().addAll(g.getLocation());
	return wgb;
    }

    public WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName influencee,
					      QualifiedName influencer) {
	WasInfluencedBy res = of.createWasInfluencedBy();
	res.setId(id);
	res.setInfluencee(influencee);
	res.setInfluencer(influencer);
	return res;
    }

    public WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName a2, QualifiedName a1, Collection<Attribute> attributes) {
        WasInfluencedBy res=newWasInfluencedBy(id,a2,a1);   
        setAttributes(res, attributes);
        return res;
    }
    
    
    public WasInfluencedBy newWasInfluencedBy(WasInfluencedBy in) {
	WasInfluencedBy out = newWasInfluencedBy(in.getId(),
						 in.getInfluencee(),
						 in.getInfluencer());
	out.setId(in.getId());
	out.getOther().addAll(in.getOther());
	out.getType().addAll(in.getType());
	out.getLabel().addAll(in.getLabel());
	return out;
    }

    public WasInformedBy newWasInformedBy(Activity p1, Activity p2) {
	return newWasInformedBy(null, p1, p2);
    }

    public WasInformedBy newWasInformedBy(QualifiedName id, Activity p1, Activity p2,
					  String type) {
	WasInformedBy wtb = newWasInformedBy(p1, p2);
	wtb.setId(id);
	addType(wtb, newType(type,getName().QNAME_XSD_STRING));
	return wtb;
    }

    public WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName pid1,
					  QualifiedName pid2) {
	WasInformedBy res = of.createWasInformedBy();
	res.setId(id);
	res.setInformed(pid1);
	res.setInformant(pid2);
	return res;
    }

    public WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName a2, QualifiedName a1, Collection<Attribute> attributes) {
        WasInformedBy res=newWasInformedBy(id,a2,a1);   
        setAttributes(res, attributes);
        return res;
    }

    public WasInformedBy newWasInformedBy(QualifiedName id, Activity p1, Activity p2) {

	return newWasInformedBy(id, p1.getId(), p2.getId(),null);
    }


    public WasInformedBy newWasInformedBy(WasInformedBy d) {
	WasInformedBy wtb = newWasInformedBy(d.getId(), 
					     d.getInformed(),
					     d.getInformant());
	wtb.setId(d.getId());
	wtb.getOther().addAll(d.getOther());
	wtb.getType().addAll(d.getType());
	wtb.getLabel().addAll(d.getLabel());
	return wtb;
    }

   
    public WasInvalidatedBy newWasInvalidatedBy(QualifiedName eid,
						QualifiedName aid) {
	WasInvalidatedBy res = of.createWasInvalidatedBy();
	res.setEntity(eid);
	res.setActivity(aid);
	return res;
    }

    /** A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
     * @return an instance of {@link WasInvalidatedBy}
     */    

    public WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, QualifiedName entity, QualifiedName activity) {
	WasInvalidatedBy res = of.createWasInvalidatedBy();
	res.setId(id);
	res.setEntity(entity);
	res.setActivity(activity);
	return res;
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasInvalidatedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
   	WasInvalidatedBy res=newWasInvalidatedBy(id,entity,activity);	
   	res.setTime(time);
	setAttributes(res, attributes);
   	return res;
    }


    public WasInvalidatedBy newWasInvalidatedBy(WasInvalidatedBy u) {
	WasInvalidatedBy u1 = newWasInvalidatedBy(u.getId(), u.getEntity(),
						  u.getActivity());
	u1.setTime(u.getTime());
	u1.getOther().addAll(u.getOther());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	u1.getLocation().addAll(u.getLocation());
	return u1;
    }
    
    /** A factory method to create an instance of a start {@link WasStartedBy}
     * @param id
     * @return an instance of {@link WasStartedBy}
     */
       
    public WasStartedBy newWasStartedBy(QualifiedName id) {
   	WasStartedBy res = of.createWasStartedBy();
   	res.setId(id);
   	return res;
    }

    public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName aid, QualifiedName eid) {
	WasStartedBy res = of.createWasStartedBy();
	res.setId(id);
	res.setActivity(aid);
	res.setTrigger(eid);
	return res;
    }
    
    /** A factory method to create an instance of a start {@link WasStartedBy}
     * @param id
     * @param activity an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
     * @param starter an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
     * @return an instance of {@link WasStartedBy}
     */
    
    public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName starter) {  	
      	WasStartedBy res=newWasStartedBy(id,activity,trigger);	
      	res.setStarter(starter);
      	return res;
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasStartedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName starter, XMLGregorianCalendar time, Collection<Attribute> attributes) {
      	WasStartedBy res=newWasStartedBy(id,activity,trigger);	
      	res.setTime(time);
      	res.setStarter(starter);
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
	u1.getOther().addAll(u.getOther());

	return u1;
    }
    public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar gc) {
	return dataFactory.newXMLGregorianCalendar(gc);
    }

    public XMLGregorianCalendar newYear(int year) {
        XMLGregorianCalendar res=dataFactory.newXMLGregorianCalendar();
        res.setYear(year);
        return res;
    }

    public void setAttributes(HasOther res, Collection<Attribute> attributes) {
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
		    loc.getLocation().add(newLocation(aValue,attr.getType()));
		}
		break;
	    case PROV_ROLE:
		if (rol!=null) {
		    rol.getRole().add(newRole(aValue,attr.getType()));
		}
		break;
	    case PROV_TYPE: 
		if (typ!=null) {
		    typ.getType().add(newType(aValue,attr.getType()));
		}
		break;
	    case PROV_VALUE:
		if (aval!=null) {
		    aval.setValue(newValue(aValue,attr.getType()));
		}
		break;
	    case OTHER:
		res.getOther().add(newOther(attr.getElementName(), aValue, attr.getType()));
		break;
	    
	    default:
		break;
	    
	    }
	    
	}
    }

    @Override
    public void startBundle(QualifiedName bundleId, Namespace namespaces) {
      
    }
    
    /* Uses the xsd:type to java:type mapping of JAXB */

    @Override
    public void startDocument(Namespace namespace) {
        
    }

}
