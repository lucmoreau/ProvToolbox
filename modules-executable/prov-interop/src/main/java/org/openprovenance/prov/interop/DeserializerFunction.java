package org.openprovenance.prov.interop;

/* Functional interface, without argument, returning a deserializer function. This is implemented as a functional interface, since soem serialization method are thread specific, and therefore, need to be called in the current thread serializing the document. */

public interface DeserializerFunction {
        org.openprovenance.prov.model.ProvDeserialiser apply();
}
