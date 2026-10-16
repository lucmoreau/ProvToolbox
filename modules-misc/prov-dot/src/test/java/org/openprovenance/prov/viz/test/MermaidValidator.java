package org.openprovenance.prov.viz.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates mermaid files with {@code mmdc}. Launching a browser per file is slow, so all files go
 * into one markdown document rendered in a single run; only when that run fails are the files
 * rendered one by one to name the culprits.
 */
public class MermaidValidator {

    public static final String MMDC = System.getenv().getOrDefault("MMDC", "mmdc");

    public static boolean available() {
        try {
            Process p = new ProcessBuilder(MMDC, "--version").redirectErrorStream(true).start();
            p.getInputStream().readAllBytes();
            return p.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    static String run(List<String> command) throws IOException, InterruptedException {
        Process p = new ProcessBuilder(command).redirectErrorStream(true).start();
        String output = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int code = p.waitFor();
        return (code == 0) ? null : "exit " + code + "\n" + output;
    }

    /** Renders one file to svg beside it; null when fine, the tool's output otherwise. */
    public static String render(Path mmd) throws IOException, InterruptedException {
        Path svg = Path.of(mmd.toString().replaceAll("\\.mmd$", "") + ".svg");
        return run(List.of(MMDC, "-q", "-i", mmd.toString(), "-o", svg.toString()));
    }

    /** How many diagrams one mmdc run takes: it keeps every rendered svg in memory, and runs out of heap in the hundreds. */
    public static final int CHUNK = 40;

    /** All files, a chunk per mmdc run; the offending files, each with mmdc's complaint, when any fails. */
    public static List<String> validateAll(List<Path> files, Path workDir) throws IOException, InterruptedException {
        List<String> failures = new ArrayList<>();
        Files.createDirectories(workDir);
        for (int start = 0, chunk = 0; start < files.size(); start += CHUNK, chunk++) {
            List<Path> part = files.subList(start, Math.min(files.size(), start + CHUNK));
            failures.addAll(validateChunk(part, workDir, chunk));
        }
        return failures;
    }

    static List<String> validateChunk(List<Path> files, Path workDir, int chunk) throws IOException, InterruptedException {
        List<String> failures = new ArrayList<>();
        StringBuilder md = new StringBuilder();
        for (Path f : files) {
            md.append("## ").append(f.getFileName()).append("\n\n```mermaid\n");
            md.append(Files.readString(f, StandardCharsets.UTF_8));
            md.append("\n```\n\n");
        }
        Path all = workDir.resolve("all-diagrams-" + chunk + ".md");
        Files.writeString(all, md.toString(), StandardCharsets.UTF_8);
        String problem = run(List.of(MMDC, "-q", "-i", all.toString(), "-o", workDir.resolve("all-diagrams-" + chunk + ".out.md").toString()));
        if (problem == null) return failures;
        for (Path f : files) {
            String one = render(f);
            if (one != null) failures.add(f + ": " + firstLines(one, 6));
        }
        if (failures.isEmpty()) failures.add("chunk " + chunk + " failed but no single file does: " + firstLines(problem, 12));
        return failures;
    }

    static String firstLines(String s, int n) {
        String[] lines = s.split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(n, lines.length); i++) sb.append(lines[i]).append("\n");
        return sb.toString();
    }

    public static void deleteQuietly(File f) {
        //noinspection ResultOfMethodCallIgnored
        f.delete();
    }
}
