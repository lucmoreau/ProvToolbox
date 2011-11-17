package org.openprovenance.prov.asn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.tree.CommonTree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** For testing purpose, conversion back to ASN. */

class ProvRecord {
	String type;
	Object id;
	Object attributes;
	
	public ProvRecord(String type, Object id, Object attributes) {
		this.type = type;
		this.id = id;
		this.attributes = attributes;
	}
}

public class JSONConstructor implements TreeConstructor {
	
	public static void main(String[] args)  {
        try {
            Utility u=new Utility();

            CommonTree tree = u.parseASNTree(args[0]);

            u.printTree(tree,1);
            
//            System.out.println(tree.toStringTree());
            Object provStructure = new Traversal(new JSONConstructor()).convert(tree);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(provStructure));
            
        } catch(Throwable t) {
            System.out.println("exception: "+t);
            t.printStackTrace();
        } 
	}

	static final Map<String,Integer> countMap = new HashMap<String, Integer>();
	private static String getBlankID(String type) {
		if (!countMap.containsKey(type)) {
			countMap.put(type, 0);
		}
		int count = countMap.get(type);
		count += 1;
		countMap.put(type, count);
		return "_:" + type + count;
	}
	
	public static String unwrap (String s) {
		int len = s.length();
		if (s.charAt(0) == '\"' && s.charAt(len-1) == '\"')
			return s.substring(1,len-1);
		else return s;
    }
	
	private Object[] tuple(Object o1, Object o2) {
		Object[] tuple = {o1, o2};
        return tuple;
	}
	
    public Object convertActivity(Object id,Object recipe,Object startTime,Object endTime, Object aAttrs) {
    	List<Object> attrs = new ArrayList<Object>();
    	if (recipe != null) {
    		attrs.add(tuple("recipe", recipe));
    	}
    	if (startTime != null) {
    		attrs.add(tuple("startTime", startTime));
    	}
    	if (endTime != null) {
    		attrs.add(tuple("endTime", endTime));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	return new ProvRecord("activity", id, attrs);
    }
    public Object convertEntity(Object id, Object attrs) {
        return new ProvRecord("entity", id, attrs);
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
    public Object convertString(String s) {
        return s;
    }
    public Object convertUsed(Object id2,Object id1, Object aAttrs, Object time) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("cause", id1));
    	attrs.add(tuple("activity", id2));
    	if (time != null) {
    		attrs.add(tuple("time", time));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	return new ProvRecord("used", getBlankID("u"), attrs);
    }
    public Object convertWasGeneratedBy(Object id2,Object id1, Object aAttrs, Object time) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("effect", id2));
    	attrs.add(tuple("cause", id1));
    	if (time != null) {
    		attrs.add(tuple("time", time));
    	}
    	if (aAttrs != null) {
    		attrs.addAll((List<Object>)aAttrs);
    	}
    	return new ProvRecord("wasGeneratedBy", getBlankID("wGB"), attrs);
    }
    public Object convertWasDerivedFrom(Object id2,Object id1, Object pe, Object q2, Object q1) {
    	List<Object> attrs = new ArrayList<Object>();
    	attrs.add(tuple("effect", id2));
    	attrs.add(tuple("cause", id1));
    	if (pe != null) {
    		attrs.add(tuple("activity", pe));
    	}
    	// TODO: Deal with q1 and q2
    	return new ProvRecord("wasDerivedFrom", getBlankID("wDF"), attrs);
    }

    public Object convertQNAME(String qname) {
        return qname;
    }
    public Object convertIRI(String iri) {
        return iri;
    }
    public Object convertTypedLiteral(String datatype, Object value) {
    	// TODO: Convert typed literal
        return datatype + "%%" + value;
    }
    public Object convertContainer(List<Object> records) {
        Map<String,Map<Object,Object>> container = new HashMap<String, Map<Object,Object>>();
        for (Object o: records) {
            ProvRecord record = (ProvRecord)o;
            Map<Object, Object> structure = container.get(record.type);
            if (structure == null) {
            	structure = new HashMap<Object, Object>();
            	container.put(record.type, structure);
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
        return container;
    }
}