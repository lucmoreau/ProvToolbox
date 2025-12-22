package org.openprovenance.prov.template.expander.test;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.notation.Utility;

import junit.framework.TestCase;
import org.openprovenance.prov.template.expander.*;
import org.openprovenance.prov.template.expander.deprecated.BindingsBean;
import org.openprovenance.prov.template.json.Bindings;
import org.openprovenance.prov.vanilla.ProvFactory;

public class ExpandTest extends TestCase {
    ProvFactory pf= new org.openprovenance.prov.vanilla.ProvFactory();

    final BindingsBuilder bindingsBuilder = new BindingsBuilder(pf);
    boolean addOrderp=true;

    ObjectMapper mapper=new ObjectMapper();

    public  void expandAndCheck(String templateFilename, String inBindings_asProv, String inBindings, String out) throws IOException {

        Document doc= new Utility().readDocument(templateFilename, pf);
        Document docBindings= new Utility().readDocument(inBindings_asProv, pf);

        OldBindings oldBindings= OldBindings.fromDocument_v1(docBindings, pf);


        Bindings newBindings=mapper.readValue(new File(inBindings),Bindings.class);


        Bundle bun=(Bundle) doc.getStatementOrBundle().get(0);
        Groupings grp1=Groupings.fromDocument(doc, newBindings, pf);
        System.err.println("Found groupings " + grp1);

        Bundle bun1=(Bundle) new Expand(pf,addOrderp,false).expand(bun, newBindings, grp1, inBindings_asProv, templateFilename).get(0);
        Document doc1= pf.newDocument();
        doc1.getStatementOrBundle().add(bun1);


        bun1.setNamespace(Namespace.gatherNamespaces(bun1));

        //doc1.setNamespace(bun1.getNamespace());
        doc1.setNamespace(new Namespace());

        new Utility().writeDocument(doc1, out, pf);
        //InteropFramework inf=new InteropFramework();
        //inf.writeDocument(out, doc1);

        BindingsBean bindingsBean1= Conversions.toBean(oldBindings);
        Conversions.exportBean(out+".json",bindingsBean1,true);


        System.err.println("expander ==========================================> ");
    }


    public void expandAndCheck(String inFilename,
                               String outFileName,
                               OldBindings handBindingsLegacy,
                               String outBindings,
                               String inFileBindings) throws IOException {
        System.err.println("expander ==========================================> " + inFilename);

        Document doc= new Utility().readDocument(inFilename, pf);
        Bundle bun=(Bundle) doc.getStatementOrBundle().get(0);
        Bindings inBindings=mapper.readValue(new File(inFileBindings),Bindings.class);

        Groupings grp1=Groupings.fromDocument(doc, inBindings, pf);
        System.err.println("Found groupings " + grp1);

        BindingsBean inBindingsBean=inBindings.toBean();
        OldBindings inBindingsLegacy= BindingsJson.fromBean(inBindings,pf);
        //System.out.println(inBindingsLegacy.toString());
        BindingsBean inBindingsBean2=Conversions.toBean(inBindingsLegacy);


        Bundle bun1=(Bundle) new Expand(pf,addOrderp,false).expand(bun, inBindings, grp1, inFileBindings, inFilename).get(0);
        Document doc1= pf.newDocument();
        doc1.getStatementOrBundle().add(bun1);
        bun1.setNamespace(Namespace.gatherNamespaces(bun1));
        //doc1.setNamespace(bun1.getNamespace());
        doc1.setNamespace(new Namespace());
        Namespace.withThreadNamespace(doc1.getNamespace());
        new Utility().writeDocument(doc1, outFileName, pf);

        BindingsBean handBindingsBean=Conversions.toBean(handBindingsLegacy);
        Conversions.exportBean(outBindings+".json",handBindingsBean,true);


        BindingsBean inBindingsBean3 = mapper.readValue(new File(inFileBindings), BindingsBean.class);

        assertEquals("inBindingsBean (from newBindings) and directly loaded bindings bean differ ("+inFileBindings+"): ", inBindingsBean, inBindingsBean3);


        assertEquals("hand bindings and directly loaded bindings bean differ ("+inFileBindings+"): ", handBindingsBean, inBindingsBean3);


        // ignore vargen as it is being recreated
        inBindingsBean2.vargen=handBindingsBean.vargen;

        assertEquals("inBindingsBean2 and directly loaded bindings bean differ  differ ("+inFileBindings+"): ", inBindingsBean2, inBindingsBean3);

    }

    public void testGo() throws IOException{
        testExpand1();
        testExpand2();
        testExpand3();
        testExpand10();
        testExpand20();
        testExpand21();
        testExpand22();
        testExpand23();
        testExpand25();
        testExpandQualifiedName();
    }



    public void testExpand1() throws IOException {

        OldBindings bindings1 = bindingsBuilder.makeBindings1();


        expandAndCheck("src/test/resources/templates2/template1.provn",
                "target/expanded1.provn",
                bindings1,
                "target/bindings1.provn",
                "src/test/resources/bindings/bindings1.json");


    }

    public void testExpand2() throws IOException {

        OldBindings bindings2 = bindingsBuilder.makeBindings2();


        expandAndCheck("src/test/resources/templates2/template2.provn",
                "target/expanded2.provn",
                bindings2,
                "target/bindings2.provn",
                "src/test/resources/bindings/bindings2.json");


    }

    public void testExpand3() throws IOException {

        OldBindings bindings3 = bindingsBuilder.makeBindings3();


        expandAndCheck("src/test/resources/templates2/template3.provn",
                "target/expanded3.provn",
                bindings3,
                "target/bindings3.provn",
                "src/test/resources/bindings/bindings3.json");


    }



    public void testExpand20() throws IOException {

        OldBindings bindings20 = bindingsBuilder.makeBindings20();


        expandAndCheck("src/test/resources/templates2/template20.provn",
                "target/expanded20.provn",
                bindings20,
                "target/bindings20.provn",
                "src/test/resources/bindings/bindings20.json");


    }



    public void testExpand21() throws IOException {

        OldBindings bindings21 = bindingsBuilder.makeBindings21();


        expandAndCheck("src/test/resources/templates2/template21.provn",
                "target/expanded21.provn",
                bindings21,
                "target/bindings21.provn",
                "src/test/resources/bindings/bindings21.json");


    }



    public void testExpand22() throws IOException {

        OldBindings bindings22 = bindingsBuilder.makeBindings22();


        expandAndCheck("src/test/resources/templates2/template22.provn",
                "target/expanded22.provn",
                bindings22,
                "target/bindings22.provn",
                "src/test/resources/bindings/bindings22.json");


    }



    public void testExpand23() throws IOException {

        OldBindings bindings23 = bindingsBuilder.makebindinsg23();


        expandAndCheck("src/test/resources/templates2/template23.provn",
                "target/expanded23.provn",
                bindings23,
                "target/bindings23.provn",
                "src/test/resources/bindings/bindings23.json");


    }



    public void testExpand25() throws IOException {
        OldBindings bindings25a = bindingsBuilder.makeBindings25a();

            expandAndCheck("src/test/resources/templates2/template25.provn",
                    "target/expanded25a.provn",
                    bindings25a,
                    "target/bindings25a.provn",
                    "src/test/resources/bindings/bindings25a.json");


        OldBindings bindings25b = bindingsBuilder.makeBindings25b();

        expandAndCheck("src/test/resources/templates2/template25.provn",
                "target/expanded25b.provn",
                bindings25b,
                "target/bindings25b.provn",
                "src/test/resources/bindings/bindings25b.json");


    }


    public void testExpand10() throws IOException {

        OldBindings bindings10=bindingsBuilder.makeBindings10();


        expandAndCheck("src/test/resources/templates2/template10.provn",
                "target/expanded10.provn",
                bindings10,
                "target/bindings10.provn",
                "src/test/resources/bindings/bindings10.json"
                );


    }



    public void testExpandQualifiedName() throws IOException {


        expandAndCheck("src/test/resources/templates2/a_template.provn",
                "src/test/resources/bindings_as_prov/a_bindings.provn",
                    "src/test/resources/bindings/a_bindings.json",
                "target/a_expanded_template.provn");


    }







}
