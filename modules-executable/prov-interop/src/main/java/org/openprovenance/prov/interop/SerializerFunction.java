package org.openprovenance.prov.interop;

/* Functional interface, without argument, returning a serializer function. This is implemented as a functional interface, since soem serialization method are thread specific, and therefore, need to be called in the current thread serializing the document. */

public interface SerializerFunction {
        org.openprovenance.prov.model.ProvSerialiser apply();
}
