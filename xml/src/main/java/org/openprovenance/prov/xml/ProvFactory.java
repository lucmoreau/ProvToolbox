package org.openprovenance.prov.xml;
import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.util.Date;
import java.net.URI;
import javax.xml.bind.JAXBElement;
import java.util.GregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/** A stateless factory for PROV objects. */

public class ProvFactory {

    public static String printURI(java.net.URI u) {
	return u.toString();
    }


    public static final String packageList=
        "org.openprovenance.prov.xml";

    public String getPackageList() {
        return packageList;
    }

    /** Note, this method now makes it stateful :-( */
    private Hashtable<String,String> namespaces=null;
    public void setNamespaces(Hashtable<String,String> nss) {
        namespaces=nss;
    }
    

    private final static ProvFactory oFactory=new ProvFactory();

    public static ProvFactory getFactory() {
        return oFactory;
    }

    protected ObjectFactory of;

    protected DatatypeFactory dataFactory;

    void init() {
        try {
            dataFactory= DatatypeFactory.newInstance ();
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException (ex);
        }
    }



    public ProvFactory() {
        of=new ObjectFactory();
        init();
    }

    public ProvFactory(Hashtable<String,String> namespaces) {
        of=new ObjectFactory();
        this.namespaces=namespaces;
        init();
    }

    public ProvFactory(ObjectFactory of) {
        this.of=of;
        init();
    }

    public ObjectFactory getObjectFactory() {
        return of;
    }

    public ActivityRef newActivityRef(Activity p) {
        ActivityRef res=of.createActivityRef();
        res.setRef(p.getId());
        return res;
    }

    public ActivityRef newActivityRef(QName id) {
        ActivityRef res=of.createActivityRef();
        res.setRef(id);
        return res;
    }


    public QName stringToQName(String id) {
        if (id==null) return null;
        int index=id.indexOf(':');
        if (index==-1) {
            return new QName(namespaces.get("_"),id);
        }
        String prefix=id.substring(0,index);
        String local=id.substring(index+1,id.length());
        return new QName(namespaces.get(prefix),
                         local,
                         prefix);
    }

    public ActivityRef newActivityRef(String id) {
        return newActivityRef(stringToQName(id));
    }

    public NoteRef newNoteRef(Note a) {
        NoteRef res=of.createNoteRef();
        res.setRef(a.getId());
        return res;
    }

    public NoteRef newNoteRef(QName id) {
        NoteRef res=of.createNoteRef();
        res.setRef(id);
        return res;
    }
        
    public NoteRef newNoteRef(String id) {
        return newNoteRef(stringToQName(id));
    }


    public BundleRef newBundleRef(QName id) {
        BundleRef res=of.createBundleRef();
        res.setRef(id);
        return res;
    }
        
    public BundleRef newBundleRef(String id) {
        return newBundleRef(stringToQName(id));
    }


    public AnyRef newAnyRef(QName id) {
        AnyRef res=of.createAnyRef();
        res.setRef(id);
        return res;
    }
        
    public AnyRef newAnyRef(String id) {
        return newAnyRef(stringToQName(id));
    }

    public EntityRef newEntityRef(Entity a) {
        EntityRef res=of.createEntityRef();
        res.setRef(a.getId());
        return res;
    }

    public EntityRef newEntityRef(QName id) {
        EntityRef res=of.createEntityRef();
        res.setRef(id);
        return res;
    }
    public EntityRef newEntityRef(String id) {
        return newEntityRef(stringToQName(id));
    }

    public AgentRef newAgentRef(Agent a) {
        AgentRef res=of.createAgentRef();
        res.setRef(a.getId());
        return res;
    }

    public AgentRef newAgentRef(String id) {
        return newAgentRef(stringToQName(id));
    }

    public AgentRef newAgentRef(QName id) {
        AgentRef res=of.createAgentRef();
        res.setRef(id);
        return res;
    }



    public DependencyRef newDependencyRef(QName id) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(id);
        return res;
    }

    public DependencyRef newDependencyRef(String id) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(stringToQName(id));
        return res;
    }

    public DependencyRef newDependencyRef(WasGeneratedBy edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge.getId());
        return res;
    }

    public DependencyRef newDependencyRef(Used edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge.getId());
        return res;
    }
    public DependencyRef newDependencyRef(WasDerivedFrom edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge.getId());
        return res;
    }

    public DependencyRef newDependencyRef(WasInformedBy edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge.getId());
        return res;
    }



    public Activity newActivity(String pr) {
        return newActivity(pr,null);
    }

    public Activity newActivity(QName pr) {
        return newActivity(pr,null);
    }

    public Activity newActivity(QName pr,
                                String label) {
        Activity res=of.createActivity();
        res.setId(pr);
        if (label!=null) res.setLabel(label);
        return res;
    }

    public Activity newActivity(String pr,
                                String label) {
        return newActivity(stringToQName(pr),label);
    }

    public Agent newAgent(String ag) {
        return newAgent(ag,null);
    }
    public Agent newAgent(QName ag) {
        return newAgent(ag,null);
    }

    public Agent newAgent(QName ag,
                          String label) {
        Agent res=of.createAgent();
        res.setId(ag);
        if (label!=null) res.setLabel(label);
        return res;
    }
    public Agent newAgent(String ag,
                          String label) {
        return newAgent(stringToQName(ag),label);
    }



    








    public XMLGregorianCalendar
        newXMLGregorianCalendar(GregorianCalendar gc) {
        return dataFactory.newXMLGregorianCalendar(gc);
    }



	public XMLGregorianCalendar newTime (Date date) {
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(date);
        return newXMLGregorianCalendar(gc);
    }

	public XMLGregorianCalendar newTimeNow () {
        return newTime(new Date());
    }

	public XMLGregorianCalendar newISOTime (String time) {
        return newTime(javax.xml.bind.DatatypeConverter.parseDateTime(time).getTime());
    }


    /** By default, no auto generation of Id.  Override this behaviour if required. */
    public String autoGenerateId(String prefix) {
        return null;
    }

    /** Conditional autogeneration of Id. By default, no auto
     * generation of Id.  Override this behaviour if required. */
    public String autoGenerateId(String prefix, String id) {
        return id;
    }


    public Entity newEntity(Entity a) {
        Entity res=newEntity(a.getId());
        return res;
    }
    public Activity newActivity(Activity a) {
        Activity res=newActivity(a.getId());
        return res;
    }
    public Agent newAgent(Agent a) {
        Agent res=newAgent(a.getId());
        return res;
    }


    public Entity newEntity(QName id) {
        return newEntity(id,null);
    }

    public Entity newEntity(String id) {
        return newEntity(stringToQName(id),null);
    }

    public Entity newEntity(QName id,
                            String label) {
        Entity res=of.createEntity();
        res.setId(id);
        if (label!=null) res.setLabel(label);
        return res;
    }

    public Entity newEntity(String id,
                            String label) {
        return newEntity(stringToQName(id),label);
    }



    public Used newUsed(String id,
                        ActivityRef pid,
                        String role,
                        EntityRef aid) {
        Used res=of.createUsed();
        res.setId(stringToQName(id));
        res.setActivity(pid);
        addRole(res,role);
        res.setEntity(aid);
        return res;
    }

    public Used newUsed(QName id,
                        ActivityRef aid,
                        String role,
                        EntityRef eid) {
        Used res=of.createUsed();
        res.setId(id);
        res.setActivity(aid);
        addRole(res,role);
        res.setEntity(eid);
        return res;
    }

    public AlternateOf newAlternateOf(EntityRef eid2,
                                      EntityRef eid1) {
        AlternateOf res=of.createAlternateOf();
        res.setEntity2(eid2);
        res.setEntity1(eid1);
        return res;
    }
    public SpecializationOf newSpecializationOf(EntityRef eid2,
                                                EntityRef eid1) {
        SpecializationOf res=of.createSpecializationOf();
        res.setSpecializedEntity(eid2);
        res.setGeneralEntity(eid1);
        return res;
    }



    public WasAssociatedWith newWasAssociatedWith(String id,
                                                  ActivityRef eid2,
                                                  AgentRef eid1) {
        WasAssociatedWith res=of.createWasAssociatedWith();
        res.setId(stringToQName(id));
        res.setActivity(eid2);
        res.setAgent(eid1);
        return res;
    }

    public WasAssociatedWith newWasAssociatedWith(QName id,
                                                  ActivityRef eid2,
                                                  AgentRef eid1) {
        WasAssociatedWith res=of.createWasAssociatedWith();
        res.setId(id);
        res.setActivity(eid2);
        res.setAgent(eid1);
        return res;
    }


    public WasAssociatedWith newWasAssociatedWith(String id,
                                                  Activity eid2,
                                                  Agent eid1) {
        return newWasAssociatedWith(id,newActivityRef(eid2.getId()),newAgentRef(eid1.getId()));
    }

    public WasAssociatedWith newWasAssociatedWith(QName id,
                                                  Activity eid2,
                                                  Agent eid1) {
        return newWasAssociatedWith(id,newActivityRef(eid2.getId()),newAgentRef(eid1.getId()));
    }



    public ActedOnBehalfOf newActedOnBehalfOf(String id,
                                              AgentRef subordinate,
                                              AgentRef responsible,
                                              ActivityRef eid2) {
        ActedOnBehalfOf res=of.createActedOnBehalfOf();
        res.setId(stringToQName(id));
        res.setActivity(eid2);
        res.setSubordinate(subordinate);
        res.setResponsible(responsible);
        return res;
    }

    public ActedOnBehalfOf newActedOnBehalfOf(QName id,
                                              AgentRef subordinate,
                                              AgentRef responsible,
                                              ActivityRef eid2) {
        ActedOnBehalfOf res=of.createActedOnBehalfOf();
        res.setId(id);
        res.setActivity(eid2);
        res.setSubordinate(subordinate);
        res.setResponsible(responsible);
        return res;
    }

    public WasRevisionOf newWasRevisionOf(String id,
                                          EntityRef newer,
                                          EntityRef older) {
        WasRevisionOf res=of.createWasRevisionOf();
        res.setId(stringToQName(id));
        res.setNewer(newer);
        res.setOlder(older);
        return res;
    }

    public WasRevisionOf newWasRevisionOf(QName id,
                                          EntityRef newer,
                                          EntityRef older) {
        WasRevisionOf res=of.createWasRevisionOf();
        res.setId(id);
        res.setNewer(newer);
        res.setOlder(older);
        return res;
    }


    public WasQuotedFrom newWasQuotedFrom(String id,
                                          EntityRef quote,
                                          EntityRef original) {
        WasQuotedFrom res=of.createWasQuotedFrom();
        res.setId(stringToQName(id));
        res.setQuote(quote);
        res.setOriginal(original);
        return res;
    }

    public WasQuotedFrom newWasQuotedFrom(QName id,
                                          EntityRef quote,
                                          EntityRef original) {
        WasQuotedFrom res=of.createWasQuotedFrom();
        res.setId(id);
        res.setQuote(quote);
        res.setOriginal(original);
        return res;
    }


    public TracedTo newTracedTo(String id,
                                EntityRef entity,
                                EntityRef ancestor) {
        TracedTo res=of.createTracedTo();
        res.setId(stringToQName(id));
        res.setEntity(entity);
        res.setAncestor(ancestor);
        return res;
    }

    public TracedTo newTracedTo(QName id,
                                EntityRef entity,
                                EntityRef ancestor) {
        TracedTo res=of.createTracedTo();
        res.setId(id);
        res.setEntity(entity);
        res.setAncestor(ancestor);
        return res;
    }

    public HadOriginalSource newHadOriginalSource(String id,
                                                  EntityRef derived,
                                                  EntityRef source) {
        HadOriginalSource res=of.createHadOriginalSource();
        res.setId(stringToQName(id));
        res.setDerived(derived);
        res.setSource(source);
        return res;
    }

    public HadOriginalSource newHadOriginalSource(QName id,
                                                  EntityRef derived,
                                                  EntityRef source) {
        HadOriginalSource res=of.createHadOriginalSource();
        res.setId(id);
        res.setDerived(derived);
        res.setSource(source);
        return res;
    }


    public Entry newEntry(Object key,
			  EntityRef entity) {
        Entry res=of.createEntry();
        res.setKey(key);
        res.setEntity(entity);
        return res;
    }

    public DerivedByInsertionFrom newDerivedByInsertionFrom(QName id,
							    EntityRef after,
							    EntityRef before,
							    List<Entry> keyEntitySet) {
        DerivedByInsertionFrom res=of.createDerivedByInsertionFrom();
        res.setId(id);
        res.setAfter(after);
        res.setBefore(before);
	if (keyEntitySet!=null) res.getEntry().addAll(keyEntitySet);
        return res;
    }


    public DerivedByInsertionFrom newDerivedByInsertionFrom(String id,
							    EntityRef after,
							    EntityRef before,
							    List<Entry> keyEntitySet) {
        return newDerivedByInsertionFrom(stringToQName(id),
					 after,
					 before,
					 keyEntitySet);
    }



    public DerivedByRemovalFrom newDerivedByRemovalFrom(QName id,
							EntityRef after,
							EntityRef before,
							List<Object> keys) {
        DerivedByRemovalFrom res=of.createDerivedByRemovalFrom();
        res.setId(id);
        res.setAfter(after);
        res.setBefore(before);
	if (keys!=null) res.getKey().addAll(keys);
        return res;
    }


    public DerivedByRemovalFrom newDerivedByRemovalFrom(String id,
							EntityRef after,
							EntityRef before,
							List<Object> keys) {
        return newDerivedByRemovalFrom(stringToQName(id),
				       after,
				       before,
				       keys);
    }


    public MemberOf newMemberOf(QName id,
				EntityRef after,
				List<Entry> keyEntitySet) {
        MemberOf res=of.createMemberOf();
        res.setId(id);
        res.setEntity(after);
	if (keyEntitySet!=null) res.getEntry().addAll(keyEntitySet);
        return res;
    }


    public MemberOf newMemberOf(String id,
				EntityRef after,
				List<Entry> keyEntitySet) {
        return newMemberOf(stringToQName(id),
					 after,
					 keyEntitySet);
    }


    public void addRole(HasRole a,                                  
                        Object role) {
        if (role!=null) {
            a.getRole().add(role);
        }
    }

    

    public Used newUsed(Activity p,
                        String role,
                        Entity a) {
        Used res=newUsed(null,p,role,a);
        return res;
    }

    public Used newUsed(String id,
                        Activity p,
                        String role,
                        Entity a) {
        ActivityRef pid=newActivityRef(p);
        EntityRef aid=newEntityRef(a);
        return newUsed(stringToQName(id),pid,role,aid);
    }


    public Used newUsed(String id,
                        Activity p,
                        String role,
                        Entity a,
                        String type) {
        Used res=newUsed(id,p,role,a);
        addType(res,type);
        return res;
    }

    public Used newUsed(Used u) {
        Used u1=newUsed(u.getId(),
                        u.getActivity(),
                        null,
                        u.getEntity());
        u1.getAny().addAll(u.getAny());
        return u1;
    }

    public WasGeneratedBy newWasGeneratedBy(WasGeneratedBy g) {
        WasGeneratedBy wgb=newWasGeneratedBy(g.getId(),
                                             g.getEntity(),
                                             null,
                                             g.getActivity());
        wgb.setId(g.getId());
        wgb.getAny().addAll(g.getAny());
        return wgb;
    }

    public WasDerivedFrom newWasDerivedFrom(WasDerivedFrom d) {
        WasDerivedFrom wdf=newWasDerivedFrom(d.getId(),
                                             d.getGeneratedEntity(),
                                             d.getUsedEntity());
        wdf.getAny().addAll(d.getAny());
        return wdf;
    }

    public WasInformedBy newWasInformedBy(WasInformedBy d) {
        WasInformedBy wtb=newWasInformedBy(d.getId(),
                                           d.getEffect(),
                                           d.getCause());
        wtb.setId(d.getId());
        wtb.getAny().addAll(d.getAny());
        return wtb;
    }



    public WasGeneratedBy newWasGeneratedBy(QName id,
                                            EntityRef aid,
                                            String role,
                                            ActivityRef pid) {
        WasGeneratedBy res=of.createWasGeneratedBy();
        res.setId(id);
        res.setActivity(pid);
        res.setEntity(aid);
        addRole(res,role);
        return res;
    }

    public WasStartedBy newWasStartedBy(QName id,
                                        ActivityRef aid,
                                        EntityRef eid) {
        WasStartedBy res=of.createWasStartedBy();
        res.setId(id);
        res.setActivity(aid);
        res.setTrigger(eid);
        return res;
    }

    public WasStartedBy newWasStartedBy(String id,
                                        ActivityRef aid,
                                        EntityRef eid) {
        return newWasStartedBy(stringToQName(id),aid,eid);
    }

    public WasInvalidatedBy newWasInvalidatedBy(QName id,
                                                EntityRef eid,
                                                ActivityRef aid) {
        WasInvalidatedBy res=of.createWasInvalidatedBy();
        res.setId(id);
        res.setEntity(eid);
        res.setActivity(aid);
        return res;
    }

    public WasInvalidatedBy newWasInvalidatedBy(String id,
                                                EntityRef eid,
                                                ActivityRef aid) {
        return newWasInvalidatedBy(stringToQName(id),eid,aid);
    }


    public WasEndedBy newWasEndedBy(QName id,
                                    ActivityRef aid,
                                    EntityRef eid) {
        WasEndedBy res=of.createWasEndedBy();
        res.setId(id);
        res.setActivity(aid);
        res.setTrigger(eid);
        return res;
    }

    public WasEndedBy newWasEndedBy(String id,
                                    ActivityRef aid,
                                    EntityRef eid) {
        return newWasEndedBy(stringToQName(id),aid,eid);
    }


    public WasAttributedTo newWasAttributedTo(QName id,
                                              EntityRef eid,
                                              AgentRef agid) {
        WasAttributedTo res=of.createWasAttributedTo();
        res.setId(id);
        res.setEntity(eid);
        res.setAgent(agid);
        return res;
    }

    public WasAttributedTo newWasAttributedTo(String id,
                                              EntityRef eid,
                                              AgentRef agid) {
        return newWasAttributedTo(stringToQName(id),eid,agid);
    }


    public WasGeneratedBy newWasGeneratedBy(String id,
                                            EntityRef aid,
                                            String role,
                                            ActivityRef pid) {
        return newWasGeneratedBy(stringToQName(id),aid,role,pid);
    }

    public WasGeneratedBy newWasGeneratedBy(Entity a,
                                            String role,
                                            Activity p) {
        return newWasGeneratedBy((QName)null,a,role,p);
    }

    public WasGeneratedBy newWasGeneratedBy(String id,
                                            Entity a,
                                            String role,
                                            Activity p) {
        EntityRef aid=newEntityRef(a);
        ActivityRef pid=newActivityRef(p);
        LinkedList ll=new LinkedList();
        return  newWasGeneratedBy(stringToQName(id),aid,role,pid);
    }


    public WasGeneratedBy newWasGeneratedBy(QName id,
                                            Entity a,
                                            String role,
                                            Activity p) {
        EntityRef aid=newEntityRef(a);
        ActivityRef pid=newActivityRef(p);
        LinkedList ll=new LinkedList();
        return  newWasGeneratedBy(id,aid,role,pid);
    }



    public WasGeneratedBy newWasGeneratedBy(String id,
                                            Entity a,
                                            String role,
                                            Activity p,
                                            String type) {
        WasGeneratedBy wgb=newWasGeneratedBy(id,a,role,p);
        addType(wgb,type);
        return wgb;
    }


    public HasAnnotation newHasAnnotation(Identifiable i,
                                          Note n) {
	HasAnnotation res=of.createHasAnnotation();
	res.setThing(newNoteRef(i.getId()));
	res.setNote(newNoteRef(n));
	return res;
    }

    public HasAnnotation newHasAnnotation(Identifiable i,
                                          String n) {
	HasAnnotation res=of.createHasAnnotation();
	res.setThing(newNoteRef(i.getId())); // note, this id is not necessarily of a note
	res.setNote(newNoteRef(n));
	return res;
    }

    public HasAnnotation newHasAnnotation(String n1,
                                          String n) {
	HasAnnotation res=of.createHasAnnotation();
	res.setThing(newNoteRef(n1));
	res.setNote(newNoteRef(n));
	return res;
    }

    public HasProvenanceIn newHasProvenanceIn(QName id,
                                              String subject,
                                              String bundle,
                                              String target,
                                              String service,
                                              String provenance) {
        HasProvenanceIn res=of.createHasProvenanceIn();
        res.setId(id);
        res.setSubject(newAnyRef(subject));
        res.setBundle(newBundleRef(bundle));
        res.setTarget(newAnyRef(target));
        res.setService(service);
        res.setProvenance(provenance);
        return res;
    }

    public HasProvenanceIn newHasProvenanceIn(String id,
                                              String subject,
                                              String bundle,
                                              String target,
                                              String service,
                                              String provenance) {
        HasProvenanceIn res=of.createHasProvenanceIn();
        res.setId(stringToQName(id));
        res.setSubject(newAnyRef(subject));
        if (bundle!=null) res.setBundle(newBundleRef(bundle));
        if (target!=null) res.setTarget(newAnyRef(target));
        if (service!=null) res.setService(service);
        if (provenance!=null) res.setProvenance(provenance);
        return res;
    }


    public WasDerivedFrom newWasDerivedFrom(QName id,
                                            EntityRef aid1,
                                            EntityRef aid2) {
        WasDerivedFrom res=of.createWasDerivedFrom();
        res.setId(id);
        res.setUsedEntity(aid2);
        res.setGeneratedEntity(aid1);
        return res;
    }
    public WasDerivedFrom newWasDerivedFrom(String id,
                                            EntityRef aid1,
                                            EntityRef aid2) {
        return newWasDerivedFrom(stringToQName(id),aid1,aid2);
    }


    public WasDerivedFrom newWasDerivedFrom(QName id,
                                            EntityRef aid1,
                                            EntityRef aid2,
                                            ActivityRef aid,
                                            DependencyRef did1,
                                            DependencyRef did2) {
        WasDerivedFrom res=of.createWasDerivedFrom();
        res.setId(id);
        res.setUsedEntity(aid2);
        res.setGeneratedEntity(aid1);
        res.setActivity(aid);
        res.setGeneration(did1);
        res.setUsage(did2);
        return res;
    }
    public WasDerivedFrom newWasDerivedFrom(String id,
                                            EntityRef aid1,
                                            EntityRef aid2,
                                            ActivityRef aid,
                                            DependencyRef did1,
                                            DependencyRef did2) {
        return newWasDerivedFrom(stringToQName(id),aid1,aid2,aid,did1,did2);
    }



    public WasDerivedFrom newWasDerivedFrom(Entity a1,
                                            Entity a2) {
        return newWasDerivedFrom(null,a1,a2);
    }

    public WasDerivedFrom newWasDerivedFrom(String id,
                                            Entity a1,
                                            Entity a2) {
        EntityRef aid1=newEntityRef(a1);
        EntityRef aid2=newEntityRef(a2);
        return  newWasDerivedFrom(id,aid1,aid2);
    }

    public WasDerivedFrom newWasDerivedFrom(Entity a1,
                                            Entity a2,
                                            Activity a,
                                            WasGeneratedBy g2,
                                            Used u1) {
        return newWasDerivedFrom(null,a1,a2,a,g2,u1);
    }

    public WasDerivedFrom newWasDerivedFrom(String id,
                                            Entity e2,
                                            Entity e1,
                                            Activity a,
                                            WasGeneratedBy g2,
                                            Used u1) {
        EntityRef eid1=newEntityRef(e1);
        EntityRef eid2=newEntityRef(e2);
        ActivityRef aid=newActivityRef(a);
        DependencyRef did2=newDependencyRef(g2);
        DependencyRef did1=newDependencyRef(u1);
        return  newWasDerivedFrom(id,eid2,eid1,aid,did2,did1);
    }


    public WasDerivedFrom newWasDerivedFrom(String id,
                                            Entity a1,
                                            Entity a2,
                                            String type) {
        WasDerivedFrom wdf=newWasDerivedFrom(id,a1,a2);
        addType(wdf,type);
        return wdf;
    }



    public WasInformedBy newWasInformedBy(QName id,
                                          ActivityRef pid1,
                                          ActivityRef pid2) {
        WasInformedBy res=of.createWasInformedBy();
        res.setId(id);
        res.setEffect(pid1);
        res.setCause(pid2);
        return res;
    }
    public WasInformedBy newWasInformedBy(String id,
                                          ActivityRef pid1,
                                          ActivityRef pid2) {
        return newWasInformedBy(stringToQName(id),pid1,pid2);
    }

    public WasInformedBy newWasInformedBy(Activity p1,
                                          Activity p2) {
        return newWasInformedBy(null,p1,p2);
    }

    public WasInformedBy newWasInformedBy(String id,
                                          Activity p1,
                                          Activity p2) {
        ActivityRef pid1=newActivityRef(p1);
        ActivityRef pid2=newActivityRef(p2);
        return  newWasInformedBy(id,pid1,pid2);
    }


    public WasInformedBy newWasInformedBy(QName id,
                                          Activity p1,
                                          Activity p2,
                                          String type) {
        WasInformedBy wtb=newWasInformedBy(p1,p2);
        wtb.setId(id);
        addType(wtb,type);
        return wtb;
    }
    public WasInformedBy newWasInformedBy(String id,
                                          Activity p1,
                                          Activity p2,
                                          String type) {
        return newWasInformedBy(stringToQName(id),p1,p2,type);
    }



    public void addAttribute(HasExtensibility a, Object o) {
        a.getAny().add(o);
    }


    /*    public void addType(HasExtensibility a,
                        String type,
                        String typeOfType) {

        OldTypedLiteral tl=newOldTypedLiteral(type,typeOfType);
        JAXBElement<OldTypedLiteral> je=of.createType(tl);
        addAttribute(a,je);

    }
    */


    public void addTypeOLD(HasExtensibility a,
                        URI type) {

	URIWrapper u=new URIWrapper();
	u.setValue(type);
        JAXBElement<Object> je=of.createType(u);
        addAttribute(a,je);
    }

    public void addType(HasType a,
                        URI type) {
        URIWrapper u=new URIWrapper();
        u.setValue(type);
        a.getType().add(u);
    }


    public void addType(HasType a,
                        Object type) {

        a.getType().add(type);
    }

    public void addTypeOLD(HasExtensibility a,
                        Object type) {

        //TypedLiteral tl=newTypedLiteral(type);
        JAXBElement<Object> je=of.createType(type);
        addAttribute(a,je);

    }

    //    public void addType(HasExtensibility a,
    //                        String type) {
    //
    //        addType(a,type,"xsd:anyURI");
    //    }

    public void addLabel(HasExtensibility a,                                  
                         String label) {
        JAXBElement<String> je=of.createLabel(label);
        addAttribute(a,je);
    }


    public void addAttribute(HasExtensibility a,
                             String namespace,
                             String prefix,
                             String localName,                                  
                             String value) {

        a.getAny().add(newAttribute(namespace,
                                        prefix,
                                        localName,
                                        value));
    }

    public Element newAttribute(String namespace,
                                String prefix,
                                String localName,                                  
                                String value) {
        Document doc=oFactory.builder.newDocument();
        Element el=doc.createElementNS(namespace,
                                       ( (prefix.equals(""))
                                         ?
                                         ""
                                         :
                                         (prefix + ":")) + localName);
        el.appendChild(doc.createTextNode(value));
        doc.appendChild(el);
        return el;
    }



    public Note newNote(QName id) {
        Note res=of.createNote();
        res.setId(id);
        return res;
    }
    public Note newNote(String id) {
        return newNote(stringToQName(id));
    }

    public Bundle newBundle(      Collection<Activity> ps,
                                  Collection<Entity> as,
                                  Collection<Agent> ags,
                                  Collection<Object> lks) {
        return newBundle((QName)null,ps,as,ags,null,lks);
    }

    public Bundle newBundle(      Collection<Activity> ps,
                                  Collection<Entity> as,
                                  Collection<Agent> ags,
                                  Collection<Note> ns,
                                  Collection<Object> lks) {
        return newBundle((QName)null,ps,as,ags,ns,lks);
    }

    public Bundle newBundle(      QName ignore,
				  Collection<Activity> ps,
                                  Collection<Entity> as,
                                  Collection<Agent> ags,
                                  Collection<Note> ns,
                                  Collection<Object> lks)
    {
        Bundle res=of.createBundle();
        res.setRecords(of.createRecords());
        if (ps!=null) {
            res.getRecords().getActivity().addAll(ps);
        }
        if (as!=null) {
            res.getRecords().getEntity().addAll(as);
        }
        if (ags!=null) {
            res.getRecords().getAgent().addAll(ags);
        }
        if (lks!=null) {
            Dependencies ccls=of.createDependencies();
            ccls.getUsedOrWasGeneratedByOrWasStartedBy().addAll(lks);
            res.getRecords().setDependencies(ccls);
        }

        if (ns!=null) {
            res.getRecords().getNote().addAll(ns);
        }
        return res;
    }

    public NamedBundle newNamedBundle(String id,
                                  Collection<Activity> ps,
                                  Collection<Entity> as,
                                  Collection<Agent> ags,
                                  Collection<Note> ns,
                                  Collection<Object> lks)
    {
	return newNamedBundle(stringToQName(id),
			      ps,
			      as,
			      ags,
			      ns,
			      lks);
    }

    public NamedBundle newNamedBundle(QName id,
                                  Collection<Activity> ps,
                                  Collection<Entity> as,
                                  Collection<Agent> ags,
                                  Collection<Note> ns,
                                  Collection<Object> lks)
    {
        NamedBundle res=of.createNamedBundle();
        res.setRecords(of.createRecords());
        res.setId(id);
        if (ps!=null) {
            res.getRecords().getActivity().addAll(ps);
        }
        if (as!=null) {
            res.getRecords().getEntity().addAll(as);
        }
        if (ags!=null) {
            res.getRecords().getAgent().addAll(ags);
        }
        if (lks!=null) {
            Dependencies ccls=of.createDependencies();
            ccls.getUsedOrWasGeneratedByOrWasStartedBy().addAll(lks);
            res.getRecords().setDependencies(ccls);
        }

        if (ns!=null) {
            res.getRecords().getNote().addAll(ns);
        }
        return res;
    }

    public Bundle newBundle(String id,
                                  Collection<Activity> ps,
                                  Collection<Entity> as,
                                  Collection<Agent> ags,
                                  Collection<Note> ns,
                                  Collection<Object> lks) {
        return newBundle(stringToQName(id),ps,as,ags,ns,lks);
    }

    public Bundle newBundle(      Activity [] ps,
                                  Entity [] as,
                                  Agent [] ags,
                                  Object [] lks) 
    {

        return newBundle(   ((ps==null) ? null : Arrays.asList(ps)),
                            ((as==null) ? null : Arrays.asList(as)),
                            ((ags==null) ? null : Arrays.asList(ags)),
                            ((lks==null) ? null : Arrays.asList(lks)));
    }
    public Bundle newBundle(      Activity [] ps,
                                  Entity [] as,
                                  Agent [] ags,
                                  Note [] ns,
                                  Object [] lks) {
        return newBundle(null,ps,as,ags,ns,lks);
    }

    public Bundle newBundle(String id,
                                  Activity [] ps,
                                  Entity [] as,
                                  Agent [] ags,
                                  Note [] ns,
                                  Object [] lks) 
    {

        return newBundle(id,
                            ((ps==null) ? null : Arrays.asList(ps)),
                            ((as==null) ? null : Arrays.asList(as)),
                            ((ags==null) ? null : Arrays.asList(ags)),
                            ((ns==null) ? null : Arrays.asList(ns)),
                            ((lks==null) ? null : Arrays.asList(lks)));
    }

    public Bundle newBundle(      Collection<Activity> ps,
                                  Collection<Entity> as,
                                  Collection<Agent> ags,
                                  Dependencies lks)
    {
        Bundle res=of.createBundle();
	res.setRecords(of.createRecords());
        //res.setId(autoGenerateId(bundleIdPrefix));
        res.getRecords().getActivity().addAll(ps);
        res.getRecords().getEntity().addAll(as);
        res.getRecords().getAgent().addAll(ags);
        res.getRecords().setDependencies(lks);
        return res;
    }

    public Bundle newBundle(      Collection<Activity> ps,
                                  Collection<Entity> as,
                                  Collection<Agent> ags,
                                  Collection<Note> ns,
                                  Dependencies lks)
    {
        Bundle res=of.createBundle();
	res.setRecords(of.createRecords());
        //res.setId(autoGenerateId(bundleIdPrefix));
        res.getRecords().getActivity().addAll(ps);
        res.getRecords().getEntity().addAll(as);
        res.getRecords().getAgent().addAll(ags);
        res.getRecords().setDependencies(lks);
        res.getRecords().getNote().addAll(ns);
        return res;
    }



    public Bundle newBundle(Bundle graph) {
        return newBundle(   graph.getRecords().getActivity(),
                            graph.getRecords().getEntity(),
                            graph.getRecords().getAgent(),
                            graph.getRecords().getNote(),
                            graph.getRecords().getDependencies());
    }

    public String getLabel(HasExtensibility e) {

        if (e instanceof HasLabel) return ((HasLabel)e).getLabel();
        for (Object o: e.getAny()) {
            
        }
        return "pFact: label TODO";
    }

    public List<Object> getType(HasExtensibility e) {
        if (e instanceof HasType) return ((HasType)e).getType();
        for (Object o: e.getAny()) {
            
        }
        List<Object> res=new LinkedList();
        res.add("pFact: type TODO");
        return res;
    }

    public String getRole(HasExtensibility e) {
        for (Object o: e.getAny()) {
            
        }
        return "pFact: role TODO";
    }


    static {
        initBuilder();
    }
    static public DocumentBuilder builder;

	static void initBuilder() {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true);
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}
            
}

