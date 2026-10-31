package org.openprovenance.bench.throughput;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** The allow-list: the benchmark's own store and service, nothing else. */
public class GuardTest {

    private static void refused(Runnable r, String why) {
        try {
            r.run();
            fail("accepted: " + why);
        } catch (Guard.Refused e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith("GUARD: refused"));
        }
    }

    @Test
    public void theBenchmarksStoreIsAccepted() {
        assertEquals("jdbc:postgresql://localhost:55432/provbench", Guard.jdbcUrl("jdbc:postgresql://localhost:55432/provbench"));
        Guard.jdbcUrl("jdbc:postgresql://127.0.0.1:55432/provbench?sslmode=disable");
    }

    @Test
    public void anyOtherStoreIsRefused() {
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost:5432/provbench"), "the deployed cluster's port");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost:5432/bookptm"), "bookptm");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost:55432/bookptm"), "bookptm, even on the benchmark's port");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost:55432/templates"), "templates");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost:55432/chronicle_ledger"), "a chronicle store");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost/provbench"), "no port: the default 5432");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://db.example.org:55432/provbench"), "a remote host");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost:55432,localhost:5432/provbench"), "several hosts");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost:55432/provbench?PGPORT=5432"), "a port in the parameters");
        refused(() -> Guard.jdbcUrl("jdbc:postgresql://localhost:55432/provbenchx"), "a prefix match");
        refused(() -> Guard.jdbcUrl("jdbc:mysql://localhost:55432/provbench"), "another driver");
        refused(() -> Guard.jdbcUrl(null), "nothing");
    }

    @Test
    public void theBenchmarksServiceIsAccepted() {
        assertEquals("http://localhost:7099/provbench", Guard.serviceUrl("http://localhost:7099/provbench").toString());
        assertEquals("http://127.0.0.1:7099/provbench", Guard.serviceUrl("http://127.0.0.1:7099/provbench/").toString());
    }

    @Test
    public void anyOtherServiceIsRefused() {
        refused(() -> Guard.serviceUrl("http://localhost:7075/provbench"), "port 7075, a deployed store");
        refused(() -> Guard.serviceUrl("http://localhost:7075/book"), "the book service");
        refused(() -> Guard.serviceUrl("http://localhost:9088/provbench"), "port 9088");
        refused(() -> Guard.serviceUrl("http://localhost:7099/book"), "a foreign context on the benchmark's port");
        refused(() -> Guard.serviceUrl("http://localhost:7099/"), "no context");
        refused(() -> Guard.serviceUrl("http://localhost:7099/provbench/provapi"), "more than the context");
        refused(() -> Guard.serviceUrl("http://example.org:7099/provbench"), "a remote host");
        refused(() -> Guard.serviceUrl("https://localhost:7099/provbench"), "another scheme");
    }

    @Test
    public void theLabelledContainerPublishingTheBenchmarksPort() {
        assertEquals("abc123", Guard.container(List.of("abc123\t127.0.0.1:55432->5432/tcp")));
        assertEquals("abc123", Guard.container(List.of("abc123\t0.0.0.0:55432->5432/tcp, :::55432->5432/tcp", "")));
        refused(() -> Guard.container(List.of()), "no labelled container");
        refused(() -> Guard.container(List.of("abc123\t0.0.0.0:5432->5432/tcp")), "a container on 5432");
        refused(() -> Guard.container(List.of("abc123\t")), "a container publishing nothing");
        refused(() -> Guard.container(List.of("abc\t127.0.0.1:55432->5432/tcp", "def\t127.0.0.1:55433->5432/tcp")), "two labelled containers");
    }

    @Test
    public void theServerOverJdbcIsTheContainersCluster() {
        Guard.sameCluster("7431118652478613533", "7431118652478613533\n");
        refused(() -> Guard.sameCluster("7431118652478613533", "7000000000000000000"), "another cluster answering on the port");
        refused(() -> Guard.sameCluster(null, "7431118652478613533"), "no answer");
    }

    @Test
    public void theClusterHoldsNothingButTheBenchmark() {
        Guard.databases(List.of("provbench", "postgres", "template0", "template1"));
        refused(() -> Guard.databases(List.of("provbench", "postgres", "template0", "template1", "bookptm")), "bookptm in the cluster");
        refused(() -> Guard.databases(List.of("provbench", "postgres", "templates")), "templates in the cluster");
        refused(() -> Guard.databases(List.of("postgres", "template0", "template1")), "no provbench");
    }

    @Test
    public void theJsonlDirectoryHoldsNothingButTheBenchmarksFiles() throws Exception {
        java.io.File dir = java.nio.file.Files.createTempDirectory("provbench-guard").toFile();
        assertEquals(dir, Guard.jsonlDirectory(dir));                                   // empty
        assertEquals(new java.io.File(dir, "new"), Guard.jsonlDirectory(new java.io.File(dir, "new")));   // absent: created by the run
        new java.io.File(dir, "provbench-fs-w0.jsonl").createNewFile();
        new java.io.File(dir, "provbench-tr-shared.jsonl").createNewFile();
        Guard.jsonlDirectory(dir);                                                       // its own files only
        java.io.File foreign = new java.io.File(dir, "notes.txt");
        foreign.createNewFile();
        refused(() -> Guard.jsonlDirectory(dir), "a file the benchmark did not write");
        foreign.delete();
        new java.io.File(dir, "bookptm.jsonl").createNewFile();
        refused(() -> Guard.jsonlDirectory(dir), "a .jsonl file not named as the benchmark's");
        refused(() -> Guard.jsonlDirectory(new java.io.File(dir, "provbench-fs-w0.jsonl")), "a file, not a directory");
        refused(() -> Guard.jsonlDirectory(null), "no directory");
    }
}
