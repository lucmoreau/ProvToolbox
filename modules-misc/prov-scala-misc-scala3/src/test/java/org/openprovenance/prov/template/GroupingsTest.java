package org.openprovenance.prov.template;

import java.io.IOException;

import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.notation.Utility;

import static org.openprovenance.prov.template.ExpandUtil.VAR_NS;
import junit.framework.TestCase;

public class GroupingsTest extends TestCase {

    static final String EX_NS = "http://example.org/";

    public GroupingsTest(String testName) {
        super(testName);
    }

    ProvFactory pf=new org.openprovenance.prov.scala.immutable.ProvFactory();
    QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
    QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
    QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");
    QualifiedName var_d=pf.newQualifiedName(VAR_NS, "d", "var");






    public Groupings getGroupings(String filename)  {
        Document doc=(Document) new Utility(DateTimeOption.PRESERVE,null).readDocument(filename, pf);

        Groupings grps=Groupings.fromDocument(doc);

        System.out.println("Groupings is " + grps);


        return grps;
    }



    public void testGroupings1() {
        Groupings grps=getGroupings("src/test/resources/template/template20.provn");
        assertEquals(2, grps.size());
    }

    public void testGroupings2() {
        Groupings grps=getGroupings("src/test/resources/template/template21.provn");
        assertEquals(1, grps.size());
    }

    public void testGroupings3() {
        Groupings grps=getGroupings("src/test/resources/template/template24.provn");
        assertEquals(1, grps.size());
    }


}
