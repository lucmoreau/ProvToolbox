package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.openprovenance.prov.model.OpenprovTerms;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle.Kind;

import java.io.IOException;

/** The attribute keys of one kind of relation: the bare term the openprov context scopes to it, else what any key is. */
public abstract class ScopedKeySerializer extends CustomKeySerializer {

    private final Kind kind;

    protected ScopedKeySerializer(Kind kind) {
        this.kind = kind;
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String term = OpenprovTerms.isOpenprov(q) ? OpenprovTerms.term(kind, q.getLocalPart()) : null;
        if (term == null) {
            super.serialize(q, jsonGenerator, serializerProvider);
        } else {
            jsonGenerator.writeFieldName(term);
        }
    }

    public static class Attribution extends ScopedKeySerializer {
        public Attribution() { super(Kind.PROV_ATTRIBUTION); }
    }

    public static class Membership extends ScopedKeySerializer {
        public Membership() { super(Kind.PROV_MEMBERSHIP); }
    }

    public static class Specialization extends ScopedKeySerializer {
        public Specialization() { super(Kind.PROV_SPECIALIZATION); }
    }
}
