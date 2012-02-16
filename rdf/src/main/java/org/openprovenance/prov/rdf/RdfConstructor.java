package org.openprovenance.prov.rdf;
import org.openprovenance.prov.asn.TreeConstructor;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.elmo.ElmoManager;
import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
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
        Activity a = (Activity) manager.designate(qname, Activity.class);
        return a;
    }

    public Object convertEntity(Object id, Object attrs) {
        QName qname = getQName(id);
        Entity e;
        if (qname.getLocalPart().equals("rec-advance")) {
            //Hack, I need to part the attributes
            e = (Entity) manager.designate(qname, Plan.class);
        } else {
            e = (Entity) manager.designate(qname, Entity.class);
        }
        return e;
    }

    public Object convertAgent(Object id, Object attrs) {
        QName qname = getQName(id);
        Agent ag = (Agent) manager.designate(qname, Agent.class);
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
        QName qname = getQName(id);
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        java.lang.System.out.print("--> usage " + qname);
        Usage u = (Usage) manager.designate(qname, Usage.class);
        QualifiedInvolvement qi=(QualifiedInvolvement) u;

        Entity e1=(Entity)manager.find(qn1);
        Activity a2=(Activity)manager.find(qn2);
        qi.setHadQualifiedEntity(e1);

        HashSet<Usage> su=new HashSet<Usage>();
        su.add(u);
        a2.setHadQualifiedUsage(su);


        return u;
    }

    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        QName qname = getQName(id);
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        Generation g = (Generation) manager.designate(Generation.class);
        QualifiedInvolvement qi=(QualifiedInvolvement) g;
        Entity e2=(Entity)manager.find(qn2);
        Activity a1=(Activity)manager.find(qn1);
        qi.setHadQualifiedEntity(e2);

        HashSet<Generation> sg=new HashSet<Generation>();
        sg.add(g);
        a1.setHadQualifiedGeneration(sg);

        return g;
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
        QName qname = getQName(id);
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        QName qnpl = getQName(pl);
        java.lang.System.out.println("convertWasAssociatedWith " + qname);
        java.lang.System.out.println("convertWasAssociatedWith " + qnpl);
        Association a = (Association) manager.designate(qname, Association.class);
        QualifiedInvolvement qi=(QualifiedInvolvement) a;

        Activity a2=(Activity)manager.find(qn2);
        Agent ag1=(Agent)manager.find(qn1);
        qi.setHadQualifiedEntity(ag1);

        HashSet<Association> sa=new HashSet<Association>();
        sa.add(a);
        a2.setHadQualifiedAssociation(sa);

        Plan plan=(Plan)manager.find(qnpl);
        HashSet<Plan> sp=new HashSet<Plan>();
        sp.add(plan);
        a.setAdoptedPlan(sp);


        return a;

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
