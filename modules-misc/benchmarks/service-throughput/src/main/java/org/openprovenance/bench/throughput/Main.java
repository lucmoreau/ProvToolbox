package org.openprovenance.bench.throughput;

import java.util.Arrays;
import java.util.List;

/**
 * The benchmark's commands, as the Makefile calls them: {@code java ... Main <command> [KEY=value ...]}.
 *
 * <pre>
 *   guard          the allow-list (Guard), against the running store and the fixed URLs
 *   db-up          the Docker PostgreSQL (created on first use; pg_test_fsync recorded once)
 *   db-down        stopped, its volume kept
 *   db-destroy     container and volume removed
 *   db-reset       provbench dropped and recreated (the service down)
 *   service-up     the service with FAMILY, VARIANT, POOL (left running)
 *   service-down   the service stopped
 *   run            one run: FAMILY VARIANT N B POOL WARMUP DURATION [WRITERS QUEUE BATCH]
 *   matrix         the plan (PLAN=full|smoke), each run appended to results/&lt;date&gt;[-LABEL].csv/.md
 *   plan           the plan and its time budget, nothing run
 * </pre>
 * Every command that writes runs the guard first and stops on a refusal (exit status 3).
 */
public final class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("usage: Main guard|db-up|db-down|db-destroy|db-reset|service-up|service-down|run|matrix|plan [KEY=value ...]");
            System.exit(2);
        }
        Settings s = new Settings(Arrays.copyOfRange(args, 1, args.length));
        try {
            int status = command(args[0], s);
            System.exit(status);
        } catch (Guard.Refused e) {
            System.err.println(e.getMessage());
            System.exit(3);
        }
    }

    static int command(String command, Settings s) throws Exception {
        Store store = new Store(s);
        Service service = new Service(s);
        switch (command) {
            case "guard":
                String container = store.guard();
                System.out.println("GUARD ok: " + Settings.JDBC_URL + " is the labelled container " + container
                        + ", its cluster holds only " + Guard.ALLOWED_DATABASES + "; service " + Settings.SERVICE_URL);
                return 0;
            case "db-up":
                store.up();
                return 0;
            case "db-down":
                service.stop();
                store.down();
                return 0;
            case "db-destroy":
                service.stop();
                store.destroy();
                return 0;
            case "db-reset":
                if (Store.portInUse(Guard.SERVICE_PORT)) throw new Guard.Refused("the service is up: make bench.service.down first");
                store.reset();
                System.out.println("provbench dropped and recreated");
                return 0;
            case "service-up":
                store.guard();
                store.apply(s.variant().preStart);
                double t = service.start();
                store.apply(s.variant().ddl);
                System.out.printf("service up in %.1f s: %s  (%s)%n%s%n", t, service.base(), s, service.writePathLine());
                return 0;
            case "service-down":
                System.out.printf("service down in %.1f s%n", service.stop());
                return 0;
            case "run": {
                Report report = new Report(s);
                store.guard();
                report.recordEnvironment();
                Run.Row row = new Run(s).execute("r" + System.currentTimeMillis() / 1000);
                appendAligned(report, row);
                report.writeMarkdown("Service throughput " + report.name, null);
                System.out.println("results: " + report.csv() + ", " + report.markdown());
                return row.ok() ? 0 : 1;
            }
            case "plan": {
                Report report = new Report(s);
                List<Matrix.Step> steps = Matrix.plan(s);
                System.out.println(Matrix.describe(steps, s, Matrix.startupEstimate(report)));
                return 0;
            }
            case "matrix":
                return matrix(s);
            default:
                throw new IllegalArgumentException("unknown command " + command);
        }
    }

    static int matrix(Settings s) throws Exception {
        Report report = new Report(s);
        Store store = new Store(s);
        store.guard();
        List<Matrix.Step> steps = Matrix.plan(s);
        String plan = Matrix.describe(steps, s, Matrix.startupEstimate(report));
        System.out.println(plan);
        report.recordEnvironment();
        long t0 = System.nanoTime();
        int failed = 0;
        int i = 0;
        String stamp = Long.toString(System.currentTimeMillis() / 1000);
        for (Matrix.Step step : steps) {
            i++;
            Run.Row row;
            try {
                row = new Run(step.settings).execute(String.format("m%s-%02d", stamp, i));
            } catch (Guard.Refused e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                row = new Run.Row();
                row.put("run", String.format("m%s-%02d", stamp, i));
                row.put("family", step.settings.get("FAMILY"));
                row.put("variant", step.settings.get("VARIANT"));
                row.put("n", step.settings.get("N"));
                row.put("b", step.settings.get("B"));
                row.put("pool", step.settings.get("POOL"));
                row.problems.add("aborted: " + e);
                row.put("status", "FAIL");
                row.put("problems", "aborted: " + e.toString().replace('\n', ' '));
                try {
                    new Service(step.settings).stop();
                } catch (Exception ignored) {
                    // reported above
                }
            }
            if (!row.ok()) failed++;
            appendAligned(report, row);
            report.writeMarkdown("Service throughput " + report.name, plan);
            System.out.printf("  [%d/%d done, %.1f min elapsed, %d failed]%n", i, steps.size(), (System.nanoTime() - t0) / 60e9, failed);
            if (!row.ok() && "1".equals(s.get("STOP_ON_FAIL"))) break;
        }
        System.out.printf("%nmatrix: %d run(s) in %.1f min, %d failed; %s%n", i, (System.nanoTime() - t0) / 60e9, failed, report.markdown());
        return failed == 0 ? 0 : 1;
    }

    /** An aborted run's row carries fewer columns: padded to the CSV's header so the columns stay aligned. */
    private static void appendAligned(Report report, Run.Row row) throws java.io.IOException {
        List<java.util.Map<String, String>> existing = Report.readCsv(report.csv());
        if (!existing.isEmpty() && !existing.get(0).keySet().equals(row.values.keySet())) {
            Run.Row aligned = new Run.Row();
            for (String k : existing.get(0).keySet()) aligned.put(k, row.values.getOrDefault(k, ""));
            aligned.problems.addAll(row.problems);
            aligned.topStatements.addAll(row.topStatements);
            row = aligned;
        }
        report.append(row);
    }
}
