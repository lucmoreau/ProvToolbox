package org.openprovenance.prov.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.rdf.collector.QualifiedCollector;
import org.openprovenance.prov.rdf.collector.RdfCollector;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.contextaware.ContextAwareRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.sail.memory.MemoryStore;

public class Utility {
    private final ProvFactory pFactory;
    private final Ontology onto;

    public Utility(ProvFactory pFactory, Ontology onto) {
        this.pFactory = pFactory;
        this.onto = onto;

    }

    public Document parseRDF(String filename) throws RDFParseException, RDFHandlerException,
                                             IOException, JAXBException {
        // System.out.println("**** Parse "+filename);
        File file = new File(filename);
        URL documentURL = file.toURI().toURL();
        InputStream inputStream = documentURL.openStream();
        RDFParser rdfParser = Rio.createParser(Rio.getParserFormatForFileName(file.getName()).orElse(null));
        String streamName = documentURL.toString();

        return parseRDF(inputStream, rdfParser, streamName);
    }

    /** Parse from input stream, no base uri specified. */

    public Document parseRDF(InputStream inputStream,
                             RDFFormat format, 
                             String baseuri)
                                     throws RDFParseException,
                                     RDFHandlerException,
                                     IOException {
        RDFParser rdfParser = Rio.createParser(format);
        return parseRDF(inputStream, rdfParser, baseuri);
    }

    /** Parse from input stream passing base uri . */

    public Document parseRDF(InputStream inputStream, 
                             RDFParser rdfParser, 
                             String baseuri)
                                     throws IOException,
                                     RDFParseException,
                                     RDFHandlerException {
        RdfCollector rdfCollector = new QualifiedCollector(pFactory, onto);
        rdfParser.setRDFHandler(rdfCollector);
        rdfParser.parse(inputStream, baseuri);
        Document doc = rdfCollector.getDocument();
        Namespace ns = doc.getNamespace();
        return doc;
    }

    public void dumpRDF(Document document, RDFFormat format, String filename) {
        dumpRDFInternal(document, format, filename);
    }

    public void dumpRDF(Document document, RDFFormat format, OutputStream os) {
        dumpRDFInternal(document, format, os);

    }

    private void dumpRDFInternal(Document document, RDFFormat format, Object out) {
        Repository myRepository = new SailRepository(new MemoryStore());
        try {
            myRepository.initialize();
        } catch (RepositoryException e) {
            throw new RdfConverterException("failed to initialize repository", e);
        }
        ContextAwareRepository rep = new ContextAwareRepository(myRepository); // was
                                                                               // it
                                                                               // necessary
                                                                               // to
                                                                               // create
                                                                               // that?

        RepositoryHelper rHelper = new RepositoryHelper();

        RdfConstructor rdfc = new RdfConstructor(new SesameGraphBuilder(rep, pFactory), pFactory);

        Namespace ns = new Namespace(document.getNamespace());
        ns.register(NamespacePrefixMapper.RDFS_PREFIX, NamespacePrefixMapper.RDFS_NS); // RDF
                                                                                       // Schema
        ns.register(NamespacePrefixMapper.RDF_PREFIX, NamespacePrefixMapper.RDF_NS); // RDF
                                                                                     // Concepts
        rdfc.setNamespace(ns);

        Namespace.withThreadNamespace(document.getNamespace());
        BeanTraversal bt = new BeanTraversal(rdfc, pFactory);
        bt.doAction(document);

        if (out instanceof String) {
            rHelper.dumpToRDF((String) out, rep, format, rdfc.getNamespace());
        } else {
            rHelper.dumpToRDF(new OutputStreamWriter((OutputStream) out),
                              rep,
                              format,
                              rdfc.getNamespace());
        }
    }

}
