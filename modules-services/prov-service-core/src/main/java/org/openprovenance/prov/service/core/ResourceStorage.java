package org.openprovenance.prov.service.core;

import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceStorage {
    String newStore(Formats.ProvFormat format) throws IOException;
    void copyInputStreamToStore(InputStream inputStream, String id) throws IOException;
    void copyStringToStore(CharSequence str, String id) throws IOException;
    Document readDocument(String id, boolean known) throws IOException;
    Document readDocument(String id) throws IOException;
    void writeDocument(String id, Formats.ProvFormat format, Document doc) throws IOException;

    boolean delete(String storageId);
}
