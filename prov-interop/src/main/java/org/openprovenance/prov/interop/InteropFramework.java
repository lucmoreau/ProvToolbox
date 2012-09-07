package org.openprovenance.prov.interop;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Hashtable;
import javax.xml.bind.JAXBException;
import org.openprovenance.prov.xml.Bundle;
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
    
    /** Validates an input xml file, reads into a Bean instance, saves it as an asnFile.
        TODO: then compare with another asn files? */


    public void xml2asn(String inXmlFile,
                        String outAsnFile,
                        String[] schemaFiles) throws javax.xml.bind.JAXBException,  org.xml.sax.SAXException, java.io.IOException {

        File in=new File(inXmlFile);

        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();

	//        deserial.validateBundle(schemaFiles,in);

        Bundle c=deserial.deserialiseBundle(in);

        Utility u=new Utility();
        String s=u.convertBeanToASN(c);
        writeTextToFile(s,outAsnFile);

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

        deserial.validateBundle(schemaFiles,in);
        
        Bundle c=deserial.deserialiseBundle(in);

        deserial.validateBundle(schemaFiles,in);

        Utility u=new Utility();        
        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        c.setNss(outNamespaces);
        Bundle c2=(Bundle)u.convertJavaBeanToJavaBean(c);
        c2.setNss(outNamespaces);
        serial.serialiseBundle(out,c2,true);
    }
        

    public void asn2xml(String file, String file2) throws java.io.IOException, JAXBException, Throwable {

        Utility u=new Utility();
        CommonTree tree = u.convertASNToTree(file);

        Object o2=u.convertTreeToJavaBean(tree);
       
        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        serial.serialiseBundle(new File(file2),(Bundle)o2,true);

    }


    public void asn2asn(String file, String file2) throws java.io.IOException, JAXBException, Throwable {

        Utility u=new Utility();
        CommonTree tree = u.convertASNToTree(file);


        String s=u.convertTreeToASN(tree);

        writeTextToFile(s,file2);        

    }


    public void asn2html(String file, String file2) throws java.io.IOException, JAXBException, Throwable {

        Utility u=new Utility();
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

    public void asn2turtle(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	asn2rdf(file,file2,RDF_TURTLE);
    }
    public void asn2rdfxml(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	asn2rdf(file,file2,RDF_XML);
    }
    public void asn2trig(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	asn2rdf(file,file2,RDF_TRIG);
    }
    public void asn2n3(String file, String file2) throws java.io.IOException, JAXBException, Throwable {
    	asn2rdf(file,file2,RDF_N3);
    }

    public void asn2rdf(String file, String file2, String type) throws java.io.IOException, JAXBException, Throwable {

        Utility u=new Utility();

        ProvFactory pFactory=ProvFactory.getFactory();


        RepositoryHelper rHelper=new RepositoryHelper();
        ElmoModule module = new ElmoModule();
        rHelper.registerConcepts(module);
        ElmoManagerFactory factory=new SesameManagerFactory(module);
        ElmoManager manager = factory.createElmoManager();

        CommonTree tree = u.convertASNToTree(file);
        Bundle c=(Bundle)u.convertTreeToJavaBean(tree);
        
        new TreeTraversal(new RdfConstructor(pFactory, manager)).convert(tree);

        LinkedList<String[]> prefixes=new LinkedList<String[]>();

        Hashtable<String,String>  namespaceTable = c.getNss();
        for (String k: namespaceTable.keySet()) {
            String [] ss=new String[2];
            ss[0]=k;
            ss[1]=namespaceTable.get(k);
            prefixes.add(ss);  // why not take a hashtable in dumpToRDF?
        }

        rHelper.dumpToRDF(new File(file2),(SesameManager)manager,convert(type),prefixes);

    }

    public void asn2dot(String file, String dotFileOut, String pdfFileOut, String configFile)
	throws java.io.IOException, JAXBException, Throwable {

        Utility u=new Utility();
        CommonTree tree = u.convertASNToTree(file);

        Object o=u.convertTreeToJavaBean(tree);

        Bundle bundle=(Bundle)o;

        ProvToDot toDot=new ProvToDot((configFile==null)? "src/main/resources/defaultConfigWithRoleNoLabel.xml" : configFile); 

        toDot.convert(bundle, dotFileOut, pdfFileOut);

    }

    /** Reads a file into java bean. */
    public Object loadProvGraph(String filename) throws java.io.IOException, JAXBException, Throwable {
	try {
	    return loadProvKnownGraph(filename);
	} catch (Throwable e) {
	    return loadProvUnknownGraph(filename);
	}
    }

    public Object loadProvKnownGraph(String filename)
	throws java.io.IOException, JAXBException, Throwable {
	
	if (filename.endsWith(".provn")) {
	    Utility u=new Utility();
	    CommonTree tree = u.convertASNToTree(filename);
	    Object o=u.convertTreeToJavaBean(tree);
	    return o;
	} else if (filename.endsWith(".provx") || filename.endsWith(".xml")) {
	    File in=new File(filename);
	    ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();
	    Bundle c=deserial.deserialiseBundle(in);
	    return c;
	} else if (filename.endsWith(".rdf") || filename.endsWith(".prov-rdf")) {
	    throw new UnsupportedOperationException();
	} else if (filename.endsWith(".json")) {
	    throw new UnsupportedOperationException();
	} else {
	    System.out.println("Unkown format " + filename);
	    throw new UnsupportedOperationException();
	}
    }

    public Object loadProvUnknownGraph(String filename)
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
		Bundle c=deserial.deserialiseBundle(in);
		return c;
	    } catch (Throwable t2) {
		System.out.println("Unparseable format " + filename);
		throw new UnsupportedOperationException();
	    }
	}
    }

    public static void help() {
        System.out.println("Usage: provconvert -asn2turtle fileIn fileOut");
        System.out.println("Usage: provconvert -asn2rdfxml fileIn fileOut");
        System.out.println("Usage: provconvert -asn2trig fileIn fileOut");
        System.out.println("Usage: provconvert -asn2n3 fileIn fileOut");
        System.out.println("Usage: provconvert -asn2xml fileIn fileOut");
        System.out.println("Usage: provconvert -asn2asn fileIn fileOut");
        System.out.println("Usage: provconvert -asn2html fileIn fileOut");
        System.out.println("Usage: provconvert -xml2xml fileIn fileOut");
        System.out.println("Usage: provconvert -xml2asn fileIn fileOut");
        System.out.println("Usage: provconvert -asn2dot fileIn dotFileOut pdfFileOut [configFile]");
    }

    public static void main(String [] args) throws Exception { //TODO: finalize signatures
        if ((args==null) || (!((args.length>=3) && (args.length<=5)))) {
            help();
            return;
        }

        try {
            InteropFramework me=new InteropFramework();
            String fileIn=args[1];
            String fileOut=args[2];
            
            if (args[0].equals("-asn2turtle")) {
                me.asn2turtle(fileIn,fileOut);
                return;
            }
            if (args[0].equals("-asn2rdfxml")) {
                me.asn2rdfxml(fileIn,fileOut);
                return;
            }
            if (args[0].equals("-asn2trig")) {
                me.asn2trig(fileIn,fileOut);
                return;
            }
            if (args[0].equals("-asn2n3")) {
                me.asn2n3(fileIn,fileOut);
                return;
            }
            if (args[0].equals("-asn2asn")) {
                me.asn2asn(fileIn,fileOut);
                return;
            }

            if (args[0].equals("-asn2asn")) {
                me.asn2asn(fileIn,fileOut);
                return;
            }


            if (args[0].equals("-asn2html")) {
                me.asn2html(fileIn,fileOut);
                return;
            }


            if (args[0].equals("-asn2xml")) {
                me.asn2xml(fileIn,fileOut);
                return;
            }

            if (args[0].equals("-xml2xml")) {
                me.xml2xml(fileIn,fileOut,null,null);
                return;
            }

            if (args[0].equals("-xml2asn")) {
                me.xml2asn(fileIn,fileOut,null);
                return;
            }


            if (args[0].equals("-asn2dot")) {
		String pdfFileOut=args[3];
		String configFile;
		configFile= ((args.length==5)? args[4] : null);
                me.asn2dot(fileIn,fileOut,pdfFileOut,configFile);
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        help();

        //TODO: other options here


    }

}
