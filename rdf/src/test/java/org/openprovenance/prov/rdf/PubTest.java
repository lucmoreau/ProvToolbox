package org.openprovenance.prov.rdf;
import org.openprovenance.prov.xml.Container;
import org.openprovenance.prov.asn.Utility;
import  org.antlr.runtime.tree.CommonTree;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.elmo.ElmoManager;
import org.openprovenance.prov.asn.TreeTraversal;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoManagerFactory;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.rio.RDFFormat;
import java.util.List;
import java.util.LinkedList;
import java.io.File;

/**
 * 
 */
public class PubTest extends org.openprovenance.prov.asn.PubTest {
    ProvFactory pFactory=ProvFactory.getFactory();

    public PubTest( String testName ) {
        super(testName);
    }
    
    static Container co1;
    static Container co2;
    static Container co3;

    @Override
        public void testReadASNSaveXML() {
        // ignore
    }

    public void testReadASNSaveXML1() throws java.io.IOException, java.lang.Throwable {
        String file="../asn/src/test/resources/prov/w3c-publication1.prov-asn";
        testReadASNSaveXML(file,"target/w3c-publication1.prov-xml");
        co1=graph1;
    }


    public void convertTreeToRdf(CommonTree tree,
                                 ProvFactory pFactory,
                                 ElmoManager manager) {
        new TreeTraversal(new RdfConstructor(pFactory, manager)).convert(tree);
    }


    /** Produces a dot representation
     * of created graph. */
    public void testPubToRdf() throws java.io.FileNotFoundException,  java.io.IOException, java.lang.Throwable  {
        String file="../asn/src/test/resources/prov/w3c-publication1.prov-asn";
        String fileOut="target/w3c-publication1.prov-rdf";

        RepositoryHelper rHelper=new RepositoryHelper();
        ElmoModule module = new ElmoModule();
        rHelper.registerConcepts(module);
        ElmoManagerFactory factory=new SesameManagerFactory(module);
        ElmoManager manager = factory.createElmoManager();



        CommonTree t=new Utility().convertASNToTree(file);
        convertTreeToRdf(t,pFactory,manager);

        
        rHelper.dumpToRDF(new File(fileOut),(SesameManager)manager,RDFFormat.N3,new LinkedList());
        
        //ProvToDot toDot=new ProvToDot("src/main/resources/defaultConfigWithRoleNoLabel.xml"); 
        
        //toDot.convert(co1,"target/w3c-publication1.dot", "target/w3c-publication1.pdf");

    }


}
