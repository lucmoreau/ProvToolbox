package org.openprovenance.prov.xml;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;


/** Deserialiser of OPM Graphs. */
public class ProvDeserialiser {



    // it is recommended by the Jaxb documentation that one JAXB
    // context is created for one application. This object is thread
    // safe (in the sun impelmenation, but not
    // marshallers/unmarshallers.

    static protected JAXBContext jc;

    public ProvDeserialiser () throws JAXBException {
        if (jc==null) 
            jc = JAXBContext.newInstance( ProvFactory.packageList );
        // note, it is sometimes recommended to pass the current classloader
        
    }

    public ProvDeserialiser (String packageList) throws JAXBException {
        if (jc==null) 
            jc = JAXBContext.newInstance(packageList);
    }


    private static ThreadLocal<ProvDeserialiser> threadDeserialiser=
        new ThreadLocal<ProvDeserialiser> () {
        protected synchronized ProvDeserialiser initialValue () {
                try {
                    return new ProvDeserialiser();
                } catch (JAXBException jxb) {
                    throw new RuntimeException("ProvDeserialiser: deserialiser init failure()");
                }
            }
    };

    public static ProvDeserialiser getThreadProvDeserialiser() {
        return threadDeserialiser.get();
    }

    public Bundle deserialiseBundle (Element serialised)
        throws JAXBException {
        Unmarshaller u=jc.createUnmarshaller();
        JAXBElement<Bundle> root= u.unmarshal(serialised,Bundle.class);
        Bundle res=root.getValue();
        return res;
    }

    public Bundle deserialiseBundle (File serialised)
        throws JAXBException {
        Unmarshaller u=jc.createUnmarshaller();
        Object root= u.unmarshal(serialised);
        @SuppressWarnings("unchecked")
        Bundle res=(Bundle)((JAXBElement<Bundle>) root).getValue();
        return res;
    }


    public Bundle validateBundle (String[] schemaFiles, File serialised)         throws JAXBException,SAXException, IOException { 
        return validateBundle (schemaFiles, serialised,true);
    }

    public Bundle validateBundle (String[] schemaFiles, File serialised, boolean withCurie)
        throws JAXBException,SAXException, IOException {
        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source [] sources=new Source[2+schemaFiles.length];
        int schemaCount;
        if (withCurie) {
            schemaCount=3;
            sources=new Source[schemaCount+schemaFiles.length];
            sources[0]=new StreamSource(this.getClass().getResourceAsStream("/"+"curie.xsd"));
            sources[1]=new StreamSource(this.getClass().getResourceAsStream("/"+"xml.xsd"));
            sources[2]=new StreamSource(this.getClass().getResourceAsStream("/"+"prov-20120110-curie.xsd"));
        } else {
            schemaCount=3;
            sources=new Source[schemaCount+schemaFiles.length];
            sources[0]=new StreamSource(this.getClass().getResourceAsStream("/"+"curie.xsd"));
            sources[1]=new StreamSource(this.getClass().getResourceAsStream("/"+"xml.xsd"));
            sources[2]=new StreamSource(this.getClass().getResourceAsStream("/"+"prov-20120110.xsd"));
        }


        int i=0;
        for (String schemaFile: schemaFiles) {
            sources[schemaCount+i]=new StreamSource(new File(schemaFile));
            i++;
        }
        Schema schema = sf.newSchema(sources);  
        Unmarshaller u=jc.createUnmarshaller();
        //u.setValidating(true); was jaxb1.0
        u.setSchema(schema);
        Object root= u.unmarshal(serialised);
        @SuppressWarnings("unchecked")
        Bundle res=(Bundle)((JAXBElement<Bundle>) root).getValue();
        return res;
    }

    public static void main(String [] args) {
        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();
        if ((args==null) ||  (args.length==0)) {
            System.out.println("Usage: opmxml-validate <filename> {schemaFiles}*");
            return;
        }
        File f=new File(args[0]);
        String [] schemas=new String[args.length-1];
        for (int i=1; i< args.length; i++) {
            schemas[i-1]=args[i];
        }
        try {
            deserial.validateBundle(schemas,f);
            System.out.println(args[0] + " IS a valid OPM graph");
            return ;
        } catch (JAXBException je) {
            je.printStackTrace();
            System.out.println(args[0] + " IS NOT a valid OPM graph");
        } catch (SAXException je) {
            je.printStackTrace();
            System.out.println(args[0] + " IS NOT a valid OPM graph");
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(args[0] + " IS NOT a valid OPM graph");
        }
    }
}
