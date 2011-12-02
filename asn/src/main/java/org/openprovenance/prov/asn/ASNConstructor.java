package org.openprovenance.prov.asn;
import java.util.List;

/** For testing purpose, conversion back to ASN. */

public class ASNConstructor implements TreeConstructor {




    public Object convertActivity(Object id,Object recipe,Object startTime,Object endTime, Object aAttrs) {
        String s="activity(" + id + "," + recipe + "," + startTime + "," + endTime + ",[" + aAttrs + "])";
        return s;
    }
    public Object convertEntity(Object id, Object attrs) {
        String s="entity(" + id  + ",[" + attrs + "])";
        return s;
    }
    public Object convertAgent(Object id, Object attrs) {
        String s="agent(" + id  + ",[" + attrs + "])";
        return s;
    }
    public Object convertContainer(Object namespaces, List<Object> records) {
        String s="container(";
        s=s+namespaces;
        for (Object o: records) {
            s=s+o+",\n";
        }
        s=s+")";
        return s;
    }
    public Object convertAttributes(List<Object> attributes) {
        String s="";
        boolean first=true;
        for (Object o: attributes) {
            if (first) {
                first=false;
                s=s+o;
            } else {
                s=s+","+o;
            }
        }
        return s;
    }
    public Object convertId(String id) {
        return id;
    }
    public Object convertAttribute(Object name, Object value) {
        return name + "=" + value;
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
    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        String s="used(" + ((id==null)? "" : id + ",") + id2 + "," + id1 +
            ((time==null)? "" : (", " + time)) + ",[" + aAttrs + "]" + ")";
        return s;
    }
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s="wasGeneratedBy(" + ((id==null)? "" : id + ",") + id2 + "," + id1 +
            ((time==null)? "" : (", " + time)) + ",[" + aAttrs + "]" +  ")";
        return s;
    }
    public Object convertWasDerivedFrom(Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s="wasDerivedFrom(" + id2 + "," + id1 + 
            ((pe==null) ? "" : (", " + pe + ", " + g2 + "," + u1)) + ",[" + aAttrs + "]" +  ")";
        return s;
    }

    public Object convertQNAME(String qname) {
        return qname;
    }
    public Object convertIRI(String iri) {
        return iri;
    }
    public Object convertTypedLiteral(String datatype, Object value) {
        return datatype + "%%" + value;
    }

   public Object convertNamespace(Object pre, Object iri) {
       return "prefix " + pre + " " + iri;
   }

   public Object convertDefaultNamespace(Object iri) {
       return  "default " + iri;
   }

    public Object convertNamespaces(List<Object> namespaces) {
        String s="";
        for (Object o: namespaces) {
            s=s+o+"\n";
        }
        return s;
    }

 

}