package org.openprovenance.prov.template;

import static org.openprovenance.prov.template.expander.ExpandUtil.VAR_NS;

import java.io.IOException;
import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.expander.Groupings;

import junit.framework.TestCase;

public class GroupingsTest extends TestCase {

    static final String EX_NS = "http://example.org/";

    public GroupingsTest(String testName) {
	super(testName);
    }
    
    ProvFactory pf=new org.openprovenance.prov.vanilla.ProvFactory();
    QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
    QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
    QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");
    QualifiedName var_d=pf.newQualifiedName(VAR_NS, "d", "var");
 
    




    public Groupings getGroupings(String filename) throws IOException, Throwable  {
        Document doc=(Document) new Utility().readDocument(filename, pf);
        
        Groupings grps=Groupings.fromDocument(doc);

        System.out.println("Groupings is " + grps);
        
        
        return grps;
    }
    
    

    public void testGroupings1() throws IOException, JAXBException, Throwable {
	Groupings grps=getGroupings("src/test/resources/template20.provn");
	assertTrue(grps.size()==2);
    }

    public void testGroupings2() throws IOException, JAXBException, Throwable {
	Groupings grps=getGroupings("src/test/resources/template21.provn");
	assertTrue(grps.size()==1);
    }

    public void testGroupings3() throws IOException, JAXBException, Throwable {
	Groupings grps=getGroupings("src/test/resources/template24.provn");
	assertTrue(grps.size()==1);
    }


}
