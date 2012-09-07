package org.openprovenance.prov.dot;
import java.io.File;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;


/** Deserialiser of Prov Graphs. */
public class ProvPrinterConfigDeserialiser {



    // it is recommended by the Jaxb documentation that one JAXB
    // context is created for one application. This object is thread
    // safe (in the sun impelmenation, but not
    // marshallers/unmarshallers.

    static protected JAXBContext jc;

    public ProvPrinterConfigDeserialiser () throws JAXBException {
        if (jc==null) 
            jc = JAXBContext.newInstance( "org.openprovenance.prov.dot" );
    }

    public ProvPrinterConfigDeserialiser (String packageList) throws JAXBException {
        if (jc==null) 
            jc = JAXBContext.newInstance(packageList);
    }


    private static ThreadLocal<ProvPrinterConfigDeserialiser> threadDeserialiser=
        new ThreadLocal<ProvPrinterConfigDeserialiser> () {
        protected synchronized ProvPrinterConfigDeserialiser initialValue () {
                try {
                    return new ProvPrinterConfigDeserialiser();
                } catch (JAXBException jxb) {
                    jxb.printStackTrace();
                    throw new RuntimeException("ProvPrinterConfigDeserialiser: deserialiser init failure()");
                }
            }
    };

    public static ProvPrinterConfigDeserialiser getThreadProvPrinterConfigDeserialiser() {
        return threadDeserialiser.get();
    }


    public ProvPrinterConfiguration deserialiseProvPrinterConfiguration (File serialised)
        throws JAXBException {
        Unmarshaller u=jc.createUnmarshaller();
        Object root= u.unmarshal(serialised);
        @SuppressWarnings("unchecked")
        ProvPrinterConfiguration res=(ProvPrinterConfiguration)((JAXBElement<ProvPrinterConfiguration>) root).getValue();
        return res;
    }

    public ProvPrinterConfiguration deserialiseProvPrinterConfiguration (InputStream serialised)
        throws JAXBException {
        Unmarshaller u=jc.createUnmarshaller();
        Object root= u.unmarshal(serialised);
        @SuppressWarnings("unchecked")
        ProvPrinterConfiguration res=(ProvPrinterConfiguration)((JAXBElement<ProvPrinterConfiguration>) root).getValue();
        return res;
    }

}
