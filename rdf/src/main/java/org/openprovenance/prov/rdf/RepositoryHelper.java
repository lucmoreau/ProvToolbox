package org.openprovenance.prov.rdf;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;



import javax.xml.namespace.QName;
// import org.openprovenance.model.OPMDeserialiser;
// import org.openprovenance.model.OPMGraph;
// import org.openprovenance.model.OPMSerialiser;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoManagerFactory;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.rio.ntriples.NTriplesWriter;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

/** Utility class (straight from OPMToolbox) */

public class RepositoryHelper {


    public void registerConcepts(ElmoModule module) {
        module.addConcept(org.openprovenance.prov.rdf.Entity.class);
        module.addConcept(org.openprovenance.prov.rdf.Activity.class);
        module.addConcept(org.openprovenance.prov.rdf.Agent.class);
        module.addConcept(org.openprovenance.prov.rdf.Usage.class);
        module.addConcept(org.openprovenance.prov.rdf.Generation.class);
        module.addConcept(org.openprovenance.prov.rdf.Association.class);
        module.addConcept(org.openprovenance.prov.rdf.Responsibility.class);
        module.addConcept(org.openprovenance.prov.rdf.Plan.class);
        module.addConcept(org.openprovenance.prov.rdf.Location.class);
        module.addConcept(org.openprovenance.prov.rdf.SoftwareAgent.class);
        // module.addDatatype(org.openprovenance.elmo.XMLLiteral.class,
        //                    "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral");
    }

    public void setPrefixes(RDFHandler serialiser,
                            Collection<String[]> prefixes) throws org.openrdf.rio.RDFHandlerException {
            serialiser.handleNamespace("prov","http://www.w3.org/ns/prov#");
            for (String[] prefix: prefixes) {
                serialiser.handleNamespace(prefix[0],prefix[1]);
            }
    }


    public void dumpToRDF(File file,
                          SesameManager manager,
                          RDFFormat format,
                          Collection<String[]> prefixes) throws Exception {
        Writer writer = new FileWriter(file);
        RDFHandler serialiser=null;
        if (format.equals(RDFFormat.N3)) {
            serialiser=new N3Writer(writer);
        } else if (format.equals(RDFFormat.RDFXML)) {
            serialiser=new RDFXMLWriter(writer);
        } else if  (format.equals(RDFFormat.NTRIPLES)) {
            serialiser=new NTriplesWriter (writer);
        } else if (format.equals(RDFFormat.TURTLE)) {
            serialiser=new org.openrdf.rio.turtle.TurtleWriter(writer);
        }
        setPrefixes(serialiser,prefixes);
        manager.getConnection().export(serialiser);
        writer.close();
    }

    public void readFromRDF(File file, String uri, SesameManager manager, RDFFormat format) throws Exception {
        manager.getConnection().add(file,uri,format);
    }


    /*
    public static void main(String [] args) throws Exception {
        if ((args==null) || (!((args.length==4) || (args.length==5)))) {
            System.out.println("Usage: opmconvert -xml2rdf fileIn fileOut NS [yes]");
            System.out.println("Usage: opmconvert -xml2n3 fileIn fileOut NS [yes]");
            System.out.println("Usage: opmconvert -rdf2xml fileIn fileOut NS [gid]");

            return;
        }
        if (args[0].equals("-rdf2xml")) {
            String fileIn=args[1];
            String fileOut=args[2];
            String namespace=args[3];
            String gid=null;
            if (args.length==5) gid=args[4];

            RepositoryHelper rHelper=new RepositoryHelper();
            rHelper.rdfToXml(fileIn,fileOut,namespace,gid);
            return;
        }

        if (args[0].equals("-xml2rdf")) {
            String fileIn=args[1];
            String fileOut=args[2];
            String namespace=args[3];
            String gid=null;
            if (args.length==5) gid=args[4];

            RepositoryHelper rHelper=new RepositoryHelper();
            rHelper.xmlToRdf(fileIn,fileOut,namespace,gid);
            return;
        }

        if (args[0].equals("-xml2n3")) {
            String fileIn=args[1];
            String fileOut=args[2];
            String namespace=args[3];
            String gid=null;
            if (args.length==5) gid=args[4];

            RepositoryHelper rHelper=new RepositoryHelper();
            rHelper.xmlToN3(fileIn,fileOut,namespace,gid);
            return;
        }

        //TODO: other options here


    }


    public void rdfToXml(String fileIn, String fileOut, String NS, String graphId) throws Exception {
        ElmoModule module = new ElmoModule();
        registerConcepts(module);
        ElmoManagerFactory factory=new SesameManagerFactory(module);
        ElmoManager manager = factory.createElmoManager();

        RdfOPMFactory oFactory=new RdfOPMFactory(new RdfObjectFactory(manager,NS),
                                                 manager);
        File file = new File(fileIn);
        readFromRDF(file,null,(SesameManager)manager,RDFFormat.RDFXML);

        QName qname = new QName(NS, graphId);
        Object o=manager.find(qname);
        org.openprovenance.rdf.OPMGraph gr=(org.openprovenance.rdf.OPMGraph)o;
        OPMGraph oGraph=oFactory.newOPMGraph(gr);
        OPMSerialiser.getThreadOPMSerialiser().serialiseOPMGraph(new File(fileOut),oGraph,true);
    }

    public void xmlToRdf(String fileIn, String fileOut, String NS, String gid) throws Exception {
        xmlToRdf(fileIn,fileOut,NS,gid,RDFFormat.RDFXML);
    }
    public void xmlToN3(String fileIn, String fileOut, String NS, String gid) throws Exception {
        xmlToRdf(fileIn,fileOut,NS,gid,RDFFormat.N3);
    }

    public void xmlToRdf(String fileIn, String fileOut, String NS, String gid, RDFFormat format) throws Exception {
        ElmoModule module = new ElmoModule();
        registerConcepts(module);
        ElmoManagerFactory factory=new SesameManagerFactory(module);
        ElmoManager manager = factory.createElmoManager();


        OPMGraph oGraph=OPMDeserialiser.getThreadOPMDeserialiser().deserialiseOPMGraph(new File(fileIn));
        String graphId=oGraph.getId();


        String namespace=NS;
        if ((gid!=null) && (gid.equals("yes"))) namespace=namespace+ graphId +"/";
        RdfOPMFactory oFactory=new RdfOPMFactory(new RdfObjectFactory(manager,namespace));


        RdfOPMGraph graph2=oFactory.newOPMGraph(oGraph);

        Collection<String[]> prefixes=Collections.singleton(new String[]{"ex",namespace});
        
            
        dumpToRDF(new File(fileOut),(SesameManager)manager,format,prefixes);
    }
    */

}