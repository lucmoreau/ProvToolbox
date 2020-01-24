package org.openprovenance.prov.storage.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface NonDocumentGenericResourceStorage<T> {
    String newStore(String suggestedExtension, String mimeType) throws IOException;
    void copyInputStreamToStore(InputStream inputStream, String id) throws IOException;
    void copyStringToStore(CharSequence str, String id) throws IOException;

    void serializeObjectToStore(T o, String id) throws IOException;

    void copyStoreToOutputStream(String id, OutputStream outputStream) throws IOException;

    T deserializeObjectFromStore(String id) throws IOException;

    boolean delete(String storageId);
}
