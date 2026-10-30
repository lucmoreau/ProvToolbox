package org.openprovenance.prov.service.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Switches that turn off or defer parts of a submission's write path ({@code POST provapi/statements}), read once at
 * start-up from the template configuration, each with an environment-variable (or system-property) fallback. Every
 * switch defaults to the behaviour the service always had; nothing changes for a deployment that sets none.
 *
 * <ul>
 *   <li>{@value #READBACK_ACCESS_CONTROL} ({@value #ENV_READBACK_ACCESS_CONTROL}): {@code on} (default) | {@code off}.
 *       Off, the post-processing read-back of the record the request has just written is made without the
 *       {@code record_index}/{@code access_control} join. Only that read-back: every other reading (GET, search,
 *       traversals) keeps the access control.</li>
 *   <li>{@value #HASH} ({@value #ENV_HASH}): {@code update} (default: computed, sent in the {@code PROV-Hash} header,
 *       stored in {@code record_index.hash}) | {@code header} (computed and sent, not stored) | {@code off} (neither:
 *       no header, {@code record_index.hash} NULL).</li>
 *   <li>{@value #RELATIONS} ({@value #ENV_RELATIONS}): {@code inline} (default: the {@code __PROV_*} rows written with
 *       the submission, as before) | {@code async} (queued after the submission's commit and written in batches by
 *       {@link AsyncRelationWriter}), sized by {@value #ASYNC_WRITERS}, {@value #ASYNC_QUEUE} and
 *       {@value #ASYNC_BATCH}.</li>
 * </ul>
 * An unknown value, or an unknown key under {@code submission.} or {@code relations.}, is an error at start-up.
 */
public final class SubmissionSwitches {

    public static final String READBACK_ACCESS_CONTROL = "submission.readback.access_control";
    public static final String HASH = "submission.hash";
    public static final String RELATIONS = "relations.write";
    public static final String ASYNC_WRITERS = "relations.async.writers";
    public static final String ASYNC_QUEUE = "relations.async.queue";
    public static final String ASYNC_BATCH = "relations.async.batch";

    public static final String ENV_READBACK_ACCESS_CONTROL = "TPL_READBACK_ACCESS_CONTROL";
    public static final String ENV_HASH = "TPL_SUBMISSION_HASH";
    public static final String ENV_RELATIONS = "TPL_RELATIONS_WRITE";
    public static final String ENV_ASYNC_WRITERS = "TPL_RELATIONS_ASYNC_WRITERS";
    public static final String ENV_ASYNC_QUEUE = "TPL_RELATIONS_ASYNC_QUEUE";
    public static final String ENV_ASYNC_BATCH = "TPL_RELATIONS_ASYNC_BATCH";

    private static final Set<String> KEYS = Set.of(READBACK_ACCESS_CONTROL, HASH, RELATIONS, ASYNC_WRITERS, ASYNC_QUEUE, ASYNC_BATCH);

    /** What becomes of a submission's hash. */
    public enum Hash {
        /** computed, sent in the header, stored in record_index (as always) */
        UPDATE,
        /** computed and sent in the header, not stored */
        HEADER,
        /** not computed */
        OFF
    }

    /** How a submission's {@code __PROV_*} relation rows are written. */
    public enum Relations {
        /** with the submission (as always) */
        INLINE,
        /** after the submission's commit, batched, by {@link AsyncRelationWriter} */
        ASYNC
    }

    /** The switches as the service always behaved. */
    public static final SubmissionSwitches DEFAULTS = new SubmissionSwitches(true, Hash.UPDATE, Relations.INLINE, 2, 10000, 200);

    public final boolean readbackAccessControl;
    public final Hash hash;
    public final Relations relations;
    public final int asyncWriters;
    public final int asyncQueue;
    public final int asyncBatch;

    public SubmissionSwitches(boolean readbackAccessControl, Hash hash, Relations relations, int asyncWriters, int asyncQueue, int asyncBatch) {
        this.readbackAccessControl = readbackAccessControl;
        this.hash = hash;
        this.relations = relations;
        this.asyncWriters = asyncWriters;
        this.asyncQueue = asyncQueue;
        this.asyncBatch = asyncBatch;
    }

    /** The switches of the template configuration, else of the environment ({@link ServiceUtils}' lookup), else the defaults. */
    public static SubmissionSwitches from(Map<String, Object> configuration) {
        return from(configuration, name -> ServiceUtils.getSystemOrEnvironmentVariableOrDefault(name, null));
    }

    /**
     * The switches of {@code configuration}, each falling back to {@code environment} (variable name → value, or null),
     * then to its default.
     *
     * @throws IllegalArgumentException on an unknown value, or an unknown key under {@code submission.}/{@code relations.}
     */
    public static SubmissionSwitches from(Map<String, Object> configuration, Function<String, String> environment) {
        for (String key : configuration.keySet()) {
            if ((key.startsWith("submission.") || key.startsWith("relations.")) && !KEYS.contains(key)) {
                throw new IllegalArgumentException("unknown switch " + key + " in the template configuration; known: " + KEYS);
            }
        }
        String readback = value(configuration, READBACK_ACCESS_CONTROL, environment, ENV_READBACK_ACCESS_CONTROL, "on");
        boolean readbackAccessControl;
        switch (readback) {
            case "on": readbackAccessControl = true; break;
            case "off": readbackAccessControl = false; break;
            default: throw unknown(READBACK_ACCESS_CONTROL, ENV_READBACK_ACCESS_CONTROL, readback, "on, off");
        }
        String hashValue = value(configuration, HASH, environment, ENV_HASH, "update");
        Hash hash;
        switch (hashValue) {
            case "update": hash = Hash.UPDATE; break;
            case "header": hash = Hash.HEADER; break;
            case "off": hash = Hash.OFF; break;
            default: throw unknown(HASH, ENV_HASH, hashValue, "update, header, off");
        }
        String relationsValue = value(configuration, RELATIONS, environment, ENV_RELATIONS, "inline");
        Relations relations;
        switch (relationsValue) {
            case "inline": relations = Relations.INLINE; break;
            case "async": relations = Relations.ASYNC; break;
            default: throw unknown(RELATIONS, ENV_RELATIONS, relationsValue, "inline, async");
        }
        int writers = positive(configuration, ASYNC_WRITERS, environment, ENV_ASYNC_WRITERS, DEFAULTS.asyncWriters);
        int queue = positive(configuration, ASYNC_QUEUE, environment, ENV_ASYNC_QUEUE, DEFAULTS.asyncQueue);
        int batch = positive(configuration, ASYNC_BATCH, environment, ENV_ASYNC_BATCH, DEFAULTS.asyncBatch);
        return new SubmissionSwitches(readbackAccessControl, hash, relations, writers, queue, batch);
    }

    private static String value(Map<String, Object> configuration, String key, Function<String, String> environment, String variable, String fallback) {
        Object configured = configuration.get(key);
        String value = configured != null ? configured.toString() : environment.apply(variable);
        return value == null ? fallback : value.trim().toLowerCase();
    }

    private static int positive(Map<String, Object> configuration, String key, Function<String, String> environment, String variable, int fallback) {
        String value = value(configuration, key, environment, variable, Integer.toString(fallback));
        try {
            int n = Integer.parseInt(value);
            if (n >= 1) return n;
        } catch (NumberFormatException e) {
            // reported below
        }
        throw unknown(key, variable, value, "a whole number of at least 1");
    }

    private static IllegalArgumentException unknown(String key, String variable, String value, String expected) {
        return new IllegalArgumentException(key + " (or " + variable + ") is '" + value + "'; expected " + expected);
    }

    /** The switches as configuration keys and values, as logged at start-up and reported by {@code provapi/submission/status.json}. */
    public Map<String, Object> asMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put(READBACK_ACCESS_CONTROL, readbackAccessControl ? "on" : "off");
        m.put(HASH, hash.name().toLowerCase());
        m.put(RELATIONS, relations.name().toLowerCase());
        m.put(ASYNC_WRITERS, asyncWriters);
        m.put(ASYNC_QUEUE, asyncQueue);
        m.put(ASYNC_BATCH, asyncBatch);
        return m;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        asMap().forEach((k, v) -> sb.append(sb.length() == 0 ? "" : " ").append(k).append('=').append(v));
        return sb.toString();
    }
}
