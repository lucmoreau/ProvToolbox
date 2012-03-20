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

	public Object convertEntity(Object id, Object attrs) {
		return new ProvRecord("entity", id, attrs);
	}

	public Object convertAgent(Object id, Object attrs) {
		return new ProvRecord("entity", id, addAttribute(attrs, tuple("prov:type", "prov:agent")));
	}

	public Object convertContainer(Object nss, List<Object> records) {
		Map<String,Object> container = new HashMap<String, Object>();
		container.put("prefix", nss);
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
            Map<Object, Object> structure = (Map<Object, Object>)container.get(type);
            if (structure == null) {
            	structure = new HashMap<Object, Object>();
            	container.put(type, structure);
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
        	container.put("agent", agents);
        return container;
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

	public Object convertA(Object a) {
		return a;
	}

	public Object convertU(Object a) {
		return a;
	}

	public Object convertG(Object a) {
		return a;
	}

	public Object convertString(String s) {
		return s;
	}

	public Object convertInt(int i) {
		String[] value = {Integer.toString(i), "xsd:int"};
		return value;
	}

	public Object convertUsed(Object id, Object id2, Object id1, Object time, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:entity", id1));
    	attrs.add(tuple("prov:activity", id2));
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
    	attrs.add(tuple("prov:activity", id1));
    	attrs.add(tuple("prov:entity", id2));
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
        //todo
        throw new UnsupportedOperationException();
    }

	public Object convertWasEndedBy(Object id, Object id2, Object id1,
			Object time, Object aAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object gAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

	public Object convertWasDerivedFrom(Object id, Object id2, Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:usedEntity", id2));
    	attrs.add(tuple("prov:generatedEntity", id1));
    	if (pe != null) {
    		attrs.add(tuple("prov:activity", pe));
    	}
    	if (q2 != null) {
    		attrs.add(tuple("prov:usage", q2));
    	}
    	if (q1 != null) {
    		attrs.add(tuple("prov:generation", q1));
    	}
    	if (dAttrs != null) {
    		attrs.addAll((List<Object>)dAttrs);
    	}
    	
    	if (id == null)
    		id = getBlankID("wDF");

    	return new ProvRecord("wasDerivedFrom", id, attrs);
	}

	public Object convertWasAssociatedWith(Object id, Object id2, Object id1,
			Object pl, Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:agent", id2));
    	attrs.add(tuple("prov:activity", id1));
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

    public Object convertWasRevisionOf(Object id, Object id2,Object id1, Object ag, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertWasQuotedFrom(Object id, Object id2,Object id1, Object ag2, Object ag1, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertHadOriginalSource(Object id, Object id2,Object id1, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertTracedTo(Object id, Object id2, Object id1, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
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

    	return new ProvRecord("wasAssociatedWith", id, attrs);
	}

	public Object convertAlternateOf(Object id, Object id2, Object id1,
			Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:entity", id1));
    	attrs.add(tuple("prov:alternate", id2));
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("aO");

    	return new ProvRecord("alternateOf", id, attrs);
	}

	public Object convertSpecializationOf(Object id, Object id2, Object id1,
			Object aAttrs) {
		List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("prov:entity", id1));
    	attrs.add(tuple("prov:specialization", id2));
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	if (id == null)
    		id = getBlankID("aO");

    	return new ProvRecord("specializationOf", id, attrs);
	}

	public Object convertQNAME(String qname) {
		return unwrap(qname);
	}

	public Object convertIRI(String iri) {
		return unwrap(iri);
	}

	public Object convertPrefix(String pre) {
		return pre;
	}

	public Object convertTypedLiteral(String datatype, Object value) {
		// TODO: Convert typed literal
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
	