package org.openprovenance.prov.interop;

import java.io.File;
import java.io.IOException;
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
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.rdf.Ontology;

import org.antlr.runtime.tree.CommonTree;

import org.openrdf.rio.RDFFormat;
import org.openprovenance.prov.dot.ProvToDot;

import org.apache.log4j.Logger;

/**
 * The interoperability framework for PROV.
 */
public class InteropFramework {

	static Logger logger = Logger.getLogger(InteropFramework.class);

	public static final String UNKNOWN = "unknown";
	public static final String PC1_NS = "http://www.ipaw.info/pc1/";
	public static final String PC1_PREFIX = "pc1";
	public static final String PRIM_NS = "http://openprovenance.org/primitives#";
	public static final String PRIM_PREFIX = "prim";

	final Utility u = new Utility();
	final ProvFactory pFactory = ProvFactory.getFactory();
	final Ontology onto=new Ontology(pFactory);
	final private String verbose;
	final private String debug;
	final private String logfile;
	final private String infile;
	final private String outfile;
        final private String namespaces;
        final private String title;
	public final Hashtable<ProvFormat, String> extensionMap;
	public final Hashtable<String, ProvFormat> extensionRevMap;
	public final Hashtable<ProvFormat, String> mimeTypeMap;
	public final Hashtable<String, ProvFormat> mimeTypeRevMap;
	public final Hashtable<ProvFormat, ProvFormatType> provTypeMap;

	public InteropFramework() {
		this(null, null, null, null, null, null,null);
	}

	public InteropFramework(String verbose, String debug, String logfile,
			String infile, String outfile, String namespaces, String title) {
		this.verbose = verbose;
		this.debug = debug;
		this.logfile = logfile;
		this.infile = infile;
		this.outfile = outfile;
		this.namespaces = namespaces;
		this.title=title;
		extensionMap = new Hashtable<InteropFramework.ProvFormat, String>();
		extensionRevMap = new Hashtable<String, InteropFramework.ProvFormat>();
		mimeTypeMap = new Hashtable<InteropFramework.ProvFormat, String>();
		mimeTypeRevMap = new Hashtable<String, InteropFramework.ProvFormat>();
		provTypeMap = new Hashtable<InteropFramework.ProvFormat, InteropFramework.ProvFormatType>();

		initializeExtensionMap(extensionMap, extensionRevMap);
	}

	public void initializeExtensionMap(
			Hashtable<ProvFormat, String> extensionMap,
			Hashtable<String, InteropFramework.ProvFormat> extensionRevMap) {
		for (ProvFormat f : ProvFormat.values()) {
			switch (f) {
			case DOT:
				extensionMap.put(ProvFormat.DOT, "dot");
				extensionRevMap.put("dot", ProvFormat.DOT);
				extensionRevMap.put("gv", ProvFormat.DOT);
				mimeTypeMap.put(ProvFormat.DOT, "text/vnd.graphviz");
				mimeTypeRevMap.put("text/vnd.graphviz", ProvFormat.DOT);
				provTypeMap.put(ProvFormat.DOT, ProvFormatType.OUTPUT);
				break;
			case JPEG:
				extensionMap.put(ProvFormat.JPEG, "jpg");
				extensionRevMap.put("jpeg", ProvFormat.JPEG);
				extensionRevMap.put("jpg", ProvFormat.JPEG);
				mimeTypeMap.put(ProvFormat.JPEG, "image/jpeg");
				mimeTypeRevMap.put("image/jpeg", ProvFormat.JPEG);
				provTypeMap.put(ProvFormat.JPEG, ProvFormatType.OUTPUT);
				break;
			case JSON:
				extensionMap.put(ProvFormat.JSON, "json");
				extensionRevMap.put("json", ProvFormat.JSON);
				mimeTypeMap.put(ProvFormat.JSON, "application/json");
				mimeTypeRevMap.put("application/json", ProvFormat.JSON);
				provTypeMap.put(ProvFormat.JSON, ProvFormatType.INPUTOUTPUT);
				break;
			case PDF:
				extensionMap.put(ProvFormat.PDF, "pdf");
				extensionRevMap.put("pdf", ProvFormat.PDF);
				mimeTypeMap.put(ProvFormat.PDF, "application/pdf");
				mimeTypeRevMap.put("application/pdf", ProvFormat.PDF);
				provTypeMap.put(ProvFormat.PDF, ProvFormatType.OUTPUT);
				break;
			case PROVN:
				extensionMap.put(ProvFormat.PROVN, "provn");
				extensionRevMap.put("provn", ProvFormat.PROVN);
				extensionRevMap.put("pn", ProvFormat.PROVN);
				extensionRevMap.put("asn", ProvFormat.PROVN);
				extensionRevMap.put("prov-asn", ProvFormat.PROVN);
				mimeTypeMap.put(ProvFormat.PROVN, "text/provenance-notation");
				mimeTypeRevMap
						.put("text/provenance-notation", ProvFormat.PROVN);
				provTypeMap.put(ProvFormat.PROVN, ProvFormatType.INPUTOUTPUT);
				break;
			case RDFXML:
				extensionMap.put(ProvFormat.RDFXML, "rdf");
				extensionRevMap.put("rdf", ProvFormat.RDFXML);
				mimeTypeMap.put(ProvFormat.RDFXML, "application/rdf+xml");
				mimeTypeRevMap.put("application/rdf+xml", ProvFormat.RDFXML);
				provTypeMap.put(ProvFormat.RDFXML, ProvFormatType.INPUTOUTPUT);
				break;
			case SVG:
				extensionMap.put(ProvFormat.SVG, "svg");
				extensionRevMap.put("svg", ProvFormat.SVG);
				mimeTypeMap.put(ProvFormat.SVG, "image/svg+xml");
				mimeTypeRevMap.put("image/svg+xml", ProvFormat.SVG);
				provTypeMap.put(ProvFormat.SVG, ProvFormatType.OUTPUT);
				break;
			case TRIG:
				extensionMap.put(ProvFormat.TRIG, "trig");
				extensionRevMap.put("trig", ProvFormat.TRIG);
				mimeTypeMap.put(ProvFormat.TRIG, "application/x-trig");
				mimeTypeRevMap.put("application/x-trig", ProvFormat.TRIG);
				provTypeMap.put(ProvFormat.TRIG, ProvFormatType.INPUTOUTPUT);
				break;
			case TURTLE:
				extensionMap.put(ProvFormat.TURTLE, "ttl");
				extensionRevMap.put("ttl", ProvFormat.TURTLE);
				mimeTypeMap.put(ProvFormat.TURTLE, "text/turtle");
				mimeTypeRevMap.put("text/turtle", ProvFormat.TURTLE);
				provTypeMap.put(ProvFormat.TURTLE, ProvFormatType.INPUTOUTPUT);
				break;
			case XML:
				extensionMap.put(ProvFormat.XML, "provx");
				extensionRevMap.put("provx", ProvFormat.XML);
				extensionRevMap.put("xml", ProvFormat.XML);
				mimeTypeMap.put(ProvFormat.XML, "application/provenance+xml");
				mimeTypeRevMap.put("application/provenance+xml", ProvFormat.XML);
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

	public String convertToMimeType(String type) {
		ProvFormat format = extensionRevMap.get(type);
		if (format == null)
			return null;
		return mimeTypeMap.get(format);
	}

	public void provn2html(String file, String file2)
			throws java.io.IOException, JAXBException, Throwable {
		Document doc = (Document) u.convertASNToJavaBean(file,pFactory);
		String s = u.convertBeanToHTML(doc,pFactory);
		u.writeTextToFile(s, file2);

	}

	public static final String RDF_TURTLE = "turtle";
	public static final String RDF_XML = "rdf/xml";
	public static final String RDF_TRIG = "trig";
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
	public Object loadProvGraph(String filename) throws java.io.IOException,
			JAXBException, Throwable {
		try {
			return loadProvKnownGraph(filename);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
			// return loadProvUnknownGraph(filename);
		}
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
				toDot.convert(doc, dotFileOut, filename,title);
				break;
			}
			case DOT: {
				String configFile = null; // TODO: get it as option
				ProvToDot toDot = (configFile == null) ? new ProvToDot(
						ProvToDot.Config.ROLE_NO_LABEL) : new ProvToDot(
						configFile);
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

				toDot.convert(doc, dotFileOut, filename, "jpg", title);
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

				toDot.convert(doc, dotFileOut, filename, "svg", title);
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

	public Object loadProvKnownGraph(String filename) {
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
				Namespace ns=Namespace.gatherNamespaces(doc);
                                doc.setNamespace(ns);
				return o;
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
		} catch (Throwable e) {
			e.printStackTrace();
			throw new InteropException(e);

		}

	}

	public Object loadProvUnknownGraph(String filename)
			throws java.io.IOException, JAXBException, Throwable {

		try {
			Utility u = new Utility();
			CommonTree tree = u.convertASNToTree(filename);
			Object o = u.convertTreeToJavaBean(tree,pFactory);
			if (o != null) {
				return o;
			}
		} catch (Throwable t1) {
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
		} catch (Throwable t2) {
			// OK, we failed, let's try next format.
		}

		try {
			Object o = new org.openprovenance.prov.json.Converter(pFactory)
					.readDocument(filename);
			if (o != null) {
				return o;
			}
		} catch (RuntimeException e) {
			// OK, we failed, let's try next format.

		}
		try {
			org.openprovenance.prov.rdf.Utility rdfU = new org.openprovenance.prov.rdf.Utility(pFactory,onto);
			Document doc = rdfU.parseRDF(filename);
			if (doc != null) {
				return doc;
			}
		} catch (RuntimeException e) {
			// OK, we failed, let's try next format
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

	public void run() {
		if (infile == null)
			return;
		if (outfile == null)
			return;
		try {
			Document doc = (Document) loadProvKnownGraph(infile);
			// doc.setNss(new Hashtable<String, String>());
			// doc.getNss().put("pc1",PC1_NS);
			// doc.getNss().put("prim",PRIM_NS);
			// doc.getNss().put("prov","http://www.w3.org/ns/prov#");
			// doc.getNss().put("xsd","http://www.w3.org/2001/XMLSchema");
			// doc.getNss().put("xsi","http://www.w3.org/2001/XMLSchema-instance");

			// System.out.println("InteropFramework run() -> " + doc.getNss());
			writeDocument(outfile, doc);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
