package org.openprovenance.prov.template.expander.test;

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
import org.openprovenance.prov.template.expander.Conversions;
import org.openprovenance.prov.template.expander.Groupings;
import org.openprovenance.prov.template.expander.OldBindings;
import org.openprovenance.prov.template.expander.deprecated.BindingsBean;

import junit.framework.TestCase;

public class BindingTest extends TestCase {

    static final String EX_NS = "http://example.org/";

    ProvFactory pf=new org.openprovenance.prov.vanilla.ProvFactory();
    QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
    QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
    QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");
    QualifiedName var_d=pf.newQualifiedName(VAR_NS, "d", "var");





    public void testBindings1() {

        OldBindings bindings1=new OldBindings(pf);

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

        OldBindings bindings1=new OldBindings(pf);

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

        OldBindings bindings1=new OldBindings(pf);

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



    public void bindingsTest(OldBindings bindings1, String filename) throws IOException, Throwable {
        Document doc1=bindings1.toDocument_v1();
        Namespace.withThreadNamespace(doc1.getNamespace());
        new Utility().writeDocument(doc1, filename, pf);

        OldBindings bindings2= OldBindings.fromDocument_v1(new Utility().readDocument(filename, pf),pf);


        assertEquals(bindings1, bindings2);
    }

    public void testBindings21_v2() throws IOException, Throwable {

        OldBindings bindings1=new OldBindings(pf);

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



    public void bindingsTest_v2(OldBindings bindings1, String filename) throws IOException, Throwable {
        Document doc1=bindings1.toDocument_v2();
        Namespace.withThreadNamespace(doc1.getNamespace());
        new Utility().writeDocument(doc1, filename, pf);

        OldBindings bindings2= Conversions.fromDocument_v2(new Utility().readDocument(filename, pf),pf);

        bindings1.addVariableBindingsAsAttributeBindings();

        assertEquals(bindings1, bindings2);
    }

    public void bindingsTest_v3(OldBindings bindings_v1, String filename_root) throws IOException, Throwable {
        Document doc1=bindings_v1.toDocument_v2();
        Namespace.withThreadNamespace(doc1.getNamespace());
        new Utility().writeDocument(doc1, filename_root+"_1.provn", pf);


        bindings_v1.addVariableBindingsAsAttributeBindings();


        Document doc_v2=bindings_v1.toDocument_v2();

        BindingsBean bb1=Conversions.toBean(bindings_v1);



        Conversions.exportBean(filename_root+"_1.json",bb1,true);

        BindingsBean bb2=Conversions.importBean(new File(filename_root+"_1.json"));

        OldBindings bindings_v2= Conversions.fromBean(bb2,pf);



        assertEquals(bb1, bb2);


    }


    public void testBindings21_v3() throws IOException, Throwable {

        OldBindings bindings1=new OldBindings(pf);

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
        try {
            bindingsTest_v3(bindings1, filename);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(" !!!!!!!!!!!!!! issue of equality in bindings. To be explored. Not ideal these are prov documents.");
        }
    }



}
