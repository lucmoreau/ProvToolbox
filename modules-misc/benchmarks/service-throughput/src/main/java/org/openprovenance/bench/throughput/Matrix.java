package org.openprovenance.bench.throughput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The plan of {@code make bench.matrix}: baseline; each variant alone; all combined, sweeping N, the pool size and B;
 * the transport family; the relation writers. {@code PLAN=smoke} is the short check this module was proved with.
 */
final class Matrix {

    /** A run of the plan: its phase and its settings. */
    static final class Step {
        final String phase;
        final Settings settings;

        Step(String phase, Settings settings) {
            this.phase = phase;
            this.settings = settings;
        }
    }

    /** Seconds a run costs besides its warm-up and measured window: stop, reset, settles, collection (start-up apart). */
    static final double OVERHEAD_S = 12;

    static List<Step> plan(Settings base) {
        String name = base.get("PLAN");
        List<Step> steps = new ArrayList<>();
        switch (name) {
            case "smoke":
                for (String family : new String[]{"fs", "tr"}) {
                    for (String variant : new String[]{"baseline", "async"}) {
                        steps.add(new Step("smoke", base.with("FAMILY", family, "VARIANT", variant, "N", "2", "B", "1", "POOL", "4")));
                    }
                }
                return steps;
            case "full":
                break;
            default:
                throw new IllegalArgumentException("PLAN is full or smoke, not " + name);
        }
        // A. each variant alone, against the baseline: one client, one record per message, no pool (as most deployments)
        for (String v : Variant.ALL.keySet()) {
            steps.add(new Step("A variants, N=1 B=1 POOL=0", base.with("FAMILY", "fs", "VARIANT", v, "N", "1", "B", "1", "POOL", "0")));
        }
        // B. the variants that matter under concurrency: 8 clients on a pool of 8
        for (String v : new String[]{"baseline", "noacl", "nohash", "async", "nokey", "nojit", "combined"}) {
            steps.add(new Step("B variants, N=8 B=1 POOL=8", base.with("FAMILY", "fs", "VARIANT", v, "N", "8", "B", "1", "POOL", "8")));
        }
        // C. sweeping the clients, pool of 16
        for (String v : new String[]{"baseline", "combined"}) {
            for (int n : new int[]{1, 2, 4, 8, 16, 32}) {
                steps.add(new Step("C clients, POOL=16", base.with("FAMILY", "fs", "VARIANT", v, "N", Integer.toString(n), "B", "1", "POOL", "16")));
            }
        }
        // D. sweeping the pool, 16 clients (POOL=16 is in C)
        for (String v : new String[]{"baseline", "combined"}) {
            for (int pool : new int[]{0, 4, 8, 32}) {
                steps.add(new Step("D pool size, N=16", base.with("FAMILY", "fs", "VARIANT", v, "N", "16", "B", "1", "POOL", Integer.toString(pool))));
            }
        }
        // E. records per message (keyless by construction), 8 clients, pool of 8
        for (String v : new String[]{"baseline", "combined"}) {
            for (int b : new int[]{10, 100}) {
                steps.add(new Step("E records per message, N=8 POOL=8", base.with("FAMILY", "fs", "VARIANT", v, "N", "8", "B", Integer.toString(b), "POOL", "8")));
            }
        }
        // F. the transport family
        steps.add(new Step("F transport family", base.with("FAMILY", "tr", "VARIANT", "baseline", "N", "1", "B", "1", "POOL", "0")));
        steps.add(new Step("F transport family", base.with("FAMILY", "tr", "VARIANT", "combined", "N", "1", "B", "1", "POOL", "0")));
        steps.add(new Step("F transport family", base.with("FAMILY", "tr", "VARIANT", "baseline", "N", "16", "B", "1", "POOL", "16")));
        steps.add(new Step("F transport family", base.with("FAMILY", "tr", "VARIANT", "combined", "N", "16", "B", "1", "POOL", "16")));
        // G. the relation writers (2 in C)
        for (String w : new String[]{"1", "4"}) {
            steps.add(new Step("G relation writers, N=16 POOL=16", base.with("FAMILY", "fs", "VARIANT", "combined", "N", "16", "B", "1", "POOL", "16", "WRITERS", w)));
        }
        return steps;
    }

    /** The plan as Markdown, with its time budget at the given start-up time. */
    static String describe(List<Step> steps, Settings base, double startupSeconds) {
        StringBuilder sb = new StringBuilder();
        String phase = "";
        int i = 0;
        double total = 0;
        for (Step st : steps) {
            if (!st.phase.equals(phase)) {
                phase = st.phase;
                sb.append("\n**").append(phase).append("**\n\n");
            }
            i++;
            sb.append(i).append(". ").append(st.settings).append('\n');
            total += st.settings.integer("WARMUP") + st.settings.integer("DURATION") + OVERHEAD_S + startupSeconds;
        }
        int warm = base.integer("WARMUP");
        int dur = base.integer("DURATION");
        sb.append(String.format("%n%d runs x (warm-up %d s + measure %d s + start-up ~%.0f s + stop/reset/settle/collect ~%.0f s) = ~%.0f s = ~%.1f min%n",
                steps.size(), warm, dur, startupSeconds, OVERHEAD_S, total, total / 60));
        return sb.toString();
    }

    /** The start-up time to budget with: the mean of the report's runs, else 10 s (3.3-4.1 s measured in the cloud smoke run). */
    static double startupEstimate(Report report) {
        try {
            List<Map<String, String>> rows = Report.readCsv(report.csv());
            double sum = 0;
            int n = 0;
            for (Map<String, String> r : rows) {
                try {
                    sum += Double.parseDouble(r.get("startup_s"));
                    n++;
                } catch (NumberFormatException | NullPointerException e) {
                    // none
                }
            }
            return n == 0 ? 10 : sum / n;
        } catch (java.io.IOException e) {
            return 10;
        }
    }
}
