package org.openprovenance.prov.interop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;
import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.DocumentEquality;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.xml.ProvDeserialiser;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.rdf.Ontology;
import org.openprovenance.prov.template.Bindings;
import org.openprovenance.prov.template.BindingsBeanGenerator;
import org.openprovenance.prov.template.BindingsJson;
import org.openprovenance.prov.template.Expand;
import org.openprovenance.prov.template.FileBuilder;
import org.openprovenance.prov.template.GeneratorConfig;
import org.openprovenance.prov.template.TemplateBuilderGenerator;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.generator.GeneratorDetails;
import org.openprovenance.prov.generator.GraphGenerator;
import org.apache.log4j.Logger;

/**
 * The interoperability framework for PROV, with utility methods to write and read documents to files and streams, 
 * according to media types, format (specified as {@link ProvFormat}). The class also provides helper functions to support content 
 * negotiation.
 * 
 *
 * 
 * @author lavm, dtm
 *
 */
public class InteropFramework implements InteropMediaType {



    /** An enumerated type for all the PROV serializations supported by ProvToolbox. 
     * Some of these serializations can be input, output, or both. */
    
    static public enum ProvFormat {
        PROVN, XML, TURTLE, RDFXML, TRIG, JSON, DOT, JPEG, PNG, SVG, PDF
    }

    static public enum ProvFormatType {
        INPUT, OUTPUT, INPUTOUTPUT
    }


    static Logger logger = Logger.getLogger(InteropFramework.class);
    public static final String UNKNOWN = "unknown";
    final Utility u = new Utility();

    final ProvFactory pFactory;
    final Ontology onto;
    final private String verbose;
    final private String debug;
    final private String logfile;
    final private String infile;
    final private String informat;
    final private String outfile;
    final private String outformat;
    final private String namespaces;
    final private String title;

    final private String layout;
    final private String bindings;
    final private String bindingformat;
    public final Hashtable<ProvFormat, String> extensionMap;
    public final Hashtable<String, ProvFormat> extensionRevMap;
    public final Hashtable<ProvFormat, String> mimeTypeMap;

    public final Hashtable<String, ProvFormat> mimeTypeRevMap;

    public final Hashtable<ProvFormat, ProvFormatType> provTypeMap;

    final private String generator;
    final private String index;
    final private String merge;
    final private String flatten;
    final private boolean addOrderp;
    final private boolean allExpanded;
    final private String compare;
    final private String compareOut;
    final private int bindingsVersion;
    final private String template;
    final private String packge;
    final private String location;

    final private boolean builder;
    final private String template_builder;

    /** Default constructor for the ProvToolbox interoperability framework.
     * It uses {@link org.openprovenance.prov.xml.ProvFactory} as its default factory. 
     */
    public InteropFramework() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, 1, false, false, null, false, null, null, null, null, null, null, null,null,null,
                org.openprovenance.prov.xml.ProvFactory.getFactory());
    }

    public InteropFramework(ProvFactory pFactory) {
        this(null, null, null, null, null, null, null, null, null, null, null, null, 1, false, false, null, false, null, null, null, null, null, null, null,null,null,
                pFactory);
    }


    public InteropFramework(String verbose, String debug, String logfile,
            String infile, String informat, String outfile, String outformat, String namespaces, String title,
            String layout, String bindings, String bindingformat, int bindingsVersion, boolean addOrderp, boolean allExpanded, String template, boolean builder, String template_builder, String packge, String location, String generator,
            String index, String merge, String flatten, String compare, String compareOut, ProvFactory pFactory) {
        this.verbose = verbose;
        this.debug = debug;
        this.logfile = logfile;
        this.infile = infile;
        this.outfile = outfile;
        this.namespaces = namespaces;
        this.title = title;
        this.layout = layout;
        this.bindings = bindings;
        this.generator = generator;
        this.index=index;
        this.merge=merge;
        this.flatten=flatten;
        this.pFactory = pFactory;
        this.addOrderp=addOrderp;
        this.allExpanded=allExpanded;
        this.onto = new Ontology(pFactory);
        this.informat = informat;
        this.outformat = outformat;
        this.bindingformat = bindingformat;
        this.compare=compare;
        this.compareOut=compareOut;
        this.bindingsVersion=bindingsVersion;
        this.template=template;
        this.packge=packge;
        this.location=location;
        this.builder=builder;
        this.template_builder=template_builder;

        extensionMap = new Hashtable<InteropFramework.ProvFormat, String>();
        extensionRevMap = new Hashtable<String, InteropFramework.ProvFormat>();
        mimeTypeMap = new Hashtable<InteropFramework.ProvFormat, String>();
        mimeTypeRevMap = new Hashtable<String, InteropFramework.ProvFormat>();
        provTypeMap = new Hashtable<InteropFramework.ProvFormat, InteropFramework.ProvFormatType>();

        initializeExtensionMap(extensionMap, extensionRevMap);
    }

    /**
     * Create a list of mime types supported by ProvToolbox in view of constructing an Accept Header. 
     * @return a string representing the mime types.
     */
    public String buildAcceptHeader() {
        StringBuffer mimetypes = new StringBuffer();
        Enumeration<ProvFormat> e = mimeTypeMap.keys();
        String sep = "";
        while (e.hasMoreElements()) {
            ProvFormat f = e.nextElement();
            if (isInputFormat(f)) {
                // careful - cant use .hasMoreElements as we are filtering
                mimetypes.append(sep);
                sep = ",";
                mimetypes.append(mimeTypeMap.get(f));
            }
        }
        mimetypes.append(sep);
        mimetypes.append("*/*;q=0.1"); // be liberal

        return mimetypes.toString();
    }

    /**
     * A method to connect to a URL and follow redirects if any.
     * @param theURL a URL to connect to
     * @return a {@link URLConnection}
     * @throws IOException if connection cannot be opened and no response is received.
     */
    public URLConnection connectWithRedirect(URL theURL) throws IOException {
        URLConnection conn = null;

        String accept_header = buildAcceptHeader();
        int redirect_count = 0;
        boolean done = false;

        while (!done) {
            if (theURL.getProtocol().equals("file")) {
                return null;
            }
            Boolean isHttp = (theURL.getProtocol().equals("http") || theURL
                    .getProtocol().equals("https"));

            logger.debug("Requesting: " + theURL.toString());

            conn = theURL.openConnection();

            if (isHttp) {
                logger.debug("Accept: " + accept_header);
                conn.setRequestProperty("Accept", accept_header);
            }

            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);

            conn.connect();

            done = true; // by default quit after one request

            if (isHttp) {
                logger.debug("Response: " + conn.getHeaderField(0));
                int rc = ((HttpURLConnection) conn).getResponseCode();

                if ((rc == HttpURLConnection.HTTP_MOVED_PERM)
                        || (rc == HttpURLConnection.HTTP_MOVED_TEMP)
                        || (rc == HttpURLConnection.HTTP_SEE_OTHER)
                        || (rc == 307)) {
                    if (redirect_count > 10) {
                        return null; // Error: too many redirects
                    }
                    redirect_count++;
                    String loc = conn.getHeaderField("Location");
                    if (loc != null) {
                        theURL = new URL(loc);
                        done = false;
                    } else {
                        return null; // Bad redirect
                    }
                } else if ((rc < 200) || (rc >= 300)) {
                    return null; // Unsuccessful
                }
            }
        }
        return conn;
    }

  

    
    /**Maps an file extension to a Media type
     * @param extension the extension of a file containing a serialization of PROV
     * @return a String for the Internet Media type corresponding to a file with this extension
     */
    public String convertExtensionToMediaType(String extension) {
        ProvFormat format = extensionRevMap.get(extension);
        if (format == null)
            return null;
        return mimeTypeMap.get(format);
    }

    /** Returns an extension for a given type of serialization of PROV
     * @param format {@link ProvFormat} for which file extension is sought
     * @return a String for the extension of a file containing such a serialization
     */
    public String getExtension(ProvFormat format) {
        String extension = UNKNOWN;
        if (format != null) {
            extension = extensionMap.get(format);
        }
        return extension;
    }

    /**
     * Returns an option at given index in an array of options, or null
     * @param options an array of Strings
     * @param index position of the option that is sought
     * @return the option or null
     */
    String getOption(String[] options, int index) {
        if ((options != null) && (options.length > index)) {
            return options[index];
        }
        return null;
    }

    /**
     * Get a {@link ProvFormat} given the file's exetension
     * @param filename the file for which the {@link ProvFormat} is sought
     * @return a {@link ProvFormat}
     */
    public ProvFormat getTypeForFile(String filename) {
        int count = filename.lastIndexOf(".");
        if (count == -1)
            return null;
        String extension = filename.substring(count + 1);
        return extensionRevMap.get(extension);
    }

    /**
     * Get a {@link ProvFormat} given a format string
     * @param format the format for which the {@link ProvFormat} is sought
     * @return a {@link ProvFormat}
     */
    public ProvFormat getTypeForFormat(String format) {
        ProvFormat result;
        // try as mimetype and then as an extension
        result = mimeTypeRevMap.get(format);
        if (result == null)
            result = extensionRevMap.get(format);
        return result;
    }

    /**
     * Support for content negotiation, jax-rs style. Create a list of media
     * type supported by the framework.
     * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/gkqbq.html">Content Negotiation</a>
     */
    
    public List<Variant> getVariants() {
        List<Variant> vs = new ArrayList<Variant>();
        for (Map.Entry<String, ProvFormat> entry : mimeTypeRevMap.entrySet()) {
            if (isOutputFormat(entry.getValue())) {
                String[] parts = entry.getKey().split("/");
                MediaType m = new MediaType(parts[0], parts[1]);
                vs.add(new Variant(m, (java.util.Locale) null, (String) null));
            }
        }
        return vs;
    }

    /** Initialization function
     * @param extensionMap mapping of {@link ProvFormat} to extensions
     * @param extensionRevMap reverse mapping of extensions to {@link ProvFormat}
     */
    public void initializeExtensionMap(Hashtable<ProvFormat, String> extensionMap,
                                       Hashtable<String, InteropFramework.ProvFormat> extensionRevMap) {
        for (ProvFormat f : ProvFormat.values()) {
            switch (f) {
            case DOT:
                extensionMap.put(ProvFormat.DOT, EXTENSION_DOT);
                extensionRevMap.put(EXTENSION_DOT, ProvFormat.DOT);
                extensionRevMap.put("gv", ProvFormat.DOT);
                mimeTypeMap.put(ProvFormat.DOT, MEDIA_TEXT_VND_GRAPHVIZ);
                mimeTypeRevMap.put(MEDIA_TEXT_VND_GRAPHVIZ, ProvFormat.DOT);
                provTypeMap.put(ProvFormat.DOT, ProvFormatType.OUTPUT);
                break;
            case JPEG:
                extensionMap.put(ProvFormat.JPEG, EXTENSION_JPG);
                extensionRevMap.put(EXTENSION_JPEG, ProvFormat.JPEG);
                extensionRevMap.put(EXTENSION_JPG, ProvFormat.JPEG);
                mimeTypeMap.put(ProvFormat.JPEG, MEDIA_IMAGE_JPEG);
                mimeTypeRevMap.put(MEDIA_IMAGE_JPEG, ProvFormat.JPEG);
                provTypeMap.put(ProvFormat.JPEG, ProvFormatType.OUTPUT);
                break;
            case PNG:
                extensionMap.put(ProvFormat.PNG, EXTENSION_PNG);
                extensionRevMap.put(EXTENSION_PNG, ProvFormat.PNG);
                mimeTypeMap.put(ProvFormat.PNG, MEDIA_IMAGE_PNG);
                mimeTypeRevMap.put(MEDIA_IMAGE_PNG, ProvFormat.PNG);
                provTypeMap.put(ProvFormat.PNG, ProvFormatType.OUTPUT);
                break;
            case JSON:
                extensionMap.put(ProvFormat.JSON, EXTENSION_JSON);
                extensionRevMap.put(EXTENSION_JSON, ProvFormat.JSON);
                mimeTypeMap.put(ProvFormat.JSON, MediaType.APPLICATION_JSON);
                mimeTypeRevMap.put(MediaType.APPLICATION_JSON, ProvFormat.JSON);
                provTypeMap.put(ProvFormat.JSON, ProvFormatType.INPUTOUTPUT);
                break;
            case PDF:
                extensionMap.put(ProvFormat.PDF, EXTENSION_PDF);
                extensionRevMap.put(EXTENSION_PDF, ProvFormat.PDF);
                mimeTypeMap.put(ProvFormat.PDF, MEDIA_APPLICATION_PDF);
                mimeTypeRevMap.put(MEDIA_APPLICATION_PDF, ProvFormat.PDF);
                provTypeMap.put(ProvFormat.PDF, ProvFormatType.OUTPUT);
                break;
            case PROVN:
                extensionMap.put(ProvFormat.PROVN, EXTENSION_PROVN);
                extensionRevMap.put(EXTENSION_PROVN, ProvFormat.PROVN);
                extensionRevMap.put("pn", ProvFormat.PROVN);
                extensionRevMap.put("asn", ProvFormat.PROVN);
                extensionRevMap.put("prov-asn", ProvFormat.PROVN);
                mimeTypeMap.put(ProvFormat.PROVN,
                                MEDIA_TEXT_PROVENANCE_NOTATION);
                mimeTypeRevMap.put(MEDIA_TEXT_PROVENANCE_NOTATION,
                                   ProvFormat.PROVN);
                provTypeMap.put(ProvFormat.PROVN, ProvFormatType.INPUTOUTPUT);
                break;
            case RDFXML:
                extensionMap.put(ProvFormat.RDFXML, EXTENSION_RDF);
                extensionRevMap.put(EXTENSION_RDF, ProvFormat.RDFXML);
                mimeTypeMap.put(ProvFormat.RDFXML, MEDIA_APPLICATION_RDF_XML);
                mimeTypeRevMap
                        .put(MEDIA_APPLICATION_RDF_XML, ProvFormat.RDFXML);
                provTypeMap.put(ProvFormat.RDFXML, ProvFormatType.INPUTOUTPUT);
                break;
            case SVG:
                extensionMap.put(ProvFormat.SVG, EXTENSION_SVG);
                extensionRevMap.put(EXTENSION_SVG, ProvFormat.SVG);
                mimeTypeMap.put(ProvFormat.SVG, MEDIA_IMAGE_SVG_XML);
                mimeTypeRevMap.put(MEDIA_IMAGE_SVG_XML, ProvFormat.SVG);
                provTypeMap.put(ProvFormat.SVG, ProvFormatType.OUTPUT);
                break;
            case TRIG:
                extensionMap.put(ProvFormat.TRIG, EXTENSION_TRIG);
                extensionRevMap.put(EXTENSION_TRIG, ProvFormat.TRIG);
                mimeTypeMap.put(ProvFormat.TRIG, MEDIA_APPLICATION_TRIG);
                mimeTypeRevMap.put(MEDIA_APPLICATION_TRIG, ProvFormat.TRIG);
                provTypeMap.put(ProvFormat.TRIG, ProvFormatType.INPUTOUTPUT);
                break;
            case TURTLE:
                extensionMap.put(ProvFormat.TURTLE, EXTENSION_TTL);
                extensionRevMap.put(EXTENSION_TTL, ProvFormat.TURTLE);
                mimeTypeMap.put(ProvFormat.TURTLE, MEDIA_TEXT_TURTLE);
                mimeTypeRevMap.put(MEDIA_TEXT_TURTLE, ProvFormat.TURTLE);
                provTypeMap.put(ProvFormat.TURTLE, ProvFormatType.INPUTOUTPUT);
                break;
            case XML:
                extensionMap.put(ProvFormat.XML, EXTENSION_PROVX);
                extensionRevMap.put(EXTENSION_PROVX, ProvFormat.XML);
                extensionRevMap.put(EXTENSION_XML, ProvFormat.XML);
                mimeTypeMap.put(ProvFormat.XML,
                                MEDIA_APPLICATION_PROVENANCE_XML);
                mimeTypeRevMap.put(MEDIA_APPLICATION_PROVENANCE_XML,
                                   ProvFormat.XML);
                provTypeMap.put(ProvFormat.XML, ProvFormatType.INPUTOUTPUT);
                break;
            default:
                break;

            }
        }

    }

    /**
     * Determines whether this format received as argument is an input format.
     * @param format a {@link ProvFormat}
     * @return true if format is an input format
     */
    public Boolean isInputFormat(ProvFormat format) {
        ProvFormatType t = provTypeMap.get(format);
        return (t.equals(ProvFormatType.INPUT) || 
                t.equals(ProvFormatType.INPUTOUTPUT));
    }


    /**
     * Determines whether this format received as argument is an output format.
     * @param format a {@link ProvFormat}
     * @return true if format is an output format
     */
    public Boolean isOutputFormat(ProvFormat format) {
        ProvFormatType t = provTypeMap.get(format);
        return (t.equals(ProvFormatType.OUTPUT) ||
                t.equals(ProvFormatType.INPUTOUTPUT));
    }

    
    /** Experimental code, trying to load a document without knowing its serialization format. 
     * First parser that succeeds returns a results. Not a robust method!
     * @param filename a file to load the provenance document from
     * @return a document
     */
    public Object loadProvUnknownGraph(String filename) {

        try {
            Utility u = new Utility();
            CommonTree tree = u.convertASNToTree(filename);
            Object o = u.convertTreeToJavaBean(tree, pFactory);
            if (o != null) {
                return o;
            }
        } catch (RecognitionException t1) {
            // OK, we failed, let's try next format.
        } catch (IOException e) {
            // OK, we failed, let's try next format.
        }
        try {
            File in = new File(filename);
            ProvDeserialiser deserial = ProvDeserialiser
                    .getThreadProvDeserialiser();
            Document c = deserial.deserialiseDocument(in);
            if (c != null) {
                return c;
            }
        } catch (JAXBException t2) {
            // OK, we failed, let's try next format.
        }

        try {
            Document o = new org.openprovenance.prov.json.Converter(pFactory)
                    .readDocument(filename);
            if (o != null) {
                return o;
            }
        } catch (IOException e) {
            // OK, we failed, let's try next format.

        }
        try {
            org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                    pFactory, onto);
            Document doc = rdfU.parseRDF(filename);
            if (doc != null) {
                return doc;
            }
        } catch (RDFParseException e) {
        } catch (RDFHandlerException e) {
        } catch (IOException e) {
        } catch (JAXBException e) {
        }
        System.out.println("Unparseable format " + filename);
        throw new UnsupportedOperationException();

    }
    
    /** Creates a factory for the XML Java beans. 
     * 
     * @return a {@link ProvFactory}
     */
    
    static public ProvFactory newXMLProvFactory() {
        return org.openprovenance.prov.xml.ProvFactory.getFactory();
    }
    
    public void provn2html(String file, String file2)
            throws java.io.IOException, JAXBException, RecognitionException {
        Document doc = (Document) u.convertASNToJavaBean(file, pFactory);
        String s = u.convertBeanToHTML(doc, pFactory);
        u.writeTextToFile(s, file2);
    }
    
    /**
     * Reads a Document from an input stream, using the parser specified by the format argument.
     * @param is an input stream
     * @param format one of the input formats supported by ProvToolbox
     * @param baseuri a base uri for the input stream document	
     * @return a Document
     */

    public Document readDocument(InputStream is, ProvFormat format, String baseuri) {
        return readDocument(is, format, pFactory, baseuri);
    }

    /**
     * Reads a Document from an input stream, using the parser specified by the format argument.
     * @param is an input stream
     * @param format one of the input formats supported by ProvToolbox
     * @param pFactory a provenance factory used to construct the Document
     * @param baseuri a base uri for the input stream document
     * @return a Document
     */

  
    public Document readDocument(InputStream is, 
                                 ProvFormat format,
                                 ProvFactory pFactory,
                                 String baseuri) {
        try {

            switch (format) {
            case DOT:
            case JPEG:
            case PNG:
            case SVG:
                throw new UnsupportedOperationException(); // we don't load PROV
                // from these
                // formats
            case JSON: {
                return new org.openprovenance.prov.json.Converter(pFactory)
                        .readDocument(is);
            }
            case PROVN: {
                Utility u = new Utility();
                CommonTree tree = u.convertASNToTree(is);
                Object o = u.convertTreeToJavaBean(tree, pFactory);
                Document doc = (Document) o;
                // Namespace ns=Namespace.gatherNamespaces(doc);
                // doc.setNamespace(ns);
                return doc;
            }
            case RDFXML: {
                org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                        pFactory, onto);
                return rdfU.parseRDF(is, RDFFormat.RDFXML, baseuri);
            }
            case TRIG: {
                org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                        pFactory, onto);
                return rdfU.parseRDF(is, RDFFormat.TRIG,baseuri);
            }
            case TURTLE: {
                org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                        pFactory, onto);
                return rdfU.parseRDF(is, RDFFormat.TURTLE,baseuri);
            }
            case XML: {
                ProvDeserialiser deserial = ProvDeserialiser
                        .getThreadProvDeserialiser();
                Document doc = deserial.deserialiseDocument(is);
                return doc;
            }
            default: {
                System.out.println("Unknown format " + format);
                throw new UnsupportedOperationException();
            }
            }
        } catch (IOException e) {
            throw new InteropException(e);
        } catch (JAXBException e) {
            throw new InteropException(e);
        } catch (RecognitionException e) {
            throw new InteropException(e);
        } catch (RDFParseException e) {
            throw new InteropException(e);
        } catch (RDFHandlerException e) {
            throw new InteropException(e);
        }

    }

    /**
     * Reads a document from a URL. Uses the Content-type header field to determine the 
     * mime-type of the resource, and therefore the parser to read the document. 
     * @param url a URL
     * @return a Document
     */
    public Document readDocument(String url) {
        try {
            URL theURL = new URL(url);
            URLConnection conn = connectWithRedirect(theURL);
            if (conn == null)
                return null;

            ProvFormat format = null;
            String content_type = conn.getContentType();

            logger.debug("Content-type: " + content_type);
            if (content_type != null) {
                // Need to trim optional parameters
                // Content-Type: text/plain; charset=UTF-8
                int end = content_type.indexOf(";");
                if (end < 0) {
                    end = content_type.length();
                }
                String actual_content_type = content_type.substring(0, end)
                        .trim();
                logger.debug("Found Content-type: " + actual_content_type);
                // TODO: might be worth skipping if text/plain as that seems
                // to be the
                // default returned by unconfigured web servers
                format = mimeTypeRevMap.get(actual_content_type);
            }
            logger.debug("Format after Content-type: " + format);

            if (format == null) {
                format = getTypeForFile(theURL.toString());
            }
            logger.debug("Format after extension: " + format);

            InputStream content_stream = conn.getInputStream();

            return readDocument(content_stream, format,url);
        } catch (IOException e) {
            throw new InteropException(e);
        }
    }
    

    /**
     * Reads a document from a file, using the file extension to decide which parser to read the file with.
     * @param filename the file to read a document from
     * @return a Document
     */
    public Document readDocumentFromFile(String filename) {

            ProvFormat format = getTypeForFile(filename);
            if (format == null) {
                throw new InteropException("Unknown output file format: "
                        + filename);
            }
            return readDocumentFromFile(filename, format);
    }

    /**
     * Reads a document from a file, using the format to decide which parser to read the file with.
     * @param filename the file to read a document from
     * @param format the format of the file
     * @return a Document
     */
    public Document readDocumentFromFile(String filename, ProvFormat format) {

        try {
            switch (format) {
            case DOT:
            case JPEG:
            case PNG:
            case SVG:
                throw new UnsupportedOperationException(); // we don't load PROV
                                                           // from these
                                                           // formats
            case JSON: {
                return new org.openprovenance.prov.json.Converter(pFactory)
                        .readDocument(filename);
            }

            case PROVN: {
                Utility u = new Utility();
                CommonTree tree = u.convertASNToTree(filename);
                Object o = u.convertTreeToJavaBean(tree, pFactory);
                Document doc = (Document) o;
                // Namespace ns=Namespace.gatherNamespaces(doc);
                // doc.setNamespace(ns);
                return doc;
            }
            case RDFXML:
            case TRIG:
            case TURTLE: {
                org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(
                        pFactory, onto);
                Document doc = rdfU.parseRDF(filename);
                return doc;
            }
            case XML: {
                File in = new File(filename);
                ProvDeserialiser deserial = ProvDeserialiser
                        .getThreadProvDeserialiser();
                Document doc = deserial.deserialiseDocument(in);
                return doc;
            }
            default: {
                System.out.println("Unknown format " + filename);
                throw new UnsupportedOperationException();
            }
            }
        } catch (IOException e) {
            throw new InteropException(e);
        } catch (RDFParseException e) {
            throw new InteropException(e);
        } catch (RDFHandlerException e) {
            throw new InteropException(e);
        } catch (JAXBException e) {
            throw new InteropException(e);
        } catch (RecognitionException e) {
            throw new InteropException(e);
        }

    }
    public java.util.List<java.util.Map<String, String>>getSupportedFormats() {
        java.util.List<java.util.Map<String, String>> tripleList = new java.util.ArrayList<>();
        java.util.Map<String, String>trip;
        for (InteropFramework.ProvFormat pt:  provTypeMap.keySet()) {
            for (String mt:  mimeTypeRevMap.keySet()) {
                if (mimeTypeRevMap.get(mt) == pt) {
                    for (String ext: extensionRevMap.keySet()) {
                        if (extensionRevMap.get(ext) == pt) {
                            if (isInputFormat(pt)) {
                                trip = new java.util.HashMap<String, String>();
                                trip.put("extension", ext);
                                trip.put("mediatype", mt);
                                trip.put("type", "input");
                                tripleList.add(trip);
                            }
                            if (isOutputFormat(pt)) {
                                trip = new java.util.HashMap<String, String>();
                                trip.put("extension", ext);
                                trip.put("mediatype", mt);
                                trip.put("type", "output");
                                tripleList.add(trip);
                            }
                        }
                    }
                }
            }
        }
        return tripleList;
    }

    private Document doReadDocument(String filename, String format)
    {
        Document doc;
        ProvFormat informat;
        if (format != null) {
            informat = getTypeForFormat(format);
            if (informat == null) {
                throw new InteropException("Unknown format: "
                        + format);
            }
        } else {
            informat = getTypeForFile(filename);
            if (informat == null) {
                throw new InteropException("Unknown file format for: "
                        + filename);
            }
        }

        if (filename == "-") {
            if (informat == null) {
                throw new InteropException("File format for standard input not specified");
            }
            doc = (Document) readDocument(System.in, informat,"file://stdin/");
        } else {
            doc = (Document) readDocumentFromFile(filename, informat);
        }

        return doc;
    }

    private void doWriteDocument(String filename, String format, Document doc)
    {
        ProvFormat outformat;
        if (format != null) {
            outformat = getTypeForFormat(format);
            if (outformat == null) {
                throw new InteropException("Unknown format: "
                        + format);
            }
        } else {
            outformat = getTypeForFile(filename);
            if (outformat == null) {
                throw new InteropException("Unknown file format for: "
                        + filename);
            }
        }

        if (filename == "-") {
            if (outformat == null) {
                throw new InteropException("File format for standard output not specified");
            }
            writeDocument(System.out, outformat, doc);
        } else {
            writeDocument(filename, outformat, doc);
        }
    }
    
    static String SEPARATOR=",";
    enum FileKind { FILE , URL };
    
    class ToRead {
    	FileKind kind;
    	String url;
    	ProvFormat format;

    	public String toString () {
    		return "[" + kind + "," + url + "," + format + "]"; 
    	}
    	
		ToRead(FileKind kind, String url, ProvFormat format) {
    		this.kind=kind;
    		this.url=url;
    		this.format=format;
    	}
    }
    
    public Document readDocument(ToRead something) {
    	Document doc=null;
    	switch (something.kind) {
		case FILE:
			doc=readDocumentFromFile(something.url, something.format);
			break;
		case URL:
			doc=readDocument(something.url);// note: ignore format?
			break;
    	}
    	return doc;
    }
    
    private List<ToRead> readIndexFile(File fin) throws IOException {
    	FileInputStream fis = new FileInputStream(fin);
    	return readIndexFile(fis);
    }
    
    
    private List<ToRead> readIndexFile(InputStream is) throws IOException {
    	List<ToRead> res=new LinkedList<InteropFramework.ToRead>();
     
    	BufferedReader br = new BufferedReader(new InputStreamReader(is));
     
    	String line = null;
    	while ((line = br.readLine()) != null) {
    		String [] parts=line.split(SEPARATOR);
    		if (parts.length>=3) {
    			FileKind kind=parts[0].trim().equals("URL") ? FileKind.URL : FileKind.FILE;
    			String path=parts[1].trim();
    			ProvFormat format=getTypeForFormat(parts[2].trim());
    			ToRead elem=new ToRead(kind, path, format);
    			res.add(elem);
    		} else if (parts.length==1) {
    			String filename=parts[0].trim();
    			ToRead elem=new ToRead(FileKind.FILE, filename, getTypeForFile(filename));
    			res.add(elem);
    		}
    	}     
    	br.close();
    	return res;
    }


    
    /**
     * Top level entry point of this class, when called from the command line.
     * <p>
     * See method {@link CommandLineArguments#main(String[])}
     */

    public int run() {
        if (outfile == null && compare == null && template_builder == null)
            return CommandLineArguments.STATUS_NO_OUTPUT_OR_COMPARISON;
        if (infile == null && generator == null && template_builder == null && merge == null)
            return CommandLineArguments.STATUS_NO_INPUT;

        if ((infile == "-") && (bindings == "-"))
            throw new InteropException(
                    "Cannot use standard input for both infile and bindings");

        Document doc;
        if (infile != null) {
            doc = doReadDocument(infile, informat);
        } else if (merge != null) {
            IndexedDocument iDoc = new IndexedDocument(pFactory,
                                                       pFactory.newDocument(),
                                                       flatten != null);
            try {
                List<ToRead> files;
                if (merge.equals("-")) {
                    files = readIndexFile(System.in);
                } else {
                    files = readIndexFile(new File(merge));
                }
                //System.err.println("files to merge " + files);
                for (ToRead something : files) {
                    iDoc.merge(readDocument(something));
                }

            } catch (IOException e) {
                System.err.println("problem reading index file");
                e.printStackTrace();

            }
            doc = iDoc.toDocument();

        } else if (template_builder!=null) {
            return processTemplateGenerationConfig(template_builder);
            
        } else {

            String[] options = generator.split(":");
            String noOfNodes = getOption(options, 0);
            String noOfEdges = getOption(options, 1);
            String firstNode = getOption(options, 2);
            String namespace = "http://expample.org/";
            String seed = getOption(options, 3);
            String term = getOption(options, 4);

            if (term == null)
                term = "e1";

            GeneratorDetails gd = new GeneratorDetails(
                                                       Integer.valueOf(noOfNodes),
                                                       Integer.valueOf(noOfEdges),
                                                       firstNode,
                                                       namespace,
                                                       (seed == null) ? null
                                                               : Long.valueOf(seed),
                                                               term);
            System.err.println(gd);
            GraphGenerator gg = new GraphGenerator(gd, pFactory);
            gg.generateElements();

            doc = gg.getDetails().getDocument();
        }

        if (compare!=null) {
            return doCompare(doc,doReadDocument(compare, informat));
        } 
        
        if (template!=null  && !builder) {
            BindingsBeanGenerator bbgen=new BindingsBeanGenerator(pFactory);
            
            boolean val=bbgen.generate(doc, template, packge, outfile, location);
            return (val) ? 0 : CommandLineArguments.STATUS_BEAN_GENERATION;
      }
        
        if (template!=null  && builder) {
            JsonNode bindings_schema=null;
            if (bindings != null && bindingsVersion>=3) {
                try {
                    bindings_schema = mapper.readTree(new File(bindings));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            TemplateBuilderGenerator tbg=new TemplateBuilderGenerator(pFactory);
            
            tbg.generate(doc, template, packge, outfile, location,bindings_schema);
            return CommandLineArguments.STATUS_OK;
        }

        if (index != null) {
            Document indexedDoc = new IndexedDocument(pFactory, doc,
                                                      (flatten != null)).toDocument();
            doc = indexedDoc;
        }
        if (bindings != null) {
            Expand myExpand=new Expand(pFactory, addOrderp,allExpanded);
            Document expanded;
            if (bindingsVersion==3) {
                Bindings bb=BindingsJson.fromBean(BindingsJson.importBean(new File(bindings)),pFactory);
                expanded = myExpand.expander(doc,
                                             bb);

            } else {
                Document docBindings = (Document) doReadDocument(bindings,
                                                                 bindingformat);
                expanded = myExpand.expander(doc,
                                             outfile,
                                             docBindings);
            }
            boolean flag=myExpand.getAllExpanded();
            doWriteDocument(outfile, outformat, expanded);

            if (!flag) {
                return CommandLineArguments.STATUS_TEMPLATE_UNBOUND_VARIABLE;
            }
        } else {
            doWriteDocument(outfile, outformat, doc);
        }
        return CommandLineArguments.STATUS_OK;

    }
    static ObjectMapper mapper = new ObjectMapper();

    private int doCompare(Document doc1, Document doc2) {
        if (doc1==null) return CommandLineArguments.STATUS_COMPARE_NO_ARG1;
        if (doc2==null) return CommandLineArguments.STATUS_COMPARE_NO_ARG2;

        //System.out.println("doCompare()");
        PrintStream ps=System.out;
        try {
            if (!(compareOut==null || compareOut.equals("-"))) {
                ps=new PrintStream(compareOut);
            }
        } catch (FileNotFoundException e) {
            // ok, we ignore the exception, we continue with stdout
        }
        DocumentEquality comparator=new DocumentEquality(false,ps);
        logger.debug("about to compare two docs");
        if (comparator.check(doc1, doc2)) {
            //System.out.println("doCompare(): Success");

            return CommandLineArguments.STATUS_OK;
        }	    
        //System.out.println("doCompare(): Failure");
        return CommandLineArguments.STATUS_COMPARE_DIFFERENT;
    }

    /** Initializes a Document's namespace. */

    public void setNamespaces(Document doc) {
        if (doc.getNamespace() == null)
            doc.setNamespace(new Namespace());

    }
    
    /**
     * Write a {@link Document} to output stream, according to specified Internet Media Type
     * @param os an {@link OutputStream} to write the Document to
     * @param mt a {@link MediaType}
     * @param document a {@link Document} to serialize
     */

    public void writeDocument(OutputStream os, MediaType mt, Document document) {
        ProvFormat format = mimeTypeRevMap.get(mt.toString());
        writeDocument(os, format, document);
    }

    
    /**
     * Write a {@link Document} to output stream, according to specified {@link ProvFormat}
     * @param os an {@link OutputStream} to write the Document to
     * @param format a {@link ProvFormat}
     * @param document a {@link Document} to serialize
     */

    public void writeDocument(OutputStream os, ProvFormat format, Document document) {

        Namespace.withThreadNamespace(document.getNamespace());

        try {
            if (format == null) {
                System.err.println("Unknown output format: " + format);
                return;
            }
            logger.debug("writing " + format);
            setNamespaces(document);
            switch (format) {
            case PROVN: {
                u.writeDocument(document, os, pFactory);
                break;
            }
            case XML: {
                org.openprovenance.prov.model.ProvSerialiser serial = pFactory
                        .getSerializer();
                logger.debug("namespaces " + document.getNamespace());
                serial.serialiseDocument(os, document, true);
                break;
            }
            case TURTLE: {
                new org.openprovenance.prov.rdf.Utility(pFactory, onto)
                        .dumpRDF(document, RDFFormat.TURTLE, os);
                break;
            }
            case RDFXML: {
                new org.openprovenance.prov.rdf.Utility(pFactory, onto)
                        .dumpRDF(document, RDFFormat.RDFXML, os);
                break;
            }
            case TRIG: {
                new org.openprovenance.prov.rdf.Utility(pFactory, onto)
                        .dumpRDF(document, RDFFormat.TRIG, os);
                break;
            }
            case JSON: {
                new org.openprovenance.prov.json.Converter(pFactory)
                        .writeDocument(document, new OutputStreamWriter(os));
                break;
            }
            case DOT: {
                String configFile = null; // TODO: get it as option
                ProvToDot toDot = (configFile == null) ? new ProvToDot(
                        ProvToDot.Config.ROLE_NO_LABEL) : new ProvToDot(
                        configFile);
                toDot.convert(document, os, title);
                break;
            }
            case PDF:
            case JPEG:
            case PNG:
            case SVG: {
                String configFile = null; // give it as option
                File tmp = File.createTempFile("viz-", ".dot");

                String dotFileOut = tmp.getAbsolutePath(); // give it as option,
                                                           // if not available
                                                           // create tmp file
                ProvToDot toDot;
                if (configFile != null) {
                    toDot = new ProvToDot(configFile);
                } else {
                    toDot = new ProvToDot(ProvToDot.Config.ROLE_NO_LABEL);
                }

                toDot.convert(document, dotFileOut, os, extensionMap.get(format), title);
                tmp.delete();
                break;
            }

            default:
                break;
            }
        } catch (JAXBException e) {
            if (verbose != null)
                e.printStackTrace();
            throw new InteropException(e);

        } catch (Exception e) {
            if (verbose != null)
                e.printStackTrace();
            throw new InteropException(e);
        }

    }

    
    /**
     * Write a {@link Document} to file, serialized according to the file extension
     * @param filename path of the file to write the Document to
     * @param document a {@link Document} to serialize
     */

    public void writeDocument(String filename, Document document) {
            ProvFormat format = getTypeForFile(filename);
            if (format == null) {
                System.err.println("Unknown output file format: " + filename);
                return;
            }
        writeDocument(filename, format, document);
    }
    /**
     * Write a {@link Document} to file, serialized according to the file extension
     * @param filename path of the file to write the Document to
     * @param document a {@link Document} to serialize
     */

    public void writeDocument(String filename, ProvFormat format, Document document) {
        Namespace.withThreadNamespace(document.getNamespace());
        try {
            logger.debug("writing " + format);
            logger.debug("writing " + filename);
            setNamespaces(document);
            switch (format) {
            case PROVN: {
                u.writeDocument(document, filename, pFactory);
                break;
            }
            case XML: {
                ProvSerialiser serial = ProvSerialiser
                        .getThreadProvSerialiser();
                logger.debug("namespaces " + document.getNamespace());
                serial.serialiseDocument(new File(filename), document, true);
                break;
            }
            case TURTLE: {
                new org.openprovenance.prov.rdf.Utility(pFactory, onto)
                        .dumpRDF(document, RDFFormat.TURTLE, filename);
                break;
            }
            case RDFXML: {
                new org.openprovenance.prov.rdf.Utility(pFactory, onto)
                        .dumpRDF(document, RDFFormat.RDFXML, filename);
                break;
            }
            case TRIG: {
                new org.openprovenance.prov.rdf.Utility(pFactory, onto)
                        .dumpRDF(document, RDFFormat.TRIG, filename);
                break;
            }
            case JSON: {
                new org.openprovenance.prov.json.Converter(pFactory)
                        .writeDocument(document, filename);
                break;
            }
            case DOT: {
                String configFile = null; // TODO: get it as option
                ProvToDot toDot = (configFile == null) ? new ProvToDot(
                        ProvToDot.Config.ROLE_NO_LABEL) : new ProvToDot(
                        configFile);
                toDot.setLayout(layout);
                toDot.convert(document, filename, title);
                break;
            }
            case PDF:
            case JPEG:
            case PNG:
            case SVG: {
                String configFile = null; // give it as option
                File tmp = File.createTempFile("viz-", ".dot");

                String dotFileOut = tmp.getAbsolutePath(); // give it as option,
                                                           // if not available
                                                           // create tmp file
                ProvToDot toDot;
                if (configFile != null) {
                    toDot = new ProvToDot(configFile);
                } else {
                    toDot = new ProvToDot(ProvToDot.Config.ROLE_NO_LABEL);
                }
                toDot.setLayout(layout);
                toDot.convert(document, dotFileOut, filename, extensionMap.get(format), title);
                tmp.delete();
                break;
            }

            default:
                break;
            }
        } catch (JAXBException e) {
            if (verbose != null)
                e.printStackTrace();
            throw new InteropException(e);

        } catch (IOException e) {
            if (verbose != null)
                e.printStackTrace();
            throw new InteropException(e);
        }

    }
    
    public static int processTemplateGenerationConfig(String template_builder) {
        ObjectMapper objectMapper = new ObjectMapper();
        GeneratorConfig[] configs;
        try {
            configs = objectMapper.readValue(new File(template_builder), GeneratorConfig[].class);
            for (GeneratorConfig config: configs) {
                System.out.println(config.toString());
                // provconvert -infile templates/grow.provn -template grow -builder -package foo -bindings bindings/grow_bs.json -bindver 3 -outfile src/main/java

                CommandLineArguments.mainExit(new String[]{"-infile", config.template, "-template", config.name, "-builder", "-package", config.package_, "-bindings", config.bindings, "-bindver", "3", "-outfile", config.destination}, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    

}
