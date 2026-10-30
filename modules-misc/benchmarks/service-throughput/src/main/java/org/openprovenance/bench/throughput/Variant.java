package org.openprovenance.bench.throughput;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * What a run switches off or defers: the service's switches (template-config.json, read at start-up), whether the
 * harness sends an Idempotency-Key, and DDL applied to {@code provbench} once the service has created its schema.
 */
final class Variant {
    final String name;
    final String description;
    /** service switches: SubmissionSwitches' configuration keys */
    final Map<String, String> switches;
    /** whether messages carry an Idempotency-Key (B=1 only: the service refuses a key on a message of several entries) */
    final boolean idempotencyKey;
    /** run in provbench after the service has created the schema, before the warm-up */
    final List<String> ddl;
    /** run in provbench after its reset, before the service starts (its connections open with it) */
    final List<String> preStart;
    final boolean async;

    private Variant(String name, String description, Map<String, String> switches, boolean idempotencyKey, List<String> ddl, List<String> preStart) {
        this.name = name;
        this.preStart = preStart;
        this.description = description;
        this.switches = switches;
        this.idempotencyKey = idempotencyKey;
        this.ddl = ddl;
        this.async = "async".equals(switches.get("relations.write"));
    }

    static final Map<String, Variant> ALL = new LinkedHashMap<>();

    private static void add(String name, String description, boolean key, List<String> ddl, String... switches) {
        addWith(name, description, key, ddl, List.of(), switches);
    }

    private static void addWith(String name, String description, boolean key, List<String> ddl, List<String> preStart, String... switches) {
        Map<String, String> m = new LinkedHashMap<>();
        for (int i = 0; i < switches.length; i += 2) m.put(switches[i], switches[i + 1]);
        ALL.put(name, new Variant(name, description, m, key, ddl, preStart));
    }

    static {
        add("baseline", "the service as deployed: every switch off, Idempotency-Key sent", true, List.of());
        add("noacl", "read-back of the record just written without the record_index/access_control join", true, List.of(),
                "submission.readback.access_control", "off");
        add("acl-index", "baseline + an index on access_control(record), in provbench only", true,
                List.of("CREATE INDEX IF NOT EXISTS provbench_access_control_record ON access_control (record)"));
        add("nohash", "no hash: neither computed nor stored, no PROV-Hash header", true, List.of(),
                "submission.hash", "off");
        add("hash-header", "hash computed and sent in the header, not stored (no UPDATE of record_index)", true, List.of(),
                "submission.hash", "header");
        add("async", "__PROV_* relations written after the commit, batched, by the relation writers", true, List.of(),
                "relations.write", "async");
        add("nokey", "no Idempotency-Key header", false, List.of());
        add("key-noindex", "Idempotency-Key sent, the partial unique index record_index_submission_key dropped, in provbench only", true,
                List.of("DROP INDEX IF EXISTS record_index_submission_key"));
        addWith("nojit", "baseline with PostgreSQL's JIT off for provbench (the composites' insert is JIT-compiled at every call otherwise)", true,
                List.of(), List.of("ALTER DATABASE provbench SET jit = off"));
        add("combined", "noacl + nohash + async + nokey", false, List.of(),
                "submission.readback.access_control", "off", "submission.hash", "off", "relations.write", "async");
    }

    static Variant named(String name) {
        Variant v = ALL.get(name);
        if (v == null) throw new IllegalArgumentException("unknown VARIANT " + name + "; known: " + ALL.keySet());
        return v;
    }

    /** Whether the service should send PROV-Hash (the harness sends Accept-PROV-hash). */
    boolean hashHeader() {
        return !"off".equals(switches.get("submission.hash"));
    }
}
