package org.openprovenance.prov.benchmarks;

import tools.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvSerialiser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

class RawJsonSerialiser implements ProvSerialiser {
    ObjectMapper om=tools.jackson.databind.json.JsonMapper.builderWithJackson2Defaults().build();
    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        try {
            om.writeValue(out,document);
        } catch (tools.jackson.core.JacksonException e) { // jackson 3: unchecked
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}