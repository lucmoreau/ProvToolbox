package org.openprovenance.prov.template;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.expander.*;

import junit.framework.TestCase;
import org.openprovenance.prov.vanilla.ProvFactory;

public class ExpandTest extends TestCase {
    ProvFactory pf= new org.openprovenance.prov.vanilla.ProvFactory();

    final BindingsBuilder bindingsBuilder = new BindingsBuilder(pf);
    boolean addOrderp=true;

    ObjectMapper mapper=new ObjectMapper();

    public  void expandAndCheck(String in, String inBindings, String out)  {

        Document doc= new Utility().readDocument(in, pf);
        Document docBindings= new Utility().readDocument(inBindings, pf);

        Bindings bindings1=Bindings.fromDocument_v1(docBindings, pf);




        Bundle bun=(Bundle) doc.getStatementOrBundle().get(0);

        Groupings grp1=Groupings.fromDocument(doc);

        System.err.println("Found groupings " + grp1);

        Bundle bun1=(Bundle) new Expand(pf,addOrderp,false).expand(bun, bindings1, grp1).get(0);
        Document doc1= pf.newDocument();
        doc1.getStatementOrBundle().add(bun1);


        bun1.setNamespace(Namespace.gatherNamespaces(bun1));

        //doc1.setNamespace(bun1.getNamespace());
        doc1.setNamespace(new Namespace());

        new Utility().writeDocument(doc1, out, pf);
        //InteropFramework inf=new InteropFramework();
        //inf.writeDocument(out, doc1);

        System.err.println("expander ==========================================> ");
    }


    public void expandAndCheck(String inFilename,
                               String outFileName,
                               Bindings bindings1,
                               String outBindings,
                               String inFileBindings) throws IOException {
        System.err.println("expander ==========================================> " + inFilename);

        Document doc= new Utility().readDocument(inFilename, pf);
        Bundle bun=(Bundle) doc.getStatementOrBundle().get(0);

        Groupings grp1=Groupings.fromDocument(doc);
        System.err.println("Found groupings " + grp1);



        Bundle bun1=(Bundle) new Expand(pf,addOrderp,false).expand(bun, bindings1, grp1).get(0);
        Document doc1= pf.newDocument();
        doc1.getStatementOrBundle().add(bun1);


        bun1.setNamespace(Namespace.gatherNamespaces(bun1));

        //doc1.setNamespace(bun1.getNamespace());
        doc1.setNamespace(new Namespace());

        //System.out.println(bun1.getNamespace());


        Namespace.withThreadNamespace(doc1.getNamespace());
        new Utility().writeDocument(doc1, outFileName, pf);

        BindingsBean bindingsBean1=BindingsJson.toBean(bindings1);
        BindingsJson.exportBean(outBindings+".json",bindingsBean1,true);

        if (inFileBindings!=null) {
            BindingsBean bindingsBean2=mapper.readValue(new File(inFileBindings),BindingsBean.class);
            bindingsBean2.vargen=bindingsBean1.vargen;

            assertEquals("hand bindings and loaded bindings differ ("+inFileBindings+"): ", bindingsBean1, bindingsBean2);
        }

    }



    public void testExpand1() throws IOException {

        Bindings bindings1 = bindingsBuilder.makeBindings1();


        expandAndCheck("src/test/resources/templates/template1.provn",
                "target/expanded1.provn",
                bindings1,
                "target/bindings1.provn",
                "src/test/resources/bindings/bindings1.json");


    }

    public void testExpand2() throws IOException {

        Bindings bindings2 = bindingsBuilder.makeBindings2();


        expandAndCheck("src/test/resources/templates/template2.provn",
                "target/expanded2.provn",
                bindings2,
                "target/bindings2.provn",
                "src/test/resources/bindings/bindings2.json");


    }

    public void testExpand3() throws IOException {

        Bindings bindings3 = bindingsBuilder.makeBindings3();


        expandAndCheck("src/test/resources/templates/template3.provn",
                "target/expanded3.provn",
                bindings3,
                "target/bindings3.provn",
                "src/test/resources/bindings/bindings3.json");


    }



    public void testExpand20() throws IOException {

        Bindings bindings20 = bindingsBuilder.makeBindings20();


        expandAndCheck("src/test/resources/templates/template20.provn",
                "target/expanded20.provn",
                bindings20,
                "target/bindings20.provn",
                "src/test/resources/bindings/bindings20.json");


    }



    public void testExpand21() throws IOException {

        Bindings bindings21 = bindingsBuilder.makeBindings21();


        expandAndCheck("src/test/resources/templates/template21.provn",
                "target/expanded21.provn",
                bindings21,
                "target/bindings21.provn",
                "src/test/resources/bindings/bindings21.json");


    }



    public void testExpand22() throws IOException {

        Bindings bindings22 = bindingsBuilder.makeBindings22();


        expandAndCheck("src/test/resources/templates/template22.provn",
                "target/expanded22.provn",
                bindings22,
                "target/bindings22.provn",
                "src/test/resources/bindings/bindings22.json");


    }



    public void testExpand23() throws IOException {

        Bindings bindings23 = bindingsBuilder.makebindinsg23();


        expandAndCheck("src/test/resources/templates/template23.provn",
                "target/expanded23.provn",
                bindings23,
                "target/bindings23.provn",
                "src/test/resources/bindings/bindings23.json");


    }



    public void testExpand25() throws IOException {
        Bindings bindings25a = bindingsBuilder.makeBindings25a();

            expandAndCheck("src/test/resources/templates/template25.provn",
                    "target/expanded25a.provn",
                    bindings25a,
                    "target/bindings25a.provn",
                    "src/test/resources/bindings/bindings25a.json");


        Bindings bindings25b = bindingsBuilder.makeBindings25b();

        expandAndCheck("src/test/resources/templates/template25.provn",
                "target/expanded25b.provn",
                bindings25b,
                "target/bindings25b.provn",
                "src/test/resources/bindings/bindings25b.json");


    }


    public void testExpand10() throws IOException {

        Bindings bindings10=bindingsBuilder.makeBindings10();


        expandAndCheck("src/test/resources/templates/template10.provn",
                "src/test/resources/bindings10.provn",
                "target/expanded10.provn");


    }



    public void testExpandQualifiedName() throws IOException {


        expandAndCheck("src/test/resources/templates/a_template.provn",
                "src/test/resources/a_binding.provn",
                "target/a_expanded_template.provn");


    }







}
