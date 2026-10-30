package org.openprovenance.prov.service.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** The write path's switches: the service's own behaviour by default, the configuration before the environment, an unknown value refused. */
public class SubmissionSwitchesTest {

    private static final Map<String, String> NO_ENV = Map.of();

    private static SubmissionSwitches of(Map<String, Object> config, Map<String, String> env) {
        return SubmissionSwitches.from(config, env::get);
    }

    @Test
    public void nothingSetIsTheServiceAsItWas() {
        SubmissionSwitches s = of(Map.of(), NO_ENV);
        assertTrue(s.readbackAccessControl);
        assertEquals(SubmissionSwitches.Hash.UPDATE, s.hash);
        assertEquals(SubmissionSwitches.Relations.INLINE, s.relations);
        assertEquals(SubmissionSwitches.DEFAULTS.toString(), s.toString());
        assertEquals("submission.readback.access_control=on submission.hash=update relations.write=inline "
                + "relations.async.writers=2 relations.async.queue=10000 relations.async.batch=200", s.toString());
    }

    @Test
    public void everySwitchFromTheConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("submission.readback.access_control", "off");
        config.put("submission.hash", "header");
        config.put("relations.write", "async");
        config.put("relations.async.writers", 4);
        config.put("relations.async.queue", "500");
        config.put("relations.async.batch", 50);
        config.put("jdbc.url", "jdbc:postgresql://localhost:55432/provbench");   // other keys untouched
        SubmissionSwitches s = of(config, NO_ENV);
        assertFalse(s.readbackAccessControl);
        assertEquals(SubmissionSwitches.Hash.HEADER, s.hash);
        assertEquals(SubmissionSwitches.Relations.ASYNC, s.relations);
        assertEquals(4, s.asyncWriters);
        assertEquals(500, s.asyncQueue);
        assertEquals(50, s.asyncBatch);
    }

    @Test
    public void theEnvironmentWhenTheConfigurationIsSilent() {
        SubmissionSwitches s = of(Map.of(), Map.of(
                "TPL_READBACK_ACCESS_CONTROL", "OFF",
                "TPL_SUBMISSION_HASH", "off",
                "TPL_RELATIONS_WRITE", "async",
                "TPL_RELATIONS_ASYNC_WRITERS", "3"));
        assertFalse(s.readbackAccessControl);
        assertEquals(SubmissionSwitches.Hash.OFF, s.hash);
        assertEquals(SubmissionSwitches.Relations.ASYNC, s.relations);
        assertEquals(3, s.asyncWriters);
    }

    @Test
    public void theConfigurationBeforeTheEnvironment() {
        SubmissionSwitches s = of(Map.of("submission.hash", "update"), Map.of("TPL_SUBMISSION_HASH", "off"));
        assertEquals(SubmissionSwitches.Hash.UPDATE, s.hash);
    }

    @Test
    public void anUnknownValueIsAnError() {
        refused(Map.of("submission.hash", "maybe"), NO_ENV, "submission.hash");
        refused(Map.of("submission.readback.access_control", "no"), NO_ENV, "submission.readback.access_control");
        refused(Map.of(), Map.of("TPL_RELATIONS_WRITE", "later"), "TPL_RELATIONS_WRITE");
        refused(Map.of("relations.async.writers", "0"), NO_ENV, "relations.async.writers");
        refused(Map.of("relations.async.queue", "lots"), NO_ENV, "relations.async.queue");
    }

    @Test
    public void anUnknownKeyUnderTheSwitchesIsAnError() {
        refused(Map.of("submission.hsah", "off"), NO_ENV, "submission.hsah");
        refused(Map.of("relations.async.batches", 10), NO_ENV, "relations.async.batches");
    }

    private static void refused(Map<String, Object> config, Map<String, String> env, String named) {
        try {
            of(config, env);
            fail("accepted " + config + " " + env);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains(named));
        }
    }
}
