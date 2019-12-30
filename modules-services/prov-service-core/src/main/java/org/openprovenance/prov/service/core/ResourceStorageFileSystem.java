package org.openprovenance.prov.service.core;

import org.apache.commons.io.FileUtils;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceStorageFileSystem implements ResourceStorage {

    private final InteropFramework interop;

    public ResourceStorageFileSystem() {
        this.interop=new InteropFramework();
    }

    @Override
    public String newStore(Formats.ProvFormat format) throws IOException {
        String extension = interop.getExtension(format);
        if (extension==null) extension="UNKNOWN";
        File temp = createTempFile(extension);
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
    public Document readDocument(String id) throws IOException {
        return readDocument(id,true);

    }

    @Override
    public Document readDocument(String id, boolean known) throws IOException {
        Document doc;
        if (known) {
            doc = (Document) interop.readDocumentFromFile(id);
        } else {
            doc = (Document) interop.loadProvUnknownGraph(id);
        }
        return doc;
    }

    @Override
    public void writeDocument(String id, Formats.ProvFormat format, Document doc) throws IOException {
        interop.writeDocument(id,format,doc);
    }

    public File createTempFile(String extension) throws IOException {
        return File.createTempFile("graph", "." + extension, new File(ServiceUtils.UPLOADED_FILE_PATH));
    }
}
