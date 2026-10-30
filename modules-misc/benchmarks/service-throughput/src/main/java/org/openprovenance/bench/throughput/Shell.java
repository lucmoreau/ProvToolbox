package org.openprovenance.bench.throughput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Runs a command (no shell: the arguments as given), its output captured. */
final class Shell {

    static final class Result {
        final int exit;
        final String out;

        Result(int exit, String out) {
            this.exit = exit;
            this.out = out;
        }

        boolean ok() {
            return exit == 0;
        }
    }

    private Shell() {
    }

    static Result run(long timeoutSeconds, List<String> command) {
        try {
            Process p = new ProcessBuilder(command).redirectErrorStream(true).start();
            p.getOutputStream().close();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thread reader = new Thread(() -> copy(p.getInputStream(), out));
            reader.start();
            if (!p.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
                p.destroyForcibly();
                reader.join(1000);
                return new Result(-1, "timed out after " + timeoutSeconds + " s: " + command + "\n" + out.toString(StandardCharsets.UTF_8));
            }
            reader.join(5000);
            return new Result(p.exitValue(), out.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            return new Result(-1, "cannot run " + command + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Result(-1, "interrupted: " + command);
        }
    }

    static Result run(List<String> command) {
        return run(120, command);
    }

    /** The command's output, trimmed; fails when it fails. */
    static String check(long timeoutSeconds, List<String> command) {
        Result r = run(timeoutSeconds, command);
        if (!r.ok()) throw new IllegalStateException("failed (" + r.exit + "): " + String.join(" ", command) + "\n" + r.out);
        return r.out.trim();
    }

    private static void copy(InputStream in, ByteArrayOutputStream out) {
        byte[] buf = new byte[8192];
        try (in) {
            int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
        } catch (IOException ignored) {
            // the process ended
        }
    }
}
