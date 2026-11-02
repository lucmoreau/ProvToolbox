import junit.framework.TestCase;
import org.openprovenance.prov.nf.xml.Attr;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NormalizeTest  extends TestCase {

    public void testCompare1() {
        Attr a1=new Attr();
        Attr a2=new Attr();

        a1.element="foo";
        a2.element="foo";
        a1.type="t";
        a2.type="t";
        a1.value="v2";
        a2.value="v1";

        assertEquals(0,a1.compareTo(a1));
        assert(a1.compareTo(a2)>0);
        assert(a2.compareTo(a1)<0);


        List<Attr> ll=new LinkedList<Attr>();
        ll.add(a1);
        ll.add(a2);
        assert(ll.get(0)==a1);
        System.out.println(String.join(",",ll.stream().map(Object::toString).collect(Collectors.toList())));


        Collections.sort(ll);

        System.out.println(String.join(",",ll.stream().map(Object::toString).collect(Collectors.toList())));
        assert(ll.get(0)==a2);

    }
}
