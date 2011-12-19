package org.openprovenance.prov.xml;
import java.util.List;
import java.util.LinkedList;
import javax.xml.namespace.QName;


public class BeanTraversal {
    private BeanConstructor c;

    public BeanTraversal(BeanConstructor c) {
        this.c=c;
    }

    public Object convert(Container cont) {
        System.out.println("--> cont" );
        List lnkRecords=new LinkedList();
        List aRecords=new LinkedList();
        List eRecords=new LinkedList();
        List agRecords=new LinkedList();
        for (Entity e: cont.getRecords().getEntity() ) {
            eRecords.add(convert(e));
        }
        for (Activity a: cont.getRecords().getActivity() ) {
            aRecords.add(convert(a));
        }
        for (Agent ag: cont.getRecords().getAgent() ) {
            agRecords.add(convert(ag));
        }
        for (Object lnk: cont.getRecords().getDependencies().getUsedOrWasGeneratedByOrWasInformedBy() ) {
            lnkRecords.add(convertRelation(lnk));
        }
        Object namespaces=null;
        return c.convertContainer(namespaces,
                                  aRecords,
                                  eRecords,
                                  agRecords,
                                  lnkRecords);
    }

    public Object convert(Entity e) {
        return c.convert(c.convert(e.getId()),e);
    }

    public Object convert(Activity e) {
        return c.convert(c.convert(e.getId()),e);
    }

    public Object convert(Agent e) {
        return c.convert(c.convert(e.getId()),e);
    }

    public Object convertRelation(Object o) {
        // no visitors, so ...
        if (o instanceof WasAssociatedWith) {
            return convert((WasAssociatedWith) o);
        } else if (o instanceof Used) {
            return convert((Used) o);
        } else if (o instanceof WasDerivedFrom) {
            return convert((WasDerivedFrom) o);
        } else if (o instanceof WasControlledBy) {
            return convert((WasControlledBy) o);
        } else if (o instanceof HasAnnotation) {
            return convert((HasAnnotation) o);
        } else if (o instanceof WasInformedBy) {
            return convert((WasInformedBy) o);
        } else if (o instanceof WasComplementOf) {
            return convert((WasComplementOf) o);
        } else if (o instanceof HadPlan) {
            return convert((HadPlan) o);
        } else {// if (o instanceof WasGeneratedBy) {
            return convert((WasGeneratedBy) o);
        }
    }

    public Object convert(WasAssociatedWith o) {
        return c.convert(c.convert(o.getId()),o);
    }
    public Object convert(Used o) {
        return c.convert(c.convert(o.getId()),o);
    }
    public Object convert(WasDerivedFrom o) {
        return c.convert(c.convert(o.getId()),o);
    }
    public Object convert(WasControlledBy o) {
        return c.convert(c.convert(o.getId()),o);
    }
    public Object convert(HasAnnotation o) {
        return c.convert(c.convert(o.getId()),o);
    }
    public Object convert(WasInformedBy o) {
        return c.convert(c.convert(o.getId()),o);
    }
    public Object convert(WasComplementOf o) {
        return c.convert(c.convert(o.getId()),o);
    }
    public Object convert(HadPlan o) {
        return c.convert(c.convert(o.getId()),o);
    }
    public Object convert(WasGeneratedBy o) {
        return c.convert(c.convert(o.getId()),o);
    }
}
