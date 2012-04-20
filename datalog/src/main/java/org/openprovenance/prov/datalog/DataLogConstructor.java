package org.openprovenance.prov.datalog;

import  org.openprovenance.prov.notation.TreeConstructor;
import  org.antlr.runtime.tree.CommonTree;

import java.util.List;

/** For testing purpose, conversion back to ASN. */

public class DataLogConstructor implements TreeConstructor {
    int i=0;
    public String genId() {
	return "al_" + (i++);
    }
 

    public String optionalAttributes(Object attrs) {
        String s_attrs=(String)attrs;
        if ("".equals(s_attrs)) {
            return ", nil";
        } else {
	    String val=genId();
            return "," + val;
        }
    }

    public String optionalTime(Object time) {
        return ((time==null)? ", nil" : (", " + time));
    }            

    public Object optional(Object str) {
        return ((str==null)? "nil" : str);
    }

    public Object convertActivity(Object id,Object startTime,Object endTime, Object aAttrs) {
        String s="activity(" + id + "," + optional(startTime) + "," + optional(endTime) + optionalAttributes(aAttrs) + ")";
        return s;
    }
    public Object convertEntity(Object id, Object attrs) {
        String s="entity(" + id  + optionalAttributes(attrs) + ")";
        return s;
    }
    public Object convertAgent(Object id, Object attrs) {
        String s="agent(" + id  + optionalAttributes(attrs) + ")";
        return s;
    }
    public Object convertContainer(Object namespaces, List<Object> records) {
        String s="";
        for (Object o: records) {
            s=s+o+".\n";
        }
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
        return ((id==null)? id : id.replace(':','_'));
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

    public Object convertInt(int i) {
        return i;
    }

    public String optionalId(Object id) {
        return ((id==null)? "nil, " : (id + ","));
    }            

    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        String s="used(" + optionalId(id) + id2 + "," + id1 +
            optionalTime(time) + optionalAttributes(aAttrs) + ")";
        return s;
    }
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s="wasGeneratedBy(" + optionalId(id) + id2 + "," + id1 +
            optionalTime(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertWasInvalidatedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s="wasInvalidatedBy(" + optionalId(id) + id2 + "," + id1 +
            optionalTime(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertWasStartedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s="wasStartedBy(" + optionalId(id) + id2 + "," + id1 +
            optionalTime(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertWasEndedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s="wasEndedBy(" + optionalId(id) + id2 + "," + id1 +
            optionalTime(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertWasInformedBy(Object id, Object id2, Object id1, Object aAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertWasStartedByActivity(Object id, Object id2, Object id1, Object aAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object gAttrs) {
        String s="wasAttributedTo(" + optionalId(id) + id2 + "," + id1 + optionalAttributes(gAttrs) +  ")";
        return s;
    }

    public Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s="wasDerivedFrom(" + optionalId(id) + id2 + "," + id1 + 
            ((pe==null) ? ", nil " : (", " + pe + ", " + g2 + "," + u1)) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertAlternateOf(Object id2, Object id1) {
        String s="alternateOf(" + id2 + "," + id1 + ")";
        return s;
    }

    public Object convertSpecializationOf(Object id2, Object id1) {
        String s="specializationOf(" + id2 + "," + id1 + ")";
        return s;
    }

	public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        throw new UnsupportedOperationException();
    }

    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        String s="wasAssociatedWith(" + optionalId(id) + id2 + "," + id1 
            + ((pl==null)? "" : " , " + pl) +
            optionalAttributes(aAttrs) + ")";
        return s;
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

    public Object convertQNAME(String qname) {
        return qname;
    }
    public Object convertIRI(String iri) {
        return iri;
    }

    public Object convertTypedLiteral(String datatype, Object value) {
        return value + "%%" + datatype;
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

    public Object convertPrefix(String pre) {
        return pre;
    }

   /* Component 6 */

    public Object convertNote(Object id, Object attrs) {
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertHasAnnotation(Object something, Object note) {
        //todo
        throw new UnsupportedOperationException();
    }



}