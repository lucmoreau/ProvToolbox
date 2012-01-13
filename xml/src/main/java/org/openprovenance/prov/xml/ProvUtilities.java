package org.openprovenance.prov.xml;
import java.util.List;
import java.util.LinkedList;
import javax.xml.namespace.QName;

/** Utilities for manipulating OPM Graphs. */

public class ProvUtilities {

    private ProvFactory of=new ProvFactory();

    public List<Element> getElements(Container g) {
        List<Element> res=new LinkedList();
        res.addAll(g.getRecords().getEntity());
        res.addAll(g.getRecords().getActivity());
        res.addAll(g.getRecords().getAgent());
        return res;
    }

    public List<Relation> getRelations(Container g) {
        List<Relation> res=new LinkedList();
        Dependencies dep=g.getRecords().getDependencies();
        for (Object o:dep.getUsedOrWasGeneratedByOrWasInformedBy()) {
            res.add((Relation)o);
        }
        return res;
    }

    public QName getEffect(Relation r) {
        if (r instanceof Used) {
            return ((Used)r).getActivity().getRef();
        }
        if (r instanceof WasGeneratedBy) {
            return ((WasGeneratedBy)r).getEntity().getRef();
        }
        if (r instanceof WasDerivedFrom) {
            return ((WasDerivedFrom)r).getEffect().getRef();
        }
        if (r instanceof WasControlledBy) {
            return ((WasControlledBy)r).getEffect().getRef();
        }
        if (r instanceof WasAssociatedWith) {
            return ((WasAssociatedWith)r).getActivity().getRef();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf)r).getEntity2().getRef();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf)r).getEntity2().getRef();
        }
        System.out.println("Unknow relation " + r);
        throw new NullPointerException();
    }
        
    public QName getCause(Relation r) {
        if (r instanceof Used) {
            return ((Used)r).getEntity().getRef();
        }
        if (r instanceof WasGeneratedBy) {
            return ((WasGeneratedBy)r).getActivity().getRef();
        }
        if (r instanceof WasDerivedFrom) {
            return ((WasDerivedFrom)r).getCause().getRef();
        }
        if (r instanceof WasControlledBy) {
            return ((WasControlledBy)r).getCause().getRef();
        }
        if (r instanceof WasAssociatedWith) { // what about entity plan?
            return ((WasAssociatedWith)r).getAgent().getRef();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf)r).getEntity1().getRef();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf)r).getEntity1().getRef();
        }

        throw new NullPointerException();
    }
        


}

