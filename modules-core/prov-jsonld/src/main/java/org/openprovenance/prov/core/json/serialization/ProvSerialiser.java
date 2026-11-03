package org.openprovenance.prov.core.json.serialization;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.model.interop.InteropMediaType;
import tools.jackson.core.JacksonException;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

/** Serialises a {@link Document} as <a href="https://www.w3.org/submissions/prov-json/">PROV-JSON</a>. */
public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {

    final static private Collection<String> myMedia = Set.of(InteropMediaType.MEDIA_APPLICATION_JSON);

    // Jackson 3: immutable mappers, configured when built; Jackson 2 defaults (JACKSON3_MIGRATION.md, D2)
    private final ObjectMapper mapper = JsonMapper.builderWithJackson2Defaults().disable(SerializationFeature.INDENT_OUTPUT).build();
    private final ObjectMapper mapperWithFormat = JsonMapper.builderWithJackson2Defaults().enable(SerializationFeature.INDENT_OUTPUT).build();
    private final ProvJsonWriter writer;

    public ProvSerialiser() {
        this(org.openprovenance.prov.vanilla.ProvFactory.getFactory());
    }

    public ProvSerialiser(ProvFactory pf) {
        this.writer = new ProvJsonWriter(pf);
    }

    /** The PROV-JSON tree of a document, for callers that want to inspect or embed it rather than write it. */
    public JsonNode toJson(Document document) {
        return writer.write(document);
    }

    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        try {
            (formatted ? mapperWithFormat : mapper).writeValue(out, writer.write(document));
        } catch (JacksonException e) {
            throw new UncheckedException(e);
        }
    }

    public void serialiseObject(OutputStream out, Object document, boolean formatted) {
        try {
            (formatted ? mapperWithFormat : mapper).writeValue(out, document);
        } catch (JacksonException e) {
            throw new UncheckedException(e);
        }
    }
}
