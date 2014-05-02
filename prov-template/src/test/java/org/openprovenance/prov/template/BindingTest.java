package org.openprovenance.prov.template;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;
import static org.openprovenance.prov.template.Expand.VAR_NS;
import static org.openprovenance.prov.template.Expand.APP_NS;
import junit.framework.TestCase;

public class BindingTest extends TestCase {

    static final String EX_NS = "http://example.org/";

    public BindingTest(String testName) {
	super(testName);
    }
    
    ProvFactory pf=new org.openprovenance.prov.xml.ProvFactory();
    QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
    QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
    QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");
    QualifiedName var_d=pf.newQualifiedName(VAR_NS, "d", "var");
 
    



    public void testBindings1() {
        
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
        
        

        
        Groupings grp1=new Groupings();
        grp1.addVariable(var_a);
        grp1.addVariable(var_b);
        grp1.addVariable(var_c);
        System.out.println(grp1);
        
        
    }
    
    public void testBindings20() {
        
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
        
        List<TypedValue> ll=new LinkedList<TypedValue>();
        ll.add(pf.newOther(pf.newQualifiedName(APP_NS, "ignore", "app"), "Luc@example", pf.getName().XSD_STRING));
        ll.add(pf.newOther(pf.newQualifiedName(APP_NS, "ignore", "app"), "lm@soton", pf.getName().XSD_STRING));
        
        bindings1.addAttribute(var_d, ll);
        
        



    }
    
    
    
    public void testBindings21() {
        
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
        
        List<TypedValue> ll1=new LinkedList<TypedValue>();
        ll1.add(pf.newOther(pf.newQualifiedName(APP_NS, "ignore", "app"), "Luc@example", pf.getName().XSD_STRING));
        
        List<TypedValue> ll2=new LinkedList<TypedValue>();

        ll2.add(pf.newOther(pf.newQualifiedName(APP_NS, "ignore", "app"), "lm@soton", pf.getName().XSD_STRING));
        
        bindings1.addAttribute(var_d, ll1);
        bindings1.addAttribute(var_d, ll2);
        
        
        
        String filename = "target/b21.provn";
        bindingsTest(bindings1, filename);
    }



    public void bindingsTest(Bindings bindings1, String filename) {
        new InteropFramework().writeDocument(filename, bindings1.toDocument());
        

        Bindings bindings2=Bindings.fromDocument(new InteropFramework().loadProvKnownGraph(filename));

        
        assertEquals(bindings1, bindings2);
    }
    
    

}
