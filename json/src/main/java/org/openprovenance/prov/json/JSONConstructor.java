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
	
	private static final String PROVJS_ARRAY = "provjs:array";
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
		// TODO Deal with provjs:array and typed literal array
//		for (int i = 0; i < result.size(); i++) {
//			Object[] arr = result.get(i);
//			if (key.equals(arr[0])) {
//				if (arr[1] instanceof Object[]) {
//					Object[] array = (Object[])arr[1];
//					int n = array.length;
//					// Can be typed literal or a provjs:array
//					if (array[n-1].equals(PROVJS_ARRAY)) {
//						// It is a provjs:array
//						
//					}
//					else {
//						
//					}
//				}
//				else {
//					
//				}
//			}
//		}
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
    
    public Object convertUsed(Object id, Object id2, Object id1, Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:activity", id2));
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

	public Object convertWasStartedBy(Object id, Object id2, Object id1,
			Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:activity", id2));
    	if (id1 != null) {
    		attrs.add(tuple("prov:trigger", id1));
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

	public Object convertWasEndedBy(Object id, Object id2, Object id1,
			Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:activity", id2));
    	if (id1 != null) {
    		attrs.add(tuple("prov:trigger", id1));
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

    public Object convertWasStartedByActivity(Object id, Object id2, Object id1, Object aAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:started", id2));
    	attrs.add(tuple("prov:starter", id1));
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("wSBA");

    	return new ProvRecord("wasStartedByActivity", id, attrs);
    }


	
    /* Component 2 */
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


    /* Component 3 */
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
    	attrs.add(tuple("prov:newer", id2));
    	attrs.add(tuple("prov:older", id1));
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
    		id = getBlankID("wRO");

    	return new ProvRecord("wasRevisionOf", id, attrs);
    }

    public Object convertWasQuotedFrom(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:quote", id2));
    	attrs.add(tuple("prov:original", id1));
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
    		id = getBlankID("wQF");

    	return new ProvRecord("wasQuotedFrom", id, attrs);
    }
    
	public Object convertHadOriginalSource(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:derived", id2));
    	attrs.add(tuple("prov:source", id1));
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
    		id = getBlankID("hOS");

    	return new ProvRecord("hadOriginalSource", id, attrs);
    }

	public Object convertTracedTo(Object id, Object id2, Object id1, Object dAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:entity", id2));
    	attrs.add(tuple("prov:ancestor", id1));
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
    		id = getBlankID("tT");

    	return new ProvRecord("tracedTo", id, attrs);
    }

	/* Component 4 */
	public Object convertAlternateOf(Object id2, Object id1) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:alternate1", id1));
    	attrs.add(tuple("prov:alternate2", id2));
    	Object id = getBlankID("aO");

    	return new ProvRecord("alternateOf", id, attrs);
	}

	public Object convertSpecializationOf(Object id2, Object id1) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:specializedEntity", id2));
    	attrs.add(tuple("prov:generalEntity", id1));
        Object id = getBlankID("sO");

    	return new ProvRecord("specializationOf", id, attrs);
	}

	/* Component 5 */
    public Object convertInsertion(Object id, Object id2, Object id1, Object map, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:after", id2));
    	attrs.add(tuple("prov:before", id1));
    	attrs.add(tuple("prov:key-entity-set", map));
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

    public Object convertMembership(Object id, Object id2, Object keys, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:after", id2));
    	attrs.add(tuple("prov:key-entity-set", keys));
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
        	id = getBlankID("mO");

    	return new ProvRecord("memberOf", id, attrs);
    }

    public Object convertKeys(List<Object> keys) {
        return keys;
    }

    public Object convertMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:after", id2));
    	attrs.add(tuple("prov:key-entity-set", map));
    	attrs.add(tuple("prov:complete", complete));
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
        	id = getBlankID("mO");

    	return new ProvRecord("memberOf", id, attrs);
    }

   /* Component 6 */
    public Object convertNote(Object id, Object attrs) {
    	return new ProvRecord("note", id, attrs);
    }
    public Object convertHasAnnotation(Object something, Object note) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:something", something));
    	attrs.add(tuple("prov:note", note));
        Object id = getBlankID("sO");

    	return new ProvRecord("specializationOf", id, attrs);
    }


    /* Other conversions */    
	public Object convertBundle(Object nss, List<Object> records, List<Object> bundles) {
		Map<String,Object> bundle = new HashMap<String, Object>();
		bundle.put("prefix", nss);
		List<Object> agents = new ArrayList<Object>();
        for (Object o: records) {
        	if (o == null) continue;
            ProvRecord record = (ProvRecord)o;
            String type = record.type;
            if (type == "agent") {
            	agents.add(record.id);
            	if (record.attributes == null)
            		continue;
            	type = "entity";
            }
            Map<Object, Object> structure = (Map<Object, Object>)bundle.get(type);
            if (structure == null) {
            	structure = new HashMap<Object, Object>();
            	bundle.put(type, structure);
            }
            Map<Object,Object> hash = new HashMap<Object, Object>();
            List<Object[]> tuples = (List<Object[]>)record.attributes;
            for (Object[] tuple : tuples) {
            	if (hash.containsKey(tuple[0])) {
            		// TODO: Deal with duplicated keys
            	}
            	else {
            		hash.put(tuple[0], tuple[1]);
            	}
            }
            structure.put(record.id, hash);
        }
        if (!agents.isEmpty())
        	bundle.put("agent", agents);
        return bundle;
	}

	public Object convertNamedBundle(Object id, Object nss, List<Object> records) {
        return null;
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

	public Object convertInt(int i) {
		String[] value = {Integer.toString(i), "xsd:int"};
		return value;
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
		Object[] result = {unwrap((String)value), unwrap(datatype)};
    	return result;
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
}
	