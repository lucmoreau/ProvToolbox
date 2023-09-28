package org.openprovenance.prov.interop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.ProvSerialiser;
import org.openprovenance.prov.notation.Utility;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.openprovenance.prov.interop.Formats.ProvFormat.*;

public class Outputer implements InteropMediaType {
    private final InteropFramework interopFramework;
    private final ProvFactory pFactory;
    Integer maxStringLength = 100;

    public Outputer(InteropFramework interopFramework, ProvFactory pFactory) {
        this.interopFramework = interopFramework;
        this.pFactory=pFactory;
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
     * Write a {@link Document} to output stream, according to specified {@link Formats.ProvFormat}
     *
     * @param os       an {@link OutputStream} to write the Document to
     * @param format   a {@link Formats.ProvFormat}
     * @param document a {@link Document} to serialize
     */

    public void writeDocument(OutputStream os, Formats.ProvFormat format, Document document) {
        serialiseDocument(os, document, interopFramework.getMimeTypeMap().get(format), true);
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
            System.err.println("Unknown output file format: " + filename);
            return;
        }
        writeDocument(filename, format, document);
    }

    public void doWriteDocument(String filename, String format, Document doc) throws IOException {
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
            if (outformat == null) {
                throw new InteropException("File format for standard output not specified");
            }
            writeDocument(System.out, outformat, doc);
        } else {
            writeDocument(Files.newOutputStream(new File(filename).toPath()), outformat, doc);
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

    public void setMaxStringLength(Integer maxStringLength) {
        this.maxStringLength = maxStringLength;
        interopFramework.getSerializerMap().put(
                SVG, () -> new org.openprovenance.prov.dot.ProvSerialiser(pFactory, interopFramework.getExtensionMap().get(SVG), maxStringLength));
    }

    /**
     * Write a {@link Document} to file, serialized according to the file extension
     *
     * @param filename path of the file to write the Document to
     * @param format   a {@link Formats.ProvFormat} to serialize the document to
     * @param document a {@link Document} to serialize
     */


    public void writeDocument(String filename, Formats.ProvFormat format, Document document) {
        Namespace.withThreadNamespace(document.getNamespace());
        try {
            InteropFramework.logger.debug("writing " + format);
            InteropFramework.logger.debug("writing " + filename);
            setNamespaces(document);
            switch (format) {
                case PROVN: {
                    new Utility(interopFramework.getConfig().dateTime, interopFramework.getConfig().timeZone).writeDocument(document, filename, pFactory);
                    break;
                }
                case PROVX: {
                    org.openprovenance.prov.core.xml.serialization.ProvSerialiser serial = new org.openprovenance.prov.core.xml.serialization.ProvSerialiser(true);
                    InteropFramework.logger.debug("namespaces " + document.getNamespace());
                    serial.serialiseDocument(Files.newOutputStream(new File(filename).toPath()), document, true);
                    break;
                }


                case DOT: {
                    ProvToDot toDot = new ProvToDot(pFactory);
                    toDot.setMaxStringLength(maxStringLength);
                    toDot.setLayout(interopFramework.getConfig().layout);
                    toDot.convert(document, filename, interopFramework.getConfig().title);
                    break;
                }
                case PDF:
                case JPEG:
                case PNG:
                case SVG: {
                    File tmp = File.createTempFile("viz-", ".dot");

                    String dotFileOut = tmp.getAbsolutePath(); // give it as option,
                    // if not available
                    // create tmp file
                    ProvToDot toDot = new ProvToDot(pFactory);
                    toDot.setMaxStringLength(maxStringLength);
                    toDot.setLayout(interopFramework.getConfig().layout);
                    toDot.convert(document, dotFileOut, filename, interopFramework.getExtensionMap().get(format), interopFramework.getConfig().title);
                    tmp.delete();
                    break;
                }

                default:
                    break;
            }
        } catch (RuntimeException | IOException e) {
            if (interopFramework.getConfig().verbose != null)
                e.printStackTrace();
            throw new InteropException(e);

        }

    }

    /**
     * Serializes a document to a stream
     *
     * @param out       an {@link OutputStream}
     * @param document  a {@link Document}
     * @param formatted a boolean indicating whether the output should be pretty-printed
     */

    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        throw new UnsupportedOperationException("InteropFramework()  serialize Document requires a media type");
    }


    public void serialiseDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
        Formats.ProvFormat format = interopFramework.getMimeTypeRevMap().get(mediaType);
        if (format == null) {
            throw new UnsupportedOperationException("InteropFramework(): serialisedDocument unknown mediatype " + mediaType);
        }
        SerializerFunction serializerMaker = interopFramework.getSerializerMap().get(format);
        InteropFramework.logger.debug("serializer " + format + " " + serializerMaker);
        serializerMaker.apply().serialiseDocument(out, document, mediaType, formatted);
    }
}