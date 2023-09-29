package org.openprovenance.prov.interop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.ProvDeserialiser;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.openprovenance.prov.interop.Formats.ProvFormat.*;

public class Inputer implements InteropMediaType {
    static Logger logger = LogManager.getLogger(Inputer.class);

    private final InteropFramework interopFramework;
    static String SEPARATOR = ",";
    private final ProvFactory pFactory;
    final Map<Formats.ProvFormat, DeserializerFunction> deserializerMap;
    final Map<Formats.ProvFormat, DeserializerFunction2> deserializerMap2;
    List<Formats.ProvFormat> preferredOrder = List.of(PROVN, JSONLD, JSON, PROVX);

    public Inputer(InteropFramework interopFramework, ProvFactory pFactory) {
        this.interopFramework = interopFramework;
        this.pFactory = pFactory;
        this.deserializerMap = createDeserializerMap();
        deserializerMap2 = createDeserializerMap2();
    }

    private Map<Formats.ProvFormat, DeserializerFunction> createDeserializerMap() {

        //NOTE: Syntax restricted to 10 entries
        Map<Formats.ProvFormat, DeserializerFunction> deserializer = new HashMap<Formats.ProvFormat, DeserializerFunction>();
        deserializer.putAll(
                Map.of(PROVN, () -> new ProvDeserialiser(pFactory, interopFramework.getConfig().dateTime, interopFramework.getConfig().timeZone),
                        PROVX, () -> new org.openprovenance.prov.core.xml.serialization.ProvDeserialiser(interopFramework.getConfig().dateTime, interopFramework.getConfig().timeZone),
                        JSONLD, () -> new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser(new ObjectMapper(), interopFramework.getConfig().dateTime, interopFramework.getConfig().timeZone),
                        JSON, () -> new org.openprovenance.prov.core.json.serialization.ProvDeserialiser(new ObjectMapper(), interopFramework.getConfig().dateTime, interopFramework.getConfig().timeZone))
        );

        return deserializer;
    }

    final public Map<Formats.ProvFormat, DeserializerFunction2> createDeserializerMap2() {

        //NOTE: Syntax restricted to 10 entries
        Map<Formats.ProvFormat, DeserializerFunction2> deserializer = new HashMap<Formats.ProvFormat, DeserializerFunction2>();
        deserializer.putAll(
                Map.of(PROVN, (DateTimeOption dateTime, TimeZone timeZone) -> new ProvDeserialiser(pFactory, dateTime, timeZone),
                        PROVX, (DateTimeOption dateTime, TimeZone timeZone) -> new org.openprovenance.prov.core.xml.serialization.ProvDeserialiser(dateTime, timeZone),
                        JSONLD, (DateTimeOption dateTime, TimeZone timeZone) -> new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser(new ObjectMapper(), dateTime, timeZone),
                        JSON, (DateTimeOption dateTime, TimeZone timeZone) -> new org.openprovenance.prov.core.json.serialization.ProvDeserialiser(new ObjectMapper(), dateTime, timeZone))
        );

        return deserializer;
    }


    Document deserialiseDocument(InputStream is, Formats.ProvFormat format) throws IOException {
        DeserializerFunction deserializer = interopFramework.getDeserializerMap().get(format);
        logger.debug("deserializer " + format + " " + deserializer);
        return deserializer.apply().deserialiseDocument(is);
    }

    Document deserialiseDocument(InputStream is, Formats.ProvFormat format, DateTimeOption dateTimeOption, TimeZone timeZone) throws IOException {
        DeserializerFunction2 deserializer = interopFramework.getDeserializerMap2().get(format);
        logger.debug("deserializer " + format + " " + deserializer);
        return deserializer.apply(dateTimeOption, timeZone).deserialiseDocument(is);
    }


    Document readDocument(String filename, String format) throws IOException {
        Document doc;
        Formats.ProvFormat informat;
        if (format != null) {
            informat = interopFramework.getTypeForFormat(format);
            if (informat == null) {
                throw new InteropException("Unknown format: " + format);
            }
        } else {
            informat = interopFramework.getTypeForFile(filename);
            if (informat == null) {
                throw new InteropException("Unknown file format for: " + filename);
            }
        }

        if (Objects.equals(filename, "-")) {
            if (informat == null) {
                throw new InteropException("File format for standard input not specified");
            }
            doc = deserialiseDocument(System.in, informat);
        } else {
            doc = deserialiseDocument(Files.newInputStream(Paths.get(filename)), informat);
        }

        return doc;
    }

    Document readDocument(ToRead something) throws IOException {
        Document doc = null;
        switch (something.kind) {
            case FILE:
                //doc=readDocumentFromFile(something.url, something.format);
                doc = deserialiseDocument(Files.newInputStream(Paths.get(something.url)), something.format);
                break;
            case URL:
                doc = readDocumentFromURL(something.url);// note: ignore format?
                break;
        }
        return doc;
    }

    Document readDocumentFromFile(String filename, DateTimeOption dateTimeOption, TimeZone timeZone) {
        Formats.ProvFormat format = interopFramework.getTypeForFile(filename);
        if (format == null) {
            throw new InteropException("Unknown output file format: " + filename);
        }
        try {
            return deserialiseDocument(Files.newInputStream(Paths.get(filename)), format, dateTimeOption, timeZone);
        } catch (IOException e) {
            throw new InteropException(e);
        }
    }

    Document readDocumentFromFile(String filename) {
        Formats.ProvFormat format = interopFramework.getTypeForFile(filename);
        if (format == null) {
            throw new InteropException("Unknown output file format: " + filename);
        }
        try {
            return deserialiseDocument(Files.newInputStream(Paths.get(filename)), format);
        } catch (IOException e) {
            throw new InteropException(e);
        }
    }

    Document readDocumentFromFileWithUnknownType(String filename) {
        for (Formats.ProvFormat format : preferredOrder) {
            try {
                return interopFramework.getDeserializerMap().get(format).apply().deserialiseDocument(Files.newInputStream(Paths.get(filename)));
            } catch (IOException ignored) {
                // we fail, let's continue with the next one
            }
        }

        System.out.println("Unparseable format " + filename);
        throw new UnsupportedOperationException("Unparseable format " + filename);
    }

    Document readDocumentFromFileWithUnknownType(String filename, DateTimeOption dateTimeOption, TimeZone timeZone) {
        for (Formats.ProvFormat format : preferredOrder) {
            try {
                return interopFramework.getDeserializerMap2().get(format).apply(dateTimeOption, timeZone).deserialiseDocument(Files.newInputStream(Paths.get(filename)));
            } catch (IOException ignored) {
                // we fail, let's continue with the next one
            }
        }
        System.out.println("Unparseable format " + filename);
        throw new UnsupportedOperationException("Unparseable format " + filename);
    }

    /**
     * Reads a document from a URL. Uses the Content-type header field to determine the
     * mime-type of the resource, and therefore the parser to read the document.
     *
     * @param url a URL
     * @return a Document
     */
    Document readDocumentFromURL(String url) {
        try {
            URL theURL = new URL(url);
            URLConnection conn = interopFramework.connectWithRedirect(theURL);
            if (conn == null)
                return null;

            Formats.ProvFormat format = null;
            String content_type = conn.getContentType();

            logger.debug("Content-type: " + content_type);
            if (content_type != null) {
                // Need to trim optional parameters
                // Content-Type: text/plain; charset=UTF-8
                int end = content_type.indexOf(";");
                if (end < 0) {
                    end = content_type.length();
                }
                String actual_content_type = content_type.substring(0, end).trim();
                logger.debug("Found Content-type: " + actual_content_type);
                // TODO: might be worth skipping if text/plain as that seems
                // to be the
                // default returned by unconfigured web servers
                format = interopFramework.getMimeTypeRevMap().get(actual_content_type);
            }
            logger.debug("Format after Content-type: " + format);

            if (format == null) {
                format = interopFramework.getTypeForFile(theURL.toString());
            }
            logger.debug("Format after extension: " + format);

            InputStream content_stream = conn.getInputStream();

            return deserialiseDocument(content_stream, format);
        } catch (IOException e) {
            throw new InteropException(e);
        }
    }

    List<ToRead> readIndexFile(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);
        return readIndexFile(fis);
    }

    List<ToRead> readIndexFile(InputStream is) throws IOException {
        List<ToRead> res = new LinkedList<ToRead>();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(SEPARATOR);
            if (parts.length >= 3) {
                FileKind kind = parts[0].trim().equals("URL") ? FileKind.URL : FileKind.FILE;
                String path = parts[1].trim();
                Formats.ProvFormat format = interopFramework.getTypeForFormat(parts[2].trim());
                ToRead elem = new ToRead(kind, path, format);
                res.add(elem);
            } else if (parts.length == 1) {
                String filename = parts[0].trim();
                ToRead elem = new ToRead(FileKind.FILE, filename, interopFramework.getTypeForFile(filename));
                res.add(elem);
            }
        }
        br.close();
        return res;
    }


    enum FileKind {FILE, URL}

    static class ToRead {
        FileKind kind;
        String url;
        Formats.ProvFormat format;

        public String toString() {
            return "[" + kind + "," + url + "," + format + "]";
        }

        ToRead(FileKind kind, String url, Formats.ProvFormat format) {
            this.kind = kind;
            this.url = url;
            this.format = format;
        }
    }
}