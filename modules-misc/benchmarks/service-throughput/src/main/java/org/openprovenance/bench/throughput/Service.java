package org.openprovenance.bench.throughput;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The service under test: the generated project's webapp ({@code make bench.service.build}) on an embedded Jetty
 * ({@link ServiceLauncher}) in a JVM of its own, on {@value Guard#SERVICE_PORT}{@value Guard#SERVICE_CONTEXT}, with the
 * run's template-config.json and storage configuration. Its environment is built here: nothing of the caller's
 * TPL_*, PROV_*, TEMPLATE_CONFIG or JDBC_POOL_SIZE reaches it.
 */
final class Service {

    static final String CONFIG_PROPERTIES = "catalogue.package jdbc.url sql.initializer nlg.xplan.library nlg.xplan.selection nlg.xplan.strategy icons.folder.for.graphviz";
    private static final ObjectMapper OM = new ObjectMapper();

    private final Settings settings;
    private final HttpClient http = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(5)).build();
    private final URI base = Guard.serviceUrl(Settings.SERVICE_URL);

    Service(Settings settings) {
        this.settings = settings;
    }

    URI base() {
        return base;
    }

    File pidFile() {
        return new File(settings.runDir(), "service.pid");
    }

    File logFile() {
        return new File(settings.runDir(), "service.log");
    }

    /** The run's template-config.json: the generated one's library settings, the benchmark's JDBC URL, pool and switches. */
    File writeConfig() throws IOException {
        File generated = new File(settings.serviceTarget(), "test-classes/template-config.json");
        if (!generated.exists()) throw new IllegalStateException("no generated service for " + settings.family() + ": make bench.service.build FAMILY=" + settings.family());
        JsonNode g = OM.readTree(generated);
        ObjectNode config = OM.createObjectNode();
        for (String key : CONFIG_PROPERTIES.split(" ")) {
            if (g.has(key)) config.set(key, g.get(key));
        }
        config.put("jdbc.url", Guard.jdbcUrl(Settings.JDBC_URL));
        config.put("jdbc.pool.size", settings.integer("POOL"));
        Variant v = settings.variant();
        v.switches.forEach(config::put);
        if (v.async) {
            config.put("relations.async.writers", settings.integer("WRITERS"));
            config.put("relations.async.queue", settings.integer("QUEUE"));
            config.put("relations.async.batch", settings.integer("BATCH"));
        }
        File out = new File(settings.runDir(), "template-config.json");
        OM.writerWithDefaultPrettyPrinter().writeValue(out, config);
        // translation storage: in memory and on the file system, never the Redis or MongoDB of the deployed services
        ObjectNode storage = OM.createObjectNode();
        storage.put("dbname", "provbench");
        storage.put("index", "memory");
        storage.put("storage", "fs");
        storage.put("redis_host", "127.0.0.1");
        storage.put("redis_port", 1);
        storage.put("mongo_host", "127.0.0.1");
        storage.put("mongo_port", 1);
        File uploads = new File(settings.runDir(), "uploads");
        uploads.mkdirs();
        storage.put("uploaded_filepath", uploads.getAbsolutePath());
        OM.writerWithDefaultPrettyPrinter().writeValue(new File(settings.runDir(), "storage-configuration-provbench.json"), storage);
        return out;
    }

    List<String> jvmFlags() {
        List<String> flags = new ArrayList<>();
        for (String f : settings.get("SERVICE_JVM_FLAGS").split("\\s+")) if (!f.isBlank()) flags.add(f);
        return flags;
    }

    /**
     * Starts the service with the run's configuration and waits until it answers, with the switches asked for.
     *
     * @return the time it took, in seconds
     */
    double start() throws IOException, InterruptedException {
        if (Store.portInUse(Guard.SERVICE_PORT)) {
            throw new Guard.Refused("port " + Guard.SERVICE_PORT + " is in use already (make bench.service.down)");
        }
        File webapp = settings.webapp();
        if (!webapp.isDirectory()) throw new IllegalStateException("no webapp at " + webapp + ": make bench.service.build FAMILY=" + settings.family());
        int n = settings.integer("N");
        int maxThreads = settings.integer("JETTY_MAX_THREADS");
        if (maxThreads < n + 8) throw new IllegalArgumentException("JETTY_MAX_THREADS (" + maxThreads + ") must be at least N + 8 (" + (n + 8) + ")");
        File config = writeConfig();
        List<String> cmd = new ArrayList<>();
        cmd.add(settings.get("JAVA"));
        cmd.addAll(jvmFlags());
        cmd.add("-Dprovbench.service=1");
        cmd.add("-cp");
        cmd.add(settings.file("target/service-throughput.jar").getAbsolutePath() + File.pathSeparator
                + settings.file("target/launcher-lib").getAbsolutePath() + File.separator + "*");
        cmd.add(ServiceLauncher.class.getName());
        cmd.add("--webapp=" + webapp.getAbsolutePath());
        cmd.add("--host=127.0.0.1");
        cmd.add("--port=" + Guard.SERVICE_PORT);
        cmd.add("--context=" + Guard.SERVICE_CONTEXT);
        cmd.add("--max-threads=" + maxThreads);
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Map<String, String> env = pb.environment();
        env.keySet().removeIf(k -> k.startsWith("TPL_") || k.startsWith("PROV_") || k.startsWith("XPLAIN_") || k.equals("TEMPLATE_CONFIG")
                || k.equals("JDBC_POOL_SIZE") || k.equals("RESOURCES_HOST"));
        for (String opts : new String[]{"JAVA_TOOL_OPTIONS", "JDK_JAVA_OPTIONS", "_JAVA_OPTIONS"}) {
            String v = env.get(opts);
            if (v != null && v.matches("(?s).*-D(TPL_|PROV_|TEMPLATE_CONFIG|JDBC_POOL_SIZE).*")) {
                throw new Guard.Refused(opts + " sets a service variable (" + v + "); unset it for the benchmark");
            }
        }
        env.put("TEMPLATE_CONFIG", config.getAbsolutePath());
        env.put("PROV_STORAGE_CONFIG", new File(settings.runDir(), "storage-configuration-provbench.json").getAbsolutePath());
        env.put("TPL_DB_USER", Settings.DB_USER);
        env.put("TPL_DB_PASS", Settings.DB_PASS);
        env.put("TPL_HOST", base.toString());
        env.put("PROV_HOST", base.toString());
        env.put("PROV_API", base + "/provapi");
        env.put("RESOURCES_HOST", base + "/provapi/resources");
        env.put("TPL_ACTIVITY_FLUSH_SECONDS", "1");
        env.put("PROV_SERVICE_NAME", "provbench");
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile()));
        Files.writeString(logFile().toPath(), "\n==== " + java.time.Instant.now() + " " + settings + "\n==== " + String.join(" ", cmd) + "\n",
                StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
        long t0 = System.nanoTime();
        Process p = pb.start();
        Files.writeString(pidFile().toPath(), Long.toString(p.pid()));
        long deadline = System.currentTimeMillis() + 300_000;
        while (System.currentTimeMillis() < deadline) {
            if (!p.isAlive()) {
                throw new IllegalStateException("the service ended while starting (exit " + p.exitValue() + "); see " + logFile() + "\n" + tail(40));
            }
            JsonNode status = statusOrNull();
            if (status != null) {
                double seconds = (System.nanoTime() - t0) / 1e9;
                checkSwitches(status);
                return seconds;
            }
            Thread.sleep(250);
        }
        throw new IllegalStateException("the service did not answer within 300 s; see " + logFile());
    }

    /** The switches the service reports are the ones asked for (an unknown or ignored setting would make the run meaningless). */
    private void checkSwitches(JsonNode status) {
        JsonNode switches = status.get("switches");
        Variant v = settings.variant();
        Map<String, String> expected = new java.util.LinkedHashMap<>();
        expected.put("submission.readback.access_control", "on");
        expected.put("submission.hash", "update");
        expected.put("relations.write", "inline");
        expected.putAll(v.switches);
        for (Map.Entry<String, String> e : expected.entrySet()) {
            String actual = switches == null || switches.get(e.getKey()) == null ? null : switches.get(e.getKey()).asText();
            if (!e.getValue().equals(actual)) {
                throw new IllegalStateException("the service runs with " + e.getKey() + "=" + actual + ", not " + e.getValue());
            }
        }
        int pool = status.path("jdbc.pool.size").asInt(-1);
        if (pool != settings.integer("POOL")) throw new IllegalStateException("the service runs with jdbc.pool.size=" + pool + ", not " + settings.get("POOL"));
    }

    /** GET provapi/submission/status.json, or null when the service does not answer (yet). */
    JsonNode statusOrNull() {
        try {
            HttpResponse<String> r = http.send(HttpRequest.newBuilder(URI.create(base + "/provapi/submission/status.json"))
                    .timeout(Duration.ofSeconds(10)).GET().build(), HttpResponse.BodyHandlers.ofString());
            return r.statusCode() == 200 ? OM.readTree(r.body()) : null;
        } catch (IOException e) {
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    JsonNode status() {
        JsonNode s = statusOrNull();
        if (s == null) throw new IllegalStateException("the service does not answer " + base + "/provapi/submission/status.json");
        return s;
    }

    /** Flushes the service's activity counters (the summary flushes them first). */
    void flushActivity() throws IOException, InterruptedException {
        HttpResponse<String> r = http.send(HttpRequest.newBuilder(URI.create(base + "/provapi/activity/summary.json"))
                .timeout(Duration.ofSeconds(30)).GET().build(), HttpResponse.BodyHandlers.ofString());
        if (r.statusCode() != 200) throw new IllegalStateException("activity/summary.json answered " + r.statusCode());
    }

    /** The service JVM's own CPU time, in ns, from the launcher's meter (portable: no /proc, no ps). */
    long cpuNanos() {
        try {
            HttpResponse<String> r = http.send(HttpRequest.newBuilder(URI.create(base + ServiceLauncher.METER_PATH))
                    .timeout(Duration.ofSeconds(10)).GET().build(), HttpResponse.BodyHandlers.ofString());
            if (r.statusCode() == 200) return OM.readTree(r.body()).path("process_cpu_ns").asLong(-1);
        } catch (IOException e) {
            // below
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("  (the service's CPU meter did not answer: CPU time from /proc or ps, coarser)");
        return psCpuNanos();
    }

    /** The JVM's CPU time from /proc (Linux, 10 ms ticks), else as ps reports it ([[dd-]hh:]mm:ss[.cc]); -1 when unknown. */
    long psCpuNanos() {
        Optional<Long> pid = pid();
        if (pid.isEmpty()) return -1;
        File stat = new File("/proc/" + pid.get() + "/stat");
        if (stat.exists()) {
            try {
                String s = Files.readString(stat.toPath());
                String[] f = s.substring(s.lastIndexOf(')') + 2).split(" ");
                return (Long.parseLong(f[11]) + Long.parseLong(f[12])) * 10_000_000L;   // utime + stime, at 100 ticks/s
            } catch (IOException | RuntimeException e) {
                // ps below
            }
        }
        Shell.Result r = Shell.run(10, List.of("ps", "-o", "time=", "-p", Long.toString(pid.get())));
        if (!r.ok()) return -1;
        return parseCpuTime(r.out.trim());
    }

    static long parseCpuTime(String s) {
        long days = 0;
        int dash = s.indexOf('-');
        if (dash > 0) {
            days = Long.parseLong(s.substring(0, dash));
            s = s.substring(dash + 1);
        }
        String[] parts = s.split(":");
        double seconds = 0;
        for (String part : parts) seconds = seconds * 60 + Double.parseDouble(part);
        return (long) ((days * 86400 + seconds) * 1e9);
    }

    Optional<Long> pid() {
        File f = pidFile();
        if (!f.exists()) return Optional.empty();
        try {
            return Optional.of(Long.parseLong(Files.readString(f.toPath()).trim()));
        } catch (IOException | NumberFormatException e) {
            return Optional.empty();
        }
    }

    /** Stops the service this harness started (SIGTERM: Jetty stops the webapp, the relation writer drains); the time it took, in s. */
    double stop() throws InterruptedException {
        Optional<Long> pid = pid();
        if (pid.isEmpty()) {
            if (Store.portInUse(Guard.SERVICE_PORT)) {
                throw new IllegalStateException("port " + Guard.SERVICE_PORT + " answers but no service.pid: stop that process yourself");
            }
            return 0;
        }
        Optional<ProcessHandle> h = ProcessHandle.of(pid.get());
        long t0 = System.nanoTime();
        if (h.isPresent() && h.get().isAlive()) {
            String command = h.get().info().commandLine().orElse("");
            if (!command.isEmpty() && !command.contains(ServiceLauncher.class.getName())) {
                throw new IllegalStateException("pid " + pid.get() + " is not the benchmark's service: " + command);
            }
            h.get().destroy();
            long deadline = System.currentTimeMillis() + 150_000;
            while (h.get().isAlive() && System.currentTimeMillis() < deadline) Thread.sleep(100);
            if (h.get().isAlive()) {
                System.out.println("the service did not stop within 150 s: killed");
                h.get().destroyForcibly();
            }
        }
        pidFile().delete();
        long deadline = System.currentTimeMillis() + 30_000;
        while (Store.portInUse(Guard.SERVICE_PORT) && System.currentTimeMillis() < deadline) Thread.sleep(100);
        return (System.nanoTime() - t0) / 1e9;
    }

    String tail(int lines) {
        try {
            List<String> all = Files.readAllLines(logFile().toPath(), StandardCharsets.UTF_8);
            return String.join("\n", all.subList(Math.max(0, all.size() - lines), all.size()));
        } catch (IOException e) {
            return "(no log)";
        }
    }

    /** The service's log lines that matter for the report: the switches it started with. */
    String writePathLine() {
        try {
            List<String> all = Files.readAllLines(logFile().toPath(), StandardCharsets.UTF_8);
            for (int i = all.size() - 1; i >= 0; i--) {
                int k = all.get(i).indexOf("write path: ");
                if (k >= 0) return all.get(i).substring(k);
            }
        } catch (IOException e) {
            // none
        }
        return "";
    }
}
