package org.openprovenance.prov.template;

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
	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av1", "ex"));
	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av2", "ex"));
	bindings1.addVariable(var_a,
	                      pf.newQualifiedName(EX_NS, "av3", "ex"));
	
	QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
	bindings1.addVariable(var_b,
	                      pf.newQualifiedName(EX_NS, "bv1", "ex"));
	bindings1.addVariable(var_b,
	                      pf.newQualifiedName(EX_NS, "bv2", "ex"));
	
	System.out.println(bindings1);
	

	
    }

}
