package org.openprovenance.prov.benchmarks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvDeserialiser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


class RawJsonDeserialiser implements ProvDeserialiser {
    static ObjectMapper om=new ObjectMapper();
    @Override
    public Document deserialiseDocument(InputStream in) throws IOException {
        om.readValue(in, Map.class);
        return null;
    }
}