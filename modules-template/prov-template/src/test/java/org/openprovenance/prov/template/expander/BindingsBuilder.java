package org.openprovenance.prov.template.expander;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.template.expander.OldBindings;
import org.openprovenance.prov.template.expander.ExpandUtil;

import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.expander.ExpandUtil.TMPL_NS;

public class BindingsBuilder {
    private static final String EX_NS = "http://example.org/";
    final private ProvFactory pf;
    private final QualifiedName var_a;
    private final QualifiedName var_b;
    private final QualifiedName var_c;
    private final QualifiedName var_d;
    private final QualifiedName var_e;
    private final QualifiedName var_e2;
    private final QualifiedName var_ag;
    private final QualifiedName var_pl;
    private final QualifiedName var_label;
    private final QualifiedName var_start;
    private final QualifiedName var_end;

    public BindingsBuilder(ProvFactory pf) {
        this.pf = pf;
        this.var_a = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "a", "var");
        this.var_b = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "b", "var");
        this.var_c = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "c", "var");
        this.var_d = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "d", "var");
        this.var_e = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "e", "var");
        this.var_e2 = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "e2", "var");
        this.var_ag = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "ag", "var");
        this.var_pl = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "pl", "var");
        this.var_label = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "label", "var");
        this.var_start = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "start", "var");
        this.var_end = this.pf.newQualifiedName(ExpandUtil.VAR_NS, "end", "var");
    }

    OldBindings makeBindings1() {
        OldBindings bindings1 = new OldBindings(pf);

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
        return bindings1;
    }

    OldBindings makeBindings2() {
        OldBindings bindings1 = new OldBindings(pf);

        bindings1.addVariable(var_a,
                pf.newQualifiedName(EX_NS, "av1", "ex"));
        bindings1.addVariable(var_a,
                pf.newQualifiedName(EX_NS, "av2", "ex"));

        bindings1.addVariable(var_b,
                pf.newQualifiedName(EX_NS, "bv1", "ex"));
        bindings1.addVariable(var_b,
                pf.newQualifiedName(EX_NS, "bv2", "ex"));
        return bindings1;
    }

    OldBindings makeBindings3() {
        OldBindings bindings1 = new OldBindings(pf);

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
        return bindings1;
    }

    OldBindings makeBindings20() {
        OldBindings bindings1 = new OldBindings(pf);

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

        List<TypedValue> ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me1@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_d, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "m22@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_d, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc1@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc2@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc3@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc4@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc5@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc6@example", pf.getName().XSD_STRING));
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc7@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);
        return bindings1;
    }

    OldBindings makeBindings21() {
        OldBindings bindings1 = new OldBindings(pf);

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

        bindings1.addVariable(var_b,
                pf.newQualifiedName(EX_NS, "bv3", "ex"));


        List<TypedValue> ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me1@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_d, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me2@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_d, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me3@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_d, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc1@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc2@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc6@example", pf.getName().XSD_STRING));
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc7@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);
        return bindings1;
    }

    OldBindings makeBindings22() {
        OldBindings bindings1 = new OldBindings(pf);

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

        bindings1.addVariable(var_b,
                pf.newQualifiedName(EX_NS, "bv3", "ex"));


        List<TypedValue> ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me1@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_d, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me2@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_d, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me3@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_d, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc1@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc2@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc6@example", pf.getName().XSD_STRING));
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc7@example", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_e, ll);
        return bindings1;
    }

    OldBindings makebindinsg23() {
        OldBindings bindings23 = new OldBindings(pf);

        bindings23.addVariable(var_a,
                pf.newQualifiedName(EX_NS, "av1", "ex"));
        bindings23.addVariable(var_a,
                pf.newQualifiedName(EX_NS, "av2", "ex"));
        bindings23.addVariable(var_a,
                pf.newQualifiedName(EX_NS, "av3", "ex"));

        bindings23.addVariable(var_b,
                pf.newQualifiedName(EX_NS, "bv1", "ex"));
        bindings23.addVariable(var_b,
                pf.newQualifiedName(EX_NS, "bv2", "ex"));

        bindings23.addVariable(var_b,
                pf.newQualifiedName(EX_NS, "bv3", "ex"));



        List<TypedValue> ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me1@example", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_d, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me2@example", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_d, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "me3@example", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_d, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc1@example", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_e, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc2@example", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_e, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc6@example", pf.getName().XSD_STRING));
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "Luc7@example", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_e, ll);


        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "label1", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_label, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "label2", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_label, ll);

        ll = new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "label3", pf.getName().XSD_STRING));
        ll.add(pf.newOther(pf.newQualifiedName(ExpandUtil.TMPL_NS, "ignore", "app"), "label3b", pf.getName().XSD_STRING));
        bindings23.addAttribute(var_label, ll);
        return bindings23;
    }

    OldBindings makeBindings10() {
        OldBindings bindings10=new OldBindings(pf);

        bindings10.addVariable(var_a,
                pf.newQualifiedName(EX_NS, "av1", "ex"));


        bindings10.addVariable(var_e,
                pf.newQualifiedName(EX_NS, "ev1", "ex"));
        bindings10.addVariable(var_e2,
                pf.newQualifiedName(EX_NS, "ev2", "ex"));
        bindings10.addVariable(var_ag,
                pf.newQualifiedName(EX_NS, "agv1", "ex"));
        bindings10.addVariable(var_pl,
                pf.newQualifiedName(EX_NS, "pl1", "ex"));


        List<TypedValue> ll=new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "me1@example", pf.getName().XSD_STRING));
        ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "m22@example", pf.getName().XSD_STRING));
        bindings10.addAttribute(var_d, ll);


        List<TypedValue> ll2=new LinkedList<>();
        ll2.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), pf.newISOTime("2023-09-05T21:31:56.570+01:00"), pf.getName().XSD_DATETIME));
        bindings10.addAttribute(var_start, ll2);

        List<TypedValue> ll3=new LinkedList<>();
        ll3.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), pf.newISOTime("2023-09-05T21:31:56.571+01:00"), pf.getName().XSD_DATETIME));
        bindings10.addAttribute(var_end, ll3);
        return bindings10;
    }


    OldBindings makeBindings25a() {
        OldBindings bindings1=new OldBindings(pf);

        bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "apple", "ex"));
        bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "orange", "ex"));
        bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "pear", "ex"));

        List<TypedValue> ll;

        ll= new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "apple_val", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_b, ll);

        ll= new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "orange_val", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_b, ll);

        ll= new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "pear_val", pf.getName().XSD_STRING));
        bindings1.addAttribute(var_b, ll);


        return bindings1;
    }

    OldBindings makeBindings25b() {
        OldBindings bindings1=new OldBindings(pf);

        bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "apple", "ex"));
        bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "orange", "ex"));
        bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "pear", "ex"));

        List<TypedValue> ll;

        ll= new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), pf.newQualifiedName(EX_NS, "apple/v/0", "ex"), pf.getName().PROV_QUALIFIED_NAME));
        bindings1.addAttribute(var_b, ll);

        ll= new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), pf.newQualifiedName(EX_NS, "orange/v/0", "ex"), pf.getName().PROV_QUALIFIED_NAME));
        bindings1.addAttribute(var_b, ll);

        ll= new LinkedList<>();
        ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), pf.newQualifiedName(EX_NS, "pear/v/0", "ex"), pf.getName().PROV_QUALIFIED_NAME));
        bindings1.addAttribute(var_b, ll);


        return bindings1;
    }


}