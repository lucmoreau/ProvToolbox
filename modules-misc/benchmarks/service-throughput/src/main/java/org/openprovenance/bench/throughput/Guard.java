package org.openprovenance.bench.throughput;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The benchmark writes only to its own store and its own service: an allow-list, checked before anything that writes
 * (the harness, and every make target that resets, starts or loads).
 *
 * <ol>
 *   <li>the JDBC URL's host is localhost or 127.0.0.1, its port {@value #DB_PORT}, its database {@value #DB_NAME};</li>
 *   <li>the service URL's host is localhost or 127.0.0.1, its port {@value #SERVICE_PORT}, its context
 *       {@value #SERVICE_CONTEXT};</li>
 *   <li>the PostgreSQL answering on port {@value #DB_PORT} is the running container labelled {@value #LABEL} (one
 *       container only, publishing that port; the system identifier the server reports over JDBC is the one the
 *       container's own cluster reports through {@code docker exec});</li>
 *   <li>its cluster holds no database but {@value #DB_NAME}, postgres, template0 and template1.</li>
 * </ol>
 * Anything else, missing or unparsable is refused.
 */
public final class Guard {

    public static final String DB_NAME = "provbench";
    public static final int DB_PORT = 55432;
    public static final int SERVICE_PORT = 7099;
    public static final String SERVICE_CONTEXT = "/provbench";
    public static final String LABEL = "provbench=1";
    public static final Set<String> LOCAL_HOSTS = Set.of("localhost", "127.0.0.1");
    public static final Set<String> ALLOWED_DATABASES = Set.of(DB_NAME, "postgres", "template0", "template1");

    /** Something the benchmark must not touch. */
    public static final class Refused extends RuntimeException {
        public Refused(String message) {
            super("GUARD: refused: " + message);
        }
    }

    private Guard() {
    }

    /** Checks a JDBC URL against the allow-list; returns it unchanged. */
    public static String jdbcUrl(String url) {
        String prefix = "jdbc:postgresql://";
        if (url == null || !url.startsWith(prefix)) {
            throw new Refused("the JDBC URL is not jdbc:postgresql://localhost:" + DB_PORT + "/" + DB_NAME + ": " + url);
        }
        String rest = url.substring(prefix.length());
        int slash = rest.indexOf('/');
        if (slash < 0) throw new Refused("no database in the JDBC URL " + url);
        String hostPort = rest.substring(0, slash);
        String database = rest.substring(slash + 1);
        int q = database.indexOf('?');
        String params = q < 0 ? "" : database.substring(q + 1);
        if (q >= 0) database = database.substring(0, q);
        if (hostPort.contains(",")) throw new Refused("several hosts in the JDBC URL " + url);
        int colon = hostPort.lastIndexOf(':');
        if (colon < 0) throw new Refused("no port in the JDBC URL " + url + " (the default, 5432, is never the benchmark's)");
        String host = hostPort.substring(0, colon);
        String port = hostPort.substring(colon + 1);
        if (!LOCAL_HOSTS.contains(host)) throw new Refused("the JDBC host is " + host + ", not localhost: " + url);
        if (!port.equals(Integer.toString(DB_PORT))) throw new Refused("the JDBC port is " + port + ", not " + DB_PORT + ": " + url);
        if (!database.equals(DB_NAME)) throw new Refused("the database is " + database + ", not " + DB_NAME + ": " + url);
        for (String p : params.split("&")) {
            String key = p.contains("=") ? p.substring(0, p.indexOf('=')).toLowerCase() : p.toLowerCase();
            // parameters that could send the connection elsewhere
            if (key.equals("host") || key.equals("port") || key.equals("dbname") || key.equals("pgdbname") || key.equals("pghost")
                    || key.equals("pgport") || key.equals("service") || key.equals("socketfactory")) {
                throw new Refused("the JDBC URL parameter " + key + " is not allowed: " + url);
            }
        }
        return url;
    }

    /** Checks the service's base URL (scheme, host, port and context, nothing after it) against the allow-list. */
    public static URI serviceUrl(String url) {
        URI uri;
        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            throw new Refused("the service URL is not a URL: " + url);
        }
        if (!"http".equals(uri.getScheme())) throw new Refused("the service URL is not http: " + url);
        if (uri.getHost() == null || !LOCAL_HOSTS.contains(uri.getHost())) throw new Refused("the service host is " + uri.getHost() + ", not localhost: " + url);
        if (uri.getPort() != SERVICE_PORT) throw new Refused("the service port is " + uri.getPort() + ", not " + SERVICE_PORT + ": " + url);
        String path = uri.getPath() == null ? "" : uri.getPath();
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
        if (!path.equals(SERVICE_CONTEXT)) throw new Refused("the service context is '" + path + "', not " + SERVICE_CONTEXT + ": " + url);
        if (uri.getQuery() != null || uri.getFragment() != null || uri.getUserInfo() != null) throw new Refused("the service URL carries more than a context: " + url);
        return URI.create("http://" + uri.getHost() + ":" + SERVICE_PORT + SERVICE_CONTEXT);
    }

    /**
     * The benchmark's container among the lines of {@code docker ps --filter label=provbench=1 --format '{{.ID}}\t{{.Ports}}'}
     * (running containers only): exactly one, and it publishes {@value #DB_PORT} on the loopback interface, or on all
     * interfaces.
     *
     * @return the container's id
     */
    public static String container(List<String> dockerPsLines) {
        List<String> found = new ArrayList<>();
        for (String line : dockerPsLines) {
            if (!line.isBlank()) found.add(line.trim());
        }
        if (found.isEmpty()) throw new Refused("no running container labelled " + LABEL + " (make bench.db.up)");
        if (found.size() > 1) throw new Refused("more than one running container labelled " + LABEL + ": " + found);
        String line = found.get(0);
        String[] parts = line.split("\t", 2);
        String ports = parts.length > 1 ? parts[1] : "";
        boolean published = false;
        for (String mapping : ports.split(",")) {
            String m = mapping.trim();
            if (m.endsWith("->5432/tcp") && (m.startsWith("127.0.0.1:" + DB_PORT + "->") || m.startsWith("0.0.0.0:" + DB_PORT + "->"))) {
                published = true;
            }
        }
        if (!published) throw new Refused("the container labelled " + LABEL + " does not publish port " + DB_PORT + ": " + line);
        return parts[0];
    }

    /** The server answering over JDBC is the container's cluster: their system identifiers are the same. */
    public static void sameCluster(String overJdbc, String inContainer) {
        if (overJdbc == null || inContainer == null || overJdbc.isBlank() || !overJdbc.trim().equals(inContainer.trim())) {
            throw new Refused("the PostgreSQL answering on port " + DB_PORT + " (system identifier " + overJdbc
                    + ") is not the labelled container's (" + inContainer + ")");
        }
    }

    /** The files the JSONL mode writes, and the only ones it ever deletes: provbench-&lt;family&gt;-&lt;writer&gt;.jsonl. */
    public static final java.util.regex.Pattern JSONL_FILE = java.util.regex.Pattern.compile("provbench-[a-z]+-[a-z0-9]+\\.jsonl");

    /**
     * The JSONL mode's directory: absent (it is created), or holding nothing but the mode's own files
     * ({@link #JSONL_FILE}), so that clearing it between runs can only delete what the benchmark wrote.
     */
    public static java.io.File jsonlDirectory(java.io.File dir) {
        if (dir == null) throw new Refused("no JSONL directory");
        if (dir.exists() && !dir.isDirectory()) throw new Refused("the JSONL directory " + dir + " is a file");
        String[] names = dir.list();
        if (names != null) {
            for (String n : names) {
                if (!JSONL_FILE.matcher(n).matches()) {
                    throw new Refused("the JSONL directory " + dir + " holds " + n + ", which the benchmark did not write: name an empty or new directory");
                }
            }
        }
        return dir;
    }

    /** The cluster holds no database but the benchmark's and PostgreSQL's own. */
    public static void databases(Collection<String> names) {
        Set<String> foreign = new TreeSet<>(names);
        foreign.removeAll(ALLOWED_DATABASES);
        if (!foreign.isEmpty()) throw new Refused("the cluster on port " + DB_PORT + " holds databases other than " + DB_NAME + ": " + foreign);
        if (!names.contains(DB_NAME)) throw new Refused("the cluster on port " + DB_PORT + " has no database " + DB_NAME);
    }
}
