package org.openprovenance.bench.throughput;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Serves the generated service's webapp on an embedded Jetty, as {@code mvn -Pjetty,nosecurity jetty:run-war} does,
 * without Maven's start-up: annotation scanning on (RESTEasy's servlet initializer), the no-security web.xml of the
 * build, a thread pool of {@code --max-threads}. Beside the webapp, {@code <context>}{@value #METER_PATH} reports this JVM's CPU time
 * and GC, read by the harness before and after a measured window.
 */
public final class ServiceLauncher {

    /** Under the service's context: {@code http://localhost:7099/provbench/__bench/jvm.json}. */
    static final String METER_PATH = "/__bench/jvm.json";

    public static void main(String[] args) throws Exception {
        Map<String, String> a = new HashMap<>();
        for (String arg : args) {
            int eq = arg.indexOf('=');
            a.put(arg.substring(2, eq), arg.substring(eq + 1));
        }
        String webapp = a.get("webapp");
        String host = a.getOrDefault("host", "127.0.0.1");
        int port = Integer.parseInt(a.get("port"));
        String context = a.get("context");
        int maxThreads = Integer.parseInt(a.getOrDefault("max-threads", "200"));
        Guard.serviceUrl("http://" + host + ":" + port + context);

        long t0 = System.nanoTime();
        QueuedThreadPool threads = new QueuedThreadPool(maxThreads, Math.min(8, maxThreads));
        threads.setName("jetty");
        Server server = new Server(threads);
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(host);
        connector.setPort(port);
        connector.setAcceptQueueSize(1024);
        server.addConnector(connector);

        WebAppContext app = new WebAppContext();
        app.setContextPath(context);
        app.setWar(webapp);
        app.addConfiguration(new AnnotationConfiguration());
        app.setThrowUnavailableOnStartupException(true);
        app.setParentLoaderPriority(false);

        ContextHandler meter = new ContextHandler(context + METER_PATH);
        meter.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request base, HttpServletRequest request, HttpServletResponse response) throws IOException {
                base.setHandled(true);
                response.setContentType("application/json");
                response.getOutputStream().write(meter().getBytes(StandardCharsets.UTF_8));
            }
        });
        server.setHandler(new ContextHandlerCollection(meter, app));
        server.setStopAtShutdown(true);   // SIGTERM: the webapp stops, its shutdown listeners run (the relation writer drains)
        server.setStopTimeout(120_000);
        server.start();
        if (app.getUnavailableException() != null) {
            System.err.println("FATAL the webapp failed to start: " + app.getUnavailableException());
            server.stop();
            System.exit(1);
        }
        System.out.println("provbench: service started in " + (System.nanoTime() - t0) / 1_000_000 + " ms on http://" + host + ":" + port + context
                + " (Jetty max threads " + maxThreads + ", Java " + System.getProperty("java.version") + ")");
        server.join();
    }

    static String meter() {
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long gcCount = 0;
        long gcMillis = 0;
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcCount += Math.max(0, gc.getCollectionCount());
            gcMillis += Math.max(0, gc.getCollectionTime());
        }
        return "{\"process_cpu_ns\":" + os.getProcessCpuTime()
                + ",\"uptime_ms\":" + ManagementFactory.getRuntimeMXBean().getUptime()
                + ",\"gc_count\":" + gcCount + ",\"gc_ms\":" + gcMillis
                + ",\"heap_used\":" + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()
                + ",\"threads\":" + ManagementFactory.getThreadMXBean().getThreadCount()
                + ",\"cpus\":" + Runtime.getRuntime().availableProcessors() + "}";
    }
}
