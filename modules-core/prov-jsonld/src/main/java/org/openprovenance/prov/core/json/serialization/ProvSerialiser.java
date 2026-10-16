package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.model.interop.InteropMediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

/** Serialises a {@link Document} as <a href="https://www.w3.org/submissions/prov-json/">PROV-JSON</a>. */
public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {

    final static private Collection<String> myMedia = Set.of(InteropMediaType.MEDIA_APPLICATION_JSON);

    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper mapperWithFormat = new ObjectMapper();
    private final ProvJsonWriter writer;

    public ProvSerialiser() {
        this(org.openprovenance.prov.vanilla.ProvFactory.getFactory());
    }

    public ProvSerialiser(ProvFactory pf) {
        this.writer = new ProvJsonWriter(pf);
        mapperWithFormat.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
    }

    /** The PROV-JSON tree of a document, for callers that want to inspect or embed it rather than write it. */
    public JsonNode toJson(Document document) {
        return writer.write(document);
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        try {
            (formatted ? mapperWithFormat : mapper).writeValue(out, writer.write(document));
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }

    public void serialiseObject(OutputStream out, Object document, boolean formatted) {
        try {
            (formatted ? mapperWithFormat : mapper).writeValue(out, document);
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }
}
