package org.openprovenance.prov.interop;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Variant;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.Formats.ProvFormatType;
import org.openprovenance.prov.model.exception.DocumentedUnsupportedCaseException;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.compiler.BindingsBeanGenerator;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.expander.Expand;



import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.generator.GeneratorDetails;
import org.openprovenance.prov.generator.GraphGenerator;
import org.openprovenance.prov.template.json.Bindings;

import static org.openprovenance.prov.interop.Formats.ProvFormat.*;

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
public class InteropFramework implements InteropMediaType, org.openprovenance.prov.model.ProvSerialiser {

    public final static String configFile="config.interop.properties";
    public final static String configuration;
    public final static String factoryClass;
    public final static ProvFactory defaultFactory;

    static {
        Properties properties=getPropertiesFromClasspath(configFile);
        configuration=(String)properties.get("interop.config");
        factoryClass = properties.getProperty("prov.factory");
        defaultFactory=dynamicallyLoadFactory(factoryClass);
    }


    public static Properties getPropertiesFromClasspath(String propFileName) {
        return Configuration.getPropertiesFromClasspath(InteropFramework.class, propFileName);
    }

    static public ProvFactory getDefaultFactory(){
        return defaultFactory;
    }



    static public ProvFactory dynamicallyLoadFactory(String factory) {
        Class<?> clazz=null;
        try {
            clazz = Class.forName(factory);
            Method method=clazz.getMethod("getFactory");
            return (ProvFactory) method.invoke(new Object[0]);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.throwing(e);
        }
        return null;
    }


    static Logger logger = LogManager.getLogger(InteropFramework.class);
    public static final String UNKNOWN = "unknown";

    final ProvFactory pFactory;

    public final Hashtable<ProvFormat, String> extensionMap;
    public final Hashtable<String, Formats.ProvFormat> extensionRevMap;
    public final Hashtable<Formats.ProvFormat, String> mimeTypeMap;
    public final Hashtable<String, Formats.ProvFormat> mimeTypeRevMap;
    public final Hashtable<ProvFormat, ProvFormatType> provTypeMap;
    final private CommandLineArguments config;
    final private Map<ProvFormat, SerializerFunction> serializerMap;
    final private Map<ProvFormat, DeserializerFunction> deserializerMap;
    final private Map<ProvFormat, DeserializerFunction2> deserializerMap2;

    /** Default constructor for the ProvToolbox interoperability framework.
     * It uses the factory declared in the configuration file as its default factory.
     */
 
    
    public InteropFramework() {
        this(new CommandLineArguments(), defaultFactory);
    }

    public InteropFramework(ProvFactory pFactory) {
        this(new CommandLineArguments(),pFactory);
    }

    public InteropFramework(CommandLineArguments config,
                            ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.config=config;
        
        extensionMap = new Hashtable<>();
        extensionRevMap = new Hashtable<>();
        mimeTypeMap = new Hashtable<>();
        mimeTypeRevMap = new Hashtable<>();
        provTypeMap = new Hashtable<>();

        initializeExtensionMap(extensionMap, extensionRevMap);

        serializerMap = createSerializerMap();
        deserializerMap = createDeserializerMap();
        deserializerMap2 = createDeserializerMap2();
    }

    /**
     * Create a list of mime types supported by ProvToolbox in view of constructing an Accept Header. 
     * @return a string representing the mime types.
     */
    public String buildAcceptHeader() {
        StringBuilder mimetypes = new StringBuilder();
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
            boolean isHttp = (theURL.getProtocol().equals("http") || theURL
                    .getProtocol().equals("https"));

            logger.debug("Requesting: " + theURL);

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
     * @return a list of {@link Variant}
     */
    
    public List<Variant> getVariants() {
        List<Variant> vs = new ArrayList<>();
        for (Map.Entry<String, ProvFormat> entry : mimeTypeRevMap.entrySet()) {
            if (isOutputFormat(entry.getValue())) {
                String[] parts = entry.getKey().split("/");
                MediaType m = new MediaType(parts[0], parts[1]);
                vs.add(new Variant(m, (java.util.Locale) null, null));
            }
        }
        return vs;
    }

    /** Initialization function
     * @param extensionMap mapping of {@link ProvFormat} to extensions
     * @param extensionRevMap reverse mapping of extensions to {@link ProvFormat}
     */
    public void initializeExtensionMap(Hashtable<ProvFormat, String> extensionMap,
                                       Hashtable<String, ProvFormat> extensionRevMap) {
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
                extensionMap.put(PROVN, EXTENSION_PROVN);
                extensionRevMap.put(EXTENSION_PROVN, PROVN);
                extensionRevMap.put("pn", PROVN);
                extensionRevMap.put("asn", PROVN);
                extensionRevMap.put("prov-asn", PROVN);
                mimeTypeMap.put(PROVN,
                                MEDIA_TEXT_PROVENANCE_NOTATION);
                mimeTypeRevMap.put(MEDIA_TEXT_PROVENANCE_NOTATION,
                                   PROVN);
                provTypeMap.put(PROVN, ProvFormatType.INPUTOUTPUT);
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
            case JSONLD:
                extensionMap.put(ProvFormat.JSONLD, EXTENSION_JSONLD);
                extensionRevMap.put(EXTENSION_JSONLD, ProvFormat.JSONLD);
                mimeTypeMap.put(ProvFormat.JSONLD, MEDIA_APPLICATION_JSONLD);
                mimeTypeRevMap.put(MEDIA_APPLICATION_JSONLD, ProvFormat.JSONLD);
                provTypeMap.put(ProvFormat.JSONLD, ProvFormatType.INPUTOUTPUT);
                break;
            case PROVX:
                extensionMap.put(ProvFormat.PROVX, EXTENSION_PROVX);
                extensionRevMap.put(EXTENSION_PROVX, ProvFormat.PROVX);
                extensionRevMap.put(EXTENSION_XML, ProvFormat.PROVX);
                mimeTypeMap.put(ProvFormat.PROVX, MEDIA_APPLICATION_PROVENANCE_XML);
                mimeTypeRevMap.put(MEDIA_APPLICATION_PROVENANCE_XML, ProvFormat.PROVX);
                provTypeMap.put(ProvFormat.PROVX, ProvFormatType.INPUTOUTPUT);
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
        for (ProvFormat format: preferredOrder) {
            try {
                return deserializerMap.get(format).apply().deserialiseDocument(Files.newInputStream(Paths.get(filename)));
            } catch (IOException ignored) {
                // we fail, let's continue with the next one
            }
        }

        System.out.println("Unparseable format " + filename);
        throw new UnsupportedOperationException("Unparseable format " + filename);
    }


    public Object loadProvUnknownGraph(String filename, DateTimeOption dateTimeOption, TimeZone timeZone) {
        for (ProvFormat format: preferredOrder) {
            try {
                return deserializerMap2.get(format).apply(dateTimeOption, timeZone).deserialiseDocument(Files.newInputStream(Paths.get(filename)));
            } catch (IOException ignored) {
                // we fail, let's continue with the next one
            }
        }
        System.out.println("Unparseable format " + filename);
        throw new UnsupportedOperationException("Unparseable format " + filename);
    }

    /**
     * Reads a document from a URL. Uses the Content-type header field to determine the 
     * mime-type of the resource, and therefore the parser to read the document. 
     * @param url a URL
     * @return a Document
     */
    public Document readDocumentFromURL(String url) {
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
                String actual_content_type = content_type.substring(0, end).trim();
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

            return deserialiseDocument(content_stream, format);
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
            throw new InteropException("Unknown output file format: " + filename);
        }
        try {
            return deserialiseDocument(Files.newInputStream(Paths.get(filename)), format);
        } catch (IOException e) {
            throw new InteropException(e);
        }
    }


    public List<Map<String, String>>getSupportedFormats() {
        List<Map<String, String>> tripleList = new ArrayList<>();
        Map<String, String> trip;
        for (ProvFormat pt: provTypeMap.keySet()) {
            for (String mt: mimeTypeRevMap.keySet()) {
                if (mimeTypeRevMap.get(mt) == pt) {
                    for (String ext: extensionRevMap.keySet()) {
                        if (extensionRevMap.get(ext) == pt) {
                            if (isInputFormat(pt)) {
                                trip = new java.util.HashMap<>();
                                trip.put("extension", ext);
                                trip.put("mediatype", mt);
                                trip.put("type", "input");
                                tripleList.add(trip);
                            }
                            if (isOutputFormat(pt)) {
                                trip = new java.util.HashMap<>();
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



    private Document doReadDocument(String filename, String format) throws IOException {
        Document doc;
        ProvFormat informat;
        if (format != null) {
            informat = getTypeForFormat(format);
            if (informat == null) {
                throw new InteropException("Unknown format: " + format);
            }
        } else {
            informat = getTypeForFile(filename);
            if (informat == null) {
                throw new InteropException("Unknown file format for: " + filename);
            }
        }

        if (Objects.equals(filename, "-")) {
            if (informat == null) {
                throw new InteropException("File format for standard input not specified");
            }
            doc = deserialiseDocument(System.in, informat);
        } else {
            doc=deserialiseDocument(Files.newInputStream(Paths.get(filename)), informat);
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

        if (Objects.equals(filename, "-")) {
            if (outformat == null) {
                throw new InteropException("File format for standard output not specified");
            }
            writeDocument(System.out, outformat, doc);
        } else {
            writeDocument(filename, outformat, doc);
        }
    }
    
    static String SEPARATOR=",";

    /**
     * Serializes a document to a stream
     *
     * @param out       an {@link OutputStream}
     * @param document  a {@link Document}
     * @param formatted a boolean indicating whether the output should be pretty-printed
     */
    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        throw new UnsupportedOperationException("InteropFramework()  serialize Document requires a media type");
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
        ProvFormat format=mimeTypeRevMap.get(mediaType);
        if (format==null) {
            throw new UnsupportedOperationException("InteropFramework(): serialisedDocument unknown mediatype " + mediaType);
        }
        SerializerFunction serializerMaker=serializerMap.get(format);
        logger.debug("serializer " + format + " " + serializerMaker);
        serializerMaker.apply().serialiseDocument(out,document,mediaType,formatted);
    }


    public Document deserialiseDocument(InputStream is, ProvFormat format) throws IOException {
        DeserializerFunction deserializer=deserializerMap.get(format);
        logger.debug("deserializer " + format + " " + deserializer);
        return deserializer.apply().deserialiseDocument(is);
    }
    public Document deserialiseDocument(InputStream is, ProvFormat format, DateTimeOption dateTimeOption, TimeZone timeZone) throws IOException {
        DeserializerFunction2 deserializer=deserializerMap2.get(format);
        logger.debug("deserializer " + format + " " + deserializer);
        return deserializer.apply(dateTimeOption, timeZone).deserialiseDocument(is);
    }
    @Override
    public Collection<String> mediaTypes() {
        return mimeTypeRevMap.keySet();
    }

    public Object readDocumentFromFile(String filename, DateTimeOption dateTimeOption, TimeZone timeZone) {
        ProvFormat format = getTypeForFile(filename);
        if (format == null) {
            throw new InteropException("Unknown output file format: " + filename);
        }
        try {
            return deserialiseDocument(Files.newInputStream(Paths.get(filename)), format, dateTimeOption, timeZone);
        } catch (IOException e) {
            throw new InteropException(e);
        }
    }


    enum FileKind { FILE , URL }

    static class ToRead {
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
    
    public Document readDocument(ToRead something) throws IOException {
    	Document doc=null;
    	switch (something.kind) {
		case FILE:
			//doc=readDocumentFromFile(something.url, something.format);
            doc=deserialiseDocument(Files.newInputStream(Paths.get(something.url)), something.format);
			break;
		case URL:
			doc= readDocumentFromURL(something.url);// note: ignore format?
			break;
    	}
    	return doc;
    }
    
    private List<ToRead> readIndexFile(File fin) throws IOException {
    	FileInputStream fis = new FileInputStream(fin);
    	return readIndexFile(fis);
    }
    
    
    private List<ToRead> readIndexFile(InputStream is) throws IOException {
    	List<ToRead> res= new LinkedList<>();
     
    	BufferedReader br = new BufferedReader(new InputStreamReader(is));
     
    	String line;
    	while ((line = br.readLine()) != null) {
    		String [] parts=line.split(SEPARATOR);
    		if (parts.length>=3) {
    			FileKind kind=parts[0].trim().equals("URL") ? FileKind.URL : FileKind.FILE;
    			String path=parts[1].trim();
    			ProvFormat format=getTypeForFormat(parts[2].trim());
    			ToRead elem= new ToRead(kind, path, format);
    			res.add(elem);
    		} else if (parts.length==1) {
    			String filename=parts[0].trim();
    			ToRead elem= new ToRead(FileKind.FILE, filename, getTypeForFile(filename));
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
     * @return an exit code
     */

    public int run() {
        if (config.config) {
            String classpath = System.getProperty("java.class.path");
            System.out.println("provconvert.classpath=" + classpath);
            System.out.println("provconvert.main=" + CommandLineArguments.class.getName());
            return CommandLineArguments.STATUS_OK;
        }
        if (config.outfile == null && config.compare == null && config.template_builder == null)
            return CommandLineArguments.STATUS_NO_OUTPUT_OR_COMPARISON;
        if (config.infile == null && config.generator == null && config.template_builder == null && config.merge == null)
            return CommandLineArguments.STATUS_NO_INPUT;

        if ((Objects.equals(config.infile, "-")) && (Objects.equals(config.bindings, "-")))
            throw new InteropException("Cannot use standard input for both infile and bindings");
        

        Document doc;
        if (config.infile != null && config.log2prov==null) {  // if log2prov is set, then the input file is a log, to be converted
            try {
                doc = doReadDocument(config.infile, config.informat);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (config.merge != null) {
            IndexedDocument iDoc = new IndexedDocument(pFactory,
                                                       pFactory.newDocument(),
                                                       config.flatten != null);
            try {
                List<ToRead> files;
                if (config.merge.equals("-")) {
                    files = readIndexFile(System.in);
                } else {
                    files = readIndexFile(new File(config.merge));
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

        } else if (config.template_builder!=null) {
            ConfigProcessor cp=new ConfigProcessor(pFactory);
            return cp.processTemplateGenerationConfig(config.template_builder, pFactory);
            
        } else if (config.log2prov!=null) {
            try {
                Class<?>  clazz = Class.forName(config.log2prov);
                Method method = clazz.getMethod("main", String[].class);
                try {
                    if ( config.log2kernel) {
                        List<String> init=List.of("kernel", "-infile", config.infile, "-outfile", config.outfile);
                        List<String> ll = new LinkedList<>(init);
                        Map<String, String> env=System.getenv();
                        String tmp;
                        if ((tmp=env.get("LEVEL_OFFSET"))!=null && !tmp.isEmpty()) {
                            ll.add("-levelOffset");
                            ll.add(tmp);
                        }
                        if ((tmp=env.get("LEVEL_NUMBER"))!=null && !tmp.isEmpty()) {
                            ll.add("-levelNumber");
                            ll.add(tmp);
                        }
                        if ((tmp=env.get("SET_OFFSET"))!=null && !tmp.isEmpty()) {
                            ll.add("-setOffset");
                            ll.add(tmp);
                        }
                        if ((tmp=env.get("RELATION_OFFSET"))!=null && !tmp.isEmpty()) {
                            ll.add("-relationOffset");
                            ll.add(tmp);
                        }
                        if ((tmp=env.get("KNOWN_TYPES"))!=null && !tmp.isEmpty()) {
                            ll.add("-knownTypes");
                            ll.add(tmp);
                        }
                        if ((tmp=env.get("KNOWN_RELATIONS"))!=null && !tmp.isEmpty()) {
                            ll.add("-knownRelations");
                            ll.add(tmp);
                        }
                        if ((tmp=env.get("TRANSLATION"))!=null && !tmp.isEmpty()) {
                            ll.add("-translation");
                            ll.add(tmp);
                        }
                        if ((tmp=env.get("ADDLEVEL0"))!=null && !tmp.isEmpty()) {
                            ll.add("-addlevel0");
                            ll.add(tmp);
                        }
                        if ((tmp=env.get("PROPERTY_CONVERTERS"))!=null && !tmp.isEmpty()) {
                            ll.add("-propertyConverters");
                            ll.add(tmp);
                        }

                        logger.debug("log2kernel " + ll);

                        method.invoke(null,new Object[]{ll.toArray(new String[]{})});
                    } else {
                        method.invoke(null, new Object[]{new String[]{config.infile, config.outfile, "-merge"}});
                    }
                    return 0;
             } catch (Throwable e) {
                    e.printStackTrace();
                    return -1;
             }
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException e) {
                e.printStackTrace();
            }

            return -1;
        }
        else {

            String[] options = config.generator.split(":");
            String noOfNodes = getOption(options, 0);
            String noOfEdges = getOption(options, 1);
            String firstNode = getOption(options, 2);
            String namespace = "http://expample.org/";
            String seed = getOption(options, 3);
            String term = getOption(options, 4);

            if (term == null)
                term = "e1";

            GeneratorDetails gd = new GeneratorDetails(
                                                       Integer.parseInt(noOfNodes),
                                                       Integer.parseInt(noOfEdges),
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

        if (config.compare!=null) {
            try {
                return doCompare(doc,doReadDocument(config.compare, config.informat));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } 
        
        if (config.template!=null  && !config.builder) {
            BindingsBeanGenerator bbgen=new BindingsBeanGenerator(pFactory);
            
            boolean val=bbgen.generate(doc, config.template, config.packge, config.outfile, config.location);
            return (val) ? 0 : CommandLineArguments.STATUS_BEAN_GENERATION;
      }
        
        if (config.template!=null  && config.builder) {
            ConfigProcessor cp=new ConfigProcessor(pFactory);

            if (config.bindings != null) {
                if (config.bindingsVersion>=3) {
                    try {

                        TemplatesCompilerConfig configs = new TemplatesCompilerConfig();
                        //FIXME: configs not initialized!!
                        Locations locations = new Locations(configs, null);


                        cp.generate(doc, locations, config.template, config.packge, config.outfile, config.location, config.location, true, config.location, "schema.json", "documentation.html", cp.readTree(new File(config.bindings)), cp.getBindingsSchema(config.bindings), null, config.location + "/src/main/resources/project/version/", false, new LinkedList<>(), null, null);
                        return CommandLineArguments.STATUS_OK;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new DocumentedUnsupportedCaseException("bindings version number < 3");
                }
            }

        }

        if (config.index != null) {
            doc = new IndexedDocument(pFactory, doc, (config.flatten != null)).toDocument();
        }
        if (config.bindings != null) {
            Expand myExpand=new Expand(pFactory, config.addOrderp,config.allExpanded);
            Document expanded;
            if (config.bindingsVersion==3) {
                Bindings inBindings;
                try {
                    inBindings = new ObjectMapper().readValue(new File(config.bindings), Bindings.class);
                } catch (IOException e) {
                    throw new InteropException("problem parsing bindings file " + config.bindings,e);
                }
                expanded = myExpand.expander(doc, inBindings);

            } else {
                throw new DocumentedUnsupportedCaseException("bindings version number <> 3");
            }
            boolean flag=myExpand.getAllExpanded();
            doWriteDocument(config.outfile, config.outformat, expanded);

            if (!flag) {
                return CommandLineArguments.STATUS_TEMPLATE_UNBOUND_VARIABLE;
            }
        } else {
            doWriteDocument(config.outfile, config.outformat, doc);
        }
        return CommandLineArguments.STATUS_OK;

    }

    private int doCompare(Document doc1, Document doc2) {
        if (doc1==null) return CommandLineArguments.STATUS_COMPARE_NO_ARG1;
        if (doc2==null) return CommandLineArguments.STATUS_COMPARE_NO_ARG2;

        //System.out.println("doCompare()");
        PrintStream ps=System.out;
        try {
            if (!(config.compareOut==null || config.compareOut.equals("-"))) {
                ps=new PrintStream(config.compareOut);
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

    /** Initializes a Document's namespace.
     * @param doc a {@link Document} to initialize
     * */

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
        serialiseDocument(os,document,mimeTypeMap.get(format),true);
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




    final public Map<ProvFormat, SerializerFunction> createSerializerMap() {

        //NOTE: Syntax restricted to 10 entries
        Map<ProvFormat, SerializerFunction> serializer=new HashMap<>();
        serializer.putAll(
                Map.of(PROVN,    () -> new org.openprovenance.prov.notation.ProvSerialiser(pFactory),
                        PROVX,     () -> new org.openprovenance.prov.core.xml.serialization.ProvSerialiser(true),
                        TURTLE,  () -> { throw new UnsupportedOperationException("light turtle converter not integrated yet");}, //new org.openprovenance.prov.rdf.ProvSerialiser(pFactory, TURTLE),
                        JSONLD,  () -> new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser(new ObjectMapper(), false),
                         TRIG,   () -> { throw new UnsupportedOperationException("light turtle converter not integrated yet");},  //() -> new org.openprovenance.prov.rdf.ProvSerialiser(pFactory, TRIG),
                        JSON,    org.openprovenance.prov.core.json.serialization.ProvSerialiser::new));

        serializer.putAll(
                Map.of(JPEG,    () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory,extensionMap.get(JPEG), maxStringLength),
                        SVG,     () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory,extensionMap.get(SVG), maxStringLength),
                        PDF,     () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory,extensionMap.get(PDF), maxStringLength),
                        PNG,     () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory,extensionMap.get(PNG), maxStringLength),
                        DOT,     () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory,extensionMap.get(DOT), maxStringLength)

                ) );

        return serializer;
    }

    final public Map<ProvFormat, DeserializerFunction> createDeserializerMap() {

        //NOTE: Syntax restricted to 10 entries
        Map<ProvFormat, DeserializerFunction> deserializer=new HashMap<>();
        deserializer.putAll(
                Map.of(PROVN,    () -> new org.openprovenance.prov.notation.ProvDeserialiser(pFactory,config.dateTime, config.timeZone),
                        PROVX,     () -> new org.openprovenance.prov.core.xml.serialization.ProvDeserialiser(config.dateTime, config.timeZone),
                        JSONLD,  () -> new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser(new ObjectMapper(), config.dateTime, config.timeZone),
                        JSON,    () -> new org.openprovenance.prov.core.json.serialization.ProvDeserialiser(new ObjectMapper(), config.dateTime, config.timeZone))
        );

        return deserializer;
    }

    final public Map<ProvFormat, DeserializerFunction2> createDeserializerMap2() {

        //NOTE: Syntax restricted to 10 entries
        Map<ProvFormat, DeserializerFunction2> deserializer=new HashMap<>();
        deserializer.putAll(
                Map.of(PROVN,    (DateTimeOption dateTime, TimeZone timeZone) -> new org.openprovenance.prov.notation.ProvDeserialiser(pFactory,dateTime, timeZone),
                        PROVX,     (DateTimeOption dateTime, TimeZone timeZone) -> new org.openprovenance.prov.core.xml.serialization.ProvDeserialiser(dateTime, timeZone),
                        JSONLD,  (DateTimeOption dateTime, TimeZone timeZone) -> new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser(new ObjectMapper(), dateTime, timeZone),
                        JSON,    (DateTimeOption dateTime, TimeZone timeZone) -> new org.openprovenance.prov.core.json.serialization.ProvDeserialiser(new ObjectMapper(), dateTime, timeZone))
        );

        return deserializer;
    }

    List<ProvFormat> preferredOrder=List.of(PROVN, JSONLD, JSON,PROVX);




    private Integer maxStringLength=100;

    public void setMaxStringLength(Integer maxStringLength) {
        this.maxStringLength = maxStringLength;
        serializerMap.put(
        SVG,     () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory,extensionMap.get(SVG),maxStringLength));
    }


    /**
         * Write a {@link Document} to file, serialized according to the file extension
         * @param filename path of the file to write the Document to
         * @param format a {@link ProvFormat} to serialize the document to
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
                new Utility(config.dateTime, config.timeZone).writeDocument(document, filename, pFactory);
                break;
            }
                case PROVX: {
                org.openprovenance.prov.core.xml.serialization.ProvSerialiser serial = new org.openprovenance.prov.core.xml.serialization.ProvSerialiser();
                logger.debug("namespaces " + document.getNamespace());
                serial.serialiseDocument(Files.newOutputStream(new File(filename).toPath()), document, true);
                break;
            }


            case DOT: {
                ProvToDot toDot = new ProvToDot(pFactory);
                toDot.setMaxStringLength(maxStringLength);
                toDot.setLayout(config.layout);
                toDot.convert(document, filename, config.title);
                break;
            }
            case PDF:
            case JPEG:
            case PNG:
            case SVG: {
                File tmp = File.createTempFile("viz-", ".dot");

                String dotFileOut = tmp.getAbsolutePath(); // give it as option,
                                                           // if not available
                                                           // create tmp file
                ProvToDot toDot= new ProvToDot(pFactory);
                toDot.setMaxStringLength(maxStringLength);
                toDot.setLayout(config.layout);
                toDot.convert(document, dotFileOut, filename, extensionMap.get(format), config.title);
                tmp.delete();
                break;
            }

            default:
                break;
            }
        } catch (RuntimeException | IOException e) {
            if (config.verbose != null)
                e.printStackTrace();
            throw new InteropException(e);

        }

    }
    


}
