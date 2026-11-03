package org.openprovenance.prov.benchmarks;

import tools.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvDeserialiser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


class RawJsonDeserialiser implements ProvDeserialiser {
    static ObjectMapper om=tools.jackson.databind.json.JsonMapper.builderWithJackson2Defaults().build();
    @Override
    public Document deserialiseDocument(InputStream in) throws IOException {
        om.readValue(in, Map.class);
        return null;
    }
}