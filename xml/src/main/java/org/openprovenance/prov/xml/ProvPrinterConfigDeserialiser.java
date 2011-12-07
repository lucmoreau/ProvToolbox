package org.openprovenance.prov.printer;
import java.io.File;
import java.io.InputStream;
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


/** Deserialiser of Prov Graphs. */
public class ProvPrinterConfigDeserialiser {



    // it is recommended by the Jaxb documentation that one JAXB
    // context is created for one application. This object is thread
    // safe (in the sun impelmenation, but not
    // marshallers/unmarshallers.

    static protected JAXBContext jc;

    public ProvPrinterConfigDeserialiser () throws JAXBException {
        if (jc==null) 
            jc = JAXBContext.newInstance( "org.openprovenance.model.printer" );
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
        ProvPrinterConfiguration res=(ProvPrinterConfiguration)((JAXBElement<ProvPrinterConfiguration>) root).getValue();
        return res;
    }

    public ProvPrinterConfiguration deserialiseProvPrinterConfiguration (InputStream serialised)
        throws JAXBException {
        Unmarshaller u=jc.createUnmarshaller();
        Object root= u.unmarshal(serialised);
        ProvPrinterConfiguration res=(ProvPrinterConfiguration)((JAXBElement<ProvPrinterConfiguration>) root).getValue();
        return res;
    }

}
