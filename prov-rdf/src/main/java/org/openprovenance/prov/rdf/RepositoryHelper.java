package org.openprovenance.prov.rdf;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Map;

import org.openprovenance.prov.model.Namespace;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.contextaware.ContextAwareRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.rio.ntriples.NTriplesWriter;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.rio.trig.TriGWriter;

/** Utility class (straight from OPMToolbox) */

public class RepositoryHelper {


    public void setPrefixes(RDFHandler serialiser,
                            Map<String,String> prefixes) throws org.openrdf.rio.RDFHandlerException {
            serialiser.handleNamespace("prov","http://www.w3.org/ns/prov#");
            for (String key: prefixes.keySet()) {
                serialiser.handleNamespace(key,prefixes.get(key));
            }
    }

    

    public void dumpToRDF(String file,
                          ContextAwareRepository manager,
                          RDFFormat format,
                          Namespace namespace)  {
        Writer writer;
	try {
	    writer = new FileWriter(file);
	} catch (IOException e) {
	    throw new RdfConverterException("couldn't create file writer", e);
	} 
        dumpToRDF(writer, manager, format, namespace);
    }
	
	
    public void dumpToRDF(Writer writer,
	                          ContextAwareRepository manager,
	                          RDFFormat format,
	                          Namespace namespace) {	
	Map<String,String> prefixes=namespace.getPrefixes();
	if (namespace.getDefaultNamespace()!=null) {
	    // TODO not sure how to handle this?
	}
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
        try {
	    setPrefixes(serialiser,prefixes);
	    manager.getConnection().export(serialiser);

	} catch (RDFHandlerException e) {
	    e.printStackTrace();
	    throw new RdfConverterException("setting prefix failed", e);
	} catch (RepositoryException e) {
	    throw new RdfConverterException("export failed", e);

	} finally {
	    try {
		writer.close();
	    } catch (IOException e) {
		throw new RdfConverterException("writer closing failed", e);
	    }

	}

    }

    public void readFromRDF(File file, String uri, ContextAwareRepository manager, RDFFormat format) throws Exception {
        manager.getConnection().add(file,uri,format);
    }


 
}
