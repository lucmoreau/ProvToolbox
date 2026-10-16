package org.openprovenance.prov.model.interop;

/**
 * Thread-scoped carrier for a statement POST's idempotency key, mirroring
 * {@link PrincipalManager}: the service reads the {@code Idempotency-Key}
 * HTTP header and stashes it here; the generated query invoker (see
 * {@code CompilerQueryInvokerWithPrincipal}) reads it back — as a ready-made
 * SQL literal — while composing the {@code record_index} insert that rides
 * the same statement as the template insert.
 *
 * <p>The key is comms infrastructure, not provenance: it never reaches a
 * template table, only {@code record_index.submission_key}, whose partial
 * unique index turns a duplicate delivery into a unique violation that
 * aborts the whole composed statement (template insert included). Absent
 * key (the default) renders as SQL {@code NULL}, which the partial index
 * ignores — clients that do not send the header keep their exact previous
 * behaviour.</p>
 */
public class SubmissionKeyManager {

    private static final ThreadLocal<String> submissionKey = ThreadLocal.withInitial(() -> null);

    public void setSubmissionKey(String key) {
        submissionKey.set(key);
    }

    public String getSubmissionKey() {
        return submissionKey.get();
    }

    public void clearSubmissionKey() {
        submissionKey.remove();
    }

    /**
     * The current key as a SQL literal for the composed {@code record_index}
     * insert: {@code NULL} when no key is set (so the partial unique index
     * never sees keyless submissions), otherwise a single-quote-escaped
     * {@code TEXT} literal. Static so generated code can call it without
     * holding a manager instance.
     */
    public static String asSqlLiteral() {
        String key = submissionKey.get();
        if (key == null) return "NULL";
        return "'" + key.replace("'", "''") + "'::TEXT";
    }
}
