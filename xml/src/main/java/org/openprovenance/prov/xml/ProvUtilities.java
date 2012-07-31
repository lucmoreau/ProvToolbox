package org.openprovenance.prov.xml;
import java.util.List;
import java.util.LinkedList;

import javax.xml.namespace.QName;


/** Utilities for manipulating PROV Descriptions. */

public class ProvUtilities {

    private ProvFactory of=new ProvFactory();

    public List<Element> getElements(Bundle g) {
        List<Element> res=new LinkedList();
        res.addAll(g.getRecords().getEntity());
        res.addAll(g.getRecords().getActivity());
        res.addAll(g.getRecords().getAgent());
        return res;
    }

    public List<Relation0> getRelations(Bundle g) {
        List<Relation0> res=new LinkedList();
        Dependencies dep=g.getRecords().getDependencies();
        for (Object o:dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
            res.add((Relation0)o);
        }
        return res;
    }

    public QName getEffect(Relation0 r) {
        if (r instanceof Used) {
            return ((Used)r).getActivity().getRef();
        }
        if (r instanceof WasStartedBy) {
            return ((WasStartedBy)r).getActivity().getRef();
        }
        if (r instanceof WasEndedBy) {
            return ((WasEndedBy)r).getActivity().getRef();
        }
        if (r instanceof WasGeneratedBy) {
            return ((WasGeneratedBy)r).getEntity().getRef();
        }
        if (r instanceof WasDerivedFrom) {
            return ((WasDerivedFrom)r).getGeneratedEntity().getRef();
        }
        if (r instanceof WasRevisionOf) {
            return ((WasRevisionOf)r).getNewer().getRef();
        }
        if (r instanceof WasAssociatedWith) {
            return ((WasAssociatedWith)r).getActivity().getRef();
        }
        if (r instanceof WasInvalidatedBy) {
            return ((WasInvalidatedBy)r).getEntity().getRef();
        }

        if (r instanceof WasAttributedTo) {
            return ((WasAttributedTo)r).getEntity().getRef();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf)r).getEntity2().getRef();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf)r).getGeneralEntity().getRef();
        }
        if (r instanceof WasInformedBy) {
            return ((WasInformedBy)r).getEffect().getRef();
        }
        if (r instanceof WasInfluencedBy) {
            return ((WasInfluencedBy)r).getInfluencee().getRef();
        }

        if (r instanceof ActedOnBehalfOf) {
            return ((ActedOnBehalfOf)r).getSubordinate().getRef();
        }

        if (r instanceof DerivedByInsertionFrom) {
            return ((DerivedByInsertionFrom)r).getAfter().getRef();
        }
        System.out.println("Unknow relation " + r);
        throw new NullPointerException();
    }
        
    public QName getCause(Relation0 r) {
        if (r instanceof Used) {
            return ((Used)r).getEntity().getRef();
        }
        if (r instanceof WasGeneratedBy) {
	    ActivityRef ref=((WasGeneratedBy)r).getActivity();
	    if (ref==null) return null;
            return ref.getRef();
        }
        if (r instanceof WasInvalidatedBy) {
	    ActivityRef ref=((WasInvalidatedBy)r).getActivity();
	    if (ref==null) return null;
            return ref.getRef();
        }
        if (r instanceof WasStartedBy) {
	    EntityRef ref=((WasStartedBy)r).getTrigger();
	    if (ref==null) return null;
            return ref.getRef();
        }
        if (r instanceof WasEndedBy) {
	    EntityRef ref=((WasEndedBy)r).getTrigger();
	    if (ref==null) return null;
            return ref.getRef();
        }
        if (r instanceof WasDerivedFrom) {
            return ((WasDerivedFrom)r).getUsedEntity().getRef();
        }

        if (r instanceof WasInfluencedBy) {
            return ((WasInfluencedBy)r).getInfluencer().getRef();
        }
        if (r instanceof WasRevisionOf) {
            return ((WasRevisionOf)r).getOlder().getRef();
        }
        if (r instanceof WasAssociatedWith) {
            return ((WasAssociatedWith)r).getAgent().getRef();
        }
        if (r instanceof WasAttributedTo) {
            return ((WasAttributedTo)r).getAgent().getRef();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf)r).getEntity1().getRef();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf)r).getSpecializedEntity().getRef();
        }
        if (r instanceof WasInformedBy) {
            return ((WasInformedBy)r).getCause().getRef();
        }
        if (r instanceof ActedOnBehalfOf) {
            return ((ActedOnBehalfOf)r).getResponsible().getRef();
        }
        if (r instanceof DerivedByInsertionFrom) {
            return ((DerivedByInsertionFrom)r).getBefore().getRef();
        }
        throw new NullPointerException();
    }

    
    public List<QName> getOtherCauses(Relation0 r) {
        if (r instanceof WasAssociatedWith) {
            List<QName> res=new LinkedList<QName>();
            EntityRef e=((WasAssociatedWith)r).getPlan();
            if (e==null) return null;
            res.add(e.getRef());
            return res;
        }
        if (r instanceof WasStartedBy) {
            List<QName> res=new LinkedList<QName>();
            ActivityRef a=((WasStartedBy)r).getStarter();
            if (a==null) return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof WasEndedBy) {
            List<QName> res=new LinkedList<QName>();
            ActivityRef a=((WasEndedBy)r).getEnder();
            if (a==null) return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof ActedOnBehalfOf) {
            List<QName> res=new LinkedList<QName>();
            ActivityRef a=((ActedOnBehalfOf)r).getActivity();
            if (a==null) return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof DerivedByInsertionFrom) {
            List<QName> res=new LinkedList<QName>();
            DerivedByInsertionFrom dbif=((DerivedByInsertionFrom)r);
            for (Entry entry: dbif.getEntry()) {
                res.add(entry.getEntity().getRef());
            }
            return res;
        }
        return null;
    }

    public MentionOf getMentionForRemoteEntity(Bundle local, Entity remoteEntity, NamedBundle remote) {
        return getMentionForRemoteEntity(local.getRecords(),remoteEntity,remote);
    }

    public MentionOf getMentionForRemoteEntity(NamedBundle local, Entity remoteEntity, NamedBundle remote) {
        return getMentionForRemoteEntity(local.getRecords(),remoteEntity,remote);
    }

    MentionOf getMentionForRemoteEntity(Records local, Entity remoteEntity, NamedBundle remote) {
        Dependencies dep=local.getDependencies();
        for (Object o:dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
            if (o instanceof MentionOf) {
                MentionOf ctxt=(MentionOf) o;
                QName id1=remoteEntity.getId();
                QName id2=remote.getId();
                if (ctxt.getGeneralEntity().getRef().equals(id1)
                    &&
                    ctxt.getBundle().getRef().equals(id2))
                    return ctxt;
            }
        }
        return null;
    }

    public MentionOf getMentionForLocalEntity(Bundle local, Entity localEntity, NamedBundle remote) {
        return getMentionForLocalEntity(local.getRecords(),localEntity,remote);
    }

    public MentionOf getMentionForLocalEntity(NamedBundle local, Entity localEntity, NamedBundle remote) {
        return getMentionForLocalEntity(local.getRecords(),localEntity,remote);
    }

    MentionOf getMentionForLocalEntity(Records local, Entity localEntity, NamedBundle remote) {
        Dependencies dep=local.getDependencies();
        for (Object o:dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
            if (o instanceof MentionOf) {
                MentionOf ctxt=(MentionOf) o;
                QName id1=localEntity.getId();
                QName id2=remote.getId();
                if (ctxt.getSpecializedEntity().getRef().equals(id1)
                    &&
                    ctxt.getBundle().getRef().equals(id2))
                    return ctxt;
            }
        }
        return null;
    }

    /** TODO: should \-unescape local part */
    public String toURI(QName qname) {
	return qname.getNamespaceURI() + qname.getLocalPart();
    }


}

