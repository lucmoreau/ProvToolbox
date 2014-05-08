package org.openprovenance.prov.template;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementAction;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;

import static org.openprovenance.prov.template.Expand.TMPL_NS;
import static org.openprovenance.prov.template.Expand.TMPL_PREFIX;

public class ExpandAction implements StatementAction {
    
    public static final String UUID_PREFIX = "uuid";
    public static final String URN_UUID_NS = "urn:uuid:";
    final private ProvFactory pf;
    final private Expand expand;
    final private Hashtable<QualifiedName, QualifiedName> env;
    final private ProvUtilities u;
    final private List<StatementOrBundle> ll=new LinkedList<StatementOrBundle>();
    final private List<Integer> index;
    final private Bindings bindings;
    final private Groupings grp1;
    final private Hashtable<QualifiedName, List<TypedValue>> env2;

    public ExpandAction(ProvFactory pf, ProvUtilities u, Expand expand, Hashtable<QualifiedName, QualifiedName> env, Hashtable<QualifiedName, List<TypedValue>> env2, List<Integer> index, Bindings bindings1, Groupings grp1) {
	this.pf=pf;
	this.expand=expand;
	this.env=env;
	this.u=u;
	this.index=index;
	this.bindings=bindings1;
	this.grp1=grp1;
	this.env2=env2;
    }

    @Override
    public void doAction(Activity s) {
	Activity res=pf.newActivity(s.getId(), s.getStartTime(), s.getEndTime(), null);
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	boolean updated2=expandAttributes(s,res);
	boolean updated=updated1 || updated2;
	ll.add(res);
	if (updated) addOrderAttribute(res);
    }

    @Override
    public void doAction(Used s) {
	Used res=pf.newUsed(s.getId(),s.getActivity(), s.getEntity());
	res.setTime(s.getTime());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName en=res.getActivity();
	boolean updated2=setExpand(res, en, 1);	
	QualifiedName ag=res.getEntity();
	boolean updated3=setExpand(res, ag, 2);	
	boolean updated4=expandAttributes(s,res);

	boolean updated=updated1 || updated2 || updated3 || updated4;
	ll.add(res);
	if (updated) addOrderAttribute(res);		
    }

    @Override
    public void doAction(WasStartedBy s) {
	WasStartedBy res=pf.newWasStartedBy(s.getId(),s.getActivity(), s.getTrigger(), s.getStarter());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName en=res.getActivity();
	boolean updated2=setExpand(res, en, 1);	
	QualifiedName ag=res.getTrigger();
	boolean updated3=setExpand(res, ag, 2);	
	QualifiedName st=res.getStarter();
	boolean updated4=setExpand(res,st,3);	
	boolean updated5=expandAttributes(s,res);

	boolean updated=updated1 || updated2 || updated3 || updated4 || updated5;
	ll.add(res);
	if (updated) addOrderAttribute(res);			
    }

    @Override
    public void doAction(Agent e) {
	Agent res=pf.newAgent(e.getId());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	boolean updated2=expandAttributes(e,res);
	boolean updated=updated1 || updated2;
	ll.add(res);
	if (updated) addOrderAttribute(res);	
    }

    @Override
    public void doAction(AlternateOf s) {
	AlternateOf res=pf.newAlternateOf(s.getAlternate1(), s.getAlternate2());
	
	QualifiedName alt1=res.getAlternate1();
	boolean updated0=setExpand(res, alt1, 0);	
	QualifiedName alt2=res.getAlternate2();
	boolean updated1=setExpand(res, alt2, 1);	

	boolean updated=updated0 || updated1;
	ll.add(res);
	//if (updated) addOrderAttribute(res);		
    }

    @Override
    public void doAction(WasAssociatedWith s) {
	WasAssociatedWith res=pf.newWasAssociatedWith(s.getId(),s.getActivity(), s.getAgent());
	res.setPlan(s.getPlan());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName en=res.getActivity();
	boolean updated2=setExpand(res, en, 1);	
	QualifiedName ag=res.getAgent();
	boolean updated3=setExpand(res, ag, 2);	
	QualifiedName pl=res.getPlan();
	boolean updated4=setExpand(res, pl, 3);	
	boolean updated5=expandAttributes(s,res);
	boolean updated=updated1 || updated2 || updated3 || updated4|| updated5;
	ll.add(res);
	if (updated) addOrderAttribute(res);	
    }

    @Override
    public void doAction(WasAttributedTo s) {
	WasAttributedTo res=pf.newWasAttributedTo(s.getId(),s.getEntity(), s.getAgent());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName en=res.getEntity();
	boolean updated2=setExpand(res, en, 1);	
	QualifiedName ag=res.getAgent();
	boolean updated3=setExpand(res, ag, 2);	
	boolean updated4=expandAttributes(s,res);
	boolean updated=updated1 || updated2 || updated3 || updated4;
	ll.add(res);
	if (updated) addOrderAttribute(res);	
    }

    @Override
    public void doAction(WasInfluencedBy s) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void doAction(ActedOnBehalfOf s) {
	ActedOnBehalfOf res=pf.newActedOnBehalfOf(s.getId(),s.getDelegate(), s.getResponsible(), s.getActivity());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName del=res.getDelegate();
	boolean updated2=setExpand(res, del, 1);	
	QualifiedName resp=res.getResponsible();
	boolean updated3=setExpand(res, resp, 2);
	QualifiedName act=res.getActivity();
	boolean updated4=setExpand(res, act, 3);
	
	
	boolean updated5=expandAttributes(s,res);
	boolean updated=updated1 || updated2 || updated3 || updated4|| updated5;
	ll.add(res);
	if (updated) addOrderAttribute(res);		
    }

    @Override
    public void doAction(WasDerivedFrom s) {
	WasDerivedFrom res=pf.newWasDerivedFrom(s.getId(), s.getGeneratedEntity(), s.getUsedEntity());
	res.setActivity(s.getActivity());
	res.setUsage(s.getUsage());
	res.setGeneration(s.getGeneration());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName en2=res.getGeneratedEntity();
	boolean updated2=setExpand(res, en2, 1);	
	QualifiedName en1=res.getUsedEntity();
	boolean updated3=setExpand(res, en1, 2);	
	QualifiedName act=res.getActivity();
	boolean updated4=setExpand(res,act,3);
	QualifiedName gen=res.getGeneration();
	boolean updated5=setExpand(res,gen,4);
	QualifiedName use=res.getUsage();
	boolean updated6=setExpand(res,use,5);
	
	boolean updated7=expandAttributes(s,res);

	boolean updated=updated1 || updated2 || updated3 || updated4 || updated5|| updated6|| updated7;
	ll.add(res);
	if (updated) addOrderAttribute(res);			
	
	
    }

    @Override
    public void doAction(DictionaryMembership s) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void doAction(DerivedByRemovalFrom s) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void doAction(WasEndedBy s) {
	WasEndedBy res=pf.newWasEndedBy(s.getId(),s.getActivity(), s.getTrigger(), s.getEnder());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName en=res.getActivity();
	boolean updated2=setExpand(res, en, 1);	
	QualifiedName ag=res.getTrigger();
	boolean updated3=setExpand(res, ag, 2);	
	QualifiedName st=res.getEnder();
	boolean updated4=setExpand(res,st,3);	
	boolean updated5=expandAttributes(s,res);

	boolean updated=updated1 || updated2 || updated3 || updated4 || updated5;
	ll.add(res);
	if (updated) addOrderAttribute(res);		
    }

    @Override
    public void doAction(Entity e) {
	Entity res=pf.newEntity(e.getId());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	boolean updated2=expandAttributes(e,res);
	boolean updated=updated1 || updated2;
	ll.add(res);
	if (updated) addOrderAttribute(res);
    }

    public boolean expandAttributes(Statement srcStatement, Statement dstStatement) {
        boolean found=false;
        if (dstStatement instanceof HasOther) {

            Collection<Attribute> attributes=pf.getAttributes(srcStatement);
            Collection<Attribute> dstAttributes=new LinkedList<Attribute>();
            for (Attribute attribute: attributes) {
                if (pf.getName().XSD_QNAME.equals(attribute.getType())) {
                    Object o=attribute.getValue();
                    if (o instanceof QualifiedName) {
                        QualifiedName qn1=(QualifiedName)o;
                        List<TypedValue> vals=env2.get(qn1);
                        if (vals==null) {
                            if (Expand.isGensymVariable(qn1)) {
                        	dstAttributes.add(pf.newAttribute(attribute.getElementName(),
                        	                                  getUUIDQualifiedName(),
                        	                                  pf.getName().XSD_QNAME));
                            }
                            // if not a vargen, then simply drop this attribute
                            //dstAttributes.add(attribute);
                        } else {
                            found=true;
                            for (TypedValue val: vals) {
                        	if (Expand.LABEL_URI.equals(attribute.getElementName().getUri())) {
                        	    dstAttributes.add(pf.newAttribute(pf.getName().PROV_LABEL, 
                        	                                      val.getValue(), 
                        	                                      val.getType()));
                        	} else {
                        	    dstAttributes.add(pf.newAttribute(attribute.getElementName(), 
                        	                                      val.getValue(), 
                        	                                      val.getType()));
                        	}
                            }
                        }
                    } else {
                        dstAttributes.add(attribute); 
                    }
                } else {
                    dstAttributes.add(attribute);

                }
            }
            pf.setAttributes((HasOther) dstStatement, dstAttributes);
        }       
        return found;
    }

    public QualifiedName getUUIDQualifiedName() {
	UUID uuid=UUID.randomUUID();
	return pf.newQualifiedName(URN_UUID_NS, uuid.toString(), UUID_PREFIX);
    }

    public void addOrderAttribute(HasOther res) {
	res.getOther().add(pf.newOther(TMPL_NS, "order", TMPL_PREFIX, index, pf.getName().XSD_STRING));
    }

    private boolean setExpand(Statement res, QualifiedName id, int position) {
	if (Expand.isVariable(id)) {
	    QualifiedName val=env.get(id);
	    if (val!=null) {
		u.setter(res, position, val);
		return true;
	    } else {
		if (Expand.isGensymVariable(id)) {
		    QualifiedName uuid=getUUIDQualifiedName();
		    u.setter(res,position,uuid);
		    bindings.addVariable(id, uuid);
		}
	    }
	}
	return false;
    }

    @Override
    public void doAction(WasGeneratedBy s) {
	WasGeneratedBy res=pf.newWasGeneratedBy(s.getId(),s.getEntity(), s.getActivity());
	res.setTime(s.getTime());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName en=res.getEntity();
	boolean updated2=setExpand(res, en, 1);	
	QualifiedName act=res.getActivity();
	boolean updated3=setExpand(res, act, 2);	
	boolean updated4=expandAttributes(s,res);

	boolean updated=updated1 || updated2 || updated3 || updated4;
	ll.add(res);
	if (updated) addOrderAttribute(res);			
    }

    @Override
    public void doAction(WasInvalidatedBy s) {
	WasInvalidatedBy res=pf.newWasInvalidatedBy(s.getId(),s.getEntity(), s.getActivity());
	res.setTime(s.getTime());
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName en=res.getEntity();
	boolean updated2=setExpand(res, en, 1);	
	QualifiedName act=res.getActivity();
	boolean updated3=setExpand(res, act, 2);	
	boolean updated4=expandAttributes(s,res);

	boolean updated=updated1 || updated2 || updated3 || updated4;
	ll.add(res);
	if (updated) addOrderAttribute(res);			
    }

    @Override
    public void doAction(HadMember s) {
	HadMember res=pf.newHadMember(s.getCollection(), s.getEntity());
	
	QualifiedName col=res.getCollection();
	boolean updated0=setExpand(res, col, 0);	
	List<QualifiedName> gen=res.getEntity();
	System.out.println("FIXME: to do , expand entities");
	
	boolean updated=updated0 ;
	ll.add(res);
	//if (updated) addOrderAttribute(res);		
	// TODO Auto-generated method stub
	
    }

    @Override
    public void doAction(MentionOf s) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void doAction(SpecializationOf s) {
	SpecializationOf res=pf.newSpecializationOf(s.getSpecificEntity(), s.getGeneralEntity());
	
	QualifiedName spe=res.getSpecificEntity();
	boolean updated0=setExpand(res, spe, 0);	
	QualifiedName gen=res.getGeneralEntity();
	boolean updated1=setExpand(res, gen, 1);	

	boolean updated=updated0 || updated1;
	ll.add(res);
	//if (updated) addOrderAttribute(res);		
	
    }

    @Override
    public void doAction(DerivedByInsertionFrom s) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void doAction(WasInformedBy s) {
	WasInformedBy res=pf.newWasInformedBy(s.getId(),s.getInformed(), s.getInformant());
	
	QualifiedName id=res.getId();
	boolean updated1=setExpand(res, id, 0);	
	QualifiedName a2=res.getInformed();
	boolean updated2=setExpand(res, a2, 1);	
	QualifiedName a1=res.getInformant();
	boolean updated3=setExpand(res, a1, 2);	
	boolean updated4=expandAttributes(s,res);

	boolean updated=updated1 || updated2 || updated3 || updated4;
	ll.add(res);
	if (updated) addOrderAttribute(res);		
	
    }

    @Override
    public void doAction(NamedBundle bun, ProvUtilities provUtilities) {
	List<Statement> statements=bun.getStatement();
	List<Statement> newStatements=new LinkedList<Statement>();
		
	for (Statement s: statements) {
	    for (StatementOrBundle sb: expand.expand(s, bindings, grp1)) {
		newStatements.add((Statement)sb);
	    }
	    
	}
	ll.add(pf.newNamedBundle(bun.getId(), newStatements));
	
    }

    public List<StatementOrBundle> getList() {
	return ll;
    }

}
