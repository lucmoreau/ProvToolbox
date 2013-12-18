package org.openprovenance.prov.rdf;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Hashtable;

import org.openprovenance.prov.model.Namespace;
import org.openrdf.repository.contextaware.ContextAwareRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.rio.ntriples.NTriplesWriter;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.rio.trig.TriGWriter;

/** Utility class (straight from OPMToolbox) */

public class RepositoryHelper {


    public void setPrefixes(RDFHandler serialiser,
                            Hashtable<String,String> prefixes) throws org.openrdf.rio.RDFHandlerException {
            serialiser.handleNamespace("prov","http://www.w3.org/ns/prov#");
            for (String key: prefixes.keySet()) {
                serialiser.handleNamespace(key,prefixes.get(key));
            }
    }

    

    public void dumpToRDF(String file,
                          ContextAwareRepository manager,
                          RDFFormat format,
                          Namespace namespace) throws Exception {
	Hashtable<String,String> prefixes=namespace.getPrefixes();
	if (namespace.getDefaultNamespace()!=null) {
	    // TODO not sure how to handle this?
	}
        Writer writer = new FileWriter(file); 
        RDFHandler serialiser=null;
        if (format.equals(RDFFormat.N3)) {
            serialiser=new N3Writer(writer);
        } else if (format.equals(RDFFormat.RDFXML)) {
            serialiser=new RDFXMLWriter(writer);
        } else if  (format.equals(RDFFormat.NTRIPLES)) {
            serialiser=new NTriplesWriter (writer);
        } else if  (format.equals(RDFFormat.TRIG)) {
            serialiser=new TriGWriter (writer);
        } else if (format.equals(RDFFormat.TURTLE)) {
            serialiser=new org.openrdf.rio.turtle.TurtleWriter(writer);
        }
        setPrefixes(serialiser,prefixes);
        manager.getConnection().export(serialiser);

        writer.close();
    }

    public void readFromRDF(File file, String uri, ContextAwareRepository manager, RDFFormat format) throws Exception {
        manager.getConnection().add(file,uri,format);
    }


 
}
