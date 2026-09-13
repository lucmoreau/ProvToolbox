package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.openprovenance.prov.core.jsonld11.serialization.OpenprovTerms;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle.Kind;

import java.io.IOException;

/** The attribute keys of one kind of relation: a term the openprov context scopes to it, else what any key is. */
public abstract class ScopedKeyDeserializer extends CustomKeyDeserializer {

    private final Kind kind;

    protected ScopedKeyDeserializer(Kind kind) {
        this.kind = kind;
    }

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
        QualifiedName scoped = OpenprovTerms.property(kind, s);
        if (scoped == null) return super.deserializeKey(s, deserializationContext);
        deserializationContext.setAttribute(PROV_ATTRIBUTE_CONTEXT_KEY, scoped);
        return scoped;
    }

    public static class Attribution extends ScopedKeyDeserializer {
        public Attribution() { super(Kind.PROV_ATTRIBUTION); }
    }

    public static class Membership extends ScopedKeyDeserializer {
        public Membership() { super(Kind.PROV_MEMBERSHIP); }
    }

    public static class Specialization extends ScopedKeyDeserializer {
        public Specialization() { super(Kind.PROV_SPECIALIZATION); }
    }
}
