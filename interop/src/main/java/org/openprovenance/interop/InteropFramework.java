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
import org.openprovenance.prov.xml.Container;
import org.openprovenance.prov.xml.ProvDeserialiser;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.NamespacePrefixMapper;

import org.openprovenance.prov.asn.Utility;

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

        deserial.validateContainer(schemaFiles,in);

        Container c=deserial.deserialiseContainer(in);

        Utility u=new Utility();
        String s=u.convertBeanToASN(c);
        writeTextToFile(s,outAsnFile);

    }
        


    /** Validates an input xml file, reads into a Bean instance, saves it as an xmlFile.
        TODO: then compare xml files. */

    public void xml2xml(String inXmlFile,
                        String outXmlFile,
                        String[] schemaFiles) throws javax.xml.bind.JAXBException,  org.xml.sax.SAXException, java.io.IOException {

        File in=new File(inXmlFile);
        File out=new File(outXmlFile);

        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();

        deserial.validateContainer(schemaFiles,in);
        
        Container c=deserial.deserialiseContainer(in);

        deserial.validateContainer(schemaFiles,in);

        Utility u=new Utility();        
        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        Container c2=(Container)u.convertJavaBeanToJavaBean(c);
        serial.serialiseContainer(out,c2,true);
    }
        


}
