package org.openprovenance.prov.model;

import java.math.BigInteger;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.DatatypeConverter;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;


/** A stateless factory for PROV objects. */


public abstract class ProvFactory implements LiteralConstructor, ModelConstructor, ModelConstructorExtension {

	public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

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
				getName().PROV_QUALIFIED_NAME));
	}

	public void addQuotationType(HasType a) {
		a.getType().add(newType(getName().PROV_QUOTATION,getName().PROV_QUALIFIED_NAME));
	}

	public void addRevisionType(HasType a) {
		a.getType().add(newType(getName().PROV_REVISION,getName().PROV_QUALIFIED_NAME));
	}

	public void addBundleType(HasType a) {
		a.getType().add(newType(getName().PROV_BUNDLE,getName().PROV_QUALIFIED_NAME));
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

	public void addType(HasType a, QualifiedName type) {
		a.getType().add(newType(type,getName().PROV_QUALIFIED_NAME));
	}

	public byte [] base64Decoding(String s) {
		return Base64.getDecoder().decode(s);
	}





	public String base64Encoding(byte [] b) {
		return Base64.getEncoder().encodeToString(b);
	}


	/* Return the first label, if it exists */
	public String getLabel(HasLabel e) {

		List<LangString> labels = e.getLabel();
		if ((labels == null) || (labels.isEmpty()))
			return null;
        return labels.get(0).getValue();
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


	abstract public ProvSerialiser getSerializer();


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
		byte[] byteArray = new BigInteger(s, 16).toByteArray();
		if (byteArray[0] == 0) {
			byte[] output = new byte[byteArray.length - 1];
			System.arraycopy(byteArray, 1, output, 0, output.length);
			return output;
		}
		return byteArray;
		/*
		try {
			return org.apache.commons.codec.binary.Hex.decodeHex(s.toCharArray());
		} catch  (Exception e) {
			return s.getBytes(); // fall back, but obviously, this is not converted
		}
		 */
	}
	public String hexEncoding(byte [] b) {
		BigInteger bigInteger = new BigInteger(1, b);
		return bigInteger.toString(16);
		//return org.apache.commons.codec.binary.Hex.encodeHexString(b);
	}



	protected void init() {
		try {
			dataFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}

	public ActedOnBehalfOf newActedOnBehalfOf(ActedOnBehalfOf u) {
		return newActedOnBehalfOf(u.getId(), u.getDelegate(), u.getResponsible(), u.getActivity(),getAttributes(u));
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
		return newActedOnBehalfOf(id, delegate, responsible, activity,null);
	}



	/*
	 * (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newActedOnBehalfOf(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
	 */
	/*public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id,
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


	 */

	/** A factory method to create an instance of a delegation {@link ActedOnBehalfOf}
	 * @param id identifier for the delegation association between delegate and responsible
	 * @param delegate identifier for the agent associated with an activity, acting on behalf of the responsible agent
	 * @param responsible identifier for the agent, on behalf of which the delegate agent acted
	 * @return an instance of {@link ActedOnBehalfOf}
	 */
	public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, QualifiedName delegate, QualifiedName responsible) {
        return newActedOnBehalfOf(id, delegate, responsible, null,null);
	}



	public Activity newActivity(Activity a) {
		return newActivity(a.getId(), a.getStartTime(), a.getEndTime(),getAttributes(a));
	}

	public Activity newActivity(QualifiedName a) {
		return newActivity(a, null,null,null);
	}

	public Activity newActivity(QualifiedName q, String label) {
		Activity res = newActivity(q);
		if (label != null)
			res.getLabel().add(newInternationalizedString(label));
		return res;
	}

	/*
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

	 */


	/**
	 * Creates a copy of an agent. The copy is shallow in the sense that the new Agent shares the same attributes as the original Agent.
	 * @param a an {@link Agent} to copy
	 * @return a copy of the input {@link Agent}
	 */

	public Agent newAgent(Agent a) {
		return newAgent(a.getId(), getAttributes(a));
	}

	/**
	 * Creates a new {@link Agent} with provided identifier
	 * @param ag a {@link QualifiedName} for the agent
	 * @return an object of type {@link Agent}
	 */
	public Agent newAgent(QualifiedName ag) {
		return newAgent(ag, (Collection<Attribute>) null);
	}

	/**
	 * Creates a new {@link Agent} with provided identifier and attributes
	 * @param id a {@link QualifiedName} for the agent
	 * @param attributes a collection of {@link Attribute} for the agent
	 * @return an object of type {@link Agent}


	public Agent newAgent(QualifiedName id, Collection<Attribute> attributes) {
		Agent res = newAgent(id);
		setAttributes(res, attributes);
		return res;
	}
	 */

	/**
	 * Creates a new {@link Agent} with provided identifier and label
	 * @param ag a {@link QualifiedName} for the agent
	 * @param label a String for the label property (see {@link HasLabel#getLabel()}
	 * @return an object of type {@link Agent}
	 */
	public Agent newAgent(QualifiedName ag, String label) {
		Agent res = newAgent(ag);
		if (label != null)
			res.getLabel().add(newInternationalizedString(label));
		return res;
	}


	/** A factory method to create an instance of an alternate {@link AlternateOf}
	 * @p aram entity1 an identifier for the first {@link Entity}
	 * @p aram entity2 an identifier for the second {@link Entity}
	 * @return an instance of {@link AlternateOf}

	public AlternateOf newAlternateOf(QualifiedName entity1, QualifiedName entity2) {
		AlternateOf res = of.createAlternateOf();
		res.setAlternate1(entity1);
		res.setAlternate2(entity2);
		return res;
	}
	 */


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

	/**
	 * Factory method to construct a {@link Document}
	 * @return a new instance of {@link Document}
	 */
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
		if (graph.getNamespace()!=null) {
			res.setNamespace(new Namespace(graph.getNamespace()));
		}
		return res;
	}


	/*
	 * (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newDocument(org.openprovenance.prov.model.Namespace, java.util.Collection, java.util.Collection)
	 */
	@Override
	public Document newDocument(Namespace namespace,
								Collection<Statement> statements,
								Collection<Bundle> bundles) {
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


	/**
	 * Creates a copy of an entity. The copy is shallow in the sense that the new Entity shares the same attributes as the original Entity.
	 * @param e an {@link Entity} to copy
	 * @return a copy of the input {@link Entity}
	 */
	public Entity newEntity(Entity e) {
		Entity res = newEntity(e.getId());
		res.getOther().addAll(e.getOther());
		res.getType().addAll(e.getType());
		res.getLabel().addAll(e.getLabel());
		res.getLocation().addAll(e.getLocation());
		return res;
	}

	/**
	 * Creates a new {@link Entity} with provided identifier
	 * @param id a {@link QualifiedName} for the entity
	 * @return an object of type {@link Entity}
	 */
	public Entity newEntity(QualifiedName id) {
		Entity res = of.createEntity();
		res.setId(id);
		return res;
	}

	/**
	 * Creates a new {@link Entity} with provided identifier and attributes
	 * @param id a {@link QualifiedName} for the entity
	 * @param attributes a collection of {@link Attribute} for the entity
	 * @return an object of type {@link Entity}
	 */
	public Entity newEntity(QualifiedName id, Collection<Attribute> attributes) {
		Entity res = newEntity(id);
		setAttributes(res, attributes);
		return res;
	}
	/**
	 * Creates a new {@link Entity} with provided identifier and label
	 * @param id a {@link QualifiedName} for the entity
	 * @param label a String for the label property (see {@link HasLabel#getLabel()}
	 * @return an object of type {@link Entity}
	 */
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
		List<QualifiedName> ll= new LinkedList<>();
		if (e!=null) {
			ll.addAll(e);
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

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.model.LiteralConstructor#newISOTime(java.lang.String)
	 */
	public XMLGregorianCalendar newISOTime(String time) {
		return newTime(DatatypeConverter.parseDateTime(time).getTime());
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.model.LiteralConstructor#newISOTimeTZ(java.lang.String)
	 */

	public XMLGregorianCalendar newISOTimeTZ(String time) {
		Calendar parsedDateTime = DatatypeConverter.parseDateTime(time);
		return newTime(parsedDateTime.getTime(), parsedDateTime.getTimeZone());
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.model.LiteralConstructor#newISOTimeUTC(java.lang.String)
	 */
	public XMLGregorianCalendar newISOTimeUTC(String time) {
		return newISOTime(time, UTC_TIMEZONE);
	}

	public XMLGregorianCalendar newISOTime(String time, TimeZone timeZone) {
		Calendar parsedDateTime = DatatypeConverter.parseDateTime(time);
		return newTime(parsedDateTime.getTime(), timeZone);
	}

	public XMLGregorianCalendar newISOTime(String time, DateTimeOption option, TimeZone optionalTimeZone) {
		switch (option) {
			case TIMEZONE:  return newISOTime(time, optionalTimeZone);
			case PRESERVE: return newISOTimeTZ(time);
			case UTC: return newISOTimeUTC(time);
			case SYSTEM: return newISOTime(time);
			default: throw new UnsupportedOperationException("Unknown option: " + option);
		}
	}

	public abstract Key newKey(Object o, QualifiedName type);

	public abstract Attribute newLabel(Object value, org.openprovenance.prov.model.QualifiedName type);

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


	public Bundle newNamedBundle(QualifiedName id,
								 Collection<Activity> ps,
								 Collection<Entity> as,
								 Collection<Agent> ags,
								 Collection<Statement> lks) {
		Bundle res = of.createNamedBundle();
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


	public Bundle newNamedBundle(QualifiedName id, Collection<Statement> lks) {
		Bundle res = of.createNamedBundle();
		res.setId(id);
		if (lks != null) {
			res.getStatement().addAll(lks);
		}
		return res;
	}
	public Bundle newNamedBundle(QualifiedName id, Namespace namespace, Collection<Statement> statements) {
		Bundle res = of.createNamedBundle();
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
		res.setValueFromObject(value);
		res.setElementName(elementName);
		return res;
	}

	public Other newOther(String namespace, String local, String prefix,  Object value, QualifiedName type) {
		QualifiedName elementName=newQualifiedName(namespace,local,prefix);
		return newOther(elementName,value,type);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newQualifiedName(java.lang.String, java.lang.String, java.lang.String)
	 */

	abstract public QualifiedName newQualifiedName(String namespace, String local, String prefix);

	abstract public QualifiedName newQualifiedName(String namespace, String local, String prefix, ProvUtilities.BuildFlag flag);


	/* A convenience function. */
	public QualifiedName newQualifiedName(javax.xml.namespace.QName qname) {
		return newQualifiedName(qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
	}


	abstract public Role newRole(Object value, QualifiedName type) ;

	/** A factory method to create an instance of a specialization {@link SpecializationOf}
	 * @param specific an identifier ({@link QualifiedName}) for the specific {@link Entity}
	 * @param general an identifier  ({@link QualifiedName}) for the general {@link Entity}
	 * @return an instance of {@link SpecializationOf}

	@Override
	public SpecializationOf newSpecializationOf(QualifiedName specific, QualifiedName general) {
		return newSpecializationOf(null, specific, general, null);
	}
	 */

	@Override
	public QualifiedSpecializationOf newQualifiedSpecializationOf(QualifiedName id, QualifiedName specific, QualifiedName general, Collection<Attribute> attributes) {
		QualifiedSpecializationOf res = of.createQualifiedSpecializationOf();
		res.setId(id);
		res.setSpecificEntity(specific);
		res.setGeneralEntity(general);
		setAttributes(res, attributes);
		return res;
	}
	@Override
	public QualifiedAlternateOf newQualifiedAlternateOf(QualifiedName id, QualifiedName alt1, QualifiedName alt2, Collection<Attribute> attributes) {
		QualifiedAlternateOf res=of.createQualifiedAlternateOf();
		res.setId(id);
		res.setAlternate1(alt1);
		res.setAlternate2(alt2);
		setAttributes(res, attributes);
		return res;
	}
	@Override
	public QualifiedHadMember newQualifiedHadMember(QualifiedName id, QualifiedName c, Collection<QualifiedName> e, Collection<Attribute> attributes) {
		List<QualifiedName> ll= new LinkedList<>();
		if (e!=null) {
			ll.addAll(e);
		}
		QualifiedHadMember res=of.createQualifiedHadMember();
		res.setId(id);
		res.setCollection(c);
		if (e!=null) res.getEntity().addAll(e);
		return res;
	}


	public XMLGregorianCalendar newTime(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		return newXMLGregorianCalendar(gc);
	}

	public XMLGregorianCalendar newTime(Date date, TimeZone timeZone) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.setTimeZone(timeZone);
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
		return newUsed(id, null, null, null, null);
	}

	public Used newUsed(QualifiedName id, QualifiedName activity, String role, QualifiedName entity) {
		return newUsed(id, activity, entity, null, roleAsAttributeList(role));
	}

	/** A factory method to create an instance of a usage {@link Used}
	 * @param id an optional identifier for a usage
	 * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
	 * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
	 * @return an instance of {@link Used}
	 */

	public Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity) {
		return newUsed(id, activity,  entity, null, null);
	}


	/** A factory method to create an instance of a usage {@link Used}
	 * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
	 * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
	 * @return an instance of {@link Used}
	 */

	public Used newUsed(QualifiedName activity, QualifiedName entity) {
		return newUsed(null, activity,  entity, null, null);
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newUsed(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)

	public Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity,
						XMLGregorianCalendar time,
						Collection<Attribute> attributes) {
		Used res = newUsed(id, activity, null, entity);
		res.setTime(time);
		setAttributes(res, attributes);
		return res;
	} */

	public Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity,
						XMLGregorianCalendar time) {
		return newUsed(id, activity,  entity, time, null);
	}

	/** A factory method to create an instance of a usage {@link Used} from another
	 * @param u an instance of a usage
	 * @return an instance of {@link Used} equal (in the sense of @see Object.equals()) to the input
	 */

	public Used newUsed(Used u) {
		return newUsed(u.getId(), u.getActivity(), u.getEntity(), u.getTime(), getAttributes(u));
	}

	/**
	 * Factory method to create an instance of  the PROV-DM prov:value attribute (see {@link Value}).
	 * @param value a String
	 * @return a new {@link Value} with type xsd:string (see {@link Name#XSD_STRING})
	 */
	public Value newValue(String value) {
		return newValue(value,getName().XSD_STRING);
	}

	/**
	 * Factory method to create an instance of the PROV-DM prov:value attribute (see {@link Value}).
	 * @param value an integer
	 * @return a new {@link Value} with type xsd:int (see {@link Name#XSD_INT})
	 */
	public Value newValue(int value) {
		return newValue(value,getName().XSD_INT);
	}

	/**
	 * Factory method to create an instance of the PROV-DM prov:value attribute (see {@link Value}).
	 * Use class {@link Name} for predefined {@link QualifiedName}s for the common types.
	 * @param value an {@link Object}
	 * @param type a {@link QualifiedName} to denote the type of value
	 * @return a new {@link Value}
	 */
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
		return newWasAssociatedWith(id, activity, agent, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasAssociatedWith(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
	 */

	/*
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

	 */

	public WasAssociatedWith  newWasAssociatedWith(QualifiedName id,
												   QualifiedName a,
												   QualifiedName ag,
												   QualifiedName plan) {
		return newWasAssociatedWith(id, a, ag, plan, null);
	}



	public WasAssociatedWith newWasAssociatedWith(WasAssociatedWith u) {
		return newWasAssociatedWith(u.getId(), u.getActivity(), u.getAgent(), u.getPlan(), getAttributes(u));
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
		return newWasAttributedTo(id, entity, agent, null);
	}


	/*
	 * (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasAttributedTo(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
	 */
	/*public WasAttributedTo newWasAttributedTo(QualifiedName id,
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

	 */



	public WasAttributedTo newWasAttributedTo(WasAttributedTo u) {
		return newWasAttributedTo(u.getId(), u.getEntity(), u.getAgent(), getAttributes(u));
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
		return newWasDerivedFrom(id,e2,e1,null,null,null,null);
	}

	/** A factory method to create an instance of a derivation {@link WasDerivedFrom}
	 * @param e2 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation
	 * @param e1 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
	 * @return an instance of {@link WasDerivedFrom}
	 */
	public WasDerivedFrom newWasDerivedFrom(QualifiedName e2,
											QualifiedName e1) {
		WasDerivedFrom res = of.createWasDerivedFrom();
		res.setUsedEntity(e1);
		res.setGeneratedEntity(e2);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasDerivedFrom(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)


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

	 */


	public WasDerivedFrom newWasDerivedFrom(WasDerivedFrom d) {
		return newWasDerivedFrom(d.getId(), d.getGeneratedEntity(), d.getUsedEntity(),d.getActivity(),d.getGeneration(),d.getUsage(),getAttributes(d));
	}

	/** A factory method to create an instance of an end {@link WasEndedBy}
	 * @param id an identifier for an end
	 * @return an instance of {@link WasEndedBy}
	 */

	public WasEndedBy newWasEndedBy(QualifiedName id) {
		return newWasEndedBy(id, null, null, null, null, null);
	}

	/** A factory method to create an instance of an end {@link WasEndedBy}
	 * @param id an identifier for an end
	 * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
	 * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
	 * @return an instance of {@link WasEndedBy}
	 */

	public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger) {
		return newWasEndedBy(id, activity, trigger, null, null, null);
	}


	/** A factory method to create an instance of an end {@link WasEndedBy}
	 * @param id optional identifier for an end
	 * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
	 * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
	 * @param ender an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
	 * @return an instance of {@link WasEndedBy}
	 */
	public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName ender) {
		return newWasEndedBy(id, activity, trigger, ender, null, null);
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasEndedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)

	public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName ender, XMLGregorianCalendar time, Collection<Attribute> attributes) {
		WasEndedBy res=newWasEndedBy(id,activity,trigger);
		res.setTime(time);
		res.setEnder(ender);
		setAttributes(res, attributes);
		return res;
	}*/
	public WasEndedBy newWasEndedBy(WasEndedBy u) {
		return newWasEndedBy(u.getId(), u.getActivity(), u.getTrigger(), u.getEnder(), u.getTime(), getAttributes(u));
	}

	public WasGeneratedBy newWasGeneratedBy(Entity a, String role, Activity p) {
		return newWasGeneratedBy((QualifiedName) null, a, role, p);
	}

	public WasGeneratedBy newWasGeneratedBy(QualifiedName id) {
		return newWasGeneratedBy(id,null,null,null,null);
	}

	public WasGeneratedBy newWasGeneratedBy(QualifiedName id,
											Entity a,
											String role,
											Activity p) {
		List<Attribute> attrs = roleAsAttributeList(role);
		return newWasGeneratedBy(id, a.getId(), p.getId(), null, attrs);
	}

	private List<Attribute> roleAsAttributeList(String role) {
		List<Attribute> attrs=null;
		if (role !=null) {
			attrs = new LinkedList<>();
			attrs.add(newRole(role, getName().XSD_STRING));
		}
		return attrs;
	}

	public WasGeneratedBy newWasGeneratedBy(QualifiedName id,
											QualifiedName entity,
											String role,
											QualifiedName activity) {
		List<Attribute> attrs = roleAsAttributeList(role);
		return newWasGeneratedBy(id, entity, activity, null, attrs);
	}

	/** A factory method to create an instance of a generation {@link WasGeneratedBy}
	 * @param id an optional identifier for a usage
	 * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
	 * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
	 * @return an instance of {@link WasGeneratedBy}
	 */

	public WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity, QualifiedName activity) {
        return newWasGeneratedBy(id, entity,null, activity);
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasGeneratedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)

	public WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
		WasGeneratedBy res=newWasGeneratedBy(id,entity,null,activity);
		res.setTime(time);
		setAttributes(res, attributes);
		return res;
	}
 */


	public WasGeneratedBy newWasGeneratedBy(WasGeneratedBy wgb) {
		return newWasGeneratedBy(wgb.getId(), wgb.getEntity(), wgb.getActivity(), wgb.getTime(), getAllAttributes(wgb));
	}

	protected List<Attribute> getAllAttributes(Statement s) {
		List<Attribute> attrs=new LinkedList<>();
		if (s instanceof HasRole) {
			if ((((HasRole) s).getRole()) != null) attrs.addAll(((HasRole) s).getRole());
		}
		if (s instanceof HasRole) {
			if (((HasType) s).getType()!=null) attrs.addAll(((HasType) s).getType());
		}
		if (s instanceof HasLabel) {
			if (((HasLabel) s).getLabel() != null)
				attrs.addAll(((HasLabel) s).getLabel().stream().map(l -> newLabel(l, this.getName().XSD_STRING)).collect(Collectors.toList()));
		}
		if (s instanceof HasLocation) {
			if (((HasLocation) s).getLocation()!=null) attrs.addAll(((HasLocation) s).getLocation());
		}
		if (s instanceof HasOther) {
			if (((HasOther) s).getOther()!=null) attrs.addAll(((HasOther) s).getOther());
		}
		return attrs;
	}


	/**A factory method to create an instance of an influence {@link WasInfluencedBy}
	 * @param id optional identifier identifying the association
	 * @param influencee an identifier for an entity, activity, or agent
	 * @param influencer an identifier for an ancestor entity, activity, or agent that the former depends on
	 * @return an instance of {@link WasInfluencedBy}
	 */

	public WasInfluencedBy newWasInfluencedBy(QualifiedName id,
											  QualifiedName influencee,
											  QualifiedName influencer) {
		return newWasInfluencedBy(id, influencee, influencer, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasInfluencedBy(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)


	public WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName influencee, QualifiedName influencer, Collection<Attribute> attributes) {
		WasInfluencedBy res=newWasInfluencedBy(id,influencee,influencer);
		setAttributes(res, attributes);
		return res;
	}
 */

	public WasInfluencedBy newWasInfluencedBy(WasInfluencedBy in) {
		return newWasInfluencedBy(in.getId(), in.getInfluencee(), in.getInfluencer(), getAllAttributes(in));
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
		return newWasInformedBy(id, informed, informant, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasInformedBy(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
	 */
/*
	public WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName a2, QualifiedName a1, Collection<Attribute> attributes) {
		WasInformedBy res=newWasInformedBy(id,a2,a1);
		setAttributes(res, attributes);
		return res;
	}

 */


	public WasInformedBy newWasInformedBy(WasInformedBy d) {
		return newWasInformedBy(d.getId(), d.getInformed(), d.getInformant(), getAllAttributes(d));
	}


	public WasInvalidatedBy newWasInvalidatedBy(QualifiedName eid,
												QualifiedName aid) {
		return newWasInvalidatedBy(null, eid, aid, null, null);
	}

	/** A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
	 * @param id an optional identifier for a usage
	 * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
	 * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
	 * @return an instance of {@link WasInvalidatedBy}
	 */

	public WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, QualifiedName entity, QualifiedName activity) {
		return newWasInvalidatedBy(id, entity, activity, null, null);
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasInvalidatedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
	 */
	/*
	public WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
		WasInvalidatedBy res=newWasInvalidatedBy(id,entity,activity);
		res.setTime(time);
		setAttributes(res, attributes);
		return res;
	}
	 */

	public WasInvalidatedBy newWasInvalidatedBy(WasInvalidatedBy u) {
		return newWasInvalidatedBy(u.getId(), u.getEntity(), u.getActivity(), u.getTime(), getAttributes(u));
	}

	/** A factory method to create an instance of a start {@link WasStartedBy}
	 * @param id an identifier for a start
	 * @return an instance of {@link WasStartedBy}
	 */

	public WasStartedBy newWasStartedBy(QualifiedName id) {
		return newWasStartedBy(id, null, null, null, null, null);
	}

	public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger) {
		return newWasStartedBy(id, activity, trigger, null, null, null);
	}

	/** A factory method to create an instance of a start {@link WasStartedBy}
	 * @param id optional identifier for a start
	 * @param activity an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
	 * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
	 * @param starter an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
	 * @return an instance of {@link WasStartedBy}
	 */

	public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName starter) {
		return newWasStartedBy(id, activity, trigger, starter, null, null);
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.model.ModelConstructor#newWasStartedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)

	public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName starter, XMLGregorianCalendar time, Collection<Attribute> attributes) {
		WasStartedBy res=newWasStartedBy(id,activity,trigger);
		res.setTime(time);
		res.setStarter(starter);
		setAttributes(res, attributes);
		return res;
	}
*/

	public WasStartedBy newWasStartedBy(WasStartedBy u) {
		return newWasStartedBy(u.getId(), u.getActivity(), u.getTrigger(), u.getStarter(), u.getTime(), getAttributes(u));
	}
	public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar gc) {
		return dataFactory.newXMLGregorianCalendar(gc);
	}

	public XMLGregorianCalendar newYear(int year) {
		XMLGregorianCalendar res=dataFactory.newXMLGregorianCalendar();
		res.setYear(year);
		return res;
	}
	public Collection<Attribute> getAttributes(Statement statement) {
		Collection<Attribute> result=new LinkedList<>();
		if (statement instanceof HasType)     result.addAll(((HasType)statement).getType());
		if (statement instanceof HasLocation) result.addAll(((HasLocation)statement).getLocation());
		if (statement instanceof HasRole)     result.addAll(((HasRole)statement).getRole());
		if (statement instanceof HasValue) {
			Value val=((HasValue)statement).getValue();
			if (val!=null) {
				result.add(val);
			}
		}
		if (statement instanceof HasOther) {
			result.addAll(((HasOther) statement).getOther());
		}
		return result;
	}


	public void setAttributes(HasOther res, Collection<Attribute> attributes) {
		if (attributes==null) return;
		if (attributes.isEmpty()) return;
		HasType typ=(res instanceof HasType)? (HasType)res : null;
		HasLocation loc=(res instanceof HasLocation)? (HasLocation)res : null;
		HasLabel lab=(res instanceof HasLabel)? (HasLabel)res : null;
		HasValue aval=(res instanceof HasValue)? (HasValue)res : null;
		HasRole rol=(res instanceof HasRole)? (HasRole)res : null;

		for (Attribute attr: attributes) {

			Object aValue=attr.getValue();

			ValueConverter vconv=new ValueConverter(this);
			if (getName().RDF_LITERAL.equals(attr.getType())&& (aValue instanceof String)) {
				System.out.println("Converting " + aValue);
				aValue=vconv.convertToJava(attr.getType(),(String)aValue);
			}



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

	public Namespace newNamespace(Namespace ns) {
		return new Namespace(ns);
	}

	public Namespace newNamespace() {
		return new Namespace();
	}



	public AlternateOf newAlternateOf(AlternateOf s) {
        return newAlternateOf(s.getAlternate1(), s.getAlternate2());
	}

	public SpecializationOf newSpecializationOf(SpecializationOf s) {
        return newSpecializationOf(s.getSpecificEntity(), s.getGeneralEntity());
	}

	public HadMember newHadMember(HadMember s) {
        return newHadMember(s.getCollection(), s.getEntity());
	}

	ProvUtilities util=new ProvUtilities();

	@SuppressWarnings("unchecked")
	public <T extends Statement> T newStatement(T s) {
		return (T) util.doAction(s,new Cloner());
	}

	public class Cloner implements StatementActionValue {

		@Override
		public Object doAction(Activity s) {
			return newActivity(s);
		}

		@Override
		public Object doAction(Used s) {
			return newUsed(s);
		}

		@Override
		public Object doAction(WasStartedBy s) {
			return newWasStartedBy(s);
		}

		@Override
		public Object doAction(Agent s) {
			return newAgent(s);
		}

		@Override
		public Object doAction(AlternateOf s) {
			return newAlternateOf(s);
		}

		@Override
		public Object doAction(WasAssociatedWith s) {
			return newWasAssociatedWith(s);
		}

		@Override
		public Object doAction(WasAttributedTo s) {
			return newWasAttributedTo(s);
		}

		@Override
		public Object doAction(WasInfluencedBy s) {
			return newWasInfluencedBy(s);
		}

		@Override
		public Object doAction(ActedOnBehalfOf s) {
			return newActedOnBehalfOf(s);
		}

		@Override
		public Object doAction(WasDerivedFrom s) {
			return newWasDerivedFrom(s);
		}

		@Override
		public Object doAction(DictionaryMembership s) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object doAction(DerivedByRemovalFrom s) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object doAction(WasEndedBy s) {
			return newWasEndedBy(s);
		}

		@Override
		public Object doAction(Entity s) {
			return newEntity(s);
		}

		@Override
		public Object doAction(WasGeneratedBy s) {
			return newWasGeneratedBy(s);
		}

		@Override
		public Object doAction(WasInvalidatedBy s) {
			return newWasInvalidatedBy(s);
		}

		@Override
		public Object doAction(HadMember s) {
			return newHadMember(s);
		}

		@Override
		public Object doAction(MentionOf s) {
			return newMentionOf(s);
		}

		@Override
		public Object doAction(SpecializationOf s) {
			return newSpecializationOf(s);
		}
		@Override
		public Object doAction(QualifiedSpecializationOf s) {
			return newQualifiedSpecializationOf(s.getId(),s.getSpecificEntity(),s.getGeneralEntity(), getAttributes(s));
		}

		@Override
		public Object doAction(QualifiedHadMember s) {
			return newQualifiedHadMember(s.getId(),s.getCollection(),s.getEntity(),getAttributes(s));
		}

		@Override
		public Object doAction(DerivedByInsertionFrom s) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object doAction(WasInformedBy s) {
			return newWasInformedBy(s);
		}

		@Override
		public Object doAction(Bundle s, ProvUtilities provUtilities) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object doAction(QualifiedAlternateOf s) {
			return newQualifiedAlternateOf(s.getId(),s.getAlternate1(),s.getAlternate2(),getAttributes(s));
		}

	}

}
