package org.openprovenance.bench.throughput;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A run's parameters: {@code key=value} arguments (the Makefile passes its variables so), else the defaults below.
 * The store's and the service's coordinates are fixed (see {@link Guard}); only what the guard allows can be named.
 */
final class Settings {

    // ---- fixed: the benchmark's own store and service
    static final String JDBC_URL = "jdbc:postgresql://localhost:" + Guard.DB_PORT + "/" + Guard.DB_NAME;
    static final String SERVICE_URL = "http://localhost:" + Guard.SERVICE_PORT + Guard.SERVICE_CONTEXT;
    /** The benchmark's own credentials, set explicitly, never inherited (TPL_DB_USER/TPL_DB_PASS name the deployed stores'). */
    static final String DB_USER = "provbench";
    static final String DB_PASS = "provbench";
    static final String CONTAINER = "provbench-pg";
    static final String VOLUME = "provbench-pgdata";

    final Map<String, String> values = new LinkedHashMap<>();
    final File home;

    Settings(String[] args) {
        values.put("FAMILY", "fs");
        values.put("VARIANT", "baseline");
        values.put("N", "1");
        values.put("B", "1");
        values.put("POOL", "0");
        values.put("WARMUP", "10");
        values.put("DURATION", "30");
        values.put("WRITERS", "2");
        values.put("QUEUE", "10000");
        values.put("BATCH", "200");
        values.put("COMPOSITE_SIZE", "3");
        values.put("JETTY_MAX_THREADS", "200");
        values.put("SERVICE_JVM_FLAGS", "-Xms2g -Xmx2g -XX:+UseG1GC");
        values.put("IMAGE", "postgres:16");
        values.put("PG_SHARED_BUFFERS", "1GB");
        values.put("PG_MAX_CONNECTIONS", "200");
        values.put("RESULTS", "results");
        values.put("LABEL", "");
        values.put("STOP_ON_FAIL", "0");
        values.put("PLAN", "full");
        values.put("JAVA", System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
        // the direct JDBC mode (DirectMain): what each writer does per record, and how records are grouped
        values.put("LAYER", "post");          // insert: insert function + record_index; post: + the service's post-processing
        values.put("KEY", "on");              // an Idempotency-Key per record
        values.put("READBACK", "on");         // post: the read-back's access-control join (submission.readback.access_control)
        values.put("HASH", "update");         // post: update|header|off (submission.hash)
        values.put("RELATIONS", "inline");    // post: inline|async (relations.write)
        values.put("JDBC_BATCH", "0");        // insert, B > 1: the B statements sent in one round trip
        values.put("HTTP_RESULTS", "");       // bench.direct.compare: the HTTP CSV(s) to compare with (comma-separated), else all

        for (String a : args) {
            int eq = a.indexOf('=');
            if (eq <= 0) throw new IllegalArgumentException("expected KEY=value, got " + a);
            String key = a.substring(0, eq).trim();
            String value = a.substring(eq + 1).trim();
            if (value.isEmpty()) continue;   // an empty make variable: the default
            if (!values.containsKey(key)) throw new IllegalArgumentException("unknown setting " + key + "; known: " + values.keySet());
            values.put(key, value);
        }
        this.home = new File(System.getProperty("bench.home", ".")).getAbsoluteFile();
    }

    private Settings(Settings other) {
        this.values.putAll(other.values);
        this.home = other.home;
    }

    /** A copy with some settings changed (a run of the matrix). */
    Settings with(String... keyValues) {
        Settings s = new Settings(this);
        for (int i = 0; i < keyValues.length; i += 2) {
            if (!s.values.containsKey(keyValues[i])) throw new IllegalArgumentException(keyValues[i]);
            s.values.put(keyValues[i], keyValues[i + 1]);
        }
        return s;
    }

    String get(String key) {
        return values.get(key);
    }

    int integer(String key) {
        try {
            return Integer.parseInt(values.get(key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " is not a whole number: " + values.get(key));
        }
    }

    String family() {
        String f = get("FAMILY");
        if (!f.equals("fs") && !f.equals("tr")) throw new IllegalArgumentException("FAMILY is fs or tr, not " + f);
        return f;
    }

    Variant variant() {
        return Variant.named(get("VARIANT"));
    }

    File file(String relative) {
        return new File(home, relative);
    }

    File runDir() {
        File d = file("target/run");
        d.mkdirs();
        return d;
    }

    File resultsDir() {
        File d = new File(get("RESULTS"));
        if (!d.isAbsolute()) d = file(get("RESULTS"));
        d.mkdirs();
        return d;
    }

    /** The generated service's exploded webapp (make bench.service.build FAMILY=...). */
    File webapp() {
        String f = family();
        return file("target/services/provbench-" + f + "/provbench-" + f + "-service/target/provbench-" + f + "-service-0.1.0-SNAPSHOT-unsecure");
    }

    File serviceTarget() {
        String f = family();
        return file("target/services/provbench-" + f + "/provbench-" + f + "-service/target");
    }

    @Override
    public String toString() {
        return "FAMILY=" + get("FAMILY") + " VARIANT=" + get("VARIANT") + " N=" + get("N") + " B=" + get("B") + " POOL=" + get("POOL")
                + " WARMUP=" + get("WARMUP") + " DURATION=" + get("DURATION")
                + (variant().async ? " WRITERS=" + get("WRITERS") + " QUEUE=" + get("QUEUE") + " BATCH=" + get("BATCH") : "");
    }
}
