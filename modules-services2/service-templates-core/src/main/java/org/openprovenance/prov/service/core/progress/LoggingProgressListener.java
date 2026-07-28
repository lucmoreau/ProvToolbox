package org.openprovenance.prov.service.core.progress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@link ProgressListener} that emits one log4j entry per event.
 *
 * <p>Intended for phase 1 of the streaming-progress rollout: gives per-stage
 * timings in the server log without changing the wire protocol or any client.
 */
public final class LoggingProgressListener implements ProgressListener {

    private static final Logger logger = LogManager.getLogger(LoggingProgressListener.class);

    @Override
    public void started(String stage) {
        logger.info("progress: {} started", stage);
    }

    @Override
    public void done(String stage, long durationMs) {
        logger.info("progress: {} done in {} ms", stage, durationMs);
    }

    @Override
    public void failed(String stage, Throwable error) {
        logger.warn("progress: {} failed: {}", stage, error.toString());
    }

    @Override
    public void detail(String stage, String message) {
        logger.info("progress: {} — {}", stage, message);
    }

    @Override
    public void progress(String stage, double fraction) {
        logger.debug("progress: {} {}%", stage, Math.round(fraction * 100));
    }
}
