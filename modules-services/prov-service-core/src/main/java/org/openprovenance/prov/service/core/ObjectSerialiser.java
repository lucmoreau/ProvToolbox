package org.openprovenance.prov.service.core;

import java.io.OutputStream;

public interface ObjectSerialiser {
    void serialiseObject (OutputStream out, Object document, String mediaType, boolean formatted);
}


