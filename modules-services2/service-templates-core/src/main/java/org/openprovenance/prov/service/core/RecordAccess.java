package org.openprovenance.prov.service.core;

import java.util.Collection;
import java.util.Set;

/**
 * The record-level access control of the template queries: a record is visible to the principal that wrote it
 * ({@code record_index.principal}) or through an {@code access_control} grant — except to a reader, a principal the
 * security configuration names ({@code readers}), who sees every record. No readers by default.
 */
public final class RecordAccess {

    public static final RecordAccess NONE = new RecordAccess(Set.of());

    private final Set<String> readers;

    private RecordAccess(Set<String> readers) {
        this.readers = readers;
    }

    public static RecordAccess readers(Collection<String> readers) {
        return readers == null || readers.isEmpty() ? NONE : new RecordAccess(Set.copyOf(readers));
    }

    public boolean isReader(String principal) {
        return principal != null && readers.contains(principal);
    }

    /** The filter as an AND clause after a WHERE; nothing for a reader (the access-control joins are harmless). */
    public void and(String principal, StringBuilder sb) {
        if (isReader(principal)) {
            return;
        }
        sb.append("\n AND (record_index.principal='");
        sb.append(principal);
        sb.append("' OR access_control.record IS NOT NULL)");
    }

    /** The filter as the WHERE clause; {@code WHERE TRUE} for a reader. */
    public void where(String principal, StringBuilder sb) {
        if (isReader(principal)) {
            sb.append("\n WHERE TRUE");
            return;
        }
        sb.append("\n WHERE (record_index.principal='");
        sb.append(principal);
        sb.append("' OR access_control.record IS NOT NULL)");
    }
}
