package org.openprovenance.bench.throughput;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The JSONL mode, for comparison: the same records' bindings serialised as JSON lines and appended to files of the
 * local file system — no store, no service, no index, no relation, nothing read back. A floor: what writing the
 * records down costs at all.
 *
 * <p>Each record ({@link Family}: same templates, weights, composites of 3, unique values, references to ids
 * allocated earlier) is one line: its complete bindings ({@link Family#bindings}): {@code isA}, its {@code ID}, its
 * input values, its output variables bound to ids the harness allocates (a store's insert function would); a
 * composite one line with its {@code __elements}. N writer threads; each serialises B records (Jackson), appends them
 * in one write, and with {@code FSYNC=batch} forces them to disk ({@code FileChannel.force(false)}: fdatasync on
 * Linux, fsync on macOS — which, unlike {@code F_FULLFSYNC}, leaves the drive's own cache) before the next: B records
 * as one durable unit, as a transaction's commit. {@code FSYNC=off} leaves them to the OS. {@code FILES=writer}: a file
 * per writer; {@code shared}: one file, the appends (and their force) serialised.</p>
 *
 * <p>Files: {@code JSONL_DIR} (default {@code target/jsonl}), which may hold nothing but the mode's own
 * {@code provbench-<family>-<writer>.jsonl} ({@link Guard#jsonlDirectory}); they are deleted before each run. Checks: the
 * files hold exactly the bytes and the lines written (one per entry), and sampled lines parse.</p>
 */
final class JsonlRun {

    private static final ObjectMapper OM = new ObjectMapper();
    static final long SAMPLE_EVERY = 997;

    private final Settings s;

    JsonlRun(Settings settings) {
        this.s = settings;
    }

    File directory() {
        File d = new File(s.get("JSONL_DIR"));
        if (!d.isAbsolute()) d = s.file(s.get("JSONL_DIR"));
        return Guard.jsonlDirectory(d);
    }

    static boolean fsync(Settings s) {
        String v = s.get("FSYNC");
        if (!v.equals("batch") && !v.equals("off")) throw new IllegalArgumentException("FSYNC is batch or off, not " + v);
        return v.equals("batch");
    }

    static boolean shared(Settings s) {
        String v = s.get("FILES");
        if (!v.equals("writer") && !v.equals("shared")) throw new IllegalArgumentException("FILES is writer or shared, not " + v);
        return v.equals("shared");
    }

    static String describe(Settings s) {
        return "FAMILY=" + s.get("FAMILY") + " N=" + s.get("N") + " B=" + s.get("B") + " FSYNC=" + s.get("FSYNC") + " FILES=" + s.get("FILES")
                + " WARMUP=" + s.get("WARMUP") + " DURATION=" + s.get("DURATION");
    }

    /** A phase's counts. */
    static final class Outcome {
        long batches;
        long entries;
        long records;
        long bytes;
        long failures;
        double seconds;
        long[] latencyNanos = new long[0];
        final List<String> failureSamples = Collections.synchronizedList(new ArrayList<>());

        double percentileMillis(double p) {
            if (latencyNanos.length == 0) return Double.NaN;
            int i = (int) Math.ceil(p / 100.0 * latencyNanos.length) - 1;
            return latencyNanos[Math.max(0, Math.min(latencyNanos.length - 1, i))] / 1e6;
        }
    }

    private Outcome phase(double seconds, Family family, List<FileChannel> channels, boolean fsync, int n, int b, AtomicLong ids) throws InterruptedException {
        Outcome o = new Outcome();
        AtomicLong batches = new AtomicLong();
        AtomicLong entries = new AtomicLong();
        AtomicLong records = new AtomicLong();
        AtomicLong bytes = new AtomicLong();
        AtomicLong failures = new AtomicLong();
        List<long[]> perWriter = Collections.synchronizedList(new ArrayList<>());
        long start = System.nanoTime();
        long end = start + (long) (seconds * 1e9);
        List<Thread> threads = new ArrayList<>();
        for (int w = 0; w < n; w++) {
            FileChannel ch = channels.size() == 1 ? channels.get(0) : channels.get(w);
            boolean sharedChannel = channels.size() == 1 && n > 1;
            Thread t = new Thread(() -> {
                long[] lat = new long[1024];
                int count = 0;
                ByteArrayOutputStream buf = new ByteArrayOutputStream(1 << 16);
                while (System.nanoTime() < end) {
                    Family.Template template = family.pick();
                    List<Map<String, Object>> batch = new ArrayList<>(b);
                    int recs = 0;
                    for (int i = 0; i < b; i++) {
                        batch.add(family.entry(template));
                        recs += family.records(template);
                    }
                    long t0 = System.nanoTime();
                    try {
                        buf.reset();
                        for (Map<String, Object> e : batch) {
                            buf.write(OM.writeValueAsBytes(family.bindings(template, e, ids)));
                            buf.write('\n');
                        }
                        ByteBuffer bb = ByteBuffer.wrap(buf.toByteArray());
                        if (sharedChannel) {
                            synchronized (ch) {
                                writeFully(ch, bb, fsync);
                            }
                        } else {
                            writeFully(ch, bb, fsync);
                        }
                    } catch (IOException | RuntimeException e) {
                        failures.incrementAndGet();
                        if (o.failureSamples.size() < 5) o.failureSamples.add(template.isA + ": " + e);
                        continue;
                    }
                    long t1 = System.nanoTime();
                    batches.incrementAndGet();
                    entries.addAndGet(b);
                    records.addAndGet(recs);
                    bytes.addAndGet(buf.size());
                    if (count == lat.length) lat = Arrays.copyOf(lat, lat.length * 2);
                    lat[count++] = t1 - t0;
                }
                perWriter.add(Arrays.copyOf(lat, count));
            }, "jsonl-writer-" + w);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) t.join();
        o.seconds = (System.nanoTime() - start) / 1e9;
        o.batches = batches.get();
        o.entries = entries.get();
        o.records = records.get();
        o.bytes = bytes.get();
        o.failures = failures.get();
        int total = 0;
        synchronized (perWriter) {
            for (long[] l : perWriter) total += l.length;
            o.latencyNanos = new long[total];
            int k = 0;
            for (long[] l : perWriter) {
                System.arraycopy(l, 0, o.latencyNanos, k, l.length);
                k += l.length;
            }
        }
        Arrays.sort(o.latencyNanos);
        return o;
    }

    private static void writeFully(FileChannel ch, ByteBuffer bb, boolean fsync) throws IOException {
        while (bb.hasRemaining()) ch.write(bb);
        if (fsync) ch.force(false);
    }

    Run.Row execute(String runId) throws Exception {
        int n = s.integer("N");
        int b = s.integer("B");
        boolean fsync = fsync(s);
        boolean shared = shared(s);
        File dir = directory();
        Run.Row row = new Run.Row();
        row.put("run", runId);
        row.put("mode", "jsonl");
        row.put("family", s.family());
        row.put("n", n);
        row.put("b", b);
        row.put("fsync", s.get("FSYNC"));
        row.put("files", s.get("FILES"));
        row.put("warmup_s", s.integer("WARMUP"));
        row.put("duration_s", s.integer("DURATION"));
        System.out.println("\n=== jsonl run " + runId + ": " + describe(s) + " in " + dir);

        // clear: the mode's own files only (the guard has checked the directory holds nothing else)
        dir.mkdirs();
        String[] old = dir.list();
        if (old != null) for (String name : old) if (Guard.JSONL_FILE.matcher(name).matches()) Files.delete(new File(dir, name).toPath());
        List<File> files = new ArrayList<>();
        List<FileChannel> channels = new ArrayList<>();
        int fileCount = shared ? 1 : n;
        for (int i = 0; i < fileCount; i++) {
            File f = new File(dir, "provbench-" + s.family() + "-" + (shared ? "shared" : "w" + i) + ".jsonl");
            files.add(f);
            channels.add(FileChannel.open(f.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, StandardOpenOption.APPEND));
        }
        Family family = Family.named(s.family(), s.integer("COMPOSITE_SIZE"));
        AtomicLong ids = new AtomicLong();
        Outcome warm;
        Outcome m;
        long cpuBefore;
        long cpuAfter;
        try {
            System.out.printf("  warm-up %d s ...%n", s.integer("WARMUP"));
            warm = phase(s.integer("WARMUP"), family, channels, fsync, n, b, ids);
            if (warm.failures > 0) row.problems.add("warm-up: " + warm.failures + " failed write(s): " + warm.failureSamples);
            cpuBefore = processCpuNanos();
            System.out.printf("  measure %d s, N=%d B=%d ...%n", s.integer("DURATION"), n, b);
            m = phase(s.integer("DURATION"), family, channels, fsync, n, b, ids);
            cpuAfter = processCpuNanos();
        } finally {
            for (FileChannel ch : channels) ch.close();
        }
        double d = m.seconds;
        double cpu = (cpuAfter - cpuBefore) / 1e9;
        row.put("batches", m.batches);
        row.put("records", m.records);
        row.put("batches_per_s", Run.round(m.batches / d, 1));
        row.put("records_per_s", Run.round(m.records / d, 1));
        row.put("mb_per_s", Run.round(m.bytes / 1048576.0 / d, 2));
        row.put("bytes_per_record", m.records == 0 ? "" : Run.round((double) m.bytes / m.records, 0));
        row.put("lat_p50_ms", Run.round(m.percentileMillis(50), 3));
        row.put("lat_p95_ms", Run.round(m.percentileMillis(95), 3));
        row.put("lat_p99_ms", Run.round(m.percentileMillis(99), 3));
        row.put("lat_max_ms", Run.round(m.percentileMillis(100), 3));
        row.put("client_cpu_pct", Run.round(100 * cpu / d, 0));
        row.put("client_cpu_ms_per_record", m.records == 0 ? "" : Run.round(1000 * cpu / m.records, 4));
        row.put("failures", m.failures);

        // ---- checks: the files hold exactly what was written, one line per entry, and the lines parse
        long expectedBytes = warm.bytes + m.bytes;
        long expectedLines = warm.entries + m.entries;
        long[] counted = count(files);
        row.put("file_bytes", counted[0]);
        if (m.failures > 0) row.problems.add(m.failures + " failed write(s): " + m.failureSamples);
        if (counted[0] != expectedBytes) row.problems.add("the files hold " + counted[0] + " bytes, " + expectedBytes + " were written");
        if (counted[1] != expectedLines) row.problems.add("the files hold " + counted[1] + " lines, " + expectedLines + " entries were written");
        if (counted[2] > 0) row.problems.add(counted[2] + " sampled line(s) do not parse as JSON");
        System.out.printf("  %.1f batches/s, %.1f records/s, %.1f MB/s (%.0f B/record), p50 %.3f ms p99 %.3f ms per batch, client CPU %.0f%%%n",
                m.batches / d, m.records / d, m.bytes / 1048576.0 / d, (double) m.bytes / Math.max(1, m.records),
                m.percentileMillis(50), m.percentileMillis(99), 100 * cpu / d);
        for (File f : files) Files.delete(f.toPath());   // gigabytes at full speed: not kept
        row.put("status", row.ok() ? "ok" : "FAIL");
        row.put("problems", String.join("; ", row.problems));
        if (!row.ok()) {
            System.out.println("  ************************************************************");
            System.out.println("  JSONL RUN " + runId + " FAILED:");
            for (String p : row.problems) System.out.println("   - " + p);
            System.out.println("  ************************************************************");
        }
        return row;
    }

    /** Bytes, lines, and the lines that do not parse among those sampled (every {@value #SAMPLE_EVERY}th) or left unterminated. */
    static long[] count(List<File> files) throws IOException {
        long bytes = 0;
        long lines = 0;
        long bad = 0;
        byte[] chunk = new byte[1 << 20];
        for (File f : files) {
            bytes += f.length();
            ByteArrayOutputStream sampled = null;
            int lastByte = -1;
            try (InputStream in = Files.newInputStream(f.toPath())) {
                int read;
                while ((read = in.read(chunk)) > 0) {
                    int from = 0;
                    for (int k = 0; k < read; k++) {
                        if (chunk[k] == '\n') {
                            if (sampled != null) {
                                sampled.write(chunk, from, k - from);
                                if (!parses(sampled.toString(StandardCharsets.UTF_8))) bad++;
                            }
                            lines++;
                            sampled = lines % SAMPLE_EVERY == 0 ? new ByteArrayOutputStream() : null;
                            from = k + 1;
                        }
                    }
                    if (sampled != null) sampled.write(chunk, from, read - from);
                    lastByte = chunk[read - 1];
                }
            }
            if (lastByte >= 0 && lastByte != '\n') bad++;   // an unterminated last line
        }
        return new long[]{bytes, lines, bad};
    }

    private static boolean parses(String line) {
        try {
            return OM.readTree(line).has("isA");
        } catch (IOException e) {
            return false;
        }
    }

    static long processCpuNanos() {
        return ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuTime();
    }

    // ------------------------------------------------------------------ plan, matrix

    static List<Matrix.Step> plan(Settings base) {
        List<Matrix.Step> steps = new ArrayList<>();
        String name = base.get("PLAN");
        if (name.equals("smoke")) {
            for (String family : new String[]{"fs", "tr"}) {
                steps.add(new Matrix.Step("smoke", base.with("FAMILY", family, "N", "2", "B", "1", "FSYNC", "batch", "FILES", "writer")));
            }
            steps.add(new Matrix.Step("smoke", base.with("FAMILY", "fs", "N", "2", "B", "1", "FSYNC", "off", "FILES", "writer")));
            steps.add(new Matrix.Step("smoke", base.with("FAMILY", "fs", "N", "2", "B", "10", "FSYNC", "batch", "FILES", "shared")));
            return steps;
        }
        if (!name.equals("full")) throw new IllegalArgumentException("PLAN is full or smoke, not " + name);
        for (String fsync : new String[]{"batch", "off"}) {
            steps.add(new Matrix.Step("J1 one writer, fs, B=1", base.with("FAMILY", "fs", "N", "1", "B", "1", "FSYNC", fsync, "FILES", "writer")));
        }
        for (int n : new int[]{2, 4, 8, 16, 32, 64}) {
            steps.add(new Matrix.Step("J2 writers, fs, B=1, a force per record, a file each", base.with("FAMILY", "fs", "N", Integer.toString(n), "B", "1", "FSYNC", "batch", "FILES", "writer")));
        }
        for (int n : new int[]{16, 64}) {
            steps.add(new Matrix.Step("J3 one shared file, fs, B=1", base.with("FAMILY", "fs", "N", Integer.toString(n), "B", "1", "FSYNC", "batch", "FILES", "shared")));
        }
        for (int b : new int[]{10, 100}) {
            steps.add(new Matrix.Step("J4 records per force, fs, N=16", base.with("FAMILY", "fs", "N", "16", "B", Integer.toString(b), "FSYNC", "batch", "FILES", "writer")));
        }
        for (int b : new int[]{1, 100}) {
            steps.add(new Matrix.Step("J4 records per force, fs, N=16", base.with("FAMILY", "fs", "N", "16", "B", Integer.toString(b), "FSYNC", "off", "FILES", "writer")));
        }
        for (int n : new int[]{1, 16}) {
            steps.add(new Matrix.Step("J5 transport family, B=1", base.with("FAMILY", "tr", "N", Integer.toString(n), "B", "1", "FSYNC", "batch", "FILES", "writer")));
        }
        return steps;
    }

    /** Seconds a run costs besides its warm-up and window: clearing, counting and deleting the files. */
    static final double OVERHEAD_S = 3;

    static String describePlan(List<Matrix.Step> steps, Settings base) {
        StringBuilder sb = new StringBuilder();
        String phase = "";
        int i = 0;
        double total = 0;
        for (Matrix.Step st : steps) {
            if (!st.phase.equals(phase)) {
                phase = st.phase;
                sb.append("\n**").append(phase).append("**\n\n");
            }
            sb.append(++i).append(". ").append(describe(st.settings)).append('\n');
            total += st.settings.integer("WARMUP") + st.settings.integer("DURATION") + OVERHEAD_S;
        }
        sb.append(String.format("%n%d runs x (warm-up %d s + measure %d s + clear/count ~%.0f s) = ~%.0f s = ~%.1f min%n",
                steps.size(), base.integer("WARMUP"), base.integer("DURATION"), OVERHEAD_S, total, total / 60));
        return sb.toString();
    }

    static final String[][] COLUMNS = {
            {"run", "run"}, {"family", "fam"}, {"n", "N"}, {"b", "B"}, {"fsync", "fsync"}, {"files", "files"},
            {"batches_per_s", "batches/s"}, {"records_per_s", "records/s"}, {"mb_per_s", "MB/s"}, {"bytes_per_record", "B/rec"},
            {"lat_p50_ms", "p50 ms/batch"}, {"lat_p99_ms", "p99 ms/batch"}, {"lat_max_ms", "max ms/batch"},
            {"client_cpu_pct", "CPU %"}, {"client_cpu_ms_per_record", "CPU ms/rec"}, {"status", "status"}};

    static final String INTRO = "Generated by `make bench.jsonl.matrix` / `make bench.jsonl.run` (modules-misc/benchmarks/service-throughput, "
            + "JSONL mode: the records' bindings appended to local files, no store, no service); see BENCHMARK.md, section \"JSONL mode\".";

    static final String LEGEND = "Rates are per measured second; a batch = B records serialised, appended in one write and, with fsync=batch, "
            + "forced to disk (FileChannel.force: fdatasync on Linux, fsync on macOS); latency is per batch, serialisation included; "
            + "CPU % is the harness JVM's, of one core; B/rec = bytes per template record (a composite's line counts for its 1 + 3 records).";

    /** The JSONL matrix: every run appended to results/&lt;date&gt;-jsonl[-LABEL].csv, the Markdown rewritten after each. */
    static int matrix(Settings s) throws Exception {
        Report report = new Report(s, "jsonl");
        List<Matrix.Step> steps = plan(s);
        String plan = describePlan(steps, s);
        System.out.println(plan);
        recordEnvironment(report, s);
        long t0 = System.nanoTime();
        int failed = 0;
        int i = 0;
        String stamp = Long.toString(System.currentTimeMillis() / 1000);
        for (Matrix.Step step : steps) {
            i++;
            String id = String.format("j%s-%02d", stamp, i);
            Run.Row row;
            try {
                row = new JsonlRun(step.settings).execute(id);
            } catch (Guard.Refused e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                row = new Run.Row();
                row.put("run", id);
                row.put("mode", "jsonl");
                row.put("family", step.settings.get("FAMILY"));
                row.put("n", step.settings.get("N"));
                row.put("b", step.settings.get("B"));
                row.problems.add("aborted: " + e);
                row.put("status", "FAIL");
                row.put("problems", "aborted: " + e.toString().replace('\n', ' '));
            }
            if (!row.ok()) failed++;
            Main.appendAligned(report, row);
            report.writeMarkdown("Service throughput, JSONL mode, " + report.name, plan, COLUMNS, INTRO, LEGEND);
            System.out.printf("  [%d/%d done, %.1f min elapsed, %d failed]%n", i, steps.size(), (System.nanoTime() - t0) / 60e9, failed);
            if (!row.ok() && "1".equals(s.get("STOP_ON_FAIL"))) break;
        }
        System.out.printf("%njsonl matrix: %d run(s) in %.1f min, %d failed; %s%n", i, (System.nanoTime() - t0) / 60e9, failed, report.markdown());
        return failed == 0 ? 0 : 1;
    }

    /** The environment file: the machine, and the file system the JSONL directory is on. */
    static void recordEnvironment(Report report, Settings s) throws Exception {
        report.recordEnvironment(false);
        File f = report.environment();
        String text = Files.readString(f.toPath());
        if (!text.contains("| JSONL directory |")) {
            File dir = new JsonlRun(s).directory();
            dir.mkdirs();
            Shell.Result df = Shell.run(30, List.of("df", "-h", dir.getAbsolutePath()));
            String fs = df.out.trim().replace("\n", " / ").replace("|", "\\|");
            text = text + "| JSONL directory | " + dir.getAbsolutePath() + " (on the host, not in the Docker VM): " + fs + " |\n"
                    + "| JSONL client JVM | " + System.getProperty("java.vendor") + " " + System.getProperty("java.version")
                    + "; the service JVM and PostgreSQL rows above do not apply (no service, no store) |\n";
            Files.writeString(f.toPath(), text);
        }
    }
}
