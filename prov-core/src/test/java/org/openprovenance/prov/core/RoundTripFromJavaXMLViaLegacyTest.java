package org.openprovenance.prov.core;

import org.openprovenance.prov.core.vanilla.ProvFactory;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HasType;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RoundTripFromJavaXMLViaLegacyTest extends RoundTripFromJavaXMLTest {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoundTripFromJavaXMLViaLegacyTest(String testName) {
        super(testName);
    }

    ProvFactory pf=new ProvFactory();
    public Document readXMLDocument(String file)
            throws IOException {

        System.out.println("reading (xml) from " + file);


      //  ProvDeserialiser deserial=new ProvDeserialiser();
       // return deserial.deserialiseDocument(new File(file));

        Document doc= null;
        try {
            doc = org.openprovenance.prov.xml.ProvDeserialiser.getThreadProvDeserialiser().deserialiseDocument(new File(file));
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        BeanTraversal bc=new BeanTraversal(pFactory, pFactory);
        org.openprovenance.prov.model.Document doc2=bc.doAction(doc);
        return doc2;
    }

    @Override
    public boolean wrapper_erase() {
        return true;
    }
}
