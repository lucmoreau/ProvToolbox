package org.openprovenance.bench.throughput;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * The benchmark's PostgreSQL: a Docker container ({@value Settings#CONTAINER}, label {@value Guard#LABEL}, named volume
 * {@value Settings#VOLUME}, published on 127.0.0.1:{@value Guard#DB_PORT}), and what the harness reads from it. Every
 * write goes through {@link #guard} first.
 */
class Store {

    private final Settings settings;

    Store(Settings settings) {
        this.settings = settings;
    }

    // ------------------------------------------------------------------ docker

    static List<String> docker(String... args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("docker");
        cmd.addAll(List.of(args));
        return cmd;
    }

    /** The labelled, running containers: {@code id<TAB>ports} lines. */
    static List<String> labelledContainers() {
        Shell.Result r = Shell.run(30, docker("ps", "--filter", "label=" + Guard.LABEL, "--format", "{{.ID}}\t{{.Ports}}"));
        if (!r.ok()) throw new Guard.Refused("cannot ask docker for the labelled container: " + r.out.trim());
        return List.of(r.out.split("\n"));
    }

    /** psql inside the labelled container, as the benchmark's user. */
    static String psql(String container, String database, String sql) {
        return Shell.check(300, docker("exec", container, "psql", "-U", Settings.DB_USER, "-d", database, "-v", "ON_ERROR_STOP=1", "-Atc", sql));
    }

    /**
     * The guard (see {@link Guard}): the URLs, then the container, the cluster answering on the port and its databases.
     *
     * @return the container's id
     */
    String guard() {
        Guard.jdbcUrl(Settings.JDBC_URL);
        Guard.serviceUrl(Settings.SERVICE_URL);
        String container = Guard.container(labelledContainers());
        String inside = psql(container, "postgres", "SELECT system_identifier FROM pg_control_system()");
        try (Connection c = connect("postgres")) {
            Guard.sameCluster(scalar(c, "SELECT system_identifier::text FROM pg_control_system()"), inside);
            List<String> names = new ArrayList<>();
            try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT datname FROM pg_database")) {
                while (rs.next()) names.add(rs.getString(1));
            }
            Guard.databases(names);
        } catch (SQLException e) {
            throw new Guard.Refused("cannot reach the PostgreSQL on port " + Guard.DB_PORT + ": " + e.getMessage());
        }
        return container;
    }

    /** A connection to the benchmark's cluster: provbench, or postgres (to list its databases). */
    static Connection connect(String database) throws SQLException {
        return connect(database, "provbench-harness");
    }

    /** As {@link #connect(String)}, its backend named {@code applicationName} in pg_stat_activity. */
    static Connection connect(String database, String applicationName) throws SQLException {
        String url = database.equals(Guard.DB_NAME) ? Settings.JDBC_URL : "jdbc:postgresql://localhost:" + Guard.DB_PORT + "/postgres";
        if (database.equals(Guard.DB_NAME)) Guard.jdbcUrl(url);
        else if (!database.equals("postgres")) throw new Guard.Refused("no connection to " + database);
        Properties p = new Properties();
        p.setProperty("user", Settings.DB_USER);
        p.setProperty("password", Settings.DB_PASS);
        p.setProperty("ApplicationName", applicationName);
        return DriverManager.getConnection(url, p);
    }

    static String scalar(Connection c, String sql) throws SQLException {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getString(1) : null;
        }
    }

    /** Starts the container (creating it, its volume and the database the first time); records pg_test_fsync once. */
    void up() {
        Shell.Result exists = Shell.run(30, docker("ps", "-a", "--filter", "name=^" + Settings.CONTAINER + "$", "--format", "{{.ID}} {{.Label \"provbench\"}}"));
        if (!exists.ok()) throw new IllegalStateException("docker is not available: " + exists.out);
        String line = exists.out.trim();
        if (!line.isEmpty()) {
            if (!line.endsWith(" 1")) throw new Guard.Refused("a container named " + Settings.CONTAINER + " exists without the label " + Guard.LABEL);
            Shell.check(120, docker("start", Settings.CONTAINER));
        } else {
            if (!labelledContainers().stream().allMatch(String::isBlank)) {
                throw new Guard.Refused("another container labelled " + Guard.LABEL + " is running");
            }
            if (portInUse(Guard.DB_PORT)) throw new Guard.Refused("port " + Guard.DB_PORT + " is in use by something else");
            Shell.check(600, docker("run", "-d", "--name", Settings.CONTAINER, "--label", Guard.LABEL,
                    "-p", "127.0.0.1:" + Guard.DB_PORT + ":5432",
                    "-v", Settings.VOLUME + ":/var/lib/postgresql/data",
                    "--shm-size", "1g",
                    "-e", "POSTGRES_USER=" + Settings.DB_USER, "-e", "POSTGRES_PASSWORD=" + Settings.DB_PASS, "-e", "POSTGRES_DB=" + Guard.DB_NAME,
                    settings.get("IMAGE"),
                    "-c", "shared_preload_libraries=pg_stat_statements",
                    "-c", "pg_stat_statements.max=10000",
                    "-c", "track_io_timing=on",
                    "-c", "max_connections=" + settings.get("PG_MAX_CONNECTIONS"),
                    "-c", "shared_buffers=" + settings.get("PG_SHARED_BUFFERS")));
        }
        waitReady();
        String container = guard();
        psql(container, Guard.DB_NAME, "CREATE EXTENSION IF NOT EXISTS pg_stat_statements");
        File fsync = fsyncFile(settings);
        if (!fsync.exists()) {
            System.out.println("pg_test_fsync on the container's volume (once, ~30 s) ...");
            String out = Shell.check(600, docker("exec", container, "pg_test_fsync", "-s", "2", "-f", "/var/lib/postgresql/data/provbench_fsync.tmp"));
            Shell.run(30, docker("exec", container, "rm", "-f", "/var/lib/postgresql/data/provbench_fsync.tmp"));
            try {
                Files.writeString(fsync.toPath(), "# pg_test_fsync -s 2 on the named volume " + Settings.VOLUME
                        + " (inside the Docker VM: it measures the VM's disk)\n" + out + "\n", StandardCharsets.UTF_8);
            } catch (java.io.IOException e) {
                throw new IllegalStateException(e);
            }
        }
        System.out.println("store up: " + Settings.JDBC_URL + " (container " + container + "); fdatasync: " + fdatasyncFigure(settings));
    }

    private void waitReady() {
        long deadline = System.currentTimeMillis() + 120_000;
        while (System.currentTimeMillis() < deadline) {
            Shell.Result r = Shell.run(30, docker("exec", Settings.CONTAINER, "pg_isready", "-U", Settings.DB_USER, "-d", Guard.DB_NAME));
            if (r.ok()) {
                try (Connection c = connect(Guard.DB_NAME)) {
                    return;
                } catch (SQLException e) {
                    // not yet
                }
            }
            sleep(500);
        }
        throw new IllegalStateException("the benchmark's PostgreSQL did not become ready");
    }

    void down() {
        guard();
        Shell.check(120, docker("stop", Settings.CONTAINER));
        System.out.println("store stopped (volume " + Settings.VOLUME + " kept)");
    }

    /** Removes the container and its volume (make bench.db.destroy). */
    void destroy() {
        guard();
        Shell.check(120, docker("rm", "-f", Settings.CONTAINER));
        Shell.check(120, docker("volume", "rm", Settings.VOLUME));
        System.out.println("store removed, with its volume");
    }

    /** Drops and recreates provbench (the service must be down: its schema is made at start-up). */
    void reset() {
        String container = guard();
        psql(container, "postgres", "DROP DATABASE IF EXISTS " + Guard.DB_NAME + " WITH (FORCE)");
        psql(container, "postgres", "CREATE DATABASE " + Guard.DB_NAME);
        psql(container, Guard.DB_NAME, "CREATE EXTENSION IF NOT EXISTS pg_stat_statements");
        psql(container, "postgres", "CHECKPOINT");
        guard();
    }

    /** DDL of a variant, in provbench, after the guard. */
    void apply(List<String> ddl) throws SQLException {
        if (ddl.isEmpty()) return;
        guard();
        try (Connection c = connect(Guard.DB_NAME); Statement st = c.createStatement()) {
            for (String sql : ddl) {
                System.out.println("  DDL: " + sql);
                st.execute(sql);
            }
        }
    }

    // ------------------------------------------------------------------ measurements

    /** What the harness reads before and after the measured window. */
    static final class Snapshot {
        long rowsWritten;       // sum of n_tup_ins + n_tup_upd, all user tables
        long walBytes;          // pg_current_wal_lsn() - 0/0
        long recordIndex;       // rows of record_index
        Map<String, Long> activity = new LinkedHashMap<>();   // activity_log, per kind
        long cpuMicros;         // the container's cgroup CPU usage
    }

    Snapshot snapshot(Connection c, String container) throws SQLException {
        Snapshot s = new Snapshot();
        s.rowsWritten = Long.parseLong(scalar(c, "SELECT COALESCE(SUM(n_tup_ins + n_tup_upd), 0)::bigint FROM pg_stat_user_tables"));
        s.walBytes = Long.parseLong(scalar(c, "SELECT (pg_current_wal_lsn() - '0/0'::pg_lsn)::bigint"));
        s.recordIndex = Long.parseLong(scalar(c, "SELECT count(*) FROM record_index"));
        if (scalar(c, "SELECT to_regclass('activity_log')::text") != null) {   // none without the service (direct mode)
            try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT kind, SUM(count)::bigint FROM activity_log GROUP BY kind")) {
                while (rs.next()) s.activity.put(rs.getString(1), rs.getLong(2));
            }
        }
        s.cpuMicros = containerCpuMicros(container);
        return s;
    }

    /** The container's CPU time, from its cgroup: cpu.stat's usage_usec (cgroup v2, Docker Desktop), else cpuacct.usage (v1, ns). */
    static long containerCpuMicros(String container) {
        Shell.Result v2 = Shell.run(30, docker("exec", container, "cat", "/sys/fs/cgroup/cpu.stat"));
        if (v2.ok()) {
            for (String line : v2.out.split("\n")) {
                if (line.startsWith("usage_usec ")) return Long.parseLong(line.substring("usage_usec ".length()).trim());
            }
        }
        Shell.Result v1 = Shell.run(30, docker("exec", container, "cat", "/sys/fs/cgroup/cpuacct/cpuacct.usage"));
        if (v1.ok()) return Long.parseLong(v1.out.trim()) / 1000;
        return -1;
    }

    static void resetStatements(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("SELECT pg_stat_statements_reset()");
        }
    }

    /** The top statements by total execution time since the last reset. */
    static List<String[]> topStatements(Connection c, int limit) throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT calls, round(total_exec_time::numeric, 1), round(mean_exec_time::numeric, 3), rows, "
                + "round((100 * total_exec_time / NULLIF(SUM(total_exec_time) OVER (), 0))::numeric, 1), "
                + "regexp_replace(left(query, 160), '\\s+', ' ', 'g') "
                + "FROM pg_stat_statements WHERE dbid = (SELECT oid FROM pg_database WHERE datname = current_database()) "
                + "ORDER BY total_exec_time DESC LIMIT " + limit;
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
            }
        }
        return rows;
    }

    /** Whether any of the record's relation rows is visible (the lag probe of relations.write=async). */
    static PreparedStatement relationProbe(Connection c) throws SQLException {
        String[] tables = {"__prov_generation", "__prov_usage", "__prov_derivation", "__prov_association", "__prov_delegation",
                "__prov_specialization", "__prov_membership"};
        StringBuilder sb = new StringBuilder("SELECT 1 FROM (");
        for (int i = 0; i < tables.length; i++) {
            if (i > 0) sb.append(" UNION ALL ");
            sb.append("(SELECT 1 FROM ").append(tables[i]).append(" WHERE template_id = ? AND template = ? LIMIT 1)");
        }
        sb.append(") AS found LIMIT 1");
        return c.prepareStatement(sb.toString());
    }

    static String version(Connection c) throws SQLException {
        return scalar(c, "SELECT version()");
    }

    /** The server's settings that differ from the defaults (the report lists them). */
    static String nonDefaultSettings(Connection c) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(
                "SELECT name, setting, unit FROM pg_settings WHERE source NOT IN ('default', 'override') "
                        + "AND name NOT IN ('application_name', 'client_encoding', 'DateStyle', 'TimeZone', 'lc_messages', 'lc_monetary', "
                        + "'lc_numeric', 'lc_time', 'default_text_search_config', 'log_timezone', 'extra_float_digits') ORDER BY name")) {
            while (rs.next()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(rs.getString(1)).append('=').append(rs.getString(2)).append(rs.getString(3) == null ? "" : rs.getString(3));
            }
        }
        return sb.toString();
    }

    /** pg_test_fsync's output for this machine: results/pg_test_fsync-&lt;host&gt;.txt (one per machine, never another's). */
    static File fsyncFile(Settings settings) {
        String host;
        try {
            host = java.net.InetAddress.getLocalHost().getHostName();
        } catch (java.io.IOException e) {
            host = "local";
        }
        int dot = host.indexOf('.');
        if (dot > 0) host = host.substring(0, dot);
        return new File(settings.resultsDir(), "pg_test_fsync-" + host.replaceAll("[^A-Za-z0-9_-]", "_") + ".txt");
    }

    /** The fdatasync figure of the recorded pg_test_fsync (one 8kB write), or "not recorded". */
    static String fdatasyncFigure(Settings settings) {
        File f = fsyncFile(settings);
        if (!f.exists()) return "not recorded";
        try {
            for (String line : Files.readAllLines(f.toPath())) {
                String t = line.trim();
                if (t.startsWith("fdatasync") && t.contains("ops/sec")) return t.replaceAll("\\s+", " ");
            }
        } catch (java.io.IOException e) {
            return "unreadable";
        }
        return "not found";
    }

    static boolean portInUse(int port) {
        try (java.net.Socket s = new java.net.Socket()) {
            s.connect(new java.net.InetSocketAddress("127.0.0.1", port), 300);
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted");
        }
    }
}
