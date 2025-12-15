package org.openprovenance.prov.interop;

import org.openprovenance.prov.model.ProvDeserialiser;
import org.openprovenance.prov.model.ProvSerialiser;

import java.util.HashMap;
import java.util.Map;

public class BatchExecutor {

    public static void main(String[] args) {
        Map<String, ProvDeserialiser> deserializerMap2 = new HashMap<>();
        Map<String, ProvSerialiser> serializerMap2 = new HashMap<>();
        new InteropFramework().populateSerializerDeserializerMaps(deserializerMap2, serializerMap2);
        new org.openprovenance.prov.template.expander.ttf.BatchExecutor(serializerMap2, deserializerMap2).execute(args);
    }
}