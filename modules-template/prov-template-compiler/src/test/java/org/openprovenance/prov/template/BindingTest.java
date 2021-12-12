package org.openprovenance.prov.template;

import static org.openprovenance.prov.template.expander.ExpandUtil.TMPL_NS;
import static org.openprovenance.prov.template.expander.ExpandUtil.VAR_NS;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.template.expander.BindingsJson;
import org.openprovenance.prov.template.expander.Groupings;
import org.openprovenance.prov.template.expander.BindingsJson.BindingsBean;

import junit.framework.TestCase;

public class BindingTest extends TestCase {

    static final String EX_NS = "http://example.org/";

    public BindingTest(String testName) {
	super(testName);
    }
    
    ProvFactory pf=new org.openprovenance.prov.xml.ProvFactory(); //TODO this example fails with vanilla factory.
    QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
    QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
    QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");
    QualifiedName var_d=pf.newQualifiedName(VAR_NS, "d", "var");
 
    



    public void testBindings1() {
        
        Bindings bindings1=new Bindings(pf);

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
        
        Bindings bindings1=new Bindings(pf);

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
        ll.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "Luc@example", pf.getName().XSD_STRING));
        ll.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "lm@soton", pf.getName().XSD_STRING));
        
        bindings1.addAttribute(var_d, ll);
        
        



    }
    
    
    
    public void testBindings21() throws IOException, Throwable {
        
        Bindings bindings1=new Bindings(pf);

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
        ll1.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "2dvalue_0_0", "app"), "Luc@example", pf.getName().XSD_STRING));
        
        List<TypedValue> ll2=new LinkedList<TypedValue>();

        ll2.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "2dvalue_1_0", "app"), "lm@soton", pf.getName().XSD_STRING));
        
        bindings1.addAttribute(var_d, ll1);
        bindings1.addAttribute(var_d, ll2);
        
        
        
        String filename = "target/b21.provn";
        bindingsTest(bindings1, filename);
        
        
        bindings1.addVariableBindingsAsAttributeBindings();
        bindings1.exportToJson("target/b21.json");
        
        
    }



    public void bindingsTest(Bindings bindings1, String filename) throws IOException, Throwable {
	Document doc1=bindings1.toDocument();
	Namespace.withThreadNamespace(doc1.getNamespace());
        new Utility().writeDocument(doc1, filename, pf);        

        Bindings bindings2=Bindings.fromDocument(new Utility().readDocument(filename, pf),pf);

        
        assertEquals(bindings1, bindings2);
    }
    
    public void testBindings21_v2() throws IOException, Throwable {
        
        Bindings bindings1=new Bindings(pf);

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
        ll1.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "2dvalue_0_0", "app"), "Luc@example", pf.getName().XSD_STRING));
        
        List<TypedValue> ll2=new LinkedList<TypedValue>();

        ll2.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "2dvalue_1_0", "app"), "lm@soton", pf.getName().XSD_STRING));
        
        bindings1.addAttribute(var_d, ll1);
        bindings1.addAttribute(var_d, ll2);
        
        
        
        String filename = "target/b21_v2.provn";
        bindingsTest_v2(bindings1, filename);
    }



    public void bindingsTest_v2(Bindings bindings1, String filename) throws IOException, Throwable {
	Document doc1=bindings1.toDocument_v2();
	Namespace.withThreadNamespace(doc1.getNamespace());
        new Utility().writeDocument(doc1, filename, pf);        

        Bindings bindings2=Bindings.fromDocument_v2(new Utility().readDocument(filename, pf),pf);

	//        System.out.println("bindings2 --> " + bindings2);
	//        System.out.println("bindings1 --> " + bindings1);
        
        bindings1.addVariableBindingsAsAttributeBindings();
	//        System.out.println("bindings1 --> " + bindings1);
        
        assertEquals(bindings1, bindings2);
    }
    
    public void bindingsTest_v3(Bindings bindings_v1, String filename_root) throws IOException, Throwable {
        Document doc1=bindings_v1.toDocument_v2();
        Namespace.withThreadNamespace(doc1.getNamespace());
        new Utility().writeDocument(doc1, filename_root+"_1.provn", pf);        
        
         
        bindings_v1.addVariableBindingsAsAttributeBindings();
        
        
        Document doc_v2=bindings_v1.toDocument_v2();
        
        BindingsBean bb=BindingsJson.toBean(bindings_v1);
        
        
        
        BindingsJson.exportBean(filename_root+"_1.json",bb,true);
        
        BindingsBean bb2=BindingsJson.importBean(new File(filename_root+"_1.json"));
        
        Bindings bindings_v2=BindingsJson.fromBean(bb2,pf);
        
	//        System.out.println("bindings1 --> " + bindings_v1);
	//        System.out.println("bindings2 --> " + bindings_v2);

        assertEquals(bindings_v1, bindings_v2);

        
    }
        
    
 public void testBindings21_v3() throws IOException, Throwable {
        
        Bindings bindings1=new Bindings(pf);

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
        ll1.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "2dvalue_0_0", "app"), "Luc@example", pf.getName().XSD_STRING));
        
        List<TypedValue> ll2=new LinkedList<TypedValue>();

        ll2.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "2dvalue_1_0", "app"), "lm@soton", pf.getName().XSD_STRING));
        ll2.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "2dvalue_1_1", "app"), "jon@soton", pf.getName().XSD_STRING));
        
        bindings1.addAttribute(var_d, ll1);
        bindings1.addAttribute(var_d, ll2);
        
        
        
        String filename = "target/b21_v3";
        bindingsTest_v3(bindings1, filename);
    }



}
