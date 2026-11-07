package org.openprovenance.prov.core.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import org.openprovenance.prov.model.OpenprovTerms;
import org.openprovenance.prov.vanilla.QualifiedName;
import tools.jackson.databind.SerializationContext;
import org.openprovenance.prov.model.StatementOrBundle.Kind;

/** The attribute keys of one kind of relation: the bare term the openprov context scopes to it, else what any key is. */
public abstract class ScopedKeySerializer extends CustomKeySerializer {

    private final Kind kind;

    protected ScopedKeySerializer(Kind kind) {
        this.kind = kind;
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        String term = OpenprovTerms.isOpenprov(q) ? OpenprovTerms.shownAs(kind, q.getLocalPart()) : null;
        if (term == null) {
            super.serialize(q, jsonGenerator, serializerProvider);
        } else {
            serializerProvider.setAttribute(ID_COERCED_KEY, true);  // every scoped term is "@type": "@id"
            jsonGenerator.writeName(term);
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

    public static class Communication extends ScopedKeySerializer {
        public Communication() { super(Kind.PROV_COMMUNICATION); }
    }

    public static class Start extends ScopedKeySerializer {
        public Start() { super(Kind.PROV_START); }
    }

    public static class End extends ScopedKeySerializer {
        public End() { super(Kind.PROV_END); }
    }
}
