package org.openprovenance.prov.service.core.progress;

/**
 * Receives progress notifications from long-running server operations.
 *
 * <p>Implementations route the events to a sink — log, metrics, SSE stream,
 * etc. — without knowing what work produced them.  Callers bracket each
 * observable phase with {@link #started(String)} and {@link #done(String, long)};
 * the listener stays stateless and stages stay decoupled from transports.
 *
 * <p>Stage names are conventionally hierarchical, dot-separated strings
 * (e.g. {@code "viz.sql"}, {@code "viz.prov-build"}, {@code "viz.render"}).
 * Use the constants in {@link VizStages} (or feature-specific equivalents)
 * to keep names typo-free.
 *
 * <p>Every method except {@link #started} and {@link #done} is defaulted to
 * a no-op so implementations only override what they care about.
 */
public interface ProgressListener {

    /** Called once at the start of a stage. */
    void started(String stage);

    /** Called once at the normal end of a stage, with the wall-clock elapsed time. */
    void done(String stage, long durationMs);

    /**
     * Called when a stage terminated abnormally.  Transports that surface
     * progress to a client (SSE, websocket) should close the stream after
     * dispatching this event.
     */
    default void failed(String stage, Throwable error) { }

    /**
     * Optional sub-event with a free-form message — e.g. a row count, a
     * generated artefact size, or any other piece of diagnostic context
     * worth exposing alongside the stage timing.
     */
    default void detail(String stage, String message) { }

    /** Optional fractional progress within a stage (0.0 – 1.0). */
    default void progress(String stage, double fraction) { }
}
