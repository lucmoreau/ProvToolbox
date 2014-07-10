package org.openprovenance.prov.interop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;
import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.xml.ProvDeserialiser;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.rdf.Ontology;
import org.openprovenance.prov.template.Expand;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.generator.GeneratorDetails;
import org.openprovenance.prov.generator.GraphGenerator;
import org.apache.log4j.Logger;

/**
 * The interoperability framework for PROV.
 */
public class InteropFramework {

	public static final String EXTENSION_TRIG = "trig";
	public static final String EXTENSION_XML = "xml";
	public static final String EXTENSION_PROVX = "provx";
	public static final String EXTENSION_TTL = "ttl";
	public static final String EXTENSION_SVG = "svg";
	public static final String EXTENSION_PROVN = "provn";
	public static final String EXTENSION_RDF = "rdf";
	public static final String EXTENSION_JSON = "json";
	public static final String EXTENSION_DOT = "dot";
	public static final String EXTENSION_PDF = "pdf";
	public static final String EXTENSION_JPEG = "jpeg";
	public static final String EXTENSION_JPG = "jpg";

	public static final String MEDIA_IMAGE_JPEG = "image/jpeg";
	public static final String MEDIA_IMAGE_SVG_XML = "image/svg+xml";
	public static final String MEDIA_APPLICATION_PROVENANCE_XML = "application/provenance+xml";
	public static final String MEDIA_TEXT_TURTLE = "text/turtle";
	public static final String MEDIA_APPLICATION_X_TRIG = "application/x-trig";	
	public static final String MEDIA_APPLICATION_RDF_XML = "application/rdf+xml";
	public static final String MEDIA_TEXT_PROVENANCE_NOTATION = "text/provenance-notation";
	public static final String MEDIA_APPLICATION_PDF = "application/pdf";
	public static final String MEDIA_APPLICATION_JSON = MediaType.APPLICATION_JSON;
	public static final String MEDIA_TEXT_HTML = MediaType.TEXT_HTML;
	public static final String MEDIA_TEXT_PLAIN = MediaType.TEXT_PLAIN;
	public static final String MEDIA_TEXT_XML = MediaType.TEXT_XML;
	public static final String MEDIA_APPLICATION_XML = MediaType.APPLICATION_XML;
	public static final String MEDIA_APPLICATION_FORM_URLENCODED=MediaType.APPLICATION_FORM_URLENCODED;
	
	public final static String [] ALL_PROV_MEDIA_TYPES= 
		    new String[] { "text/turtle", 
		                   "text/provenance-notation",
		                   "application/provenance+xml",
		                   "application/x-trig",
		                   "application/rdf+xml", 
		                   "application/json", 
		                   "image/svg+xml"};
	    

	static Logger logger = Logger.getLogger(InteropFramework.class);

	public static final String UNKNOWN = "unknown";
//	public static final String PC1_NS = "http://www.ipaw.info/pc1/";
//	public static final String PC1_PREFIX = "pc1";
//	public static final String PRIM_NS = "http://openprovenance.org/primitives#";
//	public static final String PRIM_PREFIX = "prim";

	final Utility u = new Utility();
	final ProvFactory pFactory;
	final Ontology onto;
	
	final private String verbose;
	final private String debug;
	final private String logfile;
	final private String infile;
	final private String outfile;
	final private String namespaces;
	final private String title;
	final private String layout;
	final private String bindings;

	public final Hashtable<ProvFormat, String> extensionMap;
	public final Hashtable<String, ProvFormat> extensionRevMap;
	public final Hashtable<ProvFormat, String> mimeTypeMap;
	public final Hashtable<String, ProvFormat> mimeTypeRevMap;
	public final Hashtable<ProvFormat, ProvFormatType> provTypeMap;

	final private String generator;


	public InteropFramework(String verbose, String debug, String logfile,
			String infile, String outfile, String namespaces, String title, String layout, String bindings, String generator, ProvFactory pFactory) {
		this.verbose = verbose;
		this.debug = debug;
		this.logfile = logfile;
		this.infile = infile;
		this.outfile = outfile;
		this.namespaces = namespaces;
		this.title=title;
		this.layout=layout;
		this.bindings=bindings;
		this.generator=generator;
		this.pFactory=pFactory;
		this.onto=new Ontology(pFactory);

		extensionMap = new Hashtable<InteropFramework.ProvFormat, String>();
		extensionRevMap = new Hashtable<String, InteropFramework.ProvFormat>();
		mimeTypeMap = new Hashtable<InteropFramework.ProvFormat, String>();
		mimeTypeRevMap = new Hashtable<String, InteropFramework.ProvFormat>();
		provTypeMap = new Hashtable<InteropFramework.ProvFormat, InteropFramework.ProvFormatType>();

		initializeExtensionMap(extensionMap, extensionRevMap);
	}

	public InteropFramework() {
	    this(null, null, null, null, null, null, null, null, null, null, org.openprovenance.prov.xml.ProvFactory.getFactory());
	}
	
	public InteropFramework(ProvFactory pFactory) {
	    this(null, null, null, null, null, null, null, null, null, null, pFactory);
	}

    public void initializeExtensionMap(
			Hashtable<ProvFormat, String> extensionMap,
			Hashtable<String, InteropFramework.ProvFormat> extensionRevMap) {
		for (ProvFormat f : ProvFormat.values()) {
			switch (f) {
			case DOT:
				extensionMap.put(ProvFormat.DOT, EXTENSION_DOT);
				extensionRevMap.put(EXTENSION_DOT, ProvFormat.DOT);
				extensionRevMap.put("gv", ProvFormat.DOT);
				mimeTypeMap.put(ProvFormat.DOT, "text/vnd.graphviz");
				mimeTypeRevMap.put("text/vnd.graphviz", ProvFormat.DOT);
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
				mimeTypeMap.put(ProvFormat.PROVN, MEDIA_TEXT_PROVENANCE_NOTATION);
				mimeTypeRevMap
						.put(MEDIA_TEXT_PROVENANCE_NOTATION, ProvFormat.PROVN);
				provTypeMap.put(ProvFormat.PROVN, ProvFormatType.INPUTOUTPUT);
				break;
			case RDFXML:
				extensionMap.put(ProvFormat.RDFXML, EXTENSION_RDF);
				extensionRevMap.put(EXTENSION_RDF, ProvFormat.RDFXML);
				mimeTypeMap.put(ProvFormat.RDFXML, MEDIA_APPLICATION_RDF_XML);
				mimeTypeRevMap.put(MEDIA_APPLICATION_RDF_XML, ProvFormat.RDFXML);
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
				mimeTypeMap.put(ProvFormat.TRIG, MEDIA_APPLICATION_X_TRIG);
				mimeTypeRevMap.put(MEDIA_APPLICATION_X_TRIG, ProvFormat.TRIG);
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
				mimeTypeMap.put(ProvFormat.XML, MEDIA_APPLICATION_PROVENANCE_XML);
				mimeTypeRevMap.put(MEDIA_APPLICATION_PROVENANCE_XML, ProvFormat.XML);
				provTypeMap.put(ProvFormat.XML, ProvFormatType.INPUTOUTPUT);
				break;
			default:
				break;

			}
		}

	}

	public String getExtension(ProvFormat format) {
		String extension = UNKNOWN;
		if (format != null) {
			extension = extensionMap.get(format);
		}
		return extension;
	}

	public String convertExtensionToMediaType(String type) {
		ProvFormat format = extensionRevMap.get(type);
		if (format == null)
			return null;
		return mimeTypeMap.get(format);
	}

	public void provn2html(String file, String file2)
			throws java.io.IOException, JAXBException, RecognitionException {
		Document doc = (Document) u.convertASNToJavaBean(file,pFactory);
		String s = u.convertBeanToHTML(doc,pFactory);
		u.writeTextToFile(s, file2);

	}

	public static final String RDF_TURTLE = "turtle";
	public static final String RDF_XML = "rdf/xml";
	public static final String RDF_TRIG = EXTENSION_TRIG;
	public static final String RDF_N3 = "n3";

	public RDFFormat convert(String type) {
		if (RDF_TURTLE.equals(type))
			return RDFFormat.TURTLE;
		if (RDF_XML.equals(type))
			return RDFFormat.RDFXML;
		if (RDF_N3.equals(type))
			return RDFFormat.N3;
		if (RDF_TRIG.equals(type))
			return RDFFormat.TRIG;
		return null;
	}

	/** Reads a file into java bean. */
	public Object loadProvGraph(String filename) {
	    return loadProvKnownGraph(filename);
		
	}

	public enum ProvFormat {
		PROVN, XML, TURTLE, RDFXML, TRIG, JSON, DOT, JPEG, SVG, PDF
	}

	public enum ProvFormatType {
		INPUT, OUTPUT, INPUTOUTPUT
	}

	public Boolean isInputFormat(ProvFormat format) {
		ProvFormatType t = provTypeMap.get(format);
		return (t.equals(ProvFormatType.INPUT) || t
				.equals(ProvFormatType.INPUTOUTPUT));
	}

	public Boolean isOutputFormat(ProvFormat format) {
		ProvFormatType t = provTypeMap.get(format);
		return (t.equals(ProvFormatType.OUTPUT) || t
				.equals(ProvFormatType.INPUTOUTPUT));
	}

	public ProvFormat getTypeForFile(String filename) {
		int count = filename.lastIndexOf(".");
		if (count == -1)
			return null;
		String extension = filename.substring(count + 1);
		return extensionRevMap.get(extension);
	}

	public void writeDocument(String filename, Document doc) {
	    Namespace.withThreadNamespace(doc.getNamespace());
		try {
			ProvFormat format = getTypeForFile(filename);
			if (format == null) {
				System.err.println("Unknown output file format: " + filename);
				return;
			}
			logger.debug("writing " + format);
			logger.debug("writing " + filename);
			setNamespaces(doc);
			switch (format) {
			case PROVN: {
				u.writeDocument(doc, filename,pFactory);
				break;
			}
			case XML: {
				ProvSerialiser serial = ProvSerialiser
						.getThreadProvSerialiser();
				logger.debug("namespaces " + doc.getNamespace());
				serial.serialiseDocument(new File(filename), doc, true);
				break;
			}
			case TURTLE: {
				new org.openprovenance.prov.rdf.Utility(pFactory,onto).dumpRDF(
						doc, RDFFormat.TURTLE, filename);
				break;
			}
			case RDFXML: {
				new org.openprovenance.prov.rdf.Utility(pFactory,onto).dumpRDF(
						doc, RDFFormat.RDFXML, filename);
				break;
			}
			case TRIG: {
				new org.openprovenance.prov.rdf.Utility(pFactory,onto).dumpRDF(
						doc, RDFFormat.TRIG, filename);
				break;
			}
			case JSON: {
				new org.openprovenance.prov.json.Converter(pFactory).writeDocument(doc,
						filename);
				break;
			}
			case PDF: {
				String configFile = null; // TODO: get it as option
				File tmp = File.createTempFile("viz-", ".dot");

				String dotFileOut = tmp.getAbsolutePath(); // give it as option,
															// if not available
															// create tmp file
				ProvToDot toDot = (configFile == null) ? new ProvToDot(
						ProvToDot.Config.ROLE_NO_LABEL) : new ProvToDot(
						configFile);
				toDot.setLayout(layout);
				toDot.convert(doc, dotFileOut, filename,title);
				break;
			}
			case DOT: {
				String configFile = null; // TODO: get it as option
				ProvToDot toDot = (configFile == null) ? new ProvToDot(
						ProvToDot.Config.ROLE_NO_LABEL) : new ProvToDot(
						configFile);
				toDot.setLayout(layout);		
				toDot.convert(doc, filename,title);
				break;
			}
			case JPEG: {
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
				toDot.convert(doc, dotFileOut, filename, EXTENSION_JPG, title);
				tmp.delete();
				break;
			}
			case SVG: {
				String configFile = null; // give it as option
				File tmp = File.createTempFile("viz-", ".dot");

				String dotFileOut = tmp.getAbsolutePath(); // give it as option,
															// if not available
															// create tmp file
				// ProvToDot toDot=new ProvToDot((configFile==null)?
				// "../../ProvToolbox/prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml"
				// : configFile);
				ProvToDot toDot;
				if (configFile != null) {
					toDot = new ProvToDot(configFile);
				} else {
					toDot = new ProvToDot(ProvToDot.Config.ROLE_NO_LABEL);
				}
				toDot.setLayout(layout);
				toDot.convert(doc, dotFileOut, filename, EXTENSION_SVG, title);
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
	
	public void writeDocument(OutputStream os, MediaType mt, Document doc) {
	    Namespace.withThreadNamespace(doc.getNamespace());
	    
		try {
			ProvFormat format = mimeTypeRevMap.get(mt.toString());
			if (format == null) {
			    System.err.println("Unknown output format: " + mt);
			    return;
			}
			logger.debug("writing " + format);
			setNamespaces(doc);
			switch (format) {
			case PROVN: {
				u.writeDocument(doc, os,pFactory);
				break;
			}
			case XML: {
				org.openprovenance.prov.model.ProvSerialiser serial = pFactory.getSerializer();
				logger.debug("namespaces " + doc.getNamespace());
				serial.serialiseDocument(os, doc, true);
				break;
			}
			case TURTLE: {
				new org.openprovenance.prov.rdf.Utility(pFactory,onto).dumpRDF(
						doc, RDFFormat.TURTLE, os);
				break;
			}
			case RDFXML: {
				new org.openprovenance.prov.rdf.Utility(pFactory,onto).dumpRDF(
						doc, RDFFormat.RDFXML, os);
				break;
			}
			case TRIG: {
				new org.openprovenance.prov.rdf.Utility(pFactory,onto).dumpRDF(
						doc, RDFFormat.TRIG, os);
				break;
			}
			case JSON: {
				new org.openprovenance.prov.json.Converter(pFactory).writeDocument(doc,
						new OutputStreamWriter(os));
				break;
			}
			case PDF: {
				String configFile = null; // TODO: get it as option
				File tmp = File.createTempFile("viz-", ".dot");

				String dotFileOut = tmp.getAbsolutePath(); // give it as option,
															// if not available
															// create tmp file
				ProvToDot toDot = (configFile == null) ? new ProvToDot(
						ProvToDot.Config.ROLE_NO_LABEL) : new ProvToDot(
						configFile);
				toDot.convert(doc, dotFileOut, os,title);
				break;
			}
			case DOT: {
				String configFile = null; // TODO: get it as option
				ProvToDot toDot = (configFile == null) ? new ProvToDot(
						ProvToDot.Config.ROLE_NO_LABEL) : new ProvToDot(
						configFile);
				toDot.convert(doc, os,title);
				break;
			}
			case JPEG: {
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

				toDot.convert(doc, dotFileOut, os, EXTENSION_JPG, title);
				tmp.delete();
				break;
			}
			case SVG: {
				String configFile = null; // give it as option
				File tmp = File.createTempFile("viz-", ".dot");

				String dotFileOut = tmp.getAbsolutePath(); // give it as option,
															// if not available
															// create tmp file
				// ProvToDot toDot=new ProvToDot((configFile==null)?
				// "../../ProvToolbox/prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml"
				// : configFile);
				ProvToDot toDot;
				if (configFile != null) {
					toDot = new ProvToDot(configFile);
				} else {
					toDot = new ProvToDot(ProvToDot.Config.ROLE_NO_LABEL);
				}

				toDot.convert(doc, dotFileOut, os, EXTENSION_SVG, title);
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

	public void setNamespaces(Document doc) {
		if (doc.getNamespace() == null)
			doc.setNamespace(new Namespace());

	}

	public Document loadProvKnownGraph(String filename) {
		try {

			ProvFormat format = getTypeForFile(filename);
			if (format == null) {
				throw new InteropException("Unknown output file format: "
						+ filename);
			}

			switch (format) {
			case DOT:
			case JPEG:
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
				Object o = u.convertTreeToJavaBean(tree,pFactory);
				Document doc=(Document)o;
				//Namespace ns=Namespace.gatherNamespaces(doc);
                                //doc.setNamespace(ns);
				return doc;
			}
			case RDFXML:
			case TRIG:
			case TURTLE: {
				org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(pFactory,onto);
				Document doc = rdfU.parseRDF(filename);
				return doc;
			}
			case XML: {
				File in = new File(filename);
				ProvDeserialiser deserial = ProvDeserialiser
						.getThreadProvDeserialiser();
				Document doc = deserial.deserialiseDocument(in);
				Namespace ns=Namespace.gatherNamespaces(doc);
				doc.setNamespace(ns);
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

	public Object loadProvUnknownGraph(String filename)
			 {

		try {
			Utility u = new Utility();
			CommonTree tree = u.convertASNToTree(filename);
			Object o = u.convertTreeToJavaBean(tree,pFactory);
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
			Object o = new org.openprovenance.prov.json.Converter(pFactory)
					.readDocument(filename);
			if (o != null) {
				return o;
			}
		} catch (IOException e) {
			// OK, we failed, let's try next format.

		}
		try {
			org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(pFactory,onto);
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
	
	/* Support for content negotiation, jax-rs style. Create a list of media type supported by the framework. */
	// http://docs.oracle.com/javaee/6/tutorial/doc/gkqbq.html
	public List<Variant> getVariants() {
		List<Variant> vs = new ArrayList<Variant>();
		for (Map.Entry<String, ProvFormat> entry : mimeTypeRevMap
				.entrySet()) {
			if (isOutputFormat(entry.getValue())) {
				String[] parts = entry.getKey().split("/");
				MediaType m = new MediaType(parts[0], parts[1]);
				vs.add(new Variant(m, (java.util.Locale) null, (String) null));
			}
		}
		return vs;
	}

	


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
	String option(String [] options, int index) {
	    if ((options!=null) && (options.length>index)) {
	        return options[index];
	    }
	    return null;
	}
	
    public void run() {
	if (outfile == null)
	    return;
	if (infile == null && generator == null)
	    return;

	Document doc;
	if (infile != null) {
	    doc = (Document) loadProvKnownGraph(infile);
	} else {
	    String[] options = generator.split(":");
	    String noOfNodes = option(options, 0);
	    String noOfEdges = option(options, 1);
	    String firstNode = option(options, 2);
	    String namespace = "http://expample.org/";
	    String seed = option(options, 3);
	    String term = option(options, 4);

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
	    System.out.println(gd);
	    GraphGenerator gg = new GraphGenerator(gd, pFactory);
	    gg.generateElements();

	    doc = gg.getDetails().getDocument();
	}
	if (bindings != null) {
	    Document docBindings = (Document) loadProvKnownGraph(bindings);
	    Document expanded = new Expand(pFactory).expander(doc, outfile, docBindings);
	    writeDocument(outfile, expanded);

	} else {
	    writeDocument(outfile, doc);
	}

    }
   
    
    public Document readDocument(InputStream is, ProvFormat format) {
    	return readDocument(is,format,pFactory);
    }
        public Document readDocument(InputStream is, ProvFormat format, ProvFactory pFactory) {
	try {

	    switch (format) {
	    case DOT:
	    case JPEG:
	    case SVG:
		throw new UnsupportedOperationException(); // we don't load PROV
		// from these
		// formats
	    case JSON: {
		return new org.openprovenance.prov.json.Converter(pFactory).readDocument(is);
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
	    	org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(pFactory,onto);
	    	return rdfU.parseRDF(is,RDFFormat.RDFXML);
	    }
	    case TRIG: {
	    	org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(pFactory,onto);
	    	return rdfU.parseRDF(is,RDFFormat.TRIG);
	    }
	    case TURTLE: {
	    	org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(pFactory,onto);
	    	return rdfU.parseRDF(is,RDFFormat.TURTLE);
	    }
	    case XML: {
		ProvDeserialiser deserial = ProvDeserialiser.getThreadProvDeserialiser();
		Document doc = deserial.deserialiseDocument(is);
		Namespace ns = Namespace.gatherNamespaces(doc);
		doc.setNamespace(ns);
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

    
    public Document readDocument(String url) throws IOException {
	URL theURL = new URL(url);
	URLConnection conn = connectWithRedirect(theURL);
	if (conn==null) return null;


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
	
	return readDocument(content_stream, format);
    }
    

}
