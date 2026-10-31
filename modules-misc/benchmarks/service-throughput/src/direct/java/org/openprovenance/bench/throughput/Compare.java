package org.openprovenance.bench.throughput;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Direct against HTTP, from the results files already there: each direct run beside the HTTP run doing the same work
 * through the service. The pairs: direct {@code post+key} (key, access-controlled read-back, hash, inline relations) and
 * HTTP {@code baseline} with its key (B=1); direct {@code post} and HTTP {@code nokey}, or {@code baseline} at B&gt;1
 * (keyless by construction); direct {@code combined} and HTTP {@code combined};
 * same family, same N, same B (records per transaction against records per message), and the HTTP run's pool at
 * least N (each direct writer has its own connection; at POOL=0 the service shares one, which is another store). When
 * several HTTP runs match, the last one read wins. By default the HTTP files are those of this machine (their
 * environment file names the direct report's CPU); {@code HTTP_RESULTS} names them instead (e.g. the JIT-fix runs only).
 */
final class Compare {

    static String directPreset(Map<String, String> r) {
        if (!"direct".equals(r.get("mode"))) return null;
        if ("insert".equals(r.get("layer"))) return null;
        boolean key = "on".equals(r.get("key"));
        if ("on".equals(r.get("readback")) && "update".equals(r.get("hash")) && "inline".equals(r.get("relations"))) return key ? "post+key" : "post";
        if (!key && "off".equals(r.get("readback")) && "off".equals(r.get("hash")) && "async".equals(r.get("relations"))) return "combined";
        return null;
    }

    /**
     * The direct preset doing an HTTP run's work: baseline with its key (B=1) is post+key; baseline without one (B&gt;1,
     * keyless by construction) and nokey are post; combined is combined; any other variant has no direct counterpart.
     */
    static String presetOfHttp(Map<String, String> r) {
        String v = r.get("variant");
        if ("combined".equals(v)) return "combined";
        if ("baseline".equals(v) || "nokey".equals(v)) return "yes".equals(r.get("key")) ? "post+key" : "post";
        return null;
    }

    /** The CPU row of a report's environment file, or null. */
    static String cpuOf(File environment) throws IOException {
        if (!environment.exists()) return null;
        for (String line : Files.readAllLines(environment.toPath(), StandardCharsets.UTF_8)) {
            if (line.startsWith("| CPU |")) return line.trim();
        }
        return null;
    }

    /**
     * The JSONL mode beside the others, at the same family, N and B: the records' bindings appended to local files with
     * a force per batch (FSYNC=batch, a file per writer), the direct insert layer, direct post+key and combined, HTTP
     * baseline and combined (records/s). JSONL reports of this machine only (same CPU in their environment file).
     */
    static void threeWays(Settings s, File results, List<Map<String, String>> direct, Map<String, Map<String, String>> http, StringBuilder sb) throws IOException {
        String cpu = cpuOf(new File(results, new Report(s, "direct").name + "-environment.md"));
        File[] all = results.listFiles((d, n) -> n.endsWith(".csv") && n.contains("-jsonl"));
        if (all == null || all.length == 0) return;
        Arrays.sort(all);
        Map<String, Map<String, String>> jsonl = new LinkedHashMap<>();
        for (File f : all) {
            String base = f.getName().substring(0, f.getName().length() - ".csv".length());
            if (cpu == null || !cpu.equals(cpuOf(new File(results, base + "-environment.md")))) continue;
            for (Map<String, String> r : Report.readCsv(f)) {
                if ("ok".equals(r.get("status")) && "batch".equals(r.get("fsync")) && "writer".equals(r.get("files"))) {
                    jsonl.put(r.get("family") + "|" + r.get("n") + "|" + r.get("b"), r);
                }
            }
        }
        if (jsonl.isEmpty()) return;
        Map<String, Map<String, String>> directBy = new LinkedHashMap<>();
        for (Map<String, String> d : direct) {
            if (!"ok".equals(d.get("status")) || !"0".equals(d.get("jdbc_batch"))) continue;
            String preset = "insert".equals(d.get("layer")) ? ("on".equals(d.get("key")) ? "insert+key" : "insert") : directPreset(d);
            if (preset != null) directBy.put(d.get("family") + "|" + preset + "|" + d.get("n") + "|" + d.get("b"), d);
        }
        sb.append("\n## Three ways to write the same records\n\nrecords/s at the same family, N and B: JSONL (the bindings appended to local files, ")
                .append("a force per batch, a file per writer), direct (no web service), HTTP (the service). '' = not run.\n\n");
        sb.append("| fam | N | B | JSONL | direct insert | direct post+key | direct combined | HTTP baseline | HTTP combined | JSONL B/rec |\n");
        sb.append("|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|\n");
        for (Map<String, String> j : jsonl.values()) {
            String f = j.get("family");
            String n = j.get("n");
            String b = j.get("b");
            sb.append("| ").append(f).append(" | ").append(n).append(" | ").append(b).append(" | ").append(j.get("records_per_s"))
                    .append(" | ").append(rate(directBy.get(f + "|insert|" + n + "|" + b)))
                    .append(" | ").append(rate(directBy.get(f + "|post+key|" + n + "|" + b)))
                    .append(" | ").append(rate(directBy.get(f + "|combined|" + n + "|" + b)))
                    .append(" | ").append(rate(http.get(f + "|post+key|" + n + "|" + b)))
                    .append(" | ").append(rate(http.get(f + "|combined|" + n + "|" + b)))
                    .append(" | ").append(j.get("bytes_per_record")).append(" |\n");
        }
    }

    private static String rate(Map<String, String> r) {
        return r == null ? "" : r.get("records_per_s");
    }

    static int run(Settings s) throws IOException {
        File results = s.resultsDir();
        List<File> http = new ArrayList<>();
        String named = s.get("HTTP_RESULTS");
        if (named != null && !named.isBlank()) {
            for (String n : named.split(",")) {
                File f = new File(n.trim());
                if (!f.isAbsolute()) f = new File(s.home, n.trim());
                if (!f.exists()) throw new IllegalArgumentException("no HTTP results " + f);
                http.add(f);
            }
        } else {
            // every HTTP report of this machine (its environment file names the same CPU): never another machine's
            String cpu = cpuOf(new File(results, new Report(s, "direct").name + "-environment.md"));
            File[] all = results.listFiles((d, n) -> n.endsWith(".csv") && !n.contains("-direct") && !n.contains("-compare") && !n.contains("-jsonl"));
            if (all != null) {
                Arrays.sort(all);
                for (File f : all) {
                    String base = f.getName().substring(0, f.getName().length() - ".csv".length());
                    String other = cpuOf(new File(results, base + "-environment.md"));
                    if (cpu != null && cpu.equals(other)) http.add(f);
                }
            }
        }
        Map<String, Map<String, String>> byKey = new LinkedHashMap<>();
        for (File f : http) {
            for (Map<String, String> r : Report.readCsv(f)) {
                if (!"ok".equals(r.get("status"))) continue;
                int n = Integer.parseInt(r.get("n"));
                int pool = Integer.parseInt(r.get("pool"));
                if (pool < n) continue;
                String preset = presetOfHttp(r);
                if (preset == null) continue;
                r.put("source", f.getName());
                byKey.put(r.get("family") + "|" + preset + "|" + n + "|" + r.get("b"), r);
            }
        }
        Report report = new Report(s, "direct");
        List<Map<String, String>> direct = Report.readCsv(report.csv());
        StringBuilder sb = new StringBuilder();
        sb.append("# Direct JDBC mode against HTTP, ").append(report.name).append("\n\n");
        sb.append("Direct: ").append(report.csv().getName()).append(". HTTP: ");
        for (File f : http) sb.append(f.getName()).append(' ');
        sb.append("\n\nPairs: direct post+key ~ HTTP baseline with its key (B=1), direct post ~ HTTP nokey or keyless baseline (B>1), direct combined ~ HTTP combined; same family, N and B; ")
                .append("HTTP runs with POOL >= N only. The service's share = 1 - HTTP/direct records/s.\n\n");
        sb.append("| fam | direct | HTTP | N | B | direct records/s | HTTP records/s | HTTP/direct | direct rows/rec | HTTP rows/rec | direct PG ms/rec | HTTP PG ms/rec | direct p50 ms/tx | HTTP p50 ms/msg | HTTP svc ms/rec | direct client ms/rec | HTTP source |\n");
        sb.append("|---|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|\n");
        int pairs = 0;
        for (Map<String, String> d : direct) {
            if (!"ok".equals(d.get("status"))) continue;
            String preset = directPreset(d);
            if (preset == null || !"0".equals(d.get("jdbc_batch"))) continue;
            Map<String, String> h = byKey.get(d.get("family") + "|" + preset + "|" + d.get("n") + "|" + d.get("b"));
            if (h == null) continue;
            pairs++;
            double dr = Double.parseDouble(d.get("records_per_s"));
            double hr = Double.parseDouble(h.get("records_per_s"));
            sb.append("| ").append(d.get("family")).append(" | ").append(preset).append(" | ").append(h.get("variant"))
                    .append(" | ").append(d.get("n")).append(" | ").append(d.get("b"))
                    .append(" | ").append(d.get("records_per_s")).append(" | ").append(h.get("records_per_s"))
                    .append(" | ").append(String.format("%.2f", hr / dr))
                    .append(" | ").append(d.get("rows_per_record")).append(" | ").append(h.get("rows_per_record"))
                    .append(" | ").append(d.get("pg_cpu_ms_per_record")).append(" | ").append(h.get("pg_cpu_ms_per_record"))
                    .append(" | ").append(d.get("lat_p50_ms")).append(" | ").append(h.get("lat_p50_ms"))
                    .append(" | ").append(h.get("svc_cpu_ms_per_record")).append(" | ").append(d.get("client_cpu_ms_per_record"))
                    .append(" | ").append(h.get("source")).append(" |\n");
        }
        if (pairs == 0) sb.append("\n(no direct run has an HTTP counterpart in these files)\n");
        threeWays(s, results, direct, byKey, sb);
        File out = new File(results, report.name + "-compare.md");
        Files.writeString(out.toPath(), sb.toString(), StandardCharsets.UTF_8);
        System.out.println(sb);
        System.out.println("written: " + out);
        return 0;
    }
}
