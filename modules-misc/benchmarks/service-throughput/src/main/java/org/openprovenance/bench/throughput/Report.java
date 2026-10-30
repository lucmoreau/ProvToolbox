package org.openprovenance.bench.throughput;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The results: one CSV row per run ({@code results/<date>[-LABEL].csv}, appended), the top statements of each run
 * ({@code ...-statements.md}), and a Markdown report ({@code results/<date>[-LABEL].md}) with the machine, the
 * software and a table of the runs, rewritten after every run.
 */
final class Report {

    private final Settings s;
    final String name;

    Report(Settings settings) {
        this.s = settings;
        String label = settings.get("LABEL");
        this.name = LocalDate.now() + (label == null || label.isBlank() ? "" : "-" + label);
    }

    File csv() {
        return new File(s.resultsDir(), name + ".csv");
    }

    File markdown() {
        return new File(s.resultsDir(), name + ".md");
    }

    File statements() {
        return new File(s.resultsDir(), name + "-statements.md");
    }

    File environment() {
        return new File(s.resultsDir(), name + "-environment.md");
    }

    void append(Run.Row row) throws IOException {
        File f = csv();
        boolean fresh = !f.exists();
        StringBuilder sb = new StringBuilder();
        if (fresh) sb.append(String.join(",", row.values.keySet())).append('\n');
        List<String> cells = new ArrayList<>();
        for (Object v : row.values.values()) cells.add(cell(String.valueOf(v)));
        sb.append(String.join(",", cells)).append('\n');
        Files.writeString(f.toPath(), sb.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        StringBuilder st = new StringBuilder();
        st.append("\n### ").append(row.values.get("run")).append(" — ").append(row.values.get("family")).append(' ').append(row.values.get("variant"))
                .append(" N=").append(row.values.get("n")).append(" B=").append(row.values.get("b")).append(" POOL=").append(row.values.get("pool")).append("\n\n");
        st.append("| calls | total ms | mean ms | rows | % time | statement |\n|---:|---:|---:|---:|---:|---|\n");
        for (String[] t : row.topStatements) {
            st.append("| ").append(t[0]).append(" | ").append(t[1]).append(" | ").append(t[2]).append(" | ").append(t[3]).append(" | ").append(t[4])
                    .append(" | `").append(t[5].replace("|", "\\|").replace("`", "'")).append("` |\n");
        }
        Files.writeString(statements().toPath(), st.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private static String cell(String v) {
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) return "\"" + v.replace("\"", "\"\"").replace("\n", " ") + "\"";
        return v;
    }

    static List<Map<String, String>> readCsv(File f) throws IOException {
        List<Map<String, String>> rows = new ArrayList<>();
        if (!f.exists()) return rows;
        List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
        if (lines.isEmpty()) return rows;
        List<String> header = split(lines.get(0));
        for (String line : lines.subList(1, lines.size())) {
            if (line.isBlank()) continue;
            List<String> cells = split(line);
            Map<String, String> m = new LinkedHashMap<>();
            for (int i = 0; i < header.size(); i++) m.put(header.get(i), i < cells.size() ? cells.get(i) : "");
            rows.add(m);
        }
        return rows;
    }

    private static List<String> split(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (quoted) {
                if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else if (c == '"') {
                    quoted = false;
                } else {
                    cur.append(c);
                }
            } else if (c == '"') {
                quoted = true;
            } else if (c == ',') {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out;
    }

    /** The machine and the software, recorded once per report (the environment file). */
    void recordEnvironment() throws Exception {
        if (environment().exists()) return;
        StringBuilder sb = new StringBuilder();
        String os = System.getProperty("os.name");
        boolean mac = os.toLowerCase().contains("mac");
        sb.append("| | |\n|---|---|\n");
        row(sb, "date", java.time.ZonedDateTime.now().toString());
        row(sb, "OS", os + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
        if (mac) {
            row(sb, "CPU", out("sysctl", "-n", "machdep.cpu.brand_string"));
            row(sb, "cores", out("sysctl", "-n", "hw.physicalcpu") + " physical, " + out("sysctl", "-n", "hw.logicalcpu") + " logical");
            row(sb, "RAM", gib(out("sysctl", "-n", "hw.memsize")));
            String disk = out("diskutil", "info", "/");
            StringBuilder storage = new StringBuilder();
            for (String l : disk.split("\n")) {
                String t = l.trim();
                if (t.startsWith("Device / Media Name:") || t.startsWith("Solid State:") || t.startsWith("Protocol:") || t.startsWith("File System Personality:")) {
                    if (storage.length() > 0) storage.append("; ");
                    storage.append(t.replaceAll("\\s+", " "));
                }
            }
            row(sb, "storage", storage.toString());
        } else {
            String model = "";
            File cpuinfo = new File("/proc/cpuinfo");
            if (cpuinfo.exists()) {
                for (String l : Files.readAllLines(cpuinfo.toPath())) {
                    if (l.startsWith("model name")) {
                        model = l.substring(l.indexOf(':') + 1).trim();
                        break;
                    }
                }
            }
            row(sb, "CPU", model);
            row(sb, "cores", Runtime.getRuntime().availableProcessors() + " available to the JVM");
            File meminfo = new File("/proc/meminfo");
            if (meminfo.exists()) row(sb, "RAM", Files.readAllLines(meminfo.toPath()).get(0).replaceAll("\\s+", " "));
            row(sb, "storage", out("df", "-h", s.home.getAbsolutePath()).replace("\n", " / "));
        }
        row(sb, "Docker", out("docker", "info", "--format",
                "server {{.ServerVersion}}, VM {{.OperatingSystem}} kernel {{.KernelVersion}}, {{.NCPU}} CPUs, {{.MemTotal}} bytes RAM, cgroup v{{.CgroupVersion}}, storage {{.Driver}}"));
        row(sb, "PostgreSQL storage", "named Docker volume " + Settings.VOLUME + " inside the Docker VM (not a bind mount): pg_test_fsync measures the VM's disk");
        row(sb, "pg_test_fsync", Store.fdatasyncFigure(s) + " (full output: results/" + Store.fsyncFile(s).getName() + ")");
        try (Connection c = Store.connect(Guard.DB_NAME)) {
            row(sb, "PostgreSQL", Store.version(c));
            row(sb, "PostgreSQL settings (non-default)", Store.nonDefaultSettings(c));
        }
        String commit = out("git", "-C", s.home.getAbsolutePath(), "rev-parse", "HEAD");
        String dirty = out("git", "-C", s.home.getAbsolutePath(), "status", "--porcelain", "--untracked-files=no").isBlank() ? "" : " (+ uncommitted changes)";
        row(sb, "ProvToolbox", out("git", "-C", s.home.getAbsolutePath(), "rev-parse", "--abbrev-ref", "HEAD") + " " + commit + dirty);
        row(sb, "Java (service and harness)", out(s.get("JAVA"), "-version").replace("\n", " / "));
        row(sb, "service JVM flags", s.get("SERVICE_JVM_FLAGS") + "; Jetty max threads " + s.get("JETTY_MAX_THREADS") + " (embedded Jetty 11, ServiceLauncher)");
        row(sb, "template library", "provenance-templates-library 0.1.0-SNAPSHOT; composite size " + s.get("COMPOSITE_SIZE"));
        Files.writeString(environment().toPath(), sb.toString(), StandardCharsets.UTF_8);
    }

    private static void row(StringBuilder sb, String k, String v) {
        sb.append("| ").append(k).append(" | ").append(v.replace("|", "\\|").trim()).append(" |\n");
    }

    private static String gib(String bytes) {
        try {
            return String.format("%.0f GiB", Long.parseLong(bytes.trim()) / 1073741824.0);
        } catch (NumberFormatException e) {
            return bytes;
        }
    }

    private static String out(String... cmd) {
        Shell.Result r = Shell.run(60, List.of(cmd));
        StringBuilder sb = new StringBuilder();
        for (String line : r.out.split("\n")) {
            if (line.startsWith("Picked up ")) continue;   // the JVM echoing JAVA_TOOL_OPTIONS
            if (sb.length() > 0) sb.append('\n');
            sb.append(line);
        }
        return sb.toString().trim();
    }

    static final String[][] COLUMNS = {
            {"run", "run"}, {"family", "fam"}, {"variant", "variant"}, {"n", "N"}, {"b", "B"}, {"pool", "pool"}, {"key", "key"}, {"writers", "writers"},
            {"msg_per_s", "msg/s"}, {"records_per_s", "records/s"}, {"rows_per_record", "rows/rec"}, {"wal_bytes_per_record", "WAL B/rec"},
            {"lat_p50_ms", "p50 ms"}, {"lat_p95_ms", "p95 ms"}, {"lat_p99_ms", "p99 ms"}, {"lat_max_ms", "max ms"},
            {"svc_cpu_pct", "svc CPU %"}, {"pg_cpu_pct", "PG CPU %"}, {"svc_cpu_ms_per_record", "svc ms/rec"}, {"pg_cpu_ms_per_record", "PG ms/rec"},
            {"lag_p50_ms", "lag p50 ms"}, {"lag_p95_ms", "lag p95 ms"}, {"drain_s", "drain s"}, {"startup_s", "start s"}, {"status", "status"}};

    /** Rewrites the Markdown report from the environment file and the CSV. */
    void writeMarkdown(String title, String plan) throws IOException {
        List<Map<String, String>> rows = readCsv(csv());
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(title).append("\n\n");
        sb.append("Generated by `make bench.matrix` / `make bench.run` (modules-misc/benchmarks/service-throughput); see BENCHMARK.md for what each variant switches off.\n\n");
        long failed = rows.stream().filter(r -> !"ok".equals(r.get("status"))).count();
        if (failed > 0) {
            sb.append("**").append(failed).append(" run(s) FAILED** — their figures are not results:\n\n");
            for (Map<String, String> r : rows) {
                if (!"ok".equals(r.get("status"))) sb.append("- ").append(r.get("run")).append(": ").append(r.get("problems")).append('\n');
            }
            sb.append('\n');
        }
        sb.append("## Machine and software\n\n");
        sb.append(environment().exists() ? Files.readString(environment().toPath()) : "(not recorded)\n");
        if (plan != null) sb.append("\n## Plan\n\n").append(plan).append('\n');
        sb.append("\n## Runs\n\n");
        sb.append("Rates are per measured second; CPU % is of one core (100 = one core busy); rows = n_tup_ins + n_tup_upd over all tables; ")
                .append("lag = from a record's response to its first relation row being visible (probed); drain = relation queue drain after the window.\n\n");
        StringBuilder head = new StringBuilder("|");
        StringBuilder rule = new StringBuilder("|");
        for (String[] c : COLUMNS) {
            head.append(' ').append(c[1]).append(" |");
            rule.append(c[0].equals("run") || c[0].equals("family") || c[0].equals("variant") || c[0].equals("key") || c[0].equals("status") ? "---|" : "---:|");
        }
        sb.append(head).append('\n').append(rule).append('\n');
        for (Map<String, String> r : rows) {
            sb.append('|');
            for (String[] c : COLUMNS) sb.append(' ').append(r.getOrDefault(c[0], "")).append(" |");
            sb.append('\n');
        }
        sb.append("\nTop statements of each run (pg_stat_statements, by total time): ").append(statements().getName()).append('\n');
        Files.writeString(markdown().toPath(), sb.toString(), StandardCharsets.UTF_8);
    }
}
