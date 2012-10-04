/**
 * 
 */
package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.ActivityRef;
import org.openprovenance.prov.xml.AgentRef;
import org.openprovenance.prov.xml.AnyRef;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.EntityRef;
import org.openprovenance.prov.xml.GenerationRef;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.HasLabel;
import org.openprovenance.prov.xml.HasLocation;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.HasValue;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Statement;
import org.openprovenance.prov.xml.StatementOrBundle;
import org.openprovenance.prov.xml.URIWrapper;
import org.openprovenance.prov.xml.UsageRef;
import org.openprovenance.prov.xml.Used;
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
	
	enum ProvStatement {
	    ENTITY,
	    ACTIVITY,
	    GENERATION,
	    USAGE,
	    COMMUNICATION,
	    START,
	    END,
	    INVALIDATION,
	    AGENT,
	    ATTRIBUTION,
	    ASSOCIATION,
	    DERIVATION,
	    DELEGATION,
	    INFLUENCE,
	    ALTERNATE,
	    SPECIALIZATION,
	    MENTION,
	    MEMBERSHIP,
	    BUNDLE
	}

	
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
    private static final String PROV_JSON_INFLUENCE =           "wasInfluencedBy";
    private static final String PROV_JSON_ALTERNATE =           "alternateOf";
    private static final String PROV_JSON_SPECIALIZATION =      "specializationOf";
    private static final String PROV_JSON_MENTION =             "mentionOf";
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
        map.put(PROV_JSON_INFLUENCE, ProvStatement.INFLUENCE);
        map.put(PROV_JSON_ALTERNATE, ProvStatement.ALTERNATE);
        map.put(PROV_JSON_SPECIALIZATION, ProvStatement.SPECIALIZATION);
        map.put(PROV_JSON_MENTION, ProvStatement.MENTION);
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
        List<StatementOrBundle> statements = decodeBundle(provJSONDoc);
        
        // Create the document
        Document doc = pf.newDocument();
        doc.setNss(namespaces);
        doc.getEntityOrActivityOrWasGeneratedBy().addAll(statements);
        return doc;
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
        
        ProvStatement provStatement = provTypeMap.get(statementType);
        if (provStatement == null) {
            throw new UnsupportedOperationException("Unsupported statement type: " + statementType);
        }
            
        switch (provStatement) {
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
            AgentRef agent = optionalAgentRef("prov:agent", attributeMap); 
            statement = pf.newWasAssociatedWith(id, activity, agent);
            break;
        case ATTRIBUTION:
            EntityRef entity = entityRef("prov:entity", attributeMap);
            agent = agentRef("prov:agent", attributeMap);
            statement = pf.newWasAttributedTo(id, entity, agent);
            break;
        case BUNDLE:
            List<StatementOrBundle> statements = decodeBundle(attributeMap);
            NamedBundle namedBundle = new NamedBundle(); 
            namedBundle.setId(id);
            namedBundle.getEntityOrActivityOrWasGeneratedBy().addAll((Collection<? extends Statement>)statements);
            statement = namedBundle;
            break;
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
            switch (provStatement) {
            default:
            case DERIVATION:
                statement = pf.newWasDerivedFrom(id, generatedEntity, usedEntity, activity, generation, usage);
                break;
            }
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
        case INFLUENCE:
            AnyRef influencee = anyRef("prov:influencee", attributeMap);
            AnyRef influencer = anyRef("prov:influencer", attributeMap);
            statement = pf.newWasInfluencedBy(id, influencee, influencer);
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
        // All the remaining attributes can have multiple values
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
        // prov:value decoding
        values = popMultiValAttribute("prov:value", attributeMap);
        if (!values.isEmpty()) {
        	if (values.size() > 1) {
        		// only one instance allowed
        		throw new UnsupportedOperationException("Only one instance of prov:value is allowed in a statement - id: " + idStr);
        	}
        	if (statement instanceof HasValue) {
        		// TODO set the value of the statement
        	}
        	else {
        		throw new UnsupportedOperationException("prov:value is not allowed in a " + statementType + "statement - id: " + idStr);
        	}
        }
        
        // arbitrary attribute decoding
        if (provStatement != ProvStatement.BUNDLE && !attributeMap.entrySet().isEmpty()) {
        	if (statement instanceof HasExtensibility) {
	            List<Object> attributes = ((HasExtensibility)statement).getAny();
	            for (Map.Entry<String, JsonElement> aPair: attributeMap.entrySet()) {
	                QName attributeName = pf.stringToQName(aPair.getKey());
	                JsonElement element = aPair.getValue();
	                values = pickMultiValues(element);
	                for (JsonElement value: values) {
	                    Object attributeValue = decodeAttributeValue(value);
	                    attributes.add(new JAXBElement<Object>(attributeName, Object.class, null, attributeValue));
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
        if (element.isJsonPrimitive()) {
            return decodeQNameOrURI(element.getAsString());
        }
        else {
            JsonObject struct = element.getAsJsonObject(); 
            String value = struct.get("$").getAsString();
            String datatype = struct.get("type").getAsString();
            // TODO: Should be accepting only xsd:anyURI or xsd:QName
            return decodeXSDType(value, datatype);
        }
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
            }
            String datatype = struct.get("type").getAsString();
            // TODO: decode typed literals that are not of XSD types
            return decodeXSDType(value, datatype);
        }
    }
    
    private Object decodeQNameOrURI(String ref) {
        QName qname = pf.stringToQName(ref);
        if (qname.getNamespaceURI() == XMLConstants.NULL_NS_URI) {
            // No valid namespace, expect the ref as an URI
            URI uri = URI.create(ref);
            URIWrapper u = new URIWrapper();
            u.setValue(uri);
            return u;
        }
        return qname;
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
    
    private Object decodeXSDType(String value, String datatype) {
        
        if (datatype.equals("xsd:string"))  return value;
        if (datatype.equals("xsd:int"))     return DatatypeConverter.parseInt(value);
        if (datatype.equals("xsd:long"))    return DatatypeConverter.parseLong(value);
        if (datatype.equals("xsd:short"))   return DatatypeConverter.parseShort(value);
        if (datatype.equals("xsd:double"))  return DatatypeConverter.parseDouble(value);
        if (datatype.equals("xsd:float"))   return DatatypeConverter.parseFloat(value);
        if (datatype.equals("xsd:decimal")) return DatatypeConverter.parseDecimal(value);
        if (datatype.equals("xsd:boolean")) return DatatypeConverter.parseBoolean(value);
        if (datatype.equals("xsd:byte"))    return DatatypeConverter.parseByte(value);
        if (datatype.equals("xsd:unsignedInt"))   return DatatypeConverter.parseUnsignedInt(value);
        if (datatype.equals("xsd:unsignedShort")) return DatatypeConverter.parseUnsignedShort(value);
        if (datatype.equals("xsd:unsignedByte"))  return DatatypeConverter.parseUnsignedInt(value);
        if (datatype.equals("xsd:unsignedLong"))  return DatatypeConverter.parseUnsignedInt(value);
        if (datatype.equals("xsd:integer"))             return DatatypeConverter.parseInteger(value);
        if (datatype.equals("xsd:nonNegativeInteger"))  return DatatypeConverter.parseInteger(value);
        if (datatype.equals("xsd:nonPositiveInteger"))  return DatatypeConverter.parseInteger(value);
        if (datatype.equals("xsd:positiveInteger"))     return DatatypeConverter.parseInteger(value);
        if (datatype.equals("xsd:dateTime"))            return DatatypeConverter.parseDateTime(value);

        if (datatype.equals("xsd:anyURI")) {
            URIWrapper u=new URIWrapper();
            u.setValue(URI.create(value));
            return u;
        }
        if (datatype.equals("xsd:QName")) {
            return pf.stringToQName(value);
        }



        if ((datatype.equals("rdf:XMLLiteral"))
            || (datatype.equals("xsd:normalizedString"))
            || (datatype.equals("xsd:token"))
            || (datatype.equals("xsd:language"))
            || (datatype.equals("xsd:Name"))
            || (datatype.equals("xsd:NCName"))
            || (datatype.equals("xsd:NMTOKEN"))
            || (datatype.equals("xsd:hexBinary"))
            || (datatype.equals("xsd:base64Binary"))) {

            throw new UnsupportedOperationException("KNOWN literal type but conversion not supported yet " + datatype);
        }
        throw new UnsupportedOperationException("UNKNOWN literal type " + datatype);
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
        return pf.newAnyRef(popString(attributeMap, attributeName));
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


class NamespaceContextMap implements NamespaceContext {
    private String defaultNamespaceURI = null;
    private Map<String, String> namespaces = new Hashtable<String, String>();
    
    public NamespaceContextMap(Map<String, String> namespaces) {
        this.namespaces.putAll(namespaces);
    }
    
    public NamespaceContextMap(Map<String, String> namespaces, String defaultNamespaceURI) {
        this.namespaces.putAll(namespaces);
        this.defaultNamespaceURI = defaultNamespaceURI;
    }
    
    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        if (prefix == XMLConstants.DEFAULT_NS_PREFIX && defaultNamespaceURI != null) return defaultNamespaceURI; 
        if (prefix == XMLConstants.XML_NS_PREFIX) return XMLConstants.XML_NS_URI;
        if (prefix == XMLConstants.XMLNS_ATTRIBUTE) return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        if (namespaces.containsKey(prefix)) return namespaces.get(prefix);
        
        return XMLConstants.NULL_NS_URI;
    }

    @Override
    public String getPrefix(String namespaceURI) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator getPrefixes(String namespaceURI) {
        // TODO Auto-generated method stub
        return null;
    }
    
}