package org.openprovenance.prov.configuration;

import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * A process that gives up says so in one line, on standard error:
 * <pre>FATAL service=&lt;s&gt; at=&lt;instant&gt; reason="…" cause="&lt;class&gt;: &lt;message&gt;"</pre>
 * always one line, its values quoted, a quote turned into an apostrophe, so that a log watcher (a CloudWatch metric
 * filter, a cron job reading the container's log) finds it by its prefix. The same line as the provmind services'
 * (nextgen's com.provmind.resilience.Fatal, T-548).
 */
public final class Fatal {
    public static final String PREFIX = "FATAL ";
    static final int MAX_CAUSE = 300;

    private Fatal() {
    }

    /** The line, without printing it. */
    public static String line(String service, String reason, Throwable cause, Instant at) {
        StringBuilder sb = new StringBuilder(PREFIX)
                .append("service=").append(token(service))
                .append(" at=").append(at.truncatedTo(ChronoUnit.SECONDS))
                .append(" reason=\"").append(clean(reason)).append('"');
        if (cause != null) {
            String c = cause.getClass().getSimpleName() + (cause.getMessage() == null ? "" : ": " + cause.getMessage());
            c = clean(c);
            sb.append(" cause=\"").append(c.length() > MAX_CAUSE ? c.substring(0, MAX_CAUSE) + "…" : c).append('"');
        }
        return sb.toString();
    }

    /** Prints the line on standard error. */
    public static void report(String service, String reason, Throwable cause) {
        report(System.err, service, reason, cause);
    }

    static void report(PrintStream out, String service, String reason, Throwable cause) {
        out.println(line(service, reason, cause, Instant.now()));
        out.flush();
    }

    /** Prints the line and its cause's stack trace, then ends the process (with status 1 when given 0). */
    public static void exit(String service, String reason, Throwable cause, int status) {
        report(service, reason, cause);
        if (cause != null) cause.printStackTrace();
        System.exit(status == 0 ? 1 : status);
    }

    private static String token(String s) {
        String t = s == null ? "" : s.trim().replaceAll("\\s+", "-");
        return t.isEmpty() ? "unknown" : t;
    }

    private static String clean(String s) {
        return s == null ? "" : s.replace('"', '\'').replaceAll("[\\r\\n\\t]", " ");
    }
}
