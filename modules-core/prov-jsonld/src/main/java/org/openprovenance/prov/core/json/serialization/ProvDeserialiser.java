package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;

/** Deserialises <a href="https://www.w3.org/submissions/prov-json/">PROV-JSON</a> into a {@link org.openprovenance.prov.model.Document}. */
public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {

    protected final ProvFactory pf = new ProvFactory();
    protected final ObjectMapper mapper;
    private final ProvJsonReader reader;

    public ProvDeserialiser() {
        this(new ObjectMapper());
    }

    public ProvDeserialiser(ObjectMapper mapper) {
        this(mapper, DateTimeOption.PRESERVE, null);
    }

    public ProvDeserialiser(ObjectMapper mapper, DateTimeOption dateTimeOption) {
        this(mapper, dateTimeOption, null);
    }

    public ProvDeserialiser(ObjectMapper mapper, DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.mapper = mapper;
        this.reader = new ProvJsonReader(pf, dateTimeOption, optionalTimeZone);
    }

    public org.openprovenance.prov.model.Document deserialiseDocument(File serialised) throws IOException {
        try (InputStream in = new FileInputStream(serialised)) {
            return deserialiseDocument(in);
        }
    }

    public org.openprovenance.prov.model.Document deserialiseDocument(InputStream in) {
        try {
            return deserialiseDocument(mapper.readTree(in));
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }

    /** A document from its PROV-JSON tree, for callers that already hold the tree. */
    public org.openprovenance.prov.model.Document deserialiseDocument(JsonNode tree) {
        return reader.read(tree);
    }
}
