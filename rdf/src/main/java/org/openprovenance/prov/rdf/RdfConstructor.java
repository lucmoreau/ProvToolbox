package org.openprovenance.prov.rdf;
import org.openprovenance.prov.asn.TreeConstructor;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.elmo.ElmoManager;
import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.List;
import java.net.URI;

public class RdfConstructor implements TreeConstructor {
    final ProvFactory pFactory;
    final ElmoManager manager;

    Hashtable<String,String>  namespaceTable = new Hashtable<String,String>();

    public RdfConstructor(ProvFactory pFactory, ElmoManager manager) {
        this.pFactory=pFactory;
        this.manager=manager;
        pFactory.setNamespaces(namespaceTable);
    }

    public Object convertActivity(Object id,Object startTime,Object endTime, Object aAttrs) {
        QName qname = getQName(id);
        org.openprovenance.prov.rdf.Activity a = (org.openprovenance.prov.rdf.Activity) manager.designate(qname, org.openprovenance.prov.rdf.Activity.class);
        return a;
    }

    public Object convertEntity(Object id, Object attrs) {
        QName qname = getQName(id);
        org.openprovenance.prov.rdf.Entity e = (org.openprovenance.prov.rdf.Entity) manager.designate(qname, org.openprovenance.prov.rdf.Entity.class);
        return e;
    }

    public Object convertAgent(Object id, Object attrs) {
        QName qname = getQName(id);
        org.openprovenance.prov.rdf.Agent ag = (org.openprovenance.prov.rdf.Agent) manager.designate(qname, org.openprovenance.prov.rdf.Agent.class);
        return ag;
    }

    public Object convertContainer(Object nss, List<Object> records) {
        return null;
    }
    public Object convertAttributes(List<Object> attributes) {
        return null;
    }
    public Object convertId(String id) {
        return id;
    }
    public Object convertAttribute(Object name, Object value) {
        return null;
    }
    public Object convertStart(String start) {
        return null;
    }
    public Object convertEnd(String end) {
        return null;
    }
    public Object convertA(Object a) {
        return null;
    }
    public Object convertU(Object a) {
        return null;
    }
    public Object convertG(Object a) {
        return null;
    }
    public Object convertString(String s) {
        s=unwrap(s);
        return s;
    }


    public Object convertInt(int s) {
        return null;
    }

    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }
    public Object convertWasDerivedFrom(Object id2,Object id1, Object pe, Object q2, Object q1, Object time, Object dAttrs) {
        return null;
    }

    public Object convertAlternateOf(Object id, Object id2,Object id1, Object aAttrs) {
        return null;
    }

    public Object convertSpecializationOf(Object id, Object id2,Object id1, Object aAttrs) {
        return null;
    }

    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        return null;
    }

    public Object convertQNAME(String qname) {
        return qname;
    }
    public Object convertIRI(String iri) {
        iri=unwrap(iri);
        return URI.create(iri);
    }

    public Object convertTypedLiteral(String datatype, Object value) {
        return null;
    }
    public Object convertNamespace(Object pre, Object iri) {
       String s_pre=(String)pre;
       String s_iri=(String)iri;
       s_iri=unwrap(s_iri);
       namespaceTable.put(s_pre,s_iri);
       return null;
    }

    public Object convertDefaultNamespace(Object iri) {
       String s_iri=(String)iri;
       s_iri=unwrap(s_iri);
       namespaceTable.put("_",s_iri);
       return null;
    }
    public Object convertNamespaces(List<Object> namespaces) {
        pFactory.setNamespaces(namespaceTable);
        return namespaceTable;
    }
    public Object convertPrefix(String pre) {
        return pre;
    }

    
    public QName getQName(Object id) {
        String idAsString=(String)id;
        return pFactory.stringToQName(idAsString);
    }

    public String unwrap (String s) {
        return s.substring(1,s.length()-1);
    }

        
}
