package org.openprovenance.prov.interop;
import java.io.File;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.net.URI;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;


import org.openprovenance.prov.xml.BeanTraversal;
import org.openprovenance.prov.xml.Bundle;
import org.openprovenance.prov.xml.ProvDeserialiser;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.NamespacePrefixMapper;

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
        File out=new File(outAsnFile);

        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();

        deserial.validateBundle(schemaFiles,in);

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

        String o3=u.convertTreeToASN(tree);

        

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


    public void asn2rdf(String file, String file2) throws java.io.IOException, JAXBException, Throwable {

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

        LinkedList prefixes=new LinkedList();

        Hashtable<String,String>  namespaceTable = c.getNss();
        for (String k: namespaceTable.keySet()) {
            String [] ss=new String[2];
            ss[0]=k;
            ss[1]=namespaceTable.get(k);
            prefixes.add(ss);  // why not take a hashtable in dumpToRDF?
        }

        rHelper.dumpToRDF(new File(file2),(SesameManager)manager,RDFFormat.TURTLE,prefixes);

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
    public Object loadProvGraph(String filename)
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
	} else {
	    System.out.println("Unkownn format " + filename);
	    throw new UnsupportedOperationException();
	}
    }


    public static void main(String [] args) throws Exception { //TODO: finalize signatures
        if ((args==null) || (!((args.length>=3) || (args.length<=5)))) {
            System.out.println("Usage: provconvert -asn2rdf fileIn fileOut");
            System.out.println("Usage: provconvert -asn2xml fileIn fileOut");
            System.out.println("Usage: provconvert -asn2asn fileIn fileOut");
            System.out.println("Usage: provconvert -asn2html fileIn fileOut");
            System.out.println("Usage: provconvert -xml2xml fileIn fileOut");
            System.out.println("Usage: provconvert -xml2asn fileIn fileOut");
            System.out.println("Usage: provconvert -asn2dot fileIn dotFileOut pdfFileOut [configFile]");
            
            return;
        }

        try {
            InteropFramework me=new InteropFramework();
            String fileIn=args[1];
            String fileOut=args[2];
            if (args[0].equals("-asn2rdf")) {
                me.asn2rdf(fileIn,fileOut);
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
                me.xml2xml(fileIn,fileOut,null,null);
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

        //TODO: other options here


    }

}
