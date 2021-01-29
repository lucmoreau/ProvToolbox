package org.openprovenance.prov.json;

import com.google.gson.*;
import org.openprovenance.prov.model.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Type;
import java.util.*;

/**
 * A Gson deserializer adapter for PROV-JSON Decode a PROV-JSON structure and
 * produces a {@link Document}
 *
 * @author Trung Dong Huynh &lt;trungdong@donggiang.com&gt;
 *
 */
public class ProvDocumentDeserializer implements JsonDeserializer<Document> {
	Namespace documentNamespace;
	Namespace currentNamespace;



	private static final String PROV_JSON_PREFIX = "prefix";


	private final ProvFactory pf;
	private final Name name;
	private final ValueConverter vconv;

	public ProvDocumentDeserializer(ProvFactory pf) {
		this.pf=pf;
		this.name=pf.getName();
		this.vconv = new ValueConverter(pf);
	}

	public QualifiedName stringToQualifiedName(Namespace namespace,String val, ProvFactory pf, boolean flag) {
		//	try {
		//	    val=URLDecoder.decode(val,"UTF-8");
		//	} catch (java.io.UnsupportedEncodingException e1) {
		// encoding known to be correct
		//	}
		return namespace.stringToQualifiedName(val,pf,flag);
	}

	@Override
	public Document deserialize(JsonElement json, Type typeOfT,
								JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject provJSONDoc = json.getAsJsonObject();

		// Initialise namespaces
		currentNamespace = decodePrefixes(provJSONDoc);
		documentNamespace = currentNamespace;



		// Decoding structures
		List<StatementOrBundle> statements = decodeBundle(provJSONDoc);

		// Create the document
		Document doc = pf.newDocument();
		doc.setNamespace(currentNamespace);

		doc.getStatementOrBundle().addAll(statements);
		return doc;
	}

	private Namespace decodePrefixes(JsonObject bundleStructure) {
		Namespace ns = new Namespace();
		// prefixes prov and xsd are implicit in prov-json
		ns.addKnownNamespaces();
		JsonObject prefixes = getObjectAndRemove(bundleStructure, PROV_JSON_PREFIX);
		if (prefixes != null) {
			for (String prefix : prefixes.keySet()) {
				//String prefix = pair.getKey();
				String uri = prefixes.get(prefix).toString();
				if (prefix.equals("default")) {
					ns.registerDefault(uri);
				} else {
					ns.register(prefix, uri);
				}
			}
		}
		return ns;
	}

	private List<StatementOrBundle> decodeBundle(JsonObject bundleStructure) {
		List<StatementOrBundle> statements = new ArrayList<StatementOrBundle>();
		for (String statementType : bundleStructure.keySet()) {
			JsonObject statementMap = bundleStructure.get(statementType).getAsJsonObject();
			statements.addAll(decodeElements(statementType, statementMap));
		}
		return statements;
	}

	private List<StatementOrBundle> decodeElements(String statementType,
												   JsonObject statementMap) {
		List<StatementOrBundle> statements = new ArrayList<StatementOrBundle>();
		for (String idStr : statementMap.keySet()) {
			JsonElement value=statementMap.get(idStr);
			if (value.isJsonArray()) {
				// Multiple elements having the same identifier
				JsonArray elements = value.getAsJsonArray();
				for (JsonElement element : elements) {
					JsonObject attributeMap = element.getAsJsonObject();
					StatementOrBundle statement = decodeStatement(statementType, idStr, attributeMap);
					statements.add(statement);
				}
			} else {
				JsonObject attributeMap = value.getAsJsonObject();
				StatementOrBundle statement = decodeStatement(statementType, idStr, attributeMap);
				statements.add(statement);
			}
		}
		return statements;
	}

	@SuppressWarnings("unchecked")
	private StatementOrBundle decodeStatement(String statementType,
											  String idStr,
											  JsonObject attributeMap) {
		StatementOrBundle statement;
		QualifiedName id;

		ProvJSONStatement provStatement = ProvJSONStatement.valueOf(statementType);
		switch (provStatement) {
			case bundle: {
				id=null; //we will deal with it later, when we have setup the namespace for the bundle
				break;
			}
			default: {
				if (idStr.startsWith("_:")) {
					// Ignore all blank node IDs
					id = null;
				} else {
					id = stringToQualifiedName(currentNamespace,idStr, pf, false);
				}
			}
		}

		// Decoding attributes
		switch (provStatement) {
			case entity:
				statement = pf.newEntity(id);
				break;
			case activity:
				XMLGregorianCalendar startTime = optionalTime("prov:startTime", attributeMap);
				XMLGregorianCalendar endTime = optionalTime("prov:endTime", attributeMap);
				Activity a = pf.newActivity(id);
				if (startTime != null) a.setStartTime(startTime);
				if (endTime != null) a.setEndTime(endTime);
				statement = a;
				break;
			case agent:
				statement = pf.newAgent(id);
				break;
			case alternateOf:
				QualifiedName alternate1 = optionalQualifiedName("prov:alternate1", attributeMap);
				QualifiedName alternate2 = optionalQualifiedName("prov:alternate2", attributeMap);
				statement = pf.newAlternateOf(alternate1, alternate2);
				break;
			case wasAssociatedWith:
				QualifiedName activity = optionalQualifiedName("prov:activity", attributeMap);
				QualifiedName agent = optionalQualifiedName("prov:agent", attributeMap);
				QualifiedName plan = optionalQualifiedName("prov:plan", attributeMap);
				WasAssociatedWith wAW = pf.newWasAssociatedWith(id, activity, agent);
				if (plan != null) wAW.setPlan(plan);
				statement = wAW;
				break;
			case wasAttributedTo:
				QualifiedName entity = optionalQualifiedName("prov:entity", attributeMap);
				agent = optionalQualifiedName("prov:agent", attributeMap);
				statement = pf.newWasAttributedTo(id, entity, agent);
				break;
			case bundle:
				Namespace ns = decodePrefixes(attributeMap);
				currentNamespace = ns;
				currentNamespace.setParent(documentNamespace);
				// Re-resolve the bundle's id with the bundle's namespace
				id = stringToQualifiedName(currentNamespace,idStr, pf, false);
				@SuppressWarnings("rawtypes")
				Collection statements = decodeBundle(attributeMap);
				Bundle namedBundle = pf.newNamedBundle(id,ns,statements);
				// namedBundle.setId(id);
				// namedBundle.setNamespace(ns);
				// namedBundle.getStatement()
				//    .addAll((Collection<? extends Statement>) statements);
				statement = namedBundle;

				// Restore the document's namespace as the current one
				currentNamespace = documentNamespace;
				break;
			case wasInformedBy:
				QualifiedName informed = optionalQualifiedName("prov:informed", attributeMap);
				QualifiedName informant = optionalQualifiedName("prov:informant", attributeMap);
				statement = pf.newWasInformedBy(id, informed, informant);
				break;
			case actedOnBehalfOf:
				QualifiedName delegate = optionalQualifiedName("prov:delegate", attributeMap);
				QualifiedName responsible = optionalQualifiedName("prov:responsible", attributeMap);
				activity = optionalQualifiedName("prov:activity", attributeMap);
				statement = pf.newActedOnBehalfOf(id, delegate, responsible, activity);
				break;
			case wasDerivedFrom:
				QualifiedName generatedEntity = optionalQualifiedName("prov:generatedEntity",
						attributeMap);
				QualifiedName usedEntity = optionalQualifiedName("prov:usedEntity", attributeMap);
				activity = optionalQualifiedName("prov:activity", attributeMap);
				QualifiedName generation = optionalQualifiedName("prov:generation", attributeMap);
				QualifiedName usage = optionalQualifiedName("prov:usage", attributeMap);
				statement = pf.newWasDerivedFrom(id, generatedEntity, usedEntity, activity, generation, usage,null);
				break;
			case wasEndedBy:
				activity = optionalQualifiedName("prov:activity", attributeMap);
				QualifiedName trigger = optionalQualifiedName("prov:trigger", attributeMap);
				QualifiedName ender = optionalQualifiedName("prov:ender", attributeMap);
				XMLGregorianCalendar time = optionalTime("prov:time", attributeMap);
				WasEndedBy wEB = pf.newWasEndedBy(id, activity, trigger);
				if (ender != null)
					wEB.setEnder(ender);
				if (time != null) {
					wEB.setTime(time);
				}
				statement = wEB;
				break;
			case wasGeneratedBy:
				entity = optionalQualifiedName("prov:entity", attributeMap);
				activity = optionalQualifiedName("prov:activity", attributeMap);
				time = optionalTime("prov:time", attributeMap);
				WasGeneratedBy wGB = pf.newWasGeneratedBy(id,entity,activity,time,new LinkedList<>());
				//wGB.setEntity(entity);
				// if (activity != null)
				//wGB.setActivity(activity);
				// if (time != null) {
				//wGB.setTime(time);
				// }
				statement = wGB;
				break;
			case wasInfluencedBy:
				QualifiedName influencee = anyRef("prov:influencee", attributeMap);
				QualifiedName influencer = anyRef("prov:influencer", attributeMap);
				statement = pf.newWasInfluencedBy(id, influencee, influencer);
				break;
			case wasInvalidatedBy:
				entity = optionalQualifiedName("prov:entity", attributeMap);
				activity = optionalQualifiedName("prov:activity", attributeMap);
				time = optionalTime("prov:time", attributeMap);
				WasInvalidatedBy wIB = pf.newWasInvalidatedBy(id, entity, activity);
				if (time != null) {
					wIB.setTime(time);
				}
				statement = wIB;
				break;
			case hadMember:
				QualifiedName collection = optionalQualifiedName("prov:collection", attributeMap);
				Collection<QualifiedName> entities = optionalQualifiedNames("prov:entity",
						attributeMap);
				HadMember membership = pf.newHadMember(collection,entities);
				//membership.setCollection(collection);
				//if (entities != null)
				//membership.getEntity().addAll(entities);
				statement = membership;
				break;
			case mentionOf:
				QualifiedName specificEntity = optionalQualifiedName("prov:specificEntity",
						attributeMap);
				QualifiedName generalEntity = optionalQualifiedName("prov:generalEntity",
						attributeMap);
				QualifiedName bundle = optionalQualifiedName("prov:bundle", attributeMap);
				statement = pf.newMentionOf(specificEntity, generalEntity, bundle);
				break;
			case specializationOf:
				specificEntity = optionalQualifiedName("prov:specificEntity", attributeMap);
				generalEntity = optionalQualifiedName("prov:generalEntity", attributeMap);
				statement = pf.newSpecializationOf(specificEntity, generalEntity);
				break;
			case wasStartedBy:
				activity = optionalQualifiedName("prov:activity", attributeMap);
				trigger = optionalQualifiedName("prov:trigger", attributeMap);
				QualifiedName starter = optionalQualifiedName("prov:starter", attributeMap);
				time = optionalTime("prov:time", attributeMap);
				WasStartedBy wSB = pf.newWasStartedBy(id, activity, trigger);
				if (starter != null)
					wSB.setStarter(starter);
				if (time != null) {
					wSB.setTime(time);
				}
				statement = wSB;
				break;
			case used:
				QualifiedName activity2 = optionalQualifiedName("prov:activity", attributeMap);
				QualifiedName entity2 = optionalQualifiedName("prov:entity", attributeMap);
				time = optionalTime("prov:time", attributeMap);
				Used wUB = pf.newUsed(id);
				wUB.setActivity(activity2);
				if (entity2 != null)
					wUB.setEntity(entity2);
				if (time != null) {
					wUB.setTime(time);
				}
				statement = wUB;
				break;
			case derivedByInsertionFrom:
				QualifiedName before = optionalQualifiedName("prov:before", attributeMap);
				QualifiedName after = optionalQualifiedName("prov:after", attributeMap);
				List<Entry> keyEntitySet = optionalEntrySet("prov:key-entity-set",
						attributeMap);
				DerivedByInsertionFrom dBIF = pf.newDerivedByInsertionFrom(id,
						after,
						before,
						keyEntitySet,
						null);
				statement = dBIF;
				break;
			case derivedByRemovalFrom:
				before = optionalQualifiedName("prov:before", attributeMap);
				after = optionalQualifiedName("prov:after", attributeMap);
				List<Key> keys = optionalKeySet("prov:key-set", attributeMap);
				DerivedByRemovalFrom dBRF = pf.newDerivedByRemovalFrom(id, after,
						before,
						keys, null);
				statement = dBRF;
				break;
			case hadDictionaryMember:
				QualifiedName dictionary = optionalQualifiedName("prov:dictionary", attributeMap);
				keyEntitySet = optionalEntrySet("prov:key-entity-set",
						attributeMap);
				DictionaryMembership hDM = pf.newDictionaryMembership(dictionary,
						keyEntitySet);
				statement = hDM;
				break;
			default:
				statement = null;
				break;

		}

		// All the remaining attributes can have multiple values, except
		// prov:value
		// prov:type decoding
		List<JsonElement> values = popMultiValAttribute("prov:type",
				attributeMap);
		if (!values.isEmpty()) {
			if (statement instanceof HasType) {
				List<org.openprovenance.prov.model.Type> types = ((HasType) statement).getType();
				for (JsonElement value : values) {
					types.add((org.openprovenance.prov.model.Type) decodeAttributeValue(value,
							name.PROV_TYPE));
				}
			} else {
				throw new UnsupportedOperationException(
						"prov:type is not allowed in a "
								+ statementType
								+ "statement - id: "
								+ idStr);
			}
		}
		// prov:label decoding
		values = popMultiValAttribute("prov:label", attributeMap);
		if (!values.isEmpty()) {
			if (statement instanceof HasLabel) {
				List<LangString> labels = ((HasLabel) statement).getLabel();
				for (JsonElement value : values) {
					labels.add(decodeInternationalizedString(value));
				}
			} else {
				throw new UnsupportedOperationException(
						"prov:label is not allowed in a "
								+ statementType
								+ "statement - id: "
								+ idStr);
			}
		}
		// prov:location decoding
		values = popMultiValAttribute("prov:location", attributeMap);
		if (!values.isEmpty()) {
			if (statement instanceof HasLocation) {
				List<Location> locations = ((HasLocation) statement).getLocation();
				for (JsonElement value : values) {
					locations.add((Location) decodeAttributeValue(value,
							name.PROV_LOCATION));
				}
			} else {
				throw new UnsupportedOperationException(
						"prov:location is not allowed in a "
								+ statementType
								+ "statement - id: "
								+ idStr);
			}
		}
		// prov:role decoding
		values = popMultiValAttribute("prov:role", attributeMap);
		if (!values.isEmpty()) {
			if (statement instanceof HasRole) {
				List<Role> roles = ((HasRole) statement).getRole();
				for (JsonElement value : values) {
					roles.add((Role) decodeAttributeValue(value,
							name.PROV_ROLE));
				}
			} else {
				throw new UnsupportedOperationException(
						"prov:role is not allowed in a "
								+ statementType
								+ "statement - id: "
								+ idStr);
			}
		}
		// prov:value decoding
		values = popMultiValAttribute("prov:value", attributeMap);
		if (!values.isEmpty()) {
			if (values.size() > 1) {
				// only one instance allowed
				throw new UnsupportedOperationException(
						"Only one instance of prov:value is allowed in a statement - id: "
								+ idStr);
			}
			if (statement instanceof HasValue) {
				((HasValue) statement).setValue((Value) decodeAttributeValue(values.get(0),
						name.PROV_VALUE));
			} else {
				throw new UnsupportedOperationException(
						"prov:value is not allowed in a "
								+ statementType
								+ "statement - id: "
								+ idStr);
			}
		}

		// arbitrary attribute decoding
		if (provStatement != ProvJSONStatement.bundle
				&& !attributeMap.keySet().isEmpty()) {
			if (statement instanceof HasOther) {
				// TODO: to clean up this casting
				@SuppressWarnings("rawtypes")
				List ll = ((HasOther) statement).getOther();
				List<Attribute> attributes = (List<Attribute>) ll;
				for (String key : attributeMap.keySet()) {
					QualifiedName attributeName = stringToQualifiedName(currentNamespace,key,pf,false);
					JsonElement element = attributeMap.get(key);
					values = pickMultiValues(element);
					for (JsonElement value : values) {

						Attribute attr = decodeAttributeValue(value,
								attributeName);
						attributes.add(attr);
					}
				}
			} else {
				throw new UnsupportedOperationException(
						"Arbitrary attributes are not allowed in a "
								+ statementType
								+ "statement - id: "
								+ idStr);
			}
		}

		return statement;
	}

	private LangString decodeInternationalizedString(JsonElement element) {
		LangString iString = pf.newInternationalizedString("s");
		if (element.isJsonPrimitive()) {
			iString.setValue(element.toString());
		} else {
			JsonObject struct = element.getAsJsonObject();
			String value = struct.get("$").getAsJsonPrimitive().getAsString();
			iString.setValue(value);
			if (struct.has("lang")) {
				String lang = struct.get("lang").getAsJsonPrimitive().toString();
				iString.setLang(lang);
			}
		}
		return iString;
	}

	private Attribute decodeAttributeValue(JsonElement element, QualifiedName elementName) {
		if (element.isJsonPrimitive()) {
			Object o = decodeJSONPrimitive(element.getAsJsonPrimitive().getAsString());
			QualifiedName type = vconv.getXsdType(o);
			return pf.newAttribute(elementName, o, type);
		} else {
			JsonObject struct = element.getAsJsonObject();
			String value = struct.get("$").getAsJsonPrimitive().getAsString();
			if (struct.has("lang")) {
				// Internationalized strings
				String lang = struct.get("lang").getAsJsonPrimitive().getAsString();
				LangString iString = pf.newInternationalizedString(value,lang);
				//iString.setValue(value);
				//iString.setLang(lang);
				return pf.newAttribute(elementName,
						iString,
						name.PROV_LANG_STRING);
			} else if (struct.has("type")) {
				String datatypeAsString = struct.get("type").getAsJsonPrimitive().getAsString();
				QualifiedName xsdType = stringToQualifiedName(currentNamespace,datatypeAsString, pf, false);
				if (xsdType.equals(name.PROV_QUALIFIED_NAME)) { /* we ignore xsdType.equals(name.FOR_XML_XSD_QNAME)  */
					return pf.newAttribute(elementName,
							stringToQualifiedName(currentNamespace,value,pf,false), xsdType);
				} else {
					return pf.newAttribute(elementName, value, xsdType);
				}
			} else {
				// if no type or lang information, decode as an
				// Internationalized string without lang
				LangString iString = pf.getObjectFactory()
						.createInternationalizedString();
				iString.setValue(value);
				return pf.newAttribute(elementName,
						iString,
						name.PROV_LANG_STRING);
			}
		}
	}

	private Object decodeJSONPrimitive(String value) {
		if (value == "true") {
			return true;
		} else if (value == "false") {
			return false;
		}
		try {
			// Try to parse an integer value first
			return Integer.parseInt(value);
		} catch (NumberFormatException ex1) {
			try {
				// Try double next
				return Double.parseDouble(value);
			} catch (NumberFormatException ex2) {
				// Not a number, return the string
				return value;
			}
		}
	}

	private List<JsonElement> popMultiValAttribute(String attributeName,
												   JsonObject attributeMap) {
		if (attributeMap.has(attributeName)) {
			JsonElement element = attributeMap.remove(attributeName);
			return pickMultiValues(element);
		} else
			return Collections.emptyList();
	}

	private List<JsonElement> pickMultiValues(JsonElement element) {
		if (element.isJsonArray()) {
			// Picking multiple values
			JsonArray array = element.getAsJsonArray();
			List<JsonElement> elements = new ArrayList<JsonElement>(
					array.size());
			Iterator<JsonElement> iter = array.iterator();
			while (iter.hasNext()) {
				elements.add(iter.next());
			}
			return elements;
		} else {
			// Return a single item list
			return Collections.singletonList(element);
		}
	}

	private String popString(JsonObject jo, String memberName) {
		return jo.remove(memberName).getAsJsonPrimitive().getAsString();
	}


	private QualifiedName qualifiedName(String attributeName, JsonObject attributeMap) {
		return stringToQualifiedName(currentNamespace,popString(attributeMap,
				attributeName),
				pf,
				false);
	}

	// TODO: this name is legacy, what should it be?
	private QualifiedName anyRef(String attributeName, JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return stringToQualifiedName(currentNamespace,popString(attributeMap,
					attributeName),
					pf,
					false);
		else
			return null;
	}



	private QualifiedName optionalQualifiedName(String attributeName, JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return qualifiedName(attributeName, attributeMap);
		else
			return null;
	}

	private XMLGregorianCalendar optionalTime(String attributeName,
											  JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return pf.newISOTime(popString(attributeMap, attributeName));
		else
			return null;
	}

	private Collection<QualifiedName> qualifiedNames(String attributeName,
													 JsonObject attributeMap) {
		List<QualifiedName> results = new ArrayList<QualifiedName>();
		List<JsonElement> elements = popMultiValAttribute(attributeName,
				attributeMap);
		for (JsonElement element : elements) {
			results.add(stringToQualifiedName(currentNamespace,element.getAsJsonPrimitive().getAsString(),pf,false));
		}
		return results;
	}

	private Collection<QualifiedName> optionalQualifiedNames(String attributeName,
															 JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return qualifiedNames(attributeName, attributeMap);
		else
			return null;
	}

	private List<Entry> entrySet(String attributeName,
								 JsonObject attributeMap) {
		List<Entry> results = new ArrayList<Entry>();
		// check if it is a dictionary or a list
		JsonElement kESElement = attributeMap.remove(attributeName);
		if (kESElement.isJsonArray()) {
			// decode it as a list of elements
			List<JsonElement> elements = pickMultiValues(kESElement);
			for (JsonElement element : elements) {
				JsonObject item = element.getAsJsonObject();
				Entry pair = pf.newEntry((Key) decodeAttributeValue(item.remove("key"),
						name.PROV_KEY),

						stringToQualifiedName(currentNamespace,this.popString(item, "$"),pf,false));
				results.add(pair);
			}
		} else if (kESElement.isJsonObject()) {
			// decode it as a dictionary whose keys are of the same datatype
			JsonObject dictionary = kESElement.getAsJsonObject();
			// TODO This does not conform with PROV-JSON !!!
			String keyDatatype = dictionary.remove("$key-datatype")
					.getAsJsonPrimitive().getAsString();
			QualifiedName datatype = stringToQualifiedName(currentNamespace,keyDatatype,pf,false);
			for (String entryKey : dictionary.keySet()) {
				JsonElement entryValue = dictionary.get(entryKey);

				Entry pair = decodeDictionaryEntry(datatype, entryKey,
						entryValue);
				results.add(pair);
			}
		}
		return results;
	}


	public Entry decodeDictionaryEntry(QualifiedName datatype,
									   String entryKey,
									   JsonElement entryValue) {

		Key kk;
		if (datatype.equals(name.PROV_QUALIFIED_NAME)) {
			kk=(Key) pf.newAttribute(name.PROV_KEY,
					stringToQualifiedName(currentNamespace,entryKey,pf,false), datatype);
		} else {
			kk=(Key) pf.newAttribute(name.PROV_KEY, entryKey, datatype);
		}


		Entry pair = pf.newEntry(kk,
				stringToQualifiedName(currentNamespace,entryValue.getAsJsonPrimitive().getAsString(), pf, false));
		return pair;
	}


	private List<Entry> optionalEntrySet(String attributeName,
										 JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return entrySet(attributeName, attributeMap);
		else
			return null;
	}


	private List<Key> keySet(String attributeName, JsonObject attributeMap) {
		List<Key> results = new ArrayList<Key>();
		List<JsonElement> elements = popMultiValAttribute(attributeName,
				attributeMap);
		for (JsonElement element : elements) {
			Key key = (Key) decodeAttributeValue(element, name.PROV_KEY);
			results.add(key);
		}
		return results;
	}

	private List<Key> optionalKeySet(String attributeName,
									 JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return keySet(attributeName, attributeMap);
		else
			return null;
	}

	private JsonObject getObjectAndRemove(JsonObject jsonObject,
										  String memberName) {
		if (jsonObject.has(memberName)) {
			JsonObject result = jsonObject.getAsJsonObject(memberName);
			jsonObject.remove(memberName);
			return result;
		} else
			return null;
	}


}
