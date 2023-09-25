package org.openprovenance.prov.storage.filesystem;

import org.apache.commons.io.FileUtils;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.storage.api.ResourceStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

public class DocumentResourceStorageFileSystem implements ResourceStorage {

    private final InteropFramework interop;
    private final File location;

    public DocumentResourceStorageFileSystem(ProvFactory provFactory, File location) {
        this.interop=new InteropFramework(provFactory);
        this.location=location;
    }

    @Override
    public String newStore(Formats.ProvFormat format) throws IOException {
        String extension = interop.getExtension(format);
        if (extension==null) extension="UNKNOWN";
        File temp = createTempFile(extension);
        return temp.getAbsolutePath();
    }

    @Override
    public void copyInputStreamToStore(InputStream inputStream, Formats.ProvFormat format, String id) throws IOException {
        FileUtils.copyInputStreamToFile(inputStream, new File(id));
    }

    @Override
    public void copyStringToStore(CharSequence str, Formats.ProvFormat format, String id) throws IOException {
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
    public Document readDocument(String id, boolean known, DateTimeOption dateTimeOption, TimeZone timeZone) throws IOException {
        Document doc;
        if (known) {
            doc = (Document) interop.readDocumentFromFile(id, dateTimeOption, timeZone);
        } else {
            doc = (Document) interop.loadProvUnknownGraph(id, dateTimeOption, timeZone);
        }
        return doc;
    }

    @Override
    public void writeDocument(String id, Formats.ProvFormat format, Document doc) throws IOException {
        interop.writeDocument(id,format,doc);
    }

    @Override
    public boolean delete(String storageId) {
        return new File(storageId).delete();
    }

    public File createTempFile(String extension) throws IOException {
        return File.createTempFile("doc", "." + extension, location);
    }
}
