package org.openprovenance.prov.rdf;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.notation.Utility;
import  org.antlr.runtime.tree.CommonTree;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.elmo.ElmoManager;
import org.openprovenance.prov.notation.TreeTraversal;
import org.openrdf.elmo.ElmoManagerFactory;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.model.Resource;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.rio.RDFFormat;

import java.util.Hashtable;
import java.util.LinkedList;
import java.io.File;

/**
 * 
 */
public class PubTest extends org.openprovenance.prov.notation.PubTest {
    ProvFactory pFactory=ProvFactory.getFactory();

    public PubTest( String testName ) {
        super(testName);
    }
    
    
    static Document co1;
    static Document co2;
    static Document co3;

    @Override
        public void testReadASNSaveXML() {
        // ignore
    }

    public void testReadASNSaveXML1() throws java.io.IOException, java.lang.Throwable {
        String file="../prov-n/src/test/resources/prov/w3c-publication1.prov-asn";
        testReadASNSaveXML(file,"target/w3c-publication1.prov-xml");
        co1=graph1;
    }


  



    /** Produces a dot representation
     * of created graph. */
    public void testPubToRdf() throws java.io.FileNotFoundException,  java.io.IOException, java.lang.Throwable  {
        String file="src/test/resources/w3c-publication.provn";
        String fileOut1="target/w3c-publication.ttl";
        String fileOut2="target/w3c-publication.trig";

        RepositoryHelper rHelper=new RepositoryHelper();
        ElmoModule module = new ElmoModule();
        rHelper.registerConcepts(module);
        //ContextAwareRepository repo=new ContextAwareRepository();

        ElmoManagerFactory factory=new SesameManagerFactory(module);
        ElmoManager manager = factory.createElmoManager();
        //

        CommonTree t=new Utility().convertASNToTree(file);
        RdfConstructor rdfc=new RdfConstructor(pFactory, manager);

        new TreeTraversal(rdfc).convert(t);
        Hashtable<String,String>  allNamespaceTable = rdfc.getNamespaceTable();
        


        
        rHelper.dumpToRDF(new File(fileOut1),(SesameManager)manager,RDFFormat.TURTLE,allNamespaceTable);
        rHelper.dumpToRDF(new File(fileOut2),(SesameManager)manager,RDFFormat.TRIG,allNamespaceTable);

        
        org.openrdf.repository.RepositoryConnection repo= ((SesameManager)manager).getConnection().getRepository().getConnection();
        RepositoryResult<Resource> res=repo.getContextIDs();

        
        LinkedList<Resource> ll=new LinkedList<Resource>();
        res.addTo(ll);
        
        System.out.println("---> contexts " + ll);     
        assertTrue(ll.size()==2); // two bundles provided!
        

    }


}
