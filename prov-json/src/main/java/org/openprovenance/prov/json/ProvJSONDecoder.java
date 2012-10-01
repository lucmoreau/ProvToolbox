/**
 * 
 */
package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Statement;

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
	DELEGATION,
	ALTERNATE,
	SPECIALIZATION,
	MENTION,
	COLLECTION,
	MEMBERSHIP,
	BUNDLE
}

class ProvDocumentDeserializer implements JsonDeserializer<Document> {
	private final String PROV_JSON_PREFIX =              "prefix";
	private final String PROV_JSON_ENTITY =              "entity";
	private final String PROV_JSON_ACTIVITY =            "activity";
	private final String PROV_JSON_GENERATION =          "wasGeneratedBy";
	private final String PROV_JSON_USAGE =               "used";
	private final String PROV_JSON_COMMUNICATION =       "wasInformedBy";
	private final String PROV_JSON_START =               "wasStartedBy";
	private final String PROV_JSON_END =                 "wasEndedBy";
	private final String PROV_JSON_INVALIDATION =        "wasInvalidatedBy";
	private final String PROV_JSON_DERIVATION =          "wasDerivedFrom";
	private final String PROV_JSON_AGENT =               "agent";
	private final String PROV_JSON_ATTRIBUTION =         "wasAttributedTo";
	private final String PROV_JSON_ASSOCIATION =         "wasAssociatedWith";
	private final String PROV_JSON_DELEGATION =          "actedOnBehalfOf";
	private final String PROV_JSON_ALTERNATE =           "alternateOf";
	private final String PROV_JSON_SPECIALIZATION =      "specializationOf";
	private final String PROV_JSON_MENTION =             "mentionOf";
	private final String PROV_JSON_COLLECTION =          "collection";
	private final String PROV_JSON_MEMBERSHIP =          "memberOf";
	private final String PROV_JSON_BUNDLE =              "bundle";

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
		}
		ProvFactory pf = new ProvFactory(namespaces);
		
		
		// Decoding structures
		List<Statement> statements = new ArrayList<Statement>();
		for (Map.Entry<String, JsonElement> sPair: provJSONDoc.entrySet()) {
			String statementType = sPair.getKey();
			JsonObject statementMap = sPair.getValue().getAsJsonObject();
			statements.addAll(decodeElements(statementType, statementMap));
		}
		
		// Entities
		JsonObject eElements = getObjectAndRemove(provJSONDoc, PROV_JSON_ENTITY);
		if (eElements != null) {
			for (Map.Entry<String, JsonElement> pair: eElements.entrySet()) {
				
			}
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
		// TODO: decode the elements based on its type 
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
