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
        List records=new LinkedList();
        for (Entity e: cont.getRecords().getEntity() ) {
            records.add(convert(e));
        }
        for (Activity a: cont.getRecords().getActivity() ) {
            records.add(convert(a));
        }
        for (Agent ag: cont.getRecords().getAgent() ) {
            records.add(convert(ag));
        }
        Object namespaces=null;
        return c.convertContainer(namespaces,
                                  records);
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


}
