package org.openprovenance.prov.benchmarks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvSerialiser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

class RawJsonSerialiser implements ProvSerialiser {
    ObjectMapper om=new ObjectMapper();
    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        try {
            om.writeValue(out,document);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}