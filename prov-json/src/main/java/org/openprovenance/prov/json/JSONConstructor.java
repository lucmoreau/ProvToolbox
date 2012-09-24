package org.openprovenance.prov.json;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.CommonTree;
import org.openprovenance.prov.notation.TreeConstructor;
import org.openprovenance.prov.notation.TreeTraversal;
import org.openprovenance.prov.notation.Utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** Conversion back to PROV-JSON. */

@SuppressWarnings("unchecked")
class ProvRecord {
	String type;
	Object id;
	Object attributes;
	
	public ProvRecord(String type, Object id, Object attributes) {
		this.type = type;
		this.id = id;
		this.attributes = attributes;
	}
	
	public String toString() {
		String result = type + "(" + id;
		List<Object[]> attrs = (List<Object[]>)attributes;
		for (Object[] tuple: attrs) {
			result += ", " + tuple[0] + "=" + tuple[1];
		}
		result += ")";
		return result;
	}
}

@SuppressWarnings("unchecked")
class JSONConstructor implements TreeConstructor {
	public static void main(String[] args)  {
        try {
            Utility u=new Utility();

            CommonTree tree = u.convertASNToTree(args[0]);

            Object provStructure = new TreeTraversal(new JSONConstructor()).convert(tree);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(provStructure);
            System.out.println(json);
            
            RhinoValidator validator = new RhinoValidator();
			List<String> errors = validator.validate(json);
			if (errors.size() > 0)
			{
				System.err.println(errors.size() + " validation error(s):");
				for (String error : errors)
				{
					System.err.println(error);
				}
			}
        } catch(Throwable t) {
            System.out.println("exception: "+t);
            t.printStackTrace();
        } 
	}

	private static final Map<String,Integer> countMap = new HashMap<String, Integer>();
	private static String getBlankID(String type) {
		if (!countMap.containsKey(type)) {
			countMap.put(type, 0);
		}
		int count = countMap.get(type);
		count += 1;
		countMap.put(type, count);
		return "_:" + type + count;
	}
	
	private static String unwrap (String s) {
		if (s == null)
			return null;
		int len = s.length();
		if ((s.charAt(0) == '\"' && s.charAt(len-1) == '\"') ||
				(s.charAt(0) == '<' && s.charAt(len-1) == '>'))
			return s.substring(1,len-1);
		else return s;
    }
	
	private Object[] tuple(Object o1, Object o2) {
		Object[] tuple = {o1, o2};
        return tuple;
	}
	
	private Map<String, String> typedLiteral(String value, String datatype, String lang) {
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
	
	private List<Object[]> addAttribute(Object attrs, Object[] attr) {
		List<Object []> result;
		Object key = attr[0];
		Object value = attr[1];
		if (attrs != null) {
			result = (List<Object[]>)attrs;
		}
		else {
			result = new ArrayList<Object[]>();
		}
		result.add(attr);
		return result;
	}
	
	/* Component 1 */
    public Object convertEntity(Object id, Object attrs) {
		return new ProvRecord("entity", id, attrs);
	}
	
    public Object convertActivity(Object id, Object startTime, Object endTime, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	if (startTime != null) {
    		attrs.add(tuple("prov:startTime", startTime));
    	}
    	if (endTime != null) {
    		attrs.add(tuple("prov:endTime", endTime));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	return new ProvRecord("activity", id, attrs);
	}
    
	public Object convertWasGeneratedBy(Object id, Object id2, Object id1,
			Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:entity", id2));
    	if (id1 != null) {
    		attrs.add(tuple("prov:activity", id1));
    	}
    	if (time != null) {
    		attrs.add(tuple("prov:time", time));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("wGB");

    	return new ProvRecord("wasGeneratedBy", id, attrs);
	}

    public Object convertUsed(Object id, Object id2, Object id1, Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:activity", id2));
    	if (id1 != null)
    		attrs.add(tuple("prov:entity", id1));
    	if (time != null) {
    		attrs.add(tuple("prov:time", time));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("u");
    	
    	return new ProvRecord("used", id , attrs);
	}

	public Object convertWasInformedBy(Object id, Object id2, Object id1, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:informed", id2));
    	attrs.add(tuple("prov:informant", id1));
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("wIB");

    	return new ProvRecord("wasInformedBy", id, attrs);
    }

	public Object convertWasStartedBy(Object id, Object id2, Object id1, Object id3,
			Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:activity", id2));
    	if (id1 != null) {
    		attrs.add(tuple("prov:trigger", id1));
    	}
    	if (id3 != null) {
    		attrs.add(tuple("prov:starter", id3));
    	}
    	if (time != null) {
    		attrs.add(tuple("prov:time", time));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("wSB");

    	return new ProvRecord("wasStartedBy", id, attrs);
    }

	public Object convertWasEndedBy(Object id, Object id2, Object id1, Object id3,
			Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:activity", id2));
    	if (id1 != null) {
    		attrs.add(tuple("prov:trigger", id1));
    	}
    	if (id3 != null) {
    		attrs.add(tuple("prov:ender", id3));
    	}
    	if (time != null) {
    		attrs.add(tuple("prov:time", time));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("wEB");

    	return new ProvRecord("wasEndedBy", id, attrs);
    }

	public Object convertWasInvalidatedBy(Object id, Object id2, Object id1,
			Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:entity", id2));
    	if (id1 != null) {
    		attrs.add(tuple("prov:activity", id1));
    	}
    	if (time != null) {
    		attrs.add(tuple("prov:time", time));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("wINVB");

    	return new ProvRecord("wasInvalidatedBy", id, attrs);
	}
	
    /* Component 2 */
    public Object convertWasDerivedFrom(Object id, Object id2, Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:generatedEntity", id2));
    	attrs.add(tuple("prov:usedEntity", id1));
    	if (pe != null) {
    		attrs.add(tuple("prov:activity", pe));
    	}
    	if (q2 != null) {
    		attrs.add(tuple("prov:generation", q2));
    	}
    	if (q1 != null) {
    		attrs.add(tuple("prov:usage", q1));
    	}
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
    		id = getBlankID("wDF");

    	return new ProvRecord("wasDerivedFrom", id, attrs);
	}


    public Object convertWasRevisionOf(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs)
    {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:generatedEntity", id2));
    	attrs.add(tuple("prov:usedEntity", id1));
    	if (pe != null) {
    		attrs.add(tuple("prov:activity", pe));
    	}
    	if (q2 != null) {
    		attrs.add(tuple("prov:generation", q2));
    	}
    	if (q1 != null) {
    		attrs.add(tuple("prov:usage", q1));
    	}
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
//    	attrs.add(tuple("prov:type", "prov:Revision"));
    	
    	if (id == null)
    		id = getBlankID("wRO");

    	return new ProvRecord("wasRevisionOf", id, attrs);
    }

    public Object convertWasQuotedFrom(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:generatedEntity", id2));
    	attrs.add(tuple("prov:usedEntity", id1));
    	if (pe != null) {
    		attrs.add(tuple("prov:activity", pe));
    	}
    	if (q2 != null) {
    		attrs.add(tuple("prov:generation", q2));
    	}
    	if (q1 != null) {
    		attrs.add(tuple("prov:usage", q1));
    	}
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
//    	attrs.add(tuple("prov:type", "prov:Quotation"));
    	if (id == null)
    		id = getBlankID("wQF");

    	return new ProvRecord("wasQuotedFrom", id, attrs);
    }
    
	public Object convertHadPrimarySource(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:generatedEntity", id2));
    	attrs.add(tuple("prov:usedEntity", id1));
    	if (pe != null) {
    		attrs.add(tuple("prov:activity", pe));
    	}
    	if (q2 != null) {
    		attrs.add(tuple("prov:generation", q2));
    	}
    	if (q1 != null) {
    		attrs.add(tuple("prov:usage", q1));
    	}
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
    		id = getBlankID("hPS");

    	return new ProvRecord("hadPrimarySource", id, attrs);
    }

    /* Component 3 */
    public Object convertAgent(Object id, Object attrs) {
		return new ProvRecord("agent", id, attrs);
	}

    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object gAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:entity", id2));
    	attrs.add(tuple("prov:agent", id1));
    	if (gAttrs != null) {
    		attrs.addAll((List<Object>)gAttrs);
    	}
    	if (id == null)
    		id = getBlankID("wAT");

    	return new ProvRecord("wasAttributedTo", id, attrs);
    }

    public Object convertWasAssociatedWith(Object id, Object id2, Object id1,
			Object pl, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:activity", id2));
    	if (id1 != null) {
    		attrs.add(tuple("prov:agent", id1));
    	}
    	if (pl != null) {
    		attrs.add(tuple("prov:plan", pl));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("wAW");

    	return new ProvRecord("wasAssociatedWith", id, attrs);
	}

    public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:subordinate", id2));
    	attrs.add(tuple("prov:responsible", id1));
    	if (a != null) {
    		attrs.add(tuple("prov:activity", a));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("aOBO");

    	return new ProvRecord("actedOnBehalfOf", id, attrs);
	}

	public Object convertWasInfluencedBy(Object id, Object id2, Object id1, Object dAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:influencee", id2));
    	attrs.add(tuple("prov:influencer", id1));
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
    		id = getBlankID("wIB");

    	return new ProvRecord("wasInfluencedBy", id, attrs);
    }

	/* Component 5 */
	public Object convertSpecializationOf(Object id2, Object id1) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:specificEntity", id2));
    	attrs.add(tuple("prov:generalEntity", id1));
        Object id = getBlankID("sO");

    	return new ProvRecord("specializationOf", id, attrs);
	}

	public Object convertAlternateOf(Object id2, Object id1) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:alternate1", id1));
    	attrs.add(tuple("prov:alternate2", id2));
    	Object id = getBlankID("aO");

    	return new ProvRecord("alternateOf", id, attrs);
	}

    public Object convertMentionOf(Object su, Object bu, Object ta) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:specificEntity", su));
    	attrs.add(tuple("prov:generalEntity", ta));
    	if (bu != null) {
    		attrs.add(tuple("prov:bundle", bu));
    	}
    	
    	String id = getBlankID("mO");

    	return new ProvRecord("mentionOf", id, attrs);
    }

    /* Component 6 */
    public Object convertInsertion(Object id, Object id2, Object id1, Object map, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:after", id2));
    	attrs.add(tuple("prov:before", id1));
    	List<Object[]> tuples = (List<Object[]>)map;
    	Map<Object, Object> key_entities = new HashMap<Object, Object>(); 
    	for (Object[] t : tuples) {
    		key_entities.put(t[0], t[1]);
    	}
    	attrs.add(tuple("prov:key-entity-set", key_entities));
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
        	id = getBlankID("dBIF");

    	return new ProvRecord("derivedByInsertionFrom", id, attrs);
    }

    public Object convertEntry(Object o1, Object o2) {
        return tuple(o1, o2);
    }

    public Object convertKeyEntitySet(List<Object> o) {
        return o;
    }

    public Object convertRemoval(Object id, Object id2, Object id1, Object keys, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:after", id2));
    	attrs.add(tuple("prov:before", id1));
    	attrs.add(tuple("prov:key-set", keys));
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
        	id = getBlankID("dBIF");

    	return new ProvRecord("derivedByRemovalFrom", id, attrs);
    }

    public Object convertKeys(List<Object> keys) {
        return keys;
    }

    public Object convertCollectionMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:collection", id2));
    	attrs.add(tuple("prov:entity-set", map));
    	attrs.add(tuple("prov:complete", complete));
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
        	id = getBlankID("mO");

    	return new ProvRecord("memberOf", id, attrs);
    }

    public Object convertDictionaryMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:dictionary", id2));
    	attrs.add(tuple("prov:key-entity-set", map));
    	attrs.add(tuple("prov:complete", complete));
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
        	id = getBlankID("mOD");

    	return new ProvRecord("memberOfDictionary", id, attrs);
    }

	/* Component 4 */
	public Object convertNamedBundle(Object id, Object nss, List<Object> records) {
		Map<String,Object> bundle = buildBundle(nss, records);
		return tuple(id, bundle);
    }
	
    /* Other conversions */
    public Object convertExtension(Object name, Object id, Object args, Object dAttrs) {
    	return null;
    }

    private Map<String,Object> buildBundle(Object nss, List<Object> records) {
    	Map<String,Object> bundle = new HashMap<String, Object>();
    	if (nss != null)
    		bundle.put("prefix", nss);
		for (Object o: records) {
        	if (o == null) continue;
            ProvRecord record = (ProvRecord)o;
            String type = record.type;
            
            Map<Object, Object> structure = (Map<Object, Object>)bundle.get(type);
            if (structure == null) {
            	structure = new HashMap<Object, Object>();
            	bundle.put(type, structure);
            }
            Map<Object,Object> hash = new HashMap<Object, Object>();
            List<Object[]> tuples = (List<Object[]>)record.attributes;
            for (Object[] tuple : tuples) {
            	Object attribute = tuple[0];
            	Object value = tuple[1];
            	if (hash.containsKey(attribute)) {
            		Object existing = hash.get(attribute);
            		if (existing instanceof List) {
            			// Already a multi-value attribute
            			List<Object> values = (List<Object>)existing;
            			values.add(value);
            		}
            		else {
            			// A multi-value list needs to be created
            			List<Object> values = new ArrayList<Object>();
            			values.add(existing);
            			values.add(value);
            			hash.put(attribute, values);
            		}
            	}
            	else {
            		hash.put(attribute, value);
            	}
            }
            if (structure.containsKey(record.id)) {
            	Object existing = structure.get(record.id);
            	if (existing instanceof List) {
        			List<Object> values = (List<Object>)existing;
        			values.add(hash);
        		}
        		else {
        			// A multi-value list needs to be created
        			List<Object> values = new ArrayList<Object>();
        			values.add(existing);
        			values.add(hash);
        			structure.put(record.id, values);
        		}
            }
            else
            	structure.put(record.id, hash);
        }
        return bundle;
    }
    
	public Object convertBundle(Object nss, List<Object> records, List<Object> bundles) {
		// constructing a normal bundle 
		Map<String,Object> bundle = buildBundle(nss, records);
		Map<Object, Object> bundleStructure = new HashMap<Object, Object>();
    	bundle.put("bundle", bundleStructure);
    	for (Object obj : bundles) {
    		Object[] tuple = (Object[])obj;
    		bundleStructure.put(tuple[0], tuple[1]);
    	}
		return bundle;
	}

	public Object convertAttributes(List<Object> attributes) {
		return attributes;
	}

	public Object convertId(String id) {
		return id;
	}

	public Object convertAttribute(Object name, Object value) {
		if (value instanceof String) {
            String val1=(String)(value);
            return tuple(name, unwrap(val1));
        } else {
        	// TODO Handling qnames
        	return tuple(name, value);
        }
	}

	public Object convertStart(String start) {
		return start;
	}

	public Object convertEnd(String end) {
		return end;
	}

	public Object convertString(String s) {
		return s;
	}

	public Object convertString(String s, String lang) {
		return typedLiteral(s, null, lang);
	}

	public Object convertInt(int i) {
		return new Integer(i);
	}

	public Object convertQualifiedName(String qname) {
		return unwrap(qname);
	}

	public Object convertIRI(String iri) {
		return unwrap(iri);
	}

	public Object convertPrefix(String pre) {
		return pre;
	}

	public Object convertTypedLiteral(String datatype, Object value) {
		if (value instanceof Map) {
			// TODO: Hack to deal with the case of string@lang, which
			// is already converted into a map
			((Map<String, String>) value).put("type", datatype);
			return value;
		}
		else {
			return typedLiteral(unwrap((String)value), datatype, null);
		}
	}

	public Object convertNamespace(Object pre, Object iri) {
		return tuple(pre, unwrap((String)iri));
	}

	public Object convertDefaultNamespace(Object iri) {
		return tuple("default", unwrap((String)iri));
	}

	public Object convertNamespaces(List<Object> namespaces) {
		Map<Object,Object> nss = new HashMap<Object, Object>();
		for (Object o : namespaces) {
			Object[] ns = (Object[])o;
			nss.put(ns[0], ns[1]);
		}
		return nss;
	}

	@Override
        public void startBundle(Object bundleId) {
	    // TODO Auto-generated method stub
	    
        }
}
	