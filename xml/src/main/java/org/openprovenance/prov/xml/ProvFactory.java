package org.openprovenance.prov.xml;
import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import java.util.GregorianCalendar;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/** A stateless factory for PROV objects. */

public class ProvFactory implements CommonURIs {

    public static String roleIdPrefix="r_";
    public static String usedIdPrefix="u_";
    public static String wasGenerateByIdPrefix="g_";
    public static String wasDerivedFromIdPrefix="d_";
    public static String wasTriggeredByIdPrefix="t_";
    public static String wasControlledByIdPrefix="c_";
    public static String containerIdPrefix="gr_";

    public static final String packageList=
        "org.openprovenance.prov.xml";

    public String getPackageList() {
        return packageList;
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

    public ProvFactory(ObjectFactory of) {
        this.of=of;
        init();
    }

    public ObjectFactory getObjectFactory() {
        return of;
    }

    public ActivityRef newActivityRef(Activity p) {
        ActivityRef res=of.createActivityRef();
        res.setRef(p);
        return res;
    }

    public RoleRef newRoleRef(Role p) {
        RoleRef res=of.createRoleRef();
        res.setRef(p);
        return res;
    }

    public AnnotationRef newAnnotationRef(Annotation a) {
        AnnotationRef res=of.createAnnotationRef();
        res.setRef(a);
        return res;
    }

    public EntityRef newEntityRef(Entity a) {
        EntityRef res=of.createEntityRef();
        res.setRef(a);
        return res;
    }
    public AgentRef newAgentRef(Agent a) {
        AgentRef res=of.createAgentRef();
        res.setRef(a);
        return res;
    }

    public AccountRef newAccountRef(Account acc) {
        AccountRef res=of.createAccountRef();
        res.setRef(acc);
        return res;
    }





    public DependencyRef newDependencyRef(WasGeneratedBy edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge);
        return res;
    }

    public DependencyRef newDependencyRef(Used edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge);
        return res;
    }
    public DependencyRef newDependencyRef(WasDerivedFrom edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge);
        return res;
    }

    public DependencyRef newDependencyRef(WasControlledBy edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge);
        return res;
    }
    public DependencyRef newDependencyRef(WasTriggeredBy edge) {
        DependencyRef res=of.createDependencyRef();
        res.setRef(edge);
        return res;
    }



    public Activity newActivity(String pr,
                              Collection<Account> accounts) {
        return newActivity(pr,accounts,null);
    }

    public Activity newActivity(String pr,
                              Collection<Account> accounts,
                              String label) {
        Activity res=of.createActivity();
        res.setId(pr);
        addAccounts(res,accounts,null);
        if (label!=null) addAnnotation(res,newLabel(label));
        return res;
    }

    public Agent newAgent(String ag,
                          Collection<Account> accounts) {
        return newAgent(ag,accounts,null);
    }

    public Agent newAgent(String ag,
                          Collection<Account> accounts,
                          String label) {
        Agent res=of.createAgent();
        res.setId(ag);
        addAccounts(res,accounts,null);
        if (label!=null) addAnnotation(res,newLabel(label));
        return res;
    }

    public Account newAccount(String id) {
        Account res=of.createAccount();
        res.setId(id);
        return res;
    }
    public Account newAccount(String id, String label) {
        Account res=of.createAccount();
        res.setId(id);
        if (label!=null) addAnnotation(res,newLabel(label));
        return res;
    }

    public EmbeddedAnnotation newLabel(String label) {
        EmbeddedAnnotation res=of.createEmbeddedAnnotation();
	res.getProperty().add(newProperty(LABEL_PROPERTY,label));
        return res;
    }

    public EmbeddedAnnotation newValue(Object value, String encoding) {
        EmbeddedAnnotation res=of.createEmbeddedAnnotation();
	res.getProperty().add(newProperty(VALUE_PROPERTY,value));
	res.getProperty().add(newProperty(ENCODING_PROPERTY,encoding));
        return res;
    }



    public Role getRole(Relation e) {
        if (e instanceof Used) {
            return ((Used) e).getRole();
        } 
        if (e instanceof WasGeneratedBy) {
            return ((WasGeneratedBy) e).getRole();
        } 
        if (e instanceof WasControlledBy) {
            return ((WasControlledBy) e).getRole();
        }
        return null;
    }
    
    public String getLabel(EmbeddedAnnotation annotation) {
	if (annotation==null) return null;
	for (Property prop: annotation.getProperty()) {
	    if (prop.getAttribute().equals(LABEL_PROPERTY)) {
		return (String) prop.getValue();
	    }
	}
	return null;
    }


    public String getType(EmbeddedAnnotation annotation) {
	if (annotation==null) return null;
	for (Property prop: annotation.getProperty()) {
	    if (prop.getAttribute().equals(TYPE_PROPERTY)) {
		return (String) prop.getValue();
	    }
	}
	return null;
    }

    public Object getValue(EmbeddedAnnotation annotation) {
	if (annotation==null) return null;
	for (Property prop: annotation.getProperty()) {
	    if (prop.getAttribute().equals(VALUE_PROPERTY)) {
		return prop.getValue();
	    }
	}
	return null;
    }

    public String getEncoding(EmbeddedAnnotation annotation) {
	if (annotation==null) return null;
	for (Property prop: annotation.getProperty()) {
	    if (prop.getAttribute().equals(ENCODING_PROPERTY)) {
		return (String) prop.getValue();
	    }
	}
	return null;
    }



    /** Return the value of the value property in the first annotation. */

    public Object getValue(List<EmbeddedAnnotation> annotations) {
        for (EmbeddedAnnotation ann: annotations) {
            Object value=getValue(ann);
            if (value!=null) return value;
        }
        return null;
    }



    /** Return the value of the label property in the first annotation. */
    public String getLabel(List<EmbeddedAnnotation> annotations) {
        for (EmbeddedAnnotation ann: annotations) {
            String label=getLabel(ann);
            if (label!=null) return label;
        }
        return null;
    }


    /** Return the value of the type property in the first annotation. */

    public String getType(List<EmbeddedAnnotation> annotations) {
        for (EmbeddedAnnotation ann: annotations) {
            String type=getType(ann);
            if (type!=null) return type;
        }
        return null;
    }



    /** Return the value of the value property. */

    public List<Object> getValues(List<EmbeddedAnnotation> annotations) {
        List<Object> res=new LinkedList();
        for (EmbeddedAnnotation ann: annotations) {
            Object value=getValue(ann);
            if (value!=null) res.add(value);
        }
        return res;
    }



    /** Return the value of the label property. */
    public List<String> getLabels(List<EmbeddedAnnotation> annotations) {
        List<String> res=new LinkedList();
        for (EmbeddedAnnotation ann: annotations) {
            String label=getLabel(ann);
            if (label!=null) res.add(label);
        }
        return res;
    }


    /** Return the value of the type property. */

    public List<String> getTypes(List<EmbeddedAnnotation> annotations) {
        List<String> res=new LinkedList();
        for (EmbeddedAnnotation ann: annotations) {
            String type=getType(ann);
            if (type!=null) res.add(type);
        }
        return res;
    }




    /** Generic accessor for annotable entities. */
    public String getLabel(Annotable annotable) {
        return getLabel(annotable.getAnnotation());
    }

    /** Generic accessor for annotable entities. */
    public String getType(Annotable annotable) {
        return getType(annotable.getAnnotation());
    }


    public EmbeddedAnnotation newType(String type) {
        EmbeddedAnnotation res=of.createEmbeddedAnnotation();
	res.getProperty().add(newProperty(TYPE_PROPERTY,type));
        return res;
    }

    public void addValue(Entity annotable, Object value, String encoding) {
        addAnnotation(annotable,newValue(value,encoding));
    }

    public void addAnnotation(Annotable annotable, EmbeddedAnnotation ann) {
        //annotable.getAnnotation().add(ann);
	//	annotable.getAnnotation().add(ann);
	EmbeddedAnnotation ea=annotable.getAnnotation();
	if (ea==null) {
	    annotable.setAnnotation(ann);
	} else {
	    ea.getProperty().addAll(ann.getProperty());
	}
    }


    public void expandAnnotation(EmbeddedAnnotation ann) {

    }

    // deprecated now
    public EmbeddedAnnotation compactAnnotation(EmbeddedAnnotation ann) {

        return ann;
    }

    public XMLGregorianCalendar
           newXMLGregorianCalendar(GregorianCalendar gc) {
                 return dataFactory.newXMLGregorianCalendar(gc);
            }


	public OTime newOTime (OTime time) {
        return newOTime(time.getNoEarlierThan(),
                        time.getNoLaterThan(),
                        time.getExactlyAt());
    }


	public OTime newOTime (XMLGregorianCalendar point1,
                           XMLGregorianCalendar point2,
                           XMLGregorianCalendar point3) {
        OTime time = of.createOTime();
        time.setNoEarlierThan (point1);
        time.setNoLaterThan (point2);
        time.setExactlyAt (point3);
        return time;
    }

	public OTime newOTime (XMLGregorianCalendar point1,
                           XMLGregorianCalendar point2) {
        OTime time = of.createOTime();
        time.setNoEarlierThan (point1);
        time.setNoLaterThan (point2);
        return time;
    }

	public OTime newOTime (XMLGregorianCalendar point) {
        OTime time = of.createOTime();
        time.setExactlyAt (point);
        return time;
    }

	public OTime newOTime (String value1, String value2) {
        XMLGregorianCalendar point1 = dataFactory.newXMLGregorianCalendar (value1);
        XMLGregorianCalendar point2 = dataFactory.newXMLGregorianCalendar (value2);
        return newOTime(point1,point2);
    }

	public OTime newOTime (Date date1,
                           Date date2) {
        GregorianCalendar gc1=new GregorianCalendar();
        gc1.setTime(date1);
        GregorianCalendar gc2=new GregorianCalendar();
        gc2.setTime(date2);
        return newOTime(newXMLGregorianCalendar(gc1),
                        newXMLGregorianCalendar(gc2));
    }

	public OTime newOTime (Date date) {
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(date);
        return newOTime(newXMLGregorianCalendar(gc));
    }

	public OTime newInstantaneousTime (XMLGregorianCalendar point) {
        return newOTime(point);
    }

	public OTime newInstantaneousTime (String value) {
        XMLGregorianCalendar point = dataFactory.newXMLGregorianCalendar (value);
        return newOTime(point);
    }


	public OTime newInstantaneousTime (Date date) {
        GregorianCalendar gc=new GregorianCalendar();
        gc.setTime(date);
        XMLGregorianCalendar xgc=newXMLGregorianCalendar(gc);
        return newOTime(xgc);
    }

	public OTime newInstantaneousTimeNow () {
        return newInstantaneousTime(new Date());
    }


    public boolean compactId=false;
    
    public void setIdForCompactAnnotation(EmbeddedAnnotation ann, String id) {
        if (compactId) ann.setId(id);
    }


    public void addAnnotation(Annotable annotable, List<EmbeddedAnnotation> anns) {
        for (EmbeddedAnnotation ann: anns) {        
            addAnnotation(annotable,ann);
        }
    }



    public void addCompactAnnotation(Annotable annotable, List<EmbeddedAnnotation> anns) {
	addAnnotation(annotable,anns);
    }



    public Overlaps newOverlaps(Collection<Account> accounts) {
        Overlaps res=of.createOverlaps();
        LinkedList ll=new LinkedList();
        int i=0;
        for (Account acc: accounts) {
            if (i==2) break;
            ll.add(newAccountRef(acc));
            i++;
        }
        res.getAccount().addAll(ll);
        return res;
    }
        
    public Overlaps newOverlaps(AccountRef aid1,AccountRef aid2) {
        Overlaps res=of.createOverlaps();
        res.getAccount().add(aid1);
        res.getAccount().add(aid2);
        return res;
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

    public Role newRole(String value) {
        return newRole(autoGenerateId(roleIdPrefix),value);
    }

    public Role newRole(Role role) {
        return newRole(autoGenerateId(roleIdPrefix,role.getId()),role.getValue());
    }

    public Role newRole(String id, String value) {
        Role res=of.createRole();
        res.setId(id);
        res.setValue(value);
        return res;
    }

    public Entity newEntity(Entity a) {
        LinkedList<Account> ll=new LinkedList();
        for (AccountRef acc: a.getAccount()) {
            ll.add(newAccount((Account)acc.getRef()));
        }
        Entity res=newEntity(a.getId(),ll);
        addNewAnnotations(res,a.getAnnotation());
        return res;
    }
    public Activity newActivity(Activity a) {
        LinkedList<Account> ll=new LinkedList();
        for (AccountRef acc: a.getAccount()) {
            ll.add(newAccount((Account)acc.getRef()));
        }
        Activity res=newActivity(a.getId(),ll);
        addNewAnnotations(res,a.getAnnotation());
        return res;
    }
    public Agent newAgent(Agent a) {
        LinkedList<Account> ll=new LinkedList();
        for (AccountRef acc: a.getAccount()) {
            ll.add(newAccount((Account)acc.getRef()));
        }
        Agent res=newAgent(a.getId(),ll);
        addNewAnnotations(res,a.getAnnotation());
        return res;
    }

    public Account newAccount(Account acc) {
        Account res=newAccount(acc.getId());
        addNewAnnotations(res,acc.getAnnotation());
        return res;
    }

    public void addNewAnnotations(Annotable res,
                                  org.openprovenance.prov.xml.EmbeddedAnnotation ea) {
	if (ea.getId()!=null) 
            addAnnotation(res,newEmbeddedAnnotation(ea.getId(),
                                                    ea.getProperty(),
                                                    ea.getAccount(),
                                                    null));
    }


    public Entity newEntity(String id,
                                Collection<Account> accounts) {
        return newEntity(id,accounts,null);
    }

    public Entity newEntity(String id,
                                Collection<Account> accounts,
                                String label) {
        Entity res=of.createEntity();
        res.setId(id);
        addAccounts(res,accounts,null);
        if (label!=null) addAnnotation(res,newLabel(label));
        return res;
    }

    public void addAccounts(HasAccounts element, Collection<Account> accounts, Object ignoreForErasure) {
        if ((accounts !=null) && (accounts.size()!=0)) {
            LinkedList ll=new LinkedList();
            for (Account acc: accounts) {
                ll.add(newAccountRef(acc));
            }
            addAccounts(element,ll);
        }
    }
    public void addAccounts(HasAccounts element, Collection<AccountRef> accounts) {
        if ((accounts !=null) && (accounts.size()!=0)) {
            element.getAccount().addAll(accounts);
        }
    }


    public Used newUsed(String id,
                        ActivityRef pid,
                        Role role,
                        EntityRef aid,
                        Collection<AccountRef> accounts) {
        Used res=of.createUsed();
        res.setId(autoGenerateId(usedIdPrefix,id));
        res.setEffect(pid);
        res.setRole(role);
        res.setCause(aid);
        addAccounts(res,accounts);
        return res;
    }



    public UsedStar newUsedStar(ActivityRef pid,
                                EntityRef aid,
                                Collection<AccountRef> accounts) {
        UsedStar res=of.createUsedStar();
        res.setEffect(pid);
        res.setCause(aid);
        addAccounts(res,accounts);
        return res;
    }

    public Used newUsed(Activity p,
                        Role role,
                        Entity a,
                        Collection<Account> accounts) {
        Used res=newUsed(null,p,role,a,accounts);
        return res;
    }

    public Used newUsed(String id,
                        Activity p,
                        Role role,
                        Entity a,
                        Collection<Account> accounts) {
        ActivityRef pid=newActivityRef(p);
        EntityRef aid=newEntityRef(a);
        LinkedList ll=new LinkedList();
        if (accounts!=null) {
            for (Account acc: accounts) {
                ll.add(newAccountRef(acc));
            }
        }
        return newUsed(id,pid,role,aid,ll);
    }
    public UsedStar newUsedStar(Activity p,
                                Entity a,
                                Collection<Account> accounts) {
        ActivityRef pid=newActivityRef(p);
        EntityRef aid=newEntityRef(a);
        LinkedList ll=new LinkedList();
        if (accounts!=null) {
            for (Account acc: accounts) {
                ll.add(newAccountRef(acc));
            }
        }
        return newUsedStar(pid,aid,ll);
    }


    public Used newUsed(String id,
                        Activity p,
                        Role role,
                        Entity a,
                        String type,
                        Collection<Account> accounts) {
        Used res=newUsed(id,p,role,a,accounts);
        addAnnotation(res,newType(type));
        return res;
    }

    public Used newUsed(Used u) {
        Used u1=newUsed(u.getId(),
                        u.getEffect(),
                        u.getRole(),
                        u.getCause(),
                        u.getAccount());
        addAnnotation(u1,u.getAnnotation());
        return u1;
    }

    public WasControlledBy newWasControlledBy(WasControlledBy c) {
        WasControlledBy wcb=newWasControlledBy(c.getEffect(),
                                               c.getRole(),
                                               c.getCause(),
                                               c.getAccount());
        wcb.setId(c.getId());
        addAnnotation(wcb,c.getAnnotation());
        return wcb;
    }

    public WasGeneratedBy newWasGeneratedBy(WasGeneratedBy g) {
        WasGeneratedBy wgb=newWasGeneratedBy(g.getId(),
                                             g.getEffect(),
                                             g.getRole(),
                                             g.getCause(),
                                             g.getAccount());
        wgb.setId(g.getId());
        addAnnotation(wgb,g.getAnnotation());
        return wgb;
    }

    public WasDerivedFrom newWasDerivedFrom(WasDerivedFrom d) {
        WasDerivedFrom wdf=newWasDerivedFrom(d.getId(),
                                             d.getEffect(),
                                             d.getCause(),
                                             d.getAccount());
	addAnnotation(wdf,d.getAnnotation());
        return wdf;
    }

    public WasTriggeredBy newWasTriggeredBy(WasTriggeredBy d) {
        WasTriggeredBy wtb=newWasTriggeredBy(d.getId(),
                                             d.getEffect(),
                                             d.getCause(),
                                             d.getAccount());
        wtb.setId(d.getId());
        addAnnotation(wtb,d.getAnnotation());
        return wtb;
    }



    public WasGeneratedBy newWasGeneratedBy(String id,
                                            EntityRef aid,
                                            Role role,
                                            ActivityRef pid,
                                            Collection<AccountRef> accounts) {
        WasGeneratedBy res=of.createWasGeneratedBy();
        res.setId(autoGenerateId(wasGenerateByIdPrefix,id));
        res.setCause(pid);
        res.setRole(role);
        res.setEffect(aid);
        addAccounts(res,accounts);
        return res;
    }

    public WasGeneratedByStar newWasGeneratedByStar(EntityRef aid,
                                                    ActivityRef pid,
                                                    Collection<AccountRef> accounts) {
        WasGeneratedByStar res=of.createWasGeneratedByStar();
        res.setCause(pid);
        res.setEffect(aid);
        addAccounts(res,accounts);
        return res;
    }
    public WasGeneratedBy newWasGeneratedBy(Entity a,
                                            Role role,
                                            Activity p,
                                            Collection<Account> accounts) {
        return newWasGeneratedBy(null,a,role,p,accounts);
    }

    public WasGeneratedBy newWasGeneratedBy(String id,
                                            Entity a,
                                            Role role,
                                            Activity p,
                                            Collection<Account> accounts) {
        EntityRef aid=newEntityRef(a);
        ActivityRef pid=newActivityRef(p);
        LinkedList ll=new LinkedList();
        for (Account acc: accounts) {
            ll.add(newAccountRef(acc));
        }
        return  newWasGeneratedBy(id,aid,role,pid,ll);
    }


    public WasGeneratedByStar newWasGeneratedByStar(Entity a,
                                                    Activity p,
                                                    Collection<Account> accounts) {
        EntityRef aid=newEntityRef(a);
        ActivityRef pid=newActivityRef(p);
        LinkedList ll=new LinkedList();
        for (Account acc: accounts) {
            ll.add(newAccountRef(acc));
        }
        return  newWasGeneratedByStar(aid,pid,ll);
    }



    public WasGeneratedBy newWasGeneratedBy(String id,
                                            Entity a,
                                            Role role,
                                            Activity p,
                                            String type,
                                            Collection<Account> accounts) {
        WasGeneratedBy wgb=newWasGeneratedBy(id,a,role,p,accounts);
        addAnnotation(wgb,newType(type));
        return wgb;
    }

    public WasControlledBy newWasControlledBy(ActivityRef pid,
                                              Role role,
                                              AgentRef agid,
                                              Collection<AccountRef> accounts) {
        return newWasControlledBy(null,pid,role,agid,accounts);
    }
    
    public WasControlledBy newWasControlledBy(String id,
                                              ActivityRef pid,
                                              Role role,
                                              AgentRef agid,
                                              Collection<AccountRef> accounts) {
        WasControlledBy res=of.createWasControlledBy();
        res.setId(autoGenerateId(wasControlledByIdPrefix,id));
        res.setEffect(pid);
        res.setRole(role);
        res.setCause(agid);
        addAccounts(res,accounts);
        return res;
    }


    public WasControlledBy newWasControlledBy(Activity p,
                                              Role role,
                                              Agent ag,
                                              Collection<Account> accounts) {
        return newWasControlledBy(null,p,role,ag,accounts);
    }

    public WasControlledBy newWasControlledBy(String id,
                                              Activity p,
                                              Role role,
                                              Agent ag,
                                              Collection<Account> accounts) {
        AgentRef agid=newAgentRef(ag);
        ActivityRef pid=newActivityRef(p);
        LinkedList ll=new LinkedList();
        for (Account acc: accounts) {
            ll.add(newAccountRef(acc));
        }
        return  newWasControlledBy(id,pid,role,agid,ll);
    }

    public WasControlledBy newWasControlledBy(String id,
                                              Activity p,
                                              Role role,
                                              Agent ag,
                                              String type,
                                              Collection<Account> accounts) {
        WasControlledBy wcb=newWasControlledBy(id,p,role,ag,accounts);
        addAnnotation(wcb,newType(type));
        return wcb;
    }


    public WasDerivedFrom newWasDerivedFrom(String id,
                                            EntityRef aid1,
                                            EntityRef aid2,
                                            Collection<AccountRef> accounts) {
        WasDerivedFrom res=of.createWasDerivedFrom();
        res.setId(autoGenerateId(wasDerivedFromIdPrefix,id));
        res.setCause(aid2);
        res.setEffect(aid1);
        addAccounts(res,accounts);
        return res;
    }

    public WasDerivedFromStar newWasDerivedFromStar(EntityRef aid1,
                                                    EntityRef aid2,
                                                    Collection<AccountRef> accounts) {
        WasDerivedFromStar res=of.createWasDerivedFromStar();
        res.setCause(aid2);
        res.setEffect(aid1);
        addAccounts(res,accounts);
        return res;
    }

    public WasDerivedFrom newWasDerivedFrom(Entity a1,
                                            Entity a2,
                                            Collection<Account> accounts) {
        return newWasDerivedFrom(null,a1,a2,accounts);
    }

    public WasDerivedFrom newWasDerivedFrom(String id,
                                            Entity a1,
                                            Entity a2,
                                            Collection<Account> accounts) {
        EntityRef aid1=newEntityRef(a1);
        EntityRef aid2=newEntityRef(a2);
        LinkedList ll=new LinkedList();
        for (Account acc: accounts) {
            ll.add(newAccountRef(acc));
        }
        return  newWasDerivedFrom(id,aid1,aid2,ll);
    }

    public WasDerivedFromStar newWasDerivedFromStar(Entity a1,
                                                    Entity a2,
                                                    Collection<Account> accounts) {
        EntityRef aid1=newEntityRef(a1);
        EntityRef aid2=newEntityRef(a2);
        LinkedList ll=new LinkedList();
        for (Account acc: accounts) {
            ll.add(newAccountRef(acc));
        }
        return  newWasDerivedFromStar(aid1,aid2,ll);
    }


    public WasDerivedFrom newWasDerivedFrom(String id,
                                            Entity a1,
                                            Entity a2,
                                            String type,
                                            Collection<Account> accounts) {
        WasDerivedFrom wdf=newWasDerivedFrom(id,a1,a2,accounts);
        addAnnotation(wdf,newType(type));
        return wdf;
    }



    public WasTriggeredBy newWasTriggeredBy(String id,
                                            ActivityRef pid1,
                                            ActivityRef pid2,
                                            Collection<AccountRef> accounts) {
        WasTriggeredBy res=of.createWasTriggeredBy();
        res.setId(autoGenerateId(wasTriggeredByIdPrefix,id));
        res.setEffect(pid1);
        res.setCause(pid2);
        addAccounts(res,accounts);
        return res;
    }

    public WasTriggeredByStar newWasTriggeredByStar(ActivityRef pid1,
                                                    ActivityRef pid2,
                                                    Collection<AccountRef> accounts) {
        WasTriggeredByStar res=of.createWasTriggeredByStar();
        res.setEffect(pid1);
        res.setCause(pid2);
        addAccounts(res,accounts);
        return res;
    }

    public WasTriggeredBy newWasTriggeredBy(Activity p1,
                                            Activity p2,
                                            Collection<Account> accounts) {
        return newWasTriggeredBy(null,p1,p2,accounts);
    }

    public WasTriggeredBy newWasTriggeredBy(String id,
                                            Activity p1,
                                            Activity p2,
                                            Collection<Account> accounts) {
        ActivityRef pid1=newActivityRef(p1);
        ActivityRef pid2=newActivityRef(p2);
        LinkedList<AccountRef> ll=new LinkedList();
        for (Account acc: accounts) {
            ll.add(newAccountRef(acc));
        }
        return  newWasTriggeredBy(id,pid1,pid2,ll);
    }

    public WasTriggeredByStar newWasTriggeredByStar(Activity p1,
                                                    Activity p2,
                                                    Collection<Account> accounts) {
        ActivityRef pid1=newActivityRef(p1);
        ActivityRef pid2=newActivityRef(p2);
        LinkedList<AccountRef> ll=new LinkedList();
        for (Account acc: accounts) {
            ll.add(newAccountRef(acc));
        }
        return  newWasTriggeredByStar(pid1,pid2,ll);
    }

    public WasTriggeredBy newWasTriggeredBy(String id,
                                            Activity p1,
                                            Activity p2,
                                            String type,
                                            Collection<Account> accounts) {
        WasTriggeredBy wtb=newWasTriggeredBy(p1,p2,accounts);
        wtb.setId(id);
        addAnnotation(wtb,newType(type));
        return wtb;
    }


    /*
    public Attributes newAttributes(String property,
                                    Object value) {
        Attributes res=of.createAttributes();
        addAttribute(res,newAttribute(property,value));
        return res;
    }
    

    public Attribute newAttributeORIGINAL(String property,
                                Object value) {
        Attribute res=of.createAttribute();
        
        res.setName(property);
        res.setValue(value);
        return res;
    }


    public Attribute newAttribute(String property,
                                  Object value) {
        Attribute res=of.createAttribute();
        //QName q1=new QName(property, "test");


        res.getAny().add(value);
        return res;
    }

    */

    public void addAttribute(Activity a,
                             String namespace,
                             String prefix,
                             String localName,                                  
                             String value) {
        if (a.getAttributes()==null) {
            a.setAttributes(of.createAttributes());
        }
        addAttribute(a.getAttributes(),
                     namespace,
                     prefix,
                     localName,
                     value);
    }


    public void addAttribute(Attributes attrs,
                             String namespace,
                                  String prefix,
                                  String localName,                                  
                                  String value) {

        Document doc=oFactory.builder.newDocument();
        Element el=doc.createElementNS(namespace,prefix + ":" + localName);
        el.appendChild(doc.createTextNode(value));
        doc.appendChild(el);

        attrs.getAny().add(el);
    }

    /*
    public void addAttribute(Attributes attrs, Attribute p) {
        attrs.getAttribute().add(p);
    }
    */


    public EmbeddedAnnotation newEmbeddedAnnotation(String id,
                                                    String property,
                                                    Object value,
                                                    Collection<Account> accs) {
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newEmbeddedAnnotation(id,property,value,ll,null);
    }

    public Annotation newAnnotation(String id,
                                    Entity a,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        EntityRef aid=newEntityRef(a);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,aid,property,value,ll);
    }
    public Annotation newAnnotation(String id,
                                    Activity p,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        ActivityRef pid=newActivityRef(p);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,pid,property,value,ll);
    }

    public Annotation newAnnotation(String id,
                                    Annotation a,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        AnnotationRef aid=newAnnotationRef(a);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,aid,property,value,ll);
    }

    public Annotation newAnnotation(String id,
                                    WasDerivedFrom edge,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        DependencyRef cid=newDependencyRef(edge);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,cid,property,value,ll);
    }
    public Annotation newAnnotation(String id,
                                    Used edge,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        DependencyRef cid=newDependencyRef(edge);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,cid,property,value,ll);
    }
    public Annotation newAnnotation(String id,
                                    WasGeneratedBy edge,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        DependencyRef cid=newDependencyRef(edge);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,cid,property,value,ll);
    }
    public Annotation newAnnotation(String id,
                                    WasControlledBy edge,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        DependencyRef cid=newDependencyRef(edge);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,cid,property,value,ll);
    }
    public Annotation newAnnotation(String id,
                                    WasTriggeredBy edge,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        DependencyRef cid=newDependencyRef(edge);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,cid,property,value,ll);
    }

    public Annotation newAnnotation(String id,
                                    Role role,
                                    String property,
                                    Object value,
                                    Collection<Account> accs) {
        RoleRef rid=newRoleRef(role);
        LinkedList<AccountRef> ll=new LinkedList();
        if (accs!=null) {
            for (Account acc: accs) {
                ll.add(newAccountRef(acc));
            }
        }
        return newAnnotation(id,rid,property,value,ll);
    }

    public Property newProperty(String property,
                                Object value) {
        Property res=of.createProperty();
        res.setAttribute(property);
        res.setValue(value);
        return res;
    }

    public Property newProperty(Property property) {
        return newProperty(property.getAttribute(),property.getValue());
    }


    public void addProperty(Annotation ann, Property p) {
        ann.getProperty().add(p);
    }

    public void addProperty(Annotation ann, List<Property> p) {
        ann.getProperty().addAll(p);
    }

    public void addProperty(EmbeddedAnnotation ann, Property p) {
        ann.getProperty().add(p);
    }

    public void addProperty(EmbeddedAnnotation ann, List<Property> p) {
        ann.getProperty().addAll(p);
    }

    public Annotation newAnnotation(String id,
                                    Ref ref,
                                    String property,
                                    Object value,
                                    Collection<AccountRef> accs) {
        Annotation res=of.createAnnotation();
        res.setId(id);
        res.setLocalSubject(ref.getRef());
        addProperty(res,newProperty(property,value));
        addAccounts(res,accs);
        return res;
    }

    public Annotation newAnnotation(String id,
                                    Object o,
                                    List<Property> properties,
                                    Collection<AccountRef> accs) {
        Annotation res=of.createAnnotation();
        res.setId(id);
        res.setLocalSubject(o);
        for (Property property: properties) {
            addProperty(res,property);
        }
        addAccounts(res,accs);
        return res;
    }

    public Annotation newAnnotation(Annotation ann) {
        Annotation res=newAnnotation(ann.getId(),
                                     ann.getLocalSubject(),
                                     ann.getProperty(),
                                     ann.getAccount());
        return res;
    }

    public EmbeddedAnnotation newEmbeddedAnnotation(String id,
                                                    String property,
                                                    Object value,
                                                    Collection<AccountRef> accs,
                                                    Object dummyParameterForAvoidingSameErasure) {
        EmbeddedAnnotation res=of.createEmbeddedAnnotation();
        res.setId(id);
        addProperty(res,newProperty(property,value));
        addAccounts(res,accs);
        return res;
    }
    public EmbeddedAnnotation newEmbeddedAnnotation(String id,
                                                    List<Property> properties,
                                                    Collection<AccountRef> accs,
                                                    Object dummyParameterForAvoidingSameErasure) {
        EmbeddedAnnotation res=of.createEmbeddedAnnotation();
        res.setId(id);
        if (properties!=null) {
            addProperty(res,properties);
        }
        addAccounts(res,accs);
        return res;
    }


    public Container newContainer(Collection<Account> accs,
                                Collection<Overlaps> ops,
                                Collection<Activity> ps,
                                Collection<Entity> as,
                                Collection<Agent> ags,
                                Collection<Object> lks) {
        return newContainer(null,accs,ops,ps,as,ags,lks,null);
    }

    public Container newContainer(Collection<Account> accs,
                                Collection<Overlaps> ops,
                                Collection<Activity> ps,
                                Collection<Entity> as,
                                Collection<Agent> ags,
                                Collection<Object> lks,
                                Collection<Annotation> anns) {
        return newContainer(null,accs,ops,ps,as,ags,lks,anns);
    }

    public Container newContainer(String id,
                                Collection<Account> accs,
                                Collection<Overlaps> ops,
                                Collection<Activity> ps,
                                Collection<Entity> as,
                                Collection<Agent> ags,
                                Collection<Object> lks,
                                Collection<Annotation> anns)
    {
        Container res=of.createContainer();
        res.setId(autoGenerateId(containerIdPrefix,id));
        if (accs!=null) {
            Accounts aaccs=of.createAccounts();
            aaccs.getAccount().addAll(accs);
            if (ops!=null) 
                aaccs.getOverlaps().addAll(ops);
            res.setAccounts(aaccs);
            
        }
        if (ps!=null) {
            Activities pps=of.createActivities();
            pps.getActivity().addAll(ps);
            res.setActivities(pps);
        }
        if (as!=null) {
            Entities aas=of.createEntities();
            aas.getEntity().addAll(as);
            res.setEntities(aas);
        }
        if (ags!=null) {
            Agents aags=of.createAgents();
            aags.getAgent().addAll(ags);
            res.setAgents(aags);
        }
        if (lks!=null) {
            Dependencies ccls=of.createDependencies();
            ccls.getUsedOrWasGeneratedByOrWasTriggeredBy().addAll(lks);
            res.setDependencies(ccls);
        }

        if (anns!=null) {
            Annotations l=of.createAnnotations();
            l.getAnnotation().addAll(anns);
            res.setAnnotations(l);
        }
        return res;
    }

    public Container newContainer(Collection<Account> accs,
                                Overlaps [] ovs,
                                Activity [] ps,
                                Entity [] as,
                                Agent [] ags,
                                Object [] lks) 
    {

        return newContainer(accs,
                           ((ovs==null) ? null : Arrays.asList(ovs)),
                           ((ps==null) ? null : Arrays.asList(ps)),
                           ((as==null) ? null : Arrays.asList(as)),
                           ((ags==null) ? null : Arrays.asList(ags)),
                           ((lks==null) ? null : Arrays.asList(lks)));
    }
    public Container newContainer(Collection<Account> accs,
                                Overlaps [] ovs,
                                Activity [] ps,
                                Entity [] as,
                                Agent [] ags,
                                Object [] lks,
                                Annotation [] anns) {
        return newContainer(null,accs,ovs,ps,as,ags,lks,anns);
    }

    public Container newContainer(String id,
                                Collection<Account> accs,
                                Overlaps [] ovs,
                                Activity [] ps,
                                Entity [] as,
                                Agent [] ags,
                                Object [] lks,
                                Annotation [] anns) 
    {

        return newContainer(id,
                           accs,
                           ((ovs==null) ? null : Arrays.asList(ovs)),
                           ((ps==null) ? null : Arrays.asList(ps)),
                           ((as==null) ? null : Arrays.asList(as)),
                           ((ags==null) ? null : Arrays.asList(ags)),
                           ((lks==null) ? null : Arrays.asList(lks)),
                           ((anns==null) ? null : Arrays.asList(anns)));
    }

    public Container newContainer(Accounts accs,
                                Activities ps,
                                Entities as,
                                Agents ags,
                                Dependencies lks)
    {
        Container res=of.createContainer();
        //res.setId(autoGenerateId(containerIdPrefix));
        res.setAccounts(accs);
        res.setActivities(ps);
        res.setEntities(as);
        res.setAgents(ags);
        res.setDependencies(lks);
        return res;
    }

    public Container newContainer(Accounts accs,
                                Activities ps,
                                Entities as,
                                Agents ags,
                                Dependencies lks,
                                Annotations anns)
    {
        Container res=of.createContainer();
        //res.setId(autoGenerateId(containerIdPrefix));
        res.setAccounts(accs);
        res.setActivities(ps);
        res.setEntities(as);
        res.setAgents(ags);
        res.setDependencies(lks);
        res.setAnnotations(anns);
        return res;
    }

    public Container newContainer(Container graph) {
        return newContainer(graph.getAccounts(),
                           graph.getActivities(),
                           graph.getEntities(),
                           graph.getAgents(),
                           graph.getDependencies(),
                           graph.getAnnotations());
    }


    public Accounts newAccounts(Collection<Account> accs,
                                Collection<Overlaps> ovlps) {
        Accounts res=of.createAccounts();
        if (accs!=null) {
            res.getAccount().addAll(accs);
        }
        if (ovlps!=null) {
            res.getOverlaps().addAll(ovlps);
        }
        return res;
    }

//     public Encoding newEncoding(String encoding) {
//         Encoding res=of.createEncoding();
//         res.setValue(encoding);
//         return res;
//     }
//     public String getEncoding(EmbeddedAnnotation annotation) {
//         if (annotation instanceof Encoding) {
//             Encoding encoding=(Encoding) annotation;
//             return encoding.getValue();
//         } else {
//             for (Property prop: annotation.getProperty()) {
//                 if (prop.equals(ENCODING_PROPERTY)) {
//                     return (String) prop.getValue();
//                 }
//             }
//             return null;
//         }
//     }

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

