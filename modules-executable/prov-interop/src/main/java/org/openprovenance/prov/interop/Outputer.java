package org.openprovenance.prov.interop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.ProvSerialiser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.openprovenance.prov.interop.Formats.ProvFormat.*;

public class Outputer implements InteropMediaType {
    private final InteropFramework interopFramework;
    private final ProvFactory pFactory;
    Integer maxStringLength = 100;

    final private Map<Formats.ProvFormat, SerializerFunction> serializerMap;


    public Outputer(InteropFramework interopFramework, ProvFactory pFactory) {
        this.interopFramework = interopFramework;
        this.pFactory=pFactory;
        this.serializerMap=createSerializerMap();
    }

    /**
     * Initializes a Document's namespace.
     *
     * @param doc a {@link Document} to initialize
     */

    void setNamespaces(Document doc) {
        if (doc.getNamespace() == null)
            doc.setNamespace(new Namespace());

    }


    /**
     * Write a {@link Document} to file, serialized according to the file extension
     *
     * @param filename path of the file to write the Document to
     * @param document a {@link Document} to serialize
     */

    public void writeDocument(String filename, Document document) {
        Formats.ProvFormat format = interopFramework.getTypeForFile(filename);
        if (format == null) {
            throw new InteropException("Format could not be determined from filename extension: " + filename);
        }
        writeDocument(filename, document, format);
    }

    public void writeDocumentToFileOrDefaultOutput(String filename, Document doc, String format) throws IOException {
        Formats.ProvFormat outformat;
        if (format != null) {
            outformat = interopFramework.getTypeForFormat(format);
            if (outformat == null) {
                throw new InteropException("Unknown format: " + format);
            }
        } else {
            outformat = interopFramework.getTypeForFile(filename);
            if (outformat == null) {
                throw new InteropException("Unknown file format for: " + filename);
            }
        }

        if (Objects.equals(filename, "-")) {
            writeDocument(System.out, doc, outformat);
        } else {
            writeDocument(Files.newOutputStream(new File(filename).toPath()), doc, outformat);
        }
    }

    final public Map<Formats.ProvFormat, SerializerFunction> createSerializerMap() {

        //NOTE: Syntax restricted to 10 entries
        Map<Formats.ProvFormat, SerializerFunction> serializer = new HashMap<>();
        serializer.putAll(
                Map.of( PROVN,  () -> new ProvSerialiser(pFactory),
                        PROVX,  () -> new org.openprovenance.prov.core.xml.serialization.ProvSerialiser(true),
                        JSONLD, () -> new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser(new ObjectMapper(), false),
                        JSON,   org.openprovenance.prov.core.json.serialization.ProvSerialiser::new,
                        TURTLE, () -> { throw new UnsupportedOperationException("light turtle converter not integrated yet"); },
                        TRIG,   () -> { throw new UnsupportedOperationException("light turtle converter not integrated yet"); }
                ));

        serializer.putAll(
                Map.of(JPEG, () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory, interopFramework.getExtensionMap().get(JPEG), maxStringLength),
                        SVG, () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory, interopFramework.getExtensionMap().get(SVG),  maxStringLength),
                        PDF, () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory, interopFramework.getExtensionMap().get(PDF),  maxStringLength),
                        PNG, () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory, interopFramework.getExtensionMap().get(PNG),  maxStringLength),
                        DOT, () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory, interopFramework.getExtensionMap().get(DOT),  maxStringLength)
                ));

        return serializer;
    }

    void setMaxStringLength(Integer maxStringLength) {
        this.maxStringLength = maxStringLength;
        serializerMap.put(
                SVG, () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory, interopFramework.getExtensionMap().get(SVG), maxStringLength));
    }

    /**
     * Write a {@link Document} to output stream, according to specified {@link Formats.ProvFormat}
     *
     * @param os       an {@link OutputStream} to write the Document to
     * @param document a {@link Document} to serialize
     * @param format   a {@link Formats.ProvFormat}
     */

    public void writeDocument(OutputStream os, Document document, Formats.ProvFormat format) {
        writeDocument(os, document, interopFramework.getMimeTypeMap().get(format), true);
    }
    /**
     * Write a {@link Document} to file, serialized according to the file extension
     *
     * @param filename path of the file to write the Document to
     * @param document a {@link Document} to serialize
     * @param format   a {@link Formats.ProvFormat} to serialize the document to
     */


    public void writeDocument(String filename, Document document, Formats.ProvFormat format) {
        try {
            writeDocument(Files.newOutputStream(Paths.get(filename)), document, format);
        } catch (IOException e) {
            throw new InteropException(e);
        }
    }

    /**
     * Serializes a document to a stream
     *
     * @param out       an {@link OutputStream}
     * @param document  a {@link Document}
     * @param mediaType a {@link String} representing the media type
     * @param formatted a boolean indicating whether the output should be pretty-printed
     */



    public void writeDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
        Formats.ProvFormat format = interopFramework.getMimeTypeRevMap().get(mediaType);
        if (format == null) {
            throw new InteropException("InteropFramework(): serialisedDocument unknown mediatype " + mediaType);
        }
        SerializerFunction serializerMaker = serializerMap.get(format);
        InteropFramework.logger.debug("serializer " + format + " " + serializerMaker);
        serializerMaker.apply().serialiseDocument(out, document, formatted);
    }
}