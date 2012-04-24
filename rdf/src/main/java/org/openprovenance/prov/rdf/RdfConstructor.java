package org.openprovenance.prov.rdf;
import org.openprovenance.prov.notation.TreeConstructor;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.elmo.ElmoManager;
import javax.xml.namespace.QName;
import java.util.Hashtable;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
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
            //Hack, I need to parse the attributes
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

        Entity e1=(Entity)manager.find(qn1);
        Activity a2=(Activity)manager.find(qn2);
        Usage u=null;

        /** Creates instance of Usage class, only if required. */

	//         java.lang.System.out.println("used -> " + qn1);

        if ((id!=null)  || (time!=null) || (aAttrs!=null)) {

            u = (Usage) manager.designate(qname, Usage.class);
            EntityInvolvement qi=(EntityInvolvement) u;

            qi.getEntities().add(e1);

            //HashSet<Usage> su=new HashSet<Usage>();
            //su.add(u);
            a2.getQualifiedUsage().add(u);

        }

        Set<Entity> se=a2.getUsed();
        se.add(e1);


        return u;
    }

    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        QName qname = getQName(id);
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);

        Entity e2=(Entity)manager.find(qn2);
        Activity a1=(Activity)manager.find(qn1);

        Generation g=null;

        if ((id!=null)  || (time!=null) || (aAttrs!=null)) {

            g = (Generation) manager.designate(Generation.class);
            ActivityInvolvement qi=(ActivityInvolvement) g;

            qi.getActivities().add(a1);

            e2.getQualifiedGeneration().add(g);
        }

        e2.setWasGeneratedBy(a1);

        return g;
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

    public Object convertWasInvalidatedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        //todo
        throw new UnsupportedOperationException();
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
        //todo
        throw new UnsupportedOperationException();
    }
    public Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        Entity e2=(Entity)manager.find(qn2);
        Entity e1=(Entity)manager.find(qn1);

        Set<Entity> se=e2.getWasDerivedFrom();
        se.add(e1);

        
        return null;
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
    public Object convertAlternateOf(Object id2, Object id1) {
        return null;
    }

    public Object convertSpecializationOf(Object id2,Object id1) {
        return null;
    }
	public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        return null;
    }

    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        QName qname = getQName(id);
        QName qn2 = getQName(id2);
        QName qn1 = getQName(id1);
        QName qnpl = getQName(pl);

        Association a = (Association) manager.designate(qname, Association.class);
        AgentInvolvement qi=(AgentInvolvement) a;

        Activity a2=(Activity)manager.find(qn2);
        Agent ag1=(Agent)manager.find(qn1);
        qi.getEntities().add(ag1);

        a2.getQualifiedAssociation().add(a);

	if (qnpl!=null) {
	    Plan plan=(Plan)manager.find(qnpl);
	    a.setHadPlan(plan);
	}

	a2.getWasAssociatedWith().add(ag1);

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


   /* Component 5 */

    public Object convertInsertion(Object id, Object id2, Object id1, Object map, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertEntry(Object o1, Object o2) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertKeyEntitySet(List<Object> o) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertRemoval(Object id, Object id2, Object id1, Object keys, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
        //todo
        throw new UnsupportedOperationException();
    }

    public Object convertKeys(List<Object> keys) {
        //todo
        throw new UnsupportedOperationException();
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
