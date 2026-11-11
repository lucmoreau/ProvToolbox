package org.openprovenance.prov.template.descriptors;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * T31: the data-protection annotation a variable carries once the application's policy has been applied to what the
 * library declared. Resolved once, when the code is generated, and fixed from then on (T31 Ruling 1). Generators read
 * it through {@link DescriptorUtils}; it is never serialised.
 */
public class EffectivePrivacy {

    public enum Kind { TRANSIENT, PSEUDONYM }

    /** The places a value can leave the process to; a transient method keeps it in some of them. */
    public enum Place { STORE, LOG, EXPANSION }

    public final Kind kind;
    /** Qualified method name, e.g. {@code pt:ext-ref}. */
    public final String method;
    /** For a transient method: the places it keeps the value in. Empty for {@code pt:drop}. */
    public final Set<Place> keeps;
    /** For a pseudonym method: the pattern, in the subset Java, Python, JavaScript, Rust and PostgreSQL read alike. */
    public final String pattern;
    /** For a pseudonym method: a boolean SQL expression over {@code v} replacing the pattern test in the database, or null. */
    public final String sql;
    /** For a pseudonym method: the application's own PAST method {package, class, method} for the client check, or null. */
    public final Map<String, String> past;
    public final String resolver;
    /** SHA-256 (hex) of the method's resolved definition: what the method meant when the code was generated. */
    public final String digest;

    public EffectivePrivacy(Kind kind, String method, Set<Place> keeps, String pattern, String sql,
                            Map<String, String> past, String resolver, String digest) {
        this.kind = kind;
        this.method = method;
        this.keeps = keeps == null ? Collections.emptySet() : Collections.unmodifiableSet(keeps);
        this.pattern = pattern;
        this.sql = sql;
        this.past = past;
        this.resolver = resolver;
        this.digest = digest;
    }

    public boolean isTransient() {
        return kind == Kind.TRANSIENT;
    }

    public boolean isPseudonym() {
        return kind == Kind.PSEUDONYM;
    }

    /** Whether a value with this annotation may go to the given place. A pseudonymised value goes everywhere. */
    public boolean keeps(Place place) {
        return kind == Kind.PSEUDONYM || keeps.contains(place);
    }

    @Override
    public String toString() {
        return (kind == Kind.TRANSIENT ? "@transient(" : "@pseudonym(") + method + ")";
    }
}
