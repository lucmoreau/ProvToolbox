package org.openprovenance.prov.template.compiler.past.emitter;

import java.io.File;
import java.io.IOException;

public interface WritableObject {
    void writeTo(File directory) throws IOException ;
}