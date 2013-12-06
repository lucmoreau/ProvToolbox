/**
 * 
 */
package org.openprovenance.prov.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.model.Entry;
import org.openprovenance.prov.model.Key;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.InternationalizedString;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.QNameExport;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
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

/**
 * @author Trung Dong Huynh
 * 
 *         Constructing JSON-friendly structure from a Document
 * 
 */
public class JSONConstructor implements ModelConstructor {
    private class JsonProvRecord {
	String type;
	String id;
	List<Object[]> attributes;

	public JsonProvRecord(String type, String id, List<Object[]> attributes) {
	    this.type = type;
	    this.id = id;
	    this.attributes = attributes;
	}
    }

    final private QNameExport qnExport;
    private Map<String, Object> documentBundles = new HashMap<String, Object>();
    private List<JsonProvRecord> documentRecords = new ArrayList<JsonProvRecord>();
    private Map<String, String> documentNamespaces = null;
    private List<JsonProvRecord> currentRecords = documentRecords;
    private Map<String, String> currentNamespaces = null;

    private final ProvFactory pf = new ProvFactory();

    public JSONConstructor(QNameExport qnExport) {
	this.qnExport = qnExport;
    }

    public Map<String, Object> getJSONStructure() {
	// Build the document-level structure
	Map<String, Object> document = getJSONStructure(documentRecords,
							documentNamespaces);
	;
	if (!documentBundles.isEmpty())
	    document.put("bundle", documentBundles);
	return document;
    }

    public Map<String, Object> getJSONStructure(List<JsonProvRecord> records,
						Map<String, String> namespaces) {
	Map<String, Object> bundle = new HashMap<String, Object>();
	if (namespaces != null)
	    bundle.put("prefix", namespaces);
	for (Object o : records) {
	    if (o == null)
		continue;
	    JsonProvRecord record = (JsonProvRecord) o;
	    String type = record.type;

	    Map<Object, Object> structure = (Map<Object, Object>) bundle.get(type);
	    if (structure == null) {
		structure = new HashMap<Object, Object>();
		bundle.put(type, structure);
	    }
	    Map<Object, Object> hash = new HashMap<Object, Object>();
	    List<Object[]> tuples = (List<Object[]>) record.attributes;
	    for (Object[] tuple : tuples) {
		Object attribute = tuple[0];
		Object value = tuple[1];
		if (hash.containsKey(attribute)) {
		    Object existing = hash.get(attribute);
		    if (existing instanceof List) {
			// Already a multi-value attribute
			List<Object> values = (List<Object>) existing;
			values.add(value);
		    } else {
			// A multi-value list needs to be created
			List<Object> values = new ArrayList<Object>();
			values.add(existing);
			values.add(value);
			hash.put(attribute, values);
		    }
		} else {
		    hash.put(attribute, value);
		}
	    }
	    if (structure.containsKey(record.id)) {
		Object existing = structure.get(record.id);
		if (existing instanceof List) {
		    List<Object> values = (List<Object>) existing;
		    values.add(hash);
		} else {
		    // A multi-value list needs to be created
		    List<Object> values = new ArrayList<Object>();
		    values.add(existing);
		    values.add(hash);
		    structure.put(record.id, values);
		}
	    } else
		structure.put(record.id, hash);
	}
	return bundle;
    }

    private static final Map<String, Integer> countMap = new HashMap<String, Integer>();

    private static String getBlankID(String type) {
	if (!countMap.containsKey(type)) {
	    countMap.put(type, 0);
	}
	int count = countMap.get(type);
	count += 1;
	countMap.put(type, count);
	return "_:" + type + count;
    }

    private Object[] tuple(Object o1, Object o2) {
	Object[] tuple = { o1, o2 };
	return tuple;
    }

    private Object typedLiteral(String value, String datatype, String lang) {
	// TODO: Converting default types to JSON primitives
	if (datatype == "xsd:string" && lang == null)
	    return value;
	if (datatype == "xsd:double")
	    return Double.parseDouble(value);
	if (datatype == "xsd:int")
	    return Integer.parseInt(value);
	if (datatype == "xsd:boolean")
	    return Boolean.parseBoolean(value);

	Map<String, String> result = new HashMap<String, String>();
	result.put("$", value);
	if (datatype != null) {
	    result.put("type", datatype);
	}
	if (lang != null) {
	    result.put("lang", lang);
	}
	return result;
    }

    private String convertValueToString(Object value) {
	if (value instanceof String) {
	    return (String) value;
	}

	if (value instanceof QualifiedName)
	    return qnExport.qnameToString((QualifiedName) value);
	if (value instanceof InternationalizedString) {
	    InternationalizedString iStr = (InternationalizedString) value;
	    return iStr.getValue();
	}
	return value.toString();
    }

    private Object convertValue(Object value) {
	if (value instanceof String || value instanceof Double
		|| value instanceof Integer || value instanceof Boolean)
	    return value;

	if (value instanceof QualifiedName) {
	    return typedLiteral(qnExport.qnameToString((QualifiedName) value),
				"xsd:QName", null);
	}
	if (value instanceof InternationalizedString) {
	    InternationalizedString iStr = (InternationalizedString) value;
	    String lang = iStr.getLang();
	    if (lang != null)
		// If 'lang' is defined
		return typedLiteral(iStr.getValue(),
				    "prov:InternationalizedString", // "xsd:string",
				    lang);
	    else
		return iStr.getValue();
	}
	// TODO: Raise an exception?
	return null;
    }

    private Object[] convertAttribute(Attribute attr) {
	String attrName = qnExport.qnameToString(attr.getElementName());
	Object value = attr.getValue();
	Object attrValue;
 	if (value instanceof QualifiedName) {
	    // force type xsd:QName

	    attrValue = typedLiteral(qnExport.qnameToString((QualifiedName) value),
				     "xsd:QName", null);
	} else  if (value instanceof InternationalizedString) {
	    InternationalizedString iStr = (InternationalizedString) value;
	    String lang = iStr.getLang();
	    if (lang != null) {
		// If 'lang' is defined
		attrValue = typedLiteral(iStr.getValue(),
					 "prov:InternationalizedString", // "xsd:string",
					 lang);
	    } else {
		// Otherwise, just return the string
		attrValue = iStr.getValue();
	    }
	} else {
	    String datatype = qnExport.qnameToString(attr.getType());
	    attrValue = typedLiteral(value.toString(), datatype, null);
	}
	return tuple(attrName, attrValue);
    }

    private List<Object[]> convertAttributes(Collection<Attribute> attrs) {
	List<Object[]> result = new ArrayList<Object[]>();
	if (attrs != null)
	    for (Attribute attr : attrs) {
		result.add(convertAttribute(attr));
	    }
	return result;
    }

    @Override
    public Entity newEntity(QualifiedName id, Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	JsonProvRecord record = new JsonProvRecord("entity",
						   qnExport.qnameToString(id),
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public Activity newActivity(QualifiedName id, XMLGregorianCalendar startTime,
				XMLGregorianCalendar endTime,
				Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (startTime != null) {
	    attrs.add(tuple("prov:startTime", startTime.toXMLFormat()));
	}
	if (endTime != null) {
	    attrs.add(tuple("prov:endTime", endTime.toXMLFormat()));
	}
	JsonProvRecord record = new JsonProvRecord("activity",
						   qnExport.qnameToString(id),
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public Agent newAgent(QualifiedName id, Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	JsonProvRecord record = new JsonProvRecord("agent",
						   qnExport.qnameToString(id),
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity,
			XMLGregorianCalendar time,
			Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (activity != null)
	    attrs.add(tuple("prov:activity", qnExport.qnameToString(activity)));
	if (entity != null)
	    attrs.add(tuple("prov:entity", qnExport.qnameToString(entity)));
	if (time != null)
	    attrs.add(tuple("prov:time", time.toXMLFormat()));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("u");
	JsonProvRecord record = new JsonProvRecord("used", recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity,
					    QualifiedName activity,
					    XMLGregorianCalendar time,
					    Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (entity != null)
	    attrs.add(tuple("prov:entity", qnExport.qnameToString(entity)));
	if (activity != null)
	    attrs.add(tuple("prov:activity", qnExport.qnameToString(activity)));
	if (time != null)
	    attrs.add(tuple("prov:time", time.toXMLFormat()));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("wGB");
	JsonProvRecord record = new JsonProvRecord("wasGeneratedBy", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, 
                                                QualifiedName entity,
						QualifiedName activity,
						XMLGregorianCalendar time,
						Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (entity != null)
	    attrs.add(tuple("prov:entity", qnExport.qnameToString(entity)));
	if (activity != null)
	    attrs.add(tuple("prov:activity", qnExport.qnameToString(activity)));
	if (time != null)
	    attrs.add(tuple("prov:time", time.toXMLFormat()));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("wIB");
	JsonProvRecord record = new JsonProvRecord("wasInvalidatedBy",
						   recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity,
					QualifiedName trigger, QualifiedName starter,
					XMLGregorianCalendar time,
					Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (activity != null)
	    attrs.add(tuple("prov:activity", qnExport.qnameToString(activity)));
	if (trigger != null)
	    attrs.add(tuple("prov:trigger", qnExport.qnameToString(trigger)));
	if (starter != null)
	    attrs.add(tuple("prov:starter", qnExport.qnameToString(starter)));
	if (time != null)
	    attrs.add(tuple("prov:time", time.toXMLFormat()));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("wSB");
	JsonProvRecord record = new JsonProvRecord("wasStartedBy", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger,
				    QualifiedName ender, XMLGregorianCalendar time,
				    Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (activity != null)
	    attrs.add(tuple("prov:activity", qnExport.qnameToString(activity)));
	if (trigger != null)
	    attrs.add(tuple("prov:trigger", qnExport.qnameToString(trigger)));
	if (ender != null)
	    attrs.add(tuple("prov:ender", qnExport.qnameToString(ender)));
	if (time != null)
	    attrs.add(tuple("prov:time", time.toXMLFormat()));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("wEB");
	JsonProvRecord record = new JsonProvRecord("wasEndedBy", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasDerivedFrom newWasDerivedFrom(QualifiedName id, QualifiedName e2, QualifiedName e1,
					    QualifiedName activity, QualifiedName generation,
					    QualifiedName usage,
					    Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (e2 != null)
	    attrs.add(tuple("prov:generatedEntity", qnExport.qnameToString(e2)));
	if (e1 != null)
	    attrs.add(tuple("prov:usedEntity", qnExport.qnameToString(e1)));
	if (activity != null)
	    attrs.add(tuple("prov:activity", qnExport.qnameToString(activity)));
	if (generation != null)
	    attrs.add(tuple("prov:generation",
			    qnExport.qnameToString(generation)));
	if (usage != null)
	    attrs.add(tuple("prov:usage", qnExport.qnameToString(usage)));

	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("wDF");
	JsonProvRecord record = new JsonProvRecord("wasDerivedFrom", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasAssociatedWith newWasAssociatedWith(QualifiedName id,
						  QualifiedName a,
						  QualifiedName ag,
						  QualifiedName plan,
						  Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (a != null)
	    attrs.add(tuple("prov:activity", qnExport.qnameToString(a)));
	if (ag != null)
	    attrs.add(tuple("prov:agent", qnExport.qnameToString(ag)));
	if (plan != null)
	    attrs.add(tuple("prov:plan", qnExport.qnameToString(plan)));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("wAW");
	JsonProvRecord record = new JsonProvRecord("wasAssociatedWith",
						   recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasAttributedTo newWasAttributedTo(QualifiedName id, QualifiedName e, QualifiedName ag,
					      Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (e != null)
	    attrs.add(tuple("prov:entity", qnExport.qnameToString(e)));
	if (ag != null)
	    attrs.add(tuple("prov:agent", qnExport.qnameToString(ag)));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("wAT");
	JsonProvRecord record = new JsonProvRecord("wasAttributedTo", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, QualifiedName ag2, QualifiedName ag1,
					      QualifiedName a,
					      Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (ag2 != null)
	    attrs.add(tuple("prov:delegate", qnExport.qnameToString(ag2)));
	if (ag1 != null)
	    attrs.add(tuple("prov:responsible", qnExport.qnameToString(ag1)));
	if (a != null)
	    attrs.add(tuple("prov:activity", qnExport.qnameToString(a)));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("aOBO");
	JsonProvRecord record = new JsonProvRecord("actedOnBehalfOf", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName a2, QualifiedName a1,
					  Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (a2 != null)
	    attrs.add(tuple("prov:informed", qnExport.qnameToString(a2)));
	if (a1 != null)
	    attrs.add(tuple("prov:informant", qnExport.qnameToString(a1)));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("Infm");
	JsonProvRecord record = new JsonProvRecord("wasInformedBy", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName a2, QualifiedName a1,
					      Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (a2 != null)
	    attrs.add(tuple("prov:influencee", qnExport.qnameToString(a2)));
	if (a1 != null)
	    attrs.add(tuple("prov:influencer", qnExport.qnameToString(a1)));
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("Infl");
	JsonProvRecord record = new JsonProvRecord("wasInfluencedBy", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public AlternateOf newAlternateOf(QualifiedName e2, QualifiedName e1) {
	List<Object[]> attrs = new ArrayList<Object[]>();
	if (e2 != null)
	    attrs.add(tuple("prov:alternate2", qnExport.qnameToString(e2)));
	if (e1 != null)
	    attrs.add(tuple("prov:alternate1", qnExport.qnameToString(e1)));
	String recordID = getBlankID("aO");
	JsonProvRecord record = new JsonProvRecord("alternateOf", recordID,
						   attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public SpecializationOf newSpecializationOf(QualifiedName e2, QualifiedName e1) {
	List<Object[]> attrs = new ArrayList<Object[]>();
	if (e2 != null)
	    attrs.add(tuple("prov:specificEntity", qnExport.qnameToString(e2)));
	if (e1 != null)
	    attrs.add(tuple("prov:generalEntity", qnExport.qnameToString(e1)));
	String recordID = getBlankID("sO");
	JsonProvRecord record = new JsonProvRecord("specializationOf",
						   recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public MentionOf newMentionOf(QualifiedName e2, QualifiedName e1, QualifiedName b) {
	List<Object[]> attrs = new ArrayList<Object[]>();
	if (e2 != null)
	    attrs.add(tuple("prov:specificEntity", qnExport.qnameToString(e2)));
	if (e1 != null)
	    attrs.add(tuple("prov:generalEntity", qnExport.qnameToString(e1)));
	if (b != null)
	    attrs.add(tuple("prov:bundle", qnExport.qnameToString(b)));
	String recordID = getBlankID("mO");
	JsonProvRecord record = new JsonProvRecord("mentionOf", recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public HadMember newHadMember(QualifiedName c, Collection<QualifiedName> e) {
	List<Object[]> attrs = new ArrayList<Object[]>();
	if (c != null)
	    attrs.add(tuple("prov:collection", qnExport.qnameToString(c)));
	if (e != null && !e.isEmpty()) {
	    List<String> entityList = new ArrayList<String>();
	    for (QualifiedName entity : e)
		entityList.add(qnExport.qnameToString(entity));
	    attrs.add(tuple("prov:entity", entityList));
	}
	// TODO Add id to the interface
	QualifiedName id = null;
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("hM");
	JsonProvRecord record = new JsonProvRecord("hadMember", recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public Document newDocument(Namespace namespaces,
				Collection<Statement> statements,
				Collection<NamedBundle> bundles) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public NamedBundle newNamedBundle(QualifiedName id, Namespace namespaces,
				      Collection<Statement> statements) {
	Object bundle = getJSONStructure(currentRecords, currentNamespaces);
	documentBundles.put(qnExport.qnameToString(id), bundle);
	// Reset to document-level records and namespaces
	currentRecords = documentRecords;
	currentNamespaces = documentNamespaces;
	return null;
    }

    @Override
    public void startDocument(Namespace namespace) {
	Hashtable<String, String> hashtable = namespace.getPrefixes();
	if (namespace.getDefaultNamespace()!=null) {
	    hashtable.put("default", namespace.getDefaultNamespace());
	}
	// Make a copy of the namespace table
	if (hashtable != null && !hashtable.isEmpty()) {
	    documentNamespaces = new Hashtable<String, String>(hashtable);
	    currentNamespaces = documentNamespaces;
	}
    }

    @Override
    public void startBundle(QualifiedName bundleId, Namespace namespaces) {
	Hashtable<String, String> hashtable = namespaces.getPrefixes();

	// Make a copy of the namespace table
	if (hashtable != null && !hashtable.isEmpty())
	    currentNamespaces = new Hashtable<String, String>(hashtable);
	// Create a separate list of records for the bundle
	currentRecords = new ArrayList<JsonProvRecord>();
    }

    private Object encodeKeyEntitySet(List<Entry> keyEntitySet) {
	// Check for the types of keys
	boolean isAllKeyOfSameDatatype = true;
	Key firstKey = keyEntitySet.get(0).getKey();
	QualifiedName firstKeyClass = firstKey.getType();
	for (Entry pair : keyEntitySet) {
	    QualifiedName keyClass = pair.getKey().getType();

	    if (keyClass != firstKeyClass) {
		isAllKeyOfSameDatatype = false;
		break;
	    }
	}

	if (isAllKeyOfSameDatatype) {
	    // encode as a dictionary
	    Map<Object, String> dictionary = new HashMap<Object, String>();
	    String keyDatatype = qnExport.qnameToString(keyEntitySet.get(0).getKey().getType());
	    dictionary.put("$key-datatype", keyDatatype);
	    for (Entry pair : keyEntitySet) {
		// String key = convertValueToString(pair.key);
		String key = convertValueToString(pair.getKey().getValueAsObject());
		String entity = qnExport.qnameToString(pair.getEntity());
		dictionary.put(key, entity);
	    }
	    return dictionary;
	}

	// encode as a generic list of key-value pairs
	List<Map<String, Object>> values = new ArrayList<Map<String, Object>>(
									      keyEntitySet.size());
	for (Entry pair : keyEntitySet) {
	    Object entity = qnExport.qnameToString(pair.getEntity());
	    Map<String, Object> item = new Hashtable<String, Object>();
	    item.put("$", entity);
	    item.put("key", convertValue(pair.getKey().getValueAsObject()));
	    values.add(item);
	}
	return values;
    }

    @Override
    public DerivedByInsertionFrom newDerivedByInsertionFrom(QualifiedName id,
							    QualifiedName after,
							    QualifiedName before,
							    List<Entry> keyEntitySet,
							    Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (after != null)
	    attrs.add(tuple("prov:after", qnExport.qnameToString(after)));
	if (before != null)
	    attrs.add(tuple("prov:before", qnExport.qnameToString(before)));
	if (keyEntitySet != null && !keyEntitySet.isEmpty()) {
	    attrs.add(tuple("prov:key-entity-set",
			    encodeKeyEntitySet(keyEntitySet)));
	}
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("dBIF");
	JsonProvRecord record = new JsonProvRecord("derivedByInsertionFrom",
						   recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public DerivedByRemovalFrom newDerivedByRemovalFrom(QualifiedName id,
							QualifiedName after,
							QualifiedName before,
							List<Key> keys,
							Collection<Attribute> attributes) {
	List<Object[]> attrs = convertAttributes(attributes);
	if (after != null)
	    attrs.add(tuple("prov:after", qnExport.qnameToString(after)));
	if (before != null)
	    attrs.add(tuple("prov:before", qnExport.qnameToString(before)));
	if (keys != null && !keys.isEmpty()) {
	    List<Object> values = new ArrayList<Object>(keys.size());
	    for (Key key : keys) {

		values.add(convertValue(key.getValue()));

	    }
	    attrs.add(tuple("prov:key-set", values));
	}
	String recordID = (id != null) ? qnExport.qnameToString(id)
		: getBlankID("dBRF");
	JsonProvRecord record = new JsonProvRecord("derivedByRemovalFrom",
						   recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public DictionaryMembership newDictionaryMembership(QualifiedName dict,
							List<Entry> keyEntitySet) {
	List<Object[]> attrs = new ArrayList<Object[]>();
	if (dict != null)
	    attrs.add(tuple("prov:dictionary", qnExport.qnameToString(dict)));
	if (keyEntitySet != null && !keyEntitySet.isEmpty()) {
	    attrs.add(tuple("prov:key-entity-set",
			    encodeKeyEntitySet(keyEntitySet)));
	}
	String recordID = getBlankID("hDM");
	JsonProvRecord record = new JsonProvRecord("hadDictionaryMember",
						   recordID, attrs);
	this.currentRecords.add(record);
	return null;
    }

    @Override
    public QualifiedName newQualifiedName(String namespace, String local,
					  String prefix) {	return null;
    }

}
