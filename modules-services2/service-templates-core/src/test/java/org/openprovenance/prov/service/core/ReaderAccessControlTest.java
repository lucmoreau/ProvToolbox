package org.openprovenance.prov.service.core;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Readers: principals the security configuration names see every record — the record-level access control (a record
 * visible to the principal that wrote it, or through an access_control grant) is not applied to them. Anyone else keeps
 * exactly the filter as before.
 */
public class ReaderAccessControlTest {

    private static final String FILTER = " (record_index.principal='joe' OR access_control.record IS NOT NULL)";

    @Test
    public void aPrincipalOutsideTheReadersKeepsTheFilter() {
        RecordAccess a = RecordAccess.readers(List.of("service-account-reader"));
        StringBuilder and = new StringBuilder();
        a.and("joe", and);
        assertEquals("\n AND" + FILTER, and.toString());
        StringBuilder where = new StringBuilder();
        a.where("joe", where);
        assertEquals("\n WHERE" + FILTER, where.toString());
    }

    @Test
    public void aReaderSeesEveryRecord() {
        RecordAccess a = RecordAccess.readers(List.of("service-account-reader"));
        assertTrue(a.isReader("service-account-reader"));
        StringBuilder and = new StringBuilder();
        a.and("service-account-reader", and);
        assertEquals("", and.toString());
        StringBuilder where = new StringBuilder();
        a.where("service-account-reader", where);
        assertEquals("\n WHERE TRUE", where.toString());
    }

    @Test
    public void noReadersByDefault() {
        assertSame(RecordAccess.NONE, RecordAccess.readers(null));
        assertSame(RecordAccess.NONE, RecordAccess.readers(List.of()));
        assertFalse(RecordAccess.NONE.isReader("service-account-reader"));
        assertFalse(RecordAccess.NONE.isReader(null));
        StringBuilder and = new StringBuilder();
        RecordAccess.NONE.and("joe", and);
        assertEquals("the filter as it always was", "\n AND" + FILTER, and.toString());
    }
}
