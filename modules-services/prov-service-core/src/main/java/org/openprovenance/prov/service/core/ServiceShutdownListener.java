package org.openprovenance.prov.service.core;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Closes registered service resources (storage indexes, database connection
 * pools, schedulers) when the web application stops. Without this, such
 * resources keep their background threads (commons-pool eviction timer, MongoDB
 * server monitors, Quartz scheduler thread) running after the webapp classloader
 * is closed, leading to NoClassDefFoundError in those threads and to
 * classloader/connection leaks on redeploy.
 *
 * The registry is static: it is scoped per webapp by the webapp classloader.
 */
@WebListener
public class ServiceShutdownListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(ServiceShutdownListener.class);

    private static final List<AutoCloseable> closeables = new LinkedList<>();

    /** Registers a resource to be closed when the web application stops. */
    public static synchronized void registerForShutdown(AutoCloseable closeable) {
        closeables.add(closeable);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // nothing to do
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        synchronized (ServiceShutdownListener.class) {
            for (AutoCloseable closeable : closeables) {
                try {
                    logger.info("closing service resource " + closeable);
                    closeable.close();
                } catch (Exception e) {
                    logger.warn("failed to close service resource " + closeable, e);
                }
            }
            closeables.clear();
        }
    }
}
