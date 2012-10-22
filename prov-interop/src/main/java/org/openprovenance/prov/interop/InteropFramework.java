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


/**
 * The interoperability framework for PROV.
 */
public class InteropFramework 
{

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

    public InteropFramework(String verbose,
	    String debug, String logfile, String infile, String outfile,
	    String namespaces) {
	this.verbose=verbose;
	this.debug=debug;
	this.logfile=logfile;
	this.infile=infile;
	this.outfile=outfile;
	this.namespaces=namespaces;
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

    }
        


    /** Validates an input xml file, reads into a Bean instance, saves it as an xmlFile.
        TODO: then compare xml files. */

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

    }

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

    public void provn2turtle(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	provn2rdf(file,file2,RDF_TURTLE);
    }
    public void provn2rdfxml(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	provn2rdf(file,file2,RDF_XML);
    }
    public void provn2trig(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	provn2rdf(file,file2,RDF_TRIG);
    }
    public void provn2n3(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	provn2rdf(file,file2,RDF_N3);
    }
    
    public void provn2rdf(String file, String file2, String type) throws java.io.IOException, JAXBException, Throwable {
        CommonTree tree = u.convertASNToTree(file);
        tree2rdf(tree, file2, type);
    }
    
    public void tree2rdf(CommonTree tree, String file2, String type) throws java.io.IOException, JAXBException, Throwable {


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
    
    public void repository2rdf(ElmoManager manager, RepositoryHelper rHelper, String type, String file2, Hashtable<String,String>  namespaceTable ) throws Exception {

	rHelper.dumpToRDF(new File(file2),(SesameManager)manager,convert(type),namespaceTable);

    }

    public void provn2dot(String file, String dotFileOut, String pdfFileOut, String configFile)
	throws java.io.IOException, JAXBException, Throwable {

        Utility u=new Utility();
        CommonTree tree = u.convertASNToTree(file);

        Object o=u.convertTreeToJavaBean(tree);

        Document bundle=(Document)o;

        ProvToDot toDot=new ProvToDot((configFile==null)? "src/main/resources/defaultConfigWithRoleNoLabel.xml" : configFile); 

        toDot.convert(bundle, dotFileOut, pdfFileOut);

    }

    
    public void xml2dot(String fileIn, String dotFileOut, String pdfFileOut,
			 String configFile) throws JAXBException, FileNotFoundException, IOException {
	
    	File in=new File(fileIn);


    	ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();

	//TODO: should do xml validation (conditionally?)
	//    	deserial.validateDocument(schemaFiles,in);
    	
    	Document doc=deserial.deserialiseDocument(in);


	ProvToDot toDot=new ProvToDot((configFile==null)? "src/main/resources/defaultConfigWithRoleNoLabel.xml" : configFile); 

        toDot.convert(doc, dotFileOut, pdfFileOut);
	
    }



    public void rdfxml2xml(String fileIn, String fileOut, Object object) throws RDFParseException, RDFHandlerException, IOException, JAXBException {
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
	if ((filename.endsWith(".provn"))  || filename.endsWith(".prov-asn") || filename.endsWith(".pn")) {  //legacy extensions
	    return ProvFormat.PROVN;
	} else if (filename.endsWith(".provx") || filename.endsWith(".xml")) {
	    return ProvFormat.XML;
	} else if (filename.endsWith(".rdf")) {
	    return ProvFormat.RDFXML;
	} else if (filename.endsWith(".json")) {
	    return ProvFormat.JSON;
	} else if (filename.endsWith(".trig")) {
	    return ProvFormat.TRIG;
	} else if (filename.endsWith(".ttl")) {
	    return ProvFormat.TURTLE;
	} else if (filename.endsWith(".svg")) {
	    return ProvFormat.SVG;
	} else if (filename.endsWith(".pdf")) {
	    return ProvFormat.PDF;
	} else if (filename.endsWith(".dot")) {
	    return ProvFormat.DOT;
	} else if ((filename.endsWith(".jpeg")) ||  (filename.endsWith(".jpg"))) {
	    return ProvFormat.JPEG;
	} else {
	    return null;
	}
    }

    public void writeDocument(String filename, Document doc) {
	try {
	    ProvFormat format = getTypeForFile(filename);
	    if (format == null) {
		System.err.println("Unknown output file format: " + filename);
		return;
	    }
	    System.err.println("writing " + format);
	    System.err.println("writing " + filename);
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
	    }
	    case PDF: {
		String configFile=null; // give it as option
		String dotFileOut="target/foo.dot"; //give it as option, if not available create tmp file
		ProvToDot toDot=new ProvToDot((configFile==null)? "../prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml" : configFile); 
	        toDot.convert(doc, dotFileOut, filename);       
	    }}
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
            System.err.println("Unknown output file format: " + filename);
            return null;
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	 
    }

    

    public Object IGNOREloadProvUnknownGraph(String filename)
	throws java.io.IOException, JAXBException, Throwable {
	
	try {
	    Utility u=new Utility();
	    CommonTree tree = u.convertASNToTree(filename);
	    Object o=u.convertTreeToJavaBean(tree);
	    return o;
	} catch (Throwable t1) {
	    try {
		File in=new File(filename);
		ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();
		Document c=deserial.deserialiseDocument(in);
		return c;
	    } catch (Throwable t2) {
		System.out.println("Unparseable format " + filename);
		throw new UnsupportedOperationException();
	    }
	}
    }

    public static void help() {
        System.out.println("Usage: provconvert -provn2turtle fileIn fileOut");
        System.out.println("Usage: provconvert -provn2rdfxml fileIn fileOut");
        System.out.println("Usage: provconvert -provn2trig fileIn fileOut");
        System.out.println("Usage: provconvert -provn2n3 fileIn fileOut");
        System.out.println("Usage: provconvert -provn2xml fileIn fileOut");
        System.out.println("Usage: provconvert -provn2provn fileIn fileOut");
        
        System.out.println("Usage: provconvert -xml2turtle fileIn fileOut");
        System.out.println("Usage: provconvert -xml2rdfxml fileIn fileOut");
        System.out.println("Usage: provconvert -xml2trig fileIn fileOut");
        System.out.println("Usage: provconvert -xml2n3 fileIn fileOut");     
        System.out.println("Usage: provconvert -xml2xml fileIn fileOut");
        System.out.println("Usage: provconvert -xml2provn fileIn fileOut");
        
        System.out.println("Usage: provconvert -xml2html fileIn fileOut");
        
        System.out.println("Usage: provconvert -rdfxml2xml fileIn fileOut");
        
	System.out.println("Usage: provconvert -xml2dot fileIn dotFileOut pdfFileOut [configFile]");
        System.out.println("Usage: provconvert -provn2dot fileIn dotFileOut pdfFileOut [configFile]");
    }

    public static void IGNOREmain(String [] args) throws Exception { //TODO: finalize signatures
        if ((args==null) || (!((args.length>=3) && (args.length<=5)))) {
            help();
            return;
        }

        try {
            InteropFramework me=new InteropFramework();
            String fileIn=args[1];
            String fileOut=args[2];
            
            if (args[0].equals("-provn2turtle")) {
                me.provn2turtle(fileIn,fileOut);
                return;
            }
            if (args[0].equals("-provn2rdfxml")) {
                me.provn2rdfxml(fileIn,fileOut);
                return;
            }
            if (args[0].equals("-provn2trig")) {
                me.provn2trig(fileIn,fileOut);
                return;
            }
            if (args[0].equals("-provn2n3")) {
                me.provn2n3(fileIn,fileOut);
                return;
            }
            if (args[0].equals("-provn2provn")) {
                me.provn2provn(fileIn,fileOut);
                return;
            }

            if (args[0].equals("-provn2provn")) {
                me.provn2provn(fileIn,fileOut);
                return;
            }


            if (args[0].equals("-provn2html")) {
                me.provn2html(fileIn,fileOut);
                return;
            }


            if (args[0].equals("-xml2turtle")) {
                me.xml2turtle(fileIn,fileOut,null);
                return;
            }
            if (args[0].equals("-xml2rdfxml")) {
                me.xml2rdfxml(fileIn,fileOut,null);
                return;
            }
            if (args[0].equals("-xml2trig")) {
                me.xml2trig(fileIn,fileOut,null);
                return;
            }
            if (args[0].equals("-xml2n3")) {
                me.xml2n3(fileIn,fileOut,null);
                return;
            }
  
            if (args[0].equals("-xml2xml")) {
                me.xml2xml(fileIn,fileOut,null,null);
                return;
            }

            if (args[0].equals("-xml2provn")) {
                me.xml2provn(fileIn,fileOut,null);
                return;
            }

            if (args[0].equals("-rdfxml2xml")) {
                me.rdfxml2xml(fileIn,fileOut,null);
                return;
            }



            if (args[0].equals("-provn2dot")) {
		String pdfFileOut=args[3];
		String configFile;
		configFile= ((args.length==5)? args[4] : null);
                me.provn2dot(fileIn,fileOut,pdfFileOut,configFile);
                return;
            }

            if (args[0].equals("-xml2dot")) {
		String pdfFileOut=args[3];
		String configFile;
		configFile= ((args.length==5)? args[4] : null);
                me.xml2dot(fileIn,fileOut,pdfFileOut,configFile);
                return;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        help();

        //TODO: other options here


    }

    public void run() {
	if (infile==null) return;
	if (outfile==null) return;
	try {
	    Document doc=(Document) loadProvGraph(infile);
	    doc.setNss(new Hashtable<String, String>());
	    doc.getNss().put("pc1",PC1_NS);
	    doc.getNss().put("prim",PRIM_NS);
	    doc.getNss().put("prov","http://www.w3.org/ns/prov#");
	    doc.getNss().put("xsd","http://www.w3.org/2001/XMLSchema");
	    doc.getNss().put("xsi","http://www.w3.org/2001/XMLSchema-instance");
	    
	    System.out.println("InteropFramework run() -> " + doc.getNss());
	    writeDocument(outfile, doc);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (JAXBException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Throwable e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

}
