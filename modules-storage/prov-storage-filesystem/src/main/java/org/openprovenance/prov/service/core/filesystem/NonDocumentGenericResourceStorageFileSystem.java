package org.openprovenance.prov.service.core.filesystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.service.core.NonDocumentGenericResourceStorage;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class NonDocumentGenericResourceStorageFileSystem<TYPE> implements NonDocumentGenericResourceStorage<TYPE> {

    private final InteropFramework interop;

    private final ObjectMapper om;
    private final Class<TYPE> clazz;
    private final File location;

    public NonDocumentGenericResourceStorageFileSystem(ObjectMapper om, Class<TYPE> clazz, File location) {
        this.om=om;
        this.clazz=clazz;
        this.interop=new InteropFramework();
        this.location=location;
    }


    @Override
    public String newStore(String suggestedExtension, String mimeType) throws IOException {
        if (suggestedExtension==null) suggestedExtension="UNKNOWN";
        File temp = createTempFile(suggestedExtension);
        return temp.getAbsolutePath();
    }

    @Override
    public void copyInputStreamToStore(InputStream inputStream, String id) throws IOException {
        FileUtils.copyInputStreamToFile(inputStream, new File(id));
    }

    @Override
    public void copyStringToStore(CharSequence str, String id) throws IOException {
        FileUtils.write(new File(id), str, StandardCharsets.UTF_8);
    }

    @Override
    public void serializeObjectToStore(TYPE o, String id) throws IOException {
        om.writeValue(new File(id),o);
    }

    @Override
    public void copyStoreToOutputStream(String id, OutputStream outputStream) throws IOException {
        FileUtils.copyFile(new File(id),outputStream);
    }

    @Override
    public TYPE deserializeObjectFromStore(String id) throws IOException {
        return om.readValue(new FileInputStream(id),clazz);
    }

    @Override
    public boolean delete(String storageId) {
        return new File(storageId).delete();
    }

    public File createTempFile(String extension) throws IOException {
        return File.createTempFile("ndResource", "." + extension, location);
    }
}
