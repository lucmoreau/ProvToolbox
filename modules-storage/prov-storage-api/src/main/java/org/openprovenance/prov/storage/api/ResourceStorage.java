package org.openprovenance.prov.storage.api;

import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;

public interface ResourceStorage {
    String newStore(ProvFormat format) throws IOException;
    void copyInputStreamToStore(InputStream inputStream, ProvFormat format, String id) throws IOException;
    void copyStringToStore(CharSequence str, ProvFormat format, String id) throws IOException;
    Document readDocument(String id, boolean known) throws IOException;
    Document readDocument(String id, boolean known, DateTimeOption dateTimeOption, TimeZone timeZone) throws IOException;
    Document readDocument(String id) throws IOException;
    void writeDocument(String id, ProvFormat format, Document doc) throws IOException;
    boolean delete(String storageId);
}
