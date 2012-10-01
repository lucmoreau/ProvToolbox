/**
 * 
 */
package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.ActivityRef;
import org.openprovenance.prov.xml.AgentRef;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.EntityRef;
import org.openprovenance.prov.xml.GenerationRef;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Statement;
import org.openprovenance.prov.xml.UsageRef;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInvalidatedBy;
import org.openprovenance.prov.xml.WasStartedBy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * A JSON decoder for PROV-JSON
 * Accepts a PROV-JSON string, produces a {@link Document}
 * @author tdh
 *
 */
public class ProvJSONDecoder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Document.class, new ProvDocumentDeserializer());
		Gson gson = gsonBuilder.create();
		
		try {
			Document provDoc = gson.fromJson(new BufferedReader(new FileReader(args[0])), Document.class);
			Utility u =  new Utility();
			String provN = u.convertBeanToASN(provDoc);
			System.out.println(provN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

enum ProvStatement {
	ENTITY,
	ACTIVITY,
	AGENT,
	GENERATION,
	USAGE,
	COMMUNICATION,
	START,
	END,
	INVALIDATION,
	ATTRIBUTION,
	ASSOCIATION,
	DERIVATION,
	DELEGATION,
	ALTERNATE,
	SPECIALIZATION,
	MENTION,
	COLLECTION,
	MEMBERSHIP,
	BUNDLE
}

class ProvDocumentDeserializer implements JsonDeserializer<Document> {
	private static final String PROV_JSON_PREFIX =              "prefix";
	private static final String PROV_JSON_ENTITY =              "entity";
	private static final String PROV_JSON_ACTIVITY =            "activity";
	private static final String PROV_JSON_GENERATION =          "wasGeneratedBy";
	private static final String PROV_JSON_USAGE =               "used";
	private static final String PROV_JSON_COMMUNICATION =       "wasInformedBy";
	private static final String PROV_JSON_START =               "wasStartedBy";
	private static final String PROV_JSON_END =                 "wasEndedBy";
	private static final String PROV_JSON_INVALIDATION =        "wasInvalidatedBy";
	private static final String PROV_JSON_DERIVATION =          "wasDerivedFrom";
	private static final String PROV_JSON_AGENT =               "agent";
	private static final String PROV_JSON_ATTRIBUTION =         "wasAttributedTo";
	private static final String PROV_JSON_ASSOCIATION =         "wasAssociatedWith";
	private static final String PROV_JSON_DELEGATION =          "actedOnBehalfOf";
	private static final String PROV_JSON_ALTERNATE =           "alternateOf";
	private static final String PROV_JSON_SPECIALIZATION =      "specializationOf";
	private static final String PROV_JSON_MENTION =             "mentionOf";
	private static final String PROV_JSON_COLLECTION =          "collection";
	private static final String PROV_JSON_MEMBERSHIP =          "memberOf";
	private static final String PROV_JSON_BUNDLE =              "bundle";
	
	private static final Map<String, ProvStatement> provTypeMap;
	static {
		Map<String, ProvStatement> map = new Hashtable<String, ProvStatement>();
		map.put(PROV_JSON_ENTITY, ProvStatement.ENTITY);
		map.put(PROV_JSON_ACTIVITY, ProvStatement.ACTIVITY);
		map.put(PROV_JSON_GENERATION, ProvStatement.GENERATION);
		map.put(PROV_JSON_USAGE, ProvStatement.USAGE);
		map.put(PROV_JSON_COMMUNICATION, ProvStatement.COMMUNICATION);
		map.put(PROV_JSON_START, ProvStatement.START);
		map.put(PROV_JSON_END, ProvStatement.END);
		map.put(PROV_JSON_INVALIDATION, ProvStatement.INVALIDATION);
		map.put(PROV_JSON_DERIVATION, ProvStatement.DERIVATION);
		map.put(PROV_JSON_AGENT, ProvStatement.AGENT);
		map.put(PROV_JSON_ATTRIBUTION, ProvStatement.ATTRIBUTION);
		map.put(PROV_JSON_ASSOCIATION, ProvStatement.ASSOCIATION);
		map.put(PROV_JSON_DELEGATION, ProvStatement.DELEGATION);
		map.put(PROV_JSON_ALTERNATE, ProvStatement.ALTERNATE);
		map.put(PROV_JSON_SPECIALIZATION, ProvStatement.SPECIALIZATION);
		map.put(PROV_JSON_MENTION, ProvStatement.MENTION);
		map.put(PROV_JSON_COLLECTION, ProvStatement.COLLECTION);
		map.put(PROV_JSON_MEMBERSHIP, ProvStatement.MEMBERSHIP);
		map.put(PROV_JSON_BUNDLE, ProvStatement.BUNDLE);
		provTypeMap = Collections.unmodifiableMap(map);
	}
	
	private final ProvFactory pf = new ProvFactory();

	@Override
	public Document deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject provJSONDoc = json.getAsJsonObject();
		
		// Initialise namespaces
		Hashtable<String, String> namespaces = new Hashtable<String, String>();
		JsonObject prefixes = getObjectAndRemove(provJSONDoc, PROV_JSON_PREFIX);
		if (prefixes != null) {
			for (Map.Entry<String, JsonElement> pair: prefixes.entrySet()) {
				namespaces.put(pair.getKey(), pair.getValue().getAsString());
			}
			pf.setNamespaces(namespaces);
		}
		
		// Decoding structures
		List<Statement> statements = new ArrayList<Statement>();
		for (Map.Entry<String, JsonElement> sPair: provJSONDoc.entrySet()) {
			String statementType = sPair.getKey();
			JsonObject statementMap = sPair.getValue().getAsJsonObject();
			statements.addAll(decodeElements(statementType, statementMap));
		}
		
		// Create the document
		Document doc = pf.newDocument();
		doc.setNss(namespaces);
		doc.getEntityOrActivityOrWasGeneratedBy().addAll(statements);
		return doc;
	}
	
	private List<Statement> decodeElements(String statementType, JsonObject statementMap) {
		List<Statement> statements = new ArrayList<Statement>();
		for (Map.Entry<String, JsonElement> pair: statementMap.entrySet()) {
			String idStr = pair.getKey();
			JsonObject attributeMap = pair.getValue().getAsJsonObject();
			Statement statement = decodeElement(statementType, idStr, attributeMap);
			statements.add(statement);
		}
		return statements;
	}
	
	private Statement decodeElement(String statementType, String idStr, JsonObject attributeMap) {
		Statement statement;
		QName id;
		if (idStr.startsWith("_:")) {
			id = null;
		}
		else {
			id = pf.stringToQName(idStr);
		}
		// Decoding attributes
		
		
		switch (provTypeMap.get(statementType)) {
		case ENTITY:
			statement = pf.newEntity(id);
			break;
		case ACTIVITY:
			XMLGregorianCalendar startTime = optionalTime("prov:startTime", attributeMap);
			XMLGregorianCalendar endTime = optionalTime("prov:endTime", attributeMap);
			Activity a = pf.newActivity(id);
			if (startTime != null) {
				a.setStartTime(startTime);
			}
			if (endTime != null) {
				a.setEndTime(endTime);
			}
			statement = a;
			break;
		case AGENT:
			statement = pf.newAgent(id);
			break;			
		case ALTERNATE:
			EntityRef alternate1 = entityRef("prov:alternate1", attributeMap);
			EntityRef alternate2 = entityRef("prov:alternate2", attributeMap);
			statement = pf.newAlternateOf(alternate2, alternate1);
			break;
		case ASSOCIATION:
			ActivityRef activity = activityRef("prov:activity", attributeMap);
			AgentRef agent = agentRef("prov:agent", attributeMap); 
			statement = pf.newWasAssociatedWith(id, activity, agent);
			break;
		case ATTRIBUTION:
			EntityRef entity = entityRef("prov:entity", attributeMap);
			agent = agentRef("prov:agent", attributeMap);
			statement = pf.newWasAttributedTo(id, entity, agent);
			break;
		// TODO: Implement bundle decoding
//		case BUNDLE:
//			break;
//		case COLLECTION:
//			break;
		case COMMUNICATION:
			ActivityRef informed = activityRef("prov:informed", attributeMap);
			ActivityRef informant = activityRef("prov:informant", attributeMap);
			statement = pf.newWasInformedBy(id, informed, informant);
			break;
		case DELEGATION:
			AgentRef delegate = agentRef("prov:delegate", attributeMap);
			AgentRef responsible = agentRef("prov:responsible", attributeMap);
			activity = optionalActivityRef("prov:activity", attributeMap);
			statement = pf.newActedOnBehalfOf(id, delegate, responsible, activity);
			break;
		case DERIVATION:
			EntityRef generatedEntity = entityRef("prov:generatedEntity", attributeMap);
			EntityRef usedEntity = entityRef("prov:usedEntity", attributeMap);
			activity = optionalActivityRef("prov:activity", attributeMap);
			GenerationRef generation = optionalGenerationRef("prov:generation", attributeMap);
			UsageRef usage = optionalUsageRef("prov:usage", attributeMap);
			statement = pf.newWasDerivedFrom(id, generatedEntity, usedEntity, activity, generation, usage);
			break;
		case END:
			activity = activityRef("prov:activity", attributeMap);
			EntityRef trigger = optionalEntityRef("prov:trigger", attributeMap);
			ActivityRef ender = optionalActivityRef("prov:ender", attributeMap);
			XMLGregorianCalendar time = optionalTime("prov:time", attributeMap);
			WasEndedBy wEB = pf.newWasEndedBy(id, activity, trigger);
			if (ender != null)
				wEB.setEnder(ender);
			if (time != null) {
				wEB.setTime(time);
			}
			statement = wEB;
			break;
		case GENERATION:
			entity = entityRef("prov:entity", attributeMap);
			activity = optionalActivityRef("prov:activity", attributeMap);
			time = optionalTime("prov:time", attributeMap);
			WasGeneratedBy wGB = pf.newWasGeneratedBy(id);
			wGB.setEntity(entity);
			if (activity != null)
				wGB.setActivity(activity);
			if (time != null) {
				wGB.setTime(time);
			}
			statement = wGB;
			break;
		case INVALIDATION:
			entity = entityRef("prov:entity", attributeMap);
			activity = optionalActivityRef("prov:activity", attributeMap);
			time = optionalTime("prov:time", attributeMap);
			WasInvalidatedBy wIB = pf.newWasInvalidatedBy(id, entity, activity);
			if (time != null) {
				wIB.setTime(time);
			}
			statement = wIB;
			break;
		case MEMBERSHIP:
			EntityRef collection = entityRef("prov:collection", attributeMap);
			entity = entityRef("prov:entity", attributeMap);
			statement = pf.newMembership(collection, entity);
			break;
		case MENTION:
			EntityRef specificEntity = entityRef("prov:specificEntity", attributeMap);
			EntityRef generalEntity = entityRef("prov:generalEntity", attributeMap);
			EntityRef bundle = entityRef("prov:bundle", attributeMap);
			statement = pf.newMentionOf(specificEntity, generalEntity, bundle);
			break;
		case SPECIALIZATION:
			specificEntity = entityRef("prov:specificEntity", attributeMap);
			generalEntity = entityRef("prov:generalEntity", attributeMap);
			statement = pf.newSpecializationOf(specificEntity, generalEntity);
			break;
		case START:
			activity = activityRef("prov:activity", attributeMap);
			trigger = optionalEntityRef("prov:trigger", attributeMap);
			ActivityRef starter = optionalActivityRef("prov:starter", attributeMap);
			time = optionalTime("prov:time", attributeMap);
			WasStartedBy wSB = pf.newWasStartedBy(id, activity, trigger);
			if (starter != null)
				wSB.setStarter(starter);
			if (time != null) {
				wSB.setTime(time);
			}
			statement = wSB;
			break;
		case USAGE:
			activity = activityRef("prov:activity", attributeMap);
			entity = optionalEntityRef("prov:entity", attributeMap);
			time = optionalTime("prov:time", attributeMap);
			Used wUB = pf.newUsed(id);
			wUB.setActivity(activity);
			if (entity != null)
				wUB.setEntity(entity);
			if (time != null) {
				wUB.setTime(time);
			}
			statement = wUB;
			break;
		default:
			statement = null;
			break;
		
		}
		// TODO: implement prov:type decoding
		// TODO: implement prov:label decoding
		// TODO: implement arbitrary attribute decoding
		
		return statement;
	}
	
	private String popString(JsonObject jo, String memberName) {
		return jo.remove(memberName).getAsString();
	}
	
	private EntityRef entityRef(String attributeName, JsonObject attributeMap) {
		return pf.newEntityRef(popString(attributeMap, attributeName));
	}
	
	private ActivityRef activityRef(String attributeName, JsonObject attributeMap) {
		return pf.newActivityRef(popString(attributeMap, attributeName));
	}
	
	private AgentRef agentRef(String attributeName, JsonObject attributeMap) {
		return pf.newAgentRef(popString(attributeMap, attributeName));
	}
	
	private EntityRef optionalEntityRef(String attributeName, JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return entityRef(attributeName, attributeMap);
		else
			return null;
	}
	
	private ActivityRef optionalActivityRef(String attributeName, JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return activityRef(attributeName, attributeMap);
		else
			return null;
	}
	
	private AgentRef optionalAgentRef(String attributeName, JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return agentRef(attributeName, attributeMap);
		else
			return null;
	}
	
	private GenerationRef optionalGenerationRef(String attributeName, JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return pf.newGenerationRef(popString(attributeMap, attributeName));
		else
			return null;
	}
	
	private UsageRef optionalUsageRef(String attributeName, JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return pf.newUsageRef(popString(attributeMap, attributeName));
		else
			return null;
	}
	
	private XMLGregorianCalendar optionalTime(String attributeName, JsonObject attributeMap) {
		if (attributeMap.has(attributeName))
			return pf.newISOTime(popString(attributeMap, attributeName));
		else
			return null;
	}
	
	private JsonObject getObjectAndRemove(JsonObject jsonObject, String memberName) {
		if (jsonObject.has(memberName)) {
			JsonObject result = jsonObject.getAsJsonObject(memberName);
			jsonObject.remove(memberName);
			return result;
		}
		else return null;
	}
	
}
