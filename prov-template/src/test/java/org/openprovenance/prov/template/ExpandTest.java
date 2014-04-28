package org.openprovenance.prov.template;

import java.util.Arrays;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import junit.framework.TestCase;

public class ExpandTest extends TestCase {

    static final String VAR_NS = "http://openprovenance.org/var#";
    static final String EX_NS = "http://example.org/";

    public ExpandTest(String testName) {
	super(testName);
    }
    
    ProvFactory pf=new org.openprovenance.prov.xml.ProvFactory();
    
    public void testBinding1() {
	Bindings bindings1=new Bindings();
	QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
	QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
	QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");

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
	
	assertEquals(grp1.get(Arrays.asList(new Integer[]{0,0})).get(0),
	             var_a);
	assertEquals(grp1.get(Arrays.asList(new Integer[]{0,0})).get(1),
	             var_b);

	

	Groupings grp2=new Groupings();
	grp2.addVariable(var_a);
	grp2.addVariable(0,var_b);
	System.out.println(grp2);

	assertEquals(grp2.get(Arrays.asList(new Integer[]{0})).get(0),
	             var_a);
	

	assertEquals(grp2.get(Arrays.asList(new Integer[]{1})).get(0),
	             var_b);
	

	Groupings grp3=new Groupings();
	grp3.addVariable(var_a);
	grp3.addVariable(0,var_b);
	grp3.addVariable(var_c);

	System.out.println(grp3);

	assertEquals(grp3.get(Arrays.asList(new Integer[]{0,0})).get(0),
	             var_a);
	
	assertEquals(grp3.get(Arrays.asList(new Integer[]{0,0})).get(1),
	             var_c);
	

	assertEquals(grp3.get(Arrays.asList(new Integer[]{1,0})).get(0),
	             var_b);
	

	

	
    }

}
