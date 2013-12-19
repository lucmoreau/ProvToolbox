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
import java.util.GregorianCalendar;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;




/** A stateless factory for PROV objects. */


public abstract class ProvFactory implements LiteralConstructor, ModelConstructor {
 
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
	a.getType().add(newType(getName().PROV_PRIMARY_SOURCE,
	                        getName().XSD_QNAME));
    }

    public void addQuotationType(HasType a) {
	a.getType().add(newType(getName().PROV_QUOTATION,getName().XSD_QNAME));
    }

    public void addRevisionType(HasType a) {
	a.getType().add(newType(getName().PROV_REVISION,getName().XSD_QNAME));
    }

    public void addBundleType(HasType a) {
	a.getType().add(newType(getName().PROV_BUNDLE,getName().XSD_QNAME));
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
	a.getType().add(newType(type,getName().XSD_ANY_URI));
    }

    public byte [] base64Decoding(String s) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(s);
    }

 
 


    public String base64Encoding(byte [] b) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(b);
    }


    /* Return the first label, it it exists */
    public String getLabel(HasOther e) {

	List<LangString> labels = ((HasLabel) e).getLabel();
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
	res.add(newType("pFact: type TODO",getName().XSD_STRING));
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

    /** A factory method to create an instance of a delegation {@link ActedOnBehalfOf}
     * @param id identifier for the delegation association between delegate and responsible
     * @param delegate identifier for the agent associated with an activity, acting on behalf of the responsible agent
     * @param responsible identifier for the agent, on behalf of which the delegate agent acted
     * @param activity optional identifier of an activity for which the delegation association holds
     * @return an instance of {@link ActedOnBehalfOf}
     */
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
    
    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newActedOnBehalfOf(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */
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

    
    /** A factory method to create an instance of a delegation {@link ActedOnBehalfOf}
     * @param id identifier for the delegation association between delegate and responsible
     * @param delegate identifier for the agent associated with an activity, acting on behalf of the responsible agent
     * @param responsible identifier for the agent, on behalf of which the delegate agent acted
     * @return an instance of {@link ActedOnBehalfOf}
     */
    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, QualifiedName delegate, QualifiedName responsible) {
        ActedOnBehalfOf res=newActedOnBehalfOf(id, delegate, responsible, null,null);
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
    

    /** A factory method to create an instance of an alternate {@link AlternateOf}
     * @param entity1 an identifier for the first {@link Entity}
     * @param entity2 an identifier for the second {@link Entity}
     * @return an instance of {@link AlternateOf}
     */   
    public AlternateOf newAlternateOf(QualifiedName entity1, QualifiedName entity2) {
	AlternateOf res = of.createAlternateOf();
	res.setAlternate1(entity1);
	res.setAlternate2(entity2);
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


    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newDocument(org.openprovenance.prov.model.Namespace, java.util.Collection, java.util.Collection)
     */
    @Override
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
    

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.LiteralConstructor#newDuration(java.lang.String)
     */
    public Duration newDuration(String lexicalRepresentation) {
        Duration dur=dataFactory.newDuration(lexicalRepresentation);
        return dur;
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

    /** Factory method for Key-entity entries. Key-entity entries are used to identify the members of a dictionary.
     * @param key indexing the entity in the dictionary
     * @param entity a {@link QualifiedName} denoting an entity
     * @return an instance of {@link Entry}
     */
    public Entry newEntry(Key key, QualifiedName entity) {
	Entry res = of.createEntry();
	res.setKey(key);
	res.setEntity(entity);
	return res;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.LiteralConstructor#newGDay(int)
     */
    public XMLGregorianCalendar newGDay(int day) {
        XMLGregorianCalendar cal=dataFactory.newXMLGregorianCalendar();
        cal.setDay(day);
        return cal;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.LiteralConstructor#newGMonth(int)
     */
    public XMLGregorianCalendar newGMonth(int month) {
        XMLGregorianCalendar cal=dataFactory.newXMLGregorianCalendar();
        cal.setMonth(month);
        return cal;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.LiteralConstructor#newGMonthDay(int, int)
     */
    public XMLGregorianCalendar newGMonthDay(int month, int day) {
        XMLGregorianCalendar cal=dataFactory.newXMLGregorianCalendar();
        cal.setMonth(month);
        cal.setDay(day);
        return cal;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.LiteralConstructor#newGYear(int)
     */
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

    public LangString newInternationalizedString(String s) {
	LangString res = of.createInternationalizedString();
	res.setValue(s);
	return res;
    }

    public LangString newInternationalizedString(String s,
							      String lang) {
	LangString res = of.createInternationalizedString();
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
    	res.setValueFromObject(o);
    	return res;
       }


    public Location newLocation(Object value, QualifiedName type) {
            Location res =  of.createLocation();
            res.setType(type);
            res.setValueFromObject(value);
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
        res.setValueFromObject(value);
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
        res.setValueFromObject(value);
        return res;
    }


    /** A factory method to create an instance of a specialization {@link SpecializationOf}
     * @param specific an identifier ({@link QualifiedName}) for the specific {@link Entity}
     * @param general an identifier  ({@link QualifiedName}) for the general {@link Entity}
     * @return an instance of {@link SpecializationOf}
     */       
    public SpecializationOf newSpecializationOf(QualifiedName specific, QualifiedName general) {
	SpecializationOf res = of.createSpecializationOf();
	res.setSpecificEntity(specific);
	res.setGeneralEntity(general);
	return res;
    }

    public XMLGregorianCalendar newTime(Date date) {
	GregorianCalendar gc = new GregorianCalendar();
	gc.setTime(date);
	return newXMLGregorianCalendar(gc);
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.LiteralConstructor#newTimeNow()
     */
    public XMLGregorianCalendar newTimeNow() {
	return newTime(new Date());
    }
  
    public Type newType(Object value, QualifiedName type) {
	if (value==null) return null;
        Type res =  of.createType();
        res.setType(type);
        res.setValueFromObject(value);
        return res;
    }


    /** A factory method to create an instance of a usage {@link Used}
     * @param id an optional identifier for a usage
     * @return an instance of {@link Used}
     */    
    public Used newUsed(QualifiedName id) {
	Used res = of.createUsed();
	res.setId(id);
	return res;
    }

    public Used newUsed(QualifiedName id, QualifiedName aid, String role, QualifiedName eid) {
	Used res = newUsed(id);
	res.setActivity(aid);
	if (role!=null)
	addRole(res, newRole(role,getName().XSD_STRING));
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

    
    /** A factory method to create an instance of a usage {@link Used}
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
     * @return an instance of {@link Used}
     */    

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


    
    /** A factory method to create an instance of a usage {@link Used} from another
     * @param u an instance of a usage 
     * @return an instance of {@link Used} equal (in the sense of @see Object.equals()) to the input
     */    

    public Used newUsed(Used u) {
	Used u1 = newUsed(u.getId(), u.getActivity(), u.getEntity());
	u1.getOther().addAll(u.getOther());
	u1.setTime(u.getTime());
	u1.getType().addAll(u.getType());
	u1.getLabel().addAll(u.getLabel());
	u1.getLocation().addAll(u.getLocation());
	u1.getOther().addAll(u.getOther());
	return u1;
    }

    
    

    public Value newValue(Object value, QualifiedName type) {
	if (value==null) return null;
        Value res =  of.createValue();
        res.setType(type);
        res.setValueFromObject(value);
        return res;
      }

    /** A factory method to create an instance of an Association {@link WasAssociatedWith}
     * @param id an identifier for the association
     * @return an instance of {@link WasAssociatedWith}
     */
 
    public WasAssociatedWith newWasAssociatedWith(QualifiedName id) {
	return newWasAssociatedWith(id, (QualifiedName)null,(QualifiedName)null);
    }

    /** A factory method to create an instance of an Association {@link WasAssociatedWith}
     * @param id an optional identifier for the association between an activity and an agent
     * @param activity an identifier for the activity
     * @param agent an optional identifier for the agent associated with the activity
     * @return an instance of {@link WasAssociatedWith}
     */
 
    
    public WasAssociatedWith newWasAssociatedWith(QualifiedName id,
                                                  QualifiedName activity,
						  QualifiedName agent) {
	WasAssociatedWith res = of.createWasAssociatedWith();
	res.setId(id);
	res.setActivity(activity);
	res.setAgent(agent);
	return res;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasAssociatedWith(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */
    
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



    /** A factory method to create an instance of an attribution {@link WasAttributedTo}
     * @param id  an optional identifier for the relation
     * @param entity an entity identifier
     * @param agent  the identifier of the agent whom the entity is ascribed to, and therefore bears some responsibility for its existence
     * @return an instance of {@link WasAttributedTo}
     */ 
    public WasAttributedTo newWasAttributedTo(QualifiedName id, 
                                              QualifiedName entity,
					      QualifiedName agent) {
	WasAttributedTo res = of.createWasAttributedTo();
	res.setId(id);
	res.setEntity(entity);
	res.setAgent(agent);
	return res;
    }


    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasAttributedTo(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */
    public WasAttributedTo newWasAttributedTo(QualifiedName id, 
                                              QualifiedName entity,
					      QualifiedName agent,
					      Collection<Attribute> attributes) {
	WasAttributedTo res = of.createWasAttributedTo();
	res.setId(id);
	res.setEntity(entity);
	res.setAgent(agent);
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
   
    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasDerivedFrom(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */

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
    /** A factory method to create an instance of an end {@link WasEndedBy}
     * @param id
     * @return an instance of {@link WasEndedBy}
     */
      
    public WasEndedBy newWasEndedBy(QualifiedName id) {
	WasEndedBy res = of.createWasEndedBy();
	res.setId(id);
	return res;
    }

    /** A factory method to create an instance of an end {@link WasEndedBy}
     * @param id
     * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
     * @return an instance of {@link WasEndedBy}
     */
    
    public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger) {
	WasEndedBy res = of.createWasEndedBy();
	res.setId(id);
	res.setActivity(activity);
	res.setTrigger(trigger);
	return res;
    }
    
    
    /** A factory method to create an instance of an end {@link WasEndedBy}
     * @param id
     * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
     * @param ender an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
     * @return an instance of {@link WasEndedBy}
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
	if (role!=null) addRole(res, newRole(role,getName().XSD_STRING));
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
	if (role!=null) addRole(res, newRole(role,getName().XSD_STRING));
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


    /**A factory method to create an instance of an influence {@link WasInfluencedBy}
     * @param id optional identifier identifying the association
     * @param influencee an identifier for an entity, activity, or agent
     * @param influencer an identifier for an ancestor entity, activity, or agent that the former depends on
     *
     * @return an instance of {@link WasInfluencedBy}
     */
    
    public WasInfluencedBy newWasInfluencedBy(QualifiedName id, 
                                              QualifiedName influencee,
					      QualifiedName influencer) {
	WasInfluencedBy res = of.createWasInfluencedBy();
	res.setId(id);
	res.setInfluencee(influencee);
	res.setInfluencer(influencer);
	return res;
    }
    
    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasInfluencedBy(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */

    public WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName influencee, QualifiedName influencer, Collection<Attribute> attributes) {
        WasInfluencedBy res=newWasInfluencedBy(id,influencee,influencer);   
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

    /** A factory method to create an instance of an communication {@link WasInformedBy}
     * @param id an optional identifier identifying the association;
     * @param informed the identifier of the informed activity;
     * @param informant the identifier of the informant activity;
     * @return an instance of {@link WasInformedBy}
     */

    public WasInformedBy newWasInformedBy(QualifiedName id, 
                                          QualifiedName informed,
					  QualifiedName informant) {
	WasInformedBy res = of.createWasInformedBy();
	res.setId(id);
	res.setInformed(informed);
	res.setInformant(informant);
	return res;
    }
    
    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasInformedBy(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */

    public WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName a2, QualifiedName a1, Collection<Attribute> attributes) {
        WasInformedBy res=newWasInformedBy(id,a2,a1);   
        setAttributes(res, attributes);
        return res;
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
		    if (aValue instanceof LangString) {
			lab.getLabel().add((LangString) aValue);		
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
