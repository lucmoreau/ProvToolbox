package org.openprovenance.prov.interop;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import javax.xml.bind.JAXBException;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.ProvDeserialiser;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.notation.TreeTraversal;
import org.openprovenance.prov.notation.Utility;

import  org.antlr.runtime.tree.CommonTree;

import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoManagerFactory;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import org.openprovenance.prov.rdf.RdfConstructor;
import org.openprovenance.prov.rdf.RepositoryHelper;

import org.openprovenance.prov.dot.ProvToDot;

import org.apache.log4j.Logger;

/**
 * The interoperability framework for PROV.
 */
public class InteropFramework 
{
    
    static Logger logger = Logger.getLogger(InteropFramework.class);


    public static final String UNKNOWN = "unknown";
    public static final String PC1_NS="http://www.ipaw.info/pc1/";
    public static final String PC1_PREFIX="pc1";
    public static final String PRIM_NS="http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX="prim";
    
    final Utility u=new Utility();
    final ProvFactory pFactory=ProvFactory.getFactory();
    final private String verbose;
    final private String debug;
    final private String logfile;
    final private String infile;
    final private String outfile;
    final private String namespaces;
    public final Hashtable<ProvFormat,String> extensionMap;
    public final Hashtable<String,ProvFormat> extensionRevMap;

    public InteropFramework(String verbose,
                            String debug, String logfile, String infile, String outfile,
                            String namespaces) {
	this.verbose=verbose;
	this.debug=debug;
	this.logfile=logfile;
	this.infile=infile;
	this.outfile=outfile;
	this.namespaces=namespaces;
	extensionMap=new Hashtable<InteropFramework.ProvFormat, String>();
	extensionRevMap=new Hashtable<String, InteropFramework.ProvFormat>();

	initializeExtensionMap(extensionMap, extensionRevMap);
    }
    
    public void initializeExtensionMap(Hashtable<ProvFormat,String> extensionMap,
                                       Hashtable<String, InteropFramework.ProvFormat> extensionRevMap) {
        for (ProvFormat f: ProvFormat.values()) {
            switch (f) {
            case DOT:
                extensionMap.put(ProvFormat.DOT,"dot");
                extensionRevMap.put("dot", ProvFormat.DOT);
                break;
            case JPEG:
                extensionMap.put(ProvFormat.JPEG,"jpg");
                extensionRevMap.put("jpeg", ProvFormat.JPEG);
                extensionRevMap.put("jpg", ProvFormat.JPEG);
                break;
            case JSON:
                extensionMap.put(ProvFormat.JSON,"json");
                extensionRevMap.put("json", ProvFormat.JSON);
                break;
            case PDF:
                extensionMap.put(ProvFormat.PDF,"pdf");
                extensionRevMap.put("pdf", ProvFormat.PDF);
                break;
            case PROVN:
                extensionMap.put(ProvFormat.PROVN,"provn");
                extensionRevMap.put("provn", ProvFormat.PROVN);
                extensionRevMap.put("pn", ProvFormat.PROVN);
                extensionRevMap.put("asn", ProvFormat.PROVN);
                extensionRevMap.put("prov-asn", ProvFormat.PROVN);

                break;
            case RDFXML:
                extensionMap.put(ProvFormat.RDFXML,"rdf");
                extensionRevMap.put("rdf", ProvFormat.RDFXML);
                break;
            case SVG:
                extensionMap.put(ProvFormat.SVG,"svg");
                extensionRevMap.put("svg", ProvFormat.SVG);
                break;
            case TRIG:
                extensionMap.put(ProvFormat.TRIG,"trig");
                extensionRevMap.put("trig", ProvFormat.TRIG);
                break;
            case TURTLE:
                extensionMap.put(ProvFormat.TURTLE,"ttl");
                extensionRevMap.put("ttl", ProvFormat.TURTLE);               
                break;
            case XML:
                extensionMap.put(ProvFormat.XML,"provx");
                extensionRevMap.put("provx", ProvFormat.XML);             
                extensionRevMap.put("xml", ProvFormat.XML);             
                break;
            default:
                break;
       
            }
        }
        
    }
    
    public String getExtension(ProvFormat format) {
        String extension=UNKNOWN;
        if (format!=null) {
            extension=extensionMap.get(format);
        }
        return extension;
    }
    

    public InteropFramework() {
	this(null, null, null, null, null, null);
    }

    
    public void writeTextToFile(String text,
                                String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(text);
        }
        catch (IOException e) {
        }
        finally {
            try {
                if (writer != null)
                    writer.close( );
            }
            catch (IOException e) {}
        }
    }
    
    /** Validates an input xml file, reads into a Bean instance, saves it as an provnFile.
        TODO: then compare with another provn files? */

/*
    public void xml2provn(String inXmlFile,
			  String outprovnFile,
			  String[] schemaFiles) throws javax.xml.bind.JAXBException,  org.xml.sax.SAXException, java.io.IOException {

        File in=new File(inXmlFile);

        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();

	//TODO: should do xml validation (conditionally?)
	//        deserial.validateDocument(schemaFiles,in);

        Document c=deserial.deserialiseDocument(in);

        String s=u.convertBeanToASN(c);
        writeTextToFile(s,outprovnFile);

    }*/
        


    /** Validates an input xml file, reads into a Bean instance, saves it as an xmlFile.
        TODO: then compare xml files. *//*
    public void xml2xml(String inXmlFile,
                        String outXmlFile,
                        String[] schemaFiles,
                        Hashtable<String,String> outNamespaces) throws javax.xml.bind.JAXBException,  org.xml.sax.SAXException, java.io.IOException {

        File in=new File(inXmlFile);
        File out=new File(outXmlFile);

        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();

        deserial.validateDocument(schemaFiles,in);
        
        Document c=deserial.deserialiseDocument(in);

	//TODO: should do xml validation (conditionally?)
        deserial.validateDocument(schemaFiles,in);

        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        c.setNss(outNamespaces);
        Document c2=(Document)u.convertJavaBeanToJavaBean(c);
        c2.setNss(outNamespaces);
        serial.serialiseDocument(out,c2,true);
    }
        */
    
    /*
    
    public void xml2turtle(String file, String file2, String[] schemaFiles) throws java.io.IOException, JAXBException, Throwable {
    	xml2rdf(file,file2,RDF_TURTLE, schemaFiles);
    }
    public void xml2rdfxml(String file, String file2, String[] schemaFiles) throws java.io.IOException, JAXBException, Throwable {
    	xml2rdf(file,file2,RDF_XML, schemaFiles);
    }
    public void xml2trig(String file, String file2, String[] schemaFiles) throws java.io.IOException, JAXBException, Throwable {
    	xml2rdf(file,file2,RDF_TRIG, schemaFiles);
    }
    public void xml2n3(String file, String file2, String[] schemaFiles) throws java.io.IOException, JAXBException, Throwable {
    	xml2rdf(file,file2,RDF_N3, schemaFiles);
    }
    
    public void xml2rdf(String inXmlFile,
    		            String outXmlFile,
    		            String type,
                        String[] schemaFiles) throws Throwable {

    	File in=new File(inXmlFile);


    	ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();

	//TODO: should do xml validation (conditionally?)
	//    	deserial.validateDocument(schemaFiles,in);
    	
    	Document c=deserial.deserialiseDocument(in);
    	

        RepositoryHelper rHelper=new RepositoryHelper();
        ElmoModule module = new ElmoModule();
        rHelper.registerConcepts(module);
        ElmoManagerFactory factory=new SesameManagerFactory(module);
        ElmoManager manager = factory.createElmoManager();

    	
    	
    	org.openprovenance.prov.rdf.Utility rdfU=new org.openprovenance.prov.rdf.Utility();
    	rdfU.convertTreeToJavaRdf(c, pFactory, manager);
    	
   	
    }

    

    public void provn2xml(String file, String file2) throws java.io.IOException, JAXBException, Throwable {

        CommonTree tree = u.convertASNToTree(file);

        Object o2=u.convertTreeToJavaBean(tree);
       
        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        serial.serialiseDocument(new File(file2),(Document)o2,true);

    }


    public void provn2provn(String file, String file2) throws java.io.IOException, JAXBException, Throwable {

        CommonTree tree = u.convertASNToTree(file);


        String s=u.convertTreeToASN(tree);

        writeTextToFile(s,file2);        

    }


    public void provn2html(String file, String file2) throws java.io.IOException, JAXBException, Throwable {

        CommonTree tree = u.convertASNToTree(file);


        String s=u.convertTreeToHTML(tree);

        writeTextToFile(s,file2);        

    }*/

    public static final String RDF_TURTLE="turtle";
    public static final String RDF_XML="rdf/xml";
    public static final String RDF_TRIG="trig";
    public static final String RDF_N3="n3";
    
    public RDFFormat convert(String type) {
    	if (RDF_TURTLE.equals(type)) return RDFFormat.TURTLE;
    	if (RDF_XML.equals(type)) return RDFFormat.RDFXML;
    	if (RDF_N3.equals(type)) return RDFFormat.N3;
    	if (RDF_TRIG.equals(type)) return RDFFormat.TRIG;
	    return null;
     }
    /*

    public void OLDprovn2turtle(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	provn2rdf(file,file2,RDF_TURTLE);
    }
    public void OLDprovn2rdfxml(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	provn2rdf(file,file2,RDF_XML);
    }
    public void OLDprovn2trig(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	provn2rdf(file,file2,RDF_TRIG);
    }
    public void OLDprovn2n3(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	provn2rdf(file,file2,RDF_N3);
    }
    
    public void OLDprovn2rdf(String file, String file2, String type) throws java.io.IOException, JAXBException, Throwable {
        CommonTree tree = u.convertASNToTree(file);
        tree2rdf(tree, file2, type);
    }
    
    public void LDtree2rdf(CommonTree tree, String file2, String type) throws java.io.IOException, JAXBException, Throwable {


        RepositoryHelper rHelper=new RepositoryHelper();
        ElmoModule module = new ElmoModule();
        rHelper.registerConcepts(module);
        ElmoManagerFactory factory=new SesameManagerFactory(module);
        ElmoManager manager = factory.createElmoManager();

    	
        Document c=(Document)u.convertTreeToJavaBean(tree);
        Hashtable<String,String>  namespaceTable = c.getNss();

        RdfConstructor rdfc=new RdfConstructor(pFactory, manager);
        
        new TreeTraversal(rdfc).convert(tree);
        
        Hashtable<String,String>  allNamespaceTable = rdfc.getNamespaceTable();

        
        
        repository2rdf(manager, rHelper, type, file2, allNamespaceTable);
    }
    */
    public void OLDrepository2rdf(ElmoManager manager, RepositoryHelper rHelper, String type, String file2, Hashtable<String,String>  namespaceTable ) throws Exception {

	rHelper.dumpToRDF(new File(file2),(SesameManager)manager,convert(type),namespaceTable);

    }

    public void OLDprovn2dot(String file, String dotFileOut, String pdfFileOut, String configFile)
	throws java.io.IOException, JAXBException, Throwable {

        Utility u=new Utility();
        CommonTree tree = u.convertASNToTree(file);

        Object o=u.convertTreeToJavaBean(tree);

        Document bundle=(Document)o;

        ProvToDot toDot=new ProvToDot((configFile==null)? "src/main/resources/defaultConfigWithRoleNoLabel.xml" : configFile); 

        toDot.convert(bundle, dotFileOut, pdfFileOut);

    }

    
    public void OLDxml2dot(String fileIn, String dotFileOut, String pdfFileOut,
			 String configFile) throws JAXBException, FileNotFoundException, IOException {
	
    	File in=new File(fileIn);


    	ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();

	//TODO: should do xml validation (conditionally?)
	//    	deserial.validateDocument(schemaFiles,in);
    	
    	Document doc=deserial.deserialiseDocument(in);


	ProvToDot toDot=new ProvToDot((configFile==null)? "src/main/resources/defaultConfigWithRoleNoLabel.xml" : configFile); 

        toDot.convert(doc, dotFileOut, pdfFileOut);
	
    }



    public void OLDrdfxml2xml(String fileIn, String fileOut, Object object) throws RDFParseException, RDFHandlerException, IOException, JAXBException {
	// TODO Auto-generated method stub
	org.openprovenance.prov.rdf.Utility rdfU=new org.openprovenance.prov.rdf.Utility();
	Document doc=rdfU.parseRDF(fileIn);
	
	ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        serial.serialiseDocument(new File(fileOut),doc,true);
	
	
    }

    
    /** Reads a file into java bean. */
    public Object loadProvGraph(String filename) throws java.io.IOException, JAXBException, Throwable {
	try {
	    return loadProvKnownGraph(filename);
	} catch (Throwable e) {
	    e.printStackTrace();
	    return null;
	    //return loadProvUnknownGraph(filename);
	}
    }
    

    public enum ProvFormat {  PROVN, XML, TURTLE, RDFXML, TRIG,  JSON, DOT, JPEG, SVG, PDF }
    
    public ProvFormat getTypeForFile(String filename) {
        int count=filename.lastIndexOf(".");
        if (count==-1) return null;
        String extension=filename.substring(count+1);
        return extensionRevMap.get(extension);
    }

    public void writeDocument(String filename, Document doc) {
	try {
	    ProvFormat format = getTypeForFile(filename);
	    if (format == null) {
		System.err.println("Unknown output file format: " + filename);
		return;
	    }
	    logger.debug("writing " + format);
	    logger.debug("writing " + filename);
	    switch (format) {
	    case PROVN: {
		String s=u.convertBeanToASN(doc);
	        writeTextToFile(s,filename);
	        break;
	    }
	    case XML: {
		ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();
		serial.serialiseDocument(new File(filename), doc, true);
		break;
	    }
	    case TURTLE: {
		new org.openprovenance.prov.rdf.Utility().dumpRDF(pFactory, doc, RDFFormat.TURTLE, filename);
		break;
	    }
	    case RDFXML: {
		new org.openprovenance.prov.rdf.Utility().dumpRDF(pFactory, doc, RDFFormat.RDFXML, filename);
		break;
	    }
	    case TRIG: {
		new org.openprovenance.prov.rdf.Utility().dumpRDF(pFactory, doc, RDFFormat.TRIG, filename);
		break;
	    }
	    case JSON: {
		new org.openprovenance.prov.json.Converter().writeDocument(doc, filename);
		break;
	    }
	    case PDF: {
		String configFile=null; // give it as option
		String dotFileOut="target/foo.dot"; //give it as option, if not available create tmp file
		ProvToDot toDot=new ProvToDot((configFile==null)? "../../ProvToolbox/prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml" : configFile); 
	        toDot.convert(doc, dotFileOut, filename);       
	    }
	    case DOT:
	    case JPEG:
	    case SVG:{
		String configFile=null; // give it as option
		String dotFileOut="target/foo.dot"; //give it as option, if not available create tmp file
		ProvToDot toDot=new ProvToDot((configFile==null)? "../../ProvToolbox/prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml" : configFile); 
	        toDot.convert(doc, dotFileOut, filename, "svg");       
	    }
		
	    default:
		break;}
	} catch (JAXBException e) {
	    if (verbose!=null) e.printStackTrace();
	    throw new InteropException(e);
	    
	} catch (Exception e) {
	    if (verbose!=null) e.printStackTrace();
	    throw new InteropException(e);
	}

    }

    public Object loadProvKnownGraph(String filename) {
        try {
        
        ProvFormat format = getTypeForFile(filename);
        if (format == null) {
            throw new InteropException("Unknown output file format: " + filename);
        }
        
        switch (format) {
        case DOT:
        case JPEG:
        case SVG:
            throw new UnsupportedOperationException(); //we don't load PROV from these formats
        case JSON: {
            return new org.openprovenance.prov.json.Converter().readDocument(filename);
        }
        case PROVN: {
            Utility u=new Utility();
            CommonTree tree = u.convertASNToTree(filename);
            Object o=u.convertTreeToJavaBean(tree);
            return o;
        }
        case RDFXML:
        case TRIG:
        case TURTLE:{
            org.openprovenance.prov.rdf.Utility rdfU=new org.openprovenance.prov.rdf.Utility();
            Document doc=rdfU.parseRDF(filename);
            return doc;
        }    
        case XML: {
            File in=new File(filename);
            ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();
            Document c=deserial.deserialiseDocument(in);
            return c;
        }
        default: {
            System.out.println("Unknown format " + filename);
            throw new UnsupportedOperationException();
        }
        }
        } catch (IOException e) {
            throw new InteropException(e);
        } catch (Throwable e) {
            throw new InteropException(e);

        }
 
    }

    

    public Object loadProvUnknownGraph(String filename)
	throws java.io.IOException, JAXBException, Throwable {
	
	try {
	    Utility u=new Utility();
	    CommonTree tree = u.convertASNToTree(filename);
	    Object o=u.convertTreeToJavaBean(tree);
	    if (o!=null) {
		return o;
	    }
	} catch (Throwable t1) {
	    // OK, we failed, let's try next format.
	}
	try {
	    File in=new File(filename);
	    ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();
	    Document c=deserial.deserialiseDocument(in);
	    if (c!=null) {
		return c;
	    } 
	} catch (Throwable t2) {
	    // OK, we failed, let's try next format.
	}
	
	try {
	    Object o=new org.openprovenance.prov.json.Converter().readDocument(filename);
	    if (o!=null) {
		return o;
	    }
	} catch (RuntimeException e) {
	    // OK, we failed, let's try next format.

	}
	try {
	    org.openprovenance.prov.rdf.Utility rdfU=new org.openprovenance.prov.rdf.Utility();
	    Document doc=rdfU.parseRDF(filename);
	    if (doc!=null) {
		return doc;
	    }
	} catch (RuntimeException e) {
	    //OK, we failed, let's try next format
	}
	System.out.println("Unparseable format " + filename);
	throw new UnsupportedOperationException();
	    
    }


    public void run() {
	if (infile==null) return;
	if (outfile==null) return;
	try {
	    Document doc=(Document) loadProvKnownGraph(infile);
	    doc.setNss(new Hashtable<String, String>());
	    doc.getNss().put("pc1",PC1_NS);
	    doc.getNss().put("prim",PRIM_NS);
	    doc.getNss().put("prov","http://www.w3.org/ns/prov#");
	    doc.getNss().put("xsd","http://www.w3.org/2001/XMLSchema");
	    doc.getNss().put("xsi","http://www.w3.org/2001/XMLSchema-instance");
	    
	    System.out.println("InteropFramework run() -> " + doc.getNss());
	    writeDocument(outfile, doc);
	} catch (Throwable e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

}
