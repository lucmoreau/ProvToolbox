package org.openprovenance.prov.template;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.xml.ProvDeserialiser;

import static org.openprovenance.prov.template.Expand.VAR_NS;
import static org.openprovenance.prov.template.Expand.APP_NS;
import junit.framework.TestCase;

public class ExpandTest extends TestCase {

    static final String EX_NS = "http://example.org/";

    public ExpandTest(String testName) {
	super(testName);
    }
    
    ProvFactory pf=new org.openprovenance.prov.xml.ProvFactory();
    QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
    QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
    QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");
 
    public void NOtestBinding1() {
	Bindings bindings1=new Bindings();

	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av1", "ex"));
	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av2", "ex"));
	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av3", "ex"));
	
	bindings1.addVariable(var_b,
	                      pf.newQualifiedName(EX_NS, "bv1", "ex"));
	bindings1.addVariable(var_b,
	                      pf.newQualifiedName(EX_NS, "bv2", "ex"));
	
	System.out.println(bindings1);
	

	Groupings grp1=new Groupings();
	grp1.addVariable(var_a);
	grp1.addVariable(var_b);
	System.out.println(grp1);
	

	

	Groupings grp2=new Groupings();
	grp2.addVariable(var_a);
	grp2.addVariable(0,var_b);
	System.out.println(grp2);


	Groupings grp3=new Groupings();
	grp3.addVariable(var_a);
	grp3.addVariable(0,var_b);
	grp3.addVariable(var_c);

	System.out.println(grp3);



	Using us1=new Using();
	us1.addGroup(1, 2);
	us1.addGroup(2, 3);
        System.out.println(us1);
        assertTrue(us1.checkIndex(Arrays.asList(new Integer[]{0,0})));
        assertTrue(us1.checkIndex(Arrays.asList(new Integer[]{1,2})));
        assertFalse(us1.checkIndex(Arrays.asList(new Integer[]{1,3})));
        assertFalse(us1.checkIndex(Arrays.asList(new Integer[]{2,2})));
        assertFalse(us1.checkIndex(Arrays.asList(new Integer[]{2,2,4})));
        assertFalse(us1.checkIndex(Arrays.asList(new Integer[]{2})));
        
        assertEquals(us1.nextIndex(Arrays.asList(new Integer[]{0,0})),
                     Arrays.asList(new Integer[]{1,0}));
        assertEquals(us1.nextIndex(Arrays.asList(new Integer[]{1,0})),
                     Arrays.asList(new Integer[]{0,1}));
        assertEquals(us1.nextIndex(Arrays.asList(new Integer[]{0,1})),
                     Arrays.asList(new Integer[]{1,1}));
        assertEquals(us1.nextIndex(Arrays.asList(new Integer[]{1,1})),
                     Arrays.asList(new Integer[]{0,2}));
        assertNull(us1.nextIndex(Arrays.asList(new Integer[]{1,2})));
        
        Iterator<List<Integer>> it=us1.iterator();
        
        while (it.hasNext()) {
            System.out.println("next is " + it.next());
        }
        

	
    }

    public void expander (String in,
                          String out,
                          Bindings bindings1,
                          Groupings grp1) {
	System.out.println("expander ==========================================> ");
	
	Document doc= new InteropFramework().loadProvKnownGraph(in);
	
	NamedBundle bun=(NamedBundle) doc.getStatementOrBundle().get(0);

	

	
	NamedBundle bun1=(NamedBundle) new Expand().expand(bun, bindings1, grp1).get(0);
	Document doc1=pf.newDocument();
	doc1.getStatementOrBundle().add(bun1);
	
	
	bun1.setNamespace(Namespace.gatherNamespaces(bun1));

	doc1.setNamespace(bun1.getNamespace());
	
	System.out.println(bun1.getNamespace());
		
	
	InteropFramework inf=new InteropFramework();
	inf.writeDocument(out, doc1);
	
	
	System.out.println("expander ==========================================> ");

    }

    

    public void testExpand1() {
	
	Bindings bindings1=new Bindings();

	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av1", "ex"));
	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av2", "ex"));
	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av3", "ex"));
	
	bindings1.addVariable(var_b,
	                      pf.newQualifiedName(EX_NS, "bv1", "ex"));
	bindings1.addVariable(var_b,
	                      pf.newQualifiedName(EX_NS, "bv2", "ex"));
	System.out.println(bindings1);

	
	Groupings grp1=new Groupings();
	grp1.addVariable(var_a);
	grp1.addVariable(var_b);
	System.out.println(grp1);
	
	expander("src/test/resources/template1.provn",
	         "target/expanded1.provn",
	         bindings1,
	         grp1);
	
	
    }
    
    public void testExpand2() {
	
	Bindings bindings1=new Bindings();

	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av1", "ex"));
	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av2", "ex"));
	
	bindings1.addVariable(var_b,
	                      pf.newQualifiedName(EX_NS, "bv1", "ex"));
	bindings1.addVariable(var_b,
	                      pf.newQualifiedName(EX_NS, "bv2", "ex"));
	System.out.println(bindings1);

	
	Groupings grp1=new Groupings();
	grp1.addVariable(var_a);
	grp1.addVariable(0,var_b);
	System.out.println(grp1);
	
	expander("src/test/resources/template1.provn",
	         "target/expanded2.provn",
	         bindings1,
	         grp1);
	
	
    }
    
    public void testExpand3() {
	
 	Bindings bindings1=new Bindings();

 	bindings1.addVariable(var_a,
 	                      pf.newQualifiedName(EX_NS, "av1", "ex"));
 	bindings1.addVariable(var_a,
 	                      pf.newQualifiedName(EX_NS, "av2", "ex"));
 	bindings1.addVariable(var_a,
 	                      pf.newQualifiedName(EX_NS, "av3", "ex"));
 	
 	bindings1.addVariable(var_b,
 	                      pf.newQualifiedName(EX_NS, "bv1", "ex"));
 	bindings1.addVariable(var_b,
 	                      pf.newQualifiedName(EX_NS, "bv2", "ex"));
 	
 	bindings1.addVariable(var_c,
 	                      pf.newQualifiedName(EX_NS, "cv1", "ex"));
 	bindings1.addVariable(var_c,
 	                      pf.newQualifiedName(EX_NS, "cv2", "ex"));
 	
 	System.out.println(bindings1);

 	
 	Groupings grp1=new Groupings();
 	grp1.addVariable(var_a);
 	grp1.addVariable(var_b);
 	grp1.addVariable(1,var_c);
 	System.out.println(grp1);
 	
 	expander("src/test/resources/template2.provn",
 	         "target/expanded3.provn",
 	         bindings1,
 	         grp1);
 	
 	
     }
    

    public void testExpand10() {
        
        Bindings bindings1=new Bindings();

        bindings1.addVariable(var_a,
                              pf.newQualifiedName(EX_NS, "av1", "ex"));
        bindings1.addVariable(var_a,
                              pf.newQualifiedName(EX_NS, "av2", "ex"));
        bindings1.addVariable(var_a,
                              pf.newQualifiedName(EX_NS, "av3", "ex"));
        
        bindings1.addVariable(var_b,
                              pf.newQualifiedName(EX_NS, "bv1", "ex"));
        bindings1.addVariable(var_b,
                              pf.newQualifiedName(EX_NS, "bv2", "ex"));
        
        bindings1.addVariable(var_c,
                              pf.newQualifiedName(EX_NS, "cv1", "ex"));
        bindings1.addVariable(var_c,
                              pf.newQualifiedName(EX_NS, "cv2", "ex"));
        
        
        System.out.println(bindings1);

        
        Groupings grp1=new Groupings();
        grp1.addVariable(var_a);
        grp1.addVariable(var_b);
        grp1.addVariable(1,var_c);
        System.out.println(grp1);
        
        expander("src/test/resources/template10.provn",
                 "target/expanded10.provn",
                 bindings1,
                 grp1);
        
        
    }
    
     

}
