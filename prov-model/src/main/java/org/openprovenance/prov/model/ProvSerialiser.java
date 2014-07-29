package org.openprovenance.prov.model;

import java.io.OutputStream;

import javax.xml.bind.JAXBException;

public interface ProvSerialiser {

    public void serialiseDocument (OutputStream out, Document graph, boolean format)
	        throws JAXBException;
    
}
