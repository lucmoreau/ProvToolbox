/**
 * 
 */
package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.ActivityRef;
import org.openprovenance.prov.xml.AgentRef;
import org.openprovenance.prov.xml.AnyRef;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.DerivedByInsertionFrom;
import org.openprovenance.prov.xml.DerivedByRemovalFrom;
import org.openprovenance.prov.xml.DictionaryMembership;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.EntityRef;
import org.openprovenance.prov.xml.GenerationRef;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.HasLabel;
import org.openprovenance.prov.xml.HasLocation;
import org.openprovenance.prov.xml.HasRole;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.HasValue;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.KeyQNamePair;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Statement;
import org.openprovenance.prov.xml.StatementOrBundle;
import org.openprovenance.prov.xml.UsageRef;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.ValueConverter;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInvalidatedBy;
import org.openprovenance.prov.xml.WasStartedBy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


/**
 * A Gson deserializer adapter for PROV-JSON
 * Decode a PROV-JSON structure and produces a {@link Document}
 * @author Trung Dong Huynh <trungdong@donggiang.com>
 *
 */
public class ProvDocumentDeserializer implements JsonDeserializer<Document> {
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

	private static final String PROV_JSON_PREFIX =              "prefix";
    
    private final ProvFactory pf = new ProvFactory();
    private final ValueConverter vconv=new ValueConverter(pf);
    
    @Override
    public Document deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject provJSONDoc = json.getAsJsonObject();
        
        // Initialise namespaces
        Hashtable<String, String> namespaces = decodePrefixes(provJSONDoc);
        if (namespaces != null)
            pf.setNamespaces(namespaces);
        
        // Decoding structures
        List<StatementOrBundle> statements = decodeBundle(provJSONDoc);
        
        // Create the document
        Document doc = pf.newDocument();
        doc.setNss(namespaces);
        doc.getEntityAndActivityAndWasGeneratedBy().addAll(statements);
        return doc;
    }
    
    private Hashtable<String, String> decodePrefixes(JsonObject bundleStructure) {
    	Hashtable<String, String> namespaces = new Hashtable<String, String>();
        JsonObject prefixes = getObjectAndRemove(bundleStructure, PROV_JSON_PREFIX);
        if (prefixes != null) {
            for (Map.Entry<String, JsonElement> pair: prefixes.entrySet())
                namespaces.put(pair.getKey(), pair.getValue().getAsString());
            return namespaces;
        }
        else
        	return null;
    }
    
    private List<StatementOrBundle> decodeBundle(JsonObject bundleStructure) {
        List<StatementOrBundle> statements = new ArrayList<StatementOrBundle>();
        for (Map.Entry<String, JsonElement> sPair: bundleStructure.entrySet()) {
            String statementType = sPair.getKey();
            JsonObject statementMap = sPair.getValue().getAsJsonObject();
            statements.addAll(decodeElements(statementType, statementMap));
        }
        return statements;
    }
    
    private List<StatementOrBundle> decodeElements(String statementType, JsonObject statementMap) {
        List<StatementOrBundle> statements = new ArrayList<StatementOrBundle>();
        for (Map.Entry<String, JsonElement> pair: statementMap.entrySet()) {
            String idStr = pair.getKey();
            if (pair.getValue().isJsonArray()) {
                // Multiple elements having the same identifier
                JsonArray elements = pair.getValue().getAsJsonArray();
                Iterator<JsonElement> iter = elements.iterator();
                while (iter.hasNext()) {
                    JsonObject attributeMap = iter.next().getAsJsonObject();
                    StatementOrBundle statement = decodeStatement(statementType, idStr, attributeMap);
                    statements.add(statement);
                }
            }
            else {
                JsonObject attributeMap = pair.getValue().getAsJsonObject();
                StatementOrBundle statement = decodeStatement(statementType, idStr, attributeMap);
                statements.add(statement);
            }
        }
        return statements;
    }
    
    @SuppressWarnings("unchecked")
	private StatementOrBundle decodeStatement(String statementType, String idStr, JsonObject attributeMap) {
        StatementOrBundle statement;
        QName id;
        if (idStr.startsWith("_:")) {
            // Ignore all blank node IDs
            id = null;
        }
        else {
            id = pf.stringToQName(idStr);
        }

        // Decoding attributes
        ProvJSONStatement provStatement = ProvJSONStatement.valueOf(statementType);
        switch (provStatement) {
        case entity:
            statement = pf.newEntity(id);
            break;
        case activity:
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
        case agent:
            statement = pf.newAgent(id);
            break;            
        case alternateOf:
            EntityRef alternate1 = optionalEntityRef("prov:alternate1", attributeMap);
            EntityRef alternate2 = optionalEntityRef("prov:alternate2", attributeMap);
            statement = pf.newAlternateOf(alternate2, alternate1);
            break;
        case wasAssociatedWith:
            ActivityRef activity = optionalActivityRef("prov:activity", attributeMap);
            AgentRef agent = optionalAgentRef("prov:agent", attributeMap); 
            EntityRef plan = optionalEntityRef("prov:plan", attributeMap);
            WasAssociatedWith wAW = pf.newWasAssociatedWith(id, activity, agent);
            if (plan != null) wAW.setPlan(plan);
            statement = wAW;
            break;
        case wasAttributedTo:
            EntityRef entity = optionalEntityRef("prov:entity", attributeMap);
            agent = optionalAgentRef("prov:agent", attributeMap);
            statement = pf.newWasAttributedTo(id, entity, agent);
            break;
        case bundle:
        	Hashtable<String, String> localNamespaces = decodePrefixes(attributeMap);
        	// TODO: Use the local namespace while preserving the top-level namespace
        	@SuppressWarnings("rawtypes")
            Collection statements = decodeBundle(attributeMap);
            NamedBundle namedBundle = new NamedBundle(); 
            namedBundle.setId(id);
            namedBundle.getEntityAndActivityAndWasGeneratedBy().addAll((Collection<? extends Statement>)statements);
            statement = namedBundle;
            break;
        case wasInformedBy:
            ActivityRef informed = optionalActivityRef("prov:informed", attributeMap);
            ActivityRef informant = optionalActivityRef("prov:informant", attributeMap);
            statement = pf.newWasInformedBy(id, informed, informant);
            break;
        case actedOnBehalfOf:
            AgentRef delegate = optionalAgentRef("prov:delegate", attributeMap);
            AgentRef responsible = optionalAgentRef("prov:responsible", attributeMap);
            activity = optionalActivityRef("prov:activity", attributeMap);
            statement = pf.newActedOnBehalfOf(id, delegate, responsible, activity);
            break;
        case wasDerivedFrom:
            EntityRef generatedEntity = optionalEntityRef("prov:generatedEntity", attributeMap);
            EntityRef usedEntity = optionalEntityRef("prov:usedEntity", attributeMap);
            activity = optionalActivityRef("prov:activity", attributeMap);
            GenerationRef generation = optionalGenerationRef("prov:generation", attributeMap);
            UsageRef usage = optionalUsageRef("prov:usage", attributeMap);
            statement = pf.newWasDerivedFrom(id, generatedEntity, usedEntity, activity, generation, usage);
            break;
        case wasEndedBy:
            activity = optionalActivityRef("prov:activity", attributeMap);
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
        case wasGeneratedBy:
            entity = optionalEntityRef("prov:entity", attributeMap);
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
        case wasInfluencedBy:
            AnyRef influencee = anyRef("prov:influencee", attributeMap);
            AnyRef influencer = anyRef("prov:influencer", attributeMap);
            statement = pf.newWasInfluencedBy(id, influencee, influencer);
            break;
        case wasInvalidatedBy:
            entity = optionalEntityRef("prov:entity", attributeMap);
            activity = optionalActivityRef("prov:activity", attributeMap);
            time = optionalTime("prov:time", attributeMap);
            WasInvalidatedBy wIB = pf.newWasInvalidatedBy(id, entity, activity);
            if (time != null) {
                wIB.setTime(time);
            }
            statement = wIB;
            break;
        case hadMember:
            EntityRef collection = optionalEntityRef("prov:collection", attributeMap);
            Collection<EntityRef> entities = optionalEntityRefs("prov:entity", attributeMap);
            HadMember membership = new HadMember();
            membership.setCollection(collection);
            if (entities != null)
            	membership.getEntity().addAll(entities);
            statement = membership;
            break;
        case mentionOf:
            EntityRef specificEntity = optionalEntityRef("prov:specificEntity", attributeMap);
            EntityRef generalEntity = optionalEntityRef("prov:generalEntity", attributeMap);
            EntityRef bundle = optionalEntityRef("prov:bundle", attributeMap);
            statement = pf.newMentionOf(specificEntity, generalEntity, bundle);
            break;
        case specializationOf:
            specificEntity = optionalEntityRef("prov:specificEntity", attributeMap);
            generalEntity = optionalEntityRef("prov:generalEntity", attributeMap);
            statement = pf.newSpecializationOf(specificEntity, generalEntity);
            break;
        case wasStartedBy:
            activity = optionalActivityRef("prov:activity", attributeMap);
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
        case used:
            activity = optionalActivityRef("prov:activity", attributeMap);
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
        case derivedByInsertionFrom:
        	QName before = optionalQName("prov:before", attributeMap);
        	QName after = optionalQName("prov:after", attributeMap);
        	List<KeyQNamePair> keyEntitySet = optionalKeyEntitySet("prov:key-entity-set", attributeMap);
        	DerivedByInsertionFrom dBIF = pf.newDerivedByInsertionFrom(id, after, before, keyEntitySet, null);
        	statement = dBIF;
        	break;
        case derivedByRemovalFrom:
        	before = optionalQName("prov:before", attributeMap);
        	after = optionalQName("prov:after", attributeMap);
        	List<Object> keys = optionalKeySet("prov:key-set", attributeMap);
        	DerivedByRemovalFrom dBRF = pf.newDerivedByRemovalFrom(id, after, before, keys, null);
        	statement = dBRF;
        	break;
        case hadDictionaryMember:
        	QName dictionary = optionalQName("prov:dictionary", attributeMap);
        	keyEntitySet = optionalKeyEntitySet("prov:key-entity-set", attributeMap);
        	DictionaryMembership hDM = pf.newDictionaryMembership(dictionary, keyEntitySet);
        	statement = hDM;
        	break;
        default:
            statement = null;
            break;
        
        }
        
        // All the remaining attributes can have multiple values, except prov:value
        // prov:type decoding
        List<JsonElement> values = popMultiValAttribute("prov:type", attributeMap);
        if (!values.isEmpty()) {
        	if (statement instanceof HasType) {
	            List<Object> types = ((HasType)statement).getType();
	            for (JsonElement value: values) {
	                types.add(decodeTypeRef(value));
	            }
        	}
        	else {
        		throw new UnsupportedOperationException("prov:type is not allowed in a " + statementType + "statement - id: " + idStr);
        	}
        }
        // prov:label decoding
        values = popMultiValAttribute("prov:label", attributeMap);
        if (!values.isEmpty()) {
        	if (statement instanceof HasLabel) {
	            List<InternationalizedString> labels = ((HasLabel)statement).getLabel();
	            for (JsonElement value: values) {
	                labels.add(decodeInternationalizedString(value));
	            }
        	}
        	else {
        		throw new UnsupportedOperationException("prov:label is not allowed in a " + statementType + "statement - id: " + idStr);
        	}
        }
        // prov:location decoding
        values = popMultiValAttribute("prov:location", attributeMap);
        if (!values.isEmpty()) {
        	if (statement instanceof HasLocation) {
	            List<Object> locations = ((HasLocation)statement).getLocation();
	            for (JsonElement value: values) {
	                locations.add(decodeAttributeValue(value));
	            }
        	}
        	else {
        		throw new UnsupportedOperationException("prov:location is not allowed in a " + statementType + "statement - id: " + idStr);
        	}
        }
        // prov:role decoding
        values = popMultiValAttribute("prov:role", attributeMap);
        if (!values.isEmpty()) {
        	if (statement instanceof HasRole) {
	            List<Object> roles = ((HasRole)statement).getRole();
	            for (JsonElement value: values) {
	                roles.add(decodeAttributeValue(value));
	            }
        	}
        	else {
        		throw new UnsupportedOperationException("prov:role is not allowed in a " + statementType + "statement - id: " + idStr);
        	}
        }
        // prov:value decoding
        values = popMultiValAttribute("prov:value", attributeMap);
        if (!values.isEmpty()) {
        	if (values.size() > 1) {
        		// only one instance allowed
        		throw new UnsupportedOperationException("Only one instance of prov:value is allowed in a statement - id: " + idStr);
        	}
        	if (statement instanceof HasValue) {
        		((HasValue)statement).setValue(decodeAttributeValue(values.get(0)));
        	}
        	else {
        		throw new UnsupportedOperationException("prov:value is not allowed in a " + statementType + "statement - id: " + idStr);
        	}
        }
        
        // arbitrary attribute decoding
        if (provStatement != ProvJSONStatement.bundle && !attributeMap.entrySet().isEmpty()) {
        	if (statement instanceof HasExtensibility) {
	            List<Attribute> attributes = ((HasExtensibility)statement).getAny();
	            for (Map.Entry<String, JsonElement> aPair: attributeMap.entrySet()) {
	                QName attributeName = pf.stringToQName(aPair.getKey());
	                JsonElement element = aPair.getValue();
	                values = pickMultiValues(element);
	                for (JsonElement value: values) {
	                    Attribute attr = decodeAttribute(attributeName, value);
	                    attributes.add(attr);
	                }
	            }
        	}
        	else {
        		throw new UnsupportedOperationException("Arbitrary attributes are not allowed in a " + statementType + "statement - id: " + idStr);
        	}
        }
        
        return statement;
    }
    
    private Object decodeTypeRef(JsonElement element) {
    	return decodeAttributeValue(element);
    }
    
    private InternationalizedString decodeInternationalizedString(JsonElement element) {
        InternationalizedString iString = new InternationalizedString();
        if (element.isJsonPrimitive()) {
            iString.setValue(element.getAsString());
        }
        else {
            JsonObject struct = element.getAsJsonObject(); 
            String value = struct.get("$").getAsString();
            iString.setValue(value);
            if (struct.has("lang")) {
                String lang = struct.get("lang").getAsString();
                iString.setLang(lang);
            }
        }
        return iString;
    }
    
    private Attribute decodeAttribute(QName attributeName, JsonElement element) {
    	Object value;
    	QName xsdType;
    	
    	if (element.isJsonPrimitive()) {
            value = decodeJSONPrimitive(element.getAsString());
            xsdType = vconv.getXsdType(value);
        } else {
            JsonObject struct = element.getAsJsonObject();
            if (struct.has("lang")) {
                // Internationalized strings
                String lang = struct.get("lang").getAsString();
                InternationalizedString iString = new InternationalizedString();
                iString.setValue(struct.get("$").getAsString());
                iString.setLang(lang);
                value = iString;
                xsdType = ValueConverter.QNAME_XSD_STRING;
            } else {
	            // The following implicitly assume the type is one of XSD types
	            String datatypeAsString = struct.get("type").getAsString();
	            xsdType = pf.stringToQName(datatypeAsString);
	            value = vconv.convertToJava(xsdType, struct.get("$").getAsString());
            }
        }    	
    	return new Attribute(attributeName, value, xsdType);
    }
    
    private Object decodeAttributeValue(JsonElement element) {
        if (element.isJsonPrimitive()) {
            return decodeJSONPrimitive(element.getAsString());
        } else {
            JsonObject struct = element.getAsJsonObject(); 
            String value = struct.get("$").getAsString();
            if (struct.has("lang")) {
                // Internationalized strings
                String lang = struct.get("lang").getAsString();
                InternationalizedString iString = new InternationalizedString();
                iString.setValue(value);
                iString.setLang(lang);
                return iString;
            } else if (struct.has("type")) {
            	String datatypeAsString = struct.get("type").getAsString();
	            QName xsdType = pf.stringToQName(datatypeAsString);
	            return vconv.convertToJava(xsdType, struct.get("$").getAsString());
            } else {
            	// if no type or lang information, decode as an Internationalized string without lang
            	InternationalizedString iString = new InternationalizedString();
                iString.setValue(value);
                return iString;
//            	return decodeJSONPrimitive(value);
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
        }
        catch (NumberFormatException ex1) {
            try {
                // Try double next
                return Double.parseDouble(value);
            }
            catch (NumberFormatException ex2) {
                // Not a number, return the string
                return value;
            }
        }
    }
    
    private List<JsonElement> popMultiValAttribute(String attributeName, JsonObject attributeMap) {
        if (attributeMap.has(attributeName)) {
            JsonElement element = attributeMap.remove(attributeName);
            return pickMultiValues(element);
        }
        else
            return Collections.emptyList();
    }
    
    private List<JsonElement> pickMultiValues(JsonElement element) {
        if (element.isJsonArray()) {
            // Picking multiple values
            JsonArray array = element.getAsJsonArray();
            List<JsonElement> elements = new ArrayList<JsonElement>(array.size());
            Iterator<JsonElement> iter = array.iterator();
            while (iter.hasNext()) {
                elements.add(iter.next());
            }
            return elements;
        }
        else {
            // Return a single item list
            return Collections.singletonList(element);
            }
    } 
    
    private String popString(JsonObject jo, String memberName) {
        return jo.remove(memberName).getAsString();
    }

    
    private QName qName(String attributeName, JsonObject attributeMap) {
    	return pf.stringToQName(popString(attributeMap, attributeName));
    }
    
    private QName optionalQName(String attributeName, JsonObject attributeMap) {
    	if (attributeMap.has(attributeName))
            return qName(attributeName, attributeMap);
        else
            return null;
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
    
    private AnyRef anyRef(String attributeName, JsonObject attributeMap) {
    	if (attributeMap.has(attributeName)) 
    		return pf.newAnyRef(popString(attributeMap, attributeName));
    	else return null;
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
    
    private Collection<EntityRef> entityRefs(String attributeName, JsonObject attributeMap) {
        List<EntityRef> results = new ArrayList<EntityRef>();
        List<JsonElement> elements = popMultiValAttribute(attributeName, attributeMap);
        for (JsonElement element : elements) {
        	results.add(pf.newEntityRef(element.getAsString()));
        }
        return results;
    }
    
    private Collection<EntityRef> optionalEntityRefs(String attributeName, JsonObject attributeMap) {
        if (attributeMap.has(attributeName))
            return entityRefs(attributeName, attributeMap);
        else
            return null;
    }
    
    private List<KeyQNamePair> keyEntitySet(String attributeName, JsonObject attributeMap) {
    	List<KeyQNamePair> results = new ArrayList<KeyQNamePair>();
    	List<JsonElement> elements = popMultiValAttribute(attributeName, attributeMap);
        for (JsonElement element : elements) {
        	JsonObject item = element.getAsJsonObject();
        	KeyQNamePair pair = new KeyQNamePair();
        	pair.key = this.decodeAttributeValue(item.remove("key"));
        	pair.name = pf.stringToQName(this.popString(item, "$"));
        	results.add(pair);
        }
        return results;
    }
    
    private List<KeyQNamePair> optionalKeyEntitySet(String attributeName, JsonObject attributeMap) {
    	if (attributeMap.has(attributeName))
            return keyEntitySet(attributeName, attributeMap);
        else
            return null;
    }
    
    private List<Object> keySet(String attributeName, JsonObject attributeMap) {
    	List<Object> results = new ArrayList<Object>();
    	List<JsonElement> elements = popMultiValAttribute(attributeName, attributeMap);
        for (JsonElement element : elements) {
        	Object key = decodeAttributeValue(element);
        	results.add(key);
        }
        return results;
    }
    
    private List<Object> optionalKeySet(String attributeName, JsonObject attributeMap) {
    	if (attributeMap.has(attributeName))
            return keySet(attributeName, attributeMap);
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
